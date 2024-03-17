package com.tac.guns.client.resource.index;

import com.google.common.collect.Maps;
import com.tac.guns.api.TimelessAPI;
import com.tac.guns.client.model.BedrockAmmoModel;
import com.tac.guns.client.resource.ClientAssetManager;
import com.tac.guns.client.resource.pojo.display.ammo.AmmoDisplay;
import com.tac.guns.client.resource.pojo.model.BedrockModelPOJO;
import com.tac.guns.client.resource.pojo.model.BedrockVersion;
import com.tac.guns.resource.pojo.AmmoIndexPOJO;
import com.tac.guns.resource.pojo.data.ammo.BulletVariation;
import com.tac.guns.resource.pojo.data.ammo.BulletVariationWrapper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.jetbrains.annotations.NotNull;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class ClientAmmoIndex {
    private String name;
    private int stackSize;

    // Map of <BulletType, ItemId.name>
    private Map<String, BulletVariation> variationNames;
    private Map<BulletVariation, String> suffixPerVariation;
    private Map<Pair<BulletVariation, String>, BulletVariationWrapper> bulletVariations;
    private Map<BulletVariation, Pair<BedrockAmmoModel, ResourceLocation>> modelTexturePairs;
    private ClientAmmoIndex() {
    }

    public static ClientAmmoIndex getInstance(AmmoIndexPOJO clientPojo) throws IllegalArgumentException {
        ClientAmmoIndex index = new ClientAmmoIndex();
        index.bulletVariations = Maps.newHashMap();
        index.variationNames = Maps.newHashMap();
        index.modelTexturePairs = Maps.newHashMap();
        index.suffixPerVariation = Maps.newHashMap();
        checkIndex(clientPojo, index);
        checkName(clientPojo, index);
        cacheAllBulletTypes(clientPojo, index);
        @NotNull List<Pair<BulletVariation, AmmoDisplay>> displays = checkAllVariationDisplay(clientPojo);
        for(var displayPair : displays) {
            checkTextureAndModel(displayPair.getRight(), index, displayPair.getLeft());
            checkSlotTexture(displayPair.getRight(), index, displayPair.getLeft());
        }
        checkStackSize(clientPojo, index);
        return index;
    }

    private static void cacheAllBulletTypes(AmmoIndexPOJO ammoIndexPOJO, ClientAmmoIndex index) {
        if(ammoIndexPOJO.getBulletVariations() == null || ammoIndexPOJO.getBulletVariations().isEmpty())
            throw new IllegalArgumentException(index.name + " No bullet variations defined! At least one bullet variation must exist");

        for (var x : ammoIndexPOJO.getBulletVariations()) {
            var name = index.name;
            // Must be built before we can build complete name
            index.suffixPerVariation.put(x.getBulletVariation(), x.getSuffix());
            name = index.buildNameFromVariation(x.getBulletVariation());
            index.bulletVariations.put(Pair.of(x.getBulletVariation(), name), x);
            index.variationNames.put(name, x.getBulletVariation());
        }

        index.name = ammoIndexPOJO.getName();
        if (StringUtils.isBlank(index.name)) {
            index.name = "custom.tac.error.no_name";
        }
    }

    private static void checkIndex(AmmoIndexPOJO ammoIndexPOJO, ClientAmmoIndex index) {
        if (ammoIndexPOJO == null) {
            throw new IllegalArgumentException("index object file is empty");
        }
    }

    private static void checkName(AmmoIndexPOJO ammoIndexPOJO, ClientAmmoIndex index) {
        index.name = ammoIndexPOJO.getName();
        if (StringUtils.isBlank(index.name)) {
            index.name = "custom.tac.error.no_name";
        }
    }

    @NotNull
    private static AmmoDisplay checkDisplay(AmmoIndexPOJO ammoIndexPOJO) {
        if(ammoIndexPOJO.getBulletVariations() == null || ammoIndexPOJO.getBulletVariations().isEmpty())
            throw new IllegalArgumentException(ammoIndexPOJO.getName() + " requires at least a single element within BulletVariations");
        ResourceLocation pojoDisplay = ammoIndexPOJO.getBulletVariations().get(0).getDisplay();
        if (pojoDisplay == null) {
            throw new IllegalArgumentException("index object missing display field");
        }
        AmmoDisplay display = ClientAssetManager.INSTANCE.getAmmoDisplay(pojoDisplay);
        if (display == null) {
            throw new IllegalArgumentException("there is no corresponding display file");
        }
        return display;
    }


    /**
     * @return Returns display associated with provided bullet type
     * @throws IllegalArgumentException if bulletType is not found in ammoIndex
     */
    @NotNull
    private static List<Pair<BulletVariation, AmmoDisplay>> checkAllVariationDisplay(AmmoIndexPOJO ammoIndexPOJO) {
        var displays = new ArrayList<Pair<BulletVariation, AmmoDisplay>>();
        for(BulletVariationWrapper wrapper : ammoIndexPOJO.getBulletVariations()) {
            ResourceLocation pojoDisplay = wrapper.getDisplay();
            if (pojoDisplay == null) {
                throw new IllegalArgumentException("index object missing display field");
            }
            AmmoDisplay display = ClientAssetManager.INSTANCE.getAmmoDisplay(pojoDisplay);
            if (display == null) {
                throw new IllegalArgumentException("there is no corresponding display file");
            }
            displays.add(Pair.of(wrapper.getBulletVariation(), display));
        }
        return displays;
    }
    private static void checkTextureAndModel(AmmoDisplay display, ClientAmmoIndex index, BulletVariation variation) {
        // 检查模型
        ResourceLocation modelLocation = display.getModelLocation();
        if (modelLocation == null) {
            throw new IllegalArgumentException("display object missing model field");
        }
        BedrockModelPOJO modelPOJO = ClientAssetManager.INSTANCE.getModels(modelLocation);
        if (modelPOJO == null) {
            throw new IllegalArgumentException("there is no corresponding model file");
        }
        // 检查材质
        ResourceLocation texture = display.getModelTexture();
        if (texture == null) {
            throw new IllegalArgumentException("display object missing textures field");
        }
        // 创建默认的 RenderType
        RenderType renderType = RenderType.itemEntityTranslucentCull(texture);
        // 先判断是不是 1.10.0 版本基岩版模型文件
        if (modelPOJO.getFormatVersion().equals(BedrockVersion.LEGACY.getVersion()) && modelPOJO.getGeometryModelLegacy() != null) {
            index.modelTexturePairs.put(variation, Pair.of(new BedrockAmmoModel(modelPOJO, BedrockVersion.LEGACY, renderType), null));
        }
        // 判定是不是 1.12.0 版本基岩版模型文件
        if (modelPOJO.getFormatVersion().equals(BedrockVersion.NEW.getVersion()) && modelPOJO.getGeometryModelNew() != null) {
            index.modelTexturePairs.put(variation, Pair.of(new BedrockAmmoModel(modelPOJO, BedrockVersion.NEW, renderType), null));
        }
        if (index.modelTexturePairs.get(variation) == null) {
            throw new IllegalArgumentException("there is no model data in the model file");
        }
    }

    private static void checkSlotTexture(AmmoDisplay display, ClientAmmoIndex index, BulletVariation variation) {
        // 加载 GUI 内枪械图标
        //index.modelTexturePairs.computeIfAbsent(variation, k -> new MutablePair<>());
        var pair = new MutablePair<BedrockAmmoModel, ResourceLocation>();//index.modelTexturePairs.get(variation);
        pair.setLeft(index.modelTexturePairs.get(variation).getLeft());
        pair.setValue(Objects.requireNonNullElseGet(display.getSlotTextureLocation(), MissingTextureAtlasSprite::getLocation));
        index.modelTexturePairs.put(variation, pair);
    }

    private static void checkStackSize(AmmoIndexPOJO clientPojo, ClientAmmoIndex index) {
        index.stackSize = Math.max(clientPojo.getStackSize(), 1);
    }

    public String getName() {
        return name;
    }

    public BulletVariation getVariationFromName(String name) {
        return variationNames.get(name);
    }

    public BedrockAmmoModel getAmmoModel(BulletVariation variation) {
        return modelTexturePairs.get(variation).getLeft();
    }

    public ResourceLocation getSlotTextureLocation(BulletVariation variation) {
        return modelTexturePairs.get(variation).getRight();
    }

    public String getSuffixPerVariation(BulletVariation variation) {
        return suffixPerVariation.get(variation);
    }

    public String buildNameFromVariation(BulletVariation variation) {
        return this.name + "_" + suffixPerVariation.get(variation);
    }

    public String buildNameFromId(ResourceLocation itemId) {
        return this.name + "_" + suffixPerVariation.get(TimelessAPI.getClientAmmoIndex(itemId).get().getRight());
    }
    public int getStackSize() {
        return stackSize;
    }
}

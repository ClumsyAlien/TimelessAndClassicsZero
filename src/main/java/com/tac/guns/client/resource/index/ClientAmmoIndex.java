package com.tac.guns.client.resource.index;

import com.google.common.collect.Maps;
import com.tac.guns.client.model.BedrockAmmoModel;
import com.tac.guns.client.resource.ClientAssetManager;
import com.tac.guns.client.resource.pojo.display.ammo.AmmoDisplay;
import com.tac.guns.client.resource.pojo.display.ammo.AmmoEntityDisplay;
import com.tac.guns.client.resource.pojo.model.BedrockModelPOJO;
import com.tac.guns.client.resource.pojo.model.BedrockVersion;
import com.tac.guns.resource.pojo.AmmoIndexPOJO;
import com.tac.guns.resource.pojo.data.ammo.BulletVariationWrapper;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.jetbrains.annotations.NotNull;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;


import java.util.*;

public class ClientAmmoIndex {
    private String name;

    private BedrockAmmoModel ammoModel;
    private ResourceLocation modelTextureLocation;
    private ResourceLocation slotTextureLocation;
    private @Nullable BedrockAmmoModel ammoEntityModel;
    private @Nullable ResourceLocation ammoEntityTextureLocation;

    private int stackSize;

    // Map of name + suffix and wrapper
    private Map<String, BulletVariationWrapper> bulletVariations;
    // Map of name and ammo model
    private Map<String, Pair<BedrockAmmoModel, ResourceLocation>> modelTexturePairs;
    private ClientAmmoIndex() {
    }

    public static ClientAmmoIndex getInstance(AmmoIndexPOJO clientPojo) throws IllegalArgumentException {
        ClientAmmoIndex index = new ClientAmmoIndex();
        index.bulletVariations = Maps.newHashMap();
        index.modelTexturePairs = Maps.newHashMap();
        checkIndex(clientPojo, index);
        checkName(clientPojo, index);
        cacheAllBulletTypes(clientPojo, index);
        @NotNull List<Pair<String, AmmoDisplay>> displays = checkAllDisplay(clientPojo);
        for(var displayPair : displays) {
            checkTextureAndModel(displayPair.getRight(), index, displayPair.getLeft());
            checkSlotTexture(displayPair.getRight(), index, displayPair.getLeft());
            checkAmmoEntity(displayPair.getRight(), index);
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
            name = index.buildWithSuffixOrDefault(x.getSuffix());
            index.bulletVariations.put(name, x);
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

    @NotNull
    private static List<Pair<String, AmmoDisplay>> checkAllDisplay(AmmoIndexPOJO ammoIndexPOJO) {
        var displays = new ArrayList<Pair<String, AmmoDisplay>>();
        for(BulletVariationWrapper wrapper : ammoIndexPOJO.getBulletVariations()) {
            ResourceLocation pojoDisplay = wrapper.getDisplay();
            if (pojoDisplay == null) {
                throw new IllegalArgumentException("index object missing display field");
            }
            AmmoDisplay display = ClientAssetManager.INSTANCE.getAmmoDisplay(pojoDisplay);
            if (display == null) {
                throw new IllegalArgumentException("there is no corresponding display file");
            }
            displays.add(Pair.of(ClientAmmoIndex.buildWithSuffixOrDefault(ammoIndexPOJO.getName(), wrapper.getSuffix()), display));
        }
        return displays;
    }
    private static void checkTextureAndModel(AmmoDisplay display, ClientAmmoIndex index, String name) {
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
        index.modelTextureLocation = texture;
        // 先判断是不是 1.10.0 版本基岩版模型文件
        if (modelPOJO.getFormatVersion().equals(BedrockVersion.LEGACY.getVersion()) && modelPOJO.getGeometryModelLegacy() != null) {
            index.modelTexturePairs.put(name, Pair.of(new BedrockAmmoModel(modelPOJO, BedrockVersion.LEGACY), null));
        }
        // 判定是不是 1.12.0 版本基岩版模型文件
        if (modelPOJO.getFormatVersion().equals(BedrockVersion.NEW.getVersion()) && modelPOJO.getGeometryModelNew() != null) {
            index.modelTexturePairs.put(name, Pair.of(new BedrockAmmoModel(modelPOJO, BedrockVersion.NEW), null));
        }
        if (index.modelTexturePairs.get(name) == null) {
            throw new IllegalArgumentException("there is no model data in the model file");
        }
    }

    private static void checkSlotTexture(AmmoDisplay display, ClientAmmoIndex index, String name) {
        // 加载 GUI 内枪械图标
        //index.modelTexturePairs.computeIfAbsent(variation, k -> new MutablePair<>());
        var pair = new MutablePair<BedrockAmmoModel, ResourceLocation>();//index.modelTexturePairs.get(variation);
        pair.setLeft(index.modelTexturePairs.get(name).getLeft());
        pair.setValue(Objects.requireNonNullElseGet(display.getSlotTextureLocation(), MissingTextureAtlasSprite::getLocation));
        index.modelTexturePairs.put(name, pair);
    }

    private static void checkAmmoEntity(AmmoDisplay display, ClientAmmoIndex index) {
        AmmoEntityDisplay ammoEntity = display.getAmmoEntity();
        if (ammoEntity != null && ammoEntity.getModelLocation() != null && ammoEntity.getModelTexture() != null) {
            index.ammoEntityTextureLocation = ammoEntity.getModelTexture();
            ResourceLocation modelLocation = ammoEntity.getModelLocation();
            BedrockModelPOJO modelPOJO = ClientAssetManager.INSTANCE.getModels(modelLocation);
            if (modelPOJO == null) {
                return;
            }
            // 先判断是不是 1.10.0 版本基岩版模型文件
            if (modelPOJO.getFormatVersion().equals(BedrockVersion.LEGACY.getVersion()) && modelPOJO.getGeometryModelLegacy() != null) {
                index.ammoEntityModel = new BedrockAmmoModel(modelPOJO, BedrockVersion.LEGACY);
            }
            // 判定是不是 1.12.0 版本基岩版模型文件
            if (modelPOJO.getFormatVersion().equals(BedrockVersion.NEW.getVersion()) && modelPOJO.getGeometryModelNew() != null) {
                index.ammoEntityModel = new BedrockAmmoModel(modelPOJO, BedrockVersion.NEW);
            }
        }
    }

    private static void checkStackSize(AmmoIndexPOJO clientPojo, ClientAmmoIndex index) {
        index.stackSize = Math.max(clientPojo.getStackSize(), 1);
    }

    public String getName() {
        return name;
    }
    public BedrockAmmoModel getAmmoModel(String name) {
        return modelTexturePairs.get(name).getLeft();
    }

    public ResourceLocation getSlotTextureLocation(String name) {
        return modelTexturePairs.get(name).getRight();
    }
    private String buildWithSuffixOrDefault(String suffix) {
        if(suffix == null || suffix.isEmpty())
            return this.name;
        else return this.name + "_" + suffix;
    }
    public static String buildWithSuffixOrDefault(String name, String... suffix) {
        StringBuilder result = new StringBuilder(name);
        for(String part : suffix) {
            if(part == null || part.isEmpty())
                return result.toString();
            else
                result.append("_").append(part);
        }
        return result.toString();
    }
    public int getStackSize() {
        return stackSize;
    }

    @Nullable
    public BedrockAmmoModel getAmmoEntityModel() {
        return ammoEntityModel;
    }

    @Nullable
    public ResourceLocation getAmmoEntityTextureLocation() {
        return ammoEntityTextureLocation;
    }

    public ResourceLocation getModelTextureLocation() {
        return modelTextureLocation;
    }
}

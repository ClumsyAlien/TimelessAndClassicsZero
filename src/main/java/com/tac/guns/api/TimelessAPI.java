package com.tac.guns.api;

import com.tac.guns.api.client.animation.IThirdPersonAnimation;
import com.tac.guns.client.animation.thrid.ThirdPersonManager;
import com.tac.guns.client.resource.ClientGunPackLoader;
import com.tac.guns.client.resource.index.ClientAmmoIndex;
import com.tac.guns.client.resource.index.ClientAttachmentIndex;
import com.tac.guns.client.resource.index.ClientGunIndex;
import com.tac.guns.crafting.GunSmithTableRecipe;
import com.tac.guns.resource.CommonAssetManager;
import com.tac.guns.resource.CommonGunPackLoader;
import com.tac.guns.resource.index.CommonAmmoIndex;
import com.tac.guns.resource.index.CommonAttachmentIndex;
import com.tac.guns.resource.index.CommonGunIndex;
import com.tac.guns.resource.pojo.data.ammo.BulletVariation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class TimelessAPI {
    @OnlyIn(Dist.CLIENT)
    public static Optional<ClientGunIndex> getClientGunIndex(ResourceLocation gunId) {
        return ClientGunPackLoader.getGunIndex(gunId);
    }

    @OnlyIn(Dist.CLIENT)
    public static Optional<ClientAttachmentIndex> getClientAttachmentIndex(ResourceLocation attachmentId) {
        return ClientGunPackLoader.getAttachmentIndex(attachmentId);
    }

    @OnlyIn(Dist.CLIENT)
    public static Optional<Pair<ClientAmmoIndex, BulletVariation>> getClientAmmoIndex(ResourceLocation ammoId) {
        return ClientGunPackLoader.getAmmoIndex(ammoId);
    }

    @OnlyIn(Dist.CLIENT)
    public static Set<Map.Entry<ResourceLocation, ClientGunIndex>> getAllClientGunIndex() {
        return ClientGunPackLoader.getAllGuns();
    }

    @OnlyIn(Dist.CLIENT)
    public static Set<Map.Entry<ResourceLocation, Pair<ClientAmmoIndex, BulletVariation>>> getAllClientAmmoIndex() {
        return ClientGunPackLoader.getAllAmmo();
    }

    @OnlyIn(Dist.CLIENT)
    public static Set<Map.Entry<ResourceLocation, ClientAttachmentIndex>> getAllClientAttachmentIndex() {
        return ClientGunPackLoader.getAllAttachments();
    }

    public static Optional<CommonGunIndex> getCommonGunIndex(ResourceLocation gunId) {
        return CommonGunPackLoader.getGunIndex(gunId);
    }

    public static Optional<CommonAttachmentIndex> getCommonAttachmentIndex(ResourceLocation attachmentId) {
        return CommonGunPackLoader.getAttachmentIndex(attachmentId);
    }

    public static Optional<CommonAmmoIndex> getCommonAmmoIndex(ResourceLocation ammoId) {
        return CommonGunPackLoader.getAmmoIndex(ammoId);
    }

    public static Optional<GunSmithTableRecipe> getRecipe(ResourceLocation recipeId) {
        return CommonAssetManager.INSTANCE.getRecipe(recipeId);
    }

    public static Set<Map.Entry<ResourceLocation, CommonGunIndex>> getAllCommonGunIndex() {
        return CommonGunPackLoader.getAllGuns();
    }

    public static Set<Map.Entry<ResourceLocation, CommonAmmoIndex>> getAllCommonAmmoIndex() {
        return CommonGunPackLoader.getAllAmmo();
    }

    public static Set<Map.Entry<ResourceLocation, CommonAttachmentIndex>> getAllCommonAttachmentIndex() {
        return CommonGunPackLoader.getAllAttachments();
    }

    public static Map<ResourceLocation, GunSmithTableRecipe> getAllRecipes() {
        return CommonAssetManager.INSTANCE.getAllRecipes();
    }

    public static void registerThirdPersonAnimation(String name, IThirdPersonAnimation animation) {
        ThirdPersonManager.register(name, animation);
    }
}

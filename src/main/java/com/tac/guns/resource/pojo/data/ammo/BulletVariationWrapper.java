package com.tac.guns.resource.pojo.data.ammo;

import com.google.gson.annotations.SerializedName;
import net.minecraft.resources.ResourceLocation;

public class BulletVariationWrapper {
    @SerializedName("suffix")
    private String suffix;
    @SerializedName("display")
    private ResourceLocation display;
    @SerializedName("bullet_variation")
    private BulletVariation bulletVariation;
    @SerializedName("damage_type")
    private BulletDamageType damageType;

    // Constructor used to create custom bullets using custom BulletVariation
    public BulletVariationWrapper() {
        this.suffix = "standard";
        this.display = null;
        this.bulletVariation = BulletVariation.STANDARD;
        this.damageType = BulletDamageType.STANDARD;
    }
    public BulletVariationWrapper(String suffix, ResourceLocation display, BulletVariation bulletVariation, BulletDamageType damageType) {
        this.suffix = suffix;
        this.display = display;
        this.bulletVariation = bulletVariation;
        this.damageType = damageType;
    }
    public String getSuffix() {
        return suffix;
    }
    public ResourceLocation getDisplay() {
        return display;
    }
    public BulletVariation getBulletVariation() {
        return bulletVariation;
    }
    public BulletDamageType getDamageType() {
        return damageType;
    }
}

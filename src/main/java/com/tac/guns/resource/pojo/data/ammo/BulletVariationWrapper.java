package com.tac.guns.resource.pojo.data.ammo;

import com.google.gson.annotations.SerializedName;
import net.minecraft.resources.ResourceLocation;

public class BulletVariationWrapper {
    @SerializedName("suffix")
    private String suffix;
    @SerializedName("display")
    private ResourceLocation display;
    @SerializedName("damage_type")
    private BulletDamageType damageType;

    // Constructor used to create custom bullets using custom BulletVariation
    public BulletVariationWrapper() {
        this.suffix = "";
        this.display = null;
        this.damageType = BulletDamageType.STANDARD;
    }
    public BulletVariationWrapper(String suffix, ResourceLocation display, BulletDamageType damageType) {
        this.suffix = suffix;
        this.display = display;
        this.damageType = damageType;
    }
    public String getSuffix() {
        return suffix;
    }
    public ResourceLocation getDisplay() {
        return display;
    }
    public BulletDamageType getDamageType() {
        return damageType;
    }
}

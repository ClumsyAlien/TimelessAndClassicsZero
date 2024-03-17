package com.tac.guns.resource.pojo;

import com.google.gson.annotations.SerializedName;
import com.tac.guns.api.gun.FireMode;
import com.tac.guns.resource.pojo.data.ammo.BulletVariation;
import com.tac.guns.resource.pojo.data.ammo.BulletVariationWrapper;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.List;

public class AmmoIndexPOJO {
    @SerializedName("name")
    private String name;
    @SerializedName("stack_size")
    private int stackSize;
    @SerializedName("bullet")
    private List<BulletVariationWrapper> bulletVariations;

    public List<BulletVariationWrapper> getBulletVariations() {return bulletVariations;}

    public String getName() {
        return name;
    }

    public int getStackSize() {
        return stackSize;
    }
}

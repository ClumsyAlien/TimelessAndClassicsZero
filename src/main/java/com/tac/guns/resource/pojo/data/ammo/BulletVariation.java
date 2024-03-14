package com.tac.guns.resource.pojo.data.ammo;

import com.google.gson.annotations.SerializedName;

public enum BulletVariation {
    @SerializedName("standard")
    STANDARD,
    @SerializedName("ap")
    AP,
    @SerializedName("hp")
    HP,
    @SerializedName("incendiary")
    INCENDIARY,
    @SerializedName("explosive")
    EXPLOSIVE,
    @SerializedName("custom")
    CUSTOM;
    public String nameLower() {
        return this.name().toLowerCase();
    }
}

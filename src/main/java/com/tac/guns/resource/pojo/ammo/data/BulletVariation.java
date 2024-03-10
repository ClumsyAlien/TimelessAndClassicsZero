package com.tac.guns.resource.pojo.ammo.data;

import com.google.gson.annotations.SerializedName;

public enum BulletVariation {
    @SerializedName("ap")
    AP,
    @SerializedName("hp")
    HP,
    @SerializedName("incendiary")
    INCENDIARY,
    @SerializedName("explosive")
    EXPLOSIVE;

    public String nameLower() {
        return this.name().toLowerCase();
    }
}

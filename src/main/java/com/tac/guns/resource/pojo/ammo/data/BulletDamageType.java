package com.tac.guns.resource.pojo.ammo.data;

import com.google.gson.annotations.SerializedName;

public enum BulletDamageType {
    @SerializedName("standard")
    STANDARD, // Standard bullet damage calculation
    @SerializedName("magic")
    MAGIC, // Apply damage as standard Minecraft magic damage
}

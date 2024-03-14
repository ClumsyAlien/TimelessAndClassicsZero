package com.tac.guns.resource.pojo.data.ammo;

import com.google.gson.annotations.SerializedName;

public enum BulletDamageType {
    //TODO (ClumsyAlien): Rebuild BulletDamageType to only EXTEND Minecraft damage types, this will be a breaking change, fix before making API release!
    @SerializedName("standard")
    STANDARD, // Standard bullet damage calculation
    @SerializedName("magic")
    MAGIC; // Apply damage as standard Minecraft magic damage
}

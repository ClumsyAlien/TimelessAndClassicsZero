package com.tac.guns.resource;

import com.google.common.collect.Maps;
import com.tac.guns.resource.pojo.ammo.BulletData;
import com.tac.guns.resource.pojo.gun.GunData;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public enum CommonAssetManager {
    INSTANCE;

    /**
     * 储存 data 数据
     */
    private final Map<ResourceLocation, GunData> gunData = Maps.newHashMap();

    public void putGunData(ResourceLocation registryName, GunData data) {
        gunData.put(registryName, data);
    }

    public GunData getGunData(ResourceLocation registryName) {
        return gunData.get(registryName);
    }

    private final Map<ResourceLocation, BulletData> bulletData = Maps.newHashMap();

    public void putBulletData(ResourceLocation registryName, BulletData data) {
        bulletData.put(registryName, data);
    }

    public BulletData getBulletData(ResourceLocation registryName) {
        return bulletData.get(registryName);
    }

    public void clearAll() {
        // TODO：重载时清理缓存
    }
}

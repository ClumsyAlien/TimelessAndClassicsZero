package com.tac.guns.resource.serialize;

import com.google.gson.*;
import com.tac.guns.resource.pojo.data.ammo.BulletDamageType;
import com.tac.guns.resource.pojo.data.ammo.BulletVariation;
import com.tac.guns.resource.pojo.data.ammo.BulletVariationWrapper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Type;

public class BulletVariationWrapperSerializer implements JsonDeserializer<BulletVariationWrapper>, JsonSerializer<BulletVariationWrapper> {
    @Override
    public BulletVariationWrapper deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        // Default case, we don't care to have custom ammunition, bullets only have 1 definition
        String suffix = "";
        try {
            JsonElement suffixElement = jsonObject.get("suffix");
            suffix = suffixElement.getAsString();
        } catch(NullPointerException ignored) {}

        ResourceLocation display = context.deserialize(jsonObject.get("display"), ResourceLocation.class);

        BulletDamageType bulletDamageType = BulletDamageType.STANDARD;
        JsonElement bulletDamageTypeElement = jsonObject.get("damage_type");
        try {
            bulletDamageType = BulletDamageType.valueOf(bulletDamageTypeElement.getAsString().toUpperCase());
        } catch(NullPointerException ignored) {}


        return new BulletVariationWrapper(suffix, display,  bulletDamageType);
    }

    // Added just in case serialize is overwritten for this type due to custom handler
    @Override
    public JsonElement serialize(BulletVariationWrapper src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("suffix", src.getSuffix());

        JsonElement displayJson = context.serialize(src.getDisplay());
        jsonObject.add("display", displayJson);

        JsonElement bulletDamageTypeJson = context.serialize(src.getDamageType());
        jsonObject.add("damage_type", bulletDamageTypeJson);

        return jsonObject;
    }
}

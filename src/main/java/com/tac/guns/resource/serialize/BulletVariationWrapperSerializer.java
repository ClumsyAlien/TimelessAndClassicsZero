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

        String suffix = jsonObject.get("suffix").getAsString();
        ResourceLocation display = context.deserialize(jsonObject.get("display"), ResourceLocation.class);

        BulletVariation bulletVariation;
        try {
            JsonElement bulletVariationElement = jsonObject.get("bullet_variation");
            bulletVariation = BulletVariation.valueOf(bulletVariationElement.getAsString().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            bulletVariation = BulletVariation.CUSTOM; // TODO: Use "name" as identifier and suffix for custom model if display is not included.
        }

        JsonElement bulletDamageTypeElement = jsonObject.get("damage_type");
        BulletDamageType bulletDamageType = BulletDamageType.valueOf(bulletDamageTypeElement.getAsString().toUpperCase());

        return new BulletVariationWrapper(suffix, display, bulletVariation, bulletDamageType);
    }

    // Added just in case serialize is overwritten for this type due to custom handler
    @Override
    public JsonElement serialize(BulletVariationWrapper src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("suffix", src.getSuffix());

        JsonElement displayJson = context.serialize(src.getDisplay());
        jsonObject.add("display", displayJson);

        JsonElement bulletVariationJson = context.serialize(src.getBulletVariation());
        jsonObject.add("bullet_variation", bulletVariationJson);

        JsonElement bulletDamageTypeJson = context.serialize(src.getDamageType());
        jsonObject.add("damage_type", bulletDamageTypeJson);

        return jsonObject;
    }
}

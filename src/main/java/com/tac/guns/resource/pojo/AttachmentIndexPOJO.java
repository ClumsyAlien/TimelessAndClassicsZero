package com.tac.guns.resource.pojo;

import com.google.gson.annotations.SerializedName;
import com.tac.guns.api.attachment.AttachmentType;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public class AttachmentIndexPOJO {
    @SerializedName("name")
    private String name;

    @SerializedName("tooltip")
    @Nullable
    private String tooltip;

    @SerializedName("display")
    private ResourceLocation display;

    @SerializedName("data")
    private ResourceLocation data;

    @SerializedName("type")
    private AttachmentType type;

    public String getName() {
        return name;
    }

    @Nullable
    public String getTooltip() {
        return tooltip;
    }

    public ResourceLocation getDisplay() {
        return display;
    }

    public ResourceLocation getData() {
        return data;
    }

    public AttachmentType getType() {
        return type;
    }
}

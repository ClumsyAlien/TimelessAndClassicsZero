package com.tac.guns.item.builder;

import com.tac.guns.api.item.IAttachment;
import com.tac.guns.init.ModItems;
import com.tac.guns.resource.DefaultAssets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class AttachmentItemBuilder {
    private int count = 1;
    private ResourceLocation attachmentId = DefaultAssets.DEFAULT_ATTACHMENT_ID;
    private ResourceLocation skinId = null;

    private AttachmentItemBuilder() {
    }

    public static AttachmentItemBuilder create() {
        return new AttachmentItemBuilder();
    }

    public AttachmentItemBuilder setCount(int count) {
        this.count = Math.max(count, 1);
        return this;
    }

    public AttachmentItemBuilder setId(ResourceLocation id) {
        this.attachmentId = id;
        return this;
    }

    public AttachmentItemBuilder setSkinId(ResourceLocation skinId) {
        this.skinId = skinId;
        return this;
    }

    public ItemStack build() {
        ItemStack attachment = new ItemStack(ModItems.ATTACHMENT.get(), this.count);
        if (attachment.getItem() instanceof IAttachment iAttachment) {
            iAttachment.setAttachmentId(attachment, this.attachmentId);
            iAttachment.setSkinId(attachment, this.skinId);
        }
        return attachment;
    }
}

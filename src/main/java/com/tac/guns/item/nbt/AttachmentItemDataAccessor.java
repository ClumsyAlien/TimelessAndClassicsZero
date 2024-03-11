package com.tac.guns.item.nbt;

import com.tac.guns.api.item.IAttachment;
import com.tac.guns.resource.DefaultAssets;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public interface AttachmentItemDataAccessor extends IAttachment {
    String ATTACHMENT_ID_TAG = "AttachmentId";

    @Override
    @Nonnull
    default ResourceLocation getAttachmentId(ItemStack attachmentStack){
        CompoundTag nbt = attachmentStack.getOrCreateTag();
        if (nbt.contains(ATTACHMENT_ID_TAG, Tag.TAG_STRING)) {
            ResourceLocation attachmentId = ResourceLocation.tryParse(nbt.getString(ATTACHMENT_ID_TAG));
            return Objects.requireNonNullElse(attachmentId, DefaultAssets.DEFAULT_ATTACHMENT_ID);
        }
        return DefaultAssets.DEFAULT_ATTACHMENT_ID;
    }

    default void setAttachmentId(ItemStack attachmentStack, @Nullable ResourceLocation attachmentId){
        CompoundTag nbt = attachmentStack.getOrCreateTag();
        if (attachmentId != null) {
            nbt.putString(ATTACHMENT_ID_TAG, attachmentId.toString());
            return;
        }
        nbt.putString(ATTACHMENT_ID_TAG, DefaultAssets.DEFAULT_ATTACHMENT_ID.toString());
    }
}

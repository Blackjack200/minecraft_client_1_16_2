package net.minecraft.client.resources.metadata.texture;

import net.minecraft.util.GsonHelper;
import com.google.gson.JsonObject;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;

public class TextureMetadataSectionSerializer implements MetadataSectionSerializer<TextureMetadataSection> {
    public TextureMetadataSection fromJson(final JsonObject jsonObject) {
        final boolean boolean3 = GsonHelper.getAsBoolean(jsonObject, "blur", false);
        final boolean boolean4 = GsonHelper.getAsBoolean(jsonObject, "clamp", false);
        return new TextureMetadataSection(boolean3, boolean4);
    }
    
    public String getMetadataSectionName() {
        return "texture";
    }
}

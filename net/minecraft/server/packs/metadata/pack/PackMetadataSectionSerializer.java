package net.minecraft.server.packs.metadata.pack;

import net.minecraft.util.GsonHelper;
import com.google.gson.JsonParseException;
import net.minecraft.network.chat.Component;
import com.google.gson.JsonObject;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;

public class PackMetadataSectionSerializer implements MetadataSectionSerializer<PackMetadataSection> {
    public PackMetadataSection fromJson(final JsonObject jsonObject) {
        final Component nr3 = Component.Serializer.fromJson(jsonObject.get("description"));
        if (nr3 == null) {
            throw new JsonParseException("Invalid/missing description!");
        }
        final int integer4 = GsonHelper.getAsInt(jsonObject, "pack_format");
        return new PackMetadataSection(nr3, integer4);
    }
    
    public String getMetadataSectionName() {
        return "pack";
    }
}

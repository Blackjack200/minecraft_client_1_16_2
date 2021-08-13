package net.minecraft.client.resources.metadata.animation;

import net.minecraft.util.GsonHelper;
import com.google.gson.JsonObject;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;

public class VillagerMetadataSectionSerializer implements MetadataSectionSerializer<VillagerMetaDataSection> {
    public VillagerMetaDataSection fromJson(final JsonObject jsonObject) {
        return new VillagerMetaDataSection(VillagerMetaDataSection.Hat.getByName(GsonHelper.getAsString(jsonObject, "hat", "none")));
    }
    
    public String getMetadataSectionName() {
        return "villager";
    }
}

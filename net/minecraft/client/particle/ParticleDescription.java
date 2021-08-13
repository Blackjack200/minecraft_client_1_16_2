package net.minecraft.client.particle;

import com.google.gson.JsonElement;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import com.google.gson.JsonArray;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import java.util.List;

public class ParticleDescription {
    @Nullable
    private final List<ResourceLocation> textures;
    
    private ParticleDescription(@Nullable final List<ResourceLocation> list) {
        this.textures = list;
    }
    
    @Nullable
    public List<ResourceLocation> getTextures() {
        return this.textures;
    }
    
    public static ParticleDescription fromJson(final JsonObject jsonObject) {
        final JsonArray jsonArray2 = GsonHelper.getAsJsonArray(jsonObject, "textures", (JsonArray)null);
        List<ResourceLocation> list3;
        if (jsonArray2 != null) {
            list3 = (List<ResourceLocation>)Streams.stream((Iterable)jsonArray2).map(jsonElement -> GsonHelper.convertToString(jsonElement, "texture")).map(ResourceLocation::new).collect(ImmutableList.toImmutableList());
        }
        else {
            list3 = null;
        }
        return new ParticleDescription(list3);
    }
}

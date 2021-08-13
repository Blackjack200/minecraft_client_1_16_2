package net.minecraft.client.resources.metadata.animation;

import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import java.util.List;
import com.google.gson.JsonParseException;
import org.apache.commons.lang3.Validate;
import net.minecraft.util.GsonHelper;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;

public class AnimationMetadataSectionSerializer implements MetadataSectionSerializer<AnimationMetadataSection> {
    public AnimationMetadataSection fromJson(final JsonObject jsonObject) {
        final List<AnimationFrame> list3 = (List<AnimationFrame>)Lists.newArrayList();
        final int integer4 = GsonHelper.getAsInt(jsonObject, "frametime", 1);
        if (integer4 != 1) {
            Validate.inclusiveBetween(1L, 2147483647L, (long)integer4, "Invalid default frame time");
        }
        if (jsonObject.has("frames")) {
            try {
                final JsonArray jsonArray5 = GsonHelper.getAsJsonArray(jsonObject, "frames");
                for (int integer5 = 0; integer5 < jsonArray5.size(); ++integer5) {
                    final JsonElement jsonElement7 = jsonArray5.get(integer5);
                    final AnimationFrame ekt8 = this.getFrame(integer5, jsonElement7);
                    if (ekt8 != null) {
                        list3.add(ekt8);
                    }
                }
            }
            catch (ClassCastException classCastException5) {
                throw new JsonParseException(new StringBuilder().append("Invalid animation->frames: expected array, was ").append(jsonObject.get("frames")).toString(), (Throwable)classCastException5);
            }
        }
        final int integer6 = GsonHelper.getAsInt(jsonObject, "width", -1);
        int integer5 = GsonHelper.getAsInt(jsonObject, "height", -1);
        if (integer6 != -1) {
            Validate.inclusiveBetween(1L, 2147483647L, (long)integer6, "Invalid width");
        }
        if (integer5 != -1) {
            Validate.inclusiveBetween(1L, 2147483647L, (long)integer5, "Invalid height");
        }
        final boolean boolean7 = GsonHelper.getAsBoolean(jsonObject, "interpolate", false);
        return new AnimationMetadataSection(list3, integer6, integer5, integer4, boolean7);
    }
    
    private AnimationFrame getFrame(final int integer, final JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive()) {
            return new AnimationFrame(GsonHelper.convertToInt(jsonElement, new StringBuilder().append("frames[").append(integer).append("]").toString()));
        }
        if (jsonElement.isJsonObject()) {
            final JsonObject jsonObject4 = GsonHelper.convertToJsonObject(jsonElement, new StringBuilder().append("frames[").append(integer).append("]").toString());
            final int integer2 = GsonHelper.getAsInt(jsonObject4, "time", -1);
            if (jsonObject4.has("time")) {
                Validate.inclusiveBetween(1L, 2147483647L, (long)integer2, "Invalid frame time");
            }
            final int integer3 = GsonHelper.getAsInt(jsonObject4, "index");
            Validate.inclusiveBetween(0L, 2147483647L, (long)integer3, "Invalid frame index");
            return new AnimationFrame(integer3, integer2);
        }
        return null;
    }
    
    public String getMetadataSectionName() {
        return "animation";
    }
}

package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonArray;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import javax.annotation.Nullable;

public class BlockFaceUV {
    public float[] uvs;
    public final int rotation;
    
    public BlockFaceUV(@Nullable final float[] arr, final int integer) {
        this.uvs = arr;
        this.rotation = integer;
    }
    
    public float getU(final int integer) {
        if (this.uvs == null) {
            throw new NullPointerException("uvs");
        }
        final int integer2 = this.getShiftedIndex(integer);
        return this.uvs[(integer2 == 0 || integer2 == 1) ? 0 : 2];
    }
    
    public float getV(final int integer) {
        if (this.uvs == null) {
            throw new NullPointerException("uvs");
        }
        final int integer2 = this.getShiftedIndex(integer);
        return this.uvs[(integer2 == 0 || integer2 == 3) ? 1 : 3];
    }
    
    private int getShiftedIndex(final int integer) {
        return (integer + this.rotation / 90) % 4;
    }
    
    public int getReverseIndex(final int integer) {
        return (integer + 4 - this.rotation / 90) % 4;
    }
    
    public void setMissingUv(final float[] arr) {
        if (this.uvs == null) {
            this.uvs = arr;
        }
    }
    
    public static class Deserializer implements JsonDeserializer<BlockFaceUV> {
        protected Deserializer() {
        }
        
        public BlockFaceUV deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            final JsonObject jsonObject5 = jsonElement.getAsJsonObject();
            final float[] arr6 = this.getUVs(jsonObject5);
            final int integer7 = this.getRotation(jsonObject5);
            return new BlockFaceUV(arr6, integer7);
        }
        
        protected int getRotation(final JsonObject jsonObject) {
            final int integer3 = GsonHelper.getAsInt(jsonObject, "rotation", 0);
            if (integer3 < 0 || integer3 % 90 != 0 || integer3 / 90 > 3) {
                throw new JsonParseException(new StringBuilder().append("Invalid rotation ").append(integer3).append(" found, only 0/90/180/270 allowed").toString());
            }
            return integer3;
        }
        
        @Nullable
        private float[] getUVs(final JsonObject jsonObject) {
            if (!jsonObject.has("uv")) {
                return null;
            }
            final JsonArray jsonArray3 = GsonHelper.getAsJsonArray(jsonObject, "uv");
            if (jsonArray3.size() != 4) {
                throw new JsonParseException(new StringBuilder().append("Expected 4 uv values, found: ").append(jsonArray3.size()).toString());
            }
            final float[] arr4 = new float[4];
            for (int integer5 = 0; integer5 < arr4.length; ++integer5) {
                arr4[integer5] = GsonHelper.convertToFloat(jsonArray3.get(integer5), new StringBuilder().append("uv[").append(integer5).append("]").toString());
            }
            return arr4;
        }
    }
}

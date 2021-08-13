package net.minecraft.client.renderer.block.model;

import net.minecraft.util.GsonHelper;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;

public class BlockElementFace {
    public final Direction cullForDirection;
    public final int tintIndex;
    public final String texture;
    public final BlockFaceUV uv;
    
    public BlockElementFace(@Nullable final Direction gc, final int integer, final String string, final BlockFaceUV eaw) {
        this.cullForDirection = gc;
        this.tintIndex = integer;
        this.texture = string;
        this.uv = eaw;
    }
    
    public static class Deserializer implements JsonDeserializer<BlockElementFace> {
        protected Deserializer() {
        }
        
        public BlockElementFace deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            final JsonObject jsonObject5 = jsonElement.getAsJsonObject();
            final Direction gc6 = this.getCullFacing(jsonObject5);
            final int integer7 = this.getTintIndex(jsonObject5);
            final String string8 = this.getTexture(jsonObject5);
            final BlockFaceUV eaw9 = (BlockFaceUV)jsonDeserializationContext.deserialize((JsonElement)jsonObject5, (Type)BlockFaceUV.class);
            return new BlockElementFace(gc6, integer7, string8, eaw9);
        }
        
        protected int getTintIndex(final JsonObject jsonObject) {
            return GsonHelper.getAsInt(jsonObject, "tintindex", -1);
        }
        
        private String getTexture(final JsonObject jsonObject) {
            return GsonHelper.getAsString(jsonObject, "texture");
        }
        
        @Nullable
        private Direction getCullFacing(final JsonObject jsonObject) {
            final String string3 = GsonHelper.getAsString(jsonObject, "cullface", "");
            return Direction.byName(string3);
        }
    }
}

package net.minecraft.client.renderer.block.model;

import net.minecraft.util.GsonHelper;
import com.google.gson.JsonParseException;
import net.minecraft.client.resources.model.BlockModelRotation;
import com.google.gson.JsonObject;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import java.util.Objects;
import com.mojang.math.Transformation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.resources.model.ModelState;

public class Variant implements ModelState {
    private final ResourceLocation modelLocation;
    private final Transformation rotation;
    private final boolean uvLock;
    private final int weight;
    
    public Variant(final ResourceLocation vk, final Transformation f, final boolean boolean3, final int integer) {
        this.modelLocation = vk;
        this.rotation = f;
        this.uvLock = boolean3;
        this.weight = integer;
    }
    
    public ResourceLocation getModelLocation() {
        return this.modelLocation;
    }
    
    public Transformation getRotation() {
        return this.rotation;
    }
    
    public boolean isUvLocked() {
        return this.uvLock;
    }
    
    public int getWeight() {
        return this.weight;
    }
    
    public String toString() {
        return new StringBuilder().append("Variant{modelLocation=").append(this.modelLocation).append(", rotation=").append(this.rotation).append(", uvLock=").append(this.uvLock).append(", weight=").append(this.weight).append('}').toString();
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof Variant) {
            final Variant ebg3 = (Variant)object;
            return this.modelLocation.equals(ebg3.modelLocation) && Objects.equals(this.rotation, ebg3.rotation) && this.uvLock == ebg3.uvLock && this.weight == ebg3.weight;
        }
        return false;
    }
    
    public int hashCode() {
        int integer2 = this.modelLocation.hashCode();
        integer2 = 31 * integer2 + this.rotation.hashCode();
        integer2 = 31 * integer2 + Boolean.valueOf(this.uvLock).hashCode();
        integer2 = 31 * integer2 + this.weight;
        return integer2;
    }
    
    public static class Deserializer implements JsonDeserializer<Variant> {
        public Variant deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            final JsonObject jsonObject5 = jsonElement.getAsJsonObject();
            final ResourceLocation vk6 = this.getModel(jsonObject5);
            final BlockModelRotation elh7 = this.getBlockRotation(jsonObject5);
            final boolean boolean8 = this.getUvLock(jsonObject5);
            final int integer9 = this.getWeight(jsonObject5);
            return new Variant(vk6, elh7.getRotation(), boolean8, integer9);
        }
        
        private boolean getUvLock(final JsonObject jsonObject) {
            return GsonHelper.getAsBoolean(jsonObject, "uvlock", false);
        }
        
        protected BlockModelRotation getBlockRotation(final JsonObject jsonObject) {
            final int integer3 = GsonHelper.getAsInt(jsonObject, "x", 0);
            final int integer4 = GsonHelper.getAsInt(jsonObject, "y", 0);
            final BlockModelRotation elh5 = BlockModelRotation.by(integer3, integer4);
            if (elh5 == null) {
                throw new JsonParseException(new StringBuilder().append("Invalid BlockModelRotation x: ").append(integer3).append(", y: ").append(integer4).toString());
            }
            return elh5;
        }
        
        protected ResourceLocation getModel(final JsonObject jsonObject) {
            return new ResourceLocation(GsonHelper.getAsString(jsonObject, "model"));
        }
        
        protected int getWeight(final JsonObject jsonObject) {
            final int integer3 = GsonHelper.getAsInt(jsonObject, "weight", 1);
            if (integer3 < 1) {
                throw new JsonParseException(new StringBuilder().append("Invalid weight ").append(integer3).append(" found, expected integer >= 1").toString());
            }
            return integer3;
        }
    }
}

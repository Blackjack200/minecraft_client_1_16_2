package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonArray;
import com.google.common.collect.Maps;
import java.util.Locale;
import net.minecraft.util.Mth;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import java.util.Map;
import com.mojang.math.Vector3f;

public class BlockElement {
    public final Vector3f from;
    public final Vector3f to;
    public final Map<Direction, BlockElementFace> faces;
    public final BlockElementRotation rotation;
    public final boolean shade;
    
    public BlockElement(final Vector3f g1, final Vector3f g2, final Map<Direction, BlockElementFace> map, @Nullable final BlockElementRotation eav, final boolean boolean5) {
        this.from = g1;
        this.to = g2;
        this.faces = map;
        this.rotation = eav;
        this.shade = boolean5;
        this.fillUvs();
    }
    
    private void fillUvs() {
        for (final Map.Entry<Direction, BlockElementFace> entry3 : this.faces.entrySet()) {
            final float[] arr4 = this.uvsByFace((Direction)entry3.getKey());
            ((BlockElementFace)entry3.getValue()).uv.setMissingUv(arr4);
        }
    }
    
    private float[] uvsByFace(final Direction gc) {
        switch (gc) {
            case DOWN: {
                return new float[] { this.from.x(), 16.0f - this.to.z(), this.to.x(), 16.0f - this.from.z() };
            }
            case UP: {
                return new float[] { this.from.x(), this.from.z(), this.to.x(), this.to.z() };
            }
            default: {
                return new float[] { 16.0f - this.to.x(), 16.0f - this.to.y(), 16.0f - this.from.x(), 16.0f - this.from.y() };
            }
            case SOUTH: {
                return new float[] { this.from.x(), 16.0f - this.to.y(), this.to.x(), 16.0f - this.from.y() };
            }
            case WEST: {
                return new float[] { this.from.z(), 16.0f - this.to.y(), this.to.z(), 16.0f - this.from.y() };
            }
            case EAST: {
                return new float[] { 16.0f - this.to.z(), 16.0f - this.to.y(), 16.0f - this.from.z(), 16.0f - this.from.y() };
            }
        }
    }
    
    public static class Deserializer implements JsonDeserializer<BlockElement> {
        protected Deserializer() {
        }
        
        public BlockElement deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            final JsonObject jsonObject5 = jsonElement.getAsJsonObject();
            final Vector3f g6 = this.getFrom(jsonObject5);
            final Vector3f g7 = this.getTo(jsonObject5);
            final BlockElementRotation eav8 = this.getRotation(jsonObject5);
            final Map<Direction, BlockElementFace> map9 = this.getFaces(jsonDeserializationContext, jsonObject5);
            if (jsonObject5.has("shade") && !GsonHelper.isBooleanValue(jsonObject5, "shade")) {
                throw new JsonParseException("Expected shade to be a Boolean");
            }
            final boolean boolean10 = GsonHelper.getAsBoolean(jsonObject5, "shade", true);
            return new BlockElement(g6, g7, map9, eav8, boolean10);
        }
        
        @Nullable
        private BlockElementRotation getRotation(final JsonObject jsonObject) {
            BlockElementRotation eav3 = null;
            if (jsonObject.has("rotation")) {
                final JsonObject jsonObject2 = GsonHelper.getAsJsonObject(jsonObject, "rotation");
                final Vector3f g5 = this.getVector3f(jsonObject2, "origin");
                g5.mul(0.0625f);
                final Direction.Axis a6 = this.getAxis(jsonObject2);
                final float float7 = this.getAngle(jsonObject2);
                final boolean boolean8 = GsonHelper.getAsBoolean(jsonObject2, "rescale", false);
                eav3 = new BlockElementRotation(g5, a6, float7, boolean8);
            }
            return eav3;
        }
        
        private float getAngle(final JsonObject jsonObject) {
            final float float3 = GsonHelper.getAsFloat(jsonObject, "angle");
            if (float3 != 0.0f && Mth.abs(float3) != 22.5f && Mth.abs(float3) != 45.0f) {
                throw new JsonParseException(new StringBuilder().append("Invalid rotation ").append(float3).append(" found, only -45/-22.5/0/22.5/45 allowed").toString());
            }
            return float3;
        }
        
        private Direction.Axis getAxis(final JsonObject jsonObject) {
            final String string3 = GsonHelper.getAsString(jsonObject, "axis");
            final Direction.Axis a4 = Direction.Axis.byName(string3.toLowerCase(Locale.ROOT));
            if (a4 == null) {
                throw new JsonParseException("Invalid rotation axis: " + string3);
            }
            return a4;
        }
        
        private Map<Direction, BlockElementFace> getFaces(final JsonDeserializationContext jsonDeserializationContext, final JsonObject jsonObject) {
            final Map<Direction, BlockElementFace> map4 = this.filterNullFromFaces(jsonDeserializationContext, jsonObject);
            if (map4.isEmpty()) {
                throw new JsonParseException("Expected between 1 and 6 unique faces, got 0");
            }
            return map4;
        }
        
        private Map<Direction, BlockElementFace> filterNullFromFaces(final JsonDeserializationContext jsonDeserializationContext, final JsonObject jsonObject) {
            final Map<Direction, BlockElementFace> map4 = (Map<Direction, BlockElementFace>)Maps.newEnumMap((Class)Direction.class);
            final JsonObject jsonObject2 = GsonHelper.getAsJsonObject(jsonObject, "faces");
            for (final Map.Entry<String, JsonElement> entry7 : jsonObject2.entrySet()) {
                final Direction gc8 = this.getFacing((String)entry7.getKey());
                map4.put(gc8, jsonDeserializationContext.deserialize((JsonElement)entry7.getValue(), (Type)BlockElementFace.class));
            }
            return map4;
        }
        
        private Direction getFacing(final String string) {
            final Direction gc3 = Direction.byName(string);
            if (gc3 == null) {
                throw new JsonParseException("Unknown facing: " + string);
            }
            return gc3;
        }
        
        private Vector3f getTo(final JsonObject jsonObject) {
            final Vector3f g3 = this.getVector3f(jsonObject, "to");
            if (g3.x() < -16.0f || g3.y() < -16.0f || g3.z() < -16.0f || g3.x() > 32.0f || g3.y() > 32.0f || g3.z() > 32.0f) {
                throw new JsonParseException(new StringBuilder().append("'to' specifier exceeds the allowed boundaries: ").append(g3).toString());
            }
            return g3;
        }
        
        private Vector3f getFrom(final JsonObject jsonObject) {
            final Vector3f g3 = this.getVector3f(jsonObject, "from");
            if (g3.x() < -16.0f || g3.y() < -16.0f || g3.z() < -16.0f || g3.x() > 32.0f || g3.y() > 32.0f || g3.z() > 32.0f) {
                throw new JsonParseException(new StringBuilder().append("'from' specifier exceeds the allowed boundaries: ").append(g3).toString());
            }
            return g3;
        }
        
        private Vector3f getVector3f(final JsonObject jsonObject, final String string) {
            final JsonArray jsonArray4 = GsonHelper.getAsJsonArray(jsonObject, string);
            if (jsonArray4.size() != 3) {
                throw new JsonParseException("Expected 3 " + string + " values, found: " + jsonArray4.size());
            }
            final float[] arr5 = new float[3];
            for (int integer6 = 0; integer6 < arr5.length; ++integer6) {
                arr5[integer6] = GsonHelper.convertToFloat(jsonArray4.get(integer6), string + "[" + integer6 + "]");
            }
            return new Vector3f(arr5[0], arr5[1], arr5[2]);
        }
    }
}

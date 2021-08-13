package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonArray;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import com.mojang.math.Quaternion;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

public class ItemTransform {
    public static final ItemTransform NO_TRANSFORM;
    public final Vector3f rotation;
    public final Vector3f translation;
    public final Vector3f scale;
    
    public ItemTransform(final Vector3f g1, final Vector3f g2, final Vector3f g3) {
        this.rotation = g1.copy();
        this.translation = g2.copy();
        this.scale = g3.copy();
    }
    
    public void apply(final boolean boolean1, final PoseStack dfj) {
        if (this == ItemTransform.NO_TRANSFORM) {
            return;
        }
        final float float4 = this.rotation.x();
        float float5 = this.rotation.y();
        float float6 = this.rotation.z();
        if (boolean1) {
            float5 = -float5;
            float6 = -float6;
        }
        final int integer7 = boolean1 ? -1 : 1;
        dfj.translate(integer7 * this.translation.x(), this.translation.y(), this.translation.z());
        dfj.mulPose(new Quaternion(float4, float5, float6, true));
        dfj.scale(this.scale.x(), this.scale.y(), this.scale.z());
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (this.getClass() == object.getClass()) {
            final ItemTransform ebd3 = (ItemTransform)object;
            return this.rotation.equals(ebd3.rotation) && this.scale.equals(ebd3.scale) && this.translation.equals(ebd3.translation);
        }
        return false;
    }
    
    public int hashCode() {
        int integer2 = this.rotation.hashCode();
        integer2 = 31 * integer2 + this.translation.hashCode();
        integer2 = 31 * integer2 + this.scale.hashCode();
        return integer2;
    }
    
    static {
        NO_TRANSFORM = new ItemTransform(new Vector3f(), new Vector3f(), new Vector3f(1.0f, 1.0f, 1.0f));
    }
    
    public static class Deserializer implements JsonDeserializer<ItemTransform> {
        private static final Vector3f DEFAULT_ROTATION;
        private static final Vector3f DEFAULT_TRANSLATION;
        private static final Vector3f DEFAULT_SCALE;
        
        protected Deserializer() {
        }
        
        public ItemTransform deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            final JsonObject jsonObject5 = jsonElement.getAsJsonObject();
            final Vector3f g6 = this.getVector3f(jsonObject5, "rotation", Deserializer.DEFAULT_ROTATION);
            final Vector3f g7 = this.getVector3f(jsonObject5, "translation", Deserializer.DEFAULT_TRANSLATION);
            g7.mul(0.0625f);
            g7.clamp(-5.0f, 5.0f);
            final Vector3f g8 = this.getVector3f(jsonObject5, "scale", Deserializer.DEFAULT_SCALE);
            g8.clamp(-4.0f, 4.0f);
            return new ItemTransform(g6, g7, g8);
        }
        
        private Vector3f getVector3f(final JsonObject jsonObject, final String string, final Vector3f g) {
            if (!jsonObject.has(string)) {
                return g;
            }
            final JsonArray jsonArray5 = GsonHelper.getAsJsonArray(jsonObject, string);
            if (jsonArray5.size() != 3) {
                throw new JsonParseException("Expected 3 " + string + " values, found: " + jsonArray5.size());
            }
            final float[] arr6 = new float[3];
            for (int integer7 = 0; integer7 < arr6.length; ++integer7) {
                arr6[integer7] = GsonHelper.convertToFloat(jsonArray5.get(integer7), string + "[" + integer7 + "]");
            }
            return new Vector3f(arr6[0], arr6[1], arr6[2]);
        }
        
        static {
            DEFAULT_ROTATION = new Vector3f(0.0f, 0.0f, 0.0f);
            DEFAULT_TRANSLATION = new Vector3f(0.0f, 0.0f, 0.0f);
            DEFAULT_SCALE = new Vector3f(1.0f, 1.0f, 1.0f);
        }
    }
}

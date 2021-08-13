package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;

public class ItemTransforms {
    public static final ItemTransforms NO_TRANSFORMS;
    public final ItemTransform thirdPersonLeftHand;
    public final ItemTransform thirdPersonRightHand;
    public final ItemTransform firstPersonLeftHand;
    public final ItemTransform firstPersonRightHand;
    public final ItemTransform head;
    public final ItemTransform gui;
    public final ItemTransform ground;
    public final ItemTransform fixed;
    
    private ItemTransforms() {
        this(ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM);
    }
    
    public ItemTransforms(final ItemTransforms ebe) {
        this.thirdPersonLeftHand = ebe.thirdPersonLeftHand;
        this.thirdPersonRightHand = ebe.thirdPersonRightHand;
        this.firstPersonLeftHand = ebe.firstPersonLeftHand;
        this.firstPersonRightHand = ebe.firstPersonRightHand;
        this.head = ebe.head;
        this.gui = ebe.gui;
        this.ground = ebe.ground;
        this.fixed = ebe.fixed;
    }
    
    public ItemTransforms(final ItemTransform ebd1, final ItemTransform ebd2, final ItemTransform ebd3, final ItemTransform ebd4, final ItemTransform ebd5, final ItemTransform ebd6, final ItemTransform ebd7, final ItemTransform ebd8) {
        this.thirdPersonLeftHand = ebd1;
        this.thirdPersonRightHand = ebd2;
        this.firstPersonLeftHand = ebd3;
        this.firstPersonRightHand = ebd4;
        this.head = ebd5;
        this.gui = ebd6;
        this.ground = ebd7;
        this.fixed = ebd8;
    }
    
    public ItemTransform getTransform(final TransformType b) {
        switch (b) {
            case THIRD_PERSON_LEFT_HAND: {
                return this.thirdPersonLeftHand;
            }
            case THIRD_PERSON_RIGHT_HAND: {
                return this.thirdPersonRightHand;
            }
            case FIRST_PERSON_LEFT_HAND: {
                return this.firstPersonLeftHand;
            }
            case FIRST_PERSON_RIGHT_HAND: {
                return this.firstPersonRightHand;
            }
            case HEAD: {
                return this.head;
            }
            case GUI: {
                return this.gui;
            }
            case GROUND: {
                return this.ground;
            }
            case FIXED: {
                return this.fixed;
            }
            default: {
                return ItemTransform.NO_TRANSFORM;
            }
        }
    }
    
    public boolean hasTransform(final TransformType b) {
        return this.getTransform(b) != ItemTransform.NO_TRANSFORM;
    }
    
    static {
        NO_TRANSFORMS = new ItemTransforms();
    }
    
    public enum TransformType {
        NONE, 
        THIRD_PERSON_LEFT_HAND, 
        THIRD_PERSON_RIGHT_HAND, 
        FIRST_PERSON_LEFT_HAND, 
        FIRST_PERSON_RIGHT_HAND, 
        HEAD, 
        GUI, 
        GROUND, 
        FIXED;
        
        public boolean firstPerson() {
            return this == TransformType.FIRST_PERSON_LEFT_HAND || this == TransformType.FIRST_PERSON_RIGHT_HAND;
        }
    }
    
    public static class Deserializer implements JsonDeserializer<ItemTransforms> {
        protected Deserializer() {
        }
        
        public ItemTransforms deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            final JsonObject jsonObject5 = jsonElement.getAsJsonObject();
            final ItemTransform ebd6 = this.getTransform(jsonDeserializationContext, jsonObject5, "thirdperson_righthand");
            ItemTransform ebd7 = this.getTransform(jsonDeserializationContext, jsonObject5, "thirdperson_lefthand");
            if (ebd7 == ItemTransform.NO_TRANSFORM) {
                ebd7 = ebd6;
            }
            final ItemTransform ebd8 = this.getTransform(jsonDeserializationContext, jsonObject5, "firstperson_righthand");
            ItemTransform ebd9 = this.getTransform(jsonDeserializationContext, jsonObject5, "firstperson_lefthand");
            if (ebd9 == ItemTransform.NO_TRANSFORM) {
                ebd9 = ebd8;
            }
            final ItemTransform ebd10 = this.getTransform(jsonDeserializationContext, jsonObject5, "head");
            final ItemTransform ebd11 = this.getTransform(jsonDeserializationContext, jsonObject5, "gui");
            final ItemTransform ebd12 = this.getTransform(jsonDeserializationContext, jsonObject5, "ground");
            final ItemTransform ebd13 = this.getTransform(jsonDeserializationContext, jsonObject5, "fixed");
            return new ItemTransforms(ebd7, ebd6, ebd9, ebd8, ebd10, ebd11, ebd12, ebd13);
        }
        
        private ItemTransform getTransform(final JsonDeserializationContext jsonDeserializationContext, final JsonObject jsonObject, final String string) {
            if (jsonObject.has(string)) {
                return (ItemTransform)jsonDeserializationContext.deserialize(jsonObject.get(string), (Type)ItemTransform.class);
            }
            return ItemTransform.NO_TRANSFORM;
        }
    }
}

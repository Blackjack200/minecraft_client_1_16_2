package net.minecraft.data.models.blockstates;

import java.util.function.Function;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;

public class VariantProperties {
    public static final VariantProperty<Rotation> X_ROT;
    public static final VariantProperty<Rotation> Y_ROT;
    public static final VariantProperty<ResourceLocation> MODEL;
    public static final VariantProperty<Boolean> UV_LOCK;
    public static final VariantProperty<Integer> WEIGHT;
    
    static {
        X_ROT = new VariantProperty<Rotation>("x", (java.util.function.Function<Rotation, JsonElement>)(a -> new JsonPrimitive((Number)a.value)));
        Y_ROT = new VariantProperty<Rotation>("y", (java.util.function.Function<Rotation, JsonElement>)(a -> new JsonPrimitive((Number)a.value)));
        MODEL = new VariantProperty<ResourceLocation>("model", (java.util.function.Function<ResourceLocation, JsonElement>)(vk -> new JsonPrimitive(vk.toString())));
        UV_LOCK = new VariantProperty<Boolean>("uvlock", (java.util.function.Function<Boolean, JsonElement>)JsonPrimitive::new);
        WEIGHT = new VariantProperty<Integer>("weight", (java.util.function.Function<Integer, JsonElement>)JsonPrimitive::new);
    }
    
    public enum Rotation {
        R0(0), 
        R90(90), 
        R180(180), 
        R270(270);
        
        private final int value;
        
        private Rotation(final int integer3) {
            this.value = integer3;
        }
    }
}

package net.minecraft.util.datafix.schemas;

import com.mojang.serialization.Codec;
import com.mojang.datafixers.types.templates.Const;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.datafixers.DSL;
import net.minecraft.resources.ResourceLocation;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.datafixers.schemas.Schema;

public class NamespacedSchema extends Schema {
    public static final PrimitiveCodec<String> NAMESPACED_STRING_CODEC;
    private static final Type<String> NAMESPACED_STRING;
    
    public NamespacedSchema(final int integer, final Schema schema) {
        super(integer, schema);
    }
    
    public static String ensureNamespaced(final String string) {
        final ResourceLocation vk2 = ResourceLocation.tryParse(string);
        if (vk2 != null) {
            return vk2.toString();
        }
        return string;
    }
    
    public static Type<String> namespacedString() {
        return NamespacedSchema.NAMESPACED_STRING;
    }
    
    public Type<?> getChoiceType(final DSL.TypeReference typeReference, final String string) {
        return super.getChoiceType(typeReference, ensureNamespaced(string));
    }
    
    static {
        NAMESPACED_STRING_CODEC = (PrimitiveCodec)new PrimitiveCodec<String>() {
            public <T> DataResult<String> read(final DynamicOps<T> dynamicOps, final T object) {
                return (DataResult<String>)dynamicOps.getStringValue(object).map(NamespacedSchema::ensureNamespaced);
            }
            
            public <T> T write(final DynamicOps<T> dynamicOps, final String string) {
                return (T)dynamicOps.createString(string);
            }
            
            public String toString() {
                return "NamespacedString";
            }
        };
        NAMESPACED_STRING = (Type)new Const.PrimitiveType((Codec)NamespacedSchema.NAMESPACED_STRING_CODEC);
    }
}

package net.minecraft.data.models.blockstates;

import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import java.util.function.Function;

public class VariantProperty<T> {
    private final String key;
    private final Function<T, JsonElement> serializer;
    
    public VariantProperty(final String string, final Function<T, JsonElement> function) {
        this.key = string;
        this.serializer = function;
    }
    
    public Value withValue(final T object) {
        return new Value(object);
    }
    
    public String toString() {
        return this.key;
    }
    
    public class Value {
        private final T value;
        
        public Value(final T object) {
            this.value = object;
        }
        
        public void addToVariant(final JsonObject jsonObject) {
            jsonObject.add(VariantProperty.this.key, (JsonElement)VariantProperty.this.serializer.apply(this.value));
        }
        
        public String toString() {
            return VariantProperty.this.key + "=" + this.value;
        }
    }
}

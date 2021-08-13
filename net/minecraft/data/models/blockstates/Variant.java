package net.minecraft.data.models.blockstates;

import com.google.gson.JsonArray;
import java.util.List;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import java.util.Map;
import com.google.gson.JsonElement;
import java.util.function.Supplier;

public class Variant implements Supplier<JsonElement> {
    private final Map<VariantProperty<?>, VariantProperty.Value> values;
    
    public Variant() {
        this.values = (Map<VariantProperty<?>, VariantProperty.Value>)Maps.newLinkedHashMap();
    }
    
    public <T> Variant with(final VariantProperty<T> it, final T object) {
        final VariantProperty.Value a4 = (VariantProperty.Value)this.values.put(it, it.withValue(object));
        if (a4 != null) {
            throw new IllegalStateException(new StringBuilder().append("Replacing value of ").append(a4).append(" with ").append(object).toString());
        }
        return this;
    }
    
    public static Variant variant() {
        return new Variant();
    }
    
    public static Variant merge(final Variant ir1, final Variant ir2) {
        final Variant ir3 = new Variant();
        ir3.values.putAll((Map)ir1.values);
        ir3.values.putAll((Map)ir2.values);
        return ir3;
    }
    
    public JsonElement get() {
        final JsonObject jsonObject2 = new JsonObject();
        this.values.values().forEach(a -> a.addToVariant(jsonObject2));
        return (JsonElement)jsonObject2;
    }
    
    public static JsonElement convertList(final List<Variant> list) {
        if (list.size() == 1) {
            return ((Variant)list.get(0)).get();
        }
        final JsonArray jsonArray2 = new JsonArray();
        list.forEach(ir -> jsonArray2.add(ir.get()));
        return (JsonElement)jsonArray2;
    }
}

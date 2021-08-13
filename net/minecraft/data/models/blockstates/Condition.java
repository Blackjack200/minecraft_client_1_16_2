package net.minecraft.data.models.blockstates;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.google.common.collect.Maps;
import net.minecraft.world.level.block.state.properties.Property;
import java.util.Map;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.util.List;
import java.util.Arrays;
import net.minecraft.world.level.block.state.StateDefinition;
import com.google.gson.JsonElement;
import java.util.function.Supplier;

public interface Condition extends Supplier<JsonElement> {
    void validate(final StateDefinition<?, ?> cef);
    
    default TerminalCondition condition() {
        return new TerminalCondition();
    }
    
    default Condition or(final Condition... arr) {
        return new CompositeCondition(Operation.OR, Arrays.asList((Object[])arr));
    }
    
    public enum Operation {
        AND("AND"), 
        OR("OR");
        
        private final String id;
        
        private Operation(final String string3) {
            this.id = string3;
        }
    }
    
    public static class CompositeCondition implements Condition {
        private final Operation operation;
        private final List<Condition> subconditions;
        
        private CompositeCondition(final Operation b, final List<Condition> list) {
            this.operation = b;
            this.subconditions = list;
        }
        
        public void validate(final StateDefinition<?, ?> cef) {
            this.subconditions.forEach(im -> im.validate(cef));
        }
        
        public JsonElement get() {
            final JsonArray jsonArray2 = new JsonArray();
            this.subconditions.stream().map(Supplier::get).forEach(jsonArray2::add);
            final JsonObject jsonObject3 = new JsonObject();
            jsonObject3.add(this.operation.id, (JsonElement)jsonArray2);
            return (JsonElement)jsonObject3;
        }
    }
    
    public static class TerminalCondition implements Condition {
        private final Map<Property<?>, String> terms;
        
        public TerminalCondition() {
            this.terms = (Map<Property<?>, String>)Maps.newHashMap();
        }
        
        private static <T extends Comparable<T>> String joinValues(final Property<T> cfg, final Stream<T> stream) {
            return (String)stream.map(cfg::getName).collect(Collectors.joining("|"));
        }
        
        private static <T extends Comparable<T>> String getTerm(final Property<T> cfg, final T comparable, final T[] arr) {
            return TerminalCondition.<T>joinValues(cfg, (java.util.stream.Stream<T>)Stream.concat(Stream.of(comparable), Stream.of((Object[])arr)));
        }
        
        private <T extends Comparable<T>> void putValue(final Property<T> cfg, final String string) {
            final String string2 = (String)this.terms.put(cfg, string);
            if (string2 != null) {
                throw new IllegalStateException(new StringBuilder().append("Tried to replace ").append(cfg).append(" value from ").append(string2).append(" to ").append(string).toString());
            }
        }
        
        public final <T extends Comparable<T>> TerminalCondition term(final Property<T> cfg, final T comparable) {
            this.<T>putValue(cfg, cfg.getName(comparable));
            return this;
        }
        
        @SafeVarargs
        public final <T extends Comparable<T>> TerminalCondition term(final Property<T> cfg, final T comparable, final T... arr) {
            this.<T>putValue(cfg, TerminalCondition.<T>getTerm(cfg, comparable, arr));
            return this;
        }
        
        public JsonElement get() {
            final JsonObject jsonObject2 = new JsonObject();
            this.terms.forEach((cfg, string) -> jsonObject2.addProperty(cfg.getName(), string));
            return (JsonElement)jsonObject2;
        }
        
        public void validate(final StateDefinition<?, ?> cef) {
            final List<Property<?>> list3 = (List<Property<?>>)this.terms.keySet().stream().filter(cfg -> cef.getProperty(cfg.getName()) != cfg).collect(Collectors.toList());
            if (!list3.isEmpty()) {
                throw new IllegalStateException(new StringBuilder().append("Properties ").append(list3).append(" are missing from ").append(cef).toString());
            }
        }
    }
}

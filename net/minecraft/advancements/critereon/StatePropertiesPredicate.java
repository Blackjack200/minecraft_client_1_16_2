package net.minecraft.advancements.critereon;

import net.minecraft.util.StringRepresentable;
import com.google.gson.JsonPrimitive;
import java.util.Optional;
import net.minecraft.world.level.block.state.properties.Property;
import com.google.gson.JsonNull;
import java.util.Map;
import com.google.common.collect.Lists;
import java.util.function.Consumer;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Iterator;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.StateDefinition;
import java.util.Collection;
import com.google.common.collect.ImmutableList;
import javax.annotation.Nullable;
import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonElement;
import java.util.List;

public class StatePropertiesPredicate {
    public static final StatePropertiesPredicate ANY;
    private final List<PropertyMatcher> properties;
    
    private static PropertyMatcher fromJson(final String string, final JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive()) {
            final String string2 = jsonElement.getAsString();
            return new ExactPropertyMatcher(string, string2);
        }
        final JsonObject jsonObject3 = GsonHelper.convertToJsonObject(jsonElement, "value");
        final String string3 = jsonObject3.has("min") ? getStringOrNull(jsonObject3.get("min")) : null;
        final String string4 = jsonObject3.has("max") ? getStringOrNull(jsonObject3.get("max")) : null;
        return (string3 != null && string3.equals(string4)) ? new ExactPropertyMatcher(string, string3) : new RangedPropertyMatcher(string, string3, string4);
    }
    
    @Nullable
    private static String getStringOrNull(final JsonElement jsonElement) {
        if (jsonElement.isJsonNull()) {
            return null;
        }
        return jsonElement.getAsString();
    }
    
    private StatePropertiesPredicate(final List<PropertyMatcher> list) {
        this.properties = (List<PropertyMatcher>)ImmutableList.copyOf((Collection)list);
    }
    
    public <S extends StateHolder<?, S>> boolean matches(final StateDefinition<?, S> cef, final S ceg) {
        for (final PropertyMatcher c5 : this.properties) {
            if (!c5.<S>match(cef, ceg)) {
                return false;
            }
        }
        return true;
    }
    
    public boolean matches(final BlockState cee) {
        return this.<BlockState>matches(cee.getBlock().getStateDefinition(), cee);
    }
    
    public boolean matches(final FluidState cuu) {
        return this.<FluidState>matches(cuu.getType().getStateDefinition(), cuu);
    }
    
    public void checkState(final StateDefinition<?, ?> cef, final Consumer<String> consumer) {
        this.properties.forEach(c -> c.checkState(cef, consumer));
    }
    
    public static StatePropertiesPredicate fromJson(@Nullable final JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return StatePropertiesPredicate.ANY;
        }
        final JsonObject jsonObject2 = GsonHelper.convertToJsonObject(jsonElement, "properties");
        final List<PropertyMatcher> list3 = (List<PropertyMatcher>)Lists.newArrayList();
        for (final Map.Entry<String, JsonElement> entry5 : jsonObject2.entrySet()) {
            list3.add(fromJson((String)entry5.getKey(), (JsonElement)entry5.getValue()));
        }
        return new StatePropertiesPredicate(list3);
    }
    
    public JsonElement serializeToJson() {
        if (this == StatePropertiesPredicate.ANY) {
            return (JsonElement)JsonNull.INSTANCE;
        }
        final JsonObject jsonObject2 = new JsonObject();
        if (!this.properties.isEmpty()) {
            this.properties.forEach(c -> jsonObject2.add(c.getName(), c.toJson()));
        }
        return (JsonElement)jsonObject2;
    }
    
    static {
        ANY = new StatePropertiesPredicate((List<PropertyMatcher>)ImmutableList.of());
    }
    
    abstract static class PropertyMatcher {
        private final String name;
        
        public PropertyMatcher(final String string) {
            this.name = string;
        }
        
        public <S extends StateHolder<?, S>> boolean match(final StateDefinition<?, S> cef, final S ceg) {
            final Property<?> cfg4 = cef.getProperty(this.name);
            return cfg4 != null && this.match(ceg, cfg4);
        }
        
        protected abstract <T extends Comparable<T>> boolean match(final StateHolder<?, ?> ceg, final Property<T> cfg);
        
        public abstract JsonElement toJson();
        
        public String getName() {
            return this.name;
        }
        
        public void checkState(final StateDefinition<?, ?> cef, final Consumer<String> consumer) {
            final Property<?> cfg4 = cef.getProperty(this.name);
            if (cfg4 == null) {
                consumer.accept(this.name);
            }
        }
    }
    
    static class ExactPropertyMatcher extends PropertyMatcher {
        private final String value;
        
        public ExactPropertyMatcher(final String string1, final String string2) {
            super(string1);
            this.value = string2;
        }
        
        @Override
        protected <T extends Comparable<T>> boolean match(final StateHolder<?, ?> ceg, final Property<T> cfg) {
            final T comparable4 = ceg.<T>getValue(cfg);
            final Optional<T> optional5 = cfg.getValue(this.value);
            return optional5.isPresent() && comparable4.compareTo(optional5.get()) == 0;
        }
        
        @Override
        public JsonElement toJson() {
            return (JsonElement)new JsonPrimitive(this.value);
        }
    }
    
    static class RangedPropertyMatcher extends PropertyMatcher {
        @Nullable
        private final String minValue;
        @Nullable
        private final String maxValue;
        
        public RangedPropertyMatcher(final String string1, @Nullable final String string2, @Nullable final String string3) {
            super(string1);
            this.minValue = string2;
            this.maxValue = string3;
        }
        
        @Override
        protected <T extends Comparable<T>> boolean match(final StateHolder<?, ?> ceg, final Property<T> cfg) {
            final T comparable4 = ceg.<T>getValue(cfg);
            if (this.minValue != null) {
                final Optional<T> optional5 = cfg.getValue(this.minValue);
                if (!optional5.isPresent() || comparable4.compareTo(optional5.get()) < 0) {
                    return false;
                }
            }
            if (this.maxValue != null) {
                final Optional<T> optional5 = cfg.getValue(this.maxValue);
                if (!optional5.isPresent() || comparable4.compareTo(optional5.get()) > 0) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public JsonElement toJson() {
            final JsonObject jsonObject2 = new JsonObject();
            if (this.minValue != null) {
                jsonObject2.addProperty("min", this.minValue);
            }
            if (this.maxValue != null) {
                jsonObject2.addProperty("max", this.maxValue);
            }
            return (JsonElement)jsonObject2;
        }
    }
    
    public static class Builder {
        private final List<PropertyMatcher> matchers;
        
        private Builder() {
            this.matchers = (List<PropertyMatcher>)Lists.newArrayList();
        }
        
        public static Builder properties() {
            return new Builder();
        }
        
        public Builder hasProperty(final Property<?> cfg, final String string) {
            this.matchers.add(new ExactPropertyMatcher(cfg.getName(), string));
            return this;
        }
        
        public Builder hasProperty(final Property<Integer> cfg, final int integer) {
            return this.hasProperty(cfg, Integer.toString(integer));
        }
        
        public Builder hasProperty(final Property<Boolean> cfg, final boolean boolean2) {
            return this.hasProperty(cfg, Boolean.toString(boolean2));
        }
        
        public <T extends java.lang.Comparable> Builder hasProperty(final Property<T> cfg, final T comparable) {
            return this.hasProperty(cfg, ((StringRepresentable)comparable).getSerializedName());
        }
        
        public StatePropertiesPredicate build() {
            return new StatePropertiesPredicate(this.matchers, null);
        }
    }
}

package net.minecraft.world.level.block.state;

import javax.annotation.Nullable;
import com.mojang.serialization.Codec;
import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.collect.ArrayTable;
import com.google.common.collect.HashBasedTable;
import java.util.Optional;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.Iterator;
import java.util.Collection;
import com.mojang.serialization.MapCodec;
import com.google.common.collect.Table;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.level.block.state.properties.Property;
import java.util.Map;
import java.util.function.Function;

public abstract class StateHolder<O, S> {
    private static final Function<Map.Entry<Property<?>, Comparable<?>>, String> PROPERTY_ENTRY_TO_STRING_FUNCTION;
    protected final O owner;
    private final ImmutableMap<Property<?>, Comparable<?>> values;
    private Table<Property<?>, Comparable<?>, S> neighbours;
    protected final MapCodec<S> propertiesCodec;
    
    protected StateHolder(final O object, final ImmutableMap<Property<?>, Comparable<?>> immutableMap, final MapCodec<S> mapCodec) {
        this.owner = object;
        this.values = immutableMap;
        this.propertiesCodec = mapCodec;
    }
    
    public <T extends Comparable<T>> S cycle(final Property<T> cfg) {
        return this.<T, Comparable>setValue(cfg, (Comparable)StateHolder.<V>findNextInCollection((java.util.Collection<V>)cfg.getPossibleValues(), (V)this.<T>getValue((Property<T>)cfg)));
    }
    
    protected static <T> T findNextInCollection(final Collection<T> collection, final T object) {
        final Iterator<T> iterator3 = (Iterator<T>)collection.iterator();
        while (iterator3.hasNext()) {
            if (iterator3.next().equals(object)) {
                if (iterator3.hasNext()) {
                    return (T)iterator3.next();
                }
                return (T)collection.iterator().next();
            }
        }
        return (T)iterator3.next();
    }
    
    public String toString() {
        final StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(this.owner);
        if (!this.getValues().isEmpty()) {
            stringBuilder2.append('[');
            stringBuilder2.append((String)this.getValues().entrySet().stream().map((Function)StateHolder.PROPERTY_ENTRY_TO_STRING_FUNCTION).collect(Collectors.joining(",")));
            stringBuilder2.append(']');
        }
        return stringBuilder2.toString();
    }
    
    public Collection<Property<?>> getProperties() {
        return (Collection<Property<?>>)Collections.unmodifiableCollection((Collection)this.values.keySet());
    }
    
    public <T extends Comparable<T>> boolean hasProperty(final Property<T> cfg) {
        return this.values.containsKey(cfg);
    }
    
    public <T extends Comparable<T>> T getValue(final Property<T> cfg) {
        final Comparable<?> comparable3 = this.values.get(cfg);
        if (comparable3 == null) {
            throw new IllegalArgumentException(new StringBuilder().append("Cannot get property ").append(cfg).append(" as it does not exist in ").append(this.owner).toString());
        }
        return (T)cfg.getValueClass().cast(comparable3);
    }
    
    public <T extends Comparable<T>> Optional<T> getOptionalValue(final Property<T> cfg) {
        final Comparable<?> comparable3 = this.values.get(cfg);
        if (comparable3 == null) {
            return (Optional<T>)Optional.empty();
        }
        return (Optional<T>)Optional.of(cfg.getValueClass().cast(comparable3));
    }
    
    public <T extends Comparable<T>, V extends T> S setValue(final Property<T> cfg, final V comparable) {
        final Comparable<?> comparable2 = this.values.get(cfg);
        if (comparable2 == null) {
            throw new IllegalArgumentException(new StringBuilder().append("Cannot set property ").append(cfg).append(" as it does not exist in ").append(this.owner).toString());
        }
        if (comparable2 == comparable) {
            return (S)this;
        }
        final S object5 = (S)this.neighbours.get(cfg, comparable);
        if (object5 == null) {
            throw new IllegalArgumentException(new StringBuilder().append("Cannot set property ").append(cfg).append(" to ").append(comparable).append(" on ").append(this.owner).append(", it is not an allowed value").toString());
        }
        return object5;
    }
    
    public void populateNeighbours(final Map<Map<Property<?>, Comparable<?>>, S> map) {
        if (this.neighbours != null) {
            throw new IllegalStateException();
        }
        final Table<Property<?>, Comparable<?>, S> table3 = (Table<Property<?>, Comparable<?>, S>)HashBasedTable.create();
        for (final Map.Entry<Property<?>, Comparable<?>> entry5 : this.values.entrySet()) {
            final Property<?> cfg6 = entry5.getKey();
            for (final Comparable<?> comparable8 : cfg6.getPossibleValues()) {
                if (comparable8 != entry5.getValue()) {
                    table3.put(cfg6, comparable8, map.get(this.makeNeighbourValues(cfg6, comparable8)));
                }
            }
        }
        this.neighbours = (Table<Property<?>, Comparable<?>, S>)(table3.isEmpty() ? table3 : ArrayTable.create((Table)table3));
    }
    
    private Map<Property<?>, Comparable<?>> makeNeighbourValues(final Property<?> cfg, final Comparable<?> comparable) {
        final Map<Property<?>, Comparable<?>> map4 = (Map<Property<?>, Comparable<?>>)Maps.newHashMap((Map)this.values);
        map4.put(cfg, comparable);
        return map4;
    }
    
    public ImmutableMap<Property<?>, Comparable<?>> getValues() {
        return this.values;
    }
    
    protected static <O, S extends StateHolder<O, S>> Codec<S> codec(final Codec<O> codec, final Function<O, S> function) {
        return (Codec<S>)codec.dispatch("Name", ceg -> ceg.owner, object -> {
            final S ceg3 = (S)function.apply(object);
            if (((StateHolder)ceg3).getValues().isEmpty()) {
                return Codec.unit(ceg3);
            }
            return ((StateHolder)ceg3).propertiesCodec.fieldOf("Properties").codec();
        });
    }
    
    static {
        PROPERTY_ENTRY_TO_STRING_FUNCTION = (Function)new Function<Map.Entry<Property<?>, Comparable<?>>, String>() {
            public String apply(@Nullable final Map.Entry<Property<?>, Comparable<?>> entry) {
                if (entry == null) {
                    return "<NULL>";
                }
                final Property<?> cfg3 = entry.getKey();
                return cfg3.getName() + "=" + this.getName(cfg3, entry.getValue());
            }
            
            private <T extends Comparable<T>> String getName(final Property<T> cfg, final Comparable<?> comparable) {
                return cfg.getName((T)comparable);
            }
        };
    }
}

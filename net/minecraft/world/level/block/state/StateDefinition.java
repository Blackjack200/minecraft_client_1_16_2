package net.minecraft.world.level.block.state;

import com.google.common.collect.ImmutableMap;
import javax.annotation.Nullable;
import java.util.stream.Collectors;
import com.google.common.base.MoreObjects;
import com.mojang.serialization.Codec;
import java.util.Iterator;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Collection;
import java.util.stream.Stream;
import java.util.Collections;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import java.util.function.Supplier;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import java.util.Map;
import java.util.function.Function;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.block.state.properties.Property;
import com.google.common.collect.ImmutableSortedMap;
import java.util.regex.Pattern;

public class StateDefinition<O, S extends StateHolder<O, S>> {
    private static final Pattern NAME_PATTERN;
    private final O owner;
    private final ImmutableSortedMap<String, Property<?>> propertiesByName;
    private final ImmutableList<S> states;
    
    protected StateDefinition(final Function<O, S> function, final O object, final Factory<O, S> b, final Map<String, Property<?>> map) {
        this.owner = object;
        this.propertiesByName = (ImmutableSortedMap<String, Property<?>>)ImmutableSortedMap.copyOf((Map)map);
        final Supplier<S> supplier6 = (Supplier<S>)(() -> (StateHolder)function.apply(object));
        MapCodec<S> mapCodec7 = (MapCodec<S>)MapCodec.of(Encoder.empty(), Decoder.unit((Supplier)supplier6));
        for (final Map.Entry<String, Property<?>> entry9 : this.propertiesByName.entrySet()) {
            mapCodec7 = StateDefinition.<S, Comparable>appendPropertyCodec(mapCodec7, supplier6, (String)entry9.getKey(), (Property<Comparable>)entry9.getValue());
        }
        final MapCodec<S> mapCodec8 = mapCodec7;
        final Map<Map<Property<?>, Comparable<?>>, S> map2 = (Map<Map<Property<?>, Comparable<?>>, S>)Maps.newLinkedHashMap();
        final List<S> list10 = (List<S>)Lists.newArrayList();
        Stream<List<Pair<Property<?>, Comparable<?>>>> stream11 = (Stream<List<Pair<Property<?>, Comparable<?>>>>)Stream.of(Collections.emptyList());
        for (final Property<?> cfg13 : this.propertiesByName.values()) {
            stream11 = (Stream<List<Pair<Property<?>, Comparable<?>>>>)stream11.flatMap(list -> cfg13.getPossibleValues().stream().map(comparable -> {
                final List<Pair<Property<?>, Comparable<?>>> list2 = (List<Pair<Property<?>, Comparable<?>>>)Lists.newArrayList((Iterable)list);
                list2.add(Pair.of((Object)cfg13, (Object)comparable));
                return list2;
            }));
        }
        stream11.forEach(list6 -> {
            final ImmutableMap<Property<?>, Comparable<?>> immutableMap7 = (ImmutableMap<Property<?>, Comparable<?>>)list6.stream().collect(ImmutableMap.toImmutableMap(Pair::getFirst, Pair::getSecond));
            final S ceg8 = b.create(object, immutableMap7, mapCodec8);
            map2.put(immutableMap7, ceg8);
            list10.add(ceg8);
        });
        for (final S ceg13 : list10) {
            ((StateHolder<O, S>)ceg13).populateNeighbours(map2);
        }
        this.states = (ImmutableList<S>)ImmutableList.copyOf((Collection)list10);
    }
    
    private static <S extends StateHolder<?, S>, T extends Comparable<T>> MapCodec<S> appendPropertyCodec(final MapCodec<S> mapCodec, final Supplier<S> supplier, final String string, final Property<T> cfg) {
        return (MapCodec<S>)Codec.mapPair((MapCodec)mapCodec, cfg.valueCodec().fieldOf(string).setPartial(() -> cfg.value((StateHolder)supplier.get()))).xmap(pair -> ((StateHolder)pair.getFirst()).<Comparable, Comparable>setValue(cfg, ((Property.Value)pair.getSecond()).value()), ceg -> Pair.of(ceg, cfg.value(ceg)));
    }
    
    public ImmutableList<S> getPossibleStates() {
        return this.states;
    }
    
    public S any() {
        return (S)this.states.get(0);
    }
    
    public O getOwner() {
        return this.owner;
    }
    
    public Collection<Property<?>> getProperties() {
        return (Collection<Property<?>>)this.propertiesByName.values();
    }
    
    public String toString() {
        return MoreObjects.toStringHelper(this).add("block", this.owner).add("properties", this.propertiesByName.values().stream().map(Property::getName).collect(Collectors.toList())).toString();
    }
    
    @Nullable
    public Property<?> getProperty(final String string) {
        return this.propertiesByName.get(string);
    }
    
    static {
        NAME_PATTERN = Pattern.compile("^[a-z0-9_]+$");
    }
    
    public static class Builder<O, S extends StateHolder<O, S>> {
        private final O owner;
        private final Map<String, Property<?>> properties;
        
        public Builder(final O object) {
            this.properties = (Map<String, Property<?>>)Maps.newHashMap();
            this.owner = object;
        }
        
        public Builder<O, S> add(final Property<?>... arr) {
            for (final Property<?> cfg6 : arr) {
                this.validateProperty(cfg6);
                this.properties.put(cfg6.getName(), cfg6);
            }
            return this;
        }
        
        private <T extends Comparable<T>> void validateProperty(final Property<T> cfg) {
            final String string3 = cfg.getName();
            if (!StateDefinition.NAME_PATTERN.matcher((CharSequence)string3).matches()) {
                throw new IllegalArgumentException(new StringBuilder().append(this.owner).append(" has invalidly named property: ").append(string3).toString());
            }
            final Collection<T> collection4 = cfg.getPossibleValues();
            if (collection4.size() <= 1) {
                throw new IllegalArgumentException(new StringBuilder().append(this.owner).append(" attempted use property ").append(string3).append(" with <= 1 possible values").toString());
            }
            for (final T comparable6 : collection4) {
                final String string4 = cfg.getName(comparable6);
                if (!StateDefinition.NAME_PATTERN.matcher((CharSequence)string4).matches()) {
                    throw new IllegalArgumentException(new StringBuilder().append(this.owner).append(" has property: ").append(string3).append(" with invalidly named value: ").append(string4).toString());
                }
            }
            if (this.properties.containsKey(string3)) {
                throw new IllegalArgumentException(new StringBuilder().append(this.owner).append(" has duplicate property: ").append(string3).toString());
            }
        }
        
        public StateDefinition<O, S> create(final Function<O, S> function, final Factory<O, S> b) {
            return new StateDefinition<O, S>(function, this.owner, b, this.properties);
        }
    }
    
    public interface Factory<O, S> {
        S create(final O object, final ImmutableMap<Property<?>, Comparable<?>> immutableMap, final MapCodec<S> mapCodec);
    }
}

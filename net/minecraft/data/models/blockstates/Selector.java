package net.minecraft.data.models.blockstates;

import java.util.stream.Collectors;
import com.google.common.collect.ImmutableList;
import java.util.List;
import net.minecraft.world.level.block.state.properties.Property;
import java.util.Comparator;

public final class Selector {
    private static final Selector EMPTY;
    private static final Comparator<Property.Value<?>> COMPARE_BY_NAME;
    private final List<Property.Value<?>> values;
    
    public Selector extend(final Property.Value<?> a) {
        return new Selector((List<Property.Value<?>>)ImmutableList.builder().addAll((Iterable)this.values).add(a).build());
    }
    
    public Selector extend(final Selector iq) {
        return new Selector((List<Property.Value<?>>)ImmutableList.builder().addAll((Iterable)this.values).addAll((Iterable)iq.values).build());
    }
    
    private Selector(final List<Property.Value<?>> list) {
        this.values = list;
    }
    
    public static Selector empty() {
        return Selector.EMPTY;
    }
    
    public static Selector of(final Property.Value<?>... arr) {
        return new Selector((List<Property.Value<?>>)ImmutableList.copyOf((Object[])arr));
    }
    
    public boolean equals(final Object object) {
        return this == object || (object instanceof Selector && this.values.equals(((Selector)object).values));
    }
    
    public int hashCode() {
        return this.values.hashCode();
    }
    
    public String getKey() {
        return (String)this.values.stream().sorted((Comparator)Selector.COMPARE_BY_NAME).map(Property.Value::toString).collect(Collectors.joining(","));
    }
    
    public String toString() {
        return this.getKey();
    }
    
    static {
        EMPTY = new Selector((List<Property.Value<?>>)ImmutableList.of());
        COMPARE_BY_NAME = Comparator.comparing(a -> a.getProperty().getName());
    }
}

package net.minecraft.world.level.block.state.properties;

import com.google.common.collect.Lists;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.function.Predicate;
import com.google.common.base.Predicates;
import java.util.Optional;
import java.util.Iterator;
import net.minecraft.util.StringRepresentable;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import com.google.common.collect.ImmutableSet;

public class EnumProperty<T extends Enum> extends Property<T> {
    private final ImmutableSet<T> values;
    private final Map<String, T> names;
    
    protected EnumProperty(final String string, final Class<T> class2, final Collection<T> collection) {
        super(string, class2);
        this.names = (Map<String, T>)Maps.newHashMap();
        this.values = (ImmutableSet<T>)ImmutableSet.copyOf((Collection)collection);
        for (final T enum6 : collection) {
            final String string2 = ((StringRepresentable)enum6).getSerializedName();
            if (this.names.containsKey(string2)) {
                throw new IllegalArgumentException("Multiple values have the same name '" + string2 + "'");
            }
            this.names.put(string2, enum6);
        }
    }
    
    @Override
    public Collection<T> getPossibleValues() {
        return (Collection<T>)this.values;
    }
    
    @Override
    public Optional<T> getValue(final String string) {
        return (Optional<T>)Optional.ofNullable(this.names.get(string));
    }
    
    @Override
    public String getName(final T enum1) {
        return ((StringRepresentable)enum1).getSerializedName();
    }
    
    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof EnumProperty && super.equals(object)) {
            final EnumProperty<?> cfb3 = object;
            return this.values.equals(cfb3.values) && this.names.equals(cfb3.names);
        }
        return false;
    }
    
    @Override
    public int generateHashCode() {
        int integer2 = super.generateHashCode();
        integer2 = 31 * integer2 + this.values.hashCode();
        integer2 = 31 * integer2 + this.names.hashCode();
        return integer2;
    }
    
    public static <T extends java.lang.Enum> EnumProperty<T> create(final String string, final Class<T> class2) {
        return EnumProperty.<T>create(string, class2, (java.util.function.Predicate<T>)Predicates.alwaysTrue());
    }
    
    public static <T extends java.lang.Enum> EnumProperty<T> create(final String string, final Class<T> class2, final Predicate<T> predicate) {
        return EnumProperty.<T>create(string, class2, (java.util.Collection<T>)Arrays.stream(class2.getEnumConstants()).filter((Predicate)predicate).collect(Collectors.toList()));
    }
    
    public static <T extends java.lang.Enum> EnumProperty<T> create(final String string, final Class<T> class2, final T... arr) {
        return EnumProperty.<T>create(string, class2, (java.util.Collection<T>)Lists.newArrayList((Object[])arr));
    }
    
    public static <T extends java.lang.Enum> EnumProperty<T> create(final String string, final Class<T> class2, final Collection<T> collection) {
        return new EnumProperty<T>(string, class2, collection);
    }
}

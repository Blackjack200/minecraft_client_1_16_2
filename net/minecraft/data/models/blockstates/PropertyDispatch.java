package net.minecraft.data.models.blockstates;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.Collections;
import com.google.common.collect.ImmutableList;
import java.util.Iterator;
import java.util.stream.Collectors;
import net.minecraft.world.level.block.state.properties.Property;
import java.util.stream.Stream;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;

public abstract class PropertyDispatch {
    private final Map<Selector, List<Variant>> values;
    
    public PropertyDispatch() {
        this.values = (Map<Selector, List<Variant>>)Maps.newHashMap();
    }
    
    protected void putValue(final Selector iq, final List<Variant> list) {
        final List<Variant> list2 = (List<Variant>)this.values.put(iq, list);
        if (list2 != null) {
            throw new IllegalStateException(new StringBuilder().append("Value ").append(iq).append(" is already defined").toString());
        }
    }
    
    Map<Selector, List<Variant>> getEntries() {
        this.verifyComplete();
        return (Map<Selector, List<Variant>>)ImmutableMap.copyOf((Map)this.values);
    }
    
    private void verifyComplete() {
        final List<Property<?>> list2 = this.getDefinedProperties();
        Stream<Selector> stream3 = (Stream<Selector>)Stream.of(Selector.empty());
        for (final Property<?> cfg5 : list2) {
            stream3 = (Stream<Selector>)stream3.flatMap(iq -> cfg5.getAllValues().map(iq::extend));
        }
        final List<Selector> list3 = (List<Selector>)stream3.filter(iq -> !this.values.containsKey(iq)).collect(Collectors.toList());
        if (!list3.isEmpty()) {
            throw new IllegalStateException(new StringBuilder().append("Missing definition for properties: ").append(list3).toString());
        }
    }
    
    abstract List<Property<?>> getDefinedProperties();
    
    public static <T1 extends Comparable<T1>> C1<T1> property(final Property<T1> cfg) {
        return new C1<T1>((Property)cfg);
    }
    
    public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>> C2<T1, T2> properties(final Property<T1> cfg1, final Property<T2> cfg2) {
        return new C2<T1, T2>((Property)cfg1, (Property)cfg2);
    }
    
    public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>> C3<T1, T2, T3> properties(final Property<T1> cfg1, final Property<T2> cfg2, final Property<T3> cfg3) {
        return new C3<T1, T2, T3>((Property)cfg1, (Property)cfg2, (Property)cfg3);
    }
    
    public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>> C4<T1, T2, T3, T4> properties(final Property<T1> cfg1, final Property<T2> cfg2, final Property<T3> cfg3, final Property<T4> cfg4) {
        return new C4<T1, T2, T3, T4>((Property)cfg1, (Property)cfg2, (Property)cfg3, (Property)cfg4);
    }
    
    public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>, T5 extends Comparable<T5>> C5<T1, T2, T3, T4, T5> properties(final Property<T1> cfg1, final Property<T2> cfg2, final Property<T3> cfg3, final Property<T4> cfg4, final Property<T5> cfg5) {
        return new C5<T1, T2, T3, T4, T5>((Property)cfg1, (Property)cfg2, (Property)cfg3, (Property)cfg4, (Property)cfg5);
    }
    
    public static class C1<T1 extends Comparable<T1>> extends PropertyDispatch {
        private final Property<T1> property1;
        
        private C1(final Property<T1> cfg) {
            this.property1 = cfg;
        }
        
        public List<Property<?>> getDefinedProperties() {
            return (List<Property<?>>)ImmutableList.of(this.property1);
        }
        
        public C1<T1> select(final T1 comparable, final List<Variant> list) {
            final Selector iq4 = Selector.of(this.property1.value(comparable));
            this.putValue(iq4, list);
            return this;
        }
        
        public C1<T1> select(final T1 comparable, final Variant ir) {
            return this.select(comparable, (List<Variant>)Collections.singletonList(ir));
        }
        
        public PropertyDispatch generate(final Function<T1, Variant> function) {
            this.property1.getPossibleValues().forEach(comparable -> this.select(comparable, (Variant)function.apply(comparable)));
            return this;
        }
    }
    
    public static class C2<T1 extends Comparable<T1>, T2 extends Comparable<T2>> extends PropertyDispatch {
        private final Property<T1> property1;
        private final Property<T2> property2;
        
        private C2(final Property<T1> cfg1, final Property<T2> cfg2) {
            this.property1 = cfg1;
            this.property2 = cfg2;
        }
        
        public List<Property<?>> getDefinedProperties() {
            return (List<Property<?>>)ImmutableList.of(this.property1, this.property2);
        }
        
        public C2<T1, T2> select(final T1 comparable1, final T2 comparable2, final List<Variant> list) {
            final Selector iq5 = Selector.of(this.property1.value(comparable1), this.property2.value(comparable2));
            this.putValue(iq5, list);
            return this;
        }
        
        public C2<T1, T2> select(final T1 comparable1, final T2 comparable2, final Variant ir) {
            return this.select(comparable1, comparable2, (List<Variant>)Collections.singletonList(ir));
        }
        
        public PropertyDispatch generate(final BiFunction<T1, T2, Variant> biFunction) {
            this.property1.getPossibleValues().forEach(comparable -> this.property2.getPossibleValues().forEach(comparable3 -> this.select(comparable, comparable3, (Variant)biFunction.apply(comparable, comparable3))));
            return this;
        }
        
        public PropertyDispatch generateList(final BiFunction<T1, T2, List<Variant>> biFunction) {
            this.property1.getPossibleValues().forEach(comparable -> this.property2.getPossibleValues().forEach(comparable3 -> this.select(comparable, comparable3, (List<Variant>)biFunction.apply(comparable, comparable3))));
            return this;
        }
    }
    
    public static class C3<T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>> extends PropertyDispatch {
        private final Property<T1> property1;
        private final Property<T2> property2;
        private final Property<T3> property3;
        
        private C3(final Property<T1> cfg1, final Property<T2> cfg2, final Property<T3> cfg3) {
            this.property1 = cfg1;
            this.property2 = cfg2;
            this.property3 = cfg3;
        }
        
        public List<Property<?>> getDefinedProperties() {
            return (List<Property<?>>)ImmutableList.of(this.property1, this.property2, this.property3);
        }
        
        public C3<T1, T2, T3> select(final T1 comparable1, final T2 comparable2, final T3 comparable3, final List<Variant> list) {
            final Selector iq6 = Selector.of(this.property1.value(comparable1), this.property2.value(comparable2), this.property3.value(comparable3));
            this.putValue(iq6, list);
            return this;
        }
        
        public C3<T1, T2, T3> select(final T1 comparable1, final T2 comparable2, final T3 comparable3, final Variant ir) {
            return this.select(comparable1, comparable2, comparable3, (List<Variant>)Collections.singletonList(ir));
        }
        
        public PropertyDispatch generate(final TriFunction<T1, T2, T3, Variant> h) {
            this.property1.getPossibleValues().forEach(comparable -> this.property2.getPossibleValues().forEach(comparable3 -> this.property3.getPossibleValues().forEach(comparable4 -> this.select(comparable, comparable3, comparable4, h.apply(comparable, comparable3, comparable4)))));
            return this;
        }
    }
    
    public static class C4<T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>> extends PropertyDispatch {
        private final Property<T1> property1;
        private final Property<T2> property2;
        private final Property<T3> property3;
        private final Property<T4> property4;
        
        private C4(final Property<T1> cfg1, final Property<T2> cfg2, final Property<T3> cfg3, final Property<T4> cfg4) {
            this.property1 = cfg1;
            this.property2 = cfg2;
            this.property3 = cfg3;
            this.property4 = cfg4;
        }
        
        public List<Property<?>> getDefinedProperties() {
            return (List<Property<?>>)ImmutableList.of(this.property1, this.property2, this.property3, this.property4);
        }
        
        public C4<T1, T2, T3, T4> select(final T1 comparable1, final T2 comparable2, final T3 comparable3, final T4 comparable4, final List<Variant> list) {
            final Selector iq7 = Selector.of(this.property1.value(comparable1), this.property2.value(comparable2), this.property3.value(comparable3), this.property4.value(comparable4));
            this.putValue(iq7, list);
            return this;
        }
        
        public C4<T1, T2, T3, T4> select(final T1 comparable1, final T2 comparable2, final T3 comparable3, final T4 comparable4, final Variant ir) {
            return this.select(comparable1, comparable2, comparable3, comparable4, (List<Variant>)Collections.singletonList(ir));
        }
    }
    
    public static class C5<T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>, T5 extends Comparable<T5>> extends PropertyDispatch {
        private final Property<T1> property1;
        private final Property<T2> property2;
        private final Property<T3> property3;
        private final Property<T4> property4;
        private final Property<T5> property5;
        
        private C5(final Property<T1> cfg1, final Property<T2> cfg2, final Property<T3> cfg3, final Property<T4> cfg4, final Property<T5> cfg5) {
            this.property1 = cfg1;
            this.property2 = cfg2;
            this.property3 = cfg3;
            this.property4 = cfg4;
            this.property5 = cfg5;
        }
        
        public List<Property<?>> getDefinedProperties() {
            return (List<Property<?>>)ImmutableList.of(this.property1, this.property2, this.property3, this.property4, this.property5);
        }
        
        public C5<T1, T2, T3, T4, T5> select(final T1 comparable1, final T2 comparable2, final T3 comparable3, final T4 comparable4, final T5 comparable5, final List<Variant> list) {
            final Selector iq8 = Selector.of(this.property1.value(comparable1), this.property2.value(comparable2), this.property3.value(comparable3), this.property4.value(comparable4), this.property5.value(comparable5));
            this.putValue(iq8, list);
            return this;
        }
        
        public C5<T1, T2, T3, T4, T5> select(final T1 comparable1, final T2 comparable2, final T3 comparable3, final T4 comparable4, final T5 comparable5, final Variant ir) {
            return this.select(comparable1, comparable2, comparable3, comparable4, comparable5, (List<Variant>)Collections.singletonList(ir));
        }
    }
    
    @FunctionalInterface
    public interface TriFunction<P1, P2, P3, R> {
        R apply(final P1 object1, final P2 object2, final P3 object3);
    }
}

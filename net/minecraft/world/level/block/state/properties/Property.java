package net.minecraft.world.level.block.state.properties;

import com.mojang.serialization.DataResult;
import com.google.common.base.MoreObjects;
import java.util.Optional;
import java.util.Collection;
import java.util.stream.Stream;
import net.minecraft.world.level.block.state.StateHolder;
import com.mojang.serialization.Codec;

public abstract class Property<T extends Comparable<T>> {
    private final Class<T> clazz;
    private final String name;
    private Integer hashCode;
    private final Codec<T> codec;
    private final Codec<Value<T>> valueCodec;
    
    protected Property(final String string, final Class<T> class2) {
        this.codec = (Codec<T>)Codec.STRING.comapFlatMap(string -> (DataResult)this.getValue(string).map(DataResult::success).orElseGet(() -> DataResult.error(new StringBuilder().append("Unable to read property: ").append(this).append(" with value: ").append(string).toString())), this::getName);
        this.valueCodec = (Codec<Value<T>>)this.codec.xmap(this::value, Value::value);
        this.clazz = class2;
        this.name = string;
    }
    
    public Value<T> value(final T comparable) {
        return new Value<T>(this, (Comparable)comparable);
    }
    
    public Value<T> value(final StateHolder<?, ?> ceg) {
        return new Value<T>(this, ceg.<Comparable>getValue((Property<Comparable>)this));
    }
    
    public Stream<Value<T>> getAllValues() {
        return (Stream<Value<T>>)this.getPossibleValues().stream().map(this::value);
    }
    
    public Codec<Value<T>> valueCodec() {
        return this.valueCodec;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Class<T> getValueClass() {
        return this.clazz;
    }
    
    public abstract Collection<T> getPossibleValues();
    
    public abstract String getName(final T comparable);
    
    public abstract Optional<T> getValue(final String string);
    
    public String toString() {
        return MoreObjects.toStringHelper(this).add("name", this.name).add("clazz", this.clazz).add("values", this.getPossibleValues()).toString();
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof Property) {
            final Property<?> cfg3 = object;
            return this.clazz.equals(cfg3.clazz) && this.name.equals(cfg3.name);
        }
        return false;
    }
    
    public final int hashCode() {
        if (this.hashCode == null) {
            this.hashCode = this.generateHashCode();
        }
        return this.hashCode;
    }
    
    public int generateHashCode() {
        return 31 * this.clazz.hashCode() + this.name.hashCode();
    }
    
    public static final class Value<T extends Comparable<T>> {
        private final Property<T> property;
        private final T value;
        
        private Value(final Property<T> cfg, final T comparable) {
            if (!cfg.getPossibleValues().contains(comparable)) {
                throw new IllegalArgumentException(new StringBuilder().append("Value ").append(comparable).append(" does not belong to property ").append(cfg).toString());
            }
            this.property = cfg;
            this.value = comparable;
        }
        
        public Property<T> getProperty() {
            return this.property;
        }
        
        public T value() {
            return this.value;
        }
        
        public String toString() {
            return this.property.getName() + "=" + this.property.getName(this.value);
        }
        
        public boolean equals(final Object object) {
            if (this == object) {
                return true;
            }
            if (!(object instanceof Value)) {
                return false;
            }
            final Value<?> a3 = object;
            return this.property == a3.property && this.value.equals(a3.value);
        }
        
        public int hashCode() {
            int integer2 = this.property.hashCode();
            integer2 = 31 * integer2 + this.value.hashCode();
            return integer2;
        }
    }
}

package net.minecraft.world.level.block.state.properties;

import java.util.Optional;
import java.util.Set;
import java.util.Collection;
import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableSet;

public class IntegerProperty extends Property<Integer> {
    private final ImmutableSet<Integer> values;
    
    protected IntegerProperty(final String string, final int integer2, final int integer3) {
        super(string, Integer.class);
        if (integer2 < 0) {
            throw new IllegalArgumentException("Min value of " + string + " must be 0 or greater");
        }
        if (integer3 <= integer2) {
            throw new IllegalArgumentException("Max value of " + string + " must be greater than min (" + integer2 + ")");
        }
        final Set<Integer> set5 = (Set<Integer>)Sets.newHashSet();
        for (int integer4 = integer2; integer4 <= integer3; ++integer4) {
            set5.add(integer4);
        }
        this.values = (ImmutableSet<Integer>)ImmutableSet.copyOf((Collection)set5);
    }
    
    @Override
    public Collection<Integer> getPossibleValues() {
        return (Collection<Integer>)this.values;
    }
    
    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof IntegerProperty && super.equals(object)) {
            final IntegerProperty cfd3 = (IntegerProperty)object;
            return this.values.equals(cfd3.values);
        }
        return false;
    }
    
    @Override
    public int generateHashCode() {
        return 31 * super.generateHashCode() + this.values.hashCode();
    }
    
    public static IntegerProperty create(final String string, final int integer2, final int integer3) {
        return new IntegerProperty(string, integer2, integer3);
    }
    
    @Override
    public Optional<Integer> getValue(final String string) {
        try {
            final Integer integer3 = Integer.valueOf(string);
            return (Optional<Integer>)(this.values.contains(integer3) ? Optional.of(integer3) : Optional.empty());
        }
        catch (NumberFormatException numberFormatException3) {
            return (Optional<Integer>)Optional.empty();
        }
    }
    
    @Override
    public String getName(final Integer integer) {
        return integer.toString();
    }
}

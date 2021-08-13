package net.minecraft.world.entity.ai.attributes;

import org.apache.logging.log4j.LogManager;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import java.util.Objects;
import java.util.Random;
import net.minecraft.util.Mth;
import io.netty.util.internal.ThreadLocalRandom;
import java.util.UUID;
import java.util.function.Supplier;
import org.apache.logging.log4j.Logger;

public class AttributeModifier {
    private static final Logger LOGGER;
    private final double amount;
    private final Operation operation;
    private final Supplier<String> nameGetter;
    private final UUID id;
    
    public AttributeModifier(final String string, final double double2, final Operation a) {
        this(Mth.createInsecureUUID((Random)ThreadLocalRandom.current()), (Supplier<String>)(() -> string), double2, a);
    }
    
    public AttributeModifier(final UUID uUID, final String string, final double double3, final Operation a) {
        this(uUID, (Supplier<String>)(() -> string), double3, a);
    }
    
    public AttributeModifier(final UUID uUID, final Supplier<String> supplier, final double double3, final Operation a) {
        this.id = uUID;
        this.nameGetter = supplier;
        this.amount = double3;
        this.operation = a;
    }
    
    public UUID getId() {
        return this.id;
    }
    
    public String getName() {
        return (String)this.nameGetter.get();
    }
    
    public Operation getOperation() {
        return this.operation;
    }
    
    public double getAmount() {
        return this.amount;
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        final AttributeModifier arg3 = (AttributeModifier)object;
        return Objects.equals(this.id, arg3.id);
    }
    
    public int hashCode() {
        return this.id.hashCode();
    }
    
    public String toString() {
        return new StringBuilder().append("AttributeModifier{amount=").append(this.amount).append(", operation=").append(this.operation).append(", name='").append((String)this.nameGetter.get()).append('\'').append(", id=").append(this.id).append('}').toString();
    }
    
    public CompoundTag save() {
        final CompoundTag md2 = new CompoundTag();
        md2.putString("Name", this.getName());
        md2.putDouble("Amount", this.amount);
        md2.putInt("Operation", this.operation.toValue());
        md2.putUUID("UUID", this.id);
        return md2;
    }
    
    @Nullable
    public static AttributeModifier load(final CompoundTag md) {
        try {
            final UUID uUID2 = md.getUUID("UUID");
            final Operation a3 = Operation.fromValue(md.getInt("Operation"));
            return new AttributeModifier(uUID2, md.getString("Name"), md.getDouble("Amount"), a3);
        }
        catch (Exception exception2) {
            AttributeModifier.LOGGER.warn("Unable to create attribute: {}", exception2.getMessage());
            return null;
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public enum Operation {
        ADDITION(0), 
        MULTIPLY_BASE(1), 
        MULTIPLY_TOTAL(2);
        
        private static final Operation[] OPERATIONS;
        private final int value;
        
        private Operation(final int integer3) {
            this.value = integer3;
        }
        
        public int toValue() {
            return this.value;
        }
        
        public static Operation fromValue(final int integer) {
            if (integer < 0 || integer >= Operation.OPERATIONS.length) {
                throw new IllegalArgumentException(new StringBuilder().append("No operation with value ").append(integer).toString());
            }
            return Operation.OPERATIONS[integer];
        }
        
        static {
            OPERATIONS = new Operation[] { Operation.ADDITION, Operation.MULTIPLY_BASE, Operation.MULTIPLY_TOTAL };
        }
    }
}

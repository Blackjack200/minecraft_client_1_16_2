package net.minecraft.world.entity.ai.attributes;

import com.google.common.collect.Maps;
import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.UUID;
import net.minecraft.core.Registry;
import com.google.common.collect.ImmutableMap;
import java.util.Map;

public class AttributeSupplier {
    private final Map<Attribute, AttributeInstance> instances;
    
    public AttributeSupplier(final Map<Attribute, AttributeInstance> map) {
        this.instances = (Map<Attribute, AttributeInstance>)ImmutableMap.copyOf((Map)map);
    }
    
    private AttributeInstance getAttributeInstance(final Attribute ard) {
        final AttributeInstance are3 = (AttributeInstance)this.instances.get(ard);
        if (are3 == null) {
            throw new IllegalArgumentException(new StringBuilder().append("Can't find attribute ").append(Registry.ATTRIBUTE.getKey(ard)).toString());
        }
        return are3;
    }
    
    public double getValue(final Attribute ard) {
        return this.getAttributeInstance(ard).getValue();
    }
    
    public double getBaseValue(final Attribute ard) {
        return this.getAttributeInstance(ard).getBaseValue();
    }
    
    public double getModifierValue(final Attribute ard, final UUID uUID) {
        final AttributeModifier arg4 = this.getAttributeInstance(ard).getModifier(uUID);
        if (arg4 == null) {
            throw new IllegalArgumentException(new StringBuilder().append("Can't find modifier ").append(uUID).append(" on attribute ").append(Registry.ATTRIBUTE.getKey(ard)).toString());
        }
        return arg4.getAmount();
    }
    
    @Nullable
    public AttributeInstance createInstance(final Consumer<AttributeInstance> consumer, final Attribute ard) {
        final AttributeInstance are4 = (AttributeInstance)this.instances.get(ard);
        if (are4 == null) {
            return null;
        }
        final AttributeInstance are5 = new AttributeInstance(ard, consumer);
        are5.replaceFrom(are4);
        return are5;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public boolean hasAttribute(final Attribute ard) {
        return this.instances.containsKey(ard);
    }
    
    public boolean hasModifier(final Attribute ard, final UUID uUID) {
        final AttributeInstance are4 = (AttributeInstance)this.instances.get(ard);
        return are4 != null && are4.getModifier(uUID) != null;
    }
    
    public static class Builder {
        private final Map<Attribute, AttributeInstance> builder;
        private boolean instanceFrozen;
        
        public Builder() {
            this.builder = (Map<Attribute, AttributeInstance>)Maps.newHashMap();
        }
        
        private AttributeInstance create(final Attribute ard) {
            final AttributeInstance are3 = new AttributeInstance(ard, (Consumer<AttributeInstance>)(are -> {
                if (this.instanceFrozen) {
                    throw new UnsupportedOperationException(new StringBuilder().append("Tried to change value for default attribute instance: ").append(Registry.ATTRIBUTE.getKey(ard)).toString());
                }
            }));
            this.builder.put(ard, are3);
            return are3;
        }
        
        public Builder add(final Attribute ard) {
            this.create(ard);
            return this;
        }
        
        public Builder add(final Attribute ard, final double double2) {
            final AttributeInstance are5 = this.create(ard);
            are5.setBaseValue(double2);
            return this;
        }
        
        public AttributeSupplier build() {
            this.instanceFrozen = true;
            return new AttributeSupplier(this.builder);
        }
    }
}

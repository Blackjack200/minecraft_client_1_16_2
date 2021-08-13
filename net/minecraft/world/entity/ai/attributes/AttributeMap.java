package net.minecraft.world.entity.ai.attributes;

import org.apache.logging.log4j.LogManager;
import net.minecraft.nbt.CompoundTag;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import java.util.Iterator;
import net.minecraft.nbt.ListTag;
import com.google.common.collect.Multimap;
import java.util.UUID;
import javax.annotation.Nullable;
import java.util.stream.Collectors;
import java.util.Collection;
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import java.util.Set;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class AttributeMap {
    private static final Logger LOGGER;
    private final Map<Attribute, AttributeInstance> attributes;
    private final Set<AttributeInstance> dirtyAttributes;
    private final AttributeSupplier supplier;
    
    public AttributeMap(final AttributeSupplier arh) {
        this.attributes = (Map<Attribute, AttributeInstance>)Maps.newHashMap();
        this.dirtyAttributes = (Set<AttributeInstance>)Sets.newHashSet();
        this.supplier = arh;
    }
    
    private void onAttributeModified(final AttributeInstance are) {
        if (are.getAttribute().isClientSyncable()) {
            this.dirtyAttributes.add(are);
        }
    }
    
    public Set<AttributeInstance> getDirtyAttributes() {
        return this.dirtyAttributes;
    }
    
    public Collection<AttributeInstance> getSyncableAttributes() {
        return (Collection<AttributeInstance>)this.attributes.values().stream().filter(are -> are.getAttribute().isClientSyncable()).collect(Collectors.toList());
    }
    
    @Nullable
    public AttributeInstance getInstance(final Attribute ard) {
        return (AttributeInstance)this.attributes.computeIfAbsent(ard, ard -> this.supplier.createInstance((Consumer<AttributeInstance>)this::onAttributeModified, ard));
    }
    
    public boolean hasAttribute(final Attribute ard) {
        return this.attributes.get(ard) != null || this.supplier.hasAttribute(ard);
    }
    
    public boolean hasModifier(final Attribute ard, final UUID uUID) {
        final AttributeInstance are4 = (AttributeInstance)this.attributes.get(ard);
        return (are4 != null) ? (are4.getModifier(uUID) != null) : this.supplier.hasModifier(ard, uUID);
    }
    
    public double getValue(final Attribute ard) {
        final AttributeInstance are3 = (AttributeInstance)this.attributes.get(ard);
        return (are3 != null) ? are3.getValue() : this.supplier.getValue(ard);
    }
    
    public double getBaseValue(final Attribute ard) {
        final AttributeInstance are3 = (AttributeInstance)this.attributes.get(ard);
        return (are3 != null) ? are3.getBaseValue() : this.supplier.getBaseValue(ard);
    }
    
    public double getModifierValue(final Attribute ard, final UUID uUID) {
        final AttributeInstance are4 = (AttributeInstance)this.attributes.get(ard);
        return (are4 != null) ? are4.getModifier(uUID).getAmount() : this.supplier.getModifierValue(ard, uUID);
    }
    
    public void removeAttributeModifiers(final Multimap<Attribute, AttributeModifier> multimap) {
        multimap.asMap().forEach((ard, collection) -> {
            final AttributeInstance are4 = (AttributeInstance)this.attributes.get(ard);
            if (are4 != null) {
                collection.forEach(are4::removeModifier);
            }
        });
    }
    
    public void addTransientAttributeModifiers(final Multimap<Attribute, AttributeModifier> multimap) {
        multimap.forEach((ard, arg) -> {
            final AttributeInstance are4 = this.getInstance(ard);
            if (are4 != null) {
                are4.removeModifier(arg);
                are4.addTransientModifier(arg);
            }
        });
    }
    
    public void assignValues(final AttributeMap arf) {
        arf.attributes.values().forEach(are -> {
            final AttributeInstance are2 = this.getInstance(are.getAttribute());
            if (are2 != null) {
                are2.replaceFrom(are);
            }
        });
    }
    
    public ListTag save() {
        final ListTag mj2 = new ListTag();
        for (final AttributeInstance are4 : this.attributes.values()) {
            mj2.add(are4.save());
        }
        return mj2;
    }
    
    public void load(final ListTag mj) {
        for (int integer3 = 0; integer3 < mj.size(); ++integer3) {
            final CompoundTag md4 = mj.getCompound(integer3);
            final String string5 = md4.getString("Name");
            Util.<Attribute>ifElse(Registry.ATTRIBUTE.getOptional(ResourceLocation.tryParse(string5)), (java.util.function.Consumer<Attribute>)(ard -> {
                final AttributeInstance are4 = this.getInstance(ard);
                if (are4 != null) {
                    are4.load(md4);
                }
            }), () -> AttributeMap.LOGGER.warn("Ignoring unknown attribute '{}'", string5));
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}

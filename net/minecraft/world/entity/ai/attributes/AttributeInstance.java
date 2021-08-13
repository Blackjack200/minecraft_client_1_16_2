package net.minecraft.world.entity.ai.attributes;

import com.google.common.collect.Sets;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import java.util.Collections;
import java.util.Collection;
import java.util.Iterator;
import javax.annotation.Nullable;
import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import com.google.common.collect.Maps;
import java.util.function.Consumer;
import java.util.UUID;
import java.util.Set;
import java.util.Map;

public class AttributeInstance {
    private final Attribute attribute;
    private final Map<AttributeModifier.Operation, Set<AttributeModifier>> modifiersByOperation;
    private final Map<UUID, AttributeModifier> modifierById;
    private final Set<AttributeModifier> permanentModifiers;
    private double baseValue;
    private boolean dirty;
    private double cachedValue;
    private final Consumer<AttributeInstance> onDirty;
    
    public AttributeInstance(final Attribute ard, final Consumer<AttributeInstance> consumer) {
        this.modifiersByOperation = (Map<AttributeModifier.Operation, Set<AttributeModifier>>)Maps.newEnumMap((Class)AttributeModifier.Operation.class);
        this.modifierById = (Map<UUID, AttributeModifier>)new Object2ObjectArrayMap();
        this.permanentModifiers = (Set<AttributeModifier>)new ObjectArraySet();
        this.dirty = true;
        this.attribute = ard;
        this.onDirty = consumer;
        this.baseValue = ard.getDefaultValue();
    }
    
    public Attribute getAttribute() {
        return this.attribute;
    }
    
    public double getBaseValue() {
        return this.baseValue;
    }
    
    public void setBaseValue(final double double1) {
        if (double1 == this.baseValue) {
            return;
        }
        this.baseValue = double1;
        this.setDirty();
    }
    
    public Set<AttributeModifier> getModifiers(final AttributeModifier.Operation a) {
        return (Set<AttributeModifier>)this.modifiersByOperation.computeIfAbsent(a, a -> Sets.newHashSet());
    }
    
    public Set<AttributeModifier> getModifiers() {
        return (Set<AttributeModifier>)ImmutableSet.copyOf(this.modifierById.values());
    }
    
    @Nullable
    public AttributeModifier getModifier(final UUID uUID) {
        return (AttributeModifier)this.modifierById.get(uUID);
    }
    
    public boolean hasModifier(final AttributeModifier arg) {
        return this.modifierById.get(arg.getId()) != null;
    }
    
    private void addModifier(final AttributeModifier arg) {
        final AttributeModifier arg2 = (AttributeModifier)this.modifierById.putIfAbsent(arg.getId(), arg);
        if (arg2 != null) {
            throw new IllegalArgumentException("Modifier is already applied on this attribute!");
        }
        this.getModifiers(arg.getOperation()).add(arg);
        this.setDirty();
    }
    
    public void addTransientModifier(final AttributeModifier arg) {
        this.addModifier(arg);
    }
    
    public void addPermanentModifier(final AttributeModifier arg) {
        this.addModifier(arg);
        this.permanentModifiers.add(arg);
    }
    
    protected void setDirty() {
        this.dirty = true;
        this.onDirty.accept(this);
    }
    
    public void removeModifier(final AttributeModifier arg) {
        this.getModifiers(arg.getOperation()).remove(arg);
        this.modifierById.remove(arg.getId());
        this.permanentModifiers.remove(arg);
        this.setDirty();
    }
    
    public void removeModifier(final UUID uUID) {
        final AttributeModifier arg3 = this.getModifier(uUID);
        if (arg3 != null) {
            this.removeModifier(arg3);
        }
    }
    
    public boolean removePermanentModifier(final UUID uUID) {
        final AttributeModifier arg3 = this.getModifier(uUID);
        if (arg3 != null && this.permanentModifiers.contains(arg3)) {
            this.removeModifier(arg3);
            return true;
        }
        return false;
    }
    
    public void removeModifiers() {
        for (final AttributeModifier arg3 : this.getModifiers()) {
            this.removeModifier(arg3);
        }
    }
    
    public double getValue() {
        if (this.dirty) {
            this.cachedValue = this.calculateValue();
            this.dirty = false;
        }
        return this.cachedValue;
    }
    
    private double calculateValue() {
        double double2 = this.getBaseValue();
        for (final AttributeModifier arg5 : this.getModifiersOrEmpty(AttributeModifier.Operation.ADDITION)) {
            double2 += arg5.getAmount();
        }
        double double3 = double2;
        for (final AttributeModifier arg6 : this.getModifiersOrEmpty(AttributeModifier.Operation.MULTIPLY_BASE)) {
            double3 += double2 * arg6.getAmount();
        }
        for (final AttributeModifier arg6 : this.getModifiersOrEmpty(AttributeModifier.Operation.MULTIPLY_TOTAL)) {
            double3 *= 1.0 + arg6.getAmount();
        }
        return this.attribute.sanitizeValue(double3);
    }
    
    private Collection<AttributeModifier> getModifiersOrEmpty(final AttributeModifier.Operation a) {
        return (Collection<AttributeModifier>)this.modifiersByOperation.getOrDefault(a, Collections.emptySet());
    }
    
    public void replaceFrom(final AttributeInstance are) {
        this.baseValue = are.baseValue;
        this.modifierById.clear();
        this.modifierById.putAll((Map)are.modifierById);
        this.permanentModifiers.clear();
        this.permanentModifiers.addAll((Collection)are.permanentModifiers);
        this.modifiersByOperation.clear();
        are.modifiersByOperation.forEach((a, set) -> this.getModifiers(a).addAll((Collection)set));
        this.setDirty();
    }
    
    public CompoundTag save() {
        final CompoundTag md2 = new CompoundTag();
        md2.putString("Name", Registry.ATTRIBUTE.getKey(this.attribute).toString());
        md2.putDouble("Base", this.baseValue);
        if (!this.permanentModifiers.isEmpty()) {
            final ListTag mj3 = new ListTag();
            for (final AttributeModifier arg5 : this.permanentModifiers) {
                mj3.add(arg5.save());
            }
            md2.put("Modifiers", (Tag)mj3);
        }
        return md2;
    }
    
    public void load(final CompoundTag md) {
        this.baseValue = md.getDouble("Base");
        if (md.contains("Modifiers", 9)) {
            final ListTag mj3 = md.getList("Modifiers", 10);
            for (int integer4 = 0; integer4 < mj3.size(); ++integer4) {
                final AttributeModifier arg5 = AttributeModifier.load(mj3.getCompound(integer4));
                if (arg5 != null) {
                    this.modifierById.put(arg5.getId(), arg5);
                    this.getModifiers(arg5.getOperation()).add(arg5);
                    this.permanentModifiers.add(arg5);
                }
            }
        }
        this.setDirty();
    }
}

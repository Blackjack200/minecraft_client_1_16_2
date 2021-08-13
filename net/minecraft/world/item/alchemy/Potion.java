package net.minecraft.world.item.alchemy;

import com.google.common.collect.UnmodifiableIterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffectInstance;
import com.google.common.collect.ImmutableList;

public class Potion {
    private final String name;
    private final ImmutableList<MobEffectInstance> effects;
    
    public static Potion byName(final String string) {
        return Registry.POTION.get(ResourceLocation.tryParse(string));
    }
    
    public Potion(final MobEffectInstance... arr) {
        this((String)null, arr);
    }
    
    public Potion(@Nullable final String string, final MobEffectInstance... arr) {
        this.name = string;
        this.effects = (ImmutableList<MobEffectInstance>)ImmutableList.copyOf((Object[])arr);
    }
    
    public String getName(final String string) {
        return string + ((this.name == null) ? Registry.POTION.getKey(this).getPath() : this.name);
    }
    
    public List<MobEffectInstance> getEffects() {
        return (List<MobEffectInstance>)this.effects;
    }
    
    public boolean hasInstantEffects() {
        if (!this.effects.isEmpty()) {
            for (final MobEffectInstance apr3 : this.effects) {
                if (apr3.getEffect().isInstantenous()) {
                    return true;
                }
            }
        }
        return false;
    }
}

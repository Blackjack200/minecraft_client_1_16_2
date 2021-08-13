package net.minecraft.world.damagesource;

import net.minecraft.network.chat.Component;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;

public class CombatEntry {
    private final DamageSource source;
    private final int time;
    private final float damage;
    private final float health;
    private final String location;
    private final float fallDistance;
    
    public CombatEntry(final DamageSource aph, final int integer, final float float3, final float float4, final String string, final float float6) {
        this.source = aph;
        this.time = integer;
        this.damage = float4;
        this.health = float3;
        this.location = string;
        this.fallDistance = float6;
    }
    
    public DamageSource getSource() {
        return this.source;
    }
    
    public float getDamage() {
        return this.damage;
    }
    
    public boolean isCombatRelated() {
        return this.source.getEntity() instanceof LivingEntity;
    }
    
    @Nullable
    public String getLocation() {
        return this.location;
    }
    
    @Nullable
    public Component getAttackerName() {
        return (this.getSource().getEntity() == null) ? null : this.getSource().getEntity().getDisplayName();
    }
    
    public float getFallDistance() {
        if (this.source == DamageSource.OUT_OF_WORLD) {
            return Float.MAX_VALUE;
        }
        return this.fallDistance;
    }
}

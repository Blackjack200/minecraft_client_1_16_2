package net.minecraft.world.entity.boss.enderdragon.phases;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;

public abstract class AbstractDragonPhaseInstance implements DragonPhaseInstance {
    protected final EnderDragon dragon;
    
    public AbstractDragonPhaseInstance(final EnderDragon bbo) {
        this.dragon = bbo;
    }
    
    public boolean isSitting() {
        return false;
    }
    
    public void doClientTick() {
    }
    
    public void doServerTick() {
    }
    
    public void onCrystalDestroyed(final EndCrystal bbn, final BlockPos fx, final DamageSource aph, @Nullable final Player bft) {
    }
    
    public void begin() {
    }
    
    public void end() {
    }
    
    public float getFlySpeed() {
        return 0.6f;
    }
    
    @Nullable
    public Vec3 getFlyTargetLocation() {
        return null;
    }
    
    public float onHurt(final DamageSource aph, final float float2) {
        return float2;
    }
    
    public float getTurnSpeed() {
        final float float2 = Mth.sqrt(Entity.getHorizontalDistanceSqr(this.dragon.getDeltaMovement())) + 1.0f;
        final float float3 = Math.min(float2, 40.0f);
        return 0.7f / float3 / float2;
    }
}

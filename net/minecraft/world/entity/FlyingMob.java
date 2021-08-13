package net.minecraft.world.entity;

import net.minecraft.world.phys.Vec3;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;

public abstract class FlyingMob extends Mob {
    protected FlyingMob(final EntityType<? extends FlyingMob> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    @Override
    public boolean causeFallDamage(final float float1, final float float2) {
        return false;
    }
    
    @Override
    protected void checkFallDamage(final double double1, final boolean boolean2, final BlockState cee, final BlockPos fx) {
    }
    
    @Override
    public void travel(final Vec3 dck) {
        if (this.isInWater()) {
            this.moveRelative(0.02f, dck);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.800000011920929));
        }
        else if (this.isInLava()) {
            this.moveRelative(0.02f, dck);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.5));
        }
        else {
            float float3 = 0.91f;
            if (this.onGround) {
                float3 = this.level.getBlockState(new BlockPos(this.getX(), this.getY() - 1.0, this.getZ())).getBlock().getFriction() * 0.91f;
            }
            final float float4 = 0.16277137f / (float3 * float3 * float3);
            float3 = 0.91f;
            if (this.onGround) {
                float3 = this.level.getBlockState(new BlockPos(this.getX(), this.getY() - 1.0, this.getZ())).getBlock().getFriction() * 0.91f;
            }
            this.moveRelative(this.onGround ? (0.1f * float4) : 0.02f, dck);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(float3));
        }
        this.calculateEntityAnimation(this, false);
    }
    
    @Override
    public boolean onClimbable() {
        return false;
    }
}

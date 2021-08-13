package net.minecraft.world.level.block;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class SlimeBlock extends HalfTransparentBlock {
    public SlimeBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public void fallOn(final Level bru, final BlockPos fx, final Entity apx, final float float4) {
        if (apx.isSuppressingBounce()) {
            super.fallOn(bru, fx, apx, float4);
        }
        else {
            apx.causeFallDamage(float4, 0.0f);
        }
    }
    
    @Override
    public void updateEntityAfterFallOn(final BlockGetter bqz, final Entity apx) {
        if (apx.isSuppressingBounce()) {
            super.updateEntityAfterFallOn(bqz, apx);
        }
        else {
            this.bounceUp(apx);
        }
    }
    
    private void bounceUp(final Entity apx) {
        final Vec3 dck3 = apx.getDeltaMovement();
        if (dck3.y < 0.0) {
            final double double4 = (apx instanceof LivingEntity) ? 1.0 : 0.8;
            apx.setDeltaMovement(dck3.x, -dck3.y * double4, dck3.z);
        }
    }
    
    @Override
    public void stepOn(final Level bru, final BlockPos fx, final Entity apx) {
        final double double5 = Math.abs(apx.getDeltaMovement().y);
        if (double5 < 0.1 && !apx.isSteppingCarefully()) {
            final double double6 = 0.4 + double5 * 0.2;
            apx.setDeltaMovement(apx.getDeltaMovement().multiply(double6, 1.0, double6));
        }
        super.stepOn(bru, fx, apx);
    }
}

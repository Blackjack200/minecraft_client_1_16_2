package net.minecraft.world.entity.ai.goal;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.animal.Dolphin;

public class DolphinJumpGoal extends JumpGoal {
    private static final int[] STEPS_TO_CHECK;
    private final Dolphin dolphin;
    private final int interval;
    private boolean breached;
    
    public DolphinJumpGoal(final Dolphin bac, final int integer) {
        this.dolphin = bac;
        this.interval = integer;
    }
    
    @Override
    public boolean canUse() {
        if (this.dolphin.getRandom().nextInt(this.interval) != 0) {
            return false;
        }
        final Direction gc2 = this.dolphin.getMotionDirection();
        final int integer3 = gc2.getStepX();
        final int integer4 = gc2.getStepZ();
        final BlockPos fx5 = this.dolphin.blockPosition();
        for (final int integer5 : DolphinJumpGoal.STEPS_TO_CHECK) {
            if (!this.waterIsClear(fx5, integer3, integer4, integer5) || !this.surfaceIsClear(fx5, integer3, integer4, integer5)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean waterIsClear(final BlockPos fx, final int integer2, final int integer3, final int integer4) {
        final BlockPos fx2 = fx.offset(integer2 * integer4, 0, integer3 * integer4);
        return this.dolphin.level.getFluidState(fx2).is(FluidTags.WATER) && !this.dolphin.level.getBlockState(fx2).getMaterial().blocksMotion();
    }
    
    private boolean surfaceIsClear(final BlockPos fx, final int integer2, final int integer3, final int integer4) {
        return this.dolphin.level.getBlockState(fx.offset(integer2 * integer4, 1, integer3 * integer4)).isAir() && this.dolphin.level.getBlockState(fx.offset(integer2 * integer4, 2, integer3 * integer4)).isAir();
    }
    
    @Override
    public boolean canContinueToUse() {
        final double double2 = this.dolphin.getDeltaMovement().y;
        return (double2 * double2 >= 0.029999999329447746 || this.dolphin.xRot == 0.0f || Math.abs(this.dolphin.xRot) >= 10.0f || !this.dolphin.isInWater()) && !this.dolphin.isOnGround();
    }
    
    @Override
    public boolean isInterruptable() {
        return false;
    }
    
    @Override
    public void start() {
        final Direction gc2 = this.dolphin.getMotionDirection();
        this.dolphin.setDeltaMovement(this.dolphin.getDeltaMovement().add(gc2.getStepX() * 0.6, 0.7, gc2.getStepZ() * 0.6));
        this.dolphin.getNavigation().stop();
    }
    
    @Override
    public void stop() {
        this.dolphin.xRot = 0.0f;
    }
    
    @Override
    public void tick() {
        final boolean boolean2 = this.breached;
        if (!boolean2) {
            final FluidState cuu3 = this.dolphin.level.getFluidState(this.dolphin.blockPosition());
            this.breached = cuu3.is(FluidTags.WATER);
        }
        if (this.breached && !boolean2) {
            this.dolphin.playSound(SoundEvents.DOLPHIN_JUMP, 1.0f, 1.0f);
        }
        final Vec3 dck3 = this.dolphin.getDeltaMovement();
        if (dck3.y * dck3.y < 0.029999999329447746 && this.dolphin.xRot != 0.0f) {
            this.dolphin.xRot = Mth.rotlerp(this.dolphin.xRot, 0.0f, 0.2f);
        }
        else {
            final double double4 = Math.sqrt(Entity.getHorizontalDistanceSqr(dck3));
            final double double5 = Math.signum(-dck3.y) * Math.acos(double4 / dck3.length()) * 57.2957763671875;
            this.dolphin.xRot = (float)double5;
        }
    }
    
    static {
        STEPS_TO_CHECK = new int[] { 0, 1, 4, 5, 6, 7 };
    }
}

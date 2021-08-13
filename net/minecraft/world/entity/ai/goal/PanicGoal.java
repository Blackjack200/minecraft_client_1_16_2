package net.minecraft.world.entity.ai.goal;

import javax.annotation.Nullable;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import java.util.EnumSet;
import net.minecraft.world.entity.PathfinderMob;

public class PanicGoal extends Goal {
    protected final PathfinderMob mob;
    protected final double speedModifier;
    protected double posX;
    protected double posY;
    protected double posZ;
    protected boolean isRunning;
    
    public PanicGoal(final PathfinderMob aqr, final double double2) {
        this.mob = aqr;
        this.speedModifier = double2;
        this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE));
    }
    
    @Override
    public boolean canUse() {
        if (this.mob.getLastHurtByMob() == null && !this.mob.isOnFire()) {
            return false;
        }
        if (this.mob.isOnFire()) {
            final BlockPos fx2 = this.lookForWater(this.mob.level, this.mob, 5, 4);
            if (fx2 != null) {
                this.posX = fx2.getX();
                this.posY = fx2.getY();
                this.posZ = fx2.getZ();
                return true;
            }
        }
        return this.findRandomPosition();
    }
    
    protected boolean findRandomPosition() {
        final Vec3 dck2 = RandomPos.getPos(this.mob, 5, 4);
        if (dck2 == null) {
            return false;
        }
        this.posX = dck2.x;
        this.posY = dck2.y;
        this.posZ = dck2.z;
        return true;
    }
    
    public boolean isRunning() {
        return this.isRunning;
    }
    
    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
        this.isRunning = true;
    }
    
    @Override
    public void stop() {
        this.isRunning = false;
    }
    
    @Override
    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone();
    }
    
    @Nullable
    protected BlockPos lookForWater(final BlockGetter bqz, final Entity apx, final int integer3, final int integer4) {
        final BlockPos fx6 = apx.blockPosition();
        final int integer5 = fx6.getX();
        final int integer6 = fx6.getY();
        final int integer7 = fx6.getZ();
        float float10 = (float)(integer3 * integer3 * integer4 * 2);
        BlockPos fx7 = null;
        final BlockPos.MutableBlockPos a12 = new BlockPos.MutableBlockPos();
        for (int integer8 = integer5 - integer3; integer8 <= integer5 + integer3; ++integer8) {
            for (int integer9 = integer6 - integer4; integer9 <= integer6 + integer4; ++integer9) {
                for (int integer10 = integer7 - integer3; integer10 <= integer7 + integer3; ++integer10) {
                    a12.set(integer8, integer9, integer10);
                    if (bqz.getFluidState(a12).is(FluidTags.WATER)) {
                        final float float11 = (float)((integer8 - integer5) * (integer8 - integer5) + (integer9 - integer6) * (integer9 - integer6) + (integer10 - integer7) * (integer10 - integer7));
                        if (float11 < float10) {
                            float10 = float11;
                            fx7 = new BlockPos(a12);
                        }
                    }
                }
            }
        }
        return fx7;
    }
}

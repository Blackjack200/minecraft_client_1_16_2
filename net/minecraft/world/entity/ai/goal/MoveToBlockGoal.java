package net.minecraft.world.entity.ai.goal;

import net.minecraft.core.Vec3i;
import net.minecraft.core.Position;
import net.minecraft.world.level.LevelReader;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;

public abstract class MoveToBlockGoal extends Goal {
    protected final PathfinderMob mob;
    public final double speedModifier;
    protected int nextStartTick;
    protected int tryTicks;
    private int maxStayTicks;
    protected BlockPos blockPos;
    private boolean reachedTarget;
    private final int searchRange;
    private final int verticalSearchRange;
    protected int verticalSearchStart;
    
    public MoveToBlockGoal(final PathfinderMob aqr, final double double2, final int integer) {
        this(aqr, double2, integer, 1);
    }
    
    public MoveToBlockGoal(final PathfinderMob aqr, final double double2, final int integer3, final int integer4) {
        this.blockPos = BlockPos.ZERO;
        this.mob = aqr;
        this.speedModifier = double2;
        this.searchRange = integer3;
        this.verticalSearchStart = 0;
        this.verticalSearchRange = integer4;
        this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE, (Enum)Flag.JUMP));
    }
    
    @Override
    public boolean canUse() {
        if (this.nextStartTick > 0) {
            --this.nextStartTick;
            return false;
        }
        this.nextStartTick = this.nextStartTick(this.mob);
        return this.findNearestBlock();
    }
    
    protected int nextStartTick(final PathfinderMob aqr) {
        return 200 + aqr.getRandom().nextInt(200);
    }
    
    @Override
    public boolean canContinueToUse() {
        return this.tryTicks >= -this.maxStayTicks && this.tryTicks <= 1200 && this.isValidTarget(this.mob.level, this.blockPos);
    }
    
    @Override
    public void start() {
        this.moveMobToBlock();
        this.tryTicks = 0;
        this.maxStayTicks = this.mob.getRandom().nextInt(this.mob.getRandom().nextInt(1200) + 1200) + 1200;
    }
    
    protected void moveMobToBlock() {
        this.mob.getNavigation().moveTo((float)this.blockPos.getX() + 0.5, this.blockPos.getY() + 1, (float)this.blockPos.getZ() + 0.5, this.speedModifier);
    }
    
    public double acceptedDistance() {
        return 1.0;
    }
    
    protected BlockPos getMoveToTarget() {
        return this.blockPos.above();
    }
    
    @Override
    public void tick() {
        final BlockPos fx2 = this.getMoveToTarget();
        if (!fx2.closerThan(this.mob.position(), this.acceptedDistance())) {
            this.reachedTarget = false;
            ++this.tryTicks;
            if (this.shouldRecalculatePath()) {
                this.mob.getNavigation().moveTo((float)fx2.getX() + 0.5, fx2.getY(), (float)fx2.getZ() + 0.5, this.speedModifier);
            }
        }
        else {
            this.reachedTarget = true;
            --this.tryTicks;
        }
    }
    
    public boolean shouldRecalculatePath() {
        return this.tryTicks % 40 == 0;
    }
    
    protected boolean isReachedTarget() {
        return this.reachedTarget;
    }
    
    protected boolean findNearestBlock() {
        final int integer2 = this.searchRange;
        final int integer3 = this.verticalSearchRange;
        final BlockPos fx4 = this.mob.blockPosition();
        final BlockPos.MutableBlockPos a5 = new BlockPos.MutableBlockPos();
        for (int integer4 = this.verticalSearchStart; integer4 <= integer3; integer4 = ((integer4 > 0) ? (-integer4) : (1 - integer4))) {
            for (int integer5 = 0; integer5 < integer2; ++integer5) {
                for (int integer6 = 0; integer6 <= integer5; integer6 = ((integer6 > 0) ? (-integer6) : (1 - integer6))) {
                    for (int integer7 = (integer6 < integer5 && integer6 > -integer5) ? integer5 : 0; integer7 <= integer5; integer7 = ((integer7 > 0) ? (-integer7) : (1 - integer7))) {
                        a5.setWithOffset(fx4, integer6, integer4 - 1, integer7);
                        if (this.mob.isWithinRestriction(a5) && this.isValidTarget(this.mob.level, a5)) {
                            this.blockPos = a5;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    protected abstract boolean isValidTarget(final LevelReader brw, final BlockPos fx);
}

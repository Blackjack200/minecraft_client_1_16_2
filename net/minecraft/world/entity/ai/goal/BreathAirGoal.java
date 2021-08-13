package net.minecraft.world.entity.ai.goal;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import java.util.Iterator;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import java.util.EnumSet;
import net.minecraft.world.entity.PathfinderMob;

public class BreathAirGoal extends Goal {
    private final PathfinderMob mob;
    
    public BreathAirGoal(final PathfinderMob aqr) {
        this.mob = aqr;
        this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE, (Enum)Flag.LOOK));
    }
    
    @Override
    public boolean canUse() {
        return this.mob.getAirSupply() < 140;
    }
    
    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }
    
    @Override
    public boolean isInterruptable() {
        return false;
    }
    
    @Override
    public void start() {
        this.findAirPosition();
    }
    
    private void findAirPosition() {
        final Iterable<BlockPos> iterable2 = BlockPos.betweenClosed(Mth.floor(this.mob.getX() - 1.0), Mth.floor(this.mob.getY()), Mth.floor(this.mob.getZ() - 1.0), Mth.floor(this.mob.getX() + 1.0), Mth.floor(this.mob.getY() + 8.0), Mth.floor(this.mob.getZ() + 1.0));
        BlockPos fx3 = null;
        for (final BlockPos fx4 : iterable2) {
            if (this.givesAir(this.mob.level, fx4)) {
                fx3 = fx4;
                break;
            }
        }
        if (fx3 == null) {
            fx3 = new BlockPos(this.mob.getX(), this.mob.getY() + 8.0, this.mob.getZ());
        }
        this.mob.getNavigation().moveTo(fx3.getX(), fx3.getY() + 1, fx3.getZ(), 1.0);
    }
    
    @Override
    public void tick() {
        this.findAirPosition();
        this.mob.moveRelative(0.02f, new Vec3(this.mob.xxa, this.mob.yya, this.mob.zza));
        this.mob.move(MoverType.SELF, this.mob.getDeltaMovement());
    }
    
    private boolean givesAir(final LevelReader brw, final BlockPos fx) {
        final BlockState cee4 = brw.getBlockState(fx);
        return (brw.getFluidState(fx).isEmpty() || cee4.is(Blocks.BUBBLE_COLUMN)) && cee4.isPathfindable(brw, fx, PathComputationType.LAND);
    }
}

package net.minecraft.world.entity.ai.goal;

import java.util.Iterator;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.PathfinderMob;

public class TryFindWaterGoal extends Goal {
    private final PathfinderMob mob;
    
    public TryFindWaterGoal(final PathfinderMob aqr) {
        this.mob = aqr;
    }
    
    @Override
    public boolean canUse() {
        return this.mob.isOnGround() && !this.mob.level.getFluidState(this.mob.blockPosition()).is(FluidTags.WATER);
    }
    
    @Override
    public void start() {
        BlockPos fx2 = null;
        final Iterable<BlockPos> iterable3 = BlockPos.betweenClosed(Mth.floor(this.mob.getX() - 2.0), Mth.floor(this.mob.getY() - 2.0), Mth.floor(this.mob.getZ() - 2.0), Mth.floor(this.mob.getX() + 2.0), Mth.floor(this.mob.getY()), Mth.floor(this.mob.getZ() + 2.0));
        for (final BlockPos fx3 : iterable3) {
            if (this.mob.level.getFluidState(fx3).is(FluidTags.WATER)) {
                fx2 = fx3;
                break;
            }
        }
        if (fx2 != null) {
            this.mob.getMoveControl().setWantedPosition(fx2.getX(), fx2.getY(), fx2.getZ(), 1.0);
        }
    }
}

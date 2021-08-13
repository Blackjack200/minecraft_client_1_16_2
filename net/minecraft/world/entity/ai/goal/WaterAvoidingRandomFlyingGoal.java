package net.minecraft.world.entity.ai.goal;

import java.util.Iterator;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.core.Vec3i;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;
import javax.annotation.Nullable;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.PathfinderMob;

public class WaterAvoidingRandomFlyingGoal extends WaterAvoidingRandomStrollGoal {
    public WaterAvoidingRandomFlyingGoal(final PathfinderMob aqr, final double double2) {
        super(aqr, double2);
    }
    
    @Nullable
    @Override
    protected Vec3 getPosition() {
        Vec3 dck2 = null;
        if (this.mob.isInWater()) {
            dck2 = RandomPos.getLandPos(this.mob, 15, 15);
        }
        if (this.mob.getRandom().nextFloat() >= this.probability) {
            dck2 = this.getTreePos();
        }
        return (dck2 == null) ? super.getPosition() : dck2;
    }
    
    @Nullable
    private Vec3 getTreePos() {
        final BlockPos fx2 = this.mob.blockPosition();
        final BlockPos.MutableBlockPos a3 = new BlockPos.MutableBlockPos();
        final BlockPos.MutableBlockPos a4 = new BlockPos.MutableBlockPos();
        final Iterable<BlockPos> iterable5 = BlockPos.betweenClosed(Mth.floor(this.mob.getX() - 3.0), Mth.floor(this.mob.getY() - 6.0), Mth.floor(this.mob.getZ() - 3.0), Mth.floor(this.mob.getX() + 3.0), Mth.floor(this.mob.getY() + 6.0), Mth.floor(this.mob.getZ() + 3.0));
        for (final BlockPos fx3 : iterable5) {
            if (fx2.equals(fx3)) {
                continue;
            }
            final Block bul8 = this.mob.level.getBlockState(a4.setWithOffset(fx3, Direction.DOWN)).getBlock();
            final boolean boolean9 = bul8 instanceof LeavesBlock || bul8.is(BlockTags.LOGS);
            if (boolean9 && this.mob.level.isEmptyBlock(fx3) && this.mob.level.isEmptyBlock(a3.setWithOffset(fx3, Direction.UP))) {
                return Vec3.atBottomCenterOf(fx3);
            }
        }
        return null;
    }
}

package net.minecraft.world.level.block;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.FluidState;
import java.util.Queue;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.Direction;
import net.minecraft.util.Tuple;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class SpongeBlock extends Block {
    protected SpongeBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public void onPlace(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (cee4.is(cee1.getBlock())) {
            return;
        }
        this.tryAbsorbWater(bru, fx);
    }
    
    @Override
    public void neighborChanged(final BlockState cee, final Level bru, final BlockPos fx3, final Block bul, final BlockPos fx5, final boolean boolean6) {
        this.tryAbsorbWater(bru, fx3);
        super.neighborChanged(cee, bru, fx3, bul, fx5, boolean6);
    }
    
    protected void tryAbsorbWater(final Level bru, final BlockPos fx) {
        if (this.removeWaterBreadthFirstSearch(bru, fx)) {
            bru.setBlock(fx, Blocks.WET_SPONGE.defaultBlockState(), 2);
            bru.levelEvent(2001, fx, Block.getId(Blocks.WATER.defaultBlockState()));
        }
    }
    
    private boolean removeWaterBreadthFirstSearch(final Level bru, final BlockPos fx) {
        final Queue<Tuple<BlockPos, Integer>> queue4 = (Queue<Tuple<BlockPos, Integer>>)Lists.newLinkedList();
        queue4.add(new Tuple(fx, 0));
        int integer5 = 0;
        while (!queue4.isEmpty()) {
            final Tuple<BlockPos, Integer> afs6 = (Tuple<BlockPos, Integer>)queue4.poll();
            final BlockPos fx2 = afs6.getA();
            final int integer6 = afs6.getB();
            for (final Direction gc12 : Direction.values()) {
                final BlockPos fx3 = fx2.relative(gc12);
                final BlockState cee14 = bru.getBlockState(fx3);
                final FluidState cuu15 = bru.getFluidState(fx3);
                final Material cux16 = cee14.getMaterial();
                if (cuu15.is(FluidTags.WATER)) {
                    if (cee14.getBlock() instanceof BucketPickup && ((BucketPickup)cee14.getBlock()).takeLiquid(bru, fx3, cee14) != Fluids.EMPTY) {
                        ++integer5;
                        if (integer6 < 6) {
                            queue4.add(new Tuple(fx3, integer6 + 1));
                        }
                    }
                    else if (cee14.getBlock() instanceof LiquidBlock) {
                        bru.setBlock(fx3, Blocks.AIR.defaultBlockState(), 3);
                        ++integer5;
                        if (integer6 < 6) {
                            queue4.add(new Tuple(fx3, integer6 + 1));
                        }
                    }
                    else if (cux16 == Material.WATER_PLANT || cux16 == Material.REPLACEABLE_WATER_PLANT) {
                        final BlockEntity ccg17 = cee14.getBlock().isEntityBlock() ? bru.getBlockEntity(fx3) : null;
                        Block.dropResources(cee14, bru, fx3, ccg17);
                        bru.setBlock(fx3, Blocks.AIR.defaultBlockState(), 3);
                        ++integer5;
                        if (integer6 < 6) {
                            queue4.add(new Tuple(fx3, integer6 + 1));
                        }
                    }
                }
            }
            if (integer5 > 64) {
                break;
            }
        }
        return integer5 > 0;
    }
}

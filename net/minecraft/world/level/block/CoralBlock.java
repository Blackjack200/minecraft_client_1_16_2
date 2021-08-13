package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class CoralBlock extends Block {
    private final Block deadBlock;
    
    public CoralBlock(final Block bul, final Properties c) {
        super(c);
        this.deadBlock = bul;
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (!this.scanForWater(aag, fx)) {
            aag.setBlock(fx, this.deadBlock.defaultBlockState(), 2);
        }
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (!this.scanForWater(brv, fx5)) {
            brv.getBlockTicks().scheduleTick(fx5, this, 60 + brv.getRandom().nextInt(40));
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    protected boolean scanForWater(final BlockGetter bqz, final BlockPos fx) {
        for (final Direction gc7 : Direction.values()) {
            final FluidState cuu8 = bqz.getFluidState(fx.relative(gc7));
            if (cuu8.is(FluidTags.WATER)) {
                return true;
            }
        }
        return false;
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        if (!this.scanForWater(bnv.getLevel(), bnv.getClickedPos())) {
            bnv.getLevel().getBlockTicks().scheduleTick(bnv.getClickedPos(), this, 60 + bnv.getLevel().getRandom().nextInt(40));
        }
        return this.defaultBlockState();
    }
}

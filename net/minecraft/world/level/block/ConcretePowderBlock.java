package net.minecraft.world.level.block;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.Vec3i;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class ConcretePowderBlock extends FallingBlock {
    private final BlockState concrete;
    
    public ConcretePowderBlock(final Block bul, final Properties c) {
        super(c);
        this.concrete = bul.defaultBlockState();
    }
    
    @Override
    public void onLand(final Level bru, final BlockPos fx, final BlockState cee3, final BlockState cee4, final FallingBlockEntity bcr) {
        if (shouldSolidify(bru, fx, cee4)) {
            bru.setBlock(fx, this.concrete, 3);
        }
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final BlockGetter bqz3 = bnv.getLevel();
        final BlockPos fx4 = bnv.getClickedPos();
        final BlockState cee5 = bqz3.getBlockState(fx4);
        if (shouldSolidify(bqz3, fx4, cee5)) {
            return this.concrete;
        }
        return super.getStateForPlacement(bnv);
    }
    
    private static boolean shouldSolidify(final BlockGetter bqz, final BlockPos fx, final BlockState cee) {
        return canSolidify(cee) || touchesLiquid(bqz, fx);
    }
    
    private static boolean touchesLiquid(final BlockGetter bqz, final BlockPos fx) {
        boolean boolean3 = false;
        final BlockPos.MutableBlockPos a4 = fx.mutable();
        for (final Direction gc8 : Direction.values()) {
            BlockState cee9 = bqz.getBlockState(a4);
            if (gc8 != Direction.DOWN || canSolidify(cee9)) {
                a4.setWithOffset(fx, gc8);
                cee9 = bqz.getBlockState(a4);
                if (canSolidify(cee9) && !cee9.isFaceSturdy(bqz, fx, gc8.getOpposite())) {
                    boolean3 = true;
                    break;
                }
            }
        }
        return boolean3;
    }
    
    private static boolean canSolidify(final BlockState cee) {
        return cee.getFluidState().is(FluidTags.WATER);
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (touchesLiquid(brv, fx5)) {
            return this.concrete;
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    public int getDustColor(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return cee.getMapColor(bqz, fx).col;
    }
}

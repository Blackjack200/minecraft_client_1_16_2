package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.util.Mth;
import net.minecraft.core.Vec3i;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class FrostedIceBlock extends IceBlock {
    public static final IntegerProperty AGE;
    
    public FrostedIceBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Comparable, Integer>setValue((Property<Comparable>)FrostedIceBlock.AGE, 0));
    }
    
    @Override
    public void randomTick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        this.tick(cee, aag, fx, random);
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if ((random.nextInt(3) == 0 || this.fewerNeigboursThan(aag, fx, 4)) && aag.getMaxLocalRawBrightness(fx) > 11 - cee.<Integer>getValue((Property<Integer>)FrostedIceBlock.AGE) - cee.getLightBlock(aag, fx) && this.slightlyMelt(cee, aag, fx)) {
            final BlockPos.MutableBlockPos a6 = new BlockPos.MutableBlockPos();
            for (final Direction gc10 : Direction.values()) {
                a6.setWithOffset(fx, gc10);
                final BlockState cee2 = aag.getBlockState(a6);
                if (cee2.is(this) && !this.slightlyMelt(cee2, aag, a6)) {
                    aag.getBlockTicks().scheduleTick((BlockPos)a6, this, Mth.nextInt(random, 20, 40));
                }
            }
            return;
        }
        aag.getBlockTicks().scheduleTick(fx, this, Mth.nextInt(random, 20, 40));
    }
    
    private boolean slightlyMelt(final BlockState cee, final Level bru, final BlockPos fx) {
        final int integer5 = cee.<Integer>getValue((Property<Integer>)FrostedIceBlock.AGE);
        if (integer5 < 3) {
            bru.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)FrostedIceBlock.AGE, integer5 + 1), 2);
            return false;
        }
        this.melt(cee, bru, fx);
        return true;
    }
    
    @Override
    public void neighborChanged(final BlockState cee, final Level bru, final BlockPos fx3, final Block bul, final BlockPos fx5, final boolean boolean6) {
        if (bul == this && this.fewerNeigboursThan(bru, fx3, 2)) {
            this.melt(cee, bru, fx3);
        }
        super.neighborChanged(cee, bru, fx3, bul, fx5, boolean6);
    }
    
    private boolean fewerNeigboursThan(final BlockGetter bqz, final BlockPos fx, final int integer) {
        int integer2 = 0;
        final BlockPos.MutableBlockPos a6 = new BlockPos.MutableBlockPos();
        for (final Direction gc10 : Direction.values()) {
            a6.setWithOffset(fx, gc10);
            if (bqz.getBlockState(a6).is(this) && ++integer2 >= integer) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(FrostedIceBlock.AGE);
    }
    
    @Override
    public ItemStack getCloneItemStack(final BlockGetter bqz, final BlockPos fx, final BlockState cee) {
        return ItemStack.EMPTY;
    }
    
    static {
        AGE = BlockStateProperties.AGE_3;
    }
}

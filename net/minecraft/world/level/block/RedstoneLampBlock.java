package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.StateDefinition;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class RedstoneLampBlock extends Block {
    public static final BooleanProperty LIT;
    
    public RedstoneLampBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)RedstoneLampBlock.LIT, false));
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        return ((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)RedstoneLampBlock.LIT, bnv.getLevel().hasNeighborSignal(bnv.getClickedPos()));
    }
    
    @Override
    public void neighborChanged(final BlockState cee, final Level bru, final BlockPos fx3, final Block bul, final BlockPos fx5, final boolean boolean6) {
        if (bru.isClientSide) {
            return;
        }
        final boolean boolean7 = cee.<Boolean>getValue((Property<Boolean>)RedstoneLampBlock.LIT);
        if (boolean7 != bru.hasNeighborSignal(fx3)) {
            if (boolean7) {
                bru.getBlockTicks().scheduleTick(fx3, this, 4);
            }
            else {
                bru.setBlock(fx3, ((StateHolder<O, BlockState>)cee).<Comparable>cycle((Property<Comparable>)RedstoneLampBlock.LIT), 2);
            }
        }
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (cee.<Boolean>getValue((Property<Boolean>)RedstoneLampBlock.LIT) && !aag.hasNeighborSignal(fx)) {
            aag.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable>cycle((Property<Comparable>)RedstoneLampBlock.LIT), 2);
        }
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(RedstoneLampBlock.LIT);
    }
    
    static {
        LIT = RedstoneTorchBlock.LIT;
    }
}

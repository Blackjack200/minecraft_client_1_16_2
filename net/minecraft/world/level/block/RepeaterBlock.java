package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.DustParticleOptions;
import java.util.Random;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class RepeaterBlock extends DiodeBlock {
    public static final BooleanProperty LOCKED;
    public static final IntegerProperty DELAY;
    
    protected RepeaterBlock(final Properties c) {
        super(c);
        this.registerDefaultState((((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)RepeaterBlock.FACING, Direction.NORTH)).setValue((Property<Comparable>)RepeaterBlock.DELAY, 1)).setValue((Property<Comparable>)RepeaterBlock.LOCKED, false)).<Comparable, Boolean>setValue((Property<Comparable>)RepeaterBlock.POWERED, false));
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        if (!bft.abilities.mayBuild) {
            return InteractionResult.PASS;
        }
        bru.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable>cycle((Property<Comparable>)RepeaterBlock.DELAY), 3);
        return InteractionResult.sidedSuccess(bru.isClientSide);
    }
    
    @Override
    protected int getDelay(final BlockState cee) {
        return cee.<Integer>getValue((Property<Integer>)RepeaterBlock.DELAY) * 2;
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final BlockState cee3 = super.getStateForPlacement(bnv);
        return ((StateHolder<O, BlockState>)cee3).<Comparable, Boolean>setValue((Property<Comparable>)RepeaterBlock.LOCKED, this.isLocked(bnv.getLevel(), bnv.getClickedPos(), cee3));
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (!brv.isClientSide() && gc.getAxis() != cee1.<Direction>getValue((Property<Direction>)RepeaterBlock.FACING).getAxis()) {
            return ((StateHolder<O, BlockState>)cee1).<Comparable, Boolean>setValue((Property<Comparable>)RepeaterBlock.LOCKED, this.isLocked(brv, fx5, cee1));
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    public boolean isLocked(final LevelReader brw, final BlockPos fx, final BlockState cee) {
        return this.getAlternateSignal(brw, fx, cee) > 0;
    }
    
    @Override
    protected boolean isAlternateInput(final BlockState cee) {
        return DiodeBlock.isDiode(cee);
    }
    
    @Override
    public void animateTick(final BlockState cee, final Level bru, final BlockPos fx, final Random random) {
        if (!cee.<Boolean>getValue((Property<Boolean>)RepeaterBlock.POWERED)) {
            return;
        }
        final Direction gc6 = cee.<Direction>getValue((Property<Direction>)RepeaterBlock.FACING);
        final double double7 = fx.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.2;
        final double double8 = fx.getY() + 0.4 + (random.nextDouble() - 0.5) * 0.2;
        final double double9 = fx.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.2;
        float float13 = -5.0f;
        if (random.nextBoolean()) {
            float13 = (float)(cee.<Integer>getValue((Property<Integer>)RepeaterBlock.DELAY) * 2 - 1);
        }
        float13 /= 16.0f;
        final double double10 = float13 * gc6.getStepX();
        final double double11 = float13 * gc6.getStepZ();
        bru.addParticle(DustParticleOptions.REDSTONE, double7 + double10, double8, double9 + double11, 0.0, 0.0, 0.0);
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(RepeaterBlock.FACING, RepeaterBlock.DELAY, RepeaterBlock.LOCKED, RepeaterBlock.POWERED);
    }
    
    static {
        LOCKED = BlockStateProperties.LOCKED;
        DELAY = BlockStateProperties.DELAY;
    }
}

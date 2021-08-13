package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.entity.DaylightDetectorBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.Level;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class DaylightDetectorBlock extends BaseEntityBlock {
    public static final IntegerProperty POWER;
    public static final BooleanProperty INVERTED;
    protected static final VoxelShape SHAPE;
    
    public DaylightDetectorBlock(final Properties c) {
        super(c);
        this.registerDefaultState((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)DaylightDetectorBlock.POWER, 0)).<Comparable, Boolean>setValue((Property<Comparable>)DaylightDetectorBlock.INVERTED, false));
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return DaylightDetectorBlock.SHAPE;
    }
    
    @Override
    public boolean useShapeForLightOcclusion(final BlockState cee) {
        return true;
    }
    
    @Override
    public int getSignal(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        return cee.<Integer>getValue((Property<Integer>)DaylightDetectorBlock.POWER);
    }
    
    public static void updateSignalStrength(final BlockState cee, final Level bru, final BlockPos fx) {
        if (!bru.dimensionType().hasSkyLight()) {
            return;
        }
        int integer4 = bru.getBrightness(LightLayer.SKY, fx) - bru.getSkyDarken();
        float float5 = bru.getSunAngle(1.0f);
        final boolean boolean6 = cee.<Boolean>getValue((Property<Boolean>)DaylightDetectorBlock.INVERTED);
        if (boolean6) {
            integer4 = 15 - integer4;
        }
        else if (integer4 > 0) {
            final float float6 = (float5 < 3.1415927f) ? 0.0f : 6.2831855f;
            float5 += (float6 - float5) * 0.2f;
            integer4 = Math.round(integer4 * Mth.cos(float5));
        }
        integer4 = Mth.clamp(integer4, 0, 15);
        if (cee.<Integer>getValue((Property<Integer>)DaylightDetectorBlock.POWER) != integer4) {
            bru.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)DaylightDetectorBlock.POWER, integer4), 3);
        }
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        if (!bft.mayBuild()) {
            return super.use(cee, bru, fx, bft, aoq, dcg);
        }
        if (bru.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        final BlockState cee2 = ((StateHolder<O, BlockState>)cee).<Comparable>cycle((Property<Comparable>)DaylightDetectorBlock.INVERTED);
        bru.setBlock(fx, cee2, 4);
        updateSignalStrength(cee2, bru, fx);
        return InteractionResult.CONSUME;
    }
    
    @Override
    public RenderShape getRenderShape(final BlockState cee) {
        return RenderShape.MODEL;
    }
    
    @Override
    public boolean isSignalSource(final BlockState cee) {
        return true;
    }
    
    @Override
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        return new DaylightDetectorBlockEntity();
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(DaylightDetectorBlock.POWER, DaylightDetectorBlock.INVERTED);
    }
    
    static {
        POWER = BlockStateProperties.POWER;
        INVERTED = BlockStateProperties.INVERTED;
        SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0);
    }
}

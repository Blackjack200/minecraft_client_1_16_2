package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.apache.commons.lang3.ArrayUtils;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.entity.vehicle.DismountHelper;
import java.util.Optional;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class BedBlock extends HorizontalDirectionalBlock implements EntityBlock {
    public static final EnumProperty<BedPart> PART;
    public static final BooleanProperty OCCUPIED;
    protected static final VoxelShape BASE;
    protected static final VoxelShape LEG_NORTH_WEST;
    protected static final VoxelShape LEG_SOUTH_WEST;
    protected static final VoxelShape LEG_NORTH_EAST;
    protected static final VoxelShape LEG_SOUTH_EAST;
    protected static final VoxelShape NORTH_SHAPE;
    protected static final VoxelShape SOUTH_SHAPE;
    protected static final VoxelShape WEST_SHAPE;
    protected static final VoxelShape EAST_SHAPE;
    private final DyeColor color;
    
    public BedBlock(final DyeColor bku, final Properties c) {
        super(c);
        this.color = bku;
        this.registerDefaultState((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue(BedBlock.PART, BedPart.FOOT)).<Comparable, Boolean>setValue((Property<Comparable>)BedBlock.OCCUPIED, false));
    }
    
    @Nullable
    public static Direction getBedOrientation(final BlockGetter bqz, final BlockPos fx) {
        final BlockState cee3 = bqz.getBlockState(fx);
        return (cee3.getBlock() instanceof BedBlock) ? cee3.<Direction>getValue((Property<Direction>)BedBlock.FACING) : null;
    }
    
    public InteractionResult use(BlockState cee, final Level bru, BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        if (bru.isClientSide) {
            return InteractionResult.CONSUME;
        }
        if (cee.<BedPart>getValue(BedBlock.PART) != BedPart.HEAD) {
            fx = fx.relative(cee.<Direction>getValue((Property<Direction>)BedBlock.FACING));
            cee = bru.getBlockState(fx);
            if (!cee.is(this)) {
                return InteractionResult.CONSUME;
            }
        }
        if (!canSetSpawn(bru)) {
            bru.removeBlock(fx, false);
            final BlockPos fx2 = fx.relative(cee.<Direction>getValue((Property<Direction>)BedBlock.FACING).getOpposite());
            if (bru.getBlockState(fx2).is(this)) {
                bru.removeBlock(fx2, false);
            }
            bru.explode(null, DamageSource.badRespawnPointExplosion(), null, fx.getX() + 0.5, fx.getY() + 0.5, fx.getZ() + 0.5, 5.0f, true, Explosion.BlockInteraction.DESTROY);
            return InteractionResult.SUCCESS;
        }
        if (cee.<Boolean>getValue((Property<Boolean>)BedBlock.OCCUPIED)) {
            if (!this.kickVillagerOutOfBed(bru, fx)) {
                bft.displayClientMessage(new TranslatableComponent("block.minecraft.bed.occupied"), true);
            }
            return InteractionResult.SUCCESS;
        }
        bft.startSleepInBed(fx).ifLeft(a -> {
            if (a != null) {
                bft.displayClientMessage(a.getMessage(), true);
            }
        });
        return InteractionResult.SUCCESS;
    }
    
    public static boolean canSetSpawn(final Level bru) {
        return bru.dimensionType().bedWorks();
    }
    
    private boolean kickVillagerOutOfBed(final Level bru, final BlockPos fx) {
        final List<Villager> list4 = bru.<Villager>getEntitiesOfClass((java.lang.Class<? extends Villager>)Villager.class, new AABB(fx), (java.util.function.Predicate<? super Villager>)LivingEntity::isSleeping);
        if (list4.isEmpty()) {
            return false;
        }
        ((Villager)list4.get(0)).stopSleeping();
        return true;
    }
    
    @Override
    public void fallOn(final Level bru, final BlockPos fx, final Entity apx, final float float4) {
        super.fallOn(bru, fx, apx, float4 * 0.5f);
    }
    
    @Override
    public void updateEntityAfterFallOn(final BlockGetter bqz, final Entity apx) {
        if (apx.isSuppressingBounce()) {
            super.updateEntityAfterFallOn(bqz, apx);
        }
        else {
            this.bounceUp(apx);
        }
    }
    
    private void bounceUp(final Entity apx) {
        final Vec3 dck3 = apx.getDeltaMovement();
        if (dck3.y < 0.0) {
            final double double4 = (apx instanceof LivingEntity) ? 1.0 : 0.8;
            apx.setDeltaMovement(dck3.x, -dck3.y * 0.6600000262260437 * double4, dck3.z);
        }
    }
    
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (gc != getNeighbourDirection(cee1.<BedPart>getValue(BedBlock.PART), cee1.<Direction>getValue((Property<Direction>)BedBlock.FACING))) {
            return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
        }
        if (cee3.is(this) && cee3.<BedPart>getValue(BedBlock.PART) != cee1.<BedPart>getValue(BedBlock.PART)) {
            return ((StateHolder<O, BlockState>)cee1).<Comparable, Comparable>setValue((Property<Comparable>)BedBlock.OCCUPIED, (Comparable)cee3.<V>getValue((Property<V>)BedBlock.OCCUPIED));
        }
        return Blocks.AIR.defaultBlockState();
    }
    
    private static Direction getNeighbourDirection(final BedPart ces, final Direction gc) {
        return (ces == BedPart.FOOT) ? gc : gc.getOpposite();
    }
    
    @Override
    public void playerWillDestroy(final Level bru, final BlockPos fx, final BlockState cee, final Player bft) {
        if (!bru.isClientSide && bft.isCreative()) {
            final BedPart ces6 = cee.<BedPart>getValue(BedBlock.PART);
            if (ces6 == BedPart.FOOT) {
                final BlockPos fx2 = fx.relative(getNeighbourDirection(ces6, cee.<Direction>getValue((Property<Direction>)BedBlock.FACING)));
                final BlockState cee2 = bru.getBlockState(fx2);
                if (cee2.getBlock() == this && cee2.<BedPart>getValue(BedBlock.PART) == BedPart.HEAD) {
                    bru.setBlock(fx2, Blocks.AIR.defaultBlockState(), 35);
                    bru.levelEvent(bft, 2001, fx2, Block.getId(cee2));
                }
            }
        }
        super.playerWillDestroy(bru, fx, cee, bft);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final Direction gc3 = bnv.getHorizontalDirection();
        final BlockPos fx4 = bnv.getClickedPos();
        final BlockPos fx5 = fx4.relative(gc3);
        if (bnv.getLevel().getBlockState(fx5).canBeReplaced(bnv)) {
            return ((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)BedBlock.FACING, gc3);
        }
        return null;
    }
    
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        final Direction gc6 = getConnectedDirection(cee).getOpposite();
        switch (gc6) {
            case NORTH: {
                return BedBlock.NORTH_SHAPE;
            }
            case SOUTH: {
                return BedBlock.SOUTH_SHAPE;
            }
            case WEST: {
                return BedBlock.WEST_SHAPE;
            }
            default: {
                return BedBlock.EAST_SHAPE;
            }
        }
    }
    
    public static Direction getConnectedDirection(final BlockState cee) {
        final Direction gc2 = cee.<Direction>getValue((Property<Direction>)BedBlock.FACING);
        return (cee.<BedPart>getValue(BedBlock.PART) == BedPart.HEAD) ? gc2.getOpposite() : gc2;
    }
    
    public static DoubleBlockCombiner.BlockType getBlockType(final BlockState cee) {
        final BedPart ces2 = cee.<BedPart>getValue(BedBlock.PART);
        if (ces2 == BedPart.HEAD) {
            return DoubleBlockCombiner.BlockType.FIRST;
        }
        return DoubleBlockCombiner.BlockType.SECOND;
    }
    
    private static boolean isBunkBed(final BlockGetter bqz, final BlockPos fx) {
        return bqz.getBlockState(fx.below()).getBlock() instanceof BedBlock;
    }
    
    public static Optional<Vec3> findStandUpPosition(final EntityType<?> aqb, final CollisionGetter brd, final BlockPos fx, final float float4) {
        final Direction gc5 = brd.getBlockState(fx).<Direction>getValue((Property<Direction>)BedBlock.FACING);
        final Direction gc6 = gc5.getClockWise();
        final Direction gc7 = gc6.isFacingAngle(float4) ? gc6.getOpposite() : gc6;
        if (isBunkBed(brd, fx)) {
            return findBunkBedStandUpPosition(aqb, brd, fx, gc5, gc7);
        }
        final int[][] arr8 = bedStandUpOffsets(gc5, gc7);
        final Optional<Vec3> optional9 = findStandUpPositionAtOffset(aqb, brd, fx, arr8, true);
        if (optional9.isPresent()) {
            return optional9;
        }
        return findStandUpPositionAtOffset(aqb, brd, fx, arr8, false);
    }
    
    private static Optional<Vec3> findBunkBedStandUpPosition(final EntityType<?> aqb, final CollisionGetter brd, final BlockPos fx, final Direction gc4, final Direction gc5) {
        final int[][] arr6 = bedSurroundStandUpOffsets(gc4, gc5);
        final Optional<Vec3> optional7 = findStandUpPositionAtOffset(aqb, brd, fx, arr6, true);
        if (optional7.isPresent()) {
            return optional7;
        }
        final BlockPos fx2 = fx.below();
        final Optional<Vec3> optional8 = findStandUpPositionAtOffset(aqb, brd, fx2, arr6, true);
        if (optional8.isPresent()) {
            return optional8;
        }
        final int[][] arr7 = bedAboveStandUpOffsets(gc4);
        final Optional<Vec3> optional9 = findStandUpPositionAtOffset(aqb, brd, fx, arr7, true);
        if (optional9.isPresent()) {
            return optional9;
        }
        final Optional<Vec3> optional10 = findStandUpPositionAtOffset(aqb, brd, fx, arr6, false);
        if (optional10.isPresent()) {
            return optional10;
        }
        final Optional<Vec3> optional11 = findStandUpPositionAtOffset(aqb, brd, fx2, arr6, false);
        if (optional11.isPresent()) {
            return optional11;
        }
        return findStandUpPositionAtOffset(aqb, brd, fx, arr7, false);
    }
    
    private static Optional<Vec3> findStandUpPositionAtOffset(final EntityType<?> aqb, final CollisionGetter brd, final BlockPos fx, final int[][] arr, final boolean boolean5) {
        final BlockPos.MutableBlockPos a6 = new BlockPos.MutableBlockPos();
        for (final int[] arr2 : arr) {
            a6.set(fx.getX() + arr2[0], fx.getY(), fx.getZ() + arr2[1]);
            final Vec3 dck11 = DismountHelper.findSafeDismountLocation(aqb, brd, a6, boolean5);
            if (dck11 != null) {
                return (Optional<Vec3>)Optional.of(dck11);
            }
        }
        return (Optional<Vec3>)Optional.empty();
    }
    
    public PushReaction getPistonPushReaction(final BlockState cee) {
        return PushReaction.DESTROY;
    }
    
    public RenderShape getRenderShape(final BlockState cee) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(BedBlock.FACING, BedBlock.PART, BedBlock.OCCUPIED);
    }
    
    @Override
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        return new BedBlockEntity(this.color);
    }
    
    @Override
    public void setPlacedBy(final Level bru, final BlockPos fx, final BlockState cee, @Nullable final LivingEntity aqj, final ItemStack bly) {
        super.setPlacedBy(bru, fx, cee, aqj, bly);
        if (!bru.isClientSide) {
            final BlockPos fx2 = fx.relative(cee.<Direction>getValue((Property<Direction>)BedBlock.FACING));
            bru.setBlock(fx2, ((StateHolder<O, BlockState>)cee).<BedPart, BedPart>setValue(BedBlock.PART, BedPart.HEAD), 3);
            bru.blockUpdated(fx, Blocks.AIR);
            cee.updateNeighbourShapes(bru, fx, 3);
        }
    }
    
    public DyeColor getColor() {
        return this.color;
    }
    
    public long getSeed(final BlockState cee, final BlockPos fx) {
        final BlockPos fx2 = fx.relative(cee.<Direction>getValue((Property<Direction>)BedBlock.FACING), (cee.<BedPart>getValue(BedBlock.PART) != BedPart.HEAD) ? 1 : 0);
        return Mth.getSeed(fx2.getX(), fx.getY(), fx2.getZ());
    }
    
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    private static int[][] bedStandUpOffsets(final Direction gc1, final Direction gc2) {
        return (int[][])ArrayUtils.addAll((Object[])bedSurroundStandUpOffsets(gc1, gc2), (Object[])bedAboveStandUpOffsets(gc1));
    }
    
    private static int[][] bedSurroundStandUpOffsets(final Direction gc1, final Direction gc2) {
        return new int[][] { { gc2.getStepX(), gc2.getStepZ() }, { gc2.getStepX() - gc1.getStepX(), gc2.getStepZ() - gc1.getStepZ() }, { gc2.getStepX() - gc1.getStepX() * 2, gc2.getStepZ() - gc1.getStepZ() * 2 }, { -gc1.getStepX() * 2, -gc1.getStepZ() * 2 }, { -gc2.getStepX() - gc1.getStepX() * 2, -gc2.getStepZ() - gc1.getStepZ() * 2 }, { -gc2.getStepX() - gc1.getStepX(), -gc2.getStepZ() - gc1.getStepZ() }, { -gc2.getStepX(), -gc2.getStepZ() }, { -gc2.getStepX() + gc1.getStepX(), -gc2.getStepZ() + gc1.getStepZ() }, { gc1.getStepX(), gc1.getStepZ() }, { gc2.getStepX() + gc1.getStepX(), gc2.getStepZ() + gc1.getStepZ() } };
    }
    
    private static int[][] bedAboveStandUpOffsets(final Direction gc) {
        return new int[][] { { 0, 0 }, { -gc.getStepX(), -gc.getStepZ() } };
    }
    
    static {
        PART = BlockStateProperties.BED_PART;
        OCCUPIED = BlockStateProperties.OCCUPIED;
        BASE = Block.box(0.0, 3.0, 0.0, 16.0, 9.0, 16.0);
        LEG_NORTH_WEST = Block.box(0.0, 0.0, 0.0, 3.0, 3.0, 3.0);
        LEG_SOUTH_WEST = Block.box(0.0, 0.0, 13.0, 3.0, 3.0, 16.0);
        LEG_NORTH_EAST = Block.box(13.0, 0.0, 0.0, 16.0, 3.0, 3.0);
        LEG_SOUTH_EAST = Block.box(13.0, 0.0, 13.0, 16.0, 3.0, 16.0);
        NORTH_SHAPE = Shapes.or(BedBlock.BASE, BedBlock.LEG_NORTH_WEST, BedBlock.LEG_NORTH_EAST);
        SOUTH_SHAPE = Shapes.or(BedBlock.BASE, BedBlock.LEG_SOUTH_WEST, BedBlock.LEG_SOUTH_EAST);
        WEST_SHAPE = Shapes.or(BedBlock.BASE, BedBlock.LEG_NORTH_WEST, BedBlock.LEG_SOUTH_WEST);
        EAST_SHAPE = Shapes.or(BedBlock.BASE, BedBlock.LEG_NORTH_EAST, BedBlock.LEG_SOUTH_EAST);
    }
}

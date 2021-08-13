package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.MenuProvider;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GrindstoneBlock extends FaceAttachedHorizontalDirectionalBlock {
    public static final VoxelShape FLOOR_NORTH_SOUTH_LEFT_POST;
    public static final VoxelShape FLOOR_NORTH_SOUTH_RIGHT_POST;
    public static final VoxelShape FLOOR_NORTH_SOUTH_LEFT_PIVOT;
    public static final VoxelShape FLOOR_NORTH_SOUTH_RIGHT_PIVOT;
    public static final VoxelShape FLOOR_NORTH_SOUTH_LEFT_LEG;
    public static final VoxelShape FLOOR_NORTH_SOUTH_RIGHT_LEG;
    public static final VoxelShape FLOOR_NORTH_SOUTH_ALL_LEGS;
    public static final VoxelShape FLOOR_NORTH_SOUTH_GRINDSTONE;
    public static final VoxelShape FLOOR_EAST_WEST_LEFT_POST;
    public static final VoxelShape FLOOR_EAST_WEST_RIGHT_POST;
    public static final VoxelShape FLOOR_EAST_WEST_LEFT_PIVOT;
    public static final VoxelShape FLOOR_EAST_WEST_RIGHT_PIVOT;
    public static final VoxelShape FLOOR_EAST_WEST_LEFT_LEG;
    public static final VoxelShape FLOOR_EAST_WEST_RIGHT_LEG;
    public static final VoxelShape FLOOR_EAST_WEST_ALL_LEGS;
    public static final VoxelShape FLOOR_EAST_WEST_GRINDSTONE;
    public static final VoxelShape WALL_SOUTH_LEFT_POST;
    public static final VoxelShape WALL_SOUTH_RIGHT_POST;
    public static final VoxelShape WALL_SOUTH_LEFT_PIVOT;
    public static final VoxelShape WALL_SOUTH_RIGHT_PIVOT;
    public static final VoxelShape WALL_SOUTH_LEFT_LEG;
    public static final VoxelShape WALL_SOUTH_RIGHT_LEG;
    public static final VoxelShape WALL_SOUTH_ALL_LEGS;
    public static final VoxelShape WALL_SOUTH_GRINDSTONE;
    public static final VoxelShape WALL_NORTH_LEFT_POST;
    public static final VoxelShape WALL_NORTH_RIGHT_POST;
    public static final VoxelShape WALL_NORTH_LEFT_PIVOT;
    public static final VoxelShape WALL_NORTH_RIGHT_PIVOT;
    public static final VoxelShape WALL_NORTH_LEFT_LEG;
    public static final VoxelShape WALL_NORTH_RIGHT_LEG;
    public static final VoxelShape WALL_NORTH_ALL_LEGS;
    public static final VoxelShape WALL_NORTH_GRINDSTONE;
    public static final VoxelShape WALL_WEST_LEFT_POST;
    public static final VoxelShape WALL_WEST_RIGHT_POST;
    public static final VoxelShape WALL_WEST_LEFT_PIVOT;
    public static final VoxelShape WALL_WEST_RIGHT_PIVOT;
    public static final VoxelShape WALL_WEST_LEFT_LEG;
    public static final VoxelShape WALL_WEST_RIGHT_LEG;
    public static final VoxelShape WALL_WEST_ALL_LEGS;
    public static final VoxelShape WALL_WEST_GRINDSTONE;
    public static final VoxelShape WALL_EAST_LEFT_POST;
    public static final VoxelShape WALL_EAST_RIGHT_POST;
    public static final VoxelShape WALL_EAST_LEFT_PIVOT;
    public static final VoxelShape WALL_EAST_RIGHT_PIVOT;
    public static final VoxelShape WALL_EAST_LEFT_LEG;
    public static final VoxelShape WALL_EAST_RIGHT_LEG;
    public static final VoxelShape WALL_EAST_ALL_LEGS;
    public static final VoxelShape WALL_EAST_GRINDSTONE;
    public static final VoxelShape CEILING_NORTH_SOUTH_LEFT_POST;
    public static final VoxelShape CEILING_NORTH_SOUTH_RIGHT_POST;
    public static final VoxelShape CEILING_NORTH_SOUTH_LEFT_PIVOT;
    public static final VoxelShape CEILING_NORTH_SOUTH_RIGHT_PIVOT;
    public static final VoxelShape CEILING_NORTH_SOUTH_LEFT_LEG;
    public static final VoxelShape CEILING_NORTH_SOUTH_RIGHT_LEG;
    public static final VoxelShape CEILING_NORTH_SOUTH_ALL_LEGS;
    public static final VoxelShape CEILING_NORTH_SOUTH_GRINDSTONE;
    public static final VoxelShape CEILING_EAST_WEST_LEFT_POST;
    public static final VoxelShape CEILING_EAST_WEST_RIGHT_POST;
    public static final VoxelShape CEILING_EAST_WEST_LEFT_PIVOT;
    public static final VoxelShape CEILING_EAST_WEST_RIGHT_PIVOT;
    public static final VoxelShape CEILING_EAST_WEST_LEFT_LEG;
    public static final VoxelShape CEILING_EAST_WEST_RIGHT_LEG;
    public static final VoxelShape CEILING_EAST_WEST_ALL_LEGS;
    public static final VoxelShape CEILING_EAST_WEST_GRINDSTONE;
    private static final Component CONTAINER_TITLE;
    
    protected GrindstoneBlock(final Properties c) {
        super(c);
        this.registerDefaultState((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)GrindstoneBlock.FACING, Direction.NORTH)).<AttachFace, AttachFace>setValue(GrindstoneBlock.FACE, AttachFace.WALL));
    }
    
    @Override
    public RenderShape getRenderShape(final BlockState cee) {
        return RenderShape.MODEL;
    }
    
    private VoxelShape getVoxelShape(final BlockState cee) {
        final Direction gc3 = cee.<Direction>getValue((Property<Direction>)GrindstoneBlock.FACING);
        switch (cee.<AttachFace>getValue(GrindstoneBlock.FACE)) {
            case FLOOR: {
                if (gc3 == Direction.NORTH || gc3 == Direction.SOUTH) {
                    return GrindstoneBlock.FLOOR_NORTH_SOUTH_GRINDSTONE;
                }
                return GrindstoneBlock.FLOOR_EAST_WEST_GRINDSTONE;
            }
            case WALL: {
                if (gc3 == Direction.NORTH) {
                    return GrindstoneBlock.WALL_NORTH_GRINDSTONE;
                }
                if (gc3 == Direction.SOUTH) {
                    return GrindstoneBlock.WALL_SOUTH_GRINDSTONE;
                }
                if (gc3 == Direction.EAST) {
                    return GrindstoneBlock.WALL_EAST_GRINDSTONE;
                }
                return GrindstoneBlock.WALL_WEST_GRINDSTONE;
            }
            case CEILING: {
                if (gc3 == Direction.NORTH || gc3 == Direction.SOUTH) {
                    return GrindstoneBlock.CEILING_NORTH_SOUTH_GRINDSTONE;
                }
                return GrindstoneBlock.CEILING_EAST_WEST_GRINDSTONE;
            }
            default: {
                return GrindstoneBlock.FLOOR_EAST_WEST_GRINDSTONE;
            }
        }
    }
    
    @Override
    public VoxelShape getCollisionShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return this.getVoxelShape(cee);
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return this.getVoxelShape(cee);
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        return true;
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        if (bru.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        bft.openMenu(cee.getMenuProvider(bru, fx));
        bft.awardStat(Stats.INTERACT_WITH_GRINDSTONE);
        return InteractionResult.CONSUME;
    }
    
    @Override
    public MenuProvider getMenuProvider(final BlockState cee, final Level bru, final BlockPos fx) {
        return new SimpleMenuProvider((integer, bfs, bft) -> new GrindstoneMenu(integer, bfs, ContainerLevelAccess.create(bru, fx)), GrindstoneBlock.CONTAINER_TITLE);
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Direction>setValue((Property<Comparable>)GrindstoneBlock.FACING, bzj.rotate(cee.<Direction>getValue((Property<Direction>)GrindstoneBlock.FACING)));
    }
    
    @Override
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return cee.rotate(byd.getRotation(cee.<Direction>getValue((Property<Direction>)GrindstoneBlock.FACING)));
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(GrindstoneBlock.FACING, GrindstoneBlock.FACE);
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    static {
        FLOOR_NORTH_SOUTH_LEFT_POST = Block.box(2.0, 0.0, 6.0, 4.0, 7.0, 10.0);
        FLOOR_NORTH_SOUTH_RIGHT_POST = Block.box(12.0, 0.0, 6.0, 14.0, 7.0, 10.0);
        FLOOR_NORTH_SOUTH_LEFT_PIVOT = Block.box(2.0, 7.0, 5.0, 4.0, 13.0, 11.0);
        FLOOR_NORTH_SOUTH_RIGHT_PIVOT = Block.box(12.0, 7.0, 5.0, 14.0, 13.0, 11.0);
        FLOOR_NORTH_SOUTH_LEFT_LEG = Shapes.or(GrindstoneBlock.FLOOR_NORTH_SOUTH_LEFT_POST, GrindstoneBlock.FLOOR_NORTH_SOUTH_LEFT_PIVOT);
        FLOOR_NORTH_SOUTH_RIGHT_LEG = Shapes.or(GrindstoneBlock.FLOOR_NORTH_SOUTH_RIGHT_POST, GrindstoneBlock.FLOOR_NORTH_SOUTH_RIGHT_PIVOT);
        FLOOR_NORTH_SOUTH_ALL_LEGS = Shapes.or(GrindstoneBlock.FLOOR_NORTH_SOUTH_LEFT_LEG, GrindstoneBlock.FLOOR_NORTH_SOUTH_RIGHT_LEG);
        FLOOR_NORTH_SOUTH_GRINDSTONE = Shapes.or(GrindstoneBlock.FLOOR_NORTH_SOUTH_ALL_LEGS, Block.box(4.0, 4.0, 2.0, 12.0, 16.0, 14.0));
        FLOOR_EAST_WEST_LEFT_POST = Block.box(6.0, 0.0, 2.0, 10.0, 7.0, 4.0);
        FLOOR_EAST_WEST_RIGHT_POST = Block.box(6.0, 0.0, 12.0, 10.0, 7.0, 14.0);
        FLOOR_EAST_WEST_LEFT_PIVOT = Block.box(5.0, 7.0, 2.0, 11.0, 13.0, 4.0);
        FLOOR_EAST_WEST_RIGHT_PIVOT = Block.box(5.0, 7.0, 12.0, 11.0, 13.0, 14.0);
        FLOOR_EAST_WEST_LEFT_LEG = Shapes.or(GrindstoneBlock.FLOOR_EAST_WEST_LEFT_POST, GrindstoneBlock.FLOOR_EAST_WEST_LEFT_PIVOT);
        FLOOR_EAST_WEST_RIGHT_LEG = Shapes.or(GrindstoneBlock.FLOOR_EAST_WEST_RIGHT_POST, GrindstoneBlock.FLOOR_EAST_WEST_RIGHT_PIVOT);
        FLOOR_EAST_WEST_ALL_LEGS = Shapes.or(GrindstoneBlock.FLOOR_EAST_WEST_LEFT_LEG, GrindstoneBlock.FLOOR_EAST_WEST_RIGHT_LEG);
        FLOOR_EAST_WEST_GRINDSTONE = Shapes.or(GrindstoneBlock.FLOOR_EAST_WEST_ALL_LEGS, Block.box(2.0, 4.0, 4.0, 14.0, 16.0, 12.0));
        WALL_SOUTH_LEFT_POST = Block.box(2.0, 6.0, 0.0, 4.0, 10.0, 7.0);
        WALL_SOUTH_RIGHT_POST = Block.box(12.0, 6.0, 0.0, 14.0, 10.0, 7.0);
        WALL_SOUTH_LEFT_PIVOT = Block.box(2.0, 5.0, 7.0, 4.0, 11.0, 13.0);
        WALL_SOUTH_RIGHT_PIVOT = Block.box(12.0, 5.0, 7.0, 14.0, 11.0, 13.0);
        WALL_SOUTH_LEFT_LEG = Shapes.or(GrindstoneBlock.WALL_SOUTH_LEFT_POST, GrindstoneBlock.WALL_SOUTH_LEFT_PIVOT);
        WALL_SOUTH_RIGHT_LEG = Shapes.or(GrindstoneBlock.WALL_SOUTH_RIGHT_POST, GrindstoneBlock.WALL_SOUTH_RIGHT_PIVOT);
        WALL_SOUTH_ALL_LEGS = Shapes.or(GrindstoneBlock.WALL_SOUTH_LEFT_LEG, GrindstoneBlock.WALL_SOUTH_RIGHT_LEG);
        WALL_SOUTH_GRINDSTONE = Shapes.or(GrindstoneBlock.WALL_SOUTH_ALL_LEGS, Block.box(4.0, 2.0, 4.0, 12.0, 14.0, 16.0));
        WALL_NORTH_LEFT_POST = Block.box(2.0, 6.0, 7.0, 4.0, 10.0, 16.0);
        WALL_NORTH_RIGHT_POST = Block.box(12.0, 6.0, 7.0, 14.0, 10.0, 16.0);
        WALL_NORTH_LEFT_PIVOT = Block.box(2.0, 5.0, 3.0, 4.0, 11.0, 9.0);
        WALL_NORTH_RIGHT_PIVOT = Block.box(12.0, 5.0, 3.0, 14.0, 11.0, 9.0);
        WALL_NORTH_LEFT_LEG = Shapes.or(GrindstoneBlock.WALL_NORTH_LEFT_POST, GrindstoneBlock.WALL_NORTH_LEFT_PIVOT);
        WALL_NORTH_RIGHT_LEG = Shapes.or(GrindstoneBlock.WALL_NORTH_RIGHT_POST, GrindstoneBlock.WALL_NORTH_RIGHT_PIVOT);
        WALL_NORTH_ALL_LEGS = Shapes.or(GrindstoneBlock.WALL_NORTH_LEFT_LEG, GrindstoneBlock.WALL_NORTH_RIGHT_LEG);
        WALL_NORTH_GRINDSTONE = Shapes.or(GrindstoneBlock.WALL_NORTH_ALL_LEGS, Block.box(4.0, 2.0, 0.0, 12.0, 14.0, 12.0));
        WALL_WEST_LEFT_POST = Block.box(7.0, 6.0, 2.0, 16.0, 10.0, 4.0);
        WALL_WEST_RIGHT_POST = Block.box(7.0, 6.0, 12.0, 16.0, 10.0, 14.0);
        WALL_WEST_LEFT_PIVOT = Block.box(3.0, 5.0, 2.0, 9.0, 11.0, 4.0);
        WALL_WEST_RIGHT_PIVOT = Block.box(3.0, 5.0, 12.0, 9.0, 11.0, 14.0);
        WALL_WEST_LEFT_LEG = Shapes.or(GrindstoneBlock.WALL_WEST_LEFT_POST, GrindstoneBlock.WALL_WEST_LEFT_PIVOT);
        WALL_WEST_RIGHT_LEG = Shapes.or(GrindstoneBlock.WALL_WEST_RIGHT_POST, GrindstoneBlock.WALL_WEST_RIGHT_PIVOT);
        WALL_WEST_ALL_LEGS = Shapes.or(GrindstoneBlock.WALL_WEST_LEFT_LEG, GrindstoneBlock.WALL_WEST_RIGHT_LEG);
        WALL_WEST_GRINDSTONE = Shapes.or(GrindstoneBlock.WALL_WEST_ALL_LEGS, Block.box(0.0, 2.0, 4.0, 12.0, 14.0, 12.0));
        WALL_EAST_LEFT_POST = Block.box(0.0, 6.0, 2.0, 9.0, 10.0, 4.0);
        WALL_EAST_RIGHT_POST = Block.box(0.0, 6.0, 12.0, 9.0, 10.0, 14.0);
        WALL_EAST_LEFT_PIVOT = Block.box(7.0, 5.0, 2.0, 13.0, 11.0, 4.0);
        WALL_EAST_RIGHT_PIVOT = Block.box(7.0, 5.0, 12.0, 13.0, 11.0, 14.0);
        WALL_EAST_LEFT_LEG = Shapes.or(GrindstoneBlock.WALL_EAST_LEFT_POST, GrindstoneBlock.WALL_EAST_LEFT_PIVOT);
        WALL_EAST_RIGHT_LEG = Shapes.or(GrindstoneBlock.WALL_EAST_RIGHT_POST, GrindstoneBlock.WALL_EAST_RIGHT_PIVOT);
        WALL_EAST_ALL_LEGS = Shapes.or(GrindstoneBlock.WALL_EAST_LEFT_LEG, GrindstoneBlock.WALL_EAST_RIGHT_LEG);
        WALL_EAST_GRINDSTONE = Shapes.or(GrindstoneBlock.WALL_EAST_ALL_LEGS, Block.box(4.0, 2.0, 4.0, 16.0, 14.0, 12.0));
        CEILING_NORTH_SOUTH_LEFT_POST = Block.box(2.0, 9.0, 6.0, 4.0, 16.0, 10.0);
        CEILING_NORTH_SOUTH_RIGHT_POST = Block.box(12.0, 9.0, 6.0, 14.0, 16.0, 10.0);
        CEILING_NORTH_SOUTH_LEFT_PIVOT = Block.box(2.0, 3.0, 5.0, 4.0, 9.0, 11.0);
        CEILING_NORTH_SOUTH_RIGHT_PIVOT = Block.box(12.0, 3.0, 5.0, 14.0, 9.0, 11.0);
        CEILING_NORTH_SOUTH_LEFT_LEG = Shapes.or(GrindstoneBlock.CEILING_NORTH_SOUTH_LEFT_POST, GrindstoneBlock.CEILING_NORTH_SOUTH_LEFT_PIVOT);
        CEILING_NORTH_SOUTH_RIGHT_LEG = Shapes.or(GrindstoneBlock.CEILING_NORTH_SOUTH_RIGHT_POST, GrindstoneBlock.CEILING_NORTH_SOUTH_RIGHT_PIVOT);
        CEILING_NORTH_SOUTH_ALL_LEGS = Shapes.or(GrindstoneBlock.CEILING_NORTH_SOUTH_LEFT_LEG, GrindstoneBlock.CEILING_NORTH_SOUTH_RIGHT_LEG);
        CEILING_NORTH_SOUTH_GRINDSTONE = Shapes.or(GrindstoneBlock.CEILING_NORTH_SOUTH_ALL_LEGS, Block.box(4.0, 0.0, 2.0, 12.0, 12.0, 14.0));
        CEILING_EAST_WEST_LEFT_POST = Block.box(6.0, 9.0, 2.0, 10.0, 16.0, 4.0);
        CEILING_EAST_WEST_RIGHT_POST = Block.box(6.0, 9.0, 12.0, 10.0, 16.0, 14.0);
        CEILING_EAST_WEST_LEFT_PIVOT = Block.box(5.0, 3.0, 2.0, 11.0, 9.0, 4.0);
        CEILING_EAST_WEST_RIGHT_PIVOT = Block.box(5.0, 3.0, 12.0, 11.0, 9.0, 14.0);
        CEILING_EAST_WEST_LEFT_LEG = Shapes.or(GrindstoneBlock.CEILING_EAST_WEST_LEFT_POST, GrindstoneBlock.CEILING_EAST_WEST_LEFT_PIVOT);
        CEILING_EAST_WEST_RIGHT_LEG = Shapes.or(GrindstoneBlock.CEILING_EAST_WEST_RIGHT_POST, GrindstoneBlock.CEILING_EAST_WEST_RIGHT_PIVOT);
        CEILING_EAST_WEST_ALL_LEGS = Shapes.or(GrindstoneBlock.CEILING_EAST_WEST_LEFT_LEG, GrindstoneBlock.CEILING_EAST_WEST_RIGHT_LEG);
        CEILING_EAST_WEST_GRINDSTONE = Shapes.or(GrindstoneBlock.CEILING_EAST_WEST_ALL_LEGS, Block.box(2.0, 0.0, 4.0, 14.0, 12.0, 12.0));
        CONTAINER_TITLE = new TranslatableComponent("container.grindstone_title");
    }
}

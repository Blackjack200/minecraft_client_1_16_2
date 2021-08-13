package net.minecraft.world.level.block;

import java.util.function.Consumer;
import net.minecraft.Util;
import com.google.common.collect.Maps;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import java.util.EnumMap;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import java.util.Map;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.core.Direction;

public class PipeBlock extends Block {
    private static final Direction[] DIRECTIONS;
    public static final BooleanProperty NORTH;
    public static final BooleanProperty EAST;
    public static final BooleanProperty SOUTH;
    public static final BooleanProperty WEST;
    public static final BooleanProperty UP;
    public static final BooleanProperty DOWN;
    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION;
    protected final VoxelShape[] shapeByIndex;
    
    protected PipeBlock(final float float1, final Properties c) {
        super(c);
        this.shapeByIndex = this.makeShapes(float1);
    }
    
    private VoxelShape[] makeShapes(final float float1) {
        final float float2 = 0.5f - float1;
        final float float3 = 0.5f + float1;
        final VoxelShape dde5 = Block.box(float2 * 16.0f, float2 * 16.0f, float2 * 16.0f, float3 * 16.0f, float3 * 16.0f, float3 * 16.0f);
        final VoxelShape[] arr6 = new VoxelShape[PipeBlock.DIRECTIONS.length];
        for (int integer7 = 0; integer7 < PipeBlock.DIRECTIONS.length; ++integer7) {
            final Direction gc8 = PipeBlock.DIRECTIONS[integer7];
            arr6[integer7] = Shapes.box(0.5 + Math.min((double)(-float1), gc8.getStepX() * 0.5), 0.5 + Math.min((double)(-float1), gc8.getStepY() * 0.5), 0.5 + Math.min((double)(-float1), gc8.getStepZ() * 0.5), 0.5 + Math.max((double)float1, gc8.getStepX() * 0.5), 0.5 + Math.max((double)float1, gc8.getStepY() * 0.5), 0.5 + Math.max((double)float1, gc8.getStepZ() * 0.5));
        }
        final VoxelShape[] arr7 = new VoxelShape[64];
        for (int integer8 = 0; integer8 < 64; ++integer8) {
            VoxelShape dde6 = dde5;
            for (int integer9 = 0; integer9 < PipeBlock.DIRECTIONS.length; ++integer9) {
                if ((integer8 & 1 << integer9) != 0x0) {
                    dde6 = Shapes.or(dde6, arr6[integer9]);
                }
            }
            arr7[integer8] = dde6;
        }
        return arr7;
    }
    
    @Override
    public boolean propagatesSkylightDown(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return false;
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return this.shapeByIndex[this.getAABBIndex(cee)];
    }
    
    protected int getAABBIndex(final BlockState cee) {
        int integer3 = 0;
        for (int integer4 = 0; integer4 < PipeBlock.DIRECTIONS.length; ++integer4) {
            if (cee.<Boolean>getValue((Property<Boolean>)PipeBlock.PROPERTY_BY_DIRECTION.get(PipeBlock.DIRECTIONS[integer4]))) {
                integer3 |= 1 << integer4;
            }
        }
        return integer3;
    }
    
    static {
        DIRECTIONS = Direction.values();
        NORTH = BlockStateProperties.NORTH;
        EAST = BlockStateProperties.EAST;
        SOUTH = BlockStateProperties.SOUTH;
        WEST = BlockStateProperties.WEST;
        UP = BlockStateProperties.UP;
        DOWN = BlockStateProperties.DOWN;
        PROPERTY_BY_DIRECTION = Util.<Map>make((Map)Maps.newEnumMap((Class)Direction.class), (java.util.function.Consumer<Map>)(enumMap -> {
            enumMap.put((Enum)Direction.NORTH, PipeBlock.NORTH);
            enumMap.put((Enum)Direction.EAST, PipeBlock.EAST);
            enumMap.put((Enum)Direction.SOUTH, PipeBlock.SOUTH);
            enumMap.put((Enum)Direction.WEST, PipeBlock.WEST);
            enumMap.put((Enum)Direction.UP, PipeBlock.UP);
            enumMap.put((Enum)Direction.DOWN, PipeBlock.DOWN);
        }));
    }
}

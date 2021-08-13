package net.minecraft.world.level.block;

import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WoolCarpetBlock extends Block {
    protected static final VoxelShape SHAPE;
    private final DyeColor color;
    
    protected WoolCarpetBlock(final DyeColor bku, final Properties c) {
        super(c);
        this.color = bku;
    }
    
    public DyeColor getColor() {
        return this.color;
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return WoolCarpetBlock.SHAPE;
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (!cee1.canSurvive(brv, fx5)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        return !brw.isEmptyBlock(fx.below());
    }
    
    static {
        SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
    }
}

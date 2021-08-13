package net.minecraft.world.level.material;

import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;

public class EmptyFluid extends Fluid {
    @Override
    public Item getBucket() {
        return Items.AIR;
    }
    
    public boolean canBeReplacedWith(final FluidState cuu, final BlockGetter bqz, final BlockPos fx, final Fluid cut, final Direction gc) {
        return true;
    }
    
    public Vec3 getFlow(final BlockGetter bqz, final BlockPos fx, final FluidState cuu) {
        return Vec3.ZERO;
    }
    
    @Override
    public int getTickDelay(final LevelReader brw) {
        return 0;
    }
    
    @Override
    protected boolean isEmpty() {
        return true;
    }
    
    @Override
    protected float getExplosionResistance() {
        return 0.0f;
    }
    
    @Override
    public float getHeight(final FluidState cuu, final BlockGetter bqz, final BlockPos fx) {
        return 0.0f;
    }
    
    @Override
    public float getOwnHeight(final FluidState cuu) {
        return 0.0f;
    }
    
    @Override
    protected BlockState createLegacyBlock(final FluidState cuu) {
        return Blocks.AIR.defaultBlockState();
    }
    
    @Override
    public boolean isSource(final FluidState cuu) {
        return false;
    }
    
    @Override
    public int getAmount(final FluidState cuu) {
        return 0;
    }
    
    @Override
    public VoxelShape getShape(final FluidState cuu, final BlockGetter bqz, final BlockPos fx) {
        return Shapes.empty();
    }
}

package net.minecraft.world.level.block;

import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WaterlilyBlock extends BushBlock {
    protected static final VoxelShape AABB;
    
    protected WaterlilyBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public void entityInside(final BlockState cee, final Level bru, final BlockPos fx, final Entity apx) {
        super.entityInside(cee, bru, fx, apx);
        if (bru instanceof ServerLevel && apx instanceof Boat) {
            bru.destroyBlock(new BlockPos(fx), true, apx);
        }
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return WaterlilyBlock.AABB;
    }
    
    @Override
    protected boolean mayPlaceOn(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        final FluidState cuu5 = bqz.getFluidState(fx);
        final FluidState cuu6 = bqz.getFluidState(fx.above());
        return (cuu5.getType() == Fluids.WATER || cee.getMaterial() == Material.ICE) && cuu6.getType() == Fluids.EMPTY;
    }
    
    static {
        AABB = Block.box(1.0, 0.0, 1.0, 15.0, 1.5, 15.0);
    }
}

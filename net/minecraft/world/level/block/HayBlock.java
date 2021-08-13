package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class HayBlock extends RotatedPillarBlock {
    public HayBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Direction.Axis, Direction.Axis>setValue(HayBlock.AXIS, Direction.Axis.Y));
    }
    
    @Override
    public void fallOn(final Level bru, final BlockPos fx, final Entity apx, final float float4) {
        apx.causeFallDamage(float4, 0.2f);
    }
}

package net.minecraft.world.level.block;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WebBlock extends Block {
    public WebBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public void entityInside(final BlockState cee, final Level bru, final BlockPos fx, final Entity apx) {
        apx.makeStuckInBlock(cee, new Vec3(0.25, 0.05000000074505806, 0.25));
    }
}

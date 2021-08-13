package net.minecraft.world.phys.shapes;

import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.Item;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;

public interface CollisionContext {
    default CollisionContext empty() {
        return EntityCollisionContext.EMPTY;
    }
    
    default CollisionContext of(final Entity apx) {
        return new EntityCollisionContext(apx);
    }
    
    boolean isDescending();
    
    boolean isAbove(final VoxelShape dde, final BlockPos fx, final boolean boolean3);
    
    boolean isHoldingItem(final Item blu);
    
    boolean canStandOnFluid(final FluidState cuu, final FlowingFluid cus);
}

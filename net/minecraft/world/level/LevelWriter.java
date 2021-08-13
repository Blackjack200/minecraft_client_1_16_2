package net.minecraft.world.level;

import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;

public interface LevelWriter {
    boolean setBlock(final BlockPos fx, final BlockState cee, final int integer3, final int integer4);
    
    default boolean setBlock(final BlockPos fx, final BlockState cee, final int integer) {
        return this.setBlock(fx, cee, integer, 512);
    }
    
    boolean removeBlock(final BlockPos fx, final boolean boolean2);
    
    default boolean destroyBlock(final BlockPos fx, final boolean boolean2) {
        return this.destroyBlock(fx, boolean2, null);
    }
    
    default boolean destroyBlock(final BlockPos fx, final boolean boolean2, @Nullable final Entity apx) {
        return this.destroyBlock(fx, boolean2, apx, 512);
    }
    
    boolean destroyBlock(final BlockPos fx, final boolean boolean2, @Nullable final Entity apx, final int integer);
    
    default boolean addFreshEntity(final Entity apx) {
        return false;
    }
}

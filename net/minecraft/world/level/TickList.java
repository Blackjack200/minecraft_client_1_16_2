package net.minecraft.world.level;

import net.minecraft.core.BlockPos;

public interface TickList<T> {
    boolean hasScheduledTick(final BlockPos fx, final T object);
    
    default void scheduleTick(final BlockPos fx, final T object, final int integer) {
        this.scheduleTick(fx, object, integer, TickPriority.NORMAL);
    }
    
    void scheduleTick(final BlockPos fx, final T object, final int integer, final TickPriority bsn);
    
    boolean willTickThisTick(final BlockPos fx, final T object);
}

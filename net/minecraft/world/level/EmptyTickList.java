package net.minecraft.world.level;

import net.minecraft.core.BlockPos;

public class EmptyTickList<T> implements TickList<T> {
    private static final EmptyTickList<Object> INSTANCE;
    
    public static <T> EmptyTickList<T> empty() {
        return (EmptyTickList<T>)EmptyTickList.INSTANCE;
    }
    
    public boolean hasScheduledTick(final BlockPos fx, final T object) {
        return false;
    }
    
    public void scheduleTick(final BlockPos fx, final T object, final int integer) {
    }
    
    public void scheduleTick(final BlockPos fx, final T object, final int integer, final TickPriority bsn) {
    }
    
    public boolean willTickThisTick(final BlockPos fx, final T object) {
        return false;
    }
    
    static {
        INSTANCE = new EmptyTickList<>();
    }
}

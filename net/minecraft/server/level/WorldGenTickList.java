package net.minecraft.server.level;

import net.minecraft.world.level.TickPriority;
import net.minecraft.core.BlockPos;
import java.util.function.Function;
import net.minecraft.world.level.TickList;

public class WorldGenTickList<T> implements TickList<T> {
    private final Function<BlockPos, TickList<T>> index;
    
    public WorldGenTickList(final Function<BlockPos, TickList<T>> function) {
        this.index = function;
    }
    
    public boolean hasScheduledTick(final BlockPos fx, final T object) {
        return ((TickList)this.index.apply(fx)).hasScheduledTick(fx, object);
    }
    
    public void scheduleTick(final BlockPos fx, final T object, final int integer, final TickPriority bsn) {
        ((TickList)this.index.apply(fx)).scheduleTick(fx, object, integer, bsn);
    }
    
    public boolean willTickThisTick(final BlockPos fx, final T object) {
        return false;
    }
}

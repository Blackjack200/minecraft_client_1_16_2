package net.minecraft.world.level.chunk;

import net.minecraft.world.level.TickPriority;
import it.unimi.dsi.fastutil.shorts.ShortListIterator;
import net.minecraft.core.BlockPos;
import java.util.function.Function;
import net.minecraft.world.level.chunk.storage.ChunkSerializer;
import net.minecraft.nbt.ListTag;
import it.unimi.dsi.fastutil.shorts.ShortList;
import net.minecraft.world.level.ChunkPos;
import java.util.function.Predicate;
import net.minecraft.world.level.TickList;

public class ProtoTickList<T> implements TickList<T> {
    protected final Predicate<T> ignore;
    private final ChunkPos chunkPos;
    private final ShortList[] toBeTicked;
    
    public ProtoTickList(final Predicate<T> predicate, final ChunkPos bra) {
        this(predicate, bra, new ListTag());
    }
    
    public ProtoTickList(final Predicate<T> predicate, final ChunkPos bra, final ListTag mj) {
        this.toBeTicked = new ShortList[16];
        this.ignore = predicate;
        this.chunkPos = bra;
        for (int integer5 = 0; integer5 < mj.size(); ++integer5) {
            final ListTag mj2 = mj.getList(integer5);
            for (int integer6 = 0; integer6 < mj2.size(); ++integer6) {
                ChunkAccess.getOrCreateOffsetList(this.toBeTicked, integer5).add(mj2.getShort(integer6));
            }
        }
    }
    
    public ListTag save() {
        return ChunkSerializer.packOffsets(this.toBeTicked);
    }
    
    public void copyOut(final TickList<T> bsl, final Function<BlockPos, T> function) {
        for (int integer4 = 0; integer4 < this.toBeTicked.length; ++integer4) {
            if (this.toBeTicked[integer4] != null) {
                for (final Short short6 : this.toBeTicked[integer4]) {
                    final BlockPos fx7 = ProtoChunk.unpackOffsetCoordinates(short6, integer4, this.chunkPos);
                    bsl.scheduleTick(fx7, (T)function.apply(fx7), 0);
                }
                this.toBeTicked[integer4].clear();
            }
        }
    }
    
    public boolean hasScheduledTick(final BlockPos fx, final T object) {
        return false;
    }
    
    public void scheduleTick(final BlockPos fx, final T object, final int integer, final TickPriority bsn) {
        ChunkAccess.getOrCreateOffsetList(this.toBeTicked, fx.getY() >> 4).add(ProtoChunk.packOffsetCoordinates(fx));
    }
    
    public boolean willTickThisTick(final BlockPos fx, final T object) {
        return false;
    }
}

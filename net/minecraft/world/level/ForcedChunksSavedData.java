package net.minecraft.world.level;

import net.minecraft.nbt.CompoundTag;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.world.level.saveddata.SavedData;

public class ForcedChunksSavedData extends SavedData {
    private LongSet chunks;
    
    public ForcedChunksSavedData() {
        super("chunks");
        this.chunks = (LongSet)new LongOpenHashSet();
    }
    
    @Override
    public void load(final CompoundTag md) {
        this.chunks = (LongSet)new LongOpenHashSet(md.getLongArray("Forced"));
    }
    
    @Override
    public CompoundTag save(final CompoundTag md) {
        md.putLongArray("Forced", this.chunks.toLongArray());
        return md;
    }
    
    public LongSet getChunks() {
        return this.chunks;
    }
}

package net.minecraft.world.level.levelgen.structure;

import net.minecraft.nbt.CompoundTag;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.world.level.saveddata.SavedData;

public class StructureFeatureIndexSavedData extends SavedData {
    private LongSet all;
    private LongSet remaining;
    
    public StructureFeatureIndexSavedData(final String string) {
        super(string);
        this.all = (LongSet)new LongOpenHashSet();
        this.remaining = (LongSet)new LongOpenHashSet();
    }
    
    @Override
    public void load(final CompoundTag md) {
        this.all = (LongSet)new LongOpenHashSet(md.getLongArray("All"));
        this.remaining = (LongSet)new LongOpenHashSet(md.getLongArray("Remaining"));
    }
    
    @Override
    public CompoundTag save(final CompoundTag md) {
        md.putLongArray("All", this.all.toLongArray());
        md.putLongArray("Remaining", this.remaining.toLongArray());
        return md;
    }
    
    public void addIndex(final long long1) {
        this.all.add(long1);
        this.remaining.add(long1);
    }
    
    public boolean hasStartIndex(final long long1) {
        return this.all.contains(long1);
    }
    
    public boolean hasUnhandledIndex(final long long1) {
        return this.remaining.contains(long1);
    }
    
    public void removeIndex(final long long1) {
        this.remaining.remove(long1);
    }
    
    public LongSet getAll() {
        return this.all;
    }
}

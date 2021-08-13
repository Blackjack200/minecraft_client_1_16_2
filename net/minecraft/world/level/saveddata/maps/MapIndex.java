package net.minecraft.world.level.saveddata.maps;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Iterator;
import net.minecraft.nbt.CompoundTag;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.world.level.saveddata.SavedData;

public class MapIndex extends SavedData {
    private final Object2IntMap<String> usedAuxIds;
    
    public MapIndex() {
        super("idcounts");
        (this.usedAuxIds = (Object2IntMap<String>)new Object2IntOpenHashMap()).defaultReturnValue(-1);
    }
    
    @Override
    public void load(final CompoundTag md) {
        this.usedAuxIds.clear();
        for (final String string4 : md.getAllKeys()) {
            if (md.contains(string4, 99)) {
                this.usedAuxIds.put(string4, md.getInt(string4));
            }
        }
    }
    
    @Override
    public CompoundTag save(final CompoundTag md) {
        for (final Object2IntMap.Entry<String> entry4 : this.usedAuxIds.object2IntEntrySet()) {
            md.putInt((String)entry4.getKey(), entry4.getIntValue());
        }
        return md;
    }
    
    public int getFreeAuxValueForMap() {
        final int integer2 = this.usedAuxIds.getInt("map") + 1;
        this.usedAuxIds.put("map", integer2);
        this.setDirty();
        return integer2;
    }
}

package net.minecraft.world.level.newbiome.area;

import net.minecraft.world.level.ChunkPos;
import it.unimi.dsi.fastutil.longs.Long2IntLinkedOpenHashMap;
import net.minecraft.world.level.newbiome.layer.traits.PixelTransformer;

public final class LazyArea implements Area {
    private final PixelTransformer transformer;
    private final Long2IntLinkedOpenHashMap cache;
    private final int maxCache;
    
    public LazyArea(final Long2IntLinkedOpenHashMap long2IntLinkedOpenHashMap, final int integer, final PixelTransformer cws) {
        this.cache = long2IntLinkedOpenHashMap;
        this.maxCache = integer;
        this.transformer = cws;
    }
    
    public int get(final int integer1, final int integer2) {
        final long long4 = ChunkPos.asLong(integer1, integer2);
        synchronized (this.cache) {
            final int integer3 = this.cache.get(long4);
            if (integer3 != Integer.MIN_VALUE) {
                return integer3;
            }
            final int integer4 = this.transformer.apply(integer1, integer2);
            this.cache.put(long4, integer4);
            if (this.cache.size() > this.maxCache) {
                for (int integer5 = 0; integer5 < this.maxCache / 16; ++integer5) {
                    this.cache.removeFirstInt();
                }
            }
            return integer4;
        }
    }
    
    public int getMaxCache() {
        return this.maxCache;
    }
}

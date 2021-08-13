package net.minecraft.util;

import java.util.Arrays;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.core.IdMap;

public class CrudeIncrementalIntIdentityHashBiMap<K> implements IdMap<K> {
    private static final Object EMPTY_SLOT;
    private K[] keys;
    private int[] values;
    private K[] byId;
    private int nextId;
    private int size;
    
    public CrudeIncrementalIntIdentityHashBiMap(int integer) {
        integer /= (int)0.8f;
        this.keys = (K[])new Object[integer];
        this.values = new int[integer];
        this.byId = (K[])new Object[integer];
    }
    
    public int getId(@Nullable final K object) {
        return this.getValue(this.indexOf(object, this.hash(object)));
    }
    
    @Nullable
    public K byId(final int integer) {
        if (integer < 0 || integer >= this.byId.length) {
            return null;
        }
        return this.byId[integer];
    }
    
    private int getValue(final int integer) {
        if (integer == -1) {
            return -1;
        }
        return this.values[integer];
    }
    
    public int add(final K object) {
        final int integer3 = this.nextId();
        this.addMapping(object, integer3);
        return integer3;
    }
    
    private int nextId() {
        while (this.nextId < this.byId.length && this.byId[this.nextId] != null) {
            ++this.nextId;
        }
        return this.nextId;
    }
    
    private void grow(final int integer) {
        final K[] arr3 = this.keys;
        final int[] arr4 = this.values;
        this.keys = (K[])new Object[integer];
        this.values = new int[integer];
        this.byId = (K[])new Object[integer];
        this.nextId = 0;
        this.size = 0;
        for (int integer2 = 0; integer2 < arr3.length; ++integer2) {
            if (arr3[integer2] != null) {
                this.addMapping(arr3[integer2], arr4[integer2]);
            }
        }
    }
    
    public void addMapping(final K object, final int integer) {
        final int integer2 = Math.max(integer, this.size + 1);
        if (integer2 >= this.keys.length * 0.8f) {
            int integer3;
            for (integer3 = this.keys.length << 1; integer3 < integer; integer3 <<= 1) {}
            this.grow(integer3);
        }
        int integer3 = this.findEmpty(this.hash(object));
        this.keys[integer3] = object;
        this.values[integer3] = integer;
        this.byId[integer] = object;
        ++this.size;
        if (integer == this.nextId) {
            ++this.nextId;
        }
    }
    
    private int hash(@Nullable final K object) {
        return (Mth.murmurHash3Mixer(System.identityHashCode(object)) & Integer.MAX_VALUE) % this.keys.length;
    }
    
    private int indexOf(@Nullable final K object, final int integer) {
        for (int integer2 = integer; integer2 < this.keys.length; ++integer2) {
            if (this.keys[integer2] == object) {
                return integer2;
            }
            if (this.keys[integer2] == CrudeIncrementalIntIdentityHashBiMap.EMPTY_SLOT) {
                return -1;
            }
        }
        for (int integer2 = 0; integer2 < integer; ++integer2) {
            if (this.keys[integer2] == object) {
                return integer2;
            }
            if (this.keys[integer2] == CrudeIncrementalIntIdentityHashBiMap.EMPTY_SLOT) {
                return -1;
            }
        }
        return -1;
    }
    
    private int findEmpty(final int integer) {
        for (int integer2 = integer; integer2 < this.keys.length; ++integer2) {
            if (this.keys[integer2] == CrudeIncrementalIntIdentityHashBiMap.EMPTY_SLOT) {
                return integer2;
            }
        }
        for (int integer2 = 0; integer2 < integer; ++integer2) {
            if (this.keys[integer2] == CrudeIncrementalIntIdentityHashBiMap.EMPTY_SLOT) {
                return integer2;
            }
        }
        throw new RuntimeException("Overflowed :(");
    }
    
    public Iterator<K> iterator() {
        return (Iterator<K>)Iterators.filter((Iterator)Iterators.forArray((Object[])this.byId), Predicates.notNull());
    }
    
    public void clear() {
        Arrays.fill((Object[])this.keys, null);
        Arrays.fill((Object[])this.byId, null);
        this.nextId = 0;
        this.size = 0;
    }
    
    public int size() {
        return this.size;
    }
    
    static {
        EMPTY_SLOT = null;
    }
}

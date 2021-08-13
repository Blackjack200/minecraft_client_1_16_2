package net.minecraft.core;

import com.google.common.collect.Iterators;
import com.google.common.base.Predicates;
import java.util.Iterator;
import javax.annotation.Nullable;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.IdentityHashMap;

public class IdMapper<T> implements IdMap<T> {
    private int nextId;
    private final IdentityHashMap<T, Integer> tToId;
    private final List<T> idToT;
    
    public IdMapper() {
        this(512);
    }
    
    public IdMapper(final int integer) {
        this.idToT = (List<T>)Lists.newArrayListWithExpectedSize(integer);
        this.tToId = (IdentityHashMap<T, Integer>)new IdentityHashMap(integer);
    }
    
    public void addMapping(final T object, final int integer) {
        this.tToId.put(object, integer);
        while (this.idToT.size() <= integer) {
            this.idToT.add(null);
        }
        this.idToT.set(integer, object);
        if (this.nextId <= integer) {
            this.nextId = integer + 1;
        }
    }
    
    public void add(final T object) {
        this.addMapping(object, this.nextId);
    }
    
    public int getId(final T object) {
        final Integer integer3 = (Integer)this.tToId.get(object);
        return (integer3 == null) ? -1 : integer3;
    }
    
    @Nullable
    public final T byId(final int integer) {
        if (integer >= 0 && integer < this.idToT.size()) {
            return (T)this.idToT.get(integer);
        }
        return null;
    }
    
    public Iterator<T> iterator() {
        return (Iterator<T>)Iterators.filter(this.idToT.iterator(), Predicates.notNull());
    }
    
    public int size() {
        return this.tToId.size();
    }
}

package net.minecraft.nbt;

import java.util.AbstractList;

public abstract class CollectionTag<T extends Tag> extends AbstractList<T> implements Tag {
    public abstract T set(final int integer, final T mt);
    
    public abstract void add(final int integer, final T mt);
    
    public abstract T remove(final int integer);
    
    public abstract boolean setTag(final int integer, final Tag mt);
    
    public abstract boolean addTag(final int integer, final Tag mt);
    
    public abstract byte getElementType();
}

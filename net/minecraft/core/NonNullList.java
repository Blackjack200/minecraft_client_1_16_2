package net.minecraft.core;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.google.common.collect.Lists;
import java.util.Arrays;
import org.apache.commons.lang3.Validate;
import java.util.List;
import java.util.AbstractList;

public class NonNullList<E> extends AbstractList<E> {
    private final List<E> list;
    private final E defaultValue;
    
    public static <E> NonNullList<E> create() {
        return new NonNullList<E>();
    }
    
    public static <E> NonNullList<E> withSize(final int integer, final E object) {
        Validate.notNull(object);
        final Object[] arr3 = new Object[integer];
        Arrays.fill(arr3, object);
        return new NonNullList<E>((java.util.List<E>)Arrays.asList(arr3), object);
    }
    
    @SafeVarargs
    public static <E> NonNullList<E> of(final E object, final E... arr) {
        return new NonNullList<E>((java.util.List<E>)Arrays.asList((Object[])arr), object);
    }
    
    protected NonNullList() {
        this((java.util.List<Object>)Lists.newArrayList(), null);
    }
    
    protected NonNullList(final List<E> list, @Nullable final E object) {
        this.list = list;
        this.defaultValue = object;
    }
    
    @Nonnull
    public E get(final int integer) {
        return (E)this.list.get(integer);
    }
    
    public E set(final int integer, final E object) {
        Validate.notNull(object);
        return (E)this.list.set(integer, object);
    }
    
    public void add(final int integer, final E object) {
        Validate.notNull(object);
        this.list.add(integer, object);
    }
    
    public E remove(final int integer) {
        return (E)this.list.remove(integer);
    }
    
    public int size() {
        return this.list.size();
    }
    
    public void clear() {
        if (this.defaultValue == null) {
            super.clear();
        }
        else {
            for (int integer2 = 0; integer2 < this.size(); ++integer2) {
                this.set(integer2, this.defaultValue);
            }
        }
    }
}

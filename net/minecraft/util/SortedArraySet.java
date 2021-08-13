package net.minecraft.util;

import java.util.NoSuchElementException;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectArrays;
import java.util.Arrays;
import java.util.Comparator;
import java.util.AbstractSet;

public class SortedArraySet<T> extends AbstractSet<T> {
    private final Comparator<T> comparator;
    private T[] contents;
    private int size;
    
    private SortedArraySet(final int integer, final Comparator<T> comparator) {
        this.comparator = comparator;
        if (integer < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Initial capacity (").append(integer).append(") is negative").toString());
        }
        this.contents = SortedArraySet.<T>castRawArray(new Object[integer]);
    }
    
    public static <T extends Comparable<T>> SortedArraySet<T> create(final int integer) {
        return new SortedArraySet<T>(integer, (java.util.Comparator<T>)Comparator.naturalOrder());
    }
    
    private static <T> T[] castRawArray(final Object[] arr) {
        return (T[])arr;
    }
    
    private int findIndex(final T object) {
        return Arrays.binarySearch((Object[])this.contents, 0, this.size, object, (Comparator)this.comparator);
    }
    
    private static int getInsertionPosition(final int integer) {
        return -integer - 1;
    }
    
    public boolean add(final T object) {
        final int integer3 = this.findIndex(object);
        if (integer3 >= 0) {
            return false;
        }
        final int integer4 = getInsertionPosition(integer3);
        this.addInternal(object, integer4);
        return true;
    }
    
    private void grow(int integer) {
        if (integer <= this.contents.length) {
            return;
        }
        if (this.contents != ObjectArrays.DEFAULT_EMPTY_ARRAY) {
            integer = (int)Math.max(Math.min(this.contents.length + (long)(this.contents.length >> 1), 2147483639L), (long)integer);
        }
        else if (integer < 10) {
            integer = 10;
        }
        final Object[] arr3 = new Object[integer];
        System.arraycopy(this.contents, 0, arr3, 0, this.size);
        this.contents = SortedArraySet.<T>castRawArray(arr3);
    }
    
    private void addInternal(final T object, final int integer) {
        this.grow(this.size + 1);
        if (integer != this.size) {
            System.arraycopy(this.contents, integer, this.contents, integer + 1, this.size - integer);
        }
        this.contents[integer] = object;
        ++this.size;
    }
    
    private void removeInternal(final int integer) {
        --this.size;
        if (integer != this.size) {
            System.arraycopy(this.contents, integer + 1, this.contents, integer, this.size - integer);
        }
        this.contents[this.size] = null;
    }
    
    private T getInternal(final int integer) {
        return this.contents[integer];
    }
    
    public T addOrGet(final T object) {
        final int integer3 = this.findIndex(object);
        if (integer3 >= 0) {
            return this.getInternal(integer3);
        }
        this.addInternal(object, getInsertionPosition(integer3));
        return object;
    }
    
    public boolean remove(final Object object) {
        final int integer3 = this.findIndex(object);
        if (integer3 >= 0) {
            this.removeInternal(integer3);
            return true;
        }
        return false;
    }
    
    public T first() {
        return this.getInternal(0);
    }
    
    public boolean contains(final Object object) {
        final int integer3 = this.findIndex(object);
        return integer3 >= 0;
    }
    
    public Iterator<T> iterator() {
        return (Iterator<T>)new ArrayIterator();
    }
    
    public int size() {
        return this.size;
    }
    
    public Object[] toArray() {
        return this.contents.clone();
    }
    
    public <U> U[] toArray(final U[] arr) {
        if (arr.length < this.size) {
            return (U[])Arrays.copyOf((Object[])this.contents, this.size, arr.getClass());
        }
        System.arraycopy(this.contents, 0, arr, 0, this.size);
        if (arr.length > this.size) {
            arr[this.size] = null;
        }
        return arr;
    }
    
    public void clear() {
        Arrays.fill((Object[])this.contents, 0, this.size, null);
        this.size = 0;
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof SortedArraySet) {
            final SortedArraySet<?> afn3 = object;
            if (this.comparator.equals(afn3.comparator)) {
                return this.size == afn3.size && Arrays.equals((Object[])this.contents, (Object[])afn3.contents);
            }
        }
        return super.equals(object);
    }
    
    class ArrayIterator implements Iterator<T> {
        private int index;
        private int last;
        
        private ArrayIterator() {
            this.last = -1;
        }
        
        public boolean hasNext() {
            return this.index < SortedArraySet.this.size;
        }
        
        public T next() {
            if (this.index >= SortedArraySet.this.size) {
                throw new NoSuchElementException();
            }
            this.last = this.index++;
            return SortedArraySet.this.contents[this.last];
        }
        
        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            SortedArraySet.this.removeInternal(this.last);
            --this.index;
            this.last = -1;
        }
    }
}

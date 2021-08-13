package net.minecraft.client.searchtree;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;
import com.google.common.collect.AbstractIterator;
import java.util.Locale;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import java.util.stream.Stream;
import java.util.function.Function;

public class ReloadableIdSearchTree<T> implements MutableSearchTree<T> {
    protected SuffixArray<T> namespaceTree;
    protected SuffixArray<T> pathTree;
    private final Function<T, Stream<ResourceLocation>> idGetter;
    private final List<T> contents;
    private final Object2IntMap<T> orderT;
    
    public ReloadableIdSearchTree(final Function<T, Stream<ResourceLocation>> function) {
        this.namespaceTree = new SuffixArray<T>();
        this.pathTree = new SuffixArray<T>();
        this.contents = (List<T>)Lists.newArrayList();
        this.orderT = (Object2IntMap<T>)new Object2IntOpenHashMap();
        this.idGetter = function;
    }
    
    public void refresh() {
        this.namespaceTree = new SuffixArray<T>();
        this.pathTree = new SuffixArray<T>();
        for (final T object3 : this.contents) {
            this.index(object3);
        }
        this.namespaceTree.generate();
        this.pathTree.generate();
    }
    
    public void add(final T object) {
        this.orderT.put(object, this.contents.size());
        this.contents.add(object);
        this.index(object);
    }
    
    public void clear() {
        this.contents.clear();
        this.orderT.clear();
    }
    
    protected void index(final T object) {
        ((Stream)this.idGetter.apply(object)).forEach(vk -> {
            this.namespaceTree.add(object, vk.getNamespace().toLowerCase(Locale.ROOT));
            this.pathTree.add(object, vk.getPath().toLowerCase(Locale.ROOT));
        });
    }
    
    protected int comparePosition(final T object1, final T object2) {
        return Integer.compare(this.orderT.getInt(object1), this.orderT.getInt(object2));
    }
    
    public List<T> search(final String string) {
        final int integer3 = string.indexOf(58);
        if (integer3 == -1) {
            return this.pathTree.search(string);
        }
        final List<T> list4 = this.namespaceTree.search(string.substring(0, integer3).trim());
        final String string2 = string.substring(integer3 + 1).trim();
        final List<T> list5 = this.pathTree.search(string2);
        return (List<T>)Lists.newArrayList((Iterator)new IntersectionIterator((java.util.Iterator<Object>)list4.iterator(), (java.util.Iterator<Object>)list5.iterator(), (java.util.Comparator<Object>)this::comparePosition));
    }
    
    public static class IntersectionIterator<T> extends AbstractIterator<T> {
        private final PeekingIterator<T> firstIterator;
        private final PeekingIterator<T> secondIterator;
        private final Comparator<T> orderT;
        
        public IntersectionIterator(final Iterator<T> iterator1, final Iterator<T> iterator2, final Comparator<T> comparator) {
            this.firstIterator = (PeekingIterator<T>)Iterators.peekingIterator((Iterator)iterator1);
            this.secondIterator = (PeekingIterator<T>)Iterators.peekingIterator((Iterator)iterator2);
            this.orderT = comparator;
        }
        
        protected T computeNext() {
            while (this.firstIterator.hasNext() && this.secondIterator.hasNext()) {
                final int integer2 = this.orderT.compare(this.firstIterator.peek(), this.secondIterator.peek());
                if (integer2 == 0) {
                    this.secondIterator.next();
                    return (T)this.firstIterator.next();
                }
                if (integer2 < 0) {
                    this.firstIterator.next();
                }
                else {
                    this.secondIterator.next();
                }
            }
            return (T)this.endOfData();
        }
    }
    
    public static class IntersectionIterator<T> extends AbstractIterator<T> {
        private final PeekingIterator<T> firstIterator;
        private final PeekingIterator<T> secondIterator;
        private final Comparator<T> orderT;
        
        public IntersectionIterator(final Iterator<T> iterator1, final Iterator<T> iterator2, final Comparator<T> comparator) {
            this.firstIterator = (com.google.common.collect.PeekingIterator<T>)Iterators.peekingIterator((Iterator)iterator1);
            this.secondIterator = (com.google.common.collect.PeekingIterator<T>)Iterators.peekingIterator((Iterator)iterator2);
            this.orderT = comparator;
        }
        
        protected T computeNext() {
            while (this.firstIterator.hasNext() && this.secondIterator.hasNext()) {
                final int integer2 = this.orderT.compare(this.firstIterator.peek(), this.secondIterator.peek());
                if (integer2 == 0) {
                    this.secondIterator.next();
                    return (T)this.firstIterator.next();
                }
                if (integer2 < 0) {
                    this.firstIterator.next();
                }
                else {
                    this.secondIterator.next();
                }
            }
            return (T)this.endOfData();
        }
    }
}

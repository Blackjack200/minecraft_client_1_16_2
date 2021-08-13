package net.minecraft.client.searchtree;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;
import com.google.common.collect.AbstractIterator;
import java.util.Locale;
import com.google.common.collect.Lists;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import java.util.stream.Stream;
import java.util.function.Function;

public class ReloadableSearchTree<T> extends ReloadableIdSearchTree<T> {
    protected SuffixArray<T> tree;
    private final Function<T, Stream<String>> filler;
    
    public ReloadableSearchTree(final Function<T, Stream<String>> function1, final Function<T, Stream<ResourceLocation>> function2) {
        super(function2);
        this.tree = new SuffixArray<T>();
        this.filler = function1;
    }
    
    @Override
    public void refresh() {
        this.tree = new SuffixArray<T>();
        super.refresh();
        this.tree.generate();
    }
    
    @Override
    protected void index(final T object) {
        super.index(object);
        ((Stream)this.filler.apply(object)).forEach(string -> this.tree.add(object, string.toLowerCase(Locale.ROOT)));
    }
    
    @Override
    public List<T> search(final String string) {
        final int integer3 = string.indexOf(58);
        if (integer3 < 0) {
            return this.tree.search(string);
        }
        final List<T> list4 = this.namespaceTree.search(string.substring(0, integer3).trim());
        final String string2 = string.substring(integer3 + 1).trim();
        final List<T> list5 = this.pathTree.search(string2);
        final List<T> list6 = this.tree.search(string2);
        return (List<T>)Lists.newArrayList((Iterator)new IntersectionIterator((java.util.Iterator<Object>)list4.iterator(), (java.util.Iterator<Object>)new MergingUniqueIterator((java.util.Iterator<Object>)list5.iterator(), (java.util.Iterator<Object>)list6.iterator(), (java.util.Comparator<Object>)this::comparePosition), (java.util.Comparator<Object>)this::comparePosition));
    }
    
    static class MergingUniqueIterator<T> extends AbstractIterator<T> {
        private final PeekingIterator<T> firstIterator;
        private final PeekingIterator<T> secondIterator;
        private final Comparator<T> orderT;
        
        public MergingUniqueIterator(final Iterator<T> iterator1, final Iterator<T> iterator2, final Comparator<T> comparator) {
            this.firstIterator = (PeekingIterator<T>)Iterators.peekingIterator((Iterator)iterator1);
            this.secondIterator = (PeekingIterator<T>)Iterators.peekingIterator((Iterator)iterator2);
            this.orderT = comparator;
        }
        
        protected T computeNext() {
            final boolean boolean2 = !this.firstIterator.hasNext();
            final boolean boolean3 = !this.secondIterator.hasNext();
            if (boolean2 && boolean3) {
                return (T)this.endOfData();
            }
            if (boolean2) {
                return (T)this.secondIterator.next();
            }
            if (boolean3) {
                return (T)this.firstIterator.next();
            }
            final int integer4 = this.orderT.compare(this.firstIterator.peek(), this.secondIterator.peek());
            if (integer4 == 0) {
                this.secondIterator.next();
            }
            return (T)((integer4 <= 0) ? this.firstIterator.next() : this.secondIterator.next());
        }
    }
}

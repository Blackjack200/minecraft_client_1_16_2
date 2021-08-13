package net.minecraft.util;

import java.util.stream.StreamSupport;
import java.util.function.Consumer;
import java.util.Spliterators;
import com.google.common.collect.Lists;
import java.util.stream.Stream;
import java.util.Spliterator;
import java.util.List;

public class RewindableStream<T> {
    private final List<T> cache;
    private final Spliterator<T> source;
    
    public RewindableStream(final Stream<T> stream) {
        this.cache = (List<T>)Lists.newArrayList();
        this.source = (Spliterator<T>)stream.spliterator();
    }
    
    public Stream<T> getStream() {
        return (Stream<T>)StreamSupport.stream((Spliterator)new Spliterators.AbstractSpliterator<T>(Long.MAX_VALUE, 0) {
            private int index;
            
            public boolean tryAdvance(final Consumer<? super T> consumer) {
                while (this.index >= RewindableStream.this.cache.size()) {
                    if (!RewindableStream.this.source.tryAdvance(RewindableStream.this.cache::add)) {
                        return false;
                    }
                }
                consumer.accept(RewindableStream.this.cache.get(this.index++));
                return true;
            }
        }, false);
    }
}

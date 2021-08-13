package net.minecraft.util;

import java.util.function.Supplier;

public class LazyLoadedValue<T> {
    private Supplier<T> factory;
    private T value;
    
    public LazyLoadedValue(final Supplier<T> supplier) {
        this.factory = supplier;
    }
    
    public T get() {
        final Supplier<T> supplier2 = this.factory;
        if (supplier2 != null) {
            this.value = (T)supplier2.get();
            this.factory = null;
        }
        return this.value;
    }
}

package net.minecraft.client.searchtree;

public interface MutableSearchTree<T> extends SearchTree<T> {
    void add(final T object);
    
    void clear();
    
    void refresh();
}

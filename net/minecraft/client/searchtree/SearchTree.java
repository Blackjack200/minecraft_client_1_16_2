package net.minecraft.client.searchtree;

import java.util.List;

public interface SearchTree<T> {
    List<T> search(final String string);
}

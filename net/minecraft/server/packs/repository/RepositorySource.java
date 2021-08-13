package net.minecraft.server.packs.repository;

import java.util.function.Consumer;

public interface RepositorySource {
    void loadPacks(final Consumer<Pack> consumer, final Pack.PackConstructor a);
}

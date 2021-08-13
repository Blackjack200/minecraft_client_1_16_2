package net.minecraft.server.packs.repository;

import net.minecraft.server.packs.PackResources;
import java.util.function.Supplier;
import java.util.function.Consumer;
import net.minecraft.server.packs.VanillaPackResources;

public class ServerPacksSource implements RepositorySource {
    private final VanillaPackResources vanillaPack;
    
    public ServerPacksSource() {
        this.vanillaPack = new VanillaPackResources(new String[] { "minecraft" });
    }
    
    public void loadPacks(final Consumer<Pack> consumer, final Pack.PackConstructor a) {
        final Pack abs4 = Pack.create("vanilla", false, (Supplier<PackResources>)(() -> this.vanillaPack), a, Pack.Position.BOTTOM, PackSource.BUILT_IN);
        if (abs4 != null) {
            consumer.accept(abs4);
        }
    }
}

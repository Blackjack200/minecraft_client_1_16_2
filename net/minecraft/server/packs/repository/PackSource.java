package net.minecraft.server.packs.repository;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;

public interface PackSource {
    public static final PackSource DEFAULT = passThrough();
    public static final PackSource BUILT_IN = decorating("pack.source.builtin");
    public static final PackSource WORLD = decorating("pack.source.world");
    public static final PackSource SERVER = decorating("pack.source.server");
    
    Component decorate(final Component nr);
    
    default PackSource passThrough() {
        return nr -> nr;
    }
    
    default PackSource decorating(final String string) {
        final Component nr3 = new TranslatableComponent(string);
        final TranslatableComponent translatableComponent;
        final Object o;
        return nr2 -> {
            new TranslatableComponent("pack.nameAndSource", new Object[] { nr2, o });
            return translatableComponent;
        };
    }
}

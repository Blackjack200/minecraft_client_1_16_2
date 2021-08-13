package net.minecraft.client.gui.font.providers;

import javax.annotation.Nullable;
import com.mojang.blaze3d.font.GlyphProvider;
import net.minecraft.server.packs.resources.ResourceManager;

public interface GlyphProviderBuilder {
    @Nullable
    GlyphProvider create(final ResourceManager acf);
}

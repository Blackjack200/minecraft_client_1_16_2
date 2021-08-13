package net.minecraft.client.resources;

import java.io.IOException;
import net.minecraft.server.packs.resources.Resource;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public class LegacyStuffWrapper {
    @Deprecated
    public static int[] getPixels(final ResourceManager acf, final ResourceLocation vk) throws IOException {
        try (final Resource ace3 = acf.getResource(vk);
             final NativeImage deq5 = NativeImage.read(ace3.getInputStream())) {
            return deq5.makePixelArray();
        }
    }
}

package net.minecraft.client.renderer.texture;

import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import javax.annotation.Nullable;
import java.io.Closeable;
import org.apache.logging.log4j.LogManager;
import com.mojang.blaze3d.platform.TextureUtil;
import java.io.IOException;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class SimpleTexture extends AbstractTexture {
    private static final Logger LOGGER;
    protected final ResourceLocation location;
    
    public SimpleTexture(final ResourceLocation vk) {
        this.location = vk;
    }
    
    @Override
    public void load(final ResourceManager acf) throws IOException {
        final TextureImage a3 = this.getTextureImage(acf);
        a3.throwIfError();
        final TextureMetadataSection eld6 = a3.getTextureMetadata();
        boolean boolean4;
        boolean boolean5;
        if (eld6 != null) {
            boolean4 = eld6.isBlur();
            boolean5 = eld6.isClamp();
        }
        else {
            boolean4 = false;
            boolean5 = false;
        }
        final NativeImage deq7 = a3.getImage();
        if (!RenderSystem.isOnRenderThreadOrInit()) {
            RenderSystem.recordRenderCall(() -> this.doLoad(deq7, boolean4, boolean5));
        }
        else {
            this.doLoad(deq7, boolean4, boolean5);
        }
    }
    
    private void doLoad(final NativeImage deq, final boolean boolean2, final boolean boolean3) {
        TextureUtil.prepareImage(this.getId(), 0, deq.getWidth(), deq.getHeight());
        deq.upload(0, 0, 0, 0, 0, deq.getWidth(), deq.getHeight(), boolean2, boolean3, false, true);
    }
    
    protected TextureImage getTextureImage(final ResourceManager acf) {
        return TextureImage.load(acf, this.location);
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public static class TextureImage implements Closeable {
        @Nullable
        private final TextureMetadataSection metadata;
        @Nullable
        private final NativeImage image;
        @Nullable
        private final IOException exception;
        
        public TextureImage(final IOException iOException) {
            this.exception = iOException;
            this.metadata = null;
            this.image = null;
        }
        
        public TextureImage(@Nullable final TextureMetadataSection eld, final NativeImage deq) {
            this.exception = null;
            this.metadata = eld;
            this.image = deq;
        }
        
        public static TextureImage load(final ResourceManager acf, final ResourceLocation vk) {
            try (final Resource ace3 = acf.getResource(vk)) {
                final NativeImage deq5 = NativeImage.read(ace3.getInputStream());
                TextureMetadataSection eld6 = null;
                try {
                    eld6 = ace3.<TextureMetadataSection>getMetadata((MetadataSectionSerializer<TextureMetadataSection>)TextureMetadataSection.SERIALIZER);
                }
                catch (RuntimeException runtimeException7) {
                    SimpleTexture.LOGGER.warn("Failed reading metadata of: {}", vk, runtimeException7);
                }
                return new TextureImage(eld6, deq5);
            }
            catch (IOException iOException3) {
                return new TextureImage(iOException3);
            }
        }
        
        @Nullable
        public TextureMetadataSection getTextureMetadata() {
            return this.metadata;
        }
        
        public NativeImage getImage() throws IOException {
            if (this.exception != null) {
                throw this.exception;
            }
            return this.image;
        }
        
        public void close() {
            if (this.image != null) {
                this.image.close();
            }
        }
        
        public void throwIfError() throws IOException {
            if (this.exception != null) {
                throw this.exception;
            }
        }
    }
}

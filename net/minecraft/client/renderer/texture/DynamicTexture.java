package net.minecraft.client.renderer.texture;

import org.apache.logging.log4j.LogManager;
import net.minecraft.server.packs.resources.ResourceManager;
import java.util.function.Supplier;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import javax.annotation.Nullable;
import com.mojang.blaze3d.platform.NativeImage;
import org.apache.logging.log4j.Logger;

public class DynamicTexture extends AbstractTexture {
    private static final Logger LOGGER;
    @Nullable
    private NativeImage pixels;
    
    public DynamicTexture(final NativeImage deq) {
        this.pixels = deq;
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> {
                TextureUtil.prepareImage(this.getId(), this.pixels.getWidth(), this.pixels.getHeight());
                this.upload();
            });
        }
        else {
            TextureUtil.prepareImage(this.getId(), this.pixels.getWidth(), this.pixels.getHeight());
            this.upload();
        }
    }
    
    public DynamicTexture(final int integer1, final int integer2, final boolean boolean3) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnGameThreadOrInit);
        this.pixels = new NativeImage(integer1, integer2, boolean3);
        TextureUtil.prepareImage(this.getId(), this.pixels.getWidth(), this.pixels.getHeight());
    }
    
    @Override
    public void load(final ResourceManager acf) {
    }
    
    public void upload() {
        if (this.pixels != null) {
            this.bind();
            this.pixels.upload(0, 0, 0, false);
        }
        else {
            DynamicTexture.LOGGER.warn("Trying to upload disposed texture {}", this.getId());
        }
    }
    
    @Nullable
    public NativeImage getPixels() {
        return this.pixels;
    }
    
    public void setPixels(final NativeImage deq) {
        if (this.pixels != null) {
            this.pixels.close();
        }
        this.pixels = deq;
    }
    
    @Override
    public void close() {
        if (this.pixels != null) {
            this.pixels.close();
            this.releaseId();
            this.pixels = null;
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}

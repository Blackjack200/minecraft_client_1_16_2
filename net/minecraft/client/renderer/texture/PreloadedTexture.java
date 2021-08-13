package net.minecraft.client.renderer.texture;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import java.util.concurrent.Executor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class PreloadedTexture extends SimpleTexture {
    @Nullable
    private CompletableFuture<TextureImage> future;
    
    public PreloadedTexture(final ResourceManager acf, final ResourceLocation vk, final Executor executor) {
        super(vk);
        this.future = (CompletableFuture<TextureImage>)CompletableFuture.supplyAsync(() -> TextureImage.load(acf, vk), executor);
    }
    
    @Override
    protected TextureImage getTextureImage(final ResourceManager acf) {
        if (this.future != null) {
            final TextureImage a3 = (TextureImage)this.future.join();
            this.future = null;
            return a3;
        }
        return TextureImage.load(acf, this.location);
    }
    
    public CompletableFuture<Void> getFuture() {
        return (CompletableFuture<Void>)((this.future == null) ? CompletableFuture.completedFuture(null) : this.future.thenApply(a -> null));
    }
    
    @Override
    public void reset(final TextureManager ejv, final ResourceManager acf, final ResourceLocation vk, final Executor executor) {
        (this.future = (CompletableFuture<TextureImage>)CompletableFuture.supplyAsync(() -> TextureImage.load(acf, this.location), Util.backgroundExecutor())).thenRunAsync(() -> ejv.register(this.location, this), executor(executor));
    }
    
    private static Executor executor(final Executor executor) {
        return runnable -> executor.execute(() -> RenderSystem.recordRenderCall(runnable::run));
    }
}

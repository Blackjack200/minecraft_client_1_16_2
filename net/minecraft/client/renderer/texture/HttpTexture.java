package net.minecraft.client.renderer.texture;

import org.apache.logging.log4j.LogManager;
import com.mojang.blaze3d.systems.RenderSystem;
import org.apache.commons.io.FileUtils;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.IOException;
import net.minecraft.Util;
import java.io.InputStream;
import java.io.FileInputStream;
import net.minecraft.server.packs.resources.ResourceManager;
import com.mojang.blaze3d.platform.TextureUtil;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.resources.ResourceLocation;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nullable;
import java.io.File;
import org.apache.logging.log4j.Logger;

public class HttpTexture extends SimpleTexture {
    private static final Logger LOGGER;
    @Nullable
    private final File file;
    private final String urlString;
    private final boolean processLegacySkin;
    @Nullable
    private final Runnable onDownloaded;
    @Nullable
    private CompletableFuture<?> future;
    private boolean uploaded;
    
    public HttpTexture(@Nullable final File file, final String string, final ResourceLocation vk, final boolean boolean4, @Nullable final Runnable runnable) {
        super(vk);
        this.file = file;
        this.urlString = string;
        this.processLegacySkin = boolean4;
        this.onDownloaded = runnable;
    }
    
    private void loadCallback(final NativeImage deq) {
        if (this.onDownloaded != null) {
            this.onDownloaded.run();
        }
        Minecraft.getInstance().execute(() -> {
            this.uploaded = true;
            if (!RenderSystem.isOnRenderThread()) {
                RenderSystem.recordRenderCall(() -> this.upload(deq));
            }
            else {
                this.upload(deq);
            }
        });
    }
    
    private void upload(final NativeImage deq) {
        TextureUtil.prepareImage(this.getId(), deq.getWidth(), deq.getHeight());
        deq.upload(0, 0, 0, true);
    }
    
    @Override
    public void load(final ResourceManager acf) throws IOException {
        Minecraft.getInstance().execute(() -> {
            if (!this.uploaded) {
                try {
                    super.load(acf);
                }
                catch (IOException iOException3) {
                    HttpTexture.LOGGER.warn("Failed to load texture: {}", this.location, iOException3);
                }
                this.uploaded = true;
            }
        });
        if (this.future != null) {
            return;
        }
        NativeImage deq3;
        if (this.file != null && this.file.isFile()) {
            HttpTexture.LOGGER.debug("Loading http texture from local cache ({})", this.file);
            final FileInputStream fileInputStream4 = new FileInputStream(this.file);
            deq3 = this.load((InputStream)fileInputStream4);
        }
        else {
            deq3 = null;
        }
        if (deq3 != null) {
            this.loadCallback(deq3);
            return;
        }
        this.future = CompletableFuture.runAsync(() -> {
            HttpURLConnection httpURLConnection2 = null;
            HttpTexture.LOGGER.debug("Downloading http texture from {} to {}", this.urlString, this.file);
            try {
                httpURLConnection2 = (HttpURLConnection)new URL(this.urlString).openConnection(Minecraft.getInstance().getProxy());
                httpURLConnection2.setDoInput(true);
                httpURLConnection2.setDoOutput(false);
                httpURLConnection2.connect();
                if (httpURLConnection2.getResponseCode() / 100 != 2) {
                    return;
                }
                InputStream inputStream3;
                if (this.file != null) {
                    FileUtils.copyInputStreamToFile(httpURLConnection2.getInputStream(), this.file);
                    inputStream3 = (InputStream)new FileInputStream(this.file);
                }
                else {
                    inputStream3 = httpURLConnection2.getInputStream();
                }
                Minecraft.getInstance().execute(() -> {
                    final NativeImage deq3 = this.load(inputStream3);
                    if (deq3 != null) {
                        this.loadCallback(deq3);
                    }
                });
            }
            catch (Exception exception3) {
                HttpTexture.LOGGER.error("Couldn't download http texture", (Throwable)exception3);
            }
            finally {
                if (httpURLConnection2 != null) {
                    httpURLConnection2.disconnect();
                }
            }
        }, Util.backgroundExecutor());
    }
    
    @Nullable
    private NativeImage load(final InputStream inputStream) {
        NativeImage deq3 = null;
        try {
            deq3 = NativeImage.read(inputStream);
            if (this.processLegacySkin) {
                deq3 = processLegacySkin(deq3);
            }
        }
        catch (IOException iOException4) {
            HttpTexture.LOGGER.warn("Error while loading the skin texture", (Throwable)iOException4);
        }
        return deq3;
    }
    
    private static NativeImage processLegacySkin(NativeImage deq) {
        final boolean boolean2 = deq.getHeight() == 32;
        if (boolean2) {
            final NativeImage deq2 = new NativeImage(64, 64, true);
            deq2.copyFrom(deq);
            deq.close();
            deq = deq2;
            deq.fillRect(0, 32, 64, 32, 0);
            deq.copyRect(4, 16, 16, 32, 4, 4, true, false);
            deq.copyRect(8, 16, 16, 32, 4, 4, true, false);
            deq.copyRect(0, 20, 24, 32, 4, 12, true, false);
            deq.copyRect(4, 20, 16, 32, 4, 12, true, false);
            deq.copyRect(8, 20, 8, 32, 4, 12, true, false);
            deq.copyRect(12, 20, 16, 32, 4, 12, true, false);
            deq.copyRect(44, 16, -8, 32, 4, 4, true, false);
            deq.copyRect(48, 16, -8, 32, 4, 4, true, false);
            deq.copyRect(40, 20, 0, 32, 4, 12, true, false);
            deq.copyRect(44, 20, -8, 32, 4, 12, true, false);
            deq.copyRect(48, 20, -16, 32, 4, 12, true, false);
            deq.copyRect(52, 20, -8, 32, 4, 12, true, false);
        }
        setNoAlpha(deq, 0, 0, 32, 16);
        if (boolean2) {
            doNotchTransparencyHack(deq, 32, 0, 64, 32);
        }
        setNoAlpha(deq, 0, 16, 64, 32);
        setNoAlpha(deq, 16, 48, 48, 64);
        return deq;
    }
    
    private static void doNotchTransparencyHack(final NativeImage deq, final int integer2, final int integer3, final int integer4, final int integer5) {
        for (int integer6 = integer2; integer6 < integer4; ++integer6) {
            for (int integer7 = integer3; integer7 < integer5; ++integer7) {
                final int integer8 = deq.getPixelRGBA(integer6, integer7);
                if ((integer8 >> 24 & 0xFF) < 128) {
                    return;
                }
            }
        }
        for (int integer6 = integer2; integer6 < integer4; ++integer6) {
            for (int integer7 = integer3; integer7 < integer5; ++integer7) {
                deq.setPixelRGBA(integer6, integer7, deq.getPixelRGBA(integer6, integer7) & 0xFFFFFF);
            }
        }
    }
    
    private static void setNoAlpha(final NativeImage deq, final int integer2, final int integer3, final int integer4, final int integer5) {
        for (int integer6 = integer2; integer6 < integer4; ++integer6) {
            for (int integer7 = integer3; integer7 < integer5; ++integer7) {
                deq.setPixelRGBA(integer6, integer7, deq.getPixelRGBA(integer6, integer7) | 0xFF000000);
            }
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}

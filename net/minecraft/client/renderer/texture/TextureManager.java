package net.minecraft.client.renderer.texture;

import org.apache.logging.log4j.LogManager;
import com.mojang.realmsclient.RealmsMainScreen;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.util.profiling.ProfilerFiller;
import com.mojang.blaze3d.platform.TextureUtil;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.CrashReportDetail;
import net.minecraft.CrashReport;
import java.io.IOException;
import com.mojang.blaze3d.systems.RenderSystem;
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import net.minecraft.server.packs.resources.ResourceManager;
import java.util.Set;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;
import net.minecraft.server.packs.resources.PreparableReloadListener;

public class TextureManager implements PreparableReloadListener, Tickable, AutoCloseable {
    private static final Logger LOGGER;
    public static final ResourceLocation INTENTIONAL_MISSING_TEXTURE;
    private final Map<ResourceLocation, AbstractTexture> byPath;
    private final Set<Tickable> tickableTextures;
    private final Map<String, Integer> prefixRegister;
    private final ResourceManager resourceManager;
    
    public TextureManager(final ResourceManager acf) {
        this.byPath = (Map<ResourceLocation, AbstractTexture>)Maps.newHashMap();
        this.tickableTextures = (Set<Tickable>)Sets.newHashSet();
        this.prefixRegister = (Map<String, Integer>)Maps.newHashMap();
        this.resourceManager = acf;
    }
    
    public void bind(final ResourceLocation vk) {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> this._bind(vk));
        }
        else {
            this._bind(vk);
        }
    }
    
    private void _bind(final ResourceLocation vk) {
        AbstractTexture eji3 = (AbstractTexture)this.byPath.get(vk);
        if (eji3 == null) {
            eji3 = new SimpleTexture(vk);
            this.register(vk, eji3);
        }
        eji3.bind();
    }
    
    public void register(final ResourceLocation vk, AbstractTexture eji) {
        eji = this.loadTexture(vk, eji);
        final AbstractTexture eji2 = (AbstractTexture)this.byPath.put(vk, eji);
        if (eji2 != eji) {
            if (eji2 != null && eji2 != MissingTextureAtlasSprite.getTexture()) {
                this.tickableTextures.remove(eji2);
                this.safeClose(vk, eji2);
            }
            if (eji instanceof Tickable) {
                this.tickableTextures.add(eji);
            }
        }
    }
    
    private void safeClose(final ResourceLocation vk, final AbstractTexture eji) {
        if (eji != MissingTextureAtlasSprite.getTexture()) {
            try {
                eji.close();
            }
            catch (Exception exception4) {
                TextureManager.LOGGER.warn("Failed to close texture {}", vk, exception4);
            }
        }
        eji.releaseId();
    }
    
    private AbstractTexture loadTexture(final ResourceLocation vk, final AbstractTexture eji) {
        try {
            eji.load(this.resourceManager);
            return eji;
        }
        catch (IOException iOException4) {
            if (vk != TextureManager.INTENTIONAL_MISSING_TEXTURE) {
                TextureManager.LOGGER.warn("Failed to load texture: {}", vk, iOException4);
            }
            return MissingTextureAtlasSprite.getTexture();
        }
        catch (Throwable throwable4) {
            final CrashReport l5 = CrashReport.forThrowable(throwable4, "Registering texture");
            final CrashReportCategory m6 = l5.addCategory("Resource location being registered");
            m6.setDetail("Resource location", vk);
            m6.setDetail("Texture object class", (CrashReportDetail<String>)(() -> eji.getClass().getName()));
            throw new ReportedException(l5);
        }
    }
    
    @Nullable
    public AbstractTexture getTexture(final ResourceLocation vk) {
        return (AbstractTexture)this.byPath.get(vk);
    }
    
    public ResourceLocation register(final String string, final DynamicTexture ejk) {
        Integer integer4 = (Integer)this.prefixRegister.get(string);
        if (integer4 == null) {
            integer4 = 1;
        }
        else {
            ++integer4;
        }
        this.prefixRegister.put(string, integer4);
        final ResourceLocation vk5 = new ResourceLocation(String.format("dynamic/%s_%d", new Object[] { string, integer4 }));
        this.register(vk5, ejk);
        return vk5;
    }
    
    public CompletableFuture<Void> preload(final ResourceLocation vk, final Executor executor) {
        if (!this.byPath.containsKey(vk)) {
            final PreloadedTexture ejp4 = new PreloadedTexture(this.resourceManager, vk, executor);
            this.byPath.put(vk, ejp4);
            return (CompletableFuture<Void>)ejp4.getFuture().thenRunAsync(() -> this.register(vk, ejp4), TextureManager::execute);
        }
        return (CompletableFuture<Void>)CompletableFuture.completedFuture(null);
    }
    
    private static void execute(final Runnable runnable) {
        Minecraft.getInstance().execute(() -> RenderSystem.recordRenderCall(runnable::run));
    }
    
    public void tick() {
        for (final Tickable ejw3 : this.tickableTextures) {
            ejw3.tick();
        }
    }
    
    public void release(final ResourceLocation vk) {
        final AbstractTexture eji3 = this.getTexture(vk);
        if (eji3 != null) {
            TextureUtil.releaseTextureId(eji3.getId());
        }
    }
    
    public void close() {
        this.byPath.forEach(this::safeClose);
        this.byPath.clear();
        this.tickableTextures.clear();
        this.prefixRegister.clear();
    }
    
    public CompletableFuture<Void> reload(final PreparationBarrier a, final ResourceManager acf, final ProfilerFiller ant3, final ProfilerFiller ant4, final Executor executor5, final Executor executor6) {
        return (CompletableFuture<Void>)CompletableFuture.allOf(new CompletableFuture[] { TitleScreen.preloadResources(this, executor5), this.preload(AbstractWidget.WIDGETS_LOCATION, executor5) }).thenCompose(a::wait).thenAcceptAsync(void3 -> {
            MissingTextureAtlasSprite.getTexture();
            RealmsMainScreen.updateTeaserImages(this.resourceManager);
            final Iterator<Map.Entry<ResourceLocation, AbstractTexture>> iterator5 = (Iterator<Map.Entry<ResourceLocation, AbstractTexture>>)this.byPath.entrySet().iterator();
            while (iterator5.hasNext()) {
                final Map.Entry<ResourceLocation, AbstractTexture> entry6 = (Map.Entry<ResourceLocation, AbstractTexture>)iterator5.next();
                final ResourceLocation vk7 = (ResourceLocation)entry6.getKey();
                final AbstractTexture eji8 = (AbstractTexture)entry6.getValue();
                if (eji8 == MissingTextureAtlasSprite.getTexture() && !vk7.equals(MissingTextureAtlasSprite.getLocation())) {
                    iterator5.remove();
                }
                else {
                    eji8.reset(this, acf, vk7, executor6);
                }
            }
        }, runnable -> RenderSystem.recordRenderCall(runnable::run));
    }
    
    static {
        LOGGER = LogManager.getLogger();
        INTENTIONAL_MISSING_TEXTURE = new ResourceLocation("");
    }
}

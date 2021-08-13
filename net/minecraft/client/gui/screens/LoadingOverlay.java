package net.minecraft.client.gui.screens;

import java.io.InputStream;
import net.minecraft.server.packs.VanillaPackResources;
import java.io.IOException;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.util.FastColor;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.util.Mth;
import net.minecraft.Util;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.texture.AbstractTexture;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class LoadingOverlay extends Overlay {
    private static final ResourceLocation MOJANG_STUDIOS_LOGO_LOCATION;
    private static final int BRAND_BACKGROUND;
    private static final int BRAND_BACKGROUND_NO_ALPHA;
    private final Minecraft minecraft;
    private final ReloadInstance reload;
    private final Consumer<Optional<Throwable>> onFinish;
    private final boolean fadeIn;
    private float currentProgress;
    private long fadeOutStart;
    private long fadeInStart;
    
    public LoadingOverlay(final Minecraft djw, final ReloadInstance acc, final Consumer<Optional<Throwable>> consumer, final boolean boolean4) {
        this.fadeOutStart = -1L;
        this.fadeInStart = -1L;
        this.minecraft = djw;
        this.reload = acc;
        this.onFinish = consumer;
        this.fadeIn = boolean4;
    }
    
    public static void registerTextures(final Minecraft djw) {
        djw.getTextureManager().register(LoadingOverlay.MOJANG_STUDIOS_LOGO_LOCATION, new LogoTexture());
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        final int integer4 = this.minecraft.getWindow().getGuiScaledWidth();
        final int integer5 = this.minecraft.getWindow().getGuiScaledHeight();
        final long long8 = Util.getMillis();
        if (this.fadeIn && (this.reload.isApplying() || this.minecraft.screen != null) && this.fadeInStart == -1L) {
            this.fadeInStart = long8;
        }
        final float float5 = (this.fadeOutStart > -1L) ? ((long8 - this.fadeOutStart) / 1000.0f) : -1.0f;
        final float float6 = (this.fadeInStart > -1L) ? ((long8 - this.fadeInStart) / 500.0f) : -1.0f;
        float float7;
        if (float5 >= 1.0f) {
            if (this.minecraft.screen != null) {
                this.minecraft.screen.render(dfj, 0, 0, float4);
            }
            final int integer6 = Mth.ceil((1.0f - Mth.clamp(float5 - 1.0f, 0.0f, 1.0f)) * 255.0f);
            GuiComponent.fill(dfj, 0, 0, integer4, integer5, LoadingOverlay.BRAND_BACKGROUND_NO_ALPHA | integer6 << 24);
            float7 = 1.0f - Mth.clamp(float5 - 1.0f, 0.0f, 1.0f);
        }
        else if (this.fadeIn) {
            if (this.minecraft.screen != null && float6 < 1.0f) {
                this.minecraft.screen.render(dfj, integer2, integer3, float4);
            }
            final int integer6 = Mth.ceil(Mth.clamp(float6, 0.15, 1.0) * 255.0);
            GuiComponent.fill(dfj, 0, 0, integer4, integer5, LoadingOverlay.BRAND_BACKGROUND_NO_ALPHA | integer6 << 24);
            float7 = Mth.clamp(float6, 0.0f, 1.0f);
        }
        else {
            GuiComponent.fill(dfj, 0, 0, integer4, integer5, LoadingOverlay.BRAND_BACKGROUND);
            float7 = 1.0f;
        }
        final int integer6 = (int)(this.minecraft.getWindow().getGuiScaledWidth() * 0.5);
        final int integer7 = (int)(this.minecraft.getWindow().getGuiScaledHeight() * 0.5);
        final double double15 = Math.min(this.minecraft.getWindow().getGuiScaledWidth() * 0.75, (double)this.minecraft.getWindow().getGuiScaledHeight()) * 0.25;
        final int integer8 = (int)(double15 * 0.5);
        final double double16 = double15 * 4.0;
        final int integer9 = (int)(double16 * 0.5);
        this.minecraft.getTextureManager().bind(LoadingOverlay.MOJANG_STUDIOS_LOGO_LOCATION);
        RenderSystem.enableBlend();
        RenderSystem.blendEquation(32774);
        RenderSystem.blendFunc(770, 1);
        RenderSystem.alphaFunc(516, 0.0f);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, float7);
        GuiComponent.blit(dfj, integer6 - integer9, integer7 - integer8, integer9, (int)double15, -0.0625f, 0.0f, 120, 60, 120, 120);
        GuiComponent.blit(dfj, integer6, integer7 - integer8, integer9, (int)double15, 0.0625f, 60.0f, 120, 60, 120, 120);
        RenderSystem.defaultBlendFunc();
        RenderSystem.defaultAlphaFunc();
        RenderSystem.disableBlend();
        final int integer10 = (int)(this.minecraft.getWindow().getGuiScaledHeight() * 0.8325);
        final float float8 = this.reload.getActualProgress();
        this.currentProgress = Mth.clamp(this.currentProgress * 0.95f + float8 * 0.050000012f, 0.0f, 1.0f);
        if (float5 < 1.0f) {
            this.drawProgressBar(dfj, integer4 / 2 - integer9, integer10 - 5, integer4 / 2 + integer9, integer10 + 5, 1.0f - Mth.clamp(float5, 0.0f, 1.0f));
        }
        if (float5 >= 2.0f) {
            this.minecraft.setOverlay(null);
        }
        if (this.fadeOutStart == -1L && this.reload.isDone()) {
            if (this.fadeIn) {
                if (float6 < 2.0f) {
                    return;
                }
            }
            try {
                this.reload.checkExceptions();
                this.onFinish.accept(Optional.empty());
            }
            catch (Throwable throwable23) {
                this.onFinish.accept(Optional.of((Object)throwable23));
            }
            this.fadeOutStart = Util.getMillis();
            if (this.minecraft.screen != null) {
                this.minecraft.screen.init(this.minecraft, this.minecraft.getWindow().getGuiScaledWidth(), this.minecraft.getWindow().getGuiScaledHeight());
            }
        }
    }
    
    private void drawProgressBar(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final float float6) {
        final int integer6 = Mth.ceil((integer4 - integer2 - 2) * this.currentProgress);
        final int integer7 = Math.round(float6 * 255.0f);
        final int integer8 = FastColor.ARGB32.color(integer7, 255, 255, 255);
        GuiComponent.fill(dfj, integer2 + 1, integer3, integer4 - 1, integer3 + 1, integer8);
        GuiComponent.fill(dfj, integer2 + 1, integer5, integer4 - 1, integer5 - 1, integer8);
        GuiComponent.fill(dfj, integer2, integer3, integer2 + 1, integer5, integer8);
        GuiComponent.fill(dfj, integer4, integer3, integer4 - 1, integer5, integer8);
        GuiComponent.fill(dfj, integer2 + 2, integer3 + 2, integer2 + integer6, integer5 - 2, integer8);
    }
    
    @Override
    public boolean isPauseScreen() {
        return true;
    }
    
    static {
        MOJANG_STUDIOS_LOGO_LOCATION = new ResourceLocation("textures/gui/title/mojangstudios.png");
        BRAND_BACKGROUND = FastColor.ARGB32.color(255, 239, 50, 61);
        BRAND_BACKGROUND_NO_ALPHA = (LoadingOverlay.BRAND_BACKGROUND & 0xFFFFFF);
    }
    
    static class LogoTexture extends SimpleTexture {
        public LogoTexture() {
            super(LoadingOverlay.MOJANG_STUDIOS_LOGO_LOCATION);
        }
        
        @Override
        protected TextureImage getTextureImage(final ResourceManager acf) {
            final Minecraft djw3 = Minecraft.getInstance();
            final VanillaPackResources abk4 = djw3.getClientPackSource().getVanillaPack();
            try (final InputStream inputStream5 = abk4.getResource(PackType.CLIENT_RESOURCES, LoadingOverlay.MOJANG_STUDIOS_LOGO_LOCATION)) {
                return new TextureImage(new TextureMetadataSection(true, true), NativeImage.read(inputStream5));
            }
            catch (IOException iOException5) {
                return new TextureImage(iOException5);
            }
        }
    }
}

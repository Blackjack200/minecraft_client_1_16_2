package net.minecraft.client.gui.screens;

import org.apache.logging.log4j.LogManager;
import com.google.common.util.concurrent.Runnables;
import java.util.Iterator;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.SharedConstants;
import java.util.function.BiConsumer;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.Mth;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.Util;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.storage.LevelSummary;
import net.minecraft.world.level.storage.LevelStorageSource;
import java.io.IOException;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.server.MinecraftServer;
import net.minecraft.core.RegistryAccess;
import net.minecraft.client.gui.screens.multiplayer.SafetyScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.util.FormattedCharSequence;
import java.util.List;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.client.gui.components.ImageButton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.client.renderer.texture.TextureManager;
import java.util.Random;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.client.gui.components.Button;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.CubeMap;
import org.apache.logging.log4j.Logger;

public class TitleScreen extends Screen {
    private static final Logger LOGGER;
    public static final CubeMap CUBE_MAP;
    private static final ResourceLocation PANORAMA_OVERLAY;
    private static final ResourceLocation ACCESSIBILITY_TEXTURE;
    private final boolean minceraftEasterEgg;
    @Nullable
    private String splash;
    private Button resetDemoButton;
    private static final ResourceLocation MINECRAFT_LOGO;
    private static final ResourceLocation MINECRAFT_EDITION;
    private boolean realmsNotificationsInitialized;
    private Screen realmsNotificationsScreen;
    private int copyrightWidth;
    private int copyrightX;
    private final PanoramaRenderer panorama;
    private final boolean fading;
    private long fadeInStart;
    
    public TitleScreen() {
        this(false);
    }
    
    public TitleScreen(final boolean boolean1) {
        super(new TranslatableComponent("narrator.screen.title"));
        this.panorama = new PanoramaRenderer(TitleScreen.CUBE_MAP);
        this.fading = boolean1;
        this.minceraftEasterEgg = (new Random().nextFloat() < 1.0E-4);
    }
    
    private boolean realmsNotificationsEnabled() {
        return this.minecraft.options.realmsNotifications && this.realmsNotificationsScreen != null;
    }
    
    @Override
    public void tick() {
        if (this.realmsNotificationsEnabled()) {
            this.realmsNotificationsScreen.tick();
        }
    }
    
    public static CompletableFuture<Void> preloadResources(final TextureManager ejv, final Executor executor) {
        return (CompletableFuture<Void>)CompletableFuture.allOf(new CompletableFuture[] { ejv.preload(TitleScreen.MINECRAFT_LOGO, executor), ejv.preload(TitleScreen.MINECRAFT_EDITION, executor), ejv.preload(TitleScreen.PANORAMA_OVERLAY, executor), TitleScreen.CUBE_MAP.preload(ejv, executor) });
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
    
    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
    
    @Override
    protected void init() {
        if (this.splash == null) {
            this.splash = this.minecraft.getSplashManager().getSplash();
        }
        this.copyrightWidth = this.font.width("Copyright Mojang AB. Do not distribute!");
        this.copyrightX = this.width - this.copyrightWidth - 2;
        final int integer2 = 24;
        final int integer3 = this.height / 4 + 48;
        if (this.minecraft.isDemo()) {
            this.createDemoMenuOptions(integer3, 24);
        }
        else {
            this.createNormalMenuOptions(integer3, 24);
        }
        this.<ImageButton>addButton(new ImageButton(this.width / 2 - 124, integer3 + 72 + 12, 20, 20, 0, 106, 20, Button.WIDGETS_LOCATION, 256, 256, dlg -> this.minecraft.setScreen(new LanguageSelectScreen(this, this.minecraft.options, this.minecraft.getLanguageManager())), new TranslatableComponent("narrator.button.language")));
        this.<Button>addButton(new Button(this.width / 2 - 100, integer3 + 72 + 12, 98, 20, new TranslatableComponent("menu.options"), dlg -> this.minecraft.setScreen(new OptionsScreen(this, this.minecraft.options))));
        this.<Button>addButton(new Button(this.width / 2 + 2, integer3 + 72 + 12, 98, 20, new TranslatableComponent("menu.quit"), dlg -> this.minecraft.stop()));
        this.<ImageButton>addButton(new ImageButton(this.width / 2 + 104, integer3 + 72 + 12, 20, 20, 0, 0, 20, TitleScreen.ACCESSIBILITY_TEXTURE, 32, 64, dlg -> this.minecraft.setScreen(new AccessibilityOptionsScreen(this, this.minecraft.options)), new TranslatableComponent("narrator.button.accessibility")));
        this.minecraft.setConnectedToRealms(false);
        if (this.minecraft.options.realmsNotifications && !this.realmsNotificationsInitialized) {
            final RealmsBridge eoc4 = new RealmsBridge();
            this.realmsNotificationsScreen = eoc4.getNotificationScreen(this);
            this.realmsNotificationsInitialized = true;
        }
        if (this.realmsNotificationsEnabled()) {
            this.realmsNotificationsScreen.init(this.minecraft, this.width, this.height);
        }
    }
    
    private void createNormalMenuOptions(final int integer1, final int integer2) {
        this.<Button>addButton(new Button(this.width / 2 - 100, integer1, 200, 20, new TranslatableComponent("menu.singleplayer"), dlg -> this.minecraft.setScreen(new SelectWorldScreen(this))));
        final boolean boolean4 = this.minecraft.allowsMultiplayer();
        final Button.OnTooltip b5 = boolean4 ? Button.NO_TOOLTIP : ((dlg, dfj, integer3, integer4) -> {
            if (!dlg.active) {
                this.renderTooltip(dfj, this.minecraft.font.split(new TranslatableComponent("title.multiplayer.disabled"), Math.max(this.width / 2 - 43, 170)), integer3, integer4);
            }
            return;
        });
        final Screen screen;
        final Screen doq3;
        this.<Button>addButton(new Button(this.width / 2 - 100, integer1 + integer2 * 1, 200, 20, new TranslatableComponent("menu.multiplayer"), dlg -> {
            if (this.minecraft.options.skipMultiplayerWarning) {
                // new(net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen.class)
                new JoinMultiplayerScreen(this);
            }
            else {
                // new(net.minecraft.client.gui.screens.multiplayer.SafetyScreen.class)
                new SafetyScreen(this);
            }
            doq3 = screen;
            this.minecraft.setScreen(doq3);
            return;
        }, b5)).active = boolean4;
        this.<Button>addButton(new Button(this.width / 2 - 100, integer1 + integer2 * 2, 200, 20, new TranslatableComponent("menu.online"), dlg -> this.realmsButtonClicked(), b5)).active = boolean4;
    }
    
    private void createDemoMenuOptions(final int integer1, final int integer2) {
        final boolean boolean4 = this.checkDemoWorldPresence();
        RegistryAccess.RegistryHolder b4;
        this.<Button>addButton(new Button(this.width / 2 - 100, integer1, 200, 20, new TranslatableComponent("menu.playdemo"), dlg -> {
            if (boolean4) {
                this.minecraft.loadLevel("Demo_World");
            }
            else {
                b4 = RegistryAccess.builtin();
                this.minecraft.createLevel("Demo_World", MinecraftServer.DEMO_SETTINGS, b4, WorldGenSettings.demoSettings(b4));
            }
            return;
        }));
        final LevelStorageSource cyd3;
        LevelStorageSource.LevelStorageAccess a4;
        LevelSummary cye6;
        Minecraft minecraft;
        TranslatableComponent nr2;
        final TranslatableComponent nr3;
        final Screen screen;
        final BooleanConsumer booleanConsumer;
        final Throwable t2;
        this.resetDemoButton = this.<Button>addButton(new Button(this.width / 2 - 100, integer1 + integer2 * 1, 200, 20, new TranslatableComponent("menu.resetdemo"), dlg -> {
            cyd3 = this.minecraft.getLevelSource();
            try {
                a4 = cyd3.createAccess("Demo_World");
                try {
                    cye6 = a4.getSummary();
                    if (cye6 != null) {
                        minecraft = this.minecraft;
                        // new(net.minecraft.client.gui.screens.ConfirmScreen.class)
                        this::confirmDemo;
                        nr2 = new TranslatableComponent("selectWorld.deleteQuestion");
                        new TranslatableComponent("selectWorld.deleteWarning", new Object[] { cye6.getLevelName() });
                        new ConfirmScreen(booleanConsumer, nr2, nr3, new TranslatableComponent("selectWorld.deleteButton"), CommonComponents.GUI_CANCEL);
                        minecraft.setScreen(screen);
                    }
                }
                catch (Throwable t) {
                    throw t;
                }
                finally {
                    if (a4 != null) {
                        if (t2 != null) {
                            try {
                                a4.close();
                            }
                            catch (Throwable t3) {
                                t2.addSuppressed(t3);
                            }
                        }
                        else {
                            a4.close();
                        }
                    }
                }
            }
            catch (IOException iOException4) {
                SystemToast.onWorldAccessFailure(this.minecraft, "Demo_World");
                TitleScreen.LOGGER.warn("Failed to access demo world", (Throwable)iOException4);
            }
            return;
        }));
        this.resetDemoButton.active = boolean4;
    }
    
    private boolean checkDemoWorldPresence() {
        try (final LevelStorageSource.LevelStorageAccess a2 = this.minecraft.getLevelSource().createAccess("Demo_World")) {
            return a2.getSummary() != null;
        }
        catch (IOException iOException2) {
            SystemToast.onWorldAccessFailure(this.minecraft, "Demo_World");
            TitleScreen.LOGGER.warn("Failed to read demo world data", (Throwable)iOException2);
            return false;
        }
    }
    
    private void realmsButtonClicked() {
        final RealmsBridge eoc2 = new RealmsBridge();
        eoc2.switchToRealms(this);
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        if (this.fadeInStart == 0L && this.fading) {
            this.fadeInStart = Util.getMillis();
        }
        final float float5 = this.fading ? ((Util.getMillis() - this.fadeInStart) / 1000.0f) : 1.0f;
        GuiComponent.fill(dfj, 0, 0, this.width, this.height, -1);
        this.panorama.render(float4, Mth.clamp(float5, 0.0f, 1.0f));
        final int integer4 = 274;
        final int integer5 = this.width / 2 - 137;
        final int integer6 = 30;
        this.minecraft.getTextureManager().bind(TitleScreen.PANORAMA_OVERLAY);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, this.fading ? ((float)Mth.ceil(Mth.clamp(float5, 0.0f, 1.0f))) : 1.0f);
        GuiComponent.blit(dfj, 0, 0, this.width, this.height, 0.0f, 0.0f, 16, 128, 16, 128);
        final float float6 = this.fading ? Mth.clamp(float5 - 1.0f, 0.0f, 1.0f) : 1.0f;
        final int integer7 = Mth.ceil(float6 * 255.0f) << 24;
        if ((integer7 & 0xFC000000) == 0x0) {
            return;
        }
        this.minecraft.getTextureManager().bind(TitleScreen.MINECRAFT_LOGO);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, float6);
        if (this.minceraftEasterEgg) {
            this.blitOutlineBlack(integer5, 30, (BiConsumer<Integer, Integer>)((integer2, integer3) -> {
                this.blit(dfj, integer2 + 0, integer3, 0, 0, 99, 44);
                this.blit(dfj, integer2 + 99, integer3, 129, 0, 27, 44);
                this.blit(dfj, integer2 + 99 + 26, integer3, 126, 0, 3, 44);
                this.blit(dfj, integer2 + 99 + 26 + 3, integer3, 99, 0, 26, 44);
                this.blit(dfj, integer2 + 155, integer3, 0, 45, 155, 44);
            }));
        }
        else {
            this.blitOutlineBlack(integer5, 30, (BiConsumer<Integer, Integer>)((integer2, integer3) -> {
                this.blit(dfj, integer2 + 0, integer3, 0, 0, 155, 44);
                this.blit(dfj, integer2 + 155, integer3, 0, 45, 155, 44);
            }));
        }
        this.minecraft.getTextureManager().bind(TitleScreen.MINECRAFT_EDITION);
        GuiComponent.blit(dfj, integer5 + 88, 67, 0.0f, 0.0f, 98, 14, 128, 16);
        if (this.splash != null) {
            RenderSystem.pushMatrix();
            RenderSystem.translatef((float)(this.width / 2 + 90), 70.0f, 0.0f);
            RenderSystem.rotatef(-20.0f, 0.0f, 0.0f, 1.0f);
            float float7 = 1.8f - Mth.abs(Mth.sin(Util.getMillis() % 1000L / 1000.0f * 6.2831855f) * 0.1f);
            float7 = float7 * 100.0f / (this.font.width(this.splash) + 32);
            RenderSystem.scalef(float7, float7, float7);
            GuiComponent.drawCenteredString(dfj, this.font, this.splash, 0, -8, 0xFFFF00 | integer7);
            RenderSystem.popMatrix();
        }
        String string12 = "Minecraft " + SharedConstants.getCurrentVersion().getName();
        if (this.minecraft.isDemo()) {
            string12 += " Demo";
        }
        else {
            string12 += ("release".equalsIgnoreCase(this.minecraft.getVersionType()) ? "" : ("/" + this.minecraft.getVersionType()));
        }
        if (this.minecraft.isProbablyModded()) {
            string12 += I18n.get("menu.modded");
        }
        GuiComponent.drawString(dfj, this.font, string12, 2, this.height - 10, 0xFFFFFF | integer7);
        GuiComponent.drawString(dfj, this.font, "Copyright Mojang AB. Do not distribute!", this.copyrightX, this.height - 10, 0xFFFFFF | integer7);
        if (integer2 > this.copyrightX && integer2 < this.copyrightX + this.copyrightWidth && integer3 > this.height - 10 && integer3 < this.height) {
            GuiComponent.fill(dfj, this.copyrightX, this.height - 1, this.copyrightX + this.copyrightWidth, this.height, 0xFFFFFF | integer7);
        }
        for (final AbstractWidget dle14 : this.buttons) {
            dle14.setAlpha(float6);
        }
        super.render(dfj, integer2, integer3, float4);
        if (this.realmsNotificationsEnabled() && float6 >= 1.0f) {
            this.realmsNotificationsScreen.render(dfj, integer2, integer3, float4);
        }
    }
    
    @Override
    public boolean mouseClicked(final double double1, final double double2, final int integer) {
        if (super.mouseClicked(double1, double2, integer)) {
            return true;
        }
        if (this.realmsNotificationsEnabled() && this.realmsNotificationsScreen.mouseClicked(double1, double2, integer)) {
            return true;
        }
        if (double1 > this.copyrightX && double1 < this.copyrightX + this.copyrightWidth && double2 > this.height - 10 && double2 < this.height) {
            this.minecraft.setScreen(new WinScreen(false, Runnables.doNothing()));
        }
        return false;
    }
    
    @Override
    public void removed() {
        if (this.realmsNotificationsScreen != null) {
            this.realmsNotificationsScreen.removed();
        }
    }
    
    private void confirmDemo(final boolean boolean1) {
        if (boolean1) {
            try (final LevelStorageSource.LevelStorageAccess a3 = this.minecraft.getLevelSource().createAccess("Demo_World")) {
                a3.deleteLevel();
            }
            catch (IOException iOException3) {
                SystemToast.onWorldDeleteFailure(this.minecraft, "Demo_World");
                TitleScreen.LOGGER.warn("Failed to delete demo world", (Throwable)iOException3);
            }
        }
        this.minecraft.setScreen(this);
    }
    
    static {
        LOGGER = LogManager.getLogger();
        CUBE_MAP = new CubeMap(new ResourceLocation("textures/gui/title/background/panorama"));
        PANORAMA_OVERLAY = new ResourceLocation("textures/gui/title/background/panorama_overlay.png");
        ACCESSIBILITY_TEXTURE = new ResourceLocation("textures/gui/accessibility.png");
        MINECRAFT_LOGO = new ResourceLocation("textures/gui/title/minecraft.png");
        MINECRAFT_EDITION = new ResourceLocation("textures/gui/title/edition.png");
    }
}

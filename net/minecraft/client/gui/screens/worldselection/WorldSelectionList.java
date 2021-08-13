package net.minecraft.client.gui.screens.worldselection;

import net.minecraft.client.gui.screens.ProgressScreen;
import java.io.InputStream;
import net.minecraft.client.renderer.texture.AbstractTexture;
import org.apache.commons.lang3.Validate;
import com.mojang.blaze3d.platform.NativeImage;
import java.io.FileInputStream;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import java.nio.file.Path;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.client.gui.screens.AlertScreen;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.WorldData;
import net.minecraft.server.packs.resources.ResourceManager;
import com.mojang.datafixers.util.Function4;
import net.minecraft.world.level.DataPackConfig;
import java.util.function.Function;
import net.minecraft.core.RegistryAccess;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.client.gui.screens.BackupConfirmScreen;
import java.io.IOException;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.Font;
import net.minecraft.SharedConstants;
import net.minecraft.util.FormattedCharSequence;
import com.google.common.collect.ImmutableList;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.resources.language.I18n;
import org.apache.commons.lang3.StringUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.google.common.hash.Hashing;
import net.minecraft.Util;
import net.minecraft.client.renderer.texture.DynamicTexture;
import java.io.File;
import net.minecraft.ChatFormatting;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.client.gui.components.AbstractSelectionList;
import java.util.Date;
import net.minecraft.client.gui.chat.NarratorChatListener;
import java.util.Iterator;
import net.minecraft.world.level.storage.LevelStorageSource;
import java.util.Locale;
import java.util.Collections;
import net.minecraft.world.level.storage.LevelStorageException;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.ErrorScreen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import javax.annotation.Nullable;
import net.minecraft.world.level.storage.LevelSummary;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import java.text.DateFormat;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.gui.components.ObjectSelectionList;

public class WorldSelectionList extends ObjectSelectionList<WorldListEntry> {
    private static final Logger LOGGER;
    private static final DateFormat DATE_FORMAT;
    private static final ResourceLocation ICON_MISSING;
    private static final ResourceLocation ICON_OVERLAY_LOCATION;
    private static final Component FROM_NEWER_TOOLTIP_1;
    private static final Component FROM_NEWER_TOOLTIP_2;
    private static final Component SNAPSHOT_TOOLTIP_1;
    private static final Component SNAPSHOT_TOOLTIP_2;
    private static final Component WORLD_LOCKED_TOOLTIP;
    private final SelectWorldScreen screen;
    @Nullable
    private List<LevelSummary> cachedList;
    
    public WorldSelectionList(final SelectWorldScreen dsb, final Minecraft djw, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final Supplier<String> supplier, @Nullable final WorldSelectionList dse) {
        super(djw, integer3, integer4, integer5, integer6, integer7);
        this.screen = dsb;
        if (dse != null) {
            this.cachedList = dse.cachedList;
        }
        this.refreshList(supplier, false);
    }
    
    public void refreshList(final Supplier<String> supplier, final boolean boolean2) {
        this.clearEntries();
        final LevelStorageSource cyd4 = this.minecraft.getLevelSource();
        Label_0091: {
            if (this.cachedList != null) {
                if (!boolean2) {
                    break Label_0091;
                }
            }
            try {
                this.cachedList = cyd4.getLevelList();
            }
            catch (LevelStorageException cyc5) {
                WorldSelectionList.LOGGER.error("Couldn't load level list", (Throwable)cyc5);
                this.minecraft.setScreen(new ErrorScreen(new TranslatableComponent("selectWorld.unable_to_load"), new TextComponent(cyc5.getMessage())));
                return;
            }
            Collections.sort((List)this.cachedList);
        }
        if (this.cachedList.isEmpty()) {
            this.minecraft.setScreen(CreateWorldScreen.create(null));
            return;
        }
        final String string5 = ((String)supplier.get()).toLowerCase(Locale.ROOT);
        for (final LevelSummary cye7 : this.cachedList) {
            if (cye7.getLevelName().toLowerCase(Locale.ROOT).contains((CharSequence)string5) || cye7.getLevelId().toLowerCase(Locale.ROOT).contains((CharSequence)string5)) {
                this.addEntry(new WorldListEntry(this, cye7));
            }
        }
    }
    
    @Override
    protected int getScrollbarPosition() {
        return super.getScrollbarPosition() + 20;
    }
    
    @Override
    public int getRowWidth() {
        return super.getRowWidth() + 50;
    }
    
    @Override
    protected boolean isFocused() {
        return this.screen.getFocused() == this;
    }
    
    @Override
    public void setSelected(@Nullable final WorldListEntry a) {
        super.setSelected(a);
        if (a != null) {
            final LevelSummary cye3 = a.summary;
            NarratorChatListener.INSTANCE.sayNow(new TranslatableComponent("narrator.select", new Object[] { new TranslatableComponent("narrator.select.world", new Object[] { cye3.getLevelName(), new Date(cye3.getLastPlayed()), cye3.isHardcore() ? new TranslatableComponent("gameMode.hardcore") : new TranslatableComponent("gameMode." + cye3.getGameMode().getName()), cye3.hasCheats() ? new TranslatableComponent("selectWorld.cheats") : TextComponent.EMPTY, cye3.getWorldVersionName() }) }).getString());
        }
        this.screen.updateButtonStatus(a != null && !a.summary.isLocked());
    }
    
    @Override
    protected void moveSelection(final SelectionDirection b) {
        this.moveSelection(b, (java.util.function.Predicate<WorldListEntry>)(a -> !a.summary.isLocked()));
    }
    
    public Optional<WorldListEntry> getSelectedOpt() {
        return (Optional<WorldListEntry>)Optional.ofNullable(((AbstractSelectionList<Object>)this).getSelected());
    }
    
    public SelectWorldScreen getScreen() {
        return this.screen;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        DATE_FORMAT = (DateFormat)new SimpleDateFormat();
        ICON_MISSING = new ResourceLocation("textures/misc/unknown_server.png");
        ICON_OVERLAY_LOCATION = new ResourceLocation("textures/gui/world_selection.png");
        FROM_NEWER_TOOLTIP_1 = new TranslatableComponent("selectWorld.tooltip.fromNewerVersion1").withStyle(ChatFormatting.RED);
        FROM_NEWER_TOOLTIP_2 = new TranslatableComponent("selectWorld.tooltip.fromNewerVersion2").withStyle(ChatFormatting.RED);
        SNAPSHOT_TOOLTIP_1 = new TranslatableComponent("selectWorld.tooltip.snapshot1").withStyle(ChatFormatting.GOLD);
        SNAPSHOT_TOOLTIP_2 = new TranslatableComponent("selectWorld.tooltip.snapshot2").withStyle(ChatFormatting.GOLD);
        WORLD_LOCKED_TOOLTIP = new TranslatableComponent("selectWorld.locked").withStyle(ChatFormatting.RED);
    }
    
    public final class WorldListEntry extends Entry<WorldListEntry> implements AutoCloseable {
        private final Minecraft minecraft;
        private final SelectWorldScreen screen;
        private final LevelSummary summary;
        private final ResourceLocation iconLocation;
        private File iconFile;
        @Nullable
        private final DynamicTexture icon;
        private long lastClickTime;
        
        public WorldListEntry(final WorldSelectionList dse2, final LevelSummary cye) {
            this.screen = dse2.getScreen();
            this.summary = cye;
            this.minecraft = Minecraft.getInstance();
            final String string5 = cye.getLevelId();
            this.iconLocation = new ResourceLocation("minecraft", "worlds/" + Util.sanitizeName(string5, ResourceLocation::validPathChar) + "/" + Hashing.sha1().hashUnencodedChars((CharSequence)string5) + "/icon");
            this.iconFile = cye.getIcon();
            if (!this.iconFile.isFile()) {
                this.iconFile = null;
            }
            this.icon = this.loadServerIcon();
        }
        
        public void render(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final boolean boolean9, final float float10) {
            String string12 = this.summary.getLevelName();
            final String string13 = this.summary.getLevelId() + " (" + WorldSelectionList.DATE_FORMAT.format(new Date(this.summary.getLastPlayed())) + ")";
            if (StringUtils.isEmpty((CharSequence)string12)) {
                string12 = I18n.get("selectWorld.world") + " " + (integer2 + 1);
            }
            final Component nr14 = this.summary.getInfo();
            this.minecraft.font.draw(dfj, string12, (float)(integer4 + 32 + 3), (float)(integer3 + 1), 16777215);
            final Font font = this.minecraft.font;
            final String string14 = string13;
            final float float11 = (float)(integer4 + 32 + 3);
            this.minecraft.font.getClass();
            font.draw(dfj, string14, float11, (float)(integer3 + 9 + 3), 8421504);
            final Font font2 = this.minecraft.font;
            final Component nr15 = nr14;
            final float float12 = (float)(integer4 + 32 + 3);
            this.minecraft.font.getClass();
            final int n = integer3 + 9;
            this.minecraft.font.getClass();
            font2.draw(dfj, nr15, float12, (float)(n + 9 + 3), 8421504);
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
            this.minecraft.getTextureManager().bind((this.icon != null) ? this.iconLocation : WorldSelectionList.ICON_MISSING);
            RenderSystem.enableBlend();
            GuiComponent.blit(dfj, integer4, integer3, 0.0f, 0.0f, 32, 32, 32, 32);
            RenderSystem.disableBlend();
            if (this.minecraft.options.touchscreen || boolean9) {
                this.minecraft.getTextureManager().bind(WorldSelectionList.ICON_OVERLAY_LOCATION);
                GuiComponent.fill(dfj, integer4, integer3, integer4 + 32, integer3 + 32, -1601138544);
                RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
                final int integer9 = integer7 - integer4;
                final boolean boolean10 = integer9 < 32;
                final int integer10 = boolean10 ? 32 : 0;
                if (this.summary.isLocked()) {
                    GuiComponent.blit(dfj, integer4, integer3, 96.0f, (float)integer10, 32, 32, 256, 256);
                    if (boolean10) {
                        this.screen.setToolTip(this.minecraft.font.split(WorldSelectionList.WORLD_LOCKED_TOOLTIP, 175));
                    }
                }
                else if (this.summary.markVersionInList()) {
                    GuiComponent.blit(dfj, integer4, integer3, 32.0f, (float)integer10, 32, 32, 256, 256);
                    if (this.summary.askToOpenWorld()) {
                        GuiComponent.blit(dfj, integer4, integer3, 96.0f, (float)integer10, 32, 32, 256, 256);
                        if (boolean10) {
                            this.screen.setToolTip((List<FormattedCharSequence>)ImmutableList.of(WorldSelectionList.FROM_NEWER_TOOLTIP_1.getVisualOrderText(), WorldSelectionList.FROM_NEWER_TOOLTIP_2.getVisualOrderText()));
                        }
                    }
                    else if (!SharedConstants.getCurrentVersion().isStable()) {
                        GuiComponent.blit(dfj, integer4, integer3, 64.0f, (float)integer10, 32, 32, 256, 256);
                        if (boolean10) {
                            this.screen.setToolTip((List<FormattedCharSequence>)ImmutableList.of(WorldSelectionList.SNAPSHOT_TOOLTIP_1.getVisualOrderText(), WorldSelectionList.SNAPSHOT_TOOLTIP_2.getVisualOrderText()));
                        }
                    }
                }
                else {
                    GuiComponent.blit(dfj, integer4, integer3, 0.0f, (float)integer10, 32, 32, 256, 256);
                }
            }
        }
        
        public boolean mouseClicked(final double double1, final double double2, final int integer) {
            if (this.summary.isLocked()) {
                return true;
            }
            WorldSelectionList.this.setSelected(this);
            this.screen.updateButtonStatus(WorldSelectionList.this.getSelectedOpt().isPresent());
            if (double1 - AbstractSelectionList.this.getRowLeft() <= 32.0) {
                this.joinWorld();
                return true;
            }
            if (Util.getMillis() - this.lastClickTime < 250L) {
                this.joinWorld();
                return true;
            }
            this.lastClickTime = Util.getMillis();
            return false;
        }
        
        public void joinWorld() {
            if (this.summary.isLocked()) {
                return;
            }
            if (this.summary.shouldBackup()) {
                final Component nr2 = new TranslatableComponent("selectWorld.backupQuestion");
                final Component nr3 = new TranslatableComponent("selectWorld.backupWarning", new Object[] { this.summary.getWorldVersionName(), SharedConstants.getCurrentVersion().getName() });
                String string4;
                LevelStorageSource.LevelStorageAccess a5;
                final Throwable t2;
                this.minecraft.setScreen(new BackupConfirmScreen(this.screen, (boolean1, boolean2) -> {
                    if (boolean1) {
                        string4 = this.summary.getLevelId();
                        try {
                            a5 = this.minecraft.getLevelSource().createAccess(string4);
                            try {
                                EditWorldScreen.makeBackupAndShowToast(a5);
                            }
                            catch (Throwable t) {
                                throw t;
                            }
                            finally {
                                if (a5 != null) {
                                    if (t2 != null) {
                                        try {
                                            a5.close();
                                        }
                                        catch (Throwable t3) {
                                            t2.addSuppressed(t3);
                                        }
                                    }
                                    else {
                                        a5.close();
                                    }
                                }
                            }
                        }
                        catch (IOException iOException5) {
                            SystemToast.onWorldAccessFailure(this.minecraft, string4);
                            WorldSelectionList.LOGGER.error("Failed to backup level {}", string4, iOException5);
                        }
                    }
                    this.loadWorld();
                }, nr2, nr3, false));
            }
            else if (this.summary.askToOpenWorld()) {
                this.minecraft.setScreen(new ConfirmScreen(boolean1 -> {
                    if (boolean1) {
                        try {
                            this.loadWorld();
                        }
                        catch (Exception exception3) {
                            WorldSelectionList.LOGGER.error("Failure to open 'future world'", (Throwable)exception3);
                            this.minecraft.setScreen(new AlertScreen(() -> this.minecraft.setScreen(this.screen), new TranslatableComponent("selectWorld.futureworld.error.title"), new TranslatableComponent("selectWorld.futureworld.error.text")));
                        }
                    }
                    else {
                        this.minecraft.setScreen(this.screen);
                    }
                }, new TranslatableComponent("selectWorld.versionQuestion"), new TranslatableComponent("selectWorld.versionWarning", new Object[] { this.summary.getWorldVersionName(), new TranslatableComponent("selectWorld.versionJoinButton"), CommonComponents.GUI_CANCEL })));
            }
            else {
                this.loadWorld();
            }
        }
        
        public void deleteWorld() {
            this.minecraft.setScreen(new ConfirmScreen(boolean1 -> {
                if (boolean1) {
                    this.minecraft.setScreen(new ProgressScreen());
                    final LevelStorageSource cyd3 = this.minecraft.getLevelSource();
                    final String string4 = this.summary.getLevelId();
                    try (final LevelStorageSource.LevelStorageAccess a5 = cyd3.createAccess(string4)) {
                        a5.deleteLevel();
                    }
                    catch (IOException iOException5) {
                        SystemToast.onWorldDeleteFailure(this.minecraft, string4);
                        WorldSelectionList.LOGGER.error("Failed to delete world {}", string4, iOException5);
                    }
                    WorldSelectionList.this.refreshList((Supplier<String>)(() -> this.screen.searchBox.getValue()), true);
                }
                this.minecraft.setScreen(this.screen);
            }, new TranslatableComponent("selectWorld.deleteQuestion"), new TranslatableComponent("selectWorld.deleteWarning", new Object[] { this.summary.getLevelName() }), new TranslatableComponent("selectWorld.deleteButton"), CommonComponents.GUI_CANCEL));
        }
        
        public void editWorld() {
            final String string2 = this.summary.getLevelId();
            try {
                final LevelStorageSource.LevelStorageAccess a3 = this.minecraft.getLevelSource().createAccess(string2);
                this.minecraft.setScreen(new EditWorldScreen(boolean3 -> {
                    try {
                        a3.close();
                    }
                    catch (IOException iOException5) {
                        WorldSelectionList.LOGGER.error("Failed to unlock level {}", string2, iOException5);
                    }
                    if (boolean3) {
                        WorldSelectionList.this.refreshList((Supplier<String>)(() -> this.screen.searchBox.getValue()), true);
                    }
                    this.minecraft.setScreen(this.screen);
                }, a3));
            }
            catch (IOException iOException3) {
                SystemToast.onWorldAccessFailure(this.minecraft, string2);
                WorldSelectionList.LOGGER.error("Failed to access level {}", string2, iOException3);
                WorldSelectionList.this.refreshList((Supplier<String>)(() -> this.screen.searchBox.getValue()), true);
            }
        }
        
        public void recreateWorld() {
            this.queueLoadScreen();
            final RegistryAccess.RegistryHolder b2 = RegistryAccess.builtin();
            try (final LevelStorageSource.LevelStorageAccess a3 = this.minecraft.getLevelSource().createAccess(this.summary.getLevelId());
                 final Minecraft.ServerStem b3 = this.minecraft.makeServerStem(b2, (Function<LevelStorageSource.LevelStorageAccess, DataPackConfig>)Minecraft::loadDataPacks, (Function4<LevelStorageSource.LevelStorageAccess, RegistryAccess.RegistryHolder, ResourceManager, DataPackConfig, WorldData>)Minecraft::loadWorldData, false, a3)) {
                final LevelSettings brx7 = b3.worldData().getLevelSettings();
                final DataPackConfig brh8 = brx7.getDataPackConfig();
                final WorldGenSettings cht9 = b3.worldData().worldGenSettings();
                final Path path10 = CreateWorldScreen.createTempDataPackDirFromExistingWorld(a3.getLevelPath(LevelResource.DATAPACK_DIR), this.minecraft);
                if (cht9.isOldCustomizedWorld()) {
                    this.minecraft.setScreen(new ConfirmScreen(boolean6 -> this.minecraft.setScreen(boolean6 ? new CreateWorldScreen(this.screen, brx7, cht9, path10, brh8, b2) : this.screen), new TranslatableComponent("selectWorld.recreate.customized.title"), new TranslatableComponent("selectWorld.recreate.customized.text"), CommonComponents.GUI_PROCEED, CommonComponents.GUI_CANCEL));
                }
                else {
                    this.minecraft.setScreen(new CreateWorldScreen(this.screen, brx7, cht9, path10, brh8, b2));
                }
            }
            catch (Exception exception3) {
                WorldSelectionList.LOGGER.error("Unable to recreate world", (Throwable)exception3);
                this.minecraft.setScreen(new AlertScreen(() -> this.minecraft.setScreen(this.screen), new TranslatableComponent("selectWorld.recreate.error.title"), new TranslatableComponent("selectWorld.recreate.error.text")));
            }
        }
        
        private void loadWorld() {
            this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            if (this.minecraft.getLevelSource().levelExists(this.summary.getLevelId())) {
                this.queueLoadScreen();
                this.minecraft.loadLevel(this.summary.getLevelId());
            }
        }
        
        private void queueLoadScreen() {
            this.minecraft.forceSetScreen(new GenericDirtMessageScreen(new TranslatableComponent("selectWorld.data_read")));
        }
        
        @Nullable
        private DynamicTexture loadServerIcon() {
            final boolean boolean2 = this.iconFile != null && this.iconFile.isFile();
            if (boolean2) {
                try (final InputStream inputStream3 = (InputStream)new FileInputStream(this.iconFile)) {
                    final NativeImage deq5 = NativeImage.read(inputStream3);
                    Validate.validState(deq5.getWidth() == 64, "Must be 64 pixels wide", new Object[0]);
                    Validate.validState(deq5.getHeight() == 64, "Must be 64 pixels high", new Object[0]);
                    final DynamicTexture ejk6 = new DynamicTexture(deq5);
                    this.minecraft.getTextureManager().register(this.iconLocation, ejk6);
                    return ejk6;
                }
                catch (Throwable throwable3) {
                    WorldSelectionList.LOGGER.error("Invalid icon for world {}", this.summary.getLevelId(), throwable3);
                    this.iconFile = null;
                    return null;
                }
            }
            this.minecraft.getTextureManager().release(this.iconLocation);
            return null;
        }
        
        public void close() {
            if (this.icon != null) {
                this.icon.close();
            }
        }
    }
}

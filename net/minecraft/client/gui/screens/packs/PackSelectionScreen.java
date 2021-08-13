package net.minecraft.client.gui.screens.packs;

import java.nio.file.WatchKey;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.util.Iterator;
import java.nio.file.DirectoryStream;
import java.nio.file.LinkOption;
import java.nio.file.WatchService;
import net.minecraft.ChatFormatting;
import org.apache.logging.log4j.LogManager;
import java.nio.file.Files;
import java.nio.file.FileVisitOption;
import java.io.InputStream;
import net.minecraft.server.packs.PackResources;
import java.io.FileNotFoundException;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import com.mojang.blaze3d.platform.NativeImage;
import com.google.common.hash.Hashing;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.network.chat.TextComponent;
import java.util.stream.Collectors;
import net.minecraft.client.gui.components.toasts.SystemToast;
import org.apache.commons.lang3.mutable.MutableBoolean;
import java.nio.file.Path;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.stream.Stream;
import java.io.IOException;
import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.server.packs.repository.Pack;
import java.util.function.Function;
import com.google.common.collect.Maps;
import java.util.function.Consumer;
import net.minecraft.server.packs.repository.PackRepository;
import java.util.Map;
import net.minecraft.client.gui.components.Button;
import java.io.File;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.gui.screens.Screen;

public class PackSelectionScreen extends Screen {
    private static final Logger LOGGER;
    private static final Component DRAG_AND_DROP;
    private static final Component DIRECTORY_BUTTON_TOOLTIP;
    private static final ResourceLocation DEFAULT_ICON;
    private final PackSelectionModel model;
    private final Screen lastScreen;
    @Nullable
    private Watcher watcher;
    private long ticksToReload;
    private TransferableSelectionList availablePackList;
    private TransferableSelectionList selectedPackList;
    private final File packDir;
    private Button doneButton;
    private final Map<String, ResourceLocation> packIcons;
    
    public PackSelectionScreen(final Screen doq, final PackRepository abu, final Consumer<PackRepository> consumer, final File file, final Component nr) {
        super(nr);
        this.packIcons = (Map<String, ResourceLocation>)Maps.newHashMap();
        this.lastScreen = doq;
        this.model = new PackSelectionModel(this::populateLists, (Function<Pack, ResourceLocation>)this::getPackIcon, abu, consumer);
        this.packDir = file;
        this.watcher = Watcher.create(file);
    }
    
    @Override
    public void onClose() {
        this.model.commit();
        this.minecraft.setScreen(this.lastScreen);
        this.closeWatcher();
    }
    
    private void closeWatcher() {
        if (this.watcher != null) {
            try {
                this.watcher.close();
                this.watcher = null;
            }
            catch (Exception ex) {}
        }
    }
    
    @Override
    protected void init() {
        this.doneButton = this.<Button>addButton(new Button(this.width / 2 + 4, this.height - 48, 150, 20, CommonComponents.GUI_DONE, dlg -> this.onClose()));
        this.<Button>addButton(new Button(this.width / 2 - 154, this.height - 48, 150, 20, new TranslatableComponent("pack.openFolder"), dlg -> Util.getPlatform().openFile(this.packDir), (dlg, dfj, integer3, integer4) -> this.renderTooltip(dfj, PackSelectionScreen.DIRECTORY_BUTTON_TOOLTIP, integer3, integer4)));
        (this.availablePackList = new TransferableSelectionList(this.minecraft, 200, this.height, new TranslatableComponent("pack.available.title"))).setLeftPos(this.width / 2 - 4 - 200);
        this.children.add(this.availablePackList);
        (this.selectedPackList = new TransferableSelectionList(this.minecraft, 200, this.height, new TranslatableComponent("pack.selected.title"))).setLeftPos(this.width / 2 + 4);
        this.children.add(this.selectedPackList);
        this.reload();
    }
    
    @Override
    public void tick() {
        if (this.watcher != null) {
            try {
                if (this.watcher.pollForChanges()) {
                    this.ticksToReload = 20L;
                }
            }
            catch (IOException iOException2) {
                PackSelectionScreen.LOGGER.warn("Failed to poll for directory {} changes, stopping", this.packDir);
                this.closeWatcher();
            }
        }
        if (this.ticksToReload > 0L) {
            final long ticksToReload = this.ticksToReload - 1L;
            this.ticksToReload = ticksToReload;
            if (ticksToReload == 0L) {
                this.reload();
            }
        }
    }
    
    private void populateLists() {
        this.updateList(this.selectedPackList, this.model.getSelected());
        this.updateList(this.availablePackList, this.model.getUnselected());
        this.doneButton.active = !this.selectedPackList.children().isEmpty();
    }
    
    private void updateList(final TransferableSelectionList drg, final Stream<PackSelectionModel.Entry> stream) {
        drg.children().clear();
        stream.forEach(a -> drg.children().add(new TransferableSelectionList.PackEntry(this.minecraft, drg, this, a)));
    }
    
    private void reload() {
        this.model.findNewPacks();
        this.populateLists();
        this.ticksToReload = 0L;
        this.packIcons.clear();
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderDirtBackground(0);
        this.availablePackList.render(dfj, integer2, integer3, float4);
        this.selectedPackList.render(dfj, integer2, integer3, float4);
        GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, 8, 16777215);
        GuiComponent.drawCenteredString(dfj, this.font, PackSelectionScreen.DRAG_AND_DROP, this.width / 2, 20, 16777215);
        super.render(dfj, integer2, integer3, float4);
    }
    
    protected static void copyPacks(final Minecraft djw, final List<Path> list, final Path path) {
        final MutableBoolean mutableBoolean4 = new MutableBoolean();
        list.forEach(path3 -> {
            try (final Stream<Path> stream4 = (Stream<Path>)Files.walk(path3, new FileVisitOption[0])) {
                stream4.forEach(path4 -> {
                    try {
                        Util.copyBetweenDirs(path3.getParent(), path, path4);
                    }
                    catch (IOException iOException5) {
                        PackSelectionScreen.LOGGER.warn("Failed to copy datapack file  from {} to {}", path4, path, iOException5);
                        mutableBoolean4.setTrue();
                    }
                });
            }
            catch (IOException iOException4) {
                PackSelectionScreen.LOGGER.warn("Failed to copy datapack file from {} to {}", path3, path);
                mutableBoolean4.setTrue();
            }
        });
        if (mutableBoolean4.isTrue()) {
            SystemToast.onPackCopyFailure(djw, path.toString());
        }
    }
    
    @Override
    public void onFilesDrop(final List<Path> list) {
        final String string3 = (String)list.stream().map(Path::getFileName).map(Path::toString).collect(Collectors.joining(", "));
        this.minecraft.setScreen(new ConfirmScreen(boolean2 -> {
            if (boolean2) {
                copyPacks(this.minecraft, list, this.packDir.toPath());
                this.reload();
            }
            this.minecraft.setScreen(this);
        }, new TranslatableComponent("pack.dropConfirm"), new TextComponent(string3)));
    }
    
    private ResourceLocation loadPackIcon(final TextureManager ejv, final Pack abs) {
        try (final PackResources abh4 = abs.open();
             final InputStream inputStream6 = abh4.getRootResource("pack.png")) {
            final String string8 = abs.getId();
            final ResourceLocation vk9 = new ResourceLocation("minecraft", "pack/" + Util.sanitizeName(string8, ResourceLocation::validPathChar) + "/" + Hashing.sha1().hashUnencodedChars((CharSequence)string8) + "/icon");
            final NativeImage deq10 = NativeImage.read(inputStream6);
            ejv.register(vk9, new DynamicTexture(deq10));
            return vk9;
        }
        catch (FileNotFoundException ex) {}
        catch (Exception exception4) {
            PackSelectionScreen.LOGGER.warn("Failed to load icon from pack {}", abs.getId(), exception4);
        }
        return PackSelectionScreen.DEFAULT_ICON;
    }
    
    private ResourceLocation getPackIcon(final Pack abs) {
        return (ResourceLocation)this.packIcons.computeIfAbsent(abs.getId(), string -> this.loadPackIcon(this.minecraft.getTextureManager(), abs));
    }
    
    static {
        LOGGER = LogManager.getLogger();
        DRAG_AND_DROP = new TranslatableComponent("pack.dropInfo").withStyle(ChatFormatting.DARK_GRAY);
        DIRECTORY_BUTTON_TOOLTIP = new TranslatableComponent("pack.folderInfo");
        DEFAULT_ICON = new ResourceLocation("textures/misc/unknown_pack.png");
    }
    
    static class Watcher implements AutoCloseable {
        private final WatchService watcher;
        private final Path packPath;
        
        public Watcher(final File file) throws IOException {
            this.packPath = file.toPath();
            this.watcher = this.packPath.getFileSystem().newWatchService();
            try {
                this.watchDir(this.packPath);
                try (final DirectoryStream<Path> directoryStream3 = (DirectoryStream<Path>)Files.newDirectoryStream(this.packPath)) {
                    for (final Path path6 : directoryStream3) {
                        if (Files.isDirectory(path6, new LinkOption[] { LinkOption.NOFOLLOW_LINKS })) {
                            this.watchDir(path6);
                        }
                    }
                }
            }
            catch (Exception exception3) {
                this.watcher.close();
                throw exception3;
            }
        }
        
        @Nullable
        public static Watcher create(final File file) {
            try {
                return new Watcher(file);
            }
            catch (IOException iOException2) {
                PackSelectionScreen.LOGGER.warn("Failed to initialize pack directory {} monitoring", file, iOException2);
                return null;
            }
        }
        
        private void watchDir(final Path path) throws IOException {
            path.register(this.watcher, new WatchEvent.Kind[] { StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY });
        }
        
        public boolean pollForChanges() throws IOException {
            boolean boolean2 = false;
            WatchKey watchKey3;
            while ((watchKey3 = this.watcher.poll()) != null) {
                final List<WatchEvent<?>> list4 = (List<WatchEvent<?>>)watchKey3.pollEvents();
                for (final WatchEvent<?> watchEvent6 : list4) {
                    boolean2 = true;
                    if (watchKey3.watchable() == this.packPath && watchEvent6.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                        final Path path7 = this.packPath.resolve((Path)watchEvent6.context());
                        if (!Files.isDirectory(path7, new LinkOption[] { LinkOption.NOFOLLOW_LINKS })) {
                            continue;
                        }
                        this.watchDir(path7);
                    }
                }
                watchKey3.reset();
            }
            return boolean2;
        }
        
        public void close() throws IOException {
            this.watcher.close();
        }
    }
}

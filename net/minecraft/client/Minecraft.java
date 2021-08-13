package net.minecraft.client;

import org.apache.logging.log4j.LogManager;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Recipe;
import com.mojang.serialization.DataResult;
import net.minecraft.world.level.storage.PrimaryLevelData;
import com.google.gson.JsonElement;
import net.minecraft.resources.RegistryWriteOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.server.level.progress.ProcessorChunkProgressListener;
import java.util.concurrent.CompletionStage;
import net.minecraft.client.resources.LegacyPackResourcesAdapter;
import net.minecraft.client.resources.PackResourcesAdapterV4;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.sounds.Musics;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.sounds.Music;
import com.mojang.authlib.GameProfile;
import com.google.common.collect.Multimap;
import net.minecraft.server.packs.repository.Pack;
import java.nio.ByteOrder;
import com.mojang.blaze3d.platform.GlUtil;
import net.minecraft.CrashReportDetail;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.PlayerHeadItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import com.mojang.authlib.AuthenticationService;
import net.minecraft.client.gui.screens.ProgressScreen;
import java.util.concurrent.ExecutionException;
import net.minecraft.server.ServerResources;
import net.minecraft.commands.Commands;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.client.gui.screens.BackupConfirmScreen;
import net.minecraft.client.gui.screens.worldselection.EditWorldScreen;
import java.net.SocketAddress;
import com.mojang.authlib.GameProfileRepository;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.PacketListener;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.server.players.GameProfileCache;
import com.mojang.serialization.Lifecycle;
import net.minecraft.client.gui.screens.DatapackLoadFailureScreen;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.LevelSettings;
import com.mojang.datafixers.util.Function4;
import com.mojang.serialization.DynamicOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryReadOps;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.storage.WorldData;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.entity.player.ChatVisiblity;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.CrashReportCategory;
import net.minecraft.client.gui.screens.InBedChatScreen;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.client.gui.screens.PauseScreen;
import com.mojang.blaze3d.vertex.BufferBuilder;
import java.text.DecimalFormatSymbols;
import java.text.DecimalFormat;
import net.minecraft.util.Mth;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.util.profiling.ResultField;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.gui.screens.MenuScreens;
import java.util.Locale;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import net.minecraft.server.Bootstrap;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import net.minecraft.client.searchtree.MutableSearchTree;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.core.Registry;
import net.minecraft.core.NonNullList;
import net.minecraft.client.searchtree.ReloadableIdSearchTree;
import java.util.stream.Stream;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.searchtree.ReloadableSearchTree;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.ReportedException;
import net.minecraft.client.gui.screens.OutOfMemoryScreen;
import net.minecraft.util.profiling.SingleTickProfiler;
import java.util.Collection;
import java.util.Collections;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.SharedConstants;
import net.minecraft.server.packs.PackResources;
import java.util.List;
import java.io.InputStream;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.concurrent.Executor;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.resources.FoliageColorReloadListener;
import net.minecraft.client.resources.GrassColorReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.SimpleReloadableResourceManager;
import java.io.IOException;
import net.minecraft.server.packs.PackType;
import com.mojang.blaze3d.platform.DisplayData;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.network.chat.Component;
import java.util.function.Supplier;
import java.util.function.Function;
import net.minecraft.network.chat.KeybindComponent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.util.UUID;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.util.profiling.InactiveProfiler;
import com.google.common.collect.Queues;
import net.minecraft.Util;
import net.minecraft.client.main.GameConfig;
import net.minecraft.util.profiling.ProfileResults;
import net.minecraft.util.profiling.ContinuousProfiler;
import net.minecraft.util.profiling.ProfilerFiller;
import java.util.Queue;
import net.minecraft.CrashReport;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.Connection;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.multiplayer.ClientLevel;
import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.tutorial.Tutorial;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.client.resources.PaintingTextureManager;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.SkinManager;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import net.minecraft.client.renderer.GpuWarnlistManager;
import net.minecraft.client.resources.SplashManager;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.client.sounds.SoundManager;
import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.client.resources.ClientPackSource;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.util.FrameTimer;
import net.minecraft.world.level.storage.LevelStorageSource;
import java.net.Proxy;
import net.minecraft.client.gui.Gui;
import net.minecraft.server.level.progress.StoringChunkProgressListener;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.searchtree.SearchRegistry;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.world.Snooper;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.renderer.VirtualScreen;
import com.mojang.datafixers.DataFixer;
import net.minecraft.client.renderer.texture.TextureManager;
import com.mojang.authlib.properties.PropertyMap;
import java.io.File;
import net.minecraft.util.Unit;
import java.util.concurrent.CompletableFuture;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;
import com.mojang.blaze3d.platform.WindowEventHandler;
import net.minecraft.world.SnooperPopulator;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;

public class Minecraft extends ReentrantBlockableEventLoop<Runnable> implements SnooperPopulator, WindowEventHandler {
    private static Minecraft instance;
    private static final Logger LOGGER;
    public static final boolean ON_OSX;
    public static final ResourceLocation DEFAULT_FONT;
    public static final ResourceLocation UNIFORM_FONT;
    public static final ResourceLocation ALT_FONT;
    private static final CompletableFuture<Unit> RESOURCE_RELOAD_INITIAL_TASK;
    private final File resourcePackDirectory;
    private final PropertyMap profileProperties;
    private final TextureManager textureManager;
    private final DataFixer fixerUpper;
    private final VirtualScreen virtualScreen;
    private final Window window;
    private final Timer timer;
    private final Snooper snooper;
    private final RenderBuffers renderBuffers;
    public final LevelRenderer levelRenderer;
    private final EntityRenderDispatcher entityRenderDispatcher;
    private final ItemRenderer itemRenderer;
    private final ItemInHandRenderer itemInHandRenderer;
    public final ParticleEngine particleEngine;
    private final SearchRegistry searchRegistry;
    private final User user;
    public final Font font;
    public final GameRenderer gameRenderer;
    public final DebugRenderer debugRenderer;
    private final AtomicReference<StoringChunkProgressListener> progressListener;
    public final Gui gui;
    public final Options options;
    private final HotbarManager hotbarManager;
    public final MouseHandler mouseHandler;
    public final KeyboardHandler keyboardHandler;
    public final File gameDirectory;
    private final String launchedVersion;
    private final String versionType;
    private final Proxy proxy;
    private final LevelStorageSource levelSource;
    public final FrameTimer frameTimer;
    private final boolean is64bit;
    private final boolean demo;
    private final boolean allowsMultiplayer;
    private final boolean allowsChat;
    private final ReloadableResourceManager resourceManager;
    private final ClientPackSource clientPackSource;
    private final PackRepository resourcePackRepository;
    private final LanguageManager languageManager;
    private final BlockColors blockColors;
    private final ItemColors itemColors;
    private final RenderTarget mainRenderTarget;
    private final SoundManager soundManager;
    private final MusicManager musicManager;
    private final FontManager fontManager;
    private final SplashManager splashManager;
    private final GpuWarnlistManager gpuWarnlistManager;
    private final MinecraftSessionService minecraftSessionService;
    private final SkinManager skinManager;
    private final ModelManager modelManager;
    private final BlockRenderDispatcher blockRenderer;
    private final PaintingTextureManager paintingTextures;
    private final MobEffectTextureManager mobEffectTextures;
    private final ToastComponent toast;
    private final Game game;
    private final Tutorial tutorial;
    public static byte[] reserve;
    @Nullable
    public MultiPlayerGameMode gameMode;
    @Nullable
    public ClientLevel level;
    @Nullable
    public LocalPlayer player;
    @Nullable
    private IntegratedServer singleplayerServer;
    @Nullable
    private ServerData currentServer;
    @Nullable
    private Connection pendingConnection;
    private boolean isLocalServer;
    @Nullable
    public Entity cameraEntity;
    @Nullable
    public Entity crosshairPickEntity;
    @Nullable
    public HitResult hitResult;
    private int rightClickDelay;
    protected int missTime;
    private boolean pause;
    private float pausePartialTick;
    private long lastNanoTime;
    private long lastTime;
    private int frames;
    public boolean noRender;
    @Nullable
    public Screen screen;
    @Nullable
    public Overlay overlay;
    private boolean connectedToRealms;
    private Thread gameThread;
    private volatile boolean running;
    @Nullable
    private CrashReport delayedCrash;
    private static int fps;
    public String fpsString;
    public boolean chunkPath;
    public boolean chunkVisibility;
    public boolean smartCull;
    private boolean windowActive;
    private final Queue<Runnable> progressTasks;
    @Nullable
    private CompletableFuture<Void> pendingReload;
    private ProfilerFiller profiler;
    private int fpsPieRenderTicks;
    private final ContinuousProfiler fpsPieProfiler;
    @Nullable
    private ProfileResults fpsPieResults;
    private String debugPath;
    
    public Minecraft(final GameConfig dsr) {
        super("Client");
        this.timer = new Timer(20.0f, 0L);
        this.snooper = new Snooper("client", (SnooperPopulator)this, Util.getMillis());
        this.searchRegistry = new SearchRegistry();
        this.progressListener = (AtomicReference<StoringChunkProgressListener>)new AtomicReference();
        this.frameTimer = new FrameTimer();
        this.game = new Game(this);
        this.lastNanoTime = Util.getNanos();
        this.running = true;
        this.fpsString = "";
        this.smartCull = true;
        this.progressTasks = (Queue<Runnable>)Queues.newConcurrentLinkedQueue();
        this.profiler = InactiveProfiler.INSTANCE;
        this.fpsPieProfiler = new ContinuousProfiler(Util.timeSource, () -> this.fpsPieRenderTicks);
        this.debugPath = "root";
        Minecraft.instance = this;
        this.gameDirectory = dsr.location.gameDirectory;
        final File file3 = dsr.location.assetDirectory;
        this.resourcePackDirectory = dsr.location.resourcePackDirectory;
        this.launchedVersion = dsr.game.launchVersion;
        this.versionType = dsr.game.versionType;
        this.profileProperties = dsr.user.profileProperties;
        this.clientPackSource = new ClientPackSource(new File(this.gameDirectory, "server-resource-packs"), dsr.location.getAssetIndex());
        this.resourcePackRepository = new PackRepository(Minecraft::createClientPackAdapter, new RepositorySource[] { this.clientPackSource, new FolderRepositorySource(this.resourcePackDirectory, PackSource.DEFAULT) });
        this.proxy = dsr.user.proxy;
        this.minecraftSessionService = new YggdrasilAuthenticationService(this.proxy, UUID.randomUUID().toString()).createMinecraftSessionService();
        this.user = dsr.user.user;
        Minecraft.LOGGER.info("Setting user: {}", this.user.getName());
        Minecraft.LOGGER.debug("(Session ID is {})", this.user.getSessionId());
        this.demo = dsr.game.demo;
        this.allowsMultiplayer = !dsr.game.disableMultiplayer;
        this.allowsChat = !dsr.game.disableChat;
        this.is64bit = checkIs64Bit();
        this.singleplayerServer = null;
        String string4;
        int integer5;
        if (this.allowsMultiplayer && dsr.server.hostname != null) {
            string4 = dsr.server.hostname;
            integer5 = dsr.server.port;
        }
        else {
            string4 = null;
            integer5 = 0;
        }
        KeybindComponent.setKeyResolver((Function<String, Supplier<Component>>)KeyMapping::createNameSupplier);
        this.fixerUpper = DataFixers.getDataFixer();
        this.toast = new ToastComponent(this);
        this.tutorial = new Tutorial(this);
        this.gameThread = Thread.currentThread();
        this.options = new Options(this, this.gameDirectory);
        this.hotbarManager = new HotbarManager(this.gameDirectory, this.fixerUpper);
        Minecraft.LOGGER.info("Backend library: {}", RenderSystem.getBackendDescription());
        DisplayData deg6;
        if (this.options.overrideHeight > 0 && this.options.overrideWidth > 0) {
            deg6 = new DisplayData(this.options.overrideWidth, this.options.overrideHeight, dsr.display.fullscreenWidth, dsr.display.fullscreenHeight, dsr.display.isFullscreen);
        }
        else {
            deg6 = dsr.display;
        }
        Util.timeSource = RenderSystem.initBackendSystem();
        this.virtualScreen = new VirtualScreen(this);
        this.window = this.virtualScreen.newWindow(deg6, this.options.fullscreenVideoModeString, this.createTitle());
        this.setWindowActive(true);
        try {
            final InputStream inputStream7 = this.getClientPackSource().getVanillaPack().getResource(PackType.CLIENT_RESOURCES, new ResourceLocation("icons/icon_16x16.png"));
            final InputStream inputStream8 = this.getClientPackSource().getVanillaPack().getResource(PackType.CLIENT_RESOURCES, new ResourceLocation("icons/icon_32x32.png"));
            this.window.setIcon(inputStream7, inputStream8);
        }
        catch (IOException iOException7) {
            Minecraft.LOGGER.error("Couldn't set icon", (Throwable)iOException7);
        }
        this.window.setFramerateLimit(this.options.framerateLimit);
        (this.mouseHandler = new MouseHandler(this)).setup(this.window.getWindow());
        (this.keyboardHandler = new KeyboardHandler(this)).setup(this.window.getWindow());
        RenderSystem.initRenderer(this.options.glDebugVerbosity, false);
        (this.mainRenderTarget = new RenderTarget(this.window.getWidth(), this.window.getHeight(), true, Minecraft.ON_OSX)).setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        this.resourceManager = new SimpleReloadableResourceManager(PackType.CLIENT_RESOURCES);
        this.resourcePackRepository.reload();
        this.options.loadSelectedResourcePacks(this.resourcePackRepository);
        this.languageManager = new LanguageManager(this.options.languageCode);
        this.resourceManager.registerReloadListener(this.languageManager);
        this.textureManager = new TextureManager(this.resourceManager);
        this.resourceManager.registerReloadListener(this.textureManager);
        this.skinManager = new SkinManager(this.textureManager, new File(file3, "skins"), this.minecraftSessionService);
        this.levelSource = new LevelStorageSource(this.gameDirectory.toPath().resolve("saves"), this.gameDirectory.toPath().resolve("backups"), this.fixerUpper);
        this.soundManager = new SoundManager(this.resourceManager, this.options);
        this.resourceManager.registerReloadListener(this.soundManager);
        this.splashManager = new SplashManager(this.user);
        this.resourceManager.registerReloadListener(this.splashManager);
        this.musicManager = new MusicManager(this);
        this.fontManager = new FontManager(this.textureManager);
        this.font = this.fontManager.createFont();
        this.resourceManager.registerReloadListener(this.fontManager.getReloadListener());
        this.selectMainFont(this.isEnforceUnicode());
        this.resourceManager.registerReloadListener(new GrassColorReloadListener());
        this.resourceManager.registerReloadListener(new FoliageColorReloadListener());
        this.window.setErrorSection("Startup");
        RenderSystem.setupDefaultState(0, 0, this.window.getWidth(), this.window.getHeight());
        this.window.setErrorSection("Post startup");
        this.blockColors = BlockColors.createDefault();
        this.itemColors = ItemColors.createDefault(this.blockColors);
        this.modelManager = new ModelManager(this.textureManager, this.blockColors, this.options.mipmapLevels);
        this.resourceManager.registerReloadListener(this.modelManager);
        this.itemRenderer = new ItemRenderer(this.textureManager, this.modelManager, this.itemColors);
        this.entityRenderDispatcher = new EntityRenderDispatcher(this.textureManager, this.itemRenderer, this.resourceManager, this.font, this.options);
        this.itemInHandRenderer = new ItemInHandRenderer(this);
        this.resourceManager.registerReloadListener(this.itemRenderer);
        this.renderBuffers = new RenderBuffers();
        this.gameRenderer = new GameRenderer(this, this.resourceManager, this.renderBuffers);
        this.resourceManager.registerReloadListener(this.gameRenderer);
        this.blockRenderer = new BlockRenderDispatcher(this.modelManager.getBlockModelShaper(), this.blockColors);
        this.resourceManager.registerReloadListener(this.blockRenderer);
        this.levelRenderer = new LevelRenderer(this, this.renderBuffers);
        this.resourceManager.registerReloadListener(this.levelRenderer);
        this.createSearchTrees();
        this.resourceManager.registerReloadListener(this.searchRegistry);
        this.particleEngine = new ParticleEngine(this.level, this.textureManager);
        this.resourceManager.registerReloadListener(this.particleEngine);
        this.paintingTextures = new PaintingTextureManager(this.textureManager);
        this.resourceManager.registerReloadListener(this.paintingTextures);
        this.mobEffectTextures = new MobEffectTextureManager(this.textureManager);
        this.resourceManager.registerReloadListener(this.mobEffectTextures);
        this.gpuWarnlistManager = new GpuWarnlistManager();
        this.resourceManager.registerReloadListener(this.gpuWarnlistManager);
        this.gui = new Gui(this);
        this.debugRenderer = new DebugRenderer(this);
        RenderSystem.setErrorCallback(this::onFullscreenError);
        if (this.options.fullscreen && !this.window.isFullscreen()) {
            this.window.toggleFullScreen();
            this.options.fullscreen = this.window.isFullscreen();
        }
        this.window.updateVsync(this.options.enableVsync);
        this.window.updateRawMouseInput(this.options.rawMouseInput);
        this.window.setDefaultErrorCallback();
        this.resizeDisplay();
        if (string4 != null) {
            this.setScreen(new ConnectScreen(new TitleScreen(), this, string4, integer5));
        }
        else {
            this.setScreen(new TitleScreen(true));
        }
        LoadingOverlay.registerTextures(this);
        final List<PackResources> list7 = this.resourcePackRepository.openAllSelected();
        this.setOverlay(new LoadingOverlay(this, this.resourceManager.createFullReload(Util.backgroundExecutor(), (Executor)this, Minecraft.RESOURCE_RELOAD_INITIAL_TASK, list7), (Consumer<Optional<Throwable>>)(optional -> Util.ifElse((java.util.Optional<Object>)optional, (java.util.function.Consumer<Object>)this::rollbackResourcePacks, () -> {
            if (SharedConstants.IS_RUNNING_IN_IDE) {
                this.selfTest();
            }
        })), false));
    }
    
    public void updateTitle() {
        this.window.setTitle(this.createTitle());
    }
    
    private String createTitle() {
        final StringBuilder stringBuilder2 = new StringBuilder("Minecraft");
        if (this.isProbablyModded()) {
            stringBuilder2.append("*");
        }
        stringBuilder2.append(" ");
        stringBuilder2.append(SharedConstants.getCurrentVersion().getName());
        final ClientPacketListener dwm3 = this.getConnection();
        if (dwm3 != null && dwm3.getConnection().isConnected()) {
            stringBuilder2.append(" - ");
            if (this.singleplayerServer != null && !this.singleplayerServer.isPublished()) {
                stringBuilder2.append(I18n.get("title.singleplayer"));
            }
            else if (this.isConnectedToRealms()) {
                stringBuilder2.append(I18n.get("title.multiplayer.realms"));
            }
            else if (this.singleplayerServer != null || (this.currentServer != null && this.currentServer.isLan())) {
                stringBuilder2.append(I18n.get("title.multiplayer.lan"));
            }
            else {
                stringBuilder2.append(I18n.get("title.multiplayer.other"));
            }
        }
        return stringBuilder2.toString();
    }
    
    public boolean isProbablyModded() {
        return !"vanilla".equals(ClientBrandRetriever.getClientModName()) || Minecraft.class.getSigners() == null;
    }
    
    private void rollbackResourcePacks(final Throwable throwable) {
        if (this.resourcePackRepository.getSelectedIds().size() > 1) {
            Component nr3;
            if (throwable instanceof SimpleReloadableResourceManager.ResourcePackLoadingFailure) {
                nr3 = new TextComponent(((SimpleReloadableResourceManager.ResourcePackLoadingFailure)throwable).getPack().getName());
            }
            else {
                nr3 = null;
            }
            this.clearResourcePacksOnError(throwable, nr3);
        }
        else {
            Util.throwAsRuntime(throwable);
        }
    }
    
    public void clearResourcePacksOnError(final Throwable throwable, @Nullable final Component nr) {
        Minecraft.LOGGER.info("Caught error loading resourcepacks, removing all selected resourcepacks", throwable);
        this.resourcePackRepository.setSelected((Collection<String>)Collections.emptyList());
        this.options.resourcePacks.clear();
        this.options.incompatibleResourcePacks.clear();
        this.options.save();
        this.reloadResourcePacks().thenRun(() -> {
            final ToastComponent dmo3 = this.getToasts();
            SystemToast.addOrUpdate(dmo3, SystemToast.SystemToastIds.PACK_LOAD_FAILURE, new TranslatableComponent("resourcePack.load_fail"), nr);
        });
    }
    
    public void run() {
        this.gameThread = Thread.currentThread();
        try {
            boolean boolean2 = false;
            while (this.running) {
                if (this.delayedCrash != null) {
                    crash(this.delayedCrash);
                    return;
                }
                try {
                    final SingleTickProfiler anw3 = SingleTickProfiler.createTickProfiler("Renderer");
                    final boolean boolean3 = this.shouldRenderFpsPie();
                    this.startProfilers(boolean3, anw3);
                    this.profiler.startTick();
                    this.runTick(!boolean2);
                    this.profiler.endTick();
                    this.finishProfilers(boolean3, anw3);
                }
                catch (OutOfMemoryError outOfMemoryError3) {
                    if (boolean2) {
                        throw outOfMemoryError3;
                    }
                    this.emergencySave();
                    this.setScreen(new OutOfMemoryScreen());
                    System.gc();
                    Minecraft.LOGGER.fatal("Out of memory", (Throwable)outOfMemoryError3);
                    boolean2 = true;
                }
            }
        }
        catch (ReportedException u2) {
            this.fillReport(u2.getReport());
            this.emergencySave();
            Minecraft.LOGGER.fatal("Reported exception thrown!", (Throwable)u2);
            crash(u2.getReport());
        }
        catch (Throwable throwable2) {
            final CrashReport l3 = this.fillReport(new CrashReport("Unexpected error", throwable2));
            Minecraft.LOGGER.fatal("Unreported exception thrown!", throwable2);
            this.emergencySave();
            crash(l3);
        }
    }
    
    void selectMainFont(final boolean boolean1) {
        this.fontManager.setRenames((Map<ResourceLocation, ResourceLocation>)(boolean1 ? ImmutableMap.of(Minecraft.DEFAULT_FONT, Minecraft.UNIFORM_FONT) : ImmutableMap.of()));
    }
    
    private void createSearchTrees() {
        final ReloadableSearchTree<ItemStack> ems2 = new ReloadableSearchTree<ItemStack>((java.util.function.Function<ItemStack, Stream<String>>)(bly -> bly.getTooltipLines(null, TooltipFlag.Default.NORMAL).stream().map(nr -> ChatFormatting.stripFormatting(nr.getString()).trim()).filter(string -> !string.isEmpty())), (java.util.function.Function<ItemStack, Stream<ResourceLocation>>)(bly -> Stream.of(Registry.ITEM.getKey(bly.getItem()))));
        final ReloadableIdSearchTree<ItemStack> emr3 = new ReloadableIdSearchTree<ItemStack>((java.util.function.Function<ItemStack, Stream<ResourceLocation>>)(bly -> ItemTags.getAllTags().getMatchingTags(bly.getItem()).stream()));
        final NonNullList<ItemStack> gj4 = NonNullList.<ItemStack>create();
        for (final Item blu6 : Registry.ITEM) {
            blu6.fillItemCategory(CreativeModeTab.TAB_SEARCH, gj4);
        }
        gj4.forEach(bly -> {
            ems2.add(bly);
            emr3.add(bly);
        });
        final ReloadableSearchTree<RecipeCollection> ems3 = new ReloadableSearchTree<RecipeCollection>((java.util.function.Function<RecipeCollection, Stream<String>>)(drq -> drq.getRecipes().stream().flatMap(bon -> bon.getResultItem().getTooltipLines(null, TooltipFlag.Default.NORMAL).stream()).map(nr -> ChatFormatting.stripFormatting(nr.getString()).trim()).filter(string -> !string.isEmpty())), (java.util.function.Function<RecipeCollection, Stream<ResourceLocation>>)(drq -> drq.getRecipes().stream().map(bon -> Registry.ITEM.getKey(bon.getResultItem().getItem()))));
        this.searchRegistry.<ItemStack>register(SearchRegistry.CREATIVE_NAMES, ems2);
        this.searchRegistry.<ItemStack>register(SearchRegistry.CREATIVE_TAGS, emr3);
        this.searchRegistry.<RecipeCollection>register(SearchRegistry.RECIPE_COLLECTIONS, ems3);
    }
    
    private void onFullscreenError(final int integer, final long long2) {
        this.options.enableVsync = false;
        this.options.save();
    }
    
    private static boolean checkIs64Bit() {
        final String[] array;
        final String[] arr1 = array = new String[] { "sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch" };
        for (final String string5 : array) {
            final String string6 = System.getProperty(string5);
            if (string6 != null && string6.contains("64")) {
                return true;
            }
        }
        return false;
    }
    
    public RenderTarget getMainRenderTarget() {
        return this.mainRenderTarget;
    }
    
    public String getLaunchedVersion() {
        return this.launchedVersion;
    }
    
    public String getVersionType() {
        return this.versionType;
    }
    
    public void delayCrash(final CrashReport l) {
        this.delayedCrash = l;
    }
    
    public static void crash(final CrashReport l) {
        final File file2 = new File(getInstance().gameDirectory, "crash-reports");
        final File file3 = new File(file2, "crash-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + "-client.txt");
        Bootstrap.realStdoutPrintln(l.getFriendlyReport());
        if (l.getSaveFile() != null) {
            Bootstrap.realStdoutPrintln(new StringBuilder().append("#@!@# Game crashed! Crash report saved to: #@!@# ").append(l.getSaveFile()).toString());
            System.exit(-1);
        }
        else if (l.saveToFile(file3)) {
            Bootstrap.realStdoutPrintln("#@!@# Game crashed! Crash report saved to: #@!@# " + file3.getAbsolutePath());
            System.exit(-1);
        }
        else {
            Bootstrap.realStdoutPrintln("#@?@# Game crashed! Crash report could not be saved. #@?@#");
            System.exit(-2);
        }
    }
    
    public boolean isEnforceUnicode() {
        return this.options.forceUnicodeFont;
    }
    
    public CompletableFuture<Void> reloadResourcePacks() {
        if (this.pendingReload != null) {
            return this.pendingReload;
        }
        final CompletableFuture<Void> completableFuture2 = (CompletableFuture<Void>)new CompletableFuture();
        if (this.overlay instanceof LoadingOverlay) {
            return this.pendingReload = completableFuture2;
        }
        this.resourcePackRepository.reload();
        final List<PackResources> list3 = this.resourcePackRepository.openAllSelected();
        this.setOverlay(new LoadingOverlay(this, this.resourceManager.createFullReload(Util.backgroundExecutor(), (Executor)this, Minecraft.RESOURCE_RELOAD_INITIAL_TASK, list3), (Consumer<Optional<Throwable>>)(optional -> Util.ifElse((java.util.Optional<Object>)optional, (java.util.function.Consumer<Object>)this::rollbackResourcePacks, () -> {
            this.levelRenderer.allChanged();
            completableFuture2.complete(null);
        })), true));
        return completableFuture2;
    }
    
    private void selfTest() {
        boolean boolean2 = false;
        final BlockModelShaper eao3 = this.getBlockRenderer().getBlockModelShaper();
        final BakedModel elg4 = eao3.getModelManager().getMissingModel();
        for (final Block bul6 : Registry.BLOCK) {
            for (final BlockState cee8 : bul6.getStateDefinition().getPossibleStates()) {
                if (cee8.getRenderShape() == RenderShape.MODEL) {
                    final BakedModel elg5 = eao3.getBlockModel(cee8);
                    if (elg5 != elg4) {
                        continue;
                    }
                    Minecraft.LOGGER.debug("Missing model for: {}", cee8);
                    boolean2 = true;
                }
            }
        }
        final TextureAtlasSprite eju5 = elg4.getParticleIcon();
        for (final Block bul7 : Registry.BLOCK) {
            for (final BlockState cee9 : bul7.getStateDefinition().getPossibleStates()) {
                final TextureAtlasSprite eju6 = eao3.getParticleIcon(cee9);
                if (!cee9.isAir() && eju6 == eju5) {
                    Minecraft.LOGGER.debug("Missing particle icon for: {}", cee9);
                    boolean2 = true;
                }
            }
        }
        final NonNullList<ItemStack> gj6 = NonNullList.<ItemStack>create();
        for (final Item blu8 : Registry.ITEM) {
            gj6.clear();
            blu8.fillItemCategory(CreativeModeTab.TAB_SEARCH, gj6);
            for (final ItemStack bly10 : gj6) {
                final String string11 = bly10.getDescriptionId();
                final String string12 = new TranslatableComponent(string11).getString();
                if (string12.toLowerCase(Locale.ROOT).equals(blu8.getDescriptionId())) {
                    Minecraft.LOGGER.debug("Missing translation for: {} {} {}", bly10, string11, bly10.getItem());
                }
            }
        }
        boolean2 |= MenuScreens.selfTest();
        if (boolean2) {
            throw new IllegalStateException("Your game data is foobar, fix the errors above!");
        }
    }
    
    public LevelStorageSource getLevelSource() {
        return this.levelSource;
    }
    
    private void openChatScreen(final String string) {
        if (!this.isLocalServer() && !this.allowsChat()) {
            if (this.player != null) {
                this.player.sendMessage(new TranslatableComponent("chat.cannotSend").withStyle(ChatFormatting.RED), Util.NIL_UUID);
            }
        }
        else {
            this.setScreen(new ChatScreen(string));
        }
    }
    
    public void setScreen(@Nullable Screen doq) {
        if (this.screen != null) {
            this.screen.removed();
        }
        if (doq == null && this.level == null) {
            doq = new TitleScreen();
        }
        else if (doq == null && this.player.isDeadOrDying()) {
            if (this.player.shouldShowDeathScreen()) {
                doq = new DeathScreen(null, this.level.getLevelData().isHardcore());
            }
            else {
                this.player.respawn();
            }
        }
        if (doq instanceof TitleScreen || doq instanceof JoinMultiplayerScreen) {
            this.options.renderDebug = false;
            this.gui.getChat().clearMessages(true);
        }
        if ((this.screen = doq) != null) {
            this.mouseHandler.releaseMouse();
            KeyMapping.releaseAll();
            doq.init(this, this.window.getGuiScaledWidth(), this.window.getGuiScaledHeight());
            this.noRender = false;
            NarratorChatListener.INSTANCE.sayNow(doq.getNarrationMessage());
        }
        else {
            this.soundManager.resume();
            this.mouseHandler.grabMouse();
        }
        this.updateTitle();
    }
    
    public void setOverlay(@Nullable final Overlay dok) {
        this.overlay = dok;
    }
    
    public void destroy() {
        try {
            Minecraft.LOGGER.info("Stopping!");
            try {
                NarratorChatListener.INSTANCE.destroy();
            }
            catch (Throwable t) {}
            try {
                if (this.level != null) {
                    this.level.disconnect();
                }
                this.clearLevel();
            }
            catch (Throwable t2) {}
            if (this.screen != null) {
                this.screen.removed();
            }
            this.close();
        }
        finally {
            Util.timeSource = System::nanoTime;
            if (this.delayedCrash == null) {
                System.exit(0);
            }
        }
    }
    
    public void close() {
        try {
            this.modelManager.close();
            this.fontManager.close();
            this.gameRenderer.close();
            this.levelRenderer.close();
            this.soundManager.destroy();
            this.resourcePackRepository.close();
            this.particleEngine.close();
            this.mobEffectTextures.close();
            this.paintingTextures.close();
            this.textureManager.close();
            this.resourceManager.close();
            Util.shutdownExecutors();
        }
        catch (Throwable throwable2) {
            Minecraft.LOGGER.error("Shutdown failure!", throwable2);
            throw throwable2;
        }
        finally {
            this.virtualScreen.close();
            this.window.close();
        }
    }
    
    private void runTick(final boolean boolean1) {
        this.window.setErrorSection("Pre render");
        final long long3 = Util.getNanos();
        if (this.window.shouldClose()) {
            this.stop();
        }
        if (this.pendingReload != null && !(this.overlay instanceof LoadingOverlay)) {
            final CompletableFuture<Void> completableFuture5 = this.pendingReload;
            this.pendingReload = null;
            this.reloadResourcePacks().thenRun(() -> completableFuture5.complete(null));
        }
        Runnable runnable5;
        while ((runnable5 = (Runnable)this.progressTasks.poll()) != null) {
            runnable5.run();
        }
        if (boolean1) {
            final int integer6 = this.timer.advanceTime(Util.getMillis());
            this.profiler.push("scheduledExecutables");
            this.runAllTasks();
            this.profiler.pop();
            this.profiler.push("tick");
            for (int integer7 = 0; integer7 < Math.min(10, integer6); ++integer7) {
                this.profiler.incrementCounter("clientTick");
                this.tick();
            }
            this.profiler.pop();
        }
        this.mouseHandler.turnPlayer();
        this.window.setErrorSection("Render");
        this.profiler.push("sound");
        this.soundManager.updateSource(this.gameRenderer.getMainCamera());
        this.profiler.pop();
        this.profiler.push("render");
        RenderSystem.pushMatrix();
        RenderSystem.clear(16640, Minecraft.ON_OSX);
        this.mainRenderTarget.bindWrite(true);
        FogRenderer.setupNoFog();
        this.profiler.push("display");
        RenderSystem.enableTexture();
        RenderSystem.enableCull();
        this.profiler.pop();
        if (!this.noRender) {
            this.profiler.popPush("gameRenderer");
            this.gameRenderer.render(this.pause ? this.pausePartialTick : this.timer.partialTick, long3, boolean1);
            this.profiler.popPush("toasts");
            this.toast.render(new PoseStack());
            this.profiler.pop();
        }
        if (this.fpsPieResults != null) {
            this.profiler.push("fpsPie");
            this.renderFpsMeter(new PoseStack(), this.fpsPieResults);
            this.profiler.pop();
        }
        this.profiler.push("blit");
        this.mainRenderTarget.unbindWrite();
        RenderSystem.popMatrix();
        RenderSystem.pushMatrix();
        this.mainRenderTarget.blitToScreen(this.window.getWidth(), this.window.getHeight());
        RenderSystem.popMatrix();
        this.profiler.popPush("updateDisplay");
        this.window.updateDisplay();
        final int integer6 = this.getFramerateLimit();
        if (integer6 < Option.FRAMERATE_LIMIT.getMaxValue()) {
            RenderSystem.limitDisplayFPS(integer6);
        }
        this.profiler.popPush("yield");
        Thread.yield();
        this.profiler.pop();
        this.window.setErrorSection("Post render");
        ++this.frames;
        final boolean boolean2 = this.hasSingleplayerServer() && ((this.screen != null && this.screen.isPauseScreen()) || (this.overlay != null && this.overlay.isPauseScreen())) && !this.singleplayerServer.isPublished();
        if (this.pause != boolean2) {
            if (this.pause) {
                this.pausePartialTick = this.timer.partialTick;
            }
            else {
                this.timer.partialTick = this.pausePartialTick;
            }
            this.pause = boolean2;
        }
        final long long4 = Util.getNanos();
        this.frameTimer.logFrameDuration(long4 - this.lastNanoTime);
        this.lastNanoTime = long4;
        this.profiler.push("fpsUpdate");
        while (Util.getMillis() >= this.lastTime + 1000L) {
            Minecraft.fps = this.frames;
            this.fpsString = String.format("%d fps T: %s%s%s%s B: %d", new Object[] { Minecraft.fps, (this.options.framerateLimit == Option.FRAMERATE_LIMIT.getMaxValue()) ? "inf" : Integer.valueOf(this.options.framerateLimit), this.options.enableVsync ? " vsync" : "", this.options.graphicsMode.toString(), (this.options.renderClouds == CloudStatus.OFF) ? "" : ((this.options.renderClouds == CloudStatus.FAST) ? " fast-clouds" : " fancy-clouds"), this.options.biomeBlendRadius });
            this.lastTime += 1000L;
            this.frames = 0;
            this.snooper.prepare();
            if (!this.snooper.isStarted()) {
                this.snooper.start();
            }
        }
        this.profiler.pop();
    }
    
    private boolean shouldRenderFpsPie() {
        return this.options.renderDebug && this.options.renderDebugCharts && !this.options.hideGui;
    }
    
    private void startProfilers(final boolean boolean1, @Nullable final SingleTickProfiler anw) {
        if (boolean1) {
            if (!this.fpsPieProfiler.isEnabled()) {
                this.fpsPieRenderTicks = 0;
                this.fpsPieProfiler.enable();
            }
            ++this.fpsPieRenderTicks;
        }
        else {
            this.fpsPieProfiler.disable();
        }
        this.profiler = SingleTickProfiler.decorateFiller(this.fpsPieProfiler.getFiller(), anw);
    }
    
    private void finishProfilers(final boolean boolean1, @Nullable final SingleTickProfiler anw) {
        if (anw != null) {
            anw.endTick();
        }
        if (boolean1) {
            this.fpsPieResults = this.fpsPieProfiler.getResults();
        }
        else {
            this.fpsPieResults = null;
        }
        this.profiler = this.fpsPieProfiler.getFiller();
    }
    
    @Override
    public void resizeDisplay() {
        final int integer2 = this.window.calculateScale(this.options.guiScale, this.isEnforceUnicode());
        this.window.setGuiScale(integer2);
        if (this.screen != null) {
            this.screen.resize(this, this.window.getGuiScaledWidth(), this.window.getGuiScaledHeight());
        }
        final RenderTarget ded3 = this.getMainRenderTarget();
        ded3.resize(this.window.getWidth(), this.window.getHeight(), Minecraft.ON_OSX);
        this.gameRenderer.resize(this.window.getWidth(), this.window.getHeight());
        this.mouseHandler.setIgnoreFirstMove();
    }
    
    @Override
    public void cursorEntered() {
        this.mouseHandler.cursorEntered();
    }
    
    private int getFramerateLimit() {
        if (this.level == null && (this.screen != null || this.overlay != null)) {
            return 60;
        }
        return this.window.getFramerateLimit();
    }
    
    public void emergencySave() {
        try {
            Minecraft.reserve = new byte[0];
            this.levelRenderer.clear();
        }
        catch (Throwable t) {}
        try {
            System.gc();
            if (this.isLocalServer && this.singleplayerServer != null) {
                this.singleplayerServer.halt(true);
            }
            this.clearLevel(new GenericDirtMessageScreen(new TranslatableComponent("menu.savingLevel")));
        }
        catch (Throwable t2) {}
        System.gc();
    }
    
    void debugFpsMeterKeyPress(int integer) {
        if (this.fpsPieResults == null) {
            return;
        }
        final List<ResultField> list3 = this.fpsPieResults.getTimes(this.debugPath);
        if (list3.isEmpty()) {
            return;
        }
        final ResultField anv4 = (ResultField)list3.remove(0);
        if (integer == 0) {
            if (!anv4.name.isEmpty()) {
                final int integer2 = this.debugPath.lastIndexOf(30);
                if (integer2 >= 0) {
                    this.debugPath = this.debugPath.substring(0, integer2);
                }
            }
        }
        else if (--integer < list3.size() && !"unspecified".equals(((ResultField)list3.get(integer)).name)) {
            if (!this.debugPath.isEmpty()) {
                this.debugPath += '\u001e';
            }
            this.debugPath += ((ResultField)list3.get(integer)).name;
        }
    }
    
    private void renderFpsMeter(final PoseStack dfj, final ProfileResults ans) {
        final List<ResultField> list4 = ans.getTimes(this.debugPath);
        final ResultField anv5 = (ResultField)list4.remove(0);
        RenderSystem.clear(256, Minecraft.ON_OSX);
        RenderSystem.matrixMode(5889);
        RenderSystem.loadIdentity();
        RenderSystem.ortho(0.0, this.window.getWidth(), this.window.getHeight(), 0.0, 1000.0, 3000.0);
        RenderSystem.matrixMode(5888);
        RenderSystem.loadIdentity();
        RenderSystem.translatef(0.0f, 0.0f, -2000.0f);
        RenderSystem.lineWidth(1.0f);
        RenderSystem.disableTexture();
        final Tesselator dfl6 = Tesselator.getInstance();
        final BufferBuilder dfe7 = dfl6.getBuilder();
        final int integer8 = 160;
        final int integer9 = this.window.getWidth() - 160 - 10;
        final int integer10 = this.window.getHeight() - 320;
        RenderSystem.enableBlend();
        dfe7.begin(7, DefaultVertexFormat.POSITION_COLOR);
        dfe7.vertex(integer9 - 176.0f, integer10 - 96.0f - 16.0f, 0.0).color(200, 0, 0, 0).endVertex();
        dfe7.vertex(integer9 - 176.0f, integer10 + 320, 0.0).color(200, 0, 0, 0).endVertex();
        dfe7.vertex(integer9 + 176.0f, integer10 + 320, 0.0).color(200, 0, 0, 0).endVertex();
        dfe7.vertex(integer9 + 176.0f, integer10 - 96.0f - 16.0f, 0.0).color(200, 0, 0, 0).endVertex();
        dfl6.end();
        RenderSystem.disableBlend();
        double double11 = 0.0;
        for (final ResultField anv6 : list4) {
            final int integer11 = Mth.floor(anv6.percentage / 4.0) + 1;
            dfe7.begin(6, DefaultVertexFormat.POSITION_COLOR);
            final int integer12 = anv6.getColor();
            final int integer13 = integer12 >> 16 & 0xFF;
            final int integer14 = integer12 >> 8 & 0xFF;
            final int integer15 = integer12 & 0xFF;
            dfe7.vertex(integer9, integer10, 0.0).color(integer13, integer14, integer15, 255).endVertex();
            for (int integer16 = integer11; integer16 >= 0; --integer16) {
                final float float21 = (float)((double11 + anv6.percentage * integer16 / integer11) * 6.2831854820251465 / 100.0);
                final float float22 = Mth.sin(float21) * 160.0f;
                final float float23 = Mth.cos(float21) * 160.0f * 0.5f;
                dfe7.vertex(integer9 + float22, integer10 - float23, 0.0).color(integer13, integer14, integer15, 255).endVertex();
            }
            dfl6.end();
            dfe7.begin(5, DefaultVertexFormat.POSITION_COLOR);
            for (int integer16 = integer11; integer16 >= 0; --integer16) {
                final float float21 = (float)((double11 + anv6.percentage * integer16 / integer11) * 6.2831854820251465 / 100.0);
                final float float22 = Mth.sin(float21) * 160.0f;
                final float float23 = Mth.cos(float21) * 160.0f * 0.5f;
                if (float23 <= 0.0f) {
                    dfe7.vertex(integer9 + float22, integer10 - float23, 0.0).color(integer13 >> 1, integer14 >> 1, integer15 >> 1, 255).endVertex();
                    dfe7.vertex(integer9 + float22, integer10 - float23 + 10.0f, 0.0).color(integer13 >> 1, integer14 >> 1, integer15 >> 1, 255).endVertex();
                }
            }
            dfl6.end();
            double11 += anv6.percentage;
        }
        final DecimalFormat decimalFormat13 = new DecimalFormat("##0.00");
        decimalFormat13.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT));
        RenderSystem.enableTexture();
        final String string14 = ProfileResults.demanglePath(anv5.name);
        String string15 = "";
        if (!"unspecified".equals(string14)) {
            string15 += "[0] ";
        }
        if (string14.isEmpty()) {
            string15 += "ROOT ";
        }
        else {
            string15 = string15 + string14 + ' ';
        }
        final int integer12 = 16777215;
        this.font.drawShadow(dfj, string15, (float)(integer9 - 160), (float)(integer10 - 80 - 16), 16777215);
        string15 = decimalFormat13.format(anv5.globalPercentage) + "%";
        this.font.drawShadow(dfj, string15, (float)(integer9 + 160 - this.font.width(string15)), (float)(integer10 - 80 - 16), 16777215);
        for (int integer17 = 0; integer17 < list4.size(); ++integer17) {
            final ResultField anv7 = (ResultField)list4.get(integer17);
            final StringBuilder stringBuilder16 = new StringBuilder();
            if ("unspecified".equals(anv7.name)) {
                stringBuilder16.append("[?] ");
            }
            else {
                stringBuilder16.append("[").append(integer17 + 1).append("] ");
            }
            String string16 = stringBuilder16.append(anv7.name).toString();
            this.font.drawShadow(dfj, string16, (float)(integer9 - 160), (float)(integer10 + 80 + integer17 * 8 + 20), anv7.getColor());
            string16 = decimalFormat13.format(anv7.percentage) + "%";
            this.font.drawShadow(dfj, string16, (float)(integer9 + 160 - 50 - this.font.width(string16)), (float)(integer10 + 80 + integer17 * 8 + 20), anv7.getColor());
            string16 = decimalFormat13.format(anv7.globalPercentage) + "%";
            this.font.drawShadow(dfj, string16, (float)(integer9 + 160 - this.font.width(string16)), (float)(integer10 + 80 + integer17 * 8 + 20), anv7.getColor());
        }
    }
    
    public void stop() {
        this.running = false;
    }
    
    public boolean isRunning() {
        return this.running;
    }
    
    public void pauseGame(final boolean boolean1) {
        if (this.screen != null) {
            return;
        }
        final boolean boolean2 = this.hasSingleplayerServer() && !this.singleplayerServer.isPublished();
        if (boolean2) {
            this.setScreen(new PauseScreen(!boolean1));
            this.soundManager.pause();
        }
        else {
            this.setScreen(new PauseScreen(true));
        }
    }
    
    private void continueAttack(final boolean boolean1) {
        if (!boolean1) {
            this.missTime = 0;
        }
        if (this.missTime > 0 || this.player.isUsingItem()) {
            return;
        }
        if (boolean1 && this.hitResult != null && this.hitResult.getType() == HitResult.Type.BLOCK) {
            final BlockHitResult dcg3 = (BlockHitResult)this.hitResult;
            final BlockPos fx4 = dcg3.getBlockPos();
            if (!this.level.getBlockState(fx4).isAir()) {
                final Direction gc5 = dcg3.getDirection();
                if (this.gameMode.continueDestroyBlock(fx4, gc5)) {
                    this.particleEngine.crack(fx4, gc5);
                    this.player.swing(InteractionHand.MAIN_HAND);
                }
            }
            return;
        }
        this.gameMode.stopDestroyBlock();
    }
    
    private void startAttack() {
        if (this.missTime > 0) {
            return;
        }
        if (this.hitResult == null) {
            Minecraft.LOGGER.error("Null returned as 'hitResult', this shouldn't happen!");
            if (this.gameMode.hasMissTime()) {
                this.missTime = 10;
            }
            return;
        }
        if (this.player.isHandsBusy()) {
            return;
        }
        switch (this.hitResult.getType()) {
            case ENTITY: {
                this.gameMode.attack(this.player, ((EntityHitResult)this.hitResult).getEntity());
                break;
            }
            case BLOCK: {
                final BlockHitResult dcg2 = (BlockHitResult)this.hitResult;
                final BlockPos fx3 = dcg2.getBlockPos();
                if (!this.level.getBlockState(fx3).isAir()) {
                    this.gameMode.startDestroyBlock(fx3, dcg2.getDirection());
                    break;
                }
            }
            case MISS: {
                if (this.gameMode.hasMissTime()) {
                    this.missTime = 10;
                }
                this.player.resetAttackStrengthTicker();
                break;
            }
        }
        this.player.swing(InteractionHand.MAIN_HAND);
    }
    
    private void startUseItem() {
        if (this.gameMode.isDestroying()) {
            return;
        }
        this.rightClickDelay = 4;
        if (this.player.isHandsBusy()) {
            return;
        }
        if (this.hitResult == null) {
            Minecraft.LOGGER.warn("Null returned as 'hitResult', this shouldn't happen!");
        }
        for (final InteractionHand aoq5 : InteractionHand.values()) {
            final ItemStack bly6 = this.player.getItemInHand(aoq5);
            if (this.hitResult != null) {
                switch (this.hitResult.getType()) {
                    case ENTITY: {
                        final EntityHitResult dch7 = (EntityHitResult)this.hitResult;
                        final Entity apx8 = dch7.getEntity();
                        InteractionResult aor9 = this.gameMode.interactAt(this.player, apx8, dch7, aoq5);
                        if (!aor9.consumesAction()) {
                            aor9 = this.gameMode.interact(this.player, apx8, aoq5);
                        }
                        if (aor9.consumesAction()) {
                            if (aor9.shouldSwing()) {
                                this.player.swing(aoq5);
                            }
                            return;
                        }
                        break;
                    }
                    case BLOCK: {
                        final BlockHitResult dcg10 = (BlockHitResult)this.hitResult;
                        final int integer11 = bly6.getCount();
                        final InteractionResult aor10 = this.gameMode.useItemOn(this.player, this.level, aoq5, dcg10);
                        if (aor10.consumesAction()) {
                            if (aor10.shouldSwing()) {
                                this.player.swing(aoq5);
                                if (!bly6.isEmpty() && (bly6.getCount() != integer11 || this.gameMode.hasInfiniteItems())) {
                                    this.gameRenderer.itemInHandRenderer.itemUsed(aoq5);
                                }
                            }
                            return;
                        }
                        if (aor10 == InteractionResult.FAIL) {
                            return;
                        }
                        break;
                    }
                }
            }
            if (!bly6.isEmpty()) {
                final InteractionResult aor11 = this.gameMode.useItem(this.player, this.level, aoq5);
                if (aor11.consumesAction()) {
                    if (aor11.shouldSwing()) {
                        this.player.swing(aoq5);
                    }
                    this.gameRenderer.itemInHandRenderer.itemUsed(aoq5);
                    return;
                }
            }
        }
    }
    
    public MusicManager getMusicManager() {
        return this.musicManager;
    }
    
    public void tick() {
        if (this.rightClickDelay > 0) {
            --this.rightClickDelay;
        }
        this.profiler.push("gui");
        if (!this.pause) {
            this.gui.tick();
        }
        this.profiler.pop();
        this.gameRenderer.pick(1.0f);
        this.tutorial.onLookAt(this.level, this.hitResult);
        this.profiler.push("gameMode");
        if (!this.pause && this.level != null) {
            this.gameMode.tick();
        }
        this.profiler.popPush("textures");
        if (this.level != null) {
            this.textureManager.tick();
        }
        if (this.screen == null && this.player != null) {
            if (this.player.isDeadOrDying() && !(this.screen instanceof DeathScreen)) {
                this.setScreen(null);
            }
            else if (this.player.isSleeping() && this.level != null) {
                this.setScreen(new InBedChatScreen());
            }
        }
        else if (this.screen != null && this.screen instanceof InBedChatScreen && !this.player.isSleeping()) {
            this.setScreen(null);
        }
        if (this.screen != null) {
            this.missTime = 10000;
        }
        if (this.screen != null) {
            Screen.wrapScreenError(() -> this.screen.tick(), "Ticking screen", this.screen.getClass().getCanonicalName());
        }
        if (!this.options.renderDebug) {
            this.gui.clearCache();
        }
        if (this.overlay == null && (this.screen == null || this.screen.passEvents)) {
            this.profiler.popPush("Keybindings");
            this.handleKeybinds();
            if (this.missTime > 0) {
                --this.missTime;
            }
        }
        if (this.level != null) {
            this.profiler.popPush("gameRenderer");
            if (!this.pause) {
                this.gameRenderer.tick();
            }
            this.profiler.popPush("levelRenderer");
            if (!this.pause) {
                this.levelRenderer.tick();
            }
            this.profiler.popPush("level");
            if (!this.pause) {
                if (this.level.getSkyFlashTime() > 0) {
                    this.level.setSkyFlashTime(this.level.getSkyFlashTime() - 1);
                }
                this.level.tickEntities();
            }
        }
        else if (this.gameRenderer.currentEffect() != null) {
            this.gameRenderer.shutdownEffect();
        }
        if (!this.pause) {
            this.musicManager.tick();
        }
        this.soundManager.tick(this.pause);
        if (this.level != null) {
            if (!this.pause) {
                this.tutorial.tick();
                try {
                    this.level.tick(() -> true);
                }
                catch (Throwable throwable2) {
                    final CrashReport l3 = CrashReport.forThrowable(throwable2, "Exception in world tick");
                    if (this.level == null) {
                        final CrashReportCategory m4 = l3.addCategory("Affected level");
                        m4.setDetail("Problem", "Level is null!");
                    }
                    else {
                        this.level.fillReportDetails(l3);
                    }
                    throw new ReportedException(l3);
                }
            }
            this.profiler.popPush("animateTick");
            if (!this.pause && this.level != null) {
                this.level.animateTick(Mth.floor(this.player.getX()), Mth.floor(this.player.getY()), Mth.floor(this.player.getZ()));
            }
            this.profiler.popPush("particles");
            if (!this.pause) {
                this.particleEngine.tick();
            }
        }
        else if (this.pendingConnection != null) {
            this.profiler.popPush("pendingConnection");
            this.pendingConnection.tick();
        }
        this.profiler.popPush("keyboard");
        this.keyboardHandler.tick();
        this.profiler.pop();
    }
    
    private void handleKeybinds() {
        while (this.options.keyTogglePerspective.consumeClick()) {
            final CameraType dji2 = this.options.getCameraType();
            this.options.setCameraType(this.options.getCameraType().cycle());
            if (dji2.isFirstPerson() != this.options.getCameraType().isFirstPerson()) {
                this.gameRenderer.checkEntityPostEffect(this.options.getCameraType().isFirstPerson() ? this.getCameraEntity() : null);
            }
            this.levelRenderer.needsUpdate();
        }
        while (this.options.keySmoothCamera.consumeClick()) {
            this.options.smoothCamera = !this.options.smoothCamera;
        }
        for (int integer2 = 0; integer2 < 9; ++integer2) {
            final boolean boolean3 = this.options.keySaveHotbarActivator.isDown();
            final boolean boolean4 = this.options.keyLoadHotbarActivator.isDown();
            if (this.options.keyHotbarSlots[integer2].consumeClick()) {
                if (this.player.isSpectator()) {
                    this.gui.getSpectatorGui().onHotbarSelected(integer2);
                }
                else if (this.player.isCreative() && this.screen == null && (boolean4 || boolean3)) {
                    CreativeModeInventoryScreen.handleHotbarLoadOrSave(this, integer2, boolean4, boolean3);
                }
                else {
                    this.player.inventory.selected = integer2;
                }
            }
        }
        while (this.options.keyInventory.consumeClick()) {
            if (this.gameMode.isServerControlledInventory()) {
                this.player.sendOpenInventory();
            }
            else {
                this.tutorial.onOpenInventory();
                this.setScreen(new InventoryScreen(this.player));
            }
        }
        while (this.options.keyAdvancements.consumeClick()) {
            this.setScreen(new AdvancementsScreen(this.player.connection.getAdvancements()));
        }
        while (this.options.keySwapOffhand.consumeClick()) {
            if (!this.player.isSpectator()) {
                this.getConnection().send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ZERO, Direction.DOWN));
            }
        }
        while (this.options.keyDrop.consumeClick()) {
            if (!this.player.isSpectator() && this.player.drop(Screen.hasControlDown())) {
                this.player.swing(InteractionHand.MAIN_HAND);
            }
        }
        final boolean boolean5 = this.options.chatVisibility != ChatVisiblity.HIDDEN;
        if (boolean5) {
            while (this.options.keyChat.consumeClick()) {
                this.openChatScreen("");
            }
            if (this.screen == null && this.overlay == null && this.options.keyCommand.consumeClick()) {
                this.openChatScreen("/");
            }
        }
        if (this.player.isUsingItem()) {
            if (!this.options.keyUse.isDown()) {
                this.gameMode.releaseUsingItem(this.player);
            }
            while (this.options.keyAttack.consumeClick()) {}
            while (this.options.keyUse.consumeClick()) {}
            while (this.options.keyPickItem.consumeClick()) {}
        }
        else {
            while (this.options.keyAttack.consumeClick()) {
                this.startAttack();
            }
            while (this.options.keyUse.consumeClick()) {
                this.startUseItem();
            }
            while (this.options.keyPickItem.consumeClick()) {
                this.pickBlock();
            }
        }
        if (this.options.keyUse.isDown() && this.rightClickDelay == 0 && !this.player.isUsingItem()) {
            this.startUseItem();
        }
        this.continueAttack(this.screen == null && this.options.keyAttack.isDown() && this.mouseHandler.isMouseGrabbed());
    }
    
    public static DataPackConfig loadDataPacks(final LevelStorageSource.LevelStorageAccess a) {
        MinecraftServer.convertFromRegionFormatIfNeeded(a);
        final DataPackConfig brh2 = a.getDataPacks();
        if (brh2 == null) {
            throw new IllegalStateException("Failed to load data pack config");
        }
        return brh2;
    }
    
    public static WorldData loadWorldData(final LevelStorageSource.LevelStorageAccess a, final RegistryAccess.RegistryHolder b, final ResourceManager acf, final DataPackConfig brh) {
        final RegistryReadOps<Tag> vh5 = RegistryReadOps.<Tag>create((com.mojang.serialization.DynamicOps<Tag>)NbtOps.INSTANCE, acf, b);
        final WorldData cyk6 = a.getDataTag((DynamicOps<Tag>)vh5, brh);
        if (cyk6 == null) {
            throw new IllegalStateException("Failed to load world");
        }
        return cyk6;
    }
    
    public void loadLevel(final String string) {
        this.doLoadLevel(string, RegistryAccess.builtin(), (Function<LevelStorageSource.LevelStorageAccess, DataPackConfig>)Minecraft::loadDataPacks, (Function4<LevelStorageSource.LevelStorageAccess, RegistryAccess.RegistryHolder, ResourceManager, DataPackConfig, WorldData>)Minecraft::loadWorldData, false, ExperimentalDialogType.BACKUP);
    }
    
    public void createLevel(final String string, final LevelSettings brx, final RegistryAccess.RegistryHolder b, final WorldGenSettings cht) {
        this.doLoadLevel(string, b, (Function<LevelStorageSource.LevelStorageAccess, DataPackConfig>)(a -> brx.getDataPackConfig()), (Function4<LevelStorageSource.LevelStorageAccess, RegistryAccess.RegistryHolder, ResourceManager, DataPackConfig, WorldData>)((a, b5, acf, brh) -> {
            final RegistryWriteOps<JsonElement> vi8 = RegistryWriteOps.<JsonElement>create((com.mojang.serialization.DynamicOps<JsonElement>)JsonOps.INSTANCE, b);
            final RegistryReadOps<JsonElement> vh9 = RegistryReadOps.<JsonElement>create((com.mojang.serialization.DynamicOps<JsonElement>)JsonOps.INSTANCE, acf, b);
            final DataResult<WorldGenSettings> dataResult10 = (DataResult<WorldGenSettings>)WorldGenSettings.CODEC.encodeStart((DynamicOps)vi8, cht).setLifecycle(Lifecycle.stable()).flatMap(jsonElement -> WorldGenSettings.CODEC.parse((DynamicOps)vh9, jsonElement));
            final WorldGenSettings cht2 = (WorldGenSettings)dataResult10.resultOrPartial((Consumer)Util.prefix("Error reading worldgen settings after loading data packs: ", (Consumer<String>)Minecraft.LOGGER::error)).orElse(cht);
            return new PrimaryLevelData(brx, cht2, dataResult10.lifecycle());
        }), false, ExperimentalDialogType.CREATE);
    }
    
    private void doLoadLevel(final String string, final RegistryAccess.RegistryHolder b, final Function<LevelStorageSource.LevelStorageAccess, DataPackConfig> function, final Function4<LevelStorageSource.LevelStorageAccess, RegistryAccess.RegistryHolder, ResourceManager, DataPackConfig, WorldData> function4, final boolean boolean5, final ExperimentalDialogType a) {
        LevelStorageSource.LevelStorageAccess a2;
        try {
            a2 = this.levelSource.createAccess(string);
        }
        catch (IOException iOException9) {
            Minecraft.LOGGER.warn("Failed to read level {} data", string, iOException9);
            SystemToast.onWorldAccessFailure(this, string);
            this.setScreen(null);
            return;
        }
        ServerStem b2;
        try {
            b2 = this.makeServerStem(b, function, function4, boolean5, a2);
        }
        catch (Exception exception10) {
            Minecraft.LOGGER.warn("Failed to load datapacks, can't proceed with server load", (Throwable)exception10);
            this.setScreen(new DatapackLoadFailureScreen(() -> this.doLoadLevel(string, b, function, function4, true, a)));
            try {
                a2.close();
            }
            catch (IOException iOException10) {
                Minecraft.LOGGER.warn("Failed to unlock access to level {}", string, iOException10);
            }
            return;
        }
        final WorldData cyk10 = b2.worldData();
        final boolean boolean6 = cyk10.worldGenSettings().isOldCustomizedWorld();
        final boolean boolean7 = cyk10.worldGenSettingsLifecycle() != Lifecycle.stable();
        if (a != ExperimentalDialogType.NONE && (boolean6 || boolean7)) {
            this.displayExperimentalConfirmationDialog(a, string, boolean6, () -> this.doLoadLevel(string, b, function, function4, boolean5, ExperimentalDialogType.NONE));
            b2.close();
            try {
                a2.close();
            }
            catch (IOException iOException11) {
                Minecraft.LOGGER.warn("Failed to unlock access to level {}", string, iOException11);
            }
            return;
        }
        this.clearLevel();
        this.progressListener.set(null);
        try {
            a2.saveDataTag(b, cyk10);
            b2.serverResources().updateGlobals();
            final YggdrasilAuthenticationService yggdrasilAuthenticationService13 = new YggdrasilAuthenticationService(this.proxy, UUID.randomUUID().toString());
            final MinecraftSessionService minecraftSessionService14 = yggdrasilAuthenticationService13.createMinecraftSessionService();
            final GameProfileRepository gameProfileRepository15 = yggdrasilAuthenticationService13.createProfileRepository();
            final GameProfileCache aco16 = new GameProfileCache(gameProfileRepository15, new File(this.gameDirectory, MinecraftServer.USERID_CACHE_FILE.getName()));
            SkullBlockEntity.setProfileCache(aco16);
            SkullBlockEntity.setSessionService(minecraftSessionService14);
            GameProfileCache.setUsesAuthentication(false);
            this.singleplayerServer = MinecraftServer.<IntegratedServer>spin((java.util.function.Function<Thread, IntegratedServer>)(thread -> {
                final StoringChunkProgressListener aat3;
                return new IntegratedServer(thread, this, b2, a2, b2.packRepository(), b2.serverResources(), cyk10, minecraftSessionService14, gameProfileRepository15, aco16, integer -> {
                    aat3 = new StoringChunkProgressListener(integer + 0);
                    aat3.start();
                    this.progressListener.set(aat3);
                    return new ProcessorChunkProgressListener(aat3, this.progressTasks::add);
                });
            }));
            this.isLocalServer = true;
        }
        catch (Throwable throwable13) {
            final CrashReport l14 = CrashReport.forThrowable(throwable13, "Starting integrated server");
            final CrashReportCategory m15 = l14.addCategory("Starting integrated server");
            m15.setDetail("Level ID", string);
            m15.setDetail("Level Name", cyk10.getLevelName());
            throw new ReportedException(l14);
        }
        while (this.progressListener.get() == null) {
            Thread.yield();
        }
        final LevelLoadingScreen dod13 = new LevelLoadingScreen((StoringChunkProgressListener)this.progressListener.get());
        this.setScreen(dod13);
        this.profiler.push("waitForServer");
        while (!this.singleplayerServer.isReady()) {
            dod13.tick();
            this.runTick(false);
            try {
                Thread.sleep(16L);
            }
            catch (InterruptedException ex) {}
            if (this.delayedCrash != null) {
                crash(this.delayedCrash);
                return;
            }
        }
        this.profiler.pop();
        final SocketAddress socketAddress14 = this.singleplayerServer.getConnection().startMemoryChannel();
        final Connection nd15 = Connection.connectToLocalServer(socketAddress14);
        nd15.setListener(new ClientHandshakePacketListenerImpl(nd15, this, null, (Consumer<Component>)(nr -> {})));
        nd15.send(new ClientIntentionPacket(socketAddress14.toString(), 0, ConnectionProtocol.LOGIN));
        nd15.send(new ServerboundHelloPacket(this.getUser().getGameProfile()));
        this.pendingConnection = nd15;
    }
    
    private void displayExperimentalConfirmationDialog(final ExperimentalDialogType a, final String string, final boolean boolean3, final Runnable runnable) {
        if (a == ExperimentalDialogType.BACKUP) {
            Component nr6;
            Component nr7;
            if (boolean3) {
                nr6 = new TranslatableComponent("selectWorld.backupQuestion.customized");
                nr7 = new TranslatableComponent("selectWorld.backupWarning.customized");
            }
            else {
                nr6 = new TranslatableComponent("selectWorld.backupQuestion.experimental");
                nr7 = new TranslatableComponent("selectWorld.backupWarning.experimental");
            }
            this.setScreen(new BackupConfirmScreen(null, (boolean3, boolean4) -> {
                if (boolean3) {
                    EditWorldScreen.makeBackupAndShowToast(this.levelSource, string);
                }
                runnable.run();
            }, nr6, nr7, false));
        }
        else {
            this.setScreen(new ConfirmScreen(boolean3 -> {
                if (boolean3) {
                    runnable.run();
                }
                else {
                    this.setScreen(null);
                    try (final LevelStorageSource.LevelStorageAccess a5 = this.levelSource.createAccess(string)) {
                        a5.deleteLevel();
                    }
                    catch (IOException iOException5) {
                        SystemToast.onWorldDeleteFailure(this, string);
                        Minecraft.LOGGER.error("Failed to delete world {}", string, iOException5);
                    }
                }
            }, new TranslatableComponent("selectWorld.backupQuestion.experimental"), new TranslatableComponent("selectWorld.backupWarning.experimental"), CommonComponents.GUI_PROCEED, CommonComponents.GUI_CANCEL));
        }
    }
    
    public ServerStem makeServerStem(final RegistryAccess.RegistryHolder b, final Function<LevelStorageSource.LevelStorageAccess, DataPackConfig> function, final Function4<LevelStorageSource.LevelStorageAccess, RegistryAccess.RegistryHolder, ResourceManager, DataPackConfig, WorldData> function4, final boolean boolean4, final LevelStorageSource.LevelStorageAccess a) throws InterruptedException, ExecutionException {
        final DataPackConfig brh7 = (DataPackConfig)function.apply(a);
        final PackRepository abu8 = new PackRepository(new RepositorySource[] { new ServerPacksSource(), new FolderRepositorySource(a.getLevelPath(LevelResource.DATAPACK_DIR).toFile(), PackSource.WORLD) });
        try {
            final DataPackConfig brh8 = MinecraftServer.configurePackRepository(abu8, brh7, boolean4);
            final CompletableFuture<ServerResources> completableFuture10 = ServerResources.loadResources(abu8.openAllSelected(), Commands.CommandSelection.INTEGRATED, 2, Util.backgroundExecutor(), (Executor)this);
            this.managedBlock(completableFuture10::isDone);
            final ServerResources vz11 = (ServerResources)completableFuture10.get();
            final WorldData cyk12 = (WorldData)function4.apply(a, b, vz11.getResourceManager(), brh8);
            return new ServerStem(abu8, vz11, cyk12);
        }
        catch (InterruptedException | ExecutionException ex2) {
            final Exception ex;
            final Exception exception9 = ex;
            abu8.close();
            throw exception9;
        }
    }
    
    public void setLevel(final ClientLevel dwl) {
        final ProgressScreen doo3 = new ProgressScreen();
        doo3.progressStartNoAbort(new TranslatableComponent("connect.joining"));
        this.updateScreenAndTick(doo3);
        this.updateLevelInEngines(this.level = dwl);
        if (!this.isLocalServer) {
            final AuthenticationService authenticationService4 = (AuthenticationService)new YggdrasilAuthenticationService(this.proxy, UUID.randomUUID().toString());
            final MinecraftSessionService minecraftSessionService5 = authenticationService4.createMinecraftSessionService();
            final GameProfileRepository gameProfileRepository6 = authenticationService4.createProfileRepository();
            final GameProfileCache aco7 = new GameProfileCache(gameProfileRepository6, new File(this.gameDirectory, MinecraftServer.USERID_CACHE_FILE.getName()));
            SkullBlockEntity.setProfileCache(aco7);
            SkullBlockEntity.setSessionService(minecraftSessionService5);
            GameProfileCache.setUsesAuthentication(false);
        }
    }
    
    public void clearLevel() {
        this.clearLevel(new ProgressScreen());
    }
    
    public void clearLevel(final Screen doq) {
        final ClientPacketListener dwm3 = this.getConnection();
        if (dwm3 != null) {
            this.dropAllTasks();
            dwm3.cleanup();
        }
        final IntegratedServer emy4 = this.singleplayerServer;
        this.singleplayerServer = null;
        this.gameRenderer.resetData();
        this.gameMode = null;
        NarratorChatListener.INSTANCE.clear();
        this.updateScreenAndTick(doq);
        if (this.level != null) {
            if (emy4 != null) {
                this.profiler.push("waitForServer");
                while (!emy4.isShutdown()) {
                    this.runTick(false);
                }
                this.profiler.pop();
            }
            this.clientPackSource.clearServerPack();
            this.gui.onDisconnected();
            this.currentServer = null;
            this.isLocalServer = false;
            this.game.onLeaveGameSession();
        }
        this.updateLevelInEngines(this.level = null);
        this.player = null;
    }
    
    private void updateScreenAndTick(final Screen doq) {
        this.profiler.push("forcedTick");
        this.soundManager.stop();
        this.cameraEntity = null;
        this.pendingConnection = null;
        this.setScreen(doq);
        this.runTick(false);
        this.profiler.pop();
    }
    
    public void forceSetScreen(final Screen doq) {
        this.profiler.push("forcedTick");
        this.setScreen(doq);
        this.runTick(false);
        this.profiler.pop();
    }
    
    private void updateLevelInEngines(@Nullable final ClientLevel dwl) {
        this.levelRenderer.setLevel(dwl);
        this.particleEngine.setLevel(dwl);
        BlockEntityRenderDispatcher.instance.setLevel(dwl);
        this.updateTitle();
    }
    
    public boolean allowsMultiplayer() {
        return this.allowsMultiplayer;
    }
    
    public boolean isBlocked(final UUID uUID) {
        return !this.allowsChat() && (this.player == null || !uUID.equals(this.player.getUUID())) && !uUID.equals(Util.NIL_UUID);
    }
    
    public boolean allowsChat() {
        return this.allowsChat;
    }
    
    public final boolean isDemo() {
        return this.demo;
    }
    
    @Nullable
    public ClientPacketListener getConnection() {
        return (this.player == null) ? null : this.player.connection;
    }
    
    public static boolean renderNames() {
        return !Minecraft.instance.options.hideGui;
    }
    
    public static boolean useFancyGraphics() {
        return Minecraft.instance.options.graphicsMode.getId() >= GraphicsStatus.FANCY.getId();
    }
    
    public static boolean useShaderTransparency() {
        return Minecraft.instance.options.graphicsMode.getId() >= GraphicsStatus.FABULOUS.getId();
    }
    
    public static boolean useAmbientOcclusion() {
        return Minecraft.instance.options.ambientOcclusion != AmbientOcclusionStatus.OFF;
    }
    
    private void pickBlock() {
        if (this.hitResult == null || this.hitResult.getType() == HitResult.Type.MISS) {
            return;
        }
        final boolean boolean2 = this.player.abilities.instabuild;
        BlockEntity ccg3 = null;
        final HitResult.Type a5 = this.hitResult.getType();
        ItemStack bly4;
        if (a5 == HitResult.Type.BLOCK) {
            final BlockPos fx6 = ((BlockHitResult)this.hitResult).getBlockPos();
            final BlockState cee7 = this.level.getBlockState(fx6);
            final Block bul8 = cee7.getBlock();
            if (cee7.isAir()) {
                return;
            }
            bly4 = bul8.getCloneItemStack(this.level, fx6, cee7);
            if (bly4.isEmpty()) {
                return;
            }
            if (boolean2 && Screen.hasControlDown() && bul8.isEntityBlock()) {
                ccg3 = this.level.getBlockEntity(fx6);
            }
        }
        else {
            if (a5 != HitResult.Type.ENTITY || !boolean2) {
                return;
            }
            final Entity apx6 = ((EntityHitResult)this.hitResult).getEntity();
            if (apx6 instanceof Painting) {
                bly4 = new ItemStack(Items.PAINTING);
            }
            else if (apx6 instanceof LeashFenceKnotEntity) {
                bly4 = new ItemStack(Items.LEAD);
            }
            else if (apx6 instanceof ItemFrame) {
                final ItemFrame bcm7 = (ItemFrame)apx6;
                final ItemStack bly5 = bcm7.getItem();
                if (bly5.isEmpty()) {
                    bly4 = new ItemStack(Items.ITEM_FRAME);
                }
                else {
                    bly4 = bly5.copy();
                }
            }
            else if (apx6 instanceof AbstractMinecart) {
                final AbstractMinecart bhi7 = (AbstractMinecart)apx6;
                Item blu8 = null;
                switch (bhi7.getMinecartType()) {
                    case FURNACE: {
                        blu8 = Items.FURNACE_MINECART;
                        break;
                    }
                    case CHEST: {
                        blu8 = Items.CHEST_MINECART;
                        break;
                    }
                    case TNT: {
                        blu8 = Items.TNT_MINECART;
                        break;
                    }
                    case HOPPER: {
                        blu8 = Items.HOPPER_MINECART;
                        break;
                    }
                    case COMMAND_BLOCK: {
                        blu8 = Items.COMMAND_BLOCK_MINECART;
                        break;
                    }
                    default: {
                        blu8 = Items.MINECART;
                        break;
                    }
                }
                bly4 = new ItemStack(blu8);
            }
            else if (apx6 instanceof Boat) {
                bly4 = new ItemStack(((Boat)apx6).getDropItem());
            }
            else if (apx6 instanceof ArmorStand) {
                bly4 = new ItemStack(Items.ARMOR_STAND);
            }
            else if (apx6 instanceof EndCrystal) {
                bly4 = new ItemStack(Items.END_CRYSTAL);
            }
            else {
                final SpawnEggItem bmx7 = SpawnEggItem.byId(apx6.getType());
                if (bmx7 == null) {
                    return;
                }
                bly4 = new ItemStack(bmx7);
            }
        }
        if (bly4.isEmpty()) {
            String string6 = "";
            if (a5 == HitResult.Type.BLOCK) {
                string6 = Registry.BLOCK.getKey(this.level.getBlockState(((BlockHitResult)this.hitResult).getBlockPos()).getBlock()).toString();
            }
            else if (a5 == HitResult.Type.ENTITY) {
                string6 = Registry.ENTITY_TYPE.getKey(((EntityHitResult)this.hitResult).getEntity().getType()).toString();
            }
            Minecraft.LOGGER.warn("Picking on: [{}] {} gave null item", a5, string6);
            return;
        }
        final Inventory bfs6 = this.player.inventory;
        if (ccg3 != null) {
            this.addCustomNbtData(bly4, ccg3);
        }
        final int integer7 = bfs6.findSlotMatchingItem(bly4);
        if (boolean2) {
            bfs6.setPickedItem(bly4);
            this.gameMode.handleCreativeModeItemAdd(this.player.getItemInHand(InteractionHand.MAIN_HAND), 36 + bfs6.selected);
        }
        else if (integer7 != -1) {
            if (Inventory.isHotbarSlot(integer7)) {
                bfs6.selected = integer7;
            }
            else {
                this.gameMode.handlePickItem(integer7);
            }
        }
    }
    
    private ItemStack addCustomNbtData(final ItemStack bly, final BlockEntity ccg) {
        final CompoundTag md4 = ccg.save(new CompoundTag());
        if (bly.getItem() instanceof PlayerHeadItem && md4.contains("SkullOwner")) {
            final CompoundTag md5 = md4.getCompound("SkullOwner");
            bly.getOrCreateTag().put("SkullOwner", (Tag)md5);
            return bly;
        }
        bly.addTagElement("BlockEntityTag", (Tag)md4);
        final CompoundTag md5 = new CompoundTag();
        final ListTag mj6 = new ListTag();
        mj6.add(StringTag.valueOf("\"(+NBT)\""));
        md5.put("Lore", (Tag)mj6);
        bly.addTagElement("display", (Tag)md5);
        return bly;
    }
    
    public CrashReport fillReport(final CrashReport l) {
        fillReport(this.languageManager, this.launchedVersion, this.options, l);
        if (this.level != null) {
            this.level.fillReportDetails(l);
        }
        return l;
    }
    
    public static void fillReport(@Nullable final LanguageManager ekr, final String string, @Nullable final Options dka, final CrashReport l) {
        final CrashReportCategory m5 = l.getSystemDetails();
        m5.setDetail("Launched Version", (CrashReportDetail<String>)(() -> string));
        m5.setDetail("Backend library", (CrashReportDetail<String>)RenderSystem::getBackendDescription);
        m5.setDetail("Backend API", (CrashReportDetail<String>)RenderSystem::getApiDescription);
        m5.setDetail("GL Caps", (CrashReportDetail<String>)RenderSystem::getCapsString);
        m5.setDetail("Using VBOs", (CrashReportDetail<String>)(() -> "Yes"));
        m5.setDetail("Is Modded", (CrashReportDetail<String>)(() -> {
            final String string1 = ClientBrandRetriever.getClientModName();
            if (!"vanilla".equals(string1)) {
                return "Definitely; Client brand changed to '" + string1 + "'";
            }
            if (Minecraft.class.getSigners() == null) {
                return "Very likely; Jar signature invalidated";
            }
            return "Probably not. Jar signature remains and client brand is untouched.";
        }));
        m5.setDetail("Type", "Client (map_client.txt)");
        if (dka != null) {
            if (Minecraft.instance != null) {
                final String string2 = Minecraft.instance.getGpuWarnlistManager().getAllWarnings();
                if (string2 != null) {
                    m5.setDetail("GPU Warnings", string2);
                }
            }
            m5.setDetail("Graphics mode", dka.graphicsMode);
            m5.setDetail("Resource Packs", (CrashReportDetail<String>)(() -> {
                final StringBuilder stringBuilder2 = new StringBuilder();
                for (final String string4 : dka.resourcePacks) {
                    if (stringBuilder2.length() > 0) {
                        stringBuilder2.append(", ");
                    }
                    stringBuilder2.append(string4);
                    if (dka.incompatibleResourcePacks.contains(string4)) {
                        stringBuilder2.append(" (incompatible)");
                    }
                }
                return stringBuilder2.toString();
            }));
        }
        if (ekr != null) {
            m5.setDetail("Current Language", (CrashReportDetail<String>)(() -> ekr.getSelected().toString()));
        }
        m5.setDetail("CPU", (CrashReportDetail<String>)GlUtil::getCpuInfo);
    }
    
    public static Minecraft getInstance() {
        return Minecraft.instance;
    }
    
    public CompletableFuture<Void> delayTextureReload() {
        return (CompletableFuture<Void>)this.submit((java.util.function.Supplier<Object>)this::reloadResourcePacks).thenCompose(completableFuture -> completableFuture);
    }
    
    @Override
    public void populateSnooper(final Snooper aoz) {
        aoz.setDynamicData("fps", Minecraft.fps);
        aoz.setDynamicData("vsync_enabled", this.options.enableVsync);
        aoz.setDynamicData("display_frequency", this.window.getRefreshRate());
        aoz.setDynamicData("display_type", this.window.isFullscreen() ? "fullscreen" : "windowed");
        aoz.setDynamicData("run_time", ((Util.getMillis() - aoz.getStartupTime()) / 60L * 1000L));
        aoz.setDynamicData("current_action", this.getCurrentSnooperAction());
        aoz.setDynamicData("language", (this.options.languageCode == null) ? "en_us" : this.options.languageCode);
        final String string3 = (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) ? "little" : "big";
        aoz.setDynamicData("endianness", string3);
        aoz.setDynamicData("subtitles", this.options.showSubtitles);
        aoz.setDynamicData("touch", this.options.touchscreen ? "touch" : "mouse");
        int integer4 = 0;
        for (final Pack abs6 : this.resourcePackRepository.getSelectedPacks()) {
            if (!abs6.isRequired() && !abs6.isFixedPosition()) {
                aoz.setDynamicData(new StringBuilder().append("resource_pack[").append(integer4++).append("]").toString(), abs6.getId());
            }
        }
        aoz.setDynamicData("resource_packs", integer4);
        if (this.singleplayerServer != null) {
            aoz.setDynamicData("snooper_partner", this.singleplayerServer.getSnooper().getToken());
        }
    }
    
    private String getCurrentSnooperAction() {
        if (this.singleplayerServer != null) {
            if (this.singleplayerServer.isPublished()) {
                return "hosting_lan";
            }
            return "singleplayer";
        }
        else {
            if (this.currentServer == null) {
                return "out_of_game";
            }
            if (this.currentServer.isLan()) {
                return "playing_lan";
            }
            return "multiplayer";
        }
    }
    
    public void setCurrentServer(@Nullable final ServerData dwr) {
        this.currentServer = dwr;
    }
    
    @Nullable
    public ServerData getCurrentServer() {
        return this.currentServer;
    }
    
    public boolean isLocalServer() {
        return this.isLocalServer;
    }
    
    public boolean hasSingleplayerServer() {
        return this.isLocalServer && this.singleplayerServer != null;
    }
    
    @Nullable
    public IntegratedServer getSingleplayerServer() {
        return this.singleplayerServer;
    }
    
    public Snooper getSnooper() {
        return this.snooper;
    }
    
    public User getUser() {
        return this.user;
    }
    
    public PropertyMap getProfileProperties() {
        if (this.profileProperties.isEmpty()) {
            final GameProfile gameProfile2 = this.getMinecraftSessionService().fillProfileProperties(this.user.getGameProfile(), false);
            this.profileProperties.putAll((Multimap)gameProfile2.getProperties());
        }
        return this.profileProperties;
    }
    
    public Proxy getProxy() {
        return this.proxy;
    }
    
    public TextureManager getTextureManager() {
        return this.textureManager;
    }
    
    public ResourceManager getResourceManager() {
        return this.resourceManager;
    }
    
    public PackRepository getResourcePackRepository() {
        return this.resourcePackRepository;
    }
    
    public ClientPackSource getClientPackSource() {
        return this.clientPackSource;
    }
    
    public File getResourcePackDirectory() {
        return this.resourcePackDirectory;
    }
    
    public LanguageManager getLanguageManager() {
        return this.languageManager;
    }
    
    public Function<ResourceLocation, TextureAtlasSprite> getTextureAtlas(final ResourceLocation vk) {
        return (Function<ResourceLocation, TextureAtlasSprite>)this.modelManager.getAtlas(vk)::getSprite;
    }
    
    public boolean is64Bit() {
        return this.is64bit;
    }
    
    public boolean isPaused() {
        return this.pause;
    }
    
    public GpuWarnlistManager getGpuWarnlistManager() {
        return this.gpuWarnlistManager;
    }
    
    public SoundManager getSoundManager() {
        return this.soundManager;
    }
    
    public Music getSituationalMusic() {
        if (this.screen instanceof WinScreen) {
            return Musics.CREDITS;
        }
        if (this.player == null) {
            return Musics.MENU;
        }
        if (this.player.level.dimension() == Level.END) {
            if (this.gui.getBossOverlay().shouldPlayMusic()) {
                return Musics.END_BOSS;
            }
            return Musics.END;
        }
        else {
            final Biome.BiomeCategory b2 = this.player.level.getBiome(this.player.blockPosition()).getBiomeCategory();
            if (this.musicManager.isPlayingMusic(Musics.UNDER_WATER) || (this.player.isUnderWater() && (b2 == Biome.BiomeCategory.OCEAN || b2 == Biome.BiomeCategory.RIVER))) {
                return Musics.UNDER_WATER;
            }
            if (this.player.level.dimension() != Level.NETHER && this.player.abilities.instabuild && this.player.abilities.mayfly) {
                return Musics.CREATIVE;
            }
            return (Music)this.level.getBiomeManager().getNoiseBiomeAtPosition(this.player.blockPosition()).getBackgroundMusic().orElse(Musics.GAME);
        }
    }
    
    public MinecraftSessionService getMinecraftSessionService() {
        return this.minecraftSessionService;
    }
    
    public SkinManager getSkinManager() {
        return this.skinManager;
    }
    
    @Nullable
    public Entity getCameraEntity() {
        return this.cameraEntity;
    }
    
    public void setCameraEntity(final Entity apx) {
        this.cameraEntity = apx;
        this.gameRenderer.checkEntityPostEffect(apx);
    }
    
    public boolean shouldEntityAppearGlowing(final Entity apx) {
        return apx.isGlowing() || (this.player != null && this.player.isSpectator() && this.options.keySpectatorOutlines.isDown() && apx.getType() == EntityType.PLAYER);
    }
    
    @Override
    protected Thread getRunningThread() {
        return this.gameThread;
    }
    
    @Override
    protected Runnable wrapRunnable(final Runnable runnable) {
        return runnable;
    }
    
    @Override
    protected boolean shouldRun(final Runnable runnable) {
        return true;
    }
    
    public BlockRenderDispatcher getBlockRenderer() {
        return this.blockRenderer;
    }
    
    public EntityRenderDispatcher getEntityRenderDispatcher() {
        return this.entityRenderDispatcher;
    }
    
    public ItemRenderer getItemRenderer() {
        return this.itemRenderer;
    }
    
    public ItemInHandRenderer getItemInHandRenderer() {
        return this.itemInHandRenderer;
    }
    
    public <T> MutableSearchTree<T> getSearchTree(final SearchRegistry.Key<T> a) {
        return this.searchRegistry.<T>getTree(a);
    }
    
    public FrameTimer getFrameTimer() {
        return this.frameTimer;
    }
    
    public boolean isConnectedToRealms() {
        return this.connectedToRealms;
    }
    
    public void setConnectedToRealms(final boolean boolean1) {
        this.connectedToRealms = boolean1;
    }
    
    public DataFixer getFixerUpper() {
        return this.fixerUpper;
    }
    
    public float getFrameTime() {
        return this.timer.partialTick;
    }
    
    public float getDeltaFrameTime() {
        return this.timer.tickDelta;
    }
    
    public BlockColors getBlockColors() {
        return this.blockColors;
    }
    
    public boolean showOnlyReducedInfo() {
        return (this.player != null && this.player.isReducedDebugInfo()) || this.options.reducedDebugInfo;
    }
    
    public ToastComponent getToasts() {
        return this.toast;
    }
    
    public Tutorial getTutorial() {
        return this.tutorial;
    }
    
    public boolean isWindowActive() {
        return this.windowActive;
    }
    
    public HotbarManager getHotbarManager() {
        return this.hotbarManager;
    }
    
    public ModelManager getModelManager() {
        return this.modelManager;
    }
    
    public PaintingTextureManager getPaintingTextures() {
        return this.paintingTextures;
    }
    
    public MobEffectTextureManager getMobEffectTextures() {
        return this.mobEffectTextures;
    }
    
    @Override
    public void setWindowActive(final boolean boolean1) {
        this.windowActive = boolean1;
    }
    
    public ProfilerFiller getProfiler() {
        return this.profiler;
    }
    
    public Game getGame() {
        return this.game;
    }
    
    public SplashManager getSplashManager() {
        return this.splashManager;
    }
    
    @Nullable
    public Overlay getOverlay() {
        return this.overlay;
    }
    
    public boolean renderOnThread() {
        return false;
    }
    
    public Window getWindow() {
        return this.window;
    }
    
    public RenderBuffers renderBuffers() {
        return this.renderBuffers;
    }
    
    private static Pack createClientPackAdapter(final String string, final boolean boolean2, final Supplier<PackResources> supplier, final PackResources abh, final PackMetadataSection abm, final Pack.Position b, final PackSource abv) {
        final int integer8 = abm.getPackFormat();
        Supplier<PackResources> supplier2 = supplier;
        if (integer8 <= 3) {
            supplier2 = adaptV3(supplier2);
        }
        if (integer8 <= 4) {
            supplier2 = adaptV4(supplier2);
        }
        return new Pack(string, boolean2, supplier2, abh, abm, b, abv);
    }
    
    private static Supplier<PackResources> adaptV3(final Supplier<PackResources> supplier) {
        return (Supplier<PackResources>)(() -> new LegacyPackResourcesAdapter((PackResources)supplier.get(), LegacyPackResourcesAdapter.V3));
    }
    
    private static Supplier<PackResources> adaptV4(final Supplier<PackResources> supplier) {
        return (Supplier<PackResources>)(() -> new PackResourcesAdapterV4((PackResources)supplier.get()));
    }
    
    public void updateMaxMipLevel(final int integer) {
        this.modelManager.updateMaxMipLevel(integer);
    }
    
    static {
        LOGGER = LogManager.getLogger();
        ON_OSX = (Util.getPlatform() == Util.OS.OSX);
        DEFAULT_FONT = new ResourceLocation("default");
        UNIFORM_FONT = new ResourceLocation("uniform");
        ALT_FONT = new ResourceLocation("alt");
        RESOURCE_RELOAD_INITIAL_TASK = CompletableFuture.completedFuture(Unit.INSTANCE);
        Minecraft.reserve = new byte[10485760];
    }
    
    enum ExperimentalDialogType {
        NONE, 
        CREATE, 
        BACKUP;
    }
    
    public static final class ServerStem implements AutoCloseable {
        private final PackRepository packRepository;
        private final ServerResources serverResources;
        private final WorldData worldData;
        
        private ServerStem(final PackRepository abu, final ServerResources vz, final WorldData cyk) {
            this.packRepository = abu;
            this.serverResources = vz;
            this.worldData = cyk;
        }
        
        public PackRepository packRepository() {
            return this.packRepository;
        }
        
        public ServerResources serverResources() {
            return this.serverResources;
        }
        
        public WorldData worldData() {
            return this.worldData;
        }
        
        public void close() {
            this.packRepository.close();
            this.serverResources.close();
        }
    }
}

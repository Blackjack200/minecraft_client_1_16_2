package net.minecraft.client;

import org.apache.logging.log4j.LogManager;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import java.util.Collection;
import com.google.common.collect.ImmutableSet;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundClientInformationPacket;
import com.mojang.blaze3d.platform.VideoMode;
import net.minecraft.SharedConstants;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.io.FileOutputStream;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.datafix.DataFixTypes;
import java.util.Iterator;
import java.io.BufferedReader;
import net.minecraft.util.GsonHelper;
import com.google.common.io.Files;
import com.google.common.base.Charsets;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.Util;
import org.apache.commons.lang3.ArrayUtils;
import com.mojang.blaze3d.platform.InputConstants;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.Lists;
import net.minecraft.world.Difficulty;
import java.io.File;
import net.minecraft.client.tutorial.TutorialSteps;
import net.minecraft.sounds.SoundSource;
import java.util.Map;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.PlayerModelPart;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.ChatVisiblity;
import com.google.common.base.Splitter;
import java.util.List;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;

public class Options {
    private static final Logger LOGGER;
    private static final Gson GSON;
    private static final TypeToken<List<String>> RESOURCE_PACK_TYPE;
    private static final Splitter OPTION_SPLITTER;
    public double sensitivity;
    public int renderDistance;
    public float entityDistanceScaling;
    public int framerateLimit;
    public CloudStatus renderClouds;
    public GraphicsStatus graphicsMode;
    public AmbientOcclusionStatus ambientOcclusion;
    public List<String> resourcePacks;
    public List<String> incompatibleResourcePacks;
    public ChatVisiblity chatVisibility;
    public double chatOpacity;
    public double chatLineSpacing;
    public double textBackgroundOpacity;
    @Nullable
    public String fullscreenVideoModeString;
    public boolean hideServerAddress;
    public boolean advancedItemTooltips;
    public boolean pauseOnLostFocus;
    private final Set<PlayerModelPart> modelParts;
    public HumanoidArm mainHand;
    public int overrideWidth;
    public int overrideHeight;
    public boolean heldItemTooltips;
    public double chatScale;
    public double chatWidth;
    public double chatHeightUnfocused;
    public double chatHeightFocused;
    public double chatDelay;
    public int mipmapLevels;
    private final Map<SoundSource, Float> sourceVolumes;
    public boolean useNativeTransport;
    public AttackIndicatorStatus attackIndicator;
    public TutorialSteps tutorialStep;
    public int biomeBlendRadius;
    public double mouseWheelSensitivity;
    public boolean rawMouseInput;
    public int glDebugVerbosity;
    public boolean autoJump;
    public boolean autoSuggestions;
    public boolean chatColors;
    public boolean chatLinks;
    public boolean chatLinksPrompt;
    public boolean enableVsync;
    public boolean entityShadows;
    public boolean forceUnicodeFont;
    public boolean invertYMouse;
    public boolean discreteMouseScroll;
    public boolean realmsNotifications;
    public boolean reducedDebugInfo;
    public boolean snooperEnabled;
    public boolean showSubtitles;
    public boolean backgroundForChatOnly;
    public boolean touchscreen;
    public boolean fullscreen;
    public boolean bobView;
    public boolean toggleCrouch;
    public boolean toggleSprint;
    public boolean skipMultiplayerWarning;
    public final KeyMapping keyUp;
    public final KeyMapping keyLeft;
    public final KeyMapping keyDown;
    public final KeyMapping keyRight;
    public final KeyMapping keyJump;
    public final KeyMapping keyShift;
    public final KeyMapping keySprint;
    public final KeyMapping keyInventory;
    public final KeyMapping keySwapOffhand;
    public final KeyMapping keyDrop;
    public final KeyMapping keyUse;
    public final KeyMapping keyAttack;
    public final KeyMapping keyPickItem;
    public final KeyMapping keyChat;
    public final KeyMapping keyPlayerList;
    public final KeyMapping keyCommand;
    public final KeyMapping keyScreenshot;
    public final KeyMapping keyTogglePerspective;
    public final KeyMapping keySmoothCamera;
    public final KeyMapping keyFullscreen;
    public final KeyMapping keySpectatorOutlines;
    public final KeyMapping keyAdvancements;
    public final KeyMapping[] keyHotbarSlots;
    public final KeyMapping keySaveHotbarActivator;
    public final KeyMapping keyLoadHotbarActivator;
    public final KeyMapping[] keyMappings;
    protected Minecraft minecraft;
    private final File optionsFile;
    public Difficulty difficulty;
    public boolean hideGui;
    private CameraType cameraType;
    public boolean renderDebug;
    public boolean renderDebugCharts;
    public boolean renderFpsChart;
    public String lastMpIp;
    public boolean smoothCamera;
    public double fov;
    public float screenEffectScale;
    public float fovEffectScale;
    public double gamma;
    public int guiScale;
    public ParticleStatus particles;
    public NarratorStatus narratorStatus;
    public String languageCode;
    public boolean syncWrites;
    
    public Options(final Minecraft djw, final File file) {
        this.sensitivity = 0.5;
        this.renderDistance = -1;
        this.entityDistanceScaling = 1.0f;
        this.framerateLimit = 120;
        this.renderClouds = CloudStatus.FANCY;
        this.graphicsMode = GraphicsStatus.FANCY;
        this.ambientOcclusion = AmbientOcclusionStatus.MAX;
        this.resourcePacks = (List<String>)Lists.newArrayList();
        this.incompatibleResourcePacks = (List<String>)Lists.newArrayList();
        this.chatVisibility = ChatVisiblity.FULL;
        this.chatOpacity = 1.0;
        this.chatLineSpacing = 0.0;
        this.textBackgroundOpacity = 0.5;
        this.pauseOnLostFocus = true;
        this.modelParts = (Set<PlayerModelPart>)Sets.newHashSet((Object[])PlayerModelPart.values());
        this.mainHand = HumanoidArm.RIGHT;
        this.heldItemTooltips = true;
        this.chatScale = 1.0;
        this.chatWidth = 1.0;
        this.chatHeightUnfocused = 0.44366195797920227;
        this.chatHeightFocused = 1.0;
        this.chatDelay = 0.0;
        this.mipmapLevels = 4;
        this.sourceVolumes = (Map<SoundSource, Float>)Maps.newEnumMap((Class)SoundSource.class);
        this.useNativeTransport = true;
        this.attackIndicator = AttackIndicatorStatus.CROSSHAIR;
        this.tutorialStep = TutorialSteps.MOVEMENT;
        this.biomeBlendRadius = 2;
        this.mouseWheelSensitivity = 1.0;
        this.rawMouseInput = true;
        this.glDebugVerbosity = 1;
        this.autoJump = true;
        this.autoSuggestions = true;
        this.chatColors = true;
        this.chatLinks = true;
        this.chatLinksPrompt = true;
        this.enableVsync = true;
        this.entityShadows = true;
        this.realmsNotifications = true;
        this.snooperEnabled = true;
        this.backgroundForChatOnly = true;
        this.bobView = true;
        this.keyUp = new KeyMapping("key.forward", 87, "key.categories.movement");
        this.keyLeft = new KeyMapping("key.left", 65, "key.categories.movement");
        this.keyDown = new KeyMapping("key.back", 83, "key.categories.movement");
        this.keyRight = new KeyMapping("key.right", 68, "key.categories.movement");
        this.keyJump = new KeyMapping("key.jump", 32, "key.categories.movement");
        this.keyShift = new ToggleKeyMapping("key.sneak", 340, "key.categories.movement", () -> this.toggleCrouch);
        this.keySprint = new ToggleKeyMapping("key.sprint", 341, "key.categories.movement", () -> this.toggleSprint);
        this.keyInventory = new KeyMapping("key.inventory", 69, "key.categories.inventory");
        this.keySwapOffhand = new KeyMapping("key.swapOffhand", 70, "key.categories.inventory");
        this.keyDrop = new KeyMapping("key.drop", 81, "key.categories.inventory");
        this.keyUse = new KeyMapping("key.use", InputConstants.Type.MOUSE, 1, "key.categories.gameplay");
        this.keyAttack = new KeyMapping("key.attack", InputConstants.Type.MOUSE, 0, "key.categories.gameplay");
        this.keyPickItem = new KeyMapping("key.pickItem", InputConstants.Type.MOUSE, 2, "key.categories.gameplay");
        this.keyChat = new KeyMapping("key.chat", 84, "key.categories.multiplayer");
        this.keyPlayerList = new KeyMapping("key.playerlist", 258, "key.categories.multiplayer");
        this.keyCommand = new KeyMapping("key.command", 47, "key.categories.multiplayer");
        this.keyScreenshot = new KeyMapping("key.screenshot", 291, "key.categories.misc");
        this.keyTogglePerspective = new KeyMapping("key.togglePerspective", 294, "key.categories.misc");
        this.keySmoothCamera = new KeyMapping("key.smoothCamera", InputConstants.UNKNOWN.getValue(), "key.categories.misc");
        this.keyFullscreen = new KeyMapping("key.fullscreen", 300, "key.categories.misc");
        this.keySpectatorOutlines = new KeyMapping("key.spectatorOutlines", InputConstants.UNKNOWN.getValue(), "key.categories.misc");
        this.keyAdvancements = new KeyMapping("key.advancements", 76, "key.categories.misc");
        this.keyHotbarSlots = new KeyMapping[] { new KeyMapping("key.hotbar.1", 49, "key.categories.inventory"), new KeyMapping("key.hotbar.2", 50, "key.categories.inventory"), new KeyMapping("key.hotbar.3", 51, "key.categories.inventory"), new KeyMapping("key.hotbar.4", 52, "key.categories.inventory"), new KeyMapping("key.hotbar.5", 53, "key.categories.inventory"), new KeyMapping("key.hotbar.6", 54, "key.categories.inventory"), new KeyMapping("key.hotbar.7", 55, "key.categories.inventory"), new KeyMapping("key.hotbar.8", 56, "key.categories.inventory"), new KeyMapping("key.hotbar.9", 57, "key.categories.inventory") };
        this.keySaveHotbarActivator = new KeyMapping("key.saveToolbarActivator", 67, "key.categories.creative");
        this.keyLoadHotbarActivator = new KeyMapping("key.loadToolbarActivator", 88, "key.categories.creative");
        this.keyMappings = (KeyMapping[])ArrayUtils.addAll((Object[])new KeyMapping[] { this.keyAttack, this.keyUse, this.keyUp, this.keyLeft, this.keyDown, this.keyRight, this.keyJump, this.keyShift, this.keySprint, this.keyDrop, this.keyInventory, this.keyChat, this.keyPlayerList, this.keyPickItem, this.keyCommand, this.keyScreenshot, this.keyTogglePerspective, this.keySmoothCamera, this.keyFullscreen, this.keySpectatorOutlines, this.keySwapOffhand, this.keySaveHotbarActivator, this.keyLoadHotbarActivator, this.keyAdvancements }, (Object[])this.keyHotbarSlots);
        this.difficulty = Difficulty.NORMAL;
        this.cameraType = CameraType.FIRST_PERSON;
        this.lastMpIp = "";
        this.fov = 70.0;
        this.screenEffectScale = 1.0f;
        this.fovEffectScale = 1.0f;
        this.particles = ParticleStatus.ALL;
        this.narratorStatus = NarratorStatus.OFF;
        this.languageCode = "en_us";
        this.minecraft = djw;
        this.optionsFile = new File(file, "options.txt");
        if (djw.is64Bit() && Runtime.getRuntime().maxMemory() >= 1000000000L) {
            Option.RENDER_DISTANCE.setMaxValue(32.0f);
        }
        else {
            Option.RENDER_DISTANCE.setMaxValue(16.0f);
        }
        this.renderDistance = (djw.is64Bit() ? 12 : 8);
        this.syncWrites = (Util.getPlatform() == Util.OS.WINDOWS);
        this.load();
    }
    
    public float getBackgroundOpacity(final float float1) {
        return this.backgroundForChatOnly ? float1 : ((float)this.textBackgroundOpacity);
    }
    
    public int getBackgroundColor(final float float1) {
        return (int)(this.getBackgroundOpacity(float1) * 255.0f) << 24 & 0xFF000000;
    }
    
    public int getBackgroundColor(final int integer) {
        return this.backgroundForChatOnly ? integer : ((int)(this.textBackgroundOpacity * 255.0) << 24 & 0xFF000000);
    }
    
    public void setKey(final KeyMapping djt, final InputConstants.Key a) {
        djt.setKey(a);
        this.save();
    }
    
    public void load() {
        try {
            if (!this.optionsFile.exists()) {
                return;
            }
            this.sourceVolumes.clear();
            final CompoundTag md2 = new CompoundTag();
            try (final BufferedReader bufferedReader3 = Files.newReader(this.optionsFile, Charsets.UTF_8)) {
                bufferedReader3.lines().forEach(string -> {
                    try {
                        final Iterator<String> iterator3 = (Iterator<String>)Options.OPTION_SPLITTER.split((CharSequence)string).iterator();
                        md2.putString((String)iterator3.next(), (String)iterator3.next());
                    }
                    catch (Exception exception3) {
                        Options.LOGGER.warn("Skipping bad option: {}", string);
                    }
                });
            }
            final CompoundTag md3 = this.dataFix(md2);
            if (!md3.contains("graphicsMode") && md3.contains("fancyGraphics")) {
                if ("true".equals(md3.getString("fancyGraphics"))) {
                    this.graphicsMode = GraphicsStatus.FANCY;
                }
                else {
                    this.graphicsMode = GraphicsStatus.FAST;
                }
            }
            for (final String string5 : md3.getAllKeys()) {
                final String string6 = md3.getString(string5);
                try {
                    if ("autoJump".equals(string5)) {
                        Option.AUTO_JUMP.set(this, string6);
                    }
                    if ("autoSuggestions".equals(string5)) {
                        Option.AUTO_SUGGESTIONS.set(this, string6);
                    }
                    if ("chatColors".equals(string5)) {
                        Option.CHAT_COLOR.set(this, string6);
                    }
                    if ("chatLinks".equals(string5)) {
                        Option.CHAT_LINKS.set(this, string6);
                    }
                    if ("chatLinksPrompt".equals(string5)) {
                        Option.CHAT_LINKS_PROMPT.set(this, string6);
                    }
                    if ("enableVsync".equals(string5)) {
                        Option.ENABLE_VSYNC.set(this, string6);
                    }
                    if ("entityShadows".equals(string5)) {
                        Option.ENTITY_SHADOWS.set(this, string6);
                    }
                    if ("forceUnicodeFont".equals(string5)) {
                        Option.FORCE_UNICODE_FONT.set(this, string6);
                    }
                    if ("discrete_mouse_scroll".equals(string5)) {
                        Option.DISCRETE_MOUSE_SCROLL.set(this, string6);
                    }
                    if ("invertYMouse".equals(string5)) {
                        Option.INVERT_MOUSE.set(this, string6);
                    }
                    if ("realmsNotifications".equals(string5)) {
                        Option.REALMS_NOTIFICATIONS.set(this, string6);
                    }
                    if ("reducedDebugInfo".equals(string5)) {
                        Option.REDUCED_DEBUG_INFO.set(this, string6);
                    }
                    if ("showSubtitles".equals(string5)) {
                        Option.SHOW_SUBTITLES.set(this, string6);
                    }
                    if ("snooperEnabled".equals(string5)) {
                        Option.SNOOPER_ENABLED.set(this, string6);
                    }
                    if ("touchscreen".equals(string5)) {
                        Option.TOUCHSCREEN.set(this, string6);
                    }
                    if ("fullscreen".equals(string5)) {
                        Option.USE_FULLSCREEN.set(this, string6);
                    }
                    if ("bobView".equals(string5)) {
                        Option.VIEW_BOBBING.set(this, string6);
                    }
                    if ("toggleCrouch".equals(string5)) {
                        this.toggleCrouch = "true".equals(string6);
                    }
                    if ("toggleSprint".equals(string5)) {
                        this.toggleSprint = "true".equals(string6);
                    }
                    if ("mouseSensitivity".equals(string5)) {
                        this.sensitivity = readFloat(string6);
                    }
                    if ("fov".equals(string5)) {
                        this.fov = readFloat(string6) * 40.0f + 70.0f;
                    }
                    if ("screenEffectScale".equals(string5)) {
                        this.screenEffectScale = readFloat(string6);
                    }
                    if ("fovEffectScale".equals(string5)) {
                        this.fovEffectScale = readFloat(string6);
                    }
                    if ("gamma".equals(string5)) {
                        this.gamma = readFloat(string6);
                    }
                    if ("renderDistance".equals(string5)) {
                        this.renderDistance = Integer.parseInt(string6);
                    }
                    if ("entityDistanceScaling".equals(string5)) {
                        this.entityDistanceScaling = Float.parseFloat(string6);
                    }
                    if ("guiScale".equals(string5)) {
                        this.guiScale = Integer.parseInt(string6);
                    }
                    if ("particles".equals(string5)) {
                        this.particles = ParticleStatus.byId(Integer.parseInt(string6));
                    }
                    if ("maxFps".equals(string5)) {
                        this.framerateLimit = Integer.parseInt(string6);
                        if (this.minecraft.getWindow() != null) {
                            this.minecraft.getWindow().setFramerateLimit(this.framerateLimit);
                        }
                    }
                    if ("difficulty".equals(string5)) {
                        this.difficulty = Difficulty.byId(Integer.parseInt(string6));
                    }
                    if ("graphicsMode".equals(string5)) {
                        this.graphicsMode = GraphicsStatus.byId(Integer.parseInt(string6));
                    }
                    if ("tutorialStep".equals(string5)) {
                        this.tutorialStep = TutorialSteps.getByName(string6);
                    }
                    if ("ao".equals(string5)) {
                        if ("true".equals(string6)) {
                            this.ambientOcclusion = AmbientOcclusionStatus.MAX;
                        }
                        else if ("false".equals(string6)) {
                            this.ambientOcclusion = AmbientOcclusionStatus.OFF;
                        }
                        else {
                            this.ambientOcclusion = AmbientOcclusionStatus.byId(Integer.parseInt(string6));
                        }
                    }
                    if ("renderClouds".equals(string5)) {
                        if ("true".equals(string6)) {
                            this.renderClouds = CloudStatus.FANCY;
                        }
                        else if ("false".equals(string6)) {
                            this.renderClouds = CloudStatus.OFF;
                        }
                        else if ("fast".equals(string6)) {
                            this.renderClouds = CloudStatus.FAST;
                        }
                    }
                    if ("attackIndicator".equals(string5)) {
                        this.attackIndicator = AttackIndicatorStatus.byId(Integer.parseInt(string6));
                    }
                    if ("resourcePacks".equals(string5)) {
                        this.resourcePacks = GsonHelper.<List<String>>fromJson(Options.GSON, string6, Options.RESOURCE_PACK_TYPE);
                        if (this.resourcePacks == null) {
                            this.resourcePacks = (List<String>)Lists.newArrayList();
                        }
                    }
                    if ("incompatibleResourcePacks".equals(string5)) {
                        this.incompatibleResourcePacks = GsonHelper.<List<String>>fromJson(Options.GSON, string6, Options.RESOURCE_PACK_TYPE);
                        if (this.incompatibleResourcePacks == null) {
                            this.incompatibleResourcePacks = (List<String>)Lists.newArrayList();
                        }
                    }
                    if ("lastServer".equals(string5)) {
                        this.lastMpIp = string6;
                    }
                    if ("lang".equals(string5)) {
                        this.languageCode = string6;
                    }
                    if ("chatVisibility".equals(string5)) {
                        this.chatVisibility = ChatVisiblity.byId(Integer.parseInt(string6));
                    }
                    if ("chatOpacity".equals(string5)) {
                        this.chatOpacity = readFloat(string6);
                    }
                    if ("chatLineSpacing".equals(string5)) {
                        this.chatLineSpacing = readFloat(string6);
                    }
                    if ("textBackgroundOpacity".equals(string5)) {
                        this.textBackgroundOpacity = readFloat(string6);
                    }
                    if ("backgroundForChatOnly".equals(string5)) {
                        this.backgroundForChatOnly = "true".equals(string6);
                    }
                    if ("fullscreenResolution".equals(string5)) {
                        this.fullscreenVideoModeString = string6;
                    }
                    if ("hideServerAddress".equals(string5)) {
                        this.hideServerAddress = "true".equals(string6);
                    }
                    if ("advancedItemTooltips".equals(string5)) {
                        this.advancedItemTooltips = "true".equals(string6);
                    }
                    if ("pauseOnLostFocus".equals(string5)) {
                        this.pauseOnLostFocus = "true".equals(string6);
                    }
                    if ("overrideHeight".equals(string5)) {
                        this.overrideHeight = Integer.parseInt(string6);
                    }
                    if ("overrideWidth".equals(string5)) {
                        this.overrideWidth = Integer.parseInt(string6);
                    }
                    if ("heldItemTooltips".equals(string5)) {
                        this.heldItemTooltips = "true".equals(string6);
                    }
                    if ("chatHeightFocused".equals(string5)) {
                        this.chatHeightFocused = readFloat(string6);
                    }
                    if ("chatDelay".equals(string5)) {
                        this.chatDelay = readFloat(string6);
                    }
                    if ("chatHeightUnfocused".equals(string5)) {
                        this.chatHeightUnfocused = readFloat(string6);
                    }
                    if ("chatScale".equals(string5)) {
                        this.chatScale = readFloat(string6);
                    }
                    if ("chatWidth".equals(string5)) {
                        this.chatWidth = readFloat(string6);
                    }
                    if ("mipmapLevels".equals(string5)) {
                        this.mipmapLevels = Integer.parseInt(string6);
                    }
                    if ("useNativeTransport".equals(string5)) {
                        this.useNativeTransport = "true".equals(string6);
                    }
                    if ("mainHand".equals(string5)) {
                        this.mainHand = ("left".equals(string6) ? HumanoidArm.LEFT : HumanoidArm.RIGHT);
                    }
                    if ("narrator".equals(string5)) {
                        this.narratorStatus = NarratorStatus.byId(Integer.parseInt(string6));
                    }
                    if ("biomeBlendRadius".equals(string5)) {
                        this.biomeBlendRadius = Integer.parseInt(string6);
                    }
                    if ("mouseWheelSensitivity".equals(string5)) {
                        this.mouseWheelSensitivity = readFloat(string6);
                    }
                    if ("rawMouseInput".equals(string5)) {
                        this.rawMouseInput = "true".equals(string6);
                    }
                    if ("glDebugVerbosity".equals(string5)) {
                        this.glDebugVerbosity = Integer.parseInt(string6);
                    }
                    if ("skipMultiplayerWarning".equals(string5)) {
                        this.skipMultiplayerWarning = "true".equals(string6);
                    }
                    if ("syncChunkWrites".equals(string5)) {
                        this.syncWrites = "true".equals(string6);
                    }
                    for (final KeyMapping djt10 : this.keyMappings) {
                        if (string5.equals(("key_" + djt10.getName()))) {
                            djt10.setKey(InputConstants.getKey(string6));
                        }
                    }
                    for (final SoundSource adp10 : SoundSource.values()) {
                        if (string5.equals(("soundCategory_" + adp10.getName()))) {
                            this.sourceVolumes.put(adp10, readFloat(string6));
                        }
                    }
                    for (final PlayerModelPart bfu10 : PlayerModelPart.values()) {
                        if (string5.equals(("modelPart_" + bfu10.getId()))) {
                            this.setModelPart(bfu10, "true".equals(string6));
                        }
                    }
                }
                catch (Exception exception3) {
                    Options.LOGGER.warn("Skipping bad option: {}:{}", string5, string6);
                }
            }
            KeyMapping.resetMapping();
        }
        catch (Exception exception2) {
            Options.LOGGER.error("Failed to load options", (Throwable)exception2);
        }
    }
    
    private CompoundTag dataFix(final CompoundTag md) {
        int integer3 = 0;
        try {
            integer3 = Integer.parseInt(md.getString("version"));
        }
        catch (RuntimeException ex) {}
        return NbtUtils.update(this.minecraft.getFixerUpper(), DataFixTypes.OPTIONS, md, integer3);
    }
    
    private static float readFloat(final String string) {
        if ("true".equals(string)) {
            return 1.0f;
        }
        if ("false".equals(string)) {
            return 0.0f;
        }
        return Float.parseFloat(string);
    }
    
    public void save() {
        try (final PrintWriter printWriter2 = new PrintWriter((Writer)new OutputStreamWriter((OutputStream)new FileOutputStream(this.optionsFile), StandardCharsets.UTF_8))) {
            printWriter2.println(new StringBuilder().append("version:").append(SharedConstants.getCurrentVersion().getWorldVersion()).toString());
            printWriter2.println(new StringBuilder().append("autoJump:").append(Option.AUTO_JUMP.get(this)).toString());
            printWriter2.println(new StringBuilder().append("autoSuggestions:").append(Option.AUTO_SUGGESTIONS.get(this)).toString());
            printWriter2.println(new StringBuilder().append("chatColors:").append(Option.CHAT_COLOR.get(this)).toString());
            printWriter2.println(new StringBuilder().append("chatLinks:").append(Option.CHAT_LINKS.get(this)).toString());
            printWriter2.println(new StringBuilder().append("chatLinksPrompt:").append(Option.CHAT_LINKS_PROMPT.get(this)).toString());
            printWriter2.println(new StringBuilder().append("enableVsync:").append(Option.ENABLE_VSYNC.get(this)).toString());
            printWriter2.println(new StringBuilder().append("entityShadows:").append(Option.ENTITY_SHADOWS.get(this)).toString());
            printWriter2.println(new StringBuilder().append("forceUnicodeFont:").append(Option.FORCE_UNICODE_FONT.get(this)).toString());
            printWriter2.println(new StringBuilder().append("discrete_mouse_scroll:").append(Option.DISCRETE_MOUSE_SCROLL.get(this)).toString());
            printWriter2.println(new StringBuilder().append("invertYMouse:").append(Option.INVERT_MOUSE.get(this)).toString());
            printWriter2.println(new StringBuilder().append("realmsNotifications:").append(Option.REALMS_NOTIFICATIONS.get(this)).toString());
            printWriter2.println(new StringBuilder().append("reducedDebugInfo:").append(Option.REDUCED_DEBUG_INFO.get(this)).toString());
            printWriter2.println(new StringBuilder().append("snooperEnabled:").append(Option.SNOOPER_ENABLED.get(this)).toString());
            printWriter2.println(new StringBuilder().append("showSubtitles:").append(Option.SHOW_SUBTITLES.get(this)).toString());
            printWriter2.println(new StringBuilder().append("touchscreen:").append(Option.TOUCHSCREEN.get(this)).toString());
            printWriter2.println(new StringBuilder().append("fullscreen:").append(Option.USE_FULLSCREEN.get(this)).toString());
            printWriter2.println(new StringBuilder().append("bobView:").append(Option.VIEW_BOBBING.get(this)).toString());
            printWriter2.println(new StringBuilder().append("toggleCrouch:").append(this.toggleCrouch).toString());
            printWriter2.println(new StringBuilder().append("toggleSprint:").append(this.toggleSprint).toString());
            printWriter2.println(new StringBuilder().append("mouseSensitivity:").append(this.sensitivity).toString());
            printWriter2.println(new StringBuilder().append("fov:").append((this.fov - 70.0) / 40.0).toString());
            printWriter2.println(new StringBuilder().append("screenEffectScale:").append(this.screenEffectScale).toString());
            printWriter2.println(new StringBuilder().append("fovEffectScale:").append(this.fovEffectScale).toString());
            printWriter2.println(new StringBuilder().append("gamma:").append(this.gamma).toString());
            printWriter2.println(new StringBuilder().append("renderDistance:").append(this.renderDistance).toString());
            printWriter2.println(new StringBuilder().append("entityDistanceScaling:").append(this.entityDistanceScaling).toString());
            printWriter2.println(new StringBuilder().append("guiScale:").append(this.guiScale).toString());
            printWriter2.println(new StringBuilder().append("particles:").append(this.particles.getId()).toString());
            printWriter2.println(new StringBuilder().append("maxFps:").append(this.framerateLimit).toString());
            printWriter2.println(new StringBuilder().append("difficulty:").append(this.difficulty.getId()).toString());
            printWriter2.println(new StringBuilder().append("graphicsMode:").append(this.graphicsMode.getId()).toString());
            printWriter2.println(new StringBuilder().append("ao:").append(this.ambientOcclusion.getId()).toString());
            printWriter2.println(new StringBuilder().append("biomeBlendRadius:").append(this.biomeBlendRadius).toString());
            switch (this.renderClouds) {
                case FANCY: {
                    printWriter2.println("renderClouds:true");
                    break;
                }
                case FAST: {
                    printWriter2.println("renderClouds:fast");
                    break;
                }
                case OFF: {
                    printWriter2.println("renderClouds:false");
                    break;
                }
            }
            printWriter2.println("resourcePacks:" + Options.GSON.toJson(this.resourcePacks));
            printWriter2.println("incompatibleResourcePacks:" + Options.GSON.toJson(this.incompatibleResourcePacks));
            printWriter2.println("lastServer:" + this.lastMpIp);
            printWriter2.println("lang:" + this.languageCode);
            printWriter2.println(new StringBuilder().append("chatVisibility:").append(this.chatVisibility.getId()).toString());
            printWriter2.println(new StringBuilder().append("chatOpacity:").append(this.chatOpacity).toString());
            printWriter2.println(new StringBuilder().append("chatLineSpacing:").append(this.chatLineSpacing).toString());
            printWriter2.println(new StringBuilder().append("textBackgroundOpacity:").append(this.textBackgroundOpacity).toString());
            printWriter2.println(new StringBuilder().append("backgroundForChatOnly:").append(this.backgroundForChatOnly).toString());
            if (this.minecraft.getWindow().getPreferredFullscreenVideoMode().isPresent()) {
                printWriter2.println("fullscreenResolution:" + ((VideoMode)this.minecraft.getWindow().getPreferredFullscreenVideoMode().get()).write());
            }
            printWriter2.println(new StringBuilder().append("hideServerAddress:").append(this.hideServerAddress).toString());
            printWriter2.println(new StringBuilder().append("advancedItemTooltips:").append(this.advancedItemTooltips).toString());
            printWriter2.println(new StringBuilder().append("pauseOnLostFocus:").append(this.pauseOnLostFocus).toString());
            printWriter2.println(new StringBuilder().append("overrideWidth:").append(this.overrideWidth).toString());
            printWriter2.println(new StringBuilder().append("overrideHeight:").append(this.overrideHeight).toString());
            printWriter2.println(new StringBuilder().append("heldItemTooltips:").append(this.heldItemTooltips).toString());
            printWriter2.println(new StringBuilder().append("chatHeightFocused:").append(this.chatHeightFocused).toString());
            printWriter2.println(new StringBuilder().append("chatDelay: ").append(this.chatDelay).toString());
            printWriter2.println(new StringBuilder().append("chatHeightUnfocused:").append(this.chatHeightUnfocused).toString());
            printWriter2.println(new StringBuilder().append("chatScale:").append(this.chatScale).toString());
            printWriter2.println(new StringBuilder().append("chatWidth:").append(this.chatWidth).toString());
            printWriter2.println(new StringBuilder().append("mipmapLevels:").append(this.mipmapLevels).toString());
            printWriter2.println(new StringBuilder().append("useNativeTransport:").append(this.useNativeTransport).toString());
            printWriter2.println(new StringBuilder().append("mainHand:").append((this.mainHand == HumanoidArm.LEFT) ? "left" : "right").toString());
            printWriter2.println(new StringBuilder().append("attackIndicator:").append(this.attackIndicator.getId()).toString());
            printWriter2.println(new StringBuilder().append("narrator:").append(this.narratorStatus.getId()).toString());
            printWriter2.println("tutorialStep:" + this.tutorialStep.getName());
            printWriter2.println(new StringBuilder().append("mouseWheelSensitivity:").append(this.mouseWheelSensitivity).toString());
            printWriter2.println(new StringBuilder().append("rawMouseInput:").append(Option.RAW_MOUSE_INPUT.get(this)).toString());
            printWriter2.println(new StringBuilder().append("glDebugVerbosity:").append(this.glDebugVerbosity).toString());
            printWriter2.println(new StringBuilder().append("skipMultiplayerWarning:").append(this.skipMultiplayerWarning).toString());
            printWriter2.println(new StringBuilder().append("syncChunkWrites:").append(this.syncWrites).toString());
            for (final KeyMapping djt7 : this.keyMappings) {
                printWriter2.println("key_" + djt7.getName() + ":" + djt7.saveString());
            }
            for (final SoundSource adp7 : SoundSource.values()) {
                printWriter2.println("soundCategory_" + adp7.getName() + ":" + this.getSoundSourceVolume(adp7));
            }
            for (final PlayerModelPart bfu7 : PlayerModelPart.values()) {
                printWriter2.println("modelPart_" + bfu7.getId() + ":" + this.modelParts.contains(bfu7));
            }
        }
        catch (Exception exception2) {
            Options.LOGGER.error("Failed to save options", (Throwable)exception2);
        }
        this.broadcastOptions();
    }
    
    public float getSoundSourceVolume(final SoundSource adp) {
        if (this.sourceVolumes.containsKey(adp)) {
            return (float)this.sourceVolumes.get(adp);
        }
        return 1.0f;
    }
    
    public void setSoundCategoryVolume(final SoundSource adp, final float float2) {
        this.sourceVolumes.put(adp, float2);
        this.minecraft.getSoundManager().updateSourceVolume(adp, float2);
    }
    
    public void broadcastOptions() {
        if (this.minecraft.player != null) {
            int integer2 = 0;
            for (final PlayerModelPart bfu4 : this.modelParts) {
                integer2 |= bfu4.getMask();
            }
            this.minecraft.player.connection.send(new ServerboundClientInformationPacket(this.languageCode, this.renderDistance, this.chatVisibility, this.chatColors, integer2, this.mainHand));
        }
    }
    
    public Set<PlayerModelPart> getModelParts() {
        return (Set<PlayerModelPart>)ImmutableSet.copyOf((Collection)this.modelParts);
    }
    
    public void setModelPart(final PlayerModelPart bfu, final boolean boolean2) {
        if (boolean2) {
            this.modelParts.add(bfu);
        }
        else {
            this.modelParts.remove(bfu);
        }
        this.broadcastOptions();
    }
    
    public void toggleModelPart(final PlayerModelPart bfu) {
        if (this.getModelParts().contains(bfu)) {
            this.modelParts.remove(bfu);
        }
        else {
            this.modelParts.add(bfu);
        }
        this.broadcastOptions();
    }
    
    public CloudStatus getCloudsType() {
        if (this.renderDistance >= 4) {
            return this.renderClouds;
        }
        return CloudStatus.OFF;
    }
    
    public boolean useNativeTransport() {
        return this.useNativeTransport;
    }
    
    public void loadSelectedResourcePacks(final PackRepository abu) {
        final Set<String> set3 = (Set<String>)Sets.newLinkedHashSet();
        final Iterator<String> iterator4 = (Iterator<String>)this.resourcePacks.iterator();
        while (iterator4.hasNext()) {
            final String string5 = (String)iterator4.next();
            Pack abs6 = abu.getPack(string5);
            if (abs6 == null && !string5.startsWith("file/")) {
                abs6 = abu.getPack("file/" + string5);
            }
            if (abs6 == null) {
                Options.LOGGER.warn("Removed resource pack {} from options because it doesn't seem to exist anymore", string5);
                iterator4.remove();
            }
            else if (!abs6.getCompatibility().isCompatible() && !this.incompatibleResourcePacks.contains(string5)) {
                Options.LOGGER.warn("Removed resource pack {} from options because it is no longer compatible", string5);
                iterator4.remove();
            }
            else if (abs6.getCompatibility().isCompatible() && this.incompatibleResourcePacks.contains(string5)) {
                Options.LOGGER.info("Removed resource pack {} from incompatibility list because it's now compatible", string5);
                this.incompatibleResourcePacks.remove(string5);
            }
            else {
                set3.add(abs6.getId());
            }
        }
        abu.setSelected((Collection<String>)set3);
    }
    
    public CameraType getCameraType() {
        return this.cameraType;
    }
    
    public void setCameraType(final CameraType dji) {
        this.cameraType = dji;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        GSON = new Gson();
        RESOURCE_PACK_TYPE = new TypeToken<List<String>>() {};
        OPTION_SPLITTER = Splitter.on(':').limit(2);
    }
}

package net.minecraft.client.multiplayer;

import org.apache.logging.log4j.LogManager;
import net.minecraft.world.entity.EquipmentSlot;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.components.toasts.RecipeToast;
import net.minecraft.stats.RecipeBook;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import javax.annotation.Nullable;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.network.protocol.game.ClientboundBlockBreakAckPacket;
import net.minecraft.network.protocol.game.ClientboundSetChunkCacheCenterPacket;
import net.minecraft.network.protocol.game.ClientboundSetChunkCacheRadiusPacket;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.network.protocol.game.ClientboundMerchantOffersPacket;
import net.minecraft.world.level.LightLayer;
import net.minecraft.network.protocol.game.ClientboundLightUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundPlaceGhostRecipePacket;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
import net.minecraft.world.scores.Score;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.core.Position;
import java.util.Collection;
import net.minecraft.client.renderer.debug.BeeDebugRenderer;
import net.minecraft.core.PositionImpl;
import net.minecraft.client.renderer.debug.GoalSelectorDebugRenderer;
import net.minecraft.client.renderer.debug.BrainDebugRenderer;
import net.minecraft.client.renderer.debug.WorldGenAttemptRenderer;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.client.renderer.debug.NeighborsUpdateRenderer;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.network.protocol.game.ClientboundOpenBookPacket;
import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket;
import net.minecraft.network.protocol.game.ClientboundMoveVehiclePacket;
import net.minecraft.network.protocol.game.ClientboundCooldownPacket;
import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
import java.net.URISyntaxException;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.io.UnsupportedEncodingException;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.network.protocol.game.ServerboundResourcePackPacket;
import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import net.minecraft.network.protocol.game.ClientboundResourcePackPacket;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.protocol.game.ClientboundCustomSoundPacket;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ServerboundKeepAlivePacket;
import net.minecraft.network.protocol.game.ClientboundKeepAlivePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundSetTitlesPacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderPacket;
import net.minecraft.network.protocol.game.ClientboundSetCameraPacket;
import net.minecraft.network.protocol.game.ClientboundChangeDifficultyPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerCombatPacket;
import com.google.common.collect.Multimap;
import net.minecraft.network.protocol.game.ClientboundUpdateTagsPacket;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.network.protocol.game.ClientboundRecipePacket;
import net.minecraft.client.gui.screens.achievement.StatsUpdateListener;
import net.minecraft.stats.Stat;
import net.minecraft.network.protocol.game.ClientboundAwardStatsPacket;
import net.minecraft.network.protocol.game.ClientboundTagQueryPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerLookAtPacket;
import net.minecraft.client.searchtree.MutableSearchTree;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.client.searchtree.SearchRegistry;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
import net.minecraft.network.protocol.game.ClientboundCommandSuggestionsPacket;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import com.mojang.brigadier.tree.RootCommandNode;
import net.minecraft.network.protocol.game.ClientboundCommandsPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.advancements.Advancement;
import net.minecraft.network.protocol.game.ClientboundSelectAdvancementsTabPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateAdvancementsPacket;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.client.gui.MapRenderer;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.item.MapItem;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.DemoIntroScreen;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.world.level.GameType;
import net.minecraft.util.Mth;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
import net.minecraft.network.protocol.game.ClientboundBlockEventPacket;
import net.minecraft.network.protocol.game.ClientboundContainerClosePacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetDataPacket;
import net.minecraft.client.gui.screens.inventory.CommandBlockEditScreen;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.entity.JigsawBlockEntity;
import net.minecraft.world.level.block.entity.ConduitBlockEntity;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.network.protocol.game.ServerboundContainerAckPacket;
import net.minecraft.network.protocol.game.ClientboundContainerAckPacket;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.HorseInventoryMenu;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.network.protocol.game.ClientboundHorseScreenOpenPacket;
import net.minecraft.world.level.Explosion;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.client.resources.sounds.GuardianAttackSoundInstance;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.Mob;
import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.protocol.game.ClientboundSetDefaultSpawnPositionPacket;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import net.minecraft.client.resources.sounds.BeeSoundInstance;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.client.resources.sounds.BeeFlyingSoundInstance;
import net.minecraft.client.resources.sounds.BeeAggressiveSoundInstance;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.network.protocol.game.ClientboundAddMobPacket;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundChatPacket;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ItemPickupParticle;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.realms.DisconnectedRealmsScreen;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.network.protocol.game.ClientboundDisconnectPacket;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.protocol.game.ClientboundForgetLevelChunkPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import java.util.Iterator;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.core.IdMap;
import net.minecraft.world.level.chunk.ChunkBiomeContainer;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacket;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.BiConsumer;
import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundAcceptTeleportationPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.protocol.game.ClientboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.network.protocol.game.ClientboundAddPaintingPacket;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.network.protocol.game.ClientboundAddExperienceOrbPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.MinecartSoundInstance;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.projectile.LlamaSpit;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.entity.vehicle.MinecartCommandBlock;
import net.minecraft.world.entity.vehicle.MinecartHopper;
import net.minecraft.world.entity.vehicle.MinecartSpawner;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.entity.vehicle.MinecartFurnace;
import net.minecraft.world.entity.vehicle.MinecartChest;
import net.minecraft.world.entity.EntityType;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.level.dimension.DimensionType;
import java.util.ArrayList;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.network.FriendlyByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.stats.StatsCounter;
import net.minecraft.util.profiling.ProfilerFiller;
import java.util.function.Supplier;
import net.minecraft.world.Difficulty;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Collections;
import com.google.common.collect.Lists;
import net.minecraft.tags.StaticTags;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import com.google.common.collect.Maps;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.Set;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.commands.SharedSuggestionProvider;
import com.mojang.brigadier.CommandDispatcher;
import java.util.Random;
import net.minecraft.client.DebugQueryHandler;
import net.minecraft.tags.TagContainer;
import java.util.UUID;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.Logger;
import net.minecraft.network.protocol.game.ClientGamePacketListener;

public class ClientPacketListener implements ClientGamePacketListener {
    private static final Logger LOGGER;
    private static final Component GENERIC_DISCONNECT_MESSAGE;
    private final Connection connection;
    private final GameProfile localGameProfile;
    private final Screen callbackScreen;
    private Minecraft minecraft;
    private ClientLevel level;
    private ClientLevel.ClientLevelData levelData;
    private boolean started;
    private final Map<UUID, PlayerInfo> playerInfoMap;
    private final ClientAdvancements advancements;
    private final ClientSuggestionProvider suggestionsProvider;
    private TagContainer tags;
    private final DebugQueryHandler debugQueryHandler;
    private int serverChunkRadius;
    private final Random random;
    private CommandDispatcher<SharedSuggestionProvider> commands;
    private final RecipeManager recipeManager;
    private final UUID id;
    private Set<ResourceKey<Level>> levels;
    private RegistryAccess registryAccess;
    
    public ClientPacketListener(final Minecraft djw, final Screen doq, final Connection nd, final GameProfile gameProfile) {
        this.playerInfoMap = (Map<UUID, PlayerInfo>)Maps.newHashMap();
        this.tags = TagContainer.EMPTY;
        this.debugQueryHandler = new DebugQueryHandler(this);
        this.serverChunkRadius = 3;
        this.random = new Random();
        this.commands = (CommandDispatcher<SharedSuggestionProvider>)new CommandDispatcher();
        this.recipeManager = new RecipeManager();
        this.id = UUID.randomUUID();
        this.registryAccess = RegistryAccess.builtin();
        this.minecraft = djw;
        this.callbackScreen = doq;
        this.connection = nd;
        this.localGameProfile = gameProfile;
        this.advancements = new ClientAdvancements(djw);
        this.suggestionsProvider = new ClientSuggestionProvider(this, djw);
    }
    
    public ClientSuggestionProvider getSuggestionsProvider() {
        return this.suggestionsProvider;
    }
    
    public void cleanup() {
        this.level = null;
    }
    
    public RecipeManager getRecipeManager() {
        return this.recipeManager;
    }
    
    public void handleLogin(final ClientboundLoginPacket px) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)px, this, this.minecraft);
        this.minecraft.gameMode = new MultiPlayerGameMode(this.minecraft, this);
        if (!this.connection.isMemoryConnection()) {
            StaticTags.resetAllToEmpty();
        }
        final ArrayList<ResourceKey<Level>> arrayList3 = (ArrayList<ResourceKey<Level>>)Lists.newArrayList((Iterable)px.levels());
        Collections.shuffle((List)arrayList3);
        this.levels = (Set<ResourceKey<Level>>)Sets.newLinkedHashSet((Iterable)arrayList3);
        this.registryAccess = px.registryAccess();
        final ResourceKey<Level> vj4 = px.getDimension();
        final DimensionType cha5 = px.getDimensionType();
        this.serverChunkRadius = px.getChunkRadius();
        final boolean boolean6 = px.isDebug();
        final boolean boolean7 = px.isFlat();
        final ClientLevel.ClientLevelData a8 = new ClientLevel.ClientLevelData(Difficulty.NORMAL, px.isHardcore(), boolean7);
        this.levelData = a8;
        this.level = new ClientLevel(this, a8, vj4, cha5, this.serverChunkRadius, (Supplier<ProfilerFiller>)this.minecraft::getProfiler, this.minecraft.levelRenderer, boolean6, px.getSeed());
        this.minecraft.setLevel(this.level);
        if (this.minecraft.player == null) {
            this.minecraft.player = this.minecraft.gameMode.createPlayer(this.level, new StatsCounter(), new ClientRecipeBook());
            this.minecraft.player.yRot = -180.0f;
            if (this.minecraft.getSingleplayerServer() != null) {
                this.minecraft.getSingleplayerServer().setUUID(this.minecraft.player.getUUID());
            }
        }
        this.minecraft.debugRenderer.clear();
        this.minecraft.player.resetPos();
        final int integer9 = px.getPlayerId();
        this.level.addPlayer(integer9, this.minecraft.player);
        this.minecraft.player.input = new KeyboardInput(this.minecraft.options);
        this.minecraft.gameMode.adjustPlayer(this.minecraft.player);
        this.minecraft.cameraEntity = this.minecraft.player;
        this.minecraft.setScreen(new ReceivingLevelScreen());
        this.minecraft.player.setId(integer9);
        this.minecraft.player.setReducedDebugInfo(px.isReducedDebugInfo());
        this.minecraft.player.setShowDeathScreen(px.shouldShowDeathScreen());
        this.minecraft.gameMode.setLocalMode(px.getGameType());
        this.minecraft.gameMode.setPreviousLocalMode(px.getPreviousGameType());
        this.minecraft.options.broadcastOptions();
        this.connection.send(new ServerboundCustomPayloadPacket(ServerboundCustomPayloadPacket.BRAND, new FriendlyByteBuf(Unpooled.buffer()).writeUtf(ClientBrandRetriever.getClientModName())));
        this.minecraft.getGame().onStartGameSession();
    }
    
    public void handleAddEntity(final ClientboundAddEntityPacket on) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)on, this, this.minecraft);
        final double double3 = on.getX();
        final double double4 = on.getY();
        final double double5 = on.getZ();
        final EntityType<?> aqb10 = on.getType();
        Entity apx9;
        if (aqb10 == EntityType.CHEST_MINECART) {
            apx9 = new MinecartChest(this.level, double3, double4, double5);
        }
        else if (aqb10 == EntityType.FURNACE_MINECART) {
            apx9 = new MinecartFurnace(this.level, double3, double4, double5);
        }
        else if (aqb10 == EntityType.TNT_MINECART) {
            apx9 = new MinecartTNT(this.level, double3, double4, double5);
        }
        else if (aqb10 == EntityType.SPAWNER_MINECART) {
            apx9 = new MinecartSpawner(this.level, double3, double4, double5);
        }
        else if (aqb10 == EntityType.HOPPER_MINECART) {
            apx9 = new MinecartHopper(this.level, double3, double4, double5);
        }
        else if (aqb10 == EntityType.COMMAND_BLOCK_MINECART) {
            apx9 = new MinecartCommandBlock(this.level, double3, double4, double5);
        }
        else if (aqb10 == EntityType.MINECART) {
            apx9 = new Minecart(this.level, double3, double4, double5);
        }
        else if (aqb10 == EntityType.FISHING_BOBBER) {
            final Entity apx10 = this.level.getEntity(on.getData());
            if (apx10 instanceof Player) {
                apx9 = new FishingHook(this.level, (Player)apx10, double3, double4, double5);
            }
            else {
                apx9 = null;
            }
        }
        else if (aqb10 == EntityType.ARROW) {
            apx9 = new Arrow(this.level, double3, double4, double5);
            final Entity apx10 = this.level.getEntity(on.getData());
            if (apx10 != null) {
                ((AbstractArrow)apx9).setOwner(apx10);
            }
        }
        else if (aqb10 == EntityType.SPECTRAL_ARROW) {
            apx9 = new SpectralArrow(this.level, double3, double4, double5);
            final Entity apx10 = this.level.getEntity(on.getData());
            if (apx10 != null) {
                ((AbstractArrow)apx9).setOwner(apx10);
            }
        }
        else if (aqb10 == EntityType.TRIDENT) {
            apx9 = new ThrownTrident(this.level, double3, double4, double5);
            final Entity apx10 = this.level.getEntity(on.getData());
            if (apx10 != null) {
                ((AbstractArrow)apx9).setOwner(apx10);
            }
        }
        else if (aqb10 == EntityType.SNOWBALL) {
            apx9 = new Snowball(this.level, double3, double4, double5);
        }
        else if (aqb10 == EntityType.LLAMA_SPIT) {
            apx9 = new LlamaSpit(this.level, double3, double4, double5, on.getXa(), on.getYa(), on.getZa());
        }
        else if (aqb10 == EntityType.ITEM_FRAME) {
            apx9 = new ItemFrame(this.level, new BlockPos(double3, double4, double5), Direction.from3DDataValue(on.getData()));
        }
        else if (aqb10 == EntityType.LEASH_KNOT) {
            apx9 = new LeashFenceKnotEntity(this.level, new BlockPos(double3, double4, double5));
        }
        else if (aqb10 == EntityType.ENDER_PEARL) {
            apx9 = new ThrownEnderpearl(this.level, double3, double4, double5);
        }
        else if (aqb10 == EntityType.EYE_OF_ENDER) {
            apx9 = new EyeOfEnder(this.level, double3, double4, double5);
        }
        else if (aqb10 == EntityType.FIREWORK_ROCKET) {
            apx9 = new FireworkRocketEntity(this.level, double3, double4, double5, ItemStack.EMPTY);
        }
        else if (aqb10 == EntityType.FIREBALL) {
            apx9 = new LargeFireball(this.level, double3, double4, double5, on.getXa(), on.getYa(), on.getZa());
        }
        else if (aqb10 == EntityType.DRAGON_FIREBALL) {
            apx9 = new DragonFireball(this.level, double3, double4, double5, on.getXa(), on.getYa(), on.getZa());
        }
        else if (aqb10 == EntityType.SMALL_FIREBALL) {
            apx9 = new SmallFireball(this.level, double3, double4, double5, on.getXa(), on.getYa(), on.getZa());
        }
        else if (aqb10 == EntityType.WITHER_SKULL) {
            apx9 = new WitherSkull(this.level, double3, double4, double5, on.getXa(), on.getYa(), on.getZa());
        }
        else if (aqb10 == EntityType.SHULKER_BULLET) {
            apx9 = new ShulkerBullet(this.level, double3, double4, double5, on.getXa(), on.getYa(), on.getZa());
        }
        else if (aqb10 == EntityType.EGG) {
            apx9 = new ThrownEgg(this.level, double3, double4, double5);
        }
        else if (aqb10 == EntityType.EVOKER_FANGS) {
            apx9 = new EvokerFangs(this.level, double3, double4, double5, 0.0f, 0, null);
        }
        else if (aqb10 == EntityType.POTION) {
            apx9 = new ThrownPotion(this.level, double3, double4, double5);
        }
        else if (aqb10 == EntityType.EXPERIENCE_BOTTLE) {
            apx9 = new ThrownExperienceBottle(this.level, double3, double4, double5);
        }
        else if (aqb10 == EntityType.BOAT) {
            apx9 = new Boat(this.level, double3, double4, double5);
        }
        else if (aqb10 == EntityType.TNT) {
            apx9 = new PrimedTnt(this.level, double3, double4, double5, null);
        }
        else if (aqb10 == EntityType.ARMOR_STAND) {
            apx9 = new ArmorStand(this.level, double3, double4, double5);
        }
        else if (aqb10 == EntityType.END_CRYSTAL) {
            apx9 = new EndCrystal(this.level, double3, double4, double5);
        }
        else if (aqb10 == EntityType.ITEM) {
            apx9 = new ItemEntity(this.level, double3, double4, double5);
        }
        else if (aqb10 == EntityType.FALLING_BLOCK) {
            apx9 = new FallingBlockEntity(this.level, double3, double4, double5, Block.stateById(on.getData()));
        }
        else if (aqb10 == EntityType.AREA_EFFECT_CLOUD) {
            apx9 = new AreaEffectCloud(this.level, double3, double4, double5);
        }
        else if (aqb10 == EntityType.LIGHTNING_BOLT) {
            apx9 = new LightningBolt(EntityType.LIGHTNING_BOLT, this.level);
        }
        else {
            apx9 = null;
        }
        if (apx9 != null) {
            final int integer11 = on.getId();
            apx9.setPacketCoordinates(double3, double4, double5);
            apx9.moveTo(double3, double4, double5);
            apx9.xRot = on.getxRot() * 360 / 256.0f;
            apx9.yRot = on.getyRot() * 360 / 256.0f;
            apx9.setId(integer11);
            apx9.setUUID(on.getUUID());
            this.level.putNonPlayerEntity(integer11, apx9);
            if (apx9 instanceof AbstractMinecart) {
                this.minecraft.getSoundManager().play(new MinecartSoundInstance((AbstractMinecart)apx9));
            }
        }
    }
    
    public void handleAddExperienceOrb(final ClientboundAddExperienceOrbPacket oo) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)oo, this, this.minecraft);
        final double double3 = oo.getX();
        final double double4 = oo.getY();
        final double double5 = oo.getZ();
        final Entity apx9 = new ExperienceOrb(this.level, double3, double4, double5, oo.getValue());
        apx9.setPacketCoordinates(double3, double4, double5);
        apx9.yRot = 0.0f;
        apx9.xRot = 0.0f;
        apx9.setId(oo.getId());
        this.level.putNonPlayerEntity(oo.getId(), apx9);
    }
    
    public void handleAddPainting(final ClientboundAddPaintingPacket oq) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)oq, this, this.minecraft);
        final Painting bcp3 = new Painting(this.level, oq.getPos(), oq.getDirection(), oq.getMotive());
        bcp3.setId(oq.getId());
        bcp3.setUUID(oq.getUUID());
        this.level.putNonPlayerEntity(oq.getId(), bcp3);
    }
    
    public void handleSetEntityMotion(final ClientboundSetEntityMotionPacket rc) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)rc, this, this.minecraft);
        final Entity apx3 = this.level.getEntity(rc.getId());
        if (apx3 == null) {
            return;
        }
        apx3.lerpMotion(rc.getXa() / 8000.0, rc.getYa() / 8000.0, rc.getZa() / 8000.0);
    }
    
    public void handleSetEntityData(final ClientboundSetEntityDataPacket ra) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)ra, this, this.minecraft);
        final Entity apx3 = this.level.getEntity(ra.getId());
        if (apx3 != null && ra.getUnpackedData() != null) {
            apx3.getEntityData().assignValues(ra.getUnpackedData());
        }
    }
    
    public void handleAddPlayer(final ClientboundAddPlayerPacket or) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)or, this, this.minecraft);
        final double double3 = or.getX();
        final double double4 = or.getY();
        final double double5 = or.getZ();
        final float float9 = or.getyRot() * 360 / 256.0f;
        final float float10 = or.getxRot() * 360 / 256.0f;
        final int integer11 = or.getEntityId();
        final RemotePlayer dzf12 = new RemotePlayer(this.minecraft.level, this.getPlayerInfo(or.getPlayerId()).getProfile());
        dzf12.setId(integer11);
        dzf12.setPosAndOldPos(double3, double4, double5);
        dzf12.setPacketCoordinates(double3, double4, double5);
        dzf12.absMoveTo(double3, double4, double5, float9, float10);
        this.level.addPlayer(integer11, dzf12);
    }
    
    public void handleTeleportEntity(final ClientboundTeleportEntityPacket rs) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)rs, this, this.minecraft);
        final Entity apx3 = this.level.getEntity(rs.getId());
        if (apx3 == null) {
            return;
        }
        final double double4 = rs.getX();
        final double double5 = rs.getY();
        final double double6 = rs.getZ();
        apx3.setPacketCoordinates(double4, double5, double6);
        if (!apx3.isControlledByLocalInstance()) {
            final float float10 = rs.getyRot() * 360 / 256.0f;
            final float float11 = rs.getxRot() * 360 / 256.0f;
            apx3.lerpTo(double4, double5, double6, float10, float11, 3, true);
            apx3.setOnGround(rs.isOnGround());
        }
    }
    
    public void handleSetCarriedItem(final ClientboundSetCarriedItemPacket qv) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)qv, this, this.minecraft);
        if (Inventory.isHotbarSlot(qv.getSlot())) {
            this.minecraft.player.inventory.selected = qv.getSlot();
        }
    }
    
    public void handleMoveEntity(final ClientboundMoveEntityPacket qa) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)qa, this, this.minecraft);
        final Entity apx3 = qa.getEntity(this.level);
        if (apx3 == null) {
            return;
        }
        if (!apx3.isControlledByLocalInstance()) {
            if (qa.hasPosition()) {
                final Vec3 dck4 = qa.updateEntityPosition(apx3.getPacketCoordinates());
                apx3.setPacketCoordinates(dck4);
                final float float5 = qa.hasRotation() ? (qa.getyRot() * 360 / 256.0f) : apx3.yRot;
                final float float6 = qa.hasRotation() ? (qa.getxRot() * 360 / 256.0f) : apx3.xRot;
                apx3.lerpTo(dck4.x(), dck4.y(), dck4.z(), float5, float6, 3, false);
            }
            else if (qa.hasRotation()) {
                final float float7 = qa.getyRot() * 360 / 256.0f;
                final float float5 = qa.getxRot() * 360 / 256.0f;
                apx3.lerpTo(apx3.getX(), apx3.getY(), apx3.getZ(), float7, float5, 3, false);
            }
            apx3.setOnGround(qa.isOnGround());
        }
    }
    
    public void handleRotateMob(final ClientboundRotateHeadPacket qq) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)qq, this, this.minecraft);
        final Entity apx3 = qq.getEntity(this.level);
        if (apx3 == null) {
            return;
        }
        final float float4 = qq.getYHeadRot() * 360 / 256.0f;
        apx3.lerpHeadTo(float4, 3);
    }
    
    public void handleRemoveEntity(final ClientboundRemoveEntitiesPacket qm) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)qm, this, this.minecraft);
        for (int integer3 = 0; integer3 < qm.getEntityIds().length; ++integer3) {
            final int integer4 = qm.getEntityIds()[integer3];
            this.level.removeEntity(integer4);
        }
    }
    
    public void handleMovePlayer(final ClientboundPlayerPositionPacket qk) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)qk, this, this.minecraft);
        final Player bft3 = this.minecraft.player;
        final Vec3 dck4 = bft3.getDeltaMovement();
        final boolean boolean5 = qk.getRelativeArguments().contains(ClientboundPlayerPositionPacket.RelativeArgument.X);
        final boolean boolean6 = qk.getRelativeArguments().contains(ClientboundPlayerPositionPacket.RelativeArgument.Y);
        final boolean boolean7 = qk.getRelativeArguments().contains(ClientboundPlayerPositionPacket.RelativeArgument.Z);
        double double8;
        double double9;
        if (boolean5) {
            double8 = dck4.x();
            double9 = bft3.getX() + qk.getX();
            bft3.xOld += qk.getX();
        }
        else {
            double8 = 0.0;
            double9 = qk.getX();
            bft3.xOld = double9;
        }
        double double10;
        double double11;
        if (boolean6) {
            double10 = dck4.y();
            double11 = bft3.getY() + qk.getY();
            bft3.yOld += qk.getY();
        }
        else {
            double10 = 0.0;
            double11 = qk.getY();
            bft3.yOld = double11;
        }
        double double12;
        double double13;
        if (boolean7) {
            double12 = dck4.z();
            double13 = bft3.getZ() + qk.getZ();
            bft3.zOld += qk.getZ();
        }
        else {
            double12 = 0.0;
            double13 = qk.getZ();
            bft3.zOld = double13;
        }
        if (bft3.tickCount > 0 && bft3.getVehicle() != null) {
            bft3.removeVehicle();
        }
        bft3.setPosRaw(double9, double11, double13);
        bft3.xo = double9;
        bft3.yo = double11;
        bft3.zo = double13;
        bft3.setDeltaMovement(double8, double10, double12);
        float float20 = qk.getYRot();
        float float21 = qk.getXRot();
        if (qk.getRelativeArguments().contains(ClientboundPlayerPositionPacket.RelativeArgument.X_ROT)) {
            float21 += bft3.xRot;
        }
        if (qk.getRelativeArguments().contains(ClientboundPlayerPositionPacket.RelativeArgument.Y_ROT)) {
            float20 += bft3.yRot;
        }
        bft3.absMoveTo(double9, double11, double13, float20, float21);
        this.connection.send(new ServerboundAcceptTeleportationPacket(qk.getId()));
        this.connection.send(new ServerboundMovePlayerPacket.PosRot(bft3.getX(), bft3.getY(), bft3.getZ(), bft3.yRot, bft3.xRot, false));
        if (!this.started) {
            this.started = true;
            this.minecraft.setScreen(null);
        }
    }
    
    public void handleChunkBlocksUpdate(final ClientboundSectionBlocksUpdatePacket qr) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)qr, this, this.minecraft);
        final int integer3 = 0x13 | (qr.shouldSuppressLightUpdates() ? 128 : 0);
        qr.runUpdates((BiConsumer<BlockPos, BlockState>)((fx, cee) -> this.level.setBlock(fx, cee, integer3)));
    }
    
    public void handleLevelChunk(final ClientboundLevelChunkPacket pt) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)pt, this, this.minecraft);
        final int integer3 = pt.getX();
        final int integer4 = pt.getZ();
        final ChunkBiomeContainer cfu5 = (pt.getBiomes() == null) ? null : new ChunkBiomeContainer(this.registryAccess.registryOrThrow(Registry.BIOME_REGISTRY), pt.getBiomes());
        final LevelChunk cge6 = this.level.getChunkSource().replaceWithPacketData(integer3, integer4, cfu5, pt.getReadBuffer(), pt.getHeightmaps(), pt.getAvailableSections(), pt.isFullChunk());
        if (cge6 != null && pt.isFullChunk()) {
            this.level.reAddEntitiesToChunk(cge6);
        }
        for (int integer5 = 0; integer5 < 16; ++integer5) {
            this.level.setSectionDirtyWithNeighbors(integer3, integer5, integer4);
        }
        for (final CompoundTag md8 : pt.getBlockEntitiesTags()) {
            final BlockPos fx9 = new BlockPos(md8.getInt("x"), md8.getInt("y"), md8.getInt("z"));
            final BlockEntity ccg10 = this.level.getBlockEntity(fx9);
            if (ccg10 != null) {
                ccg10.load(this.level.getBlockState(fx9), md8);
            }
        }
    }
    
    public void handleForgetLevelChunk(final ClientboundForgetLevelChunkPacket pp) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)pp, this, this.minecraft);
        final int integer3 = pp.getX();
        final int integer4 = pp.getZ();
        final ClientChunkCache dwj5 = this.level.getChunkSource();
        dwj5.drop(integer3, integer4);
        final LevelLightEngine cul6 = dwj5.getLightEngine();
        for (int integer5 = 0; integer5 < 16; ++integer5) {
            this.level.setSectionDirtyWithNeighbors(integer3, integer5, integer4);
            cul6.updateSectionStatus(SectionPos.of(integer3, integer5, integer4), true);
        }
        cul6.enableLightSources(new ChunkPos(integer3, integer4), false);
    }
    
    public void handleBlockUpdate(final ClientboundBlockUpdatePacket oy) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)oy, this, this.minecraft);
        this.level.setKnownState(oy.getPos(), oy.getBlockState());
    }
    
    public void handleDisconnect(final ClientboundDisconnectPacket pm) {
        this.connection.disconnect(pm.getReason());
    }
    
    public void onDisconnect(final Component nr) {
        this.minecraft.clearLevel();
        if (this.callbackScreen != null) {
            if (this.callbackScreen instanceof RealmsScreen) {
                this.minecraft.setScreen(new DisconnectedRealmsScreen(this.callbackScreen, ClientPacketListener.GENERIC_DISCONNECT_MESSAGE, nr));
            }
            else {
                this.minecraft.setScreen(new DisconnectedScreen(this.callbackScreen, ClientPacketListener.GENERIC_DISCONNECT_MESSAGE, nr));
            }
        }
        else {
            this.minecraft.setScreen(new DisconnectedScreen(new JoinMultiplayerScreen(new TitleScreen()), ClientPacketListener.GENERIC_DISCONNECT_MESSAGE, nr));
        }
    }
    
    public void send(final Packet<?> oj) {
        this.connection.send(oj);
    }
    
    public void handleTakeItemEntity(final ClientboundTakeItemEntityPacket rr) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)rr, this, this.minecraft);
        final Entity apx3 = this.level.getEntity(rr.getItemId());
        LivingEntity aqj4 = (LivingEntity)this.level.getEntity(rr.getPlayerId());
        if (aqj4 == null) {
            aqj4 = this.minecraft.player;
        }
        if (apx3 != null) {
            if (apx3 instanceof ExperienceOrb) {
                this.level.playLocalSound(apx3.getX(), apx3.getY(), apx3.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.1f, (this.random.nextFloat() - this.random.nextFloat()) * 0.35f + 0.9f, false);
            }
            else {
                this.level.playLocalSound(apx3.getX(), apx3.getY(), apx3.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2f, (this.random.nextFloat() - this.random.nextFloat()) * 1.4f + 2.0f, false);
            }
            this.minecraft.particleEngine.add(new ItemPickupParticle(this.minecraft.getEntityRenderDispatcher(), this.minecraft.renderBuffers(), this.level, apx3, aqj4));
            if (apx3 instanceof ItemEntity) {
                final ItemEntity bcs5 = (ItemEntity)apx3;
                final ItemStack bly6 = bcs5.getItem();
                bly6.shrink(rr.getAmount());
                if (bly6.isEmpty()) {
                    this.level.removeEntity(rr.getItemId());
                }
            }
            else {
                this.level.removeEntity(rr.getItemId());
            }
        }
    }
    
    public void handleChat(final ClientboundChatPacket pb) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)pb, this, this.minecraft);
        this.minecraft.gui.handleChat(pb.getType(), pb.getMessage(), pb.getSender());
    }
    
    public void handleAnimate(final ClientboundAnimatePacket os) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)os, this, this.minecraft);
        final Entity apx3 = this.level.getEntity(os.getId());
        if (apx3 == null) {
            return;
        }
        if (os.getAction() == 0) {
            final LivingEntity aqj4 = (LivingEntity)apx3;
            aqj4.swing(InteractionHand.MAIN_HAND);
        }
        else if (os.getAction() == 3) {
            final LivingEntity aqj4 = (LivingEntity)apx3;
            aqj4.swing(InteractionHand.OFF_HAND);
        }
        else if (os.getAction() == 1) {
            apx3.animateHurt();
        }
        else if (os.getAction() == 2) {
            final Player bft4 = (Player)apx3;
            bft4.stopSleepInBed(false, false);
        }
        else if (os.getAction() == 4) {
            this.minecraft.particleEngine.createTrackingEmitter(apx3, ParticleTypes.CRIT);
        }
        else if (os.getAction() == 5) {
            this.minecraft.particleEngine.createTrackingEmitter(apx3, ParticleTypes.ENCHANTED_HIT);
        }
    }
    
    public void handleAddMob(final ClientboundAddMobPacket op) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)op, this, this.minecraft);
        final double double3 = op.getX();
        final double double4 = op.getY();
        final double double5 = op.getZ();
        final float float9 = op.getyRot() * 360 / 256.0f;
        final float float10 = op.getxRot() * 360 / 256.0f;
        final LivingEntity aqj11 = (LivingEntity)EntityType.create(op.getType(), this.minecraft.level);
        if (aqj11 != null) {
            aqj11.setPacketCoordinates(double3, double4, double5);
            aqj11.yBodyRot = op.getyHeadRot() * 360 / 256.0f;
            aqj11.yHeadRot = op.getyHeadRot() * 360 / 256.0f;
            if (aqj11 instanceof EnderDragon) {
                final EnderDragonPart[] arr12 = ((EnderDragon)aqj11).getSubEntities();
                for (int integer13 = 0; integer13 < arr12.length; ++integer13) {
                    arr12[integer13].setId(integer13 + op.getId());
                }
            }
            aqj11.setId(op.getId());
            aqj11.setUUID(op.getUUID());
            aqj11.absMoveTo(double3, double4, double5, float9, float10);
            aqj11.setDeltaMovement(op.getXd() / 8000.0f, op.getYd() / 8000.0f, op.getZd() / 8000.0f);
            this.level.putNonPlayerEntity(op.getId(), aqj11);
            if (aqj11 instanceof Bee) {
                final boolean boolean12 = ((Bee)aqj11).isAngry();
                BeeSoundInstance elz13;
                if (boolean12) {
                    elz13 = new BeeAggressiveSoundInstance((Bee)aqj11);
                }
                else {
                    elz13 = new BeeFlyingSoundInstance((Bee)aqj11);
                }
                this.minecraft.getSoundManager().queueTickingSound(elz13);
            }
        }
        else {
            ClientPacketListener.LOGGER.warn("Skipping Entity with id {}", op.getType());
        }
    }
    
    public void handleSetTime(final ClientboundSetTimePacket rk) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)rk, this, this.minecraft);
        this.minecraft.level.setGameTime(rk.getGameTime());
        this.minecraft.level.setDayTime(rk.getDayTime());
    }
    
    public void handleSetSpawn(final ClientboundSetDefaultSpawnPositionPacket qy) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)qy, this, this.minecraft);
        this.minecraft.level.setDefaultSpawnPos(qy.getPos(), qy.getAngle());
    }
    
    public void handleSetEntityPassengersPacket(final ClientboundSetPassengersPacket rh) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)rh, this, this.minecraft);
        final Entity apx3 = this.level.getEntity(rh.getVehicle());
        if (apx3 == null) {
            ClientPacketListener.LOGGER.warn("Received passengers for unknown entity");
            return;
        }
        final boolean boolean4 = apx3.hasIndirectPassenger(this.minecraft.player);
        apx3.ejectPassengers();
        for (final int integer8 : rh.getPassengers()) {
            final Entity apx4 = this.level.getEntity(integer8);
            if (apx4 != null) {
                apx4.startRiding(apx3, true);
                if (apx4 == this.minecraft.player && !boolean4) {
                    this.minecraft.gui.setOverlayMessage(new TranslatableComponent("mount.onboard", new Object[] { this.minecraft.options.keyShift.getTranslatedKeyMessage() }), false);
                }
            }
        }
    }
    
    public void handleEntityLinkPacket(final ClientboundSetEntityLinkPacket rb) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)rb, this, this.minecraft);
        final Entity apx3 = this.level.getEntity(rb.getSourceId());
        if (apx3 instanceof Mob) {
            ((Mob)apx3).setDelayedLeashHolderId(rb.getDestId());
        }
    }
    
    private static ItemStack findTotem(final Player bft) {
        for (final InteractionHand aoq5 : InteractionHand.values()) {
            final ItemStack bly6 = bft.getItemInHand(aoq5);
            if (bly6.getItem() == Items.TOTEM_OF_UNDYING) {
                return bly6;
            }
        }
        return new ItemStack(Items.TOTEM_OF_UNDYING);
    }
    
    public void handleEntityEvent(final ClientboundEntityEventPacket pn) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)pn, this, this.minecraft);
        final Entity apx3 = pn.getEntity(this.level);
        if (apx3 != null) {
            if (pn.getEventId() == 21) {
                this.minecraft.getSoundManager().play(new GuardianAttackSoundInstance((Guardian)apx3));
            }
            else if (pn.getEventId() == 35) {
                final int integer4 = 40;
                this.minecraft.particleEngine.createTrackingEmitter(apx3, ParticleTypes.TOTEM_OF_UNDYING, 30);
                this.level.playLocalSound(apx3.getX(), apx3.getY(), apx3.getZ(), SoundEvents.TOTEM_USE, apx3.getSoundSource(), 1.0f, 1.0f, false);
                if (apx3 == this.minecraft.player) {
                    this.minecraft.gameRenderer.displayItemActivation(findTotem(this.minecraft.player));
                }
            }
            else {
                apx3.handleEntityEvent(pn.getEventId());
            }
        }
    }
    
    public void handleSetHealth(final ClientboundSetHealthPacket rf) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)rf, this, this.minecraft);
        this.minecraft.player.hurtTo(rf.getHealth());
        this.minecraft.player.getFoodData().setFoodLevel(rf.getFood());
        this.minecraft.player.getFoodData().setSaturation(rf.getSaturation());
    }
    
    public void handleSetExperience(final ClientboundSetExperiencePacket re) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)re, this, this.minecraft);
        this.minecraft.player.setExperienceValues(re.getExperienceProgress(), re.getTotalExperience(), re.getExperienceLevel());
    }
    
    public void handleRespawn(final ClientboundRespawnPacket qp) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)qp, this, this.minecraft);
        final ResourceKey<Level> vj3 = qp.getDimension();
        final DimensionType cha4 = qp.getDimensionType();
        final LocalPlayer dze5 = this.minecraft.player;
        final int integer6 = dze5.getId();
        this.started = false;
        if (vj3 != dze5.level.dimension()) {
            final Scoreboard ddk7 = this.level.getScoreboard();
            final boolean boolean8 = qp.isDebug();
            final boolean boolean9 = qp.isFlat();
            final ClientLevel.ClientLevelData a10 = new ClientLevel.ClientLevelData(this.levelData.getDifficulty(), this.levelData.isHardcore(), boolean9);
            this.levelData = a10;
            (this.level = new ClientLevel(this, a10, vj3, cha4, this.serverChunkRadius, (Supplier<ProfilerFiller>)this.minecraft::getProfiler, this.minecraft.levelRenderer, boolean8, qp.getSeed())).setScoreboard(ddk7);
            this.minecraft.setLevel(this.level);
            this.minecraft.setScreen(new ReceivingLevelScreen());
        }
        this.level.removeAllPendingEntityRemovals();
        final String string7 = dze5.getServerBrand();
        this.minecraft.cameraEntity = null;
        final LocalPlayer dze6 = this.minecraft.gameMode.createPlayer(this.level, dze5.getStats(), dze5.getRecipeBook(), dze5.isShiftKeyDown(), dze5.isSprinting());
        dze6.setId(integer6);
        this.minecraft.player = dze6;
        if (vj3 != dze5.level.dimension()) {
            this.minecraft.getMusicManager().stopPlaying();
        }
        this.minecraft.cameraEntity = dze6;
        dze6.getEntityData().assignValues(dze5.getEntityData().getAll());
        if (qp.shouldKeepAllPlayerData()) {
            dze6.getAttributes().assignValues(dze5.getAttributes());
        }
        dze6.resetPos();
        dze6.setServerBrand(string7);
        this.level.addPlayer(integer6, dze6);
        dze6.yRot = -180.0f;
        dze6.input = new KeyboardInput(this.minecraft.options);
        this.minecraft.gameMode.adjustPlayer(dze6);
        dze6.setReducedDebugInfo(dze5.isReducedDebugInfo());
        dze6.setShowDeathScreen(dze5.shouldShowDeathScreen());
        if (this.minecraft.screen instanceof DeathScreen) {
            this.minecraft.setScreen(null);
        }
        this.minecraft.gameMode.setLocalMode(qp.getPlayerGameType());
        this.minecraft.gameMode.setPreviousLocalMode(qp.getPreviousPlayerGameType());
    }
    
    public void handleExplosion(final ClientboundExplodePacket po) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)po, this, this.minecraft);
        final Explosion brm3 = new Explosion(this.minecraft.level, null, po.getX(), po.getY(), po.getZ(), po.getPower(), po.getToBlow());
        brm3.finalizeExplosion(true);
        this.minecraft.player.setDeltaMovement(this.minecraft.player.getDeltaMovement().add(po.getKnockbackX(), po.getKnockbackY(), po.getKnockbackZ()));
    }
    
    public void handleHorseScreenOpen(final ClientboundHorseScreenOpenPacket pr) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)pr, this, this.minecraft);
        final Entity apx3 = this.level.getEntity(pr.getEntityId());
        if (apx3 instanceof AbstractHorse) {
            final LocalPlayer dze4 = this.minecraft.player;
            final AbstractHorse bay5 = (AbstractHorse)apx3;
            final SimpleContainer aox6 = new SimpleContainer(pr.getSize());
            final HorseInventoryMenu biv7 = new HorseInventoryMenu(pr.getContainerId(), dze4.inventory, aox6, bay5);
            dze4.containerMenu = biv7;
            this.minecraft.setScreen(new HorseInventoryScreen(biv7, dze4.inventory, bay5));
        }
    }
    
    public void handleOpenScreen(final ClientboundOpenScreenPacket qd) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)qd, this, this.minecraft);
        MenuScreens.create(qd.getType(), this.minecraft, qd.getContainerId(), qd.getTitle());
    }
    
    public void handleContainerSetSlot(final ClientboundContainerSetSlotPacket pi) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)pi, this, this.minecraft);
        final Player bft3 = this.minecraft.player;
        final ItemStack bly4 = pi.getItem();
        final int integer5 = pi.getSlot();
        this.minecraft.getTutorial().onGetItem(bly4);
        if (pi.getContainerId() == -1) {
            if (!(this.minecraft.screen instanceof CreativeModeInventoryScreen)) {
                bft3.inventory.setCarried(bly4);
            }
        }
        else if (pi.getContainerId() == -2) {
            bft3.inventory.setItem(integer5, bly4);
        }
        else {
            boolean boolean6 = false;
            if (this.minecraft.screen instanceof CreativeModeInventoryScreen) {
                final CreativeModeInventoryScreen dpz7 = (CreativeModeInventoryScreen)this.minecraft.screen;
                boolean6 = (dpz7.getSelectedTab() != CreativeModeTab.TAB_INVENTORY.getId());
            }
            if (pi.getContainerId() == 0 && pi.getSlot() >= 36 && integer5 < 45) {
                if (!bly4.isEmpty()) {
                    final ItemStack bly5 = bft3.inventoryMenu.getSlot(integer5).getItem();
                    if (bly5.isEmpty() || bly5.getCount() < bly4.getCount()) {
                        bly4.setPopTime(5);
                    }
                }
                bft3.inventoryMenu.setItem(integer5, bly4);
            }
            else if (pi.getContainerId() == bft3.containerMenu.containerId && (pi.getContainerId() != 0 || !boolean6)) {
                bft3.containerMenu.setItem(integer5, bly4);
            }
        }
    }
    
    public void handleContainerAck(final ClientboundContainerAckPacket pe) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)pe, this, this.minecraft);
        AbstractContainerMenu bhz3 = null;
        final Player bft4 = this.minecraft.player;
        if (pe.getContainerId() == 0) {
            bhz3 = bft4.inventoryMenu;
        }
        else if (pe.getContainerId() == bft4.containerMenu.containerId) {
            bhz3 = bft4.containerMenu;
        }
        if (bhz3 != null && !pe.isAccepted()) {
            this.send(new ServerboundContainerAckPacket(pe.getContainerId(), pe.getUid(), true));
        }
    }
    
    public void handleContainerContent(final ClientboundContainerSetContentPacket pg) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)pg, this, this.minecraft);
        final Player bft3 = this.minecraft.player;
        if (pg.getContainerId() == 0) {
            bft3.inventoryMenu.setAll(pg.getItems());
        }
        else if (pg.getContainerId() == bft3.containerMenu.containerId) {
            bft3.containerMenu.setAll(pg.getItems());
        }
    }
    
    public void handleOpenSignEditor(final ClientboundOpenSignEditorPacket qe) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)qe, this, this.minecraft);
        BlockEntity ccg3 = this.level.getBlockEntity(qe.getPos());
        if (!(ccg3 instanceof SignBlockEntity)) {
            ccg3 = new SignBlockEntity();
            ccg3.setLevelAndPosition(this.level, qe.getPos());
        }
        this.minecraft.player.openTextEdit((SignBlockEntity)ccg3);
    }
    
    public void handleBlockEntityData(final ClientboundBlockEntityDataPacket ow) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)ow, this, this.minecraft);
        final BlockPos fx3 = ow.getPos();
        final BlockEntity ccg4 = this.minecraft.level.getBlockEntity(fx3);
        final int integer5 = ow.getType();
        final boolean boolean6 = integer5 == 2 && ccg4 instanceof CommandBlockEntity;
        if ((integer5 == 1 && ccg4 instanceof SpawnerBlockEntity) || boolean6 || (integer5 == 3 && ccg4 instanceof BeaconBlockEntity) || (integer5 == 4 && ccg4 instanceof SkullBlockEntity) || (integer5 == 6 && ccg4 instanceof BannerBlockEntity) || (integer5 == 7 && ccg4 instanceof StructureBlockEntity) || (integer5 == 8 && ccg4 instanceof TheEndGatewayBlockEntity) || (integer5 == 9 && ccg4 instanceof SignBlockEntity) || (integer5 == 11 && ccg4 instanceof BedBlockEntity) || (integer5 == 5 && ccg4 instanceof ConduitBlockEntity) || (integer5 == 12 && ccg4 instanceof JigsawBlockEntity) || (integer5 == 13 && ccg4 instanceof CampfireBlockEntity) || (integer5 == 14 && ccg4 instanceof BeehiveBlockEntity)) {
            ccg4.load(this.minecraft.level.getBlockState(fx3), ow.getTag());
        }
        if (boolean6 && this.minecraft.screen instanceof CommandBlockEditScreen) {
            ((CommandBlockEditScreen)this.minecraft.screen).updateGui();
        }
    }
    
    public void handleContainerSetData(final ClientboundContainerSetDataPacket ph) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)ph, this, this.minecraft);
        final Player bft3 = this.minecraft.player;
        if (bft3.containerMenu != null && bft3.containerMenu.containerId == ph.getContainerId()) {
            bft3.containerMenu.setData(ph.getId(), ph.getValue());
        }
    }
    
    public void handleSetEquipment(final ClientboundSetEquipmentPacket rd) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)rd, this, this.minecraft);
        final Entity apx3 = this.level.getEntity(rd.getEntity());
        if (apx3 != null) {
            rd.getSlots().forEach(pair -> apx3.setItemSlot((EquipmentSlot)pair.getFirst(), (ItemStack)pair.getSecond()));
        }
    }
    
    public void handleContainerClose(final ClientboundContainerClosePacket pf) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)pf, this, this.minecraft);
        this.minecraft.player.clientSideCloseContainer();
    }
    
    public void handleBlockEvent(final ClientboundBlockEventPacket ox) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)ox, this, this.minecraft);
        this.minecraft.level.blockEvent(ox.getPos(), ox.getBlock(), ox.getB0(), ox.getB1());
    }
    
    public void handleBlockDestruction(final ClientboundBlockDestructionPacket ov) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)ov, this, this.minecraft);
        this.minecraft.level.destroyBlockProgress(ov.getId(), ov.getPos(), ov.getProgress());
    }
    
    public void handleGameEvent(final ClientboundGameEventPacket pq) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)pq, this, this.minecraft);
        final Player bft3 = this.minecraft.player;
        final ClientboundGameEventPacket.Type a4 = pq.getEvent();
        final float float5 = pq.getParam();
        final int integer6 = Mth.floor(float5 + 0.5f);
        if (a4 == ClientboundGameEventPacket.NO_RESPAWN_BLOCK_AVAILABLE) {
            bft3.displayClientMessage(new TranslatableComponent("block.minecraft.spawn.not_valid"), false);
        }
        else if (a4 == ClientboundGameEventPacket.START_RAINING) {
            this.level.getLevelData().setRaining(true);
            this.level.setRainLevel(0.0f);
        }
        else if (a4 == ClientboundGameEventPacket.STOP_RAINING) {
            this.level.getLevelData().setRaining(false);
            this.level.setRainLevel(1.0f);
        }
        else if (a4 == ClientboundGameEventPacket.CHANGE_GAME_MODE) {
            this.minecraft.gameMode.setLocalMode(GameType.byId(integer6));
        }
        else if (a4 == ClientboundGameEventPacket.WIN_GAME) {
            if (integer6 == 0) {
                this.minecraft.player.connection.send(new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.PERFORM_RESPAWN));
                this.minecraft.setScreen(new ReceivingLevelScreen());
            }
            else if (integer6 == 1) {
                this.minecraft.setScreen(new WinScreen(true, () -> this.minecraft.player.connection.send(new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.PERFORM_RESPAWN))));
            }
        }
        else if (a4 == ClientboundGameEventPacket.DEMO_EVENT) {
            final Options dka7 = this.minecraft.options;
            if (float5 == 0.0f) {
                this.minecraft.setScreen(new DemoIntroScreen());
            }
            else if (float5 == 101.0f) {
                this.minecraft.gui.getChat().addMessage(new TranslatableComponent("demo.help.movement", new Object[] { dka7.keyUp.getTranslatedKeyMessage(), dka7.keyLeft.getTranslatedKeyMessage(), dka7.keyDown.getTranslatedKeyMessage(), dka7.keyRight.getTranslatedKeyMessage() }));
            }
            else if (float5 == 102.0f) {
                this.minecraft.gui.getChat().addMessage(new TranslatableComponent("demo.help.jump", new Object[] { dka7.keyJump.getTranslatedKeyMessage() }));
            }
            else if (float5 == 103.0f) {
                this.minecraft.gui.getChat().addMessage(new TranslatableComponent("demo.help.inventory", new Object[] { dka7.keyInventory.getTranslatedKeyMessage() }));
            }
            else if (float5 == 104.0f) {
                this.minecraft.gui.getChat().addMessage(new TranslatableComponent("demo.day.6", new Object[] { dka7.keyScreenshot.getTranslatedKeyMessage() }));
            }
        }
        else if (a4 == ClientboundGameEventPacket.ARROW_HIT_PLAYER) {
            this.level.playSound(bft3, bft3.getX(), bft3.getEyeY(), bft3.getZ(), SoundEvents.ARROW_HIT_PLAYER, SoundSource.PLAYERS, 0.18f, 0.45f);
        }
        else if (a4 == ClientboundGameEventPacket.RAIN_LEVEL_CHANGE) {
            this.level.setRainLevel(float5);
        }
        else if (a4 == ClientboundGameEventPacket.THUNDER_LEVEL_CHANGE) {
            this.level.setThunderLevel(float5);
        }
        else if (a4 == ClientboundGameEventPacket.PUFFER_FISH_STING) {
            this.level.playSound(bft3, bft3.getX(), bft3.getY(), bft3.getZ(), SoundEvents.PUFFER_FISH_STING, SoundSource.NEUTRAL, 1.0f, 1.0f);
        }
        else if (a4 == ClientboundGameEventPacket.GUARDIAN_ELDER_EFFECT) {
            this.level.addParticle(ParticleTypes.ELDER_GUARDIAN, bft3.getX(), bft3.getY(), bft3.getZ(), 0.0, 0.0, 0.0);
            if (integer6 == 1) {
                this.level.playSound(bft3, bft3.getX(), bft3.getY(), bft3.getZ(), SoundEvents.ELDER_GUARDIAN_CURSE, SoundSource.HOSTILE, 1.0f, 1.0f);
            }
        }
        else if (a4 == ClientboundGameEventPacket.IMMEDIATE_RESPAWN) {
            this.minecraft.player.setShowDeathScreen(float5 == 0.0f);
        }
    }
    
    public void handleMapItemData(final ClientboundMapItemDataPacket py) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)py, this, this.minecraft);
        final MapRenderer dku3 = this.minecraft.gameRenderer.getMapRenderer();
        final String string4 = MapItem.makeKey(py.getMapId());
        MapItemSavedData cxu5 = this.minecraft.level.getMapData(string4);
        if (cxu5 == null) {
            cxu5 = new MapItemSavedData(string4);
            if (dku3.getMapInstanceIfExists(string4) != null) {
                final MapItemSavedData cxu6 = dku3.getData(dku3.getMapInstanceIfExists(string4));
                if (cxu6 != null) {
                    cxu5 = cxu6;
                }
            }
            this.minecraft.level.setMapData(cxu5);
        }
        py.applyToMap(cxu5);
        dku3.update(cxu5);
    }
    
    public void handleLevelEvent(final ClientboundLevelEventPacket pu) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)pu, this, this.minecraft);
        if (pu.isGlobalEvent()) {
            this.minecraft.level.globalLevelEvent(pu.getType(), pu.getPos(), pu.getData());
        }
        else {
            this.minecraft.level.levelEvent(pu.getType(), pu.getPos(), pu.getData());
        }
    }
    
    public void handleUpdateAdvancementsPacket(final ClientboundUpdateAdvancementsPacket rt) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)rt, this, this.minecraft);
        this.advancements.update(rt);
    }
    
    public void handleSelectAdvancementsTab(final ClientboundSelectAdvancementsTabPacket qs) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)qs, this, this.minecraft);
        final ResourceLocation vk3 = qs.getTab();
        if (vk3 == null) {
            this.advancements.setSelectedTab(null, false);
        }
        else {
            final Advancement y4 = this.advancements.getAdvancements().get(vk3);
            this.advancements.setSelectedTab(y4, false);
        }
    }
    
    public void handleCommands(final ClientboundCommandsPacket pd) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)pd, this, this.minecraft);
        this.commands = (CommandDispatcher<SharedSuggestionProvider>)new CommandDispatcher((RootCommandNode)pd.getRoot());
    }
    
    public void handleStopSoundEvent(final ClientboundStopSoundPacket ro) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)ro, this, this.minecraft);
        this.minecraft.getSoundManager().stop(ro.getName(), ro.getSource());
    }
    
    public void handleCommandSuggestions(final ClientboundCommandSuggestionsPacket pc) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)pc, this, this.minecraft);
        this.suggestionsProvider.completeCustomSuggestions(pc.getId(), pc.getSuggestions());
    }
    
    public void handleUpdateRecipes(final ClientboundUpdateRecipesPacket rw) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)rw, this, this.minecraft);
        this.recipeManager.replaceRecipes((Iterable<Recipe<?>>)rw.getRecipes());
        final MutableSearchTree<RecipeCollection> emq3 = this.minecraft.<RecipeCollection>getSearchTree(SearchRegistry.RECIPE_COLLECTIONS);
        emq3.clear();
        final ClientRecipeBook djj4 = this.minecraft.player.getRecipeBook();
        djj4.setupCollections((Iterable<Recipe<?>>)this.recipeManager.getRecipes());
        djj4.getCollections().forEach(emq3::add);
        emq3.refresh();
    }
    
    public void handleLookAt(final ClientboundPlayerLookAtPacket qj) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)qj, this, this.minecraft);
        final Vec3 dck3 = qj.getPosition(this.level);
        if (dck3 != null) {
            this.minecraft.player.lookAt(qj.getFromAnchor(), dck3);
        }
    }
    
    public void handleTagQueryPacket(final ClientboundTagQueryPacket rq) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)rq, this, this.minecraft);
        if (!this.debugQueryHandler.handleResponse(rq.getTransactionId(), rq.getTag())) {
            ClientPacketListener.LOGGER.debug("Got unhandled response to tag query {}", rq.getTransactionId());
        }
    }
    
    public void handleAwardStats(final ClientboundAwardStatsPacket ot) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)ot, this, this.minecraft);
        for (final Map.Entry<Stat<?>, Integer> entry4 : ot.getStats().entrySet()) {
            final Stat<?> adv5 = entry4.getKey();
            final int integer6 = (int)entry4.getValue();
            this.minecraft.player.getStats().setValue(this.minecraft.player, adv5, integer6);
        }
        if (this.minecraft.screen instanceof StatsUpdateListener) {
            ((StatsUpdateListener)this.minecraft.screen).onStatsUpdated();
        }
    }
    
    public void handleAddOrRemoveRecipes(final ClientboundRecipePacket ql) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)ql, this, this.minecraft);
        final ClientRecipeBook djj3 = this.minecraft.player.getRecipeBook();
        djj3.setBookSettings(ql.getBookSettings());
        final ClientboundRecipePacket.State a4 = ql.getState();
        switch (a4) {
            case REMOVE: {
                for (final ResourceLocation vk6 : ql.getRecipes()) {
                    this.recipeManager.byKey(vk6).ifPresent(djj3::remove);
                }
                break;
            }
            case INIT: {
                for (final ResourceLocation vk6 : ql.getRecipes()) {
                    this.recipeManager.byKey(vk6).ifPresent(djj3::add);
                }
                for (final ResourceLocation vk6 : ql.getHighlights()) {
                    this.recipeManager.byKey(vk6).ifPresent(djj3::addHighlight);
                }
                break;
            }
            case ADD: {
                for (final ResourceLocation vk6 : ql.getRecipes()) {
                    this.recipeManager.byKey(vk6).ifPresent(bon -> {
                        djj3.add(bon);
                        djj3.addHighlight(bon);
                        RecipeToast.addOrUpdate(this.minecraft.getToasts(), bon);
                    });
                }
                break;
            }
        }
        djj3.getCollections().forEach(drq -> drq.updateKnownRecipes(djj3));
        if (this.minecraft.screen instanceof RecipeUpdateListener) {
            ((RecipeUpdateListener)this.minecraft.screen).recipesUpdated();
        }
    }
    
    public void handleUpdateMobEffect(final ClientboundUpdateMobEffectPacket rv) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)rv, this, this.minecraft);
        final Entity apx3 = this.level.getEntity(rv.getEntityId());
        if (!(apx3 instanceof LivingEntity)) {
            return;
        }
        final MobEffect app4 = MobEffect.byId(rv.getEffectId());
        if (app4 == null) {
            return;
        }
        final MobEffectInstance apr5 = new MobEffectInstance(app4, rv.getEffectDurationTicks(), rv.getEffectAmplifier(), rv.isEffectAmbient(), rv.isEffectVisible(), rv.effectShowsIcon());
        apr5.setNoCounter(rv.isSuperLongDuration());
        ((LivingEntity)apx3).forceAddEffect(apr5);
    }
    
    public void handleUpdateTags(final ClientboundUpdateTagsPacket rx) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)rx, this, this.minecraft);
        final TagContainer ael3 = rx.getTags();
        final Multimap<ResourceLocation, ResourceLocation> multimap4 = StaticTags.getAllMissingTags(ael3);
        if (!multimap4.isEmpty()) {
            ClientPacketListener.LOGGER.warn("Incomplete server tags, disconnecting. Missing: {}", multimap4);
            this.connection.disconnect(new TranslatableComponent("multiplayer.disconnect.missing_tags"));
            return;
        }
        this.tags = ael3;
        if (!this.connection.isMemoryConnection()) {
            ael3.bindToGlobal();
        }
        this.minecraft.<ItemStack>getSearchTree(SearchRegistry.CREATIVE_TAGS).refresh();
    }
    
    public void handlePlayerCombat(final ClientboundPlayerCombatPacket qh) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)qh, this, this.minecraft);
        if (qh.event == ClientboundPlayerCombatPacket.Event.ENTITY_DIED) {
            final Entity apx3 = this.level.getEntity(qh.playerId);
            if (apx3 == this.minecraft.player) {
                if (this.minecraft.player.shouldShowDeathScreen()) {
                    this.minecraft.setScreen(new DeathScreen(qh.message, this.level.getLevelData().isHardcore()));
                }
                else {
                    this.minecraft.player.respawn();
                }
            }
        }
    }
    
    public void handleChangeDifficulty(final ClientboundChangeDifficultyPacket pa) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)pa, this, this.minecraft);
        this.levelData.setDifficulty(pa.getDifficulty());
        this.levelData.setDifficultyLocked(pa.isLocked());
    }
    
    public void handleSetCamera(final ClientboundSetCameraPacket qu) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)qu, this, this.minecraft);
        final Entity apx3 = qu.getEntity(this.level);
        if (apx3 != null) {
            this.minecraft.setCameraEntity(apx3);
        }
    }
    
    public void handleSetBorder(final ClientboundSetBorderPacket qt) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)qt, this, this.minecraft);
        qt.applyChanges(this.level.getWorldBorder());
    }
    
    public void handleSetTitles(final ClientboundSetTitlesPacket rl) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)rl, this, this.minecraft);
        final ClientboundSetTitlesPacket.Type a3 = rl.getType();
        Component nr4 = null;
        Component nr5 = null;
        final Component nr6 = (rl.getText() != null) ? rl.getText() : TextComponent.EMPTY;
        switch (a3) {
            case TITLE: {
                nr4 = nr6;
                break;
            }
            case SUBTITLE: {
                nr5 = nr6;
                break;
            }
            case ACTIONBAR: {
                this.minecraft.gui.setOverlayMessage(nr6, false);
                return;
            }
            case RESET: {
                this.minecraft.gui.setTitles(null, null, -1, -1, -1);
                this.minecraft.gui.resetTitleTimes();
                return;
            }
        }
        this.minecraft.gui.setTitles(nr4, nr5, rl.getFadeInTime(), rl.getStayTime(), rl.getFadeOutTime());
    }
    
    public void handleTabListCustomisation(final ClientboundTabListPacket rp) {
        this.minecraft.gui.getTabList().setHeader(rp.getHeader().getString().isEmpty() ? null : rp.getHeader());
        this.minecraft.gui.getTabList().setFooter(rp.getFooter().getString().isEmpty() ? null : rp.getFooter());
    }
    
    public void handleRemoveMobEffect(final ClientboundRemoveMobEffectPacket qn) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)qn, this, this.minecraft);
        final Entity apx3 = qn.getEntity(this.level);
        if (apx3 instanceof LivingEntity) {
            ((LivingEntity)apx3).removeEffectNoUpdate(qn.getEffect());
        }
    }
    
    public void handlePlayerInfo(final ClientboundPlayerInfoPacket qi) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)qi, this, this.minecraft);
        for (final ClientboundPlayerInfoPacket.PlayerUpdate b4 : qi.getEntries()) {
            if (qi.getAction() == ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER) {
                this.playerInfoMap.remove(b4.getProfile().getId());
            }
            else {
                PlayerInfo dwp5 = (PlayerInfo)this.playerInfoMap.get(b4.getProfile().getId());
                if (qi.getAction() == ClientboundPlayerInfoPacket.Action.ADD_PLAYER) {
                    dwp5 = new PlayerInfo(b4);
                    this.playerInfoMap.put(dwp5.getProfile().getId(), dwp5);
                }
                if (dwp5 == null) {
                    continue;
                }
                switch (qi.getAction()) {
                    case ADD_PLAYER: {
                        dwp5.setGameMode(b4.getGameMode());
                        dwp5.setLatency(b4.getLatency());
                        dwp5.setTabListDisplayName(b4.getDisplayName());
                        continue;
                    }
                    case UPDATE_GAME_MODE: {
                        dwp5.setGameMode(b4.getGameMode());
                        continue;
                    }
                    case UPDATE_LATENCY: {
                        dwp5.setLatency(b4.getLatency());
                        continue;
                    }
                    case UPDATE_DISPLAY_NAME: {
                        dwp5.setTabListDisplayName(b4.getDisplayName());
                        continue;
                    }
                }
            }
        }
    }
    
    public void handleKeepAlive(final ClientboundKeepAlivePacket ps) {
        this.send(new ServerboundKeepAlivePacket(ps.getId()));
    }
    
    public void handlePlayerAbilities(final ClientboundPlayerAbilitiesPacket qg) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)qg, this, this.minecraft);
        final Player bft3 = this.minecraft.player;
        bft3.abilities.flying = qg.isFlying();
        bft3.abilities.instabuild = qg.canInstabuild();
        bft3.abilities.invulnerable = qg.isInvulnerable();
        bft3.abilities.mayfly = qg.canFly();
        bft3.abilities.setFlyingSpeed(qg.getFlyingSpeed());
        bft3.abilities.setWalkingSpeed(qg.getWalkingSpeed());
    }
    
    public void handleSoundEvent(final ClientboundSoundPacket rn) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)rn, this, this.minecraft);
        this.minecraft.level.playSound(this.minecraft.player, rn.getX(), rn.getY(), rn.getZ(), rn.getSound(), rn.getSource(), rn.getVolume(), rn.getPitch());
    }
    
    public void handleSoundEntityEvent(final ClientboundSoundEntityPacket rm) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)rm, this, this.minecraft);
        final Entity apx3 = this.level.getEntity(rm.getId());
        if (apx3 == null) {
            return;
        }
        this.minecraft.level.playSound(this.minecraft.player, apx3, rm.getSound(), rm.getSource(), rm.getVolume(), rm.getPitch());
    }
    
    public void handleCustomSoundEvent(final ClientboundCustomSoundPacket pl) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)pl, this, this.minecraft);
        this.minecraft.getSoundManager().play(new SimpleSoundInstance(pl.getName(), pl.getSource(), pl.getVolume(), pl.getPitch(), false, 0, SoundInstance.Attenuation.LINEAR, pl.getX(), pl.getY(), pl.getZ(), false));
    }
    
    public void handleResourcePack(final ClientboundResourcePackPacket qo) {
        final String string3 = qo.getUrl();
        final String string4 = qo.getHash();
        if (!this.validateResourcePackUrl(string3)) {
            return;
        }
        if (string3.startsWith("level://")) {
            try {
                final String string5 = URLDecoder.decode(string3.substring("level://".length()), StandardCharsets.UTF_8.toString());
                final File file6 = new File(this.minecraft.gameDirectory, "saves");
                final File file7 = new File(file6, string5);
                if (file7.isFile()) {
                    this.send(ServerboundResourcePackPacket.Action.ACCEPTED);
                    final CompletableFuture<?> completableFuture8 = this.minecraft.getClientPackSource().setServerPack(file7, PackSource.WORLD);
                    this.downloadCallback(completableFuture8);
                    return;
                }
            }
            catch (UnsupportedEncodingException ex) {}
            this.send(ServerboundResourcePackPacket.Action.FAILED_DOWNLOAD);
            return;
        }
        final ServerData dwr5 = this.minecraft.getCurrentServer();
        if (dwr5 != null && dwr5.getResourcePackStatus() == ServerData.ServerPackStatus.ENABLED) {
            this.send(ServerboundResourcePackPacket.Action.ACCEPTED);
            this.downloadCallback(this.minecraft.getClientPackSource().downloadAndSelectResourcePack(string3, string4));
        }
        else if (dwr5 == null || dwr5.getResourcePackStatus() == ServerData.ServerPackStatus.PROMPT) {
            this.minecraft.execute(() -> this.minecraft.setScreen(new ConfirmScreen(boolean3 -> {
                this.minecraft = Minecraft.getInstance();
                final ServerData dwr5 = this.minecraft.getCurrentServer();
                if (boolean3) {
                    if (dwr5 != null) {
                        dwr5.setResourcePackStatus(ServerData.ServerPackStatus.ENABLED);
                    }
                    this.send(ServerboundResourcePackPacket.Action.ACCEPTED);
                    this.downloadCallback(this.minecraft.getClientPackSource().downloadAndSelectResourcePack(string3, string4));
                }
                else {
                    if (dwr5 != null) {
                        dwr5.setResourcePackStatus(ServerData.ServerPackStatus.DISABLED);
                    }
                    this.send(ServerboundResourcePackPacket.Action.DECLINED);
                }
                ServerList.saveSingleServer(dwr5);
                this.minecraft.setScreen(null);
            }, new TranslatableComponent("multiplayer.texturePrompt.line1"), new TranslatableComponent("multiplayer.texturePrompt.line2"))));
        }
        else {
            this.send(ServerboundResourcePackPacket.Action.DECLINED);
        }
    }
    
    private boolean validateResourcePackUrl(final String string) {
        try {
            final URI uRI3 = new URI(string);
            final String string2 = uRI3.getScheme();
            final boolean boolean5 = "level".equals(string2);
            if (!"http".equals(string2) && !"https".equals(string2) && !boolean5) {
                throw new URISyntaxException(string, "Wrong protocol");
            }
            if (boolean5 && (string.contains("..") || !string.endsWith("/resources.zip"))) {
                throw new URISyntaxException(string, "Invalid levelstorage resourcepack path");
            }
        }
        catch (URISyntaxException uRISyntaxException3) {
            this.send(ServerboundResourcePackPacket.Action.FAILED_DOWNLOAD);
            return false;
        }
        return true;
    }
    
    private void downloadCallback(final CompletableFuture<?> completableFuture) {
        completableFuture.thenRun(() -> this.send(ServerboundResourcePackPacket.Action.SUCCESSFULLY_LOADED)).exceptionally(throwable -> {
            this.send(ServerboundResourcePackPacket.Action.FAILED_DOWNLOAD);
            return null;
        });
    }
    
    private void send(final ServerboundResourcePackPacket.Action a) {
        this.connection.send(new ServerboundResourcePackPacket(a));
    }
    
    public void handleBossUpdate(final ClientboundBossEventPacket oz) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)oz, this, this.minecraft);
        this.minecraft.gui.getBossOverlay().update(oz);
    }
    
    public void handleItemCooldown(final ClientboundCooldownPacket pj) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)pj, this, this.minecraft);
        if (pj.getDuration() == 0) {
            this.minecraft.player.getCooldowns().removeCooldown(pj.getItem());
        }
        else {
            this.minecraft.player.getCooldowns().addCooldown(pj.getItem(), pj.getDuration());
        }
    }
    
    public void handleMoveVehicle(final ClientboundMoveVehiclePacket qb) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)qb, this, this.minecraft);
        final Entity apx3 = this.minecraft.player.getRootVehicle();
        if (apx3 != this.minecraft.player && apx3.isControlledByLocalInstance()) {
            apx3.absMoveTo(qb.getX(), qb.getY(), qb.getZ(), qb.getYRot(), qb.getXRot());
            this.connection.send(new ServerboundMoveVehiclePacket(apx3));
        }
    }
    
    public void handleOpenBook(final ClientboundOpenBookPacket qc) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)qc, this, this.minecraft);
        final ItemStack bly3 = this.minecraft.player.getItemInHand(qc.getHand());
        if (bly3.getItem() == Items.WRITTEN_BOOK) {
            this.minecraft.setScreen(new BookViewScreen(new BookViewScreen.WrittenBookAccess(bly3)));
        }
    }
    
    public void handleCustomPayload(final ClientboundCustomPayloadPacket pk) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)pk, this, this.minecraft);
        final ResourceLocation vk3 = pk.getIdentifier();
        FriendlyByteBuf nf4 = null;
        try {
            nf4 = pk.getData();
            if (ClientboundCustomPayloadPacket.BRAND.equals(vk3)) {
                this.minecraft.player.setServerBrand(nf4.readUtf(32767));
            }
            else if (ClientboundCustomPayloadPacket.DEBUG_PATHFINDING_PACKET.equals(vk3)) {
                final int integer5 = nf4.readInt();
                final float float6 = nf4.readFloat();
                final Path cxa7 = Path.createFromStream(nf4);
                this.minecraft.debugRenderer.pathfindingRenderer.addPath(integer5, cxa7, float6);
            }
            else if (ClientboundCustomPayloadPacket.DEBUG_NEIGHBORSUPDATE_PACKET.equals(vk3)) {
                final long long5 = nf4.readVarLong();
                final BlockPos fx7 = nf4.readBlockPos();
                ((NeighborsUpdateRenderer)this.minecraft.debugRenderer.neighborsUpdateRenderer).addUpdate(long5, fx7);
            }
            else if (ClientboundCustomPayloadPacket.DEBUG_CAVES_PACKET.equals(vk3)) {
                final BlockPos fx8 = nf4.readBlockPos();
                final int integer6 = nf4.readInt();
                final List<BlockPos> list7 = (List<BlockPos>)Lists.newArrayList();
                final List<Float> list8 = (List<Float>)Lists.newArrayList();
                for (int integer7 = 0; integer7 < integer6; ++integer7) {
                    list7.add(nf4.readBlockPos());
                    list8.add(nf4.readFloat());
                }
                this.minecraft.debugRenderer.caveRenderer.addTunnel(fx8, list7, list8);
            }
            else if (ClientboundCustomPayloadPacket.DEBUG_STRUCTURES_PACKET.equals(vk3)) {
                final DimensionType cha5 = this.registryAccess.dimensionTypes().get(nf4.readResourceLocation());
                final BoundingBox cqx6 = new BoundingBox(nf4.readInt(), nf4.readInt(), nf4.readInt(), nf4.readInt(), nf4.readInt(), nf4.readInt());
                final int integer8 = nf4.readInt();
                final List<BoundingBox> list9 = (List<BoundingBox>)Lists.newArrayList();
                final List<Boolean> list10 = (List<Boolean>)Lists.newArrayList();
                for (int integer9 = 0; integer9 < integer8; ++integer9) {
                    list9.add(new BoundingBox(nf4.readInt(), nf4.readInt(), nf4.readInt(), nf4.readInt(), nf4.readInt(), nf4.readInt()));
                    list10.add(nf4.readBoolean());
                }
                this.minecraft.debugRenderer.structureRenderer.addBoundingBox(cqx6, list9, list10, cha5);
            }
            else if (ClientboundCustomPayloadPacket.DEBUG_WORLDGENATTEMPT_PACKET.equals(vk3)) {
                ((WorldGenAttemptRenderer)this.minecraft.debugRenderer.worldGenAttemptRenderer).addPos(nf4.readBlockPos(), nf4.readFloat(), nf4.readFloat(), nf4.readFloat(), nf4.readFloat(), nf4.readFloat());
            }
            else if (ClientboundCustomPayloadPacket.DEBUG_VILLAGE_SECTIONS.equals(vk3)) {
                for (int integer5 = nf4.readInt(), integer6 = 0; integer6 < integer5; ++integer6) {
                    this.minecraft.debugRenderer.villageSectionsDebugRenderer.setVillageSection(nf4.readSectionPos());
                }
                for (int integer6 = nf4.readInt(), integer8 = 0; integer8 < integer6; ++integer8) {
                    this.minecraft.debugRenderer.villageSectionsDebugRenderer.setNotVillageSection(nf4.readSectionPos());
                }
            }
            else if (ClientboundCustomPayloadPacket.DEBUG_POI_ADDED_PACKET.equals(vk3)) {
                final BlockPos fx8 = nf4.readBlockPos();
                final String string6 = nf4.readUtf();
                final int integer8 = nf4.readInt();
                final BrainDebugRenderer.PoiInfo b8 = new BrainDebugRenderer.PoiInfo(fx8, string6, integer8);
                this.minecraft.debugRenderer.brainDebugRenderer.addPoi(b8);
            }
            else if (ClientboundCustomPayloadPacket.DEBUG_POI_REMOVED_PACKET.equals(vk3)) {
                final BlockPos fx8 = nf4.readBlockPos();
                this.minecraft.debugRenderer.brainDebugRenderer.removePoi(fx8);
            }
            else if (ClientboundCustomPayloadPacket.DEBUG_POI_TICKET_COUNT_PACKET.equals(vk3)) {
                final BlockPos fx8 = nf4.readBlockPos();
                final int integer6 = nf4.readInt();
                this.minecraft.debugRenderer.brainDebugRenderer.setFreeTicketCount(fx8, integer6);
            }
            else if (ClientboundCustomPayloadPacket.DEBUG_GOAL_SELECTOR.equals(vk3)) {
                final BlockPos fx8 = nf4.readBlockPos();
                final int integer6 = nf4.readInt();
                final int integer8 = nf4.readInt();
                final List<GoalSelectorDebugRenderer.DebugGoal> list11 = (List<GoalSelectorDebugRenderer.DebugGoal>)Lists.newArrayList();
                for (int integer7 = 0; integer7 < integer8; ++integer7) {
                    final int integer9 = nf4.readInt();
                    final boolean boolean11 = nf4.readBoolean();
                    final String string7 = nf4.readUtf(255);
                    list11.add(new GoalSelectorDebugRenderer.DebugGoal(fx8, integer9, string7, boolean11));
                }
                this.minecraft.debugRenderer.goalSelectorRenderer.addGoalSelector(integer6, list11);
            }
            else if (ClientboundCustomPayloadPacket.DEBUG_RAIDS.equals(vk3)) {
                final int integer5 = nf4.readInt();
                final Collection<BlockPos> collection6 = (Collection<BlockPos>)Lists.newArrayList();
                for (int integer8 = 0; integer8 < integer5; ++integer8) {
                    collection6.add(nf4.readBlockPos());
                }
                this.minecraft.debugRenderer.raidDebugRenderer.setRaidCenters(collection6);
            }
            else if (ClientboundCustomPayloadPacket.DEBUG_BRAIN.equals(vk3)) {
                final double double5 = nf4.readDouble();
                final double double6 = nf4.readDouble();
                final double double7 = nf4.readDouble();
                final Position gk11 = new PositionImpl(double5, double6, double7);
                final UUID uUID12 = nf4.readUUID();
                final int integer10 = nf4.readInt();
                final String string8 = nf4.readUtf();
                final String string9 = nf4.readUtf();
                final int integer11 = nf4.readInt();
                final float float7 = nf4.readFloat();
                final float float8 = nf4.readFloat();
                final String string10 = nf4.readUtf();
                final boolean boolean12 = nf4.readBoolean();
                Path cxa8;
                if (boolean12) {
                    cxa8 = Path.createFromStream(nf4);
                }
                else {
                    cxa8 = null;
                }
                final boolean boolean13 = nf4.readBoolean();
                final BrainDebugRenderer.BrainDump a23 = new BrainDebugRenderer.BrainDump(uUID12, integer10, string8, string9, integer11, float7, float8, gk11, string10, cxa8, boolean13);
                for (int integer12 = nf4.readInt(), integer13 = 0; integer13 < integer12; ++integer13) {
                    final String string11 = nf4.readUtf();
                    a23.activities.add(string11);
                }
                for (int integer13 = nf4.readInt(), integer14 = 0; integer14 < integer13; ++integer14) {
                    final String string12 = nf4.readUtf();
                    a23.behaviors.add(string12);
                }
                for (int integer14 = nf4.readInt(), integer15 = 0; integer15 < integer14; ++integer15) {
                    final String string13 = nf4.readUtf();
                    a23.memories.add(string13);
                }
                for (int integer15 = nf4.readInt(), integer16 = 0; integer16 < integer15; ++integer16) {
                    final BlockPos fx9 = nf4.readBlockPos();
                    a23.pois.add(fx9);
                }
                for (int integer16 = nf4.readInt(), integer17 = 0; integer17 < integer16; ++integer17) {
                    final BlockPos fx10 = nf4.readBlockPos();
                    a23.potentialPois.add(fx10);
                }
                for (int integer17 = nf4.readInt(), integer18 = 0; integer18 < integer17; ++integer18) {
                    final String string14 = nf4.readUtf();
                    a23.gossips.add(string14);
                }
                this.minecraft.debugRenderer.brainDebugRenderer.addOrUpdateBrainDump(a23);
            }
            else if (ClientboundCustomPayloadPacket.DEBUG_BEE.equals(vk3)) {
                final double double5 = nf4.readDouble();
                final double double6 = nf4.readDouble();
                final double double7 = nf4.readDouble();
                final Position gk11 = new PositionImpl(double5, double6, double7);
                final UUID uUID12 = nf4.readUUID();
                final int integer10 = nf4.readInt();
                final boolean boolean14 = nf4.readBoolean();
                BlockPos fx11 = null;
                if (boolean14) {
                    fx11 = nf4.readBlockPos();
                }
                final boolean boolean15 = nf4.readBoolean();
                BlockPos fx12 = null;
                if (boolean15) {
                    fx12 = nf4.readBlockPos();
                }
                final int integer19 = nf4.readInt();
                final boolean boolean16 = nf4.readBoolean();
                Path cxa9 = null;
                if (boolean16) {
                    cxa9 = Path.createFromStream(nf4);
                }
                final BeeDebugRenderer.BeeInfo a24 = new BeeDebugRenderer.BeeInfo(uUID12, integer10, gk11, cxa9, fx11, fx12, integer19);
                for (int integer20 = nf4.readInt(), integer21 = 0; integer21 < integer20; ++integer21) {
                    final String string15 = nf4.readUtf();
                    a24.goals.add(string15);
                }
                for (int integer21 = nf4.readInt(), integer12 = 0; integer12 < integer21; ++integer12) {
                    final BlockPos fx13 = nf4.readBlockPos();
                    a24.blacklistedHives.add(fx13);
                }
                this.minecraft.debugRenderer.beeDebugRenderer.addOrUpdateBeeInfo(a24);
            }
            else if (ClientboundCustomPayloadPacket.DEBUG_HIVE.equals(vk3)) {
                final BlockPos fx8 = nf4.readBlockPos();
                final String string6 = nf4.readUtf();
                final int integer8 = nf4.readInt();
                final int integer22 = nf4.readInt();
                final boolean boolean17 = nf4.readBoolean();
                final BeeDebugRenderer.HiveInfo b9 = new BeeDebugRenderer.HiveInfo(fx8, string6, integer8, integer22, boolean17, this.level.getGameTime());
                this.minecraft.debugRenderer.beeDebugRenderer.addOrUpdateHiveInfo(b9);
            }
            else if (ClientboundCustomPayloadPacket.DEBUG_GAME_TEST_CLEAR.equals(vk3)) {
                this.minecraft.debugRenderer.gameTestDebugRenderer.clear();
            }
            else if (ClientboundCustomPayloadPacket.DEBUG_GAME_TEST_ADD_MARKER.equals(vk3)) {
                final BlockPos fx8 = nf4.readBlockPos();
                final int integer6 = nf4.readInt();
                final String string16 = nf4.readUtf();
                final int integer22 = nf4.readInt();
                this.minecraft.debugRenderer.gameTestDebugRenderer.addMarker(fx8, integer6, string16, integer22);
            }
            else {
                ClientPacketListener.LOGGER.warn("Unknown custom packed identifier: {}", vk3);
            }
        }
        finally {
            if (nf4 != null) {
                nf4.release();
            }
        }
    }
    
    public void handleAddObjective(final ClientboundSetObjectivePacket rg) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)rg, this, this.minecraft);
        final Scoreboard ddk3 = this.level.getScoreboard();
        final String string4 = rg.getObjectiveName();
        if (rg.getMethod() == 0) {
            ddk3.addObjective(string4, ObjectiveCriteria.DUMMY, rg.getDisplayName(), rg.getRenderType());
        }
        else if (ddk3.hasObjective(string4)) {
            final Objective ddh5 = ddk3.getObjective(string4);
            if (rg.getMethod() == 1) {
                ddk3.removeObjective(ddh5);
            }
            else if (rg.getMethod() == 2) {
                ddh5.setRenderType(rg.getRenderType());
                ddh5.setDisplayName(rg.getDisplayName());
            }
        }
    }
    
    public void handleSetScore(final ClientboundSetScorePacket rj) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)rj, this, this.minecraft);
        final Scoreboard ddk3 = this.level.getScoreboard();
        final String string4 = rj.getObjectiveName();
        switch (rj.getMethod()) {
            case CHANGE: {
                final Objective ddh5 = ddk3.getOrCreateObjective(string4);
                final Score ddj6 = ddk3.getOrCreatePlayerScore(rj.getOwner(), ddh5);
                ddj6.setScore(rj.getScore());
                break;
            }
            case REMOVE: {
                ddk3.resetPlayerScore(rj.getOwner(), ddk3.getObjective(string4));
                break;
            }
        }
    }
    
    public void handleSetDisplayObjective(final ClientboundSetDisplayObjectivePacket qz) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)qz, this, this.minecraft);
        final Scoreboard ddk3 = this.level.getScoreboard();
        final String string4 = qz.getObjectiveName();
        final Objective ddh5 = (string4 == null) ? null : ddk3.getOrCreateObjective(string4);
        ddk3.setDisplayObjective(qz.getSlot(), ddh5);
    }
    
    public void handleSetPlayerTeamPacket(final ClientboundSetPlayerTeamPacket ri) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)ri, this, this.minecraft);
        final Scoreboard ddk3 = this.level.getScoreboard();
        PlayerTeam ddi4;
        if (ri.getMethod() == 0) {
            ddi4 = ddk3.addPlayerTeam(ri.getName());
        }
        else {
            ddi4 = ddk3.getPlayerTeam(ri.getName());
        }
        if (ri.getMethod() == 0 || ri.getMethod() == 2) {
            ddi4.setDisplayName(ri.getDisplayName());
            ddi4.setColor(ri.getColor());
            ddi4.unpackOptions(ri.getOptions());
            final Team.Visibility b5 = Team.Visibility.byName(ri.getNametagVisibility());
            if (b5 != null) {
                ddi4.setNameTagVisibility(b5);
            }
            final Team.CollisionRule a6 = Team.CollisionRule.byName(ri.getCollisionRule());
            if (a6 != null) {
                ddi4.setCollisionRule(a6);
            }
            ddi4.setPlayerPrefix(ri.getPlayerPrefix());
            ddi4.setPlayerSuffix(ri.getPlayerSuffix());
        }
        if (ri.getMethod() == 0 || ri.getMethod() == 3) {
            for (final String string6 : ri.getPlayers()) {
                ddk3.addPlayerToTeam(string6, ddi4);
            }
        }
        if (ri.getMethod() == 4) {
            for (final String string6 : ri.getPlayers()) {
                ddk3.removePlayerFromTeam(string6, ddi4);
            }
        }
        if (ri.getMethod() == 1) {
            ddk3.removePlayerTeam(ddi4);
        }
    }
    
    public void handleParticleEvent(final ClientboundLevelParticlesPacket pv) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)pv, this, this.minecraft);
        if (pv.getCount() == 0) {
            final double double3 = pv.getMaxSpeed() * pv.getXDist();
            final double double4 = pv.getMaxSpeed() * pv.getYDist();
            final double double5 = pv.getMaxSpeed() * pv.getZDist();
            try {
                this.level.addParticle(pv.getParticle(), pv.isOverrideLimiter(), pv.getX(), pv.getY(), pv.getZ(), double3, double4, double5);
            }
            catch (Throwable throwable9) {
                ClientPacketListener.LOGGER.warn("Could not spawn particle effect {}", pv.getParticle());
            }
        }
        else {
            for (int integer3 = 0; integer3 < pv.getCount(); ++integer3) {
                final double double6 = this.random.nextGaussian() * pv.getXDist();
                final double double7 = this.random.nextGaussian() * pv.getYDist();
                final double double8 = this.random.nextGaussian() * pv.getZDist();
                final double double9 = this.random.nextGaussian() * pv.getMaxSpeed();
                final double double10 = this.random.nextGaussian() * pv.getMaxSpeed();
                final double double11 = this.random.nextGaussian() * pv.getMaxSpeed();
                try {
                    this.level.addParticle(pv.getParticle(), pv.isOverrideLimiter(), pv.getX() + double6, pv.getY() + double7, pv.getZ() + double8, double9, double10, double11);
                }
                catch (Throwable throwable10) {
                    ClientPacketListener.LOGGER.warn("Could not spawn particle effect {}", pv.getParticle());
                    return;
                }
            }
        }
    }
    
    public void handleUpdateAttributes(final ClientboundUpdateAttributesPacket ru) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)ru, this, this.minecraft);
        final Entity apx3 = this.level.getEntity(ru.getEntityId());
        if (apx3 == null) {
            return;
        }
        if (!(apx3 instanceof LivingEntity)) {
            throw new IllegalStateException(new StringBuilder().append("Server tried to update attributes of a non-living entity (actually: ").append(apx3).append(")").toString());
        }
        final AttributeMap arf4 = ((LivingEntity)apx3).getAttributes();
        for (final ClientboundUpdateAttributesPacket.AttributeSnapshot a6 : ru.getValues()) {
            final AttributeInstance are7 = arf4.getInstance(a6.getAttribute());
            if (are7 == null) {
                ClientPacketListener.LOGGER.warn("Entity {} does not have attribute {}", apx3, Registry.ATTRIBUTE.getKey(a6.getAttribute()));
            }
            else {
                are7.setBaseValue(a6.getBase());
                are7.removeModifiers();
                for (final AttributeModifier arg9 : a6.getModifiers()) {
                    are7.addTransientModifier(arg9);
                }
            }
        }
    }
    
    public void handlePlaceRecipe(final ClientboundPlaceGhostRecipePacket qf) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)qf, this, this.minecraft);
        final AbstractContainerMenu bhz3 = this.minecraft.player.containerMenu;
        if (bhz3.containerId != qf.getContainerId() || !bhz3.isSynched(this.minecraft.player)) {
            return;
        }
        this.recipeManager.byKey(qf.getRecipe()).ifPresent(bon -> {
            if (this.minecraft.screen instanceof RecipeUpdateListener) {
                final RecipeBookComponent drm4 = ((RecipeUpdateListener)this.minecraft.screen).getRecipeBookComponent();
                drm4.setupGhostRecipe(bon, bhz3.slots);
            }
        });
    }
    
    public void handleLightUpdatePacked(final ClientboundLightUpdatePacket pw) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)pw, this, this.minecraft);
        final int integer3 = pw.getX();
        final int integer4 = pw.getZ();
        final LevelLightEngine cul5 = this.level.getChunkSource().getLightEngine();
        final int integer5 = pw.getSkyYMask();
        final int integer6 = pw.getEmptySkyYMask();
        final Iterator<byte[]> iterator8 = (Iterator<byte[]>)pw.getSkyUpdates().iterator();
        this.readSectionList(integer3, integer4, cul5, LightLayer.SKY, integer5, integer6, iterator8, pw.getTrustEdges());
        final int integer7 = pw.getBlockYMask();
        final int integer8 = pw.getEmptyBlockYMask();
        final Iterator<byte[]> iterator9 = (Iterator<byte[]>)pw.getBlockUpdates().iterator();
        this.readSectionList(integer3, integer4, cul5, LightLayer.BLOCK, integer7, integer8, iterator9, pw.getTrustEdges());
    }
    
    public void handleMerchantOffers(final ClientboundMerchantOffersPacket pz) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)pz, this, this.minecraft);
        final AbstractContainerMenu bhz3 = this.minecraft.player.containerMenu;
        if (pz.getContainerId() == bhz3.containerId && bhz3 instanceof MerchantMenu) {
            ((MerchantMenu)bhz3).setOffers(new MerchantOffers(pz.getOffers().createTag()));
            ((MerchantMenu)bhz3).setXp(pz.getVillagerXp());
            ((MerchantMenu)bhz3).setMerchantLevel(pz.getVillagerLevel());
            ((MerchantMenu)bhz3).setShowProgressBar(pz.showProgress());
            ((MerchantMenu)bhz3).setCanRestock(pz.canRestock());
        }
    }
    
    public void handleSetChunkCacheRadius(final ClientboundSetChunkCacheRadiusPacket qx) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)qx, this, this.minecraft);
        this.serverChunkRadius = qx.getRadius();
        this.level.getChunkSource().updateViewRadius(qx.getRadius());
    }
    
    public void handleSetChunkCacheCenter(final ClientboundSetChunkCacheCenterPacket qw) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)qw, this, this.minecraft);
        this.level.getChunkSource().updateViewCenter(qw.getX(), qw.getZ());
    }
    
    public void handleBlockBreakAck(final ClientboundBlockBreakAckPacket ou) {
        PacketUtils.<ClientPacketListener>ensureRunningOnSameThread((Packet<ClientPacketListener>)ou, this, this.minecraft);
        this.minecraft.gameMode.handleBlockBreakAck(this.level, ou.getPos(), ou.getState(), ou.action(), ou.allGood());
    }
    
    private void readSectionList(final int integer1, final int integer2, final LevelLightEngine cul, final LightLayer bsc, final int integer5, final int integer6, final Iterator<byte[]> iterator, final boolean boolean8) {
        for (int integer7 = 0; integer7 < 18; ++integer7) {
            final int integer8 = -1 + integer7;
            final boolean boolean9 = (integer5 & 1 << integer7) != 0x0;
            final boolean boolean10 = (integer6 & 1 << integer7) != 0x0;
            if (boolean9 || boolean10) {
                cul.queueSectionData(bsc, SectionPos.of(integer1, integer8, integer2), boolean9 ? new DataLayer(((byte[])iterator.next()).clone()) : new DataLayer(), boolean8);
                this.level.setSectionDirtyWithNeighbors(integer1, integer8, integer2);
            }
        }
    }
    
    public Connection getConnection() {
        return this.connection;
    }
    
    public Collection<PlayerInfo> getOnlinePlayers() {
        return (Collection<PlayerInfo>)this.playerInfoMap.values();
    }
    
    @Nullable
    public PlayerInfo getPlayerInfo(final UUID uUID) {
        return (PlayerInfo)this.playerInfoMap.get(uUID);
    }
    
    @Nullable
    public PlayerInfo getPlayerInfo(final String string) {
        for (final PlayerInfo dwp4 : this.playerInfoMap.values()) {
            if (dwp4.getProfile().getName().equals(string)) {
                return dwp4;
            }
        }
        return null;
    }
    
    public GameProfile getLocalGameProfile() {
        return this.localGameProfile;
    }
    
    public ClientAdvancements getAdvancements() {
        return this.advancements;
    }
    
    public CommandDispatcher<SharedSuggestionProvider> getCommands() {
        return this.commands;
    }
    
    public ClientLevel getLevel() {
        return this.level;
    }
    
    public TagContainer getTags() {
        return this.tags;
    }
    
    public DebugQueryHandler getDebugQueryHandler() {
        return this.debugQueryHandler;
    }
    
    public UUID getId() {
        return this.id;
    }
    
    public Set<ResourceKey<Level>> levels() {
        return this.levels;
    }
    
    public RegistryAccess registryAccess() {
        return this.registryAccess;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        GENERIC_DISCONNECT_MESSAGE = new TranslatableComponent("disconnect.lost");
    }
}

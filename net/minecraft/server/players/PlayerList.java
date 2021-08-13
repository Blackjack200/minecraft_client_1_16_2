package net.minecraft.server.players;

import org.apache.logging.log4j.LogManager;
import net.minecraft.network.protocol.game.ClientboundSetChunkCacheRadiusPacket;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.network.protocol.game.ClientboundChatPacket;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import java.util.Collection;
import net.minecraft.world.scores.Team;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.network.protocol.game.ClientboundSetDefaultSpawnPositionPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.util.Mth;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.Blocks;
import java.util.Optional;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.server.level.DemoMode;
import java.net.SocketAddress;
import net.minecraft.stats.Stats;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.protocol.game.ClientboundSetBorderPacket;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.border.BorderChangeListener;
import net.minecraft.world.scores.Objective;
import java.util.Set;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.world.scores.PlayerTeam;
import com.google.common.collect.Sets;
import net.minecraft.server.ServerScoreboard;
import java.util.Iterator;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.nbt.CompoundTag;
import com.mojang.authlib.GameProfile;
import net.minecraft.world.entity.Entity;
import java.util.function.Function;
import net.minecraft.world.entity.EntityType;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundUpdateTagsPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
import net.minecraft.network.protocol.game.ClientboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ClientboundChangeDifficultyPacket;
import net.minecraft.network.FriendlyByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.GameRules;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.Connection;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import net.minecraft.world.level.GameType;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.storage.PlayerDataStorage;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.stats.ServerStatsCounter;
import java.util.UUID;
import java.util.Map;
import net.minecraft.server.level.ServerPlayer;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.Logger;
import java.io.File;

public abstract class PlayerList {
    public static final File USERBANLIST_FILE;
    public static final File IPBANLIST_FILE;
    public static final File OPLIST_FILE;
    public static final File WHITELIST_FILE;
    private static final Logger LOGGER;
    private static final SimpleDateFormat BAN_DATE_FORMAT;
    private final MinecraftServer server;
    private final List<ServerPlayer> players;
    private final Map<UUID, ServerPlayer> playersByUUID;
    private final UserBanList bans;
    private final IpBanList ipBans;
    private final ServerOpList ops;
    private final UserWhiteList whitelist;
    private final Map<UUID, ServerStatsCounter> stats;
    private final Map<UUID, PlayerAdvancements> advancements;
    private final PlayerDataStorage playerIo;
    private boolean doWhiteList;
    private final RegistryAccess.RegistryHolder registryHolder;
    protected final int maxPlayers;
    private int viewDistance;
    private GameType overrideGameMode;
    private boolean allowCheatsForAllPlayers;
    private int sendAllPlayerInfoIn;
    
    public PlayerList(final MinecraftServer minecraftServer, final RegistryAccess.RegistryHolder b, final PlayerDataStorage cyh, final int integer) {
        this.players = (List<ServerPlayer>)Lists.newArrayList();
        this.playersByUUID = (Map<UUID, ServerPlayer>)Maps.newHashMap();
        this.bans = new UserBanList(PlayerList.USERBANLIST_FILE);
        this.ipBans = new IpBanList(PlayerList.IPBANLIST_FILE);
        this.ops = new ServerOpList(PlayerList.OPLIST_FILE);
        this.whitelist = new UserWhiteList(PlayerList.WHITELIST_FILE);
        this.stats = (Map<UUID, ServerStatsCounter>)Maps.newHashMap();
        this.advancements = (Map<UUID, PlayerAdvancements>)Maps.newHashMap();
        this.server = minecraftServer;
        this.registryHolder = b;
        this.maxPlayers = integer;
        this.playerIo = cyh;
    }
    
    public void placeNewPlayer(final Connection nd, final ServerPlayer aah) {
        final GameProfile gameProfile4 = aah.getGameProfile();
        final GameProfileCache aco5 = this.server.getProfileCache();
        final GameProfile gameProfile5 = aco5.get(gameProfile4.getId());
        final String string7 = (gameProfile5 == null) ? gameProfile4.getName() : gameProfile5.getName();
        aco5.add(gameProfile4);
        final CompoundTag md8 = this.load(aah);
        final ResourceKey<Level> vj9 = (ResourceKey<Level>)((md8 != null) ? DimensionType.parseLegacy(new Dynamic((DynamicOps)NbtOps.INSTANCE, md8.get("Dimension"))).resultOrPartial(PlayerList.LOGGER::error).orElse(Level.OVERWORLD) : Level.OVERWORLD);
        final ServerLevel aag10 = this.server.getLevel(vj9);
        ServerLevel aag11;
        if (aag10 == null) {
            PlayerList.LOGGER.warn("Unknown respawn dimension {}, defaulting to overworld", vj9);
            aag11 = this.server.overworld();
        }
        else {
            aag11 = aag10;
        }
        aah.setLevel(aag11);
        aah.gameMode.setLevel((ServerLevel)aah.level);
        String string8 = "local";
        if (nd.getRemoteAddress() != null) {
            string8 = nd.getRemoteAddress().toString();
        }
        PlayerList.LOGGER.info("{}[{}] logged in with entity id {} at ({}, {}, {})", aah.getName().getString(), string8, aah.getId(), aah.getX(), aah.getY(), aah.getZ());
        final LevelData cya13 = aag11.getLevelData();
        this.updatePlayerGameMode(aah, null, aag11);
        final ServerGamePacketListenerImpl aay14 = new ServerGamePacketListenerImpl(this.server, nd, aah);
        final GameRules brq15 = aag11.getGameRules();
        final boolean boolean16 = brq15.getBoolean(GameRules.RULE_DO_IMMEDIATE_RESPAWN);
        final boolean boolean17 = brq15.getBoolean(GameRules.RULE_REDUCEDDEBUGINFO);
        aay14.send(new ClientboundLoginPacket(aah.getId(), aah.gameMode.getGameModeForPlayer(), aah.gameMode.getPreviousGameModeForPlayer(), BiomeManager.obfuscateSeed(aag11.getSeed()), cya13.isHardcore(), this.server.levelKeys(), this.registryHolder, aag11.dimensionType(), aag11.dimension(), this.getMaxPlayers(), this.viewDistance, boolean17, !boolean16, aag11.isDebug(), aag11.isFlat()));
        aay14.send(new ClientboundCustomPayloadPacket(ClientboundCustomPayloadPacket.BRAND, new FriendlyByteBuf(Unpooled.buffer()).writeUtf(this.getServer().getServerModName())));
        aay14.send(new ClientboundChangeDifficultyPacket(cya13.getDifficulty(), cya13.isDifficultyLocked()));
        aay14.send(new ClientboundPlayerAbilitiesPacket(aah.abilities));
        aay14.send(new ClientboundSetCarriedItemPacket(aah.inventory.selected));
        aay14.send(new ClientboundUpdateRecipesPacket(this.server.getRecipeManager().getRecipes()));
        aay14.send(new ClientboundUpdateTagsPacket(this.server.getTags()));
        this.sendPlayerPermissionLevel(aah);
        aah.getStats().markAllDirty();
        aah.getRecipeBook().sendInitialRecipeBook(aah);
        this.updateEntireScoreboard(aag11.getScoreboard(), aah);
        this.server.invalidateStatus();
        MutableComponent nx18;
        if (aah.getGameProfile().getName().equalsIgnoreCase(string7)) {
            nx18 = new TranslatableComponent("multiplayer.player.joined", new Object[] { aah.getDisplayName() });
        }
        else {
            nx18 = new TranslatableComponent("multiplayer.player.joined.renamed", new Object[] { aah.getDisplayName(), string7 });
        }
        this.broadcastMessage(nx18.withStyle(ChatFormatting.YELLOW), ChatType.SYSTEM, Util.NIL_UUID);
        aay14.teleport(aah.getX(), aah.getY(), aah.getZ(), aah.yRot, aah.xRot);
        this.players.add(aah);
        this.playersByUUID.put(aah.getUUID(), aah);
        this.broadcastAll(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, new ServerPlayer[] { aah }));
        for (int integer19 = 0; integer19 < this.players.size(); ++integer19) {
            aah.connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, new ServerPlayer[] { (ServerPlayer)this.players.get(integer19) }));
        }
        aag11.addNewPlayer(aah);
        this.server.getCustomBossEvents().onPlayerConnect(aah);
        this.sendLevelInfo(aah, aag11);
        if (!this.server.getResourcePack().isEmpty()) {
            aah.sendTexturePack(this.server.getResourcePack(), this.server.getResourcePackHash());
        }
        for (final MobEffectInstance apr20 : aah.getActiveEffects()) {
            aay14.send(new ClientboundUpdateMobEffectPacket(aah.getId(), apr20));
        }
        if (md8 != null && md8.contains("RootVehicle", 10)) {
            final CompoundTag md9 = md8.getCompound("RootVehicle");
            final Entity apx20 = EntityType.loadEntityRecursive(md9.getCompound("Entity"), aag11, (Function<Entity, Entity>)(apx -> {
                if (!aag11.addWithUUID(apx)) {
                    return null;
                }
                return apx;
            }));
            if (apx20 != null) {
                UUID uUID21;
                if (md9.hasUUID("Attach")) {
                    uUID21 = md9.getUUID("Attach");
                }
                else {
                    uUID21 = null;
                }
                if (apx20.getUUID().equals(uUID21)) {
                    aah.startRiding(apx20, true);
                }
                else {
                    for (final Entity apx21 : apx20.getIndirectPassengers()) {
                        if (apx21.getUUID().equals(uUID21)) {
                            aah.startRiding(apx21, true);
                            break;
                        }
                    }
                }
                if (!aah.isPassenger()) {
                    PlayerList.LOGGER.warn("Couldn't reattach entity to player");
                    aag11.despawn(apx20);
                    for (final Entity apx21 : apx20.getIndirectPassengers()) {
                        aag11.despawn(apx21);
                    }
                }
            }
        }
        aah.initMenu();
    }
    
    protected void updateEntireScoreboard(final ServerScoreboard wa, final ServerPlayer aah) {
        final Set<Objective> set4 = (Set<Objective>)Sets.newHashSet();
        for (final PlayerTeam ddi6 : wa.getPlayerTeams()) {
            aah.connection.send(new ClientboundSetPlayerTeamPacket(ddi6, 0));
        }
        for (int integer5 = 0; integer5 < 19; ++integer5) {
            final Objective ddh6 = wa.getDisplayObjective(integer5);
            if (ddh6 != null && !set4.contains(ddh6)) {
                final List<Packet<?>> list7 = wa.getStartTrackingPackets(ddh6);
                for (final Packet<?> oj9 : list7) {
                    aah.connection.send(oj9);
                }
                set4.add(ddh6);
            }
        }
    }
    
    public void setLevel(final ServerLevel aag) {
        aag.getWorldBorder().addListener(new BorderChangeListener() {
            public void onBorderSizeSet(final WorldBorder cfr, final double double2) {
                PlayerList.this.broadcastAll(new ClientboundSetBorderPacket(cfr, ClientboundSetBorderPacket.Type.SET_SIZE));
            }
            
            public void onBorderSizeLerping(final WorldBorder cfr, final double double2, final double double3, final long long4) {
                PlayerList.this.broadcastAll(new ClientboundSetBorderPacket(cfr, ClientboundSetBorderPacket.Type.LERP_SIZE));
            }
            
            public void onBorderCenterSet(final WorldBorder cfr, final double double2, final double double3) {
                PlayerList.this.broadcastAll(new ClientboundSetBorderPacket(cfr, ClientboundSetBorderPacket.Type.SET_CENTER));
            }
            
            public void onBorderSetWarningTime(final WorldBorder cfr, final int integer) {
                PlayerList.this.broadcastAll(new ClientboundSetBorderPacket(cfr, ClientboundSetBorderPacket.Type.SET_WARNING_TIME));
            }
            
            public void onBorderSetWarningBlocks(final WorldBorder cfr, final int integer) {
                PlayerList.this.broadcastAll(new ClientboundSetBorderPacket(cfr, ClientboundSetBorderPacket.Type.SET_WARNING_BLOCKS));
            }
            
            public void onBorderSetDamagePerBlock(final WorldBorder cfr, final double double2) {
            }
            
            public void onBorderSetDamageSafeZOne(final WorldBorder cfr, final double double2) {
            }
        });
    }
    
    @Nullable
    public CompoundTag load(final ServerPlayer aah) {
        final CompoundTag md3 = this.server.getWorldData().getLoadedPlayerTag();
        CompoundTag md4;
        if (aah.getName().getString().equals(this.server.getSingleplayerName()) && md3 != null) {
            md4 = md3;
            aah.load(md4);
            PlayerList.LOGGER.debug("loading single player");
        }
        else {
            md4 = this.playerIo.load(aah);
        }
        return md4;
    }
    
    protected void save(final ServerPlayer aah) {
        this.playerIo.save(aah);
        final ServerStatsCounter adu3 = (ServerStatsCounter)this.stats.get(aah.getUUID());
        if (adu3 != null) {
            adu3.save();
        }
        final PlayerAdvancements vt4 = (PlayerAdvancements)this.advancements.get(aah.getUUID());
        if (vt4 != null) {
            vt4.save();
        }
    }
    
    public void remove(final ServerPlayer aah) {
        final ServerLevel aag3 = aah.getLevel();
        aah.awardStat(Stats.LEAVE_GAME);
        this.save(aah);
        if (aah.isPassenger()) {
            final Entity apx4 = aah.getRootVehicle();
            if (apx4.hasOnePlayerPassenger()) {
                PlayerList.LOGGER.debug("Removing player mount");
                aah.stopRiding();
                aag3.despawn(apx4);
                apx4.removed = true;
                for (final Entity apx5 : apx4.getIndirectPassengers()) {
                    aag3.despawn(apx5);
                    apx5.removed = true;
                }
                aag3.getChunk(aah.xChunk, aah.zChunk).markUnsaved();
            }
        }
        aah.unRide();
        aag3.removePlayerImmediately(aah);
        aah.getAdvancements().stopListening();
        this.players.remove(aah);
        this.server.getCustomBossEvents().onPlayerDisconnect(aah);
        final UUID uUID4 = aah.getUUID();
        final ServerPlayer aah2 = (ServerPlayer)this.playersByUUID.get(uUID4);
        if (aah2 == aah) {
            this.playersByUUID.remove(uUID4);
            this.stats.remove(uUID4);
            this.advancements.remove(uUID4);
        }
        this.broadcastAll(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, new ServerPlayer[] { aah }));
    }
    
    @Nullable
    public Component canPlayerLogin(final SocketAddress socketAddress, final GameProfile gameProfile) {
        if (this.bans.isBanned(gameProfile)) {
            final UserBanListEntry acy4 = this.bans.get(gameProfile);
            final MutableComponent nx5 = new TranslatableComponent("multiplayer.disconnect.banned.reason", new Object[] { acy4.getReason() });
            if (acy4.getExpires() != null) {
                nx5.append(new TranslatableComponent("multiplayer.disconnect.banned.expiration", new Object[] { PlayerList.BAN_DATE_FORMAT.format(acy4.getExpires()) }));
            }
            return nx5;
        }
        if (!this.isWhiteListed(gameProfile)) {
            return new TranslatableComponent("multiplayer.disconnect.not_whitelisted");
        }
        if (this.ipBans.isBanned(socketAddress)) {
            final IpBanListEntry acq4 = this.ipBans.get(socketAddress);
            final MutableComponent nx5 = new TranslatableComponent("multiplayer.disconnect.banned_ip.reason", new Object[] { acq4.getReason() });
            if (acq4.getExpires() != null) {
                nx5.append(new TranslatableComponent("multiplayer.disconnect.banned_ip.expiration", new Object[] { PlayerList.BAN_DATE_FORMAT.format(acq4.getExpires()) }));
            }
            return nx5;
        }
        if (this.players.size() >= this.maxPlayers && !this.canBypassPlayerLimit(gameProfile)) {
            return new TranslatableComponent("multiplayer.disconnect.server_full");
        }
        return null;
    }
    
    public ServerPlayer getPlayerForLogin(final GameProfile gameProfile) {
        final UUID uUID3 = Player.createPlayerUUID(gameProfile);
        final List<ServerPlayer> list4 = (List<ServerPlayer>)Lists.newArrayList();
        for (int integer5 = 0; integer5 < this.players.size(); ++integer5) {
            final ServerPlayer aah6 = (ServerPlayer)this.players.get(integer5);
            if (aah6.getUUID().equals(uUID3)) {
                list4.add(aah6);
            }
        }
        final ServerPlayer aah7 = (ServerPlayer)this.playersByUUID.get(gameProfile.getId());
        if (aah7 != null && !list4.contains(aah7)) {
            list4.add(aah7);
        }
        for (final ServerPlayer aah8 : list4) {
            aah8.connection.disconnect(new TranslatableComponent("multiplayer.disconnect.duplicate_login"));
        }
        final ServerLevel aag7 = this.server.overworld();
        ServerPlayerGameMode aai6;
        if (this.server.isDemo()) {
            aai6 = new DemoMode(aag7);
        }
        else {
            aai6 = new ServerPlayerGameMode(aag7);
        }
        return new ServerPlayer(this.server, aag7, gameProfile, aai6);
    }
    
    public ServerPlayer respawn(final ServerPlayer aah, final boolean boolean2) {
        this.players.remove(aah);
        aah.getLevel().removePlayerImmediately(aah);
        final BlockPos fx4 = aah.getRespawnPosition();
        final float float5 = aah.getRespawnAngle();
        final boolean boolean3 = aah.isRespawnForced();
        final ServerLevel aag7 = this.server.getLevel(aah.getRespawnDimension());
        Optional<Vec3> optional8;
        if (aag7 != null && fx4 != null) {
            optional8 = Player.findRespawnPositionAndUseSpawnBlock(aag7, fx4, float5, boolean3, boolean2);
        }
        else {
            optional8 = (Optional<Vec3>)Optional.empty();
        }
        final ServerLevel aag8 = (aag7 != null && optional8.isPresent()) ? aag7 : this.server.overworld();
        ServerPlayerGameMode aai9;
        if (this.server.isDemo()) {
            aai9 = new DemoMode(aag8);
        }
        else {
            aai9 = new ServerPlayerGameMode(aag8);
        }
        final ServerPlayer aah2 = new ServerPlayer(this.server, aag8, aah.getGameProfile(), aai9);
        aah2.connection = aah.connection;
        aah2.restoreFrom(aah, boolean2);
        aah2.setId(aah.getId());
        aah2.setMainArm(aah.getMainArm());
        for (final String string13 : aah.getTags()) {
            aah2.addTag(string13);
        }
        this.updatePlayerGameMode(aah2, aah, aag8);
        boolean boolean4 = false;
        if (optional8.isPresent()) {
            final BlockState cee13 = aag8.getBlockState(fx4);
            final boolean boolean5 = cee13.is(Blocks.RESPAWN_ANCHOR);
            final Vec3 dck15 = (Vec3)optional8.get();
            float float6;
            if (cee13.is(BlockTags.BEDS) || boolean5) {
                final Vec3 dck16 = Vec3.atBottomCenterOf(fx4).subtract(dck15).normalize();
                float6 = (float)Mth.wrapDegrees(Mth.atan2(dck16.z, dck16.x) * 57.2957763671875 - 90.0);
            }
            else {
                float6 = float5;
            }
            aah2.moveTo(dck15.x, dck15.y, dck15.z, float6, 0.0f);
            aah2.setRespawnPosition(aag8.dimension(), fx4, float5, boolean3, false);
            boolean4 = (!boolean2 && boolean5);
        }
        else if (fx4 != null) {
            aah2.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.NO_RESPAWN_BLOCK_AVAILABLE, 0.0f));
        }
        while (!aag8.noCollision(aah2) && aah2.getY() < 256.0) {
            aah2.setPos(aah2.getX(), aah2.getY() + 1.0, aah2.getZ());
        }
        final LevelData cya13 = aah2.level.getLevelData();
        aah2.connection.send(new ClientboundRespawnPacket(aah2.level.dimensionType(), aah2.level.dimension(), BiomeManager.obfuscateSeed(aah2.getLevel().getSeed()), aah2.gameMode.getGameModeForPlayer(), aah2.gameMode.getPreviousGameModeForPlayer(), aah2.getLevel().isDebug(), aah2.getLevel().isFlat(), boolean2));
        aah2.connection.teleport(aah2.getX(), aah2.getY(), aah2.getZ(), aah2.yRot, aah2.xRot);
        aah2.connection.send(new ClientboundSetDefaultSpawnPositionPacket(aag8.getSharedSpawnPos(), aag8.getSharedSpawnAngle()));
        aah2.connection.send(new ClientboundChangeDifficultyPacket(cya13.getDifficulty(), cya13.isDifficultyLocked()));
        aah2.connection.send(new ClientboundSetExperiencePacket(aah2.experienceProgress, aah2.totalExperience, aah2.experienceLevel));
        this.sendLevelInfo(aah2, aag8);
        this.sendPlayerPermissionLevel(aah2);
        aag8.addRespawnedPlayer(aah2);
        this.players.add(aah2);
        this.playersByUUID.put(aah2.getUUID(), aah2);
        aah2.initMenu();
        aah2.setHealth(aah2.getHealth());
        if (boolean4) {
            aah2.connection.send(new ClientboundSoundPacket(SoundEvents.RESPAWN_ANCHOR_DEPLETE, SoundSource.BLOCKS, fx4.getX(), fx4.getY(), fx4.getZ(), 1.0f, 1.0f));
        }
        return aah2;
    }
    
    public void sendPlayerPermissionLevel(final ServerPlayer aah) {
        final GameProfile gameProfile3 = aah.getGameProfile();
        final int integer4 = this.server.getProfilePermissions(gameProfile3);
        this.sendPlayerPermissionLevel(aah, integer4);
    }
    
    public void tick() {
        if (++this.sendAllPlayerInfoIn > 600) {
            this.broadcastAll(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.UPDATE_LATENCY, (Iterable<ServerPlayer>)this.players));
            this.sendAllPlayerInfoIn = 0;
        }
    }
    
    public void broadcastAll(final Packet<?> oj) {
        for (int integer3 = 0; integer3 < this.players.size(); ++integer3) {
            ((ServerPlayer)this.players.get(integer3)).connection.send(oj);
        }
    }
    
    public void broadcastAll(final Packet<?> oj, final ResourceKey<Level> vj) {
        for (int integer4 = 0; integer4 < this.players.size(); ++integer4) {
            final ServerPlayer aah5 = (ServerPlayer)this.players.get(integer4);
            if (aah5.level.dimension() == vj) {
                aah5.connection.send(oj);
            }
        }
    }
    
    public void broadcastToTeam(final Player bft, final Component nr) {
        final Team ddm4 = bft.getTeam();
        if (ddm4 == null) {
            return;
        }
        final Collection<String> collection5 = ddm4.getPlayers();
        for (final String string7 : collection5) {
            final ServerPlayer aah8 = this.getPlayerByName(string7);
            if (aah8 != null) {
                if (aah8 == bft) {
                    continue;
                }
                aah8.sendMessage(nr, bft.getUUID());
            }
        }
    }
    
    public void broadcastToAllExceptTeam(final Player bft, final Component nr) {
        final Team ddm4 = bft.getTeam();
        if (ddm4 == null) {
            this.broadcastMessage(nr, ChatType.SYSTEM, bft.getUUID());
            return;
        }
        for (int integer5 = 0; integer5 < this.players.size(); ++integer5) {
            final ServerPlayer aah6 = (ServerPlayer)this.players.get(integer5);
            if (aah6.getTeam() != ddm4) {
                aah6.sendMessage(nr, bft.getUUID());
            }
        }
    }
    
    public String[] getPlayerNamesArray() {
        final String[] arr2 = new String[this.players.size()];
        for (int integer3 = 0; integer3 < this.players.size(); ++integer3) {
            arr2[integer3] = ((ServerPlayer)this.players.get(integer3)).getGameProfile().getName();
        }
        return arr2;
    }
    
    public UserBanList getBans() {
        return this.bans;
    }
    
    public IpBanList getIpBans() {
        return this.ipBans;
    }
    
    public void op(final GameProfile gameProfile) {
        ((StoredUserList<K, ServerOpListEntry>)this.ops).add(new ServerOpListEntry(gameProfile, this.server.getOperatorUserPermissionLevel(), this.ops.canBypassPlayerLimit(gameProfile)));
        final ServerPlayer aah3 = this.getPlayer(gameProfile.getId());
        if (aah3 != null) {
            this.sendPlayerPermissionLevel(aah3);
        }
    }
    
    public void deop(final GameProfile gameProfile) {
        ((StoredUserList<GameProfile, V>)this.ops).remove(gameProfile);
        final ServerPlayer aah3 = this.getPlayer(gameProfile.getId());
        if (aah3 != null) {
            this.sendPlayerPermissionLevel(aah3);
        }
    }
    
    private void sendPlayerPermissionLevel(final ServerPlayer aah, final int integer) {
        if (aah.connection != null) {
            byte byte4;
            if (integer <= 0) {
                byte4 = 24;
            }
            else if (integer >= 4) {
                byte4 = 28;
            }
            else {
                byte4 = (byte)(24 + integer);
            }
            aah.connection.send(new ClientboundEntityEventPacket(aah, byte4));
        }
        this.server.getCommands().sendCommands(aah);
    }
    
    public boolean isWhiteListed(final GameProfile gameProfile) {
        return !this.doWhiteList || ((StoredUserList<GameProfile, V>)this.ops).contains(gameProfile) || ((StoredUserList<GameProfile, V>)this.whitelist).contains(gameProfile);
    }
    
    public boolean isOp(final GameProfile gameProfile) {
        return ((StoredUserList<GameProfile, V>)this.ops).contains(gameProfile) || (this.server.isSingleplayerOwner(gameProfile) && this.server.getWorldData().getAllowCommands()) || this.allowCheatsForAllPlayers;
    }
    
    @Nullable
    public ServerPlayer getPlayerByName(final String string) {
        for (final ServerPlayer aah4 : this.players) {
            if (aah4.getGameProfile().getName().equalsIgnoreCase(string)) {
                return aah4;
            }
        }
        return null;
    }
    
    public void broadcast(@Nullable final Player bft, final double double2, final double double3, final double double4, final double double5, final ResourceKey<Level> vj, final Packet<?> oj) {
        for (int integer13 = 0; integer13 < this.players.size(); ++integer13) {
            final ServerPlayer aah14 = (ServerPlayer)this.players.get(integer13);
            if (aah14 != bft) {
                if (aah14.level.dimension() == vj) {
                    final double double6 = double2 - aah14.getX();
                    final double double7 = double3 - aah14.getY();
                    final double double8 = double4 - aah14.getZ();
                    if (double6 * double6 + double7 * double7 + double8 * double8 < double5 * double5) {
                        aah14.connection.send(oj);
                    }
                }
            }
        }
    }
    
    public void saveAll() {
        for (int integer2 = 0; integer2 < this.players.size(); ++integer2) {
            this.save((ServerPlayer)this.players.get(integer2));
        }
    }
    
    public UserWhiteList getWhiteList() {
        return this.whitelist;
    }
    
    public String[] getWhiteListNames() {
        return this.whitelist.getUserList();
    }
    
    public ServerOpList getOps() {
        return this.ops;
    }
    
    public String[] getOpNames() {
        return this.ops.getUserList();
    }
    
    public void reloadWhiteList() {
    }
    
    public void sendLevelInfo(final ServerPlayer aah, final ServerLevel aag) {
        final WorldBorder cfr4 = this.server.overworld().getWorldBorder();
        aah.connection.send(new ClientboundSetBorderPacket(cfr4, ClientboundSetBorderPacket.Type.INITIALIZE));
        aah.connection.send(new ClientboundSetTimePacket(aag.getGameTime(), aag.getDayTime(), aag.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)));
        aah.connection.send(new ClientboundSetDefaultSpawnPositionPacket(aag.getSharedSpawnPos(), aag.getSharedSpawnAngle()));
        if (aag.isRaining()) {
            aah.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.START_RAINING, 0.0f));
            aah.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.RAIN_LEVEL_CHANGE, aag.getRainLevel(1.0f)));
            aah.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.THUNDER_LEVEL_CHANGE, aag.getThunderLevel(1.0f)));
        }
    }
    
    public void sendAllPlayerInfo(final ServerPlayer aah) {
        aah.refreshContainer(aah.inventoryMenu);
        aah.resetSentInfo();
        aah.connection.send(new ClientboundSetCarriedItemPacket(aah.inventory.selected));
    }
    
    public int getPlayerCount() {
        return this.players.size();
    }
    
    public int getMaxPlayers() {
        return this.maxPlayers;
    }
    
    public boolean isUsingWhitelist() {
        return this.doWhiteList;
    }
    
    public void setUsingWhiteList(final boolean boolean1) {
        this.doWhiteList = boolean1;
    }
    
    public List<ServerPlayer> getPlayersWithAddress(final String string) {
        final List<ServerPlayer> list3 = (List<ServerPlayer>)Lists.newArrayList();
        for (final ServerPlayer aah5 : this.players) {
            if (aah5.getIpAddress().equals(string)) {
                list3.add(aah5);
            }
        }
        return list3;
    }
    
    public int getViewDistance() {
        return this.viewDistance;
    }
    
    public MinecraftServer getServer() {
        return this.server;
    }
    
    public CompoundTag getSingleplayerData() {
        return null;
    }
    
    public void setOverrideGameMode(final GameType brr) {
        this.overrideGameMode = brr;
    }
    
    private void updatePlayerGameMode(final ServerPlayer aah1, @Nullable final ServerPlayer aah2, final ServerLevel aag) {
        if (aah2 != null) {
            aah1.gameMode.setGameModeForPlayer(aah2.gameMode.getGameModeForPlayer(), aah2.gameMode.getPreviousGameModeForPlayer());
        }
        else if (this.overrideGameMode != null) {
            aah1.gameMode.setGameModeForPlayer(this.overrideGameMode, GameType.NOT_SET);
        }
        aah1.gameMode.updateGameMode(aag.getServer().getWorldData().getGameType());
    }
    
    public void setAllowCheatsForAllPlayers(final boolean boolean1) {
        this.allowCheatsForAllPlayers = boolean1;
    }
    
    public void removeAll() {
        for (int integer2 = 0; integer2 < this.players.size(); ++integer2) {
            ((ServerPlayer)this.players.get(integer2)).connection.disconnect(new TranslatableComponent("multiplayer.disconnect.server_shutdown"));
        }
    }
    
    public void broadcastMessage(final Component nr, final ChatType no, final UUID uUID) {
        this.server.sendMessage(nr, uUID);
        this.broadcastAll(new ClientboundChatPacket(nr, no, uUID));
    }
    
    public ServerStatsCounter getPlayerStats(final Player bft) {
        final UUID uUID3 = bft.getUUID();
        ServerStatsCounter adu4 = (uUID3 == null) ? null : ((ServerStatsCounter)this.stats.get(uUID3));
        if (adu4 == null) {
            final File file5 = this.server.getWorldPath(LevelResource.PLAYER_STATS_DIR).toFile();
            final File file6 = new File(file5, new StringBuilder().append(uUID3).append(".json").toString());
            if (!file6.exists()) {
                final File file7 = new File(file5, bft.getName().getString() + ".json");
                if (file7.exists() && file7.isFile()) {
                    file7.renameTo(file6);
                }
            }
            adu4 = new ServerStatsCounter(this.server, file6);
            this.stats.put(uUID3, adu4);
        }
        return adu4;
    }
    
    public PlayerAdvancements getPlayerAdvancements(final ServerPlayer aah) {
        final UUID uUID3 = aah.getUUID();
        PlayerAdvancements vt4 = (PlayerAdvancements)this.advancements.get(uUID3);
        if (vt4 == null) {
            final File file5 = this.server.getWorldPath(LevelResource.PLAYER_ADVANCEMENTS_DIR).toFile();
            final File file6 = new File(file5, new StringBuilder().append(uUID3).append(".json").toString());
            vt4 = new PlayerAdvancements(this.server.getFixerUpper(), this, this.server.getAdvancements(), file6, aah);
            this.advancements.put(uUID3, vt4);
        }
        vt4.setPlayer(aah);
        return vt4;
    }
    
    public void setViewDistance(final int integer) {
        this.viewDistance = integer;
        this.broadcastAll(new ClientboundSetChunkCacheRadiusPacket(integer));
        for (final ServerLevel aag4 : this.server.getAllLevels()) {
            if (aag4 != null) {
                aag4.getChunkSource().setViewDistance(integer);
            }
        }
    }
    
    public List<ServerPlayer> getPlayers() {
        return this.players;
    }
    
    @Nullable
    public ServerPlayer getPlayer(final UUID uUID) {
        return (ServerPlayer)this.playersByUUID.get(uUID);
    }
    
    public boolean canBypassPlayerLimit(final GameProfile gameProfile) {
        return false;
    }
    
    public void reloadResources() {
        for (final PlayerAdvancements vt3 : this.advancements.values()) {
            vt3.reload(this.server.getAdvancements());
        }
        this.broadcastAll(new ClientboundUpdateTagsPacket(this.server.getTags()));
        final ClientboundUpdateRecipesPacket rw2 = new ClientboundUpdateRecipesPacket(this.server.getRecipeManager().getRecipes());
        for (final ServerPlayer aah4 : this.players) {
            aah4.connection.send(rw2);
            aah4.getRecipeBook().sendInitialRecipeBook(aah4);
        }
    }
    
    public boolean isAllowCheatsForAllPlayers() {
        return this.allowCheatsForAllPlayers;
    }
    
    static {
        USERBANLIST_FILE = new File("banned-players.json");
        IPBANLIST_FILE = new File("banned-ips.json");
        OPLIST_FILE = new File("ops.json");
        WHITELIST_FILE = new File("whitelist.json");
        LOGGER = LogManager.getLogger();
        BAN_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    }
}

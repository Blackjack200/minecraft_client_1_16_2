package net.minecraft.client.server;

import org.apache.logging.log4j.LogManager;
import java.util.List;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import java.util.Iterator;
import java.io.IOException;
import net.minecraft.server.level.ServerPlayer;
import java.net.InetAddress;
import net.minecraft.world.level.GameType;
import net.minecraft.world.Snooper;
import net.minecraft.client.ClientBrandRetriever;
import java.util.Optional;
import net.minecraft.CrashReportDetail;
import net.minecraft.CrashReport;
import java.io.File;
import net.minecraft.util.profiling.ProfilerFiller;
import java.util.function.BooleanSupplier;
import net.minecraft.util.Crypt;
import net.minecraft.SharedConstants;
import net.minecraft.server.players.PlayerList;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.server.players.GameProfileCache;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import net.minecraft.world.level.storage.WorldData;
import net.minecraft.server.ServerResources;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.core.RegistryAccess;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;
import net.minecraft.server.MinecraftServer;

public class IntegratedServer extends MinecraftServer {
    private static final Logger LOGGER;
    private final Minecraft minecraft;
    private boolean paused;
    private int publishedPort;
    private LanServerPinger lanPinger;
    private UUID uuid;
    
    public IntegratedServer(final Thread thread, final Minecraft djw, final RegistryAccess.RegistryHolder b, final LevelStorageSource.LevelStorageAccess a, final PackRepository abu, final ServerResources vz, final WorldData cyk, final MinecraftSessionService minecraftSessionService, final GameProfileRepository gameProfileRepository, final GameProfileCache aco, final ChunkProgressListenerFactory aaq) {
        super(thread, b, a, cyk, abu, djw.getProxy(), djw.getFixerUpper(), vz, minecraftSessionService, gameProfileRepository, aco, aaq);
        this.publishedPort = -1;
        this.setSingleplayerName(djw.getUser().getName());
        this.setDemo(djw.isDemo());
        this.setMaxBuildHeight(256);
        this.setPlayerList(new IntegratedPlayerList(this, this.registryHolder, this.playerDataStorage));
        this.minecraft = djw;
    }
    
    public boolean initServer() {
        IntegratedServer.LOGGER.info("Starting integrated minecraft server version " + SharedConstants.getCurrentVersion().getName());
        this.setUsesAuthentication(true);
        this.setPvpAllowed(true);
        this.setFlightAllowed(true);
        IntegratedServer.LOGGER.info("Generating keypair");
        this.setKeyPair(Crypt.generateKeyPair());
        this.loadLevel();
        this.setMotd(this.getSingleplayerName() + " - " + this.getWorldData().getLevelName());
        return true;
    }
    
    public void tickServer(final BooleanSupplier booleanSupplier) {
        final boolean boolean3 = this.paused;
        this.paused = (Minecraft.getInstance().getConnection() != null && Minecraft.getInstance().isPaused());
        final ProfilerFiller ant4 = this.getProfiler();
        if (!boolean3 && this.paused) {
            ant4.push("autoSave");
            IntegratedServer.LOGGER.info("Saving and pausing game...");
            this.getPlayerList().saveAll();
            this.saveAllChunks(false, false, false);
            ant4.pop();
        }
        if (this.paused) {
            return;
        }
        super.tickServer(booleanSupplier);
        final int integer5 = Math.max(2, this.minecraft.options.renderDistance - 1);
        if (integer5 != this.getPlayerList().getViewDistance()) {
            IntegratedServer.LOGGER.info("Changing view distance to {}, from {}", integer5, this.getPlayerList().getViewDistance());
            this.getPlayerList().setViewDistance(integer5);
        }
    }
    
    @Override
    public boolean shouldRconBroadcast() {
        return true;
    }
    
    @Override
    public boolean shouldInformAdmins() {
        return true;
    }
    
    @Override
    public File getServerDirectory() {
        return this.minecraft.gameDirectory;
    }
    
    @Override
    public boolean isDedicatedServer() {
        return false;
    }
    
    @Override
    public int getRateLimitPacketsPerSecond() {
        return 0;
    }
    
    @Override
    public boolean isEpollEnabled() {
        return false;
    }
    
    public void onServerCrash(final CrashReport l) {
        this.minecraft.delayCrash(l);
    }
    
    @Override
    public CrashReport fillReport(CrashReport l) {
        l = super.fillReport(l);
        l.getSystemDetails().setDetail("Type", "Integrated Server (map_client.txt)");
        l.getSystemDetails().setDetail("Is Modded", (CrashReportDetail<String>)(() -> (String)this.getModdedStatus().orElse("Probably not. Jar signature remains and both client + server brands are untouched.")));
        return l;
    }
    
    @Override
    public Optional<String> getModdedStatus() {
        String string2 = ClientBrandRetriever.getClientModName();
        if (!string2.equals("vanilla")) {
            return (Optional<String>)Optional.of(("Definitely; Client brand changed to '" + string2 + "'"));
        }
        string2 = this.getServerModName();
        if (!"vanilla".equals(string2)) {
            return (Optional<String>)Optional.of(("Definitely; Server brand changed to '" + string2 + "'"));
        }
        if (Minecraft.class.getSigners() == null) {
            return (Optional<String>)Optional.of("Very likely; Jar signature invalidated");
        }
        return (Optional<String>)Optional.empty();
    }
    
    @Override
    public void populateSnooper(final Snooper aoz) {
        super.populateSnooper(aoz);
        aoz.setDynamicData("snooper_partner", this.minecraft.getSnooper().getToken());
    }
    
    @Override
    public boolean publishServer(final GameType brr, final boolean boolean2, final int integer) {
        try {
            this.getConnection().startTcpServerListener(null, integer);
            IntegratedServer.LOGGER.info("Started serving on {}", integer);
            this.publishedPort = integer;
            (this.lanPinger = new LanServerPinger(this.getMotd(), new StringBuilder().append(integer).append("").toString())).start();
            this.getPlayerList().setOverrideGameMode(brr);
            this.getPlayerList().setAllowCheatsForAllPlayers(boolean2);
            final int integer2 = this.getProfilePermissions(this.minecraft.player.getGameProfile());
            this.minecraft.player.setPermissionLevel(integer2);
            for (final ServerPlayer aah7 : this.getPlayerList().getPlayers()) {
                this.getCommands().sendCommands(aah7);
            }
            return true;
        }
        catch (IOException ex) {
            return false;
        }
    }
    
    public void stopServer() {
        super.stopServer();
        if (this.lanPinger != null) {
            this.lanPinger.interrupt();
            this.lanPinger = null;
        }
    }
    
    @Override
    public void halt(final boolean boolean1) {
        this.executeBlocking(() -> {
            final List<ServerPlayer> list2 = (List<ServerPlayer>)Lists.newArrayList((Iterable)this.getPlayerList().getPlayers());
            for (final ServerPlayer aah4 : list2) {
                if (!aah4.getUUID().equals(this.uuid)) {
                    this.getPlayerList().remove(aah4);
                }
            }
        });
        super.halt(boolean1);
        if (this.lanPinger != null) {
            this.lanPinger.interrupt();
            this.lanPinger = null;
        }
    }
    
    @Override
    public boolean isPublished() {
        return this.publishedPort > -1;
    }
    
    @Override
    public int getPort() {
        return this.publishedPort;
    }
    
    @Override
    public void setDefaultGameType(final GameType brr) {
        super.setDefaultGameType(brr);
        this.getPlayerList().setOverrideGameMode(brr);
    }
    
    @Override
    public boolean isCommandBlockEnabled() {
        return true;
    }
    
    @Override
    public int getOperatorUserPermissionLevel() {
        return 2;
    }
    
    @Override
    public int getFunctionCompilationLevel() {
        return 2;
    }
    
    public void setUUID(final UUID uUID) {
        this.uuid = uUID;
    }
    
    @Override
    public boolean isSingleplayerOwner(final GameProfile gameProfile) {
        return gameProfile.getName().equalsIgnoreCase(this.getSingleplayerName());
    }
    
    @Override
    public int getScaledTrackingDistance(final int integer) {
        return (int)(this.minecraft.options.entityDistanceScaling * integer);
    }
    
    @Override
    public boolean forceSynchronousWrites() {
        return this.minecraft.options.syncWrites;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}

package net.minecraft.server.dedicated;

import org.apache.logging.log4j.LogManager;
import java.io.IOException;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.PlayerDataStorage;
import net.minecraft.core.RegistryAccess;
import org.apache.logging.log4j.Logger;
import net.minecraft.server.players.PlayerList;

public class DedicatedPlayerList extends PlayerList {
    private static final Logger LOGGER;
    
    public DedicatedPlayerList(final DedicatedServer zg, final RegistryAccess.RegistryHolder b, final PlayerDataStorage cyh) {
        super(zg, b, cyh, zg.getProperties().maxPlayers);
        final DedicatedServerProperties zh5 = zg.getProperties();
        this.setViewDistance(zh5.viewDistance);
        super.setUsingWhiteList(zh5.whiteList.get());
        this.loadUserBanList();
        this.saveUserBanList();
        this.loadIpBanList();
        this.saveIpBanList();
        this.loadOps();
        this.loadWhiteList();
        this.saveOps();
        if (!this.getWhiteList().getFile().exists()) {
            this.saveWhiteList();
        }
    }
    
    @Override
    public void setUsingWhiteList(final boolean boolean1) {
        super.setUsingWhiteList(boolean1);
        this.getServer().storeUsingWhiteList(boolean1);
    }
    
    @Override
    public void op(final GameProfile gameProfile) {
        super.op(gameProfile);
        this.saveOps();
    }
    
    @Override
    public void deop(final GameProfile gameProfile) {
        super.deop(gameProfile);
        this.saveOps();
    }
    
    @Override
    public void reloadWhiteList() {
        this.loadWhiteList();
    }
    
    private void saveIpBanList() {
        try {
            this.getIpBans().save();
        }
        catch (IOException iOException2) {
            DedicatedPlayerList.LOGGER.warn("Failed to save ip banlist: ", (Throwable)iOException2);
        }
    }
    
    private void saveUserBanList() {
        try {
            this.getBans().save();
        }
        catch (IOException iOException2) {
            DedicatedPlayerList.LOGGER.warn("Failed to save user banlist: ", (Throwable)iOException2);
        }
    }
    
    private void loadIpBanList() {
        try {
            this.getIpBans().load();
        }
        catch (IOException iOException2) {
            DedicatedPlayerList.LOGGER.warn("Failed to load ip banlist: ", (Throwable)iOException2);
        }
    }
    
    private void loadUserBanList() {
        try {
            this.getBans().load();
        }
        catch (IOException iOException2) {
            DedicatedPlayerList.LOGGER.warn("Failed to load user banlist: ", (Throwable)iOException2);
        }
    }
    
    private void loadOps() {
        try {
            this.getOps().load();
        }
        catch (Exception exception2) {
            DedicatedPlayerList.LOGGER.warn("Failed to load operators list: ", (Throwable)exception2);
        }
    }
    
    private void saveOps() {
        try {
            this.getOps().save();
        }
        catch (Exception exception2) {
            DedicatedPlayerList.LOGGER.warn("Failed to save operators list: ", (Throwable)exception2);
        }
    }
    
    private void loadWhiteList() {
        try {
            this.getWhiteList().load();
        }
        catch (Exception exception2) {
            DedicatedPlayerList.LOGGER.warn("Failed to load white-list: ", (Throwable)exception2);
        }
    }
    
    private void saveWhiteList() {
        try {
            this.getWhiteList().save();
        }
        catch (Exception exception2) {
            DedicatedPlayerList.LOGGER.warn("Failed to save white-list: ", (Throwable)exception2);
        }
    }
    
    @Override
    public boolean isWhiteListed(final GameProfile gameProfile) {
        return !this.isUsingWhitelist() || this.isOp(gameProfile) || this.getWhiteList().isWhiteListed(gameProfile);
    }
    
    @Override
    public DedicatedServer getServer() {
        return (DedicatedServer)super.getServer();
    }
    
    @Override
    public boolean canBypassPlayerLimit(final GameProfile gameProfile) {
        return this.getOps().canBypassPlayerLimit(gameProfile);
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}

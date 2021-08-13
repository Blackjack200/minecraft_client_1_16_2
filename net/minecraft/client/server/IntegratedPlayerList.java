package net.minecraft.client.server;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import com.mojang.authlib.GameProfile;
import java.net.SocketAddress;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.PlayerDataStorage;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.players.PlayerList;

public class IntegratedPlayerList extends PlayerList {
    private CompoundTag playerData;
    
    public IntegratedPlayerList(final IntegratedServer emy, final RegistryAccess.RegistryHolder b, final PlayerDataStorage cyh) {
        super(emy, b, cyh, 8);
        this.setViewDistance(10);
    }
    
    @Override
    protected void save(final ServerPlayer aah) {
        if (aah.getName().getString().equals(this.getServer().getSingleplayerName())) {
            this.playerData = aah.saveWithoutId(new CompoundTag());
        }
        super.save(aah);
    }
    
    @Override
    public Component canPlayerLogin(final SocketAddress socketAddress, final GameProfile gameProfile) {
        if (gameProfile.getName().equalsIgnoreCase(this.getServer().getSingleplayerName()) && this.getPlayerByName(gameProfile.getName()) != null) {
            return new TranslatableComponent("multiplayer.disconnect.name_taken");
        }
        return super.canPlayerLogin(socketAddress, gameProfile);
    }
    
    @Override
    public IntegratedServer getServer() {
        return (IntegratedServer)super.getServer();
    }
    
    @Override
    public CompoundTag getSingleplayerData() {
        return this.playerData;
    }
}

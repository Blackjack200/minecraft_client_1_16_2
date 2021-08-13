package net.minecraft.client.multiplayer;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.nbt.CompoundTag;
import java.util.Collections;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.SharedConstants;
import javax.annotation.Nullable;
import java.util.List;
import net.minecraft.network.chat.Component;

public class ServerData {
    public String name;
    public String ip;
    public Component status;
    public Component motd;
    public long ping;
    public int protocol;
    public Component version;
    public boolean pinged;
    public List<Component> playerList;
    private ServerPackStatus packStatus;
    @Nullable
    private String iconB64;
    private boolean lan;
    
    public ServerData(final String string1, final String string2, final boolean boolean3) {
        this.protocol = SharedConstants.getCurrentVersion().getProtocolVersion();
        this.version = new TextComponent(SharedConstants.getCurrentVersion().getName());
        this.playerList = (List<Component>)Collections.emptyList();
        this.packStatus = ServerPackStatus.PROMPT;
        this.name = string1;
        this.ip = string2;
        this.lan = boolean3;
    }
    
    public CompoundTag write() {
        final CompoundTag md2 = new CompoundTag();
        md2.putString("name", this.name);
        md2.putString("ip", this.ip);
        if (this.iconB64 != null) {
            md2.putString("icon", this.iconB64);
        }
        if (this.packStatus == ServerPackStatus.ENABLED) {
            md2.putBoolean("acceptTextures", true);
        }
        else if (this.packStatus == ServerPackStatus.DISABLED) {
            md2.putBoolean("acceptTextures", false);
        }
        return md2;
    }
    
    public ServerPackStatus getResourcePackStatus() {
        return this.packStatus;
    }
    
    public void setResourcePackStatus(final ServerPackStatus a) {
        this.packStatus = a;
    }
    
    public static ServerData read(final CompoundTag md) {
        final ServerData dwr2 = new ServerData(md.getString("name"), md.getString("ip"), false);
        if (md.contains("icon", 8)) {
            dwr2.setIconB64(md.getString("icon"));
        }
        if (md.contains("acceptTextures", 1)) {
            if (md.getBoolean("acceptTextures")) {
                dwr2.setResourcePackStatus(ServerPackStatus.ENABLED);
            }
            else {
                dwr2.setResourcePackStatus(ServerPackStatus.DISABLED);
            }
        }
        else {
            dwr2.setResourcePackStatus(ServerPackStatus.PROMPT);
        }
        return dwr2;
    }
    
    @Nullable
    public String getIconB64() {
        return this.iconB64;
    }
    
    public void setIconB64(@Nullable final String string) {
        this.iconB64 = string;
    }
    
    public boolean isLan() {
        return this.lan;
    }
    
    public void copyFrom(final ServerData dwr) {
        this.ip = dwr.ip;
        this.name = dwr.name;
        this.setResourcePackStatus(dwr.getResourcePackStatus());
        this.iconB64 = dwr.iconB64;
        this.lan = dwr.lan;
    }
    
    public enum ServerPackStatus {
        ENABLED("enabled"), 
        DISABLED("disabled"), 
        PROMPT("prompt");
        
        private final Component name;
        
        private ServerPackStatus(final String string3) {
            this.name = new TranslatableComponent("addServer.resourcePack." + string3);
        }
        
        public Component getName() {
            return this.name;
        }
    }
}

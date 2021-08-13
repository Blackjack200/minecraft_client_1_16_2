package net.minecraft.client.main;

import net.minecraft.client.resources.DirectAssetIndex;
import net.minecraft.client.resources.AssetIndex;
import javax.annotation.Nullable;
import java.io.File;
import java.net.Proxy;
import com.mojang.authlib.properties.PropertyMap;
import net.minecraft.client.User;
import com.mojang.blaze3d.platform.DisplayData;

public class GameConfig {
    public final UserData user;
    public final DisplayData display;
    public final FolderData location;
    public final GameData game;
    public final ServerData server;
    
    public GameConfig(final UserData d, final DisplayData deg, final FolderData a, final GameData b, final ServerData c) {
        this.user = d;
        this.display = deg;
        this.location = a;
        this.game = b;
        this.server = c;
    }
    
    public static class GameData {
        public final boolean demo;
        public final String launchVersion;
        public final String versionType;
        public final boolean disableMultiplayer;
        public final boolean disableChat;
        
        public GameData(final boolean boolean1, final String string2, final String string3, final boolean boolean4, final boolean boolean5) {
            this.demo = boolean1;
            this.launchVersion = string2;
            this.versionType = string3;
            this.disableMultiplayer = boolean4;
            this.disableChat = boolean5;
        }
    }
    
    public static class UserData {
        public final User user;
        public final PropertyMap userProperties;
        public final PropertyMap profileProperties;
        public final Proxy proxy;
        
        public UserData(final User dkj, final PropertyMap propertyMap2, final PropertyMap propertyMap3, final Proxy proxy) {
            this.user = dkj;
            this.userProperties = propertyMap2;
            this.profileProperties = propertyMap3;
            this.proxy = proxy;
        }
    }
    
    public static class FolderData {
        public final File gameDirectory;
        public final File resourcePackDirectory;
        public final File assetDirectory;
        @Nullable
        public final String assetIndex;
        
        public FolderData(final File file1, final File file2, final File file3, @Nullable final String string) {
            this.gameDirectory = file1;
            this.resourcePackDirectory = file2;
            this.assetDirectory = file3;
            this.assetIndex = string;
        }
        
        public AssetIndex getAssetIndex() {
            return (this.assetIndex == null) ? new DirectAssetIndex(this.assetDirectory) : new AssetIndex(this.assetDirectory, this.assetIndex);
        }
    }
    
    public static class ServerData {
        @Nullable
        public final String hostname;
        public final int port;
        
        public ServerData(@Nullable final String string, final int integer) {
            this.hostname = string;
            this.port = integer;
        }
    }
}

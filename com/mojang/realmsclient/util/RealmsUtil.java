package com.mojang.realmsclient.util;

import com.mojang.util.UUIDTypeAdapter;
import com.google.common.cache.CacheLoader;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import java.util.Date;
import com.google.common.collect.Maps;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import java.util.Map;
import com.mojang.authlib.GameProfile;
import com.google.common.cache.LoadingCache;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

public class RealmsUtil {
    private static final YggdrasilAuthenticationService AUTHENTICATION_SERVICE;
    private static final MinecraftSessionService SESSION_SERVICE;
    public static LoadingCache<String, GameProfile> gameProfileCache;
    
    public static String uuidToName(final String string) throws Exception {
        final GameProfile gameProfile2 = (GameProfile)RealmsUtil.gameProfileCache.get(string);
        return gameProfile2.getName();
    }
    
    public static Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> getTextures(final String string) {
        try {
            final GameProfile gameProfile2 = (GameProfile)RealmsUtil.gameProfileCache.get(string);
            return (Map<MinecraftProfileTexture.Type, MinecraftProfileTexture>)RealmsUtil.SESSION_SERVICE.getTextures(gameProfile2, false);
        }
        catch (Exception exception2) {
            return (Map<MinecraftProfileTexture.Type, MinecraftProfileTexture>)Maps.newHashMap();
        }
    }
    
    public static String convertToAgePresentation(final long long1) {
        if (long1 < 0L) {
            return "right now";
        }
        final long long2 = long1 / 1000L;
        if (long2 < 60L) {
            return new StringBuilder().append((long2 == 1L) ? "1 second" : new StringBuilder().append(long2).append(" seconds").toString()).append(" ago").toString();
        }
        if (long2 < 3600L) {
            final long long3 = long2 / 60L;
            return new StringBuilder().append((long3 == 1L) ? "1 minute" : new StringBuilder().append(long3).append(" minutes").toString()).append(" ago").toString();
        }
        if (long2 < 86400L) {
            final long long3 = long2 / 3600L;
            return new StringBuilder().append((long3 == 1L) ? "1 hour" : new StringBuilder().append(long3).append(" hours").toString()).append(" ago").toString();
        }
        final long long3 = long2 / 86400L;
        return new StringBuilder().append((long3 == 1L) ? "1 day" : new StringBuilder().append(long3).append(" days").toString()).append(" ago").toString();
    }
    
    public static String convertToAgePresentationFromInstant(final Date date) {
        return convertToAgePresentation(System.currentTimeMillis() - date.getTime());
    }
    
    static {
        AUTHENTICATION_SERVICE = new YggdrasilAuthenticationService(Minecraft.getInstance().getProxy(), UUID.randomUUID().toString());
        SESSION_SERVICE = RealmsUtil.AUTHENTICATION_SERVICE.createMinecraftSessionService();
        RealmsUtil.gameProfileCache = (LoadingCache<String, GameProfile>)CacheBuilder.newBuilder().expireAfterWrite(60L, TimeUnit.MINUTES).build((CacheLoader)new CacheLoader<String, GameProfile>() {
            public GameProfile load(final String string) throws Exception {
                final GameProfile gameProfile3 = RealmsUtil.SESSION_SERVICE.fillProfileProperties(new GameProfile(UUIDTypeAdapter.fromString(string), (String)null), false);
                if (gameProfile3 == null) {
                    throw new Exception("Couldn't get profile");
                }
                return gameProfile3;
            }
        });
    }
}

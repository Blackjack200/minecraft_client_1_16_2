package net.minecraft.client.resources;

import com.mojang.blaze3d.systems.RenderSystem;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import com.mojang.authlib.minecraft.InsecureTextureException;
import com.google.common.collect.Maps;
import com.google.common.collect.Iterables;
import net.minecraft.Util;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.HttpTexture;
import com.google.common.hash.Hashing;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import com.google.common.collect.ImmutableMap;
import com.mojang.authlib.properties.Property;
import java.util.UUID;
import com.mojang.authlib.GameProfile;
import com.google.common.cache.CacheLoader;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import java.util.Map;
import com.google.common.cache.LoadingCache;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import java.io.File;
import net.minecraft.client.renderer.texture.TextureManager;

public class SkinManager {
    private final TextureManager textureManager;
    private final File skinsDirectory;
    private final MinecraftSessionService sessionService;
    private final LoadingCache<String, Map<MinecraftProfileTexture.Type, MinecraftProfileTexture>> insecureSkinCache;
    
    public SkinManager(final TextureManager ejv, final File file, final MinecraftSessionService minecraftSessionService) {
        this.textureManager = ejv;
        this.skinsDirectory = file;
        this.sessionService = minecraftSessionService;
        this.insecureSkinCache = (LoadingCache<String, Map<MinecraftProfileTexture.Type, MinecraftProfileTexture>>)CacheBuilder.newBuilder().expireAfterAccess(15L, TimeUnit.SECONDS).build((CacheLoader)new CacheLoader<String, Map<MinecraftProfileTexture.Type, MinecraftProfileTexture>>() {
            public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> load(final String string) {
                final GameProfile gameProfile3 = new GameProfile((UUID)null, "dummy_mcdummyface");
                gameProfile3.getProperties().put("textures", new Property("textures", string, ""));
                try {
                    return (Map<MinecraftProfileTexture.Type, MinecraftProfileTexture>)minecraftSessionService.getTextures(gameProfile3, false);
                }
                catch (Throwable throwable4) {
                    return (Map<MinecraftProfileTexture.Type, MinecraftProfileTexture>)ImmutableMap.of();
                }
            }
        });
    }
    
    public ResourceLocation registerTexture(final MinecraftProfileTexture minecraftProfileTexture, final MinecraftProfileTexture.Type type) {
        return this.registerTexture(minecraftProfileTexture, type, null);
    }
    
    private ResourceLocation registerTexture(final MinecraftProfileTexture minecraftProfileTexture, final MinecraftProfileTexture.Type type, @Nullable final SkinTextureCallback a) {
        final String string5 = Hashing.sha1().hashUnencodedChars((CharSequence)minecraftProfileTexture.getHash()).toString();
        final ResourceLocation vk6 = new ResourceLocation("skins/" + string5);
        final AbstractTexture eji7 = this.textureManager.getTexture(vk6);
        if (eji7 != null) {
            if (a != null) {
                a.onSkinTextureAvailable(type, vk6, minecraftProfileTexture);
            }
        }
        else {
            final File file8 = new File(this.skinsDirectory, (string5.length() > 2) ? string5.substring(0, 2) : "xx");
            final File file9 = new File(file8, string5);
            final HttpTexture ejl10 = new HttpTexture(file9, minecraftProfileTexture.getUrl(), DefaultPlayerSkin.getDefaultSkin(), type == MinecraftProfileTexture.Type.SKIN, () -> {
                if (a != null) {
                    a.onSkinTextureAvailable(type, vk6, minecraftProfileTexture);
                }
            });
            this.textureManager.register(vk6, ejl10);
        }
        return vk6;
    }
    
    public void registerSkins(final GameProfile gameProfile, final SkinTextureCallback a, final boolean boolean3) {
        final Runnable runnable5 = () -> {
            final Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map5 = (Map<MinecraftProfileTexture.Type, MinecraftProfileTexture>)Maps.newHashMap();
            try {
                map5.putAll(this.sessionService.getTextures(gameProfile, boolean3));
            }
            catch (InsecureTextureException ex) {}
            if (map5.isEmpty()) {
                gameProfile.getProperties().clear();
                if (gameProfile.getId().equals(Minecraft.getInstance().getUser().getGameProfile().getId())) {
                    gameProfile.getProperties().putAll((Multimap)Minecraft.getInstance().getProfileProperties());
                    map5.putAll(this.sessionService.getTextures(gameProfile, false));
                }
                else {
                    this.sessionService.fillProfileProperties(gameProfile, boolean3);
                    try {
                        map5.putAll(this.sessionService.getTextures(gameProfile, boolean3));
                    }
                    catch (InsecureTextureException ex2) {}
                }
            }
            Minecraft.getInstance().execute(() -> RenderSystem.recordRenderCall(() -> ImmutableList.of(MinecraftProfileTexture.Type.SKIN, MinecraftProfileTexture.Type.CAPE).forEach(type -> {
                if (map5.containsKey(type)) {
                    this.registerTexture((MinecraftProfileTexture)map5.get(type), type, a);
                }
            })));
        };
        Util.backgroundExecutor().execute(runnable5);
    }
    
    public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> getInsecureSkinInformation(final GameProfile gameProfile) {
        final Property property3 = (Property)Iterables.getFirst((Iterable)gameProfile.getProperties().get("textures"), null);
        if (property3 == null) {
            return (Map<MinecraftProfileTexture.Type, MinecraftProfileTexture>)ImmutableMap.of();
        }
        return (Map<MinecraftProfileTexture.Type, MinecraftProfileTexture>)this.insecureSkinCache.getUnchecked(property3.getValue());
    }
    
    public interface SkinTextureCallback {
        void onSkinTextureAvailable(final MinecraftProfileTexture.Type type, final ResourceLocation vk, final MinecraftProfileTexture minecraftProfileTexture);
    }
}

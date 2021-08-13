package com.mojang.realmsclient.util;

import org.apache.logging.log4j.LogManager;
import com.google.common.collect.Maps;
import java.io.InputStream;
import java.nio.IntBuffer;
import com.mojang.blaze3d.platform.TextureUtil;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.io.ByteArrayInputStream;
import com.mojang.blaze3d.platform.GlStateManager;
import java.awt.image.BufferedImage;
import org.apache.commons.codec.binary.Base64;
import java.io.OutputStream;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import org.apache.commons.io.IOUtils;
import javax.imageio.ImageIO;
import java.net.URL;
import java.net.HttpURLConnection;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.util.UUIDTypeAdapter;
import net.minecraft.client.resources.DefaultPlayerSkin;
import java.util.UUID;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;
import java.util.Map;

public class RealmsTextureManager {
    private static final Map<String, RealmsTexture> TEXTURES;
    private static final Map<String, Boolean> SKIN_FETCH_STATUS;
    private static final Map<String, String> FETCHED_SKINS;
    private static final Logger LOGGER;
    private static final ResourceLocation TEMPLATE_ICON_LOCATION;
    
    public static void bindWorldTemplate(final String string1, @Nullable final String string2) {
        if (string2 == null) {
            Minecraft.getInstance().getTextureManager().bind(RealmsTextureManager.TEMPLATE_ICON_LOCATION);
            return;
        }
        final int integer3 = getTextureId(string1, string2);
        RenderSystem.bindTexture(integer3);
    }
    
    public static void withBoundFace(final String string, final Runnable runnable) {
        RenderSystem.pushTextureAttributes();
        try {
            bindFace(string);
            runnable.run();
        }
        finally {
            RenderSystem.popAttributes();
        }
    }
    
    private static void bindDefaultFace(final UUID uUID) {
        Minecraft.getInstance().getTextureManager().bind(DefaultPlayerSkin.getDefaultSkin(uUID));
    }
    
    private static void bindFace(final String string) {
        final UUID uUID2 = UUIDTypeAdapter.fromString(string);
        if (RealmsTextureManager.TEXTURES.containsKey(string)) {
            RenderSystem.bindTexture(((RealmsTexture)RealmsTextureManager.TEXTURES.get(string)).textureId);
            return;
        }
        if (RealmsTextureManager.SKIN_FETCH_STATUS.containsKey(string)) {
            if (!(boolean)RealmsTextureManager.SKIN_FETCH_STATUS.get(string)) {
                bindDefaultFace(uUID2);
            }
            else if (RealmsTextureManager.FETCHED_SKINS.containsKey(string)) {
                final int integer3 = getTextureId(string, (String)RealmsTextureManager.FETCHED_SKINS.get(string));
                RenderSystem.bindTexture(integer3);
            }
            else {
                bindDefaultFace(uUID2);
            }
            return;
        }
        RealmsTextureManager.SKIN_FETCH_STATUS.put(string, false);
        bindDefaultFace(uUID2);
        final Thread thread3 = new Thread("Realms Texture Downloader") {
            public void run() {
                final Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map2 = RealmsUtil.getTextures(string);
                if (map2.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                    final MinecraftProfileTexture minecraftProfileTexture3 = (MinecraftProfileTexture)map2.get(MinecraftProfileTexture.Type.SKIN);
                    final String string4 = minecraftProfileTexture3.getUrl();
                    HttpURLConnection httpURLConnection5 = null;
                    RealmsTextureManager.LOGGER.debug("Downloading http texture from {}", string4);
                    try {
                        httpURLConnection5 = (HttpURLConnection)new URL(string4).openConnection(Minecraft.getInstance().getProxy());
                        httpURLConnection5.setDoInput(true);
                        httpURLConnection5.setDoOutput(false);
                        httpURLConnection5.connect();
                        if (httpURLConnection5.getResponseCode() / 100 != 2) {
                            RealmsTextureManager.SKIN_FETCH_STATUS.remove(string);
                            return;
                        }
                        BufferedImage bufferedImage6;
                        try {
                            bufferedImage6 = ImageIO.read(httpURLConnection5.getInputStream());
                        }
                        catch (Exception exception7) {
                            RealmsTextureManager.SKIN_FETCH_STATUS.remove(string);
                            return;
                        }
                        finally {
                            IOUtils.closeQuietly(httpURLConnection5.getInputStream());
                        }
                        bufferedImage6 = new SkinProcessor().process(bufferedImage6);
                        final ByteArrayOutputStream byteArrayOutputStream7 = new ByteArrayOutputStream();
                        ImageIO.write((RenderedImage)bufferedImage6, "png", (OutputStream)byteArrayOutputStream7);
                        RealmsTextureManager.FETCHED_SKINS.put(string, new Base64().encodeToString(byteArrayOutputStream7.toByteArray()));
                        RealmsTextureManager.SKIN_FETCH_STATUS.put(string, true);
                    }
                    catch (Exception exception6) {
                        RealmsTextureManager.LOGGER.error("Couldn't download http texture", (Throwable)exception6);
                        RealmsTextureManager.SKIN_FETCH_STATUS.remove(string);
                    }
                    finally {
                        if (httpURLConnection5 != null) {
                            httpURLConnection5.disconnect();
                        }
                    }
                }
                else {
                    RealmsTextureManager.SKIN_FETCH_STATUS.put(string, true);
                }
            }
        };
        thread3.setDaemon(true);
        thread3.start();
    }
    
    private static int getTextureId(final String string1, final String string2) {
        int integer3;
        if (RealmsTextureManager.TEXTURES.containsKey(string1)) {
            final RealmsTexture a4 = (RealmsTexture)RealmsTextureManager.TEXTURES.get(string1);
            if (a4.image.equals(string2)) {
                return a4.textureId;
            }
            RenderSystem.deleteTexture(a4.textureId);
            integer3 = a4.textureId;
        }
        else {
            integer3 = GlStateManager._genTexture();
        }
        IntBuffer intBuffer4 = null;
        int integer4 = 0;
        int integer5 = 0;
        try {
            final InputStream inputStream8 = (InputStream)new ByteArrayInputStream(new Base64().decode(string2));
            BufferedImage bufferedImage7;
            try {
                bufferedImage7 = ImageIO.read(inputStream8);
            }
            finally {
                IOUtils.closeQuietly(inputStream8);
            }
            integer4 = bufferedImage7.getWidth();
            integer5 = bufferedImage7.getHeight();
            final int[] arr9 = new int[integer4 * integer5];
            bufferedImage7.getRGB(0, 0, integer4, integer5, arr9, 0, integer4);
            intBuffer4 = ByteBuffer.allocateDirect(4 * integer4 * integer5).order(ByteOrder.nativeOrder()).asIntBuffer();
            intBuffer4.put(arr9);
            intBuffer4.flip();
        }
        catch (IOException iOException7) {
            iOException7.printStackTrace();
        }
        RenderSystem.activeTexture(33984);
        RenderSystem.bindTexture(integer3);
        TextureUtil.initTexture(intBuffer4, integer4, integer5);
        RealmsTextureManager.TEXTURES.put(string1, new RealmsTexture(string2, integer3));
        return integer3;
    }
    
    static {
        TEXTURES = (Map)Maps.newHashMap();
        SKIN_FETCH_STATUS = (Map)Maps.newHashMap();
        FETCHED_SKINS = (Map)Maps.newHashMap();
        LOGGER = LogManager.getLogger();
        TEMPLATE_ICON_LOCATION = new ResourceLocation("textures/gui/presets/isles.png");
    }
    
    public static class RealmsTexture {
        private final String image;
        private final int textureId;
        
        public RealmsTexture(final String string, final int integer) {
            this.image = string;
            this.textureId = integer;
        }
    }
}

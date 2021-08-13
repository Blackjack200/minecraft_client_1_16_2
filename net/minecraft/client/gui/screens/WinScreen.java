package net.minecraft.client.gui.screens;

import org.apache.logging.log4j.LogManager;
import com.mojang.blaze3d.platform.GlStateManager;
import java.util.function.BiConsumer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.gui.GuiComponent;
import java.util.Iterator;
import java.io.InputStream;
import net.minecraft.server.packs.resources.Resource;
import java.io.Closeable;
import org.apache.commons.io.IOUtils;
import java.util.Collection;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import java.util.Random;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.chat.NarratorChatListener;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.util.FormattedCharSequence;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class WinScreen extends Screen {
    private static final Logger LOGGER;
    private static final ResourceLocation LOGO_LOCATION;
    private static final ResourceLocation EDITION_LOCATION;
    private static final ResourceLocation VIGNETTE_LOCATION;
    private static final String OBFUSCATE_TOKEN;
    private final boolean poem;
    private final Runnable onFinished;
    private float time;
    private List<FormattedCharSequence> lines;
    private IntSet centeredLines;
    private int totalScrollLength;
    private float scrollSpeed;
    
    public WinScreen(final boolean boolean1, final Runnable runnable) {
        super(NarratorChatListener.NO_TITLE);
        this.scrollSpeed = 0.5f;
        this.poem = boolean1;
        this.onFinished = runnable;
        if (!boolean1) {
            this.scrollSpeed = 0.75f;
        }
    }
    
    @Override
    public void tick() {
        this.minecraft.getMusicManager().tick();
        this.minecraft.getSoundManager().tick(false);
        final float float2 = (this.totalScrollLength + this.height + this.height + 24) / this.scrollSpeed;
        if (this.time > float2) {
            this.respawn();
        }
    }
    
    @Override
    public void onClose() {
        this.respawn();
    }
    
    private void respawn() {
        this.onFinished.run();
        this.minecraft.setScreen(null);
    }
    
    @Override
    protected void init() {
        if (this.lines != null) {
            return;
        }
        this.lines = (List<FormattedCharSequence>)Lists.newArrayList();
        this.centeredLines = (IntSet)new IntOpenHashSet();
        Resource ace2 = null;
        try {
            final int integer3 = 274;
            if (this.poem) {
                ace2 = this.minecraft.getResourceManager().getResource(new ResourceLocation("texts/end.txt"));
                final InputStream inputStream4 = ace2.getInputStream();
                final BufferedReader bufferedReader5 = new BufferedReader((Reader)new InputStreamReader(inputStream4, StandardCharsets.UTF_8));
                final Random random6 = new Random(8124371L);
                String string7;
                while ((string7 = bufferedReader5.readLine()) != null) {
                    int integer4;
                    String string8;
                    String string9;
                    for (string7 = string7.replaceAll("PLAYERNAME", this.minecraft.getUser().getName()); (integer4 = string7.indexOf(WinScreen.OBFUSCATE_TOKEN)) != -1; string7 = string8 + ChatFormatting.WHITE + ChatFormatting.OBFUSCATED + "XXXXXXXX".substring(0, random6.nextInt(4) + 3) + string9) {
                        string8 = string7.substring(0, integer4);
                        string9 = string7.substring(integer4 + WinScreen.OBFUSCATE_TOKEN.length());
                    }
                    this.lines.addAll((Collection)this.minecraft.font.split(new TextComponent(string7), 274));
                    this.lines.add(FormattedCharSequence.EMPTY);
                }
                inputStream4.close();
                for (int integer4 = 0; integer4 < 8; ++integer4) {
                    this.lines.add(FormattedCharSequence.EMPTY);
                }
            }
            final InputStream inputStream4 = this.minecraft.getResourceManager().getResource(new ResourceLocation("texts/credits.txt")).getInputStream();
            final BufferedReader bufferedReader5 = new BufferedReader((Reader)new InputStreamReader(inputStream4, StandardCharsets.UTF_8));
            String string10;
            while ((string10 = bufferedReader5.readLine()) != null) {
                string10 = string10.replaceAll("PLAYERNAME", this.minecraft.getUser().getName());
                string10 = string10.replaceAll("\t", "    ");
                boolean boolean7;
                if (string10.startsWith("[C]")) {
                    string10 = string10.substring(3);
                    boolean7 = true;
                }
                else {
                    boolean7 = false;
                }
                final List<FormattedCharSequence> list8 = this.minecraft.font.split(new TextComponent(string10), 274);
                for (final FormattedCharSequence aex10 : list8) {
                    if (boolean7) {
                        this.centeredLines.add(this.lines.size());
                    }
                    this.lines.add(aex10);
                }
                this.lines.add(FormattedCharSequence.EMPTY);
            }
            inputStream4.close();
            this.totalScrollLength = this.lines.size() * 12;
        }
        catch (Exception exception3) {
            WinScreen.LOGGER.error("Couldn't load credits", (Throwable)exception3);
        }
        finally {
            IOUtils.closeQuietly((Closeable)ace2);
        }
    }
    
    private void renderBg(final int integer1, final int integer2, final float float3) {
        this.minecraft.getTextureManager().bind(GuiComponent.BACKGROUND_LOCATION);
        final int integer3 = this.width;
        final float float4 = -this.time * 0.5f * this.scrollSpeed;
        final float float5 = this.height - this.time * 0.5f * this.scrollSpeed;
        final float float6 = 0.015625f;
        float float7 = this.time * 0.02f;
        final float float8 = (this.totalScrollLength + this.height + this.height + 24) / this.scrollSpeed;
        final float float9 = (float8 - 20.0f - this.time) * 0.005f;
        if (float9 < float7) {
            float7 = float9;
        }
        if (float7 > 1.0f) {
            float7 = 1.0f;
        }
        float7 *= float7;
        float7 = float7 * 96.0f / 255.0f;
        final Tesselator dfl12 = Tesselator.getInstance();
        final BufferBuilder dfe13 = dfl12.getBuilder();
        dfe13.begin(7, DefaultVertexFormat.POSITION_TEX_COLOR);
        dfe13.vertex(0.0, this.height, this.getBlitOffset()).uv(0.0f, float4 * 0.015625f).color(float7, float7, float7, 1.0f).endVertex();
        dfe13.vertex(integer3, this.height, this.getBlitOffset()).uv(integer3 * 0.015625f, float4 * 0.015625f).color(float7, float7, float7, 1.0f).endVertex();
        dfe13.vertex(integer3, 0.0, this.getBlitOffset()).uv(integer3 * 0.015625f, float5 * 0.015625f).color(float7, float7, float7, 1.0f).endVertex();
        dfe13.vertex(0.0, 0.0, this.getBlitOffset()).uv(0.0f, float5 * 0.015625f).color(float7, float7, float7, 1.0f).endVertex();
        dfl12.end();
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBg(integer2, integer3, float4);
        final int integer4 = 274;
        final int integer5 = this.width / 2 - 137;
        final int integer6 = this.height + 50;
        this.time += float4;
        final float float5 = -this.time * this.scrollSpeed;
        RenderSystem.pushMatrix();
        RenderSystem.translatef(0.0f, float5, 0.0f);
        this.minecraft.getTextureManager().bind(WinScreen.LOGO_LOCATION);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.enableAlphaTest();
        RenderSystem.enableBlend();
        this.blitOutlineBlack(integer5, integer6, (BiConsumer<Integer, Integer>)((integer2, integer3) -> {
            this.blit(dfj, integer2 + 0, integer3, 0, 0, 155, 44);
            this.blit(dfj, integer2 + 155, integer3, 0, 45, 155, 44);
        }));
        RenderSystem.disableBlend();
        this.minecraft.getTextureManager().bind(WinScreen.EDITION_LOCATION);
        GuiComponent.blit(dfj, integer5 + 88, integer6 + 37, 0.0f, 0.0f, 98, 14, 128, 16);
        RenderSystem.disableAlphaTest();
        int integer7 = integer6 + 100;
        for (int integer8 = 0; integer8 < this.lines.size(); ++integer8) {
            if (integer8 == this.lines.size() - 1) {
                final float float6 = integer7 + float5 - (this.height / 2 - 6);
                if (float6 < 0.0f) {
                    RenderSystem.translatef(0.0f, -float6, 0.0f);
                }
            }
            if (integer7 + float5 + 12.0f + 8.0f > 0.0f && integer7 + float5 < this.height) {
                final FormattedCharSequence aex12 = (FormattedCharSequence)this.lines.get(integer8);
                if (this.centeredLines.contains(integer8)) {
                    this.font.drawShadow(dfj, aex12, (float)(integer5 + (274 - this.font.width(aex12)) / 2), (float)integer7, 16777215);
                }
                else {
                    this.font.random.setSeed((long)(integer8 * 4238972211L + this.time / 4.0f));
                    this.font.drawShadow(dfj, aex12, (float)integer5, (float)integer7, 16777215);
                }
            }
            integer7 += 12;
        }
        RenderSystem.popMatrix();
        this.minecraft.getTextureManager().bind(WinScreen.VIGNETTE_LOCATION);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR);
        int integer8 = this.width;
        final int integer9 = this.height;
        final Tesselator dfl13 = Tesselator.getInstance();
        final BufferBuilder dfe14 = dfl13.getBuilder();
        dfe14.begin(7, DefaultVertexFormat.POSITION_TEX_COLOR);
        dfe14.vertex(0.0, integer9, this.getBlitOffset()).uv(0.0f, 1.0f).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        dfe14.vertex(integer8, integer9, this.getBlitOffset()).uv(1.0f, 1.0f).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        dfe14.vertex(integer8, 0.0, this.getBlitOffset()).uv(1.0f, 0.0f).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        dfe14.vertex(0.0, 0.0, this.getBlitOffset()).uv(0.0f, 0.0f).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        dfl13.end();
        RenderSystem.disableBlend();
        super.render(dfj, integer2, integer3, float4);
    }
    
    static {
        LOGGER = LogManager.getLogger();
        LOGO_LOCATION = new ResourceLocation("textures/gui/title/minecraft.png");
        EDITION_LOCATION = new ResourceLocation("textures/gui/title/edition.png");
        VIGNETTE_LOCATION = new ResourceLocation("textures/misc/vignette.png");
        OBFUSCATE_TOKEN = new StringBuilder().append("").append(ChatFormatting.WHITE).append(ChatFormatting.OBFUSCATED).append(ChatFormatting.GREEN).append(ChatFormatting.AQUA).toString();
    }
}

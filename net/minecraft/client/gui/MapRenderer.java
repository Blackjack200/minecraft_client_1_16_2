package net.minecraft.client.gui;

import net.minecraft.network.chat.Component;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.client.Minecraft;
import com.mojang.math.Vector3f;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.client.renderer.texture.DynamicTexture;
import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class MapRenderer implements AutoCloseable {
    private static final ResourceLocation MAP_ICONS_LOCATION;
    private static final RenderType MAP_ICONS;
    private final TextureManager textureManager;
    private final Map<String, MapInstance> maps;
    
    public MapRenderer(final TextureManager ejv) {
        this.maps = (Map<String, MapInstance>)Maps.newHashMap();
        this.textureManager = ejv;
    }
    
    public void update(final MapItemSavedData cxu) {
        this.getMapInstance(cxu).updateTexture();
    }
    
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final MapItemSavedData cxu, final boolean boolean4, final int integer) {
        this.getMapInstance(cxu).draw(dfj, dzy, boolean4, integer);
    }
    
    private MapInstance getMapInstance(final MapItemSavedData cxu) {
        MapInstance a3 = (MapInstance)this.maps.get(cxu.getId());
        if (a3 == null) {
            a3 = new MapInstance(cxu);
            this.maps.put(cxu.getId(), a3);
        }
        return a3;
    }
    
    @Nullable
    public MapInstance getMapInstanceIfExists(final String string) {
        return (MapInstance)this.maps.get(string);
    }
    
    public void resetData() {
        for (final MapInstance a3 : this.maps.values()) {
            a3.close();
        }
        this.maps.clear();
    }
    
    @Nullable
    public MapItemSavedData getData(@Nullable final MapInstance a) {
        if (a != null) {
            return a.data;
        }
        return null;
    }
    
    public void close() {
        this.resetData();
    }
    
    static {
        MAP_ICONS_LOCATION = new ResourceLocation("textures/map/map_icons.png");
        MAP_ICONS = RenderType.text(MapRenderer.MAP_ICONS_LOCATION);
    }
    
    class MapInstance implements AutoCloseable {
        private final MapItemSavedData data;
        private final DynamicTexture texture;
        private final RenderType renderType;
        
        private MapInstance(final MapItemSavedData cxu) {
            this.data = cxu;
            this.texture = new DynamicTexture(128, 128, true);
            final ResourceLocation vk4 = MapRenderer.this.textureManager.register("map/" + cxu.getId(), this.texture);
            this.renderType = RenderType.text(vk4);
        }
        
        private void updateTexture() {
            for (int integer2 = 0; integer2 < 128; ++integer2) {
                for (int integer3 = 0; integer3 < 128; ++integer3) {
                    final int integer4 = integer3 + integer2 * 128;
                    final int integer5 = this.data.colors[integer4] & 0xFF;
                    if (integer5 / 4 == 0) {
                        this.texture.getPixels().setPixelRGBA(integer3, integer2, 0);
                    }
                    else {
                        this.texture.getPixels().setPixelRGBA(integer3, integer2, MaterialColor.MATERIAL_COLORS[integer5 / 4].calculateRGBColor(integer5 & 0x3));
                    }
                }
            }
            this.texture.upload();
        }
        
        private void draw(final PoseStack dfj, final MultiBufferSource dzy, final boolean boolean3, final int integer) {
            final int integer2 = 0;
            final int integer3 = 0;
            final float float8 = 0.0f;
            final Matrix4f b9 = dfj.last().pose();
            final VertexConsumer dfn10 = dzy.getBuffer(this.renderType);
            dfn10.vertex(b9, 0.0f, 128.0f, -0.01f).color(255, 255, 255, 255).uv(0.0f, 1.0f).uv2(integer).endVertex();
            dfn10.vertex(b9, 128.0f, 128.0f, -0.01f).color(255, 255, 255, 255).uv(1.0f, 1.0f).uv2(integer).endVertex();
            dfn10.vertex(b9, 128.0f, 0.0f, -0.01f).color(255, 255, 255, 255).uv(1.0f, 0.0f).uv2(integer).endVertex();
            dfn10.vertex(b9, 0.0f, 0.0f, -0.01f).color(255, 255, 255, 255).uv(0.0f, 0.0f).uv2(integer).endVertex();
            int integer4 = 0;
            for (final MapDecoration cxr13 : this.data.decorations.values()) {
                if (boolean3 && !cxr13.renderOnFrame()) {
                    continue;
                }
                dfj.pushPose();
                dfj.translate(0.0f + cxr13.getX() / 2.0f + 64.0f, 0.0f + cxr13.getY() / 2.0f + 64.0f, -0.019999999552965164);
                dfj.mulPose(Vector3f.ZP.rotationDegrees(cxr13.getRot() * 360 / 16.0f));
                dfj.scale(4.0f, 4.0f, 3.0f);
                dfj.translate(-0.125, 0.125, 0.0);
                final byte byte14 = cxr13.getImage();
                final float float9 = (byte14 % 16 + 0) / 16.0f;
                final float float10 = (byte14 / 16 + 0) / 16.0f;
                final float float11 = (byte14 % 16 + 1) / 16.0f;
                final float float12 = (byte14 / 16 + 1) / 16.0f;
                final Matrix4f b10 = dfj.last().pose();
                final float float13 = -0.001f;
                final VertexConsumer dfn11 = dzy.getBuffer(MapRenderer.MAP_ICONS);
                dfn11.vertex(b10, -1.0f, 1.0f, integer4 * -0.001f).color(255, 255, 255, 255).uv(float9, float10).uv2(integer).endVertex();
                dfn11.vertex(b10, 1.0f, 1.0f, integer4 * -0.001f).color(255, 255, 255, 255).uv(float11, float10).uv2(integer).endVertex();
                dfn11.vertex(b10, 1.0f, -1.0f, integer4 * -0.001f).color(255, 255, 255, 255).uv(float11, float12).uv2(integer).endVertex();
                dfn11.vertex(b10, -1.0f, -1.0f, integer4 * -0.001f).color(255, 255, 255, 255).uv(float9, float12).uv2(integer).endVertex();
                dfj.popPose();
                if (cxr13.getName() != null) {
                    final Font dkr22 = Minecraft.getInstance().font;
                    final Component nr23 = cxr13.getName();
                    final float float14 = (float)dkr22.width(nr23);
                    final float float16 = 25.0f / float14;
                    final float float17 = 0.0f;
                    final float n = 6.0f;
                    dkr22.getClass();
                    final float float15 = Mth.clamp(float16, float17, n / 9.0f);
                    dfj.pushPose();
                    dfj.translate(0.0f + cxr13.getX() / 2.0f + 64.0f - float14 * float15 / 2.0f, 0.0f + cxr13.getY() / 2.0f + 64.0f + 4.0f, -0.02500000037252903);
                    dfj.scale(float15, float15, 1.0f);
                    dfj.translate(0.0, 0.0, -0.10000000149011612);
                    dkr22.drawInBatch(nr23, 0.0f, 0.0f, -1, false, dfj.last().pose(), dzy, false, Integer.MIN_VALUE, integer);
                    dfj.popPose();
                }
                ++integer4;
            }
        }
        
        public void close() {
            this.texture.close();
        }
    }
}

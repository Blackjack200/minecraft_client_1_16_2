package net.minecraft.client.renderer.debug;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import java.util.Map;

public class GameTestDebugRenderer implements DebugRenderer.SimpleDebugRenderer {
    private final Map<BlockPos, Marker> markers;
    
    public GameTestDebugRenderer() {
        this.markers = (Map<BlockPos, Marker>)Maps.newHashMap();
    }
    
    public void addMarker(final BlockPos fx, final int integer2, final String string, final int integer4) {
        this.markers.put(fx, new Marker(integer2, string, Util.getMillis() + integer4));
    }
    
    public void clear() {
        this.markers.clear();
    }
    
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final double double3, final double double4, final double double5) {
        final long long10 = Util.getMillis();
        this.markers.entrySet().removeIf(entry -> long10 > ((Marker)entry.getValue()).removeAtTime);
        this.markers.forEach(this::renderMarker);
    }
    
    private void renderMarker(final BlockPos fx, final Marker a) {
        RenderSystem.pushMatrix();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.color4f(0.0f, 1.0f, 0.0f, 0.75f);
        RenderSystem.disableTexture();
        DebugRenderer.renderFilledBox(fx, 0.02f, a.getR(), a.getG(), a.getB(), a.getA());
        if (!a.text.isEmpty()) {
            final double double4 = fx.getX() + 0.5;
            final double double5 = fx.getY() + 1.2;
            final double double6 = fx.getZ() + 0.5;
            DebugRenderer.renderFloatingText(a.text, double4, double5, double6, -1, 0.01f, true, 0.0f, true);
        }
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        RenderSystem.popMatrix();
    }
    
    static class Marker {
        public int color;
        public String text;
        public long removeAtTime;
        
        public Marker(final int integer, final String string, final long long3) {
            this.color = integer;
            this.text = string;
            this.removeAtTime = long3;
        }
        
        public float getR() {
            return (this.color >> 16 & 0xFF) / 255.0f;
        }
        
        public float getG() {
            return (this.color >> 8 & 0xFF) / 255.0f;
        }
        
        public float getB() {
            return (this.color & 0xFF) / 255.0f;
        }
        
        public float getA() {
            return (this.color >> 24 & 0xFF) / 255.0f;
        }
    }
}

package net.minecraft.client.renderer.texture;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;

public class OverlayTexture implements AutoCloseable {
    public static final int NO_OVERLAY;
    private final DynamicTexture texture;
    
    public OverlayTexture() {
        this.texture = new DynamicTexture(16, 16, false);
        final NativeImage deq2 = this.texture.getPixels();
        for (int integer3 = 0; integer3 < 16; ++integer3) {
            for (int integer4 = 0; integer4 < 16; ++integer4) {
                if (integer3 < 8) {
                    deq2.setPixelRGBA(integer4, integer3, -1308622593);
                }
                else {
                    final int integer5 = (int)((1.0f - integer4 / 15.0f * 0.75f) * 255.0f);
                    deq2.setPixelRGBA(integer4, integer3, integer5 << 24 | 0xFFFFFF);
                }
            }
        }
        RenderSystem.activeTexture(33985);
        this.texture.bind();
        RenderSystem.matrixMode(5890);
        RenderSystem.loadIdentity();
        final float float3 = 0.06666667f;
        RenderSystem.scalef(0.06666667f, 0.06666667f, 0.06666667f);
        RenderSystem.matrixMode(5888);
        this.texture.bind();
        deq2.upload(0, 0, 0, 0, 0, deq2.getWidth(), deq2.getHeight(), false, true, false, false);
        RenderSystem.activeTexture(33984);
    }
    
    public void close() {
        this.texture.close();
    }
    
    public void setupOverlayColor() {
        RenderSystem.setupOverlayColor(this.texture::getId, 16);
    }
    
    public static int u(final float float1) {
        return (int)(float1 * 15.0f);
    }
    
    public static int v(final boolean boolean1) {
        return boolean1 ? 3 : 10;
    }
    
    public static int pack(final int integer1, final int integer2) {
        return integer1 | integer2 << 16;
    }
    
    public static int pack(final float float1, final boolean boolean2) {
        return pack(u(float1), v(boolean2));
    }
    
    public void teardownOverlayColor() {
        RenderSystem.teardownOverlayColor();
    }
    
    static {
        NO_OVERLAY = pack(0, 10);
    }
}

package net.minecraft.client.renderer.texture;

import java.util.function.Consumer;
import net.minecraft.Util;
import com.mojang.blaze3d.platform.NativeImage;

public class MipmapGenerator {
    private static final float[] POW22;
    
    public static NativeImage[] generateMipLevels(final NativeImage deq, final int integer) {
        final NativeImage[] arr3 = new NativeImage[integer + 1];
        arr3[0] = deq;
        if (integer > 0) {
            boolean boolean4 = false;
        Label_0072:
            for (int integer2 = 0; integer2 < deq.getWidth(); ++integer2) {
                for (int integer3 = 0; integer3 < deq.getHeight(); ++integer3) {
                    if (deq.getPixelRGBA(integer2, integer3) >> 24 == 0) {
                        boolean4 = true;
                        break Label_0072;
                    }
                }
            }
            for (int integer2 = 1; integer2 <= integer; ++integer2) {
                final NativeImage deq2 = arr3[integer2 - 1];
                final NativeImage deq3 = new NativeImage(deq2.getWidth() >> 1, deq2.getHeight() >> 1, false);
                final int integer4 = deq3.getWidth();
                final int integer5 = deq3.getHeight();
                for (int integer6 = 0; integer6 < integer4; ++integer6) {
                    for (int integer7 = 0; integer7 < integer5; ++integer7) {
                        deq3.setPixelRGBA(integer6, integer7, alphaBlend(deq2.getPixelRGBA(integer6 * 2 + 0, integer7 * 2 + 0), deq2.getPixelRGBA(integer6 * 2 + 1, integer7 * 2 + 0), deq2.getPixelRGBA(integer6 * 2 + 0, integer7 * 2 + 1), deq2.getPixelRGBA(integer6 * 2 + 1, integer7 * 2 + 1), boolean4));
                    }
                }
                arr3[integer2] = deq3;
            }
        }
        return arr3;
    }
    
    private static int alphaBlend(final int integer1, final int integer2, final int integer3, final int integer4, final boolean boolean5) {
        if (boolean5) {
            float float6 = 0.0f;
            float float7 = 0.0f;
            float float8 = 0.0f;
            float float9 = 0.0f;
            if (integer1 >> 24 != 0) {
                float6 += getPow22(integer1 >> 24);
                float7 += getPow22(integer1 >> 16);
                float8 += getPow22(integer1 >> 8);
                float9 += getPow22(integer1 >> 0);
            }
            if (integer2 >> 24 != 0) {
                float6 += getPow22(integer2 >> 24);
                float7 += getPow22(integer2 >> 16);
                float8 += getPow22(integer2 >> 8);
                float9 += getPow22(integer2 >> 0);
            }
            if (integer3 >> 24 != 0) {
                float6 += getPow22(integer3 >> 24);
                float7 += getPow22(integer3 >> 16);
                float8 += getPow22(integer3 >> 8);
                float9 += getPow22(integer3 >> 0);
            }
            if (integer4 >> 24 != 0) {
                float6 += getPow22(integer4 >> 24);
                float7 += getPow22(integer4 >> 16);
                float8 += getPow22(integer4 >> 8);
                float9 += getPow22(integer4 >> 0);
            }
            float6 /= 4.0f;
            float7 /= 4.0f;
            float8 /= 4.0f;
            float9 /= 4.0f;
            int integer5 = (int)(Math.pow((double)float6, 0.45454545454545453) * 255.0);
            final int integer6 = (int)(Math.pow((double)float7, 0.45454545454545453) * 255.0);
            final int integer7 = (int)(Math.pow((double)float8, 0.45454545454545453) * 255.0);
            final int integer8 = (int)(Math.pow((double)float9, 0.45454545454545453) * 255.0);
            if (integer5 < 96) {
                integer5 = 0;
            }
            return integer5 << 24 | integer6 << 16 | integer7 << 8 | integer8;
        }
        final int integer9 = gammaBlend(integer1, integer2, integer3, integer4, 24);
        final int integer10 = gammaBlend(integer1, integer2, integer3, integer4, 16);
        final int integer11 = gammaBlend(integer1, integer2, integer3, integer4, 8);
        final int integer12 = gammaBlend(integer1, integer2, integer3, integer4, 0);
        return integer9 << 24 | integer10 << 16 | integer11 << 8 | integer12;
    }
    
    private static int gammaBlend(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5) {
        final float float6 = getPow22(integer1 >> integer5);
        final float float7 = getPow22(integer2 >> integer5);
        final float float8 = getPow22(integer3 >> integer5);
        final float float9 = getPow22(integer4 >> integer5);
        final float float10 = (float)Math.pow((float6 + float7 + float8 + float9) * 0.25, 0.45454545454545453);
        return (int)(float10 * 255.0);
    }
    
    private static float getPow22(final int integer) {
        return MipmapGenerator.POW22[integer & 0xFF];
    }
    
    static {
        POW22 = Util.<float[]>make(new float[256], (java.util.function.Consumer<float[]>)(arr -> {
            for (int integer2 = 0; integer2 < arr.length; ++integer2) {
                arr[integer2] = (float)Math.pow((double)(integer2 / 255.0f), 2.2);
            }
        }));
    }
}

package com.mojang.realmsclient.util;

import javax.annotation.Nullable;
import java.awt.Graphics;
import java.awt.image.DataBufferInt;
import java.awt.Color;
import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class SkinProcessor {
    private int[] pixels;
    private int width;
    private int height;
    
    @Nullable
    public BufferedImage process(final BufferedImage bufferedImage) {
        if (bufferedImage == null) {
            return null;
        }
        this.width = 64;
        this.height = 64;
        final BufferedImage bufferedImage2 = new BufferedImage(this.width, this.height, 2);
        final Graphics graphics4 = bufferedImage2.getGraphics();
        graphics4.drawImage((Image)bufferedImage, 0, 0, (ImageObserver)null);
        final boolean boolean5 = bufferedImage.getHeight() == 32;
        if (boolean5) {
            graphics4.setColor(new Color(0, 0, 0, 0));
            graphics4.fillRect(0, 32, 64, 32);
            graphics4.drawImage((Image)bufferedImage2, 24, 48, 20, 52, 4, 16, 8, 20, (ImageObserver)null);
            graphics4.drawImage((Image)bufferedImage2, 28, 48, 24, 52, 8, 16, 12, 20, (ImageObserver)null);
            graphics4.drawImage((Image)bufferedImage2, 20, 52, 16, 64, 8, 20, 12, 32, (ImageObserver)null);
            graphics4.drawImage((Image)bufferedImage2, 24, 52, 20, 64, 4, 20, 8, 32, (ImageObserver)null);
            graphics4.drawImage((Image)bufferedImage2, 28, 52, 24, 64, 0, 20, 4, 32, (ImageObserver)null);
            graphics4.drawImage((Image)bufferedImage2, 32, 52, 28, 64, 12, 20, 16, 32, (ImageObserver)null);
            graphics4.drawImage((Image)bufferedImage2, 40, 48, 36, 52, 44, 16, 48, 20, (ImageObserver)null);
            graphics4.drawImage((Image)bufferedImage2, 44, 48, 40, 52, 48, 16, 52, 20, (ImageObserver)null);
            graphics4.drawImage((Image)bufferedImage2, 36, 52, 32, 64, 48, 20, 52, 32, (ImageObserver)null);
            graphics4.drawImage((Image)bufferedImage2, 40, 52, 36, 64, 44, 20, 48, 32, (ImageObserver)null);
            graphics4.drawImage((Image)bufferedImage2, 44, 52, 40, 64, 40, 20, 44, 32, (ImageObserver)null);
            graphics4.drawImage((Image)bufferedImage2, 48, 52, 44, 64, 52, 20, 56, 32, (ImageObserver)null);
        }
        graphics4.dispose();
        this.pixels = ((DataBufferInt)bufferedImage2.getRaster().getDataBuffer()).getData();
        this.setNoAlpha(0, 0, 32, 16);
        if (boolean5) {
            this.doLegacyTransparencyHack(32, 0, 64, 32);
        }
        this.setNoAlpha(0, 16, 64, 32);
        this.setNoAlpha(16, 48, 48, 64);
        return bufferedImage2;
    }
    
    private void doLegacyTransparencyHack(final int integer1, final int integer2, final int integer3, final int integer4) {
        for (int integer5 = integer1; integer5 < integer3; ++integer5) {
            for (int integer6 = integer2; integer6 < integer4; ++integer6) {
                final int integer7 = this.pixels[integer5 + integer6 * this.width];
                if ((integer7 >> 24 & 0xFF) < 128) {
                    return;
                }
            }
        }
        for (int integer5 = integer1; integer5 < integer3; ++integer5) {
            for (int integer6 = integer2; integer6 < integer4; ++integer6) {
                final int[] pixels = this.pixels;
                final int n = integer5 + integer6 * this.width;
                pixels[n] &= 0xFFFFFF;
            }
        }
    }
    
    private void setNoAlpha(final int integer1, final int integer2, final int integer3, final int integer4) {
        for (int integer5 = integer1; integer5 < integer3; ++integer5) {
            for (int integer6 = integer2; integer6 < integer4; ++integer6) {
                final int[] pixels = this.pixels;
                final int n = integer5 + integer6 * this.width;
                pixels[n] |= 0xFF000000;
            }
        }
    }
}

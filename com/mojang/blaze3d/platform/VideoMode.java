package com.mojang.blaze3d.platform;

import java.util.regex.Matcher;
import java.util.Optional;
import javax.annotation.Nullable;
import java.util.Objects;
import org.lwjgl.glfw.GLFWVidMode;
import java.util.regex.Pattern;

public final class VideoMode {
    private final int width;
    private final int height;
    private final int redBits;
    private final int greenBits;
    private final int blueBits;
    private final int refreshRate;
    private static final Pattern PATTERN;
    
    public VideoMode(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6) {
        this.width = integer1;
        this.height = integer2;
        this.redBits = integer3;
        this.greenBits = integer4;
        this.blueBits = integer5;
        this.refreshRate = integer6;
    }
    
    public VideoMode(final GLFWVidMode.Buffer buffer) {
        this.width = buffer.width();
        this.height = buffer.height();
        this.redBits = buffer.redBits();
        this.greenBits = buffer.greenBits();
        this.blueBits = buffer.blueBits();
        this.refreshRate = buffer.refreshRate();
    }
    
    public VideoMode(final GLFWVidMode gLFWVidMode) {
        this.width = gLFWVidMode.width();
        this.height = gLFWVidMode.height();
        this.redBits = gLFWVidMode.redBits();
        this.greenBits = gLFWVidMode.greenBits();
        this.blueBits = gLFWVidMode.blueBits();
        this.refreshRate = gLFWVidMode.refreshRate();
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public int getRedBits() {
        return this.redBits;
    }
    
    public int getGreenBits() {
        return this.greenBits;
    }
    
    public int getBlueBits() {
        return this.blueBits;
    }
    
    public int getRefreshRate() {
        return this.refreshRate;
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        final VideoMode dev3 = (VideoMode)object;
        return this.width == dev3.width && this.height == dev3.height && this.redBits == dev3.redBits && this.greenBits == dev3.greenBits && this.blueBits == dev3.blueBits && this.refreshRate == dev3.refreshRate;
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.width, this.height, this.redBits, this.greenBits, this.blueBits, this.refreshRate });
    }
    
    public String toString() {
        return String.format("%sx%s@%s (%sbit)", new Object[] { this.width, this.height, this.refreshRate, this.redBits + this.greenBits + this.blueBits });
    }
    
    public static Optional<VideoMode> read(@Nullable final String string) {
        if (string == null) {
            return (Optional<VideoMode>)Optional.empty();
        }
        try {
            final Matcher matcher2 = VideoMode.PATTERN.matcher((CharSequence)string);
            if (matcher2.matches()) {
                final int integer3 = Integer.parseInt(matcher2.group(1));
                final int integer4 = Integer.parseInt(matcher2.group(2));
                final String string2 = matcher2.group(3);
                int integer5;
                if (string2 == null) {
                    integer5 = 60;
                }
                else {
                    integer5 = Integer.parseInt(string2);
                }
                final String string3 = matcher2.group(4);
                int integer6;
                if (string3 == null) {
                    integer6 = 24;
                }
                else {
                    integer6 = Integer.parseInt(string3);
                }
                final int integer7 = integer6 / 3;
                return (Optional<VideoMode>)Optional.of(new VideoMode(integer3, integer4, integer7, integer7, integer7, integer5));
            }
        }
        catch (Exception ex) {}
        return (Optional<VideoMode>)Optional.empty();
    }
    
    public String write() {
        return String.format("%sx%s@%s:%s", new Object[] { this.width, this.height, this.refreshRate, this.redBits + this.greenBits + this.blueBits });
    }
    
    static {
        PATTERN = Pattern.compile("(\\d+)x(\\d+)(?:@(\\d+)(?::(\\d+))?)?");
    }
}

package com.mojang.blaze3d.shaders;

import org.apache.logging.log4j.LogManager;
import com.mojang.math.Matrix4f;
import java.nio.Buffer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.platform.GlStateManager;
import org.lwjgl.system.MemoryUtil;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.apache.logging.log4j.Logger;

public class Uniform extends AbstractUniform implements AutoCloseable {
    private static final Logger LOGGER;
    private int location;
    private final int count;
    private final int type;
    private final IntBuffer intValues;
    private final FloatBuffer floatValues;
    private final String name;
    private boolean dirty;
    private final Effect parent;
    
    public Uniform(final String string, final int integer2, final int integer3, final Effect dfa) {
        this.name = string;
        this.count = integer3;
        this.type = integer2;
        this.parent = dfa;
        if (integer2 <= 3) {
            this.intValues = MemoryUtil.memAllocInt(integer3);
            this.floatValues = null;
        }
        else {
            this.intValues = null;
            this.floatValues = MemoryUtil.memAllocFloat(integer3);
        }
        this.location = -1;
        this.markDirty();
    }
    
    public static int glGetUniformLocation(final int integer, final CharSequence charSequence) {
        return GlStateManager._glGetUniformLocation(integer, charSequence);
    }
    
    public static void uploadInteger(final int integer1, final int integer2) {
        RenderSystem.glUniform1i(integer1, integer2);
    }
    
    public static int glGetAttribLocation(final int integer, final CharSequence charSequence) {
        return GlStateManager._glGetAttribLocation(integer, charSequence);
    }
    
    public void close() {
        if (this.intValues != null) {
            MemoryUtil.memFree((Buffer)this.intValues);
        }
        if (this.floatValues != null) {
            MemoryUtil.memFree((Buffer)this.floatValues);
        }
    }
    
    private void markDirty() {
        this.dirty = true;
        if (this.parent != null) {
            this.parent.markDirty();
        }
    }
    
    public static int getTypeFromString(final String string) {
        int integer2 = -1;
        if ("int".equals(string)) {
            integer2 = 0;
        }
        else if ("float".equals(string)) {
            integer2 = 4;
        }
        else if (string.startsWith("matrix")) {
            if (string.endsWith("2x2")) {
                integer2 = 8;
            }
            else if (string.endsWith("3x3")) {
                integer2 = 9;
            }
            else if (string.endsWith("4x4")) {
                integer2 = 10;
            }
        }
        return integer2;
    }
    
    public void setLocation(final int integer) {
        this.location = integer;
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public void set(final float float1) {
        this.floatValues.position(0);
        this.floatValues.put(0, float1);
        this.markDirty();
    }
    
    @Override
    public void set(final float float1, final float float2) {
        this.floatValues.position(0);
        this.floatValues.put(0, float1);
        this.floatValues.put(1, float2);
        this.markDirty();
    }
    
    @Override
    public void set(final float float1, final float float2, final float float3) {
        this.floatValues.position(0);
        this.floatValues.put(0, float1);
        this.floatValues.put(1, float2);
        this.floatValues.put(2, float3);
        this.markDirty();
    }
    
    @Override
    public void set(final float float1, final float float2, final float float3, final float float4) {
        this.floatValues.position(0);
        this.floatValues.put(float1);
        this.floatValues.put(float2);
        this.floatValues.put(float3);
        this.floatValues.put(float4);
        this.floatValues.flip();
        this.markDirty();
    }
    
    @Override
    public void setSafe(final float float1, final float float2, final float float3, final float float4) {
        this.floatValues.position(0);
        if (this.type >= 4) {
            this.floatValues.put(0, float1);
        }
        if (this.type >= 5) {
            this.floatValues.put(1, float2);
        }
        if (this.type >= 6) {
            this.floatValues.put(2, float3);
        }
        if (this.type >= 7) {
            this.floatValues.put(3, float4);
        }
        this.markDirty();
    }
    
    @Override
    public void setSafe(final int integer1, final int integer2, final int integer3, final int integer4) {
        this.intValues.position(0);
        if (this.type >= 0) {
            this.intValues.put(0, integer1);
        }
        if (this.type >= 1) {
            this.intValues.put(1, integer2);
        }
        if (this.type >= 2) {
            this.intValues.put(2, integer3);
        }
        if (this.type >= 3) {
            this.intValues.put(3, integer4);
        }
        this.markDirty();
    }
    
    @Override
    public void set(final float[] arr) {
        if (arr.length < this.count) {
            Uniform.LOGGER.warn("Uniform.set called with a too-small value array (expected {}, got {}). Ignoring.", this.count, arr.length);
            return;
        }
        this.floatValues.position(0);
        this.floatValues.put(arr);
        this.floatValues.position(0);
        this.markDirty();
    }
    
    @Override
    public void set(final Matrix4f b) {
        this.floatValues.position(0);
        b.store(this.floatValues);
        this.markDirty();
    }
    
    public void upload() {
        if (!this.dirty) {}
        this.dirty = false;
        if (this.type <= 3) {
            this.uploadAsInteger();
        }
        else if (this.type <= 7) {
            this.uploadAsFloat();
        }
        else {
            if (this.type > 10) {
                Uniform.LOGGER.warn("Uniform.upload called, but type value ({}) is not a valid type. Ignoring.", this.type);
                return;
            }
            this.uploadAsMatrix();
        }
    }
    
    private void uploadAsInteger() {
        this.floatValues.clear();
        switch (this.type) {
            case 0: {
                RenderSystem.glUniform1(this.location, this.intValues);
                break;
            }
            case 1: {
                RenderSystem.glUniform2(this.location, this.intValues);
                break;
            }
            case 2: {
                RenderSystem.glUniform3(this.location, this.intValues);
                break;
            }
            case 3: {
                RenderSystem.glUniform4(this.location, this.intValues);
                break;
            }
            default: {
                Uniform.LOGGER.warn("Uniform.upload called, but count value ({}) is  not in the range of 1 to 4. Ignoring.", this.count);
                break;
            }
        }
    }
    
    private void uploadAsFloat() {
        this.floatValues.clear();
        switch (this.type) {
            case 4: {
                RenderSystem.glUniform1(this.location, this.floatValues);
                break;
            }
            case 5: {
                RenderSystem.glUniform2(this.location, this.floatValues);
                break;
            }
            case 6: {
                RenderSystem.glUniform3(this.location, this.floatValues);
                break;
            }
            case 7: {
                RenderSystem.glUniform4(this.location, this.floatValues);
                break;
            }
            default: {
                Uniform.LOGGER.warn("Uniform.upload called, but count value ({}) is not in the range of 1 to 4. Ignoring.", this.count);
                break;
            }
        }
    }
    
    private void uploadAsMatrix() {
        this.floatValues.clear();
        switch (this.type) {
            case 8: {
                RenderSystem.glUniformMatrix2(this.location, false, this.floatValues);
                break;
            }
            case 9: {
                RenderSystem.glUniformMatrix3(this.location, false, this.floatValues);
                break;
            }
            case 10: {
                RenderSystem.glUniformMatrix4(this.location, false, this.floatValues);
                break;
            }
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}

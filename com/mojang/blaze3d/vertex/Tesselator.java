package com.mojang.blaze3d.vertex;

import java.util.function.Supplier;
import com.mojang.blaze3d.systems.RenderSystem;

public class Tesselator {
    private final BufferBuilder builder;
    private static final Tesselator INSTANCE;
    
    public static Tesselator getInstance() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnGameThreadOrInit);
        return Tesselator.INSTANCE;
    }
    
    public Tesselator(final int integer) {
        this.builder = new BufferBuilder(integer);
    }
    
    public Tesselator() {
        this(2097152);
    }
    
    public void end() {
        this.builder.end();
        BufferUploader.end(this.builder);
    }
    
    public BufferBuilder getBuilder() {
        return this.builder;
    }
    
    static {
        INSTANCE = new Tesselator();
    }
}

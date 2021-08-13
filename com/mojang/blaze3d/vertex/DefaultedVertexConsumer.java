package com.mojang.blaze3d.vertex;

public abstract class DefaultedVertexConsumer implements VertexConsumer {
    protected boolean defaultColorSet;
    protected int defaultR;
    protected int defaultG;
    protected int defaultB;
    protected int defaultA;
    
    public DefaultedVertexConsumer() {
        this.defaultColorSet = false;
        this.defaultR = 255;
        this.defaultG = 255;
        this.defaultB = 255;
        this.defaultA = 255;
    }
    
    public void defaultColor(final int integer1, final int integer2, final int integer3, final int integer4) {
        this.defaultR = integer1;
        this.defaultG = integer2;
        this.defaultB = integer3;
        this.defaultA = integer4;
        this.defaultColorSet = true;
    }
}

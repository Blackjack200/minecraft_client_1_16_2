package net.minecraft.client.renderer.block.model;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

public class BakedQuad {
    protected final int[] vertices;
    protected final int tintIndex;
    protected final Direction direction;
    protected final TextureAtlasSprite sprite;
    private final boolean shade;
    
    public BakedQuad(final int[] arr, final int integer, final Direction gc, final TextureAtlasSprite eju, final boolean boolean5) {
        this.vertices = arr;
        this.tintIndex = integer;
        this.direction = gc;
        this.sprite = eju;
        this.shade = boolean5;
    }
    
    public int[] getVertices() {
        return this.vertices;
    }
    
    public boolean isTinted() {
        return this.tintIndex != -1;
    }
    
    public int getTintIndex() {
        return this.tintIndex;
    }
    
    public Direction getDirection() {
        return this.direction;
    }
    
    public boolean isShade() {
        return this.shade;
    }
}

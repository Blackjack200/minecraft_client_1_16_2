package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public abstract class TextureSheetParticle extends SingleQuadParticle {
    protected TextureAtlasSprite sprite;
    
    protected TextureSheetParticle(final ClientLevel dwl, final double double2, final double double3, final double double4) {
        super(dwl, double2, double3, double4);
    }
    
    protected TextureSheetParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7) {
        super(dwl, double2, double3, double4, double5, double6, double7);
    }
    
    protected void setSprite(final TextureAtlasSprite eju) {
        this.sprite = eju;
    }
    
    @Override
    protected float getU0() {
        return this.sprite.getU0();
    }
    
    @Override
    protected float getU1() {
        return this.sprite.getU1();
    }
    
    @Override
    protected float getV0() {
        return this.sprite.getV0();
    }
    
    @Override
    protected float getV1() {
        return this.sprite.getV1();
    }
    
    public void pickSprite(final SpriteSet dyo) {
        this.setSprite(dyo.get(this.random));
    }
    
    public void setSpriteFromAge(final SpriteSet dyo) {
        this.setSprite(dyo.get(this.age, this.lifetime));
    }
}

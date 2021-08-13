package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;

public class SimpleAnimatedParticle extends TextureSheetParticle {
    protected final SpriteSet sprites;
    private final float baseGravity;
    private float baseAirFriction;
    private float fadeR;
    private float fadeG;
    private float fadeB;
    private boolean hasFade;
    
    protected SimpleAnimatedParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final SpriteSet dyo, final float float6) {
        super(dwl, double2, double3, double4);
        this.baseAirFriction = 0.91f;
        this.sprites = dyo;
        this.baseGravity = float6;
    }
    
    public void setColor(final int integer) {
        final float float3 = ((integer & 0xFF0000) >> 16) / 255.0f;
        final float float4 = ((integer & 0xFF00) >> 8) / 255.0f;
        final float float5 = ((integer & 0xFF) >> 0) / 255.0f;
        final float float6 = 1.0f;
        this.setColor(float3 * 1.0f, float4 * 1.0f, float5 * 1.0f);
    }
    
    public void setFadeColor(final int integer) {
        this.fadeR = ((integer & 0xFF0000) >> 16) / 255.0f;
        this.fadeG = ((integer & 0xFF00) >> 8) / 255.0f;
        this.fadeB = ((integer & 0xFF) >> 0) / 255.0f;
        this.hasFade = true;
    }
    
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
    
    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }
        this.setSpriteFromAge(this.sprites);
        if (this.age > this.lifetime / 2) {
            this.setAlpha(1.0f - (this.age - (float)(this.lifetime / 2)) / this.lifetime);
            if (this.hasFade) {
                this.rCol += (this.fadeR - this.rCol) * 0.2f;
                this.gCol += (this.fadeG - this.gCol) * 0.2f;
                this.bCol += (this.fadeB - this.bCol) * 0.2f;
            }
        }
        this.yd += this.baseGravity;
        this.move(this.xd, this.yd, this.zd);
        this.xd *= this.baseAirFriction;
        this.yd *= this.baseAirFriction;
        this.zd *= this.baseAirFriction;
        if (this.onGround) {
            this.xd *= 0.699999988079071;
            this.zd *= 0.699999988079071;
        }
    }
    
    public int getLightColor(final float float1) {
        return 15728880;
    }
    
    protected void setBaseAirFriction(final float float1) {
        this.baseAirFriction = float1;
    }
}

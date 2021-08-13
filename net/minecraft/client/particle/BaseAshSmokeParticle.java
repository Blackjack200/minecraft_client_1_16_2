package net.minecraft.client.particle;

import net.minecraft.util.Mth;
import net.minecraft.client.multiplayer.ClientLevel;

public class BaseAshSmokeParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final double fallSpeed;
    
    protected BaseAshSmokeParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final float float5, final float float6, final float float7, final double double8, final double double9, final double double10, final float float11, final SpriteSet dyo, final float float13, final int integer, final double double15, final boolean boolean16) {
        super(dwl, double2, double3, double4, 0.0, 0.0, 0.0);
        this.fallSpeed = double15;
        this.sprites = dyo;
        this.xd *= float5;
        this.yd *= float6;
        this.zd *= float7;
        this.xd += double8;
        this.yd += double9;
        this.zd += double10;
        final float float14 = dwl.random.nextFloat() * float13;
        this.rCol = float14;
        this.gCol = float14;
        this.bCol = float14;
        this.quadSize *= 0.75f * float11;
        this.lifetime = (int)(integer / (dwl.random.nextFloat() * 0.8 + 0.2));
        this.lifetime *= (int)float11;
        this.lifetime = Math.max(this.lifetime, 1);
        this.setSpriteFromAge(dyo);
        this.hasPhysics = boolean16;
    }
    
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }
    
    @Override
    public float getQuadSize(final float float1) {
        return this.quadSize * Mth.clamp((this.age + float1) / this.lifetime * 32.0f, 0.0f, 1.0f);
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
        this.yd += this.fallSpeed;
        this.move(this.xd, this.yd, this.zd);
        if (this.y == this.yo) {
            this.xd *= 1.1;
            this.zd *= 1.1;
        }
        this.xd *= 0.9599999785423279;
        this.yd *= 0.9599999785423279;
        this.zd *= 0.9599999785423279;
        if (this.onGround) {
            this.xd *= 0.699999988079071;
            this.zd *= 0.699999988079071;
        }
    }
}

package net.minecraft.client.particle;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.client.multiplayer.ClientLevel;

public class NoteParticle extends TextureSheetParticle {
    private NoteParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final double double5) {
        super(dwl, double2, double3, double4, 0.0, 0.0, 0.0);
        this.xd *= 0.009999999776482582;
        this.yd *= 0.009999999776482582;
        this.zd *= 0.009999999776482582;
        this.yd += 0.2;
        this.rCol = Math.max(0.0f, Mth.sin(((float)double5 + 0.0f) * 6.2831855f) * 0.65f + 0.35f);
        this.gCol = Math.max(0.0f, Mth.sin(((float)double5 + 0.33333334f) * 6.2831855f) * 0.65f + 0.35f);
        this.bCol = Math.max(0.0f, Mth.sin(((float)double5 + 0.6666667f) * 6.2831855f) * 0.65f + 0.35f);
        this.quadSize *= 1.5f;
        this.lifetime = 6;
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
        this.move(this.xd, this.yd, this.zd);
        if (this.y == this.yo) {
            this.xd *= 1.1;
            this.zd *= 1.1;
        }
        this.xd *= 0.6600000262260437;
        this.yd *= 0.6600000262260437;
        this.zd *= 0.6600000262260437;
        if (this.onGround) {
            this.xd *= 0.699999988079071;
            this.zd *= 0.699999988079071;
        }
    }
    
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;
        
        public Provider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final NoteParticle dxx16 = new NoteParticle(dwl, double3, double4, double5, double6, null);
            dxx16.pickSprite(this.sprite);
            return dxx16;
        }
    }
}

package net.minecraft.client.particle;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.client.multiplayer.ClientLevel;

public class CritParticle extends TextureSheetParticle {
    private CritParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7) {
        super(dwl, double2, double3, double4, 0.0, 0.0, 0.0);
        this.xd *= 0.10000000149011612;
        this.yd *= 0.10000000149011612;
        this.zd *= 0.10000000149011612;
        this.xd += double5 * 0.4;
        this.yd += double6 * 0.4;
        this.zd += double7 * 0.4;
        final float float15 = (float)(Math.random() * 0.30000001192092896 + 0.6000000238418579);
        this.rCol = float15;
        this.gCol = float15;
        this.bCol = float15;
        this.quadSize *= 0.75f;
        this.lifetime = Math.max((int)(6.0 / (Math.random() * 0.8 + 0.6)), 1);
        this.hasPhysics = false;
        this.tick();
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
        this.gCol *= (float)0.96;
        this.bCol *= (float)0.9;
        this.xd *= 0.699999988079071;
        this.yd *= 0.699999988079071;
        this.zd *= 0.699999988079071;
        this.yd -= 0.019999999552965164;
        if (this.onGround) {
            this.xd *= 0.699999988079071;
            this.zd *= 0.699999988079071;
        }
    }
    
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }
    
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;
        
        public Provider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final CritParticle dxf16 = new CritParticle(dwl, double3, double4, double5, double6, double7, double8, null);
            dxf16.pickSprite(this.sprite);
            return dxf16;
        }
    }
    
    public static class MagicProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;
        
        public MagicProvider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final CritParticle critParticle;
            final CritParticle dxf16 = critParticle = new CritParticle(dwl, double3, double4, double5, double6, double7, double8, null);
            critParticle.rCol *= 0.3f;
            final CritParticle critParticle2 = dxf16;
            critParticle2.gCol *= 0.8f;
            dxf16.pickSprite(this.sprite);
            return dxf16;
        }
    }
    
    public static class DamageIndicatorProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;
        
        public DamageIndicatorProvider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final CritParticle dxf16 = new CritParticle(dwl, double3, double4, double5, double6, double7 + 1.0, double8, null);
            dxf16.setLifetime(20);
            dxf16.pickSprite(this.sprite);
            return dxf16;
        }
    }
}

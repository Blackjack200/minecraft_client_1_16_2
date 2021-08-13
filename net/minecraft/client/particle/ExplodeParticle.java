package net.minecraft.client.particle;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.client.multiplayer.ClientLevel;

public class ExplodeParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    
    protected ExplodeParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7, final SpriteSet dyo) {
        super(dwl, double2, double3, double4);
        this.sprites = dyo;
        this.xd = double5 + (Math.random() * 2.0 - 1.0) * 0.05000000074505806;
        this.yd = double6 + (Math.random() * 2.0 - 1.0) * 0.05000000074505806;
        this.zd = double7 + (Math.random() * 2.0 - 1.0) * 0.05000000074505806;
        final float float16 = this.random.nextFloat() * 0.3f + 0.7f;
        this.rCol = float16;
        this.gCol = float16;
        this.bCol = float16;
        this.quadSize = 0.1f * (this.random.nextFloat() * this.random.nextFloat() * 6.0f + 1.0f);
        this.lifetime = (int)(16.0 / (this.random.nextFloat() * 0.8 + 0.2)) + 2;
        this.setSpriteFromAge(dyo);
    }
    
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
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
        this.yd += 0.004;
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.8999999761581421;
        this.yd *= 0.8999999761581421;
        this.zd *= 0.8999999761581421;
        if (this.onGround) {
            this.xd *= 0.699999988079071;
            this.zd *= 0.699999988079071;
        }
    }
    
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        
        public Provider(final SpriteSet dyo) {
            this.sprites = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            return new ExplodeParticle(dwl, double3, double4, double5, double6, double7, double8, this.sprites);
        }
    }
}

package net.minecraft.client.particle;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.client.multiplayer.ClientLevel;

public class SpitParticle extends ExplodeParticle {
    private SpitParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7, final SpriteSet dyo) {
        super(dwl, double2, double3, double4, double5, double6, double7, dyo);
        this.gravity = 0.5f;
    }
    
    @Override
    public void tick() {
        super.tick();
        this.yd -= 0.004 + 0.04 * this.gravity;
    }
    
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        
        public Provider(final SpriteSet dyo) {
            this.sprites = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            return new SpitParticle(dwl, double3, double4, double5, double6, double7, double8, this.sprites, null);
        }
    }
}

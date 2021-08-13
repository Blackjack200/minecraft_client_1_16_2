package net.minecraft.client.particle;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.client.multiplayer.ClientLevel;

public class AshParticle extends BaseAshSmokeParticle {
    protected AshParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7, final float float8, final SpriteSet dyo) {
        super(dwl, double2, double3, double4, 0.1f, -0.1f, 0.1f, double5, double6, double7, float8, dyo, 0.5f, 20, -0.004, false);
    }
    
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        
        public Provider(final SpriteSet dyo) {
            this.sprites = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            return new AshParticle(dwl, double3, double4, double5, 0.0, 0.0, 0.0, 1.0f, this.sprites);
        }
    }
}

package net.minecraft.client.particle;

import net.minecraft.core.particles.ParticleOptions;
import java.util.Random;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.client.multiplayer.ClientLevel;

public class WhiteAshParticle extends BaseAshSmokeParticle {
    protected WhiteAshParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7, final float float8, final SpriteSet dyo) {
        super(dwl, double2, double3, double4, 0.1f, -0.1f, 0.1f, double5, double6, double7, float8, dyo, 0.0f, 20, -5.0E-4, false);
        this.rCol = 0.7294118f;
        this.gCol = 0.69411767f;
        this.bCol = 0.7607843f;
    }
    
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        
        public Provider(final SpriteSet dyo) {
            this.sprites = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final Random random16 = dwl.random;
            final double double9 = random16.nextFloat() * -1.9 * random16.nextFloat() * 0.1;
            final double double10 = random16.nextFloat() * -0.5 * random16.nextFloat() * 0.1 * 5.0;
            final double double11 = random16.nextFloat() * -1.9 * random16.nextFloat() * 0.1;
            return new WhiteAshParticle(dwl, double3, double4, double5, double9, double10, double11, 1.0f, this.sprites);
        }
    }
}

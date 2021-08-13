package net.minecraft.client.particle;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.client.multiplayer.ClientLevel;

public class HugeExplosionParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    
    private HugeExplosionParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final double double5, final SpriteSet dyo) {
        super(dwl, double2, double3, double4, 0.0, 0.0, 0.0);
        this.lifetime = 6 + this.random.nextInt(4);
        final float float12 = this.random.nextFloat() * 0.6f + 0.4f;
        this.rCol = float12;
        this.gCol = float12;
        this.bCol = float12;
        this.quadSize = 2.0f * (1.0f - (float)double5 * 0.5f);
        this.setSpriteFromAge(this.sprites = dyo);
    }
    
    public int getLightColor(final float float1) {
        return 15728880;
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
    }
    
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }
    
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        
        public Provider(final SpriteSet dyo) {
            this.sprites = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            return new HugeExplosionParticle(dwl, double3, double4, double5, double6, this.sprites, null);
        }
    }
}

package net.minecraft.client.particle;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.client.multiplayer.ClientLevel;

public class SoulParticle extends RisingParticle {
    private final SpriteSet sprites;
    
    private SoulParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7, final SpriteSet dyo) {
        super(dwl, double2, double3, double4, double5, double6, double7);
        this.sprites = dyo;
        this.scale(1.5f);
        this.setSpriteFromAge(dyo);
    }
    
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
    
    @Override
    public void tick() {
        super.tick();
        if (!this.removed) {
            this.setSpriteFromAge(this.sprites);
        }
    }
    
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;
        
        public Provider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final SoulParticle dyk16 = new SoulParticle(dwl, double3, double4, double5, double6, double7, double8, this.sprite, null);
            dyk16.setAlpha(1.0f);
            return dyk16;
        }
    }
}

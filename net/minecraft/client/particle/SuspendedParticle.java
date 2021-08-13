package net.minecraft.client.particle;

import java.util.Random;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.client.multiplayer.ClientLevel;

public class SuspendedParticle extends TextureSheetParticle {
    private SuspendedParticle(final ClientLevel dwl, final double double2, final double double3, final double double4) {
        super(dwl, double2, double3 - 0.125, double4);
        this.rCol = 0.4f;
        this.gCol = 0.4f;
        this.bCol = 0.7f;
        this.setSize(0.01f, 0.01f);
        this.quadSize *= this.random.nextFloat() * 0.6f + 0.2f;
        this.lifetime = (int)(16.0 / (Math.random() * 0.8 + 0.2));
        this.hasPhysics = false;
    }
    
    private SuspendedParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7) {
        super(dwl, double2, double3 - 0.125, double4, double5, double6, double7);
        this.setSize(0.01f, 0.01f);
        this.quadSize *= this.random.nextFloat() * 0.6f + 0.6f;
        this.lifetime = (int)(16.0 / (Math.random() * 0.8 + 0.2));
        this.hasPhysics = false;
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
        if (this.lifetime-- <= 0) {
            this.remove();
            return;
        }
        this.move(this.xd, this.yd, this.zd);
    }
    
    public static class UnderwaterProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;
        
        public UnderwaterProvider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final SuspendedParticle dyq16 = new SuspendedParticle(dwl, double3, double4, double5, null);
            dyq16.pickSprite(this.sprite);
            return dyq16;
        }
    }
    
    public static class CrimsonSporeProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;
        
        public CrimsonSporeProvider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final Random random16 = dwl.random;
            final double double9 = random16.nextGaussian() * 9.999999974752427E-7;
            final double double10 = random16.nextGaussian() * 9.999999747378752E-5;
            final double double11 = random16.nextGaussian() * 9.999999974752427E-7;
            final SuspendedParticle dyq23 = new SuspendedParticle(dwl, double3, double4, double5, double9, double10, double11, null);
            dyq23.pickSprite(this.sprite);
            dyq23.setColor(0.9f, 0.4f, 0.5f);
            return dyq23;
        }
    }
    
    public static class WarpedSporeProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;
        
        public WarpedSporeProvider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final double double9 = dwl.random.nextFloat() * -1.9 * dwl.random.nextFloat() * 0.1;
            final SuspendedParticle dyq18 = new SuspendedParticle(dwl, double3, double4, double5, 0.0, double9, 0.0, null);
            dyq18.pickSprite(this.sprite);
            dyq18.setColor(0.1f, 0.1f, 0.3f);
            dyq18.setSize(0.001f, 0.001f);
            return dyq18;
        }
    }
}

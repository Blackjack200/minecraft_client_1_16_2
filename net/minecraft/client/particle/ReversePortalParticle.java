package net.minecraft.client.particle;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.client.multiplayer.ClientLevel;

public class ReversePortalParticle extends PortalParticle {
    private ReversePortalParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7) {
        super(dwl, double2, double3, double4, double5, double6, double7);
        this.quadSize *= 1.5;
        this.lifetime = (int)(Math.random() * 2.0) + 60;
    }
    
    @Override
    public float getQuadSize(final float float1) {
        final float float2 = 1.0f - (this.age + float1) / (this.lifetime * 1.5f);
        return this.quadSize * float2;
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
        final float float2 = this.age / (float)this.lifetime;
        this.x += this.xd * float2;
        this.y += this.yd * float2;
        this.z += this.zd * float2;
    }
    
    public static class ReversePortalProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;
        
        public ReversePortalProvider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final ReversePortalParticle dyf16 = new ReversePortalParticle(dwl, double3, double4, double5, double6, double7, double8, null);
            dyf16.pickSprite(this.sprite);
            return dyf16;
        }
    }
}

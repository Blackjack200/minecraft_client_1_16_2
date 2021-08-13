package net.minecraft.client.particle;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.client.multiplayer.ClientLevel;

public class HugeExplosionSeedParticle extends NoRenderParticle {
    private int life;
    private final int lifeTime;
    
    private HugeExplosionSeedParticle(final ClientLevel dwl, final double double2, final double double3, final double double4) {
        super(dwl, double2, double3, double4, 0.0, 0.0, 0.0);
        this.lifeTime = 8;
    }
    
    @Override
    public void tick() {
        for (int integer2 = 0; integer2 < 6; ++integer2) {
            final double double3 = this.x + (this.random.nextDouble() - this.random.nextDouble()) * 4.0;
            final double double4 = this.y + (this.random.nextDouble() - this.random.nextDouble()) * 4.0;
            final double double5 = this.z + (this.random.nextDouble() - this.random.nextDouble()) * 4.0;
            this.level.addParticle(ParticleTypes.EXPLOSION, double3, double4, double5, this.life / (float)this.lifeTime, 0.0, 0.0);
        }
        ++this.life;
        if (this.life == this.lifeTime) {
            this.remove();
        }
    }
    
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            return new HugeExplosionSeedParticle(dwl, double3, double4, double5, null);
        }
    }
}

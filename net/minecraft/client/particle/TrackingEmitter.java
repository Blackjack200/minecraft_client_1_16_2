package net.minecraft.client.particle;

import net.minecraft.world.phys.Vec3;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;

public class TrackingEmitter extends NoRenderParticle {
    private final Entity entity;
    private int life;
    private final int lifeTime;
    private final ParticleOptions particleType;
    
    public TrackingEmitter(final ClientLevel dwl, final Entity apx, final ParticleOptions hf) {
        this(dwl, apx, hf, 3);
    }
    
    public TrackingEmitter(final ClientLevel dwl, final Entity apx, final ParticleOptions hf, final int integer) {
        this(dwl, apx, hf, integer, apx.getDeltaMovement());
    }
    
    private TrackingEmitter(final ClientLevel dwl, final Entity apx, final ParticleOptions hf, final int integer, final Vec3 dck) {
        super(dwl, apx.getX(), apx.getY(0.5), apx.getZ(), dck.x, dck.y, dck.z);
        this.entity = apx;
        this.lifeTime = integer;
        this.particleType = hf;
        this.tick();
    }
    
    @Override
    public void tick() {
        for (int integer2 = 0; integer2 < 16; ++integer2) {
            final double double3 = this.random.nextFloat() * 2.0f - 1.0f;
            final double double4 = this.random.nextFloat() * 2.0f - 1.0f;
            final double double5 = this.random.nextFloat() * 2.0f - 1.0f;
            if (double3 * double3 + double4 * double4 + double5 * double5 <= 1.0) {
                final double double6 = this.entity.getX(double3 / 4.0);
                final double double7 = this.entity.getY(0.5 + double4 / 4.0);
                final double double8 = this.entity.getZ(double5 / 4.0);
                this.level.addParticle(this.particleType, false, double6, double7, double8, double3, double4 + 0.2, double5);
            }
        }
        ++this.life;
        if (this.life >= this.lifeTime) {
            this.remove();
        }
    }
}

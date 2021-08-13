package net.minecraft.client.particle;

import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;

public interface ParticleProvider<T extends ParticleOptions> {
    @Nullable
    Particle createParticle(final T hf, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8);
}

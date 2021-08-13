package net.minecraft.client.particle;

import net.minecraft.client.Camera;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.multiplayer.ClientLevel;

public class NoRenderParticle extends Particle {
    protected NoRenderParticle(final ClientLevel dwl, final double double2, final double double3, final double double4) {
        super(dwl, double2, double3, double4);
    }
    
    protected NoRenderParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7) {
        super(dwl, double2, double3, double4, double5, double6, double7);
    }
    
    @Override
    public final void render(final VertexConsumer dfn, final Camera djh, final float float3) {
    }
    
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.NO_RENDER;
    }
}

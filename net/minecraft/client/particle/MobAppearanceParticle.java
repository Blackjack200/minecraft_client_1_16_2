package net.minecraft.client.particle;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.Minecraft;
import com.mojang.math.Vector3f;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.Mth;
import net.minecraft.client.Camera;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.entity.ElderGuardianRenderer;
import net.minecraft.client.model.GuardianModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.Model;

public class MobAppearanceParticle extends Particle {
    private final Model model;
    private final RenderType renderType;
    
    private MobAppearanceParticle(final ClientLevel dwl, final double double2, final double double3, final double double4) {
        super(dwl, double2, double3, double4);
        this.model = new GuardianModel();
        this.renderType = RenderType.entityTranslucent(ElderGuardianRenderer.GUARDIAN_ELDER_LOCATION);
        this.gravity = 0.0f;
        this.lifetime = 30;
    }
    
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }
    
    @Override
    public void render(final VertexConsumer dfn, final Camera djh, final float float3) {
        final float float4 = (this.age + float3) / this.lifetime;
        final float float5 = 0.05f + 0.5f * Mth.sin(float4 * 3.1415927f);
        final PoseStack dfj7 = new PoseStack();
        dfj7.mulPose(djh.rotation());
        dfj7.mulPose(Vector3f.XP.rotationDegrees(150.0f * float4 - 60.0f));
        dfj7.scale(-1.0f, -1.0f, 1.0f);
        dfj7.translate(0.0, -1.1009999513626099, 1.5);
        final MultiBufferSource.BufferSource a8 = Minecraft.getInstance().renderBuffers().bufferSource();
        final VertexConsumer dfn2 = a8.getBuffer(this.renderType);
        this.model.renderToBuffer(dfj7, dfn2, 15728880, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, float5);
        a8.endBatch();
    }
    
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            return new MobAppearanceParticle(dwl, double3, double4, double5, null);
        }
    }
}

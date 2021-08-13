package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.LlamaSpitModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.LlamaSpit;

public class LlamaSpitRenderer extends EntityRenderer<LlamaSpit> {
    private static final ResourceLocation LLAMA_SPIT_LOCATION;
    private final LlamaSpitModel<LlamaSpit> model;
    
    public LlamaSpitRenderer(final EntityRenderDispatcher eel) {
        super(eel);
        this.model = new LlamaSpitModel<LlamaSpit>();
    }
    
    @Override
    public void render(final LlamaSpit bgi, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        dfj.pushPose();
        dfj.translate(0.0, 0.15000000596046448, 0.0);
        dfj.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(float3, bgi.yRotO, bgi.yRot) - 90.0f));
        dfj.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(float3, bgi.xRotO, bgi.xRot)));
        this.model.setupAnim(bgi, float3, 0.0f, -0.1f, 0.0f, 0.0f);
        final VertexConsumer dfn8 = dzy.getBuffer(this.model.renderType(LlamaSpitRenderer.LLAMA_SPIT_LOCATION));
        this.model.renderToBuffer(dfj, dfn8, integer, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
        dfj.popPose();
        super.render(bgi, float2, float3, dfj, dzy, integer);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final LlamaSpit bgi) {
        return LlamaSpitRenderer.LLAMA_SPIT_LOCATION;
    }
    
    static {
        LLAMA_SPIT_LOCATION = new ResourceLocation("textures/entity/llama/spit.png");
    }
}

package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.BlockPos;
import net.minecraft.client.model.SkullModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.WitherSkull;

public class WitherSkullRenderer extends EntityRenderer<WitherSkull> {
    private static final ResourceLocation WITHER_INVULNERABLE_LOCATION;
    private static final ResourceLocation WITHER_LOCATION;
    private final SkullModel model;
    
    public WitherSkullRenderer(final EntityRenderDispatcher eel) {
        super(eel);
        this.model = new SkullModel();
    }
    
    @Override
    protected int getBlockLightLevel(final WitherSkull bgw, final BlockPos fx) {
        return 15;
    }
    
    @Override
    public void render(final WitherSkull bgw, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        dfj.pushPose();
        dfj.scale(-1.0f, -1.0f, 1.0f);
        final float float4 = Mth.rotlerp(bgw.yRotO, bgw.yRot, float3);
        final float float5 = Mth.lerp(float3, bgw.xRotO, bgw.xRot);
        final VertexConsumer dfn10 = dzy.getBuffer(this.model.renderType(this.getTextureLocation(bgw)));
        this.model.setupAnim(0.0f, float4, float5);
        this.model.renderToBuffer(dfj, dfn10, integer, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
        dfj.popPose();
        super.render(bgw, float2, float3, dfj, dzy, integer);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final WitherSkull bgw) {
        return bgw.isDangerous() ? WitherSkullRenderer.WITHER_INVULNERABLE_LOCATION : WitherSkullRenderer.WITHER_LOCATION;
    }
    
    static {
        WITHER_INVULNERABLE_LOCATION = new ResourceLocation("textures/entity/wither/wither_invulnerable.png");
        WITHER_LOCATION = new ResourceLocation("textures/entity/wither/wither.png");
    }
}

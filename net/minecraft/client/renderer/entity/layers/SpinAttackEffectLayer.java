package net.minecraft.client.renderer.entity.layers;

import net.minecraft.world.entity.Entity;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.LivingEntity;

public class SpinAttackEffectLayer<T extends LivingEntity> extends RenderLayer<T, PlayerModel<T>> {
    public static final ResourceLocation TEXTURE;
    private final ModelPart box;
    
    public SpinAttackEffectLayer(final RenderLayerParent<T, PlayerModel<T>> egc) {
        super(egc);
        (this.box = new ModelPart(64, 64, 0, 0)).addBox(-8.0f, -16.0f, -8.0f, 16.0f, 32.0f, 16.0f);
    }
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final T aqj, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        if (!aqj.isAutoSpinAttack()) {
            return;
        }
        final VertexConsumer dfn12 = dzy.getBuffer(RenderType.entityCutoutNoCull(SpinAttackEffectLayer.TEXTURE));
        for (int integer2 = 0; integer2 < 3; ++integer2) {
            dfj.pushPose();
            final float float11 = float8 * -(45 + integer2 * 5);
            dfj.mulPose(Vector3f.YP.rotationDegrees(float11));
            final float float12 = 0.75f * integer2;
            dfj.scale(float12, float12, float12);
            dfj.translate(0.0, -0.2f + 0.6f * integer2, 0.0);
            this.box.render(dfj, dfn12, integer, OverlayTexture.NO_OVERLAY);
            dfj.popPose();
        }
    }
    
    static {
        TEXTURE = new ResourceLocation("textures/entity/trident_riptide.png");
    }
}

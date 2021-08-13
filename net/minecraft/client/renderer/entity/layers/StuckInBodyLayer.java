package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import java.util.Random;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.LivingEntity;

public abstract class StuckInBodyLayer<T extends LivingEntity, M extends PlayerModel<T>> extends RenderLayer<T, M> {
    public StuckInBodyLayer(final LivingEntityRenderer<T, M> efj) {
        super(efj);
    }
    
    protected abstract int numStuck(final T aqj);
    
    protected abstract void renderStuckItem(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final Entity apx, final float float5, final float float6, final float float7, final float float8);
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final T aqj, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        final int integer2 = this.numStuck(aqj);
        final Random random13 = new Random((long)aqj.getId());
        if (integer2 <= 0) {
            return;
        }
        for (int integer3 = 0; integer3 < integer2; ++integer3) {
            dfj.pushPose();
            final ModelPart dwf15 = this.getParentModel().getRandomModelPart(random13);
            final ModelPart.Cube a16 = dwf15.getRandomCube(random13);
            dwf15.translateAndRotate(dfj);
            float float11 = random13.nextFloat();
            float float12 = random13.nextFloat();
            float float13 = random13.nextFloat();
            final float float14 = Mth.lerp(float11, a16.minX, a16.maxX) / 16.0f;
            final float float15 = Mth.lerp(float12, a16.minY, a16.maxY) / 16.0f;
            final float float16 = Mth.lerp(float13, a16.minZ, a16.maxZ) / 16.0f;
            dfj.translate(float14, float15, float16);
            float11 = -1.0f * (float11 * 2.0f - 1.0f);
            float12 = -1.0f * (float12 * 2.0f - 1.0f);
            float13 = -1.0f * (float13 * 2.0f - 1.0f);
            this.renderStuckItem(dfj, dzy, integer, aqj, float11, float12, float13, float7);
            dfj.popPose();
        }
    }
}

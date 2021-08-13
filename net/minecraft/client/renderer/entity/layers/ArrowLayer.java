package net.minecraft.client.renderer.entity.layers;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.LivingEntity;

public class ArrowLayer<T extends LivingEntity, M extends PlayerModel<T>> extends StuckInBodyLayer<T, M> {
    private final EntityRenderDispatcher dispatcher;
    private Arrow arrow;
    
    public ArrowLayer(final LivingEntityRenderer<T, M> efj) {
        super(efj);
        this.dispatcher = efj.getDispatcher();
    }
    
    @Override
    protected int numStuck(final T aqj) {
        return aqj.getArrowCount();
    }
    
    @Override
    protected void renderStuckItem(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final Entity apx, final float float5, final float float6, final float float7, final float float8) {
        final float float9 = Mth.sqrt(float5 * float5 + float7 * float7);
        this.arrow = new Arrow(apx.level, apx.getX(), apx.getY(), apx.getZ());
        this.arrow.yRot = (float)(Math.atan2((double)float5, (double)float7) * 57.2957763671875);
        this.arrow.xRot = (float)(Math.atan2((double)float6, (double)float9) * 57.2957763671875);
        this.arrow.yRotO = this.arrow.yRot;
        this.arrow.xRotO = this.arrow.xRot;
        this.dispatcher.<Arrow>render(this.arrow, 0.0, 0.0, 0.0, 0.0f, float8, dfj, dzy, integer);
    }
}

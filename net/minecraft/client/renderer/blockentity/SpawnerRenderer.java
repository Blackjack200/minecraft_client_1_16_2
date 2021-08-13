package net.minecraft.client.renderer.blockentity;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;

public class SpawnerRenderer extends BlockEntityRenderer<SpawnerBlockEntity> {
    public SpawnerRenderer(final BlockEntityRenderDispatcher ebv) {
        super(ebv);
    }
    
    @Override
    public void render(final SpawnerBlockEntity cdf, final float float2, final PoseStack dfj, final MultiBufferSource dzy, final int integer5, final int integer6) {
        dfj.pushPose();
        dfj.translate(0.5, 0.0, 0.5);
        final BaseSpawner bqw8 = cdf.getSpawner();
        final Entity apx9 = bqw8.getOrCreateDisplayEntity();
        if (apx9 != null) {
            float float3 = 0.53125f;
            final float float4 = Math.max(apx9.getBbWidth(), apx9.getBbHeight());
            if (float4 > 1.0) {
                float3 /= float4;
            }
            dfj.translate(0.0, 0.4000000059604645, 0.0);
            dfj.mulPose(Vector3f.YP.rotationDegrees((float)Mth.lerp(float2, bqw8.getoSpin(), bqw8.getSpin()) * 10.0f));
            dfj.translate(0.0, -0.20000000298023224, 0.0);
            dfj.mulPose(Vector3f.XP.rotationDegrees(-30.0f));
            dfj.scale(float3, float3, float3);
            Minecraft.getInstance().getEntityRenderDispatcher().<Entity>render(apx9, 0.0, 0.0, 0.0, 0.0f, float2, dfj, dzy, integer5);
        }
        dfj.popPose();
    }
}

package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import com.mojang.math.Vector3f;
import net.minecraft.util.Mth;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.SalmonModel;
import net.minecraft.world.entity.animal.Salmon;

public class SalmonRenderer extends MobRenderer<Salmon, SalmonModel<Salmon>> {
    private static final ResourceLocation SALMON_LOCATION;
    
    public SalmonRenderer(final EntityRenderDispatcher eel) {
        super(eel, new SalmonModel(), 0.4f);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Salmon bao) {
        return SalmonRenderer.SALMON_LOCATION;
    }
    
    @Override
    protected void setupRotations(final Salmon bao, final PoseStack dfj, final float float3, final float float4, final float float5) {
        super.setupRotations(bao, dfj, float3, float4, float5);
        float float6 = 1.0f;
        float float7 = 1.0f;
        if (!bao.isInWater()) {
            float6 = 1.3f;
            float7 = 1.7f;
        }
        final float float8 = float6 * 4.3f * Mth.sin(float7 * 0.6f * float3);
        dfj.mulPose(Vector3f.YP.rotationDegrees(float8));
        dfj.translate(0.0, 0.0, -0.4000000059604645);
        if (!bao.isInWater()) {
            dfj.translate(0.20000000298023224, 0.10000000149011612, 0.0);
            dfj.mulPose(Vector3f.ZP.rotationDegrees(90.0f));
        }
    }
    
    static {
        SALMON_LOCATION = new ResourceLocation("textures/entity/fish/salmon.png");
    }
}

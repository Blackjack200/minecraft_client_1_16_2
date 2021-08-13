package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import com.mojang.math.Vector3f;
import net.minecraft.util.Mth;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.SquidModel;
import net.minecraft.world.entity.animal.Squid;

public class SquidRenderer extends MobRenderer<Squid, SquidModel<Squid>> {
    private static final ResourceLocation SQUID_LOCATION;
    
    public SquidRenderer(final EntityRenderDispatcher eel) {
        super(eel, new SquidModel(), 0.7f);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Squid bas) {
        return SquidRenderer.SQUID_LOCATION;
    }
    
    @Override
    protected void setupRotations(final Squid bas, final PoseStack dfj, final float float3, final float float4, final float float5) {
        final float float6 = Mth.lerp(float5, bas.xBodyRotO, bas.xBodyRot);
        final float float7 = Mth.lerp(float5, bas.zBodyRotO, bas.zBodyRot);
        dfj.translate(0.0, 0.5, 0.0);
        dfj.mulPose(Vector3f.YP.rotationDegrees(180.0f - float4));
        dfj.mulPose(Vector3f.XP.rotationDegrees(float6));
        dfj.mulPose(Vector3f.YP.rotationDegrees(float7));
        dfj.translate(0.0, -1.2000000476837158, 0.0);
    }
    
    @Override
    protected float getBob(final Squid bas, final float float2) {
        return Mth.lerp(float2, bas.oldTentacleAngle, bas.tentacleAngle);
    }
    
    static {
        SQUID_LOCATION = new ResourceLocation("textures/entity/squid.png");
    }
}

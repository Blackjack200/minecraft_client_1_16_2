package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import com.mojang.math.Vector3f;
import net.minecraft.util.Mth;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.CodModel;
import net.minecraft.world.entity.animal.Cod;

public class CodRenderer extends MobRenderer<Cod, CodModel<Cod>> {
    private static final ResourceLocation COD_LOCATION;
    
    public CodRenderer(final EntityRenderDispatcher eel) {
        super(eel, new CodModel(), 0.3f);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Cod baa) {
        return CodRenderer.COD_LOCATION;
    }
    
    @Override
    protected void setupRotations(final Cod baa, final PoseStack dfj, final float float3, final float float4, final float float5) {
        super.setupRotations(baa, dfj, float3, float4, float5);
        final float float6 = 4.3f * Mth.sin(0.6f * float3);
        dfj.mulPose(Vector3f.YP.rotationDegrees(float6));
        if (!baa.isInWater()) {
            dfj.translate(0.10000000149011612, 0.10000000149011612, -0.10000000149011612);
            dfj.mulPose(Vector3f.ZP.rotationDegrees(90.0f));
        }
    }
    
    static {
        COD_LOCATION = new ResourceLocation("textures/entity/fish/cod.png");
    }
}

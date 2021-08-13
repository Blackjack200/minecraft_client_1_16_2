package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import com.mojang.math.Vector3f;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.layers.IronGolemFlowerLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.IronGolemCrackinessLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.IronGolemModel;
import net.minecraft.world.entity.animal.IronGolem;

public class IronGolemRenderer extends MobRenderer<IronGolem, IronGolemModel<IronGolem>> {
    private static final ResourceLocation GOLEM_LOCATION;
    
    public IronGolemRenderer(final EntityRenderDispatcher eel) {
        super(eel, new IronGolemModel(), 0.7f);
        this.addLayer(new IronGolemCrackinessLayer(this));
        this.addLayer(new IronGolemFlowerLayer(this));
    }
    
    @Override
    public ResourceLocation getTextureLocation(final IronGolem baf) {
        return IronGolemRenderer.GOLEM_LOCATION;
    }
    
    @Override
    protected void setupRotations(final IronGolem baf, final PoseStack dfj, final float float3, final float float4, final float float5) {
        super.setupRotations(baf, dfj, float3, float4, float5);
        if (baf.animationSpeed < 0.01) {
            return;
        }
        final float float6 = 13.0f;
        final float float7 = baf.animationPosition - baf.animationSpeed * (1.0f - float5) + 6.0f;
        final float float8 = (Math.abs(float7 % 13.0f - 6.5f) - 3.25f) / 3.25f;
        dfj.mulPose(Vector3f.ZP.rotationDegrees(6.5f * float8));
    }
    
    static {
        GOLEM_LOCATION = new ResourceLocation("textures/entity/iron_golem/iron_golem.png");
    }
}

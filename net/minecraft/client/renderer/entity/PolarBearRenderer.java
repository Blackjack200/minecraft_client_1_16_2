package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.PolarBearModel;
import net.minecraft.world.entity.animal.PolarBear;

public class PolarBearRenderer extends MobRenderer<PolarBear, PolarBearModel<PolarBear>> {
    private static final ResourceLocation BEAR_LOCATION;
    
    public PolarBearRenderer(final EntityRenderDispatcher eel) {
        super(eel, new PolarBearModel(), 0.9f);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final PolarBear bal) {
        return PolarBearRenderer.BEAR_LOCATION;
    }
    
    @Override
    protected void scale(final PolarBear bal, final PoseStack dfj, final float float3) {
        dfj.scale(1.2f, 1.2f, 1.2f);
        super.scale(bal, dfj, float3);
    }
    
    static {
        BEAR_LOCATION = new ResourceLocation("textures/entity/bear/polarbear.png");
    }
}

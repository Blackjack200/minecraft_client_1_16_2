package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.WolfCollarLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.WolfModel;
import net.minecraft.world.entity.animal.Wolf;

public class WolfRenderer extends MobRenderer<Wolf, WolfModel<Wolf>> {
    private static final ResourceLocation WOLF_LOCATION;
    private static final ResourceLocation WOLF_TAME_LOCATION;
    private static final ResourceLocation WOLF_ANGRY_LOCATION;
    
    public WolfRenderer(final EntityRenderDispatcher eel) {
        super(eel, new WolfModel(), 0.5f);
        this.addLayer(new WolfCollarLayer(this));
    }
    
    @Override
    protected float getBob(final Wolf baw, final float float2) {
        return baw.getTailAngle();
    }
    
    @Override
    public void render(final Wolf baw, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        if (baw.isWet()) {
            final float float4 = baw.getWetShade(float3);
            ((WolfModel)this.model).setColor(float4, float4, float4);
        }
        super.render(baw, float2, float3, dfj, dzy, integer);
        if (baw.isWet()) {
            ((WolfModel)this.model).setColor(1.0f, 1.0f, 1.0f);
        }
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Wolf baw) {
        if (baw.isTame()) {
            return WolfRenderer.WOLF_TAME_LOCATION;
        }
        if (baw.isAngry()) {
            return WolfRenderer.WOLF_ANGRY_LOCATION;
        }
        return WolfRenderer.WOLF_LOCATION;
    }
    
    static {
        WOLF_LOCATION = new ResourceLocation("textures/entity/wolf/wolf.png");
        WOLF_TAME_LOCATION = new ResourceLocation("textures/entity/wolf/wolf_tame.png");
        WOLF_ANGRY_LOCATION = new ResourceLocation("textures/entity/wolf/wolf_angry.png");
    }
}

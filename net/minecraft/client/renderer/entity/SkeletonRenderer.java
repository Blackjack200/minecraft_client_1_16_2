package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.world.entity.monster.AbstractSkeleton;

public class SkeletonRenderer extends HumanoidMobRenderer<AbstractSkeleton, SkeletonModel<AbstractSkeleton>> {
    private static final ResourceLocation SKELETON_LOCATION;
    
    public SkeletonRenderer(final EntityRenderDispatcher eel) {
        super(eel, new SkeletonModel(), 0.5f);
        this.addLayer((RenderLayer<AbstractSkeleton, SkeletonModel<AbstractSkeleton>>)new HumanoidArmorLayer((RenderLayerParent<LivingEntity, HumanoidModel>)this, new SkeletonModel(0.5f, true), new SkeletonModel(1.0f, true)));
    }
    
    @Override
    public ResourceLocation getTextureLocation(final AbstractSkeleton bcw) {
        return SkeletonRenderer.SKELETON_LOCATION;
    }
    
    static {
        SKELETON_LOCATION = new ResourceLocation("textures/entity/skeleton/skeleton.png");
    }
}

package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.resources.ResourceLocation;

public class WitherSkeletonRenderer extends SkeletonRenderer {
    private static final ResourceLocation WITHER_SKELETON_LOCATION;
    
    public WitherSkeletonRenderer(final EntityRenderDispatcher eel) {
        super(eel);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final AbstractSkeleton bcw) {
        return WitherSkeletonRenderer.WITHER_SKELETON_LOCATION;
    }
    
    @Override
    protected void scale(final AbstractSkeleton bcw, final PoseStack dfj, final float float3) {
        dfj.scale(1.2f, 1.2f, 1.2f);
    }
    
    static {
        WITHER_SKELETON_LOCATION = new ResourceLocation("textures/entity/skeleton/wither_skeleton.png");
    }
}

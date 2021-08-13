package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.GhastModel;
import net.minecraft.world.entity.monster.Ghast;

public class GhastRenderer extends MobRenderer<Ghast, GhastModel<Ghast>> {
    private static final ResourceLocation GHAST_LOCATION;
    private static final ResourceLocation GHAST_SHOOTING_LOCATION;
    
    public GhastRenderer(final EntityRenderDispatcher eel) {
        super(eel, new GhastModel(), 1.5f);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Ghast bdh) {
        if (bdh.isCharging()) {
            return GhastRenderer.GHAST_SHOOTING_LOCATION;
        }
        return GhastRenderer.GHAST_LOCATION;
    }
    
    @Override
    protected void scale(final Ghast bdh, final PoseStack dfj, final float float3) {
        final float float4 = 1.0f;
        final float float5 = 4.5f;
        final float float6 = 4.5f;
        dfj.scale(4.5f, 4.5f, 4.5f);
    }
    
    static {
        GHAST_LOCATION = new ResourceLocation("textures/entity/ghast/ghast.png");
        GHAST_SHOOTING_LOCATION = new ResourceLocation("textures/entity/ghast/ghast_shooting.png");
    }
}

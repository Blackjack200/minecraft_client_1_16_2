package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.model.GiantZombieModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.monster.Giant;

public class GiantMobRenderer extends MobRenderer<Giant, HumanoidModel<Giant>> {
    private static final ResourceLocation ZOMBIE_LOCATION;
    private final float scale;
    
    public GiantMobRenderer(final EntityRenderDispatcher eel, final float float2) {
        super(eel, new GiantZombieModel(), 0.5f * float2);
        this.scale = float2;
        this.addLayer(new ItemInHandLayer<Giant, HumanoidModel<Giant>>(this));
        this.addLayer((RenderLayer<Giant, HumanoidModel<Giant>>)new HumanoidArmorLayer((RenderLayerParent<LivingEntity, HumanoidModel>)this, new GiantZombieModel(0.5f, true), new GiantZombieModel(1.0f, true)));
    }
    
    @Override
    protected void scale(final Giant bdi, final PoseStack dfj, final float float3) {
        dfj.scale(this.scale, this.scale, this.scale);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Giant bdi) {
        return GiantMobRenderer.ZOMBIE_LOCATION;
    }
    
    static {
        ZOMBIE_LOCATION = new ResourceLocation("textures/entity/zombie/zombie.png");
    }
}

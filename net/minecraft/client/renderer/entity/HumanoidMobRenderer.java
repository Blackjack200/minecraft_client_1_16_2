package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Mob;

public class HumanoidMobRenderer<T extends Mob, M extends HumanoidModel<T>> extends MobRenderer<T, M> {
    private static final ResourceLocation DEFAULT_LOCATION;
    
    public HumanoidMobRenderer(final EntityRenderDispatcher eel, final M due, final float float3) {
        this(eel, due, float3, 1.0f, 1.0f, 1.0f);
    }
    
    public HumanoidMobRenderer(final EntityRenderDispatcher eel, final M due, final float float3, final float float4, final float float5, final float float6) {
        super(eel, due, float3);
        this.addLayer(new CustomHeadLayer<T, M>(this, float4, float5, float6));
        this.addLayer(new ElytraLayer<T, M>(this));
        this.addLayer(new ItemInHandLayer<T, M>(this));
    }
    
    @Override
    public ResourceLocation getTextureLocation(final T aqk) {
        return HumanoidMobRenderer.DEFAULT_LOCATION;
    }
    
    static {
        DEFAULT_LOCATION = new ResourceLocation("textures/entity/steve.png");
    }
}

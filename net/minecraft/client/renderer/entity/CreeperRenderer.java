package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.Mth;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.CreeperPowerLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.world.entity.monster.Creeper;

public class CreeperRenderer extends MobRenderer<Creeper, CreeperModel<Creeper>> {
    private static final ResourceLocation CREEPER_LOCATION;
    
    public CreeperRenderer(final EntityRenderDispatcher eel) {
        super(eel, new CreeperModel(), 0.5f);
        this.addLayer(new CreeperPowerLayer(this));
    }
    
    @Override
    protected void scale(final Creeper bcz, final PoseStack dfj, final float float3) {
        float float4 = bcz.getSwelling(float3);
        final float float5 = 1.0f + Mth.sin(float4 * 100.0f) * float4 * 0.01f;
        float4 = Mth.clamp(float4, 0.0f, 1.0f);
        float4 *= float4;
        float4 *= float4;
        final float float6 = (1.0f + float4 * 0.4f) * float5;
        final float float7 = (1.0f + float4 * 0.1f) / float5;
        dfj.scale(float6, float7, float6);
    }
    
    @Override
    protected float getWhiteOverlayProgress(final Creeper bcz, final float float2) {
        final float float3 = bcz.getSwelling(float2);
        if ((int)(float3 * 10.0f) % 2 == 0) {
            return 0.0f;
        }
        return Mth.clamp(float3, 0.5f, 1.0f);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Creeper bcz) {
        return CreeperRenderer.CREEPER_LOCATION;
    }
    
    static {
        CREEPER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper.png");
    }
}

package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import com.mojang.math.Vector3f;
import net.minecraft.util.Mth;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.FoxHeldItemLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.FoxModel;
import net.minecraft.world.entity.animal.Fox;

public class FoxRenderer extends MobRenderer<Fox, FoxModel<Fox>> {
    private static final ResourceLocation RED_FOX_TEXTURE;
    private static final ResourceLocation RED_FOX_SLEEP_TEXTURE;
    private static final ResourceLocation SNOW_FOX_TEXTURE;
    private static final ResourceLocation SNOW_FOX_SLEEP_TEXTURE;
    
    public FoxRenderer(final EntityRenderDispatcher eel) {
        super(eel, new FoxModel(), 0.4f);
        this.addLayer(new FoxHeldItemLayer(this));
    }
    
    @Override
    protected void setupRotations(final Fox bae, final PoseStack dfj, final float float3, final float float4, final float float5) {
        super.setupRotations(bae, dfj, float3, float4, float5);
        if (bae.isPouncing() || bae.isFaceplanted()) {
            final float float6 = -Mth.lerp(float5, bae.xRotO, bae.xRot);
            dfj.mulPose(Vector3f.XP.rotationDegrees(float6));
        }
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Fox bae) {
        if (bae.getFoxType() == Fox.Type.RED) {
            return bae.isSleeping() ? FoxRenderer.RED_FOX_SLEEP_TEXTURE : FoxRenderer.RED_FOX_TEXTURE;
        }
        return bae.isSleeping() ? FoxRenderer.SNOW_FOX_SLEEP_TEXTURE : FoxRenderer.SNOW_FOX_TEXTURE;
    }
    
    static {
        RED_FOX_TEXTURE = new ResourceLocation("textures/entity/fox/fox.png");
        RED_FOX_SLEEP_TEXTURE = new ResourceLocation("textures/entity/fox/fox_sleep.png");
        SNOW_FOX_TEXTURE = new ResourceLocation("textures/entity/fox/snow_fox.png");
        SNOW_FOX_SLEEP_TEXTURE = new ResourceLocation("textures/entity/fox/snow_fox_sleep.png");
    }
}

package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.Mth;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.BatModel;
import net.minecraft.world.entity.ambient.Bat;

public class BatRenderer extends MobRenderer<Bat, BatModel> {
    private static final ResourceLocation BAT_LOCATION;
    
    public BatRenderer(final EntityRenderDispatcher eel) {
        super(eel, new BatModel(), 0.25f);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Bat azr) {
        return BatRenderer.BAT_LOCATION;
    }
    
    @Override
    protected void scale(final Bat azr, final PoseStack dfj, final float float3) {
        dfj.scale(0.35f, 0.35f, 0.35f);
    }
    
    @Override
    protected void setupRotations(final Bat azr, final PoseStack dfj, final float float3, final float float4, final float float5) {
        if (azr.isResting()) {
            dfj.translate(0.0, -0.10000000149011612, 0.0);
        }
        else {
            dfj.translate(0.0, Mth.cos(float3 * 0.3f) * 0.1f, 0.0);
        }
        super.setupRotations(azr, dfj, float3, float4, float5);
    }
    
    static {
        BAT_LOCATION = new ResourceLocation("textures/entity/bat.png");
    }
}

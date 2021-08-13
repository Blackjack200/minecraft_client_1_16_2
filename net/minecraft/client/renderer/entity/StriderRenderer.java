package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.SaddleLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.StriderModel;
import net.minecraft.world.entity.monster.Strider;

public class StriderRenderer extends MobRenderer<Strider, StriderModel<Strider>> {
    private static final ResourceLocation STRIDER_LOCATION;
    private static final ResourceLocation COLD_LOCATION;
    
    public StriderRenderer(final EntityRenderDispatcher eel) {
        super(eel, new StriderModel(), 0.5f);
        this.addLayer(new SaddleLayer<Strider, StriderModel<Strider>>(this, new StriderModel<Strider>(), new ResourceLocation("textures/entity/strider/strider_saddle.png")));
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Strider bea) {
        return bea.isSuffocating() ? StriderRenderer.COLD_LOCATION : StriderRenderer.STRIDER_LOCATION;
    }
    
    @Override
    protected void scale(final Strider bea, final PoseStack dfj, final float float3) {
        if (bea.isBaby()) {
            dfj.scale(0.5f, 0.5f, 0.5f);
            this.shadowRadius = 0.25f;
        }
        else {
            this.shadowRadius = 0.5f;
        }
    }
    
    @Override
    protected boolean isShaking(final Strider bea) {
        return bea.isSuffocating();
    }
    
    static {
        STRIDER_LOCATION = new ResourceLocation("textures/entity/strider/strider.png");
        COLD_LOCATION = new ResourceLocation("textures/entity/strider/strider_cold.png");
    }
}

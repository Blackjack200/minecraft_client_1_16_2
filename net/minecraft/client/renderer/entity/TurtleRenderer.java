package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.TurtleModel;
import net.minecraft.world.entity.animal.Turtle;

public class TurtleRenderer extends MobRenderer<Turtle, TurtleModel<Turtle>> {
    private static final ResourceLocation TURTLE_LOCATION;
    
    public TurtleRenderer(final EntityRenderDispatcher eel) {
        super(eel, new TurtleModel(0.0f), 0.7f);
    }
    
    @Override
    public void render(final Turtle bau, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        if (bau.isBaby()) {
            this.shadowRadius *= 0.5f;
        }
        super.render(bau, float2, float3, dfj, dzy, integer);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Turtle bau) {
        return TurtleRenderer.TURTLE_LOCATION;
    }
    
    static {
        TURTLE_LOCATION = new ResourceLocation("textures/entity/turtle/big_sea_turtle.png");
    }
}

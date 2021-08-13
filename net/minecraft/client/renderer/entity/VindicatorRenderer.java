package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Vindicator;

public class VindicatorRenderer extends IllagerRenderer<Vindicator> {
    private static final ResourceLocation VINDICATOR;
    
    public VindicatorRenderer(final EntityRenderDispatcher eel) {
        super(eel, new IllagerModel(0.0f, 0.0f, 64, 64), 0.5f);
        this.addLayer((RenderLayer<T, IllagerModel<T>>)new ItemInHandLayer<Vindicator, IllagerModel<Vindicator>>(this) {
            @Override
            public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final Vindicator bec, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
                if (bec.isAggressive()) {
                    super.render(dfj, dzy, integer, bec, float5, float6, float7, float8, float9, float10);
                }
            }
        });
    }
    
    public ResourceLocation getTextureLocation(final Vindicator bec) {
        return VindicatorRenderer.VINDICATOR;
    }
    
    static {
        VINDICATOR = new ResourceLocation("textures/entity/illager/vindicator.png");
    }
}

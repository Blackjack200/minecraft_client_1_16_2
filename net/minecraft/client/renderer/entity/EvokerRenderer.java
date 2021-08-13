package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.SpellcasterIllager;

public class EvokerRenderer<T extends SpellcasterIllager> extends IllagerRenderer<T> {
    private static final ResourceLocation EVOKER_ILLAGER;
    
    public EvokerRenderer(final EntityRenderDispatcher eel) {
        super(eel, new IllagerModel(0.0f, 0.0f, 64, 64), 0.5f);
        this.addLayer((RenderLayer<T, IllagerModel<T>>)new ItemInHandLayer<T, IllagerModel<T>>(this) {
            @Override
            public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final T bdx, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
                if (bdx.isCastingSpell()) {
                    super.render(dfj, dzy, integer, bdx, float5, float6, float7, float8, float9, float10);
                }
            }
        });
    }
    
    public ResourceLocation getTextureLocation(final T bdx) {
        return EvokerRenderer.EVOKER_ILLAGER;
    }
    
    static {
        EVOKER_ILLAGER = new ResourceLocation("textures/entity/illager/evoker.png");
    }
}

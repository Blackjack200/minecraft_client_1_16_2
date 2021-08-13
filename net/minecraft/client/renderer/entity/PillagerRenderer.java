package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Pillager;

public class PillagerRenderer extends IllagerRenderer<Pillager> {
    private static final ResourceLocation PILLAGER;
    
    public PillagerRenderer(final EntityRenderDispatcher eel) {
        super(eel, new IllagerModel(0.0f, 0.0f, 64, 64), 0.5f);
        this.addLayer((RenderLayer<T, IllagerModel<T>>)new ItemInHandLayer<AbstractIllager, IllagerModel<T>>(this));
    }
    
    public ResourceLocation getTextureLocation(final Pillager bdq) {
        return PillagerRenderer.PILLAGER;
    }
    
    static {
        PILLAGER = new ResourceLocation("textures/entity/illager/pillager.png");
    }
}

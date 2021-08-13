package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.LlamaDecorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.LlamaModel;
import net.minecraft.world.entity.animal.horse.Llama;

public class LlamaRenderer extends MobRenderer<Llama, LlamaModel<Llama>> {
    private static final ResourceLocation[] LLAMA_LOCATIONS;
    
    public LlamaRenderer(final EntityRenderDispatcher eel) {
        super(eel, new LlamaModel(0.0f), 0.7f);
        this.addLayer(new LlamaDecorLayer(this));
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Llama bbb) {
        return LlamaRenderer.LLAMA_LOCATIONS[bbb.getVariant()];
    }
    
    static {
        LLAMA_LOCATIONS = new ResourceLocation[] { new ResourceLocation("textures/entity/llama/creamy.png"), new ResourceLocation("textures/entity/llama/white.png"), new ResourceLocation("textures/entity/llama/brown.png"), new ResourceLocation("textures/entity/llama/gray.png") };
    }
}

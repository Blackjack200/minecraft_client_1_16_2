package net.minecraft.client.renderer.entity.layers;

import net.minecraft.world.entity.Entity;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.LlamaModel;
import net.minecraft.world.entity.animal.horse.Llama;

public class LlamaDecorLayer extends RenderLayer<Llama, LlamaModel<Llama>> {
    private static final ResourceLocation[] TEXTURE_LOCATION;
    private static final ResourceLocation TRADER_LLAMA;
    private final LlamaModel<Llama> model;
    
    public LlamaDecorLayer(final RenderLayerParent<Llama, LlamaModel<Llama>> egc) {
        super(egc);
        this.model = new LlamaModel<Llama>(0.5f);
    }
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final Llama bbb, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        final DyeColor bku12 = bbb.getSwag();
        ResourceLocation vk13;
        if (bku12 != null) {
            vk13 = LlamaDecorLayer.TEXTURE_LOCATION[bku12.getId()];
        }
        else {
            if (!bbb.isTraderLlama()) {
                return;
            }
            vk13 = LlamaDecorLayer.TRADER_LLAMA;
        }
        ((RenderLayer<T, LlamaModel<Llama>>)this).getParentModel().copyPropertiesTo(this.model);
        this.model.setupAnim(bbb, float5, float6, float8, float9, float10);
        final VertexConsumer dfn14 = dzy.getBuffer(RenderType.entityCutoutNoCull(vk13));
        this.model.renderToBuffer(dfj, dfn14, integer, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    static {
        TEXTURE_LOCATION = new ResourceLocation[] { new ResourceLocation("textures/entity/llama/decor/white.png"), new ResourceLocation("textures/entity/llama/decor/orange.png"), new ResourceLocation("textures/entity/llama/decor/magenta.png"), new ResourceLocation("textures/entity/llama/decor/light_blue.png"), new ResourceLocation("textures/entity/llama/decor/yellow.png"), new ResourceLocation("textures/entity/llama/decor/lime.png"), new ResourceLocation("textures/entity/llama/decor/pink.png"), new ResourceLocation("textures/entity/llama/decor/gray.png"), new ResourceLocation("textures/entity/llama/decor/light_gray.png"), new ResourceLocation("textures/entity/llama/decor/cyan.png"), new ResourceLocation("textures/entity/llama/decor/purple.png"), new ResourceLocation("textures/entity/llama/decor/blue.png"), new ResourceLocation("textures/entity/llama/decor/brown.png"), new ResourceLocation("textures/entity/llama/decor/green.png"), new ResourceLocation("textures/entity/llama/decor/red.png"), new ResourceLocation("textures/entity/llama/decor/black.png") };
        TRADER_LLAMA = new ResourceLocation("textures/entity/llama/decor/trader_llama.png");
    }
}

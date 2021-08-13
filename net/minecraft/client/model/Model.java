package net.minecraft.client.model;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;
import net.minecraft.client.model.geom.ModelPart;
import java.util.function.Consumer;

public abstract class Model implements Consumer<ModelPart> {
    protected final Function<ResourceLocation, RenderType> renderType;
    public int texWidth;
    public int texHeight;
    
    public Model(final Function<ResourceLocation, RenderType> function) {
        this.texWidth = 64;
        this.texHeight = 32;
        this.renderType = function;
    }
    
    public void accept(final ModelPart dwf) {
    }
    
    public final RenderType renderType(final ResourceLocation vk) {
        return (RenderType)this.renderType.apply(vk);
    }
    
    public abstract void renderToBuffer(final PoseStack dfj, final VertexConsumer dfn, final int integer3, final int integer4, final float float5, final float float6, final float float7, final float float8);
}

package net.minecraft.client.model;

import net.minecraft.client.model.geom.ModelPart;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;

public abstract class ListModel<E extends Entity> extends EntityModel<E> {
    public ListModel() {
        this(RenderType::entityCutoutNoCull);
    }
    
    public ListModel(final Function<ResourceLocation, RenderType> function) {
        super(function);
    }
    
    @Override
    public void renderToBuffer(final PoseStack dfj, final VertexConsumer dfn, final int integer3, final int integer4, final float float5, final float float6, final float float7, final float float8) {
        this.parts().forEach(dwf -> dwf.render(dfj, dfn, integer3, integer4, float5, float6, float7, float8));
    }
    
    public abstract Iterable<ModelPart> parts();
}

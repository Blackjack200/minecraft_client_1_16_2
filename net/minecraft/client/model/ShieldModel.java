package net.minecraft.client.model;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.geom.ModelPart;

public class ShieldModel extends Model {
    private final ModelPart plate;
    private final ModelPart handle;
    
    public ShieldModel() {
        super((Function<ResourceLocation, RenderType>)RenderType::entitySolid);
        this.texWidth = 64;
        this.texHeight = 64;
        (this.plate = new ModelPart(this, 0, 0)).addBox(-6.0f, -11.0f, -2.0f, 12.0f, 22.0f, 1.0f, 0.0f);
        (this.handle = new ModelPart(this, 26, 0)).addBox(-1.0f, -3.0f, -1.0f, 2.0f, 6.0f, 6.0f, 0.0f);
    }
    
    public ModelPart plate() {
        return this.plate;
    }
    
    public ModelPart handle() {
        return this.handle;
    }
    
    @Override
    public void renderToBuffer(final PoseStack dfj, final VertexConsumer dfn, final int integer3, final int integer4, final float float5, final float float6, final float float7, final float float8) {
        this.plate.render(dfj, dfn, integer3, integer4, float5, float6, float7, float8);
        this.handle.render(dfj, dfn, integer3, integer4, float5, float6, float7, float8);
    }
}

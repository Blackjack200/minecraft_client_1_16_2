package net.minecraft.client.model;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.geom.ModelPart;

public class SkullModel extends Model {
    protected final ModelPart head;
    
    public SkullModel() {
        this(0, 35, 64, 64);
    }
    
    public SkullModel(final int integer1, final int integer2, final int integer3, final int integer4) {
        super((Function<ResourceLocation, RenderType>)RenderType::entityTranslucent);
        this.texWidth = integer3;
        this.texHeight = integer4;
        (this.head = new ModelPart(this, integer1, integer2)).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, 0.0f);
        this.head.setPos(0.0f, 0.0f, 0.0f);
    }
    
    public void setupAnim(final float float1, final float float2, final float float3) {
        this.head.yRot = float2 * 0.017453292f;
        this.head.xRot = float3 * 0.017453292f;
    }
    
    @Override
    public void renderToBuffer(final PoseStack dfj, final VertexConsumer dfn, final int integer3, final int integer4, final float float5, final float float6, final float float7, final float float8) {
        this.head.render(dfj, dfn, integer3, integer4, float5, float6, float7, float8);
    }
}

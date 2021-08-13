package net.minecraft.client.model;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;

public class HumanoidHeadModel extends SkullModel {
    private final ModelPart hat;
    
    public HumanoidHeadModel() {
        super(0, 0, 64, 64);
        (this.hat = new ModelPart(this, 32, 0)).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, 0.25f);
        this.hat.setPos(0.0f, 0.0f, 0.0f);
    }
    
    @Override
    public void setupAnim(final float float1, final float float2, final float float3) {
        super.setupAnim(float1, float2, float3);
        this.hat.yRot = this.head.yRot;
        this.hat.xRot = this.head.xRot;
    }
    
    @Override
    public void renderToBuffer(final PoseStack dfj, final VertexConsumer dfn, final int integer3, final int integer4, final float float5, final float float6, final float float7, final float float8) {
        super.renderToBuffer(dfj, dfn, integer3, integer4, float5, float6, float7, float8);
        this.hat.render(dfj, dfn, integer3, integer4, float5, float6, float7, float8);
    }
}

package net.minecraft.client.model.dragon;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.SkullModel;

public class DragonHeadModel extends SkullModel {
    private final ModelPart head;
    private final ModelPart jaw;
    
    public DragonHeadModel(final float float1) {
        this.texWidth = 256;
        this.texHeight = 256;
        final float float2 = -16.0f;
        (this.head = new ModelPart(this)).addBox("upperlip", -6.0f, -1.0f, -24.0f, 12, 5, 16, float1, 176, 44);
        this.head.addBox("upperhead", -8.0f, -8.0f, -10.0f, 16, 16, 16, float1, 112, 30);
        this.head.mirror = true;
        this.head.addBox("scale", -5.0f, -12.0f, -4.0f, 2, 4, 6, float1, 0, 0);
        this.head.addBox("nostril", -5.0f, -3.0f, -22.0f, 2, 2, 4, float1, 112, 0);
        this.head.mirror = false;
        this.head.addBox("scale", 3.0f, -12.0f, -4.0f, 2, 4, 6, float1, 0, 0);
        this.head.addBox("nostril", 3.0f, -3.0f, -22.0f, 2, 2, 4, float1, 112, 0);
        (this.jaw = new ModelPart(this)).setPos(0.0f, 4.0f, -8.0f);
        this.jaw.addBox("jaw", -6.0f, 0.0f, -16.0f, 12, 4, 16, float1, 176, 65);
        this.head.addChild(this.jaw);
    }
    
    @Override
    public void setupAnim(final float float1, final float float2, final float float3) {
        this.jaw.xRot = (float)(Math.sin((double)(float1 * 3.1415927f * 0.2f)) + 1.0) * 0.2f;
        this.head.yRot = float2 * 0.017453292f;
        this.head.xRot = float3 * 0.017453292f;
    }
    
    @Override
    public void renderToBuffer(final PoseStack dfj, final VertexConsumer dfn, final int integer3, final int integer4, final float float5, final float float6, final float float7, final float float8) {
        dfj.pushPose();
        dfj.translate(0.0, -0.37437498569488525, 0.0);
        dfj.scale(0.75f, 0.75f, 0.75f);
        this.head.render(dfj, dfn, integer3, integer4, float5, float6, float7, float8);
        dfj.popPose();
    }
}

package net.minecraft.client.model;

import net.minecraft.util.Mth;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.google.common.collect.ImmutableList;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;
import net.minecraft.client.renderer.RenderType;
import java.util.List;
import net.minecraft.client.model.geom.ModelPart;

public class BookModel extends Model {
    private final ModelPart leftLid;
    private final ModelPart rightLid;
    private final ModelPart leftPages;
    private final ModelPart rightPages;
    private final ModelPart flipPage1;
    private final ModelPart flipPage2;
    private final ModelPart seam;
    private final List<ModelPart> parts;
    
    public BookModel() {
        super((Function<ResourceLocation, RenderType>)RenderType::entitySolid);
        this.leftLid = new ModelPart(64, 32, 0, 0).addBox(-6.0f, -5.0f, -0.005f, 6.0f, 10.0f, 0.005f);
        this.rightLid = new ModelPart(64, 32, 16, 0).addBox(0.0f, -5.0f, -0.005f, 6.0f, 10.0f, 0.005f);
        this.seam = new ModelPart(64, 32, 12, 0).addBox(-1.0f, -5.0f, 0.0f, 2.0f, 10.0f, 0.005f);
        this.leftPages = new ModelPart(64, 32, 0, 10).addBox(0.0f, -4.0f, -0.99f, 5.0f, 8.0f, 1.0f);
        this.rightPages = new ModelPart(64, 32, 12, 10).addBox(0.0f, -4.0f, -0.01f, 5.0f, 8.0f, 1.0f);
        this.flipPage1 = new ModelPart(64, 32, 24, 10).addBox(0.0f, -4.0f, 0.0f, 5.0f, 8.0f, 0.005f);
        this.flipPage2 = new ModelPart(64, 32, 24, 10).addBox(0.0f, -4.0f, 0.0f, 5.0f, 8.0f, 0.005f);
        this.parts = (List<ModelPart>)ImmutableList.of(this.leftLid, this.rightLid, this.seam, this.leftPages, this.rightPages, this.flipPage1, this.flipPage2);
        this.leftLid.setPos(0.0f, 0.0f, -1.0f);
        this.rightLid.setPos(0.0f, 0.0f, 1.0f);
        this.seam.yRot = 1.5707964f;
    }
    
    @Override
    public void renderToBuffer(final PoseStack dfj, final VertexConsumer dfn, final int integer3, final int integer4, final float float5, final float float6, final float float7, final float float8) {
        this.render(dfj, dfn, integer3, integer4, float5, float6, float7, float8);
    }
    
    public void render(final PoseStack dfj, final VertexConsumer dfn, final int integer3, final int integer4, final float float5, final float float6, final float float7, final float float8) {
        this.parts.forEach(dwf -> dwf.render(dfj, dfn, integer3, integer4, float5, float6, float7, float8));
    }
    
    public void setupAnim(final float float1, final float float2, final float float3, final float float4) {
        final float float5 = (Mth.sin(float1 * 0.02f) * 0.1f + 1.25f) * float4;
        this.leftLid.yRot = 3.1415927f + float5;
        this.rightLid.yRot = -float5;
        this.leftPages.yRot = float5;
        this.rightPages.yRot = -float5;
        this.flipPage1.yRot = float5 - float5 * 2.0f * float2;
        this.flipPage2.yRot = float5 - float5 * 2.0f * float3;
        this.leftPages.x = Mth.sin(float5);
        this.rightPages.x = Mth.sin(float5);
        this.flipPage1.x = Mth.sin(float5);
        this.flipPage2.x = Mth.sin(float5);
    }
}

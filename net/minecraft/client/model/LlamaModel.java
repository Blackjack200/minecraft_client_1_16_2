package net.minecraft.client.model;

import net.minecraft.world.entity.Entity;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.Mth;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;

public class LlamaModel<T extends AbstractChestedHorse> extends EntityModel<T> {
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart leg0;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart chest1;
    private final ModelPart chest2;
    
    public LlamaModel(final float float1) {
        this.texWidth = 128;
        this.texHeight = 64;
        (this.head = new ModelPart(this, 0, 0)).addBox(-2.0f, -14.0f, -10.0f, 4.0f, 4.0f, 9.0f, float1);
        this.head.setPos(0.0f, 7.0f, -6.0f);
        this.head.texOffs(0, 14).addBox(-4.0f, -16.0f, -6.0f, 8.0f, 18.0f, 6.0f, float1);
        this.head.texOffs(17, 0).addBox(-4.0f, -19.0f, -4.0f, 3.0f, 3.0f, 2.0f, float1);
        this.head.texOffs(17, 0).addBox(1.0f, -19.0f, -4.0f, 3.0f, 3.0f, 2.0f, float1);
        (this.body = new ModelPart(this, 29, 0)).addBox(-6.0f, -10.0f, -7.0f, 12.0f, 18.0f, 10.0f, float1);
        this.body.setPos(0.0f, 5.0f, 2.0f);
        (this.chest1 = new ModelPart(this, 45, 28)).addBox(-3.0f, 0.0f, 0.0f, 8.0f, 8.0f, 3.0f, float1);
        this.chest1.setPos(-8.5f, 3.0f, 3.0f);
        this.chest1.yRot = 1.5707964f;
        (this.chest2 = new ModelPart(this, 45, 41)).addBox(-3.0f, 0.0f, 0.0f, 8.0f, 8.0f, 3.0f, float1);
        this.chest2.setPos(5.5f, 3.0f, 3.0f);
        this.chest2.yRot = 1.5707964f;
        final int integer3 = 4;
        final int integer4 = 14;
        (this.leg0 = new ModelPart(this, 29, 29)).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 14.0f, 4.0f, float1);
        this.leg0.setPos(-2.5f, 10.0f, 6.0f);
        (this.leg1 = new ModelPart(this, 29, 29)).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 14.0f, 4.0f, float1);
        this.leg1.setPos(2.5f, 10.0f, 6.0f);
        (this.leg2 = new ModelPart(this, 29, 29)).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 14.0f, 4.0f, float1);
        this.leg2.setPos(-2.5f, 10.0f, -4.0f);
        (this.leg3 = new ModelPart(this, 29, 29)).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 14.0f, 4.0f, float1);
        this.leg3.setPos(2.5f, 10.0f, -4.0f);
        final ModelPart leg0 = this.leg0;
        --leg0.x;
        final ModelPart leg2 = this.leg1;
        ++leg2.x;
        final ModelPart leg3 = this.leg0;
        leg3.z += 0.0f;
        final ModelPart leg4 = this.leg1;
        leg4.z += 0.0f;
        final ModelPart leg5 = this.leg2;
        --leg5.x;
        final ModelPart leg6 = this.leg3;
        ++leg6.x;
        final ModelPart leg7 = this.leg2;
        --leg7.z;
        final ModelPart leg8 = this.leg3;
        --leg8.z;
    }
    
    @Override
    public void setupAnim(final T bax, final float float2, final float float3, final float float4, final float float5, final float float6) {
        this.head.xRot = float6 * 0.017453292f;
        this.head.yRot = float5 * 0.017453292f;
        this.body.xRot = 1.5707964f;
        this.leg0.xRot = Mth.cos(float2 * 0.6662f) * 1.4f * float3;
        this.leg1.xRot = Mth.cos(float2 * 0.6662f + 3.1415927f) * 1.4f * float3;
        this.leg2.xRot = Mth.cos(float2 * 0.6662f + 3.1415927f) * 1.4f * float3;
        this.leg3.xRot = Mth.cos(float2 * 0.6662f) * 1.4f * float3;
        final boolean boolean8 = !bax.isBaby() && bax.hasChest();
        this.chest1.visible = boolean8;
        this.chest2.visible = boolean8;
    }
    
    @Override
    public void renderToBuffer(final PoseStack dfj, final VertexConsumer dfn, final int integer3, final int integer4, final float float5, final float float6, final float float7, final float float8) {
        if (this.young) {
            final float float9 = 2.0f;
            dfj.pushPose();
            final float float10 = 0.7f;
            dfj.scale(0.71428573f, 0.64935064f, 0.7936508f);
            dfj.translate(0.0, 1.3125, 0.2199999988079071);
            this.head.render(dfj, dfn, integer3, integer4, float5, float6, float7, float8);
            dfj.popPose();
            dfj.pushPose();
            final float float11 = 1.1f;
            dfj.scale(0.625f, 0.45454544f, 0.45454544f);
            dfj.translate(0.0, 2.0625, 0.0);
            this.body.render(dfj, dfn, integer3, integer4, float5, float6, float7, float8);
            dfj.popPose();
            dfj.pushPose();
            dfj.scale(0.45454544f, 0.41322312f, 0.45454544f);
            dfj.translate(0.0, 2.0625, 0.0);
            ImmutableList.of(this.leg0, this.leg1, this.leg2, this.leg3, this.chest1, this.chest2).forEach(dwf -> dwf.render(dfj, dfn, integer3, integer4, float5, float6, float7, float8));
            dfj.popPose();
        }
        else {
            ImmutableList.of(this.head, this.body, this.leg0, this.leg1, this.leg2, this.leg3, this.chest1, this.chest2).forEach(dwf -> dwf.render(dfj, dfn, integer3, integer4, float5, float6, float7, float8));
        }
    }
}

package net.minecraft.client.model;

import net.minecraft.world.entity.Entity;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.Mth;
import com.google.common.collect.Iterables;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.animal.Turtle;

public class TurtleModel<T extends Turtle> extends QuadrupedModel<T> {
    private final ModelPart eggBelly;
    
    public TurtleModel(final float float1) {
        super(12, float1, true, 120.0f, 0.0f, 9.0f, 6.0f, 120);
        this.texWidth = 128;
        this.texHeight = 64;
        (this.head = new ModelPart(this, 3, 0)).addBox(-3.0f, -1.0f, -3.0f, 6.0f, 5.0f, 6.0f, 0.0f);
        this.head.setPos(0.0f, 19.0f, -10.0f);
        this.body = new ModelPart(this);
        this.body.texOffs(7, 37).addBox(-9.5f, 3.0f, -10.0f, 19.0f, 20.0f, 6.0f, 0.0f);
        this.body.texOffs(31, 1).addBox(-5.5f, 3.0f, -13.0f, 11.0f, 18.0f, 3.0f, 0.0f);
        this.body.setPos(0.0f, 11.0f, -10.0f);
        this.eggBelly = new ModelPart(this);
        this.eggBelly.texOffs(70, 33).addBox(-4.5f, 3.0f, -14.0f, 9.0f, 18.0f, 1.0f, 0.0f);
        this.eggBelly.setPos(0.0f, 11.0f, -10.0f);
        final int integer3 = 1;
        (this.leg0 = new ModelPart(this, 1, 23)).addBox(-2.0f, 0.0f, 0.0f, 4.0f, 1.0f, 10.0f, 0.0f);
        this.leg0.setPos(-3.5f, 22.0f, 11.0f);
        (this.leg1 = new ModelPart(this, 1, 12)).addBox(-2.0f, 0.0f, 0.0f, 4.0f, 1.0f, 10.0f, 0.0f);
        this.leg1.setPos(3.5f, 22.0f, 11.0f);
        (this.leg2 = new ModelPart(this, 27, 30)).addBox(-13.0f, 0.0f, -2.0f, 13.0f, 1.0f, 5.0f, 0.0f);
        this.leg2.setPos(-5.0f, 21.0f, -4.0f);
        (this.leg3 = new ModelPart(this, 27, 24)).addBox(0.0f, 0.0f, -2.0f, 13.0f, 1.0f, 5.0f, 0.0f);
        this.leg3.setPos(5.0f, 21.0f, -4.0f);
    }
    
    @Override
    protected Iterable<ModelPart> bodyParts() {
        return (Iterable<ModelPart>)Iterables.concat((Iterable)super.bodyParts(), (Iterable)ImmutableList.of(this.eggBelly));
    }
    
    @Override
    public void setupAnim(final T bau, final float float2, final float float3, final float float4, final float float5, final float float6) {
        super.setupAnim(bau, float2, float3, float4, float5, float6);
        this.leg0.xRot = Mth.cos(float2 * 0.6662f * 0.6f) * 0.5f * float3;
        this.leg1.xRot = Mth.cos(float2 * 0.6662f * 0.6f + 3.1415927f) * 0.5f * float3;
        this.leg2.zRot = Mth.cos(float2 * 0.6662f * 0.6f + 3.1415927f) * 0.5f * float3;
        this.leg3.zRot = Mth.cos(float2 * 0.6662f * 0.6f) * 0.5f * float3;
        this.leg2.xRot = 0.0f;
        this.leg3.xRot = 0.0f;
        this.leg2.yRot = 0.0f;
        this.leg3.yRot = 0.0f;
        this.leg0.yRot = 0.0f;
        this.leg1.yRot = 0.0f;
        this.eggBelly.xRot = 1.5707964f;
        if (!bau.isInWater() && bau.isOnGround()) {
            final float float7 = bau.isLayingEgg() ? 4.0f : 1.0f;
            final float float8 = bau.isLayingEgg() ? 2.0f : 1.0f;
            final float float9 = 5.0f;
            this.leg2.yRot = Mth.cos(float7 * float2 * 5.0f + 3.1415927f) * 8.0f * float3 * float8;
            this.leg2.zRot = 0.0f;
            this.leg3.yRot = Mth.cos(float7 * float2 * 5.0f) * 8.0f * float3 * float8;
            this.leg3.zRot = 0.0f;
            this.leg0.yRot = Mth.cos(float2 * 5.0f + 3.1415927f) * 3.0f * float3;
            this.leg0.xRot = 0.0f;
            this.leg1.yRot = Mth.cos(float2 * 5.0f) * 3.0f * float3;
            this.leg1.xRot = 0.0f;
        }
        this.eggBelly.visible = (!this.young && bau.hasEgg());
    }
    
    @Override
    public void renderToBuffer(final PoseStack dfj, final VertexConsumer dfn, final int integer3, final int integer4, final float float5, final float float6, final float float7, final float float8) {
        final boolean boolean10 = this.eggBelly.visible;
        if (boolean10) {
            dfj.pushPose();
            dfj.translate(0.0, -0.07999999821186066, 0.0);
        }
        super.renderToBuffer(dfj, dfn, integer3, integer4, float5, float6, float7, float8);
        if (boolean10) {
            dfj.popPose();
        }
    }
}

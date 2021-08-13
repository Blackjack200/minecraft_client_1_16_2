package net.minecraft.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.animal.Parrot;

public class ParrotModel extends ListModel<Parrot> {
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart wingLeft;
    private final ModelPart wingRight;
    private final ModelPart head;
    private final ModelPart head2;
    private final ModelPart beak1;
    private final ModelPart beak2;
    private final ModelPart feather;
    private final ModelPart legLeft;
    private final ModelPart legRight;
    
    public ParrotModel() {
        this.texWidth = 32;
        this.texHeight = 32;
        (this.body = new ModelPart(this, 2, 8)).addBox(-1.5f, 0.0f, -1.5f, 3.0f, 6.0f, 3.0f);
        this.body.setPos(0.0f, 16.5f, -3.0f);
        (this.tail = new ModelPart(this, 22, 1)).addBox(-1.5f, -1.0f, -1.0f, 3.0f, 4.0f, 1.0f);
        this.tail.setPos(0.0f, 21.07f, 1.16f);
        (this.wingLeft = new ModelPart(this, 19, 8)).addBox(-0.5f, 0.0f, -1.5f, 1.0f, 5.0f, 3.0f);
        this.wingLeft.setPos(1.5f, 16.94f, -2.76f);
        (this.wingRight = new ModelPart(this, 19, 8)).addBox(-0.5f, 0.0f, -1.5f, 1.0f, 5.0f, 3.0f);
        this.wingRight.setPos(-1.5f, 16.94f, -2.76f);
        (this.head = new ModelPart(this, 2, 2)).addBox(-1.0f, -1.5f, -1.0f, 2.0f, 3.0f, 2.0f);
        this.head.setPos(0.0f, 15.69f, -2.76f);
        (this.head2 = new ModelPart(this, 10, 0)).addBox(-1.0f, -0.5f, -2.0f, 2.0f, 1.0f, 4.0f);
        this.head2.setPos(0.0f, -2.0f, -1.0f);
        this.head.addChild(this.head2);
        (this.beak1 = new ModelPart(this, 11, 7)).addBox(-0.5f, -1.0f, -0.5f, 1.0f, 2.0f, 1.0f);
        this.beak1.setPos(0.0f, -0.5f, -1.5f);
        this.head.addChild(this.beak1);
        (this.beak2 = new ModelPart(this, 16, 7)).addBox(-0.5f, 0.0f, -0.5f, 1.0f, 2.0f, 1.0f);
        this.beak2.setPos(0.0f, -1.75f, -2.45f);
        this.head.addChild(this.beak2);
        (this.feather = new ModelPart(this, 2, 18)).addBox(0.0f, -4.0f, -2.0f, 0.0f, 5.0f, 4.0f);
        this.feather.setPos(0.0f, -2.15f, 0.15f);
        this.head.addChild(this.feather);
        (this.legLeft = new ModelPart(this, 14, 18)).addBox(-0.5f, 0.0f, -0.5f, 1.0f, 2.0f, 1.0f);
        this.legLeft.setPos(1.0f, 22.0f, -1.05f);
        (this.legRight = new ModelPart(this, 14, 18)).addBox(-0.5f, 0.0f, -0.5f, 1.0f, 2.0f, 1.0f);
        this.legRight.setPos(-1.0f, 22.0f, -1.05f);
    }
    
    @Override
    public Iterable<ModelPart> parts() {
        return (Iterable<ModelPart>)ImmutableList.of(this.body, this.wingLeft, this.wingRight, this.tail, this.head, this.legLeft, this.legRight);
    }
    
    @Override
    public void setupAnim(final Parrot baj, final float float2, final float float3, final float float4, final float float5, final float float6) {
        this.setupAnim(getState(baj), baj.tickCount, float2, float3, float4, float5, float6);
    }
    
    @Override
    public void prepareMobModel(final Parrot baj, final float float2, final float float3, final float float4) {
        this.prepare(getState(baj));
    }
    
    public void renderOnShoulder(final PoseStack dfj, final VertexConsumer dfn, final int integer3, final int integer4, final float float5, final float float6, final float float7, final float float8, final int integer9) {
        this.prepare(State.ON_SHOULDER);
        this.setupAnim(State.ON_SHOULDER, integer9, float5, float6, 0.0f, float7, float8);
        this.parts().forEach(dwf -> dwf.render(dfj, dfn, integer3, integer4));
    }
    
    private void setupAnim(final State a, final int integer, final float float3, final float float4, final float float5, final float float6, final float float7) {
        this.head.xRot = float7 * 0.017453292f;
        this.head.yRot = float6 * 0.017453292f;
        this.head.zRot = 0.0f;
        this.head.x = 0.0f;
        this.body.x = 0.0f;
        this.tail.x = 0.0f;
        this.wingRight.x = -1.5f;
        this.wingLeft.x = 1.5f;
        switch (a) {
            case SITTING: {
                return;
            }
            case PARTY: {
                final float float8 = Mth.cos((float)integer);
                final float float9 = Mth.sin((float)integer);
                this.head.x = float8;
                this.head.y = 15.69f + float9;
                this.head.xRot = 0.0f;
                this.head.yRot = 0.0f;
                this.head.zRot = Mth.sin((float)integer) * 0.4f;
                this.body.x = float8;
                this.body.y = 16.5f + float9;
                this.wingLeft.zRot = -0.0873f - float5;
                this.wingLeft.x = 1.5f + float8;
                this.wingLeft.y = 16.94f + float9;
                this.wingRight.zRot = 0.0873f + float5;
                this.wingRight.x = -1.5f + float8;
                this.wingRight.y = 16.94f + float9;
                this.tail.x = float8;
                this.tail.y = 21.07f + float9;
                return;
            }
            case STANDING: {
                final ModelPart legLeft = this.legLeft;
                legLeft.xRot += Mth.cos(float3 * 0.6662f) * 1.4f * float4;
                final ModelPart legRight = this.legRight;
                legRight.xRot += Mth.cos(float3 * 0.6662f + 3.1415927f) * 1.4f * float4;
                break;
            }
        }
        final float float10 = float5 * 0.3f;
        this.head.y = 15.69f + float10;
        this.tail.xRot = 1.015f + Mth.cos(float3 * 0.6662f) * 0.3f * float4;
        this.tail.y = 21.07f + float10;
        this.body.y = 16.5f + float10;
        this.wingLeft.zRot = -0.0873f - float5;
        this.wingLeft.y = 16.94f + float10;
        this.wingRight.zRot = 0.0873f + float5;
        this.wingRight.y = 16.94f + float10;
        this.legLeft.y = 22.0f + float10;
        this.legRight.y = 22.0f + float10;
    }
    
    private void prepare(final State a) {
        this.feather.xRot = -0.2214f;
        this.body.xRot = 0.4937f;
        this.wingLeft.xRot = -0.6981f;
        this.wingLeft.yRot = -3.1415927f;
        this.wingRight.xRot = -0.6981f;
        this.wingRight.yRot = -3.1415927f;
        this.legLeft.xRot = -0.0299f;
        this.legRight.xRot = -0.0299f;
        this.legLeft.y = 22.0f;
        this.legRight.y = 22.0f;
        this.legLeft.zRot = 0.0f;
        this.legRight.zRot = 0.0f;
        switch (a) {
            case FLYING: {
                final ModelPart legLeft = this.legLeft;
                legLeft.xRot += 0.6981317f;
                final ModelPart legRight = this.legRight;
                legRight.xRot += 0.6981317f;
                break;
            }
            case SITTING: {
                final float float3 = 1.9f;
                this.head.y = 17.59f;
                this.tail.xRot = 1.5388988f;
                this.tail.y = 22.97f;
                this.body.y = 18.4f;
                this.wingLeft.zRot = -0.0873f;
                this.wingLeft.y = 18.84f;
                this.wingRight.zRot = 0.0873f;
                this.wingRight.y = 18.84f;
                final ModelPart legLeft2 = this.legLeft;
                legLeft2.y += 1.9f;
                final ModelPart legRight2 = this.legRight;
                legRight2.y += 1.9f;
                final ModelPart legLeft3 = this.legLeft;
                legLeft3.xRot += 1.5707964f;
                final ModelPart legRight3 = this.legRight;
                legRight3.xRot += 1.5707964f;
                break;
            }
            case PARTY: {
                this.legLeft.zRot = -0.34906584f;
                this.legRight.zRot = 0.34906584f;
                break;
            }
        }
    }
    
    private static State getState(final Parrot baj) {
        if (baj.isPartyParrot()) {
            return State.PARTY;
        }
        if (baj.isInSittingPose()) {
            return State.SITTING;
        }
        if (baj.isFlying()) {
            return State.FLYING;
        }
        return State.STANDING;
    }
    
    public enum State {
        FLYING, 
        STANDING, 
        SITTING, 
        PARTY, 
        ON_SHOULDER;
    }
}

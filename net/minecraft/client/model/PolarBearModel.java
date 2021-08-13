package net.minecraft.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.animal.PolarBear;

public class PolarBearModel<T extends PolarBear> extends QuadrupedModel<T> {
    public PolarBearModel() {
        super(12, 0.0f, true, 16.0f, 4.0f, 2.25f, 2.0f, 24);
        this.texWidth = 128;
        this.texHeight = 64;
        (this.head = new ModelPart(this, 0, 0)).addBox(-3.5f, -3.0f, -3.0f, 7.0f, 7.0f, 7.0f, 0.0f);
        this.head.setPos(0.0f, 10.0f, -16.0f);
        this.head.texOffs(0, 44).addBox(-2.5f, 1.0f, -6.0f, 5.0f, 3.0f, 3.0f, 0.0f);
        this.head.texOffs(26, 0).addBox(-4.5f, -4.0f, -1.0f, 2.0f, 2.0f, 1.0f, 0.0f);
        final ModelPart dwf2 = this.head.texOffs(26, 0);
        dwf2.mirror = true;
        dwf2.addBox(2.5f, -4.0f, -1.0f, 2.0f, 2.0f, 1.0f, 0.0f);
        this.body = new ModelPart(this);
        this.body.texOffs(0, 19).addBox(-5.0f, -13.0f, -7.0f, 14.0f, 14.0f, 11.0f, 0.0f);
        this.body.texOffs(39, 0).addBox(-4.0f, -25.0f, -7.0f, 12.0f, 12.0f, 10.0f, 0.0f);
        this.body.setPos(-2.0f, 9.0f, 12.0f);
        final int integer3 = 10;
        (this.leg0 = new ModelPart(this, 50, 22)).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 10.0f, 8.0f, 0.0f);
        this.leg0.setPos(-3.5f, 14.0f, 6.0f);
        (this.leg1 = new ModelPart(this, 50, 22)).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 10.0f, 8.0f, 0.0f);
        this.leg1.setPos(3.5f, 14.0f, 6.0f);
        (this.leg2 = new ModelPart(this, 50, 40)).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 10.0f, 6.0f, 0.0f);
        this.leg2.setPos(-2.5f, 14.0f, -7.0f);
        (this.leg3 = new ModelPart(this, 50, 40)).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 10.0f, 6.0f, 0.0f);
        this.leg3.setPos(2.5f, 14.0f, -7.0f);
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
    public void setupAnim(final T bal, final float float2, final float float3, final float float4, final float float5, final float float6) {
        super.setupAnim(bal, float2, float3, float4, float5, float6);
        final float float7 = float4 - bal.tickCount;
        float float8 = bal.getStandingAnimationScale(float7);
        float8 *= float8;
        final float float9 = 1.0f - float8;
        this.body.xRot = 1.5707964f - float8 * 3.1415927f * 0.35f;
        this.body.y = 9.0f * float9 + 11.0f * float8;
        this.leg2.y = 14.0f * float9 - 6.0f * float8;
        this.leg2.z = -8.0f * float9 - 4.0f * float8;
        final ModelPart leg2 = this.leg2;
        leg2.xRot -= float8 * 3.1415927f * 0.45f;
        this.leg3.y = this.leg2.y;
        this.leg3.z = this.leg2.z;
        final ModelPart leg3 = this.leg3;
        leg3.xRot -= float8 * 3.1415927f * 0.45f;
        if (this.young) {
            this.head.y = 10.0f * float9 - 9.0f * float8;
            this.head.z = -16.0f * float9 - 7.0f * float8;
        }
        else {
            this.head.y = 10.0f * float9 - 14.0f * float8;
            this.head.z = -16.0f * float9 - 3.0f * float8;
        }
        final ModelPart head = this.head;
        head.xRot += float8 * 3.1415927f * 0.15f;
    }
}

package net.minecraft.client.model;

import net.minecraft.world.entity.Entity;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.monster.Strider;

public class StriderModel<T extends Strider> extends ListModel<T> {
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private final ModelPart body;
    private final ModelPart bristle0;
    private final ModelPart bristle1;
    private final ModelPart bristle2;
    private final ModelPart bristle3;
    private final ModelPart bristle4;
    private final ModelPart bristle5;
    
    public StriderModel() {
        this.texWidth = 64;
        this.texHeight = 128;
        (this.rightLeg = new ModelPart(this, 0, 32)).setPos(-4.0f, 8.0f, 0.0f);
        this.rightLeg.addBox(-2.0f, 0.0f, -2.0f, 4.0f, 16.0f, 4.0f, 0.0f);
        (this.leftLeg = new ModelPart(this, 0, 55)).setPos(4.0f, 8.0f, 0.0f);
        this.leftLeg.addBox(-2.0f, 0.0f, -2.0f, 4.0f, 16.0f, 4.0f, 0.0f);
        (this.body = new ModelPart(this, 0, 0)).setPos(0.0f, 1.0f, 0.0f);
        this.body.addBox(-8.0f, -6.0f, -8.0f, 16.0f, 14.0f, 16.0f, 0.0f);
        (this.bristle0 = new ModelPart(this, 16, 65)).setPos(-8.0f, 4.0f, -8.0f);
        this.bristle0.addBox(-12.0f, 0.0f, 0.0f, 12.0f, 0.0f, 16.0f, 0.0f, true);
        this.setRotationAngle(this.bristle0, 0.0f, 0.0f, -1.2217305f);
        (this.bristle1 = new ModelPart(this, 16, 49)).setPos(-8.0f, -1.0f, -8.0f);
        this.bristle1.addBox(-12.0f, 0.0f, 0.0f, 12.0f, 0.0f, 16.0f, 0.0f, true);
        this.setRotationAngle(this.bristle1, 0.0f, 0.0f, -1.134464f);
        (this.bristle2 = new ModelPart(this, 16, 33)).setPos(-8.0f, -5.0f, -8.0f);
        this.bristle2.addBox(-12.0f, 0.0f, 0.0f, 12.0f, 0.0f, 16.0f, 0.0f, true);
        this.setRotationAngle(this.bristle2, 0.0f, 0.0f, -0.87266463f);
        (this.bristle3 = new ModelPart(this, 16, 33)).setPos(8.0f, -6.0f, -8.0f);
        this.bristle3.addBox(0.0f, 0.0f, 0.0f, 12.0f, 0.0f, 16.0f, 0.0f);
        this.setRotationAngle(this.bristle3, 0.0f, 0.0f, 0.87266463f);
        (this.bristle4 = new ModelPart(this, 16, 49)).setPos(8.0f, -2.0f, -8.0f);
        this.bristle4.addBox(0.0f, 0.0f, 0.0f, 12.0f, 0.0f, 16.0f, 0.0f);
        this.setRotationAngle(this.bristle4, 0.0f, 0.0f, 1.134464f);
        (this.bristle5 = new ModelPart(this, 16, 65)).setPos(8.0f, 3.0f, -8.0f);
        this.bristle5.addBox(0.0f, 0.0f, 0.0f, 12.0f, 0.0f, 16.0f, 0.0f);
        this.setRotationAngle(this.bristle5, 0.0f, 0.0f, 1.2217305f);
        this.body.addChild(this.bristle0);
        this.body.addChild(this.bristle1);
        this.body.addChild(this.bristle2);
        this.body.addChild(this.bristle3);
        this.body.addChild(this.bristle4);
        this.body.addChild(this.bristle5);
    }
    
    @Override
    public void setupAnim(final Strider bea, final float float2, float float3, final float float4, final float float5, final float float6) {
        float3 = Math.min(0.25f, float3);
        if (bea.getPassengers().size() <= 0) {
            this.body.xRot = float6 * 0.017453292f;
            this.body.yRot = float5 * 0.017453292f;
        }
        else {
            this.body.xRot = 0.0f;
            this.body.yRot = 0.0f;
        }
        final float float7 = 1.5f;
        this.body.zRot = 0.1f * Mth.sin(float2 * 1.5f) * 4.0f * float3;
        this.body.y = 2.0f;
        final ModelPart body = this.body;
        body.y -= 2.0f * Mth.cos(float2 * 1.5f) * 2.0f * float3;
        this.leftLeg.xRot = Mth.sin(float2 * 1.5f * 0.5f) * 2.0f * float3;
        this.rightLeg.xRot = Mth.sin(float2 * 1.5f * 0.5f + 3.1415927f) * 2.0f * float3;
        this.leftLeg.zRot = 0.17453292f * Mth.cos(float2 * 1.5f * 0.5f) * float3;
        this.rightLeg.zRot = 0.17453292f * Mth.cos(float2 * 1.5f * 0.5f + 3.1415927f) * float3;
        this.leftLeg.y = 8.0f + 2.0f * Mth.sin(float2 * 1.5f * 0.5f + 3.1415927f) * 2.0f * float3;
        this.rightLeg.y = 8.0f + 2.0f * Mth.sin(float2 * 1.5f * 0.5f) * 2.0f * float3;
        this.bristle0.zRot = -1.2217305f;
        this.bristle1.zRot = -1.134464f;
        this.bristle2.zRot = -0.87266463f;
        this.bristle3.zRot = 0.87266463f;
        this.bristle4.zRot = 1.134464f;
        this.bristle5.zRot = 1.2217305f;
        final float float8 = Mth.cos(float2 * 1.5f + 3.1415927f) * float3;
        final ModelPart bristle0 = this.bristle0;
        bristle0.zRot += float8 * 1.3f;
        final ModelPart bristle2 = this.bristle1;
        bristle2.zRot += float8 * 1.2f;
        final ModelPart bristle3 = this.bristle2;
        bristle3.zRot += float8 * 0.6f;
        final ModelPart bristle4 = this.bristle3;
        bristle4.zRot += float8 * 0.6f;
        final ModelPart bristle5 = this.bristle4;
        bristle5.zRot += float8 * 1.2f;
        final ModelPart bristle6 = this.bristle5;
        bristle6.zRot += float8 * 1.3f;
        final float float9 = 1.0f;
        final float float10 = 1.0f;
        final ModelPart bristle7 = this.bristle0;
        bristle7.zRot += 0.05f * Mth.sin(float4 * 1.0f * -0.4f);
        final ModelPart bristle8 = this.bristle1;
        bristle8.zRot += 0.1f * Mth.sin(float4 * 1.0f * 0.2f);
        final ModelPart bristle9 = this.bristle2;
        bristle9.zRot += 0.1f * Mth.sin(float4 * 1.0f * 0.4f);
        final ModelPart bristle10 = this.bristle3;
        bristle10.zRot += 0.1f * Mth.sin(float4 * 1.0f * 0.4f);
        final ModelPart bristle11 = this.bristle4;
        bristle11.zRot += 0.1f * Mth.sin(float4 * 1.0f * 0.2f);
        final ModelPart bristle12 = this.bristle5;
        bristle12.zRot += 0.05f * Mth.sin(float4 * 1.0f * -0.4f);
    }
    
    public void setRotationAngle(final ModelPart dwf, final float float2, final float float3, final float float4) {
        dwf.xRot = float2;
        dwf.yRot = float3;
        dwf.zRot = float4;
    }
    
    @Override
    public Iterable<ModelPart> parts() {
        return (Iterable<ModelPart>)ImmutableList.of(this.body, this.leftLeg, this.rightLeg);
    }
}

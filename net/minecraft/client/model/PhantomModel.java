package net.minecraft.client.model;

import net.minecraft.util.Mth;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public class PhantomModel<T extends Entity> extends ListModel<T> {
    private final ModelPart body;
    private final ModelPart leftWingBase;
    private final ModelPart leftWingTip;
    private final ModelPart rightWingBase;
    private final ModelPart rightWingTip;
    private final ModelPart tailBase;
    private final ModelPart tailTip;
    
    public PhantomModel() {
        this.texWidth = 64;
        this.texHeight = 64;
        (this.body = new ModelPart(this, 0, 8)).addBox(-3.0f, -2.0f, -8.0f, 5.0f, 3.0f, 9.0f);
        (this.tailBase = new ModelPart(this, 3, 20)).addBox(-2.0f, 0.0f, 0.0f, 3.0f, 2.0f, 6.0f);
        this.tailBase.setPos(0.0f, -2.0f, 1.0f);
        this.body.addChild(this.tailBase);
        (this.tailTip = new ModelPart(this, 4, 29)).addBox(-1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 6.0f);
        this.tailTip.setPos(0.0f, 0.5f, 6.0f);
        this.tailBase.addChild(this.tailTip);
        (this.leftWingBase = new ModelPart(this, 23, 12)).addBox(0.0f, 0.0f, 0.0f, 6.0f, 2.0f, 9.0f);
        this.leftWingBase.setPos(2.0f, -2.0f, -8.0f);
        (this.leftWingTip = new ModelPart(this, 16, 24)).addBox(0.0f, 0.0f, 0.0f, 13.0f, 1.0f, 9.0f);
        this.leftWingTip.setPos(6.0f, 0.0f, 0.0f);
        this.leftWingBase.addChild(this.leftWingTip);
        this.rightWingBase = new ModelPart(this, 23, 12);
        this.rightWingBase.mirror = true;
        this.rightWingBase.addBox(-6.0f, 0.0f, 0.0f, 6.0f, 2.0f, 9.0f);
        this.rightWingBase.setPos(-3.0f, -2.0f, -8.0f);
        this.rightWingTip = new ModelPart(this, 16, 24);
        this.rightWingTip.mirror = true;
        this.rightWingTip.addBox(-13.0f, 0.0f, 0.0f, 13.0f, 1.0f, 9.0f);
        this.rightWingTip.setPos(-6.0f, 0.0f, 0.0f);
        this.rightWingBase.addChild(this.rightWingTip);
        this.leftWingBase.zRot = 0.1f;
        this.leftWingTip.zRot = 0.1f;
        this.rightWingBase.zRot = -0.1f;
        this.rightWingTip.zRot = -0.1f;
        this.body.xRot = -0.1f;
        final ModelPart dwf2 = new ModelPart(this, 0, 0);
        dwf2.addBox(-4.0f, -2.0f, -5.0f, 7.0f, 3.0f, 5.0f);
        dwf2.setPos(0.0f, 1.0f, -7.0f);
        dwf2.xRot = 0.2f;
        this.body.addChild(dwf2);
        this.body.addChild(this.leftWingBase);
        this.body.addChild(this.rightWingBase);
    }
    
    @Override
    public Iterable<ModelPart> parts() {
        return (Iterable<ModelPart>)ImmutableList.of(this.body);
    }
    
    @Override
    public void setupAnim(final T apx, final float float2, final float float3, final float float4, final float float5, final float float6) {
        final float float7 = (apx.getId() * 3 + float4) * 0.13f;
        final float float8 = 16.0f;
        this.leftWingBase.zRot = Mth.cos(float7) * 16.0f * 0.017453292f;
        this.leftWingTip.zRot = Mth.cos(float7) * 16.0f * 0.017453292f;
        this.rightWingBase.zRot = -this.leftWingBase.zRot;
        this.rightWingTip.zRot = -this.leftWingTip.zRot;
        this.tailBase.xRot = -(5.0f + Mth.cos(float7 * 2.0f) * 5.0f) * 0.017453292f;
        this.tailTip.xRot = -(5.0f + Mth.cos(float7 * 2.0f) * 5.0f) * 0.017453292f;
    }
}

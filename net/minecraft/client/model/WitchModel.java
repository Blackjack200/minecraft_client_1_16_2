package net.minecraft.client.model;

import net.minecraft.util.Mth;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public class WitchModel<T extends Entity> extends VillagerModel<T> {
    private boolean holdingItem;
    private final ModelPart mole;
    
    public WitchModel(final float float1) {
        super(float1, 64, 128);
        (this.mole = new ModelPart(this).setTexSize(64, 128)).setPos(0.0f, -2.0f, 0.0f);
        this.mole.texOffs(0, 0).addBox(0.0f, 3.0f, -6.75f, 1.0f, 1.0f, 1.0f, -0.25f);
        this.nose.addChild(this.mole);
        (this.head = new ModelPart(this).setTexSize(64, 128)).setPos(0.0f, 0.0f, 0.0f);
        this.head.texOffs(0, 0).addBox(-4.0f, -10.0f, -4.0f, 8.0f, 10.0f, 8.0f, float1);
        (this.hat = new ModelPart(this).setTexSize(64, 128)).setPos(-5.0f, -10.03125f, -5.0f);
        this.hat.texOffs(0, 64).addBox(0.0f, 0.0f, 0.0f, 10.0f, 2.0f, 10.0f);
        this.head.addChild(this.hat);
        this.head.addChild(this.nose);
        final ModelPart dwf3 = new ModelPart(this).setTexSize(64, 128);
        dwf3.setPos(1.75f, -4.0f, 2.0f);
        dwf3.texOffs(0, 76).addBox(0.0f, 0.0f, 0.0f, 7.0f, 4.0f, 7.0f);
        dwf3.xRot = -0.05235988f;
        dwf3.zRot = 0.02617994f;
        this.hat.addChild(dwf3);
        final ModelPart dwf4 = new ModelPart(this).setTexSize(64, 128);
        dwf4.setPos(1.75f, -4.0f, 2.0f);
        dwf4.texOffs(0, 87).addBox(0.0f, 0.0f, 0.0f, 4.0f, 4.0f, 4.0f);
        dwf4.xRot = -0.10471976f;
        dwf4.zRot = 0.05235988f;
        dwf3.addChild(dwf4);
        final ModelPart dwf5 = new ModelPart(this).setTexSize(64, 128);
        dwf5.setPos(1.75f, -2.0f, 2.0f);
        dwf5.texOffs(0, 95).addBox(0.0f, 0.0f, 0.0f, 1.0f, 2.0f, 1.0f, 0.25f);
        dwf5.xRot = -0.20943952f;
        dwf5.zRot = 0.10471976f;
        dwf4.addChild(dwf5);
    }
    
    @Override
    public void setupAnim(final T apx, final float float2, final float float3, final float float4, final float float5, final float float6) {
        super.setupAnim(apx, float2, float3, float4, float5, float6);
        this.nose.setPos(0.0f, -2.0f, 0.0f);
        final float float7 = 0.01f * (apx.getId() % 10);
        this.nose.xRot = Mth.sin(apx.tickCount * float7) * 4.5f * 0.017453292f;
        this.nose.yRot = 0.0f;
        this.nose.zRot = Mth.cos(apx.tickCount * float7) * 2.5f * 0.017453292f;
        if (this.holdingItem) {
            this.nose.setPos(0.0f, 1.0f, -1.5f);
            this.nose.xRot = -0.9f;
        }
    }
    
    public ModelPart getNose() {
        return this.nose;
    }
    
    public void setHoldingItem(final boolean boolean1) {
        this.holdingItem = boolean1;
    }
}

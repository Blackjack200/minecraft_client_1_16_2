package net.minecraft.client.model;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.npc.AbstractVillager;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public class VillagerModel<T extends Entity> extends ListModel<T> implements HeadedModel, VillagerHeadModel {
    protected ModelPart head;
    protected ModelPart hat;
    protected final ModelPart hatRim;
    protected final ModelPart body;
    protected final ModelPart jacket;
    protected final ModelPart arms;
    protected final ModelPart leg0;
    protected final ModelPart leg1;
    protected final ModelPart nose;
    
    public VillagerModel(final float float1) {
        this(float1, 64, 64);
    }
    
    public VillagerModel(final float float1, final int integer2, final int integer3) {
        final float float2 = 0.5f;
        (this.head = new ModelPart(this).setTexSize(integer2, integer3)).setPos(0.0f, 0.0f, 0.0f);
        this.head.texOffs(0, 0).addBox(-4.0f, -10.0f, -4.0f, 8.0f, 10.0f, 8.0f, float1);
        (this.hat = new ModelPart(this).setTexSize(integer2, integer3)).setPos(0.0f, 0.0f, 0.0f);
        this.hat.texOffs(32, 0).addBox(-4.0f, -10.0f, -4.0f, 8.0f, 10.0f, 8.0f, float1 + 0.5f);
        this.head.addChild(this.hat);
        (this.hatRim = new ModelPart(this).setTexSize(integer2, integer3)).setPos(0.0f, 0.0f, 0.0f);
        this.hatRim.texOffs(30, 47).addBox(-8.0f, -8.0f, -6.0f, 16.0f, 16.0f, 1.0f, float1);
        this.hatRim.xRot = -1.5707964f;
        this.hat.addChild(this.hatRim);
        (this.nose = new ModelPart(this).setTexSize(integer2, integer3)).setPos(0.0f, -2.0f, 0.0f);
        this.nose.texOffs(24, 0).addBox(-1.0f, -1.0f, -6.0f, 2.0f, 4.0f, 2.0f, float1);
        this.head.addChild(this.nose);
        (this.body = new ModelPart(this).setTexSize(integer2, integer3)).setPos(0.0f, 0.0f, 0.0f);
        this.body.texOffs(16, 20).addBox(-4.0f, 0.0f, -3.0f, 8.0f, 12.0f, 6.0f, float1);
        (this.jacket = new ModelPart(this).setTexSize(integer2, integer3)).setPos(0.0f, 0.0f, 0.0f);
        this.jacket.texOffs(0, 38).addBox(-4.0f, 0.0f, -3.0f, 8.0f, 18.0f, 6.0f, float1 + 0.5f);
        this.body.addChild(this.jacket);
        (this.arms = new ModelPart(this).setTexSize(integer2, integer3)).setPos(0.0f, 2.0f, 0.0f);
        this.arms.texOffs(44, 22).addBox(-8.0f, -2.0f, -2.0f, 4.0f, 8.0f, 4.0f, float1);
        this.arms.texOffs(44, 22).addBox(4.0f, -2.0f, -2.0f, 4.0f, 8.0f, 4.0f, float1, true);
        this.arms.texOffs(40, 38).addBox(-4.0f, 2.0f, -2.0f, 8.0f, 4.0f, 4.0f, float1);
        (this.leg0 = new ModelPart(this, 0, 22).setTexSize(integer2, integer3)).setPos(-2.0f, 12.0f, 0.0f);
        this.leg0.addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, float1);
        this.leg1 = new ModelPart(this, 0, 22).setTexSize(integer2, integer3);
        this.leg1.mirror = true;
        this.leg1.setPos(2.0f, 12.0f, 0.0f);
        this.leg1.addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, float1);
    }
    
    @Override
    public Iterable<ModelPart> parts() {
        return (Iterable<ModelPart>)ImmutableList.of(this.head, this.body, this.leg0, this.leg1, this.arms);
    }
    
    @Override
    public void setupAnim(final T apx, final float float2, final float float3, final float float4, final float float5, final float float6) {
        boolean boolean8 = false;
        if (apx instanceof AbstractVillager) {
            boolean8 = (((AbstractVillager)apx).getUnhappyCounter() > 0);
        }
        this.head.yRot = float5 * 0.017453292f;
        this.head.xRot = float6 * 0.017453292f;
        if (boolean8) {
            this.head.zRot = 0.3f * Mth.sin(0.45f * float4);
            this.head.xRot = 0.4f;
        }
        else {
            this.head.zRot = 0.0f;
        }
        this.arms.y = 3.0f;
        this.arms.z = -1.0f;
        this.arms.xRot = -0.75f;
        this.leg0.xRot = Mth.cos(float2 * 0.6662f) * 1.4f * float3 * 0.5f;
        this.leg1.xRot = Mth.cos(float2 * 0.6662f + 3.1415927f) * 1.4f * float3 * 0.5f;
        this.leg0.yRot = 0.0f;
        this.leg1.yRot = 0.0f;
    }
    
    @Override
    public ModelPart getHead() {
        return this.head;
    }
    
    @Override
    public void hatVisible(final boolean boolean1) {
        this.head.visible = boolean1;
        this.hat.visible = boolean1;
        this.hatRim.visible = boolean1;
    }
}

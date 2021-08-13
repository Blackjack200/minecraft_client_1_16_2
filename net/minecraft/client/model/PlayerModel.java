package net.minecraft.client.model;

import net.minecraft.world.entity.Entity;
import java.util.Random;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.EquipmentSlot;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.google.common.collect.Iterables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.geom.ModelPart;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;

public class PlayerModel<T extends LivingEntity> extends HumanoidModel<T> {
    private List<ModelPart> cubes;
    public final ModelPart leftSleeve;
    public final ModelPart rightSleeve;
    public final ModelPart leftPants;
    public final ModelPart rightPants;
    public final ModelPart jacket;
    private final ModelPart cloak;
    private final ModelPart ear;
    private final boolean slim;
    
    public PlayerModel(final float float1, final boolean boolean2) {
        super(RenderType::entityTranslucent, float1, 0.0f, 64, 64);
        this.cubes = (List<ModelPart>)Lists.newArrayList();
        this.slim = boolean2;
        (this.ear = new ModelPart(this, 24, 0)).addBox(-3.0f, -6.0f, -1.0f, 6.0f, 6.0f, 1.0f, float1);
        (this.cloak = new ModelPart(this, 0, 0)).setTexSize(64, 32);
        this.cloak.addBox(-5.0f, 0.0f, -1.0f, 10.0f, 16.0f, 1.0f, float1);
        if (boolean2) {
            (this.leftArm = new ModelPart(this, 32, 48)).addBox(-1.0f, -2.0f, -2.0f, 3.0f, 12.0f, 4.0f, float1);
            this.leftArm.setPos(5.0f, 2.5f, 0.0f);
            (this.rightArm = new ModelPart(this, 40, 16)).addBox(-2.0f, -2.0f, -2.0f, 3.0f, 12.0f, 4.0f, float1);
            this.rightArm.setPos(-5.0f, 2.5f, 0.0f);
            (this.leftSleeve = new ModelPart(this, 48, 48)).addBox(-1.0f, -2.0f, -2.0f, 3.0f, 12.0f, 4.0f, float1 + 0.25f);
            this.leftSleeve.setPos(5.0f, 2.5f, 0.0f);
            (this.rightSleeve = new ModelPart(this, 40, 32)).addBox(-2.0f, -2.0f, -2.0f, 3.0f, 12.0f, 4.0f, float1 + 0.25f);
            this.rightSleeve.setPos(-5.0f, 2.5f, 10.0f);
        }
        else {
            (this.leftArm = new ModelPart(this, 32, 48)).addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, float1);
            this.leftArm.setPos(5.0f, 2.0f, 0.0f);
            (this.leftSleeve = new ModelPart(this, 48, 48)).addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, float1 + 0.25f);
            this.leftSleeve.setPos(5.0f, 2.0f, 0.0f);
            (this.rightSleeve = new ModelPart(this, 40, 32)).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, float1 + 0.25f);
            this.rightSleeve.setPos(-5.0f, 2.0f, 10.0f);
        }
        (this.leftLeg = new ModelPart(this, 16, 48)).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, float1);
        this.leftLeg.setPos(1.9f, 12.0f, 0.0f);
        (this.leftPants = new ModelPart(this, 0, 48)).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, float1 + 0.25f);
        this.leftPants.setPos(1.9f, 12.0f, 0.0f);
        (this.rightPants = new ModelPart(this, 0, 32)).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, float1 + 0.25f);
        this.rightPants.setPos(-1.9f, 12.0f, 0.0f);
        (this.jacket = new ModelPart(this, 16, 32)).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, float1 + 0.25f);
        this.jacket.setPos(0.0f, 0.0f, 0.0f);
    }
    
    @Override
    protected Iterable<ModelPart> bodyParts() {
        return (Iterable<ModelPart>)Iterables.concat((Iterable)super.bodyParts(), (Iterable)ImmutableList.of(this.leftPants, this.rightPants, this.leftSleeve, this.rightSleeve, this.jacket));
    }
    
    public void renderEars(final PoseStack dfj, final VertexConsumer dfn, final int integer3, final int integer4) {
        this.ear.copyFrom(this.head);
        this.ear.x = 0.0f;
        this.ear.y = 0.0f;
        this.ear.render(dfj, dfn, integer3, integer4);
    }
    
    public void renderCloak(final PoseStack dfj, final VertexConsumer dfn, final int integer3, final int integer4) {
        this.cloak.render(dfj, dfn, integer3, integer4);
    }
    
    @Override
    public void setupAnim(final T aqj, final float float2, final float float3, final float float4, final float float5, final float float6) {
        super.setupAnim(aqj, float2, float3, float4, float5, float6);
        this.leftPants.copyFrom(this.leftLeg);
        this.rightPants.copyFrom(this.rightLeg);
        this.leftSleeve.copyFrom(this.leftArm);
        this.rightSleeve.copyFrom(this.rightArm);
        this.jacket.copyFrom(this.body);
        if (aqj.getItemBySlot(EquipmentSlot.CHEST).isEmpty()) {
            if (aqj.isCrouching()) {
                this.cloak.z = 1.4f;
                this.cloak.y = 1.85f;
            }
            else {
                this.cloak.z = 0.0f;
                this.cloak.y = 0.0f;
            }
        }
        else if (aqj.isCrouching()) {
            this.cloak.z = 0.3f;
            this.cloak.y = 0.8f;
        }
        else {
            this.cloak.z = -1.1f;
            this.cloak.y = -0.85f;
        }
    }
    
    @Override
    public void setAllVisible(final boolean boolean1) {
        super.setAllVisible(boolean1);
        this.leftSleeve.visible = boolean1;
        this.rightSleeve.visible = boolean1;
        this.leftPants.visible = boolean1;
        this.rightPants.visible = boolean1;
        this.jacket.visible = boolean1;
        this.cloak.visible = boolean1;
        this.ear.visible = boolean1;
    }
    
    @Override
    public void translateToHand(final HumanoidArm aqf, final PoseStack dfj) {
        final ModelPart dwf4 = this.getArm(aqf);
        if (this.slim) {
            final float float5 = 0.5f * ((aqf == HumanoidArm.RIGHT) ? 1 : -1);
            final ModelPart modelPart = dwf4;
            modelPart.x += float5;
            dwf4.translateAndRotate(dfj);
            final ModelPart modelPart2 = dwf4;
            modelPart2.x -= float5;
        }
        else {
            dwf4.translateAndRotate(dfj);
        }
    }
    
    public ModelPart getRandomModelPart(final Random random) {
        return (ModelPart)this.cubes.get(random.nextInt(this.cubes.size()));
    }
    
    public void accept(final ModelPart dwf) {
        if (this.cubes == null) {
            this.cubes = (List<ModelPart>)Lists.newArrayList();
        }
        this.cubes.add(dwf);
    }
}

package net.minecraft.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import java.util.Arrays;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.boss.wither.WitherBoss;

public class WitherBossModel<T extends WitherBoss> extends ListModel<T> {
    private final ModelPart[] upperBodyParts;
    private final ModelPart[] heads;
    private final ImmutableList<ModelPart> parts;
    
    public WitherBossModel(final float float1) {
        this.texWidth = 64;
        this.texHeight = 64;
        this.upperBodyParts = new ModelPart[3];
        (this.upperBodyParts[0] = new ModelPart(this, 0, 16)).addBox(-10.0f, 3.9f, -0.5f, 20.0f, 3.0f, 3.0f, float1);
        (this.upperBodyParts[1] = new ModelPart(this).setTexSize(this.texWidth, this.texHeight)).setPos(-2.0f, 6.9f, -0.5f);
        this.upperBodyParts[1].texOffs(0, 22).addBox(0.0f, 0.0f, 0.0f, 3.0f, 10.0f, 3.0f, float1);
        this.upperBodyParts[1].texOffs(24, 22).addBox(-4.0f, 1.5f, 0.5f, 11.0f, 2.0f, 2.0f, float1);
        this.upperBodyParts[1].texOffs(24, 22).addBox(-4.0f, 4.0f, 0.5f, 11.0f, 2.0f, 2.0f, float1);
        this.upperBodyParts[1].texOffs(24, 22).addBox(-4.0f, 6.5f, 0.5f, 11.0f, 2.0f, 2.0f, float1);
        (this.upperBodyParts[2] = new ModelPart(this, 12, 22)).addBox(0.0f, 0.0f, 0.0f, 3.0f, 6.0f, 3.0f, float1);
        this.heads = new ModelPart[3];
        (this.heads[0] = new ModelPart(this, 0, 0)).addBox(-4.0f, -4.0f, -4.0f, 8.0f, 8.0f, 8.0f, float1);
        (this.heads[1] = new ModelPart(this, 32, 0)).addBox(-4.0f, -4.0f, -4.0f, 6.0f, 6.0f, 6.0f, float1);
        this.heads[1].x = -8.0f;
        this.heads[1].y = 4.0f;
        (this.heads[2] = new ModelPart(this, 32, 0)).addBox(-4.0f, -4.0f, -4.0f, 6.0f, 6.0f, 6.0f, float1);
        this.heads[2].x = 10.0f;
        this.heads[2].y = 4.0f;
        final ImmutableList.Builder<ModelPart> builder3 = (ImmutableList.Builder<ModelPart>)ImmutableList.builder();
        builder3.addAll((Iterable)Arrays.asList((Object[])this.heads));
        builder3.addAll((Iterable)Arrays.asList((Object[])this.upperBodyParts));
        this.parts = (ImmutableList<ModelPart>)builder3.build();
    }
    
    public ImmutableList<ModelPart> parts() {
        return this.parts;
    }
    
    @Override
    public void setupAnim(final T bci, final float float2, final float float3, final float float4, final float float5, final float float6) {
        final float float7 = Mth.cos(float4 * 0.1f);
        this.upperBodyParts[1].xRot = (0.065f + 0.05f * float7) * 3.1415927f;
        this.upperBodyParts[2].setPos(-2.0f, 6.9f + Mth.cos(this.upperBodyParts[1].xRot) * 10.0f, -0.5f + Mth.sin(this.upperBodyParts[1].xRot) * 10.0f);
        this.upperBodyParts[2].xRot = (0.265f + 0.1f * float7) * 3.1415927f;
        this.heads[0].yRot = float5 * 0.017453292f;
        this.heads[0].xRot = float6 * 0.017453292f;
    }
    
    @Override
    public void prepareMobModel(final T bci, final float float2, final float float3, final float float4) {
        for (int integer6 = 1; integer6 < 3; ++integer6) {
            this.heads[integer6].yRot = (bci.getHeadYRot(integer6 - 1) - bci.yBodyRot) * 0.017453292f;
            this.heads[integer6].xRot = bci.getHeadXRot(integer6 - 1) * 0.017453292f;
        }
    }
}

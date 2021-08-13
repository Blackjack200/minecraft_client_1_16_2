package net.minecraft.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import java.util.Arrays;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.vehicle.Boat;

public class BoatModel extends ListModel<Boat> {
    private final ModelPart[] paddles;
    private final ModelPart waterPatch;
    private final ImmutableList<ModelPart> parts;
    
    public BoatModel() {
        this.paddles = new ModelPart[2];
        final ModelPart[] arr2 = { new ModelPart(this, 0, 0).setTexSize(128, 64), new ModelPart(this, 0, 19).setTexSize(128, 64), new ModelPart(this, 0, 27).setTexSize(128, 64), new ModelPart(this, 0, 35).setTexSize(128, 64), new ModelPart(this, 0, 43).setTexSize(128, 64) };
        final int integer3 = 32;
        final int integer4 = 6;
        final int integer5 = 20;
        final int integer6 = 4;
        final int integer7 = 28;
        arr2[0].addBox(-14.0f, -9.0f, -3.0f, 28.0f, 16.0f, 3.0f, 0.0f);
        arr2[0].setPos(0.0f, 3.0f, 1.0f);
        arr2[1].addBox(-13.0f, -7.0f, -1.0f, 18.0f, 6.0f, 2.0f, 0.0f);
        arr2[1].setPos(-15.0f, 4.0f, 4.0f);
        arr2[2].addBox(-8.0f, -7.0f, -1.0f, 16.0f, 6.0f, 2.0f, 0.0f);
        arr2[2].setPos(15.0f, 4.0f, 0.0f);
        arr2[3].addBox(-14.0f, -7.0f, -1.0f, 28.0f, 6.0f, 2.0f, 0.0f);
        arr2[3].setPos(0.0f, 4.0f, -9.0f);
        arr2[4].addBox(-14.0f, -7.0f, -1.0f, 28.0f, 6.0f, 2.0f, 0.0f);
        arr2[4].setPos(0.0f, 4.0f, 9.0f);
        arr2[0].xRot = 1.5707964f;
        arr2[1].yRot = 4.712389f;
        arr2[2].yRot = 1.5707964f;
        arr2[3].yRot = 3.1415927f;
        (this.paddles[0] = this.makePaddle(true)).setPos(3.0f, -5.0f, 9.0f);
        (this.paddles[1] = this.makePaddle(false)).setPos(3.0f, -5.0f, -9.0f);
        this.paddles[1].yRot = 3.1415927f;
        this.paddles[0].zRot = 0.19634955f;
        this.paddles[1].zRot = 0.19634955f;
        (this.waterPatch = new ModelPart(this, 0, 0).setTexSize(128, 64)).addBox(-14.0f, -9.0f, -3.0f, 28.0f, 16.0f, 3.0f, 0.0f);
        this.waterPatch.setPos(0.0f, -3.0f, 1.0f);
        this.waterPatch.xRot = 1.5707964f;
        final ImmutableList.Builder<ModelPart> builder8 = (ImmutableList.Builder<ModelPart>)ImmutableList.builder();
        builder8.addAll((Iterable)Arrays.asList((Object[])arr2));
        builder8.addAll((Iterable)Arrays.asList((Object[])this.paddles));
        this.parts = (ImmutableList<ModelPart>)builder8.build();
    }
    
    @Override
    public void setupAnim(final Boat bhk, final float float2, final float float3, final float float4, final float float5, final float float6) {
        this.animatePaddle(bhk, 0, float2);
        this.animatePaddle(bhk, 1, float2);
    }
    
    public ImmutableList<ModelPart> parts() {
        return this.parts;
    }
    
    public ModelPart waterPatch() {
        return this.waterPatch;
    }
    
    protected ModelPart makePaddle(final boolean boolean1) {
        final ModelPart dwf3 = new ModelPart(this, 62, boolean1 ? 0 : 20).setTexSize(128, 64);
        final int integer4 = 20;
        final int integer5 = 7;
        final int integer6 = 6;
        final float float7 = -5.0f;
        dwf3.addBox(-1.0f, 0.0f, -5.0f, 2.0f, 2.0f, 18.0f);
        dwf3.addBox(boolean1 ? -1.001f : 0.001f, -3.0f, 8.0f, 1.0f, 6.0f, 7.0f);
        return dwf3;
    }
    
    protected void animatePaddle(final Boat bhk, final int integer, final float float3) {
        final float float4 = bhk.getRowingTime(integer, float3);
        final ModelPart dwf6 = this.paddles[integer];
        dwf6.xRot = (float)Mth.clampedLerp(-1.0471975803375244, -0.2617993950843811, (Mth.sin(-float4) + 1.0f) / 2.0f);
        dwf6.yRot = (float)Mth.clampedLerp(-0.7853981852531433, 0.7853981852531433, (Mth.sin(-float4 + 1.0f) + 1.0f) / 2.0f);
        if (integer == 1) {
            dwf6.yRot = 3.1415927f - dwf6.yRot;
        }
    }
}

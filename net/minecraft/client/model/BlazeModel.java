package net.minecraft.client.model;

import net.minecraft.util.Mth;
import java.util.Arrays;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public class BlazeModel<T extends Entity> extends ListModel<T> {
    private final ModelPart[] upperBodyParts;
    private final ModelPart head;
    private final ImmutableList<ModelPart> parts;
    
    public BlazeModel() {
        (this.head = new ModelPart(this, 0, 0)).addBox(-4.0f, -4.0f, -4.0f, 8.0f, 8.0f, 8.0f);
        this.upperBodyParts = new ModelPart[12];
        for (int integer2 = 0; integer2 < this.upperBodyParts.length; ++integer2) {
            (this.upperBodyParts[integer2] = new ModelPart(this, 0, 16)).addBox(0.0f, 0.0f, 0.0f, 2.0f, 8.0f, 2.0f);
        }
        final ImmutableList.Builder<ModelPart> builder2 = (ImmutableList.Builder<ModelPart>)ImmutableList.builder();
        builder2.add(this.head);
        builder2.addAll((Iterable)Arrays.asList((Object[])this.upperBodyParts));
        this.parts = (ImmutableList<ModelPart>)builder2.build();
    }
    
    @Override
    public Iterable<ModelPart> parts() {
        return (Iterable<ModelPart>)this.parts;
    }
    
    @Override
    public void setupAnim(final T apx, final float float2, final float float3, final float float4, final float float5, final float float6) {
        float float7 = float4 * 3.1415927f * -0.1f;
        for (int integer9 = 0; integer9 < 4; ++integer9) {
            this.upperBodyParts[integer9].y = -2.0f + Mth.cos((integer9 * 2 + float4) * 0.25f);
            this.upperBodyParts[integer9].x = Mth.cos(float7) * 9.0f;
            this.upperBodyParts[integer9].z = Mth.sin(float7) * 9.0f;
            float7 += 1.5707964f;
        }
        float7 = 0.7853982f + float4 * 3.1415927f * 0.03f;
        for (int integer9 = 4; integer9 < 8; ++integer9) {
            this.upperBodyParts[integer9].y = 2.0f + Mth.cos((integer9 * 2 + float4) * 0.25f);
            this.upperBodyParts[integer9].x = Mth.cos(float7) * 7.0f;
            this.upperBodyParts[integer9].z = Mth.sin(float7) * 7.0f;
            float7 += 1.5707964f;
        }
        float7 = 0.47123894f + float4 * 3.1415927f * -0.05f;
        for (int integer9 = 8; integer9 < 12; ++integer9) {
            this.upperBodyParts[integer9].y = 11.0f + Mth.cos((integer9 * 1.5f + float4) * 0.5f);
            this.upperBodyParts[integer9].x = Mth.cos(float7) * 5.0f;
            this.upperBodyParts[integer9].z = Mth.sin(float7) * 5.0f;
            float7 += 1.5707964f;
        }
        this.head.yRot = float5 * 0.017453292f;
        this.head.xRot = float6 * 0.017453292f;
    }
}

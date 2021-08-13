package net.minecraft.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import java.util.Arrays;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.monster.Slime;

public class LavaSlimeModel<T extends Slime> extends ListModel<T> {
    private final ModelPart[] bodyCubes;
    private final ModelPart insideCube;
    private final ImmutableList<ModelPart> parts;
    
    public LavaSlimeModel() {
        this.bodyCubes = new ModelPart[8];
        for (int integer2 = 0; integer2 < this.bodyCubes.length; ++integer2) {
            int integer3 = 0;
            int integer4;
            if ((integer4 = integer2) == 2) {
                integer3 = 24;
                integer4 = 10;
            }
            else if (integer2 == 3) {
                integer3 = 24;
                integer4 = 19;
            }
            (this.bodyCubes[integer2] = new ModelPart(this, integer3, integer4)).addBox(-4.0f, (float)(16 + integer2), -4.0f, 8.0f, 1.0f, 8.0f);
        }
        (this.insideCube = new ModelPart(this, 0, 16)).addBox(-2.0f, 18.0f, -2.0f, 4.0f, 4.0f, 4.0f);
        final ImmutableList.Builder<ModelPart> builder2 = (ImmutableList.Builder<ModelPart>)ImmutableList.builder();
        builder2.add(this.insideCube);
        builder2.addAll((Iterable)Arrays.asList((Object[])this.bodyCubes));
        this.parts = (ImmutableList<ModelPart>)builder2.build();
    }
    
    @Override
    public void setupAnim(final T bdw, final float float2, final float float3, final float float4, final float float5, final float float6) {
    }
    
    @Override
    public void prepareMobModel(final T bdw, final float float2, final float float3, final float float4) {
        float float5 = Mth.lerp(float4, bdw.oSquish, bdw.squish);
        if (float5 < 0.0f) {
            float5 = 0.0f;
        }
        for (int integer7 = 0; integer7 < this.bodyCubes.length; ++integer7) {
            this.bodyCubes[integer7].y = -(4 - integer7) * float5 * 1.7f;
        }
    }
    
    public ImmutableList<ModelPart> parts() {
        return this.parts;
    }
}

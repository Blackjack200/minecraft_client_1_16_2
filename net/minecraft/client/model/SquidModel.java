package net.minecraft.client.model;

import java.util.Arrays;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public class SquidModel<T extends Entity> extends ListModel<T> {
    private final ModelPart body;
    private final ModelPart[] tentacles;
    private final ImmutableList<ModelPart> parts;
    
    public SquidModel() {
        this.tentacles = new ModelPart[8];
        final int integer2 = -16;
        (this.body = new ModelPart(this, 0, 0)).addBox(-6.0f, -8.0f, -6.0f, 12.0f, 16.0f, 12.0f);
        final ModelPart body = this.body;
        body.y += 8.0f;
        for (int integer3 = 0; integer3 < this.tentacles.length; ++integer3) {
            this.tentacles[integer3] = new ModelPart(this, 48, 0);
            double double4 = integer3 * 3.141592653589793 * 2.0 / this.tentacles.length;
            final float float6 = (float)Math.cos(double4) * 5.0f;
            final float float7 = (float)Math.sin(double4) * 5.0f;
            this.tentacles[integer3].addBox(-1.0f, 0.0f, -1.0f, 2.0f, 18.0f, 2.0f);
            this.tentacles[integer3].x = float6;
            this.tentacles[integer3].z = float7;
            this.tentacles[integer3].y = 15.0f;
            double4 = integer3 * 3.141592653589793 * -2.0 / this.tentacles.length + 1.5707963267948966;
            this.tentacles[integer3].yRot = (float)double4;
        }
        final ImmutableList.Builder<ModelPart> builder3 = (ImmutableList.Builder<ModelPart>)ImmutableList.builder();
        builder3.add(this.body);
        builder3.addAll((Iterable)Arrays.asList((Object[])this.tentacles));
        this.parts = (ImmutableList<ModelPart>)builder3.build();
    }
    
    @Override
    public void setupAnim(final T apx, final float float2, final float float3, final float float4, final float float5, final float float6) {
        for (final ModelPart dwf11 : this.tentacles) {
            dwf11.xRot = float4;
        }
    }
    
    @Override
    public Iterable<ModelPart> parts() {
        return (Iterable<ModelPart>)this.parts;
    }
}

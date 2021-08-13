package net.minecraft.client.model;

import net.minecraft.util.Mth;
import java.util.Random;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public class GhastModel<T extends Entity> extends ListModel<T> {
    private final ModelPart[] tentacles;
    private final ImmutableList<ModelPart> parts;
    
    public GhastModel() {
        this.tentacles = new ModelPart[9];
        final ImmutableList.Builder<ModelPart> builder2 = (ImmutableList.Builder<ModelPart>)ImmutableList.builder();
        final ModelPart dwf3 = new ModelPart(this, 0, 0);
        dwf3.addBox(-8.0f, -8.0f, -8.0f, 16.0f, 16.0f, 16.0f);
        dwf3.y = 17.6f;
        builder2.add(dwf3);
        final Random random4 = new Random(1660L);
        for (int integer5 = 0; integer5 < this.tentacles.length; ++integer5) {
            this.tentacles[integer5] = new ModelPart(this, 0, 0);
            final float float6 = ((integer5 % 3 - integer5 / 3 % 2 * 0.5f + 0.25f) / 2.0f * 2.0f - 1.0f) * 5.0f;
            final float float7 = (integer5 / 3 / 2.0f * 2.0f - 1.0f) * 5.0f;
            final int integer6 = random4.nextInt(7) + 8;
            this.tentacles[integer5].addBox(-1.0f, 0.0f, -1.0f, 2.0f, (float)integer6, 2.0f);
            this.tentacles[integer5].x = float6;
            this.tentacles[integer5].z = float7;
            this.tentacles[integer5].y = 24.6f;
            builder2.add(this.tentacles[integer5]);
        }
        this.parts = (ImmutableList<ModelPart>)builder2.build();
    }
    
    @Override
    public void setupAnim(final T apx, final float float2, final float float3, final float float4, final float float5, final float float6) {
        for (int integer8 = 0; integer8 < this.tentacles.length; ++integer8) {
            this.tentacles[integer8].xRot = 0.2f * Mth.sin(float4 * 0.3f + integer8) + 0.4f;
        }
    }
    
    @Override
    public Iterable<ModelPart> parts() {
        return (Iterable<ModelPart>)this.parts;
    }
}

package net.minecraft.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public class SlimeModel<T extends Entity> extends ListModel<T> {
    private final ModelPart cube;
    private final ModelPart eye0;
    private final ModelPart eye1;
    private final ModelPart mouth;
    
    public SlimeModel(final int integer) {
        this.cube = new ModelPart(this, 0, integer);
        this.eye0 = new ModelPart(this, 32, 0);
        this.eye1 = new ModelPart(this, 32, 4);
        this.mouth = new ModelPart(this, 32, 8);
        if (integer > 0) {
            this.cube.addBox(-3.0f, 17.0f, -3.0f, 6.0f, 6.0f, 6.0f);
            this.eye0.addBox(-3.25f, 18.0f, -3.5f, 2.0f, 2.0f, 2.0f);
            this.eye1.addBox(1.25f, 18.0f, -3.5f, 2.0f, 2.0f, 2.0f);
            this.mouth.addBox(0.0f, 21.0f, -3.5f, 1.0f, 1.0f, 1.0f);
        }
        else {
            this.cube.addBox(-4.0f, 16.0f, -4.0f, 8.0f, 8.0f, 8.0f);
        }
    }
    
    @Override
    public void setupAnim(final T apx, final float float2, final float float3, final float float4, final float float5, final float float6) {
    }
    
    @Override
    public Iterable<ModelPart> parts() {
        return (Iterable<ModelPart>)ImmutableList.of(this.cube, this.eye0, this.eye1, this.mouth);
    }
}

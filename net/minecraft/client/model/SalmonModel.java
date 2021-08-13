package net.minecraft.client.model;

import net.minecraft.util.Mth;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public class SalmonModel<T extends Entity> extends ListModel<T> {
    private final ModelPart bodyFront;
    private final ModelPart bodyBack;
    private final ModelPart head;
    private final ModelPart sideFin0;
    private final ModelPart sideFin1;
    
    public SalmonModel() {
        this.texWidth = 32;
        this.texHeight = 32;
        final int integer2 = 20;
        (this.bodyFront = new ModelPart(this, 0, 0)).addBox(-1.5f, -2.5f, 0.0f, 3.0f, 5.0f, 8.0f);
        this.bodyFront.setPos(0.0f, 20.0f, 0.0f);
        (this.bodyBack = new ModelPart(this, 0, 13)).addBox(-1.5f, -2.5f, 0.0f, 3.0f, 5.0f, 8.0f);
        this.bodyBack.setPos(0.0f, 20.0f, 8.0f);
        (this.head = new ModelPart(this, 22, 0)).addBox(-1.0f, -2.0f, -3.0f, 2.0f, 4.0f, 3.0f);
        this.head.setPos(0.0f, 20.0f, 0.0f);
        final ModelPart dwf3 = new ModelPart(this, 20, 10);
        dwf3.addBox(0.0f, -2.5f, 0.0f, 0.0f, 5.0f, 6.0f);
        dwf3.setPos(0.0f, 0.0f, 8.0f);
        this.bodyBack.addChild(dwf3);
        final ModelPart dwf4 = new ModelPart(this, 2, 1);
        dwf4.addBox(0.0f, 0.0f, 0.0f, 0.0f, 2.0f, 3.0f);
        dwf4.setPos(0.0f, -4.5f, 5.0f);
        this.bodyFront.addChild(dwf4);
        final ModelPart dwf5 = new ModelPart(this, 0, 2);
        dwf5.addBox(0.0f, 0.0f, 0.0f, 0.0f, 2.0f, 4.0f);
        dwf5.setPos(0.0f, -4.5f, -1.0f);
        this.bodyBack.addChild(dwf5);
        (this.sideFin0 = new ModelPart(this, -4, 0)).addBox(-2.0f, 0.0f, 0.0f, 2.0f, 0.0f, 2.0f);
        this.sideFin0.setPos(-1.5f, 21.5f, 0.0f);
        this.sideFin0.zRot = -0.7853982f;
        (this.sideFin1 = new ModelPart(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 2.0f, 0.0f, 2.0f);
        this.sideFin1.setPos(1.5f, 21.5f, 0.0f);
        this.sideFin1.zRot = 0.7853982f;
    }
    
    @Override
    public Iterable<ModelPart> parts() {
        return (Iterable<ModelPart>)ImmutableList.of(this.bodyFront, this.bodyBack, this.head, this.sideFin0, this.sideFin1);
    }
    
    @Override
    public void setupAnim(final T apx, final float float2, final float float3, final float float4, final float float5, final float float6) {
        float float7 = 1.0f;
        float float8 = 1.0f;
        if (!apx.isInWater()) {
            float7 = 1.3f;
            float8 = 1.7f;
        }
        this.bodyBack.yRot = -float7 * 0.25f * Mth.sin(float8 * 0.6f * float4);
    }
}

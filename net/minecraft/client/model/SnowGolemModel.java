package net.minecraft.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public class SnowGolemModel<T extends Entity> extends ListModel<T> {
    private final ModelPart piece1;
    private final ModelPart piece2;
    private final ModelPart head;
    private final ModelPart arm1;
    private final ModelPart arm2;
    
    public SnowGolemModel() {
        final float float2 = 4.0f;
        final float float3 = 0.0f;
        (this.head = new ModelPart(this, 0, 0).setTexSize(64, 64)).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, -0.5f);
        this.head.setPos(0.0f, 4.0f, 0.0f);
        (this.arm1 = new ModelPart(this, 32, 0).setTexSize(64, 64)).addBox(-1.0f, 0.0f, -1.0f, 12.0f, 2.0f, 2.0f, -0.5f);
        this.arm1.setPos(0.0f, 6.0f, 0.0f);
        (this.arm2 = new ModelPart(this, 32, 0).setTexSize(64, 64)).addBox(-1.0f, 0.0f, -1.0f, 12.0f, 2.0f, 2.0f, -0.5f);
        this.arm2.setPos(0.0f, 6.0f, 0.0f);
        (this.piece1 = new ModelPart(this, 0, 16).setTexSize(64, 64)).addBox(-5.0f, -10.0f, -5.0f, 10.0f, 10.0f, 10.0f, -0.5f);
        this.piece1.setPos(0.0f, 13.0f, 0.0f);
        (this.piece2 = new ModelPart(this, 0, 36).setTexSize(64, 64)).addBox(-6.0f, -12.0f, -6.0f, 12.0f, 12.0f, 12.0f, -0.5f);
        this.piece2.setPos(0.0f, 24.0f, 0.0f);
    }
    
    @Override
    public void setupAnim(final T apx, final float float2, final float float3, final float float4, final float float5, final float float6) {
        this.head.yRot = float5 * 0.017453292f;
        this.head.xRot = float6 * 0.017453292f;
        this.piece1.yRot = float5 * 0.017453292f * 0.25f;
        final float float7 = Mth.sin(this.piece1.yRot);
        final float float8 = Mth.cos(this.piece1.yRot);
        this.arm1.zRot = 1.0f;
        this.arm2.zRot = -1.0f;
        this.arm1.yRot = 0.0f + this.piece1.yRot;
        this.arm2.yRot = 3.1415927f + this.piece1.yRot;
        this.arm1.x = float8 * 5.0f;
        this.arm1.z = -float7 * 5.0f;
        this.arm2.x = -float8 * 5.0f;
        this.arm2.z = float7 * 5.0f;
    }
    
    @Override
    public Iterable<ModelPart> parts() {
        return (Iterable<ModelPart>)ImmutableList.of(this.piece1, this.piece2, this.head, this.arm1, this.arm2);
    }
    
    public ModelPart getHead() {
        return this.head;
    }
}

package net.minecraft.client.model;

import net.minecraft.util.Mth;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public class DolphinModel<T extends Entity> extends ListModel<T> {
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart tailFin;
    
    public DolphinModel() {
        this.texWidth = 64;
        this.texHeight = 64;
        final float float2 = 18.0f;
        final float float3 = -8.0f;
        (this.body = new ModelPart(this, 22, 0)).addBox(-4.0f, -7.0f, 0.0f, 8.0f, 7.0f, 13.0f);
        this.body.setPos(0.0f, 22.0f, -5.0f);
        final ModelPart dwf4 = new ModelPart(this, 51, 0);
        dwf4.addBox(-0.5f, 0.0f, 8.0f, 1.0f, 4.0f, 5.0f);
        dwf4.xRot = 1.0471976f;
        this.body.addChild(dwf4);
        final ModelPart dwf5 = new ModelPart(this, 48, 20);
        dwf5.mirror = true;
        dwf5.addBox(-0.5f, -4.0f, 0.0f, 1.0f, 4.0f, 7.0f);
        dwf5.setPos(2.0f, -2.0f, 4.0f);
        dwf5.xRot = 1.0471976f;
        dwf5.zRot = 2.0943952f;
        this.body.addChild(dwf5);
        final ModelPart dwf6 = new ModelPart(this, 48, 20);
        dwf6.addBox(-0.5f, -4.0f, 0.0f, 1.0f, 4.0f, 7.0f);
        dwf6.setPos(-2.0f, -2.0f, 4.0f);
        dwf6.xRot = 1.0471976f;
        dwf6.zRot = -2.0943952f;
        this.body.addChild(dwf6);
        (this.tail = new ModelPart(this, 0, 19)).addBox(-2.0f, -2.5f, 0.0f, 4.0f, 5.0f, 11.0f);
        this.tail.setPos(0.0f, -2.5f, 11.0f);
        this.tail.xRot = -0.10471976f;
        this.body.addChild(this.tail);
        (this.tailFin = new ModelPart(this, 19, 20)).addBox(-5.0f, -0.5f, 0.0f, 10.0f, 1.0f, 6.0f);
        this.tailFin.setPos(0.0f, 0.0f, 9.0f);
        this.tailFin.xRot = 0.0f;
        this.tail.addChild(this.tailFin);
        final ModelPart dwf7 = new ModelPart(this, 0, 0);
        dwf7.addBox(-4.0f, -3.0f, -3.0f, 8.0f, 7.0f, 6.0f);
        dwf7.setPos(0.0f, -4.0f, -3.0f);
        final ModelPart dwf8 = new ModelPart(this, 0, 13);
        dwf8.addBox(-1.0f, 2.0f, -7.0f, 2.0f, 2.0f, 4.0f);
        dwf7.addChild(dwf8);
        this.body.addChild(dwf7);
    }
    
    @Override
    public Iterable<ModelPart> parts() {
        return (Iterable<ModelPart>)ImmutableList.of(this.body);
    }
    
    @Override
    public void setupAnim(final T apx, final float float2, final float float3, final float float4, final float float5, final float float6) {
        this.body.xRot = float6 * 0.017453292f;
        this.body.yRot = float5 * 0.017453292f;
        if (Entity.getHorizontalDistanceSqr(apx.getDeltaMovement()) > 1.0E-7) {
            final ModelPart body = this.body;
            body.xRot += -0.05f + -0.05f * Mth.cos(float4 * 0.3f);
            this.tail.xRot = -0.1f * Mth.cos(float4 * 0.3f);
            this.tailFin.xRot = -0.2f * Mth.cos(float4 * 0.3f);
        }
    }
}

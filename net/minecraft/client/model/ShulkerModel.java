package net.minecraft.client.model;

import net.minecraft.world.entity.Entity;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.monster.Shulker;

public class ShulkerModel<T extends Shulker> extends ListModel<T> {
    private final ModelPart base;
    private final ModelPart lid;
    private final ModelPart head;
    
    public ShulkerModel() {
        super(RenderType::entityCutoutNoCullZOffset);
        this.lid = new ModelPart(64, 64, 0, 0);
        this.base = new ModelPart(64, 64, 0, 28);
        this.head = new ModelPart(64, 64, 0, 52);
        this.lid.addBox(-8.0f, -16.0f, -8.0f, 16.0f, 12.0f, 16.0f);
        this.lid.setPos(0.0f, 24.0f, 0.0f);
        this.base.addBox(-8.0f, -8.0f, -8.0f, 16.0f, 8.0f, 16.0f);
        this.base.setPos(0.0f, 24.0f, 0.0f);
        this.head.addBox(-3.0f, 0.0f, -3.0f, 6.0f, 6.0f, 6.0f);
        this.head.setPos(0.0f, 12.0f, 0.0f);
    }
    
    @Override
    public void setupAnim(final T bdt, final float float2, final float float3, final float float4, final float float5, final float float6) {
        final float float7 = float4 - bdt.tickCount;
        final float float8 = (0.5f + bdt.getClientPeekAmount(float7)) * 3.1415927f;
        final float float9 = -1.0f + Mth.sin(float8);
        float float10 = 0.0f;
        if (float8 > 3.1415927f) {
            float10 = Mth.sin(float4 * 0.1f) * 0.7f;
        }
        this.lid.setPos(0.0f, 16.0f + Mth.sin(float8) * 8.0f + float10, 0.0f);
        if (bdt.getClientPeekAmount(float7) > 0.3f) {
            this.lid.yRot = float9 * float9 * float9 * float9 * 3.1415927f * 0.125f;
        }
        else {
            this.lid.yRot = 0.0f;
        }
        this.head.xRot = float6 * 0.017453292f;
        this.head.yRot = (bdt.yHeadRot - 180.0f - bdt.yBodyRot) * 0.017453292f;
    }
    
    @Override
    public Iterable<ModelPart> parts() {
        return (Iterable<ModelPart>)ImmutableList.of(this.base, this.lid);
    }
    
    public ModelPart getBase() {
        return this.base;
    }
    
    public ModelPart getLid() {
        return this.lid;
    }
    
    public ModelPart getHead() {
        return this.head;
    }
}

package net.minecraft.client.model;

import net.minecraft.util.Mth;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public class CodModel<T extends Entity> extends ListModel<T> {
    private final ModelPart body;
    private final ModelPart topFin;
    private final ModelPart head;
    private final ModelPart nose;
    private final ModelPart sideFin0;
    private final ModelPart sideFin1;
    private final ModelPart tailFin;
    
    public CodModel() {
        this.texWidth = 32;
        this.texHeight = 32;
        final int integer2 = 22;
        (this.body = new ModelPart(this, 0, 0)).addBox(-1.0f, -2.0f, 0.0f, 2.0f, 4.0f, 7.0f);
        this.body.setPos(0.0f, 22.0f, 0.0f);
        (this.head = new ModelPart(this, 11, 0)).addBox(-1.0f, -2.0f, -3.0f, 2.0f, 4.0f, 3.0f);
        this.head.setPos(0.0f, 22.0f, 0.0f);
        (this.nose = new ModelPart(this, 0, 0)).addBox(-1.0f, -2.0f, -1.0f, 2.0f, 3.0f, 1.0f);
        this.nose.setPos(0.0f, 22.0f, -3.0f);
        (this.sideFin0 = new ModelPart(this, 22, 1)).addBox(-2.0f, 0.0f, -1.0f, 2.0f, 0.0f, 2.0f);
        this.sideFin0.setPos(-1.0f, 23.0f, 0.0f);
        this.sideFin0.zRot = -0.7853982f;
        (this.sideFin1 = new ModelPart(this, 22, 4)).addBox(0.0f, 0.0f, -1.0f, 2.0f, 0.0f, 2.0f);
        this.sideFin1.setPos(1.0f, 23.0f, 0.0f);
        this.sideFin1.zRot = 0.7853982f;
        (this.tailFin = new ModelPart(this, 22, 3)).addBox(0.0f, -2.0f, 0.0f, 0.0f, 4.0f, 4.0f);
        this.tailFin.setPos(0.0f, 22.0f, 7.0f);
        (this.topFin = new ModelPart(this, 20, -6)).addBox(0.0f, -1.0f, -1.0f, 0.0f, 1.0f, 6.0f);
        this.topFin.setPos(0.0f, 20.0f, 0.0f);
    }
    
    @Override
    public Iterable<ModelPart> parts() {
        return (Iterable<ModelPart>)ImmutableList.of(this.body, this.head, this.nose, this.sideFin0, this.sideFin1, this.tailFin, this.topFin);
    }
    
    @Override
    public void setupAnim(final T apx, final float float2, final float float3, final float float4, final float float5, final float float6) {
        float float7 = 1.0f;
        if (!apx.isInWater()) {
            float7 = 1.5f;
        }
        this.tailFin.yRot = -float7 * 0.45f * Mth.sin(0.6f * float4);
    }
}

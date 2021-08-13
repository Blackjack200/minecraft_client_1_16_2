package net.minecraft.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public class CowModel<T extends Entity> extends QuadrupedModel<T> {
    public CowModel() {
        super(12, 0.0f, false, 10.0f, 4.0f, 2.0f, 2.0f, 24);
        (this.head = new ModelPart(this, 0, 0)).addBox(-4.0f, -4.0f, -6.0f, 8.0f, 8.0f, 6.0f, 0.0f);
        this.head.setPos(0.0f, 4.0f, -8.0f);
        this.head.texOffs(22, 0).addBox(-5.0f, -5.0f, -4.0f, 1.0f, 3.0f, 1.0f, 0.0f);
        this.head.texOffs(22, 0).addBox(4.0f, -5.0f, -4.0f, 1.0f, 3.0f, 1.0f, 0.0f);
        (this.body = new ModelPart(this, 18, 4)).addBox(-6.0f, -10.0f, -7.0f, 12.0f, 18.0f, 10.0f, 0.0f);
        this.body.setPos(0.0f, 5.0f, 2.0f);
        this.body.texOffs(52, 0).addBox(-2.0f, 2.0f, -8.0f, 4.0f, 6.0f, 1.0f);
        final ModelPart leg0 = this.leg0;
        --leg0.x;
        final ModelPart leg2 = this.leg1;
        ++leg2.x;
        final ModelPart leg3 = this.leg0;
        leg3.z += 0.0f;
        final ModelPart leg4 = this.leg1;
        leg4.z += 0.0f;
        final ModelPart leg5 = this.leg2;
        --leg5.x;
        final ModelPart leg6 = this.leg3;
        ++leg6.x;
        final ModelPart leg7 = this.leg2;
        --leg7.z;
        final ModelPart leg8 = this.leg3;
        --leg8.z;
    }
    
    public ModelPart getHead() {
        return this.head;
    }
}

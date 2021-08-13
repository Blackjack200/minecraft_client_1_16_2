package net.minecraft.client.model;

import net.minecraft.world.entity.Entity;

public class PigModel<T extends Entity> extends QuadrupedModel<T> {
    public PigModel() {
        this(0.0f);
    }
    
    public PigModel(final float float1) {
        super(6, float1, false, 4.0f, 4.0f, 2.0f, 2.0f, 24);
        this.head.texOffs(16, 16).addBox(-2.0f, 0.0f, -9.0f, 4.0f, 3.0f, 1.0f, float1);
    }
}

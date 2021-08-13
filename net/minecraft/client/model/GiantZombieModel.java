package net.minecraft.client.model;

import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Giant;

public class GiantZombieModel extends AbstractZombieModel<Giant> {
    public GiantZombieModel() {
        this(0.0f, false);
    }
    
    public GiantZombieModel(final float float1, final boolean boolean2) {
        super(float1, 0.0f, 64, boolean2 ? 32 : 64);
    }
    
    @Override
    public boolean isAggressive(final Giant bdi) {
        return false;
    }
}

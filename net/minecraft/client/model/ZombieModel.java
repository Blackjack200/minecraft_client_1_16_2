package net.minecraft.client.model;

import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;

public class ZombieModel<T extends Zombie> extends AbstractZombieModel<T> {
    public ZombieModel(final float float1, final boolean boolean2) {
        this(float1, 0.0f, 64, boolean2 ? 32 : 64);
    }
    
    protected ZombieModel(final float float1, final float float2, final int integer3, final int integer4) {
        super(float1, float2, integer3, integer4);
    }
    
    @Override
    public boolean isAggressive(final T beg) {
        return beg.isAggressive();
    }
}

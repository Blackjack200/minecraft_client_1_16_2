package net.minecraft.world.entity;

import net.minecraft.nbt.CompoundTag;
import java.util.Random;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;

public class ItemBasedSteering {
    private final SynchedEntityData entityData;
    private final EntityDataAccessor<Integer> boostTimeAccessor;
    private final EntityDataAccessor<Boolean> hasSaddleAccessor;
    public boolean boosting;
    public int boostTime;
    public int boostTimeTotal;
    
    public ItemBasedSteering(final SynchedEntityData uv, final EntityDataAccessor<Integer> us2, final EntityDataAccessor<Boolean> us3) {
        this.entityData = uv;
        this.boostTimeAccessor = us2;
        this.hasSaddleAccessor = us3;
    }
    
    public void onSynced() {
        this.boosting = true;
        this.boostTime = 0;
        this.boostTimeTotal = this.entityData.<Integer>get(this.boostTimeAccessor);
    }
    
    public boolean boost(final Random random) {
        if (this.boosting) {
            return false;
        }
        this.boosting = true;
        this.boostTime = 0;
        this.boostTimeTotal = random.nextInt(841) + 140;
        this.entityData.<Integer>set(this.boostTimeAccessor, this.boostTimeTotal);
        return true;
    }
    
    public void addAdditionalSaveData(final CompoundTag md) {
        md.putBoolean("Saddle", this.hasSaddle());
    }
    
    public void readAdditionalSaveData(final CompoundTag md) {
        this.setSaddle(md.getBoolean("Saddle"));
    }
    
    public void setSaddle(final boolean boolean1) {
        this.entityData.<Boolean>set(this.hasSaddleAccessor, boolean1);
    }
    
    public boolean hasSaddle() {
        return this.entityData.<Boolean>get(this.hasSaddleAccessor);
    }
}

package net.minecraft.world.entity;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.network.syncher.EntityDataAccessor;

public abstract class AgableMob extends PathfinderMob {
    private static final EntityDataAccessor<Boolean> DATA_BABY_ID;
    protected int age;
    protected int forcedAge;
    protected int forcedAgeTimer;
    
    protected AgableMob(final EntityType<? extends AgableMob> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable SpawnGroupData aqz, @Nullable final CompoundTag md) {
        if (aqz == null) {
            aqz = new AgableMobGroupData(true);
        }
        final AgableMobGroupData a7 = (AgableMobGroupData)aqz;
        if (a7.isShouldSpawnBaby() && a7.getGroupSize() > 0 && this.random.nextFloat() <= a7.getBabySpawnChance()) {
            this.setAge(-24000);
        }
        a7.increaseGroupSizeByOne();
        return super.finalizeSpawn(bsh, aop, aqm, aqz, md);
    }
    
    @Nullable
    public abstract AgableMob getBreedOffspring(final ServerLevel aag, final AgableMob apv);
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Boolean>define(AgableMob.DATA_BABY_ID, false);
    }
    
    public boolean canBreed() {
        return false;
    }
    
    public int getAge() {
        if (this.level.isClientSide) {
            return this.entityData.<Boolean>get(AgableMob.DATA_BABY_ID) ? -1 : 1;
        }
        return this.age;
    }
    
    public void ageUp(final int integer, final boolean boolean2) {
        final int integer3;
        int integer2 = integer3 = this.getAge();
        integer2 += integer * 20;
        if (integer2 > 0) {
            integer2 = 0;
        }
        final int integer4 = integer2 - integer3;
        this.setAge(integer2);
        if (boolean2) {
            this.forcedAge += integer4;
            if (this.forcedAgeTimer == 0) {
                this.forcedAgeTimer = 40;
            }
        }
        if (this.getAge() == 0) {
            this.setAge(this.forcedAge);
        }
    }
    
    public void ageUp(final int integer) {
        this.ageUp(integer, false);
    }
    
    public void setAge(final int integer) {
        final int integer2 = this.age;
        this.age = integer;
        if ((integer2 < 0 && integer >= 0) || (integer2 >= 0 && integer < 0)) {
            this.entityData.<Boolean>set(AgableMob.DATA_BABY_ID, integer < 0);
            this.ageBoundaryReached();
        }
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putInt("Age", this.getAge());
        md.putInt("ForcedAge", this.forcedAge);
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.setAge(md.getInt("Age"));
        this.forcedAge = md.getInt("ForcedAge");
    }
    
    @Override
    public void onSyncedDataUpdated(final EntityDataAccessor<?> us) {
        if (AgableMob.DATA_BABY_ID.equals(us)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(us);
    }
    
    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level.isClientSide) {
            if (this.forcedAgeTimer > 0) {
                if (this.forcedAgeTimer % 4 == 0) {
                    this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), 0.0, 0.0, 0.0);
                }
                --this.forcedAgeTimer;
            }
        }
        else if (this.isAlive()) {
            int integer2 = this.getAge();
            if (integer2 < 0) {
                ++integer2;
                this.setAge(integer2);
            }
            else if (integer2 > 0) {
                --integer2;
                this.setAge(integer2);
            }
        }
    }
    
    protected void ageBoundaryReached() {
    }
    
    @Override
    public boolean isBaby() {
        return this.getAge() < 0;
    }
    
    @Override
    public void setBaby(final boolean boolean1) {
        this.setAge(boolean1 ? -24000 : 0);
    }
    
    static {
        DATA_BABY_ID = SynchedEntityData.<Boolean>defineId(AgableMob.class, EntityDataSerializers.BOOLEAN);
    }
    
    public static class AgableMobGroupData implements SpawnGroupData {
        private int groupSize;
        private final boolean shouldSpawnBaby;
        private final float babySpawnChance;
        
        private AgableMobGroupData(final boolean boolean1, final float float2) {
            this.shouldSpawnBaby = boolean1;
            this.babySpawnChance = float2;
        }
        
        public AgableMobGroupData(final boolean boolean1) {
            this(boolean1, 0.05f);
        }
        
        public AgableMobGroupData(final float float1) {
            this(true, float1);
        }
        
        public int getGroupSize() {
            return this.groupSize;
        }
        
        public void increaseGroupSizeByOne() {
            ++this.groupSize;
        }
        
        public boolean isShouldSpawnBaby() {
            return this.shouldSpawnBaby;
        }
        
        public float getBabySpawnChance() {
            return this.babySpawnChance;
        }
    }
}

package net.minecraft.world.entity.raid;

import java.util.Objects;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import java.util.Optional;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import com.google.common.collect.Lists;
import java.util.Iterator;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.core.Position;
import java.util.List;
import java.util.EnumSet;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import java.util.Random;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.PathfindToRaidGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import javax.annotation.Nullable;
import net.minecraft.world.entity.item.ItemEntity;
import java.util.function.Predicate;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.monster.PatrollingMonster;

public abstract class Raider extends PatrollingMonster {
    protected static final EntityDataAccessor<Boolean> IS_CELEBRATING;
    private static final Predicate<ItemEntity> ALLOWED_ITEMS;
    @Nullable
    protected Raid raid;
    private int wave;
    private boolean canJoinRaid;
    private int ticksOutsideRaid;
    
    protected Raider(final EntityType<? extends Raider> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new ObtainRaidLeaderBannerGoal<>(this));
        this.goalSelector.addGoal(3, new PathfindToRaidGoal<>(this));
        this.goalSelector.addGoal(4, new RaiderMoveThroughVillageGoal(this, 1.0499999523162842, 1));
        this.goalSelector.addGoal(5, new RaiderCelebration(this));
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Boolean>define(Raider.IS_CELEBRATING, false);
    }
    
    public abstract void applyRaidBuffs(final int integer, final boolean boolean2);
    
    public boolean canJoinRaid() {
        return this.canJoinRaid;
    }
    
    public void setCanJoinRaid(final boolean boolean1) {
        this.canJoinRaid = boolean1;
    }
    
    @Override
    public void aiStep() {
        if (this.level instanceof ServerLevel && this.isAlive()) {
            final Raid bgy2 = this.getCurrentRaid();
            if (this.canJoinRaid()) {
                if (bgy2 == null) {
                    if (this.level.getGameTime() % 20L == 0L) {
                        final Raid bgy3 = ((ServerLevel)this.level).getRaidAt(this.blockPosition());
                        if (bgy3 != null && Raids.canJoinRaid(this, bgy3)) {
                            bgy3.joinRaid(bgy3.getGroupsSpawned(), this, null, true);
                        }
                    }
                }
                else {
                    final LivingEntity aqj3 = this.getTarget();
                    if (aqj3 != null && (aqj3.getType() == EntityType.PLAYER || aqj3.getType() == EntityType.IRON_GOLEM)) {
                        this.noActionTime = 0;
                    }
                }
            }
        }
        super.aiStep();
    }
    
    @Override
    protected void updateNoActionTime() {
        this.noActionTime += 2;
    }
    
    public void die(final DamageSource aph) {
        if (this.level instanceof ServerLevel) {
            final Entity apx3 = aph.getEntity();
            final Raid bgy4 = this.getCurrentRaid();
            if (bgy4 != null) {
                if (this.isPatrolLeader()) {
                    bgy4.removeLeader(this.getWave());
                }
                if (apx3 != null && apx3.getType() == EntityType.PLAYER) {
                    bgy4.addHeroOfTheVillage(apx3);
                }
                bgy4.removeFromRaid(this, false);
            }
            if (this.isPatrolLeader() && bgy4 == null && ((ServerLevel)this.level).getRaidAt(this.blockPosition()) == null) {
                final ItemStack bly5 = this.getItemBySlot(EquipmentSlot.HEAD);
                Player bft6 = null;
                final Entity apx4 = apx3;
                if (apx4 instanceof Player) {
                    bft6 = (Player)apx4;
                }
                else if (apx4 instanceof Wolf) {
                    final Wolf baw8 = (Wolf)apx4;
                    final LivingEntity aqj9 = baw8.getOwner();
                    if (baw8.isTame() && aqj9 instanceof Player) {
                        bft6 = (Player)aqj9;
                    }
                }
                if (!bly5.isEmpty() && ItemStack.matches(bly5, Raid.getLeaderBannerInstance()) && bft6 != null) {
                    final MobEffectInstance apr8 = bft6.getEffect(MobEffects.BAD_OMEN);
                    int integer9 = 1;
                    if (apr8 != null) {
                        integer9 += apr8.getAmplifier();
                        bft6.removeEffectNoUpdate(MobEffects.BAD_OMEN);
                    }
                    else {
                        --integer9;
                    }
                    integer9 = Mth.clamp(integer9, 0, 4);
                    final MobEffectInstance apr9 = new MobEffectInstance(MobEffects.BAD_OMEN, 120000, integer9, false, false, true);
                    if (!this.level.getGameRules().getBoolean(GameRules.RULE_DISABLE_RAIDS)) {
                        bft6.addEffect(apr9);
                    }
                }
            }
        }
        super.die(aph);
    }
    
    @Override
    public boolean canJoinPatrol() {
        return !this.hasActiveRaid();
    }
    
    public void setCurrentRaid(@Nullable final Raid bgy) {
        this.raid = bgy;
    }
    
    @Nullable
    public Raid getCurrentRaid() {
        return this.raid;
    }
    
    public boolean hasActiveRaid() {
        return this.getCurrentRaid() != null && this.getCurrentRaid().isActive();
    }
    
    public void setWave(final int integer) {
        this.wave = integer;
    }
    
    public int getWave() {
        return this.wave;
    }
    
    public boolean isCelebrating() {
        return this.entityData.<Boolean>get(Raider.IS_CELEBRATING);
    }
    
    public void setCelebrating(final boolean boolean1) {
        this.entityData.<Boolean>set(Raider.IS_CELEBRATING, boolean1);
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putInt("Wave", this.wave);
        md.putBoolean("CanJoinRaid", this.canJoinRaid);
        if (this.raid != null) {
            md.putInt("RaidId", this.raid.getId());
        }
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.wave = md.getInt("Wave");
        this.canJoinRaid = md.getBoolean("CanJoinRaid");
        if (md.contains("RaidId", 3)) {
            if (this.level instanceof ServerLevel) {
                this.raid = ((ServerLevel)this.level).getRaids().get(md.getInt("RaidId"));
            }
            if (this.raid != null) {
                this.raid.addWaveMob(this.wave, this, false);
                if (this.isPatrolLeader()) {
                    this.raid.setLeader(this.wave, this);
                }
            }
        }
    }
    
    @Override
    protected void pickUpItem(final ItemEntity bcs) {
        final ItemStack bly3 = bcs.getItem();
        final boolean boolean4 = this.hasActiveRaid() && this.getCurrentRaid().getLeader(this.getWave()) != null;
        if (this.hasActiveRaid() && !boolean4 && ItemStack.matches(bly3, Raid.getLeaderBannerInstance())) {
            final EquipmentSlot aqc5 = EquipmentSlot.HEAD;
            final ItemStack bly4 = this.getItemBySlot(aqc5);
            final double double7 = this.getEquipmentDropChance(aqc5);
            if (!bly4.isEmpty() && Math.max(this.random.nextFloat() - 0.1f, 0.0f) < double7) {
                this.spawnAtLocation(bly4);
            }
            this.onItemPickup(bcs);
            this.setItemSlot(aqc5, bly3);
            this.take(bcs, bly3.getCount());
            bcs.remove();
            this.getCurrentRaid().setLeader(this.getWave(), this);
            this.setPatrolLeader(true);
        }
        else {
            super.pickUpItem(bcs);
        }
    }
    
    @Override
    public boolean removeWhenFarAway(final double double1) {
        return this.getCurrentRaid() == null && super.removeWhenFarAway(double1);
    }
    
    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.getCurrentRaid() != null;
    }
    
    public int getTicksOutsideRaid() {
        return this.ticksOutsideRaid;
    }
    
    public void setTicksOutsideRaid(final int integer) {
        this.ticksOutsideRaid = integer;
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        if (this.hasActiveRaid()) {
            this.getCurrentRaid().updateBossbar();
        }
        return super.hurt(aph, float2);
    }
    
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable final SpawnGroupData aqz, @Nullable final CompoundTag md) {
        this.setCanJoinRaid(this.getType() != EntityType.WITCH || aqm != MobSpawnType.NATURAL);
        return super.finalizeSpawn(bsh, aop, aqm, aqz, md);
    }
    
    public abstract SoundEvent getCelebrateSound();
    
    static {
        IS_CELEBRATING = SynchedEntityData.<Boolean>defineId(Raider.class, EntityDataSerializers.BOOLEAN);
        ALLOWED_ITEMS = (bcs -> !bcs.hasPickUpDelay() && bcs.isAlive() && ItemStack.matches(bcs.getItem(), Raid.getLeaderBannerInstance()));
    }
    
    public class ObtainRaidLeaderBannerGoal<T extends Raider> extends Goal {
        private final T mob;
        
        public ObtainRaidLeaderBannerGoal(final T bgz2) {
            this.mob = bgz2;
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE));
        }
        
        @Override
        public boolean canUse() {
            final Raid bgy2 = this.mob.getCurrentRaid();
            if (!this.mob.hasActiveRaid() || this.mob.getCurrentRaid().isOver() || !this.mob.canBeLeader() || ItemStack.matches(this.mob.getItemBySlot(EquipmentSlot.HEAD), Raid.getLeaderBannerInstance())) {
                return false;
            }
            final Raider bgz3 = bgy2.getLeader(this.mob.getWave());
            if (bgz3 == null || !bgz3.isAlive()) {
                final List<ItemEntity> list4 = this.mob.level.<ItemEntity>getEntitiesOfClass((java.lang.Class<? extends ItemEntity>)ItemEntity.class, this.mob.getBoundingBox().inflate(16.0, 8.0, 16.0), (java.util.function.Predicate<? super ItemEntity>)Raider.ALLOWED_ITEMS);
                if (!list4.isEmpty()) {
                    return this.mob.getNavigation().moveTo((Entity)list4.get(0), 1.149999976158142);
                }
            }
            return false;
        }
        
        @Override
        public void tick() {
            if (this.mob.getNavigation().getTargetPos().closerThan(this.mob.position(), 1.414)) {
                final List<ItemEntity> list2 = this.mob.level.<ItemEntity>getEntitiesOfClass((java.lang.Class<? extends ItemEntity>)ItemEntity.class, this.mob.getBoundingBox().inflate(4.0, 4.0, 4.0), (java.util.function.Predicate<? super ItemEntity>)Raider.ALLOWED_ITEMS);
                if (!list2.isEmpty()) {
                    this.mob.pickUpItem((ItemEntity)list2.get(0));
                }
            }
        }
    }
    
    public class RaiderCelebration extends Goal {
        private final Raider mob;
        
        RaiderCelebration(final Raider bgz2) {
            this.mob = bgz2;
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE));
        }
        
        @Override
        public boolean canUse() {
            final Raid bgy2 = this.mob.getCurrentRaid();
            return this.mob.isAlive() && this.mob.getTarget() == null && bgy2 != null && bgy2.isLoss();
        }
        
        @Override
        public void start() {
            this.mob.setCelebrating(true);
            super.start();
        }
        
        @Override
        public void stop() {
            this.mob.setCelebrating(false);
            super.stop();
        }
        
        @Override
        public void tick() {
            if (!this.mob.isSilent() && this.mob.random.nextInt(100) == 0) {
                Raider.this.playSound(Raider.this.getCelebrateSound(), LivingEntity.this.getSoundVolume(), LivingEntity.this.getVoicePitch());
            }
            if (!this.mob.isPassenger() && this.mob.random.nextInt(50) == 0) {
                this.mob.getJumpControl().jump();
            }
            super.tick();
        }
    }
    
    public class HoldGroundAttackGoal extends Goal {
        private final Raider mob;
        private final float hostileRadiusSqr;
        public final TargetingConditions shoutTargeting;
        
        public HoldGroundAttackGoal(final AbstractIllager bcv, final float float3) {
            this.shoutTargeting = new TargetingConditions().range(8.0).allowNonAttackable().allowInvulnerable().allowSameTeam().allowUnseeable().ignoreInvisibilityTesting();
            this.mob = bcv;
            this.hostileRadiusSqr = float3 * float3;
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE, (Enum)Flag.LOOK));
        }
        
        @Override
        public boolean canUse() {
            final LivingEntity aqj2 = this.mob.getLastHurtByMob();
            return this.mob.getCurrentRaid() == null && this.mob.isPatrolling() && this.mob.getTarget() != null && !this.mob.isAggressive() && (aqj2 == null || aqj2.getType() != EntityType.PLAYER);
        }
        
        @Override
        public void start() {
            super.start();
            this.mob.getNavigation().stop();
            final List<Raider> list2 = this.mob.level.<Raider>getNearbyEntities((java.lang.Class<? extends Raider>)Raider.class, this.shoutTargeting, (LivingEntity)this.mob, this.mob.getBoundingBox().inflate(8.0, 8.0, 8.0));
            for (final Raider bgz4 : list2) {
                bgz4.setTarget(this.mob.getTarget());
            }
        }
        
        @Override
        public void stop() {
            super.stop();
            final LivingEntity aqj2 = this.mob.getTarget();
            if (aqj2 != null) {
                final List<Raider> list3 = this.mob.level.<Raider>getNearbyEntities((java.lang.Class<? extends Raider>)Raider.class, this.shoutTargeting, (LivingEntity)this.mob, this.mob.getBoundingBox().inflate(8.0, 8.0, 8.0));
                for (final Raider bgz5 : list3) {
                    bgz5.setTarget(aqj2);
                    bgz5.setAggressive(true);
                }
                this.mob.setAggressive(true);
            }
        }
        
        @Override
        public void tick() {
            final LivingEntity aqj2 = this.mob.getTarget();
            if (aqj2 == null) {
                return;
            }
            if (this.mob.distanceToSqr(aqj2) > this.hostileRadiusSqr) {
                this.mob.getLookControl().setLookAt(aqj2, 30.0f, 30.0f);
                if (this.mob.random.nextInt(50) == 0) {
                    this.mob.playAmbientSound();
                }
            }
            else {
                this.mob.setAggressive(true);
            }
            super.tick();
        }
    }
    
    static class RaiderMoveThroughVillageGoal extends Goal {
        private final Raider raider;
        private final double speedModifier;
        private BlockPos poiPos;
        private final List<BlockPos> visited;
        private final int distanceToPoi;
        private boolean stuck;
        
        public RaiderMoveThroughVillageGoal(final Raider bgz, final double double2, final int integer) {
            this.visited = (List<BlockPos>)Lists.newArrayList();
            this.raider = bgz;
            this.speedModifier = double2;
            this.distanceToPoi = integer;
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE));
        }
        
        @Override
        public boolean canUse() {
            this.updateVisited();
            return this.isValidRaid() && this.hasSuitablePoi() && this.raider.getTarget() == null;
        }
        
        private boolean isValidRaid() {
            return this.raider.hasActiveRaid() && !this.raider.getCurrentRaid().isOver();
        }
        
        private boolean hasSuitablePoi() {
            final ServerLevel aag2 = (ServerLevel)this.raider.level;
            final BlockPos fx3 = this.raider.blockPosition();
            final Optional<BlockPos> optional4 = aag2.getPoiManager().getRandom((Predicate<PoiType>)(azo -> azo == PoiType.HOME), (Predicate<BlockPos>)this::hasNotVisited, PoiManager.Occupancy.ANY, fx3, 48, this.raider.random);
            if (!optional4.isPresent()) {
                return false;
            }
            this.poiPos = ((BlockPos)optional4.get()).immutable();
            return true;
        }
        
        @Override
        public boolean canContinueToUse() {
            return !this.raider.getNavigation().isDone() && this.raider.getTarget() == null && !this.poiPos.closerThan(this.raider.position(), this.raider.getBbWidth() + this.distanceToPoi) && !this.stuck;
        }
        
        @Override
        public void stop() {
            if (this.poiPos.closerThan(this.raider.position(), this.distanceToPoi)) {
                this.visited.add(this.poiPos);
            }
        }
        
        @Override
        public void start() {
            super.start();
            this.raider.setNoActionTime(0);
            this.raider.getNavigation().moveTo(this.poiPos.getX(), this.poiPos.getY(), this.poiPos.getZ(), this.speedModifier);
            this.stuck = false;
        }
        
        @Override
        public void tick() {
            if (this.raider.getNavigation().isDone()) {
                final Vec3 dck2 = Vec3.atBottomCenterOf(this.poiPos);
                Vec3 dck3 = RandomPos.getPosTowards(this.raider, 16, 7, dck2, 0.3141592741012573);
                if (dck3 == null) {
                    dck3 = RandomPos.getPosTowards(this.raider, 8, 7, dck2);
                }
                if (dck3 == null) {
                    this.stuck = true;
                    return;
                }
                this.raider.getNavigation().moveTo(dck3.x, dck3.y, dck3.z, this.speedModifier);
            }
        }
        
        private boolean hasNotVisited(final BlockPos fx) {
            for (final BlockPos fx2 : this.visited) {
                if (Objects.equals(fx, fx2)) {
                    return false;
                }
            }
            return true;
        }
        
        private void updateVisited() {
            if (this.visited.size() > 2) {
                this.visited.remove(0);
            }
        }
    }
}

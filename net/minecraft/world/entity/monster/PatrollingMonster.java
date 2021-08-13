package net.minecraft.world.entity.monster;

import net.minecraft.world.entity.Entity;
import java.util.function.Predicate;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Position;
import java.util.EnumSet;
import net.minecraft.world.level.LightLayer;
import java.util.Random;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.EquipmentSlot;
import javax.annotation.Nullable;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.BlockPos;

public abstract class PatrollingMonster extends Monster {
    private BlockPos patrolTarget;
    private boolean patrolLeader;
    private boolean patrolling;
    
    protected PatrollingMonster(final EntityType<? extends PatrollingMonster> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new LongDistancePatrolGoal<>(this, 0.7, 0.595));
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        if (this.patrolTarget != null) {
            md.put("PatrolTarget", (Tag)NbtUtils.writeBlockPos(this.patrolTarget));
        }
        md.putBoolean("PatrolLeader", this.patrolLeader);
        md.putBoolean("Patrolling", this.patrolling);
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        if (md.contains("PatrolTarget")) {
            this.patrolTarget = NbtUtils.readBlockPos(md.getCompound("PatrolTarget"));
        }
        this.patrolLeader = md.getBoolean("PatrolLeader");
        this.patrolling = md.getBoolean("Patrolling");
    }
    
    public double getMyRidingOffset() {
        return -0.45;
    }
    
    public boolean canBeLeader() {
        return true;
    }
    
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable final SpawnGroupData aqz, @Nullable final CompoundTag md) {
        if (aqm != MobSpawnType.PATROL && aqm != MobSpawnType.EVENT && aqm != MobSpawnType.STRUCTURE && this.random.nextFloat() < 0.06f && this.canBeLeader()) {
            this.patrolLeader = true;
        }
        if (this.isPatrolLeader()) {
            this.setItemSlot(EquipmentSlot.HEAD, Raid.getLeaderBannerInstance());
            this.setDropChance(EquipmentSlot.HEAD, 2.0f);
        }
        if (aqm == MobSpawnType.PATROL) {
            this.patrolling = true;
        }
        return super.finalizeSpawn(bsh, aop, aqm, aqz, md);
    }
    
    public static boolean checkPatrollingMonsterSpawnRules(final EntityType<? extends PatrollingMonster> aqb, final LevelAccessor brv, final MobSpawnType aqm, final BlockPos fx, final Random random) {
        return brv.getBrightness(LightLayer.BLOCK, fx) <= 8 && Monster.checkAnyLightMonsterSpawnRules(aqb, brv, aqm, fx, random);
    }
    
    @Override
    public boolean removeWhenFarAway(final double double1) {
        return !this.patrolling || double1 > 16384.0;
    }
    
    public void setPatrolTarget(final BlockPos fx) {
        this.patrolTarget = fx;
        this.patrolling = true;
    }
    
    public BlockPos getPatrolTarget() {
        return this.patrolTarget;
    }
    
    public boolean hasPatrolTarget() {
        return this.patrolTarget != null;
    }
    
    public void setPatrolLeader(final boolean boolean1) {
        this.patrolLeader = boolean1;
        this.patrolling = true;
    }
    
    public boolean isPatrolLeader() {
        return this.patrolLeader;
    }
    
    public boolean canJoinPatrol() {
        return true;
    }
    
    public void findPatrolTarget() {
        this.patrolTarget = this.blockPosition().offset(-500 + this.random.nextInt(1000), 0, -500 + this.random.nextInt(1000));
        this.patrolling = true;
    }
    
    protected boolean isPatrolling() {
        return this.patrolling;
    }
    
    protected void setPatrolling(final boolean boolean1) {
        this.patrolling = boolean1;
    }
    
    public static class LongDistancePatrolGoal<T extends PatrollingMonster> extends Goal {
        private final T mob;
        private final double speedModifier;
        private final double leaderSpeedModifier;
        private long cooldownUntil;
        
        public LongDistancePatrolGoal(final T bdo, final double double2, final double double3) {
            this.mob = bdo;
            this.speedModifier = double2;
            this.leaderSpeedModifier = double3;
            this.cooldownUntil = -1L;
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE));
        }
        
        @Override
        public boolean canUse() {
            final boolean boolean2 = this.mob.level.getGameTime() < this.cooldownUntil;
            return this.mob.isPatrolling() && this.mob.getTarget() == null && !this.mob.isVehicle() && this.mob.hasPatrolTarget() && !boolean2;
        }
        
        @Override
        public void start() {
        }
        
        @Override
        public void stop() {
        }
        
        @Override
        public void tick() {
            final boolean boolean2 = this.mob.isPatrolLeader();
            final PathNavigation ayg3 = this.mob.getNavigation();
            if (ayg3.isDone()) {
                final List<PatrollingMonster> list4 = this.findPatrolCompanions();
                if (this.mob.isPatrolling() && list4.isEmpty()) {
                    this.mob.setPatrolling(false);
                }
                else if (!boolean2 || !this.mob.getPatrolTarget().closerThan(this.mob.position(), 10.0)) {
                    Vec3 dck5 = Vec3.atBottomCenterOf(this.mob.getPatrolTarget());
                    final Vec3 dck6 = this.mob.position();
                    final Vec3 dck7 = dck6.subtract(dck5);
                    dck5 = dck7.yRot(90.0f).scale(0.4).add(dck5);
                    final Vec3 dck8 = dck5.subtract(dck6).normalize().scale(10.0).add(dck6);
                    BlockPos fx9 = new BlockPos(dck8);
                    fx9 = this.mob.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, fx9);
                    if (!ayg3.moveTo(fx9.getX(), fx9.getY(), fx9.getZ(), boolean2 ? this.leaderSpeedModifier : this.speedModifier)) {
                        this.moveRandomly();
                        this.cooldownUntil = this.mob.level.getGameTime() + 200L;
                    }
                    else if (boolean2) {
                        for (final PatrollingMonster bdo11 : list4) {
                            bdo11.setPatrolTarget(fx9);
                        }
                    }
                }
                else {
                    this.mob.findPatrolTarget();
                }
            }
        }
        
        private List<PatrollingMonster> findPatrolCompanions() {
            return this.mob.level.<PatrollingMonster>getEntitiesOfClass((java.lang.Class<? extends PatrollingMonster>)PatrollingMonster.class, this.mob.getBoundingBox().inflate(16.0), (java.util.function.Predicate<? super PatrollingMonster>)(bdo -> bdo.canJoinPatrol() && !bdo.is(this.mob)));
        }
        
        private boolean moveRandomly() {
            final Random random2 = this.mob.getRandom();
            final BlockPos fx3 = this.mob.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, this.mob.blockPosition().offset(-8 + random2.nextInt(16), 0, -8 + random2.nextInt(16)));
            return this.mob.getNavigation().moveTo(fx3.getX(), fx3.getY(), fx3.getZ(), this.speedModifier);
        }
    }
}

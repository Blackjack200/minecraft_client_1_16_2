package net.minecraft.world.entity.monster;

import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.BlockGetter;
import java.util.Random;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.util.Mth;
import net.minecraft.world.level.GameRules;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.util.TimeUtil;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import javax.annotation.Nullable;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.util.IntRange;
import net.minecraft.world.entity.LivingEntity;
import java.util.function.Predicate;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Optional;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import java.util.UUID;
import net.minecraft.world.entity.NeutralMob;

public class EnderMan extends Monster implements NeutralMob {
    private static final UUID SPEED_MODIFIER_ATTACKING_UUID;
    private static final AttributeModifier SPEED_MODIFIER_ATTACKING;
    private static final EntityDataAccessor<Optional<BlockState>> DATA_CARRY_STATE;
    private static final EntityDataAccessor<Boolean> DATA_CREEPY;
    private static final EntityDataAccessor<Boolean> DATA_STARED_AT;
    private static final Predicate<LivingEntity> ENDERMITE_SELECTOR;
    private int lastStareSound;
    private int targetChangeTime;
    private static final IntRange PERSISTENT_ANGER_TIME;
    private int remainingPersistentAngerTime;
    private UUID persistentAngerTarget;
    
    public EnderMan(final EntityType<? extends EnderMan> aqb, final Level bru) {
        super(aqb, bru);
        this.lastStareSound = Integer.MIN_VALUE;
        this.maxUpStep = 1.0f;
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0f);
    }
    
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new EndermanFreezeWhenLookedAt(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0, 0.0f));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(10, new EndermanLeaveBlockGoal(this));
        this.goalSelector.addGoal(11, new EndermanTakeBlockGoal(this));
        this.targetSelector.addGoal(1, new EndermanLookForPlayerGoal(this, (Predicate<LivingEntity>)this::isAngryAt));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this, new Class[0]));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Endermite.class, 10, true, false, EnderMan.ENDERMITE_SELECTOR));
        this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal<>(this, false));
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 40.0).add(Attributes.MOVEMENT_SPEED, 0.30000001192092896).add(Attributes.ATTACK_DAMAGE, 7.0).add(Attributes.FOLLOW_RANGE, 64.0);
    }
    
    @Override
    public void setTarget(@Nullable final LivingEntity aqj) {
        super.setTarget(aqj);
        final AttributeInstance are3 = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (aqj == null) {
            this.targetChangeTime = 0;
            this.entityData.<Boolean>set(EnderMan.DATA_CREEPY, false);
            this.entityData.<Boolean>set(EnderMan.DATA_STARED_AT, false);
            are3.removeModifier(EnderMan.SPEED_MODIFIER_ATTACKING);
        }
        else {
            this.targetChangeTime = this.tickCount;
            this.entityData.<Boolean>set(EnderMan.DATA_CREEPY, true);
            if (!are3.hasModifier(EnderMan.SPEED_MODIFIER_ATTACKING)) {
                are3.addTransientModifier(EnderMan.SPEED_MODIFIER_ATTACKING);
            }
        }
    }
    
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Optional<BlockState>>define(EnderMan.DATA_CARRY_STATE, (Optional<BlockState>)Optional.empty());
        this.entityData.<Boolean>define(EnderMan.DATA_CREEPY, false);
        this.entityData.<Boolean>define(EnderMan.DATA_STARED_AT, false);
    }
    
    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(EnderMan.PERSISTENT_ANGER_TIME.randomValue(this.random));
    }
    
    @Override
    public void setRemainingPersistentAngerTime(final int integer) {
        this.remainingPersistentAngerTime = integer;
    }
    
    @Override
    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }
    
    @Override
    public void setPersistentAngerTarget(@Nullable final UUID uUID) {
        this.persistentAngerTarget = uUID;
    }
    
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }
    
    public void playStareSound() {
        if (this.tickCount >= this.lastStareSound + 400) {
            this.lastStareSound = this.tickCount;
            if (!this.isSilent()) {
                this.level.playLocalSound(this.getX(), this.getEyeY(), this.getZ(), SoundEvents.ENDERMAN_STARE, this.getSoundSource(), 2.5f, 1.0f, false);
            }
        }
    }
    
    public void onSyncedDataUpdated(final EntityDataAccessor<?> us) {
        if (EnderMan.DATA_CREEPY.equals(us) && this.hasBeenStaredAt() && this.level.isClientSide) {
            this.playStareSound();
        }
        super.onSyncedDataUpdated(us);
    }
    
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        final BlockState cee3 = this.getCarriedBlock();
        if (cee3 != null) {
            md.put("carriedBlockState", (Tag)NbtUtils.writeBlockState(cee3));
        }
        this.addPersistentAngerSaveData(md);
    }
    
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        BlockState cee3 = null;
        if (md.contains("carriedBlockState", 10)) {
            cee3 = NbtUtils.readBlockState(md.getCompound("carriedBlockState"));
            if (cee3.isAir()) {
                cee3 = null;
            }
        }
        this.setCarriedBlock(cee3);
        this.readPersistentAngerSaveData((ServerLevel)this.level, md);
    }
    
    private boolean isLookingAtMe(final Player bft) {
        final ItemStack bly3 = bft.inventory.armor.get(3);
        if (bly3.getItem() == Blocks.CARVED_PUMPKIN.asItem()) {
            return false;
        }
        final Vec3 dck4 = bft.getViewVector(1.0f).normalize();
        Vec3 dck5 = new Vec3(this.getX() - bft.getX(), this.getEyeY() - bft.getEyeY(), this.getZ() - bft.getZ());
        final double double6 = dck5.length();
        dck5 = dck5.normalize();
        final double double7 = dck4.dot(dck5);
        return double7 > 1.0 - 0.025 / double6 && bft.canSee(this);
    }
    
    protected float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return 2.55f;
    }
    
    @Override
    public void aiStep() {
        if (this.level.isClientSide) {
            for (int integer2 = 0; integer2 < 2; ++integer2) {
                this.level.addParticle(ParticleTypes.PORTAL, this.getRandomX(0.5), this.getRandomY() - 0.25, this.getRandomZ(0.5), (this.random.nextDouble() - 0.5) * 2.0, -this.random.nextDouble(), (this.random.nextDouble() - 0.5) * 2.0);
            }
        }
        this.jumping = false;
        if (!this.level.isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level, true);
        }
        super.aiStep();
    }
    
    public boolean isSensitiveToWater() {
        return true;
    }
    
    protected void customServerAiStep() {
        if (this.level.isDay() && this.tickCount >= this.targetChangeTime + 600) {
            final float float2 = this.getBrightness();
            if (float2 > 0.5f && this.level.canSeeSky(this.blockPosition()) && this.random.nextFloat() * 30.0f < (float2 - 0.4f) * 2.0f) {
                this.setTarget(null);
                this.teleport();
            }
        }
        super.customServerAiStep();
    }
    
    protected boolean teleport() {
        if (this.level.isClientSide() || !this.isAlive()) {
            return false;
        }
        final double double2 = this.getX() + (this.random.nextDouble() - 0.5) * 64.0;
        final double double3 = this.getY() + (this.random.nextInt(64) - 32);
        final double double4 = this.getZ() + (this.random.nextDouble() - 0.5) * 64.0;
        return this.teleport(double2, double3, double4);
    }
    
    private boolean teleportTowards(final Entity apx) {
        Vec3 dck3 = new Vec3(this.getX() - apx.getX(), this.getY(0.5) - apx.getEyeY(), this.getZ() - apx.getZ());
        dck3 = dck3.normalize();
        final double double4 = 16.0;
        final double double5 = this.getX() + (this.random.nextDouble() - 0.5) * 8.0 - dck3.x * 16.0;
        final double double6 = this.getY() + (this.random.nextInt(16) - 8) - dck3.y * 16.0;
        final double double7 = this.getZ() + (this.random.nextDouble() - 0.5) * 8.0 - dck3.z * 16.0;
        return this.teleport(double5, double6, double7);
    }
    
    private boolean teleport(final double double1, final double double2, final double double3) {
        final BlockPos.MutableBlockPos a8 = new BlockPos.MutableBlockPos(double1, double2, double3);
        while (a8.getY() > 0 && !this.level.getBlockState(a8).getMaterial().blocksMotion()) {
            a8.move(Direction.DOWN);
        }
        final BlockState cee9 = this.level.getBlockState(a8);
        final boolean boolean10 = cee9.getMaterial().blocksMotion();
        final boolean boolean11 = cee9.getFluidState().is(FluidTags.WATER);
        if (!boolean10 || boolean11) {
            return false;
        }
        final boolean boolean12 = this.randomTeleport(double1, double2, double3, true);
        if (boolean12 && !this.isSilent()) {
            this.level.playSound(null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0f, 1.0f);
            this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0f, 1.0f);
        }
        return boolean12;
    }
    
    protected SoundEvent getAmbientSound() {
        return this.isCreepy() ? SoundEvents.ENDERMAN_SCREAM : SoundEvents.ENDERMAN_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.ENDERMAN_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENDERMAN_DEATH;
    }
    
    protected void dropCustomDeathLoot(final DamageSource aph, final int integer, final boolean boolean3) {
        super.dropCustomDeathLoot(aph, integer, boolean3);
        final BlockState cee5 = this.getCarriedBlock();
        if (cee5 != null) {
            this.spawnAtLocation(cee5.getBlock());
        }
    }
    
    public void setCarriedBlock(@Nullable final BlockState cee) {
        this.entityData.<Optional<BlockState>>set(EnderMan.DATA_CARRY_STATE, (Optional<BlockState>)Optional.ofNullable(cee));
    }
    
    @Nullable
    public BlockState getCarriedBlock() {
        return (BlockState)this.entityData.<Optional<BlockState>>get(EnderMan.DATA_CARRY_STATE).orElse(null);
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        if (this.isInvulnerableTo(aph)) {
            return false;
        }
        if (aph instanceof IndirectEntityDamageSource) {
            for (int integer4 = 0; integer4 < 64; ++integer4) {
                if (this.teleport()) {
                    return true;
                }
            }
            return false;
        }
        final boolean boolean4 = super.hurt(aph, float2);
        if (!this.level.isClientSide() && !(aph.getEntity() instanceof LivingEntity) && this.random.nextInt(10) != 0) {
            this.teleport();
        }
        return boolean4;
    }
    
    public boolean isCreepy() {
        return this.entityData.<Boolean>get(EnderMan.DATA_CREEPY);
    }
    
    public boolean hasBeenStaredAt() {
        return this.entityData.<Boolean>get(EnderMan.DATA_STARED_AT);
    }
    
    public void setBeingStaredAt() {
        this.entityData.<Boolean>set(EnderMan.DATA_STARED_AT, true);
    }
    
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.getCarriedBlock() != null;
    }
    
    static {
        SPEED_MODIFIER_ATTACKING_UUID = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
        SPEED_MODIFIER_ATTACKING = new AttributeModifier(EnderMan.SPEED_MODIFIER_ATTACKING_UUID, "Attacking speed boost", 0.15000000596046448, AttributeModifier.Operation.ADDITION);
        DATA_CARRY_STATE = SynchedEntityData.<Optional<BlockState>>defineId(EnderMan.class, EntityDataSerializers.BLOCK_STATE);
        DATA_CREEPY = SynchedEntityData.<Boolean>defineId(EnderMan.class, EntityDataSerializers.BOOLEAN);
        DATA_STARED_AT = SynchedEntityData.<Boolean>defineId(EnderMan.class, EntityDataSerializers.BOOLEAN);
        ENDERMITE_SELECTOR = (aqj -> aqj instanceof Endermite && ((Endermite)aqj).isPlayerSpawned());
        PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    }
    
    static class EndermanLookForPlayerGoal extends NearestAttackableTargetGoal<Player> {
        private final EnderMan enderman;
        private Player pendingTarget;
        private int aggroTime;
        private int teleportTime;
        private final TargetingConditions startAggroTargetConditions;
        private final TargetingConditions continueAggroTargetConditions;
        
        public EndermanLookForPlayerGoal(final EnderMan bdd, @Nullable final Predicate<LivingEntity> predicate) {
            super(bdd, Player.class, 10, false, false, predicate);
            this.continueAggroTargetConditions = new TargetingConditions().allowUnseeable();
            this.enderman = bdd;
            this.startAggroTargetConditions = new TargetingConditions().range(this.getFollowDistance()).selector((Predicate<LivingEntity>)(aqj -> bdd.isLookingAtMe((Player)aqj)));
        }
        
        @Override
        public boolean canUse() {
            this.pendingTarget = this.enderman.level.getNearestPlayer(this.startAggroTargetConditions, this.enderman);
            return this.pendingTarget != null;
        }
        
        @Override
        public void start() {
            this.aggroTime = 5;
            this.teleportTime = 0;
            this.enderman.setBeingStaredAt();
        }
        
        @Override
        public void stop() {
            this.pendingTarget = null;
            super.stop();
        }
        
        @Override
        public boolean canContinueToUse() {
            if (this.pendingTarget == null) {
                return (this.target != null && this.continueAggroTargetConditions.test(this.enderman, this.target)) || super.canContinueToUse();
            }
            if (!this.enderman.isLookingAtMe(this.pendingTarget)) {
                return false;
            }
            this.enderman.lookAt(this.pendingTarget, 10.0f, 10.0f);
            return true;
        }
        
        @Override
        public void tick() {
            if (this.enderman.getTarget() == null) {
                super.setTarget(null);
            }
            if (this.pendingTarget != null) {
                if (--this.aggroTime <= 0) {
                    this.target = this.pendingTarget;
                    this.pendingTarget = null;
                    super.start();
                }
            }
            else {
                if (this.target != null && !this.enderman.isPassenger()) {
                    if (this.enderman.isLookingAtMe((Player)this.target)) {
                        if (this.target.distanceToSqr(this.enderman) < 16.0) {
                            this.enderman.teleport();
                        }
                        this.teleportTime = 0;
                    }
                    else if (this.target.distanceToSqr(this.enderman) > 256.0 && this.teleportTime++ >= 30 && this.enderman.teleportTowards(this.target)) {
                        this.teleportTime = 0;
                    }
                }
                super.tick();
            }
        }
    }
    
    static class EndermanFreezeWhenLookedAt extends Goal {
        private final EnderMan enderman;
        private LivingEntity target;
        
        public EndermanFreezeWhenLookedAt(final EnderMan bdd) {
            this.enderman = bdd;
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.JUMP, (Enum)Flag.MOVE));
        }
        
        @Override
        public boolean canUse() {
            this.target = this.enderman.getTarget();
            if (!(this.target instanceof Player)) {
                return false;
            }
            final double double2 = this.target.distanceToSqr(this.enderman);
            return double2 <= 256.0 && this.enderman.isLookingAtMe((Player)this.target);
        }
        
        @Override
        public void start() {
            this.enderman.getNavigation().stop();
        }
        
        @Override
        public void tick() {
            this.enderman.getLookControl().setLookAt(this.target.getX(), this.target.getEyeY(), this.target.getZ());
        }
    }
    
    static class EndermanLeaveBlockGoal extends Goal {
        private final EnderMan enderman;
        
        public EndermanLeaveBlockGoal(final EnderMan bdd) {
            this.enderman = bdd;
        }
        
        @Override
        public boolean canUse() {
            return this.enderman.getCarriedBlock() != null && this.enderman.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && this.enderman.getRandom().nextInt(2000) == 0;
        }
        
        @Override
        public void tick() {
            final Random random2 = this.enderman.getRandom();
            final Level bru3 = this.enderman.level;
            final int integer4 = Mth.floor(this.enderman.getX() - 1.0 + random2.nextDouble() * 2.0);
            final int integer5 = Mth.floor(this.enderman.getY() + random2.nextDouble() * 2.0);
            final int integer6 = Mth.floor(this.enderman.getZ() - 1.0 + random2.nextDouble() * 2.0);
            final BlockPos fx7 = new BlockPos(integer4, integer5, integer6);
            final BlockState cee8 = bru3.getBlockState(fx7);
            final BlockPos fx8 = fx7.below();
            final BlockState cee9 = bru3.getBlockState(fx8);
            BlockState cee10 = this.enderman.getCarriedBlock();
            if (cee10 == null) {
                return;
            }
            cee10 = Block.updateFromNeighbourShapes(cee10, this.enderman.level, fx7);
            if (this.canPlaceBlock(bru3, fx7, cee10, cee8, cee9, fx8)) {
                bru3.setBlock(fx7, cee10, 3);
                this.enderman.setCarriedBlock(null);
            }
        }
        
        private boolean canPlaceBlock(final Level bru, final BlockPos fx2, final BlockState cee3, final BlockState cee4, final BlockState cee5, final BlockPos fx6) {
            return cee4.isAir() && !cee5.isAir() && !cee5.is(Blocks.BEDROCK) && cee5.isCollisionShapeFullBlock(bru, fx6) && cee3.canSurvive(bru, fx2) && bru.getEntities(this.enderman, AABB.unitCubeFromLowerCorner(Vec3.atLowerCornerOf(fx2))).isEmpty();
        }
    }
    
    static class EndermanTakeBlockGoal extends Goal {
        private final EnderMan enderman;
        
        public EndermanTakeBlockGoal(final EnderMan bdd) {
            this.enderman = bdd;
        }
        
        @Override
        public boolean canUse() {
            return this.enderman.getCarriedBlock() == null && this.enderman.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && this.enderman.getRandom().nextInt(20) == 0;
        }
        
        @Override
        public void tick() {
            final Random random2 = this.enderman.getRandom();
            final Level bru3 = this.enderman.level;
            final int integer4 = Mth.floor(this.enderman.getX() - 2.0 + random2.nextDouble() * 4.0);
            final int integer5 = Mth.floor(this.enderman.getY() + random2.nextDouble() * 3.0);
            final int integer6 = Mth.floor(this.enderman.getZ() - 2.0 + random2.nextDouble() * 4.0);
            final BlockPos fx7 = new BlockPos(integer4, integer5, integer6);
            final BlockState cee8 = bru3.getBlockState(fx7);
            final Block bul9 = cee8.getBlock();
            final Vec3 dck10 = new Vec3(Mth.floor(this.enderman.getX()) + 0.5, integer5 + 0.5, Mth.floor(this.enderman.getZ()) + 0.5);
            final Vec3 dck11 = new Vec3(integer4 + 0.5, integer5 + 0.5, integer6 + 0.5);
            final BlockHitResult dcg12 = bru3.clip(new ClipContext(dck10, dck11, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this.enderman));
            final boolean boolean13 = dcg12.getBlockPos().equals(fx7);
            if (bul9.is(BlockTags.ENDERMAN_HOLDABLE) && boolean13) {
                bru3.removeBlock(fx7, false);
                this.enderman.setCarriedBlock(cee8.getBlock().defaultBlockState());
            }
        }
    }
}

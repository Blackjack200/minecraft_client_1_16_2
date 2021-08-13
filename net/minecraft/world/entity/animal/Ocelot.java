package net.minecraft.world.entity.animal;

import net.minecraft.world.entity.ai.goal.TemptGoal;
import java.util.function.Predicate;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelReader;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import javax.annotation.Nullable;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.OcelotAttackGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.item.crafting.Ingredient;

public class Ocelot extends Animal {
    private static final Ingredient TEMPT_INGREDIENT;
    private static final EntityDataAccessor<Boolean> DATA_TRUSTING;
    private OcelotAvoidEntityGoal<Player> ocelotAvoidPlayersGoal;
    private OcelotTemptGoal temptGoal;
    
    public Ocelot(final EntityType<? extends Ocelot> aqb, final Level bru) {
        super(aqb, bru);
        this.reassessTrustingGoals();
    }
    
    private boolean isTrusting() {
        return this.entityData.<Boolean>get(Ocelot.DATA_TRUSTING);
    }
    
    private void setTrusting(final boolean boolean1) {
        this.entityData.<Boolean>set(Ocelot.DATA_TRUSTING, boolean1);
        this.reassessTrustingGoals();
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putBoolean("Trusting", this.isTrusting());
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.setTrusting(md.getBoolean("Trusting"));
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Boolean>define(Ocelot.DATA_TRUSTING, false);
    }
    
    @Override
    protected void registerGoals() {
        this.temptGoal = new OcelotTemptGoal(this, 0.6, Ocelot.TEMPT_INGREDIENT, true);
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, this.temptGoal);
        this.goalSelector.addGoal(7, new LeapAtTargetGoal(this, 0.3f));
        this.goalSelector.addGoal(8, new OcelotAttackGoal(this));
        this.goalSelector.addGoal(9, new BreedGoal(this, 0.8));
        this.goalSelector.addGoal(10, new WaterAvoidingRandomStrollGoal(this, 0.8, 1.0000001E-5f));
        this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Player.class, 10.0f));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Chicken.class, false));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Turtle.class, 10, false, false, Turtle.BABY_ON_LAND_SELECTOR));
    }
    
    public void customServerAiStep() {
        if (this.getMoveControl().hasWanted()) {
            final double double2 = this.getMoveControl().getSpeedModifier();
            if (double2 == 0.6) {
                this.setPose(Pose.CROUCHING);
                this.setSprinting(false);
            }
            else if (double2 == 1.33) {
                this.setPose(Pose.STANDING);
                this.setSprinting(true);
            }
            else {
                this.setPose(Pose.STANDING);
                this.setSprinting(false);
            }
        }
        else {
            this.setPose(Pose.STANDING);
            this.setSprinting(false);
        }
    }
    
    @Override
    public boolean removeWhenFarAway(final double double1) {
        return !this.isTrusting() && this.tickCount > 2400;
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.MOVEMENT_SPEED, 0.30000001192092896).add(Attributes.ATTACK_DAMAGE, 3.0);
    }
    
    @Override
    public boolean causeFallDamage(final float float1, final float float2) {
        return false;
    }
    
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.OCELOT_AMBIENT;
    }
    
    @Override
    public int getAmbientSoundInterval() {
        return 900;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.OCELOT_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.OCELOT_DEATH;
    }
    
    private float getAttackDamage() {
        return (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }
    
    @Override
    public boolean doHurtTarget(final Entity apx) {
        return apx.hurt(DamageSource.mobAttack(this), this.getAttackDamage());
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        return !this.isInvulnerableTo(aph) && super.hurt(aph, float2);
    }
    
    @Override
    public InteractionResult mobInteract(final Player bft, final InteractionHand aoq) {
        final ItemStack bly4 = bft.getItemInHand(aoq);
        if ((this.temptGoal == null || this.temptGoal.isRunning()) && !this.isTrusting() && this.isFood(bly4) && bft.distanceToSqr(this) < 9.0) {
            this.usePlayerItem(bft, bly4);
            if (!this.level.isClientSide) {
                if (this.random.nextInt(3) == 0) {
                    this.setTrusting(true);
                    this.spawnTrustingParticles(true);
                    this.level.broadcastEntityEvent(this, (byte)41);
                }
                else {
                    this.spawnTrustingParticles(false);
                    this.level.broadcastEntityEvent(this, (byte)40);
                }
            }
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        return super.mobInteract(bft, aoq);
    }
    
    @Override
    public void handleEntityEvent(final byte byte1) {
        if (byte1 == 41) {
            this.spawnTrustingParticles(true);
        }
        else if (byte1 == 40) {
            this.spawnTrustingParticles(false);
        }
        else {
            super.handleEntityEvent(byte1);
        }
    }
    
    private void spawnTrustingParticles(final boolean boolean1) {
        ParticleOptions hf3 = ParticleTypes.HEART;
        if (!boolean1) {
            hf3 = ParticleTypes.SMOKE;
        }
        for (int integer4 = 0; integer4 < 7; ++integer4) {
            final double double5 = this.random.nextGaussian() * 0.02;
            final double double6 = this.random.nextGaussian() * 0.02;
            final double double7 = this.random.nextGaussian() * 0.02;
            this.level.addParticle(hf3, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), double5, double6, double7);
        }
    }
    
    protected void reassessTrustingGoals() {
        if (this.ocelotAvoidPlayersGoal == null) {
            this.ocelotAvoidPlayersGoal = new OcelotAvoidEntityGoal<Player>(this, Player.class, 16.0f, 0.8, 1.33);
        }
        this.goalSelector.removeGoal(this.ocelotAvoidPlayersGoal);
        if (!this.isTrusting()) {
            this.goalSelector.addGoal(4, this.ocelotAvoidPlayersGoal);
        }
    }
    
    @Override
    public Ocelot getBreedOffspring(final ServerLevel aag, final AgableMob apv) {
        return EntityType.OCELOT.create(aag);
    }
    
    @Override
    public boolean isFood(final ItemStack bly) {
        return Ocelot.TEMPT_INGREDIENT.test(bly);
    }
    
    public static boolean checkOcelotSpawnRules(final EntityType<Ocelot> aqb, final LevelAccessor brv, final MobSpawnType aqm, final BlockPos fx, final Random random) {
        return random.nextInt(3) != 0;
    }
    
    @Override
    public boolean checkSpawnObstruction(final LevelReader brw) {
        if (brw.isUnobstructed(this) && !brw.containsAnyLiquid(this.getBoundingBox())) {
            final BlockPos fx3 = this.blockPosition();
            if (fx3.getY() < brw.getSeaLevel()) {
                return false;
            }
            final BlockState cee4 = brw.getBlockState(fx3.below());
            if (cee4.is(Blocks.GRASS_BLOCK) || cee4.is(BlockTags.LEAVES)) {
                return true;
            }
        }
        return false;
    }
    
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable SpawnGroupData aqz, @Nullable final CompoundTag md) {
        if (aqz == null) {
            aqz = new AgableMobGroupData(1.0f);
        }
        return super.finalizeSpawn(bsh, aop, aqm, aqz, md);
    }
    
    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0, 0.5f * this.getEyeHeight(), this.getBbWidth() * 0.4f);
    }
    
    static {
        TEMPT_INGREDIENT = Ingredient.of(Items.COD, Items.SALMON);
        DATA_TRUSTING = SynchedEntityData.<Boolean>defineId(Ocelot.class, EntityDataSerializers.BOOLEAN);
    }
    
    static class OcelotAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
        private final Ocelot ocelot;
        
        public OcelotAvoidEntityGoal(final Ocelot bah, final Class<T> class2, final float float3, final double double4, final double double5) {
            super(bah, class2, float3, double4, double5, (Predicate<LivingEntity>)EntitySelector.NO_CREATIVE_OR_SPECTATOR::test);
            this.ocelot = bah;
        }
        
        @Override
        public boolean canUse() {
            return !this.ocelot.isTrusting() && super.canUse();
        }
        
        @Override
        public boolean canContinueToUse() {
            return !this.ocelot.isTrusting() && super.canContinueToUse();
        }
    }
    
    static class OcelotTemptGoal extends TemptGoal {
        private final Ocelot ocelot;
        
        public OcelotTemptGoal(final Ocelot bah, final double double2, final Ingredient bok, final boolean boolean4) {
            super(bah, double2, bok, boolean4);
            this.ocelot = bah;
        }
        
        @Override
        protected boolean canScare() {
            return super.canScare() && !this.ocelot.isTrusting();
        }
    }
}

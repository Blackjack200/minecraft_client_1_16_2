package net.minecraft.world.entity.monster;

import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

public class Illusioner extends SpellcasterIllager implements RangedAttackMob {
    private int clientSideIllusionTicks;
    private final Vec3[][] clientSideIllusionOffsets;
    
    public Illusioner(final EntityType<? extends Illusioner> aqb, final Level bru) {
        super(aqb, bru);
        this.xpReward = 5;
        this.clientSideIllusionOffsets = new Vec3[2][4];
        for (int integer4 = 0; integer4 < 4; ++integer4) {
            this.clientSideIllusionOffsets[0][integer4] = Vec3.ZERO;
            this.clientSideIllusionOffsets[1][integer4] = Vec3.ZERO;
        }
    }
    
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SpellcasterCastingSpellGoal());
        this.goalSelector.addGoal(4, new IllusionerMirrorSpellGoal());
        this.goalSelector.addGoal(5, new IllusionerBlindnessSpellGoal());
        this.goalSelector.addGoal(6, new RangedBowAttackGoal<>(this, 0.5, 20, 15.0f));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0f, 1.0f));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0f));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[] { Raider.class }).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, false).setUnseenMemoryTicks(300));
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.5).add(Attributes.FOLLOW_RANGE, 18.0).add(Attributes.MAX_HEALTH, 32.0);
    }
    
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable final SpawnGroupData aqz, @Nullable final CompoundTag md) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        return super.finalizeSpawn(bsh, aop, aqm, aqz, md);
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }
    
    public AABB getBoundingBoxForCulling() {
        return this.getBoundingBox().inflate(3.0, 0.0, 3.0);
    }
    
    public void aiStep() {
        super.aiStep();
        if (this.level.isClientSide && this.isInvisible()) {
            --this.clientSideIllusionTicks;
            if (this.clientSideIllusionTicks < 0) {
                this.clientSideIllusionTicks = 0;
            }
            if (this.hurtTime == 1 || this.tickCount % 1200 == 0) {
                this.clientSideIllusionTicks = 3;
                final float float2 = -6.0f;
                final int integer3 = 13;
                for (int integer4 = 0; integer4 < 4; ++integer4) {
                    this.clientSideIllusionOffsets[0][integer4] = this.clientSideIllusionOffsets[1][integer4];
                    this.clientSideIllusionOffsets[1][integer4] = new Vec3((-6.0f + this.random.nextInt(13)) * 0.5, Math.max(0, this.random.nextInt(6) - 4), (-6.0f + this.random.nextInt(13)) * 0.5);
                }
                for (int integer4 = 0; integer4 < 16; ++integer4) {
                    this.level.addParticle(ParticleTypes.CLOUD, this.getRandomX(0.5), this.getRandomY(), this.getZ(0.5), 0.0, 0.0, 0.0);
                }
                this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ILLUSIONER_MIRROR_MOVE, this.getSoundSource(), 1.0f, 1.0f, false);
            }
            else if (this.hurtTime == this.hurtDuration - 1) {
                this.clientSideIllusionTicks = 3;
                for (int integer5 = 0; integer5 < 4; ++integer5) {
                    this.clientSideIllusionOffsets[0][integer5] = this.clientSideIllusionOffsets[1][integer5];
                    this.clientSideIllusionOffsets[1][integer5] = new Vec3(0.0, 0.0, 0.0);
                }
            }
        }
    }
    
    public SoundEvent getCelebrateSound() {
        return SoundEvents.ILLUSIONER_AMBIENT;
    }
    
    public Vec3[] getIllusionOffsets(final float float1) {
        if (this.clientSideIllusionTicks <= 0) {
            return this.clientSideIllusionOffsets[1];
        }
        double double3 = (this.clientSideIllusionTicks - float1) / 3.0f;
        double3 = Math.pow(double3, 0.25);
        final Vec3[] arr5 = new Vec3[4];
        for (int integer6 = 0; integer6 < 4; ++integer6) {
            arr5[integer6] = this.clientSideIllusionOffsets[1][integer6].scale(1.0 - double3).add(this.clientSideIllusionOffsets[0][integer6].scale(double3));
        }
        return arr5;
    }
    
    public boolean isAlliedTo(final Entity apx) {
        return super.isAlliedTo(apx) || (apx instanceof LivingEntity && ((LivingEntity)apx).getMobType() == MobType.ILLAGER && this.getTeam() == null && apx.getTeam() == null);
    }
    
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ILLUSIONER_AMBIENT;
    }
    
    protected SoundEvent getDeathSound() {
        return SoundEvents.ILLUSIONER_DEATH;
    }
    
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.ILLUSIONER_HURT;
    }
    
    @Override
    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.ILLUSIONER_CAST_SPELL;
    }
    
    public void applyRaidBuffs(final int integer, final boolean boolean2) {
    }
    
    @Override
    public void performRangedAttack(final LivingEntity aqj, final float float2) {
        final ItemStack bly4 = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW)));
        final AbstractArrow bfx5 = ProjectileUtil.getMobArrow(this, bly4, float2);
        final double double6 = aqj.getX() - this.getX();
        final double double7 = aqj.getY(0.3333333333333333) - bfx5.getY();
        final double double8 = aqj.getZ() - this.getZ();
        final double double9 = Mth.sqrt(double6 * double6 + double8 * double8);
        bfx5.shoot(double6, double7 + double9 * 0.20000000298023224, double8, 1.6f, (float)(14 - this.level.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
        this.level.addFreshEntity(bfx5);
    }
    
    @Override
    public IllagerArmPose getArmPose() {
        if (this.isCastingSpell()) {
            return IllagerArmPose.SPELLCASTING;
        }
        if (this.isAggressive()) {
            return IllagerArmPose.BOW_AND_ARROW;
        }
        return IllagerArmPose.CROSSED;
    }
    
    class IllusionerMirrorSpellGoal extends SpellcasterUseSpellGoal {
        private IllusionerMirrorSpellGoal() {
        }
        
        @Override
        public boolean canUse() {
            return super.canUse() && !Illusioner.this.hasEffect(MobEffects.INVISIBILITY);
        }
        
        @Override
        protected int getCastingTime() {
            return 20;
        }
        
        @Override
        protected int getCastingInterval() {
            return 340;
        }
        
        @Override
        protected void performSpellCasting() {
            Illusioner.this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 1200));
        }
        
        @Nullable
        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ILLUSIONER_PREPARE_MIRROR;
        }
        
        @Override
        protected IllagerSpell getSpell() {
            return IllagerSpell.DISAPPEAR;
        }
    }
    
    class IllusionerBlindnessSpellGoal extends SpellcasterUseSpellGoal {
        private int lastTargetId;
        
        private IllusionerBlindnessSpellGoal() {
        }
        
        @Override
        public boolean canUse() {
            return super.canUse() && Illusioner.this.getTarget() != null && Illusioner.this.getTarget().getId() != this.lastTargetId && Illusioner.this.level.getCurrentDifficultyAt(Illusioner.this.blockPosition()).isHarderThan((float)Difficulty.NORMAL.ordinal());
        }
        
        @Override
        public void start() {
            super.start();
            this.lastTargetId = Illusioner.this.getTarget().getId();
        }
        
        @Override
        protected int getCastingTime() {
            return 20;
        }
        
        @Override
        protected int getCastingInterval() {
            return 180;
        }
        
        @Override
        protected void performSpellCasting() {
            Illusioner.this.getTarget().addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 400));
        }
        
        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ILLUSIONER_PREPARE_BLINDNESS;
        }
        
        @Override
        protected IllagerSpell getSpell() {
            return IllagerSpell.BLINDNESS;
        }
    }
}

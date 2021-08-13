package net.minecraft.world.entity.animal;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.phys.Vec3;
import javax.annotation.Nullable;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import java.util.function.Consumer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import java.util.function.Predicate;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.Shearable;

public class SnowGolem extends AbstractGolem implements Shearable, RangedAttackMob {
    private static final EntityDataAccessor<Byte> DATA_PUMPKIN_ID;
    
    public SnowGolem(final EntityType<? extends SnowGolem> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new RangedAttackGoal(this, 1.25, 20, 10.0f));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0, 1.0000001E-5f));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Mob.class, 10, true, false, (Predicate<LivingEntity>)(aqj -> aqj instanceof Enemy)));
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0).add(Attributes.MOVEMENT_SPEED, 0.20000000298023224);
    }
    
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Byte>define(SnowGolem.DATA_PUMPKIN_ID, (Byte)16);
    }
    
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putBoolean("Pumpkin", this.hasPumpkin());
    }
    
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        if (md.contains("Pumpkin")) {
            this.setPumpkin(md.getBoolean("Pumpkin"));
        }
    }
    
    public boolean isSensitiveToWater() {
        return true;
    }
    
    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide) {
            int integer2 = Mth.floor(this.getX());
            int integer3 = Mth.floor(this.getY());
            int integer4 = Mth.floor(this.getZ());
            if (this.level.getBiome(new BlockPos(integer2, 0, integer4)).getTemperature(new BlockPos(integer2, integer3, integer4)) > 1.0f) {
                this.hurt(DamageSource.ON_FIRE, 1.0f);
            }
            if (!this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                return;
            }
            final BlockState cee5 = Blocks.SNOW.defaultBlockState();
            for (int integer5 = 0; integer5 < 4; ++integer5) {
                integer2 = Mth.floor(this.getX() + (integer5 % 2 * 2 - 1) * 0.25f);
                integer3 = Mth.floor(this.getY());
                integer4 = Mth.floor(this.getZ() + (integer5 / 2 % 2 * 2 - 1) * 0.25f);
                final BlockPos fx7 = new BlockPos(integer2, integer3, integer4);
                if (this.level.getBlockState(fx7).isAir() && this.level.getBiome(fx7).getTemperature(fx7) < 0.8f && cee5.canSurvive(this.level, fx7)) {
                    this.level.setBlockAndUpdate(fx7, cee5);
                }
            }
        }
    }
    
    @Override
    public void performRangedAttack(final LivingEntity aqj, final float float2) {
        final Snowball bgn4 = new Snowball(this.level, this);
        final double double5 = aqj.getEyeY() - 1.100000023841858;
        final double double6 = aqj.getX() - this.getX();
        final double double7 = double5 - bgn4.getY();
        final double double8 = aqj.getZ() - this.getZ();
        final float float3 = Mth.sqrt(double6 * double6 + double8 * double8) * 0.2f;
        bgn4.shoot(double6, double7 + float3, double8, 1.6f, 12.0f);
        this.playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1.0f, 0.4f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
        this.level.addFreshEntity(bgn4);
    }
    
    protected float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return 1.7f;
    }
    
    protected InteractionResult mobInteract(final Player bft, final InteractionHand aoq) {
        final ItemStack bly4 = bft.getItemInHand(aoq);
        if (bly4.getItem() == Items.SHEARS && this.readyForShearing()) {
            this.shear(SoundSource.PLAYERS);
            if (!this.level.isClientSide) {
                bly4.<Player>hurtAndBreak(1, bft, (java.util.function.Consumer<Player>)(bft -> bft.broadcastBreakEvent(aoq)));
            }
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        return InteractionResult.PASS;
    }
    
    @Override
    public void shear(final SoundSource adp) {
        this.level.playSound(null, this, SoundEvents.SNOW_GOLEM_SHEAR, adp, 1.0f, 1.0f);
        if (!this.level.isClientSide()) {
            this.setPumpkin(false);
            this.spawnAtLocation(new ItemStack(Items.CARVED_PUMPKIN), 1.7f);
        }
    }
    
    @Override
    public boolean readyForShearing() {
        return this.isAlive() && this.hasPumpkin();
    }
    
    public boolean hasPumpkin() {
        return (this.entityData.<Byte>get(SnowGolem.DATA_PUMPKIN_ID) & 0x10) != 0x0;
    }
    
    public void setPumpkin(final boolean boolean1) {
        final byte byte3 = this.entityData.<Byte>get(SnowGolem.DATA_PUMPKIN_ID);
        if (boolean1) {
            this.entityData.<Byte>set(SnowGolem.DATA_PUMPKIN_ID, (byte)(byte3 | 0x10));
        }
        else {
            this.entityData.<Byte>set(SnowGolem.DATA_PUMPKIN_ID, (byte)(byte3 & 0xFFFFFFEF));
        }
    }
    
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SNOW_GOLEM_AMBIENT;
    }
    
    @Nullable
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.SNOW_GOLEM_HURT;
    }
    
    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SNOW_GOLEM_DEATH;
    }
    
    public Vec3 getLeashOffset() {
        return new Vec3(0.0, 0.75f * this.getEyeHeight(), this.getBbWidth() * 0.4f);
    }
    
    static {
        DATA_PUMPKIN_ID = SynchedEntityData.<Byte>defineId(SnowGolem.class, EntityDataSerializers.BYTE);
    }
}

package net.minecraft.world.entity.animal.horse;

import java.util.function.Predicate;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.phys.Vec3;
import java.util.Iterator;
import net.minecraft.world.entity.projectile.LlamaSpit;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.WoolCarpetBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.Container;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.LlamaFollowCaravanGoal;
import net.minecraft.world.entity.ai.goal.RunAroundLikeCrazyGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import javax.annotation.Nullable;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.entity.monster.RangedAttackMob;

public class Llama extends AbstractChestedHorse implements RangedAttackMob {
    private static final Ingredient FOOD_ITEMS;
    private static final EntityDataAccessor<Integer> DATA_STRENGTH_ID;
    private static final EntityDataAccessor<Integer> DATA_SWAG_ID;
    private static final EntityDataAccessor<Integer> DATA_VARIANT_ID;
    private boolean didSpit;
    @Nullable
    private Llama caravanHead;
    @Nullable
    private Llama caravanTail;
    
    public Llama(final EntityType<? extends Llama> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    public boolean isTraderLlama() {
        return false;
    }
    
    private void setStrength(final int integer) {
        this.entityData.<Integer>set(Llama.DATA_STRENGTH_ID, Math.max(1, Math.min(5, integer)));
    }
    
    private void setRandomStrength() {
        final int integer2 = (this.random.nextFloat() < 0.04f) ? 5 : 3;
        this.setStrength(1 + this.random.nextInt(integer2));
    }
    
    public int getStrength() {
        return this.entityData.<Integer>get(Llama.DATA_STRENGTH_ID);
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putInt("Variant", this.getVariant());
        md.putInt("Strength", this.getStrength());
        if (!this.inventory.getItem(1).isEmpty()) {
            md.put("DecorItem", (Tag)this.inventory.getItem(1).save(new CompoundTag()));
        }
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        this.setStrength(md.getInt("Strength"));
        super.readAdditionalSaveData(md);
        this.setVariant(md.getInt("Variant"));
        if (md.contains("DecorItem", 10)) {
            this.inventory.setItem(1, ItemStack.of(md.getCompound("DecorItem")));
        }
        this.updateContainerEquipment();
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RunAroundLikeCrazyGoal(this, 1.2));
        this.goalSelector.addGoal(2, new LlamaFollowCaravanGoal(this, 2.0999999046325684));
        this.goalSelector.addGoal(3, new RangedAttackGoal(this, 1.25, 40, 20.0f));
        this.goalSelector.addGoal(3, new PanicGoal(this, 1.2));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.0));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.7));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new LlamaHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new LlamaAttackWolfGoal(this));
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return AbstractChestedHorse.createBaseChestedHorseAttributes().add(Attributes.FOLLOW_RANGE, 40.0);
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Integer>define(Llama.DATA_STRENGTH_ID, 0);
        this.entityData.<Integer>define(Llama.DATA_SWAG_ID, -1);
        this.entityData.<Integer>define(Llama.DATA_VARIANT_ID, 0);
    }
    
    public int getVariant() {
        return Mth.clamp(this.entityData.<Integer>get(Llama.DATA_VARIANT_ID), 0, 3);
    }
    
    public void setVariant(final int integer) {
        this.entityData.<Integer>set(Llama.DATA_VARIANT_ID, integer);
    }
    
    @Override
    protected int getInventorySize() {
        if (this.hasChest()) {
            return 2 + 3 * this.getInventoryColumns();
        }
        return super.getInventorySize();
    }
    
    @Override
    public void positionRider(final Entity apx) {
        if (!this.hasPassenger(apx)) {
            return;
        }
        final float float3 = Mth.cos(this.yBodyRot * 0.017453292f);
        final float float4 = Mth.sin(this.yBodyRot * 0.017453292f);
        final float float5 = 0.3f;
        apx.setPos(this.getX() + 0.3f * float4, this.getY() + this.getPassengersRidingOffset() + apx.getMyRidingOffset(), this.getZ() - 0.3f * float3);
    }
    
    @Override
    public double getPassengersRidingOffset() {
        return this.getBbHeight() * 0.67;
    }
    
    @Override
    public boolean canBeControlledByRider() {
        return false;
    }
    
    @Override
    public boolean isFood(final ItemStack bly) {
        return Llama.FOOD_ITEMS.test(bly);
    }
    
    @Override
    protected boolean handleEating(final Player bft, final ItemStack bly) {
        int integer4 = 0;
        int integer5 = 0;
        float float6 = 0.0f;
        boolean boolean7 = false;
        final Item blu8 = bly.getItem();
        if (blu8 == Items.WHEAT) {
            integer4 = 10;
            integer5 = 3;
            float6 = 2.0f;
        }
        else if (blu8 == Blocks.HAY_BLOCK.asItem()) {
            integer4 = 90;
            integer5 = 6;
            float6 = 10.0f;
            if (this.isTamed() && this.getAge() == 0 && this.canFallInLove()) {
                boolean7 = true;
                this.setInLove(bft);
            }
        }
        if (this.getHealth() < this.getMaxHealth() && float6 > 0.0f) {
            this.heal(float6);
            boolean7 = true;
        }
        if (this.isBaby() && integer4 > 0) {
            this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), 0.0, 0.0, 0.0);
            if (!this.level.isClientSide) {
                this.ageUp(integer4);
            }
            boolean7 = true;
        }
        if (integer5 > 0 && (boolean7 || !this.isTamed()) && this.getTemper() < this.getMaxTemper()) {
            boolean7 = true;
            if (!this.level.isClientSide) {
                this.modifyTemper(integer5);
            }
        }
        if (boolean7 && !this.isSilent()) {
            final SoundEvent adn9 = this.getEatingSound();
            if (adn9 != null) {
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), this.getEatingSound(), this.getSoundSource(), 1.0f, 1.0f + (this.random.nextFloat() - this.random.nextFloat()) * 0.2f);
            }
        }
        return boolean7;
    }
    
    @Override
    protected boolean isImmobile() {
        return this.isDeadOrDying() || this.isEating();
    }
    
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable SpawnGroupData aqz, @Nullable final CompoundTag md) {
        this.setRandomStrength();
        int integer7;
        if (aqz instanceof LlamaGroupData) {
            integer7 = ((LlamaGroupData)aqz).variant;
        }
        else {
            integer7 = this.random.nextInt(4);
            aqz = new LlamaGroupData(integer7);
        }
        this.setVariant(integer7);
        return super.finalizeSpawn(bsh, aop, aqm, aqz, md);
    }
    
    @Override
    protected SoundEvent getAngrySound() {
        return SoundEvents.LLAMA_ANGRY;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.LLAMA_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.LLAMA_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.LLAMA_DEATH;
    }
    
    @Nullable
    @Override
    protected SoundEvent getEatingSound() {
        return SoundEvents.LLAMA_EAT;
    }
    
    @Override
    protected void playStepSound(final BlockPos fx, final BlockState cee) {
        this.playSound(SoundEvents.LLAMA_STEP, 0.15f, 1.0f);
    }
    
    @Override
    protected void playChestEquipsSound() {
        this.playSound(SoundEvents.LLAMA_CHEST, 1.0f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
    }
    
    @Override
    public void makeMad() {
        final SoundEvent adn2 = this.getAngrySound();
        if (adn2 != null) {
            this.playSound(adn2, this.getSoundVolume(), this.getVoicePitch());
        }
    }
    
    @Override
    public int getInventoryColumns() {
        return this.getStrength();
    }
    
    @Override
    public boolean canWearArmor() {
        return true;
    }
    
    @Override
    public boolean isWearingArmor() {
        return !this.inventory.getItem(1).isEmpty();
    }
    
    @Override
    public boolean isArmor(final ItemStack bly) {
        final Item blu3 = bly.getItem();
        return ItemTags.CARPETS.contains(blu3);
    }
    
    @Override
    public boolean isSaddleable() {
        return false;
    }
    
    @Override
    public void containerChanged(final Container aok) {
        final DyeColor bku3 = this.getSwag();
        super.containerChanged(aok);
        final DyeColor bku4 = this.getSwag();
        if (this.tickCount > 20 && bku4 != null && bku4 != bku3) {
            this.playSound(SoundEvents.LLAMA_SWAG, 0.5f, 1.0f);
        }
    }
    
    @Override
    protected void updateContainerEquipment() {
        if (this.level.isClientSide) {
            return;
        }
        super.updateContainerEquipment();
        this.setSwag(getDyeColor(this.inventory.getItem(1)));
    }
    
    private void setSwag(@Nullable final DyeColor bku) {
        this.entityData.<Integer>set(Llama.DATA_SWAG_ID, (bku == null) ? -1 : bku.getId());
    }
    
    @Nullable
    private static DyeColor getDyeColor(final ItemStack bly) {
        final Block bul2 = Block.byItem(bly.getItem());
        if (bul2 instanceof WoolCarpetBlock) {
            return ((WoolCarpetBlock)bul2).getColor();
        }
        return null;
    }
    
    @Nullable
    public DyeColor getSwag() {
        final int integer2 = this.entityData.<Integer>get(Llama.DATA_SWAG_ID);
        return (integer2 == -1) ? null : DyeColor.byId(integer2);
    }
    
    @Override
    public int getMaxTemper() {
        return 30;
    }
    
    @Override
    public boolean canMate(final Animal azw) {
        return azw != this && azw instanceof Llama && this.canParent() && ((Llama)azw).canParent();
    }
    
    @Override
    public Llama getBreedOffspring(final ServerLevel aag, final AgableMob apv) {
        final Llama bbb4 = this.makeBabyLlama();
        this.setOffspringAttributes(apv, bbb4);
        final Llama bbb5 = (Llama)apv;
        int integer6 = this.random.nextInt(Math.max(this.getStrength(), bbb5.getStrength())) + 1;
        if (this.random.nextFloat() < 0.03f) {
            ++integer6;
        }
        bbb4.setStrength(integer6);
        bbb4.setVariant(this.random.nextBoolean() ? this.getVariant() : bbb5.getVariant());
        return bbb4;
    }
    
    protected Llama makeBabyLlama() {
        return EntityType.LLAMA.create(this.level);
    }
    
    private void spit(final LivingEntity aqj) {
        final LlamaSpit bgi3 = new LlamaSpit(this.level, this);
        final double double4 = aqj.getX() - this.getX();
        final double double5 = aqj.getY(0.3333333333333333) - bgi3.getY();
        final double double6 = aqj.getZ() - this.getZ();
        final float float10 = Mth.sqrt(double4 * double4 + double6 * double6) * 0.2f;
        bgi3.shoot(double4, double5 + float10, double6, 1.5f, 10.0f);
        if (!this.isSilent()) {
            this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LLAMA_SPIT, this.getSoundSource(), 1.0f, 1.0f + (this.random.nextFloat() - this.random.nextFloat()) * 0.2f);
        }
        this.level.addFreshEntity(bgi3);
        this.didSpit = true;
    }
    
    private void setDidSpit(final boolean boolean1) {
        this.didSpit = boolean1;
    }
    
    @Override
    public boolean causeFallDamage(final float float1, final float float2) {
        final int integer4 = this.calculateFallDamage(float1, float2);
        if (integer4 <= 0) {
            return false;
        }
        if (float1 >= 6.0f) {
            this.hurt(DamageSource.FALL, (float)integer4);
            if (this.isVehicle()) {
                for (final Entity apx6 : this.getIndirectPassengers()) {
                    apx6.hurt(DamageSource.FALL, (float)integer4);
                }
            }
        }
        this.playBlockFallSound();
        return true;
    }
    
    public void leaveCaravan() {
        if (this.caravanHead != null) {
            this.caravanHead.caravanTail = null;
        }
        this.caravanHead = null;
    }
    
    public void joinCaravan(final Llama bbb) {
        this.caravanHead = bbb;
        this.caravanHead.caravanTail = this;
    }
    
    public boolean hasCaravanTail() {
        return this.caravanTail != null;
    }
    
    public boolean inCaravan() {
        return this.caravanHead != null;
    }
    
    @Nullable
    public Llama getCaravanHead() {
        return this.caravanHead;
    }
    
    protected double followLeashSpeed() {
        return 2.0;
    }
    
    @Override
    protected void followMommy() {
        if (!this.inCaravan() && this.isBaby()) {
            super.followMommy();
        }
    }
    
    @Override
    public boolean canEatGrass() {
        return false;
    }
    
    @Override
    public void performRangedAttack(final LivingEntity aqj, final float float2) {
        this.spit(aqj);
    }
    
    public Vec3 getLeashOffset() {
        return new Vec3(0.0, 0.75 * this.getEyeHeight(), this.getBbWidth() * 0.5);
    }
    
    static {
        FOOD_ITEMS = Ingredient.of(Items.WHEAT, Blocks.HAY_BLOCK.asItem());
        DATA_STRENGTH_ID = SynchedEntityData.<Integer>defineId(Llama.class, EntityDataSerializers.INT);
        DATA_SWAG_ID = SynchedEntityData.<Integer>defineId(Llama.class, EntityDataSerializers.INT);
        DATA_VARIANT_ID = SynchedEntityData.<Integer>defineId(Llama.class, EntityDataSerializers.INT);
    }
    
    static class LlamaGroupData extends AgableMobGroupData {
        public final int variant;
        
        private LlamaGroupData(final int integer) {
            super(true);
            this.variant = integer;
        }
    }
    
    static class LlamaHurtByTargetGoal extends HurtByTargetGoal {
        public LlamaHurtByTargetGoal(final Llama bbb) {
            super(bbb, new Class[0]);
        }
        
        @Override
        public boolean canContinueToUse() {
            if (this.mob instanceof Llama) {
                final Llama bbb2 = (Llama)this.mob;
                if (bbb2.didSpit) {
                    bbb2.setDidSpit(false);
                    return false;
                }
            }
            return super.canContinueToUse();
        }
    }
    
    static class LlamaAttackWolfGoal extends NearestAttackableTargetGoal<Wolf> {
        public LlamaAttackWolfGoal(final Llama bbb) {
            super(bbb, Wolf.class, 16, false, true, (Predicate<LivingEntity>)(aqj -> !((Wolf)aqj).isTame()));
        }
        
        @Override
        protected double getFollowDistance() {
            return super.getFollowDistance() * 0.25;
        }
    }
}

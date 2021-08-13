package net.minecraft.world.entity.animal;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.control.MoveControl;
import java.util.Comparator;
import java.util.Arrays;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import java.util.Random;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.damagesource.DamageSource;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.util.Mth;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import javax.annotation.Nullable;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import java.util.function.Predicate;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.network.syncher.EntityDataAccessor;

public class Panda extends Animal {
    private static final EntityDataAccessor<Integer> UNHAPPY_COUNTER;
    private static final EntityDataAccessor<Integer> SNEEZE_COUNTER;
    private static final EntityDataAccessor<Integer> EAT_COUNTER;
    private static final EntityDataAccessor<Byte> MAIN_GENE_ID;
    private static final EntityDataAccessor<Byte> HIDDEN_GENE_ID;
    private static final EntityDataAccessor<Byte> DATA_ID_FLAGS;
    private static final TargetingConditions BREED_TARGETING;
    private boolean gotBamboo;
    private boolean didBite;
    public int rollCounter;
    private Vec3 rollDelta;
    private float sitAmount;
    private float sitAmountO;
    private float onBackAmount;
    private float onBackAmountO;
    private float rollAmount;
    private float rollAmountO;
    private PandaLookAtPlayerGoal lookAtPlayerGoal;
    private static final Predicate<ItemEntity> PANDA_ITEMS;
    
    public Panda(final EntityType<? extends Panda> aqb, final Level bru) {
        super(aqb, bru);
        this.moveControl = new PandaMoveControl(this);
        if (!this.isBaby()) {
            this.setCanPickUpLoot(true);
        }
    }
    
    @Override
    public boolean canTakeItem(final ItemStack bly) {
        final EquipmentSlot aqc3 = Mob.getEquipmentSlotForItem(bly);
        return this.getItemBySlot(aqc3).isEmpty() && aqc3 == EquipmentSlot.MAINHAND && super.canTakeItem(bly);
    }
    
    public int getUnhappyCounter() {
        return this.entityData.<Integer>get(Panda.UNHAPPY_COUNTER);
    }
    
    public void setUnhappyCounter(final int integer) {
        this.entityData.<Integer>set(Panda.UNHAPPY_COUNTER, integer);
    }
    
    public boolean isSneezing() {
        return this.getFlag(2);
    }
    
    public boolean isSitting() {
        return this.getFlag(8);
    }
    
    public void sit(final boolean boolean1) {
        this.setFlag(8, boolean1);
    }
    
    public boolean isOnBack() {
        return this.getFlag(16);
    }
    
    public void setOnBack(final boolean boolean1) {
        this.setFlag(16, boolean1);
    }
    
    public boolean isEating() {
        return this.entityData.<Integer>get(Panda.EAT_COUNTER) > 0;
    }
    
    public void eat(final boolean boolean1) {
        this.entityData.<Integer>set(Panda.EAT_COUNTER, boolean1 ? 1 : 0);
    }
    
    private int getEatCounter() {
        return this.entityData.<Integer>get(Panda.EAT_COUNTER);
    }
    
    private void setEatCounter(final int integer) {
        this.entityData.<Integer>set(Panda.EAT_COUNTER, integer);
    }
    
    public void sneeze(final boolean boolean1) {
        this.setFlag(2, boolean1);
        if (!boolean1) {
            this.setSneezeCounter(0);
        }
    }
    
    public int getSneezeCounter() {
        return this.entityData.<Integer>get(Panda.SNEEZE_COUNTER);
    }
    
    public void setSneezeCounter(final int integer) {
        this.entityData.<Integer>set(Panda.SNEEZE_COUNTER, integer);
    }
    
    public Gene getMainGene() {
        return Gene.byId(this.entityData.<Byte>get(Panda.MAIN_GENE_ID));
    }
    
    public void setMainGene(Gene a) {
        if (a.getId() > 6) {
            a = Gene.getRandom(this.random);
        }
        this.entityData.<Byte>set(Panda.MAIN_GENE_ID, (byte)a.getId());
    }
    
    public Gene getHiddenGene() {
        return Gene.byId(this.entityData.<Byte>get(Panda.HIDDEN_GENE_ID));
    }
    
    public void setHiddenGene(Gene a) {
        if (a.getId() > 6) {
            a = Gene.getRandom(this.random);
        }
        this.entityData.<Byte>set(Panda.HIDDEN_GENE_ID, (byte)a.getId());
    }
    
    public boolean isRolling() {
        return this.getFlag(4);
    }
    
    public void roll(final boolean boolean1) {
        this.setFlag(4, boolean1);
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Integer>define(Panda.UNHAPPY_COUNTER, 0);
        this.entityData.<Integer>define(Panda.SNEEZE_COUNTER, 0);
        this.entityData.<Byte>define(Panda.MAIN_GENE_ID, (Byte)0);
        this.entityData.<Byte>define(Panda.HIDDEN_GENE_ID, (Byte)0);
        this.entityData.<Byte>define(Panda.DATA_ID_FLAGS, (Byte)0);
        this.entityData.<Integer>define(Panda.EAT_COUNTER, 0);
    }
    
    private boolean getFlag(final int integer) {
        return (this.entityData.<Byte>get(Panda.DATA_ID_FLAGS) & integer) != 0x0;
    }
    
    private void setFlag(final int integer, final boolean boolean2) {
        final byte byte4 = this.entityData.<Byte>get(Panda.DATA_ID_FLAGS);
        if (boolean2) {
            this.entityData.<Byte>set(Panda.DATA_ID_FLAGS, (byte)(byte4 | integer));
        }
        else {
            this.entityData.<Byte>set(Panda.DATA_ID_FLAGS, (byte)(byte4 & ~integer));
        }
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putString("MainGene", this.getMainGene().getName());
        md.putString("HiddenGene", this.getHiddenGene().getName());
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.setMainGene(Gene.byName(md.getString("MainGene")));
        this.setHiddenGene(Gene.byName(md.getString("HiddenGene")));
    }
    
    @Nullable
    @Override
    public AgableMob getBreedOffspring(final ServerLevel aag, final AgableMob apv) {
        final Panda bai4 = EntityType.PANDA.create(aag);
        if (apv instanceof Panda) {
            bai4.setGeneFromParents(this, (Panda)apv);
        }
        bai4.setAttributes();
        return bai4;
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new PandaPanicGoal(this, 2.0));
        this.goalSelector.addGoal(2, new PandaBreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new PandaAttackGoal(this, 1.2000000476837158, true));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.0, Ingredient.of(Blocks.BAMBOO.asItem()), false));
        this.goalSelector.addGoal(6, new PandaAvoidGoal<>(this, Player.class, 8.0f, 2.0, 2.0));
        this.goalSelector.addGoal(6, new PandaAvoidGoal<>(this, Monster.class, 4.0f, 2.0, 2.0));
        this.goalSelector.addGoal(7, new PandaSitGoal());
        this.goalSelector.addGoal(8, new PandaLieOnBackGoal(this));
        this.goalSelector.addGoal(8, new PandaSneezeGoal(this));
        this.lookAtPlayerGoal = new PandaLookAtPlayerGoal(this, Player.class, 6.0f);
        this.goalSelector.addGoal(9, this.lookAtPlayerGoal);
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(12, new PandaRollGoal(this));
        this.goalSelector.addGoal(13, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(14, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(1, new PandaHurtByTargetGoal(this, new Class[0]).setAlertOthers(new Class[0]));
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.15000000596046448).add(Attributes.ATTACK_DAMAGE, 6.0);
    }
    
    public Gene getVariant() {
        return getVariantFromGenes(this.getMainGene(), this.getHiddenGene());
    }
    
    public boolean isLazy() {
        return this.getVariant() == Gene.LAZY;
    }
    
    public boolean isWorried() {
        return this.getVariant() == Gene.WORRIED;
    }
    
    public boolean isPlayful() {
        return this.getVariant() == Gene.PLAYFUL;
    }
    
    public boolean isWeak() {
        return this.getVariant() == Gene.WEAK;
    }
    
    @Override
    public boolean isAggressive() {
        return this.getVariant() == Gene.AGGRESSIVE;
    }
    
    @Override
    public boolean canBeLeashed(final Player bft) {
        return false;
    }
    
    @Override
    public boolean doHurtTarget(final Entity apx) {
        this.playSound(SoundEvents.PANDA_BITE, 1.0f, 1.0f);
        if (!this.isAggressive()) {
            this.didBite = true;
        }
        return super.doHurtTarget(apx);
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.isWorried()) {
            if (this.level.isThundering() && !this.isInWater()) {
                this.sit(true);
                this.eat(false);
            }
            else if (!this.isEating()) {
                this.sit(false);
            }
        }
        if (this.getTarget() == null) {
            this.gotBamboo = false;
            this.didBite = false;
        }
        if (this.getUnhappyCounter() > 0) {
            if (this.getTarget() != null) {
                this.lookAt(this.getTarget(), 90.0f, 90.0f);
            }
            if (this.getUnhappyCounter() == 29 || this.getUnhappyCounter() == 14) {
                this.playSound(SoundEvents.PANDA_CANT_BREED, 1.0f, 1.0f);
            }
            this.setUnhappyCounter(this.getUnhappyCounter() - 1);
        }
        if (this.isSneezing()) {
            this.setSneezeCounter(this.getSneezeCounter() + 1);
            if (this.getSneezeCounter() > 20) {
                this.sneeze(false);
                this.afterSneeze();
            }
            else if (this.getSneezeCounter() == 1) {
                this.playSound(SoundEvents.PANDA_PRE_SNEEZE, 1.0f, 1.0f);
            }
        }
        if (this.isRolling()) {
            this.handleRoll();
        }
        else {
            this.rollCounter = 0;
        }
        if (this.isSitting()) {
            this.xRot = 0.0f;
        }
        this.updateSitAmount();
        this.handleEating();
        this.updateOnBackAnimation();
        this.updateRollAmount();
    }
    
    public boolean isScared() {
        return this.isWorried() && this.level.isThundering();
    }
    
    private void handleEating() {
        if (!this.isEating() && this.isSitting() && !this.isScared() && !this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() && this.random.nextInt(80) == 1) {
            this.eat(true);
        }
        else if (this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() || !this.isSitting()) {
            this.eat(false);
        }
        if (this.isEating()) {
            this.addEatingParticles();
            if (!this.level.isClientSide && this.getEatCounter() > 80 && this.random.nextInt(20) == 1) {
                if (this.getEatCounter() > 100 && this.isFoodOrCake(this.getItemBySlot(EquipmentSlot.MAINHAND))) {
                    if (!this.level.isClientSide) {
                        this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    }
                    this.sit(false);
                }
                this.eat(false);
                return;
            }
            this.setEatCounter(this.getEatCounter() + 1);
        }
    }
    
    private void addEatingParticles() {
        if (this.getEatCounter() % 5 == 0) {
            this.playSound(SoundEvents.PANDA_EAT, 0.5f + 0.5f * this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
            for (int integer2 = 0; integer2 < 6; ++integer2) {
                Vec3 dck3 = new Vec3((this.random.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, (this.random.nextFloat() - 0.5) * 0.1);
                dck3 = dck3.xRot(-this.xRot * 0.017453292f);
                dck3 = dck3.yRot(-this.yRot * 0.017453292f);
                final double double4 = -this.random.nextFloat() * 0.6 - 0.3;
                Vec3 dck4 = new Vec3((this.random.nextFloat() - 0.5) * 0.8, double4, 1.0 + (this.random.nextFloat() - 0.5) * 0.4);
                dck4 = dck4.yRot(-this.yBodyRot * 0.017453292f);
                dck4 = dck4.add(this.getX(), this.getEyeY() + 1.0, this.getZ());
                this.level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getItemBySlot(EquipmentSlot.MAINHAND)), dck4.x, dck4.y, dck4.z, dck3.x, dck3.y + 0.05, dck3.z);
            }
        }
    }
    
    private void updateSitAmount() {
        this.sitAmountO = this.sitAmount;
        if (this.isSitting()) {
            this.sitAmount = Math.min(1.0f, this.sitAmount + 0.15f);
        }
        else {
            this.sitAmount = Math.max(0.0f, this.sitAmount - 0.19f);
        }
    }
    
    private void updateOnBackAnimation() {
        this.onBackAmountO = this.onBackAmount;
        if (this.isOnBack()) {
            this.onBackAmount = Math.min(1.0f, this.onBackAmount + 0.15f);
        }
        else {
            this.onBackAmount = Math.max(0.0f, this.onBackAmount - 0.19f);
        }
    }
    
    private void updateRollAmount() {
        this.rollAmountO = this.rollAmount;
        if (this.isRolling()) {
            this.rollAmount = Math.min(1.0f, this.rollAmount + 0.15f);
        }
        else {
            this.rollAmount = Math.max(0.0f, this.rollAmount - 0.19f);
        }
    }
    
    public float getSitAmount(final float float1) {
        return Mth.lerp(float1, this.sitAmountO, this.sitAmount);
    }
    
    public float getLieOnBackAmount(final float float1) {
        return Mth.lerp(float1, this.onBackAmountO, this.onBackAmount);
    }
    
    public float getRollAmount(final float float1) {
        return Mth.lerp(float1, this.rollAmountO, this.rollAmount);
    }
    
    private void handleRoll() {
        ++this.rollCounter;
        if (this.rollCounter > 32) {
            this.roll(false);
            return;
        }
        if (!this.level.isClientSide) {
            final Vec3 dck2 = this.getDeltaMovement();
            if (this.rollCounter == 1) {
                final float float3 = this.yRot * 0.017453292f;
                final float float4 = this.isBaby() ? 0.1f : 0.2f;
                this.rollDelta = new Vec3(dck2.x + -Mth.sin(float3) * float4, 0.0, dck2.z + Mth.cos(float3) * float4);
                this.setDeltaMovement(this.rollDelta.add(0.0, 0.27, 0.0));
            }
            else if (this.rollCounter == 7.0f || this.rollCounter == 15.0f || this.rollCounter == 23.0f) {
                this.setDeltaMovement(0.0, this.onGround ? 0.27 : dck2.y, 0.0);
            }
            else {
                this.setDeltaMovement(this.rollDelta.x, dck2.y, this.rollDelta.z);
            }
        }
    }
    
    private void afterSneeze() {
        final Vec3 dck2 = this.getDeltaMovement();
        this.level.addParticle(ParticleTypes.SNEEZE, this.getX() - (this.getBbWidth() + 1.0f) * 0.5 * Mth.sin(this.yBodyRot * 0.017453292f), this.getEyeY() - 0.10000000149011612, this.getZ() + (this.getBbWidth() + 1.0f) * 0.5 * Mth.cos(this.yBodyRot * 0.017453292f), dck2.x, 0.0, dck2.z);
        this.playSound(SoundEvents.PANDA_SNEEZE, 1.0f, 1.0f);
        final List<Panda> list3 = this.level.<Panda>getEntitiesOfClass((java.lang.Class<? extends Panda>)Panda.class, this.getBoundingBox().inflate(10.0));
        for (final Panda bai5 : list3) {
            if (!bai5.isBaby() && bai5.onGround && !bai5.isInWater() && bai5.canPerformAction()) {
                bai5.jumpFromGround();
            }
        }
        if (!this.level.isClientSide() && this.random.nextInt(700) == 0 && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            this.spawnAtLocation(Items.SLIME_BALL);
        }
    }
    
    @Override
    protected void pickUpItem(final ItemEntity bcs) {
        if (this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() && Panda.PANDA_ITEMS.test(bcs)) {
            this.onItemPickup(bcs);
            final ItemStack bly3 = bcs.getItem();
            this.setItemSlot(EquipmentSlot.MAINHAND, bly3);
            this.handDropChances[EquipmentSlot.MAINHAND.getIndex()] = 2.0f;
            this.take(bcs, bly3.getCount());
            bcs.remove();
        }
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        this.sit(false);
        return super.hurt(aph, float2);
    }
    
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable SpawnGroupData aqz, @Nullable final CompoundTag md) {
        this.setMainGene(Gene.getRandom(this.random));
        this.setHiddenGene(Gene.getRandom(this.random));
        this.setAttributes();
        if (aqz == null) {
            aqz = new AgableMobGroupData(0.2f);
        }
        return super.finalizeSpawn(bsh, aop, aqm, aqz, md);
    }
    
    public void setGeneFromParents(final Panda bai1, @Nullable final Panda bai2) {
        if (bai2 == null) {
            if (this.random.nextBoolean()) {
                this.setMainGene(bai1.getOneOfGenesRandomly());
                this.setHiddenGene(Gene.getRandom(this.random));
            }
            else {
                this.setMainGene(Gene.getRandom(this.random));
                this.setHiddenGene(bai1.getOneOfGenesRandomly());
            }
        }
        else if (this.random.nextBoolean()) {
            this.setMainGene(bai1.getOneOfGenesRandomly());
            this.setHiddenGene(bai2.getOneOfGenesRandomly());
        }
        else {
            this.setMainGene(bai2.getOneOfGenesRandomly());
            this.setHiddenGene(bai1.getOneOfGenesRandomly());
        }
        if (this.random.nextInt(32) == 0) {
            this.setMainGene(Gene.getRandom(this.random));
        }
        if (this.random.nextInt(32) == 0) {
            this.setHiddenGene(Gene.getRandom(this.random));
        }
    }
    
    private Gene getOneOfGenesRandomly() {
        if (this.random.nextBoolean()) {
            return this.getMainGene();
        }
        return this.getHiddenGene();
    }
    
    public void setAttributes() {
        if (this.isWeak()) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(10.0);
        }
        if (this.isLazy()) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.07000000029802322);
        }
    }
    
    private void tryToSit() {
        if (!this.isInWater()) {
            this.setZza(0.0f);
            this.getNavigation().stop();
            this.sit(true);
        }
    }
    
    @Override
    public InteractionResult mobInteract(final Player bft, final InteractionHand aoq) {
        final ItemStack bly4 = bft.getItemInHand(aoq);
        if (this.isScared()) {
            return InteractionResult.PASS;
        }
        if (this.isOnBack()) {
            this.setOnBack(false);
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        if (this.isFood(bly4)) {
            if (this.getTarget() != null) {
                this.gotBamboo = true;
            }
            if (this.isBaby()) {
                this.usePlayerItem(bft, bly4);
                this.ageUp((int)(-this.getAge() / 20 * 0.1f), true);
            }
            else if (!this.level.isClientSide && this.getAge() == 0 && this.canFallInLove()) {
                this.usePlayerItem(bft, bly4);
                this.setInLove(bft);
            }
            else {
                if (this.level.isClientSide || this.isSitting() || this.isInWater()) {
                    return InteractionResult.PASS;
                }
                this.tryToSit();
                this.eat(true);
                final ItemStack bly5 = this.getItemBySlot(EquipmentSlot.MAINHAND);
                if (!bly5.isEmpty() && !bft.abilities.instabuild) {
                    this.spawnAtLocation(bly5);
                }
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(bly4.getItem(), 1));
                this.usePlayerItem(bft, bly4);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
    
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        if (this.isAggressive()) {
            return SoundEvents.PANDA_AGGRESSIVE_AMBIENT;
        }
        if (this.isWorried()) {
            return SoundEvents.PANDA_WORRIED_AMBIENT;
        }
        return SoundEvents.PANDA_AMBIENT;
    }
    
    @Override
    protected void playStepSound(final BlockPos fx, final BlockState cee) {
        this.playSound(SoundEvents.PANDA_STEP, 0.15f, 1.0f);
    }
    
    @Override
    public boolean isFood(final ItemStack bly) {
        return bly.getItem() == Blocks.BAMBOO.asItem();
    }
    
    private boolean isFoodOrCake(final ItemStack bly) {
        return this.isFood(bly) || bly.getItem() == Blocks.CAKE.asItem();
    }
    
    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PANDA_DEATH;
    }
    
    @Nullable
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.PANDA_HURT;
    }
    
    public boolean canPerformAction() {
        return !this.isOnBack() && !this.isScared() && !this.isEating() && !this.isRolling() && !this.isSitting();
    }
    
    static {
        UNHAPPY_COUNTER = SynchedEntityData.<Integer>defineId(Panda.class, EntityDataSerializers.INT);
        SNEEZE_COUNTER = SynchedEntityData.<Integer>defineId(Panda.class, EntityDataSerializers.INT);
        EAT_COUNTER = SynchedEntityData.<Integer>defineId(Panda.class, EntityDataSerializers.INT);
        MAIN_GENE_ID = SynchedEntityData.<Byte>defineId(Panda.class, EntityDataSerializers.BYTE);
        HIDDEN_GENE_ID = SynchedEntityData.<Byte>defineId(Panda.class, EntityDataSerializers.BYTE);
        DATA_ID_FLAGS = SynchedEntityData.<Byte>defineId(Panda.class, EntityDataSerializers.BYTE);
        BREED_TARGETING = new TargetingConditions().range(8.0).allowSameTeam().allowInvulnerable();
        PANDA_ITEMS = (bcs -> {
            final Item blu2 = bcs.getItem().getItem();
            return (blu2 == Blocks.BAMBOO.asItem() || blu2 == Blocks.CAKE.asItem()) && bcs.isAlive() && !bcs.hasPickUpDelay();
        });
    }
    
    public enum Gene {
        NORMAL(0, "normal", false), 
        LAZY(1, "lazy", false), 
        WORRIED(2, "worried", false), 
        PLAYFUL(3, "playful", false), 
        BROWN(4, "brown", true), 
        WEAK(5, "weak", true), 
        AGGRESSIVE(6, "aggressive", false);
        
        private static final Gene[] BY_ID;
        private final int id;
        private final String name;
        private final boolean isRecessive;
        
        private Gene(final int integer3, final String string4, final boolean boolean5) {
            this.id = integer3;
            this.name = string4;
            this.isRecessive = boolean5;
        }
        
        public int getId() {
            return this.id;
        }
        
        public String getName() {
            return this.name;
        }
        
        public boolean isRecessive() {
            return this.isRecessive;
        }
        
        private static Gene getVariantFromGenes(final Gene a1, final Gene a2) {
            if (!a1.isRecessive()) {
                return a1;
            }
            if (a1 == a2) {
                return a1;
            }
            return Gene.NORMAL;
        }
        
        public static Gene byId(int integer) {
            if (integer < 0 || integer >= Gene.BY_ID.length) {
                integer = 0;
            }
            return Gene.BY_ID[integer];
        }
        
        public static Gene byName(final String string) {
            for (final Gene a5 : values()) {
                if (a5.name.equals(string)) {
                    return a5;
                }
            }
            return Gene.NORMAL;
        }
        
        public static Gene getRandom(final Random random) {
            final int integer2 = random.nextInt(16);
            if (integer2 == 0) {
                return Gene.LAZY;
            }
            if (integer2 == 1) {
                return Gene.WORRIED;
            }
            if (integer2 == 2) {
                return Gene.PLAYFUL;
            }
            if (integer2 == 4) {
                return Gene.AGGRESSIVE;
            }
            if (integer2 < 9) {
                return Gene.WEAK;
            }
            if (integer2 < 11) {
                return Gene.BROWN;
            }
            return Gene.NORMAL;
        }
        
        static {
            BY_ID = (Gene[])Arrays.stream((Object[])values()).sorted(Comparator.comparingInt(Gene::getId)).toArray(Gene[]::new);
        }
    }
    
    static class PandaMoveControl extends MoveControl {
        private final Panda panda;
        
        public PandaMoveControl(final Panda bai) {
            super(bai);
            this.panda = bai;
        }
        
        @Override
        public void tick() {
            if (!this.panda.canPerformAction()) {
                return;
            }
            super.tick();
        }
    }
    
    static class PandaAttackGoal extends MeleeAttackGoal {
        private final Panda panda;
        
        public PandaAttackGoal(final Panda bai, final double double2, final boolean boolean3) {
            super(bai, double2, boolean3);
            this.panda = bai;
        }
        
        @Override
        public boolean canUse() {
            return this.panda.canPerformAction() && super.canUse();
        }
    }
    
    static class PandaLookAtPlayerGoal extends LookAtPlayerGoal {
        private final Panda panda;
        
        public PandaLookAtPlayerGoal(final Panda bai, final Class<? extends LivingEntity> class2, final float float3) {
            super(bai, class2, float3);
            this.panda = bai;
        }
        
        public void setTarget(final LivingEntity aqj) {
            this.lookAt = aqj;
        }
        
        @Override
        public boolean canContinueToUse() {
            return this.lookAt != null && super.canContinueToUse();
        }
        
        @Override
        public boolean canUse() {
            if (this.mob.getRandom().nextFloat() >= this.probability) {
                return false;
            }
            if (this.lookAt == null) {
                if (this.lookAtType == Player.class) {
                    this.lookAt = this.mob.level.getNearestPlayer(this.lookAtContext, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
                }
                else {
                    this.lookAt = this.mob.level.<Entity>getNearestLoadedEntity((java.lang.Class<? extends Entity>)this.lookAtType, this.lookAtContext, (LivingEntity)this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ(), this.mob.getBoundingBox().inflate(this.lookDistance, 3.0, this.lookDistance));
                }
            }
            return this.panda.canPerformAction() && this.lookAt != null;
        }
        
        @Override
        public void tick() {
            if (this.lookAt != null) {
                super.tick();
            }
        }
    }
    
    static class PandaRollGoal extends Goal {
        private final Panda panda;
        
        public PandaRollGoal(final Panda bai) {
            this.panda = bai;
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE, (Enum)Flag.LOOK, (Enum)Flag.JUMP));
        }
        
        @Override
        public boolean canUse() {
            if ((!this.panda.isBaby() && !this.panda.isPlayful()) || !this.panda.onGround) {
                return false;
            }
            if (!this.panda.canPerformAction()) {
                return false;
            }
            final float float2 = this.panda.yRot * 0.017453292f;
            int integer3 = 0;
            int integer4 = 0;
            final float float3 = -Mth.sin(float2);
            final float float4 = Mth.cos(float2);
            if (Math.abs(float3) > 0.5) {
                integer3 += (int)(float3 / Math.abs(float3));
            }
            if (Math.abs(float4) > 0.5) {
                integer4 += (int)(float4 / Math.abs(float4));
            }
            return this.panda.level.getBlockState(this.panda.blockPosition().offset(integer3, -1, integer4)).isAir() || (this.panda.isPlayful() && this.panda.random.nextInt(60) == 1) || this.panda.random.nextInt(500) == 1;
        }
        
        @Override
        public boolean canContinueToUse() {
            return false;
        }
        
        @Override
        public void start() {
            this.panda.roll(true);
        }
        
        @Override
        public boolean isInterruptable() {
            return false;
        }
    }
    
    static class PandaSneezeGoal extends Goal {
        private final Panda panda;
        
        public PandaSneezeGoal(final Panda bai) {
            this.panda = bai;
        }
        
        @Override
        public boolean canUse() {
            return this.panda.isBaby() && this.panda.canPerformAction() && ((this.panda.isWeak() && this.panda.random.nextInt(500) == 1) || this.panda.random.nextInt(6000) == 1);
        }
        
        @Override
        public boolean canContinueToUse() {
            return false;
        }
        
        @Override
        public void start() {
            this.panda.sneeze(true);
        }
    }
    
    class PandaBreedGoal extends BreedGoal {
        private final Panda panda;
        private int unhappyCooldown;
        
        public PandaBreedGoal(final Panda bai2, final double double3) {
            super(bai2, double3);
            this.panda = bai2;
        }
        
        @Override
        public boolean canUse() {
            if (!super.canUse() || this.panda.getUnhappyCounter() != 0) {
                return false;
            }
            if (!this.canFindBamboo()) {
                if (this.unhappyCooldown <= this.panda.tickCount) {
                    this.panda.setUnhappyCounter(32);
                    this.unhappyCooldown = this.panda.tickCount + 600;
                    if (this.panda.isEffectiveAi()) {
                        final Player bft2 = this.level.getNearestPlayer(Panda.BREED_TARGETING, this.panda);
                        this.panda.lookAtPlayerGoal.setTarget(bft2);
                    }
                }
                return false;
            }
            return true;
        }
        
        private boolean canFindBamboo() {
            final BlockPos fx2 = this.panda.blockPosition();
            final BlockPos.MutableBlockPos a3 = new BlockPos.MutableBlockPos();
            for (int integer4 = 0; integer4 < 3; ++integer4) {
                for (int integer5 = 0; integer5 < 8; ++integer5) {
                    for (int integer6 = 0; integer6 <= integer5; integer6 = ((integer6 > 0) ? (-integer6) : (1 - integer6))) {
                        for (int integer7 = (integer6 < integer5 && integer6 > -integer5) ? integer5 : 0; integer7 <= integer5; integer7 = ((integer7 > 0) ? (-integer7) : (1 - integer7))) {
                            a3.setWithOffset(fx2, integer6, integer4, integer7);
                            if (this.level.getBlockState(a3).is(Blocks.BAMBOO)) {
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }
    }
    
    static class PandaAvoidGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
        private final Panda panda;
        
        public PandaAvoidGoal(final Panda bai, final Class<T> class2, final float float3, final double double4, final double double5) {
            super(bai, class2, float3, double4, double5, (Predicate<LivingEntity>)EntitySelector.NO_SPECTATORS::test);
            this.panda = bai;
        }
        
        @Override
        public boolean canUse() {
            return this.panda.isWorried() && this.panda.canPerformAction() && super.canUse();
        }
    }
    
    class PandaSitGoal extends Goal {
        private int cooldown;
        
        public PandaSitGoal() {
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE));
        }
        
        @Override
        public boolean canUse() {
            if (this.cooldown > Panda.this.tickCount || Panda.this.isBaby() || Panda.this.isInWater() || !Panda.this.canPerformAction() || Panda.this.getUnhappyCounter() > 0) {
                return false;
            }
            final List<ItemEntity> list2 = Panda.this.level.<ItemEntity>getEntitiesOfClass((java.lang.Class<? extends ItemEntity>)ItemEntity.class, Panda.this.getBoundingBox().inflate(6.0, 6.0, 6.0), (java.util.function.Predicate<? super ItemEntity>)Panda.PANDA_ITEMS);
            return !list2.isEmpty() || !Panda.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty();
        }
        
        @Override
        public boolean canContinueToUse() {
            return !Panda.this.isInWater() && (Panda.this.isLazy() || Panda.this.random.nextInt(600) != 1) && Panda.this.random.nextInt(2000) != 1;
        }
        
        @Override
        public void tick() {
            if (!Panda.this.isSitting() && !Panda.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
                Panda.this.tryToSit();
            }
        }
        
        @Override
        public void start() {
            final List<ItemEntity> list2 = Panda.this.level.<ItemEntity>getEntitiesOfClass((java.lang.Class<? extends ItemEntity>)ItemEntity.class, Panda.this.getBoundingBox().inflate(8.0, 8.0, 8.0), (java.util.function.Predicate<? super ItemEntity>)Panda.PANDA_ITEMS);
            if (!list2.isEmpty() && Panda.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
                Panda.this.getNavigation().moveTo((Entity)list2.get(0), 1.2000000476837158);
            }
            else if (!Panda.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
                Panda.this.tryToSit();
            }
            this.cooldown = 0;
        }
        
        @Override
        public void stop() {
            final ItemStack bly2 = Panda.this.getItemBySlot(EquipmentSlot.MAINHAND);
            if (!bly2.isEmpty()) {
                Panda.this.spawnAtLocation(bly2);
                Panda.this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                final int integer3 = Panda.this.isLazy() ? (Panda.this.random.nextInt(50) + 10) : (Panda.this.random.nextInt(150) + 10);
                this.cooldown = Panda.this.tickCount + integer3 * 20;
            }
            Panda.this.sit(false);
        }
    }
    
    static class PandaLieOnBackGoal extends Goal {
        private final Panda panda;
        private int cooldown;
        
        public PandaLieOnBackGoal(final Panda bai) {
            this.panda = bai;
        }
        
        @Override
        public boolean canUse() {
            return this.cooldown < this.panda.tickCount && this.panda.isLazy() && this.panda.canPerformAction() && this.panda.random.nextInt(400) == 1;
        }
        
        @Override
        public boolean canContinueToUse() {
            return !this.panda.isInWater() && (this.panda.isLazy() || this.panda.random.nextInt(600) != 1) && this.panda.random.nextInt(2000) != 1;
        }
        
        @Override
        public void start() {
            this.panda.setOnBack(true);
            this.cooldown = 0;
        }
        
        @Override
        public void stop() {
            this.panda.setOnBack(false);
            this.cooldown = this.panda.tickCount + 200;
        }
    }
    
    static class PandaHurtByTargetGoal extends HurtByTargetGoal {
        private final Panda panda;
        
        public PandaHurtByTargetGoal(final Panda bai, final Class<?>... arr) {
            super(bai, arr);
            this.panda = bai;
        }
        
        @Override
        public boolean canContinueToUse() {
            if (this.panda.gotBamboo || this.panda.didBite) {
                this.panda.setTarget(null);
                return false;
            }
            return super.canContinueToUse();
        }
        
        @Override
        protected void alertOther(final Mob aqk, final LivingEntity aqj) {
            if (aqk instanceof Panda && ((Panda)aqk).isAggressive()) {
                aqk.setTarget(aqj);
            }
        }
    }
    
    static class PandaPanicGoal extends PanicGoal {
        private final Panda panda;
        
        public PandaPanicGoal(final Panda bai, final double double2) {
            super(bai, double2);
            this.panda = bai;
        }
        
        @Override
        public boolean canUse() {
            if (!this.panda.isOnFire()) {
                return false;
            }
            final BlockPos fx2 = this.lookForWater(this.mob.level, this.mob, 5, 4);
            if (fx2 != null) {
                this.posX = fx2.getX();
                this.posY = fx2.getY();
                this.posZ = fx2.getZ();
                return true;
            }
            return this.findRandomPosition();
        }
        
        @Override
        public boolean canContinueToUse() {
            if (this.panda.isSitting()) {
                this.panda.getNavigation().stop();
                return false;
            }
            return super.canContinueToUse();
        }
    }
}

package net.minecraft.world.entity.animal;

import net.minecraft.core.Direction;
import net.minecraft.world.level.storage.loot.LootTable;
import java.util.Random;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.core.Vec3i;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import java.util.function.Consumer;
import net.minecraft.Util;
import com.google.common.collect.Maps;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import java.util.HashMap;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.damagesource.DamageSource;
import javax.annotation.Nullable;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Pose;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import java.util.function.Predicate;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.OcelotAttackGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.CatSitOnBlockGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.CatLieOnBedGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.entity.TamableAnimal;

public class Cat extends TamableAnimal {
    private static final Ingredient TEMPT_INGREDIENT;
    private static final EntityDataAccessor<Integer> DATA_TYPE_ID;
    private static final EntityDataAccessor<Boolean> IS_LYING;
    private static final EntityDataAccessor<Boolean> RELAX_STATE_ONE;
    private static final EntityDataAccessor<Integer> DATA_COLLAR_COLOR;
    public static final Map<Integer, ResourceLocation> TEXTURE_BY_TYPE;
    private CatAvoidEntityGoal<Player> avoidPlayersGoal;
    private TemptGoal temptGoal;
    private float lieDownAmount;
    private float lieDownAmountO;
    private float lieDownAmountTail;
    private float lieDownAmountOTail;
    private float relaxStateOneAmount;
    private float relaxStateOneAmountO;
    
    public Cat(final EntityType<? extends Cat> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    public ResourceLocation getResourceLocation() {
        return (ResourceLocation)Cat.TEXTURE_BY_TYPE.getOrDefault(this.getCatType(), Cat.TEXTURE_BY_TYPE.get(0));
    }
    
    @Override
    protected void registerGoals() {
        this.temptGoal = new CatTemptGoal(this, 0.6, Cat.TEMPT_INGREDIENT, true);
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new CatRelaxOnOwnerGoal(this));
        this.goalSelector.addGoal(3, this.temptGoal);
        this.goalSelector.addGoal(5, new CatLieOnBedGoal(this, 1.1, 8));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0, 10.0f, 5.0f, false));
        this.goalSelector.addGoal(7, new CatSitOnBlockGoal(this, 0.8));
        this.goalSelector.addGoal(8, new LeapAtTargetGoal(this, 0.3f));
        this.goalSelector.addGoal(9, new OcelotAttackGoal(this));
        this.goalSelector.addGoal(10, new BreedGoal(this, 0.8));
        this.goalSelector.addGoal(11, new WaterAvoidingRandomStrollGoal(this, 0.8, 1.0000001E-5f));
        this.goalSelector.addGoal(12, new LookAtPlayerGoal(this, Player.class, 10.0f));
        this.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>(this, Rabbit.class, false, null));
        this.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>(this, Turtle.class, false, Turtle.BABY_ON_LAND_SELECTOR));
    }
    
    public int getCatType() {
        return this.entityData.<Integer>get(Cat.DATA_TYPE_ID);
    }
    
    public void setCatType(int integer) {
        if (integer < 0 || integer >= 11) {
            integer = this.random.nextInt(10);
        }
        this.entityData.<Integer>set(Cat.DATA_TYPE_ID, integer);
    }
    
    public void setLying(final boolean boolean1) {
        this.entityData.<Boolean>set(Cat.IS_LYING, boolean1);
    }
    
    public boolean isLying() {
        return this.entityData.<Boolean>get(Cat.IS_LYING);
    }
    
    public void setRelaxStateOne(final boolean boolean1) {
        this.entityData.<Boolean>set(Cat.RELAX_STATE_ONE, boolean1);
    }
    
    public boolean isRelaxStateOne() {
        return this.entityData.<Boolean>get(Cat.RELAX_STATE_ONE);
    }
    
    public DyeColor getCollarColor() {
        return DyeColor.byId(this.entityData.<Integer>get(Cat.DATA_COLLAR_COLOR));
    }
    
    public void setCollarColor(final DyeColor bku) {
        this.entityData.<Integer>set(Cat.DATA_COLLAR_COLOR, bku.getId());
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Integer>define(Cat.DATA_TYPE_ID, 1);
        this.entityData.<Boolean>define(Cat.IS_LYING, false);
        this.entityData.<Boolean>define(Cat.RELAX_STATE_ONE, false);
        this.entityData.<Integer>define(Cat.DATA_COLLAR_COLOR, DyeColor.RED.getId());
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putInt("CatType", this.getCatType());
        md.putByte("CollarColor", (byte)this.getCollarColor().getId());
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.setCatType(md.getInt("CatType"));
        if (md.contains("CollarColor", 99)) {
            this.setCollarColor(DyeColor.byId(md.getInt("CollarColor")));
        }
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
    
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        if (!this.isTame()) {
            return SoundEvents.CAT_STRAY_AMBIENT;
        }
        if (this.isInLove()) {
            return SoundEvents.CAT_PURR;
        }
        if (this.random.nextInt(4) == 0) {
            return SoundEvents.CAT_PURREOW;
        }
        return SoundEvents.CAT_AMBIENT;
    }
    
    @Override
    public int getAmbientSoundInterval() {
        return 120;
    }
    
    public void hiss() {
        this.playSound(SoundEvents.CAT_HISS, this.getSoundVolume(), this.getVoicePitch());
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.CAT_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CAT_DEATH;
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.MOVEMENT_SPEED, 0.30000001192092896).add(Attributes.ATTACK_DAMAGE, 3.0);
    }
    
    @Override
    public boolean causeFallDamage(final float float1, final float float2) {
        return false;
    }
    
    @Override
    protected void usePlayerItem(final Player bft, final ItemStack bly) {
        if (this.isFood(bly)) {
            this.playSound(SoundEvents.CAT_EAT, 1.0f, 1.0f);
        }
        super.usePlayerItem(bft, bly);
    }
    
    private float getAttackDamage() {
        return (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }
    
    @Override
    public boolean doHurtTarget(final Entity apx) {
        return apx.hurt(DamageSource.mobAttack(this), this.getAttackDamage());
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.temptGoal != null && this.temptGoal.isRunning() && !this.isTame() && this.tickCount % 100 == 0) {
            this.playSound(SoundEvents.CAT_BEG_FOR_FOOD, 1.0f, 1.0f);
        }
        this.handleLieDown();
    }
    
    private void handleLieDown() {
        if ((this.isLying() || this.isRelaxStateOne()) && this.tickCount % 5 == 0) {
            this.playSound(SoundEvents.CAT_PURR, 0.6f + 0.4f * (this.random.nextFloat() - this.random.nextFloat()), 1.0f);
        }
        this.updateLieDownAmount();
        this.updateRelaxStateOneAmount();
    }
    
    private void updateLieDownAmount() {
        this.lieDownAmountO = this.lieDownAmount;
        this.lieDownAmountOTail = this.lieDownAmountTail;
        if (this.isLying()) {
            this.lieDownAmount = Math.min(1.0f, this.lieDownAmount + 0.15f);
            this.lieDownAmountTail = Math.min(1.0f, this.lieDownAmountTail + 0.08f);
        }
        else {
            this.lieDownAmount = Math.max(0.0f, this.lieDownAmount - 0.22f);
            this.lieDownAmountTail = Math.max(0.0f, this.lieDownAmountTail - 0.13f);
        }
    }
    
    private void updateRelaxStateOneAmount() {
        this.relaxStateOneAmountO = this.relaxStateOneAmount;
        if (this.isRelaxStateOne()) {
            this.relaxStateOneAmount = Math.min(1.0f, this.relaxStateOneAmount + 0.1f);
        }
        else {
            this.relaxStateOneAmount = Math.max(0.0f, this.relaxStateOneAmount - 0.13f);
        }
    }
    
    public float getLieDownAmount(final float float1) {
        return Mth.lerp(float1, this.lieDownAmountO, this.lieDownAmount);
    }
    
    public float getLieDownAmountTail(final float float1) {
        return Mth.lerp(float1, this.lieDownAmountOTail, this.lieDownAmountTail);
    }
    
    public float getRelaxStateOneAmount(final float float1) {
        return Mth.lerp(float1, this.relaxStateOneAmountO, this.relaxStateOneAmount);
    }
    
    @Override
    public Cat getBreedOffspring(final ServerLevel aag, final AgableMob apv) {
        final Cat azy4 = EntityType.CAT.create(aag);
        if (apv instanceof Cat) {
            if (this.random.nextBoolean()) {
                azy4.setCatType(this.getCatType());
            }
            else {
                azy4.setCatType(((Cat)apv).getCatType());
            }
            if (this.isTame()) {
                azy4.setOwnerUUID(this.getOwnerUUID());
                azy4.setTame(true);
                if (this.random.nextBoolean()) {
                    azy4.setCollarColor(this.getCollarColor());
                }
                else {
                    azy4.setCollarColor(((Cat)apv).getCollarColor());
                }
            }
        }
        return azy4;
    }
    
    @Override
    public boolean canMate(final Animal azw) {
        if (!this.isTame()) {
            return false;
        }
        if (!(azw instanceof Cat)) {
            return false;
        }
        final Cat azy3 = (Cat)azw;
        return azy3.isTame() && super.canMate(azw);
    }
    
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable SpawnGroupData aqz, @Nullable final CompoundTag md) {
        aqz = super.finalizeSpawn(bsh, aop, aqm, aqz, md);
        if (bsh.getMoonBrightness() > 0.9f) {
            this.setCatType(this.random.nextInt(11));
        }
        else {
            this.setCatType(this.random.nextInt(10));
        }
        final Level bru7 = bsh.getLevel();
        if (bru7 instanceof ServerLevel && ((ServerLevel)bru7).structureFeatureManager().getStructureAt(this.blockPosition(), true, StructureFeature.SWAMP_HUT).isValid()) {
            this.setCatType(10);
            this.setPersistenceRequired();
        }
        return aqz;
    }
    
    @Override
    public InteractionResult mobInteract(final Player bft, final InteractionHand aoq) {
        final ItemStack bly4 = bft.getItemInHand(aoq);
        final Item blu5 = bly4.getItem();
        if (!this.level.isClientSide) {
            if (this.isTame()) {
                if (this.isOwnedBy(bft)) {
                    if (blu5 instanceof DyeItem) {
                        final DyeColor bku6 = ((DyeItem)blu5).getDyeColor();
                        if (bku6 != this.getCollarColor()) {
                            this.setCollarColor(bku6);
                            if (!bft.abilities.instabuild) {
                                bly4.shrink(1);
                            }
                            this.setPersistenceRequired();
                            return InteractionResult.CONSUME;
                        }
                    }
                    else {
                        if (blu5.isEdible() && this.isFood(bly4) && this.getHealth() < this.getMaxHealth()) {
                            this.usePlayerItem(bft, bly4);
                            this.heal((float)blu5.getFoodProperties().getNutrition());
                            return InteractionResult.CONSUME;
                        }
                        final InteractionResult aor6 = super.mobInteract(bft, aoq);
                        if (!aor6.consumesAction() || this.isBaby()) {
                            this.setOrderedToSit(!this.isOrderedToSit());
                        }
                        return aor6;
                    }
                }
            }
            else if (this.isFood(bly4)) {
                this.usePlayerItem(bft, bly4);
                if (this.random.nextInt(3) == 0) {
                    this.tame(bft);
                    this.setOrderedToSit(true);
                    this.level.broadcastEntityEvent(this, (byte)7);
                }
                else {
                    this.level.broadcastEntityEvent(this, (byte)6);
                }
                this.setPersistenceRequired();
                return InteractionResult.CONSUME;
            }
            final InteractionResult aor6 = super.mobInteract(bft, aoq);
            if (aor6.consumesAction()) {
                this.setPersistenceRequired();
            }
            return aor6;
        }
        if (this.isTame() && this.isOwnedBy(bft)) {
            return InteractionResult.SUCCESS;
        }
        if (this.isFood(bly4) && (this.getHealth() < this.getMaxHealth() || !this.isTame())) {
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
    
    @Override
    public boolean isFood(final ItemStack bly) {
        return Cat.TEMPT_INGREDIENT.test(bly);
    }
    
    @Override
    protected float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return apy.height * 0.5f;
    }
    
    @Override
    public boolean removeWhenFarAway(final double double1) {
        return !this.isTame() && this.tickCount > 2400;
    }
    
    @Override
    protected void reassessTameGoals() {
        if (this.avoidPlayersGoal == null) {
            this.avoidPlayersGoal = new CatAvoidEntityGoal<Player>(this, Player.class, 16.0f, 0.8, 1.33);
        }
        this.goalSelector.removeGoal(this.avoidPlayersGoal);
        if (!this.isTame()) {
            this.goalSelector.addGoal(4, this.avoidPlayersGoal);
        }
    }
    
    static {
        TEMPT_INGREDIENT = Ingredient.of(Items.COD, Items.SALMON);
        DATA_TYPE_ID = SynchedEntityData.<Integer>defineId(Cat.class, EntityDataSerializers.INT);
        IS_LYING = SynchedEntityData.<Boolean>defineId(Cat.class, EntityDataSerializers.BOOLEAN);
        RELAX_STATE_ONE = SynchedEntityData.<Boolean>defineId(Cat.class, EntityDataSerializers.BOOLEAN);
        DATA_COLLAR_COLOR = SynchedEntityData.<Integer>defineId(Cat.class, EntityDataSerializers.INT);
        TEXTURE_BY_TYPE = Util.<Map>make((Map)Maps.newHashMap(), (java.util.function.Consumer<Map>)(hashMap -> {
            hashMap.put(0, new ResourceLocation("textures/entity/cat/tabby.png"));
            hashMap.put(1, new ResourceLocation("textures/entity/cat/black.png"));
            hashMap.put(2, new ResourceLocation("textures/entity/cat/red.png"));
            hashMap.put(3, new ResourceLocation("textures/entity/cat/siamese.png"));
            hashMap.put(4, new ResourceLocation("textures/entity/cat/british_shorthair.png"));
            hashMap.put(5, new ResourceLocation("textures/entity/cat/calico.png"));
            hashMap.put(6, new ResourceLocation("textures/entity/cat/persian.png"));
            hashMap.put(7, new ResourceLocation("textures/entity/cat/ragdoll.png"));
            hashMap.put(8, new ResourceLocation("textures/entity/cat/white.png"));
            hashMap.put(9, new ResourceLocation("textures/entity/cat/jellie.png"));
            hashMap.put(10, new ResourceLocation("textures/entity/cat/all_black.png"));
        }));
    }
    
    static class CatAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
        private final Cat cat;
        
        public CatAvoidEntityGoal(final Cat azy, final Class<T> class2, final float float3, final double double4, final double double5) {
            super(azy, class2, float3, double4, double5, (Predicate<LivingEntity>)EntitySelector.NO_CREATIVE_OR_SPECTATOR::test);
            this.cat = azy;
        }
        
        @Override
        public boolean canUse() {
            return !this.cat.isTame() && super.canUse();
        }
        
        @Override
        public boolean canContinueToUse() {
            return !this.cat.isTame() && super.canContinueToUse();
        }
    }
    
    static class CatTemptGoal extends TemptGoal {
        @Nullable
        private Player selectedPlayer;
        private final Cat cat;
        
        public CatTemptGoal(final Cat azy, final double double2, final Ingredient bok, final boolean boolean4) {
            super(azy, double2, bok, boolean4);
            this.cat = azy;
        }
        
        @Override
        public void tick() {
            super.tick();
            if (this.selectedPlayer == null && this.mob.getRandom().nextInt(600) == 0) {
                this.selectedPlayer = this.player;
            }
            else if (this.mob.getRandom().nextInt(500) == 0) {
                this.selectedPlayer = null;
            }
        }
        
        @Override
        protected boolean canScare() {
            return (this.selectedPlayer == null || !this.selectedPlayer.equals(this.player)) && super.canScare();
        }
        
        @Override
        public boolean canUse() {
            return super.canUse() && !this.cat.isTame();
        }
    }
    
    static class CatRelaxOnOwnerGoal extends Goal {
        private final Cat cat;
        private Player ownerPlayer;
        private BlockPos goalPos;
        private int onBedTicks;
        
        public CatRelaxOnOwnerGoal(final Cat azy) {
            this.cat = azy;
        }
        
        @Override
        public boolean canUse() {
            if (!this.cat.isTame()) {
                return false;
            }
            if (this.cat.isOrderedToSit()) {
                return false;
            }
            final LivingEntity aqj2 = this.cat.getOwner();
            if (aqj2 instanceof Player) {
                this.ownerPlayer = (Player)aqj2;
                if (!aqj2.isSleeping()) {
                    return false;
                }
                if (this.cat.distanceToSqr(this.ownerPlayer) > 100.0) {
                    return false;
                }
                final BlockPos fx3 = this.ownerPlayer.blockPosition();
                final BlockState cee4 = this.cat.level.getBlockState(fx3);
                if (cee4.getBlock().is(BlockTags.BEDS)) {
                    this.goalPos = (BlockPos)cee4.<Comparable>getOptionalValue((Property<Comparable>)BedBlock.FACING).map(gc -> fx3.relative(gc.getOpposite())).orElseGet(() -> new BlockPos(fx3));
                    return !this.spaceIsOccupied();
                }
            }
            return false;
        }
        
        private boolean spaceIsOccupied() {
            final List<Cat> list2 = this.cat.level.<Cat>getEntitiesOfClass((java.lang.Class<? extends Cat>)Cat.class, new AABB(this.goalPos).inflate(2.0));
            for (final Cat azy4 : list2) {
                if (azy4 != this.cat && (azy4.isLying() || azy4.isRelaxStateOne())) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public boolean canContinueToUse() {
            return this.cat.isTame() && !this.cat.isOrderedToSit() && this.ownerPlayer != null && this.ownerPlayer.isSleeping() && this.goalPos != null && !this.spaceIsOccupied();
        }
        
        @Override
        public void start() {
            if (this.goalPos != null) {
                this.cat.setInSittingPose(false);
                this.cat.getNavigation().moveTo(this.goalPos.getX(), this.goalPos.getY(), this.goalPos.getZ(), 1.100000023841858);
            }
        }
        
        @Override
        public void stop() {
            this.cat.setLying(false);
            final float float2 = this.cat.level.getTimeOfDay(1.0f);
            if (this.ownerPlayer.getSleepTimer() >= 100 && float2 > 0.77 && float2 < 0.8 && this.cat.level.getRandom().nextFloat() < 0.7) {
                this.giveMorningGift();
            }
            this.onBedTicks = 0;
            this.cat.setRelaxStateOne(false);
            this.cat.getNavigation().stop();
        }
        
        private void giveMorningGift() {
            final Random random2 = this.cat.getRandom();
            final BlockPos.MutableBlockPos a3 = new BlockPos.MutableBlockPos();
            a3.set(this.cat.blockPosition());
            this.cat.randomTeleport(a3.getX() + random2.nextInt(11) - 5, a3.getY() + random2.nextInt(5) - 2, a3.getZ() + random2.nextInt(11) - 5, false);
            a3.set(this.cat.blockPosition());
            final LootTable cyv4 = this.cat.level.getServer().getLootTables().get(BuiltInLootTables.CAT_MORNING_GIFT);
            final LootContext.Builder a4 = new LootContext.Builder((ServerLevel)this.cat.level).<Vec3>withParameter(LootContextParams.ORIGIN, this.cat.position()).<Entity>withParameter(LootContextParams.THIS_ENTITY, this.cat).withRandom(random2);
            final List<ItemStack> list6 = cyv4.getRandomItems(a4.create(LootContextParamSets.GIFT));
            for (final ItemStack bly8 : list6) {
                this.cat.level.addFreshEntity(new ItemEntity(this.cat.level, a3.getX() - (double)Mth.sin(this.cat.yBodyRot * 0.017453292f), a3.getY(), a3.getZ() + (double)Mth.cos(this.cat.yBodyRot * 0.017453292f), bly8));
            }
        }
        
        @Override
        public void tick() {
            if (this.ownerPlayer != null && this.goalPos != null) {
                this.cat.setInSittingPose(false);
                this.cat.getNavigation().moveTo(this.goalPos.getX(), this.goalPos.getY(), this.goalPos.getZ(), 1.100000023841858);
                if (this.cat.distanceToSqr(this.ownerPlayer) < 2.5) {
                    ++this.onBedTicks;
                    if (this.onBedTicks > 16) {
                        this.cat.setLying(true);
                        this.cat.setRelaxStateOne(false);
                    }
                    else {
                        this.cat.lookAt(this.ownerPlayer, 45.0f, 45.0f);
                        this.cat.setRelaxStateOne(true);
                    }
                }
                else {
                    this.cat.setLying(false);
                }
            }
        }
    }
}

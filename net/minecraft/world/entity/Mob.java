package net.minecraft.world.entity;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
import net.minecraft.core.Vec3i;
import java.util.Optional;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.BlockGetter;
import java.util.Random;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.util.Mth;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.world.Difficulty;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.SwordItem;
import java.util.List;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.storage.loot.LootContext;
import java.util.UUID;
import java.util.Iterator;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import java.util.Arrays;
import com.google.common.collect.Maps;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import java.util.Map;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.ai.sensing.Sensing;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.network.syncher.EntityDataAccessor;

public abstract class Mob extends LivingEntity {
    private static final EntityDataAccessor<Byte> DATA_MOB_FLAGS_ID;
    public int ambientSoundTime;
    protected int xpReward;
    protected LookControl lookControl;
    protected MoveControl moveControl;
    protected JumpControl jumpControl;
    private final BodyRotationControl bodyRotationControl;
    protected PathNavigation navigation;
    protected final GoalSelector goalSelector;
    protected final GoalSelector targetSelector;
    private LivingEntity target;
    private final Sensing sensing;
    private final NonNullList<ItemStack> handItems;
    protected final float[] handDropChances;
    private final NonNullList<ItemStack> armorItems;
    protected final float[] armorDropChances;
    private boolean canPickUpLoot;
    private boolean persistenceRequired;
    private final Map<BlockPathTypes, Float> pathfindingMalus;
    private ResourceLocation lootTable;
    private long lootTableSeed;
    @Nullable
    private Entity leashHolder;
    private int delayedLeashHolderId;
    @Nullable
    private CompoundTag leashInfoTag;
    private BlockPos restrictCenter;
    private float restrictRadius;
    
    protected Mob(final EntityType<? extends Mob> aqb, final Level bru) {
        super(aqb, bru);
        this.handItems = NonNullList.<ItemStack>withSize(2, ItemStack.EMPTY);
        this.handDropChances = new float[2];
        this.armorItems = NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY);
        this.armorDropChances = new float[4];
        this.pathfindingMalus = (Map<BlockPathTypes, Float>)Maps.newEnumMap((Class)BlockPathTypes.class);
        this.restrictCenter = BlockPos.ZERO;
        this.restrictRadius = -1.0f;
        this.goalSelector = new GoalSelector(bru.getProfilerSupplier());
        this.targetSelector = new GoalSelector(bru.getProfilerSupplier());
        this.lookControl = new LookControl(this);
        this.moveControl = new MoveControl(this);
        this.jumpControl = new JumpControl(this);
        this.bodyRotationControl = this.createBodyControl();
        this.navigation = this.createNavigation(bru);
        this.sensing = new Sensing(this);
        Arrays.fill(this.armorDropChances, 0.085f);
        Arrays.fill(this.handDropChances, 0.085f);
        if (bru != null && !bru.isClientSide) {
            this.registerGoals();
        }
    }
    
    protected void registerGoals() {
    }
    
    public static AttributeSupplier.Builder createMobAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.FOLLOW_RANGE, 16.0).add(Attributes.ATTACK_KNOCKBACK);
    }
    
    protected PathNavigation createNavigation(final Level bru) {
        return new GroundPathNavigation(this, bru);
    }
    
    protected boolean shouldPassengersInheritMalus() {
        return false;
    }
    
    public float getPathfindingMalus(final BlockPathTypes cww) {
        Mob aqk3;
        if (this.getVehicle() instanceof Mob && ((Mob)this.getVehicle()).shouldPassengersInheritMalus()) {
            aqk3 = (Mob)this.getVehicle();
        }
        else {
            aqk3 = this;
        }
        final Float float4 = (Float)aqk3.pathfindingMalus.get(cww);
        return (float4 == null) ? cww.getMalus() : float4;
    }
    
    public void setPathfindingMalus(final BlockPathTypes cww, final float float2) {
        this.pathfindingMalus.put(cww, float2);
    }
    
    public boolean canCutCorner(final BlockPathTypes cww) {
        return cww != BlockPathTypes.DANGER_FIRE && cww != BlockPathTypes.DANGER_CACTUS && cww != BlockPathTypes.DANGER_OTHER && cww != BlockPathTypes.WALKABLE_DOOR;
    }
    
    protected BodyRotationControl createBodyControl() {
        return new BodyRotationControl(this);
    }
    
    public LookControl getLookControl() {
        return this.lookControl;
    }
    
    public MoveControl getMoveControl() {
        if (this.isPassenger() && this.getVehicle() instanceof Mob) {
            final Mob aqk2 = (Mob)this.getVehicle();
            return aqk2.getMoveControl();
        }
        return this.moveControl;
    }
    
    public JumpControl getJumpControl() {
        return this.jumpControl;
    }
    
    public PathNavigation getNavigation() {
        if (this.isPassenger() && this.getVehicle() instanceof Mob) {
            final Mob aqk2 = (Mob)this.getVehicle();
            return aqk2.getNavigation();
        }
        return this.navigation;
    }
    
    public Sensing getSensing() {
        return this.sensing;
    }
    
    @Nullable
    public LivingEntity getTarget() {
        return this.target;
    }
    
    public void setTarget(@Nullable final LivingEntity aqj) {
        this.target = aqj;
    }
    
    @Override
    public boolean canAttackType(final EntityType<?> aqb) {
        return aqb != EntityType.GHAST;
    }
    
    public boolean canFireProjectileWeapon(final ProjectileWeaponItem bml) {
        return false;
    }
    
    public void ate() {
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Byte>define(Mob.DATA_MOB_FLAGS_ID, (Byte)0);
    }
    
    public int getAmbientSoundInterval() {
        return 80;
    }
    
    public void playAmbientSound() {
        final SoundEvent adn2 = this.getAmbientSound();
        if (adn2 != null) {
            this.playSound(adn2, this.getSoundVolume(), this.getVoicePitch());
        }
    }
    
    @Override
    public void baseTick() {
        super.baseTick();
        this.level.getProfiler().push("mobBaseTick");
        if (this.isAlive() && this.random.nextInt(1000) < this.ambientSoundTime++) {
            this.resetAmbientSoundTime();
            this.playAmbientSound();
        }
        this.level.getProfiler().pop();
    }
    
    @Override
    protected void playHurtSound(final DamageSource aph) {
        this.resetAmbientSoundTime();
        super.playHurtSound(aph);
    }
    
    private void resetAmbientSoundTime() {
        this.ambientSoundTime = -this.getAmbientSoundInterval();
    }
    
    @Override
    protected int getExperienceReward(final Player bft) {
        if (this.xpReward > 0) {
            int integer3 = this.xpReward;
            for (int integer4 = 0; integer4 < this.armorItems.size(); ++integer4) {
                if (!this.armorItems.get(integer4).isEmpty() && this.armorDropChances[integer4] <= 1.0f) {
                    integer3 += 1 + this.random.nextInt(3);
                }
            }
            for (int integer4 = 0; integer4 < this.handItems.size(); ++integer4) {
                if (!this.handItems.get(integer4).isEmpty() && this.handDropChances[integer4] <= 1.0f) {
                    integer3 += 1 + this.random.nextInt(3);
                }
            }
            return integer3;
        }
        return this.xpReward;
    }
    
    public void spawnAnim() {
        if (this.level.isClientSide) {
            for (int integer2 = 0; integer2 < 20; ++integer2) {
                final double double3 = this.random.nextGaussian() * 0.02;
                final double double4 = this.random.nextGaussian() * 0.02;
                final double double5 = this.random.nextGaussian() * 0.02;
                final double double6 = 10.0;
                this.level.addParticle(ParticleTypes.POOF, this.getX(1.0) - double3 * 10.0, this.getRandomY() - double4 * 10.0, this.getRandomZ(1.0) - double5 * 10.0, double3, double4, double5);
            }
        }
        else {
            this.level.broadcastEntityEvent(this, (byte)20);
        }
    }
    
    @Override
    public void handleEntityEvent(final byte byte1) {
        if (byte1 == 20) {
            this.spawnAnim();
        }
        else {
            super.handleEntityEvent(byte1);
        }
    }
    
    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            this.tickLeash();
            if (this.tickCount % 5 == 0) {
                this.updateControlFlags();
            }
        }
    }
    
    protected void updateControlFlags() {
        final boolean boolean2 = !(this.getControllingPassenger() instanceof Mob);
        final boolean boolean3 = !(this.getVehicle() instanceof Boat);
        this.goalSelector.setControlFlag(Goal.Flag.MOVE, boolean2);
        this.goalSelector.setControlFlag(Goal.Flag.JUMP, boolean2 && boolean3);
        this.goalSelector.setControlFlag(Goal.Flag.LOOK, boolean2);
    }
    
    @Override
    protected float tickHeadTurn(final float float1, final float float2) {
        this.bodyRotationControl.clientTick();
        return float2;
    }
    
    @Nullable
    protected SoundEvent getAmbientSound() {
        return null;
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putBoolean("CanPickUpLoot", this.canPickUpLoot());
        md.putBoolean("PersistenceRequired", this.persistenceRequired);
        final ListTag mj3 = new ListTag();
        for (final ItemStack bly5 : this.armorItems) {
            final CompoundTag md2 = new CompoundTag();
            if (!bly5.isEmpty()) {
                bly5.save(md2);
            }
            mj3.add(md2);
        }
        md.put("ArmorItems", (Tag)mj3);
        final ListTag mj4 = new ListTag();
        for (final ItemStack bly6 : this.handItems) {
            final CompoundTag md3 = new CompoundTag();
            if (!bly6.isEmpty()) {
                bly6.save(md3);
            }
            mj4.add(md3);
        }
        md.put("HandItems", (Tag)mj4);
        final ListTag mj5 = new ListTag();
        for (final float float9 : this.armorDropChances) {
            mj5.add(FloatTag.valueOf(float9));
        }
        md.put("ArmorDropChances", (Tag)mj5);
        final ListTag mj6 = new ListTag();
        for (final float float10 : this.handDropChances) {
            mj6.add(FloatTag.valueOf(float10));
        }
        md.put("HandDropChances", (Tag)mj6);
        if (this.leashHolder != null) {
            final CompoundTag md3 = new CompoundTag();
            if (this.leashHolder instanceof LivingEntity) {
                final UUID uUID8 = this.leashHolder.getUUID();
                md3.putUUID("UUID", uUID8);
            }
            else if (this.leashHolder instanceof HangingEntity) {
                final BlockPos fx8 = ((HangingEntity)this.leashHolder).getPos();
                md3.putInt("X", fx8.getX());
                md3.putInt("Y", fx8.getY());
                md3.putInt("Z", fx8.getZ());
            }
            md.put("Leash", (Tag)md3);
        }
        else if (this.leashInfoTag != null) {
            md.put("Leash", (Tag)this.leashInfoTag.copy());
        }
        md.putBoolean("LeftHanded", this.isLeftHanded());
        if (this.lootTable != null) {
            md.putString("DeathLootTable", this.lootTable.toString());
            if (this.lootTableSeed != 0L) {
                md.putLong("DeathLootTableSeed", this.lootTableSeed);
            }
        }
        if (this.isNoAi()) {
            md.putBoolean("NoAI", this.isNoAi());
        }
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        if (md.contains("CanPickUpLoot", 1)) {
            this.setCanPickUpLoot(md.getBoolean("CanPickUpLoot"));
        }
        this.persistenceRequired = md.getBoolean("PersistenceRequired");
        if (md.contains("ArmorItems", 9)) {
            final ListTag mj3 = md.getList("ArmorItems", 10);
            for (int integer4 = 0; integer4 < this.armorItems.size(); ++integer4) {
                this.armorItems.set(integer4, ItemStack.of(mj3.getCompound(integer4)));
            }
        }
        if (md.contains("HandItems", 9)) {
            final ListTag mj3 = md.getList("HandItems", 10);
            for (int integer4 = 0; integer4 < this.handItems.size(); ++integer4) {
                this.handItems.set(integer4, ItemStack.of(mj3.getCompound(integer4)));
            }
        }
        if (md.contains("ArmorDropChances", 9)) {
            final ListTag mj3 = md.getList("ArmorDropChances", 5);
            for (int integer4 = 0; integer4 < mj3.size(); ++integer4) {
                this.armorDropChances[integer4] = mj3.getFloat(integer4);
            }
        }
        if (md.contains("HandDropChances", 9)) {
            final ListTag mj3 = md.getList("HandDropChances", 5);
            for (int integer4 = 0; integer4 < mj3.size(); ++integer4) {
                this.handDropChances[integer4] = mj3.getFloat(integer4);
            }
        }
        if (md.contains("Leash", 10)) {
            this.leashInfoTag = md.getCompound("Leash");
        }
        this.setLeftHanded(md.getBoolean("LeftHanded"));
        if (md.contains("DeathLootTable", 8)) {
            this.lootTable = new ResourceLocation(md.getString("DeathLootTable"));
            this.lootTableSeed = md.getLong("DeathLootTableSeed");
        }
        this.setNoAi(md.getBoolean("NoAI"));
    }
    
    @Override
    protected void dropFromLootTable(final DamageSource aph, final boolean boolean2) {
        super.dropFromLootTable(aph, boolean2);
        this.lootTable = null;
    }
    
    @Override
    protected LootContext.Builder createLootContext(final boolean boolean1, final DamageSource aph) {
        return super.createLootContext(boolean1, aph).withOptionalRandomSeed(this.lootTableSeed, this.random);
    }
    
    @Override
    public final ResourceLocation getLootTable() {
        return (this.lootTable == null) ? this.getDefaultLootTable() : this.lootTable;
    }
    
    protected ResourceLocation getDefaultLootTable() {
        return super.getLootTable();
    }
    
    public void setZza(final float float1) {
        this.zza = float1;
    }
    
    public void setYya(final float float1) {
        this.yya = float1;
    }
    
    public void setXxa(final float float1) {
        this.xxa = float1;
    }
    
    @Override
    public void setSpeed(final float float1) {
        super.setSpeed(float1);
        this.setZza(float1);
    }
    
    @Override
    public void aiStep() {
        super.aiStep();
        this.level.getProfiler().push("looting");
        if (!this.level.isClientSide && this.canPickUpLoot() && this.isAlive() && !this.dead && this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            final List<ItemEntity> list2 = this.level.<ItemEntity>getEntitiesOfClass((java.lang.Class<? extends ItemEntity>)ItemEntity.class, this.getBoundingBox().inflate(1.0, 0.0, 1.0));
            for (final ItemEntity bcs4 : list2) {
                if (!bcs4.removed && !bcs4.getItem().isEmpty()) {
                    if (bcs4.hasPickUpDelay()) {
                        continue;
                    }
                    if (!this.wantsToPickUp(bcs4.getItem())) {
                        continue;
                    }
                    this.pickUpItem(bcs4);
                }
            }
        }
        this.level.getProfiler().pop();
    }
    
    protected void pickUpItem(final ItemEntity bcs) {
        final ItemStack bly3 = bcs.getItem();
        if (this.equipItemIfPossible(bly3)) {
            this.onItemPickup(bcs);
            this.take(bcs, bly3.getCount());
            bcs.remove();
        }
    }
    
    public boolean equipItemIfPossible(final ItemStack bly) {
        final EquipmentSlot aqc3 = getEquipmentSlotForItem(bly);
        final ItemStack bly2 = this.getItemBySlot(aqc3);
        final boolean boolean5 = this.canReplaceCurrentItem(bly, bly2);
        if (boolean5 && this.canHoldItem(bly)) {
            final double double6 = this.getEquipmentDropChance(aqc3);
            if (!bly2.isEmpty() && Math.max(this.random.nextFloat() - 0.1f, 0.0f) < double6) {
                this.spawnAtLocation(bly2);
            }
            this.setItemSlotAndDropWhenKilled(aqc3, bly);
            this.playEquipSound(bly);
            return true;
        }
        return false;
    }
    
    protected void setItemSlotAndDropWhenKilled(final EquipmentSlot aqc, final ItemStack bly) {
        this.setItemSlot(aqc, bly);
        this.setGuaranteedDrop(aqc);
        this.persistenceRequired = true;
    }
    
    public void setGuaranteedDrop(final EquipmentSlot aqc) {
        switch (aqc.getType()) {
            case HAND: {
                this.handDropChances[aqc.getIndex()] = 2.0f;
                break;
            }
            case ARMOR: {
                this.armorDropChances[aqc.getIndex()] = 2.0f;
                break;
            }
        }
    }
    
    protected boolean canReplaceCurrentItem(final ItemStack bly1, final ItemStack bly2) {
        if (bly2.isEmpty()) {
            return true;
        }
        if (bly1.getItem() instanceof SwordItem) {
            if (!(bly2.getItem() instanceof SwordItem)) {
                return true;
            }
            final SwordItem bnc4 = (SwordItem)bly1.getItem();
            final SwordItem bnc5 = (SwordItem)bly2.getItem();
            if (bnc4.getDamage() != bnc5.getDamage()) {
                return bnc4.getDamage() > bnc5.getDamage();
            }
            return this.canReplaceEqualItem(bly1, bly2);
        }
        else {
            if (bly1.getItem() instanceof BowItem && bly2.getItem() instanceof BowItem) {
                return this.canReplaceEqualItem(bly1, bly2);
            }
            if (bly1.getItem() instanceof CrossbowItem && bly2.getItem() instanceof CrossbowItem) {
                return this.canReplaceEqualItem(bly1, bly2);
            }
            if (!(bly1.getItem() instanceof ArmorItem)) {
                if (bly1.getItem() instanceof DiggerItem) {
                    if (bly2.getItem() instanceof BlockItem) {
                        return true;
                    }
                    if (bly2.getItem() instanceof DiggerItem) {
                        final DiggerItem bks4 = (DiggerItem)bly1.getItem();
                        final DiggerItem bks5 = (DiggerItem)bly2.getItem();
                        if (bks4.getAttackDamage() != bks5.getAttackDamage()) {
                            return bks4.getAttackDamage() > bks5.getAttackDamage();
                        }
                        return this.canReplaceEqualItem(bly1, bly2);
                    }
                }
                return false;
            }
            if (EnchantmentHelper.hasBindingCurse(bly2)) {
                return false;
            }
            if (!(bly2.getItem() instanceof ArmorItem)) {
                return true;
            }
            final ArmorItem bjv4 = (ArmorItem)bly1.getItem();
            final ArmorItem bjv5 = (ArmorItem)bly2.getItem();
            if (bjv4.getDefense() != bjv5.getDefense()) {
                return bjv4.getDefense() > bjv5.getDefense();
            }
            if (bjv4.getToughness() != bjv5.getToughness()) {
                return bjv4.getToughness() > bjv5.getToughness();
            }
            return this.canReplaceEqualItem(bly1, bly2);
        }
    }
    
    public boolean canReplaceEqualItem(final ItemStack bly1, final ItemStack bly2) {
        return bly1.getDamageValue() < bly2.getDamageValue() || (bly1.hasTag() && !bly2.hasTag()) || (bly1.hasTag() && bly2.hasTag() && bly1.getTag().getAllKeys().stream().anyMatch(string -> !string.equals("Damage")) && !bly2.getTag().getAllKeys().stream().anyMatch(string -> !string.equals("Damage")));
    }
    
    public boolean canHoldItem(final ItemStack bly) {
        return true;
    }
    
    public boolean wantsToPickUp(final ItemStack bly) {
        return this.canHoldItem(bly);
    }
    
    public boolean removeWhenFarAway(final double double1) {
        return true;
    }
    
    public boolean requiresCustomPersistence() {
        return this.isPassenger();
    }
    
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }
    
    @Override
    public void checkDespawn() {
        if (this.level.getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) {
            this.remove();
            return;
        }
        if (this.isPersistenceRequired() || this.requiresCustomPersistence()) {
            this.noActionTime = 0;
            return;
        }
        final Entity apx2 = this.level.getNearestPlayer(this, -1.0);
        if (apx2 != null) {
            final double double3 = apx2.distanceToSqr(this);
            final int integer5 = this.getType().getCategory().getDespawnDistance();
            final int integer6 = integer5 * integer5;
            if (double3 > integer6 && this.removeWhenFarAway(double3)) {
                this.remove();
            }
            final int integer7 = this.getType().getCategory().getNoDespawnDistance();
            final int integer8 = integer7 * integer7;
            if (this.noActionTime > 600 && this.random.nextInt(800) == 0 && double3 > integer8 && this.removeWhenFarAway(double3)) {
                this.remove();
            }
            else if (double3 < integer8) {
                this.noActionTime = 0;
            }
        }
    }
    
    @Override
    protected final void serverAiStep() {
        ++this.noActionTime;
        this.level.getProfiler().push("sensing");
        this.sensing.tick();
        this.level.getProfiler().pop();
        this.level.getProfiler().push("targetSelector");
        this.targetSelector.tick();
        this.level.getProfiler().pop();
        this.level.getProfiler().push("goalSelector");
        this.goalSelector.tick();
        this.level.getProfiler().pop();
        this.level.getProfiler().push("navigation");
        this.navigation.tick();
        this.level.getProfiler().pop();
        this.level.getProfiler().push("mob tick");
        this.customServerAiStep();
        this.level.getProfiler().pop();
        this.level.getProfiler().push("controls");
        this.level.getProfiler().push("move");
        this.moveControl.tick();
        this.level.getProfiler().popPush("look");
        this.lookControl.tick();
        this.level.getProfiler().popPush("jump");
        this.jumpControl.tick();
        this.level.getProfiler().pop();
        this.level.getProfiler().pop();
        this.sendDebugPackets();
    }
    
    protected void sendDebugPackets() {
        DebugPackets.sendGoalSelector(this.level, this, this.goalSelector);
    }
    
    protected void customServerAiStep() {
    }
    
    public int getMaxHeadXRot() {
        return 40;
    }
    
    public int getMaxHeadYRot() {
        return 75;
    }
    
    public int getHeadRotSpeed() {
        return 10;
    }
    
    public void lookAt(final Entity apx, final float float2, final float float3) {
        final double double5 = apx.getX() - this.getX();
        final double double6 = apx.getZ() - this.getZ();
        double double7;
        if (apx instanceof LivingEntity) {
            final LivingEntity aqj11 = (LivingEntity)apx;
            double7 = aqj11.getEyeY() - this.getEyeY();
        }
        else {
            double7 = (apx.getBoundingBox().minY + apx.getBoundingBox().maxY) / 2.0 - this.getEyeY();
        }
        final double double8 = Mth.sqrt(double5 * double5 + double6 * double6);
        final float float4 = (float)(Mth.atan2(double6, double5) * 57.2957763671875) - 90.0f;
        final float float5 = (float)(-(Mth.atan2(double7, double8) * 57.2957763671875));
        this.xRot = this.rotlerp(this.xRot, float5, float3);
        this.yRot = this.rotlerp(this.yRot, float4, float2);
    }
    
    private float rotlerp(final float float1, final float float2, final float float3) {
        float float4 = Mth.wrapDegrees(float2 - float1);
        if (float4 > float3) {
            float4 = float3;
        }
        if (float4 < -float3) {
            float4 = -float3;
        }
        return float1 + float4;
    }
    
    public static boolean checkMobSpawnRules(final EntityType<? extends Mob> aqb, final LevelAccessor brv, final MobSpawnType aqm, final BlockPos fx, final Random random) {
        final BlockPos fx2 = fx.below();
        return aqm == MobSpawnType.SPAWNER || brv.getBlockState(fx2).isValidSpawn(brv, fx2, aqb);
    }
    
    public boolean checkSpawnRules(final LevelAccessor brv, final MobSpawnType aqm) {
        return true;
    }
    
    public boolean checkSpawnObstruction(final LevelReader brw) {
        return !brw.containsAnyLiquid(this.getBoundingBox()) && brw.isUnobstructed(this);
    }
    
    public int getMaxSpawnClusterSize() {
        return 4;
    }
    
    public boolean isMaxGroupSizeReached(final int integer) {
        return false;
    }
    
    @Override
    public int getMaxFallDistance() {
        if (this.getTarget() == null) {
            return 3;
        }
        int integer2 = (int)(this.getHealth() - this.getMaxHealth() * 0.33f);
        integer2 -= (3 - this.level.getDifficulty().getId()) * 4;
        if (integer2 < 0) {
            integer2 = 0;
        }
        return integer2 + 3;
    }
    
    @Override
    public Iterable<ItemStack> getHandSlots() {
        return (Iterable<ItemStack>)this.handItems;
    }
    
    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return (Iterable<ItemStack>)this.armorItems;
    }
    
    @Override
    public ItemStack getItemBySlot(final EquipmentSlot aqc) {
        switch (aqc.getType()) {
            case HAND: {
                return this.handItems.get(aqc.getIndex());
            }
            case ARMOR: {
                return this.armorItems.get(aqc.getIndex());
            }
            default: {
                return ItemStack.EMPTY;
            }
        }
    }
    
    @Override
    public void setItemSlot(final EquipmentSlot aqc, final ItemStack bly) {
        switch (aqc.getType()) {
            case HAND: {
                this.handItems.set(aqc.getIndex(), bly);
                break;
            }
            case ARMOR: {
                this.armorItems.set(aqc.getIndex(), bly);
                break;
            }
        }
    }
    
    @Override
    protected void dropCustomDeathLoot(final DamageSource aph, final int integer, final boolean boolean3) {
        super.dropCustomDeathLoot(aph, integer, boolean3);
        for (final EquipmentSlot aqc8 : EquipmentSlot.values()) {
            final ItemStack bly9 = this.getItemBySlot(aqc8);
            final float float10 = this.getEquipmentDropChance(aqc8);
            final boolean boolean4 = float10 > 1.0f;
            if (!bly9.isEmpty() && !EnchantmentHelper.hasVanishingCurse(bly9) && (boolean3 || boolean4) && Math.max(this.random.nextFloat() - integer * 0.01f, 0.0f) < float10) {
                if (!boolean4 && bly9.isDamageableItem()) {
                    bly9.setDamageValue(bly9.getMaxDamage() - this.random.nextInt(1 + this.random.nextInt(Math.max(bly9.getMaxDamage() - 3, 1))));
                }
                this.spawnAtLocation(bly9);
            }
        }
    }
    
    protected float getEquipmentDropChance(final EquipmentSlot aqc) {
        float float3 = 0.0f;
        switch (aqc.getType()) {
            case HAND: {
                float3 = this.handDropChances[aqc.getIndex()];
                break;
            }
            case ARMOR: {
                float3 = this.armorDropChances[aqc.getIndex()];
                break;
            }
            default: {
                float3 = 0.0f;
                break;
            }
        }
        return float3;
    }
    
    protected void populateDefaultEquipmentSlots(final DifficultyInstance aop) {
        if (this.random.nextFloat() < 0.15f * aop.getSpecialMultiplier()) {
            int integer3 = this.random.nextInt(2);
            final float float4 = (this.level.getDifficulty() == Difficulty.HARD) ? 0.1f : 0.25f;
            if (this.random.nextFloat() < 0.095f) {
                ++integer3;
            }
            if (this.random.nextFloat() < 0.095f) {
                ++integer3;
            }
            if (this.random.nextFloat() < 0.095f) {
                ++integer3;
            }
            boolean boolean5 = true;
            for (final EquipmentSlot aqc9 : EquipmentSlot.values()) {
                if (aqc9.getType() == EquipmentSlot.Type.ARMOR) {
                    final ItemStack bly10 = this.getItemBySlot(aqc9);
                    if (!boolean5 && this.random.nextFloat() < float4) {
                        break;
                    }
                    boolean5 = false;
                    if (bly10.isEmpty()) {
                        final Item blu11 = getEquipmentForSlot(aqc9, integer3);
                        if (blu11 != null) {
                            this.setItemSlot(aqc9, new ItemStack(blu11));
                        }
                    }
                }
            }
        }
    }
    
    public static EquipmentSlot getEquipmentSlotForItem(final ItemStack bly) {
        final Item blu2 = bly.getItem();
        if (blu2 == Blocks.CARVED_PUMPKIN.asItem() || (blu2 instanceof BlockItem && ((BlockItem)blu2).getBlock() instanceof AbstractSkullBlock)) {
            return EquipmentSlot.HEAD;
        }
        if (blu2 instanceof ArmorItem) {
            return ((ArmorItem)blu2).getSlot();
        }
        if (blu2 == Items.ELYTRA) {
            return EquipmentSlot.CHEST;
        }
        if (blu2 == Items.SHIELD) {
            return EquipmentSlot.OFFHAND;
        }
        return EquipmentSlot.MAINHAND;
    }
    
    @Nullable
    public static Item getEquipmentForSlot(final EquipmentSlot aqc, final int integer) {
        switch (aqc) {
            case HEAD: {
                if (integer == 0) {
                    return Items.LEATHER_HELMET;
                }
                if (integer == 1) {
                    return Items.GOLDEN_HELMET;
                }
                if (integer == 2) {
                    return Items.CHAINMAIL_HELMET;
                }
                if (integer == 3) {
                    return Items.IRON_HELMET;
                }
                if (integer == 4) {
                    return Items.DIAMOND_HELMET;
                }
            }
            case CHEST: {
                if (integer == 0) {
                    return Items.LEATHER_CHESTPLATE;
                }
                if (integer == 1) {
                    return Items.GOLDEN_CHESTPLATE;
                }
                if (integer == 2) {
                    return Items.CHAINMAIL_CHESTPLATE;
                }
                if (integer == 3) {
                    return Items.IRON_CHESTPLATE;
                }
                if (integer == 4) {
                    return Items.DIAMOND_CHESTPLATE;
                }
            }
            case LEGS: {
                if (integer == 0) {
                    return Items.LEATHER_LEGGINGS;
                }
                if (integer == 1) {
                    return Items.GOLDEN_LEGGINGS;
                }
                if (integer == 2) {
                    return Items.CHAINMAIL_LEGGINGS;
                }
                if (integer == 3) {
                    return Items.IRON_LEGGINGS;
                }
                if (integer == 4) {
                    return Items.DIAMOND_LEGGINGS;
                }
            }
            case FEET: {
                if (integer == 0) {
                    return Items.LEATHER_BOOTS;
                }
                if (integer == 1) {
                    return Items.GOLDEN_BOOTS;
                }
                if (integer == 2) {
                    return Items.CHAINMAIL_BOOTS;
                }
                if (integer == 3) {
                    return Items.IRON_BOOTS;
                }
                if (integer == 4) {
                    return Items.DIAMOND_BOOTS;
                }
                break;
            }
        }
        return null;
    }
    
    protected void populateDefaultEquipmentEnchantments(final DifficultyInstance aop) {
        final float float3 = aop.getSpecialMultiplier();
        this.enchantSpawnedWeapon(float3);
        for (final EquipmentSlot aqc7 : EquipmentSlot.values()) {
            if (aqc7.getType() == EquipmentSlot.Type.ARMOR) {
                this.enchantSpawnedArmor(float3, aqc7);
            }
        }
    }
    
    protected void enchantSpawnedWeapon(final float float1) {
        if (!this.getMainHandItem().isEmpty() && this.random.nextFloat() < 0.25f * float1) {
            this.setItemSlot(EquipmentSlot.MAINHAND, EnchantmentHelper.enchantItem(this.random, this.getMainHandItem(), (int)(5.0f + float1 * this.random.nextInt(18)), false));
        }
    }
    
    protected void enchantSpawnedArmor(final float float1, final EquipmentSlot aqc) {
        final ItemStack bly4 = this.getItemBySlot(aqc);
        if (!bly4.isEmpty() && this.random.nextFloat() < 0.5f * float1) {
            this.setItemSlot(aqc, EnchantmentHelper.enchantItem(this.random, bly4, (int)(5.0f + float1 * this.random.nextInt(18)), false));
        }
    }
    
    @Nullable
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable final SpawnGroupData aqz, @Nullable final CompoundTag md) {
        this.getAttribute(Attributes.FOLLOW_RANGE).addPermanentModifier(new AttributeModifier("Random spawn bonus", this.random.nextGaussian() * 0.05, AttributeModifier.Operation.MULTIPLY_BASE));
        if (this.random.nextFloat() < 0.05f) {
            this.setLeftHanded(true);
        }
        else {
            this.setLeftHanded(false);
        }
        return aqz;
    }
    
    public boolean canBeControlledByRider() {
        return false;
    }
    
    public void setPersistenceRequired() {
        this.persistenceRequired = true;
    }
    
    public void setDropChance(final EquipmentSlot aqc, final float float2) {
        switch (aqc.getType()) {
            case HAND: {
                this.handDropChances[aqc.getIndex()] = float2;
                break;
            }
            case ARMOR: {
                this.armorDropChances[aqc.getIndex()] = float2;
                break;
            }
        }
    }
    
    public boolean canPickUpLoot() {
        return this.canPickUpLoot;
    }
    
    public void setCanPickUpLoot(final boolean boolean1) {
        this.canPickUpLoot = boolean1;
    }
    
    @Override
    public boolean canTakeItem(final ItemStack bly) {
        final EquipmentSlot aqc3 = getEquipmentSlotForItem(bly);
        return this.getItemBySlot(aqc3).isEmpty() && this.canPickUpLoot();
    }
    
    public boolean isPersistenceRequired() {
        return this.persistenceRequired;
    }
    
    @Override
    public final InteractionResult interact(final Player bft, final InteractionHand aoq) {
        if (!this.isAlive()) {
            return InteractionResult.PASS;
        }
        if (this.getLeashHolder() == bft) {
            this.dropLeash(true, !bft.abilities.instabuild);
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        InteractionResult aor4 = this.checkAndHandleImportantInteractions(bft, aoq);
        if (aor4.consumesAction()) {
            return aor4;
        }
        aor4 = this.mobInteract(bft, aoq);
        if (aor4.consumesAction()) {
            return aor4;
        }
        return super.interact(bft, aoq);
    }
    
    private InteractionResult checkAndHandleImportantInteractions(final Player bft, final InteractionHand aoq) {
        final ItemStack bly4 = bft.getItemInHand(aoq);
        if (bly4.getItem() == Items.LEAD && this.canBeLeashed(bft)) {
            this.setLeashedTo(bft, true);
            bly4.shrink(1);
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        if (bly4.getItem() == Items.NAME_TAG) {
            final InteractionResult aor5 = bly4.interactLivingEntity(bft, this, aoq);
            if (aor5.consumesAction()) {
                return aor5;
            }
        }
        if (!(bly4.getItem() instanceof SpawnEggItem)) {
            return InteractionResult.PASS;
        }
        if (this.level instanceof ServerLevel) {
            final SpawnEggItem bmx5 = (SpawnEggItem)bly4.getItem();
            final Optional<Mob> optional6 = bmx5.spawnOffspringFromSpawnEgg(bft, this, this.getType(), (ServerLevel)this.level, this.position(), bly4);
            optional6.ifPresent(aqk -> this.onOffspringSpawnedFromEgg(bft, aqk));
            return optional6.isPresent() ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
        return InteractionResult.CONSUME;
    }
    
    protected void onOffspringSpawnedFromEgg(final Player bft, final Mob aqk) {
    }
    
    protected InteractionResult mobInteract(final Player bft, final InteractionHand aoq) {
        return InteractionResult.PASS;
    }
    
    public boolean isWithinRestriction() {
        return this.isWithinRestriction(this.blockPosition());
    }
    
    public boolean isWithinRestriction(final BlockPos fx) {
        return this.restrictRadius == -1.0f || this.restrictCenter.distSqr(fx) < this.restrictRadius * this.restrictRadius;
    }
    
    public void restrictTo(final BlockPos fx, final int integer) {
        this.restrictCenter = fx;
        this.restrictRadius = (float)integer;
    }
    
    public BlockPos getRestrictCenter() {
        return this.restrictCenter;
    }
    
    public float getRestrictRadius() {
        return this.restrictRadius;
    }
    
    public boolean hasRestriction() {
        return this.restrictRadius != -1.0f;
    }
    
    @Nullable
    public <T extends Mob> T convertTo(final EntityType<T> aqb, final boolean boolean2) {
        if (this.removed) {
            return null;
        }
        final T aqk4 = aqb.create(this.level);
        aqk4.copyPosition(this);
        aqk4.setBaby(this.isBaby());
        aqk4.setNoAi(this.isNoAi());
        if (this.hasCustomName()) {
            aqk4.setCustomName(this.getCustomName());
            aqk4.setCustomNameVisible(this.isCustomNameVisible());
        }
        if (this.isPersistenceRequired()) {
            aqk4.setPersistenceRequired();
        }
        aqk4.setInvulnerable(this.isInvulnerable());
        if (boolean2) {
            aqk4.setCanPickUpLoot(this.canPickUpLoot());
            for (final EquipmentSlot aqc8 : EquipmentSlot.values()) {
                final ItemStack bly9 = this.getItemBySlot(aqc8);
                if (!bly9.isEmpty()) {
                    aqk4.setItemSlot(aqc8, bly9.copy());
                    aqk4.setDropChance(aqc8, this.getEquipmentDropChance(aqc8));
                    bly9.setCount(0);
                }
            }
        }
        this.level.addFreshEntity(aqk4);
        if (this.isPassenger()) {
            final Entity apx5 = this.getVehicle();
            this.stopRiding();
            aqk4.startRiding(apx5, true);
        }
        this.remove();
        return aqk4;
    }
    
    protected void tickLeash() {
        if (this.leashInfoTag != null) {
            this.restoreLeashFromSave();
        }
        if (this.leashHolder == null) {
            return;
        }
        if (!this.isAlive() || !this.leashHolder.isAlive()) {
            this.dropLeash(true, true);
        }
    }
    
    public void dropLeash(final boolean boolean1, final boolean boolean2) {
        if (this.leashHolder != null) {
            this.forcedLoading = false;
            if (!(this.leashHolder instanceof Player)) {
                this.leashHolder.forcedLoading = false;
            }
            this.leashHolder = null;
            this.leashInfoTag = null;
            if (!this.level.isClientSide && boolean2) {
                this.spawnAtLocation(Items.LEAD);
            }
            if (!this.level.isClientSide && boolean1 && this.level instanceof ServerLevel) {
                ((ServerLevel)this.level).getChunkSource().broadcast(this, new ClientboundSetEntityLinkPacket(this, null));
            }
        }
    }
    
    public boolean canBeLeashed(final Player bft) {
        return !this.isLeashed() && !(this instanceof Enemy);
    }
    
    public boolean isLeashed() {
        return this.leashHolder != null;
    }
    
    @Nullable
    public Entity getLeashHolder() {
        if (this.leashHolder == null && this.delayedLeashHolderId != 0 && this.level.isClientSide) {
            this.leashHolder = this.level.getEntity(this.delayedLeashHolderId);
        }
        return this.leashHolder;
    }
    
    public void setLeashedTo(final Entity apx, final boolean boolean2) {
        this.leashHolder = apx;
        this.leashInfoTag = null;
        this.forcedLoading = true;
        if (!(this.leashHolder instanceof Player)) {
            this.leashHolder.forcedLoading = true;
        }
        if (!this.level.isClientSide && boolean2 && this.level instanceof ServerLevel) {
            ((ServerLevel)this.level).getChunkSource().broadcast(this, new ClientboundSetEntityLinkPacket(this, this.leashHolder));
        }
        if (this.isPassenger()) {
            this.stopRiding();
        }
    }
    
    public void setDelayedLeashHolderId(final int integer) {
        this.delayedLeashHolderId = integer;
        this.dropLeash(false, false);
    }
    
    @Override
    public boolean startRiding(final Entity apx, final boolean boolean2) {
        final boolean boolean3 = super.startRiding(apx, boolean2);
        if (boolean3 && this.isLeashed()) {
            this.dropLeash(true, true);
        }
        return boolean3;
    }
    
    private void restoreLeashFromSave() {
        if (this.leashInfoTag != null && this.level instanceof ServerLevel) {
            if (this.leashInfoTag.hasUUID("UUID")) {
                final UUID uUID2 = this.leashInfoTag.getUUID("UUID");
                final Entity apx3 = ((ServerLevel)this.level).getEntity(uUID2);
                if (apx3 != null) {
                    this.setLeashedTo(apx3, true);
                    return;
                }
            }
            else if (this.leashInfoTag.contains("X", 99) && this.leashInfoTag.contains("Y", 99) && this.leashInfoTag.contains("Z", 99)) {
                final BlockPos fx2 = new BlockPos(this.leashInfoTag.getInt("X"), this.leashInfoTag.getInt("Y"), this.leashInfoTag.getInt("Z"));
                this.setLeashedTo(LeashFenceKnotEntity.getOrCreateKnot(this.level, fx2), true);
                return;
            }
            if (this.tickCount > 100) {
                this.spawnAtLocation(Items.LEAD);
                this.leashInfoTag = null;
            }
        }
    }
    
    @Override
    public boolean setSlot(final int integer, final ItemStack bly) {
        EquipmentSlot aqc4;
        if (integer == 98) {
            aqc4 = EquipmentSlot.MAINHAND;
        }
        else if (integer == 99) {
            aqc4 = EquipmentSlot.OFFHAND;
        }
        else if (integer == 100 + EquipmentSlot.HEAD.getIndex()) {
            aqc4 = EquipmentSlot.HEAD;
        }
        else if (integer == 100 + EquipmentSlot.CHEST.getIndex()) {
            aqc4 = EquipmentSlot.CHEST;
        }
        else if (integer == 100 + EquipmentSlot.LEGS.getIndex()) {
            aqc4 = EquipmentSlot.LEGS;
        }
        else {
            if (integer != 100 + EquipmentSlot.FEET.getIndex()) {
                return false;
            }
            aqc4 = EquipmentSlot.FEET;
        }
        if (bly.isEmpty() || isValidSlotForItem(aqc4, bly) || aqc4 == EquipmentSlot.HEAD) {
            this.setItemSlot(aqc4, bly);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean isControlledByLocalInstance() {
        return this.canBeControlledByRider() && super.isControlledByLocalInstance();
    }
    
    public static boolean isValidSlotForItem(final EquipmentSlot aqc, final ItemStack bly) {
        final EquipmentSlot aqc2 = getEquipmentSlotForItem(bly);
        return aqc2 == aqc || (aqc2 == EquipmentSlot.MAINHAND && aqc == EquipmentSlot.OFFHAND) || (aqc2 == EquipmentSlot.OFFHAND && aqc == EquipmentSlot.MAINHAND);
    }
    
    @Override
    public boolean isEffectiveAi() {
        return super.isEffectiveAi() && !this.isNoAi();
    }
    
    public void setNoAi(final boolean boolean1) {
        final byte byte3 = this.entityData.<Byte>get(Mob.DATA_MOB_FLAGS_ID);
        this.entityData.<Byte>set(Mob.DATA_MOB_FLAGS_ID, boolean1 ? ((byte)(byte3 | 0x1)) : ((byte)(byte3 & 0xFFFFFFFE)));
    }
    
    public void setLeftHanded(final boolean boolean1) {
        final byte byte3 = this.entityData.<Byte>get(Mob.DATA_MOB_FLAGS_ID);
        this.entityData.<Byte>set(Mob.DATA_MOB_FLAGS_ID, boolean1 ? ((byte)(byte3 | 0x2)) : ((byte)(byte3 & 0xFFFFFFFD)));
    }
    
    public void setAggressive(final boolean boolean1) {
        final byte byte3 = this.entityData.<Byte>get(Mob.DATA_MOB_FLAGS_ID);
        this.entityData.<Byte>set(Mob.DATA_MOB_FLAGS_ID, boolean1 ? ((byte)(byte3 | 0x4)) : ((byte)(byte3 & 0xFFFFFFFB)));
    }
    
    public boolean isNoAi() {
        return (this.entityData.<Byte>get(Mob.DATA_MOB_FLAGS_ID) & 0x1) != 0x0;
    }
    
    public boolean isLeftHanded() {
        return (this.entityData.<Byte>get(Mob.DATA_MOB_FLAGS_ID) & 0x2) != 0x0;
    }
    
    public boolean isAggressive() {
        return (this.entityData.<Byte>get(Mob.DATA_MOB_FLAGS_ID) & 0x4) != 0x0;
    }
    
    public void setBaby(final boolean boolean1) {
    }
    
    @Override
    public HumanoidArm getMainArm() {
        return this.isLeftHanded() ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
    }
    
    @Override
    public boolean canAttack(final LivingEntity aqj) {
        return (aqj.getType() != EntityType.PLAYER || !((Player)aqj).abilities.invulnerable) && super.canAttack(aqj);
    }
    
    @Override
    public boolean doHurtTarget(final Entity apx) {
        float float3 = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float float4 = (float)this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        if (apx instanceof LivingEntity) {
            float3 += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity)apx).getMobType());
            float4 += EnchantmentHelper.getKnockbackBonus(this);
        }
        final int integer5 = EnchantmentHelper.getFireAspect(this);
        if (integer5 > 0) {
            apx.setSecondsOnFire(integer5 * 4);
        }
        final boolean boolean6 = apx.hurt(DamageSource.mobAttack(this), float3);
        if (boolean6) {
            if (float4 > 0.0f && apx instanceof LivingEntity) {
                ((LivingEntity)apx).knockback(float4 * 0.5f, Mth.sin(this.yRot * 0.017453292f), -Mth.cos(this.yRot * 0.017453292f));
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 1.0, 0.6));
            }
            if (apx instanceof Player) {
                final Player bft7 = (Player)apx;
                this.maybeDisableShield(bft7, this.getMainHandItem(), bft7.isUsingItem() ? bft7.getUseItem() : ItemStack.EMPTY);
            }
            this.doEnchantDamageEffects(this, apx);
            this.setLastHurtMob(apx);
        }
        return boolean6;
    }
    
    private void maybeDisableShield(final Player bft, final ItemStack bly2, final ItemStack bly3) {
        if (!bly2.isEmpty() && !bly3.isEmpty() && bly2.getItem() instanceof AxeItem && bly3.getItem() == Items.SHIELD) {
            final float float5 = 0.25f + EnchantmentHelper.getBlockEfficiency(this) * 0.05f;
            if (this.random.nextFloat() < float5) {
                bft.getCooldowns().addCooldown(Items.SHIELD, 100);
                this.level.broadcastEntityEvent(bft, (byte)30);
            }
        }
    }
    
    protected boolean isSunBurnTick() {
        if (this.level.isDay() && !this.level.isClientSide) {
            final float float2 = this.getBrightness();
            final BlockPos fx3 = (this.getVehicle() instanceof Boat) ? new BlockPos(this.getX(), (double)Math.round(this.getY()), this.getZ()).above() : new BlockPos(this.getX(), (double)Math.round(this.getY()), this.getZ());
            if (float2 > 0.5f && this.random.nextFloat() * 30.0f < (float2 - 0.4f) * 2.0f && this.level.canSeeSky(fx3)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    protected void jumpInLiquid(final net.minecraft.tags.Tag<Fluid> aej) {
        if (this.getNavigation().canFloat()) {
            super.jumpInLiquid(aej);
        }
        else {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.3, 0.0));
        }
    }
    
    @Override
    protected void removeAfterChangingDimensions() {
        super.removeAfterChangingDimensions();
        this.dropLeash(true, false);
    }
    
    static {
        DATA_MOB_FLAGS_ID = SynchedEntityData.<Byte>defineId(Mob.class, EntityDataSerializers.BYTE);
    }
}

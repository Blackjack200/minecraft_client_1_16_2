package net.minecraft.world.entity.animal;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.core.Position;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.CropBlock;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.Comparator;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import java.util.Iterator;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.Blocks;
import java.util.Optional;
import net.minecraft.world.entity.ai.control.LookControl;
import com.google.common.collect.Lists;
import net.minecraft.world.level.pathfinder.Path;
import java.util.List;
import java.util.EnumSet;
import java.util.function.Predicate;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.util.TimeUtil;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import java.util.Random;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.Mth;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import java.util.UUID;
import net.minecraft.util.IntRange;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.NeutralMob;

public class Bee extends Animal implements NeutralMob, FlyingAnimal {
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID;
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME;
    private static final IntRange PERSISTENT_ANGER_TIME;
    private UUID persistentAngerTarget;
    private float rollAmount;
    private float rollAmountO;
    private int timeSinceSting;
    private int ticksWithoutNectarSinceExitingHive;
    private int stayOutOfHiveCountdown;
    private int numCropsGrownSincePollination;
    private int remainingCooldownBeforeLocatingNewHive;
    private int remainingCooldownBeforeLocatingNewFlower;
    @Nullable
    private BlockPos savedFlowerPos;
    @Nullable
    private BlockPos hivePos;
    private BeePollinateGoal beePollinateGoal;
    private BeeGoToHiveGoal goToHiveGoal;
    private BeeGoToKnownFlowerGoal goToKnownFlowerGoal;
    private int underWaterTicks;
    
    public Bee(final EntityType<? extends Bee> aqb, final Level bru) {
        super(aqb, bru);
        this.remainingCooldownBeforeLocatingNewHive = 0;
        this.remainingCooldownBeforeLocatingNewFlower = 0;
        this.savedFlowerPos = null;
        this.hivePos = null;
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.lookControl = new BeeLookControl(this);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0f);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0f);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 16.0f);
        this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0f);
        this.setPathfindingMalus(BlockPathTypes.FENCE, -1.0f);
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Byte>define(Bee.DATA_FLAGS_ID, (Byte)0);
        this.entityData.<Integer>define(Bee.DATA_REMAINING_ANGER_TIME, 0);
    }
    
    @Override
    public float getWalkTargetValue(final BlockPos fx, final LevelReader brw) {
        if (brw.getBlockState(fx).isAir()) {
            return 10.0f;
        }
        return 0.0f;
    }
    
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new BeeAttackGoal(this, 1.399999976158142, true));
        this.goalSelector.addGoal(1, new BeeEnterHiveGoal());
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, Ingredient.of(ItemTags.FLOWERS), false));
        this.beePollinateGoal = new BeePollinateGoal();
        this.goalSelector.addGoal(4, this.beePollinateGoal);
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(5, new BeeLocateHiveGoal());
        this.goToHiveGoal = new BeeGoToHiveGoal();
        this.goalSelector.addGoal(5, this.goToHiveGoal);
        this.goToKnownFlowerGoal = new BeeGoToKnownFlowerGoal();
        this.goalSelector.addGoal(6, this.goToKnownFlowerGoal);
        this.goalSelector.addGoal(7, new BeeGrowCropGoal());
        this.goalSelector.addGoal(8, new BeeWanderGoal());
        this.goalSelector.addGoal(9, new FloatGoal(this));
        this.targetSelector.addGoal(1, new BeeHurtByOtherGoal(this).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(2, new BeeBecomeAngryTargetGoal(this));
        this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, true));
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        if (this.hasHive()) {
            md.put("HivePos", (net.minecraft.nbt.Tag)NbtUtils.writeBlockPos(this.getHivePos()));
        }
        if (this.hasSavedFlowerPos()) {
            md.put("FlowerPos", (net.minecraft.nbt.Tag)NbtUtils.writeBlockPos(this.getSavedFlowerPos()));
        }
        md.putBoolean("HasNectar", this.hasNectar());
        md.putBoolean("HasStung", this.hasStung());
        md.putInt("TicksSincePollination", this.ticksWithoutNectarSinceExitingHive);
        md.putInt("CannotEnterHiveTicks", this.stayOutOfHiveCountdown);
        md.putInt("CropsGrownSincePollination", this.numCropsGrownSincePollination);
        this.addPersistentAngerSaveData(md);
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        this.hivePos = null;
        if (md.contains("HivePos")) {
            this.hivePos = NbtUtils.readBlockPos(md.getCompound("HivePos"));
        }
        this.savedFlowerPos = null;
        if (md.contains("FlowerPos")) {
            this.savedFlowerPos = NbtUtils.readBlockPos(md.getCompound("FlowerPos"));
        }
        super.readAdditionalSaveData(md);
        this.setHasNectar(md.getBoolean("HasNectar"));
        this.setHasStung(md.getBoolean("HasStung"));
        this.ticksWithoutNectarSinceExitingHive = md.getInt("TicksSincePollination");
        this.stayOutOfHiveCountdown = md.getInt("CannotEnterHiveTicks");
        this.numCropsGrownSincePollination = md.getInt("CropsGrownSincePollination");
        this.readPersistentAngerSaveData((ServerLevel)this.level, md);
    }
    
    public boolean doHurtTarget(final Entity apx) {
        final boolean boolean3 = apx.hurt(DamageSource.sting(this), (float)(int)this.getAttributeValue(Attributes.ATTACK_DAMAGE));
        if (boolean3) {
            this.doEnchantDamageEffects(this, apx);
            if (apx instanceof LivingEntity) {
                ((LivingEntity)apx).setStingerCount(((LivingEntity)apx).getStingerCount() + 1);
                int integer4 = 0;
                if (this.level.getDifficulty() == Difficulty.NORMAL) {
                    integer4 = 10;
                }
                else if (this.level.getDifficulty() == Difficulty.HARD) {
                    integer4 = 18;
                }
                if (integer4 > 0) {
                    ((LivingEntity)apx).addEffect(new MobEffectInstance(MobEffects.POISON, integer4 * 20, 0));
                }
            }
            this.setHasStung(true);
            this.stopBeingAngry();
            this.playSound(SoundEvents.BEE_STING, 1.0f, 1.0f);
        }
        return boolean3;
    }
    
    public void tick() {
        super.tick();
        if (this.hasNectar() && this.getCropsGrownSincePollination() < 10 && this.random.nextFloat() < 0.05f) {
            for (int integer2 = 0; integer2 < this.random.nextInt(2) + 1; ++integer2) {
                this.spawnFluidParticle(this.level, this.getX() - 0.30000001192092896, this.getX() + 0.30000001192092896, this.getZ() - 0.30000001192092896, this.getZ() + 0.30000001192092896, this.getY(0.5), ParticleTypes.FALLING_NECTAR);
            }
        }
        this.updateRollAmount();
    }
    
    private void spawnFluidParticle(final Level bru, final double double2, final double double3, final double double4, final double double5, final double double6, final ParticleOptions hf) {
        bru.addParticle(hf, Mth.lerp(bru.random.nextDouble(), double2, double3), double6, Mth.lerp(bru.random.nextDouble(), double4, double5), 0.0, 0.0, 0.0);
    }
    
    private void pathfindRandomlyTowards(final BlockPos fx) {
        final Vec3 dck3 = Vec3.atBottomCenterOf(fx);
        int integer4 = 0;
        final BlockPos fx2 = this.blockPosition();
        final int integer5 = (int)dck3.y - fx2.getY();
        if (integer5 > 2) {
            integer4 = 4;
        }
        else if (integer5 < -2) {
            integer4 = -4;
        }
        int integer6 = 6;
        int integer7 = 8;
        final int integer8 = fx2.distManhattan(fx);
        if (integer8 < 15) {
            integer6 = integer8 / 2;
            integer7 = integer8 / 2;
        }
        final Vec3 dck4 = RandomPos.getAirPosTowards(this, integer6, integer7, integer4, dck3, 0.3141592741012573);
        if (dck4 == null) {
            return;
        }
        this.navigation.setMaxVisitedNodesMultiplier(0.5f);
        this.navigation.moveTo(dck4.x, dck4.y, dck4.z, 1.0);
    }
    
    @Nullable
    public BlockPos getSavedFlowerPos() {
        return this.savedFlowerPos;
    }
    
    public boolean hasSavedFlowerPos() {
        return this.savedFlowerPos != null;
    }
    
    public void setSavedFlowerPos(final BlockPos fx) {
        this.savedFlowerPos = fx;
    }
    
    private boolean isTiredOfLookingForNectar() {
        return this.ticksWithoutNectarSinceExitingHive > 3600;
    }
    
    private boolean wantsToEnterHive() {
        if (this.stayOutOfHiveCountdown > 0 || this.beePollinateGoal.isPollinating() || this.hasStung() || this.getTarget() != null) {
            return false;
        }
        final boolean boolean2 = this.isTiredOfLookingForNectar() || this.level.isRaining() || this.level.isNight() || this.hasNectar();
        return boolean2 && !this.isHiveNearFire();
    }
    
    public void setStayOutOfHiveCountdown(final int integer) {
        this.stayOutOfHiveCountdown = integer;
    }
    
    public float getRollAmount(final float float1) {
        return Mth.lerp(float1, this.rollAmountO, this.rollAmount);
    }
    
    private void updateRollAmount() {
        this.rollAmountO = this.rollAmount;
        if (this.isRolling()) {
            this.rollAmount = Math.min(1.0f, this.rollAmount + 0.2f);
        }
        else {
            this.rollAmount = Math.max(0.0f, this.rollAmount - 0.24f);
        }
    }
    
    @Override
    protected void customServerAiStep() {
        final boolean boolean2 = this.hasStung();
        if (this.isInWaterOrBubble()) {
            ++this.underWaterTicks;
        }
        else {
            this.underWaterTicks = 0;
        }
        if (this.underWaterTicks > 20) {
            this.hurt(DamageSource.DROWN, 1.0f);
        }
        if (boolean2) {
            ++this.timeSinceSting;
            if (this.timeSinceSting % 5 == 0 && this.random.nextInt(Mth.clamp(1200 - this.timeSinceSting, 1, 1200)) == 0) {
                this.hurt(DamageSource.GENERIC, this.getHealth());
            }
        }
        if (!this.hasNectar()) {
            ++this.ticksWithoutNectarSinceExitingHive;
        }
        if (!this.level.isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level, false);
        }
    }
    
    public void resetTicksWithoutNectarSinceExitingHive() {
        this.ticksWithoutNectarSinceExitingHive = 0;
    }
    
    private boolean isHiveNearFire() {
        if (this.hivePos == null) {
            return false;
        }
        final BlockEntity ccg2 = this.level.getBlockEntity(this.hivePos);
        return ccg2 instanceof BeehiveBlockEntity && ((BeehiveBlockEntity)ccg2).isFireNearby();
    }
    
    @Override
    public int getRemainingPersistentAngerTime() {
        return this.entityData.<Integer>get(Bee.DATA_REMAINING_ANGER_TIME);
    }
    
    @Override
    public void setRemainingPersistentAngerTime(final int integer) {
        this.entityData.<Integer>set(Bee.DATA_REMAINING_ANGER_TIME, integer);
    }
    
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }
    
    @Override
    public void setPersistentAngerTarget(@Nullable final UUID uUID) {
        this.persistentAngerTarget = uUID;
    }
    
    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(Bee.PERSISTENT_ANGER_TIME.randomValue(this.random));
    }
    
    private boolean doesHiveHaveSpace(final BlockPos fx) {
        final BlockEntity ccg3 = this.level.getBlockEntity(fx);
        return ccg3 instanceof BeehiveBlockEntity && !((BeehiveBlockEntity)ccg3).isFull();
    }
    
    public boolean hasHive() {
        return this.hivePos != null;
    }
    
    @Nullable
    public BlockPos getHivePos() {
        return this.hivePos;
    }
    
    protected void sendDebugPackets() {
        super.sendDebugPackets();
        DebugPackets.sendBeeInfo(this);
    }
    
    private int getCropsGrownSincePollination() {
        return this.numCropsGrownSincePollination;
    }
    
    private void resetNumCropsGrownSincePollination() {
        this.numCropsGrownSincePollination = 0;
    }
    
    private void incrementNumCropsGrownSincePollination() {
        ++this.numCropsGrownSincePollination;
    }
    
    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide) {
            if (this.stayOutOfHiveCountdown > 0) {
                --this.stayOutOfHiveCountdown;
            }
            if (this.remainingCooldownBeforeLocatingNewHive > 0) {
                --this.remainingCooldownBeforeLocatingNewHive;
            }
            if (this.remainingCooldownBeforeLocatingNewFlower > 0) {
                --this.remainingCooldownBeforeLocatingNewFlower;
            }
            final boolean boolean2 = this.isAngry() && !this.hasStung() && this.getTarget() != null && this.getTarget().distanceToSqr(this) < 4.0;
            this.setRolling(boolean2);
            if (this.tickCount % 20 == 0 && !this.isHiveValid()) {
                this.hivePos = null;
            }
        }
    }
    
    private boolean isHiveValid() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   net/minecraft/world/entity/animal/Bee.hasHive:()Z
        //     4: ifne            9
        //     7: iconst_0       
        //     8: ireturn        
        //     9: aload_0         /* this */
        //    10: getfield        net/minecraft/world/entity/animal/Bee.level:Lnet/minecraft/world/level/Level;
        //    13: aload_0         /* this */
        //    14: getfield        net/minecraft/world/entity/animal/Bee.hivePos:Lnet/minecraft/core/BlockPos;
        //    17: invokevirtual   net/minecraft/world/level/Level.getBlockEntity:(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/entity/BlockEntity;
        //    20: astore_1        /* ccg2 */
        //    21: aload_1         /* ccg2 */
        //    22: ifnull          39
        //    25: aload_1         /* ccg2 */
        //    26: invokevirtual   net/minecraft/world/level/block/entity/BlockEntity.getType:()Lnet/minecraft/world/level/block/entity/BlockEntityType;
        //    29: getstatic       net/minecraft/world/level/block/entity/BlockEntityType.BEEHIVE:Lnet/minecraft/world/level/block/entity/BlockEntityType;
        //    32: if_acmpne       39
        //    35: iconst_1       
        //    36: goto            40
        //    39: iconst_0       
        //    40: ireturn        
        //    StackMapTable: 00 03 09 FA 00 1D 40 01
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
        //     at java.util.Vector.get(Vector.java:751)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:111)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:621)
        //     at com.strobel.assembler.metadata.FieldReference.resolve(FieldReference.java:61)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1036)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:881)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:344)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at cuchaz.enigma.source.procyon.ProcyonDecompiler.getSource(ProcyonDecompiler.java:75)
        //     at cuchaz.enigma.EnigmaProject$JarExport.decompileClass(EnigmaProject.java:266)
        //     at cuchaz.enigma.EnigmaProject$JarExport.lambda$decompileStream$1(EnigmaProject.java:242)
        //     at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:193)
        //     at java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1382)
        //     at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:482)
        //     at java.util.stream.ForEachOps$ForEachTask.compute(ForEachOps.java:291)
        //     at java.util.concurrent.CountedCompleter.exec(CountedCompleter.java:731)
        //     at java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:289)
        //     at java.util.concurrent.ForkJoinPool$WorkQueue.runTask(ForkJoinPool.java:1056)
        //     at java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1692)
        //     at java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:157)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public boolean hasNectar() {
        return this.getFlag(8);
    }
    
    private void setHasNectar(final boolean boolean1) {
        if (boolean1) {
            this.resetTicksWithoutNectarSinceExitingHive();
        }
        this.setFlag(8, boolean1);
    }
    
    public boolean hasStung() {
        return this.getFlag(4);
    }
    
    private void setHasStung(final boolean boolean1) {
        this.setFlag(4, boolean1);
    }
    
    private boolean isRolling() {
        return this.getFlag(2);
    }
    
    private void setRolling(final boolean boolean1) {
        this.setFlag(2, boolean1);
    }
    
    private boolean isTooFarAway(final BlockPos fx) {
        return !this.closerThan(fx, 32);
    }
    
    private void setFlag(final int integer, final boolean boolean2) {
        if (boolean2) {
            this.entityData.<Byte>set(Bee.DATA_FLAGS_ID, (byte)(this.entityData.<Byte>get(Bee.DATA_FLAGS_ID) | integer));
        }
        else {
            this.entityData.<Byte>set(Bee.DATA_FLAGS_ID, (byte)(this.entityData.<Byte>get(Bee.DATA_FLAGS_ID) & ~integer));
        }
    }
    
    private boolean getFlag(final int integer) {
        return (this.entityData.<Byte>get(Bee.DATA_FLAGS_ID) & integer) != 0x0;
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.FLYING_SPEED, 0.6000000238418579).add(Attributes.MOVEMENT_SPEED, 0.30000001192092896).add(Attributes.ATTACK_DAMAGE, 2.0).add(Attributes.FOLLOW_RANGE, 48.0);
    }
    
    protected PathNavigation createNavigation(final Level bru) {
        final FlyingPathNavigation aye3 = new FlyingPathNavigation(this, bru) {
            @Override
            public boolean isStableDestination(final BlockPos fx) {
                return !this.level.getBlockState(fx.below()).isAir();
            }
            
            @Override
            public void tick() {
                if (Bee.this.beePollinateGoal.isPollinating()) {
                    return;
                }
                super.tick();
            }
        };
        aye3.setCanOpenDoors(false);
        aye3.setCanFloat(false);
        aye3.setCanPassDoors(true);
        return aye3;
    }
    
    @Override
    public boolean isFood(final ItemStack bly) {
        return bly.getItem().is(ItemTags.FLOWERS);
    }
    
    private boolean isFlowerValid(final BlockPos fx) {
        return this.level.isLoaded(fx) && this.level.getBlockState(fx).getBlock().is(BlockTags.FLOWERS);
    }
    
    protected void playStepSound(final BlockPos fx, final BlockState cee) {
    }
    
    protected SoundEvent getAmbientSound() {
        return null;
    }
    
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.BEE_HURT;
    }
    
    protected SoundEvent getDeathSound() {
        return SoundEvents.BEE_DEATH;
    }
    
    protected float getSoundVolume() {
        return 0.4f;
    }
    
    @Override
    public Bee getBreedOffspring(final ServerLevel aag, final AgableMob apv) {
        return EntityType.BEE.create(aag);
    }
    
    protected float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        if (this.isBaby()) {
            return apy.height * 0.5f;
        }
        return apy.height * 0.5f;
    }
    
    public boolean causeFallDamage(final float float1, final float float2) {
        return false;
    }
    
    protected void checkFallDamage(final double double1, final boolean boolean2, final BlockState cee, final BlockPos fx) {
    }
    
    protected boolean makeFlySound() {
        return true;
    }
    
    public void dropOffNectar() {
        this.setHasNectar(false);
        this.resetNumCropsGrownSincePollination();
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        if (this.isInvulnerableTo(aph)) {
            return false;
        }
        final Entity apx4 = aph.getEntity();
        if (!this.level.isClientSide) {
            this.beePollinateGoal.stopPollinating();
        }
        return super.hurt(aph, float2);
    }
    
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }
    
    protected void jumpInLiquid(final Tag<Fluid> aej) {
        this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.01, 0.0));
    }
    
    public Vec3 getLeashOffset() {
        return new Vec3(0.0, 0.5f * this.getEyeHeight(), this.getBbWidth() * 0.2f);
    }
    
    private boolean closerThan(final BlockPos fx, final int integer) {
        return fx.closerThan(this.blockPosition(), integer);
    }
    
    static {
        DATA_FLAGS_ID = SynchedEntityData.<Byte>defineId(Bee.class, EntityDataSerializers.BYTE);
        DATA_REMAINING_ANGER_TIME = SynchedEntityData.<Integer>defineId(Bee.class, EntityDataSerializers.INT);
        PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    }
    
    class BeeHurtByOtherGoal extends HurtByTargetGoal {
        BeeHurtByOtherGoal(final Bee azx2) {
            super(azx2, new Class[0]);
        }
        
        @Override
        public boolean canContinueToUse() {
            return Bee.this.isAngry() && super.canContinueToUse();
        }
        
        @Override
        protected void alertOther(final Mob aqk, final LivingEntity aqj) {
            if (aqk instanceof Bee && this.mob.canSee(aqj)) {
                aqk.setTarget(aqj);
            }
        }
    }
    
    static class BeeBecomeAngryTargetGoal extends NearestAttackableTargetGoal<Player> {
        BeeBecomeAngryTargetGoal(final Bee azx) {
            super(azx, Player.class, 10, true, false, (Predicate<LivingEntity>)azx::isAngryAt);
        }
        
        @Override
        public boolean canUse() {
            return this.beeCanTarget() && super.canUse();
        }
        
        @Override
        public boolean canContinueToUse() {
            final boolean boolean2 = this.beeCanTarget();
            if (!boolean2 || this.mob.getTarget() == null) {
                this.targetMob = null;
                return false;
            }
            return super.canContinueToUse();
        }
        
        private boolean beeCanTarget() {
            final Bee azx2 = (Bee)this.mob;
            return azx2.isAngry() && !azx2.hasStung();
        }
    }
    
    abstract class BaseBeeGoal extends Goal {
        private BaseBeeGoal() {
        }
        
        public abstract boolean canBeeUse();
        
        public abstract boolean canBeeContinueToUse();
        
        @Override
        public boolean canUse() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: invokevirtual   net/minecraft/world/entity/animal/Bee$BaseBeeGoal.canBeeUse:()Z
            //     4: ifeq            21
            //     7: aload_0         /* this */
            //     8: getfield        net/minecraft/world/entity/animal/Bee$BaseBeeGoal.this$0:Lnet/minecraft/world/entity/animal/Bee;
            //    11: invokevirtual   net/minecraft/world/entity/animal/Bee.isAngry:()Z
            //    14: ifne            21
            //    17: iconst_1       
            //    18: goto            22
            //    21: iconst_0       
            //    22: ireturn        
            //    StackMapTable: 00 02 FA 00 15 40 01
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 6
            //     at java.util.Vector.get(Vector.java:751)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:111)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:621)
            //     at com.strobel.assembler.metadata.FieldReference.resolve(FieldReference.java:61)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1036)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferBinaryExpression(TypeAnalysis.java:2104)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1531)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1551)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
            //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
            //     at cuchaz.enigma.source.procyon.ProcyonDecompiler.getSource(ProcyonDecompiler.java:75)
            //     at cuchaz.enigma.EnigmaProject$JarExport.decompileClass(EnigmaProject.java:266)
            //     at cuchaz.enigma.EnigmaProject$JarExport.lambda$decompileStream$1(EnigmaProject.java:242)
            //     at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:193)
            //     at java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1382)
            //     at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:482)
            //     at java.util.stream.ForEachOps$ForEachTask.compute(ForEachOps.java:291)
            //     at java.util.concurrent.CountedCompleter.exec(CountedCompleter.java:731)
            //     at java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:289)
            //     at java.util.concurrent.ForkJoinPool$WorkQueue.runTask(ForkJoinPool.java:1056)
            //     at java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1692)
            //     at java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:157)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        @Override
        public boolean canContinueToUse() {
            return this.canBeeContinueToUse() && !Bee.this.isAngry();
        }
    }
    
    class BeeWanderGoal extends Goal {
        BeeWanderGoal() {
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE));
        }
        
        @Override
        public boolean canUse() {
            return Bee.this.navigation.isDone() && Bee.this.random.nextInt(10) == 0;
        }
        
        @Override
        public boolean canContinueToUse() {
            return Bee.this.navigation.isInProgress();
        }
        
        @Override
        public void start() {
            final Vec3 dck2 = this.findPos();
            if (dck2 != null) {
                Bee.this.navigation.moveTo(Bee.this.navigation.createPath(new BlockPos(dck2), 1), 1.0);
            }
        }
        
        @Nullable
        private Vec3 findPos() {
            Vec3 dck4;
            if (Bee.this.isHiveValid() && !Bee.this.closerThan(Bee.this.hivePos, 22)) {
                final Vec3 dck3 = Vec3.atCenterOf(Bee.this.hivePos);
                dck4 = dck3.subtract(Bee.this.position()).normalize();
            }
            else {
                dck4 = Bee.this.getViewVector(0.0f);
            }
            final int integer3 = 8;
            final Vec3 dck5 = RandomPos.getAboveLandPos(Bee.this, 8, 7, dck4, 1.5707964f, 2, 1);
            if (dck5 != null) {
                return dck5;
            }
            return RandomPos.getAirPos(Bee.this, 8, 4, -2, dck4, 1.5707963705062866);
        }
    }
    
    public class BeeGoToHiveGoal extends BaseBeeGoal {
        private int travellingTicks;
        private List<BlockPos> blacklistedTargets;
        @Nullable
        private Path lastPath;
        private int ticksStuck;
        
        BeeGoToHiveGoal() {
            this.travellingTicks = Bee.this.level.random.nextInt(10);
            this.blacklistedTargets = (List<BlockPos>)Lists.newArrayList();
            this.lastPath = null;
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE));
        }
        
        @Override
        public boolean canBeeUse() {
            return Bee.this.hivePos != null && !Bee.this.hasRestriction() && Bee.this.wantsToEnterHive() && !this.hasReachedTarget(Bee.this.hivePos) && Bee.this.level.getBlockState(Bee.this.hivePos).is(BlockTags.BEEHIVES);
        }
        
        @Override
        public boolean canBeeContinueToUse() {
            return this.canBeeUse();
        }
        
        @Override
        public void start() {
            this.travellingTicks = 0;
            this.ticksStuck = 0;
            super.start();
        }
        
        @Override
        public void stop() {
            this.travellingTicks = 0;
            this.ticksStuck = 0;
            Bee.this.navigation.stop();
            Bee.this.navigation.resetMaxVisitedNodesMultiplier();
        }
        
        @Override
        public void tick() {
            if (Bee.this.hivePos == null) {
                return;
            }
            ++this.travellingTicks;
            if (this.travellingTicks > 600) {
                this.dropAndBlacklistHive();
                return;
            }
            if (Bee.this.navigation.isInProgress()) {
                return;
            }
            if (Bee.this.closerThan(Bee.this.hivePos, 16)) {
                final boolean boolean2 = this.pathfindDirectlyTowards(Bee.this.hivePos);
                if (!boolean2) {
                    this.dropAndBlacklistHive();
                }
                else if (this.lastPath != null && Bee.this.navigation.getPath().sameAs(this.lastPath)) {
                    ++this.ticksStuck;
                    if (this.ticksStuck > 60) {
                        this.dropHive();
                        this.ticksStuck = 0;
                    }
                }
                else {
                    this.lastPath = Bee.this.navigation.getPath();
                }
                return;
            }
            if (Bee.this.isTooFarAway(Bee.this.hivePos)) {
                this.dropHive();
                return;
            }
            Bee.this.pathfindRandomlyTowards(Bee.this.hivePos);
        }
        
        private boolean pathfindDirectlyTowards(final BlockPos fx) {
            Bee.this.navigation.setMaxVisitedNodesMultiplier(10.0f);
            Bee.this.navigation.moveTo(fx.getX(), fx.getY(), fx.getZ(), 1.0);
            return Bee.this.navigation.getPath() != null && Bee.this.navigation.getPath().canReach();
        }
        
        private boolean isTargetBlacklisted(final BlockPos fx) {
            return this.blacklistedTargets.contains(fx);
        }
        
        private void blacklistTarget(final BlockPos fx) {
            this.blacklistedTargets.add(fx);
            while (this.blacklistedTargets.size() > 3) {
                this.blacklistedTargets.remove(0);
            }
        }
        
        private void clearBlacklist() {
            this.blacklistedTargets.clear();
        }
        
        private void dropAndBlacklistHive() {
            if (Bee.this.hivePos != null) {
                this.blacklistTarget(Bee.this.hivePos);
            }
            this.dropHive();
        }
        
        private void dropHive() {
            Bee.this.hivePos = null;
            Bee.this.remainingCooldownBeforeLocatingNewHive = 200;
        }
        
        private boolean hasReachedTarget(final BlockPos fx) {
            if (Bee.this.closerThan(fx, 2)) {
                return true;
            }
            final Path cxa3 = Bee.this.navigation.getPath();
            return cxa3 != null && cxa3.getTarget().equals(fx) && cxa3.canReach() && cxa3.isDone();
        }
    }
    
    public class BeeGoToKnownFlowerGoal extends BaseBeeGoal {
        private int travellingTicks;
        
        BeeGoToKnownFlowerGoal() {
            this.travellingTicks = Bee.this.level.random.nextInt(10);
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE));
        }
        
        @Override
        public boolean canBeeUse() {
            return Bee.this.savedFlowerPos != null && !Bee.this.hasRestriction() && this.wantsToGoToKnownFlower() && Bee.this.isFlowerValid(Bee.this.savedFlowerPos) && !Bee.this.closerThan(Bee.this.savedFlowerPos, 2);
        }
        
        @Override
        public boolean canBeeContinueToUse() {
            return this.canBeeUse();
        }
        
        @Override
        public void start() {
            this.travellingTicks = 0;
            super.start();
        }
        
        @Override
        public void stop() {
            this.travellingTicks = 0;
            Bee.this.navigation.stop();
            Bee.this.navigation.resetMaxVisitedNodesMultiplier();
        }
        
        @Override
        public void tick() {
            if (Bee.this.savedFlowerPos == null) {
                return;
            }
            ++this.travellingTicks;
            if (this.travellingTicks > 600) {
                Bee.this.savedFlowerPos = null;
                return;
            }
            if (Bee.this.navigation.isInProgress()) {
                return;
            }
            if (Bee.this.isTooFarAway(Bee.this.savedFlowerPos)) {
                Bee.this.savedFlowerPos = null;
                return;
            }
            Bee.this.pathfindRandomlyTowards(Bee.this.savedFlowerPos);
        }
        
        private boolean wantsToGoToKnownFlower() {
            return Bee.this.ticksWithoutNectarSinceExitingHive > 2400;
        }
    }
    
    class BeeLookControl extends LookControl {
        BeeLookControl(final Mob aqk) {
            super(aqk);
        }
        
        @Override
        public void tick() {
            if (Bee.this.isAngry()) {
                return;
            }
            super.tick();
        }
        
        @Override
        protected boolean resetXRotOnTick() {
            return !Bee.this.beePollinateGoal.isPollinating();
        }
    }
    
    class BeePollinateGoal extends BaseBeeGoal {
        private final Predicate<BlockState> VALID_POLLINATION_BLOCKS;
        private int successfulPollinatingTicks;
        private int lastSoundPlayedTick;
        private boolean pollinating;
        private Vec3 hoverPos;
        private int pollinatingTicks;
        
        BeePollinateGoal() {
            this.VALID_POLLINATION_BLOCKS = (Predicate<BlockState>)(cee -> {
                if (cee.is(BlockTags.TALL_FLOWERS)) {
                    return !cee.is(Blocks.SUNFLOWER) || cee.<DoubleBlockHalf>getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER;
                }
                return cee.is(BlockTags.SMALL_FLOWERS);
            });
            this.successfulPollinatingTicks = 0;
            this.lastSoundPlayedTick = 0;
            this.pollinatingTicks = 0;
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE));
        }
        
        @Override
        public boolean canBeeUse() {
            if (Bee.this.remainingCooldownBeforeLocatingNewFlower > 0) {
                return false;
            }
            if (Bee.this.hasNectar()) {
                return false;
            }
            if (Bee.this.level.isRaining()) {
                return false;
            }
            if (Bee.this.random.nextFloat() < 0.7f) {
                return false;
            }
            final Optional<BlockPos> optional2 = this.findNearbyFlower();
            if (optional2.isPresent()) {
                Bee.this.savedFlowerPos = (BlockPos)optional2.get();
                Bee.this.navigation.moveTo(Bee.this.savedFlowerPos.getX() + 0.5, Bee.this.savedFlowerPos.getY() + 0.5, Bee.this.savedFlowerPos.getZ() + 0.5, 1.2000000476837158);
                return true;
            }
            return false;
        }
        
        @Override
        public boolean canBeeContinueToUse() {
            if (!this.pollinating) {
                return false;
            }
            if (!Bee.this.hasSavedFlowerPos()) {
                return false;
            }
            if (Bee.this.level.isRaining()) {
                return false;
            }
            if (this.hasPollinatedLongEnough()) {
                return Bee.this.random.nextFloat() < 0.2f;
            }
            if (Bee.this.tickCount % 20 == 0 && !Bee.this.isFlowerValid(Bee.this.savedFlowerPos)) {
                Bee.this.savedFlowerPos = null;
                return false;
            }
            return true;
        }
        
        private boolean hasPollinatedLongEnough() {
            return this.successfulPollinatingTicks > 400;
        }
        
        private boolean isPollinating() {
            return this.pollinating;
        }
        
        private void stopPollinating() {
            this.pollinating = false;
        }
        
        @Override
        public void start() {
            this.successfulPollinatingTicks = 0;
            this.pollinatingTicks = 0;
            this.lastSoundPlayedTick = 0;
            this.pollinating = true;
            Bee.this.resetTicksWithoutNectarSinceExitingHive();
        }
        
        @Override
        public void stop() {
            if (this.hasPollinatedLongEnough()) {
                Bee.this.setHasNectar(true);
            }
            this.pollinating = false;
            Bee.this.navigation.stop();
            Bee.this.remainingCooldownBeforeLocatingNewFlower = 200;
        }
        
        @Override
        public void tick() {
            ++this.pollinatingTicks;
            if (this.pollinatingTicks > 600) {
                Bee.this.savedFlowerPos = null;
                return;
            }
            final Vec3 dck2 = Vec3.atBottomCenterOf(Bee.this.savedFlowerPos).add(0.0, 0.6000000238418579, 0.0);
            if (dck2.distanceTo(Bee.this.position()) > 1.0) {
                this.hoverPos = dck2;
                this.setWantedPos();
                return;
            }
            if (this.hoverPos == null) {
                this.hoverPos = dck2;
            }
            final boolean boolean3 = Bee.this.position().distanceTo(this.hoverPos) <= 0.1;
            boolean boolean4 = true;
            if (!boolean3 && this.pollinatingTicks > 600) {
                Bee.this.savedFlowerPos = null;
                return;
            }
            if (boolean3) {
                final boolean boolean5 = Bee.this.random.nextInt(25) == 0;
                if (boolean5) {
                    this.hoverPos = new Vec3(dck2.x() + this.getOffset(), dck2.y(), dck2.z() + this.getOffset());
                    Bee.this.navigation.stop();
                }
                else {
                    boolean4 = false;
                }
                Bee.this.getLookControl().setLookAt(dck2.x(), dck2.y(), dck2.z());
            }
            if (boolean4) {
                this.setWantedPos();
            }
            ++this.successfulPollinatingTicks;
            if (Bee.this.random.nextFloat() < 0.05f && this.successfulPollinatingTicks > this.lastSoundPlayedTick + 60) {
                this.lastSoundPlayedTick = this.successfulPollinatingTicks;
                Bee.this.playSound(SoundEvents.BEE_POLLINATE, 1.0f, 1.0f);
            }
        }
        
        private void setWantedPos() {
            Bee.this.getMoveControl().setWantedPosition(this.hoverPos.x(), this.hoverPos.y(), this.hoverPos.z(), 0.3499999940395355);
        }
        
        private float getOffset() {
            return (Bee.this.random.nextFloat() * 2.0f - 1.0f) * 0.33333334f;
        }
        
        private Optional<BlockPos> findNearbyFlower() {
            return this.findNearestBlock(this.VALID_POLLINATION_BLOCKS, 5.0);
        }
        
        private Optional<BlockPos> findNearestBlock(final Predicate<BlockState> predicate, final double double2) {
            final BlockPos fx5 = Bee.this.blockPosition();
            final BlockPos.MutableBlockPos a6 = new BlockPos.MutableBlockPos();
            for (int integer7 = 0; integer7 <= double2; integer7 = ((integer7 > 0) ? (-integer7) : (1 - integer7))) {
                for (int integer8 = 0; integer8 < double2; ++integer8) {
                    for (int integer9 = 0; integer9 <= integer8; integer9 = ((integer9 > 0) ? (-integer9) : (1 - integer9))) {
                        for (int integer10 = (integer9 < integer8 && integer9 > -integer8) ? integer8 : 0; integer10 <= integer8; integer10 = ((integer10 > 0) ? (-integer10) : (1 - integer10))) {
                            a6.setWithOffset(fx5, integer9, integer7 - 1, integer10);
                            if (fx5.closerThan(a6, double2) && predicate.test(Bee.this.level.getBlockState(a6))) {
                                return (Optional<BlockPos>)Optional.of(a6);
                            }
                        }
                    }
                }
            }
            return (Optional<BlockPos>)Optional.empty();
        }
    }
    
    class BeeLocateHiveGoal extends BaseBeeGoal {
        private BeeLocateHiveGoal() {
        }
        
        @Override
        public boolean canBeeUse() {
            return Bee.this.remainingCooldownBeforeLocatingNewHive == 0 && !Bee.this.hasHive() && Bee.this.wantsToEnterHive();
        }
        
        @Override
        public boolean canBeeContinueToUse() {
            return false;
        }
        
        @Override
        public void start() {
            Bee.this.remainingCooldownBeforeLocatingNewHive = 200;
            final List<BlockPos> list2 = this.findNearbyHivesWithSpace();
            if (list2.isEmpty()) {
                return;
            }
            for (final BlockPos fx4 : list2) {
                if (!Bee.this.goToHiveGoal.isTargetBlacklisted(fx4)) {
                    Bee.this.hivePos = fx4;
                    return;
                }
            }
            Bee.this.goToHiveGoal.clearBlacklist();
            Bee.this.hivePos = (BlockPos)list2.get(0);
        }
        
        private List<BlockPos> findNearbyHivesWithSpace() {
            final BlockPos fx2 = Bee.this.blockPosition();
            final PoiManager azl3 = ((ServerLevel)Bee.this.level).getPoiManager();
            final Stream<PoiRecord> stream4 = azl3.getInRange((Predicate<PoiType>)(azo -> azo == PoiType.BEEHIVE || azo == PoiType.BEE_NEST), fx2, 20, PoiManager.Occupancy.ANY);
            return (List<BlockPos>)stream4.map(PoiRecord::getPos).filter(fx -> azx.doesHiveHaveSpace(fx)).sorted(Comparator.comparingDouble(fx2 -> fx2.distSqr(fx2))).collect(Collectors.toList());
        }
    }
    
    class BeeGrowCropGoal extends BaseBeeGoal {
        private BeeGrowCropGoal() {
        }
        
        @Override
        public boolean canBeeUse() {
            return Bee.this.getCropsGrownSincePollination() < 10 && Bee.this.random.nextFloat() >= 0.3f && Bee.this.hasNectar() && Bee.this.isHiveValid();
        }
        
        @Override
        public boolean canBeeContinueToUse() {
            return this.canBeeUse();
        }
        
        @Override
        public void tick() {
            if (Bee.this.random.nextInt(30) != 0) {
                return;
            }
            for (int integer2 = 1; integer2 <= 2; ++integer2) {
                final BlockPos fx3 = Bee.this.blockPosition().below(integer2);
                final BlockState cee4 = Bee.this.level.getBlockState(fx3);
                final Block bul5 = cee4.getBlock();
                boolean boolean6 = false;
                IntegerProperty cfd7 = null;
                if (bul5.is(BlockTags.BEE_GROWABLES)) {
                    if (bul5 instanceof CropBlock) {
                        final CropBlock bvp8 = (CropBlock)bul5;
                        if (!bvp8.isMaxAge(cee4)) {
                            boolean6 = true;
                            cfd7 = bvp8.getAgeProperty();
                        }
                    }
                    else if (bul5 instanceof StemBlock) {
                        final int integer3 = cee4.<Integer>getValue((Property<Integer>)StemBlock.AGE);
                        if (integer3 < 7) {
                            boolean6 = true;
                            cfd7 = StemBlock.AGE;
                        }
                    }
                    else if (bul5 == Blocks.SWEET_BERRY_BUSH) {
                        final int integer3 = cee4.<Integer>getValue((Property<Integer>)SweetBerryBushBlock.AGE);
                        if (integer3 < 3) {
                            boolean6 = true;
                            cfd7 = SweetBerryBushBlock.AGE;
                        }
                    }
                    if (boolean6) {
                        Bee.this.level.levelEvent(2005, fx3, 0);
                        Bee.this.level.setBlockAndUpdate(fx3, ((StateHolder<O, BlockState>)cee4).<Comparable, Integer>setValue((Property<Comparable>)cfd7, cee4.<Integer>getValue((Property<Integer>)cfd7) + 1));
                        Bee.this.incrementNumCropsGrownSincePollination();
                    }
                }
            }
        }
    }
    
    class BeeAttackGoal extends MeleeAttackGoal {
        BeeAttackGoal(final PathfinderMob aqr, final double double3, final boolean boolean4) {
            super(aqr, double3, boolean4);
        }
        
        @Override
        public boolean canUse() {
            return super.canUse() && Bee.this.isAngry() && !Bee.this.hasStung();
        }
        
        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && Bee.this.isAngry() && !Bee.this.hasStung();
        }
    }
    
    class BeeEnterHiveGoal extends BaseBeeGoal {
        private BeeEnterHiveGoal() {
        }
        
        @Override
        public boolean canBeeUse() {
            if (Bee.this.hasHive() && Bee.this.wantsToEnterHive() && Bee.this.hivePos.closerThan(Bee.this.position(), 2.0)) {
                final BlockEntity ccg2 = Bee.this.level.getBlockEntity(Bee.this.hivePos);
                if (ccg2 instanceof BeehiveBlockEntity) {
                    final BeehiveBlockEntity ccd3 = (BeehiveBlockEntity)ccg2;
                    if (!ccd3.isFull()) {
                        return true;
                    }
                    Bee.this.hivePos = null;
                }
            }
            return false;
        }
        
        @Override
        public boolean canBeeContinueToUse() {
            return false;
        }
        
        @Override
        public void start() {
            final BlockEntity ccg2 = Bee.this.level.getBlockEntity(Bee.this.hivePos);
            if (ccg2 instanceof BeehiveBlockEntity) {
                final BeehiveBlockEntity ccd3 = (BeehiveBlockEntity)ccg2;
                ccd3.addOccupant(Bee.this, Bee.this.hasNectar());
            }
        }
    }
}

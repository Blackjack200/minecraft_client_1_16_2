package net.minecraft.world.entity.animal;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.CarrotBlock;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import javax.annotation.Nullable;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.Util;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.syncher.EntityDataAccessor;

public class Rabbit extends Animal {
    private static final EntityDataAccessor<Integer> DATA_TYPE_ID;
    private static final ResourceLocation KILLER_BUNNY;
    private int jumpTicks;
    private int jumpDuration;
    private boolean wasOnGround;
    private int jumpDelayTicks;
    private int moreCarrotTicks;
    
    public Rabbit(final EntityType<? extends Rabbit> aqb, final Level bru) {
        super(aqb, bru);
        this.jumpControl = new RabbitJumpControl(this);
        this.moveControl = new RabbitMoveControl(this);
        this.setSpeedModifier(0.0);
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RabbitPanicGoal(this, 2.2));
        this.goalSelector.addGoal(2, new BreedGoal(this, 0.8));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0, Ingredient.of(Items.CARROT, Items.GOLDEN_CARROT, Blocks.DANDELION), false));
        this.goalSelector.addGoal(4, new RabbitAvoidEntityGoal<>(this, Player.class, 8.0f, 2.2, 2.2));
        this.goalSelector.addGoal(4, new RabbitAvoidEntityGoal<>(this, Wolf.class, 10.0f, 2.2, 2.2));
        this.goalSelector.addGoal(4, new RabbitAvoidEntityGoal<>(this, Monster.class, 4.0f, 2.2, 2.2));
        this.goalSelector.addGoal(5, new RaidGardenGoal(this));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Player.class, 10.0f));
    }
    
    @Override
    protected float getJumpPower() {
        if (this.horizontalCollision || (this.moveControl.hasWanted() && this.moveControl.getWantedY() > this.getY() + 0.5)) {
            return 0.5f;
        }
        final Path cxa2 = this.navigation.getPath();
        if (cxa2 != null && !cxa2.isDone()) {
            final Vec3 dck3 = cxa2.getNextEntityPos(this);
            if (dck3.y > this.getY() + 0.5) {
                return 0.5f;
            }
        }
        if (this.moveControl.getSpeedModifier() <= 0.6) {
            return 0.2f;
        }
        return 0.3f;
    }
    
    @Override
    protected void jumpFromGround() {
        super.jumpFromGround();
        final double double2 = this.moveControl.getSpeedModifier();
        if (double2 > 0.0) {
            final double double3 = Entity.getHorizontalDistanceSqr(this.getDeltaMovement());
            if (double3 < 0.01) {
                this.moveRelative(0.1f, new Vec3(0.0, 0.0, 1.0));
            }
        }
        if (!this.level.isClientSide) {
            this.level.broadcastEntityEvent(this, (byte)1);
        }
    }
    
    public float getJumpCompletion(final float float1) {
        if (this.jumpDuration == 0) {
            return 0.0f;
        }
        return (this.jumpTicks + float1) / this.jumpDuration;
    }
    
    public void setSpeedModifier(final double double1) {
        this.getNavigation().setSpeedModifier(double1);
        this.moveControl.setWantedPosition(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ(), double1);
    }
    
    @Override
    public void setJumping(final boolean boolean1) {
        super.setJumping(boolean1);
        if (boolean1) {
            this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f) * 0.8f);
        }
    }
    
    public void startJumping() {
        this.setJumping(true);
        this.jumpDuration = 10;
        this.jumpTicks = 0;
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Integer>define(Rabbit.DATA_TYPE_ID, 0);
    }
    
    public void customServerAiStep() {
        if (this.jumpDelayTicks > 0) {
            --this.jumpDelayTicks;
        }
        if (this.moreCarrotTicks > 0) {
            this.moreCarrotTicks -= this.random.nextInt(3);
            if (this.moreCarrotTicks < 0) {
                this.moreCarrotTicks = 0;
            }
        }
        if (this.onGround) {
            if (!this.wasOnGround) {
                this.setJumping(false);
                this.checkLandingDelay();
            }
            if (this.getRabbitType() == 99 && this.jumpDelayTicks == 0) {
                final LivingEntity aqj2 = this.getTarget();
                if (aqj2 != null && this.distanceToSqr(aqj2) < 16.0) {
                    this.facePoint(aqj2.getX(), aqj2.getZ());
                    this.moveControl.setWantedPosition(aqj2.getX(), aqj2.getY(), aqj2.getZ(), this.moveControl.getSpeedModifier());
                    this.startJumping();
                    this.wasOnGround = true;
                }
            }
            final RabbitJumpControl d2 = (RabbitJumpControl)this.jumpControl;
            if (!d2.wantJump()) {
                if (this.moveControl.hasWanted() && this.jumpDelayTicks == 0) {
                    final Path cxa3 = this.navigation.getPath();
                    Vec3 dck4 = new Vec3(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ());
                    if (cxa3 != null && !cxa3.isDone()) {
                        dck4 = cxa3.getNextEntityPos(this);
                    }
                    this.facePoint(dck4.x, dck4.z);
                    this.startJumping();
                }
            }
            else if (!d2.canJump()) {
                this.enableJumpControl();
            }
        }
        this.wasOnGround = this.onGround;
    }
    
    @Override
    public boolean canSpawnSprintParticle() {
        return false;
    }
    
    private void facePoint(final double double1, final double double2) {
        this.yRot = (float)(Mth.atan2(double2 - this.getZ(), double1 - this.getX()) * 57.2957763671875) - 90.0f;
    }
    
    private void enableJumpControl() {
        ((RabbitJumpControl)this.jumpControl).setCanJump(true);
    }
    
    private void disableJumpControl() {
        ((RabbitJumpControl)this.jumpControl).setCanJump(false);
    }
    
    private void setLandingDelay() {
        if (this.moveControl.getSpeedModifier() < 2.2) {
            this.jumpDelayTicks = 10;
        }
        else {
            this.jumpDelayTicks = 1;
        }
    }
    
    private void checkLandingDelay() {
        this.setLandingDelay();
        this.disableJumpControl();
    }
    
    @Override
    public void aiStep() {
        super.aiStep();
        if (this.jumpTicks != this.jumpDuration) {
            ++this.jumpTicks;
        }
        else if (this.jumpDuration != 0) {
            this.jumpTicks = 0;
            this.jumpDuration = 0;
            this.setJumping(false);
        }
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 3.0).add(Attributes.MOVEMENT_SPEED, 0.30000001192092896);
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putInt("RabbitType", this.getRabbitType());
        md.putInt("MoreCarrotTicks", this.moreCarrotTicks);
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.setRabbitType(md.getInt("RabbitType"));
        this.moreCarrotTicks = md.getInt("MoreCarrotTicks");
    }
    
    protected SoundEvent getJumpSound() {
        return SoundEvents.RABBIT_JUMP;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.RABBIT_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.RABBIT_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.RABBIT_DEATH;
    }
    
    @Override
    public boolean doHurtTarget(final Entity apx) {
        if (this.getRabbitType() == 99) {
            this.playSound(SoundEvents.RABBIT_ATTACK, 1.0f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
            return apx.hurt(DamageSource.mobAttack(this), 8.0f);
        }
        return apx.hurt(DamageSource.mobAttack(this), 3.0f);
    }
    
    @Override
    public SoundSource getSoundSource() {
        return (this.getRabbitType() == 99) ? SoundSource.HOSTILE : SoundSource.NEUTRAL;
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        return !this.isInvulnerableTo(aph) && super.hurt(aph, float2);
    }
    
    private boolean isTemptingItem(final Item blu) {
        return blu == Items.CARROT || blu == Items.GOLDEN_CARROT || blu == Blocks.DANDELION.asItem();
    }
    
    @Override
    public Rabbit getBreedOffspring(final ServerLevel aag, final AgableMob apv) {
        final Rabbit ban4 = EntityType.RABBIT.create(aag);
        int integer5 = this.getRandomRabbitType(aag);
        if (this.random.nextInt(20) != 0) {
            if (apv instanceof Rabbit && this.random.nextBoolean()) {
                integer5 = ((Rabbit)apv).getRabbitType();
            }
            else {
                integer5 = this.getRabbitType();
            }
        }
        ban4.setRabbitType(integer5);
        return ban4;
    }
    
    @Override
    public boolean isFood(final ItemStack bly) {
        return this.isTemptingItem(bly.getItem());
    }
    
    public int getRabbitType() {
        return this.entityData.<Integer>get(Rabbit.DATA_TYPE_ID);
    }
    
    public void setRabbitType(final int integer) {
        if (integer == 99) {
            this.getAttribute(Attributes.ARMOR).setBaseValue(8.0);
            this.goalSelector.addGoal(4, new EvilRabbitAttackGoal(this));
            this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]).setAlertOthers(new Class[0]));
            this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
            this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Wolf.class, true));
            if (!this.hasCustomName()) {
                this.setCustomName(new TranslatableComponent(Util.makeDescriptionId("entity", Rabbit.KILLER_BUNNY)));
            }
        }
        this.entityData.<Integer>set(Rabbit.DATA_TYPE_ID, integer);
    }
    
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable SpawnGroupData aqz, @Nullable final CompoundTag md) {
        int integer7 = this.getRandomRabbitType(bsh);
        if (aqz instanceof RabbitGroupData) {
            integer7 = ((RabbitGroupData)aqz).rabbitType;
        }
        else {
            aqz = new RabbitGroupData(integer7);
        }
        this.setRabbitType(integer7);
        return super.finalizeSpawn(bsh, aop, aqm, aqz, md);
    }
    
    private int getRandomRabbitType(final LevelAccessor brv) {
        final Biome bss3 = brv.getBiome(this.blockPosition());
        final int integer4 = this.random.nextInt(100);
        if (bss3.getPrecipitation() == Biome.Precipitation.SNOW) {
            return (integer4 < 80) ? 1 : 3;
        }
        if (bss3.getBiomeCategory() == Biome.BiomeCategory.DESERT) {
            return 4;
        }
        return (integer4 < 50) ? 0 : ((integer4 < 90) ? 5 : 2);
    }
    
    public static boolean checkRabbitSpawnRules(final EntityType<Rabbit> aqb, final LevelAccessor brv, final MobSpawnType aqm, final BlockPos fx, final Random random) {
        final BlockState cee6 = brv.getBlockState(fx.below());
        return (cee6.is(Blocks.GRASS_BLOCK) || cee6.is(Blocks.SNOW) || cee6.is(Blocks.SAND)) && brv.getRawBrightness(fx, 0) > 8;
    }
    
    private boolean wantsMoreFood() {
        return this.moreCarrotTicks == 0;
    }
    
    @Override
    public void handleEntityEvent(final byte byte1) {
        if (byte1 == 1) {
            this.spawnSprintParticle();
            this.jumpDuration = 10;
            this.jumpTicks = 0;
        }
        else {
            super.handleEntityEvent(byte1);
        }
    }
    
    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0, 0.6f * this.getEyeHeight(), this.getBbWidth() * 0.4f);
    }
    
    static {
        DATA_TYPE_ID = SynchedEntityData.<Integer>defineId(Rabbit.class, EntityDataSerializers.INT);
        KILLER_BUNNY = new ResourceLocation("killer_bunny");
    }
    
    public static class RabbitGroupData extends AgableMobGroupData {
        public final int rabbitType;
        
        public RabbitGroupData(final int integer) {
            super(1.0f);
            this.rabbitType = integer;
        }
    }
    
    public class RabbitJumpControl extends JumpControl {
        private final Rabbit rabbit;
        private boolean canJump;
        
        public RabbitJumpControl(final Rabbit ban2) {
            super(ban2);
            this.rabbit = ban2;
        }
        
        public boolean wantJump() {
            return this.jump;
        }
        
        public boolean canJump() {
            return this.canJump;
        }
        
        public void setCanJump(final boolean boolean1) {
            this.canJump = boolean1;
        }
        
        @Override
        public void tick() {
            if (this.jump) {
                this.rabbit.startJumping();
                this.jump = false;
            }
        }
    }
    
    static class RabbitMoveControl extends MoveControl {
        private final Rabbit rabbit;
        private double nextJumpSpeed;
        
        public RabbitMoveControl(final Rabbit ban) {
            super(ban);
            this.rabbit = ban;
        }
        
        @Override
        public void tick() {
            if (this.rabbit.onGround && !this.rabbit.jumping && !((RabbitJumpControl)this.rabbit.jumpControl).wantJump()) {
                this.rabbit.setSpeedModifier(0.0);
            }
            else if (this.hasWanted()) {
                this.rabbit.setSpeedModifier(this.nextJumpSpeed);
            }
            super.tick();
        }
        
        @Override
        public void setWantedPosition(final double double1, final double double2, final double double3, double double4) {
            if (this.rabbit.isInWater()) {
                double4 = 1.5;
            }
            super.setWantedPosition(double1, double2, double3, double4);
            if (double4 > 0.0) {
                this.nextJumpSpeed = double4;
            }
        }
    }
    
    static class RabbitAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
        private final Rabbit rabbit;
        
        public RabbitAvoidEntityGoal(final Rabbit ban, final Class<T> class2, final float float3, final double double4, final double double5) {
            super(ban, class2, float3, double4, double5);
            this.rabbit = ban;
        }
        
        @Override
        public boolean canUse() {
            return this.rabbit.getRabbitType() != 99 && super.canUse();
        }
    }
    
    static class RabbitAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
        private final Rabbit rabbit;
        
        public RabbitAvoidEntityGoal(final Rabbit ban, final Class<T> class2, final float float3, final double double4, final double double5) {
            super(ban, class2, float3, double4, double5);
            this.rabbit = ban;
        }
        
        @Override
        public boolean canUse() {
            return this.rabbit.getRabbitType() != 99 && super.canUse();
        }
    }
    
    static class RaidGardenGoal extends MoveToBlockGoal {
        private final Rabbit rabbit;
        private boolean wantsToRaid;
        private boolean canRaid;
        
        public RaidGardenGoal(final Rabbit ban) {
            super(ban, 0.699999988079071, 16);
            this.rabbit = ban;
        }
        
        @Override
        public boolean canUse() {
            if (this.nextStartTick <= 0) {
                if (!this.rabbit.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                    return false;
                }
                this.canRaid = false;
                this.wantsToRaid = this.rabbit.wantsMoreFood();
                this.wantsToRaid = true;
            }
            return super.canUse();
        }
        
        @Override
        public boolean canContinueToUse() {
            return this.canRaid && super.canContinueToUse();
        }
        
        @Override
        public void tick() {
            super.tick();
            this.rabbit.getLookControl().setLookAt(this.blockPos.getX() + 0.5, this.blockPos.getY() + 1, this.blockPos.getZ() + 0.5, 10.0f, (float)this.rabbit.getMaxHeadXRot());
            if (this.isReachedTarget()) {
                final Level bru2 = this.rabbit.level;
                final BlockPos fx3 = this.blockPos.above();
                final BlockState cee4 = bru2.getBlockState(fx3);
                final Block bul5 = cee4.getBlock();
                if (this.canRaid && bul5 instanceof CarrotBlock) {
                    final Integer integer6 = cee4.<Integer>getValue((Property<Integer>)CarrotBlock.AGE);
                    if (integer6 == 0) {
                        bru2.setBlock(fx3, Blocks.AIR.defaultBlockState(), 2);
                        bru2.destroyBlock(fx3, true, this.rabbit);
                    }
                    else {
                        bru2.setBlock(fx3, ((StateHolder<O, BlockState>)cee4).<Comparable, Integer>setValue((Property<Comparable>)CarrotBlock.AGE, integer6 - 1), 2);
                        bru2.levelEvent(2001, fx3, Block.getId(cee4));
                    }
                    this.rabbit.moreCarrotTicks = 40;
                }
                this.canRaid = false;
                this.nextStartTick = 10;
            }
        }
        
        @Override
        protected boolean isValidTarget(final LevelReader brw, BlockPos fx) {
            Block bul4 = brw.getBlockState(fx).getBlock();
            if (bul4 == Blocks.FARMLAND && this.wantsToRaid && !this.canRaid) {
                fx = fx.above();
                final BlockState cee5 = brw.getBlockState(fx);
                bul4 = cee5.getBlock();
                if (bul4 instanceof CarrotBlock && ((CarrotBlock)bul4).isMaxAge(cee5)) {
                    return this.canRaid = true;
                }
            }
            return false;
        }
    }
    
    static class RabbitPanicGoal extends PanicGoal {
        private final Rabbit rabbit;
        
        public RabbitPanicGoal(final Rabbit ban, final double double2) {
            super(ban, double2);
            this.rabbit = ban;
        }
        
        @Override
        public void tick() {
            super.tick();
            this.rabbit.setSpeedModifier(this.speedModifier);
        }
    }
    
    static class EvilRabbitAttackGoal extends MeleeAttackGoal {
        public EvilRabbitAttackGoal(final Rabbit ban) {
            super(ban, 1.4, true);
        }
        
        @Override
        protected double getAttackReachSqr(final LivingEntity aqj) {
            return 4.0f + aqj.getBbWidth();
        }
    }
}

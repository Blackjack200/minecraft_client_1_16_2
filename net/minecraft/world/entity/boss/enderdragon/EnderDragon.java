package net.minecraft.world.entity.boss.enderdragon;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import org.apache.logging.log4j.LogManager;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.core.Position;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.nbt.CompoundTag;
import com.google.common.collect.Lists;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.material.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
import java.util.function.Predicate;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.pathfinder.BinaryHeap;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhaseManager;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import javax.annotation.Nullable;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.network.syncher.EntityDataAccessor;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.Mob;

public class EnderDragon extends Mob implements Enemy {
    private static final Logger LOGGER;
    public static final EntityDataAccessor<Integer> DATA_PHASE;
    private static final TargetingConditions CRYSTAL_DESTROY_TARGETING;
    public final double[][] positions;
    public int posPointer;
    private final EnderDragonPart[] subEntities;
    public final EnderDragonPart head;
    private final EnderDragonPart neck;
    private final EnderDragonPart body;
    private final EnderDragonPart tail1;
    private final EnderDragonPart tail2;
    private final EnderDragonPart tail3;
    private final EnderDragonPart wing1;
    private final EnderDragonPart wing2;
    public float oFlapTime;
    public float flapTime;
    public boolean inWall;
    public int dragonDeathTime;
    public float yRotA;
    @Nullable
    public EndCrystal nearestCrystal;
    @Nullable
    private final EndDragonFight dragonFight;
    private final EnderDragonPhaseManager phaseManager;
    private int growlTime;
    private int sittingDamageReceived;
    private final Node[] nodes;
    private final int[] nodeAdjacency;
    private final BinaryHeap openSet;
    
    public EnderDragon(final EntityType<? extends EnderDragon> aqb, final Level bru) {
        super(EntityType.ENDER_DRAGON, bru);
        this.positions = new double[64][3];
        this.posPointer = -1;
        this.growlTime = 100;
        this.nodes = new Node[24];
        this.nodeAdjacency = new int[24];
        this.openSet = new BinaryHeap();
        this.head = new EnderDragonPart(this, "head", 1.0f, 1.0f);
        this.neck = new EnderDragonPart(this, "neck", 3.0f, 3.0f);
        this.body = new EnderDragonPart(this, "body", 5.0f, 3.0f);
        this.tail1 = new EnderDragonPart(this, "tail", 2.0f, 2.0f);
        this.tail2 = new EnderDragonPart(this, "tail", 2.0f, 2.0f);
        this.tail3 = new EnderDragonPart(this, "tail", 2.0f, 2.0f);
        this.wing1 = new EnderDragonPart(this, "wing", 4.0f, 2.0f);
        this.wing2 = new EnderDragonPart(this, "wing", 4.0f, 2.0f);
        this.subEntities = new EnderDragonPart[] { this.head, this.neck, this.body, this.tail1, this.tail2, this.tail3, this.wing1, this.wing2 };
        this.setHealth(this.getMaxHealth());
        this.noPhysics = true;
        this.noCulling = true;
        if (bru instanceof ServerLevel) {
            this.dragonFight = ((ServerLevel)bru).dragonFight();
        }
        else {
            this.dragonFight = null;
        }
        this.phaseManager = new EnderDragonPhaseManager(this);
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 200.0);
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().<Integer>define(EnderDragon.DATA_PHASE, EnderDragonPhase.HOVERING.getId());
    }
    
    public double[] getLatencyPos(final int integer, float float2) {
        if (this.isDeadOrDying()) {
            float2 = 0.0f;
        }
        float2 = 1.0f - float2;
        final int integer2 = this.posPointer - integer & 0x3F;
        final int integer3 = this.posPointer - integer - 1 & 0x3F;
        final double[] arr6 = new double[3];
        double double7 = this.positions[integer2][0];
        double double8 = Mth.wrapDegrees(this.positions[integer3][0] - double7);
        arr6[0] = double7 + double8 * float2;
        double7 = this.positions[integer2][1];
        double8 = this.positions[integer3][1] - double7;
        arr6[1] = double7 + double8 * float2;
        arr6[2] = Mth.lerp(float2, this.positions[integer2][2], this.positions[integer3][2]);
        return arr6;
    }
    
    @Override
    public void aiStep() {
        if (this.level.isClientSide) {
            this.setHealth(this.getHealth());
            if (!this.isSilent()) {
                final float float2 = Mth.cos(this.flapTime * 6.2831855f);
                final float float3 = Mth.cos(this.oFlapTime * 6.2831855f);
                if (float3 <= -0.3f && float2 >= -0.3f) {
                    this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENDER_DRAGON_FLAP, this.getSoundSource(), 5.0f, 0.8f + this.random.nextFloat() * 0.3f, false);
                }
                if (!this.phaseManager.getCurrentPhase().isSitting() && --this.growlTime < 0) {
                    this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENDER_DRAGON_GROWL, this.getSoundSource(), 2.5f, 0.8f + this.random.nextFloat() * 0.3f, false);
                    this.growlTime = 200 + this.random.nextInt(200);
                }
            }
        }
        this.oFlapTime = this.flapTime;
        if (this.isDeadOrDying()) {
            final float float2 = (this.random.nextFloat() - 0.5f) * 8.0f;
            final float float3 = (this.random.nextFloat() - 0.5f) * 4.0f;
            final float float4 = (this.random.nextFloat() - 0.5f) * 8.0f;
            this.level.addParticle(ParticleTypes.EXPLOSION, this.getX() + float2, this.getY() + 2.0 + float3, this.getZ() + float4, 0.0, 0.0, 0.0);
            return;
        }
        this.checkCrystals();
        final Vec3 dck2 = this.getDeltaMovement();
        float float3 = 0.2f / (Mth.sqrt(Entity.getHorizontalDistanceSqr(dck2)) * 10.0f + 1.0f);
        float3 *= (float)Math.pow(2.0, dck2.y);
        if (this.phaseManager.getCurrentPhase().isSitting()) {
            this.flapTime += 0.1f;
        }
        else if (this.inWall) {
            this.flapTime += float3 * 0.5f;
        }
        else {
            this.flapTime += float3;
        }
        this.yRot = Mth.wrapDegrees(this.yRot);
        if (this.isNoAi()) {
            this.flapTime = 0.5f;
            return;
        }
        if (this.posPointer < 0) {
            for (int integer4 = 0; integer4 < this.positions.length; ++integer4) {
                this.positions[integer4][0] = this.yRot;
                this.positions[integer4][1] = this.getY();
            }
        }
        if (++this.posPointer == this.positions.length) {
            this.posPointer = 0;
        }
        this.positions[this.posPointer][0] = this.yRot;
        this.positions[this.posPointer][1] = this.getY();
        if (this.level.isClientSide) {
            if (this.lerpSteps > 0) {
                final double double4 = this.getX() + (this.lerpX - this.getX()) / this.lerpSteps;
                final double double5 = this.getY() + (this.lerpY - this.getY()) / this.lerpSteps;
                final double double6 = this.getZ() + (this.lerpZ - this.getZ()) / this.lerpSteps;
                final double double7 = Mth.wrapDegrees(this.lerpYRot - this.yRot);
                this.yRot += (float)(double7 / this.lerpSteps);
                this.xRot += (float)((this.lerpXRot - this.xRot) / this.lerpSteps);
                --this.lerpSteps;
                this.setPos(double4, double5, double6);
                this.setRot(this.yRot, this.xRot);
            }
            this.phaseManager.getCurrentPhase().doClientTick();
        }
        else {
            DragonPhaseInstance bby4 = this.phaseManager.getCurrentPhase();
            bby4.doServerTick();
            if (this.phaseManager.getCurrentPhase() != bby4) {
                bby4 = this.phaseManager.getCurrentPhase();
                bby4.doServerTick();
            }
            final Vec3 dck3 = bby4.getFlyTargetLocation();
            if (dck3 != null) {
                final double double5 = dck3.x - this.getX();
                double double6 = dck3.y - this.getY();
                final double double7 = dck3.z - this.getZ();
                final double double8 = double5 * double5 + double6 * double6 + double7 * double7;
                final float float5 = bby4.getFlySpeed();
                final double double9 = Mth.sqrt(double5 * double5 + double7 * double7);
                if (double9 > 0.0) {
                    double6 = Mth.clamp(double6 / double9, -float5, float5);
                }
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, double6 * 0.01, 0.0));
                this.yRot = Mth.wrapDegrees(this.yRot);
                final double double10 = Mth.clamp(Mth.wrapDegrees(180.0 - Mth.atan2(double5, double7) * 57.2957763671875 - this.yRot), -50.0, 50.0);
                final Vec3 dck4 = dck3.subtract(this.getX(), this.getY(), this.getZ()).normalize();
                final Vec3 dck5 = new Vec3(Mth.sin(this.yRot * 0.017453292f), this.getDeltaMovement().y, -Mth.cos(this.yRot * 0.017453292f)).normalize();
                final float float6 = Math.max(((float)dck5.dot(dck4) + 0.5f) / 1.5f, 0.0f);
                this.yRotA *= 0.8f;
                this.yRotA += (float)(double10 * bby4.getTurnSpeed());
                this.yRot += this.yRotA * 0.1f;
                final float float7 = (float)(2.0 / (double8 + 1.0));
                final float float8 = 0.06f;
                this.moveRelative(0.06f * (float6 * float7 + (1.0f - float7)), new Vec3(0.0, 0.0, -1.0));
                if (this.inWall) {
                    this.move(MoverType.SELF, this.getDeltaMovement().scale(0.800000011920929));
                }
                else {
                    this.move(MoverType.SELF, this.getDeltaMovement());
                }
                final Vec3 dck6 = this.getDeltaMovement().normalize();
                final double double11 = 0.8 + 0.15 * (dck6.dot(dck5) + 1.0) / 2.0;
                this.setDeltaMovement(this.getDeltaMovement().multiply(double11, 0.9100000262260437, double11));
            }
        }
        this.yBodyRot = this.yRot;
        final Vec3[] arr4 = new Vec3[this.subEntities.length];
        for (int integer5 = 0; integer5 < this.subEntities.length; ++integer5) {
            arr4[integer5] = new Vec3(this.subEntities[integer5].getX(), this.subEntities[integer5].getY(), this.subEntities[integer5].getZ());
        }
        final float float9 = (float)(this.getLatencyPos(5, 1.0f)[1] - this.getLatencyPos(10, 1.0f)[1]) * 10.0f * 0.017453292f;
        final float float10 = Mth.cos(float9);
        final float float11 = Mth.sin(float9);
        final float float12 = this.yRot * 0.017453292f;
        final float float13 = Mth.sin(float12);
        final float float14 = Mth.cos(float12);
        this.tickPart(this.body, float13 * 0.5f, 0.0, -float14 * 0.5f);
        this.tickPart(this.wing1, float14 * 4.5f, 2.0, float13 * 4.5f);
        this.tickPart(this.wing2, float14 * -4.5f, 2.0, float13 * -4.5f);
        if (!this.level.isClientSide && this.hurtTime == 0) {
            this.knockBack(this.level.getEntities(this, this.wing1.getBoundingBox().inflate(4.0, 2.0, 4.0).move(0.0, -2.0, 0.0), EntitySelector.NO_CREATIVE_OR_SPECTATOR));
            this.knockBack(this.level.getEntities(this, this.wing2.getBoundingBox().inflate(4.0, 2.0, 4.0).move(0.0, -2.0, 0.0), EntitySelector.NO_CREATIVE_OR_SPECTATOR));
            this.hurt(this.level.getEntities(this, this.head.getBoundingBox().inflate(1.0), EntitySelector.NO_CREATIVE_OR_SPECTATOR));
            this.hurt(this.level.getEntities(this, this.neck.getBoundingBox().inflate(1.0), EntitySelector.NO_CREATIVE_OR_SPECTATOR));
        }
        final float float15 = Mth.sin(this.yRot * 0.017453292f - this.yRotA * 0.01f);
        final float float16 = Mth.cos(this.yRot * 0.017453292f - this.yRotA * 0.01f);
        final float float17 = this.getHeadYOffset();
        this.tickPart(this.head, float15 * 6.5f * float10, float17 + float11 * 6.5f, -float16 * 6.5f * float10);
        this.tickPart(this.neck, float15 * 5.5f * float10, float17 + float11 * 5.5f, -float16 * 5.5f * float10);
        final double[] arr5 = this.getLatencyPos(5, 1.0f);
        for (int integer6 = 0; integer6 < 3; ++integer6) {
            EnderDragonPart bbm16 = null;
            if (integer6 == 0) {
                bbm16 = this.tail1;
            }
            if (integer6 == 1) {
                bbm16 = this.tail2;
            }
            if (integer6 == 2) {
                bbm16 = this.tail3;
            }
            final double[] arr6 = this.getLatencyPos(12 + integer6 * 2, 1.0f);
            final float float18 = this.yRot * 0.017453292f + this.rotWrap(arr6[0] - arr5[0]) * 0.017453292f;
            final float float19 = Mth.sin(float18);
            final float float20 = Mth.cos(float18);
            final float float6 = 1.5f;
            final float float7 = (integer6 + 1) * 2.0f;
            this.tickPart(bbm16, -(float13 * 1.5f + float19 * float7) * float10, arr6[1] - arr5[1] - (float7 + 1.5f) * float11 + 1.5, (float14 * 1.5f + float20 * float7) * float10);
        }
        if (!this.level.isClientSide) {
            this.inWall = (this.checkWalls(this.head.getBoundingBox()) | this.checkWalls(this.neck.getBoundingBox()) | this.checkWalls(this.body.getBoundingBox()));
            if (this.dragonFight != null) {
                this.dragonFight.updateDragon(this);
            }
        }
        for (int integer6 = 0; integer6 < this.subEntities.length; ++integer6) {
            this.subEntities[integer6].xo = arr4[integer6].x;
            this.subEntities[integer6].yo = arr4[integer6].y;
            this.subEntities[integer6].zo = arr4[integer6].z;
            this.subEntities[integer6].xOld = arr4[integer6].x;
            this.subEntities[integer6].yOld = arr4[integer6].y;
            this.subEntities[integer6].zOld = arr4[integer6].z;
        }
    }
    
    private void tickPart(final EnderDragonPart bbm, final double double2, final double double3, final double double4) {
        bbm.setPos(this.getX() + double2, this.getY() + double3, this.getZ() + double4);
    }
    
    private float getHeadYOffset() {
        if (this.phaseManager.getCurrentPhase().isSitting()) {
            return -1.0f;
        }
        final double[] arr2 = this.getLatencyPos(5, 1.0f);
        final double[] arr3 = this.getLatencyPos(0, 1.0f);
        return (float)(arr2[1] - arr3[1]);
    }
    
    private void checkCrystals() {
        if (this.nearestCrystal != null) {
            if (this.nearestCrystal.removed) {
                this.nearestCrystal = null;
            }
            else if (this.tickCount % 10 == 0 && this.getHealth() < this.getMaxHealth()) {
                this.setHealth(this.getHealth() + 1.0f);
            }
        }
        if (this.random.nextInt(10) == 0) {
            final List<EndCrystal> list2 = this.level.<EndCrystal>getEntitiesOfClass((java.lang.Class<? extends EndCrystal>)EndCrystal.class, this.getBoundingBox().inflate(32.0));
            EndCrystal bbn3 = null;
            double double4 = Double.MAX_VALUE;
            for (final EndCrystal bbn4 : list2) {
                final double double5 = bbn4.distanceToSqr(this);
                if (double5 < double4) {
                    double4 = double5;
                    bbn3 = bbn4;
                }
            }
            this.nearestCrystal = bbn3;
        }
    }
    
    private void knockBack(final List<Entity> list) {
        final double double3 = (this.body.getBoundingBox().minX + this.body.getBoundingBox().maxX) / 2.0;
        final double double4 = (this.body.getBoundingBox().minZ + this.body.getBoundingBox().maxZ) / 2.0;
        for (final Entity apx8 : list) {
            if (apx8 instanceof LivingEntity) {
                final double double5 = apx8.getX() - double3;
                final double double6 = apx8.getZ() - double4;
                final double double7 = double5 * double5 + double6 * double6;
                apx8.push(double5 / double7 * 4.0, 0.20000000298023224, double6 / double7 * 4.0);
                if (this.phaseManager.getCurrentPhase().isSitting() || ((LivingEntity)apx8).getLastHurtByMobTimestamp() >= apx8.tickCount - 2) {
                    continue;
                }
                apx8.hurt(DamageSource.mobAttack(this), 5.0f);
                this.doEnchantDamageEffects(this, apx8);
            }
        }
    }
    
    private void hurt(final List<Entity> list) {
        for (final Entity apx4 : list) {
            if (apx4 instanceof LivingEntity) {
                apx4.hurt(DamageSource.mobAttack(this), 10.0f);
                this.doEnchantDamageEffects(this, apx4);
            }
        }
    }
    
    private float rotWrap(final double double1) {
        return (float)Mth.wrapDegrees(double1);
    }
    
    private boolean checkWalls(final AABB dcf) {
        final int integer3 = Mth.floor(dcf.minX);
        final int integer4 = Mth.floor(dcf.minY);
        final int integer5 = Mth.floor(dcf.minZ);
        final int integer6 = Mth.floor(dcf.maxX);
        final int integer7 = Mth.floor(dcf.maxY);
        final int integer8 = Mth.floor(dcf.maxZ);
        boolean boolean9 = false;
        boolean boolean10 = false;
        for (int integer9 = integer3; integer9 <= integer6; ++integer9) {
            for (int integer10 = integer4; integer10 <= integer7; ++integer10) {
                for (int integer11 = integer5; integer11 <= integer8; ++integer11) {
                    final BlockPos fx14 = new BlockPos(integer9, integer10, integer11);
                    final BlockState cee15 = this.level.getBlockState(fx14);
                    final Block bul16 = cee15.getBlock();
                    if (!cee15.isAir()) {
                        if (cee15.getMaterial() != Material.FIRE) {
                            if (!this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) || BlockTags.DRAGON_IMMUNE.contains(bul16)) {
                                boolean9 = true;
                            }
                            else {
                                boolean10 = (this.level.removeBlock(fx14, false) || boolean10);
                            }
                        }
                    }
                }
            }
        }
        if (boolean10) {
            final BlockPos fx15 = new BlockPos(integer3 + this.random.nextInt(integer6 - integer3 + 1), integer4 + this.random.nextInt(integer7 - integer4 + 1), integer5 + this.random.nextInt(integer8 - integer5 + 1));
            this.level.levelEvent(2008, fx15, 0);
        }
        return boolean9;
    }
    
    public boolean hurt(final EnderDragonPart bbm, final DamageSource aph, float float3) {
        if (this.phaseManager.getCurrentPhase().getPhase() == EnderDragonPhase.DYING) {
            return false;
        }
        float3 = this.phaseManager.getCurrentPhase().onHurt(aph, float3);
        if (bbm != this.head) {
            float3 = float3 / 4.0f + Math.min(float3, 1.0f);
        }
        if (float3 < 0.01f) {
            return false;
        }
        if (aph.getEntity() instanceof Player || aph.isExplosion()) {
            final float float4 = this.getHealth();
            this.reallyHurt(aph, float3);
            if (this.isDeadOrDying() && !this.phaseManager.getCurrentPhase().isSitting()) {
                this.setHealth(1.0f);
                this.phaseManager.setPhase(EnderDragonPhase.DYING);
            }
            if (this.phaseManager.getCurrentPhase().isSitting()) {
                this.sittingDamageReceived += (int)(float4 - this.getHealth());
                if (this.sittingDamageReceived > 0.25f * this.getMaxHealth()) {
                    this.sittingDamageReceived = 0;
                    this.phaseManager.setPhase(EnderDragonPhase.TAKEOFF);
                }
            }
        }
        return true;
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        if (aph instanceof EntityDamageSource && ((EntityDamageSource)aph).isThorns()) {
            this.hurt(this.body, aph, float2);
        }
        return false;
    }
    
    protected boolean reallyHurt(final DamageSource aph, final float float2) {
        return super.hurt(aph, float2);
    }
    
    @Override
    public void kill() {
        this.remove();
        if (this.dragonFight != null) {
            this.dragonFight.updateDragon(this);
            this.dragonFight.setDragonKilled(this);
        }
    }
    
    @Override
    protected void tickDeath() {
        if (this.dragonFight != null) {
            this.dragonFight.updateDragon(this);
        }
        ++this.dragonDeathTime;
        if (this.dragonDeathTime >= 180 && this.dragonDeathTime <= 200) {
            final float float2 = (this.random.nextFloat() - 0.5f) * 8.0f;
            final float float3 = (this.random.nextFloat() - 0.5f) * 4.0f;
            final float float4 = (this.random.nextFloat() - 0.5f) * 8.0f;
            this.level.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX() + float2, this.getY() + 2.0 + float3, this.getZ() + float4, 0.0, 0.0, 0.0);
        }
        final boolean boolean2 = this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT);
        int integer3 = 500;
        if (this.dragonFight != null && !this.dragonFight.hasPreviouslyKilledDragon()) {
            integer3 = 12000;
        }
        if (!this.level.isClientSide) {
            if (this.dragonDeathTime > 150 && this.dragonDeathTime % 5 == 0 && boolean2) {
                this.dropExperience(Mth.floor(integer3 * 0.08f));
            }
            if (this.dragonDeathTime == 1 && !this.isSilent()) {
                this.level.globalLevelEvent(1028, this.blockPosition(), 0);
            }
        }
        this.move(MoverType.SELF, new Vec3(0.0, 0.10000000149011612, 0.0));
        this.yRot += 20.0f;
        this.yBodyRot = this.yRot;
        if (this.dragonDeathTime == 200 && !this.level.isClientSide) {
            if (boolean2) {
                this.dropExperience(Mth.floor(integer3 * 0.2f));
            }
            if (this.dragonFight != null) {
                this.dragonFight.setDragonKilled(this);
            }
            this.remove();
        }
    }
    
    private void dropExperience(int integer) {
        while (integer > 0) {
            final int integer2 = ExperienceOrb.getExperienceValue(integer);
            integer -= integer2;
            this.level.addFreshEntity(new ExperienceOrb(this.level, this.getX(), this.getY(), this.getZ(), integer2));
        }
    }
    
    public int findClosestNode() {
        if (this.nodes[0] == null) {
            for (int integer2 = 0; integer2 < 24; ++integer2) {
                int integer3 = 5;
                int integer4;
                int integer5;
                int integer6;
                if ((integer4 = integer2) < 12) {
                    integer5 = Mth.floor(60.0f * Mth.cos(2.0f * (-3.1415927f + 0.2617994f * integer4)));
                    integer6 = Mth.floor(60.0f * Mth.sin(2.0f * (-3.1415927f + 0.2617994f * integer4)));
                }
                else if (integer2 < 20) {
                    integer4 -= 12;
                    integer5 = Mth.floor(40.0f * Mth.cos(2.0f * (-3.1415927f + 0.3926991f * integer4)));
                    integer6 = Mth.floor(40.0f * Mth.sin(2.0f * (-3.1415927f + 0.3926991f * integer4)));
                    integer3 += 10;
                }
                else {
                    integer4 -= 20;
                    integer5 = Mth.floor(20.0f * Mth.cos(2.0f * (-3.1415927f + 0.7853982f * integer4)));
                    integer6 = Mth.floor(20.0f * Mth.sin(2.0f * (-3.1415927f + 0.7853982f * integer4)));
                }
                final int integer7 = Math.max(this.level.getSeaLevel() + 10, this.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new BlockPos(integer5, 0, integer6)).getY() + integer3);
                this.nodes[integer2] = new Node(integer5, integer7, integer6);
            }
            this.nodeAdjacency[0] = 6146;
            this.nodeAdjacency[1] = 8197;
            this.nodeAdjacency[2] = 8202;
            this.nodeAdjacency[3] = 16404;
            this.nodeAdjacency[4] = 32808;
            this.nodeAdjacency[5] = 32848;
            this.nodeAdjacency[6] = 65696;
            this.nodeAdjacency[7] = 131392;
            this.nodeAdjacency[8] = 131712;
            this.nodeAdjacency[9] = 263424;
            this.nodeAdjacency[10] = 526848;
            this.nodeAdjacency[11] = 525313;
            this.nodeAdjacency[12] = 1581057;
            this.nodeAdjacency[13] = 3166214;
            this.nodeAdjacency[14] = 2138120;
            this.nodeAdjacency[15] = 6373424;
            this.nodeAdjacency[16] = 4358208;
            this.nodeAdjacency[17] = 12910976;
            this.nodeAdjacency[18] = 9044480;
            this.nodeAdjacency[19] = 9706496;
            this.nodeAdjacency[20] = 15216640;
            this.nodeAdjacency[21] = 13688832;
            this.nodeAdjacency[22] = 11763712;
            this.nodeAdjacency[23] = 8257536;
        }
        return this.findClosestNode(this.getX(), this.getY(), this.getZ());
    }
    
    public int findClosestNode(final double double1, final double double2, final double double3) {
        float float8 = 10000.0f;
        int integer9 = 0;
        final Node cwy10 = new Node(Mth.floor(double1), Mth.floor(double2), Mth.floor(double3));
        int integer10 = 0;
        if (this.dragonFight == null || this.dragonFight.getCrystalsAlive() == 0) {
            integer10 = 12;
        }
        for (int integer11 = integer10; integer11 < 24; ++integer11) {
            if (this.nodes[integer11] != null) {
                final float float9 = this.nodes[integer11].distanceToSqr(cwy10);
                if (float9 < float8) {
                    float8 = float9;
                    integer9 = integer11;
                }
            }
        }
        return integer9;
    }
    
    @Nullable
    public Path findPath(final int integer1, final int integer2, @Nullable final Node cwy) {
        for (int integer3 = 0; integer3 < 24; ++integer3) {
            final Node cwy2 = this.nodes[integer3];
            cwy2.closed = false;
            cwy2.f = 0.0f;
            cwy2.g = 0.0f;
            cwy2.h = 0.0f;
            cwy2.cameFrom = null;
            cwy2.heapIdx = -1;
        }
        final Node cwy3 = this.nodes[integer1];
        Node cwy2 = this.nodes[integer2];
        cwy3.g = 0.0f;
        cwy3.h = cwy3.distanceTo(cwy2);
        cwy3.f = cwy3.h;
        this.openSet.clear();
        this.openSet.insert(cwy3);
        Node cwy4 = cwy3;
        int integer4 = 0;
        if (this.dragonFight == null || this.dragonFight.getCrystalsAlive() == 0) {
            integer4 = 12;
        }
        while (!this.openSet.isEmpty()) {
            final Node cwy5 = this.openSet.pop();
            if (cwy5.equals(cwy2)) {
                if (cwy != null) {
                    cwy.cameFrom = cwy2;
                    cwy2 = cwy;
                }
                return this.reconstructPath(cwy3, cwy2);
            }
            if (cwy5.distanceTo(cwy2) < cwy4.distanceTo(cwy2)) {
                cwy4 = cwy5;
            }
            cwy5.closed = true;
            int integer5 = 0;
            for (int integer6 = 0; integer6 < 24; ++integer6) {
                if (this.nodes[integer6] == cwy5) {
                    integer5 = integer6;
                    break;
                }
            }
            for (int integer6 = integer4; integer6 < 24; ++integer6) {
                if ((this.nodeAdjacency[integer5] & 1 << integer6) > 0) {
                    final Node cwy6 = this.nodes[integer6];
                    if (!cwy6.closed) {
                        final float float13 = cwy5.g + cwy5.distanceTo(cwy6);
                        if (!cwy6.inOpenSet() || float13 < cwy6.g) {
                            cwy6.cameFrom = cwy5;
                            cwy6.g = float13;
                            cwy6.h = cwy6.distanceTo(cwy2);
                            if (cwy6.inOpenSet()) {
                                this.openSet.changeCost(cwy6, cwy6.g + cwy6.h);
                            }
                            else {
                                cwy6.f = cwy6.g + cwy6.h;
                                this.openSet.insert(cwy6);
                            }
                        }
                    }
                }
            }
        }
        if (cwy4 == cwy3) {
            return null;
        }
        EnderDragon.LOGGER.debug("Failed to find path from {} to {}", integer1, integer2);
        if (cwy != null) {
            cwy.cameFrom = cwy4;
            cwy4 = cwy;
        }
        return this.reconstructPath(cwy3, cwy4);
    }
    
    private Path reconstructPath(final Node cwy1, final Node cwy2) {
        final List<Node> list4 = (List<Node>)Lists.newArrayList();
        Node cwy3 = cwy2;
        list4.add(0, cwy3);
        while (cwy3.cameFrom != null) {
            cwy3 = cwy3.cameFrom;
            list4.add(0, cwy3);
        }
        return new Path(list4, new BlockPos(cwy2.x, cwy2.y, cwy2.z), true);
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putInt("DragonPhase", this.phaseManager.getCurrentPhase().getPhase().getId());
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        if (md.contains("DragonPhase")) {
            this.phaseManager.setPhase(EnderDragonPhase.getById(md.getInt("DragonPhase")));
        }
    }
    
    @Override
    public void checkDespawn() {
    }
    
    public EnderDragonPart[] getSubEntities() {
        return this.subEntities;
    }
    
    @Override
    public boolean isPickable() {
        return false;
    }
    
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENDER_DRAGON_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.ENDER_DRAGON_HURT;
    }
    
    @Override
    protected float getSoundVolume() {
        return 5.0f;
    }
    
    public float getHeadPartYOffset(final int integer, final double[] arr2, final double[] arr3) {
        final DragonPhaseInstance bby5 = this.phaseManager.getCurrentPhase();
        final EnderDragonPhase<? extends DragonPhaseInstance> bce6 = bby5.getPhase();
        double double7;
        if (bce6 == EnderDragonPhase.LANDING || bce6 == EnderDragonPhase.TAKEOFF) {
            final BlockPos fx9 = this.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.END_PODIUM_LOCATION);
            final float float10 = Math.max(Mth.sqrt(fx9.distSqr(this.position(), true)) / 4.0f, 1.0f);
            double7 = integer / float10;
        }
        else if (bby5.isSitting()) {
            double7 = integer;
        }
        else if (integer == 6) {
            double7 = 0.0;
        }
        else {
            double7 = arr3[1] - arr2[1];
        }
        return (float)double7;
    }
    
    public Vec3 getHeadLookVector(final float float1) {
        final DragonPhaseInstance bby3 = this.phaseManager.getCurrentPhase();
        final EnderDragonPhase<? extends DragonPhaseInstance> bce4 = bby3.getPhase();
        Vec3 dck5;
        if (bce4 == EnderDragonPhase.LANDING || bce4 == EnderDragonPhase.TAKEOFF) {
            final BlockPos fx6 = this.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.END_PODIUM_LOCATION);
            final float float2 = Math.max(Mth.sqrt(fx6.distSqr(this.position(), true)) / 4.0f, 1.0f);
            final float float3 = 6.0f / float2;
            final float float4 = this.xRot;
            final float float5 = 1.5f;
            this.xRot = -float3 * 1.5f * 5.0f;
            dck5 = this.getViewVector(float1);
            this.xRot = float4;
        }
        else if (bby3.isSitting()) {
            final float float6 = this.xRot;
            final float float2 = 1.5f;
            this.xRot = -45.0f;
            dck5 = this.getViewVector(float1);
            this.xRot = float6;
        }
        else {
            dck5 = this.getViewVector(float1);
        }
        return dck5;
    }
    
    public void onCrystalDestroyed(final EndCrystal bbn, final BlockPos fx, final DamageSource aph) {
        Player bft5;
        if (aph.getEntity() instanceof Player) {
            bft5 = (Player)aph.getEntity();
        }
        else {
            bft5 = this.level.getNearestPlayer(EnderDragon.CRYSTAL_DESTROY_TARGETING, fx.getX(), fx.getY(), fx.getZ());
        }
        if (bbn == this.nearestCrystal) {
            this.hurt(this.head, DamageSource.explosion(bft5), 10.0f);
        }
        this.phaseManager.getCurrentPhase().onCrystalDestroyed(bbn, fx, aph, bft5);
    }
    
    @Override
    public void onSyncedDataUpdated(final EntityDataAccessor<?> us) {
        if (EnderDragon.DATA_PHASE.equals(us) && this.level.isClientSide) {
            this.phaseManager.setPhase(EnderDragonPhase.getById(this.getEntityData().<Integer>get(EnderDragon.DATA_PHASE)));
        }
        super.onSyncedDataUpdated(us);
    }
    
    public EnderDragonPhaseManager getPhaseManager() {
        return this.phaseManager;
    }
    
    @Nullable
    public EndDragonFight getDragonFight() {
        return this.dragonFight;
    }
    
    @Override
    public boolean addEffect(final MobEffectInstance apr) {
        return false;
    }
    
    protected boolean canRide(final Entity apx) {
        return false;
    }
    
    public boolean canChangeDimensions() {
        return false;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        DATA_PHASE = SynchedEntityData.<Integer>defineId(EnderDragon.class, EntityDataSerializers.INT);
        CRYSTAL_DESTROY_TARGETING = new TargetingConditions().range(64.0);
    }
}

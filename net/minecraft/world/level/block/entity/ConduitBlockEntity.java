package net.minecraft.world.level.block.entity;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.sounds.SoundEvent;
import java.util.Random;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundSource;
import java.util.function.Predicate;
import java.util.Iterator;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import com.google.common.collect.Lists;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.BlockPos;
import java.util.List;
import net.minecraft.world.level.block.Block;

public class ConduitBlockEntity extends BlockEntity implements TickableBlockEntity {
    private static final Block[] VALID_BLOCKS;
    public int tickCount;
    private float activeRotation;
    private boolean isActive;
    private boolean isHunting;
    private final List<BlockPos> effectBlocks;
    @Nullable
    private LivingEntity destroyTarget;
    @Nullable
    private UUID destroyTargetUUID;
    private long nextAmbientSoundActivation;
    
    public ConduitBlockEntity() {
        this(BlockEntityType.CONDUIT);
    }
    
    public ConduitBlockEntity(final BlockEntityType<?> cch) {
        super(cch);
        this.effectBlocks = (List<BlockPos>)Lists.newArrayList();
    }
    
    @Override
    public void load(final BlockState cee, final CompoundTag md) {
        super.load(cee, md);
        if (md.hasUUID("Target")) {
            this.destroyTargetUUID = md.getUUID("Target");
        }
        else {
            this.destroyTargetUUID = null;
        }
    }
    
    @Override
    public CompoundTag save(final CompoundTag md) {
        super.save(md);
        if (this.destroyTarget != null) {
            md.putUUID("Target", this.destroyTarget.getUUID());
        }
        return md;
    }
    
    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 5, this.getUpdateTag());
    }
    
    @Override
    public CompoundTag getUpdateTag() {
        return this.save(new CompoundTag());
    }
    
    @Override
    public void tick() {
        ++this.tickCount;
        final long long2 = this.level.getGameTime();
        if (long2 % 40L == 0L) {
            this.setActive(this.updateShape());
            if (!this.level.isClientSide && this.isActive()) {
                this.applyEffects();
                this.updateDestroyTarget();
            }
        }
        if (long2 % 80L == 0L && this.isActive()) {
            this.playSound(SoundEvents.CONDUIT_AMBIENT);
        }
        if (long2 > this.nextAmbientSoundActivation && this.isActive()) {
            this.nextAmbientSoundActivation = long2 + 60L + this.level.getRandom().nextInt(40);
            this.playSound(SoundEvents.CONDUIT_AMBIENT_SHORT);
        }
        if (this.level.isClientSide) {
            this.updateClientTarget();
            this.animationTick();
            if (this.isActive()) {
                ++this.activeRotation;
            }
        }
    }
    
    private boolean updateShape() {
        this.effectBlocks.clear();
        for (int integer2 = -1; integer2 <= 1; ++integer2) {
            for (int integer3 = -1; integer3 <= 1; ++integer3) {
                for (int integer4 = -1; integer4 <= 1; ++integer4) {
                    final BlockPos fx5 = this.worldPosition.offset(integer2, integer3, integer4);
                    if (!this.level.isWaterAt(fx5)) {
                        return false;
                    }
                }
            }
        }
        for (int integer2 = -2; integer2 <= 2; ++integer2) {
            for (int integer3 = -2; integer3 <= 2; ++integer3) {
                for (int integer4 = -2; integer4 <= 2; ++integer4) {
                    final int integer5 = Math.abs(integer2);
                    final int integer6 = Math.abs(integer3);
                    final int integer7 = Math.abs(integer4);
                    if (integer5 > 1 || integer6 > 1 || integer7 > 1) {
                        if ((integer2 == 0 && (integer6 == 2 || integer7 == 2)) || (integer3 == 0 && (integer5 == 2 || integer7 == 2)) || (integer4 == 0 && (integer5 == 2 || integer6 == 2))) {
                            final BlockPos fx6 = this.worldPosition.offset(integer2, integer3, integer4);
                            final BlockState cee9 = this.level.getBlockState(fx6);
                            for (final Block bul13 : ConduitBlockEntity.VALID_BLOCKS) {
                                if (cee9.is(bul13)) {
                                    this.effectBlocks.add(fx6);
                                }
                            }
                        }
                    }
                }
            }
        }
        this.setHunting(this.effectBlocks.size() >= 42);
        return this.effectBlocks.size() >= 16;
    }
    
    private void applyEffects() {
        final int integer2 = this.effectBlocks.size();
        final int integer3 = integer2 / 7 * 16;
        final int integer4 = this.worldPosition.getX();
        final int integer5 = this.worldPosition.getY();
        final int integer6 = this.worldPosition.getZ();
        final AABB dcf7 = new AABB(integer4, integer5, integer6, integer4 + 1, integer5 + 1, integer6 + 1).inflate(integer3).expandTowards(0.0, this.level.getMaxBuildHeight(), 0.0);
        final List<Player> list8 = this.level.<Player>getEntitiesOfClass((java.lang.Class<? extends Player>)Player.class, dcf7);
        if (list8.isEmpty()) {
            return;
        }
        for (final Player bft10 : list8) {
            if (this.worldPosition.closerThan(bft10.blockPosition(), integer3) && bft10.isInWaterOrRain()) {
                bft10.addEffect(new MobEffectInstance(MobEffects.CONDUIT_POWER, 260, 0, true, true));
            }
        }
    }
    
    private void updateDestroyTarget() {
        final LivingEntity aqj2 = this.destroyTarget;
        final int integer3 = this.effectBlocks.size();
        if (integer3 < 42) {
            this.destroyTarget = null;
        }
        else if (this.destroyTarget == null && this.destroyTargetUUID != null) {
            this.destroyTarget = this.findDestroyTarget();
            this.destroyTargetUUID = null;
        }
        else if (this.destroyTarget == null) {
            final List<LivingEntity> list4 = this.level.<LivingEntity>getEntitiesOfClass((java.lang.Class<? extends LivingEntity>)LivingEntity.class, this.getDestroyRangeAABB(), (java.util.function.Predicate<? super LivingEntity>)(aqj -> aqj instanceof Enemy && aqj.isInWaterOrRain()));
            if (!list4.isEmpty()) {
                this.destroyTarget = (LivingEntity)list4.get(this.level.random.nextInt(list4.size()));
            }
        }
        else if (!this.destroyTarget.isAlive() || !this.worldPosition.closerThan(this.destroyTarget.blockPosition(), 8.0)) {
            this.destroyTarget = null;
        }
        if (this.destroyTarget != null) {
            this.level.playSound(null, this.destroyTarget.getX(), this.destroyTarget.getY(), this.destroyTarget.getZ(), SoundEvents.CONDUIT_ATTACK_TARGET, SoundSource.BLOCKS, 1.0f, 1.0f);
            this.destroyTarget.hurt(DamageSource.MAGIC, 4.0f);
        }
        if (aqj2 != this.destroyTarget) {
            final BlockState cee4 = this.getBlockState();
            this.level.sendBlockUpdated(this.worldPosition, cee4, cee4, 2);
        }
    }
    
    private void updateClientTarget() {
        if (this.destroyTargetUUID == null) {
            this.destroyTarget = null;
        }
        else if (this.destroyTarget == null || !this.destroyTarget.getUUID().equals(this.destroyTargetUUID)) {
            this.destroyTarget = this.findDestroyTarget();
            if (this.destroyTarget == null) {
                this.destroyTargetUUID = null;
            }
        }
    }
    
    private AABB getDestroyRangeAABB() {
        final int integer2 = this.worldPosition.getX();
        final int integer3 = this.worldPosition.getY();
        final int integer4 = this.worldPosition.getZ();
        return new AABB(integer2, integer3, integer4, integer2 + 1, integer3 + 1, integer4 + 1).inflate(8.0);
    }
    
    @Nullable
    private LivingEntity findDestroyTarget() {
        final List<LivingEntity> list2 = this.level.<LivingEntity>getEntitiesOfClass((java.lang.Class<? extends LivingEntity>)LivingEntity.class, this.getDestroyRangeAABB(), (java.util.function.Predicate<? super LivingEntity>)(aqj -> aqj.getUUID().equals(this.destroyTargetUUID)));
        if (list2.size() == 1) {
            return (LivingEntity)list2.get(0);
        }
        return null;
    }
    
    private void animationTick() {
        final Random random2 = this.level.random;
        double double3 = Mth.sin((this.tickCount + 35) * 0.1f) / 2.0f + 0.5f;
        double3 = (double3 * double3 + double3) * 0.30000001192092896;
        final Vec3 dck5 = new Vec3(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 1.5 + double3, this.worldPosition.getZ() + 0.5);
        for (final BlockPos fx7 : this.effectBlocks) {
            if (random2.nextInt(50) != 0) {
                continue;
            }
            final float float8 = -0.5f + random2.nextFloat();
            final float float9 = -2.0f + random2.nextFloat();
            final float float10 = -0.5f + random2.nextFloat();
            final BlockPos fx8 = fx7.subtract(this.worldPosition);
            final Vec3 dck6 = new Vec3(float8, float9, float10).add(fx8.getX(), fx8.getY(), fx8.getZ());
            this.level.addParticle(ParticleTypes.NAUTILUS, dck5.x, dck5.y, dck5.z, dck6.x, dck6.y, dck6.z);
        }
        if (this.destroyTarget != null) {
            final Vec3 dck7 = new Vec3(this.destroyTarget.getX(), this.destroyTarget.getEyeY(), this.destroyTarget.getZ());
            final float float11 = (-0.5f + random2.nextFloat()) * (3.0f + this.destroyTarget.getBbWidth());
            final float float8 = -1.0f + random2.nextFloat() * this.destroyTarget.getBbHeight();
            final float float9 = (-0.5f + random2.nextFloat()) * (3.0f + this.destroyTarget.getBbWidth());
            final Vec3 dck8 = new Vec3(float11, float8, float9);
            this.level.addParticle(ParticleTypes.NAUTILUS, dck7.x, dck7.y, dck7.z, dck8.x, dck8.y, dck8.z);
        }
    }
    
    public boolean isActive() {
        return this.isActive;
    }
    
    public boolean isHunting() {
        return this.isHunting;
    }
    
    private void setActive(final boolean boolean1) {
        if (boolean1 != this.isActive) {
            this.playSound(boolean1 ? SoundEvents.CONDUIT_ACTIVATE : SoundEvents.CONDUIT_DEACTIVATE);
        }
        this.isActive = boolean1;
    }
    
    private void setHunting(final boolean boolean1) {
        this.isHunting = boolean1;
    }
    
    public float getActiveRotation(final float float1) {
        return (this.activeRotation + float1) * -0.0375f;
    }
    
    public void playSound(final SoundEvent adn) {
        this.level.playSound(null, this.worldPosition, adn, SoundSource.BLOCKS, 1.0f, 1.0f);
    }
    
    static {
        VALID_BLOCKS = new Block[] { Blocks.PRISMARINE, Blocks.PRISMARINE_BRICKS, Blocks.SEA_LANTERN, Blocks.DARK_PRISMARINE };
    }
}

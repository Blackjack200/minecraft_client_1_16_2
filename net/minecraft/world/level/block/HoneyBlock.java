package net.minecraft.world.level.block;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HoneyBlock extends HalfTransparentBlock {
    protected static final VoxelShape SHAPE;
    
    public HoneyBlock(final Properties c) {
        super(c);
    }
    
    private static boolean doesEntityDoHoneyBlockSlideEffects(final Entity apx) {
        return apx instanceof LivingEntity || apx instanceof AbstractMinecart || apx instanceof PrimedTnt || apx instanceof Boat;
    }
    
    @Override
    public VoxelShape getCollisionShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return HoneyBlock.SHAPE;
    }
    
    @Override
    public void fallOn(final Level bru, final BlockPos fx, final Entity apx, final float float4) {
        apx.playSound(SoundEvents.HONEY_BLOCK_SLIDE, 1.0f, 1.0f);
        if (!bru.isClientSide) {
            bru.broadcastEntityEvent(apx, (byte)54);
        }
        if (apx.causeFallDamage(float4, 0.2f)) {
            apx.playSound(this.soundType.getFallSound(), this.soundType.getVolume() * 0.5f, this.soundType.getPitch() * 0.75f);
        }
    }
    
    @Override
    public void entityInside(final BlockState cee, final Level bru, final BlockPos fx, final Entity apx) {
        if (this.isSlidingDown(fx, apx)) {
            this.maybeDoSlideAchievement(apx, fx);
            this.doSlideMovement(apx);
            this.maybeDoSlideEffects(bru, apx);
        }
        super.entityInside(cee, bru, fx, apx);
    }
    
    private boolean isSlidingDown(final BlockPos fx, final Entity apx) {
        if (apx.isOnGround()) {
            return false;
        }
        if (apx.getY() > fx.getY() + 0.9375 - 1.0E-7) {
            return false;
        }
        if (apx.getDeltaMovement().y >= -0.08) {
            return false;
        }
        final double double4 = Math.abs(fx.getX() + 0.5 - apx.getX());
        final double double5 = Math.abs(fx.getZ() + 0.5 - apx.getZ());
        final double double6 = 0.4375 + apx.getBbWidth() / 2.0f;
        return double4 + 1.0E-7 > double6 || double5 + 1.0E-7 > double6;
    }
    
    private void maybeDoSlideAchievement(final Entity apx, final BlockPos fx) {
        if (apx instanceof ServerPlayer && apx.level.getGameTime() % 20L == 0L) {
            CriteriaTriggers.HONEY_BLOCK_SLIDE.trigger((ServerPlayer)apx, apx.level.getBlockState(fx));
        }
    }
    
    private void doSlideMovement(final Entity apx) {
        final Vec3 dck3 = apx.getDeltaMovement();
        if (dck3.y < -0.13) {
            final double double4 = -0.05 / dck3.y;
            apx.setDeltaMovement(new Vec3(dck3.x * double4, -0.05, dck3.z * double4));
        }
        else {
            apx.setDeltaMovement(new Vec3(dck3.x, -0.05, dck3.z));
        }
        apx.fallDistance = 0.0f;
    }
    
    private void maybeDoSlideEffects(final Level bru, final Entity apx) {
        if (doesEntityDoHoneyBlockSlideEffects(apx)) {
            if (bru.random.nextInt(5) == 0) {
                apx.playSound(SoundEvents.HONEY_BLOCK_SLIDE, 1.0f, 1.0f);
            }
            if (!bru.isClientSide && bru.random.nextInt(5) == 0) {
                bru.broadcastEntityEvent(apx, (byte)53);
            }
        }
    }
    
    public static void showSlideParticles(final Entity apx) {
        showParticles(apx, 5);
    }
    
    public static void showJumpParticles(final Entity apx) {
        showParticles(apx, 10);
    }
    
    private static void showParticles(final Entity apx, final int integer) {
        if (!apx.level.isClientSide) {
            return;
        }
        final BlockState cee3 = Blocks.HONEY_BLOCK.defaultBlockState();
        for (int integer2 = 0; integer2 < integer; ++integer2) {
            apx.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, cee3), apx.getX(), apx.getY(), apx.getZ(), 0.0, 0.0, 0.0);
        }
    }
    
    static {
        SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 15.0, 15.0);
    }
}

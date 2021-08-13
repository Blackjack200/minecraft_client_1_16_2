package net.minecraft.world.entity.ai.goal;

import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import javax.annotation.Nullable;
import net.minecraft.world.phys.Vec3;
import java.util.Random;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.Block;

public class RemoveBlockGoal extends MoveToBlockGoal {
    private final Block blockToRemove;
    private final Mob removerMob;
    private int ticksSinceReachedGoal;
    
    public RemoveBlockGoal(final Block bul, final PathfinderMob aqr, final double double3, final int integer) {
        super(aqr, double3, 24, integer);
        this.blockToRemove = bul;
        this.removerMob = aqr;
    }
    
    @Override
    public boolean canUse() {
        if (!this.removerMob.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            return false;
        }
        if (this.nextStartTick > 0) {
            --this.nextStartTick;
            return false;
        }
        if (this.tryFindBlock()) {
            this.nextStartTick = 20;
            return true;
        }
        this.nextStartTick = this.nextStartTick(this.mob);
        return false;
    }
    
    private boolean tryFindBlock() {
        return (this.blockPos != null && this.isValidTarget(this.mob.level, this.blockPos)) || this.findNearestBlock();
    }
    
    @Override
    public void stop() {
        super.stop();
        this.removerMob.fallDistance = 1.0f;
    }
    
    @Override
    public void start() {
        super.start();
        this.ticksSinceReachedGoal = 0;
    }
    
    public void playDestroyProgressSound(final LevelAccessor brv, final BlockPos fx) {
    }
    
    public void playBreakSound(final Level bru, final BlockPos fx) {
    }
    
    @Override
    public void tick() {
        super.tick();
        final Level bru2 = this.removerMob.level;
        final BlockPos fx3 = this.removerMob.blockPosition();
        final BlockPos fx4 = this.getPosWithBlock(fx3, bru2);
        final Random random5 = this.removerMob.getRandom();
        if (this.isReachedTarget() && fx4 != null) {
            if (this.ticksSinceReachedGoal > 0) {
                final Vec3 dck6 = this.removerMob.getDeltaMovement();
                this.removerMob.setDeltaMovement(dck6.x, 0.3, dck6.z);
                if (!bru2.isClientSide) {
                    final double double7 = 0.08;
                    ((ServerLevel)bru2).<ItemParticleOption>sendParticles(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.EGG)), fx4.getX() + 0.5, fx4.getY() + 0.7, fx4.getZ() + 0.5, 3, (random5.nextFloat() - 0.5) * 0.08, (random5.nextFloat() - 0.5) * 0.08, (random5.nextFloat() - 0.5) * 0.08, 0.15000000596046448);
                }
            }
            if (this.ticksSinceReachedGoal % 2 == 0) {
                final Vec3 dck6 = this.removerMob.getDeltaMovement();
                this.removerMob.setDeltaMovement(dck6.x, -0.3, dck6.z);
                if (this.ticksSinceReachedGoal % 6 == 0) {
                    this.playDestroyProgressSound(bru2, this.blockPos);
                }
            }
            if (this.ticksSinceReachedGoal > 60) {
                bru2.removeBlock(fx4, false);
                if (!bru2.isClientSide) {
                    for (int integer6 = 0; integer6 < 20; ++integer6) {
                        final double double7 = random5.nextGaussian() * 0.02;
                        final double double8 = random5.nextGaussian() * 0.02;
                        final double double9 = random5.nextGaussian() * 0.02;
                        ((ServerLevel)bru2).<SimpleParticleType>sendParticles(ParticleTypes.POOF, fx4.getX() + 0.5, fx4.getY(), fx4.getZ() + 0.5, 1, double7, double8, double9, 0.15000000596046448);
                    }
                    this.playBreakSound(bru2, fx4);
                }
            }
            ++this.ticksSinceReachedGoal;
        }
    }
    
    @Nullable
    private BlockPos getPosWithBlock(final BlockPos fx, final BlockGetter bqz) {
        if (bqz.getBlockState(fx).is(this.blockToRemove)) {
            return fx;
        }
        final BlockPos[] array;
        final BlockPos[] arr4 = array = new BlockPos[] { fx.below(), fx.west(), fx.east(), fx.north(), fx.south(), fx.below().below() };
        for (final BlockPos fx2 : array) {
            if (bqz.getBlockState(fx2).is(this.blockToRemove)) {
                return fx2;
            }
        }
        return null;
    }
    
    @Override
    protected boolean isValidTarget(final LevelReader brw, final BlockPos fx) {
        final ChunkAccess cft4 = brw.getChunk(fx.getX() >> 4, fx.getZ() >> 4, ChunkStatus.FULL, false);
        return cft4 != null && cft4.getBlockState(fx).is(this.blockToRemove) && cft4.getBlockState(fx.above()).isAir() && cft4.getBlockState(fx.above(2)).isAir();
    }
}

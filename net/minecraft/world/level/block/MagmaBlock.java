package net.minecraft.world.level.block;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class MagmaBlock extends Block {
    public MagmaBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public void stepOn(final Level bru, final BlockPos fx, final Entity apx) {
        if (!apx.fireImmune() && apx instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)apx)) {
            apx.hurt(DamageSource.HOT_FLOOR, 1.0f);
        }
        super.stepOn(bru, fx, apx);
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        BubbleColumnBlock.growColumn(aag, fx.above(), true);
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (gc == Direction.UP && cee3.is(Blocks.WATER)) {
            brv.getBlockTicks().scheduleTick(fx5, this, 20);
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    public void randomTick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        final BlockPos fx2 = fx.above();
        if (aag.getFluidState(fx).is(FluidTags.WATER)) {
            aag.playSound(null, fx, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5f, 2.6f + (aag.random.nextFloat() - aag.random.nextFloat()) * 0.8f);
            aag.<SimpleParticleType>sendParticles(ParticleTypes.LARGE_SMOKE, fx2.getX() + 0.5, fx2.getY() + 0.25, fx2.getZ() + 0.5, 8, 0.5, 0.25, 0.5, 0.0);
        }
    }
    
    @Override
    public void onPlace(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        bru.getBlockTicks().scheduleTick(fx, this, 20);
    }
}

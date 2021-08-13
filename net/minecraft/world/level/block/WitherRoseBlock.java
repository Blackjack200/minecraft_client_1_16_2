package net.minecraft.world.level.block;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.shapes.CollisionContext;
import java.util.Random;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.effect.MobEffect;

public class WitherRoseBlock extends FlowerBlock {
    public WitherRoseBlock(final MobEffect app, final Properties c) {
        super(app, 8, c);
    }
    
    @Override
    protected boolean mayPlaceOn(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return super.mayPlaceOn(cee, bqz, fx) || cee.is(Blocks.NETHERRACK) || cee.is(Blocks.SOUL_SAND) || cee.is(Blocks.SOUL_SOIL);
    }
    
    @Override
    public void animateTick(final BlockState cee, final Level bru, final BlockPos fx, final Random random) {
        final VoxelShape dde6 = this.getShape(cee, bru, fx, CollisionContext.empty());
        final Vec3 dck7 = dde6.bounds().getCenter();
        final double double8 = fx.getX() + dck7.x;
        final double double9 = fx.getZ() + dck7.z;
        for (int integer12 = 0; integer12 < 3; ++integer12) {
            if (random.nextBoolean()) {
                bru.addParticle(ParticleTypes.SMOKE, double8 + random.nextDouble() / 5.0, fx.getY() + (0.5 - random.nextDouble()), double9 + random.nextDouble() / 5.0, 0.0, 0.0, 0.0);
            }
        }
    }
    
    @Override
    public void entityInside(final BlockState cee, final Level bru, final BlockPos fx, final Entity apx) {
        if (bru.isClientSide || bru.getDifficulty() == Difficulty.PEACEFUL) {
            return;
        }
        if (apx instanceof LivingEntity) {
            final LivingEntity aqj6 = (LivingEntity)apx;
            if (!aqj6.isInvulnerableTo(DamageSource.WITHER)) {
                aqj6.addEffect(new MobEffectInstance(MobEffects.WITHER, 40));
            }
        }
    }
}

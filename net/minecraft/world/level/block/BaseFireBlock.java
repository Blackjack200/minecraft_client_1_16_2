package net.minecraft.world.level.block;

import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.player.Player;
import java.util.Optional;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import java.util.Random;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class BaseFireBlock extends Block {
    private final float fireDamage;
    protected static final VoxelShape DOWN_AABB;
    
    public BaseFireBlock(final Properties c, final float float2) {
        super(c);
        this.fireDamage = float2;
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        return getState(bnv.getLevel(), bnv.getClickedPos());
    }
    
    public static BlockState getState(final BlockGetter bqz, final BlockPos fx) {
        final BlockPos fx2 = fx.below();
        final BlockState cee4 = bqz.getBlockState(fx2);
        if (SoulFireBlock.canSurviveOnBlock(cee4.getBlock())) {
            return Blocks.SOUL_FIRE.defaultBlockState();
        }
        return ((FireBlock)Blocks.FIRE).getStateForPlacement(bqz, fx);
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return BaseFireBlock.DOWN_AABB;
    }
    
    @Override
    public void animateTick(final BlockState cee, final Level bru, final BlockPos fx, final Random random) {
        if (random.nextInt(24) == 0) {
            bru.playLocalSound(fx.getX() + 0.5, fx.getY() + 0.5, fx.getZ() + 0.5, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0f + random.nextFloat(), random.nextFloat() * 0.7f + 0.3f, false);
        }
        final BlockPos fx2 = fx.below();
        final BlockState cee2 = bru.getBlockState(fx2);
        if (this.canBurn(cee2) || cee2.isFaceSturdy(bru, fx2, Direction.UP)) {
            for (int integer8 = 0; integer8 < 3; ++integer8) {
                final double double9 = fx.getX() + random.nextDouble();
                final double double10 = fx.getY() + random.nextDouble() * 0.5 + 0.5;
                final double double11 = fx.getZ() + random.nextDouble();
                bru.addParticle(ParticleTypes.LARGE_SMOKE, double9, double10, double11, 0.0, 0.0, 0.0);
            }
        }
        else {
            if (this.canBurn(bru.getBlockState(fx.west()))) {
                for (int integer8 = 0; integer8 < 2; ++integer8) {
                    final double double9 = fx.getX() + random.nextDouble() * 0.10000000149011612;
                    final double double10 = fx.getY() + random.nextDouble();
                    final double double11 = fx.getZ() + random.nextDouble();
                    bru.addParticle(ParticleTypes.LARGE_SMOKE, double9, double10, double11, 0.0, 0.0, 0.0);
                }
            }
            if (this.canBurn(bru.getBlockState(fx.east()))) {
                for (int integer8 = 0; integer8 < 2; ++integer8) {
                    final double double9 = fx.getX() + 1 - random.nextDouble() * 0.10000000149011612;
                    final double double10 = fx.getY() + random.nextDouble();
                    final double double11 = fx.getZ() + random.nextDouble();
                    bru.addParticle(ParticleTypes.LARGE_SMOKE, double9, double10, double11, 0.0, 0.0, 0.0);
                }
            }
            if (this.canBurn(bru.getBlockState(fx.north()))) {
                for (int integer8 = 0; integer8 < 2; ++integer8) {
                    final double double9 = fx.getX() + random.nextDouble();
                    final double double10 = fx.getY() + random.nextDouble();
                    final double double11 = fx.getZ() + random.nextDouble() * 0.10000000149011612;
                    bru.addParticle(ParticleTypes.LARGE_SMOKE, double9, double10, double11, 0.0, 0.0, 0.0);
                }
            }
            if (this.canBurn(bru.getBlockState(fx.south()))) {
                for (int integer8 = 0; integer8 < 2; ++integer8) {
                    final double double9 = fx.getX() + random.nextDouble();
                    final double double10 = fx.getY() + random.nextDouble();
                    final double double11 = fx.getZ() + 1 - random.nextDouble() * 0.10000000149011612;
                    bru.addParticle(ParticleTypes.LARGE_SMOKE, double9, double10, double11, 0.0, 0.0, 0.0);
                }
            }
            if (this.canBurn(bru.getBlockState(fx.above()))) {
                for (int integer8 = 0; integer8 < 2; ++integer8) {
                    final double double9 = fx.getX() + random.nextDouble();
                    final double double10 = fx.getY() + 1 - random.nextDouble() * 0.10000000149011612;
                    final double double11 = fx.getZ() + random.nextDouble();
                    bru.addParticle(ParticleTypes.LARGE_SMOKE, double9, double10, double11, 0.0, 0.0, 0.0);
                }
            }
        }
    }
    
    protected abstract boolean canBurn(final BlockState cee);
    
    @Override
    public void entityInside(final BlockState cee, final Level bru, final BlockPos fx, final Entity apx) {
        if (!apx.fireImmune()) {
            apx.setRemainingFireTicks(apx.getRemainingFireTicks() + 1);
            if (apx.getRemainingFireTicks() == 0) {
                apx.setSecondsOnFire(8);
            }
            apx.hurt(DamageSource.IN_FIRE, this.fireDamage);
        }
        super.entityInside(cee, bru, fx, apx);
    }
    
    @Override
    public void onPlace(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (cee4.is(cee1.getBlock())) {
            return;
        }
        if (inPortalDimension(bru)) {
            final Optional<PortalShape> optional7 = PortalShape.findEmptyPortalShape(bru, fx, Direction.Axis.X);
            if (optional7.isPresent()) {
                ((PortalShape)optional7.get()).createPortalBlocks();
                return;
            }
        }
        if (!cee1.canSurvive(bru, fx)) {
            bru.removeBlock(fx, false);
        }
    }
    
    private static boolean inPortalDimension(final Level bru) {
        return bru.dimension() == Level.OVERWORLD || bru.dimension() == Level.NETHER;
    }
    
    @Override
    public void playerWillDestroy(final Level bru, final BlockPos fx, final BlockState cee, final Player bft) {
        if (!bru.isClientSide()) {
            bru.levelEvent(null, 1009, fx, 0);
        }
    }
    
    public static boolean canBePlacedAt(final Level bru, final BlockPos fx, final Direction gc) {
        final BlockState cee4 = bru.getBlockState(fx);
        return cee4.isAir() && (getState(bru, fx).canSurvive(bru, fx) || isPortal(bru, fx, gc));
    }
    
    private static boolean isPortal(final Level bru, final BlockPos fx, final Direction gc) {
        if (!inPortalDimension(bru)) {
            return false;
        }
        final BlockPos.MutableBlockPos a4 = fx.mutable();
        boolean boolean5 = false;
        for (final Direction gc2 : Direction.values()) {
            if (bru.getBlockState(a4.set(fx).move(gc2)).is(Blocks.OBSIDIAN)) {
                boolean5 = true;
                break;
            }
        }
        return boolean5 && PortalShape.findEmptyPortalShape(bru, fx, gc.getCounterClockWise().getAxis()).isPresent();
    }
    
    static {
        DOWN_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
    }
}

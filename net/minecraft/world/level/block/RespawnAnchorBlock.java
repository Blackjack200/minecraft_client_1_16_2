package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.pathfinder.PathComputationType;
import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import java.util.Random;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import java.util.Optional;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.core.Vec3i;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class RespawnAnchorBlock extends Block {
    public static final IntegerProperty CHARGE;
    private static final ImmutableList<Vec3i> RESPAWN_HORIZONTAL_OFFSETS;
    private static final ImmutableList<Vec3i> RESPAWN_OFFSETS;
    
    public RespawnAnchorBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Comparable, Integer>setValue((Property<Comparable>)RespawnAnchorBlock.CHARGE, 0));
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        final ItemStack bly8 = bft.getItemInHand(aoq);
        if (aoq == InteractionHand.MAIN_HAND && !isRespawnFuel(bly8) && isRespawnFuel(bft.getItemInHand(InteractionHand.OFF_HAND))) {
            return InteractionResult.PASS;
        }
        if (isRespawnFuel(bly8) && canBeCharged(cee)) {
            charge(bru, fx, cee);
            if (!bft.abilities.instabuild) {
                bly8.shrink(1);
            }
            return InteractionResult.sidedSuccess(bru.isClientSide);
        }
        if (cee.<Integer>getValue((Property<Integer>)RespawnAnchorBlock.CHARGE) == 0) {
            return InteractionResult.PASS;
        }
        if (canSetSpawn(bru)) {
            if (!bru.isClientSide) {
                final ServerPlayer aah9 = (ServerPlayer)bft;
                if (aah9.getRespawnDimension() != bru.dimension() || !aah9.getRespawnPosition().equals(fx)) {
                    aah9.setRespawnPosition(bru.dimension(), fx, 0.0f, false, true);
                    bru.playSound(null, fx.getX() + 0.5, fx.getY() + 0.5, fx.getZ() + 0.5, SoundEvents.RESPAWN_ANCHOR_SET_SPAWN, SoundSource.BLOCKS, 1.0f, 1.0f);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.CONSUME;
        }
        if (!bru.isClientSide) {
            this.explode(cee, bru, fx);
        }
        return InteractionResult.sidedSuccess(bru.isClientSide);
    }
    
    private static boolean isRespawnFuel(final ItemStack bly) {
        return bly.getItem() == Items.GLOWSTONE;
    }
    
    private static boolean canBeCharged(final BlockState cee) {
        return cee.<Integer>getValue((Property<Integer>)RespawnAnchorBlock.CHARGE) < 4;
    }
    
    private static boolean isWaterThatWouldFlow(final BlockPos fx, final Level bru) {
        final FluidState cuu3 = bru.getFluidState(fx);
        if (!cuu3.is(FluidTags.WATER)) {
            return false;
        }
        if (cuu3.isSource()) {
            return true;
        }
        final float float4 = (float)cuu3.getAmount();
        if (float4 < 2.0f) {
            return false;
        }
        final FluidState cuu4 = bru.getFluidState(fx.below());
        return !cuu4.is(FluidTags.WATER);
    }
    
    private void explode(final BlockState cee, final Level bru, final BlockPos fx) {
        bru.removeBlock(fx, false);
        final boolean boolean5 = Direction.Plane.HORIZONTAL.stream().map(fx::relative).anyMatch(fx -> isWaterThatWouldFlow(fx, bru));
        final boolean boolean6 = boolean5 || bru.getFluidState(fx.above()).is(FluidTags.WATER);
        final ExplosionDamageCalculator brn7 = new ExplosionDamageCalculator() {
            @Override
            public Optional<Float> getBlockExplosionResistance(final Explosion brm, final BlockGetter bqz, final BlockPos fx, final BlockState cee, final FluidState cuu) {
                if (fx.equals(fx) && boolean6) {
                    return (Optional<Float>)Optional.of(Blocks.WATER.getExplosionResistance());
                }
                return super.getBlockExplosionResistance(brm, bqz, fx, cee, cuu);
            }
        };
        bru.explode(null, DamageSource.badRespawnPointExplosion(), brn7, fx.getX() + 0.5, fx.getY() + 0.5, fx.getZ() + 0.5, 5.0f, true, Explosion.BlockInteraction.DESTROY);
    }
    
    public static boolean canSetSpawn(final Level bru) {
        return bru.dimensionType().respawnAnchorWorks();
    }
    
    public static void charge(final Level bru, final BlockPos fx, final BlockState cee) {
        bru.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)RespawnAnchorBlock.CHARGE, cee.<Integer>getValue((Property<Integer>)RespawnAnchorBlock.CHARGE) + 1), 3);
        bru.playSound(null, fx.getX() + 0.5, fx.getY() + 0.5, fx.getZ() + 0.5, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.BLOCKS, 1.0f, 1.0f);
    }
    
    @Override
    public void animateTick(final BlockState cee, final Level bru, final BlockPos fx, final Random random) {
        if (cee.<Integer>getValue((Property<Integer>)RespawnAnchorBlock.CHARGE) == 0) {
            return;
        }
        if (random.nextInt(100) == 0) {
            bru.playSound(null, fx.getX() + 0.5, fx.getY() + 0.5, fx.getZ() + 0.5, SoundEvents.RESPAWN_ANCHOR_AMBIENT, SoundSource.BLOCKS, 1.0f, 1.0f);
        }
        final double double6 = fx.getX() + 0.5 + (0.5 - random.nextDouble());
        final double double7 = fx.getY() + 1.0;
        final double double8 = fx.getZ() + 0.5 + (0.5 - random.nextDouble());
        final double double9 = random.nextFloat() * 0.04;
        bru.addParticle(ParticleTypes.REVERSE_PORTAL, double6, double7, double8, 0.0, double9, 0.0);
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(RespawnAnchorBlock.CHARGE);
    }
    
    @Override
    public boolean hasAnalogOutputSignal(final BlockState cee) {
        return true;
    }
    
    public static int getScaledChargeLevel(final BlockState cee, final int integer) {
        return Mth.floor((cee.<Integer>getValue((Property<Integer>)RespawnAnchorBlock.CHARGE) - 0) / 4.0f * integer);
    }
    
    @Override
    public int getAnalogOutputSignal(final BlockState cee, final Level bru, final BlockPos fx) {
        return getScaledChargeLevel(cee, 15);
    }
    
    public static Optional<Vec3> findStandUpPosition(final EntityType<?> aqb, final CollisionGetter brd, final BlockPos fx) {
        final Optional<Vec3> optional4 = findStandUpPosition(aqb, brd, fx, true);
        if (optional4.isPresent()) {
            return optional4;
        }
        return findStandUpPosition(aqb, brd, fx, false);
    }
    
    private static Optional<Vec3> findStandUpPosition(final EntityType<?> aqb, final CollisionGetter brd, final BlockPos fx, final boolean boolean4) {
        final BlockPos.MutableBlockPos a5 = new BlockPos.MutableBlockPos();
        for (final Vec3i gr7 : RespawnAnchorBlock.RESPAWN_OFFSETS) {
            a5.set(fx).move(gr7);
            final Vec3 dck8 = DismountHelper.findSafeDismountLocation(aqb, brd, a5, boolean4);
            if (dck8 != null) {
                return (Optional<Vec3>)Optional.of(dck8);
            }
        }
        return (Optional<Vec3>)Optional.empty();
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    static {
        CHARGE = BlockStateProperties.RESPAWN_ANCHOR_CHARGES;
        RESPAWN_HORIZONTAL_OFFSETS = ImmutableList.of(new Vec3i(0, 0, -1), new Vec3i(-1, 0, 0), new Vec3i(0, 0, 1), new Vec3i(1, 0, 0), new Vec3i(-1, 0, -1), new Vec3i(1, 0, -1), new Vec3i(-1, 0, 1), new Vec3i(1, 0, 1));
        RESPAWN_OFFSETS = new ImmutableList.Builder().addAll((Iterable)RespawnAnchorBlock.RESPAWN_HORIZONTAL_OFFSETS).addAll(RespawnAnchorBlock.RESPAWN_HORIZONTAL_OFFSETS.stream().map(Vec3i::below).iterator()).addAll(RespawnAnchorBlock.RESPAWN_HORIZONTAL_OFFSETS.stream().map(Vec3i::above).iterator()).add(new Vec3i(0, 1, 0)).build();
    }
}

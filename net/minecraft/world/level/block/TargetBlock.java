package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.BlockGetter;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.stats.Stats;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class TargetBlock extends Block {
    private static final IntegerProperty OUTPUT_POWER;
    
    public TargetBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Comparable, Integer>setValue((Property<Comparable>)TargetBlock.OUTPUT_POWER, 0));
    }
    
    @Override
    public void onProjectileHit(final Level bru, final BlockState cee, final BlockHitResult dcg, final Projectile bgj) {
        final int integer6 = updateRedstoneOutput(bru, cee, dcg, bgj);
        final Entity apx7 = bgj.getOwner();
        if (apx7 instanceof ServerPlayer) {
            final ServerPlayer aah8 = (ServerPlayer)apx7;
            aah8.awardStat(Stats.TARGET_HIT);
            CriteriaTriggers.TARGET_BLOCK_HIT.trigger(aah8, bgj, dcg.getLocation(), integer6);
        }
    }
    
    private static int updateRedstoneOutput(final LevelAccessor brv, final BlockState cee, final BlockHitResult dcg, final Entity apx) {
        final int integer5 = getRedstoneStrength(dcg, dcg.getLocation());
        final int integer6 = (apx instanceof AbstractArrow) ? 20 : 8;
        if (!brv.getBlockTicks().hasScheduledTick(dcg.getBlockPos(), cee.getBlock())) {
            setOutputPower(brv, cee, integer5, dcg.getBlockPos(), integer6);
        }
        return integer5;
    }
    
    private static int getRedstoneStrength(final BlockHitResult dcg, final Vec3 dck) {
        final Direction gc3 = dcg.getDirection();
        final double double4 = Math.abs(Mth.frac(dck.x) - 0.5);
        final double double5 = Math.abs(Mth.frac(dck.y) - 0.5);
        final double double6 = Math.abs(Mth.frac(dck.z) - 0.5);
        final Direction.Axis a12 = gc3.getAxis();
        double double7;
        if (a12 == Direction.Axis.Y) {
            double7 = Math.max(double4, double6);
        }
        else if (a12 == Direction.Axis.Z) {
            double7 = Math.max(double4, double5);
        }
        else {
            double7 = Math.max(double5, double6);
        }
        return Math.max(1, Mth.ceil(15.0 * Mth.clamp((0.5 - double7) / 0.5, 0.0, 1.0)));
    }
    
    private static void setOutputPower(final LevelAccessor brv, final BlockState cee, final int integer3, final BlockPos fx, final int integer5) {
        brv.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)TargetBlock.OUTPUT_POWER, integer3), 3);
        brv.getBlockTicks().scheduleTick(fx, cee.getBlock(), integer5);
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (cee.<Integer>getValue((Property<Integer>)TargetBlock.OUTPUT_POWER) != 0) {
            aag.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)TargetBlock.OUTPUT_POWER, 0), 3);
        }
    }
    
    @Override
    public int getSignal(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        return cee.<Integer>getValue((Property<Integer>)TargetBlock.OUTPUT_POWER);
    }
    
    @Override
    public boolean isSignalSource(final BlockState cee) {
        return true;
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(TargetBlock.OUTPUT_POWER);
    }
    
    @Override
    public void onPlace(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (bru.isClientSide() || cee1.is(cee4.getBlock())) {
            return;
        }
        if (cee1.<Integer>getValue((Property<Integer>)TargetBlock.OUTPUT_POWER) > 0 && !bru.getBlockTicks().hasScheduledTick(fx, this)) {
            bru.setBlock(fx, ((StateHolder<O, BlockState>)cee1).<Comparable, Integer>setValue((Property<Comparable>)TargetBlock.OUTPUT_POWER, 0), 18);
        }
    }
    
    static {
        OUTPUT_POWER = BlockStateProperties.POWER;
    }
}

package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import java.util.WeakHashMap;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import com.google.common.collect.Lists;
import net.minecraft.world.level.block.state.StateDefinition;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.level.block.state.BlockBehaviour;
import java.util.List;
import net.minecraft.world.level.BlockGetter;
import java.util.Map;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class RedstoneTorchBlock extends TorchBlock {
    public static final BooleanProperty LIT;
    private static final Map<BlockGetter, List<Toggle>> RECENT_TOGGLES;
    
    protected RedstoneTorchBlock(final Properties c) {
        super(c, DustParticleOptions.REDSTONE);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Comparable, Boolean>setValue((Property<Comparable>)RedstoneTorchBlock.LIT, true));
    }
    
    @Override
    public void onPlace(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        for (final Direction gc10 : Direction.values()) {
            bru.updateNeighborsAt(fx.relative(gc10), this);
        }
    }
    
    @Override
    public void onRemove(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (boolean5) {
            return;
        }
        for (final Direction gc10 : Direction.values()) {
            bru.updateNeighborsAt(fx.relative(gc10), this);
        }
    }
    
    @Override
    public int getSignal(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        if (cee.<Boolean>getValue((Property<Boolean>)RedstoneTorchBlock.LIT) && Direction.UP != gc) {
            return 15;
        }
        return 0;
    }
    
    protected boolean hasNeighborSignal(final Level bru, final BlockPos fx, final BlockState cee) {
        return bru.hasSignal(fx.below(), Direction.DOWN);
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        final boolean boolean6 = this.hasNeighborSignal(aag, fx, cee);
        final List<Toggle> list7 = (List<Toggle>)RedstoneTorchBlock.RECENT_TOGGLES.get(aag);
        while (list7 != null && !list7.isEmpty() && aag.getGameTime() - ((Toggle)list7.get(0)).when > 60L) {
            list7.remove(0);
        }
        if (cee.<Boolean>getValue((Property<Boolean>)RedstoneTorchBlock.LIT)) {
            if (boolean6) {
                aag.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)RedstoneTorchBlock.LIT, false), 3);
                if (isToggledTooFrequently(aag, fx, true)) {
                    aag.levelEvent(1502, fx, 0);
                    aag.getBlockTicks().scheduleTick(fx, aag.getBlockState(fx).getBlock(), 160);
                }
            }
        }
        else if (!boolean6 && !isToggledTooFrequently(aag, fx, false)) {
            aag.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)RedstoneTorchBlock.LIT, true), 3);
        }
    }
    
    @Override
    public void neighborChanged(final BlockState cee, final Level bru, final BlockPos fx3, final Block bul, final BlockPos fx5, final boolean boolean6) {
        if (cee.<Boolean>getValue((Property<Boolean>)RedstoneTorchBlock.LIT) == this.hasNeighborSignal(bru, fx3, cee) && !bru.getBlockTicks().willTickThisTick(fx3, this)) {
            bru.getBlockTicks().scheduleTick(fx3, this, 2);
        }
    }
    
    @Override
    public int getDirectSignal(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        if (gc == Direction.DOWN) {
            return cee.getSignal(bqz, fx, gc);
        }
        return 0;
    }
    
    @Override
    public boolean isSignalSource(final BlockState cee) {
        return true;
    }
    
    @Override
    public void animateTick(final BlockState cee, final Level bru, final BlockPos fx, final Random random) {
        if (!cee.<Boolean>getValue((Property<Boolean>)RedstoneTorchBlock.LIT)) {
            return;
        }
        final double double6 = fx.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.2;
        final double double7 = fx.getY() + 0.7 + (random.nextDouble() - 0.5) * 0.2;
        final double double8 = fx.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.2;
        bru.addParticle(this.flameParticle, double6, double7, double8, 0.0, 0.0, 0.0);
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(RedstoneTorchBlock.LIT);
    }
    
    private static boolean isToggledTooFrequently(final Level bru, final BlockPos fx, final boolean boolean3) {
        final List<Toggle> list4 = (List<Toggle>)RedstoneTorchBlock.RECENT_TOGGLES.computeIfAbsent(bru, bqz -> Lists.newArrayList());
        if (boolean3) {
            list4.add(new Toggle(fx.immutable(), bru.getGameTime()));
        }
        int integer5 = 0;
        for (int integer6 = 0; integer6 < list4.size(); ++integer6) {
            final Toggle a7 = (Toggle)list4.get(integer6);
            if (a7.pos.equals(fx) && ++integer5 >= 8) {
                return true;
            }
        }
        return false;
    }
    
    static {
        LIT = BlockStateProperties.LIT;
        RECENT_TOGGLES = (Map)new WeakHashMap();
    }
    
    public static class Toggle {
        private final BlockPos pos;
        private final long when;
        
        public Toggle(final BlockPos fx, final long long2) {
            this.pos = fx;
            this.when = long2;
        }
    }
}

package net.minecraft.world.level.material;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleOptions;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Item;
import java.util.function.Function;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.IdMapper;

public abstract class Fluid {
    public static final IdMapper<FluidState> FLUID_STATE_REGISTRY;
    protected final StateDefinition<Fluid, FluidState> stateDefinition;
    private FluidState defaultFluidState;
    
    protected Fluid() {
        final StateDefinition.Builder<Fluid, FluidState> a2 = new StateDefinition.Builder<Fluid, FluidState>(this);
        this.createFluidStateDefinition(a2);
        this.stateDefinition = a2.create((java.util.function.Function<Fluid, FluidState>)Fluid::defaultFluidState, FluidState::new);
        this.registerDefaultState(this.stateDefinition.any());
    }
    
    protected void createFluidStateDefinition(final StateDefinition.Builder<Fluid, FluidState> a) {
    }
    
    public StateDefinition<Fluid, FluidState> getStateDefinition() {
        return this.stateDefinition;
    }
    
    protected final void registerDefaultState(final FluidState cuu) {
        this.defaultFluidState = cuu;
    }
    
    public final FluidState defaultFluidState() {
        return this.defaultFluidState;
    }
    
    public abstract Item getBucket();
    
    protected void animateTick(final Level bru, final BlockPos fx, final FluidState cuu, final Random random) {
    }
    
    protected void tick(final Level bru, final BlockPos fx, final FluidState cuu) {
    }
    
    protected void randomTick(final Level bru, final BlockPos fx, final FluidState cuu, final Random random) {
    }
    
    @Nullable
    protected ParticleOptions getDripParticle() {
        return null;
    }
    
    protected abstract boolean canBeReplacedWith(final FluidState cuu, final BlockGetter bqz, final BlockPos fx, final Fluid cut, final Direction gc);
    
    protected abstract Vec3 getFlow(final BlockGetter bqz, final BlockPos fx, final FluidState cuu);
    
    public abstract int getTickDelay(final LevelReader brw);
    
    protected boolean isRandomlyTicking() {
        return false;
    }
    
    protected boolean isEmpty() {
        return false;
    }
    
    protected abstract float getExplosionResistance();
    
    public abstract float getHeight(final FluidState cuu, final BlockGetter bqz, final BlockPos fx);
    
    public abstract float getOwnHeight(final FluidState cuu);
    
    protected abstract BlockState createLegacyBlock(final FluidState cuu);
    
    public abstract boolean isSource(final FluidState cuu);
    
    public abstract int getAmount(final FluidState cuu);
    
    public boolean isSame(final Fluid cut) {
        return cut == this;
    }
    
    public boolean is(final Tag<Fluid> aej) {
        return aej.contains(this);
    }
    
    public abstract VoxelShape getShape(final FluidState cuu, final BlockGetter bqz, final BlockPos fx);
    
    static {
        FLUID_STATE_REGISTRY = new IdMapper<FluidState>();
    }
}

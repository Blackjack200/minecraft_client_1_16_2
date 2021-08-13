package net.minecraft.world.level.block;

import net.minecraft.core.particles.ParticleTypes;
import java.util.Random;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TorchBlock extends Block {
    protected static final VoxelShape AABB;
    protected final ParticleOptions flameParticle;
    
    protected TorchBlock(final Properties c, final ParticleOptions hf) {
        super(c);
        this.flameParticle = hf;
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return TorchBlock.AABB;
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (gc == Direction.DOWN && !this.canSurvive(cee1, brv, fx5)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        return Block.canSupportCenter(brw, fx.below(), Direction.UP);
    }
    
    @Override
    public void animateTick(final BlockState cee, final Level bru, final BlockPos fx, final Random random) {
        final double double6 = fx.getX() + 0.5;
        final double double7 = fx.getY() + 0.7;
        final double double8 = fx.getZ() + 0.5;
        bru.addParticle(ParticleTypes.SMOKE, double6, double7, double8, 0.0, 0.0, 0.0);
        bru.addParticle(this.flameParticle, double6, double7, double8, 0.0, 0.0, 0.0);
    }
    
    static {
        AABB = Block.box(6.0, 0.0, 6.0, 10.0, 10.0, 10.0);
    }
}

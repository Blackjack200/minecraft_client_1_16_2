package net.minecraft.world.level.block;

import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DragonEggBlock extends FallingBlock {
    protected static final VoxelShape SHAPE;
    
    public DragonEggBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return DragonEggBlock.SHAPE;
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        this.teleport(cee, bru, fx);
        return InteractionResult.sidedSuccess(bru.isClientSide);
    }
    
    @Override
    public void attack(final BlockState cee, final Level bru, final BlockPos fx, final Player bft) {
        this.teleport(cee, bru, fx);
    }
    
    private void teleport(final BlockState cee, final Level bru, final BlockPos fx) {
        for (int integer5 = 0; integer5 < 1000; ++integer5) {
            final BlockPos fx2 = fx.offset(bru.random.nextInt(16) - bru.random.nextInt(16), bru.random.nextInt(8) - bru.random.nextInt(8), bru.random.nextInt(16) - bru.random.nextInt(16));
            if (bru.getBlockState(fx2).isAir()) {
                if (bru.isClientSide) {
                    for (int integer6 = 0; integer6 < 128; ++integer6) {
                        final double double8 = bru.random.nextDouble();
                        final float float10 = (bru.random.nextFloat() - 0.5f) * 0.2f;
                        final float float11 = (bru.random.nextFloat() - 0.5f) * 0.2f;
                        final float float12 = (bru.random.nextFloat() - 0.5f) * 0.2f;
                        final double double9 = Mth.lerp(double8, fx2.getX(), fx.getX()) + (bru.random.nextDouble() - 0.5) + 0.5;
                        final double double10 = Mth.lerp(double8, fx2.getY(), fx.getY()) + bru.random.nextDouble() - 0.5;
                        final double double11 = Mth.lerp(double8, fx2.getZ(), fx.getZ()) + (bru.random.nextDouble() - 0.5) + 0.5;
                        bru.addParticle(ParticleTypes.PORTAL, double9, double10, double11, float10, float11, float12);
                    }
                }
                else {
                    bru.setBlock(fx2, cee, 2);
                    bru.removeBlock(fx, false);
                }
                return;
            }
        }
    }
    
    @Override
    protected int getDelayAfterPlace() {
        return 5;
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    static {
        SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);
    }
}

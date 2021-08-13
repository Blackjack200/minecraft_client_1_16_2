package net.minecraft.world.level.block;

import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import java.util.Random;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EndPortalBlock extends BaseEntityBlock {
    protected static final VoxelShape SHAPE;
    
    protected EndPortalBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        return new TheEndPortalBlockEntity();
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return EndPortalBlock.SHAPE;
    }
    
    @Override
    public void entityInside(final BlockState cee, final Level bru, final BlockPos fx, final Entity apx) {
        if (bru instanceof ServerLevel && !apx.isPassenger() && !apx.isVehicle() && apx.canChangeDimensions() && Shapes.joinIsNotEmpty(Shapes.create(apx.getBoundingBox().move(-fx.getX(), -fx.getY(), -fx.getZ())), cee.getShape(bru, fx), BooleanOp.AND)) {
            final ResourceKey<Level> vj6 = (bru.dimension() == Level.END) ? Level.OVERWORLD : Level.END;
            final ServerLevel aag7 = ((ServerLevel)bru).getServer().getLevel(vj6);
            if (aag7 == null) {
                return;
            }
            apx.changeDimension(aag7);
        }
    }
    
    @Override
    public void animateTick(final BlockState cee, final Level bru, final BlockPos fx, final Random random) {
        final double double6 = fx.getX() + random.nextDouble();
        final double double7 = fx.getY() + 0.8;
        final double double8 = fx.getZ() + random.nextDouble();
        bru.addParticle(ParticleTypes.SMOKE, double6, double7, double8, 0.0, 0.0, 0.0);
    }
    
    @Override
    public ItemStack getCloneItemStack(final BlockGetter bqz, final BlockPos fx, final BlockState cee) {
        return ItemStack.EMPTY;
    }
    
    @Override
    public boolean canBeReplaced(final BlockState cee, final Fluid cut) {
        return false;
    }
    
    static {
        SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0);
    }
}

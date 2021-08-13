package net.minecraft.world.level.block;

import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class EndGatewayBlock extends BaseEntityBlock {
    protected EndGatewayBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        return new TheEndGatewayBlockEntity();
    }
    
    @Override
    public void animateTick(final BlockState cee, final Level bru, final BlockPos fx, final Random random) {
        final BlockEntity ccg6 = bru.getBlockEntity(fx);
        if (!(ccg6 instanceof TheEndGatewayBlockEntity)) {
            return;
        }
        for (int integer7 = ((TheEndGatewayBlockEntity)ccg6).getParticleAmount(), integer8 = 0; integer8 < integer7; ++integer8) {
            double double9 = fx.getX() + random.nextDouble();
            final double double10 = fx.getY() + random.nextDouble();
            double double11 = fx.getZ() + random.nextDouble();
            double double12 = (random.nextDouble() - 0.5) * 0.5;
            final double double13 = (random.nextDouble() - 0.5) * 0.5;
            double double14 = (random.nextDouble() - 0.5) * 0.5;
            final int integer9 = random.nextInt(2) * 2 - 1;
            if (random.nextBoolean()) {
                double11 = fx.getZ() + 0.5 + 0.25 * integer9;
                double14 = random.nextFloat() * 2.0f * integer9;
            }
            else {
                double9 = fx.getX() + 0.5 + 0.25 * integer9;
                double12 = random.nextFloat() * 2.0f * integer9;
            }
            bru.addParticle(ParticleTypes.PORTAL, double9, double10, double11, double12, double13, double14);
        }
    }
    
    @Override
    public ItemStack getCloneItemStack(final BlockGetter bqz, final BlockPos fx, final BlockState cee) {
        return ItemStack.EMPTY;
    }
    
    @Override
    public boolean canBeReplaced(final BlockState cee, final Fluid cut) {
        return false;
    }
}

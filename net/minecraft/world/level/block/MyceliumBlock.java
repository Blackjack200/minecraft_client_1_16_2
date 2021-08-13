package net.minecraft.world.level.block;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class MyceliumBlock extends SpreadingSnowyDirtBlock {
    public MyceliumBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public void animateTick(final BlockState cee, final Level bru, final BlockPos fx, final Random random) {
        super.animateTick(cee, bru, fx, random);
        if (random.nextInt(10) == 0) {
            bru.addParticle(ParticleTypes.MYCELIUM, fx.getX() + random.nextDouble(), fx.getY() + 1.1, fx.getZ() + random.nextDouble(), 0.0, 0.0, 0.0);
        }
    }
}

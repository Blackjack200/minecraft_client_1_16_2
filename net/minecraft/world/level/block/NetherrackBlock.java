package net.minecraft.world.level.block;

import net.minecraft.server.level.ServerLevel;
import java.util.Random;
import net.minecraft.world.level.Level;
import java.util.Iterator;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class NetherrackBlock extends Block implements BonemealableBlock {
    public NetherrackBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public boolean isValidBonemealTarget(final BlockGetter bqz, final BlockPos fx, final BlockState cee, final boolean boolean4) {
        if (!bqz.getBlockState(fx.above()).propagatesSkylightDown(bqz, fx)) {
            return false;
        }
        for (final BlockPos fx2 : BlockPos.betweenClosed(fx.offset(-1, -1, -1), fx.offset(1, 1, 1))) {
            if (bqz.getBlockState(fx2).is(BlockTags.NYLIUM)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isBonemealSuccess(final Level bru, final Random random, final BlockPos fx, final BlockState cee) {
        return true;
    }
    
    @Override
    public void performBonemeal(final ServerLevel aag, final Random random, final BlockPos fx, final BlockState cee) {
        boolean boolean6 = false;
        boolean boolean7 = false;
        for (final BlockPos fx2 : BlockPos.betweenClosed(fx.offset(-1, -1, -1), fx.offset(1, 1, 1))) {
            final BlockState cee2 = aag.getBlockState(fx2);
            if (cee2.is(Blocks.WARPED_NYLIUM)) {
                boolean7 = true;
            }
            if (cee2.is(Blocks.CRIMSON_NYLIUM)) {
                boolean6 = true;
            }
            if (boolean7 && boolean6) {
                break;
            }
        }
        if (boolean7 && boolean6) {
            aag.setBlock(fx, random.nextBoolean() ? Blocks.WARPED_NYLIUM.defaultBlockState() : Blocks.CRIMSON_NYLIUM.defaultBlockState(), 3);
        }
        else if (boolean7) {
            aag.setBlock(fx, Blocks.WARPED_NYLIUM.defaultBlockState(), 3);
        }
        else if (boolean6) {
            aag.setBlock(fx, Blocks.CRIMSON_NYLIUM.defaultBlockState(), 3);
        }
    }
}

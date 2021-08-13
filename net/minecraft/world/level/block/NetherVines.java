package net.minecraft.world.level.block;

import java.util.Random;
import net.minecraft.world.level.block.state.BlockState;

public class NetherVines {
    public static boolean isValidGrowthState(final BlockState cee) {
        return cee.isAir();
    }
    
    public static int getBlocksToGrowWhenBonemealed(final Random random) {
        double double2;
        int integer4;
        for (double2 = 1.0, integer4 = 0; random.nextDouble() < double2; double2 *= 0.826, ++integer4) {}
        return integer4;
    }
}

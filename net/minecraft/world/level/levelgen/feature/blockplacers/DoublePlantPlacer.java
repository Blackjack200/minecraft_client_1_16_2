package net.minecraft.world.level.levelgen.feature.blockplacers;

import net.minecraft.world.level.block.DoublePlantBlock;
import java.util.Random;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import com.mojang.serialization.Codec;

public class DoublePlantPlacer extends BlockPlacer {
    public static final Codec<DoublePlantPlacer> CODEC;
    public static final DoublePlantPlacer INSTANCE;
    
    @Override
    protected BlockPlacerType<?> type() {
        return BlockPlacerType.DOUBLE_PLANT_PLACER;
    }
    
    @Override
    public void place(final LevelAccessor brv, final BlockPos fx, final BlockState cee, final Random random) {
        ((DoublePlantBlock)cee.getBlock()).placeAt(brv, fx, 2);
    }
    
    static {
        CODEC = Codec.unit(() -> DoublePlantPlacer.INSTANCE);
        INSTANCE = new DoublePlantPlacer();
    }
}

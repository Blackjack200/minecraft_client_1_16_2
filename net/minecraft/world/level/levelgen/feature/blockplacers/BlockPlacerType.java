package net.minecraft.world.level.levelgen.feature.blockplacers;

import net.minecraft.core.Registry;
import com.mojang.serialization.Codec;

public class BlockPlacerType<P extends BlockPlacer> {
    public static final BlockPlacerType<SimpleBlockPlacer> SIMPLE_BLOCK_PLACER;
    public static final BlockPlacerType<DoublePlantPlacer> DOUBLE_PLANT_PLACER;
    public static final BlockPlacerType<ColumnPlacer> COLUMN_PLACER;
    private final Codec<P> codec;
    
    private static <P extends BlockPlacer> BlockPlacerType<P> register(final String string, final Codec<P> codec) {
        return Registry.<BlockPlacerType<P>>register(Registry.BLOCK_PLACER_TYPES, string, new BlockPlacerType<P>(codec));
    }
    
    private BlockPlacerType(final Codec<P> codec) {
        this.codec = codec;
    }
    
    public Codec<P> codec() {
        return this.codec;
    }
    
    static {
        SIMPLE_BLOCK_PLACER = BlockPlacerType.<SimpleBlockPlacer>register("simple_block_placer", SimpleBlockPlacer.CODEC);
        DOUBLE_PLANT_PLACER = BlockPlacerType.<DoublePlantPlacer>register("double_plant_placer", DoublePlantPlacer.CODEC);
        COLUMN_PLACER = BlockPlacerType.<ColumnPlacer>register("column_placer", ColumnPlacer.CODEC);
    }
}

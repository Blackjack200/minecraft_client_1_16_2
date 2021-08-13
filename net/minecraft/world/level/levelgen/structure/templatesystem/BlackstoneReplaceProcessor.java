package net.minecraft.world.level.levelgen.structure.templatesystem;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.Blocks;
import java.util.HashMap;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import java.util.function.Consumer;
import net.minecraft.Util;
import com.google.common.collect.Maps;
import net.minecraft.world.level.block.Block;
import java.util.Map;
import com.mojang.serialization.Codec;

public class BlackstoneReplaceProcessor extends StructureProcessor {
    public static final Codec<BlackstoneReplaceProcessor> CODEC;
    public static final BlackstoneReplaceProcessor INSTANCE;
    private final Map<Block, Block> replacements;
    
    private BlackstoneReplaceProcessor() {
        this.replacements = Util.make((Map)Maps.newHashMap(), (java.util.function.Consumer<Map>)(hashMap -> {
            hashMap.put(Blocks.COBBLESTONE, Blocks.BLACKSTONE);
            hashMap.put(Blocks.MOSSY_COBBLESTONE, Blocks.BLACKSTONE);
            hashMap.put(Blocks.STONE, Blocks.POLISHED_BLACKSTONE);
            hashMap.put(Blocks.STONE_BRICKS, Blocks.POLISHED_BLACKSTONE_BRICKS);
            hashMap.put(Blocks.MOSSY_STONE_BRICKS, Blocks.POLISHED_BLACKSTONE_BRICKS);
            hashMap.put(Blocks.COBBLESTONE_STAIRS, Blocks.BLACKSTONE_STAIRS);
            hashMap.put(Blocks.MOSSY_COBBLESTONE_STAIRS, Blocks.BLACKSTONE_STAIRS);
            hashMap.put(Blocks.STONE_STAIRS, Blocks.POLISHED_BLACKSTONE_STAIRS);
            hashMap.put(Blocks.STONE_BRICK_STAIRS, Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS);
            hashMap.put(Blocks.MOSSY_STONE_BRICK_STAIRS, Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS);
            hashMap.put(Blocks.COBBLESTONE_SLAB, Blocks.BLACKSTONE_SLAB);
            hashMap.put(Blocks.MOSSY_COBBLESTONE_SLAB, Blocks.BLACKSTONE_SLAB);
            hashMap.put(Blocks.SMOOTH_STONE_SLAB, Blocks.POLISHED_BLACKSTONE_SLAB);
            hashMap.put(Blocks.STONE_SLAB, Blocks.POLISHED_BLACKSTONE_SLAB);
            hashMap.put(Blocks.STONE_BRICK_SLAB, Blocks.POLISHED_BLACKSTONE_BRICK_SLAB);
            hashMap.put(Blocks.MOSSY_STONE_BRICK_SLAB, Blocks.POLISHED_BLACKSTONE_BRICK_SLAB);
            hashMap.put(Blocks.STONE_BRICK_WALL, Blocks.POLISHED_BLACKSTONE_BRICK_WALL);
            hashMap.put(Blocks.MOSSY_STONE_BRICK_WALL, Blocks.POLISHED_BLACKSTONE_BRICK_WALL);
            hashMap.put(Blocks.COBBLESTONE_WALL, Blocks.BLACKSTONE_WALL);
            hashMap.put(Blocks.MOSSY_COBBLESTONE_WALL, Blocks.BLACKSTONE_WALL);
            hashMap.put(Blocks.CHISELED_STONE_BRICKS, Blocks.CHISELED_POLISHED_BLACKSTONE);
            hashMap.put(Blocks.CRACKED_STONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS);
            hashMap.put(Blocks.IRON_BARS, Blocks.CHAIN);
        }));
    }
    
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(final LevelReader brw, final BlockPos fx2, final BlockPos fx3, final StructureTemplate.StructureBlockInfo c4, final StructureTemplate.StructureBlockInfo c5, final StructurePlaceSettings csu) {
        final Block bul8 = (Block)this.replacements.get(c5.state.getBlock());
        if (bul8 == null) {
            return c5;
        }
        final BlockState cee9 = c5.state;
        BlockState cee10 = bul8.defaultBlockState();
        if (cee9.<Comparable>hasProperty((Property<Comparable>)StairBlock.FACING)) {
            cee10 = ((StateHolder<O, BlockState>)cee10).<Comparable, Comparable>setValue((Property<Comparable>)StairBlock.FACING, (Comparable)cee9.<V>getValue((Property<V>)StairBlock.FACING));
        }
        if (cee9.<Half>hasProperty(StairBlock.HALF)) {
            cee10 = ((StateHolder<O, BlockState>)cee10).<Half, Comparable>setValue(StairBlock.HALF, (Comparable)cee9.<V>getValue((Property<V>)StairBlock.HALF));
        }
        if (cee9.<SlabType>hasProperty(SlabBlock.TYPE)) {
            cee10 = ((StateHolder<O, BlockState>)cee10).<SlabType, Comparable>setValue(SlabBlock.TYPE, (Comparable)cee9.<V>getValue((Property<V>)SlabBlock.TYPE));
        }
        return new StructureTemplate.StructureBlockInfo(c5.pos, cee10, c5.nbt);
    }
    
    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.BLACKSTONE_REPLACE;
    }
    
    static {
        CODEC = Codec.unit(() -> BlackstoneReplaceProcessor.INSTANCE);
        INSTANCE = new BlackstoneReplaceProcessor();
    }
}

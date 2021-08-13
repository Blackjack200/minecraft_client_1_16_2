package net.minecraft.world.level.levelgen.structure.templatesystem;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.core.Direction;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Random;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import com.mojang.serialization.Codec;

public class BlockAgeProcessor extends StructureProcessor {
    public static final Codec<BlockAgeProcessor> CODEC;
    private final float mossiness;
    
    public BlockAgeProcessor(final float float1) {
        this.mossiness = float1;
    }
    
    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(final LevelReader brw, final BlockPos fx2, final BlockPos fx3, final StructureTemplate.StructureBlockInfo c4, final StructureTemplate.StructureBlockInfo c5, final StructurePlaceSettings csu) {
        final Random random8 = csu.getRandom(c5.pos);
        final BlockState cee9 = c5.state;
        final BlockPos fx4 = c5.pos;
        BlockState cee10 = null;
        if (cee9.is(Blocks.STONE_BRICKS) || cee9.is(Blocks.STONE) || cee9.is(Blocks.CHISELED_STONE_BRICKS)) {
            cee10 = this.maybeReplaceFullStoneBlock(random8);
        }
        else if (cee9.is(BlockTags.STAIRS)) {
            cee10 = this.maybeReplaceStairs(random8, c5.state);
        }
        else if (cee9.is(BlockTags.SLABS)) {
            cee10 = this.maybeReplaceSlab(random8);
        }
        else if (cee9.is(BlockTags.WALLS)) {
            cee10 = this.maybeReplaceWall(random8);
        }
        else if (cee9.is(Blocks.OBSIDIAN)) {
            cee10 = this.maybeReplaceObsidian(random8);
        }
        if (cee10 != null) {
            return new StructureTemplate.StructureBlockInfo(fx4, cee10, c5.nbt);
        }
        return c5;
    }
    
    @Nullable
    private BlockState maybeReplaceFullStoneBlock(final Random random) {
        if (random.nextFloat() >= 0.5f) {
            return null;
        }
        final BlockState[] arr3 = { Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), getRandomFacingStairs(random, Blocks.STONE_BRICK_STAIRS) };
        final BlockState[] arr4 = { Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), getRandomFacingStairs(random, Blocks.MOSSY_STONE_BRICK_STAIRS) };
        return this.getRandomBlock(random, arr3, arr4);
    }
    
    @Nullable
    private BlockState maybeReplaceStairs(final Random random, final BlockState cee) {
        final Direction gc4 = cee.<Direction>getValue((Property<Direction>)StairBlock.FACING);
        final Half cfc5 = cee.<Half>getValue(StairBlock.HALF);
        if (random.nextFloat() >= 0.5f) {
            return null;
        }
        final BlockState[] arr6 = { Blocks.STONE_SLAB.defaultBlockState(), Blocks.STONE_BRICK_SLAB.defaultBlockState() };
        final BlockState[] arr7 = { (((StateHolder<O, BlockState>)Blocks.MOSSY_STONE_BRICK_STAIRS.defaultBlockState()).setValue((Property<Comparable>)StairBlock.FACING, gc4)).<Half, Half>setValue(StairBlock.HALF, cfc5), Blocks.MOSSY_STONE_BRICK_SLAB.defaultBlockState() };
        return this.getRandomBlock(random, arr6, arr7);
    }
    
    @Nullable
    private BlockState maybeReplaceSlab(final Random random) {
        if (random.nextFloat() < this.mossiness) {
            return Blocks.MOSSY_STONE_BRICK_SLAB.defaultBlockState();
        }
        return null;
    }
    
    @Nullable
    private BlockState maybeReplaceWall(final Random random) {
        if (random.nextFloat() < this.mossiness) {
            return Blocks.MOSSY_STONE_BRICK_WALL.defaultBlockState();
        }
        return null;
    }
    
    @Nullable
    private BlockState maybeReplaceObsidian(final Random random) {
        if (random.nextFloat() < 0.15f) {
            return Blocks.CRYING_OBSIDIAN.defaultBlockState();
        }
        return null;
    }
    
    private static BlockState getRandomFacingStairs(final Random random, final Block bul) {
        return (((StateHolder<O, BlockState>)bul.defaultBlockState()).setValue((Property<Comparable>)StairBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random))).<Half, Half>setValue(StairBlock.HALF, Half.values()[random.nextInt(Half.values().length)]);
    }
    
    private BlockState getRandomBlock(final Random random, final BlockState[] arr2, final BlockState[] arr3) {
        if (random.nextFloat() < this.mossiness) {
            return getRandomBlock(random, arr3);
        }
        return getRandomBlock(random, arr2);
    }
    
    private static BlockState getRandomBlock(final Random random, final BlockState[] arr) {
        return arr[random.nextInt(arr.length)];
    }
    
    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.BLOCK_AGE;
    }
    
    static {
        CODEC = Codec.FLOAT.fieldOf("mossiness").xmap(BlockAgeProcessor::new, csa -> csa.mossiness).codec();
    }
}

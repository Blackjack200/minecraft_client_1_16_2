package net.minecraft.world.level.levelgen.structure;

import net.minecraft.world.level.block.state.StateHolder;
import java.util.Iterator;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import java.util.Random;

public class DesertPyramidPiece extends ScatteredFeaturePiece {
    private final boolean[] hasPlacedChest;
    
    public DesertPyramidPiece(final Random random, final int integer2, final int integer3) {
        super(StructurePieceType.DESERT_PYRAMID_PIECE, random, integer2, 64, integer3, 21, 15, 21);
        this.hasPlacedChest = new boolean[4];
    }
    
    public DesertPyramidPiece(final StructureManager cst, final CompoundTag md) {
        super(StructurePieceType.DESERT_PYRAMID_PIECE, md);
        (this.hasPlacedChest = new boolean[4])[0] = md.getBoolean("hasPlacedChest0");
        this.hasPlacedChest[1] = md.getBoolean("hasPlacedChest1");
        this.hasPlacedChest[2] = md.getBoolean("hasPlacedChest2");
        this.hasPlacedChest[3] = md.getBoolean("hasPlacedChest3");
    }
    
    @Override
    protected void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putBoolean("hasPlacedChest0", this.hasPlacedChest[0]);
        md.putBoolean("hasPlacedChest1", this.hasPlacedChest[1]);
        md.putBoolean("hasPlacedChest2", this.hasPlacedChest[2]);
        md.putBoolean("hasPlacedChest3", this.hasPlacedChest[3]);
    }
    
    @Override
    public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
        this.generateBox(bso, cqx, 0, -4, 0, this.width - 1, 0, this.depth - 1, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
        for (int integer9 = 1; integer9 <= 9; ++integer9) {
            this.generateBox(bso, cqx, integer9, integer9, integer9, this.width - 1 - integer9, integer9, this.depth - 1 - integer9, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
            this.generateBox(bso, cqx, integer9 + 1, integer9, integer9 + 1, this.width - 2 - integer9, integer9, this.depth - 2 - integer9, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
        }
        for (int integer9 = 0; integer9 < this.width; ++integer9) {
            for (int integer10 = 0; integer10 < this.depth; ++integer10) {
                final int integer11 = -5;
                this.fillColumnDown(bso, Blocks.SANDSTONE.defaultBlockState(), integer9, -5, integer10, cqx);
            }
        }
        final BlockState cee9 = ((StateHolder<O, BlockState>)Blocks.SANDSTONE_STAIRS.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)StairBlock.FACING, Direction.NORTH);
        final BlockState cee10 = ((StateHolder<O, BlockState>)Blocks.SANDSTONE_STAIRS.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)StairBlock.FACING, Direction.SOUTH);
        final BlockState cee11 = ((StateHolder<O, BlockState>)Blocks.SANDSTONE_STAIRS.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)StairBlock.FACING, Direction.EAST);
        final BlockState cee12 = ((StateHolder<O, BlockState>)Blocks.SANDSTONE_STAIRS.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)StairBlock.FACING, Direction.WEST);
        this.generateBox(bso, cqx, 0, 0, 0, 4, 9, 4, Blocks.SANDSTONE.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
        this.generateBox(bso, cqx, 1, 10, 1, 3, 10, 3, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
        this.placeBlock(bso, cee9, 2, 10, 0, cqx);
        this.placeBlock(bso, cee10, 2, 10, 4, cqx);
        this.placeBlock(bso, cee11, 0, 10, 2, cqx);
        this.placeBlock(bso, cee12, 4, 10, 2, cqx);
        this.generateBox(bso, cqx, this.width - 5, 0, 0, this.width - 1, 9, 4, Blocks.SANDSTONE.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
        this.generateBox(bso, cqx, this.width - 4, 10, 1, this.width - 2, 10, 3, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
        this.placeBlock(bso, cee9, this.width - 3, 10, 0, cqx);
        this.placeBlock(bso, cee10, this.width - 3, 10, 4, cqx);
        this.placeBlock(bso, cee11, this.width - 5, 10, 2, cqx);
        this.placeBlock(bso, cee12, this.width - 1, 10, 2, cqx);
        this.generateBox(bso, cqx, 8, 0, 0, 12, 4, 4, Blocks.SANDSTONE.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
        this.generateBox(bso, cqx, 9, 1, 0, 11, 3, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
        this.placeBlock(bso, Blocks.CUT_SANDSTONE.defaultBlockState(), 9, 1, 1, cqx);
        this.placeBlock(bso, Blocks.CUT_SANDSTONE.defaultBlockState(), 9, 2, 1, cqx);
        this.placeBlock(bso, Blocks.CUT_SANDSTONE.defaultBlockState(), 9, 3, 1, cqx);
        this.placeBlock(bso, Blocks.CUT_SANDSTONE.defaultBlockState(), 10, 3, 1, cqx);
        this.placeBlock(bso, Blocks.CUT_SANDSTONE.defaultBlockState(), 11, 3, 1, cqx);
        this.placeBlock(bso, Blocks.CUT_SANDSTONE.defaultBlockState(), 11, 2, 1, cqx);
        this.placeBlock(bso, Blocks.CUT_SANDSTONE.defaultBlockState(), 11, 1, 1, cqx);
        this.generateBox(bso, cqx, 4, 1, 1, 8, 3, 3, Blocks.SANDSTONE.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
        this.generateBox(bso, cqx, 4, 1, 2, 8, 2, 2, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
        this.generateBox(bso, cqx, 12, 1, 1, 16, 3, 3, Blocks.SANDSTONE.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
        this.generateBox(bso, cqx, 12, 1, 2, 16, 2, 2, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
        this.generateBox(bso, cqx, 5, 4, 5, this.width - 6, 4, this.depth - 6, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
        this.generateBox(bso, cqx, 9, 4, 9, 11, 4, 11, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
        this.generateBox(bso, cqx, 8, 1, 8, 8, 3, 8, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
        this.generateBox(bso, cqx, 12, 1, 8, 12, 3, 8, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
        this.generateBox(bso, cqx, 8, 1, 12, 8, 3, 12, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
        this.generateBox(bso, cqx, 12, 1, 12, 12, 3, 12, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
        this.generateBox(bso, cqx, 1, 1, 5, 4, 4, 11, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
        this.generateBox(bso, cqx, this.width - 5, 1, 5, this.width - 2, 4, 11, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
        this.generateBox(bso, cqx, 6, 7, 9, 6, 7, 11, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
        this.generateBox(bso, cqx, this.width - 7, 7, 9, this.width - 7, 7, 11, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
        this.generateBox(bso, cqx, 5, 5, 9, 5, 7, 11, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
        this.generateBox(bso, cqx, this.width - 6, 5, 9, this.width - 6, 7, 11, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
        this.placeBlock(bso, Blocks.AIR.defaultBlockState(), 5, 5, 10, cqx);
        this.placeBlock(bso, Blocks.AIR.defaultBlockState(), 5, 6, 10, cqx);
        this.placeBlock(bso, Blocks.AIR.defaultBlockState(), 6, 6, 10, cqx);
        this.placeBlock(bso, Blocks.AIR.defaultBlockState(), this.width - 6, 5, 10, cqx);
        this.placeBlock(bso, Blocks.AIR.defaultBlockState(), this.width - 6, 6, 10, cqx);
        this.placeBlock(bso, Blocks.AIR.defaultBlockState(), this.width - 7, 6, 10, cqx);
        this.generateBox(bso, cqx, 2, 4, 4, 2, 6, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
        this.generateBox(bso, cqx, this.width - 3, 4, 4, this.width - 3, 6, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
        this.placeBlock(bso, cee9, 2, 4, 5, cqx);
        this.placeBlock(bso, cee9, 2, 3, 4, cqx);
        this.placeBlock(bso, cee9, this.width - 3, 4, 5, cqx);
        this.placeBlock(bso, cee9, this.width - 3, 3, 4, cqx);
        this.generateBox(bso, cqx, 1, 1, 3, 2, 2, 3, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
        this.generateBox(bso, cqx, this.width - 3, 1, 3, this.width - 2, 2, 3, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
        this.placeBlock(bso, Blocks.SANDSTONE.defaultBlockState(), 1, 1, 2, cqx);
        this.placeBlock(bso, Blocks.SANDSTONE.defaultBlockState(), this.width - 2, 1, 2, cqx);
        this.placeBlock(bso, Blocks.SANDSTONE_SLAB.defaultBlockState(), 1, 2, 2, cqx);
        this.placeBlock(bso, Blocks.SANDSTONE_SLAB.defaultBlockState(), this.width - 2, 2, 2, cqx);
        this.placeBlock(bso, cee12, 2, 1, 2, cqx);
        this.placeBlock(bso, cee11, this.width - 3, 1, 2, cqx);
        this.generateBox(bso, cqx, 4, 3, 5, 4, 3, 17, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
        this.generateBox(bso, cqx, this.width - 5, 3, 5, this.width - 5, 3, 17, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
        this.generateBox(bso, cqx, 3, 1, 5, 4, 2, 16, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
        this.generateBox(bso, cqx, this.width - 6, 1, 5, this.width - 5, 2, 16, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
        for (int integer12 = 5; integer12 <= 17; integer12 += 2) {
            this.placeBlock(bso, Blocks.CUT_SANDSTONE.defaultBlockState(), 4, 1, integer12, cqx);
            this.placeBlock(bso, Blocks.CHISELED_SANDSTONE.defaultBlockState(), 4, 2, integer12, cqx);
            this.placeBlock(bso, Blocks.CUT_SANDSTONE.defaultBlockState(), this.width - 5, 1, integer12, cqx);
            this.placeBlock(bso, Blocks.CHISELED_SANDSTONE.defaultBlockState(), this.width - 5, 2, integer12, cqx);
        }
        this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 10, 0, 7, cqx);
        this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 10, 0, 8, cqx);
        this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 9, 0, 9, cqx);
        this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 11, 0, 9, cqx);
        this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 8, 0, 10, cqx);
        this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 12, 0, 10, cqx);
        this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 7, 0, 10, cqx);
        this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 13, 0, 10, cqx);
        this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 9, 0, 11, cqx);
        this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 11, 0, 11, cqx);
        this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 10, 0, 12, cqx);
        this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 10, 0, 13, cqx);
        this.placeBlock(bso, Blocks.BLUE_TERRACOTTA.defaultBlockState(), 10, 0, 10, cqx);
        for (int integer12 = 0; integer12 <= this.width - 1; integer12 += this.width - 1) {
            this.placeBlock(bso, Blocks.CUT_SANDSTONE.defaultBlockState(), integer12, 2, 1, cqx);
            this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), integer12, 2, 2, cqx);
            this.placeBlock(bso, Blocks.CUT_SANDSTONE.defaultBlockState(), integer12, 2, 3, cqx);
            this.placeBlock(bso, Blocks.CUT_SANDSTONE.defaultBlockState(), integer12, 3, 1, cqx);
            this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), integer12, 3, 2, cqx);
            this.placeBlock(bso, Blocks.CUT_SANDSTONE.defaultBlockState(), integer12, 3, 3, cqx);
            this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), integer12, 4, 1, cqx);
            this.placeBlock(bso, Blocks.CHISELED_SANDSTONE.defaultBlockState(), integer12, 4, 2, cqx);
            this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), integer12, 4, 3, cqx);
            this.placeBlock(bso, Blocks.CUT_SANDSTONE.defaultBlockState(), integer12, 5, 1, cqx);
            this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), integer12, 5, 2, cqx);
            this.placeBlock(bso, Blocks.CUT_SANDSTONE.defaultBlockState(), integer12, 5, 3, cqx);
            this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), integer12, 6, 1, cqx);
            this.placeBlock(bso, Blocks.CHISELED_SANDSTONE.defaultBlockState(), integer12, 6, 2, cqx);
            this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), integer12, 6, 3, cqx);
            this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), integer12, 7, 1, cqx);
            this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), integer12, 7, 2, cqx);
            this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), integer12, 7, 3, cqx);
            this.placeBlock(bso, Blocks.CUT_SANDSTONE.defaultBlockState(), integer12, 8, 1, cqx);
            this.placeBlock(bso, Blocks.CUT_SANDSTONE.defaultBlockState(), integer12, 8, 2, cqx);
            this.placeBlock(bso, Blocks.CUT_SANDSTONE.defaultBlockState(), integer12, 8, 3, cqx);
        }
        for (int integer12 = 2; integer12 <= this.width - 3; integer12 += this.width - 3 - 2) {
            this.placeBlock(bso, Blocks.CUT_SANDSTONE.defaultBlockState(), integer12 - 1, 2, 0, cqx);
            this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), integer12, 2, 0, cqx);
            this.placeBlock(bso, Blocks.CUT_SANDSTONE.defaultBlockState(), integer12 + 1, 2, 0, cqx);
            this.placeBlock(bso, Blocks.CUT_SANDSTONE.defaultBlockState(), integer12 - 1, 3, 0, cqx);
            this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), integer12, 3, 0, cqx);
            this.placeBlock(bso, Blocks.CUT_SANDSTONE.defaultBlockState(), integer12 + 1, 3, 0, cqx);
            this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), integer12 - 1, 4, 0, cqx);
            this.placeBlock(bso, Blocks.CHISELED_SANDSTONE.defaultBlockState(), integer12, 4, 0, cqx);
            this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), integer12 + 1, 4, 0, cqx);
            this.placeBlock(bso, Blocks.CUT_SANDSTONE.defaultBlockState(), integer12 - 1, 5, 0, cqx);
            this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), integer12, 5, 0, cqx);
            this.placeBlock(bso, Blocks.CUT_SANDSTONE.defaultBlockState(), integer12 + 1, 5, 0, cqx);
            this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), integer12 - 1, 6, 0, cqx);
            this.placeBlock(bso, Blocks.CHISELED_SANDSTONE.defaultBlockState(), integer12, 6, 0, cqx);
            this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), integer12 + 1, 6, 0, cqx);
            this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), integer12 - 1, 7, 0, cqx);
            this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), integer12, 7, 0, cqx);
            this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), integer12 + 1, 7, 0, cqx);
            this.placeBlock(bso, Blocks.CUT_SANDSTONE.defaultBlockState(), integer12 - 1, 8, 0, cqx);
            this.placeBlock(bso, Blocks.CUT_SANDSTONE.defaultBlockState(), integer12, 8, 0, cqx);
            this.placeBlock(bso, Blocks.CUT_SANDSTONE.defaultBlockState(), integer12 + 1, 8, 0, cqx);
        }
        this.generateBox(bso, cqx, 8, 4, 0, 12, 6, 0, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
        this.placeBlock(bso, Blocks.AIR.defaultBlockState(), 8, 6, 0, cqx);
        this.placeBlock(bso, Blocks.AIR.defaultBlockState(), 12, 6, 0, cqx);
        this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 9, 5, 0, cqx);
        this.placeBlock(bso, Blocks.CHISELED_SANDSTONE.defaultBlockState(), 10, 5, 0, cqx);
        this.placeBlock(bso, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 11, 5, 0, cqx);
        this.generateBox(bso, cqx, 8, -14, 8, 12, -11, 12, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
        this.generateBox(bso, cqx, 8, -10, 8, 12, -10, 12, Blocks.CHISELED_SANDSTONE.defaultBlockState(), Blocks.CHISELED_SANDSTONE.defaultBlockState(), false);
        this.generateBox(bso, cqx, 8, -9, 8, 12, -9, 12, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
        this.generateBox(bso, cqx, 8, -8, 8, 12, -1, 12, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
        this.generateBox(bso, cqx, 9, -11, 9, 11, -1, 11, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
        this.placeBlock(bso, Blocks.STONE_PRESSURE_PLATE.defaultBlockState(), 10, -11, 10, cqx);
        this.generateBox(bso, cqx, 9, -13, 9, 11, -13, 11, Blocks.TNT.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
        this.placeBlock(bso, Blocks.AIR.defaultBlockState(), 8, -11, 10, cqx);
        this.placeBlock(bso, Blocks.AIR.defaultBlockState(), 8, -10, 10, cqx);
        this.placeBlock(bso, Blocks.CHISELED_SANDSTONE.defaultBlockState(), 7, -10, 10, cqx);
        this.placeBlock(bso, Blocks.CUT_SANDSTONE.defaultBlockState(), 7, -11, 10, cqx);
        this.placeBlock(bso, Blocks.AIR.defaultBlockState(), 12, -11, 10, cqx);
        this.placeBlock(bso, Blocks.AIR.defaultBlockState(), 12, -10, 10, cqx);
        this.placeBlock(bso, Blocks.CHISELED_SANDSTONE.defaultBlockState(), 13, -10, 10, cqx);
        this.placeBlock(bso, Blocks.CUT_SANDSTONE.defaultBlockState(), 13, -11, 10, cqx);
        this.placeBlock(bso, Blocks.AIR.defaultBlockState(), 10, -11, 8, cqx);
        this.placeBlock(bso, Blocks.AIR.defaultBlockState(), 10, -10, 8, cqx);
        this.placeBlock(bso, Blocks.CHISELED_SANDSTONE.defaultBlockState(), 10, -10, 7, cqx);
        this.placeBlock(bso, Blocks.CUT_SANDSTONE.defaultBlockState(), 10, -11, 7, cqx);
        this.placeBlock(bso, Blocks.AIR.defaultBlockState(), 10, -11, 12, cqx);
        this.placeBlock(bso, Blocks.AIR.defaultBlockState(), 10, -10, 12, cqx);
        this.placeBlock(bso, Blocks.CHISELED_SANDSTONE.defaultBlockState(), 10, -10, 13, cqx);
        this.placeBlock(bso, Blocks.CUT_SANDSTONE.defaultBlockState(), 10, -11, 13, cqx);
        for (final Direction gc14 : Direction.Plane.HORIZONTAL) {
            if (!this.hasPlacedChest[gc14.get2DDataValue()]) {
                final int integer13 = gc14.getStepX() * 2;
                final int integer14 = gc14.getStepZ() * 2;
                this.hasPlacedChest[gc14.get2DDataValue()] = this.createChest(bso, cqx, random, 10 + integer13, -11, 10 + integer14, BuiltInLootTables.DESERT_PYRAMID);
            }
        }
        return true;
    }
}

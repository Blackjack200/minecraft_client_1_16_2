package net.minecraft.world.level.levelgen.structure;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.TripWireBlock;
import net.minecraft.world.level.block.TripWireHookBlock;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import java.util.Random;

public class JunglePyramidPiece extends ScatteredFeaturePiece {
    private boolean placedMainChest;
    private boolean placedHiddenChest;
    private boolean placedTrap1;
    private boolean placedTrap2;
    private static final MossStoneSelector STONE_SELECTOR;
    
    public JunglePyramidPiece(final Random random, final int integer2, final int integer3) {
        super(StructurePieceType.JUNGLE_PYRAMID_PIECE, random, integer2, 64, integer3, 12, 10, 15);
    }
    
    public JunglePyramidPiece(final StructureManager cst, final CompoundTag md) {
        super(StructurePieceType.JUNGLE_PYRAMID_PIECE, md);
        this.placedMainChest = md.getBoolean("placedMainChest");
        this.placedHiddenChest = md.getBoolean("placedHiddenChest");
        this.placedTrap1 = md.getBoolean("placedTrap1");
        this.placedTrap2 = md.getBoolean("placedTrap2");
    }
    
    @Override
    protected void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putBoolean("placedMainChest", this.placedMainChest);
        md.putBoolean("placedHiddenChest", this.placedHiddenChest);
        md.putBoolean("placedTrap1", this.placedTrap1);
        md.putBoolean("placedTrap2", this.placedTrap2);
    }
    
    @Override
    public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
        if (!this.updateAverageGroundHeight(bso, cqx, 0)) {
            return false;
        }
        this.generateBox(bso, cqx, 0, -4, 0, this.width - 1, 0, this.depth - 1, false, random, JunglePyramidPiece.STONE_SELECTOR);
        this.generateBox(bso, cqx, 2, 1, 2, 9, 2, 2, false, random, JunglePyramidPiece.STONE_SELECTOR);
        this.generateBox(bso, cqx, 2, 1, 12, 9, 2, 12, false, random, JunglePyramidPiece.STONE_SELECTOR);
        this.generateBox(bso, cqx, 2, 1, 3, 2, 2, 11, false, random, JunglePyramidPiece.STONE_SELECTOR);
        this.generateBox(bso, cqx, 9, 1, 3, 9, 2, 11, false, random, JunglePyramidPiece.STONE_SELECTOR);
        this.generateBox(bso, cqx, 1, 3, 1, 10, 6, 1, false, random, JunglePyramidPiece.STONE_SELECTOR);
        this.generateBox(bso, cqx, 1, 3, 13, 10, 6, 13, false, random, JunglePyramidPiece.STONE_SELECTOR);
        this.generateBox(bso, cqx, 1, 3, 2, 1, 6, 12, false, random, JunglePyramidPiece.STONE_SELECTOR);
        this.generateBox(bso, cqx, 10, 3, 2, 10, 6, 12, false, random, JunglePyramidPiece.STONE_SELECTOR);
        this.generateBox(bso, cqx, 2, 3, 2, 9, 3, 12, false, random, JunglePyramidPiece.STONE_SELECTOR);
        this.generateBox(bso, cqx, 2, 6, 2, 9, 6, 12, false, random, JunglePyramidPiece.STONE_SELECTOR);
        this.generateBox(bso, cqx, 3, 7, 3, 8, 7, 11, false, random, JunglePyramidPiece.STONE_SELECTOR);
        this.generateBox(bso, cqx, 4, 8, 4, 7, 8, 10, false, random, JunglePyramidPiece.STONE_SELECTOR);
        this.generateAirBox(bso, cqx, 3, 1, 3, 8, 2, 11);
        this.generateAirBox(bso, cqx, 4, 3, 6, 7, 3, 9);
        this.generateAirBox(bso, cqx, 2, 4, 2, 9, 5, 12);
        this.generateAirBox(bso, cqx, 4, 6, 5, 7, 6, 9);
        this.generateAirBox(bso, cqx, 5, 7, 6, 6, 7, 8);
        this.generateAirBox(bso, cqx, 5, 1, 2, 6, 2, 2);
        this.generateAirBox(bso, cqx, 5, 2, 12, 6, 2, 12);
        this.generateAirBox(bso, cqx, 5, 5, 1, 6, 5, 1);
        this.generateAirBox(bso, cqx, 5, 5, 13, 6, 5, 13);
        this.placeBlock(bso, Blocks.AIR.defaultBlockState(), 1, 5, 5, cqx);
        this.placeBlock(bso, Blocks.AIR.defaultBlockState(), 10, 5, 5, cqx);
        this.placeBlock(bso, Blocks.AIR.defaultBlockState(), 1, 5, 9, cqx);
        this.placeBlock(bso, Blocks.AIR.defaultBlockState(), 10, 5, 9, cqx);
        for (int integer9 = 0; integer9 <= 14; integer9 += 14) {
            this.generateBox(bso, cqx, 2, 4, integer9, 2, 5, integer9, false, random, JunglePyramidPiece.STONE_SELECTOR);
            this.generateBox(bso, cqx, 4, 4, integer9, 4, 5, integer9, false, random, JunglePyramidPiece.STONE_SELECTOR);
            this.generateBox(bso, cqx, 7, 4, integer9, 7, 5, integer9, false, random, JunglePyramidPiece.STONE_SELECTOR);
            this.generateBox(bso, cqx, 9, 4, integer9, 9, 5, integer9, false, random, JunglePyramidPiece.STONE_SELECTOR);
        }
        this.generateBox(bso, cqx, 5, 6, 0, 6, 6, 0, false, random, JunglePyramidPiece.STONE_SELECTOR);
        for (int integer9 = 0; integer9 <= 11; integer9 += 11) {
            for (int integer10 = 2; integer10 <= 12; integer10 += 2) {
                this.generateBox(bso, cqx, integer9, 4, integer10, integer9, 5, integer10, false, random, JunglePyramidPiece.STONE_SELECTOR);
            }
            this.generateBox(bso, cqx, integer9, 6, 5, integer9, 6, 5, false, random, JunglePyramidPiece.STONE_SELECTOR);
            this.generateBox(bso, cqx, integer9, 6, 9, integer9, 6, 9, false, random, JunglePyramidPiece.STONE_SELECTOR);
        }
        this.generateBox(bso, cqx, 2, 7, 2, 2, 9, 2, false, random, JunglePyramidPiece.STONE_SELECTOR);
        this.generateBox(bso, cqx, 9, 7, 2, 9, 9, 2, false, random, JunglePyramidPiece.STONE_SELECTOR);
        this.generateBox(bso, cqx, 2, 7, 12, 2, 9, 12, false, random, JunglePyramidPiece.STONE_SELECTOR);
        this.generateBox(bso, cqx, 9, 7, 12, 9, 9, 12, false, random, JunglePyramidPiece.STONE_SELECTOR);
        this.generateBox(bso, cqx, 4, 9, 4, 4, 9, 4, false, random, JunglePyramidPiece.STONE_SELECTOR);
        this.generateBox(bso, cqx, 7, 9, 4, 7, 9, 4, false, random, JunglePyramidPiece.STONE_SELECTOR);
        this.generateBox(bso, cqx, 4, 9, 10, 4, 9, 10, false, random, JunglePyramidPiece.STONE_SELECTOR);
        this.generateBox(bso, cqx, 7, 9, 10, 7, 9, 10, false, random, JunglePyramidPiece.STONE_SELECTOR);
        this.generateBox(bso, cqx, 5, 9, 7, 6, 9, 7, false, random, JunglePyramidPiece.STONE_SELECTOR);
        final BlockState cee9 = ((StateHolder<O, BlockState>)Blocks.COBBLESTONE_STAIRS.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)StairBlock.FACING, Direction.EAST);
        final BlockState cee10 = ((StateHolder<O, BlockState>)Blocks.COBBLESTONE_STAIRS.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)StairBlock.FACING, Direction.WEST);
        final BlockState cee11 = ((StateHolder<O, BlockState>)Blocks.COBBLESTONE_STAIRS.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)StairBlock.FACING, Direction.SOUTH);
        final BlockState cee12 = ((StateHolder<O, BlockState>)Blocks.COBBLESTONE_STAIRS.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)StairBlock.FACING, Direction.NORTH);
        this.placeBlock(bso, cee12, 5, 9, 6, cqx);
        this.placeBlock(bso, cee12, 6, 9, 6, cqx);
        this.placeBlock(bso, cee11, 5, 9, 8, cqx);
        this.placeBlock(bso, cee11, 6, 9, 8, cqx);
        this.placeBlock(bso, cee12, 4, 0, 0, cqx);
        this.placeBlock(bso, cee12, 5, 0, 0, cqx);
        this.placeBlock(bso, cee12, 6, 0, 0, cqx);
        this.placeBlock(bso, cee12, 7, 0, 0, cqx);
        this.placeBlock(bso, cee12, 4, 1, 8, cqx);
        this.placeBlock(bso, cee12, 4, 2, 9, cqx);
        this.placeBlock(bso, cee12, 4, 3, 10, cqx);
        this.placeBlock(bso, cee12, 7, 1, 8, cqx);
        this.placeBlock(bso, cee12, 7, 2, 9, cqx);
        this.placeBlock(bso, cee12, 7, 3, 10, cqx);
        this.generateBox(bso, cqx, 4, 1, 9, 4, 1, 9, false, random, JunglePyramidPiece.STONE_SELECTOR);
        this.generateBox(bso, cqx, 7, 1, 9, 7, 1, 9, false, random, JunglePyramidPiece.STONE_SELECTOR);
        this.generateBox(bso, cqx, 4, 1, 10, 7, 2, 10, false, random, JunglePyramidPiece.STONE_SELECTOR);
        this.generateBox(bso, cqx, 5, 4, 5, 6, 4, 5, false, random, JunglePyramidPiece.STONE_SELECTOR);
        this.placeBlock(bso, cee9, 4, 4, 5, cqx);
        this.placeBlock(bso, cee10, 7, 4, 5, cqx);
        for (int integer11 = 0; integer11 < 4; ++integer11) {
            this.placeBlock(bso, cee11, 5, 0 - integer11, 6 + integer11, cqx);
            this.placeBlock(bso, cee11, 6, 0 - integer11, 6 + integer11, cqx);
            this.generateAirBox(bso, cqx, 5, 0 - integer11, 7 + integer11, 6, 0 - integer11, 9 + integer11);
        }
        this.generateAirBox(bso, cqx, 1, -3, 12, 10, -1, 13);
        this.generateAirBox(bso, cqx, 1, -3, 1, 3, -1, 13);
        this.generateAirBox(bso, cqx, 1, -3, 1, 9, -1, 5);
        for (int integer11 = 1; integer11 <= 13; integer11 += 2) {
            this.generateBox(bso, cqx, 1, -3, integer11, 1, -2, integer11, false, random, JunglePyramidPiece.STONE_SELECTOR);
        }
        for (int integer11 = 2; integer11 <= 12; integer11 += 2) {
            this.generateBox(bso, cqx, 1, -1, integer11, 3, -1, integer11, false, random, JunglePyramidPiece.STONE_SELECTOR);
        }
        this.generateBox(bso, cqx, 2, -2, 1, 5, -2, 1, false, random, JunglePyramidPiece.STONE_SELECTOR);
        this.generateBox(bso, cqx, 7, -2, 1, 9, -2, 1, false, random, JunglePyramidPiece.STONE_SELECTOR);
        this.generateBox(bso, cqx, 6, -3, 1, 6, -3, 1, false, random, JunglePyramidPiece.STONE_SELECTOR);
        this.generateBox(bso, cqx, 6, -1, 1, 6, -1, 1, false, random, JunglePyramidPiece.STONE_SELECTOR);
        this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.TRIPWIRE_HOOK.defaultBlockState()).setValue((Property<Comparable>)TripWireHookBlock.FACING, Direction.EAST)).<Comparable, Boolean>setValue((Property<Comparable>)TripWireHookBlock.ATTACHED, true), 1, -3, 8, cqx);
        this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.TRIPWIRE_HOOK.defaultBlockState()).setValue((Property<Comparable>)TripWireHookBlock.FACING, Direction.WEST)).<Comparable, Boolean>setValue((Property<Comparable>)TripWireHookBlock.ATTACHED, true), 4, -3, 8, cqx);
        this.placeBlock(bso, ((((StateHolder<O, BlockState>)Blocks.TRIPWIRE.defaultBlockState()).setValue((Property<Comparable>)TripWireBlock.EAST, true)).setValue((Property<Comparable>)TripWireBlock.WEST, true)).<Comparable, Boolean>setValue((Property<Comparable>)TripWireBlock.ATTACHED, true), 2, -3, 8, cqx);
        this.placeBlock(bso, ((((StateHolder<O, BlockState>)Blocks.TRIPWIRE.defaultBlockState()).setValue((Property<Comparable>)TripWireBlock.EAST, true)).setValue((Property<Comparable>)TripWireBlock.WEST, true)).<Comparable, Boolean>setValue((Property<Comparable>)TripWireBlock.ATTACHED, true), 3, -3, 8, cqx);
        final BlockState cee13 = (((StateHolder<O, BlockState>)Blocks.REDSTONE_WIRE.defaultBlockState()).setValue(RedStoneWireBlock.NORTH, RedstoneSide.SIDE)).<RedstoneSide, RedstoneSide>setValue(RedStoneWireBlock.SOUTH, RedstoneSide.SIDE);
        this.placeBlock(bso, cee13, 5, -3, 7, cqx);
        this.placeBlock(bso, cee13, 5, -3, 6, cqx);
        this.placeBlock(bso, cee13, 5, -3, 5, cqx);
        this.placeBlock(bso, cee13, 5, -3, 4, cqx);
        this.placeBlock(bso, cee13, 5, -3, 3, cqx);
        this.placeBlock(bso, cee13, 5, -3, 2, cqx);
        this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.REDSTONE_WIRE.defaultBlockState()).setValue(RedStoneWireBlock.NORTH, RedstoneSide.SIDE)).<RedstoneSide, RedstoneSide>setValue(RedStoneWireBlock.WEST, RedstoneSide.SIDE), 5, -3, 1, cqx);
        this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.REDSTONE_WIRE.defaultBlockState()).setValue(RedStoneWireBlock.EAST, RedstoneSide.SIDE)).<RedstoneSide, RedstoneSide>setValue(RedStoneWireBlock.WEST, RedstoneSide.SIDE), 4, -3, 1, cqx);
        this.placeBlock(bso, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 3, -3, 1, cqx);
        if (!this.placedTrap1) {
            this.placedTrap1 = this.createDispenser(bso, cqx, random, 3, -2, 1, Direction.NORTH, BuiltInLootTables.JUNGLE_TEMPLE_DISPENSER);
        }
        this.placeBlock(bso, ((StateHolder<O, BlockState>)Blocks.VINE.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)VineBlock.SOUTH, true), 3, -2, 2, cqx);
        this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.TRIPWIRE_HOOK.defaultBlockState()).setValue((Property<Comparable>)TripWireHookBlock.FACING, Direction.NORTH)).<Comparable, Boolean>setValue((Property<Comparable>)TripWireHookBlock.ATTACHED, true), 7, -3, 1, cqx);
        this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.TRIPWIRE_HOOK.defaultBlockState()).setValue((Property<Comparable>)TripWireHookBlock.FACING, Direction.SOUTH)).<Comparable, Boolean>setValue((Property<Comparable>)TripWireHookBlock.ATTACHED, true), 7, -3, 5, cqx);
        this.placeBlock(bso, ((((StateHolder<O, BlockState>)Blocks.TRIPWIRE.defaultBlockState()).setValue((Property<Comparable>)TripWireBlock.NORTH, true)).setValue((Property<Comparable>)TripWireBlock.SOUTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)TripWireBlock.ATTACHED, true), 7, -3, 2, cqx);
        this.placeBlock(bso, ((((StateHolder<O, BlockState>)Blocks.TRIPWIRE.defaultBlockState()).setValue((Property<Comparable>)TripWireBlock.NORTH, true)).setValue((Property<Comparable>)TripWireBlock.SOUTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)TripWireBlock.ATTACHED, true), 7, -3, 3, cqx);
        this.placeBlock(bso, ((((StateHolder<O, BlockState>)Blocks.TRIPWIRE.defaultBlockState()).setValue((Property<Comparable>)TripWireBlock.NORTH, true)).setValue((Property<Comparable>)TripWireBlock.SOUTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)TripWireBlock.ATTACHED, true), 7, -3, 4, cqx);
        this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.REDSTONE_WIRE.defaultBlockState()).setValue(RedStoneWireBlock.EAST, RedstoneSide.SIDE)).<RedstoneSide, RedstoneSide>setValue(RedStoneWireBlock.WEST, RedstoneSide.SIDE), 8, -3, 6, cqx);
        this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.REDSTONE_WIRE.defaultBlockState()).setValue(RedStoneWireBlock.WEST, RedstoneSide.SIDE)).<RedstoneSide, RedstoneSide>setValue(RedStoneWireBlock.SOUTH, RedstoneSide.SIDE), 9, -3, 6, cqx);
        this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.REDSTONE_WIRE.defaultBlockState()).setValue(RedStoneWireBlock.NORTH, RedstoneSide.SIDE)).<RedstoneSide, RedstoneSide>setValue(RedStoneWireBlock.SOUTH, RedstoneSide.UP), 9, -3, 5, cqx);
        this.placeBlock(bso, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 9, -3, 4, cqx);
        this.placeBlock(bso, cee13, 9, -2, 4, cqx);
        if (!this.placedTrap2) {
            this.placedTrap2 = this.createDispenser(bso, cqx, random, 9, -2, 3, Direction.WEST, BuiltInLootTables.JUNGLE_TEMPLE_DISPENSER);
        }
        this.placeBlock(bso, ((StateHolder<O, BlockState>)Blocks.VINE.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)VineBlock.EAST, true), 8, -1, 3, cqx);
        this.placeBlock(bso, ((StateHolder<O, BlockState>)Blocks.VINE.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)VineBlock.EAST, true), 8, -2, 3, cqx);
        if (!this.placedMainChest) {
            this.placedMainChest = this.createChest(bso, cqx, random, 8, -3, 3, BuiltInLootTables.JUNGLE_TEMPLE);
        }
        this.placeBlock(bso, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 9, -3, 2, cqx);
        this.placeBlock(bso, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 8, -3, 1, cqx);
        this.placeBlock(bso, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 4, -3, 5, cqx);
        this.placeBlock(bso, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 5, -2, 5, cqx);
        this.placeBlock(bso, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 5, -1, 5, cqx);
        this.placeBlock(bso, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 6, -3, 5, cqx);
        this.placeBlock(bso, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 7, -2, 5, cqx);
        this.placeBlock(bso, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 7, -1, 5, cqx);
        this.placeBlock(bso, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 8, -3, 5, cqx);
        this.generateBox(bso, cqx, 9, -1, 1, 9, -1, 5, false, random, JunglePyramidPiece.STONE_SELECTOR);
        this.generateAirBox(bso, cqx, 8, -3, 8, 10, -1, 10);
        this.placeBlock(bso, Blocks.CHISELED_STONE_BRICKS.defaultBlockState(), 8, -2, 11, cqx);
        this.placeBlock(bso, Blocks.CHISELED_STONE_BRICKS.defaultBlockState(), 9, -2, 11, cqx);
        this.placeBlock(bso, Blocks.CHISELED_STONE_BRICKS.defaultBlockState(), 10, -2, 11, cqx);
        final BlockState cee14 = (((StateHolder<O, BlockState>)Blocks.LEVER.defaultBlockState()).setValue((Property<Comparable>)LeverBlock.FACING, Direction.NORTH)).<AttachFace, AttachFace>setValue(LeverBlock.FACE, AttachFace.WALL);
        this.placeBlock(bso, cee14, 8, -2, 12, cqx);
        this.placeBlock(bso, cee14, 9, -2, 12, cqx);
        this.placeBlock(bso, cee14, 10, -2, 12, cqx);
        this.generateBox(bso, cqx, 8, -3, 8, 8, -3, 10, false, random, JunglePyramidPiece.STONE_SELECTOR);
        this.generateBox(bso, cqx, 10, -3, 8, 10, -3, 10, false, random, JunglePyramidPiece.STONE_SELECTOR);
        this.placeBlock(bso, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 10, -2, 9, cqx);
        this.placeBlock(bso, cee13, 8, -2, 9, cqx);
        this.placeBlock(bso, cee13, 8, -2, 10, cqx);
        this.placeBlock(bso, (((((StateHolder<O, BlockState>)Blocks.REDSTONE_WIRE.defaultBlockState()).setValue(RedStoneWireBlock.NORTH, RedstoneSide.SIDE)).setValue(RedStoneWireBlock.SOUTH, RedstoneSide.SIDE)).setValue(RedStoneWireBlock.EAST, RedstoneSide.SIDE)).<RedstoneSide, RedstoneSide>setValue(RedStoneWireBlock.WEST, RedstoneSide.SIDE), 10, -1, 9, cqx);
        this.placeBlock(bso, ((StateHolder<O, BlockState>)Blocks.STICKY_PISTON.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)PistonBaseBlock.FACING, Direction.UP), 9, -2, 8, cqx);
        this.placeBlock(bso, ((StateHolder<O, BlockState>)Blocks.STICKY_PISTON.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)PistonBaseBlock.FACING, Direction.WEST), 10, -2, 8, cqx);
        this.placeBlock(bso, ((StateHolder<O, BlockState>)Blocks.STICKY_PISTON.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)PistonBaseBlock.FACING, Direction.WEST), 10, -1, 8, cqx);
        this.placeBlock(bso, ((StateHolder<O, BlockState>)Blocks.REPEATER.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)RepeaterBlock.FACING, Direction.NORTH), 10, -2, 10, cqx);
        if (!this.placedHiddenChest) {
            this.placedHiddenChest = this.createChest(bso, cqx, random, 9, -3, 10, BuiltInLootTables.JUNGLE_TEMPLE);
        }
        return true;
    }
    
    static {
        STONE_SELECTOR = new MossStoneSelector();
    }
    
    static class MossStoneSelector extends BlockSelector {
        private MossStoneSelector() {
        }
        
        @Override
        public void next(final Random random, final int integer2, final int integer3, final int integer4, final boolean boolean5) {
            if (random.nextFloat() < 0.4f) {
                this.next = Blocks.COBBLESTONE.defaultBlockState();
            }
            else {
                this.next = Blocks.MOSSY_COBBLESTONE.defaultBlockState();
            }
        }
    }
}

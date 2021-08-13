package net.minecraft.world.level.levelgen.structure;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.EndPortalFrameBlock;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import java.util.Random;
import java.util.Iterator;
import com.google.common.collect.Lists;
import java.util.List;

public class StrongholdPieces {
    private static final PieceWeight[] STRONGHOLD_PIECE_WEIGHTS;
    private static List<PieceWeight> currentPieces;
    private static Class<? extends StrongholdPiece> imposedPiece;
    private static int totalWeight;
    private static final SmoothStoneSelector SMOOTH_STONE_SELECTOR;
    
    public static void resetPieces() {
        StrongholdPieces.currentPieces = (List<PieceWeight>)Lists.newArrayList();
        for (final PieceWeight f4 : StrongholdPieces.STRONGHOLD_PIECE_WEIGHTS) {
            f4.placeCount = 0;
            StrongholdPieces.currentPieces.add(f4);
        }
        StrongholdPieces.imposedPiece = null;
    }
    
    private static boolean updatePieceWeight() {
        boolean boolean1 = false;
        StrongholdPieces.totalWeight = 0;
        for (final PieceWeight f3 : StrongholdPieces.currentPieces) {
            if (f3.maxPlaceCount > 0 && f3.placeCount < f3.maxPlaceCount) {
                boolean1 = true;
            }
            StrongholdPieces.totalWeight += f3.weight;
        }
        return boolean1;
    }
    
    private static StrongholdPiece findAndCreatePieceFactory(final Class<? extends StrongholdPiece> class1, final List<StructurePiece> list, final Random random, final int integer4, final int integer5, final int integer6, @Nullable final Direction gc, final int integer8) {
        StrongholdPiece p9 = null;
        if (class1 == Straight.class) {
            p9 = Straight.createPiece(list, random, integer4, integer5, integer6, gc, integer8);
        }
        else if (class1 == PrisonHall.class) {
            p9 = PrisonHall.createPiece(list, random, integer4, integer5, integer6, gc, integer8);
        }
        else if (class1 == LeftTurn.class) {
            p9 = LeftTurn.createPiece(list, random, integer4, integer5, integer6, gc, integer8);
        }
        else if (class1 == RightTurn.class) {
            p9 = RightTurn.createPiece(list, random, integer4, integer5, integer6, gc, integer8);
        }
        else if (class1 == RoomCrossing.class) {
            p9 = RoomCrossing.createPiece(list, random, integer4, integer5, integer6, gc, integer8);
        }
        else if (class1 == StraightStairsDown.class) {
            p9 = StraightStairsDown.createPiece(list, random, integer4, integer5, integer6, gc, integer8);
        }
        else if (class1 == StairsDown.class) {
            p9 = StairsDown.createPiece(list, random, integer4, integer5, integer6, gc, integer8);
        }
        else if (class1 == FiveCrossing.class) {
            p9 = FiveCrossing.createPiece(list, random, integer4, integer5, integer6, gc, integer8);
        }
        else if (class1 == ChestCorridor.class) {
            p9 = ChestCorridor.createPiece(list, random, integer4, integer5, integer6, gc, integer8);
        }
        else if (class1 == Library.class) {
            p9 = Library.createPiece(list, random, integer4, integer5, integer6, gc, integer8);
        }
        else if (class1 == PortalRoom.class) {
            p9 = PortalRoom.createPiece(list, integer4, integer5, integer6, gc, integer8);
        }
        return p9;
    }
    
    private static StrongholdPiece generatePieceFromSmallDoor(final StartPiece m, final List<StructurePiece> list, final Random random, final int integer4, final int integer5, final int integer6, final Direction gc, final int integer8) {
        if (!updatePieceWeight()) {
            return null;
        }
        if (StrongholdPieces.imposedPiece != null) {
            final StrongholdPiece p9 = findAndCreatePieceFactory(StrongholdPieces.imposedPiece, list, random, integer4, integer5, integer6, gc, integer8);
            StrongholdPieces.imposedPiece = null;
            if (p9 != null) {
                return p9;
            }
        }
        int integer9 = 0;
        while (integer9 < 5) {
            ++integer9;
            int integer10 = random.nextInt(StrongholdPieces.totalWeight);
            for (final PieceWeight f12 : StrongholdPieces.currentPieces) {
                integer10 -= f12.weight;
                if (integer10 < 0) {
                    if (!f12.doPlace(integer8)) {
                        break;
                    }
                    if (f12 == m.previousPiece) {
                        break;
                    }
                    final StrongholdPiece p10 = findAndCreatePieceFactory(f12.pieceClass, list, random, integer4, integer5, integer6, gc, integer8);
                    if (p10 != null) {
                        final PieceWeight pieceWeight = f12;
                        ++pieceWeight.placeCount;
                        m.previousPiece = f12;
                        if (!f12.isValid()) {
                            StrongholdPieces.currentPieces.remove(f12);
                        }
                        return p10;
                    }
                    continue;
                }
            }
        }
        final BoundingBox cqx10 = FillerCorridor.findPieceBox(list, random, integer4, integer5, integer6, gc);
        if (cqx10 != null && cqx10.y0 > 1) {
            return new FillerCorridor(integer8, cqx10, gc);
        }
        return null;
    }
    
    private static StructurePiece generateAndAddPiece(final StartPiece m, final List<StructurePiece> list, final Random random, final int integer4, final int integer5, final int integer6, @Nullable final Direction gc, final int integer8) {
        if (integer8 > 50) {
            return null;
        }
        if (Math.abs(integer4 - m.getBoundingBox().x0) > 112 || Math.abs(integer6 - m.getBoundingBox().z0) > 112) {
            return null;
        }
        final StructurePiece crr9 = generatePieceFromSmallDoor(m, list, random, integer4, integer5, integer6, gc, integer8 + 1);
        if (crr9 != null) {
            list.add(crr9);
            m.pendingChildren.add(crr9);
        }
        return crr9;
    }
    
    static {
        STRONGHOLD_PIECE_WEIGHTS = new PieceWeight[] { new PieceWeight(Straight.class, 40, 0), new PieceWeight(PrisonHall.class, 5, 5), new PieceWeight(LeftTurn.class, 20, 0), new PieceWeight(RightTurn.class, 20, 0), new PieceWeight(RoomCrossing.class, 10, 6), new PieceWeight(StraightStairsDown.class, 5, 5), new PieceWeight(StairsDown.class, 5, 5), new PieceWeight(FiveCrossing.class, 5, 4), new PieceWeight(ChestCorridor.class, 5, 4), new PieceWeight(Library.class, 10, 2) {
                @Override
                public boolean doPlace(final int integer) {
                    return super.doPlace(integer) && integer > 4;
                }
            }, new PieceWeight(PortalRoom.class, 20, 1) {
                @Override
                public boolean doPlace(final int integer) {
                    return super.doPlace(integer) && integer > 5;
                }
            } };
        SMOOTH_STONE_SELECTOR = new SmoothStoneSelector();
    }
    
    static class PieceWeight {
        public final Class<? extends StrongholdPiece> pieceClass;
        public final int weight;
        public int placeCount;
        public final int maxPlaceCount;
        
        public PieceWeight(final Class<? extends StrongholdPiece> class1, final int integer2, final int integer3) {
            this.pieceClass = class1;
            this.weight = integer2;
            this.maxPlaceCount = integer3;
        }
        
        public boolean doPlace(final int integer) {
            return this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount;
        }
        
        public boolean isValid() {
            return this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount;
        }
    }
    
    abstract static class StrongholdPiece extends StructurePiece {
        protected SmallDoorType entryDoor;
        
        protected StrongholdPiece(final StructurePieceType cky, final int integer) {
            super(cky, integer);
            this.entryDoor = SmallDoorType.OPENING;
        }
        
        public StrongholdPiece(final StructurePieceType cky, final CompoundTag md) {
            super(cky, md);
            this.entryDoor = SmallDoorType.OPENING;
            this.entryDoor = SmallDoorType.valueOf(md.getString("EntryDoor"));
        }
        
        @Override
        protected void addAdditionalSaveData(final CompoundTag md) {
            md.putString("EntryDoor", this.entryDoor.name());
        }
        
        protected void generateSmallDoor(final WorldGenLevel bso, final Random random, final BoundingBox cqx, final SmallDoorType a, final int integer5, final int integer6, final int integer7) {
            switch (a) {
                case OPENING: {
                    this.generateBox(bso, cqx, integer5, integer6, integer7, integer5 + 3 - 1, integer6 + 3 - 1, integer7, StrongholdPiece.CAVE_AIR, StrongholdPiece.CAVE_AIR, false);
                    break;
                }
                case WOOD_DOOR: {
                    this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), integer5, integer6, integer7, cqx);
                    this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), integer5, integer6 + 1, integer7, cqx);
                    this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), integer5, integer6 + 2, integer7, cqx);
                    this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), integer5 + 1, integer6 + 2, integer7, cqx);
                    this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), integer5 + 2, integer6 + 2, integer7, cqx);
                    this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), integer5 + 2, integer6 + 1, integer7, cqx);
                    this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), integer5 + 2, integer6, integer7, cqx);
                    this.placeBlock(bso, Blocks.OAK_DOOR.defaultBlockState(), integer5 + 1, integer6, integer7, cqx);
                    this.placeBlock(bso, ((StateHolder<O, BlockState>)Blocks.OAK_DOOR.defaultBlockState()).<DoubleBlockHalf, DoubleBlockHalf>setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), integer5 + 1, integer6 + 1, integer7, cqx);
                    break;
                }
                case GRATES: {
                    this.placeBlock(bso, Blocks.CAVE_AIR.defaultBlockState(), integer5 + 1, integer6, integer7, cqx);
                    this.placeBlock(bso, Blocks.CAVE_AIR.defaultBlockState(), integer5 + 1, integer6 + 1, integer7, cqx);
                    this.placeBlock(bso, ((StateHolder<O, BlockState>)Blocks.IRON_BARS.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)IronBarsBlock.WEST, true), integer5, integer6, integer7, cqx);
                    this.placeBlock(bso, ((StateHolder<O, BlockState>)Blocks.IRON_BARS.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)IronBarsBlock.WEST, true), integer5, integer6 + 1, integer7, cqx);
                    this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.IRON_BARS.defaultBlockState()).setValue((Property<Comparable>)IronBarsBlock.EAST, true)).<Comparable, Boolean>setValue((Property<Comparable>)IronBarsBlock.WEST, true), integer5, integer6 + 2, integer7, cqx);
                    this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.IRON_BARS.defaultBlockState()).setValue((Property<Comparable>)IronBarsBlock.EAST, true)).<Comparable, Boolean>setValue((Property<Comparable>)IronBarsBlock.WEST, true), integer5 + 1, integer6 + 2, integer7, cqx);
                    this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.IRON_BARS.defaultBlockState()).setValue((Property<Comparable>)IronBarsBlock.EAST, true)).<Comparable, Boolean>setValue((Property<Comparable>)IronBarsBlock.WEST, true), integer5 + 2, integer6 + 2, integer7, cqx);
                    this.placeBlock(bso, ((StateHolder<O, BlockState>)Blocks.IRON_BARS.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)IronBarsBlock.EAST, true), integer5 + 2, integer6 + 1, integer7, cqx);
                    this.placeBlock(bso, ((StateHolder<O, BlockState>)Blocks.IRON_BARS.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)IronBarsBlock.EAST, true), integer5 + 2, integer6, integer7, cqx);
                    break;
                }
                case IRON_DOOR: {
                    this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), integer5, integer6, integer7, cqx);
                    this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), integer5, integer6 + 1, integer7, cqx);
                    this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), integer5, integer6 + 2, integer7, cqx);
                    this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), integer5 + 1, integer6 + 2, integer7, cqx);
                    this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), integer5 + 2, integer6 + 2, integer7, cqx);
                    this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), integer5 + 2, integer6 + 1, integer7, cqx);
                    this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), integer5 + 2, integer6, integer7, cqx);
                    this.placeBlock(bso, Blocks.IRON_DOOR.defaultBlockState(), integer5 + 1, integer6, integer7, cqx);
                    this.placeBlock(bso, ((StateHolder<O, BlockState>)Blocks.IRON_DOOR.defaultBlockState()).<DoubleBlockHalf, DoubleBlockHalf>setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), integer5 + 1, integer6 + 1, integer7, cqx);
                    this.placeBlock(bso, ((StateHolder<O, BlockState>)Blocks.STONE_BUTTON.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)ButtonBlock.FACING, Direction.NORTH), integer5 + 2, integer6 + 1, integer7 + 1, cqx);
                    this.placeBlock(bso, ((StateHolder<O, BlockState>)Blocks.STONE_BUTTON.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)ButtonBlock.FACING, Direction.SOUTH), integer5 + 2, integer6 + 1, integer7 - 1, cqx);
                    break;
                }
            }
        }
        
        protected SmallDoorType randomSmallDoor(final Random random) {
            final int integer3 = random.nextInt(5);
            switch (integer3) {
                default: {
                    return SmallDoorType.OPENING;
                }
                case 2: {
                    return SmallDoorType.WOOD_DOOR;
                }
                case 3: {
                    return SmallDoorType.GRATES;
                }
                case 4: {
                    return SmallDoorType.IRON_DOOR;
                }
            }
        }
        
        @Nullable
        protected StructurePiece generateSmallDoorChildForward(final StartPiece m, final List<StructurePiece> list, final Random random, final int integer4, final int integer5) {
            final Direction gc7 = this.getOrientation();
            if (gc7 != null) {
                switch (gc7) {
                    case NORTH: {
                        return generateAndAddPiece(m, list, random, this.boundingBox.x0 + integer4, this.boundingBox.y0 + integer5, this.boundingBox.z0 - 1, gc7, this.getGenDepth());
                    }
                    case SOUTH: {
                        return generateAndAddPiece(m, list, random, this.boundingBox.x0 + integer4, this.boundingBox.y0 + integer5, this.boundingBox.z1 + 1, gc7, this.getGenDepth());
                    }
                    case WEST: {
                        return generateAndAddPiece(m, list, random, this.boundingBox.x0 - 1, this.boundingBox.y0 + integer5, this.boundingBox.z0 + integer4, gc7, this.getGenDepth());
                    }
                    case EAST: {
                        return generateAndAddPiece(m, list, random, this.boundingBox.x1 + 1, this.boundingBox.y0 + integer5, this.boundingBox.z0 + integer4, gc7, this.getGenDepth());
                    }
                }
            }
            return null;
        }
        
        @Nullable
        protected StructurePiece generateSmallDoorChildLeft(final StartPiece m, final List<StructurePiece> list, final Random random, final int integer4, final int integer5) {
            final Direction gc7 = this.getOrientation();
            if (gc7 != null) {
                switch (gc7) {
                    case NORTH: {
                        return generateAndAddPiece(m, list, random, this.boundingBox.x0 - 1, this.boundingBox.y0 + integer4, this.boundingBox.z0 + integer5, Direction.WEST, this.getGenDepth());
                    }
                    case SOUTH: {
                        return generateAndAddPiece(m, list, random, this.boundingBox.x0 - 1, this.boundingBox.y0 + integer4, this.boundingBox.z0 + integer5, Direction.WEST, this.getGenDepth());
                    }
                    case WEST: {
                        return generateAndAddPiece(m, list, random, this.boundingBox.x0 + integer5, this.boundingBox.y0 + integer4, this.boundingBox.z0 - 1, Direction.NORTH, this.getGenDepth());
                    }
                    case EAST: {
                        return generateAndAddPiece(m, list, random, this.boundingBox.x0 + integer5, this.boundingBox.y0 + integer4, this.boundingBox.z0 - 1, Direction.NORTH, this.getGenDepth());
                    }
                }
            }
            return null;
        }
        
        @Nullable
        protected StructurePiece generateSmallDoorChildRight(final StartPiece m, final List<StructurePiece> list, final Random random, final int integer4, final int integer5) {
            final Direction gc7 = this.getOrientation();
            if (gc7 != null) {
                switch (gc7) {
                    case NORTH: {
                        return generateAndAddPiece(m, list, random, this.boundingBox.x1 + 1, this.boundingBox.y0 + integer4, this.boundingBox.z0 + integer5, Direction.EAST, this.getGenDepth());
                    }
                    case SOUTH: {
                        return generateAndAddPiece(m, list, random, this.boundingBox.x1 + 1, this.boundingBox.y0 + integer4, this.boundingBox.z0 + integer5, Direction.EAST, this.getGenDepth());
                    }
                    case WEST: {
                        return generateAndAddPiece(m, list, random, this.boundingBox.x0 + integer5, this.boundingBox.y0 + integer4, this.boundingBox.z1 + 1, Direction.SOUTH, this.getGenDepth());
                    }
                    case EAST: {
                        return generateAndAddPiece(m, list, random, this.boundingBox.x0 + integer5, this.boundingBox.y0 + integer4, this.boundingBox.z1 + 1, Direction.SOUTH, this.getGenDepth());
                    }
                }
            }
            return null;
        }
        
        protected static boolean isOkBox(final BoundingBox cqx) {
            return cqx != null && cqx.y0 > 10;
        }
        
        public enum SmallDoorType {
            OPENING, 
            WOOD_DOOR, 
            GRATES, 
            IRON_DOOR;
        }
    }
    
    public static class FillerCorridor extends StrongholdPiece {
        private final int steps;
        
        public FillerCorridor(final int integer, final BoundingBox cqx, final Direction gc) {
            super(StructurePieceType.STRONGHOLD_FILLER_CORRIDOR, integer);
            this.setOrientation(gc);
            this.boundingBox = cqx;
            this.steps = ((gc == Direction.NORTH || gc == Direction.SOUTH) ? cqx.getZSpan() : cqx.getXSpan());
        }
        
        public FillerCorridor(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.STRONGHOLD_FILLER_CORRIDOR, md);
            this.steps = md.getInt("Steps");
        }
        
        @Override
        protected void addAdditionalSaveData(final CompoundTag md) {
            super.addAdditionalSaveData(md);
            md.putInt("Steps", this.steps);
        }
        
        public static BoundingBox findPieceBox(final List<StructurePiece> list, final Random random, final int integer3, final int integer4, final int integer5, final Direction gc) {
            final int integer6 = 3;
            BoundingBox cqx8 = BoundingBox.orientBox(integer3, integer4, integer5, -1, -1, 0, 5, 5, 4, gc);
            final StructurePiece crr9 = StructurePiece.findCollisionPiece(list, cqx8);
            if (crr9 == null) {
                return null;
            }
            if (crr9.getBoundingBox().y0 == cqx8.y0) {
                for (int integer7 = 3; integer7 >= 1; --integer7) {
                    cqx8 = BoundingBox.orientBox(integer3, integer4, integer5, -1, -1, 0, 5, 5, integer7 - 1, gc);
                    if (!crr9.getBoundingBox().intersects(cqx8)) {
                        return BoundingBox.orientBox(integer3, integer4, integer5, -1, -1, 0, 5, 5, integer7, gc);
                    }
                }
            }
            return null;
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            for (int integer9 = 0; integer9 < this.steps; ++integer9) {
                this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 0, 0, integer9, cqx);
                this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 1, 0, integer9, cqx);
                this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 2, 0, integer9, cqx);
                this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 3, 0, integer9, cqx);
                this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 4, 0, integer9, cqx);
                for (int integer10 = 1; integer10 <= 3; ++integer10) {
                    this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 0, integer10, integer9, cqx);
                    this.placeBlock(bso, Blocks.CAVE_AIR.defaultBlockState(), 1, integer10, integer9, cqx);
                    this.placeBlock(bso, Blocks.CAVE_AIR.defaultBlockState(), 2, integer10, integer9, cqx);
                    this.placeBlock(bso, Blocks.CAVE_AIR.defaultBlockState(), 3, integer10, integer9, cqx);
                    this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 4, integer10, integer9, cqx);
                }
                this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 0, 4, integer9, cqx);
                this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 1, 4, integer9, cqx);
                this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 2, 4, integer9, cqx);
                this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 3, 4, integer9, cqx);
                this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 4, 4, integer9, cqx);
            }
            return true;
        }
    }
    
    public static class StairsDown extends StrongholdPiece {
        private final boolean isSource;
        
        public StairsDown(final StructurePieceType cky, final int integer2, final Random random, final int integer4, final int integer5) {
            super(cky, integer2);
            this.isSource = true;
            this.setOrientation(Direction.Plane.HORIZONTAL.getRandomDirection(random));
            this.entryDoor = SmallDoorType.OPENING;
            if (this.getOrientation().getAxis() == Direction.Axis.Z) {
                this.boundingBox = new BoundingBox(integer4, 64, integer5, integer4 + 5 - 1, 74, integer5 + 5 - 1);
            }
            else {
                this.boundingBox = new BoundingBox(integer4, 64, integer5, integer4 + 5 - 1, 74, integer5 + 5 - 1);
            }
        }
        
        public StairsDown(final int integer, final Random random, final BoundingBox cqx, final Direction gc) {
            super(StructurePieceType.STRONGHOLD_STAIRS_DOWN, integer);
            this.isSource = false;
            this.setOrientation(gc);
            this.entryDoor = this.randomSmallDoor(random);
            this.boundingBox = cqx;
        }
        
        public StairsDown(final StructurePieceType cky, final CompoundTag md) {
            super(cky, md);
            this.isSource = md.getBoolean("Source");
        }
        
        public StairsDown(final StructureManager cst, final CompoundTag md) {
            this(StructurePieceType.STRONGHOLD_STAIRS_DOWN, md);
        }
        
        @Override
        protected void addAdditionalSaveData(final CompoundTag md) {
            super.addAdditionalSaveData(md);
            md.putBoolean("Source", this.isSource);
        }
        
        @Override
        public void addChildren(final StructurePiece crr, final List<StructurePiece> list, final Random random) {
            if (this.isSource) {
                StrongholdPieces.imposedPiece = FiveCrossing.class;
            }
            this.generateSmallDoorChildForward((StartPiece)crr, list, random, 1, 1);
        }
        
        public static StairsDown createPiece(final List<StructurePiece> list, final Random random, final int integer3, final int integer4, final int integer5, final Direction gc, final int integer7) {
            final BoundingBox cqx8 = BoundingBox.orientBox(integer3, integer4, integer5, -1, -7, 0, 5, 11, 5, gc);
            if (!StrongholdPiece.isOkBox(cqx8) || StructurePiece.findCollisionPiece(list, cqx8) != null) {
                return null;
            }
            return new StairsDown(integer7, random, cqx8, gc);
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            this.generateBox(bso, cqx, 0, 0, 0, 4, 10, 4, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.generateSmallDoor(bso, random, cqx, this.entryDoor, 1, 7, 0);
            this.generateSmallDoor(bso, random, cqx, SmallDoorType.OPENING, 1, 1, 4);
            this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 2, 6, 1, cqx);
            this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 1, 5, 1, cqx);
            this.placeBlock(bso, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 1, 6, 1, cqx);
            this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 1, 5, 2, cqx);
            this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 1, 4, 3, cqx);
            this.placeBlock(bso, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 1, 5, 3, cqx);
            this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 2, 4, 3, cqx);
            this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 3, 3, 3, cqx);
            this.placeBlock(bso, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 3, 4, 3, cqx);
            this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 3, 3, 2, cqx);
            this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 3, 2, 1, cqx);
            this.placeBlock(bso, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 3, 3, 1, cqx);
            this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 2, 2, 1, cqx);
            this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 1, 1, 1, cqx);
            this.placeBlock(bso, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 1, 2, 1, cqx);
            this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 1, 1, 2, cqx);
            this.placeBlock(bso, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 1, 1, 3, cqx);
            return true;
        }
    }
    
    public static class StartPiece extends StairsDown {
        public PieceWeight previousPiece;
        @Nullable
        public PortalRoom portalRoomPiece;
        public final List<StructurePiece> pendingChildren;
        
        public StartPiece(final Random random, final int integer2, final int integer3) {
            super(StructurePieceType.STRONGHOLD_START, 0, random, integer2, integer3);
            this.pendingChildren = (List<StructurePiece>)Lists.newArrayList();
        }
        
        public StartPiece(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.STRONGHOLD_START, md);
            this.pendingChildren = (List<StructurePiece>)Lists.newArrayList();
        }
    }
    
    public static class Straight extends StrongholdPiece {
        private final boolean leftChild;
        private final boolean rightChild;
        
        public Straight(final int integer, final Random random, final BoundingBox cqx, final Direction gc) {
            super(StructurePieceType.STRONGHOLD_STRAIGHT, integer);
            this.setOrientation(gc);
            this.entryDoor = this.randomSmallDoor(random);
            this.boundingBox = cqx;
            this.leftChild = (random.nextInt(2) == 0);
            this.rightChild = (random.nextInt(2) == 0);
        }
        
        public Straight(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.STRONGHOLD_STRAIGHT, md);
            this.leftChild = md.getBoolean("Left");
            this.rightChild = md.getBoolean("Right");
        }
        
        @Override
        protected void addAdditionalSaveData(final CompoundTag md) {
            super.addAdditionalSaveData(md);
            md.putBoolean("Left", this.leftChild);
            md.putBoolean("Right", this.rightChild);
        }
        
        @Override
        public void addChildren(final StructurePiece crr, final List<StructurePiece> list, final Random random) {
            this.generateSmallDoorChildForward((StartPiece)crr, list, random, 1, 1);
            if (this.leftChild) {
                this.generateSmallDoorChildLeft((StartPiece)crr, list, random, 1, 2);
            }
            if (this.rightChild) {
                this.generateSmallDoorChildRight((StartPiece)crr, list, random, 1, 2);
            }
        }
        
        public static Straight createPiece(final List<StructurePiece> list, final Random random, final int integer3, final int integer4, final int integer5, final Direction gc, final int integer7) {
            final BoundingBox cqx8 = BoundingBox.orientBox(integer3, integer4, integer5, -1, -1, 0, 5, 5, 7, gc);
            if (!StrongholdPiece.isOkBox(cqx8) || StructurePiece.findCollisionPiece(list, cqx8) != null) {
                return null;
            }
            return new Straight(integer7, random, cqx8, gc);
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            this.generateBox(bso, cqx, 0, 0, 0, 4, 4, 6, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.generateSmallDoor(bso, random, cqx, this.entryDoor, 1, 1, 0);
            this.generateSmallDoor(bso, random, cqx, SmallDoorType.OPENING, 1, 1, 6);
            final BlockState cee9 = ((StateHolder<O, BlockState>)Blocks.WALL_TORCH.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)WallTorchBlock.FACING, Direction.EAST);
            final BlockState cee10 = ((StateHolder<O, BlockState>)Blocks.WALL_TORCH.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)WallTorchBlock.FACING, Direction.WEST);
            this.maybeGenerateBlock(bso, cqx, random, 0.1f, 1, 2, 1, cee9);
            this.maybeGenerateBlock(bso, cqx, random, 0.1f, 3, 2, 1, cee10);
            this.maybeGenerateBlock(bso, cqx, random, 0.1f, 1, 2, 5, cee9);
            this.maybeGenerateBlock(bso, cqx, random, 0.1f, 3, 2, 5, cee10);
            if (this.leftChild) {
                this.generateBox(bso, cqx, 0, 1, 2, 0, 3, 4, Straight.CAVE_AIR, Straight.CAVE_AIR, false);
            }
            if (this.rightChild) {
                this.generateBox(bso, cqx, 4, 1, 2, 4, 3, 4, Straight.CAVE_AIR, Straight.CAVE_AIR, false);
            }
            return true;
        }
    }
    
    public static class ChestCorridor extends StrongholdPiece {
        private boolean hasPlacedChest;
        
        public ChestCorridor(final int integer, final Random random, final BoundingBox cqx, final Direction gc) {
            super(StructurePieceType.STRONGHOLD_CHEST_CORRIDOR, integer);
            this.setOrientation(gc);
            this.entryDoor = this.randomSmallDoor(random);
            this.boundingBox = cqx;
        }
        
        public ChestCorridor(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.STRONGHOLD_CHEST_CORRIDOR, md);
            this.hasPlacedChest = md.getBoolean("Chest");
        }
        
        @Override
        protected void addAdditionalSaveData(final CompoundTag md) {
            super.addAdditionalSaveData(md);
            md.putBoolean("Chest", this.hasPlacedChest);
        }
        
        @Override
        public void addChildren(final StructurePiece crr, final List<StructurePiece> list, final Random random) {
            this.generateSmallDoorChildForward((StartPiece)crr, list, random, 1, 1);
        }
        
        public static ChestCorridor createPiece(final List<StructurePiece> list, final Random random, final int integer3, final int integer4, final int integer5, final Direction gc, final int integer7) {
            final BoundingBox cqx8 = BoundingBox.orientBox(integer3, integer4, integer5, -1, -1, 0, 5, 5, 7, gc);
            if (!StrongholdPiece.isOkBox(cqx8) || StructurePiece.findCollisionPiece(list, cqx8) != null) {
                return null;
            }
            return new ChestCorridor(integer7, random, cqx8, gc);
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            this.generateBox(bso, cqx, 0, 0, 0, 4, 4, 6, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.generateSmallDoor(bso, random, cqx, this.entryDoor, 1, 1, 0);
            this.generateSmallDoor(bso, random, cqx, SmallDoorType.OPENING, 1, 1, 6);
            this.generateBox(bso, cqx, 3, 1, 2, 3, 1, 4, Blocks.STONE_BRICKS.defaultBlockState(), Blocks.STONE_BRICKS.defaultBlockState(), false);
            this.placeBlock(bso, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3, 1, 1, cqx);
            this.placeBlock(bso, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3, 1, 5, cqx);
            this.placeBlock(bso, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3, 2, 2, cqx);
            this.placeBlock(bso, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3, 2, 4, cqx);
            for (int integer9 = 2; integer9 <= 4; ++integer9) {
                this.placeBlock(bso, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 2, 1, integer9, cqx);
            }
            if (!this.hasPlacedChest && cqx.isInside(new BlockPos(this.getWorldX(3, 3), this.getWorldY(2), this.getWorldZ(3, 3)))) {
                this.hasPlacedChest = true;
                this.createChest(bso, cqx, random, 3, 2, 3, BuiltInLootTables.STRONGHOLD_CORRIDOR);
            }
            return true;
        }
    }
    
    public static class StraightStairsDown extends StrongholdPiece {
        public StraightStairsDown(final int integer, final Random random, final BoundingBox cqx, final Direction gc) {
            super(StructurePieceType.STRONGHOLD_STRAIGHT_STAIRS_DOWN, integer);
            this.setOrientation(gc);
            this.entryDoor = this.randomSmallDoor(random);
            this.boundingBox = cqx;
        }
        
        public StraightStairsDown(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.STRONGHOLD_STRAIGHT_STAIRS_DOWN, md);
        }
        
        @Override
        public void addChildren(final StructurePiece crr, final List<StructurePiece> list, final Random random) {
            this.generateSmallDoorChildForward((StartPiece)crr, list, random, 1, 1);
        }
        
        public static StraightStairsDown createPiece(final List<StructurePiece> list, final Random random, final int integer3, final int integer4, final int integer5, final Direction gc, final int integer7) {
            final BoundingBox cqx8 = BoundingBox.orientBox(integer3, integer4, integer5, -1, -7, 0, 5, 11, 8, gc);
            if (!StrongholdPiece.isOkBox(cqx8) || StructurePiece.findCollisionPiece(list, cqx8) != null) {
                return null;
            }
            return new StraightStairsDown(integer7, random, cqx8, gc);
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            this.generateBox(bso, cqx, 0, 0, 0, 4, 10, 7, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.generateSmallDoor(bso, random, cqx, this.entryDoor, 1, 7, 0);
            this.generateSmallDoor(bso, random, cqx, SmallDoorType.OPENING, 1, 1, 7);
            final BlockState cee9 = ((StateHolder<O, BlockState>)Blocks.COBBLESTONE_STAIRS.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)StairBlock.FACING, Direction.SOUTH);
            for (int integer10 = 0; integer10 < 6; ++integer10) {
                this.placeBlock(bso, cee9, 1, 6 - integer10, 1 + integer10, cqx);
                this.placeBlock(bso, cee9, 2, 6 - integer10, 1 + integer10, cqx);
                this.placeBlock(bso, cee9, 3, 6 - integer10, 1 + integer10, cqx);
                if (integer10 < 5) {
                    this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 1, 5 - integer10, 1 + integer10, cqx);
                    this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 2, 5 - integer10, 1 + integer10, cqx);
                    this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 3, 5 - integer10, 1 + integer10, cqx);
                }
            }
            return true;
        }
    }
    
    public abstract static class Turn extends StrongholdPiece {
        protected Turn(final StructurePieceType cky, final int integer) {
            super(cky, integer);
        }
        
        public Turn(final StructurePieceType cky, final CompoundTag md) {
            super(cky, md);
        }
    }
    
    public static class LeftTurn extends Turn {
        public LeftTurn(final int integer, final Random random, final BoundingBox cqx, final Direction gc) {
            super(StructurePieceType.STRONGHOLD_LEFT_TURN, integer);
            this.setOrientation(gc);
            this.entryDoor = this.randomSmallDoor(random);
            this.boundingBox = cqx;
        }
        
        public LeftTurn(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.STRONGHOLD_LEFT_TURN, md);
        }
        
        @Override
        public void addChildren(final StructurePiece crr, final List<StructurePiece> list, final Random random) {
            final Direction gc5 = this.getOrientation();
            if (gc5 == Direction.NORTH || gc5 == Direction.EAST) {
                this.generateSmallDoorChildLeft((StartPiece)crr, list, random, 1, 1);
            }
            else {
                this.generateSmallDoorChildRight((StartPiece)crr, list, random, 1, 1);
            }
        }
        
        public static LeftTurn createPiece(final List<StructurePiece> list, final Random random, final int integer3, final int integer4, final int integer5, final Direction gc, final int integer7) {
            final BoundingBox cqx8 = BoundingBox.orientBox(integer3, integer4, integer5, -1, -1, 0, 5, 5, 5, gc);
            if (!StrongholdPiece.isOkBox(cqx8) || StructurePiece.findCollisionPiece(list, cqx8) != null) {
                return null;
            }
            return new LeftTurn(integer7, random, cqx8, gc);
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            this.generateBox(bso, cqx, 0, 0, 0, 4, 4, 4, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.generateSmallDoor(bso, random, cqx, this.entryDoor, 1, 1, 0);
            final Direction gc9 = this.getOrientation();
            if (gc9 == Direction.NORTH || gc9 == Direction.EAST) {
                this.generateBox(bso, cqx, 0, 1, 1, 0, 3, 3, LeftTurn.CAVE_AIR, LeftTurn.CAVE_AIR, false);
            }
            else {
                this.generateBox(bso, cqx, 4, 1, 1, 4, 3, 3, LeftTurn.CAVE_AIR, LeftTurn.CAVE_AIR, false);
            }
            return true;
        }
    }
    
    public static class RightTurn extends Turn {
        public RightTurn(final int integer, final Random random, final BoundingBox cqx, final Direction gc) {
            super(StructurePieceType.STRONGHOLD_RIGHT_TURN, integer);
            this.setOrientation(gc);
            this.entryDoor = this.randomSmallDoor(random);
            this.boundingBox = cqx;
        }
        
        public RightTurn(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.STRONGHOLD_RIGHT_TURN, md);
        }
        
        @Override
        public void addChildren(final StructurePiece crr, final List<StructurePiece> list, final Random random) {
            final Direction gc5 = this.getOrientation();
            if (gc5 == Direction.NORTH || gc5 == Direction.EAST) {
                this.generateSmallDoorChildRight((StartPiece)crr, list, random, 1, 1);
            }
            else {
                this.generateSmallDoorChildLeft((StartPiece)crr, list, random, 1, 1);
            }
        }
        
        public static RightTurn createPiece(final List<StructurePiece> list, final Random random, final int integer3, final int integer4, final int integer5, final Direction gc, final int integer7) {
            final BoundingBox cqx8 = BoundingBox.orientBox(integer3, integer4, integer5, -1, -1, 0, 5, 5, 5, gc);
            if (!StrongholdPiece.isOkBox(cqx8) || StructurePiece.findCollisionPiece(list, cqx8) != null) {
                return null;
            }
            return new RightTurn(integer7, random, cqx8, gc);
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            this.generateBox(bso, cqx, 0, 0, 0, 4, 4, 4, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.generateSmallDoor(bso, random, cqx, this.entryDoor, 1, 1, 0);
            final Direction gc9 = this.getOrientation();
            if (gc9 == Direction.NORTH || gc9 == Direction.EAST) {
                this.generateBox(bso, cqx, 4, 1, 1, 4, 3, 3, RightTurn.CAVE_AIR, RightTurn.CAVE_AIR, false);
            }
            else {
                this.generateBox(bso, cqx, 0, 1, 1, 0, 3, 3, RightTurn.CAVE_AIR, RightTurn.CAVE_AIR, false);
            }
            return true;
        }
    }
    
    public static class RoomCrossing extends StrongholdPiece {
        protected final int type;
        
        public RoomCrossing(final int integer, final Random random, final BoundingBox cqx, final Direction gc) {
            super(StructurePieceType.STRONGHOLD_ROOM_CROSSING, integer);
            this.setOrientation(gc);
            this.entryDoor = this.randomSmallDoor(random);
            this.boundingBox = cqx;
            this.type = random.nextInt(5);
        }
        
        public RoomCrossing(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.STRONGHOLD_ROOM_CROSSING, md);
            this.type = md.getInt("Type");
        }
        
        @Override
        protected void addAdditionalSaveData(final CompoundTag md) {
            super.addAdditionalSaveData(md);
            md.putInt("Type", this.type);
        }
        
        @Override
        public void addChildren(final StructurePiece crr, final List<StructurePiece> list, final Random random) {
            this.generateSmallDoorChildForward((StartPiece)crr, list, random, 4, 1);
            this.generateSmallDoorChildLeft((StartPiece)crr, list, random, 1, 4);
            this.generateSmallDoorChildRight((StartPiece)crr, list, random, 1, 4);
        }
        
        public static RoomCrossing createPiece(final List<StructurePiece> list, final Random random, final int integer3, final int integer4, final int integer5, final Direction gc, final int integer7) {
            final BoundingBox cqx8 = BoundingBox.orientBox(integer3, integer4, integer5, -4, -1, 0, 11, 7, 11, gc);
            if (!StrongholdPiece.isOkBox(cqx8) || StructurePiece.findCollisionPiece(list, cqx8) != null) {
                return null;
            }
            return new RoomCrossing(integer7, random, cqx8, gc);
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            this.generateBox(bso, cqx, 0, 0, 0, 10, 6, 10, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.generateSmallDoor(bso, random, cqx, this.entryDoor, 4, 1, 0);
            this.generateBox(bso, cqx, 4, 1, 10, 6, 3, 10, RoomCrossing.CAVE_AIR, RoomCrossing.CAVE_AIR, false);
            this.generateBox(bso, cqx, 0, 1, 4, 0, 3, 6, RoomCrossing.CAVE_AIR, RoomCrossing.CAVE_AIR, false);
            this.generateBox(bso, cqx, 10, 1, 4, 10, 3, 6, RoomCrossing.CAVE_AIR, RoomCrossing.CAVE_AIR, false);
            switch (this.type) {
                case 0: {
                    this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 5, 1, 5, cqx);
                    this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 5, 2, 5, cqx);
                    this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 5, 3, 5, cqx);
                    this.placeBlock(bso, ((StateHolder<O, BlockState>)Blocks.WALL_TORCH.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)WallTorchBlock.FACING, Direction.WEST), 4, 3, 5, cqx);
                    this.placeBlock(bso, ((StateHolder<O, BlockState>)Blocks.WALL_TORCH.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)WallTorchBlock.FACING, Direction.EAST), 6, 3, 5, cqx);
                    this.placeBlock(bso, ((StateHolder<O, BlockState>)Blocks.WALL_TORCH.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)WallTorchBlock.FACING, Direction.SOUTH), 5, 3, 4, cqx);
                    this.placeBlock(bso, ((StateHolder<O, BlockState>)Blocks.WALL_TORCH.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)WallTorchBlock.FACING, Direction.NORTH), 5, 3, 6, cqx);
                    this.placeBlock(bso, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 4, 1, 4, cqx);
                    this.placeBlock(bso, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 4, 1, 5, cqx);
                    this.placeBlock(bso, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 4, 1, 6, cqx);
                    this.placeBlock(bso, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 6, 1, 4, cqx);
                    this.placeBlock(bso, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 6, 1, 5, cqx);
                    this.placeBlock(bso, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 6, 1, 6, cqx);
                    this.placeBlock(bso, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 5, 1, 4, cqx);
                    this.placeBlock(bso, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 5, 1, 6, cqx);
                    break;
                }
                case 1: {
                    for (int integer9 = 0; integer9 < 5; ++integer9) {
                        this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 3, 1, 3 + integer9, cqx);
                        this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 7, 1, 3 + integer9, cqx);
                        this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 3 + integer9, 1, 3, cqx);
                        this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 3 + integer9, 1, 7, cqx);
                    }
                    this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 5, 1, 5, cqx);
                    this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 5, 2, 5, cqx);
                    this.placeBlock(bso, Blocks.STONE_BRICKS.defaultBlockState(), 5, 3, 5, cqx);
                    this.placeBlock(bso, Blocks.WATER.defaultBlockState(), 5, 4, 5, cqx);
                    break;
                }
                case 2: {
                    for (int integer9 = 1; integer9 <= 9; ++integer9) {
                        this.placeBlock(bso, Blocks.COBBLESTONE.defaultBlockState(), 1, 3, integer9, cqx);
                        this.placeBlock(bso, Blocks.COBBLESTONE.defaultBlockState(), 9, 3, integer9, cqx);
                    }
                    for (int integer9 = 1; integer9 <= 9; ++integer9) {
                        this.placeBlock(bso, Blocks.COBBLESTONE.defaultBlockState(), integer9, 3, 1, cqx);
                        this.placeBlock(bso, Blocks.COBBLESTONE.defaultBlockState(), integer9, 3, 9, cqx);
                    }
                    this.placeBlock(bso, Blocks.COBBLESTONE.defaultBlockState(), 5, 1, 4, cqx);
                    this.placeBlock(bso, Blocks.COBBLESTONE.defaultBlockState(), 5, 1, 6, cqx);
                    this.placeBlock(bso, Blocks.COBBLESTONE.defaultBlockState(), 5, 3, 4, cqx);
                    this.placeBlock(bso, Blocks.COBBLESTONE.defaultBlockState(), 5, 3, 6, cqx);
                    this.placeBlock(bso, Blocks.COBBLESTONE.defaultBlockState(), 4, 1, 5, cqx);
                    this.placeBlock(bso, Blocks.COBBLESTONE.defaultBlockState(), 6, 1, 5, cqx);
                    this.placeBlock(bso, Blocks.COBBLESTONE.defaultBlockState(), 4, 3, 5, cqx);
                    this.placeBlock(bso, Blocks.COBBLESTONE.defaultBlockState(), 6, 3, 5, cqx);
                    for (int integer9 = 1; integer9 <= 3; ++integer9) {
                        this.placeBlock(bso, Blocks.COBBLESTONE.defaultBlockState(), 4, integer9, 4, cqx);
                        this.placeBlock(bso, Blocks.COBBLESTONE.defaultBlockState(), 6, integer9, 4, cqx);
                        this.placeBlock(bso, Blocks.COBBLESTONE.defaultBlockState(), 4, integer9, 6, cqx);
                        this.placeBlock(bso, Blocks.COBBLESTONE.defaultBlockState(), 6, integer9, 6, cqx);
                    }
                    this.placeBlock(bso, Blocks.TORCH.defaultBlockState(), 5, 3, 5, cqx);
                    for (int integer9 = 2; integer9 <= 8; ++integer9) {
                        this.placeBlock(bso, Blocks.OAK_PLANKS.defaultBlockState(), 2, 3, integer9, cqx);
                        this.placeBlock(bso, Blocks.OAK_PLANKS.defaultBlockState(), 3, 3, integer9, cqx);
                        if (integer9 <= 3 || integer9 >= 7) {
                            this.placeBlock(bso, Blocks.OAK_PLANKS.defaultBlockState(), 4, 3, integer9, cqx);
                            this.placeBlock(bso, Blocks.OAK_PLANKS.defaultBlockState(), 5, 3, integer9, cqx);
                            this.placeBlock(bso, Blocks.OAK_PLANKS.defaultBlockState(), 6, 3, integer9, cqx);
                        }
                        this.placeBlock(bso, Blocks.OAK_PLANKS.defaultBlockState(), 7, 3, integer9, cqx);
                        this.placeBlock(bso, Blocks.OAK_PLANKS.defaultBlockState(), 8, 3, integer9, cqx);
                    }
                    final BlockState cee9 = ((StateHolder<O, BlockState>)Blocks.LADDER.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)LadderBlock.FACING, Direction.WEST);
                    this.placeBlock(bso, cee9, 9, 1, 3, cqx);
                    this.placeBlock(bso, cee9, 9, 2, 3, cqx);
                    this.placeBlock(bso, cee9, 9, 3, 3, cqx);
                    this.createChest(bso, cqx, random, 3, 4, 8, BuiltInLootTables.STRONGHOLD_CROSSING);
                    break;
                }
            }
            return true;
        }
    }
    
    public static class PrisonHall extends StrongholdPiece {
        public PrisonHall(final int integer, final Random random, final BoundingBox cqx, final Direction gc) {
            super(StructurePieceType.STRONGHOLD_PRISON_HALL, integer);
            this.setOrientation(gc);
            this.entryDoor = this.randomSmallDoor(random);
            this.boundingBox = cqx;
        }
        
        public PrisonHall(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.STRONGHOLD_PRISON_HALL, md);
        }
        
        @Override
        public void addChildren(final StructurePiece crr, final List<StructurePiece> list, final Random random) {
            this.generateSmallDoorChildForward((StartPiece)crr, list, random, 1, 1);
        }
        
        public static PrisonHall createPiece(final List<StructurePiece> list, final Random random, final int integer3, final int integer4, final int integer5, final Direction gc, final int integer7) {
            final BoundingBox cqx8 = BoundingBox.orientBox(integer3, integer4, integer5, -1, -1, 0, 9, 5, 11, gc);
            if (!StrongholdPiece.isOkBox(cqx8) || StructurePiece.findCollisionPiece(list, cqx8) != null) {
                return null;
            }
            return new PrisonHall(integer7, random, cqx8, gc);
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            this.generateBox(bso, cqx, 0, 0, 0, 8, 4, 10, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.generateSmallDoor(bso, random, cqx, this.entryDoor, 1, 1, 0);
            this.generateBox(bso, cqx, 1, 1, 10, 3, 3, 10, PrisonHall.CAVE_AIR, PrisonHall.CAVE_AIR, false);
            this.generateBox(bso, cqx, 4, 1, 1, 4, 3, 1, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.generateBox(bso, cqx, 4, 1, 3, 4, 3, 3, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.generateBox(bso, cqx, 4, 1, 7, 4, 3, 7, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.generateBox(bso, cqx, 4, 1, 9, 4, 3, 9, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            for (int integer9 = 1; integer9 <= 3; ++integer9) {
                this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.IRON_BARS.defaultBlockState()).setValue((Property<Comparable>)IronBarsBlock.NORTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)IronBarsBlock.SOUTH, true), 4, integer9, 4, cqx);
                this.placeBlock(bso, ((((StateHolder<O, BlockState>)Blocks.IRON_BARS.defaultBlockState()).setValue((Property<Comparable>)IronBarsBlock.NORTH, true)).setValue((Property<Comparable>)IronBarsBlock.SOUTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)IronBarsBlock.EAST, true), 4, integer9, 5, cqx);
                this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.IRON_BARS.defaultBlockState()).setValue((Property<Comparable>)IronBarsBlock.NORTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)IronBarsBlock.SOUTH, true), 4, integer9, 6, cqx);
                this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.IRON_BARS.defaultBlockState()).setValue((Property<Comparable>)IronBarsBlock.WEST, true)).<Comparable, Boolean>setValue((Property<Comparable>)IronBarsBlock.EAST, true), 5, integer9, 5, cqx);
                this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.IRON_BARS.defaultBlockState()).setValue((Property<Comparable>)IronBarsBlock.WEST, true)).<Comparable, Boolean>setValue((Property<Comparable>)IronBarsBlock.EAST, true), 6, integer9, 5, cqx);
                this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.IRON_BARS.defaultBlockState()).setValue((Property<Comparable>)IronBarsBlock.WEST, true)).<Comparable, Boolean>setValue((Property<Comparable>)IronBarsBlock.EAST, true), 7, integer9, 5, cqx);
            }
            this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.IRON_BARS.defaultBlockState()).setValue((Property<Comparable>)IronBarsBlock.NORTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)IronBarsBlock.SOUTH, true), 4, 3, 2, cqx);
            this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.IRON_BARS.defaultBlockState()).setValue((Property<Comparable>)IronBarsBlock.NORTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)IronBarsBlock.SOUTH, true), 4, 3, 8, cqx);
            final BlockState cee9 = ((StateHolder<O, BlockState>)Blocks.IRON_DOOR.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)DoorBlock.FACING, Direction.WEST);
            final BlockState cee10 = (((StateHolder<O, BlockState>)Blocks.IRON_DOOR.defaultBlockState()).setValue((Property<Comparable>)DoorBlock.FACING, Direction.WEST)).<DoubleBlockHalf, DoubleBlockHalf>setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER);
            this.placeBlock(bso, cee9, 4, 1, 2, cqx);
            this.placeBlock(bso, cee10, 4, 2, 2, cqx);
            this.placeBlock(bso, cee9, 4, 1, 8, cqx);
            this.placeBlock(bso, cee10, 4, 2, 8, cqx);
            return true;
        }
    }
    
    public static class Library extends StrongholdPiece {
        private final boolean isTall;
        
        public Library(final int integer, final Random random, final BoundingBox cqx, final Direction gc) {
            super(StructurePieceType.STRONGHOLD_LIBRARY, integer);
            this.setOrientation(gc);
            this.entryDoor = this.randomSmallDoor(random);
            this.boundingBox = cqx;
            this.isTall = (cqx.getYSpan() > 6);
        }
        
        public Library(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.STRONGHOLD_LIBRARY, md);
            this.isTall = md.getBoolean("Tall");
        }
        
        @Override
        protected void addAdditionalSaveData(final CompoundTag md) {
            super.addAdditionalSaveData(md);
            md.putBoolean("Tall", this.isTall);
        }
        
        public static Library createPiece(final List<StructurePiece> list, final Random random, final int integer3, final int integer4, final int integer5, final Direction gc, final int integer7) {
            BoundingBox cqx8 = BoundingBox.orientBox(integer3, integer4, integer5, -4, -1, 0, 14, 11, 15, gc);
            if (!StrongholdPiece.isOkBox(cqx8) || StructurePiece.findCollisionPiece(list, cqx8) != null) {
                cqx8 = BoundingBox.orientBox(integer3, integer4, integer5, -4, -1, 0, 14, 6, 15, gc);
                if (!StrongholdPiece.isOkBox(cqx8) || StructurePiece.findCollisionPiece(list, cqx8) != null) {
                    return null;
                }
            }
            return new Library(integer7, random, cqx8, gc);
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            int integer9 = 11;
            if (!this.isTall) {
                integer9 = 6;
            }
            this.generateBox(bso, cqx, 0, 0, 0, 13, integer9 - 1, 14, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.generateSmallDoor(bso, random, cqx, this.entryDoor, 4, 1, 0);
            this.generateMaybeBox(bso, cqx, random, 0.07f, 2, 1, 1, 11, 4, 13, Blocks.COBWEB.defaultBlockState(), Blocks.COBWEB.defaultBlockState(), false, false);
            final int integer10 = 1;
            final int integer11 = 12;
            for (int integer12 = 1; integer12 <= 13; ++integer12) {
                if ((integer12 - 1) % 4 == 0) {
                    this.generateBox(bso, cqx, 1, 1, integer12, 1, 4, integer12, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
                    this.generateBox(bso, cqx, 12, 1, integer12, 12, 4, integer12, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
                    this.placeBlock(bso, ((StateHolder<O, BlockState>)Blocks.WALL_TORCH.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)WallTorchBlock.FACING, Direction.EAST), 2, 3, integer12, cqx);
                    this.placeBlock(bso, ((StateHolder<O, BlockState>)Blocks.WALL_TORCH.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)WallTorchBlock.FACING, Direction.WEST), 11, 3, integer12, cqx);
                    if (this.isTall) {
                        this.generateBox(bso, cqx, 1, 6, integer12, 1, 9, integer12, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
                        this.generateBox(bso, cqx, 12, 6, integer12, 12, 9, integer12, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
                    }
                }
                else {
                    this.generateBox(bso, cqx, 1, 1, integer12, 1, 4, integer12, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
                    this.generateBox(bso, cqx, 12, 1, integer12, 12, 4, integer12, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
                    if (this.isTall) {
                        this.generateBox(bso, cqx, 1, 6, integer12, 1, 9, integer12, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
                        this.generateBox(bso, cqx, 12, 6, integer12, 12, 9, integer12, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
                    }
                }
            }
            for (int integer12 = 3; integer12 < 12; integer12 += 2) {
                this.generateBox(bso, cqx, 3, 1, integer12, 4, 3, integer12, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
                this.generateBox(bso, cqx, 6, 1, integer12, 7, 3, integer12, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
                this.generateBox(bso, cqx, 9, 1, integer12, 10, 3, integer12, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
            }
            if (this.isTall) {
                this.generateBox(bso, cqx, 1, 5, 1, 3, 5, 13, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
                this.generateBox(bso, cqx, 10, 5, 1, 12, 5, 13, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
                this.generateBox(bso, cqx, 4, 5, 1, 9, 5, 2, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
                this.generateBox(bso, cqx, 4, 5, 12, 9, 5, 13, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
                this.placeBlock(bso, Blocks.OAK_PLANKS.defaultBlockState(), 9, 5, 11, cqx);
                this.placeBlock(bso, Blocks.OAK_PLANKS.defaultBlockState(), 8, 5, 11, cqx);
                this.placeBlock(bso, Blocks.OAK_PLANKS.defaultBlockState(), 9, 5, 10, cqx);
                final BlockState cee12 = (((StateHolder<O, BlockState>)Blocks.OAK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.WEST, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.EAST, true);
                final BlockState cee13 = (((StateHolder<O, BlockState>)Blocks.OAK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.NORTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.SOUTH, true);
                this.generateBox(bso, cqx, 3, 6, 3, 3, 6, 11, cee13, cee13, false);
                this.generateBox(bso, cqx, 10, 6, 3, 10, 6, 9, cee13, cee13, false);
                this.generateBox(bso, cqx, 4, 6, 2, 9, 6, 2, cee12, cee12, false);
                this.generateBox(bso, cqx, 4, 6, 12, 7, 6, 12, cee12, cee12, false);
                this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.OAK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.NORTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.EAST, true), 3, 6, 2, cqx);
                this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.OAK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.SOUTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.EAST, true), 3, 6, 12, cqx);
                this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.OAK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.NORTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.WEST, true), 10, 6, 2, cqx);
                for (int integer13 = 0; integer13 <= 2; ++integer13) {
                    this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.OAK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.SOUTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.WEST, true), 8 + integer13, 6, 12 - integer13, cqx);
                    if (integer13 != 2) {
                        this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.OAK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.NORTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.EAST, true), 8 + integer13, 6, 11 - integer13, cqx);
                    }
                }
                final BlockState cee14 = ((StateHolder<O, BlockState>)Blocks.LADDER.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)LadderBlock.FACING, Direction.SOUTH);
                this.placeBlock(bso, cee14, 10, 1, 13, cqx);
                this.placeBlock(bso, cee14, 10, 2, 13, cqx);
                this.placeBlock(bso, cee14, 10, 3, 13, cqx);
                this.placeBlock(bso, cee14, 10, 4, 13, cqx);
                this.placeBlock(bso, cee14, 10, 5, 13, cqx);
                this.placeBlock(bso, cee14, 10, 6, 13, cqx);
                this.placeBlock(bso, cee14, 10, 7, 13, cqx);
                final int integer14 = 7;
                final int integer15 = 7;
                final BlockState cee15 = ((StateHolder<O, BlockState>)Blocks.OAK_FENCE.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.EAST, true);
                this.placeBlock(bso, cee15, 6, 9, 7, cqx);
                final BlockState cee16 = ((StateHolder<O, BlockState>)Blocks.OAK_FENCE.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.WEST, true);
                this.placeBlock(bso, cee16, 7, 9, 7, cqx);
                this.placeBlock(bso, cee15, 6, 8, 7, cqx);
                this.placeBlock(bso, cee16, 7, 8, 7, cqx);
                final BlockState cee17 = (((StateHolder<O, BlockState>)cee13).setValue((Property<Comparable>)FenceBlock.WEST, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.EAST, true);
                this.placeBlock(bso, cee17, 6, 7, 7, cqx);
                this.placeBlock(bso, cee17, 7, 7, 7, cqx);
                this.placeBlock(bso, cee15, 5, 7, 7, cqx);
                this.placeBlock(bso, cee16, 8, 7, 7, cqx);
                this.placeBlock(bso, ((StateHolder<O, BlockState>)cee15).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.NORTH, true), 6, 7, 6, cqx);
                this.placeBlock(bso, ((StateHolder<O, BlockState>)cee15).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.SOUTH, true), 6, 7, 8, cqx);
                this.placeBlock(bso, ((StateHolder<O, BlockState>)cee16).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.NORTH, true), 7, 7, 6, cqx);
                this.placeBlock(bso, ((StateHolder<O, BlockState>)cee16).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.SOUTH, true), 7, 7, 8, cqx);
                final BlockState cee18 = Blocks.TORCH.defaultBlockState();
                this.placeBlock(bso, cee18, 5, 8, 7, cqx);
                this.placeBlock(bso, cee18, 8, 8, 7, cqx);
                this.placeBlock(bso, cee18, 6, 8, 6, cqx);
                this.placeBlock(bso, cee18, 6, 8, 8, cqx);
                this.placeBlock(bso, cee18, 7, 8, 6, cqx);
                this.placeBlock(bso, cee18, 7, 8, 8, cqx);
            }
            this.createChest(bso, cqx, random, 3, 3, 5, BuiltInLootTables.STRONGHOLD_LIBRARY);
            if (this.isTall) {
                this.placeBlock(bso, Library.CAVE_AIR, 12, 9, 1, cqx);
                this.createChest(bso, cqx, random, 12, 8, 1, BuiltInLootTables.STRONGHOLD_LIBRARY);
            }
            return true;
        }
    }
    
    public static class FiveCrossing extends StrongholdPiece {
        private final boolean leftLow;
        private final boolean leftHigh;
        private final boolean rightLow;
        private final boolean rightHigh;
        
        public FiveCrossing(final int integer, final Random random, final BoundingBox cqx, final Direction gc) {
            super(StructurePieceType.STRONGHOLD_FIVE_CROSSING, integer);
            this.setOrientation(gc);
            this.entryDoor = this.randomSmallDoor(random);
            this.boundingBox = cqx;
            this.leftLow = random.nextBoolean();
            this.leftHigh = random.nextBoolean();
            this.rightLow = random.nextBoolean();
            this.rightHigh = (random.nextInt(3) > 0);
        }
        
        public FiveCrossing(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.STRONGHOLD_FIVE_CROSSING, md);
            this.leftLow = md.getBoolean("leftLow");
            this.leftHigh = md.getBoolean("leftHigh");
            this.rightLow = md.getBoolean("rightLow");
            this.rightHigh = md.getBoolean("rightHigh");
        }
        
        @Override
        protected void addAdditionalSaveData(final CompoundTag md) {
            super.addAdditionalSaveData(md);
            md.putBoolean("leftLow", this.leftLow);
            md.putBoolean("leftHigh", this.leftHigh);
            md.putBoolean("rightLow", this.rightLow);
            md.putBoolean("rightHigh", this.rightHigh);
        }
        
        @Override
        public void addChildren(final StructurePiece crr, final List<StructurePiece> list, final Random random) {
            int integer5 = 3;
            int integer6 = 5;
            final Direction gc7 = this.getOrientation();
            if (gc7 == Direction.WEST || gc7 == Direction.NORTH) {
                integer5 = 8 - integer5;
                integer6 = 8 - integer6;
            }
            this.generateSmallDoorChildForward((StartPiece)crr, list, random, 5, 1);
            if (this.leftLow) {
                this.generateSmallDoorChildLeft((StartPiece)crr, list, random, integer5, 1);
            }
            if (this.leftHigh) {
                this.generateSmallDoorChildLeft((StartPiece)crr, list, random, integer6, 7);
            }
            if (this.rightLow) {
                this.generateSmallDoorChildRight((StartPiece)crr, list, random, integer5, 1);
            }
            if (this.rightHigh) {
                this.generateSmallDoorChildRight((StartPiece)crr, list, random, integer6, 7);
            }
        }
        
        public static FiveCrossing createPiece(final List<StructurePiece> list, final Random random, final int integer3, final int integer4, final int integer5, final Direction gc, final int integer7) {
            final BoundingBox cqx8 = BoundingBox.orientBox(integer3, integer4, integer5, -4, -3, 0, 10, 9, 11, gc);
            if (!StrongholdPiece.isOkBox(cqx8) || StructurePiece.findCollisionPiece(list, cqx8) != null) {
                return null;
            }
            return new FiveCrossing(integer7, random, cqx8, gc);
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            this.generateBox(bso, cqx, 0, 0, 0, 9, 8, 10, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.generateSmallDoor(bso, random, cqx, this.entryDoor, 4, 3, 0);
            if (this.leftLow) {
                this.generateBox(bso, cqx, 0, 3, 1, 0, 5, 3, FiveCrossing.CAVE_AIR, FiveCrossing.CAVE_AIR, false);
            }
            if (this.rightLow) {
                this.generateBox(bso, cqx, 9, 3, 1, 9, 5, 3, FiveCrossing.CAVE_AIR, FiveCrossing.CAVE_AIR, false);
            }
            if (this.leftHigh) {
                this.generateBox(bso, cqx, 0, 5, 7, 0, 7, 9, FiveCrossing.CAVE_AIR, FiveCrossing.CAVE_AIR, false);
            }
            if (this.rightHigh) {
                this.generateBox(bso, cqx, 9, 5, 7, 9, 7, 9, FiveCrossing.CAVE_AIR, FiveCrossing.CAVE_AIR, false);
            }
            this.generateBox(bso, cqx, 5, 1, 10, 7, 3, 10, FiveCrossing.CAVE_AIR, FiveCrossing.CAVE_AIR, false);
            this.generateBox(bso, cqx, 1, 2, 1, 8, 2, 6, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.generateBox(bso, cqx, 4, 1, 5, 4, 4, 9, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.generateBox(bso, cqx, 8, 1, 5, 8, 4, 9, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.generateBox(bso, cqx, 1, 4, 7, 3, 4, 9, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.generateBox(bso, cqx, 1, 3, 5, 3, 3, 6, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.generateBox(bso, cqx, 1, 3, 4, 3, 3, 4, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
            this.generateBox(bso, cqx, 1, 4, 6, 3, 4, 6, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
            this.generateBox(bso, cqx, 5, 1, 7, 7, 1, 8, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.generateBox(bso, cqx, 5, 1, 9, 7, 1, 9, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
            this.generateBox(bso, cqx, 5, 2, 7, 7, 2, 7, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
            this.generateBox(bso, cqx, 4, 5, 7, 4, 5, 9, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
            this.generateBox(bso, cqx, 8, 5, 7, 8, 5, 9, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
            this.generateBox(bso, cqx, 5, 5, 7, 7, 5, 9, ((StateHolder<O, BlockState>)Blocks.SMOOTH_STONE_SLAB.defaultBlockState()).<SlabType, SlabType>setValue(SlabBlock.TYPE, SlabType.DOUBLE), ((StateHolder<O, BlockState>)Blocks.SMOOTH_STONE_SLAB.defaultBlockState()).<SlabType, SlabType>setValue(SlabBlock.TYPE, SlabType.DOUBLE), false);
            this.placeBlock(bso, ((StateHolder<O, BlockState>)Blocks.WALL_TORCH.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)WallTorchBlock.FACING, Direction.SOUTH), 6, 5, 6, cqx);
            return true;
        }
    }
    
    public static class PortalRoom extends StrongholdPiece {
        private boolean hasPlacedSpawner;
        
        public PortalRoom(final int integer, final BoundingBox cqx, final Direction gc) {
            super(StructurePieceType.STRONGHOLD_PORTAL_ROOM, integer);
            this.setOrientation(gc);
            this.boundingBox = cqx;
        }
        
        public PortalRoom(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.STRONGHOLD_PORTAL_ROOM, md);
            this.hasPlacedSpawner = md.getBoolean("Mob");
        }
        
        @Override
        protected void addAdditionalSaveData(final CompoundTag md) {
            super.addAdditionalSaveData(md);
            md.putBoolean("Mob", this.hasPlacedSpawner);
        }
        
        @Override
        public void addChildren(final StructurePiece crr, final List<StructurePiece> list, final Random random) {
            if (crr != null) {
                ((StartPiece)crr).portalRoomPiece = this;
            }
        }
        
        public static PortalRoom createPiece(final List<StructurePiece> list, final int integer2, final int integer3, final int integer4, final Direction gc, final int integer6) {
            final BoundingBox cqx7 = BoundingBox.orientBox(integer2, integer3, integer4, -4, -1, 0, 11, 8, 16, gc);
            if (!StrongholdPiece.isOkBox(cqx7) || StructurePiece.findCollisionPiece(list, cqx7) != null) {
                return null;
            }
            return new PortalRoom(integer6, cqx7, gc);
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            this.generateBox(bso, cqx, 0, 0, 0, 10, 7, 15, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.generateSmallDoor(bso, random, cqx, SmallDoorType.GRATES, 4, 1, 0);
            int integer9 = 6;
            this.generateBox(bso, cqx, 1, integer9, 1, 1, integer9, 14, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.generateBox(bso, cqx, 9, integer9, 1, 9, integer9, 14, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.generateBox(bso, cqx, 2, integer9, 1, 8, integer9, 2, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.generateBox(bso, cqx, 2, integer9, 14, 8, integer9, 14, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.generateBox(bso, cqx, 1, 1, 1, 2, 1, 4, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.generateBox(bso, cqx, 8, 1, 1, 9, 1, 4, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.generateBox(bso, cqx, 1, 1, 1, 1, 1, 3, Blocks.LAVA.defaultBlockState(), Blocks.LAVA.defaultBlockState(), false);
            this.generateBox(bso, cqx, 9, 1, 1, 9, 1, 3, Blocks.LAVA.defaultBlockState(), Blocks.LAVA.defaultBlockState(), false);
            this.generateBox(bso, cqx, 3, 1, 8, 7, 1, 12, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.generateBox(bso, cqx, 4, 1, 9, 6, 1, 11, Blocks.LAVA.defaultBlockState(), Blocks.LAVA.defaultBlockState(), false);
            final BlockState cee10 = (((StateHolder<O, BlockState>)Blocks.IRON_BARS.defaultBlockState()).setValue((Property<Comparable>)IronBarsBlock.NORTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)IronBarsBlock.SOUTH, true);
            final BlockState cee11 = (((StateHolder<O, BlockState>)Blocks.IRON_BARS.defaultBlockState()).setValue((Property<Comparable>)IronBarsBlock.WEST, true)).<Comparable, Boolean>setValue((Property<Comparable>)IronBarsBlock.EAST, true);
            for (int integer10 = 3; integer10 < 14; integer10 += 2) {
                this.generateBox(bso, cqx, 0, 3, integer10, 0, 4, integer10, cee10, cee10, false);
                this.generateBox(bso, cqx, 10, 3, integer10, 10, 4, integer10, cee10, cee10, false);
            }
            for (int integer10 = 2; integer10 < 9; integer10 += 2) {
                this.generateBox(bso, cqx, integer10, 3, 15, integer10, 4, 15, cee11, cee11, false);
            }
            final BlockState cee12 = ((StateHolder<O, BlockState>)Blocks.STONE_BRICK_STAIRS.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)StairBlock.FACING, Direction.NORTH);
            this.generateBox(bso, cqx, 4, 1, 5, 6, 1, 7, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.generateBox(bso, cqx, 4, 2, 6, 6, 2, 7, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            this.generateBox(bso, cqx, 4, 3, 7, 6, 3, 7, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
            for (int integer11 = 4; integer11 <= 6; ++integer11) {
                this.placeBlock(bso, cee12, integer11, 1, 4, cqx);
                this.placeBlock(bso, cee12, integer11, 2, 5, cqx);
                this.placeBlock(bso, cee12, integer11, 3, 6, cqx);
            }
            final BlockState cee13 = ((StateHolder<O, BlockState>)Blocks.END_PORTAL_FRAME.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)EndPortalFrameBlock.FACING, Direction.NORTH);
            final BlockState cee14 = ((StateHolder<O, BlockState>)Blocks.END_PORTAL_FRAME.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)EndPortalFrameBlock.FACING, Direction.SOUTH);
            final BlockState cee15 = ((StateHolder<O, BlockState>)Blocks.END_PORTAL_FRAME.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)EndPortalFrameBlock.FACING, Direction.EAST);
            final BlockState cee16 = ((StateHolder<O, BlockState>)Blocks.END_PORTAL_FRAME.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)EndPortalFrameBlock.FACING, Direction.WEST);
            boolean boolean17 = true;
            final boolean[] arr18 = new boolean[12];
            for (int integer12 = 0; integer12 < arr18.length; ++integer12) {
                arr18[integer12] = (random.nextFloat() > 0.9f);
                boolean17 &= arr18[integer12];
            }
            this.placeBlock(bso, ((StateHolder<O, BlockState>)cee13).<Comparable, Boolean>setValue((Property<Comparable>)EndPortalFrameBlock.HAS_EYE, arr18[0]), 4, 3, 8, cqx);
            this.placeBlock(bso, ((StateHolder<O, BlockState>)cee13).<Comparable, Boolean>setValue((Property<Comparable>)EndPortalFrameBlock.HAS_EYE, arr18[1]), 5, 3, 8, cqx);
            this.placeBlock(bso, ((StateHolder<O, BlockState>)cee13).<Comparable, Boolean>setValue((Property<Comparable>)EndPortalFrameBlock.HAS_EYE, arr18[2]), 6, 3, 8, cqx);
            this.placeBlock(bso, ((StateHolder<O, BlockState>)cee14).<Comparable, Boolean>setValue((Property<Comparable>)EndPortalFrameBlock.HAS_EYE, arr18[3]), 4, 3, 12, cqx);
            this.placeBlock(bso, ((StateHolder<O, BlockState>)cee14).<Comparable, Boolean>setValue((Property<Comparable>)EndPortalFrameBlock.HAS_EYE, arr18[4]), 5, 3, 12, cqx);
            this.placeBlock(bso, ((StateHolder<O, BlockState>)cee14).<Comparable, Boolean>setValue((Property<Comparable>)EndPortalFrameBlock.HAS_EYE, arr18[5]), 6, 3, 12, cqx);
            this.placeBlock(bso, ((StateHolder<O, BlockState>)cee15).<Comparable, Boolean>setValue((Property<Comparable>)EndPortalFrameBlock.HAS_EYE, arr18[6]), 3, 3, 9, cqx);
            this.placeBlock(bso, ((StateHolder<O, BlockState>)cee15).<Comparable, Boolean>setValue((Property<Comparable>)EndPortalFrameBlock.HAS_EYE, arr18[7]), 3, 3, 10, cqx);
            this.placeBlock(bso, ((StateHolder<O, BlockState>)cee15).<Comparable, Boolean>setValue((Property<Comparable>)EndPortalFrameBlock.HAS_EYE, arr18[8]), 3, 3, 11, cqx);
            this.placeBlock(bso, ((StateHolder<O, BlockState>)cee16).<Comparable, Boolean>setValue((Property<Comparable>)EndPortalFrameBlock.HAS_EYE, arr18[9]), 7, 3, 9, cqx);
            this.placeBlock(bso, ((StateHolder<O, BlockState>)cee16).<Comparable, Boolean>setValue((Property<Comparable>)EndPortalFrameBlock.HAS_EYE, arr18[10]), 7, 3, 10, cqx);
            this.placeBlock(bso, ((StateHolder<O, BlockState>)cee16).<Comparable, Boolean>setValue((Property<Comparable>)EndPortalFrameBlock.HAS_EYE, arr18[11]), 7, 3, 11, cqx);
            if (boolean17) {
                final BlockState cee17 = Blocks.END_PORTAL.defaultBlockState();
                this.placeBlock(bso, cee17, 4, 3, 9, cqx);
                this.placeBlock(bso, cee17, 5, 3, 9, cqx);
                this.placeBlock(bso, cee17, 6, 3, 9, cqx);
                this.placeBlock(bso, cee17, 4, 3, 10, cqx);
                this.placeBlock(bso, cee17, 5, 3, 10, cqx);
                this.placeBlock(bso, cee17, 6, 3, 10, cqx);
                this.placeBlock(bso, cee17, 4, 3, 11, cqx);
                this.placeBlock(bso, cee17, 5, 3, 11, cqx);
                this.placeBlock(bso, cee17, 6, 3, 11, cqx);
            }
            if (!this.hasPlacedSpawner) {
                integer9 = this.getWorldY(3);
                final BlockPos fx2 = new BlockPos(this.getWorldX(5, 6), integer9, this.getWorldZ(5, 6));
                if (cqx.isInside(fx2)) {
                    this.hasPlacedSpawner = true;
                    bso.setBlock(fx2, Blocks.SPAWNER.defaultBlockState(), 2);
                    final BlockEntity ccg20 = bso.getBlockEntity(fx2);
                    if (ccg20 instanceof SpawnerBlockEntity) {
                        ((SpawnerBlockEntity)ccg20).getSpawner().setEntityId(EntityType.SILVERFISH);
                    }
                }
            }
            return true;
        }
    }
    
    static class SmoothStoneSelector extends StructurePiece.BlockSelector {
        private SmoothStoneSelector() {
        }
        
        @Override
        public void next(final Random random, final int integer2, final int integer3, final int integer4, final boolean boolean5) {
            if (boolean5) {
                final float float7 = random.nextFloat();
                if (float7 < 0.2f) {
                    this.next = Blocks.CRACKED_STONE_BRICKS.defaultBlockState();
                }
                else if (float7 < 0.5f) {
                    this.next = Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
                }
                else if (float7 < 0.55f) {
                    this.next = Blocks.INFESTED_STONE_BRICKS.defaultBlockState();
                }
                else {
                    this.next = Blocks.STONE_BRICKS.defaultBlockState();
                }
            }
            else {
                this.next = Blocks.CAVE_AIR.defaultBlockState();
            }
        }
    }
}

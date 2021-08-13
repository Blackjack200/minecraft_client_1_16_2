package net.minecraft.world.level.levelgen.structure;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.core.Direction;
import java.util.Random;
import java.util.List;

public class NetherBridgePieces {
    private static final PieceWeight[] BRIDGE_PIECE_WEIGHTS;
    private static final PieceWeight[] CASTLE_PIECE_WEIGHTS;
    
    private static NetherBridgePiece findAndCreateBridgePieceFactory(final PieceWeight n, final List<StructurePiece> list, final Random random, final int integer4, final int integer5, final int integer6, final Direction gc, final int integer8) {
        final Class<? extends NetherBridgePiece> class9 = n.pieceClass;
        NetherBridgePiece m10 = null;
        if (class9 == BridgeStraight.class) {
            m10 = BridgeStraight.createPiece(list, random, integer4, integer5, integer6, gc, integer8);
        }
        else if (class9 == BridgeCrossing.class) {
            m10 = BridgeCrossing.createPiece(list, integer4, integer5, integer6, gc, integer8);
        }
        else if (class9 == RoomCrossing.class) {
            m10 = RoomCrossing.createPiece(list, integer4, integer5, integer6, gc, integer8);
        }
        else if (class9 == StairsRoom.class) {
            m10 = StairsRoom.createPiece(list, integer4, integer5, integer6, integer8, gc);
        }
        else if (class9 == MonsterThrone.class) {
            m10 = MonsterThrone.createPiece(list, integer4, integer5, integer6, integer8, gc);
        }
        else if (class9 == CastleEntrance.class) {
            m10 = CastleEntrance.createPiece(list, random, integer4, integer5, integer6, gc, integer8);
        }
        else if (class9 == CastleSmallCorridorPiece.class) {
            m10 = CastleSmallCorridorPiece.createPiece(list, integer4, integer5, integer6, gc, integer8);
        }
        else if (class9 == CastleSmallCorridorRightTurnPiece.class) {
            m10 = CastleSmallCorridorRightTurnPiece.createPiece(list, random, integer4, integer5, integer6, gc, integer8);
        }
        else if (class9 == CastleSmallCorridorLeftTurnPiece.class) {
            m10 = CastleSmallCorridorLeftTurnPiece.createPiece(list, random, integer4, integer5, integer6, gc, integer8);
        }
        else if (class9 == CastleCorridorStairsPiece.class) {
            m10 = CastleCorridorStairsPiece.createPiece(list, integer4, integer5, integer6, gc, integer8);
        }
        else if (class9 == CastleCorridorTBalconyPiece.class) {
            m10 = CastleCorridorTBalconyPiece.createPiece(list, integer4, integer5, integer6, gc, integer8);
        }
        else if (class9 == CastleSmallCorridorCrossingPiece.class) {
            m10 = CastleSmallCorridorCrossingPiece.createPiece(list, integer4, integer5, integer6, gc, integer8);
        }
        else if (class9 == CastleStalkRoom.class) {
            m10 = CastleStalkRoom.createPiece(list, integer4, integer5, integer6, gc, integer8);
        }
        return m10;
    }
    
    static {
        BRIDGE_PIECE_WEIGHTS = new PieceWeight[] { new PieceWeight(BridgeStraight.class, 30, 0, true), new PieceWeight(BridgeCrossing.class, 10, 4), new PieceWeight(RoomCrossing.class, 10, 4), new PieceWeight(StairsRoom.class, 10, 3), new PieceWeight(MonsterThrone.class, 5, 2), new PieceWeight(CastleEntrance.class, 5, 1) };
        CASTLE_PIECE_WEIGHTS = new PieceWeight[] { new PieceWeight(CastleSmallCorridorPiece.class, 25, 0, true), new PieceWeight(CastleSmallCorridorCrossingPiece.class, 15, 5), new PieceWeight(CastleSmallCorridorRightTurnPiece.class, 5, 10), new PieceWeight(CastleSmallCorridorLeftTurnPiece.class, 5, 10), new PieceWeight(CastleCorridorStairsPiece.class, 10, 3, true), new PieceWeight(CastleCorridorTBalconyPiece.class, 7, 2), new PieceWeight(CastleStalkRoom.class, 5, 2) };
    }
    
    static class PieceWeight {
        public final Class<? extends NetherBridgePiece> pieceClass;
        public final int weight;
        public int placeCount;
        public final int maxPlaceCount;
        public final boolean allowInRow;
        
        public PieceWeight(final Class<? extends NetherBridgePiece> class1, final int integer2, final int integer3, final boolean boolean4) {
            this.pieceClass = class1;
            this.weight = integer2;
            this.maxPlaceCount = integer3;
            this.allowInRow = boolean4;
        }
        
        public PieceWeight(final Class<? extends NetherBridgePiece> class1, final int integer2, final int integer3) {
            this(class1, integer2, integer3, false);
        }
        
        public boolean doPlace(final int integer) {
            return this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount;
        }
        
        public boolean isValid() {
            return this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount;
        }
    }
    
    abstract static class NetherBridgePiece extends StructurePiece {
        protected NetherBridgePiece(final StructurePieceType cky, final int integer) {
            super(cky, integer);
        }
        
        public NetherBridgePiece(final StructurePieceType cky, final CompoundTag md) {
            super(cky, md);
        }
        
        @Override
        protected void addAdditionalSaveData(final CompoundTag md) {
        }
        
        private int updatePieceWeight(final List<PieceWeight> list) {
            boolean boolean3 = false;
            int integer4 = 0;
            for (final PieceWeight n6 : list) {
                if (n6.maxPlaceCount > 0 && n6.placeCount < n6.maxPlaceCount) {
                    boolean3 = true;
                }
                integer4 += n6.weight;
            }
            return boolean3 ? integer4 : -1;
        }
        
        private NetherBridgePiece generatePiece(final StartPiece q, final List<PieceWeight> list2, final List<StructurePiece> list3, final Random random, final int integer5, final int integer6, final int integer7, final Direction gc, final int integer9) {
            final int integer10 = this.updatePieceWeight(list2);
            final boolean boolean12 = integer10 > 0 && integer9 <= 30;
            int integer11 = 0;
            while (integer11 < 5 && boolean12) {
                ++integer11;
                int integer12 = random.nextInt(integer10);
                for (final PieceWeight n16 : list2) {
                    integer12 -= n16.weight;
                    if (integer12 < 0) {
                        if (!n16.doPlace(integer9)) {
                            break;
                        }
                        if (n16 == q.previousPiece && !n16.allowInRow) {
                            break;
                        }
                        final NetherBridgePiece m17 = findAndCreateBridgePieceFactory(n16, list3, random, integer5, integer6, integer7, gc, integer9);
                        if (m17 != null) {
                            final PieceWeight pieceWeight = n16;
                            ++pieceWeight.placeCount;
                            q.previousPiece = n16;
                            if (!n16.isValid()) {
                                list2.remove(n16);
                            }
                            return m17;
                        }
                        continue;
                    }
                }
            }
            return BridgeEndFiller.createPiece(list3, random, integer5, integer6, integer7, gc, integer9);
        }
        
        private StructurePiece generateAndAddPiece(final StartPiece q, final List<StructurePiece> list, final Random random, final int integer4, final int integer5, final int integer6, @Nullable final Direction gc, final int integer8, final boolean boolean9) {
            if (Math.abs(integer4 - q.getBoundingBox().x0) > 112 || Math.abs(integer6 - q.getBoundingBox().z0) > 112) {
                return BridgeEndFiller.createPiece(list, random, integer4, integer5, integer6, gc, integer8);
            }
            List<PieceWeight> list2 = q.availableBridgePieces;
            if (boolean9) {
                list2 = q.availableCastlePieces;
            }
            final StructurePiece crr12 = this.generatePiece(q, list2, list, random, integer4, integer5, integer6, gc, integer8 + 1);
            if (crr12 != null) {
                list.add(crr12);
                q.pendingChildren.add(crr12);
            }
            return crr12;
        }
        
        @Nullable
        protected StructurePiece generateChildForward(final StartPiece q, final List<StructurePiece> list, final Random random, final int integer4, final int integer5, final boolean boolean6) {
            final Direction gc8 = this.getOrientation();
            if (gc8 != null) {
                switch (gc8) {
                    case NORTH: {
                        return this.generateAndAddPiece(q, list, random, this.boundingBox.x0 + integer4, this.boundingBox.y0 + integer5, this.boundingBox.z0 - 1, gc8, this.getGenDepth(), boolean6);
                    }
                    case SOUTH: {
                        return this.generateAndAddPiece(q, list, random, this.boundingBox.x0 + integer4, this.boundingBox.y0 + integer5, this.boundingBox.z1 + 1, gc8, this.getGenDepth(), boolean6);
                    }
                    case WEST: {
                        return this.generateAndAddPiece(q, list, random, this.boundingBox.x0 - 1, this.boundingBox.y0 + integer5, this.boundingBox.z0 + integer4, gc8, this.getGenDepth(), boolean6);
                    }
                    case EAST: {
                        return this.generateAndAddPiece(q, list, random, this.boundingBox.x1 + 1, this.boundingBox.y0 + integer5, this.boundingBox.z0 + integer4, gc8, this.getGenDepth(), boolean6);
                    }
                }
            }
            return null;
        }
        
        @Nullable
        protected StructurePiece generateChildLeft(final StartPiece q, final List<StructurePiece> list, final Random random, final int integer4, final int integer5, final boolean boolean6) {
            final Direction gc8 = this.getOrientation();
            if (gc8 != null) {
                switch (gc8) {
                    case NORTH: {
                        return this.generateAndAddPiece(q, list, random, this.boundingBox.x0 - 1, this.boundingBox.y0 + integer4, this.boundingBox.z0 + integer5, Direction.WEST, this.getGenDepth(), boolean6);
                    }
                    case SOUTH: {
                        return this.generateAndAddPiece(q, list, random, this.boundingBox.x0 - 1, this.boundingBox.y0 + integer4, this.boundingBox.z0 + integer5, Direction.WEST, this.getGenDepth(), boolean6);
                    }
                    case WEST: {
                        return this.generateAndAddPiece(q, list, random, this.boundingBox.x0 + integer5, this.boundingBox.y0 + integer4, this.boundingBox.z0 - 1, Direction.NORTH, this.getGenDepth(), boolean6);
                    }
                    case EAST: {
                        return this.generateAndAddPiece(q, list, random, this.boundingBox.x0 + integer5, this.boundingBox.y0 + integer4, this.boundingBox.z0 - 1, Direction.NORTH, this.getGenDepth(), boolean6);
                    }
                }
            }
            return null;
        }
        
        @Nullable
        protected StructurePiece generateChildRight(final StartPiece q, final List<StructurePiece> list, final Random random, final int integer4, final int integer5, final boolean boolean6) {
            final Direction gc8 = this.getOrientation();
            if (gc8 != null) {
                switch (gc8) {
                    case NORTH: {
                        return this.generateAndAddPiece(q, list, random, this.boundingBox.x1 + 1, this.boundingBox.y0 + integer4, this.boundingBox.z0 + integer5, Direction.EAST, this.getGenDepth(), boolean6);
                    }
                    case SOUTH: {
                        return this.generateAndAddPiece(q, list, random, this.boundingBox.x1 + 1, this.boundingBox.y0 + integer4, this.boundingBox.z0 + integer5, Direction.EAST, this.getGenDepth(), boolean6);
                    }
                    case WEST: {
                        return this.generateAndAddPiece(q, list, random, this.boundingBox.x0 + integer5, this.boundingBox.y0 + integer4, this.boundingBox.z1 + 1, Direction.SOUTH, this.getGenDepth(), boolean6);
                    }
                    case EAST: {
                        return this.generateAndAddPiece(q, list, random, this.boundingBox.x0 + integer5, this.boundingBox.y0 + integer4, this.boundingBox.z1 + 1, Direction.SOUTH, this.getGenDepth(), boolean6);
                    }
                }
            }
            return null;
        }
        
        protected static boolean isOkBox(final BoundingBox cqx) {
            return cqx != null && cqx.y0 > 10;
        }
    }
    
    public static class StartPiece extends BridgeCrossing {
        public PieceWeight previousPiece;
        public List<PieceWeight> availableBridgePieces;
        public List<PieceWeight> availableCastlePieces;
        public final List<StructurePiece> pendingChildren;
        
        public StartPiece(final Random random, final int integer2, final int integer3) {
            super(random, integer2, integer3);
            this.pendingChildren = (List<StructurePiece>)Lists.newArrayList();
            this.availableBridgePieces = (List<PieceWeight>)Lists.newArrayList();
            for (final PieceWeight n8 : NetherBridgePieces.BRIDGE_PIECE_WEIGHTS) {
                n8.placeCount = 0;
                this.availableBridgePieces.add(n8);
            }
            this.availableCastlePieces = (List<PieceWeight>)Lists.newArrayList();
            for (final PieceWeight n8 : NetherBridgePieces.CASTLE_PIECE_WEIGHTS) {
                n8.placeCount = 0;
                this.availableCastlePieces.add(n8);
            }
        }
        
        public StartPiece(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.NETHER_FORTRESS_START, md);
            this.pendingChildren = (List<StructurePiece>)Lists.newArrayList();
        }
    }
    
    public static class BridgeStraight extends NetherBridgePiece {
        public BridgeStraight(final int integer, final Random random, final BoundingBox cqx, final Direction gc) {
            super(StructurePieceType.NETHER_FORTRESS_BRIDGE_STRAIGHT, integer);
            this.setOrientation(gc);
            this.boundingBox = cqx;
        }
        
        public BridgeStraight(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.NETHER_FORTRESS_BRIDGE_STRAIGHT, md);
        }
        
        @Override
        public void addChildren(final StructurePiece crr, final List<StructurePiece> list, final Random random) {
            this.generateChildForward((StartPiece)crr, list, random, 1, 3, false);
        }
        
        public static BridgeStraight createPiece(final List<StructurePiece> list, final Random random, final int integer3, final int integer4, final int integer5, final Direction gc, final int integer7) {
            final BoundingBox cqx8 = BoundingBox.orientBox(integer3, integer4, integer5, -1, -3, 0, 5, 10, 19, gc);
            if (!NetherBridgePiece.isOkBox(cqx8) || StructurePiece.findCollisionPiece(list, cqx8) != null) {
                return null;
            }
            return new BridgeStraight(integer7, random, cqx8, gc);
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            this.generateBox(bso, cqx, 0, 3, 0, 4, 4, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 1, 5, 0, 3, 7, 18, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 5, 0, 0, 5, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 4, 5, 0, 4, 5, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 2, 0, 4, 2, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 2, 13, 4, 2, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 0, 0, 4, 1, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 0, 15, 4, 1, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            for (int integer9 = 0; integer9 <= 4; ++integer9) {
                for (int integer10 = 0; integer10 <= 2; ++integer10) {
                    this.fillColumnDown(bso, Blocks.NETHER_BRICKS.defaultBlockState(), integer9, -1, integer10, cqx);
                    this.fillColumnDown(bso, Blocks.NETHER_BRICKS.defaultBlockState(), integer9, -1, 18 - integer10, cqx);
                }
            }
            final BlockState cee9 = (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.NORTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.SOUTH, true);
            final BlockState cee10 = ((StateHolder<O, BlockState>)cee9).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.EAST, true);
            final BlockState cee11 = ((StateHolder<O, BlockState>)cee9).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.WEST, true);
            this.generateBox(bso, cqx, 0, 1, 1, 0, 4, 1, cee10, cee10, false);
            this.generateBox(bso, cqx, 0, 3, 4, 0, 4, 4, cee10, cee10, false);
            this.generateBox(bso, cqx, 0, 3, 14, 0, 4, 14, cee10, cee10, false);
            this.generateBox(bso, cqx, 0, 1, 17, 0, 4, 17, cee10, cee10, false);
            this.generateBox(bso, cqx, 4, 1, 1, 4, 4, 1, cee11, cee11, false);
            this.generateBox(bso, cqx, 4, 3, 4, 4, 4, 4, cee11, cee11, false);
            this.generateBox(bso, cqx, 4, 3, 14, 4, 4, 14, cee11, cee11, false);
            this.generateBox(bso, cqx, 4, 1, 17, 4, 4, 17, cee11, cee11, false);
            return true;
        }
    }
    
    public static class BridgeEndFiller extends NetherBridgePiece {
        private final int selfSeed;
        
        public BridgeEndFiller(final int integer, final Random random, final BoundingBox cqx, final Direction gc) {
            super(StructurePieceType.NETHER_FORTRESS_BRIDGE_END_FILLER, integer);
            this.setOrientation(gc);
            this.boundingBox = cqx;
            this.selfSeed = random.nextInt();
        }
        
        public BridgeEndFiller(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.NETHER_FORTRESS_BRIDGE_END_FILLER, md);
            this.selfSeed = md.getInt("Seed");
        }
        
        public static BridgeEndFiller createPiece(final List<StructurePiece> list, final Random random, final int integer3, final int integer4, final int integer5, final Direction gc, final int integer7) {
            final BoundingBox cqx8 = BoundingBox.orientBox(integer3, integer4, integer5, -1, -3, 0, 5, 10, 8, gc);
            if (!NetherBridgePiece.isOkBox(cqx8) || StructurePiece.findCollisionPiece(list, cqx8) != null) {
                return null;
            }
            return new BridgeEndFiller(integer7, random, cqx8, gc);
        }
        
        @Override
        protected void addAdditionalSaveData(final CompoundTag md) {
            super.addAdditionalSaveData(md);
            md.putInt("Seed", this.selfSeed);
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            final Random random2 = new Random((long)this.selfSeed);
            for (int integer10 = 0; integer10 <= 4; ++integer10) {
                for (int integer11 = 3; integer11 <= 4; ++integer11) {
                    final int integer12 = random2.nextInt(8);
                    this.generateBox(bso, cqx, integer10, integer11, 0, integer10, integer11, integer12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
                }
            }
            int integer10 = random2.nextInt(8);
            this.generateBox(bso, cqx, 0, 5, 0, 0, 5, integer10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            integer10 = random2.nextInt(8);
            this.generateBox(bso, cqx, 4, 5, 0, 4, 5, integer10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            for (integer10 = 0; integer10 <= 4; ++integer10) {
                final int integer11 = random2.nextInt(5);
                this.generateBox(bso, cqx, integer10, 2, 0, integer10, 2, integer11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            }
            for (integer10 = 0; integer10 <= 4; ++integer10) {
                for (int integer11 = 0; integer11 <= 1; ++integer11) {
                    final int integer12 = random2.nextInt(3);
                    this.generateBox(bso, cqx, integer10, integer11, 0, integer10, integer11, integer12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
                }
            }
            return true;
        }
    }
    
    public static class BridgeCrossing extends NetherBridgePiece {
        public BridgeCrossing(final int integer, final BoundingBox cqx, final Direction gc) {
            super(StructurePieceType.NETHER_FORTRESS_BRIDGE_CROSSING, integer);
            this.setOrientation(gc);
            this.boundingBox = cqx;
        }
        
        protected BridgeCrossing(final Random random, final int integer2, final int integer3) {
            super(StructurePieceType.NETHER_FORTRESS_BRIDGE_CROSSING, 0);
            this.setOrientation(Direction.Plane.HORIZONTAL.getRandomDirection(random));
            if (this.getOrientation().getAxis() == Direction.Axis.Z) {
                this.boundingBox = new BoundingBox(integer2, 64, integer3, integer2 + 19 - 1, 73, integer3 + 19 - 1);
            }
            else {
                this.boundingBox = new BoundingBox(integer2, 64, integer3, integer2 + 19 - 1, 73, integer3 + 19 - 1);
            }
        }
        
        protected BridgeCrossing(final StructurePieceType cky, final CompoundTag md) {
            super(cky, md);
        }
        
        public BridgeCrossing(final StructureManager cst, final CompoundTag md) {
            this(StructurePieceType.NETHER_FORTRESS_BRIDGE_CROSSING, md);
        }
        
        @Override
        public void addChildren(final StructurePiece crr, final List<StructurePiece> list, final Random random) {
            this.generateChildForward((StartPiece)crr, list, random, 8, 3, false);
            this.generateChildLeft((StartPiece)crr, list, random, 3, 8, false);
            this.generateChildRight((StartPiece)crr, list, random, 3, 8, false);
        }
        
        public static BridgeCrossing createPiece(final List<StructurePiece> list, final int integer2, final int integer3, final int integer4, final Direction gc, final int integer6) {
            final BoundingBox cqx7 = BoundingBox.orientBox(integer2, integer3, integer4, -8, -3, 0, 19, 10, 19, gc);
            if (!NetherBridgePiece.isOkBox(cqx7) || StructurePiece.findCollisionPiece(list, cqx7) != null) {
                return null;
            }
            return new BridgeCrossing(integer6, cqx7, gc);
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            this.generateBox(bso, cqx, 7, 3, 0, 11, 4, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 3, 7, 18, 4, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 8, 5, 0, 10, 7, 18, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 5, 8, 18, 7, 10, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.generateBox(bso, cqx, 7, 5, 0, 7, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 7, 5, 11, 7, 5, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 11, 5, 0, 11, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 11, 5, 11, 11, 5, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 5, 7, 7, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 11, 5, 7, 18, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 5, 11, 7, 5, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 11, 5, 11, 18, 5, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 7, 2, 0, 11, 2, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 7, 2, 13, 11, 2, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 7, 0, 0, 11, 1, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 7, 0, 15, 11, 1, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            for (int integer9 = 7; integer9 <= 11; ++integer9) {
                for (int integer10 = 0; integer10 <= 2; ++integer10) {
                    this.fillColumnDown(bso, Blocks.NETHER_BRICKS.defaultBlockState(), integer9, -1, integer10, cqx);
                    this.fillColumnDown(bso, Blocks.NETHER_BRICKS.defaultBlockState(), integer9, -1, 18 - integer10, cqx);
                }
            }
            this.generateBox(bso, cqx, 0, 2, 7, 5, 2, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 13, 2, 7, 18, 2, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 0, 7, 3, 1, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 15, 0, 7, 18, 1, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            for (int integer9 = 0; integer9 <= 2; ++integer9) {
                for (int integer10 = 7; integer10 <= 11; ++integer10) {
                    this.fillColumnDown(bso, Blocks.NETHER_BRICKS.defaultBlockState(), integer9, -1, integer10, cqx);
                    this.fillColumnDown(bso, Blocks.NETHER_BRICKS.defaultBlockState(), 18 - integer9, -1, integer10, cqx);
                }
            }
            return true;
        }
    }
    
    public static class RoomCrossing extends NetherBridgePiece {
        public RoomCrossing(final int integer, final BoundingBox cqx, final Direction gc) {
            super(StructurePieceType.NETHER_FORTRESS_ROOM_CROSSING, integer);
            this.setOrientation(gc);
            this.boundingBox = cqx;
        }
        
        public RoomCrossing(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.NETHER_FORTRESS_ROOM_CROSSING, md);
        }
        
        @Override
        public void addChildren(final StructurePiece crr, final List<StructurePiece> list, final Random random) {
            this.generateChildForward((StartPiece)crr, list, random, 2, 0, false);
            this.generateChildLeft((StartPiece)crr, list, random, 0, 2, false);
            this.generateChildRight((StartPiece)crr, list, random, 0, 2, false);
        }
        
        public static RoomCrossing createPiece(final List<StructurePiece> list, final int integer2, final int integer3, final int integer4, final Direction gc, final int integer6) {
            final BoundingBox cqx7 = BoundingBox.orientBox(integer2, integer3, integer4, -2, 0, 0, 7, 9, 7, gc);
            if (!NetherBridgePiece.isOkBox(cqx7) || StructurePiece.findCollisionPiece(list, cqx7) != null) {
                return null;
            }
            return new RoomCrossing(integer6, cqx7, gc);
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            this.generateBox(bso, cqx, 0, 0, 0, 6, 1, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 2, 0, 6, 7, 6, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 2, 0, 1, 6, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 2, 6, 1, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 5, 2, 0, 6, 6, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 5, 2, 6, 6, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 2, 0, 0, 6, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 2, 5, 0, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 6, 2, 0, 6, 6, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 6, 2, 5, 6, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            final BlockState cee9 = (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.WEST, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.EAST, true);
            final BlockState cee10 = (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.NORTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.SOUTH, true);
            this.generateBox(bso, cqx, 2, 6, 0, 4, 6, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 2, 5, 0, 4, 5, 0, cee9, cee9, false);
            this.generateBox(bso, cqx, 2, 6, 6, 4, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 2, 5, 6, 4, 5, 6, cee9, cee9, false);
            this.generateBox(bso, cqx, 0, 6, 2, 0, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 5, 2, 0, 5, 4, cee10, cee10, false);
            this.generateBox(bso, cqx, 6, 6, 2, 6, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 6, 5, 2, 6, 5, 4, cee10, cee10, false);
            for (int integer11 = 0; integer11 <= 6; ++integer11) {
                for (int integer12 = 0; integer12 <= 6; ++integer12) {
                    this.fillColumnDown(bso, Blocks.NETHER_BRICKS.defaultBlockState(), integer11, -1, integer12, cqx);
                }
            }
            return true;
        }
    }
    
    public static class StairsRoom extends NetherBridgePiece {
        public StairsRoom(final int integer, final BoundingBox cqx, final Direction gc) {
            super(StructurePieceType.NETHER_FORTRESS_STAIRS_ROOM, integer);
            this.setOrientation(gc);
            this.boundingBox = cqx;
        }
        
        public StairsRoom(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.NETHER_FORTRESS_STAIRS_ROOM, md);
        }
        
        @Override
        public void addChildren(final StructurePiece crr, final List<StructurePiece> list, final Random random) {
            this.generateChildRight((StartPiece)crr, list, random, 6, 2, false);
        }
        
        public static StairsRoom createPiece(final List<StructurePiece> list, final int integer2, final int integer3, final int integer4, final int integer5, final Direction gc) {
            final BoundingBox cqx7 = BoundingBox.orientBox(integer2, integer3, integer4, -2, 0, 0, 7, 11, 7, gc);
            if (!NetherBridgePiece.isOkBox(cqx7) || StructurePiece.findCollisionPiece(list, cqx7) != null) {
                return null;
            }
            return new StairsRoom(integer5, cqx7, gc);
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            this.generateBox(bso, cqx, 0, 0, 0, 6, 1, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 2, 0, 6, 10, 6, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 2, 0, 1, 8, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 5, 2, 0, 6, 8, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 2, 1, 0, 8, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 6, 2, 1, 6, 8, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 1, 2, 6, 5, 8, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            final BlockState cee9 = (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.WEST, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.EAST, true);
            final BlockState cee10 = (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.NORTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.SOUTH, true);
            this.generateBox(bso, cqx, 0, 3, 2, 0, 5, 4, cee10, cee10, false);
            this.generateBox(bso, cqx, 6, 3, 2, 6, 5, 2, cee10, cee10, false);
            this.generateBox(bso, cqx, 6, 3, 4, 6, 5, 4, cee10, cee10, false);
            this.placeBlock(bso, Blocks.NETHER_BRICKS.defaultBlockState(), 5, 2, 5, cqx);
            this.generateBox(bso, cqx, 4, 2, 5, 4, 3, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 3, 2, 5, 3, 4, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 2, 2, 5, 2, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 1, 2, 5, 1, 6, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 1, 7, 1, 5, 7, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 6, 8, 2, 6, 8, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.generateBox(bso, cqx, 2, 6, 0, 4, 8, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 2, 5, 0, 4, 5, 0, cee9, cee9, false);
            for (int integer11 = 0; integer11 <= 6; ++integer11) {
                for (int integer12 = 0; integer12 <= 6; ++integer12) {
                    this.fillColumnDown(bso, Blocks.NETHER_BRICKS.defaultBlockState(), integer11, -1, integer12, cqx);
                }
            }
            return true;
        }
    }
    
    public static class MonsterThrone extends NetherBridgePiece {
        private boolean hasPlacedSpawner;
        
        public MonsterThrone(final int integer, final BoundingBox cqx, final Direction gc) {
            super(StructurePieceType.NETHER_FORTRESS_MONSTER_THRONE, integer);
            this.setOrientation(gc);
            this.boundingBox = cqx;
        }
        
        public MonsterThrone(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.NETHER_FORTRESS_MONSTER_THRONE, md);
            this.hasPlacedSpawner = md.getBoolean("Mob");
        }
        
        @Override
        protected void addAdditionalSaveData(final CompoundTag md) {
            super.addAdditionalSaveData(md);
            md.putBoolean("Mob", this.hasPlacedSpawner);
        }
        
        public static MonsterThrone createPiece(final List<StructurePiece> list, final int integer2, final int integer3, final int integer4, final int integer5, final Direction gc) {
            final BoundingBox cqx7 = BoundingBox.orientBox(integer2, integer3, integer4, -2, 0, 0, 7, 8, 9, gc);
            if (!NetherBridgePiece.isOkBox(cqx7) || StructurePiece.findCollisionPiece(list, cqx7) != null) {
                return null;
            }
            return new MonsterThrone(integer5, cqx7, gc);
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            this.generateBox(bso, cqx, 0, 2, 0, 6, 7, 7, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.generateBox(bso, cqx, 1, 0, 0, 5, 1, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 1, 2, 1, 5, 2, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 1, 3, 2, 5, 3, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 1, 4, 3, 5, 4, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 1, 2, 0, 1, 4, 2, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 5, 2, 0, 5, 4, 2, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 1, 5, 2, 1, 5, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 5, 5, 2, 5, 5, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 5, 3, 0, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 6, 5, 3, 6, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 1, 5, 8, 5, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            final BlockState cee9 = (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.WEST, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.EAST, true);
            final BlockState cee10 = (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.NORTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.SOUTH, true);
            this.placeBlock(bso, ((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.WEST, true), 1, 6, 3, cqx);
            this.placeBlock(bso, ((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.EAST, true), 5, 6, 3, cqx);
            this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.EAST, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.NORTH, true), 0, 6, 3, cqx);
            this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.WEST, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.NORTH, true), 6, 6, 3, cqx);
            this.generateBox(bso, cqx, 0, 6, 4, 0, 6, 7, cee10, cee10, false);
            this.generateBox(bso, cqx, 6, 6, 4, 6, 6, 7, cee10, cee10, false);
            this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.EAST, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.SOUTH, true), 0, 6, 8, cqx);
            this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.WEST, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.SOUTH, true), 6, 6, 8, cqx);
            this.generateBox(bso, cqx, 1, 6, 8, 5, 6, 8, cee9, cee9, false);
            this.placeBlock(bso, ((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.EAST, true), 1, 7, 8, cqx);
            this.generateBox(bso, cqx, 2, 7, 8, 4, 7, 8, cee9, cee9, false);
            this.placeBlock(bso, ((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.WEST, true), 5, 7, 8, cqx);
            this.placeBlock(bso, ((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.EAST, true), 2, 8, 8, cqx);
            this.placeBlock(bso, cee9, 3, 8, 8, cqx);
            this.placeBlock(bso, ((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.WEST, true), 4, 8, 8, cqx);
            if (!this.hasPlacedSpawner) {
                final BlockPos fx2 = new BlockPos(this.getWorldX(3, 5), this.getWorldY(5), this.getWorldZ(3, 5));
                if (cqx.isInside(fx2)) {
                    this.hasPlacedSpawner = true;
                    bso.setBlock(fx2, Blocks.SPAWNER.defaultBlockState(), 2);
                    final BlockEntity ccg12 = bso.getBlockEntity(fx2);
                    if (ccg12 instanceof SpawnerBlockEntity) {
                        ((SpawnerBlockEntity)ccg12).getSpawner().setEntityId(EntityType.BLAZE);
                    }
                }
            }
            for (int integer11 = 0; integer11 <= 6; ++integer11) {
                for (int integer12 = 0; integer12 <= 6; ++integer12) {
                    this.fillColumnDown(bso, Blocks.NETHER_BRICKS.defaultBlockState(), integer11, -1, integer12, cqx);
                }
            }
            return true;
        }
    }
    
    public static class CastleEntrance extends NetherBridgePiece {
        public CastleEntrance(final int integer, final Random random, final BoundingBox cqx, final Direction gc) {
            super(StructurePieceType.NETHER_FORTRESS_CASTLE_ENTRANCE, integer);
            this.setOrientation(gc);
            this.boundingBox = cqx;
        }
        
        public CastleEntrance(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.NETHER_FORTRESS_CASTLE_ENTRANCE, md);
        }
        
        @Override
        public void addChildren(final StructurePiece crr, final List<StructurePiece> list, final Random random) {
            this.generateChildForward((StartPiece)crr, list, random, 5, 3, true);
        }
        
        public static CastleEntrance createPiece(final List<StructurePiece> list, final Random random, final int integer3, final int integer4, final int integer5, final Direction gc, final int integer7) {
            final BoundingBox cqx8 = BoundingBox.orientBox(integer3, integer4, integer5, -5, -3, 0, 13, 14, 13, gc);
            if (!NetherBridgePiece.isOkBox(cqx8) || StructurePiece.findCollisionPiece(list, cqx8) != null) {
                return null;
            }
            return new CastleEntrance(integer7, random, cqx8, gc);
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            this.generateBox(bso, cqx, 0, 3, 0, 12, 4, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 5, 0, 12, 13, 12, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 5, 0, 1, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 11, 5, 0, 12, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 2, 5, 11, 4, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 8, 5, 11, 10, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 5, 9, 11, 7, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 2, 5, 0, 4, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 8, 5, 0, 10, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 5, 9, 0, 7, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 2, 11, 2, 10, 12, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 5, 8, 0, 7, 8, 0, Blocks.NETHER_BRICK_FENCE.defaultBlockState(), Blocks.NETHER_BRICK_FENCE.defaultBlockState(), false);
            final BlockState cee9 = (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.WEST, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.EAST, true);
            final BlockState cee10 = (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.NORTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.SOUTH, true);
            for (int integer11 = 1; integer11 <= 11; integer11 += 2) {
                this.generateBox(bso, cqx, integer11, 10, 0, integer11, 11, 0, cee9, cee9, false);
                this.generateBox(bso, cqx, integer11, 10, 12, integer11, 11, 12, cee9, cee9, false);
                this.generateBox(bso, cqx, 0, 10, integer11, 0, 11, integer11, cee10, cee10, false);
                this.generateBox(bso, cqx, 12, 10, integer11, 12, 11, integer11, cee10, cee10, false);
                this.placeBlock(bso, Blocks.NETHER_BRICKS.defaultBlockState(), integer11, 13, 0, cqx);
                this.placeBlock(bso, Blocks.NETHER_BRICKS.defaultBlockState(), integer11, 13, 12, cqx);
                this.placeBlock(bso, Blocks.NETHER_BRICKS.defaultBlockState(), 0, 13, integer11, cqx);
                this.placeBlock(bso, Blocks.NETHER_BRICKS.defaultBlockState(), 12, 13, integer11, cqx);
                if (integer11 != 11) {
                    this.placeBlock(bso, cee9, integer11 + 1, 13, 0, cqx);
                    this.placeBlock(bso, cee9, integer11 + 1, 13, 12, cqx);
                    this.placeBlock(bso, cee10, 0, 13, integer11 + 1, cqx);
                    this.placeBlock(bso, cee10, 12, 13, integer11 + 1, cqx);
                }
            }
            this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.NORTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.EAST, true), 0, 13, 0, cqx);
            this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.SOUTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.EAST, true), 0, 13, 12, cqx);
            this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.SOUTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.WEST, true), 12, 13, 12, cqx);
            this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.NORTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.WEST, true), 12, 13, 0, cqx);
            for (int integer11 = 3; integer11 <= 9; integer11 += 2) {
                this.generateBox(bso, cqx, 1, 7, integer11, 1, 8, integer11, ((StateHolder<O, BlockState>)cee10).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.WEST, true), ((StateHolder<O, BlockState>)cee10).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.WEST, true), false);
                this.generateBox(bso, cqx, 11, 7, integer11, 11, 8, integer11, ((StateHolder<O, BlockState>)cee10).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.EAST, true), ((StateHolder<O, BlockState>)cee10).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.EAST, true), false);
            }
            this.generateBox(bso, cqx, 4, 2, 0, 8, 2, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 2, 4, 12, 2, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 4, 0, 0, 8, 1, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 4, 0, 9, 8, 1, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 0, 4, 3, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 9, 0, 4, 12, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            for (int integer11 = 4; integer11 <= 8; ++integer11) {
                for (int integer12 = 0; integer12 <= 2; ++integer12) {
                    this.fillColumnDown(bso, Blocks.NETHER_BRICKS.defaultBlockState(), integer11, -1, integer12, cqx);
                    this.fillColumnDown(bso, Blocks.NETHER_BRICKS.defaultBlockState(), integer11, -1, 12 - integer12, cqx);
                }
            }
            for (int integer11 = 0; integer11 <= 2; ++integer11) {
                for (int integer12 = 4; integer12 <= 8; ++integer12) {
                    this.fillColumnDown(bso, Blocks.NETHER_BRICKS.defaultBlockState(), integer11, -1, integer12, cqx);
                    this.fillColumnDown(bso, Blocks.NETHER_BRICKS.defaultBlockState(), 12 - integer11, -1, integer12, cqx);
                }
            }
            this.generateBox(bso, cqx, 5, 5, 5, 7, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 6, 1, 6, 6, 4, 6, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.placeBlock(bso, Blocks.NETHER_BRICKS.defaultBlockState(), 6, 0, 6, cqx);
            this.placeBlock(bso, Blocks.LAVA.defaultBlockState(), 6, 5, 6, cqx);
            final BlockPos fx2 = new BlockPos(this.getWorldX(6, 6), this.getWorldY(5), this.getWorldZ(6, 6));
            if (cqx.isInside(fx2)) {
                bso.getLiquidTicks().scheduleTick(fx2, Fluids.LAVA, 0);
            }
            return true;
        }
    }
    
    public static class CastleStalkRoom extends NetherBridgePiece {
        public CastleStalkRoom(final int integer, final BoundingBox cqx, final Direction gc) {
            super(StructurePieceType.NETHER_FORTRESS_CASTLE_STALK_ROOM, integer);
            this.setOrientation(gc);
            this.boundingBox = cqx;
        }
        
        public CastleStalkRoom(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.NETHER_FORTRESS_CASTLE_STALK_ROOM, md);
        }
        
        @Override
        public void addChildren(final StructurePiece crr, final List<StructurePiece> list, final Random random) {
            this.generateChildForward((StartPiece)crr, list, random, 5, 3, true);
            this.generateChildForward((StartPiece)crr, list, random, 5, 11, true);
        }
        
        public static CastleStalkRoom createPiece(final List<StructurePiece> list, final int integer2, final int integer3, final int integer4, final Direction gc, final int integer6) {
            final BoundingBox cqx7 = BoundingBox.orientBox(integer2, integer3, integer4, -5, -3, 0, 13, 14, 13, gc);
            if (!NetherBridgePiece.isOkBox(cqx7) || StructurePiece.findCollisionPiece(list, cqx7) != null) {
                return null;
            }
            return new CastleStalkRoom(integer6, cqx7, gc);
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            this.generateBox(bso, cqx, 0, 3, 0, 12, 4, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 5, 0, 12, 13, 12, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 5, 0, 1, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 11, 5, 0, 12, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 2, 5, 11, 4, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 8, 5, 11, 10, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 5, 9, 11, 7, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 2, 5, 0, 4, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 8, 5, 0, 10, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 5, 9, 0, 7, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 2, 11, 2, 10, 12, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            final BlockState cee9 = (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.WEST, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.EAST, true);
            final BlockState cee10 = (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.NORTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.SOUTH, true);
            final BlockState cee11 = ((StateHolder<O, BlockState>)cee10).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.WEST, true);
            final BlockState cee12 = ((StateHolder<O, BlockState>)cee10).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.EAST, true);
            for (int integer13 = 1; integer13 <= 11; integer13 += 2) {
                this.generateBox(bso, cqx, integer13, 10, 0, integer13, 11, 0, cee9, cee9, false);
                this.generateBox(bso, cqx, integer13, 10, 12, integer13, 11, 12, cee9, cee9, false);
                this.generateBox(bso, cqx, 0, 10, integer13, 0, 11, integer13, cee10, cee10, false);
                this.generateBox(bso, cqx, 12, 10, integer13, 12, 11, integer13, cee10, cee10, false);
                this.placeBlock(bso, Blocks.NETHER_BRICKS.defaultBlockState(), integer13, 13, 0, cqx);
                this.placeBlock(bso, Blocks.NETHER_BRICKS.defaultBlockState(), integer13, 13, 12, cqx);
                this.placeBlock(bso, Blocks.NETHER_BRICKS.defaultBlockState(), 0, 13, integer13, cqx);
                this.placeBlock(bso, Blocks.NETHER_BRICKS.defaultBlockState(), 12, 13, integer13, cqx);
                if (integer13 != 11) {
                    this.placeBlock(bso, cee9, integer13 + 1, 13, 0, cqx);
                    this.placeBlock(bso, cee9, integer13 + 1, 13, 12, cqx);
                    this.placeBlock(bso, cee10, 0, 13, integer13 + 1, cqx);
                    this.placeBlock(bso, cee10, 12, 13, integer13 + 1, cqx);
                }
            }
            this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.NORTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.EAST, true), 0, 13, 0, cqx);
            this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.SOUTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.EAST, true), 0, 13, 12, cqx);
            this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.SOUTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.WEST, true), 12, 13, 12, cqx);
            this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.NORTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.WEST, true), 12, 13, 0, cqx);
            for (int integer13 = 3; integer13 <= 9; integer13 += 2) {
                this.generateBox(bso, cqx, 1, 7, integer13, 1, 8, integer13, cee11, cee11, false);
                this.generateBox(bso, cqx, 11, 7, integer13, 11, 8, integer13, cee12, cee12, false);
            }
            final BlockState cee13 = ((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_STAIRS.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)StairBlock.FACING, Direction.NORTH);
            for (int integer14 = 0; integer14 <= 6; ++integer14) {
                final int integer15 = integer14 + 4;
                for (int integer16 = 5; integer16 <= 7; ++integer16) {
                    this.placeBlock(bso, cee13, integer16, 5 + integer14, integer15, cqx);
                }
                if (integer15 >= 5 && integer15 <= 8) {
                    this.generateBox(bso, cqx, 5, 5, integer15, 7, integer14 + 4, integer15, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
                }
                else if (integer15 >= 9 && integer15 <= 10) {
                    this.generateBox(bso, cqx, 5, 8, integer15, 7, integer14 + 4, integer15, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
                }
                if (integer14 >= 1) {
                    this.generateBox(bso, cqx, 5, 6 + integer14, integer15, 7, 9 + integer14, integer15, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
                }
            }
            for (int integer14 = 5; integer14 <= 7; ++integer14) {
                this.placeBlock(bso, cee13, integer14, 12, 11, cqx);
            }
            this.generateBox(bso, cqx, 5, 6, 7, 5, 7, 7, cee12, cee12, false);
            this.generateBox(bso, cqx, 7, 6, 7, 7, 7, 7, cee11, cee11, false);
            this.generateBox(bso, cqx, 5, 13, 12, 7, 13, 12, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.generateBox(bso, cqx, 2, 5, 2, 3, 5, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 2, 5, 9, 3, 5, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 2, 5, 4, 2, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 9, 5, 2, 10, 5, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 9, 5, 9, 10, 5, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 10, 5, 4, 10, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            final BlockState cee14 = ((StateHolder<O, BlockState>)cee13).<Comparable, Direction>setValue((Property<Comparable>)StairBlock.FACING, Direction.EAST);
            final BlockState cee15 = ((StateHolder<O, BlockState>)cee13).<Comparable, Direction>setValue((Property<Comparable>)StairBlock.FACING, Direction.WEST);
            this.placeBlock(bso, cee15, 4, 5, 2, cqx);
            this.placeBlock(bso, cee15, 4, 5, 3, cqx);
            this.placeBlock(bso, cee15, 4, 5, 9, cqx);
            this.placeBlock(bso, cee15, 4, 5, 10, cqx);
            this.placeBlock(bso, cee14, 8, 5, 2, cqx);
            this.placeBlock(bso, cee14, 8, 5, 3, cqx);
            this.placeBlock(bso, cee14, 8, 5, 9, cqx);
            this.placeBlock(bso, cee14, 8, 5, 10, cqx);
            this.generateBox(bso, cqx, 3, 4, 4, 4, 4, 8, Blocks.SOUL_SAND.defaultBlockState(), Blocks.SOUL_SAND.defaultBlockState(), false);
            this.generateBox(bso, cqx, 8, 4, 4, 9, 4, 8, Blocks.SOUL_SAND.defaultBlockState(), Blocks.SOUL_SAND.defaultBlockState(), false);
            this.generateBox(bso, cqx, 3, 5, 4, 4, 5, 8, Blocks.NETHER_WART.defaultBlockState(), Blocks.NETHER_WART.defaultBlockState(), false);
            this.generateBox(bso, cqx, 8, 5, 4, 9, 5, 8, Blocks.NETHER_WART.defaultBlockState(), Blocks.NETHER_WART.defaultBlockState(), false);
            this.generateBox(bso, cqx, 4, 2, 0, 8, 2, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 2, 4, 12, 2, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 4, 0, 0, 8, 1, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 4, 0, 9, 8, 1, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 0, 4, 3, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 9, 0, 4, 12, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            for (int integer16 = 4; integer16 <= 8; ++integer16) {
                for (int integer17 = 0; integer17 <= 2; ++integer17) {
                    this.fillColumnDown(bso, Blocks.NETHER_BRICKS.defaultBlockState(), integer16, -1, integer17, cqx);
                    this.fillColumnDown(bso, Blocks.NETHER_BRICKS.defaultBlockState(), integer16, -1, 12 - integer17, cqx);
                }
            }
            for (int integer16 = 0; integer16 <= 2; ++integer16) {
                for (int integer17 = 4; integer17 <= 8; ++integer17) {
                    this.fillColumnDown(bso, Blocks.NETHER_BRICKS.defaultBlockState(), integer16, -1, integer17, cqx);
                    this.fillColumnDown(bso, Blocks.NETHER_BRICKS.defaultBlockState(), 12 - integer16, -1, integer17, cqx);
                }
            }
            return true;
        }
    }
    
    public static class CastleSmallCorridorPiece extends NetherBridgePiece {
        public CastleSmallCorridorPiece(final int integer, final BoundingBox cqx, final Direction gc) {
            super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR, integer);
            this.setOrientation(gc);
            this.boundingBox = cqx;
        }
        
        public CastleSmallCorridorPiece(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR, md);
        }
        
        @Override
        public void addChildren(final StructurePiece crr, final List<StructurePiece> list, final Random random) {
            this.generateChildForward((StartPiece)crr, list, random, 1, 0, true);
        }
        
        public static CastleSmallCorridorPiece createPiece(final List<StructurePiece> list, final int integer2, final int integer3, final int integer4, final Direction gc, final int integer6) {
            final BoundingBox cqx7 = BoundingBox.orientBox(integer2, integer3, integer4, -1, 0, 0, 5, 7, 5, gc);
            if (!NetherBridgePiece.isOkBox(cqx7) || StructurePiece.findCollisionPiece(list, cqx7) != null) {
                return null;
            }
            return new CastleSmallCorridorPiece(integer6, cqx7, gc);
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            this.generateBox(bso, cqx, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 2, 0, 4, 5, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            final BlockState cee9 = (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.NORTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.SOUTH, true);
            this.generateBox(bso, cqx, 0, 2, 0, 0, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 4, 2, 0, 4, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 3, 1, 0, 4, 1, cee9, cee9, false);
            this.generateBox(bso, cqx, 0, 3, 3, 0, 4, 3, cee9, cee9, false);
            this.generateBox(bso, cqx, 4, 3, 1, 4, 4, 1, cee9, cee9, false);
            this.generateBox(bso, cqx, 4, 3, 3, 4, 4, 3, cee9, cee9, false);
            this.generateBox(bso, cqx, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            for (int integer10 = 0; integer10 <= 4; ++integer10) {
                for (int integer11 = 0; integer11 <= 4; ++integer11) {
                    this.fillColumnDown(bso, Blocks.NETHER_BRICKS.defaultBlockState(), integer10, -1, integer11, cqx);
                }
            }
            return true;
        }
    }
    
    public static class CastleSmallCorridorCrossingPiece extends NetherBridgePiece {
        public CastleSmallCorridorCrossingPiece(final int integer, final BoundingBox cqx, final Direction gc) {
            super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_CROSSING, integer);
            this.setOrientation(gc);
            this.boundingBox = cqx;
        }
        
        public CastleSmallCorridorCrossingPiece(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_CROSSING, md);
        }
        
        @Override
        public void addChildren(final StructurePiece crr, final List<StructurePiece> list, final Random random) {
            this.generateChildForward((StartPiece)crr, list, random, 1, 0, true);
            this.generateChildLeft((StartPiece)crr, list, random, 0, 1, true);
            this.generateChildRight((StartPiece)crr, list, random, 0, 1, true);
        }
        
        public static CastleSmallCorridorCrossingPiece createPiece(final List<StructurePiece> list, final int integer2, final int integer3, final int integer4, final Direction gc, final int integer6) {
            final BoundingBox cqx7 = BoundingBox.orientBox(integer2, integer3, integer4, -1, 0, 0, 5, 7, 5, gc);
            if (!NetherBridgePiece.isOkBox(cqx7) || StructurePiece.findCollisionPiece(list, cqx7) != null) {
                return null;
            }
            return new CastleSmallCorridorCrossingPiece(integer6, cqx7, gc);
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            this.generateBox(bso, cqx, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 2, 0, 4, 5, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 2, 0, 0, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 4, 2, 0, 4, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 2, 4, 0, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 4, 2, 4, 4, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            for (int integer9 = 0; integer9 <= 4; ++integer9) {
                for (int integer10 = 0; integer10 <= 4; ++integer10) {
                    this.fillColumnDown(bso, Blocks.NETHER_BRICKS.defaultBlockState(), integer9, -1, integer10, cqx);
                }
            }
            return true;
        }
    }
    
    public static class CastleSmallCorridorRightTurnPiece extends NetherBridgePiece {
        private boolean isNeedingChest;
        
        public CastleSmallCorridorRightTurnPiece(final int integer, final Random random, final BoundingBox cqx, final Direction gc) {
            super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_RIGHT_TURN, integer);
            this.setOrientation(gc);
            this.boundingBox = cqx;
            this.isNeedingChest = (random.nextInt(3) == 0);
        }
        
        public CastleSmallCorridorRightTurnPiece(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_RIGHT_TURN, md);
            this.isNeedingChest = md.getBoolean("Chest");
        }
        
        @Override
        protected void addAdditionalSaveData(final CompoundTag md) {
            super.addAdditionalSaveData(md);
            md.putBoolean("Chest", this.isNeedingChest);
        }
        
        @Override
        public void addChildren(final StructurePiece crr, final List<StructurePiece> list, final Random random) {
            this.generateChildRight((StartPiece)crr, list, random, 0, 1, true);
        }
        
        public static CastleSmallCorridorRightTurnPiece createPiece(final List<StructurePiece> list, final Random random, final int integer3, final int integer4, final int integer5, final Direction gc, final int integer7) {
            final BoundingBox cqx8 = BoundingBox.orientBox(integer3, integer4, integer5, -1, 0, 0, 5, 7, 5, gc);
            if (!NetherBridgePiece.isOkBox(cqx8) || StructurePiece.findCollisionPiece(list, cqx8) != null) {
                return null;
            }
            return new CastleSmallCorridorRightTurnPiece(integer7, random, cqx8, gc);
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            this.generateBox(bso, cqx, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 2, 0, 4, 5, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            final BlockState cee9 = (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.WEST, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.EAST, true);
            final BlockState cee10 = (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.NORTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.SOUTH, true);
            this.generateBox(bso, cqx, 0, 2, 0, 0, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 3, 1, 0, 4, 1, cee10, cee10, false);
            this.generateBox(bso, cqx, 0, 3, 3, 0, 4, 3, cee10, cee10, false);
            this.generateBox(bso, cqx, 4, 2, 0, 4, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 1, 2, 4, 4, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 1, 3, 4, 1, 4, 4, cee9, cee9, false);
            this.generateBox(bso, cqx, 3, 3, 4, 3, 4, 4, cee9, cee9, false);
            if (this.isNeedingChest && cqx.isInside(new BlockPos(this.getWorldX(1, 3), this.getWorldY(2), this.getWorldZ(1, 3)))) {
                this.isNeedingChest = false;
                this.createChest(bso, cqx, random, 1, 2, 3, BuiltInLootTables.NETHER_BRIDGE);
            }
            this.generateBox(bso, cqx, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            for (int integer11 = 0; integer11 <= 4; ++integer11) {
                for (int integer12 = 0; integer12 <= 4; ++integer12) {
                    this.fillColumnDown(bso, Blocks.NETHER_BRICKS.defaultBlockState(), integer11, -1, integer12, cqx);
                }
            }
            return true;
        }
    }
    
    public static class CastleSmallCorridorLeftTurnPiece extends NetherBridgePiece {
        private boolean isNeedingChest;
        
        public CastleSmallCorridorLeftTurnPiece(final int integer, final Random random, final BoundingBox cqx, final Direction gc) {
            super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_LEFT_TURN, integer);
            this.setOrientation(gc);
            this.boundingBox = cqx;
            this.isNeedingChest = (random.nextInt(3) == 0);
        }
        
        public CastleSmallCorridorLeftTurnPiece(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_LEFT_TURN, md);
            this.isNeedingChest = md.getBoolean("Chest");
        }
        
        @Override
        protected void addAdditionalSaveData(final CompoundTag md) {
            super.addAdditionalSaveData(md);
            md.putBoolean("Chest", this.isNeedingChest);
        }
        
        @Override
        public void addChildren(final StructurePiece crr, final List<StructurePiece> list, final Random random) {
            this.generateChildLeft((StartPiece)crr, list, random, 0, 1, true);
        }
        
        public static CastleSmallCorridorLeftTurnPiece createPiece(final List<StructurePiece> list, final Random random, final int integer3, final int integer4, final int integer5, final Direction gc, final int integer7) {
            final BoundingBox cqx8 = BoundingBox.orientBox(integer3, integer4, integer5, -1, 0, 0, 5, 7, 5, gc);
            if (!NetherBridgePiece.isOkBox(cqx8) || StructurePiece.findCollisionPiece(list, cqx8) != null) {
                return null;
            }
            return new CastleSmallCorridorLeftTurnPiece(integer7, random, cqx8, gc);
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            this.generateBox(bso, cqx, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 2, 0, 4, 5, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            final BlockState cee9 = (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.WEST, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.EAST, true);
            final BlockState cee10 = (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.NORTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.SOUTH, true);
            this.generateBox(bso, cqx, 4, 2, 0, 4, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 4, 3, 1, 4, 4, 1, cee10, cee10, false);
            this.generateBox(bso, cqx, 4, 3, 3, 4, 4, 3, cee10, cee10, false);
            this.generateBox(bso, cqx, 0, 2, 0, 0, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 2, 4, 3, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 1, 3, 4, 1, 4, 4, cee9, cee9, false);
            this.generateBox(bso, cqx, 3, 3, 4, 3, 4, 4, cee9, cee9, false);
            if (this.isNeedingChest && cqx.isInside(new BlockPos(this.getWorldX(3, 3), this.getWorldY(2), this.getWorldZ(3, 3)))) {
                this.isNeedingChest = false;
                this.createChest(bso, cqx, random, 3, 2, 3, BuiltInLootTables.NETHER_BRIDGE);
            }
            this.generateBox(bso, cqx, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            for (int integer11 = 0; integer11 <= 4; ++integer11) {
                for (int integer12 = 0; integer12 <= 4; ++integer12) {
                    this.fillColumnDown(bso, Blocks.NETHER_BRICKS.defaultBlockState(), integer11, -1, integer12, cqx);
                }
            }
            return true;
        }
    }
    
    public static class CastleCorridorStairsPiece extends NetherBridgePiece {
        public CastleCorridorStairsPiece(final int integer, final BoundingBox cqx, final Direction gc) {
            super(StructurePieceType.NETHER_FORTRESS_CASTLE_CORRIDOR_STAIRS, integer);
            this.setOrientation(gc);
            this.boundingBox = cqx;
        }
        
        public CastleCorridorStairsPiece(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.NETHER_FORTRESS_CASTLE_CORRIDOR_STAIRS, md);
        }
        
        @Override
        public void addChildren(final StructurePiece crr, final List<StructurePiece> list, final Random random) {
            this.generateChildForward((StartPiece)crr, list, random, 1, 0, true);
        }
        
        public static CastleCorridorStairsPiece createPiece(final List<StructurePiece> list, final int integer2, final int integer3, final int integer4, final Direction gc, final int integer6) {
            final BoundingBox cqx7 = BoundingBox.orientBox(integer2, integer3, integer4, -1, -7, 0, 5, 14, 10, gc);
            if (!NetherBridgePiece.isOkBox(cqx7) || StructurePiece.findCollisionPiece(list, cqx7) != null) {
                return null;
            }
            return new CastleCorridorStairsPiece(integer6, cqx7, gc);
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            final BlockState cee9 = ((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_STAIRS.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)StairBlock.FACING, Direction.SOUTH);
            final BlockState cee10 = (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.NORTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.SOUTH, true);
            for (int integer11 = 0; integer11 <= 9; ++integer11) {
                final int integer12 = Math.max(1, 7 - integer11);
                final int integer13 = Math.min(Math.max(integer12 + 5, 14 - integer11), 13);
                final int integer14 = integer11;
                this.generateBox(bso, cqx, 0, 0, integer14, 4, integer12, integer14, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
                this.generateBox(bso, cqx, 1, integer12 + 1, integer14, 3, integer13 - 1, integer14, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
                if (integer11 <= 6) {
                    this.placeBlock(bso, cee9, 1, integer12 + 1, integer14, cqx);
                    this.placeBlock(bso, cee9, 2, integer12 + 1, integer14, cqx);
                    this.placeBlock(bso, cee9, 3, integer12 + 1, integer14, cqx);
                }
                this.generateBox(bso, cqx, 0, integer13, integer14, 4, integer13, integer14, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
                this.generateBox(bso, cqx, 0, integer12 + 1, integer14, 0, integer13 - 1, integer14, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
                this.generateBox(bso, cqx, 4, integer12 + 1, integer14, 4, integer13 - 1, integer14, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
                if ((integer11 & 0x1) == 0x0) {
                    this.generateBox(bso, cqx, 0, integer12 + 2, integer14, 0, integer12 + 3, integer14, cee10, cee10, false);
                    this.generateBox(bso, cqx, 4, integer12 + 2, integer14, 4, integer12 + 3, integer14, cee10, cee10, false);
                }
                for (int integer15 = 0; integer15 <= 4; ++integer15) {
                    this.fillColumnDown(bso, Blocks.NETHER_BRICKS.defaultBlockState(), integer15, -1, integer14, cqx);
                }
            }
            return true;
        }
    }
    
    public static class CastleCorridorTBalconyPiece extends NetherBridgePiece {
        public CastleCorridorTBalconyPiece(final int integer, final BoundingBox cqx, final Direction gc) {
            super(StructurePieceType.NETHER_FORTRESS_CASTLE_CORRIDOR_T_BALCONY, integer);
            this.setOrientation(gc);
            this.boundingBox = cqx;
        }
        
        public CastleCorridorTBalconyPiece(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.NETHER_FORTRESS_CASTLE_CORRIDOR_T_BALCONY, md);
        }
        
        @Override
        public void addChildren(final StructurePiece crr, final List<StructurePiece> list, final Random random) {
            int integer5 = 1;
            final Direction gc6 = this.getOrientation();
            if (gc6 == Direction.WEST || gc6 == Direction.NORTH) {
                integer5 = 5;
            }
            this.generateChildLeft((StartPiece)crr, list, random, 0, integer5, random.nextInt(8) > 0);
            this.generateChildRight((StartPiece)crr, list, random, 0, integer5, random.nextInt(8) > 0);
        }
        
        public static CastleCorridorTBalconyPiece createPiece(final List<StructurePiece> list, final int integer2, final int integer3, final int integer4, final Direction gc, final int integer6) {
            final BoundingBox cqx7 = BoundingBox.orientBox(integer2, integer3, integer4, -3, 0, 0, 9, 7, 9, gc);
            if (!NetherBridgePiece.isOkBox(cqx7) || StructurePiece.findCollisionPiece(list, cqx7) != null) {
                return null;
            }
            return new CastleCorridorTBalconyPiece(integer6, cqx7, gc);
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            final BlockState cee9 = (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.NORTH, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.SOUTH, true);
            final BlockState cee10 = (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.WEST, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.EAST, true);
            this.generateBox(bso, cqx, 0, 0, 0, 8, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 2, 0, 8, 5, 8, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 6, 0, 8, 6, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 0, 2, 0, 2, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 6, 2, 0, 8, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 1, 3, 0, 1, 4, 0, cee10, cee10, false);
            this.generateBox(bso, cqx, 7, 3, 0, 7, 4, 0, cee10, cee10, false);
            this.generateBox(bso, cqx, 0, 2, 4, 8, 2, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 1, 1, 4, 2, 2, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.generateBox(bso, cqx, 6, 1, 4, 7, 2, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
            this.generateBox(bso, cqx, 1, 3, 8, 7, 3, 8, cee10, cee10, false);
            this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.EAST, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.SOUTH, true), 0, 3, 8, cqx);
            this.placeBlock(bso, (((StateHolder<O, BlockState>)Blocks.NETHER_BRICK_FENCE.defaultBlockState()).setValue((Property<Comparable>)FenceBlock.WEST, true)).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.SOUTH, true), 8, 3, 8, cqx);
            this.generateBox(bso, cqx, 0, 3, 6, 0, 3, 7, cee9, cee9, false);
            this.generateBox(bso, cqx, 8, 3, 6, 8, 3, 7, cee9, cee9, false);
            this.generateBox(bso, cqx, 0, 3, 4, 0, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 8, 3, 4, 8, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 1, 3, 5, 2, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 6, 3, 5, 7, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
            this.generateBox(bso, cqx, 1, 4, 5, 1, 5, 5, cee10, cee10, false);
            this.generateBox(bso, cqx, 7, 4, 5, 7, 5, 5, cee10, cee10, false);
            for (int integer11 = 0; integer11 <= 5; ++integer11) {
                for (int integer12 = 0; integer12 <= 8; ++integer12) {
                    this.fillColumnDown(bso, Blocks.NETHER_BRICKS.defaultBlockState(), integer12, -1, integer11, cqx);
                }
            }
            return true;
        }
    }
}

package net.minecraft.world.level.levelgen.structure;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.vehicle.MinecartChest;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.Tag;
import java.util.Iterator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import com.google.common.collect.Lists;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.MineshaftFeature;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import java.util.Random;
import java.util.List;

public class MineShaftPieces {
    private static MineShaftPiece createRandomShaftPiece(final List<StructurePiece> list, final Random random, final int integer3, final int integer4, final int integer5, @Nullable final Direction gc, final int integer7, final MineshaftFeature.Type b) {
        final int integer8 = random.nextInt(100);
        if (integer8 >= 80) {
            final BoundingBox cqx10 = MineShaftCrossing.findCrossing(list, random, integer3, integer4, integer5, gc);
            if (cqx10 != null) {
                return new MineShaftCrossing(integer7, cqx10, gc, b);
            }
        }
        else if (integer8 >= 70) {
            final BoundingBox cqx10 = MineShaftStairs.findStairs(list, random, integer3, integer4, integer5, gc);
            if (cqx10 != null) {
                return new MineShaftStairs(integer7, cqx10, gc, b);
            }
        }
        else {
            final BoundingBox cqx10 = MineShaftCorridor.findCorridorSize(list, random, integer3, integer4, integer5, gc);
            if (cqx10 != null) {
                return new MineShaftCorridor(integer7, random, cqx10, gc, b);
            }
        }
        return null;
    }
    
    private static MineShaftPiece generateAndAddPiece(final StructurePiece crr, final List<StructurePiece> list, final Random random, final int integer4, final int integer5, final int integer6, final Direction gc, final int integer8) {
        if (integer8 > 8) {
            return null;
        }
        if (Math.abs(integer4 - crr.getBoundingBox().x0) > 80 || Math.abs(integer6 - crr.getBoundingBox().z0) > 80) {
            return null;
        }
        final MineshaftFeature.Type b9 = ((MineShaftPiece)crr).type;
        final MineShaftPiece c10 = createRandomShaftPiece(list, random, integer4, integer5, integer6, gc, integer8 + 1, b9);
        if (c10 != null) {
            list.add(c10);
            c10.addChildren(crr, list, random);
        }
        return c10;
    }
    
    abstract static class MineShaftPiece extends StructurePiece {
        protected MineshaftFeature.Type type;
        
        public MineShaftPiece(final StructurePieceType cky, final int integer, final MineshaftFeature.Type b) {
            super(cky, integer);
            this.type = b;
        }
        
        public MineShaftPiece(final StructurePieceType cky, final CompoundTag md) {
            super(cky, md);
            this.type = MineshaftFeature.Type.byId(md.getInt("MST"));
        }
        
        @Override
        protected void addAdditionalSaveData(final CompoundTag md) {
            md.putInt("MST", this.type.ordinal());
        }
        
        protected BlockState getPlanksBlock() {
            switch (this.type) {
                default: {
                    return Blocks.OAK_PLANKS.defaultBlockState();
                }
                case MESA: {
                    return Blocks.DARK_OAK_PLANKS.defaultBlockState();
                }
            }
        }
        
        protected BlockState getFenceBlock() {
            switch (this.type) {
                default: {
                    return Blocks.OAK_FENCE.defaultBlockState();
                }
                case MESA: {
                    return Blocks.DARK_OAK_FENCE.defaultBlockState();
                }
            }
        }
        
        protected boolean isSupportingBox(final BlockGetter bqz, final BoundingBox cqx, final int integer3, final int integer4, final int integer5, final int integer6) {
            for (int integer7 = integer3; integer7 <= integer4; ++integer7) {
                if (this.getBlock(bqz, integer7, integer5 + 1, integer6, cqx).isAir()) {
                    return false;
                }
            }
            return true;
        }
    }
    
    public static class MineShaftRoom extends MineShaftPiece {
        private final List<BoundingBox> childEntranceBoxes;
        
        public MineShaftRoom(final int integer1, final Random random, final int integer3, final int integer4, final MineshaftFeature.Type b) {
            super(StructurePieceType.MINE_SHAFT_ROOM, integer1, b);
            this.childEntranceBoxes = (List<BoundingBox>)Lists.newLinkedList();
            this.type = b;
            this.boundingBox = new BoundingBox(integer3, 50, integer4, integer3 + 7 + random.nextInt(6), 54 + random.nextInt(6), integer4 + 7 + random.nextInt(6));
        }
        
        public MineShaftRoom(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.MINE_SHAFT_ROOM, md);
            this.childEntranceBoxes = (List<BoundingBox>)Lists.newLinkedList();
            final ListTag mj4 = md.getList("Entrances", 11);
            for (int integer5 = 0; integer5 < mj4.size(); ++integer5) {
                this.childEntranceBoxes.add(new BoundingBox(mj4.getIntArray(integer5)));
            }
        }
        
        @Override
        public void addChildren(final StructurePiece crr, final List<StructurePiece> list, final Random random) {
            final int integer5 = this.getGenDepth();
            int integer6 = this.boundingBox.getYSpan() - 3 - 1;
            if (integer6 <= 0) {
                integer6 = 1;
            }
            for (int integer7 = 0; integer7 < this.boundingBox.getXSpan(); integer7 += 4) {
                integer7 += random.nextInt(this.boundingBox.getXSpan());
                if (integer7 + 3 > this.boundingBox.getXSpan()) {
                    break;
                }
                final MineShaftPiece c8 = generateAndAddPiece(crr, list, random, this.boundingBox.x0 + integer7, this.boundingBox.y0 + random.nextInt(integer6) + 1, this.boundingBox.z0 - 1, Direction.NORTH, integer5);
                if (c8 != null) {
                    final BoundingBox cqx9 = c8.getBoundingBox();
                    this.childEntranceBoxes.add(new BoundingBox(cqx9.x0, cqx9.y0, this.boundingBox.z0, cqx9.x1, cqx9.y1, this.boundingBox.z0 + 1));
                }
            }
            for (int integer7 = 0; integer7 < this.boundingBox.getXSpan(); integer7 += 4) {
                integer7 += random.nextInt(this.boundingBox.getXSpan());
                if (integer7 + 3 > this.boundingBox.getXSpan()) {
                    break;
                }
                final MineShaftPiece c8 = generateAndAddPiece(crr, list, random, this.boundingBox.x0 + integer7, this.boundingBox.y0 + random.nextInt(integer6) + 1, this.boundingBox.z1 + 1, Direction.SOUTH, integer5);
                if (c8 != null) {
                    final BoundingBox cqx9 = c8.getBoundingBox();
                    this.childEntranceBoxes.add(new BoundingBox(cqx9.x0, cqx9.y0, this.boundingBox.z1 - 1, cqx9.x1, cqx9.y1, this.boundingBox.z1));
                }
            }
            for (int integer7 = 0; integer7 < this.boundingBox.getZSpan(); integer7 += 4) {
                integer7 += random.nextInt(this.boundingBox.getZSpan());
                if (integer7 + 3 > this.boundingBox.getZSpan()) {
                    break;
                }
                final MineShaftPiece c8 = generateAndAddPiece(crr, list, random, this.boundingBox.x0 - 1, this.boundingBox.y0 + random.nextInt(integer6) + 1, this.boundingBox.z0 + integer7, Direction.WEST, integer5);
                if (c8 != null) {
                    final BoundingBox cqx9 = c8.getBoundingBox();
                    this.childEntranceBoxes.add(new BoundingBox(this.boundingBox.x0, cqx9.y0, cqx9.z0, this.boundingBox.x0 + 1, cqx9.y1, cqx9.z1));
                }
            }
            for (int integer7 = 0; integer7 < this.boundingBox.getZSpan(); integer7 += 4) {
                integer7 += random.nextInt(this.boundingBox.getZSpan());
                if (integer7 + 3 > this.boundingBox.getZSpan()) {
                    break;
                }
                final StructurePiece crr2 = generateAndAddPiece(crr, list, random, this.boundingBox.x1 + 1, this.boundingBox.y0 + random.nextInt(integer6) + 1, this.boundingBox.z0 + integer7, Direction.EAST, integer5);
                if (crr2 != null) {
                    final BoundingBox cqx9 = crr2.getBoundingBox();
                    this.childEntranceBoxes.add(new BoundingBox(this.boundingBox.x1 - 1, cqx9.y0, cqx9.z0, this.boundingBox.x1, cqx9.y1, cqx9.z1));
                }
            }
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            if (this.edgesLiquid(bso, cqx)) {
                return false;
            }
            this.generateBox(bso, cqx, this.boundingBox.x0, this.boundingBox.y0, this.boundingBox.z0, this.boundingBox.x1, this.boundingBox.y0, this.boundingBox.z1, Blocks.DIRT.defaultBlockState(), MineShaftRoom.CAVE_AIR, true);
            this.generateBox(bso, cqx, this.boundingBox.x0, this.boundingBox.y0 + 1, this.boundingBox.z0, this.boundingBox.x1, Math.min(this.boundingBox.y0 + 3, this.boundingBox.y1), this.boundingBox.z1, MineShaftRoom.CAVE_AIR, MineShaftRoom.CAVE_AIR, false);
            for (final BoundingBox cqx2 : this.childEntranceBoxes) {
                this.generateBox(bso, cqx, cqx2.x0, cqx2.y1 - 2, cqx2.z0, cqx2.x1, cqx2.y1, cqx2.z1, MineShaftRoom.CAVE_AIR, MineShaftRoom.CAVE_AIR, false);
            }
            this.generateUpperHalfSphere(bso, cqx, this.boundingBox.x0, this.boundingBox.y0 + 4, this.boundingBox.z0, this.boundingBox.x1, this.boundingBox.y1, this.boundingBox.z1, MineShaftRoom.CAVE_AIR, false);
            return true;
        }
        
        @Override
        public void move(final int integer1, final int integer2, final int integer3) {
            super.move(integer1, integer2, integer3);
            for (final BoundingBox cqx6 : this.childEntranceBoxes) {
                cqx6.move(integer1, integer2, integer3);
            }
        }
        
        @Override
        protected void addAdditionalSaveData(final CompoundTag md) {
            super.addAdditionalSaveData(md);
            final ListTag mj3 = new ListTag();
            for (final BoundingBox cqx5 : this.childEntranceBoxes) {
                mj3.add(cqx5.createTag());
            }
            md.put("Entrances", (Tag)mj3);
        }
    }
    
    public static class MineShaftCorridor extends MineShaftPiece {
        private final boolean hasRails;
        private final boolean spiderCorridor;
        private boolean hasPlacedSpider;
        private final int numSections;
        
        public MineShaftCorridor(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.MINE_SHAFT_CORRIDOR, md);
            this.hasRails = md.getBoolean("hr");
            this.spiderCorridor = md.getBoolean("sc");
            this.hasPlacedSpider = md.getBoolean("hps");
            this.numSections = md.getInt("Num");
        }
        
        @Override
        protected void addAdditionalSaveData(final CompoundTag md) {
            super.addAdditionalSaveData(md);
            md.putBoolean("hr", this.hasRails);
            md.putBoolean("sc", this.spiderCorridor);
            md.putBoolean("hps", this.hasPlacedSpider);
            md.putInt("Num", this.numSections);
        }
        
        public MineShaftCorridor(final int integer, final Random random, final BoundingBox cqx, final Direction gc, final MineshaftFeature.Type b) {
            super(StructurePieceType.MINE_SHAFT_CORRIDOR, integer, b);
            this.setOrientation(gc);
            this.boundingBox = cqx;
            this.hasRails = (random.nextInt(3) == 0);
            this.spiderCorridor = (!this.hasRails && random.nextInt(23) == 0);
            if (this.getOrientation().getAxis() == Direction.Axis.Z) {
                this.numSections = cqx.getZSpan() / 5;
            }
            else {
                this.numSections = cqx.getXSpan() / 5;
            }
        }
        
        public static BoundingBox findCorridorSize(final List<StructurePiece> list, final Random random, final int integer3, final int integer4, final int integer5, final Direction gc) {
            final BoundingBox cqx7 = new BoundingBox(integer3, integer4, integer5, integer3, integer4 + 3 - 1, integer5);
            int integer6;
            for (integer6 = random.nextInt(3) + 2; integer6 > 0; --integer6) {
                final int integer7 = integer6 * 5;
                switch (gc) {
                    default: {
                        cqx7.x1 = integer3 + 3 - 1;
                        cqx7.z0 = integer5 - (integer7 - 1);
                        break;
                    }
                    case SOUTH: {
                        cqx7.x1 = integer3 + 3 - 1;
                        cqx7.z1 = integer5 + integer7 - 1;
                        break;
                    }
                    case WEST: {
                        cqx7.x0 = integer3 - (integer7 - 1);
                        cqx7.z1 = integer5 + 3 - 1;
                        break;
                    }
                    case EAST: {
                        cqx7.x1 = integer3 + integer7 - 1;
                        cqx7.z1 = integer5 + 3 - 1;
                        break;
                    }
                }
                if (StructurePiece.findCollisionPiece(list, cqx7) == null) {
                    break;
                }
            }
            if (integer6 > 0) {
                return cqx7;
            }
            return null;
        }
        
        @Override
        public void addChildren(final StructurePiece crr, final List<StructurePiece> list, final Random random) {
            final int integer5 = this.getGenDepth();
            final int integer6 = random.nextInt(4);
            final Direction gc7 = this.getOrientation();
            if (gc7 != null) {
                switch (gc7) {
                    default: {
                        if (integer6 <= 1) {
                            generateAndAddPiece(crr, list, random, this.boundingBox.x0, this.boundingBox.y0 - 1 + random.nextInt(3), this.boundingBox.z0 - 1, gc7, integer5);
                            break;
                        }
                        if (integer6 == 2) {
                            generateAndAddPiece(crr, list, random, this.boundingBox.x0 - 1, this.boundingBox.y0 - 1 + random.nextInt(3), this.boundingBox.z0, Direction.WEST, integer5);
                            break;
                        }
                        generateAndAddPiece(crr, list, random, this.boundingBox.x1 + 1, this.boundingBox.y0 - 1 + random.nextInt(3), this.boundingBox.z0, Direction.EAST, integer5);
                        break;
                    }
                    case SOUTH: {
                        if (integer6 <= 1) {
                            generateAndAddPiece(crr, list, random, this.boundingBox.x0, this.boundingBox.y0 - 1 + random.nextInt(3), this.boundingBox.z1 + 1, gc7, integer5);
                            break;
                        }
                        if (integer6 == 2) {
                            generateAndAddPiece(crr, list, random, this.boundingBox.x0 - 1, this.boundingBox.y0 - 1 + random.nextInt(3), this.boundingBox.z1 - 3, Direction.WEST, integer5);
                            break;
                        }
                        generateAndAddPiece(crr, list, random, this.boundingBox.x1 + 1, this.boundingBox.y0 - 1 + random.nextInt(3), this.boundingBox.z1 - 3, Direction.EAST, integer5);
                        break;
                    }
                    case WEST: {
                        if (integer6 <= 1) {
                            generateAndAddPiece(crr, list, random, this.boundingBox.x0 - 1, this.boundingBox.y0 - 1 + random.nextInt(3), this.boundingBox.z0, gc7, integer5);
                            break;
                        }
                        if (integer6 == 2) {
                            generateAndAddPiece(crr, list, random, this.boundingBox.x0, this.boundingBox.y0 - 1 + random.nextInt(3), this.boundingBox.z0 - 1, Direction.NORTH, integer5);
                            break;
                        }
                        generateAndAddPiece(crr, list, random, this.boundingBox.x0, this.boundingBox.y0 - 1 + random.nextInt(3), this.boundingBox.z1 + 1, Direction.SOUTH, integer5);
                        break;
                    }
                    case EAST: {
                        if (integer6 <= 1) {
                            generateAndAddPiece(crr, list, random, this.boundingBox.x1 + 1, this.boundingBox.y0 - 1 + random.nextInt(3), this.boundingBox.z0, gc7, integer5);
                            break;
                        }
                        if (integer6 == 2) {
                            generateAndAddPiece(crr, list, random, this.boundingBox.x1 - 3, this.boundingBox.y0 - 1 + random.nextInt(3), this.boundingBox.z0 - 1, Direction.NORTH, integer5);
                            break;
                        }
                        generateAndAddPiece(crr, list, random, this.boundingBox.x1 - 3, this.boundingBox.y0 - 1 + random.nextInt(3), this.boundingBox.z1 + 1, Direction.SOUTH, integer5);
                        break;
                    }
                }
            }
            if (integer5 < 8) {
                if (gc7 == Direction.NORTH || gc7 == Direction.SOUTH) {
                    for (int integer7 = this.boundingBox.z0 + 3; integer7 + 3 <= this.boundingBox.z1; integer7 += 5) {
                        final int integer8 = random.nextInt(5);
                        if (integer8 == 0) {
                            generateAndAddPiece(crr, list, random, this.boundingBox.x0 - 1, this.boundingBox.y0, integer7, Direction.WEST, integer5 + 1);
                        }
                        else if (integer8 == 1) {
                            generateAndAddPiece(crr, list, random, this.boundingBox.x1 + 1, this.boundingBox.y0, integer7, Direction.EAST, integer5 + 1);
                        }
                    }
                }
                else {
                    for (int integer7 = this.boundingBox.x0 + 3; integer7 + 3 <= this.boundingBox.x1; integer7 += 5) {
                        final int integer8 = random.nextInt(5);
                        if (integer8 == 0) {
                            generateAndAddPiece(crr, list, random, integer7, this.boundingBox.y0, this.boundingBox.z0 - 1, Direction.NORTH, integer5 + 1);
                        }
                        else if (integer8 == 1) {
                            generateAndAddPiece(crr, list, random, integer7, this.boundingBox.y0, this.boundingBox.z1 + 1, Direction.SOUTH, integer5 + 1);
                        }
                    }
                }
            }
        }
        
        @Override
        protected boolean createChest(final WorldGenLevel bso, final BoundingBox cqx, final Random random, final int integer4, final int integer5, final int integer6, final ResourceLocation vk) {
            final BlockPos fx9 = new BlockPos(this.getWorldX(integer4, integer6), this.getWorldY(integer5), this.getWorldZ(integer4, integer6));
            if (cqx.isInside(fx9) && bso.getBlockState(fx9).isAir() && !bso.getBlockState(fx9.below()).isAir()) {
                final BlockState cee10 = ((StateHolder<O, BlockState>)Blocks.RAIL.defaultBlockState()).<Comparable, RailShape>setValue((Property<Comparable>)RailBlock.SHAPE, random.nextBoolean() ? RailShape.NORTH_SOUTH : RailShape.EAST_WEST);
                this.placeBlock(bso, cee10, integer4, integer5, integer6, cqx);
                final MinecartChest bhn11 = new MinecartChest(bso.getLevel(), fx9.getX() + 0.5, fx9.getY() + 0.5, fx9.getZ() + 0.5);
                bhn11.setLootTable(vk, random.nextLong());
                bso.addFreshEntity(bhn11);
                return true;
            }
            return false;
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            if (this.edgesLiquid(bso, cqx)) {
                return false;
            }
            final int integer9 = 0;
            final int integer10 = 2;
            final int integer11 = 0;
            final int integer12 = 2;
            final int integer13 = this.numSections * 5 - 1;
            final BlockState cee14 = this.getPlanksBlock();
            this.generateBox(bso, cqx, 0, 0, 0, 2, 1, integer13, MineShaftCorridor.CAVE_AIR, MineShaftCorridor.CAVE_AIR, false);
            this.generateMaybeBox(bso, cqx, random, 0.8f, 0, 2, 0, 2, 2, integer13, MineShaftCorridor.CAVE_AIR, MineShaftCorridor.CAVE_AIR, false, false);
            if (this.spiderCorridor) {
                this.generateMaybeBox(bso, cqx, random, 0.6f, 0, 0, 0, 2, 1, integer13, Blocks.COBWEB.defaultBlockState(), MineShaftCorridor.CAVE_AIR, false, true);
            }
            for (int integer14 = 0; integer14 < this.numSections; ++integer14) {
                final int integer15 = 2 + integer14 * 5;
                this.placeSupport(bso, cqx, 0, 0, integer15, 2, 2, random);
                this.placeCobWeb(bso, cqx, random, 0.1f, 0, 2, integer15 - 1);
                this.placeCobWeb(bso, cqx, random, 0.1f, 2, 2, integer15 - 1);
                this.placeCobWeb(bso, cqx, random, 0.1f, 0, 2, integer15 + 1);
                this.placeCobWeb(bso, cqx, random, 0.1f, 2, 2, integer15 + 1);
                this.placeCobWeb(bso, cqx, random, 0.05f, 0, 2, integer15 - 2);
                this.placeCobWeb(bso, cqx, random, 0.05f, 2, 2, integer15 - 2);
                this.placeCobWeb(bso, cqx, random, 0.05f, 0, 2, integer15 + 2);
                this.placeCobWeb(bso, cqx, random, 0.05f, 2, 2, integer15 + 2);
                if (random.nextInt(100) == 0) {
                    this.createChest(bso, cqx, random, 2, 0, integer15 - 1, BuiltInLootTables.ABANDONED_MINESHAFT);
                }
                if (random.nextInt(100) == 0) {
                    this.createChest(bso, cqx, random, 0, 0, integer15 + 1, BuiltInLootTables.ABANDONED_MINESHAFT);
                }
                if (this.spiderCorridor && !this.hasPlacedSpider) {
                    final int integer16 = this.getWorldY(0);
                    final int integer17 = integer15 - 1 + random.nextInt(3);
                    final int integer18 = this.getWorldX(1, integer17);
                    final int integer19 = this.getWorldZ(1, integer17);
                    final BlockPos fx2 = new BlockPos(integer18, integer16, integer19);
                    if (cqx.isInside(fx2) && this.isInterior(bso, 1, 0, integer17, cqx)) {
                        this.hasPlacedSpider = true;
                        bso.setBlock(fx2, Blocks.SPAWNER.defaultBlockState(), 2);
                        final BlockEntity ccg22 = bso.getBlockEntity(fx2);
                        if (ccg22 instanceof SpawnerBlockEntity) {
                            ((SpawnerBlockEntity)ccg22).getSpawner().setEntityId(EntityType.CAVE_SPIDER);
                        }
                    }
                }
            }
            for (int integer14 = 0; integer14 <= 2; ++integer14) {
                for (int integer15 = 0; integer15 <= integer13; ++integer15) {
                    final int integer16 = -1;
                    final BlockState cee15 = this.getBlock(bso, integer14, -1, integer15, cqx);
                    if (cee15.isAir() && this.isInterior(bso, integer14, -1, integer15, cqx)) {
                        final int integer18 = -1;
                        this.placeBlock(bso, cee14, integer14, -1, integer15, cqx);
                    }
                }
            }
            if (this.hasRails) {
                final BlockState cee16 = ((StateHolder<O, BlockState>)Blocks.RAIL.defaultBlockState()).<RailShape, RailShape>setValue(RailBlock.SHAPE, RailShape.NORTH_SOUTH);
                for (int integer15 = 0; integer15 <= integer13; ++integer15) {
                    final BlockState cee17 = this.getBlock(bso, 1, -1, integer15, cqx);
                    if (!cee17.isAir() && cee17.isSolidRender(bso, new BlockPos(this.getWorldX(1, integer15), this.getWorldY(-1), this.getWorldZ(1, integer15)))) {
                        final float float18 = this.isInterior(bso, 1, 0, integer15, cqx) ? 0.7f : 0.9f;
                        this.maybeGenerateBlock(bso, cqx, random, float18, 1, 0, integer15, cee16);
                    }
                }
            }
            return true;
        }
        
        private void placeSupport(final WorldGenLevel bso, final BoundingBox cqx, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final Random random) {
            if (!this.isSupportingBox(bso, cqx, integer3, integer7, integer6, integer5)) {
                return;
            }
            final BlockState cee10 = this.getPlanksBlock();
            final BlockState cee11 = this.getFenceBlock();
            this.generateBox(bso, cqx, integer3, integer4, integer5, integer3, integer6 - 1, integer5, ((StateHolder<O, BlockState>)cee11).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.WEST, true), MineShaftCorridor.CAVE_AIR, false);
            this.generateBox(bso, cqx, integer7, integer4, integer5, integer7, integer6 - 1, integer5, ((StateHolder<O, BlockState>)cee11).<Comparable, Boolean>setValue((Property<Comparable>)FenceBlock.EAST, true), MineShaftCorridor.CAVE_AIR, false);
            if (random.nextInt(4) == 0) {
                this.generateBox(bso, cqx, integer3, integer6, integer5, integer3, integer6, integer5, cee10, MineShaftCorridor.CAVE_AIR, false);
                this.generateBox(bso, cqx, integer7, integer6, integer5, integer7, integer6, integer5, cee10, MineShaftCorridor.CAVE_AIR, false);
            }
            else {
                this.generateBox(bso, cqx, integer3, integer6, integer5, integer7, integer6, integer5, cee10, MineShaftCorridor.CAVE_AIR, false);
                this.maybeGenerateBlock(bso, cqx, random, 0.05f, integer3 + 1, integer6, integer5 - 1, ((StateHolder<O, BlockState>)Blocks.WALL_TORCH.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)WallTorchBlock.FACING, Direction.NORTH));
                this.maybeGenerateBlock(bso, cqx, random, 0.05f, integer3 + 1, integer6, integer5 + 1, ((StateHolder<O, BlockState>)Blocks.WALL_TORCH.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)WallTorchBlock.FACING, Direction.SOUTH));
            }
        }
        
        private void placeCobWeb(final WorldGenLevel bso, final BoundingBox cqx, final Random random, final float float4, final int integer5, final int integer6, final int integer7) {
            if (this.isInterior(bso, integer5, integer6, integer7, cqx)) {
                this.maybeGenerateBlock(bso, cqx, random, float4, integer5, integer6, integer7, Blocks.COBWEB.defaultBlockState());
            }
        }
    }
    
    public static class MineShaftCrossing extends MineShaftPiece {
        private final Direction direction;
        private final boolean isTwoFloored;
        
        public MineShaftCrossing(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.MINE_SHAFT_CROSSING, md);
            this.isTwoFloored = md.getBoolean("tf");
            this.direction = Direction.from2DDataValue(md.getInt("D"));
        }
        
        @Override
        protected void addAdditionalSaveData(final CompoundTag md) {
            super.addAdditionalSaveData(md);
            md.putBoolean("tf", this.isTwoFloored);
            md.putInt("D", this.direction.get2DDataValue());
        }
        
        public MineShaftCrossing(final int integer, final BoundingBox cqx, @Nullable final Direction gc, final MineshaftFeature.Type b) {
            super(StructurePieceType.MINE_SHAFT_CROSSING, integer, b);
            this.direction = gc;
            this.boundingBox = cqx;
            this.isTwoFloored = (cqx.getYSpan() > 3);
        }
        
        public static BoundingBox findCrossing(final List<StructurePiece> list, final Random random, final int integer3, final int integer4, final int integer5, final Direction gc) {
            final BoundingBox cqx7 = new BoundingBox(integer3, integer4, integer5, integer3, integer4 + 3 - 1, integer5);
            if (random.nextInt(4) == 0) {
                final BoundingBox boundingBox = cqx7;
                boundingBox.y1 += 4;
            }
            switch (gc) {
                default: {
                    cqx7.x0 = integer3 - 1;
                    cqx7.x1 = integer3 + 3;
                    cqx7.z0 = integer5 - 4;
                    break;
                }
                case SOUTH: {
                    cqx7.x0 = integer3 - 1;
                    cqx7.x1 = integer3 + 3;
                    cqx7.z1 = integer5 + 3 + 1;
                    break;
                }
                case WEST: {
                    cqx7.x0 = integer3 - 4;
                    cqx7.z0 = integer5 - 1;
                    cqx7.z1 = integer5 + 3;
                    break;
                }
                case EAST: {
                    cqx7.x1 = integer3 + 3 + 1;
                    cqx7.z0 = integer5 - 1;
                    cqx7.z1 = integer5 + 3;
                    break;
                }
            }
            if (StructurePiece.findCollisionPiece(list, cqx7) != null) {
                return null;
            }
            return cqx7;
        }
        
        @Override
        public void addChildren(final StructurePiece crr, final List<StructurePiece> list, final Random random) {
            final int integer5 = this.getGenDepth();
            switch (this.direction) {
                default: {
                    generateAndAddPiece(crr, list, random, this.boundingBox.x0 + 1, this.boundingBox.y0, this.boundingBox.z0 - 1, Direction.NORTH, integer5);
                    generateAndAddPiece(crr, list, random, this.boundingBox.x0 - 1, this.boundingBox.y0, this.boundingBox.z0 + 1, Direction.WEST, integer5);
                    generateAndAddPiece(crr, list, random, this.boundingBox.x1 + 1, this.boundingBox.y0, this.boundingBox.z0 + 1, Direction.EAST, integer5);
                    break;
                }
                case SOUTH: {
                    generateAndAddPiece(crr, list, random, this.boundingBox.x0 + 1, this.boundingBox.y0, this.boundingBox.z1 + 1, Direction.SOUTH, integer5);
                    generateAndAddPiece(crr, list, random, this.boundingBox.x0 - 1, this.boundingBox.y0, this.boundingBox.z0 + 1, Direction.WEST, integer5);
                    generateAndAddPiece(crr, list, random, this.boundingBox.x1 + 1, this.boundingBox.y0, this.boundingBox.z0 + 1, Direction.EAST, integer5);
                    break;
                }
                case WEST: {
                    generateAndAddPiece(crr, list, random, this.boundingBox.x0 + 1, this.boundingBox.y0, this.boundingBox.z0 - 1, Direction.NORTH, integer5);
                    generateAndAddPiece(crr, list, random, this.boundingBox.x0 + 1, this.boundingBox.y0, this.boundingBox.z1 + 1, Direction.SOUTH, integer5);
                    generateAndAddPiece(crr, list, random, this.boundingBox.x0 - 1, this.boundingBox.y0, this.boundingBox.z0 + 1, Direction.WEST, integer5);
                    break;
                }
                case EAST: {
                    generateAndAddPiece(crr, list, random, this.boundingBox.x0 + 1, this.boundingBox.y0, this.boundingBox.z0 - 1, Direction.NORTH, integer5);
                    generateAndAddPiece(crr, list, random, this.boundingBox.x0 + 1, this.boundingBox.y0, this.boundingBox.z1 + 1, Direction.SOUTH, integer5);
                    generateAndAddPiece(crr, list, random, this.boundingBox.x1 + 1, this.boundingBox.y0, this.boundingBox.z0 + 1, Direction.EAST, integer5);
                    break;
                }
            }
            if (this.isTwoFloored) {
                if (random.nextBoolean()) {
                    generateAndAddPiece(crr, list, random, this.boundingBox.x0 + 1, this.boundingBox.y0 + 3 + 1, this.boundingBox.z0 - 1, Direction.NORTH, integer5);
                }
                if (random.nextBoolean()) {
                    generateAndAddPiece(crr, list, random, this.boundingBox.x0 - 1, this.boundingBox.y0 + 3 + 1, this.boundingBox.z0 + 1, Direction.WEST, integer5);
                }
                if (random.nextBoolean()) {
                    generateAndAddPiece(crr, list, random, this.boundingBox.x1 + 1, this.boundingBox.y0 + 3 + 1, this.boundingBox.z0 + 1, Direction.EAST, integer5);
                }
                if (random.nextBoolean()) {
                    generateAndAddPiece(crr, list, random, this.boundingBox.x0 + 1, this.boundingBox.y0 + 3 + 1, this.boundingBox.z1 + 1, Direction.SOUTH, integer5);
                }
            }
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            if (this.edgesLiquid(bso, cqx)) {
                return false;
            }
            final BlockState cee9 = this.getPlanksBlock();
            if (this.isTwoFloored) {
                this.generateBox(bso, cqx, this.boundingBox.x0 + 1, this.boundingBox.y0, this.boundingBox.z0, this.boundingBox.x1 - 1, this.boundingBox.y0 + 3 - 1, this.boundingBox.z1, MineShaftCrossing.CAVE_AIR, MineShaftCrossing.CAVE_AIR, false);
                this.generateBox(bso, cqx, this.boundingBox.x0, this.boundingBox.y0, this.boundingBox.z0 + 1, this.boundingBox.x1, this.boundingBox.y0 + 3 - 1, this.boundingBox.z1 - 1, MineShaftCrossing.CAVE_AIR, MineShaftCrossing.CAVE_AIR, false);
                this.generateBox(bso, cqx, this.boundingBox.x0 + 1, this.boundingBox.y1 - 2, this.boundingBox.z0, this.boundingBox.x1 - 1, this.boundingBox.y1, this.boundingBox.z1, MineShaftCrossing.CAVE_AIR, MineShaftCrossing.CAVE_AIR, false);
                this.generateBox(bso, cqx, this.boundingBox.x0, this.boundingBox.y1 - 2, this.boundingBox.z0 + 1, this.boundingBox.x1, this.boundingBox.y1, this.boundingBox.z1 - 1, MineShaftCrossing.CAVE_AIR, MineShaftCrossing.CAVE_AIR, false);
                this.generateBox(bso, cqx, this.boundingBox.x0 + 1, this.boundingBox.y0 + 3, this.boundingBox.z0 + 1, this.boundingBox.x1 - 1, this.boundingBox.y0 + 3, this.boundingBox.z1 - 1, MineShaftCrossing.CAVE_AIR, MineShaftCrossing.CAVE_AIR, false);
            }
            else {
                this.generateBox(bso, cqx, this.boundingBox.x0 + 1, this.boundingBox.y0, this.boundingBox.z0, this.boundingBox.x1 - 1, this.boundingBox.y1, this.boundingBox.z1, MineShaftCrossing.CAVE_AIR, MineShaftCrossing.CAVE_AIR, false);
                this.generateBox(bso, cqx, this.boundingBox.x0, this.boundingBox.y0, this.boundingBox.z0 + 1, this.boundingBox.x1, this.boundingBox.y1, this.boundingBox.z1 - 1, MineShaftCrossing.CAVE_AIR, MineShaftCrossing.CAVE_AIR, false);
            }
            this.placeSupportPillar(bso, cqx, this.boundingBox.x0 + 1, this.boundingBox.y0, this.boundingBox.z0 + 1, this.boundingBox.y1);
            this.placeSupportPillar(bso, cqx, this.boundingBox.x0 + 1, this.boundingBox.y0, this.boundingBox.z1 - 1, this.boundingBox.y1);
            this.placeSupportPillar(bso, cqx, this.boundingBox.x1 - 1, this.boundingBox.y0, this.boundingBox.z0 + 1, this.boundingBox.y1);
            this.placeSupportPillar(bso, cqx, this.boundingBox.x1 - 1, this.boundingBox.y0, this.boundingBox.z1 - 1, this.boundingBox.y1);
            for (int integer10 = this.boundingBox.x0; integer10 <= this.boundingBox.x1; ++integer10) {
                for (int integer11 = this.boundingBox.z0; integer11 <= this.boundingBox.z1; ++integer11) {
                    if (this.getBlock(bso, integer10, this.boundingBox.y0 - 1, integer11, cqx).isAir() && this.isInterior(bso, integer10, this.boundingBox.y0 - 1, integer11, cqx)) {
                        this.placeBlock(bso, cee9, integer10, this.boundingBox.y0 - 1, integer11, cqx);
                    }
                }
            }
            return true;
        }
        
        private void placeSupportPillar(final WorldGenLevel bso, final BoundingBox cqx, final int integer3, final int integer4, final int integer5, final int integer6) {
            if (!this.getBlock(bso, integer3, integer6 + 1, integer5, cqx).isAir()) {
                this.generateBox(bso, cqx, integer3, integer4, integer5, integer3, integer6, integer5, this.getPlanksBlock(), MineShaftCrossing.CAVE_AIR, false);
            }
        }
    }
    
    public static class MineShaftStairs extends MineShaftPiece {
        public MineShaftStairs(final int integer, final BoundingBox cqx, final Direction gc, final MineshaftFeature.Type b) {
            super(StructurePieceType.MINE_SHAFT_STAIRS, integer, b);
            this.setOrientation(gc);
            this.boundingBox = cqx;
        }
        
        public MineShaftStairs(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.MINE_SHAFT_STAIRS, md);
        }
        
        public static BoundingBox findStairs(final List<StructurePiece> list, final Random random, final int integer3, final int integer4, final int integer5, final Direction gc) {
            final BoundingBox cqx7 = new BoundingBox(integer3, integer4 - 5, integer5, integer3, integer4 + 3 - 1, integer5);
            switch (gc) {
                default: {
                    cqx7.x1 = integer3 + 3 - 1;
                    cqx7.z0 = integer5 - 8;
                    break;
                }
                case SOUTH: {
                    cqx7.x1 = integer3 + 3 - 1;
                    cqx7.z1 = integer5 + 8;
                    break;
                }
                case WEST: {
                    cqx7.x0 = integer3 - 8;
                    cqx7.z1 = integer5 + 3 - 1;
                    break;
                }
                case EAST: {
                    cqx7.x1 = integer3 + 8;
                    cqx7.z1 = integer5 + 3 - 1;
                    break;
                }
            }
            if (StructurePiece.findCollisionPiece(list, cqx7) != null) {
                return null;
            }
            return cqx7;
        }
        
        @Override
        public void addChildren(final StructurePiece crr, final List<StructurePiece> list, final Random random) {
            final int integer5 = this.getGenDepth();
            final Direction gc6 = this.getOrientation();
            if (gc6 != null) {
                switch (gc6) {
                    default: {
                        generateAndAddPiece(crr, list, random, this.boundingBox.x0, this.boundingBox.y0, this.boundingBox.z0 - 1, Direction.NORTH, integer5);
                        break;
                    }
                    case SOUTH: {
                        generateAndAddPiece(crr, list, random, this.boundingBox.x0, this.boundingBox.y0, this.boundingBox.z1 + 1, Direction.SOUTH, integer5);
                        break;
                    }
                    case WEST: {
                        generateAndAddPiece(crr, list, random, this.boundingBox.x0 - 1, this.boundingBox.y0, this.boundingBox.z0, Direction.WEST, integer5);
                        break;
                    }
                    case EAST: {
                        generateAndAddPiece(crr, list, random, this.boundingBox.x1 + 1, this.boundingBox.y0, this.boundingBox.z0, Direction.EAST, integer5);
                        break;
                    }
                }
            }
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            if (this.edgesLiquid(bso, cqx)) {
                return false;
            }
            this.generateBox(bso, cqx, 0, 5, 0, 2, 7, 1, MineShaftStairs.CAVE_AIR, MineShaftStairs.CAVE_AIR, false);
            this.generateBox(bso, cqx, 0, 0, 7, 2, 2, 8, MineShaftStairs.CAVE_AIR, MineShaftStairs.CAVE_AIR, false);
            for (int integer9 = 0; integer9 < 5; ++integer9) {
                this.generateBox(bso, cqx, 0, 5 - integer9 - ((integer9 < 4) ? 1 : 0), 2 + integer9, 2, 7 - integer9, 2 + integer9, MineShaftStairs.CAVE_AIR, MineShaftStairs.CAVE_AIR, false);
            }
            return true;
        }
    }
}

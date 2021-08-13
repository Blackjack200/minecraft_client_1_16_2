package net.minecraft.world.level.levelgen.structure;

import net.minecraft.world.level.block.state.StateHolder;
import java.util.Collections;
import net.minecraft.util.Tuple;
import javax.annotation.Nullable;
import java.util.Iterator;
import com.google.common.collect.Lists;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.block.Mirror;
import java.util.Random;
import java.util.List;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

public class WoodlandMansionPieces {
    public static void generateMansion(final StructureManager cst, final BlockPos fx, final Rotation bzj, final List<WoodlandMansionPiece> list, final Random random) {
        final MansionGrid c6 = new MansionGrid(random);
        final MansionPiecePlacer d7 = new MansionPiecePlacer(cst, random);
        d7.createMansion(fx, bzj, list, c6);
    }
    
    public static class WoodlandMansionPiece extends TemplateStructurePiece {
        private final String templateName;
        private final Rotation rotation;
        private final Mirror mirror;
        
        public WoodlandMansionPiece(final StructureManager cst, final String string, final BlockPos fx, final Rotation bzj) {
            this(cst, string, fx, bzj, Mirror.NONE);
        }
        
        public WoodlandMansionPiece(final StructureManager cst, final String string, final BlockPos fx, final Rotation bzj, final Mirror byd) {
            super(StructurePieceType.WOODLAND_MANSION_PIECE, 0);
            this.templateName = string;
            this.templatePosition = fx;
            this.rotation = bzj;
            this.mirror = byd;
            this.loadTemplate(cst);
        }
        
        public WoodlandMansionPiece(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.WOODLAND_MANSION_PIECE, md);
            this.templateName = md.getString("Template");
            this.rotation = Rotation.valueOf(md.getString("Rot"));
            this.mirror = Mirror.valueOf(md.getString("Mi"));
            this.loadTemplate(cst);
        }
        
        private void loadTemplate(final StructureManager cst) {
            final StructureTemplate csy3 = cst.getOrCreate(new ResourceLocation("woodland_mansion/" + this.templateName));
            final StructurePlaceSettings csu4 = new StructurePlaceSettings().setIgnoreEntities(true).setRotation(this.rotation).setMirror(this.mirror).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
            this.setup(csy3, this.templatePosition, csu4);
        }
        
        @Override
        protected void addAdditionalSaveData(final CompoundTag md) {
            super.addAdditionalSaveData(md);
            md.putString("Template", this.templateName);
            md.putString("Rot", this.placeSettings.getRotation().name());
            md.putString("Mi", this.placeSettings.getMirror().name());
        }
        
        @Override
        protected void handleDataMarker(final String string, final BlockPos fx, final ServerLevelAccessor bsh, final Random random, final BoundingBox cqx) {
            if (string.startsWith("Chest")) {
                final Rotation bzj7 = this.placeSettings.getRotation();
                BlockState cee8 = Blocks.CHEST.defaultBlockState();
                if ("ChestWest".equals(string)) {
                    cee8 = ((StateHolder<O, BlockState>)cee8).<Comparable, Direction>setValue((Property<Comparable>)ChestBlock.FACING, bzj7.rotate(Direction.WEST));
                }
                else if ("ChestEast".equals(string)) {
                    cee8 = ((StateHolder<O, BlockState>)cee8).<Comparable, Direction>setValue((Property<Comparable>)ChestBlock.FACING, bzj7.rotate(Direction.EAST));
                }
                else if ("ChestSouth".equals(string)) {
                    cee8 = ((StateHolder<O, BlockState>)cee8).<Comparable, Direction>setValue((Property<Comparable>)ChestBlock.FACING, bzj7.rotate(Direction.SOUTH));
                }
                else if ("ChestNorth".equals(string)) {
                    cee8 = ((StateHolder<O, BlockState>)cee8).<Comparable, Direction>setValue((Property<Comparable>)ChestBlock.FACING, bzj7.rotate(Direction.NORTH));
                }
                this.createChest(bsh, cqx, random, fx, BuiltInLootTables.WOODLAND_MANSION, cee8);
            }
            else {
                AbstractIllager bcv7 = null;
                switch (string) {
                    case "Mage": {
                        bcv7 = EntityType.EVOKER.create(bsh.getLevel());
                        break;
                    }
                    case "Warrior": {
                        bcv7 = EntityType.VINDICATOR.create(bsh.getLevel());
                        break;
                    }
                    default: {
                        return;
                    }
                }
                bcv7.setPersistenceRequired();
                bcv7.moveTo(fx, 0.0f, 0.0f);
                bcv7.finalizeSpawn(bsh, bsh.getCurrentDifficultyAt(bcv7.blockPosition()), MobSpawnType.STRUCTURE, null, null);
                bsh.addFreshEntityWithPassengers(bcv7);
                bsh.setBlock(fx, Blocks.AIR.defaultBlockState(), 2);
            }
        }
    }
    
    static class PlacementData {
        public Rotation rotation;
        public BlockPos position;
        public String wallType;
        
        private PlacementData() {
        }
    }
    
    static class MansionPiecePlacer {
        private final StructureManager structureManager;
        private final Random random;
        private int startX;
        private int startY;
        
        public MansionPiecePlacer(final StructureManager cst, final Random random) {
            this.structureManager = cst;
            this.random = random;
        }
        
        public void createMansion(final BlockPos fx, final Rotation bzj, final List<WoodlandMansionPiece> list, final MansionGrid c) {
            final PlacementData e6 = new PlacementData();
            e6.position = fx;
            e6.rotation = bzj;
            e6.wallType = "wall_flat";
            final PlacementData e7 = new PlacementData();
            this.entrance(list, e6);
            e7.position = e6.position.above(8);
            e7.rotation = e6.rotation;
            e7.wallType = "wall_window";
            if (!list.isEmpty()) {}
            final SimpleGrid g8 = c.baseGrid;
            final SimpleGrid g9 = c.thirdFloorGrid;
            this.startX = c.entranceX + 1;
            this.startY = c.entranceY + 1;
            final int integer10 = c.entranceX + 1;
            final int integer11 = c.entranceY;
            this.traverseOuterWalls(list, e6, g8, Direction.SOUTH, this.startX, this.startY, integer10, integer11);
            this.traverseOuterWalls(list, e7, g8, Direction.SOUTH, this.startX, this.startY, integer10, integer11);
            final PlacementData e8 = new PlacementData();
            e8.position = e6.position.above(19);
            e8.rotation = e6.rotation;
            e8.wallType = "wall_window";
            boolean boolean13 = false;
            for (int integer12 = 0; integer12 < g9.height && !boolean13; ++integer12) {
                for (int integer13 = g9.width - 1; integer13 >= 0 && !boolean13; --integer13) {
                    if (MansionGrid.isHouse(g9, integer13, integer12)) {
                        e8.position = e8.position.relative(bzj.rotate(Direction.SOUTH), 8 + (integer12 - this.startY) * 8);
                        e8.position = e8.position.relative(bzj.rotate(Direction.EAST), (integer13 - this.startX) * 8);
                        this.traverseWallPiece(list, e8);
                        this.traverseOuterWalls(list, e8, g9, Direction.SOUTH, integer13, integer12, integer13, integer12);
                        boolean13 = true;
                    }
                }
            }
            this.createRoof(list, fx.above(16), bzj, g8, g9);
            this.createRoof(list, fx.above(27), bzj, g9, null);
            if (!list.isEmpty()) {}
            final FloorRoomCollection[] arr14 = { new FirstFloorRoomCollection(), new SecondFloorRoomCollection(), new ThirdFloorRoomCollection() };
            for (int integer13 = 0; integer13 < 3; ++integer13) {
                final BlockPos fx2 = fx.above(8 * integer13 + ((integer13 == 2) ? 3 : 0));
                final SimpleGrid g10 = c.floorRooms[integer13];
                final SimpleGrid g11 = (integer13 == 2) ? g9 : g8;
                final String string19 = (integer13 == 0) ? "carpet_south_1" : "carpet_south_2";
                final String string20 = (integer13 == 0) ? "carpet_west_1" : "carpet_west_2";
                for (int integer14 = 0; integer14 < g11.height; ++integer14) {
                    for (int integer15 = 0; integer15 < g11.width; ++integer15) {
                        if (g11.get(integer15, integer14) == 1) {
                            BlockPos fx3 = fx2.relative(bzj.rotate(Direction.SOUTH), 8 + (integer14 - this.startY) * 8);
                            fx3 = fx3.relative(bzj.rotate(Direction.EAST), (integer15 - this.startX) * 8);
                            list.add(new WoodlandMansionPiece(this.structureManager, "corridor_floor", fx3, bzj));
                            if (g11.get(integer15, integer14 - 1) == 1 || (g10.get(integer15, integer14 - 1) & 0x800000) == 0x800000) {
                                list.add(new WoodlandMansionPiece(this.structureManager, "carpet_north", fx3.relative(bzj.rotate(Direction.EAST), 1).above(), bzj));
                            }
                            if (g11.get(integer15 + 1, integer14) == 1 || (g10.get(integer15 + 1, integer14) & 0x800000) == 0x800000) {
                                list.add(new WoodlandMansionPiece(this.structureManager, "carpet_east", fx3.relative(bzj.rotate(Direction.SOUTH), 1).relative(bzj.rotate(Direction.EAST), 5).above(), bzj));
                            }
                            if (g11.get(integer15, integer14 + 1) == 1 || (g10.get(integer15, integer14 + 1) & 0x800000) == 0x800000) {
                                list.add(new WoodlandMansionPiece(this.structureManager, string19, fx3.relative(bzj.rotate(Direction.SOUTH), 5).relative(bzj.rotate(Direction.WEST), 1), bzj));
                            }
                            if (g11.get(integer15 - 1, integer14) == 1 || (g10.get(integer15 - 1, integer14) & 0x800000) == 0x800000) {
                                list.add(new WoodlandMansionPiece(this.structureManager, string20, fx3.relative(bzj.rotate(Direction.WEST), 1).relative(bzj.rotate(Direction.NORTH), 1), bzj));
                            }
                        }
                    }
                }
                final String string21 = (integer13 == 0) ? "indoors_wall_1" : "indoors_wall_2";
                final String string22 = (integer13 == 0) ? "indoors_door_1" : "indoors_door_2";
                final List<Direction> list2 = (List<Direction>)Lists.newArrayList();
                for (int integer16 = 0; integer16 < g11.height; ++integer16) {
                    for (int integer17 = 0; integer17 < g11.width; ++integer17) {
                        boolean boolean14 = integer13 == 2 && g11.get(integer17, integer16) == 3;
                        if (g11.get(integer17, integer16) == 2 || boolean14) {
                            final int integer18 = g10.get(integer17, integer16);
                            final int integer19 = integer18 & 0xF0000;
                            final int integer20 = integer18 & 0xFFFF;
                            boolean14 = (boolean14 && (integer18 & 0x800000) == 0x800000);
                            list2.clear();
                            if ((integer18 & 0x200000) == 0x200000) {
                                for (final Direction gc31 : Direction.Plane.HORIZONTAL) {
                                    if (g11.get(integer17 + gc31.getStepX(), integer16 + gc31.getStepZ()) == 1) {
                                        list2.add(gc31);
                                    }
                                }
                            }
                            Direction gc32 = null;
                            if (!list2.isEmpty()) {
                                gc32 = (Direction)list2.get(this.random.nextInt(list2.size()));
                            }
                            else if ((integer18 & 0x100000) == 0x100000) {
                                gc32 = Direction.UP;
                            }
                            BlockPos fx4 = fx2.relative(bzj.rotate(Direction.SOUTH), 8 + (integer16 - this.startY) * 8);
                            fx4 = fx4.relative(bzj.rotate(Direction.EAST), -1 + (integer17 - this.startX) * 8);
                            if (MansionGrid.isHouse(g11, integer17 - 1, integer16) && !c.isRoomId(g11, integer17 - 1, integer16, integer13, integer20)) {
                                list.add(new WoodlandMansionPiece(this.structureManager, (gc32 == Direction.WEST) ? string22 : string21, fx4, bzj));
                            }
                            if (g11.get(integer17 + 1, integer16) == 1 && !boolean14) {
                                final BlockPos fx5 = fx4.relative(bzj.rotate(Direction.EAST), 8);
                                list.add(new WoodlandMansionPiece(this.structureManager, (gc32 == Direction.EAST) ? string22 : string21, fx5, bzj));
                            }
                            if (MansionGrid.isHouse(g11, integer17, integer16 + 1) && !c.isRoomId(g11, integer17, integer16 + 1, integer13, integer20)) {
                                BlockPos fx5 = fx4.relative(bzj.rotate(Direction.SOUTH), 7);
                                fx5 = fx5.relative(bzj.rotate(Direction.EAST), 7);
                                list.add(new WoodlandMansionPiece(this.structureManager, (gc32 == Direction.SOUTH) ? string22 : string21, fx5, bzj.getRotated(Rotation.CLOCKWISE_90)));
                            }
                            if (g11.get(integer17, integer16 - 1) == 1 && !boolean14) {
                                BlockPos fx5 = fx4.relative(bzj.rotate(Direction.NORTH), 1);
                                fx5 = fx5.relative(bzj.rotate(Direction.EAST), 7);
                                list.add(new WoodlandMansionPiece(this.structureManager, (gc32 == Direction.NORTH) ? string22 : string21, fx5, bzj.getRotated(Rotation.CLOCKWISE_90)));
                            }
                            if (integer19 == 65536) {
                                this.addRoom1x1(list, fx4, bzj, gc32, arr14[integer13]);
                            }
                            else if (integer19 == 131072 && gc32 != null) {
                                final Direction gc33 = c.get1x2RoomDirection(g11, integer17, integer16, integer13, integer20);
                                final boolean boolean15 = (integer18 & 0x400000) == 0x400000;
                                this.addRoom1x2(list, fx4, bzj, gc33, gc32, arr14[integer13], boolean15);
                            }
                            else if (integer19 == 262144 && gc32 != null && gc32 != Direction.UP) {
                                Direction gc33 = gc32.getClockWise();
                                if (!c.isRoomId(g11, integer17 + gc33.getStepX(), integer16 + gc33.getStepZ(), integer13, integer20)) {
                                    gc33 = gc33.getOpposite();
                                }
                                this.addRoom2x2(list, fx4, bzj, gc33, gc32, arr14[integer13]);
                            }
                            else if (integer19 == 262144 && gc32 == Direction.UP) {
                                this.addRoom2x2Secret(list, fx4, bzj, arr14[integer13]);
                            }
                        }
                    }
                }
            }
        }
        
        private void traverseOuterWalls(final List<WoodlandMansionPiece> list, final PlacementData e, final SimpleGrid g, Direction gc, final int integer5, final int integer6, final int integer7, final int integer8) {
            int integer9 = integer5;
            int integer10 = integer6;
            final Direction gc2 = gc;
            do {
                if (!MansionGrid.isHouse(g, integer9 + gc.getStepX(), integer10 + gc.getStepZ())) {
                    this.traverseTurn(list, e);
                    gc = gc.getClockWise();
                    if (integer9 == integer7 && integer10 == integer8 && gc2 == gc) {
                        continue;
                    }
                    this.traverseWallPiece(list, e);
                }
                else if (MansionGrid.isHouse(g, integer9 + gc.getStepX(), integer10 + gc.getStepZ()) && MansionGrid.isHouse(g, integer9 + gc.getStepX() + gc.getCounterClockWise().getStepX(), integer10 + gc.getStepZ() + gc.getCounterClockWise().getStepZ())) {
                    this.traverseInnerTurn(list, e);
                    integer9 += gc.getStepX();
                    integer10 += gc.getStepZ();
                    gc = gc.getCounterClockWise();
                }
                else {
                    integer9 += gc.getStepX();
                    integer10 += gc.getStepZ();
                    if (integer9 == integer7 && integer10 == integer8 && gc2 == gc) {
                        continue;
                    }
                    this.traverseWallPiece(list, e);
                }
            } while (integer9 != integer7 || integer10 != integer8 || gc2 != gc);
        }
        
        private void createRoof(final List<WoodlandMansionPiece> list, final BlockPos fx, final Rotation bzj, final SimpleGrid g4, @Nullable final SimpleGrid g5) {
            for (int integer7 = 0; integer7 < g4.height; ++integer7) {
                for (int integer8 = 0; integer8 < g4.width; ++integer8) {
                    BlockPos fx2 = fx;
                    fx2 = fx2.relative(bzj.rotate(Direction.SOUTH), 8 + (integer7 - this.startY) * 8);
                    fx2 = fx2.relative(bzj.rotate(Direction.EAST), (integer8 - this.startX) * 8);
                    final boolean boolean10 = g5 != null && MansionGrid.isHouse(g5, integer8, integer7);
                    if (MansionGrid.isHouse(g4, integer8, integer7) && !boolean10) {
                        list.add(new WoodlandMansionPiece(this.structureManager, "roof", fx2.above(3), bzj));
                        if (!MansionGrid.isHouse(g4, integer8 + 1, integer7)) {
                            final BlockPos fx3 = fx2.relative(bzj.rotate(Direction.EAST), 6);
                            list.add(new WoodlandMansionPiece(this.structureManager, "roof_front", fx3, bzj));
                        }
                        if (!MansionGrid.isHouse(g4, integer8 - 1, integer7)) {
                            BlockPos fx3 = fx2.relative(bzj.rotate(Direction.EAST), 0);
                            fx3 = fx3.relative(bzj.rotate(Direction.SOUTH), 7);
                            list.add(new WoodlandMansionPiece(this.structureManager, "roof_front", fx3, bzj.getRotated(Rotation.CLOCKWISE_180)));
                        }
                        if (!MansionGrid.isHouse(g4, integer8, integer7 - 1)) {
                            final BlockPos fx3 = fx2.relative(bzj.rotate(Direction.WEST), 1);
                            list.add(new WoodlandMansionPiece(this.structureManager, "roof_front", fx3, bzj.getRotated(Rotation.COUNTERCLOCKWISE_90)));
                        }
                        if (!MansionGrid.isHouse(g4, integer8, integer7 + 1)) {
                            BlockPos fx3 = fx2.relative(bzj.rotate(Direction.EAST), 6);
                            fx3 = fx3.relative(bzj.rotate(Direction.SOUTH), 6);
                            list.add(new WoodlandMansionPiece(this.structureManager, "roof_front", fx3, bzj.getRotated(Rotation.CLOCKWISE_90)));
                        }
                    }
                }
            }
            if (g5 != null) {
                for (int integer7 = 0; integer7 < g4.height; ++integer7) {
                    for (int integer8 = 0; integer8 < g4.width; ++integer8) {
                        BlockPos fx2 = fx;
                        fx2 = fx2.relative(bzj.rotate(Direction.SOUTH), 8 + (integer7 - this.startY) * 8);
                        fx2 = fx2.relative(bzj.rotate(Direction.EAST), (integer8 - this.startX) * 8);
                        final boolean boolean10 = MansionGrid.isHouse(g5, integer8, integer7);
                        if (MansionGrid.isHouse(g4, integer8, integer7) && boolean10) {
                            if (!MansionGrid.isHouse(g4, integer8 + 1, integer7)) {
                                final BlockPos fx3 = fx2.relative(bzj.rotate(Direction.EAST), 7);
                                list.add(new WoodlandMansionPiece(this.structureManager, "small_wall", fx3, bzj));
                            }
                            if (!MansionGrid.isHouse(g4, integer8 - 1, integer7)) {
                                BlockPos fx3 = fx2.relative(bzj.rotate(Direction.WEST), 1);
                                fx3 = fx3.relative(bzj.rotate(Direction.SOUTH), 6);
                                list.add(new WoodlandMansionPiece(this.structureManager, "small_wall", fx3, bzj.getRotated(Rotation.CLOCKWISE_180)));
                            }
                            if (!MansionGrid.isHouse(g4, integer8, integer7 - 1)) {
                                BlockPos fx3 = fx2.relative(bzj.rotate(Direction.WEST), 0);
                                fx3 = fx3.relative(bzj.rotate(Direction.NORTH), 1);
                                list.add(new WoodlandMansionPiece(this.structureManager, "small_wall", fx3, bzj.getRotated(Rotation.COUNTERCLOCKWISE_90)));
                            }
                            if (!MansionGrid.isHouse(g4, integer8, integer7 + 1)) {
                                BlockPos fx3 = fx2.relative(bzj.rotate(Direction.EAST), 6);
                                fx3 = fx3.relative(bzj.rotate(Direction.SOUTH), 7);
                                list.add(new WoodlandMansionPiece(this.structureManager, "small_wall", fx3, bzj.getRotated(Rotation.CLOCKWISE_90)));
                            }
                            if (!MansionGrid.isHouse(g4, integer8 + 1, integer7)) {
                                if (!MansionGrid.isHouse(g4, integer8, integer7 - 1)) {
                                    BlockPos fx3 = fx2.relative(bzj.rotate(Direction.EAST), 7);
                                    fx3 = fx3.relative(bzj.rotate(Direction.NORTH), 2);
                                    list.add(new WoodlandMansionPiece(this.structureManager, "small_wall_corner", fx3, bzj));
                                }
                                if (!MansionGrid.isHouse(g4, integer8, integer7 + 1)) {
                                    BlockPos fx3 = fx2.relative(bzj.rotate(Direction.EAST), 8);
                                    fx3 = fx3.relative(bzj.rotate(Direction.SOUTH), 7);
                                    list.add(new WoodlandMansionPiece(this.structureManager, "small_wall_corner", fx3, bzj.getRotated(Rotation.CLOCKWISE_90)));
                                }
                            }
                            if (!MansionGrid.isHouse(g4, integer8 - 1, integer7)) {
                                if (!MansionGrid.isHouse(g4, integer8, integer7 - 1)) {
                                    BlockPos fx3 = fx2.relative(bzj.rotate(Direction.WEST), 2);
                                    fx3 = fx3.relative(bzj.rotate(Direction.NORTH), 1);
                                    list.add(new WoodlandMansionPiece(this.structureManager, "small_wall_corner", fx3, bzj.getRotated(Rotation.COUNTERCLOCKWISE_90)));
                                }
                                if (!MansionGrid.isHouse(g4, integer8, integer7 + 1)) {
                                    BlockPos fx3 = fx2.relative(bzj.rotate(Direction.WEST), 1);
                                    fx3 = fx3.relative(bzj.rotate(Direction.SOUTH), 8);
                                    list.add(new WoodlandMansionPiece(this.structureManager, "small_wall_corner", fx3, bzj.getRotated(Rotation.CLOCKWISE_180)));
                                }
                            }
                        }
                    }
                }
            }
            for (int integer7 = 0; integer7 < g4.height; ++integer7) {
                for (int integer8 = 0; integer8 < g4.width; ++integer8) {
                    BlockPos fx2 = fx;
                    fx2 = fx2.relative(bzj.rotate(Direction.SOUTH), 8 + (integer7 - this.startY) * 8);
                    fx2 = fx2.relative(bzj.rotate(Direction.EAST), (integer8 - this.startX) * 8);
                    final boolean boolean10 = g5 != null && MansionGrid.isHouse(g5, integer8, integer7);
                    if (MansionGrid.isHouse(g4, integer8, integer7) && !boolean10) {
                        if (!MansionGrid.isHouse(g4, integer8 + 1, integer7)) {
                            final BlockPos fx3 = fx2.relative(bzj.rotate(Direction.EAST), 6);
                            if (!MansionGrid.isHouse(g4, integer8, integer7 + 1)) {
                                final BlockPos fx4 = fx3.relative(bzj.rotate(Direction.SOUTH), 6);
                                list.add(new WoodlandMansionPiece(this.structureManager, "roof_corner", fx4, bzj));
                            }
                            else if (MansionGrid.isHouse(g4, integer8 + 1, integer7 + 1)) {
                                final BlockPos fx4 = fx3.relative(bzj.rotate(Direction.SOUTH), 5);
                                list.add(new WoodlandMansionPiece(this.structureManager, "roof_inner_corner", fx4, bzj));
                            }
                            if (!MansionGrid.isHouse(g4, integer8, integer7 - 1)) {
                                list.add(new WoodlandMansionPiece(this.structureManager, "roof_corner", fx3, bzj.getRotated(Rotation.COUNTERCLOCKWISE_90)));
                            }
                            else if (MansionGrid.isHouse(g4, integer8 + 1, integer7 - 1)) {
                                BlockPos fx4 = fx2.relative(bzj.rotate(Direction.EAST), 9);
                                fx4 = fx4.relative(bzj.rotate(Direction.NORTH), 2);
                                list.add(new WoodlandMansionPiece(this.structureManager, "roof_inner_corner", fx4, bzj.getRotated(Rotation.CLOCKWISE_90)));
                            }
                        }
                        if (!MansionGrid.isHouse(g4, integer8 - 1, integer7)) {
                            BlockPos fx3 = fx2.relative(bzj.rotate(Direction.EAST), 0);
                            fx3 = fx3.relative(bzj.rotate(Direction.SOUTH), 0);
                            if (!MansionGrid.isHouse(g4, integer8, integer7 + 1)) {
                                final BlockPos fx4 = fx3.relative(bzj.rotate(Direction.SOUTH), 6);
                                list.add(new WoodlandMansionPiece(this.structureManager, "roof_corner", fx4, bzj.getRotated(Rotation.CLOCKWISE_90)));
                            }
                            else if (MansionGrid.isHouse(g4, integer8 - 1, integer7 + 1)) {
                                BlockPos fx4 = fx3.relative(bzj.rotate(Direction.SOUTH), 8);
                                fx4 = fx4.relative(bzj.rotate(Direction.WEST), 3);
                                list.add(new WoodlandMansionPiece(this.structureManager, "roof_inner_corner", fx4, bzj.getRotated(Rotation.COUNTERCLOCKWISE_90)));
                            }
                            if (!MansionGrid.isHouse(g4, integer8, integer7 - 1)) {
                                list.add(new WoodlandMansionPiece(this.structureManager, "roof_corner", fx3, bzj.getRotated(Rotation.CLOCKWISE_180)));
                            }
                            else if (MansionGrid.isHouse(g4, integer8 - 1, integer7 - 1)) {
                                final BlockPos fx4 = fx3.relative(bzj.rotate(Direction.SOUTH), 1);
                                list.add(new WoodlandMansionPiece(this.structureManager, "roof_inner_corner", fx4, bzj.getRotated(Rotation.CLOCKWISE_180)));
                            }
                        }
                    }
                }
            }
        }
        
        private void entrance(final List<WoodlandMansionPiece> list, final PlacementData e) {
            final Direction gc4 = e.rotation.rotate(Direction.WEST);
            list.add(new WoodlandMansionPiece(this.structureManager, "entrance", e.position.relative(gc4, 9), e.rotation));
            e.position = e.position.relative(e.rotation.rotate(Direction.SOUTH), 16);
        }
        
        private void traverseWallPiece(final List<WoodlandMansionPiece> list, final PlacementData e) {
            list.add(new WoodlandMansionPiece(this.structureManager, e.wallType, e.position.relative(e.rotation.rotate(Direction.EAST), 7), e.rotation));
            e.position = e.position.relative(e.rotation.rotate(Direction.SOUTH), 8);
        }
        
        private void traverseTurn(final List<WoodlandMansionPiece> list, final PlacementData e) {
            e.position = e.position.relative(e.rotation.rotate(Direction.SOUTH), -1);
            list.add(new WoodlandMansionPiece(this.structureManager, "wall_corner", e.position, e.rotation));
            e.position = e.position.relative(e.rotation.rotate(Direction.SOUTH), -7);
            e.position = e.position.relative(e.rotation.rotate(Direction.WEST), -6);
            e.rotation = e.rotation.getRotated(Rotation.CLOCKWISE_90);
        }
        
        private void traverseInnerTurn(final List<WoodlandMansionPiece> list, final PlacementData e) {
            e.position = e.position.relative(e.rotation.rotate(Direction.SOUTH), 6);
            e.position = e.position.relative(e.rotation.rotate(Direction.EAST), 8);
            e.rotation = e.rotation.getRotated(Rotation.COUNTERCLOCKWISE_90);
        }
        
        private void addRoom1x1(final List<WoodlandMansionPiece> list, final BlockPos fx, final Rotation bzj, final Direction gc, final FloorRoomCollection b) {
            Rotation bzj2 = Rotation.NONE;
            String string8 = b.get1x1(this.random);
            if (gc != Direction.EAST) {
                if (gc == Direction.NORTH) {
                    bzj2 = bzj2.getRotated(Rotation.COUNTERCLOCKWISE_90);
                }
                else if (gc == Direction.WEST) {
                    bzj2 = bzj2.getRotated(Rotation.CLOCKWISE_180);
                }
                else if (gc == Direction.SOUTH) {
                    bzj2 = bzj2.getRotated(Rotation.CLOCKWISE_90);
                }
                else {
                    string8 = b.get1x1Secret(this.random);
                }
            }
            BlockPos fx2 = StructureTemplate.getZeroPositionWithTransform(new BlockPos(1, 0, 0), Mirror.NONE, bzj2, 7, 7);
            bzj2 = bzj2.getRotated(bzj);
            fx2 = fx2.rotate(bzj);
            final BlockPos fx3 = fx.offset(fx2.getX(), 0, fx2.getZ());
            list.add(new WoodlandMansionPiece(this.structureManager, string8, fx3, bzj2));
        }
        
        private void addRoom1x2(final List<WoodlandMansionPiece> list, final BlockPos fx, final Rotation bzj, final Direction gc4, final Direction gc5, final FloorRoomCollection b, final boolean boolean7) {
            if (gc5 == Direction.EAST && gc4 == Direction.SOUTH) {
                final BlockPos fx2 = fx.relative(bzj.rotate(Direction.EAST), 1);
                list.add(new WoodlandMansionPiece(this.structureManager, b.get1x2SideEntrance(this.random, boolean7), fx2, bzj));
            }
            else if (gc5 == Direction.EAST && gc4 == Direction.NORTH) {
                BlockPos fx2 = fx.relative(bzj.rotate(Direction.EAST), 1);
                fx2 = fx2.relative(bzj.rotate(Direction.SOUTH), 6);
                list.add(new WoodlandMansionPiece(this.structureManager, b.get1x2SideEntrance(this.random, boolean7), fx2, bzj, Mirror.LEFT_RIGHT));
            }
            else if (gc5 == Direction.WEST && gc4 == Direction.NORTH) {
                BlockPos fx2 = fx.relative(bzj.rotate(Direction.EAST), 7);
                fx2 = fx2.relative(bzj.rotate(Direction.SOUTH), 6);
                list.add(new WoodlandMansionPiece(this.structureManager, b.get1x2SideEntrance(this.random, boolean7), fx2, bzj.getRotated(Rotation.CLOCKWISE_180)));
            }
            else if (gc5 == Direction.WEST && gc4 == Direction.SOUTH) {
                final BlockPos fx2 = fx.relative(bzj.rotate(Direction.EAST), 7);
                list.add(new WoodlandMansionPiece(this.structureManager, b.get1x2SideEntrance(this.random, boolean7), fx2, bzj, Mirror.FRONT_BACK));
            }
            else if (gc5 == Direction.SOUTH && gc4 == Direction.EAST) {
                final BlockPos fx2 = fx.relative(bzj.rotate(Direction.EAST), 1);
                list.add(new WoodlandMansionPiece(this.structureManager, b.get1x2SideEntrance(this.random, boolean7), fx2, bzj.getRotated(Rotation.CLOCKWISE_90), Mirror.LEFT_RIGHT));
            }
            else if (gc5 == Direction.SOUTH && gc4 == Direction.WEST) {
                final BlockPos fx2 = fx.relative(bzj.rotate(Direction.EAST), 7);
                list.add(new WoodlandMansionPiece(this.structureManager, b.get1x2SideEntrance(this.random, boolean7), fx2, bzj.getRotated(Rotation.CLOCKWISE_90)));
            }
            else if (gc5 == Direction.NORTH && gc4 == Direction.WEST) {
                BlockPos fx2 = fx.relative(bzj.rotate(Direction.EAST), 7);
                fx2 = fx2.relative(bzj.rotate(Direction.SOUTH), 6);
                list.add(new WoodlandMansionPiece(this.structureManager, b.get1x2SideEntrance(this.random, boolean7), fx2, bzj.getRotated(Rotation.CLOCKWISE_90), Mirror.FRONT_BACK));
            }
            else if (gc5 == Direction.NORTH && gc4 == Direction.EAST) {
                BlockPos fx2 = fx.relative(bzj.rotate(Direction.EAST), 1);
                fx2 = fx2.relative(bzj.rotate(Direction.SOUTH), 6);
                list.add(new WoodlandMansionPiece(this.structureManager, b.get1x2SideEntrance(this.random, boolean7), fx2, bzj.getRotated(Rotation.COUNTERCLOCKWISE_90)));
            }
            else if (gc5 == Direction.SOUTH && gc4 == Direction.NORTH) {
                BlockPos fx2 = fx.relative(bzj.rotate(Direction.EAST), 1);
                fx2 = fx2.relative(bzj.rotate(Direction.NORTH), 8);
                list.add(new WoodlandMansionPiece(this.structureManager, b.get1x2FrontEntrance(this.random, boolean7), fx2, bzj));
            }
            else if (gc5 == Direction.NORTH && gc4 == Direction.SOUTH) {
                BlockPos fx2 = fx.relative(bzj.rotate(Direction.EAST), 7);
                fx2 = fx2.relative(bzj.rotate(Direction.SOUTH), 14);
                list.add(new WoodlandMansionPiece(this.structureManager, b.get1x2FrontEntrance(this.random, boolean7), fx2, bzj.getRotated(Rotation.CLOCKWISE_180)));
            }
            else if (gc5 == Direction.WEST && gc4 == Direction.EAST) {
                final BlockPos fx2 = fx.relative(bzj.rotate(Direction.EAST), 15);
                list.add(new WoodlandMansionPiece(this.structureManager, b.get1x2FrontEntrance(this.random, boolean7), fx2, bzj.getRotated(Rotation.CLOCKWISE_90)));
            }
            else if (gc5 == Direction.EAST && gc4 == Direction.WEST) {
                BlockPos fx2 = fx.relative(bzj.rotate(Direction.WEST), 7);
                fx2 = fx2.relative(bzj.rotate(Direction.SOUTH), 6);
                list.add(new WoodlandMansionPiece(this.structureManager, b.get1x2FrontEntrance(this.random, boolean7), fx2, bzj.getRotated(Rotation.COUNTERCLOCKWISE_90)));
            }
            else if (gc5 == Direction.UP && gc4 == Direction.EAST) {
                final BlockPos fx2 = fx.relative(bzj.rotate(Direction.EAST), 15);
                list.add(new WoodlandMansionPiece(this.structureManager, b.get1x2Secret(this.random), fx2, bzj.getRotated(Rotation.CLOCKWISE_90)));
            }
            else if (gc5 == Direction.UP && gc4 == Direction.SOUTH) {
                BlockPos fx2 = fx.relative(bzj.rotate(Direction.EAST), 1);
                fx2 = fx2.relative(bzj.rotate(Direction.NORTH), 0);
                list.add(new WoodlandMansionPiece(this.structureManager, b.get1x2Secret(this.random), fx2, bzj));
            }
        }
        
        private void addRoom2x2(final List<WoodlandMansionPiece> list, final BlockPos fx, final Rotation bzj, final Direction gc4, final Direction gc5, final FloorRoomCollection b) {
            int integer8 = 0;
            int integer9 = 0;
            Rotation bzj2 = bzj;
            Mirror byd11 = Mirror.NONE;
            if (gc5 == Direction.EAST && gc4 == Direction.SOUTH) {
                integer8 = -7;
            }
            else if (gc5 == Direction.EAST && gc4 == Direction.NORTH) {
                integer8 = -7;
                integer9 = 6;
                byd11 = Mirror.LEFT_RIGHT;
            }
            else if (gc5 == Direction.NORTH && gc4 == Direction.EAST) {
                integer8 = 1;
                integer9 = 14;
                bzj2 = bzj.getRotated(Rotation.COUNTERCLOCKWISE_90);
            }
            else if (gc5 == Direction.NORTH && gc4 == Direction.WEST) {
                integer8 = 7;
                integer9 = 14;
                bzj2 = bzj.getRotated(Rotation.COUNTERCLOCKWISE_90);
                byd11 = Mirror.LEFT_RIGHT;
            }
            else if (gc5 == Direction.SOUTH && gc4 == Direction.WEST) {
                integer8 = 7;
                integer9 = -8;
                bzj2 = bzj.getRotated(Rotation.CLOCKWISE_90);
            }
            else if (gc5 == Direction.SOUTH && gc4 == Direction.EAST) {
                integer8 = 1;
                integer9 = -8;
                bzj2 = bzj.getRotated(Rotation.CLOCKWISE_90);
                byd11 = Mirror.LEFT_RIGHT;
            }
            else if (gc5 == Direction.WEST && gc4 == Direction.NORTH) {
                integer8 = 15;
                integer9 = 6;
                bzj2 = bzj.getRotated(Rotation.CLOCKWISE_180);
            }
            else if (gc5 == Direction.WEST && gc4 == Direction.SOUTH) {
                integer8 = 15;
                byd11 = Mirror.FRONT_BACK;
            }
            BlockPos fx2 = fx.relative(bzj.rotate(Direction.EAST), integer8);
            fx2 = fx2.relative(bzj.rotate(Direction.SOUTH), integer9);
            list.add(new WoodlandMansionPiece(this.structureManager, b.get2x2(this.random), fx2, bzj2, byd11));
        }
        
        private void addRoom2x2Secret(final List<WoodlandMansionPiece> list, final BlockPos fx, final Rotation bzj, final FloorRoomCollection b) {
            final BlockPos fx2 = fx.relative(bzj.rotate(Direction.EAST), 1);
            list.add(new WoodlandMansionPiece(this.structureManager, b.get2x2Secret(this.random), fx2, bzj, Mirror.NONE));
        }
    }
    
    static class MansionGrid {
        private final Random random;
        private final SimpleGrid baseGrid;
        private final SimpleGrid thirdFloorGrid;
        private final SimpleGrid[] floorRooms;
        private final int entranceX;
        private final int entranceY;
        
        public MansionGrid(final Random random) {
            this.random = random;
            final int integer3 = 11;
            this.entranceX = 7;
            this.entranceY = 4;
            (this.baseGrid = new SimpleGrid(11, 11, 5)).set(this.entranceX, this.entranceY, this.entranceX + 1, this.entranceY + 1, 3);
            this.baseGrid.set(this.entranceX - 1, this.entranceY, this.entranceX - 1, this.entranceY + 1, 2);
            this.baseGrid.set(this.entranceX + 2, this.entranceY - 2, this.entranceX + 3, this.entranceY + 3, 5);
            this.baseGrid.set(this.entranceX + 1, this.entranceY - 2, this.entranceX + 1, this.entranceY - 1, 1);
            this.baseGrid.set(this.entranceX + 1, this.entranceY + 2, this.entranceX + 1, this.entranceY + 3, 1);
            this.baseGrid.set(this.entranceX - 1, this.entranceY - 1, 1);
            this.baseGrid.set(this.entranceX - 1, this.entranceY + 2, 1);
            this.baseGrid.set(0, 0, 11, 1, 5);
            this.baseGrid.set(0, 9, 11, 11, 5);
            this.recursiveCorridor(this.baseGrid, this.entranceX, this.entranceY - 2, Direction.WEST, 6);
            this.recursiveCorridor(this.baseGrid, this.entranceX, this.entranceY + 3, Direction.WEST, 6);
            this.recursiveCorridor(this.baseGrid, this.entranceX - 2, this.entranceY - 1, Direction.WEST, 3);
            this.recursiveCorridor(this.baseGrid, this.entranceX - 2, this.entranceY + 2, Direction.WEST, 3);
            while (this.cleanEdges(this.baseGrid)) {}
            (this.floorRooms = new SimpleGrid[3])[0] = new SimpleGrid(11, 11, 5);
            this.floorRooms[1] = new SimpleGrid(11, 11, 5);
            this.floorRooms[2] = new SimpleGrid(11, 11, 5);
            this.identifyRooms(this.baseGrid, this.floorRooms[0]);
            this.identifyRooms(this.baseGrid, this.floorRooms[1]);
            this.floorRooms[0].set(this.entranceX + 1, this.entranceY, this.entranceX + 1, this.entranceY + 1, 8388608);
            this.floorRooms[1].set(this.entranceX + 1, this.entranceY, this.entranceX + 1, this.entranceY + 1, 8388608);
            this.thirdFloorGrid = new SimpleGrid(this.baseGrid.width, this.baseGrid.height, 5);
            this.setupThirdFloor();
            this.identifyRooms(this.thirdFloorGrid, this.floorRooms[2]);
        }
        
        public static boolean isHouse(final SimpleGrid g, final int integer2, final int integer3) {
            final int integer4 = g.get(integer2, integer3);
            return integer4 == 1 || integer4 == 2 || integer4 == 3 || integer4 == 4;
        }
        
        public boolean isRoomId(final SimpleGrid g, final int integer2, final int integer3, final int integer4, final int integer5) {
            return (this.floorRooms[integer4].get(integer2, integer3) & 0xFFFF) == integer5;
        }
        
        @Nullable
        public Direction get1x2RoomDirection(final SimpleGrid g, final int integer2, final int integer3, final int integer4, final int integer5) {
            for (final Direction gc8 : Direction.Plane.HORIZONTAL) {
                if (this.isRoomId(g, integer2 + gc8.getStepX(), integer3 + gc8.getStepZ(), integer4, integer5)) {
                    return gc8;
                }
            }
            return null;
        }
        
        private void recursiveCorridor(final SimpleGrid g, final int integer2, final int integer3, final Direction gc, final int integer5) {
            if (integer5 <= 0) {
                return;
            }
            g.set(integer2, integer3, 1);
            g.setif(integer2 + gc.getStepX(), integer3 + gc.getStepZ(), 0, 1);
            for (int integer6 = 0; integer6 < 8; ++integer6) {
                final Direction gc2 = Direction.from2DDataValue(this.random.nextInt(4));
                if (gc2 != gc.getOpposite()) {
                    if (gc2 != Direction.EAST || !this.random.nextBoolean()) {
                        final int integer7 = integer2 + gc.getStepX();
                        final int integer8 = integer3 + gc.getStepZ();
                        if (g.get(integer7 + gc2.getStepX(), integer8 + gc2.getStepZ()) == 0 && g.get(integer7 + gc2.getStepX() * 2, integer8 + gc2.getStepZ() * 2) == 0) {
                            this.recursiveCorridor(g, integer2 + gc.getStepX() + gc2.getStepX(), integer3 + gc.getStepZ() + gc2.getStepZ(), gc2, integer5 - 1);
                            break;
                        }
                    }
                }
            }
            final Direction gc3 = gc.getClockWise();
            final Direction gc2 = gc.getCounterClockWise();
            g.setif(integer2 + gc3.getStepX(), integer3 + gc3.getStepZ(), 0, 2);
            g.setif(integer2 + gc2.getStepX(), integer3 + gc2.getStepZ(), 0, 2);
            g.setif(integer2 + gc.getStepX() + gc3.getStepX(), integer3 + gc.getStepZ() + gc3.getStepZ(), 0, 2);
            g.setif(integer2 + gc.getStepX() + gc2.getStepX(), integer3 + gc.getStepZ() + gc2.getStepZ(), 0, 2);
            g.setif(integer2 + gc.getStepX() * 2, integer3 + gc.getStepZ() * 2, 0, 2);
            g.setif(integer2 + gc3.getStepX() * 2, integer3 + gc3.getStepZ() * 2, 0, 2);
            g.setif(integer2 + gc2.getStepX() * 2, integer3 + gc2.getStepZ() * 2, 0, 2);
        }
        
        private boolean cleanEdges(final SimpleGrid g) {
            boolean boolean3 = false;
            for (int integer4 = 0; integer4 < g.height; ++integer4) {
                for (int integer5 = 0; integer5 < g.width; ++integer5) {
                    if (g.get(integer5, integer4) == 0) {
                        int integer6 = 0;
                        integer6 += (isHouse(g, integer5 + 1, integer4) ? 1 : 0);
                        integer6 += (isHouse(g, integer5 - 1, integer4) ? 1 : 0);
                        integer6 += (isHouse(g, integer5, integer4 + 1) ? 1 : 0);
                        integer6 += (isHouse(g, integer5, integer4 - 1) ? 1 : 0);
                        if (integer6 >= 3) {
                            g.set(integer5, integer4, 2);
                            boolean3 = true;
                        }
                        else if (integer6 == 2) {
                            int integer7 = 0;
                            integer7 += (isHouse(g, integer5 + 1, integer4 + 1) ? 1 : 0);
                            integer7 += (isHouse(g, integer5 - 1, integer4 + 1) ? 1 : 0);
                            integer7 += (isHouse(g, integer5 + 1, integer4 - 1) ? 1 : 0);
                            integer7 += (isHouse(g, integer5 - 1, integer4 - 1) ? 1 : 0);
                            if (integer7 <= 1) {
                                g.set(integer5, integer4, 2);
                                boolean3 = true;
                            }
                        }
                    }
                }
            }
            return boolean3;
        }
        
        private void setupThirdFloor() {
            final List<Tuple<Integer, Integer>> list2 = (List<Tuple<Integer, Integer>>)Lists.newArrayList();
            final SimpleGrid g3 = this.floorRooms[1];
            for (int integer4 = 0; integer4 < this.thirdFloorGrid.height; ++integer4) {
                for (int integer5 = 0; integer5 < this.thirdFloorGrid.width; ++integer5) {
                    final int integer6 = g3.get(integer5, integer4);
                    final int integer7 = integer6 & 0xF0000;
                    if (integer7 == 131072 && (integer6 & 0x200000) == 0x200000) {
                        list2.add(new Tuple(integer5, integer4));
                    }
                }
            }
            if (list2.isEmpty()) {
                this.thirdFloorGrid.set(0, 0, this.thirdFloorGrid.width, this.thirdFloorGrid.height, 5);
                return;
            }
            final Tuple<Integer, Integer> afs4 = (Tuple<Integer, Integer>)list2.get(this.random.nextInt(list2.size()));
            int integer5 = g3.get(afs4.getA(), afs4.getB());
            g3.set(afs4.getA(), afs4.getB(), integer5 | 0x400000);
            final Direction gc6 = this.get1x2RoomDirection(this.baseGrid, afs4.getA(), afs4.getB(), 1, integer5 & 0xFFFF);
            final int integer7 = afs4.getA() + gc6.getStepX();
            final int integer8 = afs4.getB() + gc6.getStepZ();
            for (int integer9 = 0; integer9 < this.thirdFloorGrid.height; ++integer9) {
                for (int integer10 = 0; integer10 < this.thirdFloorGrid.width; ++integer10) {
                    if (!isHouse(this.baseGrid, integer10, integer9)) {
                        this.thirdFloorGrid.set(integer10, integer9, 5);
                    }
                    else if (integer10 == afs4.getA() && integer9 == afs4.getB()) {
                        this.thirdFloorGrid.set(integer10, integer9, 3);
                    }
                    else if (integer10 == integer7 && integer9 == integer8) {
                        this.thirdFloorGrid.set(integer10, integer9, 3);
                        this.floorRooms[2].set(integer10, integer9, 8388608);
                    }
                }
            }
            final List<Direction> list3 = (List<Direction>)Lists.newArrayList();
            for (final Direction gc7 : Direction.Plane.HORIZONTAL) {
                if (this.thirdFloorGrid.get(integer7 + gc7.getStepX(), integer8 + gc7.getStepZ()) == 0) {
                    list3.add(gc7);
                }
            }
            if (list3.isEmpty()) {
                this.thirdFloorGrid.set(0, 0, this.thirdFloorGrid.width, this.thirdFloorGrid.height, 5);
                g3.set(afs4.getA(), afs4.getB(), integer5);
                return;
            }
            final Direction gc8 = (Direction)list3.get(this.random.nextInt(list3.size()));
            this.recursiveCorridor(this.thirdFloorGrid, integer7 + gc8.getStepX(), integer8 + gc8.getStepZ(), gc8, 4);
            while (this.cleanEdges(this.thirdFloorGrid)) {}
        }
        
        private void identifyRooms(final SimpleGrid g1, final SimpleGrid g2) {
            final List<Tuple<Integer, Integer>> list4 = (List<Tuple<Integer, Integer>>)Lists.newArrayList();
            for (int integer5 = 0; integer5 < g1.height; ++integer5) {
                for (int integer6 = 0; integer6 < g1.width; ++integer6) {
                    if (g1.get(integer6, integer5) == 2) {
                        list4.add(new Tuple(integer6, integer5));
                    }
                }
            }
            Collections.shuffle((List)list4, this.random);
            int integer5 = 10;
            for (final Tuple<Integer, Integer> afs7 : list4) {
                final int integer7 = afs7.getA();
                final int integer8 = afs7.getB();
                if (g2.get(integer7, integer8) == 0) {
                    int integer9 = integer7;
                    int integer10 = integer7;
                    int integer11 = integer8;
                    int integer12 = integer8;
                    int integer13 = 65536;
                    if (g2.get(integer7 + 1, integer8) == 0 && g2.get(integer7, integer8 + 1) == 0 && g2.get(integer7 + 1, integer8 + 1) == 0 && g1.get(integer7 + 1, integer8) == 2 && g1.get(integer7, integer8 + 1) == 2 && g1.get(integer7 + 1, integer8 + 1) == 2) {
                        ++integer10;
                        ++integer12;
                        integer13 = 262144;
                    }
                    else if (g2.get(integer7 - 1, integer8) == 0 && g2.get(integer7, integer8 + 1) == 0 && g2.get(integer7 - 1, integer8 + 1) == 0 && g1.get(integer7 - 1, integer8) == 2 && g1.get(integer7, integer8 + 1) == 2 && g1.get(integer7 - 1, integer8 + 1) == 2) {
                        --integer9;
                        ++integer12;
                        integer13 = 262144;
                    }
                    else if (g2.get(integer7 - 1, integer8) == 0 && g2.get(integer7, integer8 - 1) == 0 && g2.get(integer7 - 1, integer8 - 1) == 0 && g1.get(integer7 - 1, integer8) == 2 && g1.get(integer7, integer8 - 1) == 2 && g1.get(integer7 - 1, integer8 - 1) == 2) {
                        --integer9;
                        --integer11;
                        integer13 = 262144;
                    }
                    else if (g2.get(integer7 + 1, integer8) == 0 && g1.get(integer7 + 1, integer8) == 2) {
                        ++integer10;
                        integer13 = 131072;
                    }
                    else if (g2.get(integer7, integer8 + 1) == 0 && g1.get(integer7, integer8 + 1) == 2) {
                        ++integer12;
                        integer13 = 131072;
                    }
                    else if (g2.get(integer7 - 1, integer8) == 0 && g1.get(integer7 - 1, integer8) == 2) {
                        --integer9;
                        integer13 = 131072;
                    }
                    else if (g2.get(integer7, integer8 - 1) == 0 && g1.get(integer7, integer8 - 1) == 2) {
                        --integer11;
                        integer13 = 131072;
                    }
                    int integer14 = this.random.nextBoolean() ? integer9 : integer10;
                    int integer15 = this.random.nextBoolean() ? integer11 : integer12;
                    int integer16 = 2097152;
                    if (!g1.edgesTo(integer14, integer15, 1)) {
                        integer14 = ((integer14 == integer9) ? integer10 : integer9);
                        integer15 = ((integer15 == integer11) ? integer12 : integer11);
                        if (!g1.edgesTo(integer14, integer15, 1)) {
                            integer15 = ((integer15 == integer11) ? integer12 : integer11);
                            if (!g1.edgesTo(integer14, integer15, 1)) {
                                integer14 = ((integer14 == integer9) ? integer10 : integer9);
                                integer15 = ((integer15 == integer11) ? integer12 : integer11);
                                if (!g1.edgesTo(integer14, integer15, 1)) {
                                    integer16 = 0;
                                    integer14 = integer9;
                                    integer15 = integer11;
                                }
                            }
                        }
                    }
                    for (int integer17 = integer11; integer17 <= integer12; ++integer17) {
                        for (int integer18 = integer9; integer18 <= integer10; ++integer18) {
                            if (integer18 == integer14 && integer17 == integer15) {
                                g2.set(integer18, integer17, 0x100000 | integer16 | integer13 | integer5);
                            }
                            else {
                                g2.set(integer18, integer17, integer13 | integer5);
                            }
                        }
                    }
                    ++integer5;
                }
            }
        }
    }
    
    static class SimpleGrid {
        private final int[][] grid;
        private final int width;
        private final int height;
        private final int valueIfOutside;
        
        public SimpleGrid(final int integer1, final int integer2, final int integer3) {
            this.width = integer1;
            this.height = integer2;
            this.valueIfOutside = integer3;
            this.grid = new int[integer1][integer2];
        }
        
        public void set(final int integer1, final int integer2, final int integer3) {
            if (integer1 >= 0 && integer1 < this.width && integer2 >= 0 && integer2 < this.height) {
                this.grid[integer1][integer2] = integer3;
            }
        }
        
        public void set(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5) {
            for (int integer6 = integer2; integer6 <= integer4; ++integer6) {
                for (int integer7 = integer1; integer7 <= integer3; ++integer7) {
                    this.set(integer7, integer6, integer5);
                }
            }
        }
        
        public int get(final int integer1, final int integer2) {
            if (integer1 >= 0 && integer1 < this.width && integer2 >= 0 && integer2 < this.height) {
                return this.grid[integer1][integer2];
            }
            return this.valueIfOutside;
        }
        
        public void setif(final int integer1, final int integer2, final int integer3, final int integer4) {
            if (this.get(integer1, integer2) == integer3) {
                this.set(integer1, integer2, integer4);
            }
        }
        
        public boolean edgesTo(final int integer1, final int integer2, final int integer3) {
            return this.get(integer1 - 1, integer2) == integer3 || this.get(integer1 + 1, integer2) == integer3 || this.get(integer1, integer2 + 1) == integer3 || this.get(integer1, integer2 - 1) == integer3;
        }
    }
    
    abstract static class FloorRoomCollection {
        private FloorRoomCollection() {
        }
        
        public abstract String get1x1(final Random random);
        
        public abstract String get1x1Secret(final Random random);
        
        public abstract String get1x2SideEntrance(final Random random, final boolean boolean2);
        
        public abstract String get1x2FrontEntrance(final Random random, final boolean boolean2);
        
        public abstract String get1x2Secret(final Random random);
        
        public abstract String get2x2(final Random random);
        
        public abstract String get2x2Secret(final Random random);
    }
    
    static class FirstFloorRoomCollection extends FloorRoomCollection {
        private FirstFloorRoomCollection() {
        }
        
        @Override
        public String get1x1(final Random random) {
            return new StringBuilder().append("1x1_a").append(random.nextInt(5) + 1).toString();
        }
        
        @Override
        public String get1x1Secret(final Random random) {
            return new StringBuilder().append("1x1_as").append(random.nextInt(4) + 1).toString();
        }
        
        @Override
        public String get1x2SideEntrance(final Random random, final boolean boolean2) {
            return new StringBuilder().append("1x2_a").append(random.nextInt(9) + 1).toString();
        }
        
        @Override
        public String get1x2FrontEntrance(final Random random, final boolean boolean2) {
            return new StringBuilder().append("1x2_b").append(random.nextInt(5) + 1).toString();
        }
        
        @Override
        public String get1x2Secret(final Random random) {
            return new StringBuilder().append("1x2_s").append(random.nextInt(2) + 1).toString();
        }
        
        @Override
        public String get2x2(final Random random) {
            return new StringBuilder().append("2x2_a").append(random.nextInt(4) + 1).toString();
        }
        
        @Override
        public String get2x2Secret(final Random random) {
            return "2x2_s1";
        }
    }
    
    static class SecondFloorRoomCollection extends FloorRoomCollection {
        private SecondFloorRoomCollection() {
        }
        
        @Override
        public String get1x1(final Random random) {
            return new StringBuilder().append("1x1_b").append(random.nextInt(4) + 1).toString();
        }
        
        @Override
        public String get1x1Secret(final Random random) {
            return new StringBuilder().append("1x1_as").append(random.nextInt(4) + 1).toString();
        }
        
        @Override
        public String get1x2SideEntrance(final Random random, final boolean boolean2) {
            if (boolean2) {
                return "1x2_c_stairs";
            }
            return new StringBuilder().append("1x2_c").append(random.nextInt(4) + 1).toString();
        }
        
        @Override
        public String get1x2FrontEntrance(final Random random, final boolean boolean2) {
            if (boolean2) {
                return "1x2_d_stairs";
            }
            return new StringBuilder().append("1x2_d").append(random.nextInt(5) + 1).toString();
        }
        
        @Override
        public String get1x2Secret(final Random random) {
            return new StringBuilder().append("1x2_se").append(random.nextInt(1) + 1).toString();
        }
        
        @Override
        public String get2x2(final Random random) {
            return new StringBuilder().append("2x2_b").append(random.nextInt(5) + 1).toString();
        }
        
        @Override
        public String get2x2Secret(final Random random) {
            return "2x2_s1";
        }
    }
    
    static class ThirdFloorRoomCollection extends SecondFloorRoomCollection {
        private ThirdFloorRoomCollection() {
        }
    }
}

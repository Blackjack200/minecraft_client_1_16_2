package net.minecraft.world.level.levelgen.structure;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import java.util.Iterator;
import java.util.Collection;
import com.google.common.collect.Lists;
import java.util.Random;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.util.Tuple;
import java.util.List;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

public class EndCityPieces {
    private static final StructurePlaceSettings OVERWRITE;
    private static final StructurePlaceSettings INSERT;
    private static final SectionGenerator HOUSE_TOWER_GENERATOR;
    private static final List<Tuple<Rotation, BlockPos>> TOWER_BRIDGES;
    private static final SectionGenerator TOWER_GENERATOR;
    private static final SectionGenerator TOWER_BRIDGE_GENERATOR;
    private static final List<Tuple<Rotation, BlockPos>> FAT_TOWER_BRIDGES;
    private static final SectionGenerator FAT_TOWER_GENERATOR;
    
    private static EndCityPiece addPiece(final StructureManager cst, final EndCityPiece a, final BlockPos fx, final String string, final Rotation bzj, final boolean boolean6) {
        final EndCityPiece a2 = new EndCityPiece(cst, string, a.templatePosition, bzj, boolean6);
        final BlockPos fx2 = a.template.calculateConnectedPosition(a.placeSettings, fx, a2.placeSettings, BlockPos.ZERO);
        a2.move(fx2.getX(), fx2.getY(), fx2.getZ());
        return a2;
    }
    
    public static void startHouseTower(final StructureManager cst, final BlockPos fx, final Rotation bzj, final List<StructurePiece> list, final Random random) {
        EndCityPieces.FAT_TOWER_GENERATOR.init();
        EndCityPieces.HOUSE_TOWER_GENERATOR.init();
        EndCityPieces.TOWER_BRIDGE_GENERATOR.init();
        EndCityPieces.TOWER_GENERATOR.init();
        EndCityPiece a6 = addHelper(list, new EndCityPiece(cst, "base_floor", fx, bzj, true));
        a6 = addHelper(list, addPiece(cst, a6, new BlockPos(-1, 0, -1), "second_floor_1", bzj, false));
        a6 = addHelper(list, addPiece(cst, a6, new BlockPos(-1, 4, -1), "third_floor_1", bzj, false));
        a6 = addHelper(list, addPiece(cst, a6, new BlockPos(-1, 8, -1), "third_roof", bzj, true));
        recursiveChildren(cst, EndCityPieces.TOWER_GENERATOR, 1, a6, null, list, random);
    }
    
    private static EndCityPiece addHelper(final List<StructurePiece> list, final EndCityPiece a) {
        list.add(a);
        return a;
    }
    
    private static boolean recursiveChildren(final StructureManager cst, final SectionGenerator b, final int integer, final EndCityPiece a, final BlockPos fx, final List<StructurePiece> list, final Random random) {
        if (integer > 8) {
            return false;
        }
        final List<StructurePiece> list2 = (List<StructurePiece>)Lists.newArrayList();
        if (b.generate(cst, integer, a, fx, list2, random)) {
            boolean boolean9 = false;
            final int integer2 = random.nextInt();
            for (final StructurePiece crr12 : list2) {
                crr12.genDepth = integer2;
                final StructurePiece crr13 = StructurePiece.findCollisionPiece(list, crr12.getBoundingBox());
                if (crr13 != null && crr13.genDepth != a.genDepth) {
                    boolean9 = true;
                    break;
                }
            }
            if (!boolean9) {
                list.addAll((Collection)list2);
                return true;
            }
        }
        return false;
    }
    
    static {
        OVERWRITE = new StructurePlaceSettings().setIgnoreEntities(true).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        INSERT = new StructurePlaceSettings().setIgnoreEntities(true).addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
        HOUSE_TOWER_GENERATOR = new SectionGenerator() {
            public void init() {
            }
            
            public boolean generate(final StructureManager cst, final int integer, final EndCityPiece a, final BlockPos fx, final List<StructurePiece> list, final Random random) {
                if (integer > 8) {
                    return false;
                }
                final Rotation bzj8 = a.placeSettings.getRotation();
                EndCityPiece a2 = addHelper(list, addPiece(cst, a, fx, "base_floor", bzj8, true));
                final int integer2 = random.nextInt(3);
                if (integer2 == 0) {
                    a2 = addHelper(list, addPiece(cst, a2, new BlockPos(-1, 4, -1), "base_roof", bzj8, true));
                }
                else if (integer2 == 1) {
                    a2 = addHelper(list, addPiece(cst, a2, new BlockPos(-1, 0, -1), "second_floor_2", bzj8, false));
                    a2 = addHelper(list, addPiece(cst, a2, new BlockPos(-1, 8, -1), "second_roof", bzj8, false));
                    recursiveChildren(cst, EndCityPieces.TOWER_GENERATOR, integer + 1, a2, null, list, random);
                }
                else if (integer2 == 2) {
                    a2 = addHelper(list, addPiece(cst, a2, new BlockPos(-1, 0, -1), "second_floor_2", bzj8, false));
                    a2 = addHelper(list, addPiece(cst, a2, new BlockPos(-1, 4, -1), "third_floor_2", bzj8, false));
                    a2 = addHelper(list, addPiece(cst, a2, new BlockPos(-1, 8, -1), "third_roof", bzj8, true));
                    recursiveChildren(cst, EndCityPieces.TOWER_GENERATOR, integer + 1, a2, null, list, random);
                }
                return true;
            }
        };
        TOWER_BRIDGES = (List)Lists.newArrayList((Object[])new Tuple[] { new Tuple((A)Rotation.NONE, (B)new BlockPos(1, -1, 0)), new Tuple((A)Rotation.CLOCKWISE_90, (B)new BlockPos(6, -1, 1)), new Tuple((A)Rotation.COUNTERCLOCKWISE_90, (B)new BlockPos(0, -1, 5)), new Tuple((A)Rotation.CLOCKWISE_180, (B)new BlockPos(5, -1, 6)) });
        TOWER_GENERATOR = new SectionGenerator() {
            public void init() {
            }
            
            public boolean generate(final StructureManager cst, final int integer, final EndCityPiece a, final BlockPos fx, final List<StructurePiece> list, final Random random) {
                final Rotation bzj8 = a.placeSettings.getRotation();
                EndCityPiece a2 = a;
                a2 = addHelper(list, addPiece(cst, a2, new BlockPos(3 + random.nextInt(2), -3, 3 + random.nextInt(2)), "tower_base", bzj8, true));
                a2 = addHelper(list, addPiece(cst, a2, new BlockPos(0, 7, 0), "tower_piece", bzj8, true));
                EndCityPiece a3 = (random.nextInt(3) == 0) ? a2 : null;
                for (int integer2 = 1 + random.nextInt(3), integer3 = 0; integer3 < integer2; ++integer3) {
                    a2 = addHelper(list, addPiece(cst, a2, new BlockPos(0, 4, 0), "tower_piece", bzj8, true));
                    if (integer3 < integer2 - 1 && random.nextBoolean()) {
                        a3 = a2;
                    }
                }
                if (a3 != null) {
                    for (final Tuple<Rotation, BlockPos> afs13 : EndCityPieces.TOWER_BRIDGES) {
                        if (random.nextBoolean()) {
                            final EndCityPiece a4 = addHelper(list, addPiece(cst, a3, (BlockPos)afs13.getB(), "bridge_end", bzj8.getRotated(afs13.getA()), true));
                            recursiveChildren(cst, EndCityPieces.TOWER_BRIDGE_GENERATOR, integer + 1, a4, null, list, random);
                        }
                    }
                    a2 = addHelper(list, addPiece(cst, a2, new BlockPos(-1, 4, -1), "tower_top", bzj8, true));
                }
                else {
                    if (integer != 7) {
                        return recursiveChildren(cst, EndCityPieces.FAT_TOWER_GENERATOR, integer + 1, a2, null, list, random);
                    }
                    a2 = addHelper(list, addPiece(cst, a2, new BlockPos(-1, 4, -1), "tower_top", bzj8, true));
                }
                return true;
            }
        };
        TOWER_BRIDGE_GENERATOR = new SectionGenerator() {
            public boolean shipCreated;
            
            public void init() {
                this.shipCreated = false;
            }
            
            public boolean generate(final StructureManager cst, final int integer, final EndCityPiece a, final BlockPos fx, final List<StructurePiece> list, final Random random) {
                final Rotation bzj8 = a.placeSettings.getRotation();
                final int integer2 = random.nextInt(4) + 1;
                EndCityPiece a2 = addHelper(list, addPiece(cst, a, new BlockPos(0, 0, -4), "bridge_piece", bzj8, true));
                a2.genDepth = -1;
                int integer3 = 0;
                for (int integer4 = 0; integer4 < integer2; ++integer4) {
                    if (random.nextBoolean()) {
                        a2 = addHelper(list, addPiece(cst, a2, new BlockPos(0, integer3, -4), "bridge_piece", bzj8, true));
                        integer3 = 0;
                    }
                    else {
                        if (random.nextBoolean()) {
                            a2 = addHelper(list, addPiece(cst, a2, new BlockPos(0, integer3, -4), "bridge_steep_stairs", bzj8, true));
                        }
                        else {
                            a2 = addHelper(list, addPiece(cst, a2, new BlockPos(0, integer3, -8), "bridge_gentle_stairs", bzj8, true));
                        }
                        integer3 = 4;
                    }
                }
                if (this.shipCreated || random.nextInt(10 - integer) != 0) {
                    if (!recursiveChildren(cst, EndCityPieces.HOUSE_TOWER_GENERATOR, integer + 1, a2, new BlockPos(-3, integer3 + 1, -11), list, random)) {
                        return false;
                    }
                }
                else {
                    addHelper(list, addPiece(cst, a2, new BlockPos(-8 + random.nextInt(8), integer3, -70 + random.nextInt(10)), "ship", bzj8, true));
                    this.shipCreated = true;
                }
                a2 = addHelper(list, addPiece(cst, a2, new BlockPos(4, integer3, 0), "bridge_end", bzj8.getRotated(Rotation.CLOCKWISE_180), true));
                a2.genDepth = -1;
                return true;
            }
        };
        FAT_TOWER_BRIDGES = (List)Lists.newArrayList((Object[])new Tuple[] { new Tuple((A)Rotation.NONE, (B)new BlockPos(4, -1, 0)), new Tuple((A)Rotation.CLOCKWISE_90, (B)new BlockPos(12, -1, 4)), new Tuple((A)Rotation.COUNTERCLOCKWISE_90, (B)new BlockPos(0, -1, 8)), new Tuple((A)Rotation.CLOCKWISE_180, (B)new BlockPos(8, -1, 12)) });
        FAT_TOWER_GENERATOR = new SectionGenerator() {
            public void init() {
            }
            
            public boolean generate(final StructureManager cst, final int integer, final EndCityPiece a, final BlockPos fx, final List<StructurePiece> list, final Random random) {
                final Rotation bzj9 = a.placeSettings.getRotation();
                EndCityPiece a2 = addHelper(list, addPiece(cst, a, new BlockPos(-3, 4, -3), "fat_tower_base", bzj9, true));
                a2 = addHelper(list, addPiece(cst, a2, new BlockPos(0, 4, 0), "fat_tower_middle", bzj9, true));
                for (int integer2 = 0; integer2 < 2 && random.nextInt(3) != 0; ++integer2) {
                    a2 = addHelper(list, addPiece(cst, a2, new BlockPos(0, 8, 0), "fat_tower_middle", bzj9, true));
                    for (final Tuple<Rotation, BlockPos> afs12 : EndCityPieces.FAT_TOWER_BRIDGES) {
                        if (random.nextBoolean()) {
                            final EndCityPiece a3 = addHelper(list, addPiece(cst, a2, (BlockPos)afs12.getB(), "bridge_end", bzj9.getRotated(afs12.getA()), true));
                            recursiveChildren(cst, EndCityPieces.TOWER_BRIDGE_GENERATOR, integer + 1, a3, null, list, random);
                        }
                    }
                }
                a2 = addHelper(list, addPiece(cst, a2, new BlockPos(-2, 8, -2), "fat_tower_top", bzj9, true));
                return true;
            }
        };
    }
    
    public static class EndCityPiece extends TemplateStructurePiece {
        private final String templateName;
        private final Rotation rotation;
        private final boolean overwrite;
        
        public EndCityPiece(final StructureManager cst, final String string, final BlockPos fx, final Rotation bzj, final boolean boolean5) {
            super(StructurePieceType.END_CITY_PIECE, 0);
            this.templateName = string;
            this.templatePosition = fx;
            this.rotation = bzj;
            this.overwrite = boolean5;
            this.loadTemplate(cst);
        }
        
        public EndCityPiece(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.END_CITY_PIECE, md);
            this.templateName = md.getString("Template");
            this.rotation = Rotation.valueOf(md.getString("Rot"));
            this.overwrite = md.getBoolean("OW");
            this.loadTemplate(cst);
        }
        
        private void loadTemplate(final StructureManager cst) {
            final StructureTemplate csy3 = cst.getOrCreate(new ResourceLocation("end_city/" + this.templateName));
            final StructurePlaceSettings csu4 = (this.overwrite ? EndCityPieces.OVERWRITE : EndCityPieces.INSERT).copy().setRotation(this.rotation);
            this.setup(csy3, this.templatePosition, csu4);
        }
        
        @Override
        protected void addAdditionalSaveData(final CompoundTag md) {
            super.addAdditionalSaveData(md);
            md.putString("Template", this.templateName);
            md.putString("Rot", this.rotation.name());
            md.putBoolean("OW", this.overwrite);
        }
        
        @Override
        protected void handleDataMarker(final String string, final BlockPos fx, final ServerLevelAccessor bsh, final Random random, final BoundingBox cqx) {
            if (string.startsWith("Chest")) {
                final BlockPos fx2 = fx.below();
                if (cqx.isInside(fx2)) {
                    RandomizableContainerBlockEntity.setLootTable(bsh, random, fx2, BuiltInLootTables.END_CITY_TREASURE);
                }
            }
            else if (string.startsWith("Sentry")) {
                final Shulker bdt7 = EntityType.SHULKER.create(bsh.getLevel());
                bdt7.setPos(fx.getX() + 0.5, fx.getY() + 0.5, fx.getZ() + 0.5);
                bdt7.setAttachPosition(fx);
                bsh.addFreshEntity(bdt7);
            }
            else if (string.startsWith("Elytra")) {
                final ItemFrame bcm7 = new ItemFrame(bsh.getLevel(), fx, this.rotation.rotate(Direction.SOUTH));
                bcm7.setItem(new ItemStack(Items.ELYTRA), false);
                bsh.addFreshEntity(bcm7);
            }
        }
    }
    
    interface SectionGenerator {
        void init();
        
        boolean generate(final StructureManager cst, final int integer, final EndCityPiece a, final BlockPos fx, final List<StructurePiece> list, final Random random);
    }
}

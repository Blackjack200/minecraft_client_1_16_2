package net.minecraft.world.level.levelgen.structure;

import java.util.Iterator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.Util;
import net.minecraft.world.level.levelgen.feature.configurations.ShipwreckConfiguration;
import java.util.Random;
import java.util.List;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;

public class ShipwreckPieces {
    private static final BlockPos PIVOT;
    private static final ResourceLocation[] STRUCTURE_LOCATION_BEACHED;
    private static final ResourceLocation[] STRUCTURE_LOCATION_OCEAN;
    
    public static void addPieces(final StructureManager cst, final BlockPos fx, final Rotation bzj, final List<StructurePiece> list, final Random random, final ShipwreckConfiguration cmp) {
        final ResourceLocation vk7 = Util.<ResourceLocation>getRandom(cmp.isBeached ? ShipwreckPieces.STRUCTURE_LOCATION_BEACHED : ShipwreckPieces.STRUCTURE_LOCATION_OCEAN, random);
        list.add(new ShipwreckPiece(cst, vk7, fx, bzj, cmp.isBeached));
    }
    
    static {
        PIVOT = new BlockPos(4, 0, 15);
        STRUCTURE_LOCATION_BEACHED = new ResourceLocation[] { new ResourceLocation("shipwreck/with_mast"), new ResourceLocation("shipwreck/sideways_full"), new ResourceLocation("shipwreck/sideways_fronthalf"), new ResourceLocation("shipwreck/sideways_backhalf"), new ResourceLocation("shipwreck/rightsideup_full"), new ResourceLocation("shipwreck/rightsideup_fronthalf"), new ResourceLocation("shipwreck/rightsideup_backhalf"), new ResourceLocation("shipwreck/with_mast_degraded"), new ResourceLocation("shipwreck/rightsideup_full_degraded"), new ResourceLocation("shipwreck/rightsideup_fronthalf_degraded"), new ResourceLocation("shipwreck/rightsideup_backhalf_degraded") };
        STRUCTURE_LOCATION_OCEAN = new ResourceLocation[] { new ResourceLocation("shipwreck/with_mast"), new ResourceLocation("shipwreck/upsidedown_full"), new ResourceLocation("shipwreck/upsidedown_fronthalf"), new ResourceLocation("shipwreck/upsidedown_backhalf"), new ResourceLocation("shipwreck/sideways_full"), new ResourceLocation("shipwreck/sideways_fronthalf"), new ResourceLocation("shipwreck/sideways_backhalf"), new ResourceLocation("shipwreck/rightsideup_full"), new ResourceLocation("shipwreck/rightsideup_fronthalf"), new ResourceLocation("shipwreck/rightsideup_backhalf"), new ResourceLocation("shipwreck/with_mast_degraded"), new ResourceLocation("shipwreck/upsidedown_full_degraded"), new ResourceLocation("shipwreck/upsidedown_fronthalf_degraded"), new ResourceLocation("shipwreck/upsidedown_backhalf_degraded"), new ResourceLocation("shipwreck/sideways_full_degraded"), new ResourceLocation("shipwreck/sideways_fronthalf_degraded"), new ResourceLocation("shipwreck/sideways_backhalf_degraded"), new ResourceLocation("shipwreck/rightsideup_full_degraded"), new ResourceLocation("shipwreck/rightsideup_fronthalf_degraded"), new ResourceLocation("shipwreck/rightsideup_backhalf_degraded") };
    }
    
    public static class ShipwreckPiece extends TemplateStructurePiece {
        private final Rotation rotation;
        private final ResourceLocation templateLocation;
        private final boolean isBeached;
        
        public ShipwreckPiece(final StructureManager cst, final ResourceLocation vk, final BlockPos fx, final Rotation bzj, final boolean boolean5) {
            super(StructurePieceType.SHIPWRECK_PIECE, 0);
            this.templatePosition = fx;
            this.rotation = bzj;
            this.templateLocation = vk;
            this.isBeached = boolean5;
            this.loadTemplate(cst);
        }
        
        public ShipwreckPiece(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.SHIPWRECK_PIECE, md);
            this.templateLocation = new ResourceLocation(md.getString("Template"));
            this.isBeached = md.getBoolean("isBeached");
            this.rotation = Rotation.valueOf(md.getString("Rot"));
            this.loadTemplate(cst);
        }
        
        @Override
        protected void addAdditionalSaveData(final CompoundTag md) {
            super.addAdditionalSaveData(md);
            md.putString("Template", this.templateLocation.toString());
            md.putBoolean("isBeached", this.isBeached);
            md.putString("Rot", this.rotation.name());
        }
        
        private void loadTemplate(final StructureManager cst) {
            final StructureTemplate csy3 = cst.getOrCreate(this.templateLocation);
            final StructurePlaceSettings csu4 = new StructurePlaceSettings().setRotation(this.rotation).setMirror(Mirror.NONE).setRotationPivot(ShipwreckPieces.PIVOT).addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
            this.setup(csy3, this.templatePosition, csu4);
        }
        
        @Override
        protected void handleDataMarker(final String string, final BlockPos fx, final ServerLevelAccessor bsh, final Random random, final BoundingBox cqx) {
            if ("map_chest".equals(string)) {
                RandomizableContainerBlockEntity.setLootTable(bsh, random, fx.below(), BuiltInLootTables.SHIPWRECK_MAP);
            }
            else if ("treasure_chest".equals(string)) {
                RandomizableContainerBlockEntity.setLootTable(bsh, random, fx.below(), BuiltInLootTables.SHIPWRECK_TREASURE);
            }
            else if ("supply_chest".equals(string)) {
                RandomizableContainerBlockEntity.setLootTable(bsh, random, fx.below(), BuiltInLootTables.SHIPWRECK_SUPPLY);
            }
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            int integer9 = 256;
            int integer10 = 0;
            final BlockPos fx2 = this.template.getSize();
            final Heightmap.Types a12 = this.isBeached ? Heightmap.Types.WORLD_SURFACE_WG : Heightmap.Types.OCEAN_FLOOR_WG;
            final int integer11 = fx2.getX() * fx2.getZ();
            if (integer11 == 0) {
                integer10 = bso.getHeight(a12, this.templatePosition.getX(), this.templatePosition.getZ());
            }
            else {
                final BlockPos fx3 = this.templatePosition.offset(fx2.getX() - 1, 0, fx2.getZ() - 1);
                for (final BlockPos fx4 : BlockPos.betweenClosed(this.templatePosition, fx3)) {
                    final int integer12 = bso.getHeight(a12, fx4.getX(), fx4.getZ());
                    integer10 += integer12;
                    integer9 = Math.min(integer9, integer12);
                }
                integer10 /= integer11;
            }
            final int integer13 = this.isBeached ? (integer9 - fx2.getY() / 2 - random.nextInt(3)) : integer10;
            this.templatePosition = new BlockPos(this.templatePosition.getX(), integer13, this.templatePosition.getZ());
            return super.postProcess(bso, bsk, cfv, random, cqx, bra, fx);
        }
    }
}

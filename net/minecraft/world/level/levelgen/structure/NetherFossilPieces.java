package net.minecraft.world.level.levelgen.structure;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.Util;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import java.util.Random;
import java.util.List;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.resources.ResourceLocation;

public class NetherFossilPieces {
    private static final ResourceLocation[] FOSSILS;
    
    public static void addPieces(final StructureManager cst, final List<StructurePiece> list, final Random random, final BlockPos fx) {
        final Rotation bzj5 = Rotation.getRandom(random);
        list.add(new NetherFossilPiece(cst, Util.<ResourceLocation>getRandom(NetherFossilPieces.FOSSILS, random), fx, bzj5));
    }
    
    static {
        FOSSILS = new ResourceLocation[] { new ResourceLocation("nether_fossils/fossil_1"), new ResourceLocation("nether_fossils/fossil_2"), new ResourceLocation("nether_fossils/fossil_3"), new ResourceLocation("nether_fossils/fossil_4"), new ResourceLocation("nether_fossils/fossil_5"), new ResourceLocation("nether_fossils/fossil_6"), new ResourceLocation("nether_fossils/fossil_7"), new ResourceLocation("nether_fossils/fossil_8"), new ResourceLocation("nether_fossils/fossil_9"), new ResourceLocation("nether_fossils/fossil_10"), new ResourceLocation("nether_fossils/fossil_11"), new ResourceLocation("nether_fossils/fossil_12"), new ResourceLocation("nether_fossils/fossil_13"), new ResourceLocation("nether_fossils/fossil_14") };
    }
    
    public static class NetherFossilPiece extends TemplateStructurePiece {
        private final ResourceLocation templateLocation;
        private final Rotation rotation;
        
        public NetherFossilPiece(final StructureManager cst, final ResourceLocation vk, final BlockPos fx, final Rotation bzj) {
            super(StructurePieceType.NETHER_FOSSIL, 0);
            this.templateLocation = vk;
            this.templatePosition = fx;
            this.rotation = bzj;
            this.loadTemplate(cst);
        }
        
        public NetherFossilPiece(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.NETHER_FOSSIL, md);
            this.templateLocation = new ResourceLocation(md.getString("Template"));
            this.rotation = Rotation.valueOf(md.getString("Rot"));
            this.loadTemplate(cst);
        }
        
        private void loadTemplate(final StructureManager cst) {
            final StructureTemplate csy3 = cst.getOrCreate(this.templateLocation);
            final StructurePlaceSettings csu4 = new StructurePlaceSettings().setRotation(this.rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
            this.setup(csy3, this.templatePosition, csu4);
        }
        
        @Override
        protected void addAdditionalSaveData(final CompoundTag md) {
            super.addAdditionalSaveData(md);
            md.putString("Template", this.templateLocation.toString());
            md.putString("Rot", this.rotation.name());
        }
        
        @Override
        protected void handleDataMarker(final String string, final BlockPos fx, final ServerLevelAccessor bsh, final Random random, final BoundingBox cqx) {
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            cqx.expand(this.template.getBoundingBox(this.placeSettings, this.templatePosition));
            return super.postProcess(bso, bsk, cfv, random, cqx, bra, fx);
        }
    }
}

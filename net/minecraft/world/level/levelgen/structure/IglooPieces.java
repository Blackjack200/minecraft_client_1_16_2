package net.minecraft.world.level.levelgen.structure;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import com.google.common.collect.ImmutableMap;
import java.util.Random;
import java.util.List;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.core.BlockPos;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;

public class IglooPieces {
    private static final ResourceLocation STRUCTURE_LOCATION_IGLOO;
    private static final ResourceLocation STRUCTURE_LOCATION_LADDER;
    private static final ResourceLocation STRUCTURE_LOCATION_LABORATORY;
    private static final Map<ResourceLocation, BlockPos> PIVOTS;
    private static final Map<ResourceLocation, BlockPos> OFFSETS;
    
    public static void addPieces(final StructureManager cst, final BlockPos fx, final Rotation bzj, final List<StructurePiece> list, final Random random) {
        if (random.nextDouble() < 0.5) {
            final int integer6 = random.nextInt(8) + 4;
            list.add(new IglooPiece(cst, IglooPieces.STRUCTURE_LOCATION_LABORATORY, fx, bzj, integer6 * 3));
            for (int integer7 = 0; integer7 < integer6 - 1; ++integer7) {
                list.add(new IglooPiece(cst, IglooPieces.STRUCTURE_LOCATION_LADDER, fx, bzj, integer7 * 3));
            }
        }
        list.add(new IglooPiece(cst, IglooPieces.STRUCTURE_LOCATION_IGLOO, fx, bzj, 0));
    }
    
    static {
        STRUCTURE_LOCATION_IGLOO = new ResourceLocation("igloo/top");
        STRUCTURE_LOCATION_LADDER = new ResourceLocation("igloo/middle");
        STRUCTURE_LOCATION_LABORATORY = new ResourceLocation("igloo/bottom");
        PIVOTS = (Map)ImmutableMap.of(IglooPieces.STRUCTURE_LOCATION_IGLOO, new BlockPos(3, 5, 5), IglooPieces.STRUCTURE_LOCATION_LADDER, new BlockPos(1, 3, 1), IglooPieces.STRUCTURE_LOCATION_LABORATORY, new BlockPos(3, 6, 7));
        OFFSETS = (Map)ImmutableMap.of(IglooPieces.STRUCTURE_LOCATION_IGLOO, BlockPos.ZERO, IglooPieces.STRUCTURE_LOCATION_LADDER, new BlockPos(2, -3, 4), IglooPieces.STRUCTURE_LOCATION_LABORATORY, new BlockPos(0, -3, -2));
    }
    
    public static class IglooPiece extends TemplateStructurePiece {
        private final ResourceLocation templateLocation;
        private final Rotation rotation;
        
        public IglooPiece(final StructureManager cst, final ResourceLocation vk, final BlockPos fx, final Rotation bzj, final int integer) {
            super(StructurePieceType.IGLOO, 0);
            this.templateLocation = vk;
            final BlockPos fx2 = (BlockPos)IglooPieces.OFFSETS.get(vk);
            this.templatePosition = fx.offset(fx2.getX(), fx2.getY() - integer, fx2.getZ());
            this.rotation = bzj;
            this.loadTemplate(cst);
        }
        
        public IglooPiece(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.IGLOO, md);
            this.templateLocation = new ResourceLocation(md.getString("Template"));
            this.rotation = Rotation.valueOf(md.getString("Rot"));
            this.loadTemplate(cst);
        }
        
        private void loadTemplate(final StructureManager cst) {
            final StructureTemplate csy3 = cst.getOrCreate(this.templateLocation);
            final StructurePlaceSettings csu4 = new StructurePlaceSettings().setRotation(this.rotation).setMirror(Mirror.NONE).setRotationPivot((BlockPos)IglooPieces.PIVOTS.get(this.templateLocation)).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
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
            if (!"chest".equals(string)) {
                return;
            }
            bsh.setBlock(fx, Blocks.AIR.defaultBlockState(), 3);
            final BlockEntity ccg7 = bsh.getBlockEntity(fx.below());
            if (ccg7 instanceof ChestBlockEntity) {
                ((ChestBlockEntity)ccg7).setLootTable(BuiltInLootTables.IGLOO_CHEST, random.nextLong());
            }
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            final StructurePlaceSettings csu9 = new StructurePlaceSettings().setRotation(this.rotation).setMirror(Mirror.NONE).setRotationPivot((BlockPos)IglooPieces.PIVOTS.get(this.templateLocation)).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
            final BlockPos fx2 = (BlockPos)IglooPieces.OFFSETS.get(this.templateLocation);
            final BlockPos fx3 = this.templatePosition.offset(StructureTemplate.calculateRelativePosition(csu9, new BlockPos(3 - fx2.getX(), 0, 0 - fx2.getZ())));
            final int integer12 = bso.getHeight(Heightmap.Types.WORLD_SURFACE_WG, fx3.getX(), fx3.getZ());
            final BlockPos fx4 = this.templatePosition;
            this.templatePosition = this.templatePosition.offset(0, integer12 - 90 - 1, 0);
            final boolean boolean14 = super.postProcess(bso, bsk, cfv, random, cqx, bra, fx);
            if (this.templateLocation.equals(IglooPieces.STRUCTURE_LOCATION_IGLOO)) {
                final BlockPos fx5 = this.templatePosition.offset(StructureTemplate.calculateRelativePosition(csu9, new BlockPos(3, 0, 5)));
                final BlockState cee16 = bso.getBlockState(fx5.below());
                if (!cee16.isAir() && !cee16.is(Blocks.LADDER)) {
                    bso.setBlock(fx5, Blocks.SNOW_BLOCK.defaultBlockState(), 3);
                }
            }
            this.templatePosition = fx4;
            return boolean14;
        }
    }
}

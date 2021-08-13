package net.minecraft.world.level.levelgen.structure;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.ChunkPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.core.BlockPos;

public class BuriedTreasurePieces {
    public static class BuriedTreasurePiece extends StructurePiece {
        public BuriedTreasurePiece(final BlockPos fx) {
            super(StructurePieceType.BURIED_TREASURE_PIECE, 0);
            this.boundingBox = new BoundingBox(fx.getX(), fx.getY(), fx.getZ(), fx.getX(), fx.getY(), fx.getZ());
        }
        
        public BuriedTreasurePiece(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.BURIED_TREASURE_PIECE, md);
        }
        
        @Override
        protected void addAdditionalSaveData(final CompoundTag md) {
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            final int integer9 = bso.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, this.boundingBox.x0, this.boundingBox.z0);
            final BlockPos.MutableBlockPos a10 = new BlockPos.MutableBlockPos(this.boundingBox.x0, integer9, this.boundingBox.z0);
            while (a10.getY() > 0) {
                final BlockState cee11 = bso.getBlockState(a10);
                final BlockState cee12 = bso.getBlockState(a10.below());
                if (cee12 == Blocks.SANDSTONE.defaultBlockState() || cee12 == Blocks.STONE.defaultBlockState() || cee12 == Blocks.ANDESITE.defaultBlockState() || cee12 == Blocks.GRANITE.defaultBlockState() || cee12 == Blocks.DIORITE.defaultBlockState()) {
                    final BlockState cee13 = (cee11.isAir() || this.isLiquid(cee11)) ? Blocks.SAND.defaultBlockState() : cee11;
                    for (final Direction gc17 : Direction.values()) {
                        final BlockPos fx2 = a10.relative(gc17);
                        final BlockState cee14 = bso.getBlockState(fx2);
                        if (cee14.isAir() || this.isLiquid(cee14)) {
                            final BlockPos fx3 = fx2.below();
                            final BlockState cee15 = bso.getBlockState(fx3);
                            if ((cee15.isAir() || this.isLiquid(cee15)) && gc17 != Direction.UP) {
                                bso.setBlock(fx2, cee12, 3);
                            }
                            else {
                                bso.setBlock(fx2, cee13, 3);
                            }
                        }
                    }
                    this.boundingBox = new BoundingBox(a10.getX(), a10.getY(), a10.getZ(), a10.getX(), a10.getY(), a10.getZ());
                    return this.createChest(bso, cqx, random, a10, BuiltInLootTables.BURIED_TREASURE, null);
                }
                a10.move(0, -1, 0);
            }
            return false;
        }
        
        private boolean isLiquid(final BlockState cee) {
            return cee == Blocks.WATER.defaultBlockState() || cee == Blocks.LAVA.defaultBlockState();
        }
    }
}

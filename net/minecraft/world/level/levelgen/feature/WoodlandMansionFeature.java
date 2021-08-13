package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import java.util.List;
import java.util.Collection;
import net.minecraft.world.level.levelgen.structure.WoodlandMansionPieces;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.Heightmap;
import java.util.Random;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class WoodlandMansionFeature extends StructureFeature<NoneFeatureConfiguration> {
    public WoodlandMansionFeature(final Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    protected boolean linearSeparation() {
        return false;
    }
    
    @Override
    protected boolean isFeatureChunk(final ChunkGenerator cfv, final BiomeSource bsv, final long long3, final WorldgenRandom chu, final int integer5, final int integer6, final Biome bss, final ChunkPos bra, final NoneFeatureConfiguration cme) {
        final Set<Biome> set12 = bsv.getBiomesWithin(integer5 * 16 + 9, cfv.getSeaLevel(), integer6 * 16 + 9, 32);
        for (final Biome bss2 : set12) {
            if (!bss2.getGenerationSettings().isValidStart(this)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
        return WoodlandMansionStart::new;
    }
    
    public static class WoodlandMansionStart extends StructureStart<NoneFeatureConfiguration> {
        public WoodlandMansionStart(final StructureFeature<NoneFeatureConfiguration> ckx, final int integer2, final int integer3, final BoundingBox cqx, final int integer5, final long long6) {
            super(ckx, integer2, integer3, cqx, integer5, long6);
        }
        
        @Override
        public void generatePieces(final RegistryAccess gn, final ChunkGenerator cfv, final StructureManager cst, final int integer4, final int integer5, final Biome bss, final NoneFeatureConfiguration cme) {
            final Rotation bzj9 = Rotation.getRandom(this.random);
            int integer6 = 5;
            int integer7 = 5;
            if (bzj9 == Rotation.CLOCKWISE_90) {
                integer6 = -5;
            }
            else if (bzj9 == Rotation.CLOCKWISE_180) {
                integer6 = -5;
                integer7 = -5;
            }
            else if (bzj9 == Rotation.COUNTERCLOCKWISE_90) {
                integer7 = -5;
            }
            final int integer8 = (integer4 << 4) + 7;
            final int integer9 = (integer5 << 4) + 7;
            final int integer10 = cfv.getFirstOccupiedHeight(integer8, integer9, Heightmap.Types.WORLD_SURFACE_WG);
            final int integer11 = cfv.getFirstOccupiedHeight(integer8, integer9 + integer7, Heightmap.Types.WORLD_SURFACE_WG);
            final int integer12 = cfv.getFirstOccupiedHeight(integer8 + integer6, integer9, Heightmap.Types.WORLD_SURFACE_WG);
            final int integer13 = cfv.getFirstOccupiedHeight(integer8 + integer6, integer9 + integer7, Heightmap.Types.WORLD_SURFACE_WG);
            final int integer14 = Math.min(Math.min(integer10, integer11), Math.min(integer12, integer13));
            if (integer14 < 60) {
                return;
            }
            final BlockPos fx19 = new BlockPos(integer4 * 16 + 8, integer14 + 1, integer5 * 16 + 8);
            final List<WoodlandMansionPieces.WoodlandMansionPiece> list20 = (List<WoodlandMansionPieces.WoodlandMansionPiece>)Lists.newLinkedList();
            WoodlandMansionPieces.generateMansion(cst, fx19, bzj9, list20, this.random);
            this.pieces.addAll((Collection)list20);
            this.calculateBoundingBox();
        }
        
        @Override
        public void placeInChunk(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra) {
            super.placeInChunk(bso, bsk, cfv, random, cqx, bra);
            final int integer8 = this.boundingBox.y0;
            for (int integer9 = cqx.x0; integer9 <= cqx.x1; ++integer9) {
                for (int integer10 = cqx.z0; integer10 <= cqx.z1; ++integer10) {
                    final BlockPos fx11 = new BlockPos(integer9, integer8, integer10);
                    if (!bso.isEmptyBlock(fx11) && this.boundingBox.isInside(fx11)) {
                        boolean boolean12 = false;
                        for (final StructurePiece crr14 : this.pieces) {
                            if (crr14.getBoundingBox().isInside(fx11)) {
                                boolean12 = true;
                                break;
                            }
                        }
                        if (boolean12) {
                            for (int integer11 = integer8 - 1; integer11 > 1; --integer11) {
                                final BlockPos fx12 = new BlockPos(integer9, integer11, integer10);
                                if (!bso.isEmptyBlock(fx12) && !bso.getBlockState(fx12).getMaterial().isLiquid()) {
                                    break;
                                }
                                bso.setBlock(fx12, Blocks.COBBLESTONE.defaultBlockState(), 2);
                            }
                        }
                    }
                }
            }
        }
    }
}

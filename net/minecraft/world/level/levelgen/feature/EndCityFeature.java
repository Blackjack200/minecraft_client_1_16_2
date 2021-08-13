package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.structure.EndCityPieces;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.block.Rotation;
import java.util.Random;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class EndCityFeature extends StructureFeature<NoneFeatureConfiguration> {
    public EndCityFeature(final Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    protected boolean linearSeparation() {
        return false;
    }
    
    @Override
    protected boolean isFeatureChunk(final ChunkGenerator cfv, final BiomeSource bsv, final long long3, final WorldgenRandom chu, final int integer5, final int integer6, final Biome bss, final ChunkPos bra, final NoneFeatureConfiguration cme) {
        return getYPositionForFeature(integer5, integer6, cfv) >= 60;
    }
    
    @Override
    public StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
        return EndCityStart::new;
    }
    
    private static int getYPositionForFeature(final int integer1, final int integer2, final ChunkGenerator cfv) {
        final Random random4 = new Random((long)(integer1 + integer2 * 10387313));
        final Rotation bzj5 = Rotation.getRandom(random4);
        int integer3 = 5;
        int integer4 = 5;
        if (bzj5 == Rotation.CLOCKWISE_90) {
            integer3 = -5;
        }
        else if (bzj5 == Rotation.CLOCKWISE_180) {
            integer3 = -5;
            integer4 = -5;
        }
        else if (bzj5 == Rotation.COUNTERCLOCKWISE_90) {
            integer4 = -5;
        }
        final int integer5 = (integer1 << 4) + 7;
        final int integer6 = (integer2 << 4) + 7;
        final int integer7 = cfv.getFirstOccupiedHeight(integer5, integer6, Heightmap.Types.WORLD_SURFACE_WG);
        final int integer8 = cfv.getFirstOccupiedHeight(integer5, integer6 + integer4, Heightmap.Types.WORLD_SURFACE_WG);
        final int integer9 = cfv.getFirstOccupiedHeight(integer5 + integer3, integer6, Heightmap.Types.WORLD_SURFACE_WG);
        final int integer10 = cfv.getFirstOccupiedHeight(integer5 + integer3, integer6 + integer4, Heightmap.Types.WORLD_SURFACE_WG);
        return Math.min(Math.min(integer7, integer8), Math.min(integer9, integer10));
    }
    
    public static class EndCityStart extends StructureStart<NoneFeatureConfiguration> {
        public EndCityStart(final StructureFeature<NoneFeatureConfiguration> ckx, final int integer2, final int integer3, final BoundingBox cqx, final int integer5, final long long6) {
            super(ckx, integer2, integer3, cqx, integer5, long6);
        }
        
        @Override
        public void generatePieces(final RegistryAccess gn, final ChunkGenerator cfv, final StructureManager cst, final int integer4, final int integer5, final Biome bss, final NoneFeatureConfiguration cme) {
            final Rotation bzj9 = Rotation.getRandom(this.random);
            final int integer6 = getYPositionForFeature(integer4, integer5, cfv);
            if (integer6 < 60) {
                return;
            }
            final BlockPos fx11 = new BlockPos(integer4 * 16 + 8, integer6, integer5 * 16 + 8);
            EndCityPieces.startHouseTower(cst, fx11, bzj9, this.pieces, this.random);
            this.calculateBoundingBox();
        }
    }
}

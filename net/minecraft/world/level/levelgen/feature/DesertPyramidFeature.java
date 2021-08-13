package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import java.util.Random;
import net.minecraft.world.level.levelgen.structure.DesertPyramidPiece;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class DesertPyramidFeature extends StructureFeature<NoneFeatureConfiguration> {
    public DesertPyramidFeature(final Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
        return FeatureStart::new;
    }
    
    public static class FeatureStart extends StructureStart<NoneFeatureConfiguration> {
        public FeatureStart(final StructureFeature<NoneFeatureConfiguration> ckx, final int integer2, final int integer3, final BoundingBox cqx, final int integer5, final long long6) {
            super(ckx, integer2, integer3, cqx, integer5, long6);
        }
        
        @Override
        public void generatePieces(final RegistryAccess gn, final ChunkGenerator cfv, final StructureManager cst, final int integer4, final int integer5, final Biome bss, final NoneFeatureConfiguration cme) {
            final DesertPyramidPiece cqz9 = new DesertPyramidPiece(this.random, integer4 * 16, integer5 * 16);
            this.pieces.add(cqz9);
            this.calculateBoundingBox();
        }
    }
}

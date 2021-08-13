package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.IglooPieces;
import java.util.Random;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class IglooFeature extends StructureFeature<NoneFeatureConfiguration> {
    public IglooFeature(final Codec<NoneFeatureConfiguration> codec) {
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
            final int integer6 = integer4 * 16;
            final int integer7 = integer5 * 16;
            final BlockPos fx11 = new BlockPos(integer6, 90, integer7);
            final Rotation bzj12 = Rotation.getRandom(this.random);
            IglooPieces.addPieces(cst, fx11, bzj12, this.pieces, this.random);
            this.calculateBoundingBox();
        }
    }
}

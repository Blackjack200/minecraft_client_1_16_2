package net.minecraft.world.level.levelgen.feature;

import java.util.List;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import java.util.Random;
import net.minecraft.world.level.levelgen.structure.StrongholdPieces;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class StrongholdFeature extends StructureFeature<NoneFeatureConfiguration> {
    public StrongholdFeature(final Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
        return StrongholdStart::new;
    }
    
    @Override
    protected boolean isFeatureChunk(final ChunkGenerator cfv, final BiomeSource bsv, final long long3, final WorldgenRandom chu, final int integer5, final int integer6, final Biome bss, final ChunkPos bra, final NoneFeatureConfiguration cme) {
        return cfv.hasStronghold(new ChunkPos(integer5, integer6));
    }
    
    public static class StrongholdStart extends StructureStart<NoneFeatureConfiguration> {
        private final long seed;
        
        public StrongholdStart(final StructureFeature<NoneFeatureConfiguration> ckx, final int integer2, final int integer3, final BoundingBox cqx, final int integer5, final long long6) {
            super(ckx, integer2, integer3, cqx, integer5, long6);
            this.seed = long6;
        }
        
        @Override
        public void generatePieces(final RegistryAccess gn, final ChunkGenerator cfv, final StructureManager cst, final int integer4, final int integer5, final Biome bss, final NoneFeatureConfiguration cme) {
            int integer6 = 0;
            StrongholdPieces.StartPiece m10;
            do {
                this.pieces.clear();
                this.boundingBox = BoundingBox.getUnknownBox();
                this.random.setLargeFeatureSeed(this.seed + integer6++, integer4, integer5);
                StrongholdPieces.resetPieces();
                m10 = new StrongholdPieces.StartPiece(this.random, (integer4 << 4) + 2, (integer5 << 4) + 2);
                this.pieces.add(m10);
                m10.addChildren(m10, this.pieces, this.random);
                final List<StructurePiece> list11 = m10.pendingChildren;
                while (!list11.isEmpty()) {
                    final int integer7 = this.random.nextInt(list11.size());
                    final StructurePiece crr13 = (StructurePiece)list11.remove(integer7);
                    crr13.addChildren(m10, this.pieces, this.random);
                }
                this.calculateBoundingBox();
                this.moveBelowSeaLevel(cfv.getSeaLevel(), this.random, 10);
            } while (this.pieces.isEmpty() || m10.portalRoomPiece == null);
        }
    }
}

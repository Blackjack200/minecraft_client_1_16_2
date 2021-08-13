package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import java.util.Random;
import java.util.List;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.levelgen.structure.BeardedStructureStart;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;

public class JigsawFeature extends StructureFeature<JigsawConfiguration> {
    private final int startY;
    private final boolean doExpansionHack;
    private final boolean projectStartToHeightmap;
    
    public JigsawFeature(final Codec<JigsawConfiguration> codec, final int integer, final boolean boolean3, final boolean boolean4) {
        super(codec);
        this.startY = integer;
        this.doExpansionHack = boolean3;
        this.projectStartToHeightmap = boolean4;
    }
    
    @Override
    public StructureStartFactory<JigsawConfiguration> getStartFactory() {
        return (ckx, integer2, integer3, cqx, integer5, long6) -> new FeatureStart(this, integer2, integer3, cqx, integer5, long6);
    }
    
    public static class FeatureStart extends BeardedStructureStart<JigsawConfiguration> {
        private final JigsawFeature feature;
        
        public FeatureStart(final JigsawFeature cju, final int integer2, final int integer3, final BoundingBox cqx, final int integer5, final long long6) {
            super(cju, integer2, integer3, cqx, integer5, long6);
            this.feature = cju;
        }
        
        @Override
        public void generatePieces(final RegistryAccess gn, final ChunkGenerator cfv, final StructureManager cst, final int integer4, final int integer5, final Biome bss, final JigsawConfiguration clz) {
            final BlockPos fx9 = new BlockPos(integer4 * 16, this.feature.startY, integer5 * 16);
            Pools.bootstrap();
            JigsawPlacement.addPieces(gn, clz, PoolElementStructurePiece::new, cfv, cst, fx9, this.pieces, this.random, this.feature.doExpansionHack, this.feature.projectStartToHeightmap);
            this.calculateBoundingBox();
        }
    }
}

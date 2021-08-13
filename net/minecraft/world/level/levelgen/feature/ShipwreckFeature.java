package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.ShipwreckPieces;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.ShipwreckConfiguration;

public class ShipwreckFeature extends StructureFeature<ShipwreckConfiguration> {
    public ShipwreckFeature(final Codec<ShipwreckConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public StructureStartFactory<ShipwreckConfiguration> getStartFactory() {
        return FeatureStart::new;
    }
    
    public static class FeatureStart extends StructureStart<ShipwreckConfiguration> {
        public FeatureStart(final StructureFeature<ShipwreckConfiguration> ckx, final int integer2, final int integer3, final BoundingBox cqx, final int integer5, final long long6) {
            super(ckx, integer2, integer3, cqx, integer5, long6);
        }
        
        @Override
        public void generatePieces(final RegistryAccess gn, final ChunkGenerator cfv, final StructureManager cst, final int integer4, final int integer5, final Biome bss, final ShipwreckConfiguration cmp) {
            final Rotation bzj9 = Rotation.getRandom(this.random);
            final BlockPos fx10 = new BlockPos(integer4 * 16, 90, integer5 * 16);
            ShipwreckPieces.addPieces(cst, fx10, bzj9, this.pieces, this.random, cmp);
            this.calculateBoundingBox();
        }
    }
}

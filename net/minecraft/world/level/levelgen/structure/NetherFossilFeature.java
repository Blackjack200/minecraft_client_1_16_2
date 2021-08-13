package net.minecraft.world.level.levelgen.structure;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.BlockGetter;
import java.util.Random;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.core.RegistryAccess;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.StructureFeature;

public class NetherFossilFeature extends StructureFeature<NoneFeatureConfiguration> {
    public NetherFossilFeature(final Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
        return FeatureStart::new;
    }
    
    public static class FeatureStart extends BeardedStructureStart<NoneFeatureConfiguration> {
        public FeatureStart(final StructureFeature<NoneFeatureConfiguration> ckx, final int integer2, final int integer3, final BoundingBox cqx, final int integer5, final long long6) {
            super(ckx, integer2, integer3, cqx, integer5, long6);
        }
        
        @Override
        public void generatePieces(final RegistryAccess gn, final ChunkGenerator cfv, final StructureManager cst, final int integer4, final int integer5, final Biome bss, final NoneFeatureConfiguration cme) {
            final ChunkPos bra9 = new ChunkPos(integer4, integer5);
            final int integer6 = bra9.getMinBlockX() + this.random.nextInt(16);
            final int integer7 = bra9.getMinBlockZ() + this.random.nextInt(16);
            final int integer8 = cfv.getSeaLevel();
            int integer9 = integer8 + this.random.nextInt(cfv.getGenDepth() - 2 - integer8);
            final BlockGetter bqz14 = cfv.getBaseColumn(integer6, integer7);
            final BlockPos.MutableBlockPos a15 = new BlockPos.MutableBlockPos(integer6, integer9, integer7);
            while (integer9 > integer8) {
                final BlockState cee16 = bqz14.getBlockState(a15);
                a15.move(Direction.DOWN);
                final BlockState cee17 = bqz14.getBlockState(a15);
                if (cee16.isAir()) {
                    if (cee17.is(Blocks.SOUL_SAND)) {
                        break;
                    }
                    if (cee17.isFaceSturdy(bqz14, a15, Direction.UP)) {
                        break;
                    }
                }
                --integer9;
            }
            if (integer9 <= integer8) {
                return;
            }
            NetherFossilPieces.addPieces(cst, this.pieces, this.random, new BlockPos(integer6, integer9, integer7));
            this.calculateBoundingBox();
        }
    }
}

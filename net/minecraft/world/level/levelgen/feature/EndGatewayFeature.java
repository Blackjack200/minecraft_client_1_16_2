package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import java.util.Iterator;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.EndGatewayConfiguration;

public class EndGatewayFeature extends Feature<EndGatewayConfiguration> {
    public EndGatewayFeature(final Codec<EndGatewayConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final EndGatewayConfiguration clw) {
        for (final BlockPos fx2 : BlockPos.betweenClosed(fx.offset(-1, -2, -1), fx.offset(1, 2, 1))) {
            final boolean boolean9 = fx2.getX() == fx.getX();
            final boolean boolean10 = fx2.getY() == fx.getY();
            final boolean boolean11 = fx2.getZ() == fx.getZ();
            final boolean boolean12 = Math.abs(fx2.getY() - fx.getY()) == 2;
            if (boolean9 && boolean10 && boolean11) {
                final BlockPos fx3 = fx2.immutable();
                this.setBlock(bso, fx3, Blocks.END_GATEWAY.defaultBlockState());
                clw.getExit().ifPresent(fx4 -> {
                    final BlockEntity ccg5 = bso.getBlockEntity(fx3);
                    if (ccg5 instanceof TheEndGatewayBlockEntity) {
                        final TheEndGatewayBlockEntity cdh6 = (TheEndGatewayBlockEntity)ccg5;
                        cdh6.setExitPosition(fx4, clw.isExitExact());
                        ccg5.setChanged();
                    }
                });
            }
            else if (boolean10) {
                this.setBlock(bso, fx2, Blocks.AIR.defaultBlockState());
            }
            else if (boolean12 && boolean9 && boolean11) {
                this.setBlock(bso, fx2, Blocks.BEDROCK.defaultBlockState());
            }
            else if ((!boolean9 && !boolean11) || boolean12) {
                this.setBlock(bso, fx2, Blocks.AIR.defaultBlockState());
            }
            else {
                this.setBlock(bso, fx2, Blocks.BEDROCK.defaultBlockState());
            }
        }
        return true;
    }
}

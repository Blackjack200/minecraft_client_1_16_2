package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Iterator;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.levelgen.Heightmap;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.List;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class BonusChestFeature extends Feature<NoneFeatureConfiguration> {
    public BonusChestFeature(final Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final NoneFeatureConfiguration cme) {
        final ChunkPos bra7 = new ChunkPos(fx);
        final List<Integer> list8 = (List<Integer>)IntStream.rangeClosed(bra7.getMinBlockX(), bra7.getMaxBlockX()).boxed().collect(Collectors.toList());
        Collections.shuffle((List)list8, random);
        final List<Integer> list9 = (List<Integer>)IntStream.rangeClosed(bra7.getMinBlockZ(), bra7.getMaxBlockZ()).boxed().collect(Collectors.toList());
        Collections.shuffle((List)list9, random);
        final BlockPos.MutableBlockPos a10 = new BlockPos.MutableBlockPos();
        for (final Integer integer12 : list8) {
            for (final Integer integer13 : list9) {
                a10.set(integer12, 0, integer13);
                final BlockPos fx2 = bso.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, a10);
                if (bso.isEmptyBlock(fx2) || bso.getBlockState(fx2).getCollisionShape(bso, fx2).isEmpty()) {
                    bso.setBlock(fx2, Blocks.CHEST.defaultBlockState(), 2);
                    RandomizableContainerBlockEntity.setLootTable(bso, random, fx2, BuiltInLootTables.SPAWN_BONUS_CHEST);
                    final BlockState cee16 = Blocks.TORCH.defaultBlockState();
                    for (final Direction gc18 : Direction.Plane.HORIZONTAL) {
                        final BlockPos fx3 = fx2.relative(gc18);
                        if (cee16.canSurvive(bso, fx3)) {
                            bso.setBlock(fx3, cee16, 2);
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
}

package net.minecraft.world.level.levelgen.feature.treedecorators;

import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import java.util.Set;
import net.minecraft.core.BlockPos;
import java.util.List;
import java.util.Random;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import com.mojang.serialization.Codec;

public class AlterGroundDecorator extends TreeDecorator {
    public static final Codec<AlterGroundDecorator> CODEC;
    private final BlockStateProvider provider;
    
    public AlterGroundDecorator(final BlockStateProvider cnq) {
        this.provider = cnq;
    }
    
    @Override
    protected TreeDecoratorType<?> type() {
        return TreeDecoratorType.ALTER_GROUND;
    }
    
    @Override
    public void place(final WorldGenLevel bso, final Random random, final List<BlockPos> list3, final List<BlockPos> list4, final Set<BlockPos> set, final BoundingBox cqx) {
        final int integer8 = ((BlockPos)list3.get(0)).getY();
        list3.stream().filter(fx -> fx.getY() == integer8).forEach(fx -> {
            this.placeCircle(bso, random, fx.west().north());
            this.placeCircle(bso, random, fx.east(2).north());
            this.placeCircle(bso, random, fx.west().south(2));
            this.placeCircle(bso, random, fx.east(2).south(2));
            for (int integer5 = 0; integer5 < 5; ++integer5) {
                final int integer6 = random.nextInt(64);
                final int integer7 = integer6 % 8;
                final int integer8 = integer6 / 8;
                if (integer7 == 0 || integer7 == 7 || integer8 == 0 || integer8 == 7) {
                    this.placeCircle(bso, random, fx.offset(-3 + integer7, 0, -3 + integer8));
                }
            }
        });
    }
    
    private void placeCircle(final LevelSimulatedRW bry, final Random random, final BlockPos fx) {
        for (int integer5 = -2; integer5 <= 2; ++integer5) {
            for (int integer6 = -2; integer6 <= 2; ++integer6) {
                if (Math.abs(integer5) != 2 || Math.abs(integer6) != 2) {
                    this.placeBlockAt(bry, random, fx.offset(integer5, 0, integer6));
                }
            }
        }
    }
    
    private void placeBlockAt(final LevelSimulatedRW bry, final Random random, final BlockPos fx) {
        for (int integer5 = 2; integer5 >= -3; --integer5) {
            final BlockPos fx2 = fx.above(integer5);
            if (Feature.isGrassOrDirt(bry, fx2)) {
                bry.setBlock(fx2, this.provider.getState(random, fx), 19);
                break;
            }
            if (!Feature.isAir(bry, fx2) && integer5 < 0) {
                break;
            }
        }
    }
    
    static {
        CODEC = BlockStateProvider.CODEC.fieldOf("provider").xmap(AlterGroundDecorator::new, coj -> coj.provider).codec();
    }
}

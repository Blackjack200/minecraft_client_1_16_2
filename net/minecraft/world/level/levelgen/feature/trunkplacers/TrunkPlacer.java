package net.minecraft.world.level.levelgen.feature.trunkplacers;

import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.block.Blocks;
import java.util.function.Predicate;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import java.util.List;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import java.util.Set;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.LevelSimulatedRW;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.Products;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;

public abstract class TrunkPlacer {
    public static final Codec<TrunkPlacer> CODEC;
    protected final int baseHeight;
    protected final int heightRandA;
    protected final int heightRandB;
    
    protected static <P extends TrunkPlacer> Products.P3<RecordCodecBuilder.Mu<P>, Integer, Integer, Integer> trunkPlacerParts(final RecordCodecBuilder.Instance<P> instance) {
        return (Products.P3<RecordCodecBuilder.Mu<P>, Integer, Integer, Integer>)instance.group((App)Codec.intRange(0, 32).fieldOf("base_height").forGetter(coy -> coy.baseHeight), (App)Codec.intRange(0, 24).fieldOf("height_rand_a").forGetter(coy -> coy.heightRandA), (App)Codec.intRange(0, 24).fieldOf("height_rand_b").forGetter(coy -> coy.heightRandB));
    }
    
    public TrunkPlacer(final int integer1, final int integer2, final int integer3) {
        this.baseHeight = integer1;
        this.heightRandA = integer2;
        this.heightRandB = integer3;
    }
    
    protected abstract TrunkPlacerType<?> type();
    
    public abstract List<FoliagePlacer.FoliageAttachment> placeTrunk(final LevelSimulatedRW bry, final Random random, final int integer, final BlockPos fx, final Set<BlockPos> set, final BoundingBox cqx, final TreeConfiguration cmw);
    
    public int getTreeHeight(final Random random) {
        return this.baseHeight + random.nextInt(this.heightRandA + 1) + random.nextInt(this.heightRandB + 1);
    }
    
    protected static void setBlock(final LevelWriter bsb, final BlockPos fx, final BlockState cee, final BoundingBox cqx) {
        TreeFeature.setBlockKnownShape(bsb, fx, cee);
        cqx.expand(new BoundingBox(fx, fx));
    }
    
    private static boolean isDirt(final LevelSimulatedReader brz, final BlockPos fx) {
        return brz.isStateAtPosition(fx, (Predicate<BlockState>)(cee -> {
            final Block bul2 = cee.getBlock();
            return Feature.isDirt(bul2) && !cee.is(Blocks.GRASS_BLOCK) && !cee.is(Blocks.MYCELIUM);
        }));
    }
    
    protected static void setDirtAt(final LevelSimulatedRW bry, final BlockPos fx) {
        if (!isDirt(bry, fx)) {
            TreeFeature.setBlockKnownShape(bry, fx, Blocks.DIRT.defaultBlockState());
        }
    }
    
    protected static boolean placeLog(final LevelSimulatedRW bry, final Random random, final BlockPos fx, final Set<BlockPos> set, final BoundingBox cqx, final TreeConfiguration cmw) {
        if (TreeFeature.validTreePos(bry, fx)) {
            setBlock(bry, fx, cmw.trunkProvider.getState(random, fx), cqx);
            set.add(fx.immutable());
            return true;
        }
        return false;
    }
    
    protected static void placeLogIfFree(final LevelSimulatedRW bry, final Random random, final BlockPos.MutableBlockPos a, final Set<BlockPos> set, final BoundingBox cqx, final TreeConfiguration cmw) {
        if (TreeFeature.isFree(bry, a)) {
            placeLog(bry, random, a, set, cqx, cmw);
        }
    }
    
    static {
        CODEC = Registry.TRUNK_PLACER_TYPES.dispatch(TrunkPlacer::type, TrunkPlacerType::codec);
    }
}

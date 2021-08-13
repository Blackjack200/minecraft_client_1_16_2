package net.minecraft.world.level.levelgen.feature.foliageplacers;

import com.mojang.datafixers.kinds.Applicative;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.BlockPos;
import java.util.Set;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import java.util.Random;
import net.minecraft.world.level.LevelSimulatedRW;
import com.mojang.datafixers.kinds.App;
import net.minecraft.util.UniformInt;
import com.mojang.datafixers.Products;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;

public class BlobFoliagePlacer extends FoliagePlacer {
    public static final Codec<BlobFoliagePlacer> CODEC;
    protected final int height;
    
    protected static <P extends BlobFoliagePlacer> Products.P3<RecordCodecBuilder.Mu<P>, UniformInt, UniformInt, Integer> blobParts(final RecordCodecBuilder.Instance<P> instance) {
        return (Products.P3<RecordCodecBuilder.Mu<P>, UniformInt, UniformInt, Integer>)FoliagePlacer.<P>foliagePlacerParts(instance).and((App)Codec.intRange(0, 16).fieldOf("height").forGetter(cne -> cne.height));
    }
    
    public BlobFoliagePlacer(final UniformInt aft1, final UniformInt aft2, final int integer) {
        super(aft1, aft2);
        this.height = integer;
    }
    
    @Override
    protected FoliagePlacerType<?> type() {
        return FoliagePlacerType.BLOB_FOLIAGE_PLACER;
    }
    
    @Override
    protected void createFoliage(final LevelSimulatedRW bry, final Random random, final TreeConfiguration cmw, final int integer4, final FoliageAttachment b, final int integer6, final int integer7, final Set<BlockPos> set, final int integer9, final BoundingBox cqx) {
        for (int integer10 = integer9; integer10 >= integer9 - integer6; --integer10) {
            final int integer11 = Math.max(integer7 + b.radiusOffset() - 1 - integer10 / 2, 0);
            this.placeLeavesRow(bry, random, cmw, b.foliagePos(), integer11, set, integer10, b.doubleTrunk(), cqx);
        }
    }
    
    @Override
    public int foliageHeight(final Random random, final int integer, final TreeConfiguration cmw) {
        return this.height;
    }
    
    @Override
    protected boolean shouldSkipLocation(final Random random, final int integer2, final int integer3, final int integer4, final int integer5, final boolean boolean6) {
        return integer2 == integer5 && integer4 == integer5 && (random.nextInt(2) == 0 || integer3 == 0);
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> BlobFoliagePlacer.<BlobFoliagePlacer>blobParts((RecordCodecBuilder.Instance<BlobFoliagePlacer>)instance).apply((Applicative)instance, BlobFoliagePlacer::new));
    }
}

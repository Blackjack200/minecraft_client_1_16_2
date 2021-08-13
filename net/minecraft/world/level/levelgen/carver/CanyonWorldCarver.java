package net.minecraft.world.level.levelgen.carver;

import net.minecraft.util.Mth;
import java.util.BitSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.core.BlockPos;
import java.util.function.Function;
import net.minecraft.world.level.chunk.ChunkAccess;
import java.util.Random;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;

public class CanyonWorldCarver extends WorldCarver<ProbabilityFeatureConfiguration> {
    private final float[] rs;
    
    public CanyonWorldCarver(final Codec<ProbabilityFeatureConfiguration> codec) {
        super(codec, 256);
        this.rs = new float[1024];
    }
    
    @Override
    public boolean isStartChunk(final Random random, final int integer2, final int integer3, final ProbabilityFeatureConfiguration cmh) {
        return random.nextFloat() <= cmh.probability;
    }
    
    @Override
    public boolean carve(final ChunkAccess cft, final Function<BlockPos, Biome> function, final Random random, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final BitSet bitSet, final ProbabilityFeatureConfiguration cmh) {
        final int integer9 = (this.getRange() * 2 - 1) * 16;
        final double double13 = integer5 * 16 + random.nextInt(16);
        final double double14 = random.nextInt(random.nextInt(40) + 8) + 20;
        final double double15 = integer6 * 16 + random.nextInt(16);
        final float float19 = random.nextFloat() * 6.2831855f;
        final float float20 = (random.nextFloat() - 0.5f) * 2.0f / 8.0f;
        final double double16 = 3.0;
        final float float21 = (random.nextFloat() * 2.0f + random.nextFloat()) * 2.0f;
        final int integer10 = integer9 - random.nextInt(integer9 / 4);
        final int integer11 = 0;
        this.genCanyon(cft, function, random.nextLong(), integer4, integer7, integer8, double13, double14, double15, float21, float19, float20, 0, integer10, 3.0, bitSet);
        return true;
    }
    
    private void genCanyon(final ChunkAccess cft, final Function<BlockPos, Biome> function, final long long3, final int integer4, final int integer5, final int integer6, double double7, double double8, double double9, final float float10, float float11, float float12, final int integer13, final int integer14, final double double15, final BitSet bitSet) {
        final Random random23 = new Random(long3);
        float float13 = 1.0f;
        for (int integer15 = 0; integer15 < 256; ++integer15) {
            if (integer15 == 0 || random23.nextInt(3) == 0) {
                float13 = 1.0f + random23.nextFloat() * random23.nextFloat();
            }
            this.rs[integer15] = float13 * float13;
        }
        float float14 = 0.0f;
        float float15 = 0.0f;
        for (int integer16 = integer13; integer16 < integer14; ++integer16) {
            double double16 = 1.5 + Mth.sin(integer16 * 3.1415927f / integer14) * float10;
            double double17 = double16 * double15;
            double16 *= random23.nextFloat() * 0.25 + 0.75;
            double17 *= random23.nextFloat() * 0.25 + 0.75;
            final float float16 = Mth.cos(float12);
            final float float17 = Mth.sin(float12);
            double7 += Mth.cos(float11) * float16;
            double8 += float17;
            double9 += Mth.sin(float11) * float16;
            float12 *= 0.7f;
            float12 += float15 * 0.05f;
            float11 += float14 * 0.05f;
            float15 *= 0.8f;
            float14 *= 0.5f;
            float15 += (random23.nextFloat() - random23.nextFloat()) * random23.nextFloat() * 2.0f;
            float14 += (random23.nextFloat() - random23.nextFloat()) * random23.nextFloat() * 4.0f;
            if (random23.nextInt(4) != 0) {
                if (!this.canReach(integer5, integer6, double7, double9, integer16, integer14, float10)) {
                    return;
                }
                this.carveSphere(cft, function, long3, integer4, integer5, integer6, double7, double8, double9, double16, double17, bitSet);
            }
        }
    }
    
    @Override
    protected boolean skip(final double double1, final double double2, final double double3, final int integer) {
        return (double1 * double1 + double3 * double3) * this.rs[integer - 1] + double2 * double2 / 6.0 >= 1.0;
    }
}

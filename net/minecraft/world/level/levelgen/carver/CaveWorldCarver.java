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

public class CaveWorldCarver extends WorldCarver<ProbabilityFeatureConfiguration> {
    public CaveWorldCarver(final Codec<ProbabilityFeatureConfiguration> codec, final int integer) {
        super(codec, integer);
    }
    
    @Override
    public boolean isStartChunk(final Random random, final int integer2, final int integer3, final ProbabilityFeatureConfiguration cmh) {
        return random.nextFloat() <= cmh.probability;
    }
    
    @Override
    public boolean carve(final ChunkAccess cft, final Function<BlockPos, Biome> function, final Random random, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final BitSet bitSet, final ProbabilityFeatureConfiguration cmh) {
        final int integer9 = (this.getRange() * 2 - 1) * 16;
        for (int integer10 = random.nextInt(random.nextInt(random.nextInt(this.getCaveBound()) + 1) + 1), integer11 = 0; integer11 < integer10; ++integer11) {
            final double double15 = integer5 * 16 + random.nextInt(16);
            final double double16 = this.getCaveY(random);
            final double double17 = integer6 * 16 + random.nextInt(16);
            int integer12 = 1;
            if (random.nextInt(4) == 0) {
                final double double18 = 0.5;
                final float float24 = 1.0f + random.nextFloat() * 6.0f;
                this.genRoom(cft, function, random.nextLong(), integer4, integer7, integer8, double15, double16, double17, float24, 0.5, bitSet);
                integer12 += random.nextInt(4);
            }
            for (int integer13 = 0; integer13 < integer12; ++integer13) {
                final float float25 = random.nextFloat() * 6.2831855f;
                final float float24 = (random.nextFloat() - 0.5f) / 4.0f;
                final float float26 = this.getThickness(random);
                final int integer14 = integer9 - random.nextInt(integer9 / 4);
                final int integer15 = 0;
                this.genTunnel(cft, function, random.nextLong(), integer4, integer7, integer8, double15, double16, double17, float26, float25, float24, 0, integer14, this.getYScale(), bitSet);
            }
        }
        return true;
    }
    
    protected int getCaveBound() {
        return 15;
    }
    
    protected float getThickness(final Random random) {
        float float3 = random.nextFloat() * 2.0f + random.nextFloat();
        if (random.nextInt(10) == 0) {
            float3 *= random.nextFloat() * random.nextFloat() * 3.0f + 1.0f;
        }
        return float3;
    }
    
    protected double getYScale() {
        return 1.0;
    }
    
    protected int getCaveY(final Random random) {
        return random.nextInt(random.nextInt(120) + 8);
    }
    
    protected void genRoom(final ChunkAccess cft, final Function<BlockPos, Biome> function, final long long3, final int integer4, final int integer5, final int integer6, final double double7, final double double8, final double double9, final float float10, final double double11, final BitSet bitSet) {
        final double double12 = 1.5 + Mth.sin(1.5707964f) * float10;
        final double double13 = double12 * double11;
        this.carveSphere(cft, function, long3, integer4, integer5, integer6, double7 + 1.0, double8, double9, double12, double13, bitSet);
    }
    
    protected void genTunnel(final ChunkAccess cft, final Function<BlockPos, Biome> function, final long long3, final int integer4, final int integer5, final int integer6, double double7, double double8, double double9, final float float10, float float11, float float12, final int integer13, final int integer14, final double double15, final BitSet bitSet) {
        final Random random23 = new Random(long3);
        final int integer15 = random23.nextInt(integer14 / 2) + integer14 / 4;
        final boolean boolean25 = random23.nextInt(6) == 0;
        float float13 = 0.0f;
        float float14 = 0.0f;
        for (int integer16 = integer13; integer16 < integer14; ++integer16) {
            final double double16 = 1.5 + Mth.sin(3.1415927f * integer16 / integer14) * float10;
            final double double17 = double16 * double15;
            final float float15 = Mth.cos(float12);
            double7 += Mth.cos(float11) * float15;
            double8 += Mth.sin(float12);
            double9 += Mth.sin(float11) * float15;
            float12 *= (boolean25 ? 0.92f : 0.7f);
            float12 += float14 * 0.1f;
            float11 += float13 * 0.1f;
            float14 *= 0.9f;
            float13 *= 0.75f;
            float14 += (random23.nextFloat() - random23.nextFloat()) * random23.nextFloat() * 2.0f;
            float13 += (random23.nextFloat() - random23.nextFloat()) * random23.nextFloat() * 4.0f;
            if (integer16 == integer15 && float10 > 1.0f) {
                this.genTunnel(cft, function, random23.nextLong(), integer4, integer5, integer6, double7, double8, double9, random23.nextFloat() * 0.5f + 0.5f, float11 - 1.5707964f, float12 / 3.0f, integer16, integer14, 1.0, bitSet);
                this.genTunnel(cft, function, random23.nextLong(), integer4, integer5, integer6, double7, double8, double9, random23.nextFloat() * 0.5f + 0.5f, float11 + 1.5707964f, float12 / 3.0f, integer16, integer14, 1.0, bitSet);
                return;
            }
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
        return double2 <= -0.7 || double1 * double1 + double2 * double2 + double3 * double3 >= 1.0;
    }
}

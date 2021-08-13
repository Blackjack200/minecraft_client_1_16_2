package net.minecraft.world.level.levelgen.structure.templatesystem;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.core.Vec3i;
import java.util.Random;
import net.minecraft.core.BlockPos;
import com.mojang.serialization.Codec;

public class LinearPosTest extends PosRuleTest {
    public static final Codec<LinearPosTest> CODEC;
    private final float minChance;
    private final float maxChance;
    private final int minDist;
    private final int maxDist;
    
    public LinearPosTest(final float float1, final float float2, final int integer3, final int integer4) {
        if (integer3 >= integer4) {
            throw new IllegalArgumentException(new StringBuilder().append("Invalid range: [").append(integer3).append(",").append(integer4).append("]").toString());
        }
        this.minChance = float1;
        this.maxChance = float2;
        this.minDist = integer3;
        this.maxDist = integer4;
    }
    
    @Override
    public boolean test(final BlockPos fx1, final BlockPos fx2, final BlockPos fx3, final Random random) {
        final int integer6 = fx2.distManhattan(fx3);
        final float float7 = random.nextFloat();
        return float7 <= Mth.clampedLerp(this.minChance, this.maxChance, Mth.inverseLerp(integer6, this.minDist, this.maxDist));
    }
    
    @Override
    protected PosRuleTestType<?> getType() {
        return PosRuleTestType.LINEAR_POS_TEST;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.FLOAT.fieldOf("min_chance").orElse(0.0f).forGetter(csi -> csi.minChance), (App)Codec.FLOAT.fieldOf("max_chance").orElse(0.0f).forGetter(csi -> csi.maxChance), (App)Codec.INT.fieldOf("min_dist").orElse(0).forGetter(csi -> csi.minDist), (App)Codec.INT.fieldOf("max_dist").orElse(0).forGetter(csi -> csi.maxDist)).apply((Applicative)instance, LinearPosTest::new));
    }
}

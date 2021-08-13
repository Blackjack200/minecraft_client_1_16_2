package net.minecraft.world.level.levelgen.structure.templatesystem;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import com.mojang.serialization.Codec;

public class AxisAlignedLinearPosTest extends PosRuleTest {
    public static final Codec<AxisAlignedLinearPosTest> CODEC;
    private final float minChance;
    private final float maxChance;
    private final int minDist;
    private final int maxDist;
    private final Direction.Axis axis;
    
    public AxisAlignedLinearPosTest(final float float1, final float float2, final int integer3, final int integer4, final Direction.Axis a) {
        if (integer3 >= integer4) {
            throw new IllegalArgumentException(new StringBuilder().append("Invalid range: [").append(integer3).append(",").append(integer4).append("]").toString());
        }
        this.minChance = float1;
        this.maxChance = float2;
        this.minDist = integer3;
        this.maxDist = integer4;
        this.axis = a;
    }
    
    @Override
    public boolean test(final BlockPos fx1, final BlockPos fx2, final BlockPos fx3, final Random random) {
        final Direction gc6 = Direction.get(Direction.AxisDirection.POSITIVE, this.axis);
        final float float7 = (float)Math.abs((fx2.getX() - fx3.getX()) * gc6.getStepX());
        final float float8 = (float)Math.abs((fx2.getY() - fx3.getY()) * gc6.getStepY());
        final float float9 = (float)Math.abs((fx2.getZ() - fx3.getZ()) * gc6.getStepZ());
        final int integer10 = (int)(float7 + float8 + float9);
        final float float10 = random.nextFloat();
        return float10 <= Mth.clampedLerp(this.minChance, this.maxChance, Mth.inverseLerp(integer10, this.minDist, this.maxDist));
    }
    
    @Override
    protected PosRuleTestType<?> getType() {
        return PosRuleTestType.AXIS_ALIGNED_LINEAR_POS_TEST;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.FLOAT.fieldOf("min_chance").orElse(0.0f).forGetter(cry -> cry.minChance), (App)Codec.FLOAT.fieldOf("max_chance").orElse(0.0f).forGetter(cry -> cry.maxChance), (App)Codec.INT.fieldOf("min_dist").orElse(0).forGetter(cry -> cry.minDist), (App)Codec.INT.fieldOf("max_dist").orElse(0).forGetter(cry -> cry.maxDist), (App)Direction.Axis.CODEC.fieldOf("axis").orElse(Direction.Axis.Y).forGetter(cry -> cry.axis)).apply((Applicative)instance, AxisAlignedLinearPosTest::new));
    }
}

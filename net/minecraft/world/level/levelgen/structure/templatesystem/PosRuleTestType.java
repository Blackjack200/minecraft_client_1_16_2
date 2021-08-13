package net.minecraft.world.level.levelgen.structure.templatesystem;

import net.minecraft.core.Registry;
import com.mojang.serialization.Codec;

public interface PosRuleTestType<P extends PosRuleTest> {
    public static final PosRuleTestType<PosAlwaysTrueTest> ALWAYS_TRUE_TEST = PosRuleTestType.<PosAlwaysTrueTest>register("always_true", PosAlwaysTrueTest.CODEC);
    public static final PosRuleTestType<LinearPosTest> LINEAR_POS_TEST = PosRuleTestType.<LinearPosTest>register("linear_pos", LinearPosTest.CODEC);
    public static final PosRuleTestType<AxisAlignedLinearPosTest> AXIS_ALIGNED_LINEAR_POS_TEST = PosRuleTestType.<AxisAlignedLinearPosTest>register("axis_aligned_linear_pos", AxisAlignedLinearPosTest.CODEC);
    
    Codec<P> codec();
    
    default <P extends PosRuleTest> PosRuleTestType<P> register(final String string, final Codec<P> codec) {
        return Registry.<PosRuleTestType<P>>register(Registry.POS_RULE_TEST, string, () -> codec);
    }
}

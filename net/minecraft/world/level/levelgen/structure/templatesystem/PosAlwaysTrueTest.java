package net.minecraft.world.level.levelgen.structure.templatesystem;

import java.util.Random;
import net.minecraft.core.BlockPos;
import com.mojang.serialization.Codec;

public class PosAlwaysTrueTest extends PosRuleTest {
    public static final Codec<PosAlwaysTrueTest> CODEC;
    public static final PosAlwaysTrueTest INSTANCE;
    
    private PosAlwaysTrueTest() {
    }
    
    @Override
    public boolean test(final BlockPos fx1, final BlockPos fx2, final BlockPos fx3, final Random random) {
        return true;
    }
    
    @Override
    protected PosRuleTestType<?> getType() {
        return PosRuleTestType.ALWAYS_TRUE_TEST;
    }
    
    static {
        CODEC = Codec.unit(() -> PosAlwaysTrueTest.INSTANCE);
        INSTANCE = new PosAlwaysTrueTest();
    }
}

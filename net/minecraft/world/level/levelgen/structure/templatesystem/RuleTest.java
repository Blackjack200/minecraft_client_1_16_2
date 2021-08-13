package net.minecraft.world.level.levelgen.structure.templatesystem;

import net.minecraft.core.Registry;
import java.util.Random;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;

public abstract class RuleTest {
    public static final Codec<RuleTest> CODEC;
    
    public abstract boolean test(final BlockState cee, final Random random);
    
    protected abstract RuleTestType<?> getType();
    
    static {
        CODEC = Registry.RULE_TEST.dispatch("predicate_type", RuleTest::getType, RuleTestType::codec);
    }
}

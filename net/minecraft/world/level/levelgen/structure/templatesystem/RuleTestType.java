package net.minecraft.world.level.levelgen.structure.templatesystem;

import net.minecraft.core.Registry;
import com.mojang.serialization.Codec;

public interface RuleTestType<P extends RuleTest> {
    public static final RuleTestType<AlwaysTrueTest> ALWAYS_TRUE_TEST = RuleTestType.<AlwaysTrueTest>register("always_true", AlwaysTrueTest.CODEC);
    public static final RuleTestType<BlockMatchTest> BLOCK_TEST = RuleTestType.<BlockMatchTest>register("block_match", BlockMatchTest.CODEC);
    public static final RuleTestType<BlockStateMatchTest> BLOCKSTATE_TEST = RuleTestType.<BlockStateMatchTest>register("blockstate_match", BlockStateMatchTest.CODEC);
    public static final RuleTestType<TagMatchTest> TAG_TEST = RuleTestType.<TagMatchTest>register("tag_match", TagMatchTest.CODEC);
    public static final RuleTestType<RandomBlockMatchTest> RANDOM_BLOCK_TEST = RuleTestType.<RandomBlockMatchTest>register("random_block_match", RandomBlockMatchTest.CODEC);
    public static final RuleTestType<RandomBlockStateMatchTest> RANDOM_BLOCKSTATE_TEST = RuleTestType.<RandomBlockStateMatchTest>register("random_blockstate_match", RandomBlockStateMatchTest.CODEC);
    
    Codec<P> codec();
    
    default <P extends RuleTest> RuleTestType<P> register(final String string, final Codec<P> codec) {
        return Registry.<RuleTestType<P>>register(Registry.RULE_TEST, string, () -> codec);
    }
}

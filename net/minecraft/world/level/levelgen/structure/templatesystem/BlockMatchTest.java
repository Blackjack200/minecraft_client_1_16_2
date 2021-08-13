package net.minecraft.world.level.levelgen.structure.templatesystem;

import net.minecraft.core.Registry;
import java.util.Random;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import com.mojang.serialization.Codec;

public class BlockMatchTest extends RuleTest {
    public static final Codec<BlockMatchTest> CODEC;
    private final Block block;
    
    public BlockMatchTest(final Block bul) {
        this.block = bul;
    }
    
    @Override
    public boolean test(final BlockState cee, final Random random) {
        return cee.is(this.block);
    }
    
    @Override
    protected RuleTestType<?> getType() {
        return RuleTestType.BLOCK_TEST;
    }
    
    static {
        CODEC = Registry.BLOCK.fieldOf("block").xmap(BlockMatchTest::new, csc -> csc.block).codec();
    }
}

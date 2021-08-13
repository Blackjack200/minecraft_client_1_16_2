package net.minecraft.world.level.levelgen.structure.templatesystem;

import com.mojang.datafixers.kinds.Applicative;
import net.minecraft.core.Registry;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Random;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import com.mojang.serialization.Codec;

public class RandomBlockMatchTest extends RuleTest {
    public static final Codec<RandomBlockMatchTest> CODEC;
    private final Block block;
    private final float probability;
    
    public RandomBlockMatchTest(final Block bul, final float float2) {
        this.block = bul;
        this.probability = float2;
    }
    
    @Override
    public boolean test(final BlockState cee, final Random random) {
        return cee.is(this.block) && random.nextFloat() < this.probability;
    }
    
    @Override
    protected RuleTestType<?> getType() {
        return RuleTestType.RANDOM_BLOCK_TEST;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Registry.BLOCK.fieldOf("block").forGetter(cso -> cso.block), (App)Codec.FLOAT.fieldOf("probability").forGetter(cso -> cso.probability)).apply((Applicative)instance, RandomBlockMatchTest::new));
    }
}

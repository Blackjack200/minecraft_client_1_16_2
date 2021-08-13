package net.minecraft.world.level.levelgen.structure.templatesystem;

import javax.annotation.Nullable;
import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Random;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import java.util.Collection;
import java.util.List;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;

public class RuleProcessor extends StructureProcessor {
    public static final Codec<RuleProcessor> CODEC;
    private final ImmutableList<ProcessorRule> rules;
    
    public RuleProcessor(final List<? extends ProcessorRule> list) {
        this.rules = (ImmutableList<ProcessorRule>)ImmutableList.copyOf((Collection)list);
    }
    
    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(final LevelReader brw, final BlockPos fx2, final BlockPos fx3, final StructureTemplate.StructureBlockInfo c4, final StructureTemplate.StructureBlockInfo c5, final StructurePlaceSettings csu) {
        final Random random8 = new Random(Mth.getSeed(c5.pos));
        final BlockState cee9 = brw.getBlockState(c5.pos);
        for (final ProcessorRule csn11 : this.rules) {
            if (csn11.test(c5.state, cee9, c4.pos, c5.pos, fx3, random8)) {
                return new StructureTemplate.StructureBlockInfo(c5.pos, csn11.getOutputState(), csn11.getOutputTag());
            }
        }
        return c5;
    }
    
    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
    
    static {
        CODEC = ProcessorRule.CODEC.listOf().fieldOf("rules").xmap(RuleProcessor::new, csq -> csq.rules).codec();
    }
}

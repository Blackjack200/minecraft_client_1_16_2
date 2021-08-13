package net.minecraft.world.level.levelgen.structure.templatesystem;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import com.mojang.serialization.Codec;

public class NopProcessor extends StructureProcessor {
    public static final Codec<NopProcessor> CODEC;
    public static final NopProcessor INSTANCE;
    
    private NopProcessor() {
    }
    
    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(final LevelReader brw, final BlockPos fx2, final BlockPos fx3, final StructureTemplate.StructureBlockInfo c4, final StructureTemplate.StructureBlockInfo c5, final StructurePlaceSettings csu) {
        return c5;
    }
    
    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.NOP;
    }
    
    static {
        CODEC = Codec.unit(() -> NopProcessor.INSTANCE);
        INSTANCE = new NopProcessor();
    }
}

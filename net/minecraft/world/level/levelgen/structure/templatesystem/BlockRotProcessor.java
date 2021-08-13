package net.minecraft.world.level.levelgen.structure.templatesystem;

import javax.annotation.Nullable;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import com.mojang.serialization.Codec;

public class BlockRotProcessor extends StructureProcessor {
    public static final Codec<BlockRotProcessor> CODEC;
    private final float integrity;
    
    public BlockRotProcessor(final float float1) {
        this.integrity = float1;
    }
    
    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(final LevelReader brw, final BlockPos fx2, final BlockPos fx3, final StructureTemplate.StructureBlockInfo c4, final StructureTemplate.StructureBlockInfo c5, final StructurePlaceSettings csu) {
        final Random random8 = csu.getRandom(c5.pos);
        if (this.integrity >= 1.0f || random8.nextFloat() <= this.integrity) {
            return c5;
        }
        return null;
    }
    
    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.BLOCK_ROT;
    }
    
    static {
        CODEC = Codec.FLOAT.fieldOf("integrity").orElse(1.0f).xmap(BlockRotProcessor::new, csd -> csd.integrity).codec();
    }
}

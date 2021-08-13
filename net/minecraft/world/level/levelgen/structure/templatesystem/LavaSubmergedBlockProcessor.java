package net.minecraft.world.level.levelgen.structure.templatesystem;

import javax.annotation.Nullable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import com.mojang.serialization.Codec;

public class LavaSubmergedBlockProcessor extends StructureProcessor {
    public static final Codec<LavaSubmergedBlockProcessor> CODEC;
    public static final LavaSubmergedBlockProcessor INSTANCE;
    
    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(final LevelReader brw, final BlockPos fx2, final BlockPos fx3, final StructureTemplate.StructureBlockInfo c4, final StructureTemplate.StructureBlockInfo c5, final StructurePlaceSettings csu) {
        final BlockPos fx4 = c5.pos;
        final boolean boolean9 = brw.getBlockState(fx4).is(Blocks.LAVA);
        if (boolean9 && !Block.isShapeFullBlock(c5.state.getShape(brw, fx4))) {
            return new StructureTemplate.StructureBlockInfo(fx4, Blocks.LAVA.defaultBlockState(), c5.nbt);
        }
        return c5;
    }
    
    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.LAVA_SUBMERGED_BLOCK;
    }
    
    static {
        CODEC = Codec.unit(() -> LavaSubmergedBlockProcessor.INSTANCE);
        INSTANCE = new LavaSubmergedBlockProcessor();
    }
}

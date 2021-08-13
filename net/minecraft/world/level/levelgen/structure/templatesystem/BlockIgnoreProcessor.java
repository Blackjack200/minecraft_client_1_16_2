package net.minecraft.world.level.levelgen.structure.templatesystem;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import java.util.Collection;
import java.util.List;
import net.minecraft.world.level.block.Block;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;

public class BlockIgnoreProcessor extends StructureProcessor {
    public static final Codec<BlockIgnoreProcessor> CODEC;
    public static final BlockIgnoreProcessor STRUCTURE_BLOCK;
    public static final BlockIgnoreProcessor AIR;
    public static final BlockIgnoreProcessor STRUCTURE_AND_AIR;
    private final ImmutableList<Block> toIgnore;
    
    public BlockIgnoreProcessor(final List<Block> list) {
        this.toIgnore = (ImmutableList<Block>)ImmutableList.copyOf((Collection)list);
    }
    
    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(final LevelReader brw, final BlockPos fx2, final BlockPos fx3, final StructureTemplate.StructureBlockInfo c4, final StructureTemplate.StructureBlockInfo c5, final StructurePlaceSettings csu) {
        if (this.toIgnore.contains(c5.state.getBlock())) {
            return null;
        }
        return c5;
    }
    
    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.BLOCK_IGNORE;
    }
    
    static {
        CODEC = BlockState.CODEC.xmap(BlockBehaviour.BlockStateBase::getBlock, Block::defaultBlockState).listOf().fieldOf("blocks").xmap(BlockIgnoreProcessor::new, csb -> csb.toIgnore).codec();
        STRUCTURE_BLOCK = new BlockIgnoreProcessor((List<Block>)ImmutableList.of(Blocks.STRUCTURE_BLOCK));
        AIR = new BlockIgnoreProcessor((List<Block>)ImmutableList.of(Blocks.AIR));
        STRUCTURE_AND_AIR = new BlockIgnoreProcessor((List<Block>)ImmutableList.of(Blocks.AIR, Blocks.STRUCTURE_BLOCK));
    }
}

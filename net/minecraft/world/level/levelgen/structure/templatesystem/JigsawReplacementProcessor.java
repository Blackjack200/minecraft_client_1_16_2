package net.minecraft.world.level.levelgen.structure.templatesystem;

import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import com.mojang.brigadier.StringReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import com.mojang.serialization.Codec;

public class JigsawReplacementProcessor extends StructureProcessor {
    public static final Codec<JigsawReplacementProcessor> CODEC;
    public static final JigsawReplacementProcessor INSTANCE;
    
    private JigsawReplacementProcessor() {
    }
    
    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(final LevelReader brw, final BlockPos fx2, final BlockPos fx3, final StructureTemplate.StructureBlockInfo c4, final StructureTemplate.StructureBlockInfo c5, final StructurePlaceSettings csu) {
        final BlockState cee8 = c5.state;
        if (!cee8.is(Blocks.JIGSAW)) {
            return c5;
        }
        final String string9 = c5.nbt.getString("final_state");
        final BlockStateParser ei10 = new BlockStateParser(new StringReader(string9), false);
        try {
            ei10.parse(true);
        }
        catch (CommandSyntaxException commandSyntaxException11) {
            throw new RuntimeException((Throwable)commandSyntaxException11);
        }
        if (ei10.getState().is(Blocks.STRUCTURE_VOID)) {
            return null;
        }
        return new StructureTemplate.StructureBlockInfo(c5.pos, ei10.getState(), null);
    }
    
    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.JIGSAW_REPLACEMENT;
    }
    
    static {
        CODEC = Codec.unit(() -> JigsawReplacementProcessor.INSTANCE);
        INSTANCE = new JigsawReplacementProcessor();
    }
}

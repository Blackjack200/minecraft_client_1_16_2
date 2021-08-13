package net.minecraft.world.level.levelgen.structure;

import org.apache.logging.log4j.LogManager;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Iterator;
import java.util.List;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import com.mojang.brigadier.StringReader;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.ChunkPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.apache.logging.log4j.Logger;

public abstract class TemplateStructurePiece extends StructurePiece {
    private static final Logger LOGGER;
    protected StructureTemplate template;
    protected StructurePlaceSettings placeSettings;
    protected BlockPos templatePosition;
    
    public TemplateStructurePiece(final StructurePieceType cky, final int integer) {
        super(cky, integer);
    }
    
    public TemplateStructurePiece(final StructurePieceType cky, final CompoundTag md) {
        super(cky, md);
        this.templatePosition = new BlockPos(md.getInt("TPX"), md.getInt("TPY"), md.getInt("TPZ"));
    }
    
    protected void setup(final StructureTemplate csy, final BlockPos fx, final StructurePlaceSettings csu) {
        this.template = csy;
        this.setOrientation(Direction.NORTH);
        this.templatePosition = fx;
        this.placeSettings = csu;
        this.boundingBox = csy.getBoundingBox(csu, fx);
    }
    
    @Override
    protected void addAdditionalSaveData(final CompoundTag md) {
        md.putInt("TPX", this.templatePosition.getX());
        md.putInt("TPY", this.templatePosition.getY());
        md.putInt("TPZ", this.templatePosition.getZ());
    }
    
    @Override
    public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
        this.placeSettings.setBoundingBox(cqx);
        this.boundingBox = this.template.getBoundingBox(this.placeSettings, this.templatePosition);
        if (this.template.placeInWorld(bso, this.templatePosition, fx, this.placeSettings, random, 2)) {
            final List<StructureTemplate.StructureBlockInfo> list9 = this.template.filterBlocks(this.templatePosition, this.placeSettings, Blocks.STRUCTURE_BLOCK);
            for (final StructureTemplate.StructureBlockInfo c11 : list9) {
                if (c11.nbt == null) {
                    continue;
                }
                final StructureMode cfl12 = StructureMode.valueOf(c11.nbt.getString("mode"));
                if (cfl12 != StructureMode.DATA) {
                    continue;
                }
                this.handleDataMarker(c11.nbt.getString("metadata"), c11.pos, bso, random, cqx);
            }
            final List<StructureTemplate.StructureBlockInfo> list10 = this.template.filterBlocks(this.templatePosition, this.placeSettings, Blocks.JIGSAW);
            for (final StructureTemplate.StructureBlockInfo c12 : list10) {
                if (c12.nbt == null) {
                    continue;
                }
                final String string13 = c12.nbt.getString("final_state");
                final BlockStateParser ei14 = new BlockStateParser(new StringReader(string13), false);
                BlockState cee15 = Blocks.AIR.defaultBlockState();
                try {
                    ei14.parse(true);
                    final BlockState cee16 = ei14.getState();
                    if (cee16 != null) {
                        cee15 = cee16;
                    }
                    else {
                        TemplateStructurePiece.LOGGER.error("Error while parsing blockstate {} in jigsaw block @ {}", string13, c12.pos);
                    }
                }
                catch (CommandSyntaxException commandSyntaxException16) {
                    TemplateStructurePiece.LOGGER.error("Error while parsing blockstate {} in jigsaw block @ {}", string13, c12.pos);
                }
                bso.setBlock(c12.pos, cee15, 3);
            }
        }
        return true;
    }
    
    protected abstract void handleDataMarker(final String string, final BlockPos fx, final ServerLevelAccessor bsh, final Random random, final BoundingBox cqx);
    
    @Override
    public void move(final int integer1, final int integer2, final int integer3) {
        super.move(integer1, integer2, integer3);
        this.templatePosition = this.templatePosition.offset(integer1, integer2, integer3);
    }
    
    @Override
    public Rotation getRotation() {
        return this.placeSettings.getRotation();
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}

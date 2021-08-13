package net.minecraft.world.level.levelgen.feature.structures;

import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import java.util.Collections;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import java.util.List;
import java.util.Random;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import com.mojang.serialization.Codec;

public class EmptyPoolElement extends StructurePoolElement {
    public static final Codec<EmptyPoolElement> CODEC;
    public static final EmptyPoolElement INSTANCE;
    
    private EmptyPoolElement() {
        super(StructureTemplatePool.Projection.TERRAIN_MATCHING);
    }
    
    @Override
    public List<StructureTemplate.StructureBlockInfo> getShuffledJigsawBlocks(final StructureManager cst, final BlockPos fx, final Rotation bzj, final Random random) {
        return (List<StructureTemplate.StructureBlockInfo>)Collections.emptyList();
    }
    
    @Override
    public BoundingBox getBoundingBox(final StructureManager cst, final BlockPos fx, final Rotation bzj) {
        return BoundingBox.getUnknownBox();
    }
    
    @Override
    public boolean place(final StructureManager cst, final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final BlockPos fx5, final BlockPos fx6, final Rotation bzj, final BoundingBox cqx, final Random random, final boolean boolean10) {
        return true;
    }
    
    @Override
    public StructurePoolElementType<?> getType() {
        return StructurePoolElementType.EMPTY;
    }
    
    public String toString() {
        return "Empty";
    }
    
    static {
        CODEC = Codec.unit(() -> EmptyPoolElement.INSTANCE);
        INSTANCE = new EmptyPoolElement();
    }
}

package net.minecraft.world.level.levelgen.feature.structures;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.stream.Collectors;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import java.util.Iterator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import java.util.Random;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import java.util.List;
import com.mojang.serialization.Codec;

public class ListPoolElement extends StructurePoolElement {
    public static final Codec<ListPoolElement> CODEC;
    private final List<StructurePoolElement> elements;
    
    public ListPoolElement(final List<StructurePoolElement> list, final StructureTemplatePool.Projection a) {
        super(a);
        if (list.isEmpty()) {
            throw new IllegalArgumentException("Elements are empty");
        }
        this.elements = list;
        this.setProjectionOnEachElement(a);
    }
    
    @Override
    public List<StructureTemplate.StructureBlockInfo> getShuffledJigsawBlocks(final StructureManager cst, final BlockPos fx, final Rotation bzj, final Random random) {
        return ((StructurePoolElement)this.elements.get(0)).getShuffledJigsawBlocks(cst, fx, bzj, random);
    }
    
    @Override
    public BoundingBox getBoundingBox(final StructureManager cst, final BlockPos fx, final Rotation bzj) {
        final BoundingBox cqx5 = BoundingBox.getUnknownBox();
        for (final StructurePoolElement cof7 : this.elements) {
            final BoundingBox cqx6 = cof7.getBoundingBox(cst, fx, bzj);
            cqx5.expand(cqx6);
        }
        return cqx5;
    }
    
    @Override
    public boolean place(final StructureManager cst, final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final BlockPos fx5, final BlockPos fx6, final Rotation bzj, final BoundingBox cqx, final Random random, final boolean boolean10) {
        for (final StructurePoolElement cof13 : this.elements) {
            if (!cof13.place(cst, bso, bsk, cfv, fx5, fx6, bzj, cqx, random, boolean10)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public StructurePoolElementType<?> getType() {
        return StructurePoolElementType.LIST;
    }
    
    @Override
    public StructurePoolElement setProjection(final StructureTemplatePool.Projection a) {
        super.setProjection(a);
        this.setProjectionOnEachElement(a);
        return this;
    }
    
    public String toString() {
        return "List[" + (String)this.elements.stream().map(Object::toString).collect(Collectors.joining(", ")) + "]";
    }
    
    private void setProjectionOnEachElement(final StructureTemplatePool.Projection a) {
        this.elements.forEach(cof -> cof.setProjection(a));
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)StructurePoolElement.CODEC.listOf().fieldOf("elements").forGetter(cod -> cod.elements), (App)StructurePoolElement.<StructurePoolElement>projectionCodec()).apply((Applicative)instance, ListPoolElement::new));
    }
}

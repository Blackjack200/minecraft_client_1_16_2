package net.minecraft.world.level.levelgen.feature.structures;

import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.ProcessorLists;
import com.mojang.datafixers.util.Either;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import java.util.function.Function;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import java.util.List;
import java.util.Random;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import javax.annotation.Nullable;
import com.mojang.serialization.Codec;

public abstract class StructurePoolElement {
    public static final Codec<StructurePoolElement> CODEC;
    @Nullable
    private volatile StructureTemplatePool.Projection projection;
    
    protected static <E extends StructurePoolElement> RecordCodecBuilder<E, StructureTemplatePool.Projection> projectionCodec() {
        return (RecordCodecBuilder<E, StructureTemplatePool.Projection>)StructureTemplatePool.Projection.CODEC.fieldOf("projection").forGetter(StructurePoolElement::getProjection);
    }
    
    protected StructurePoolElement(final StructureTemplatePool.Projection a) {
        this.projection = a;
    }
    
    public abstract List<StructureTemplate.StructureBlockInfo> getShuffledJigsawBlocks(final StructureManager cst, final BlockPos fx, final Rotation bzj, final Random random);
    
    public abstract BoundingBox getBoundingBox(final StructureManager cst, final BlockPos fx, final Rotation bzj);
    
    public abstract boolean place(final StructureManager cst, final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final BlockPos fx5, final BlockPos fx6, final Rotation bzj, final BoundingBox cqx, final Random random, final boolean boolean10);
    
    public abstract StructurePoolElementType<?> getType();
    
    public void handleDataMarker(final LevelAccessor brv, final StructureTemplate.StructureBlockInfo c, final BlockPos fx, final Rotation bzj, final Random random, final BoundingBox cqx) {
    }
    
    public StructurePoolElement setProjection(final StructureTemplatePool.Projection a) {
        this.projection = a;
        return this;
    }
    
    public StructureTemplatePool.Projection getProjection() {
        final StructureTemplatePool.Projection a2 = this.projection;
        if (a2 == null) {
            throw new IllegalStateException();
        }
        return a2;
    }
    
    public int getGroundLevelDelta() {
        return 1;
    }
    
    public static Function<StructureTemplatePool.Projection, EmptyPoolElement> empty() {
        return (Function<StructureTemplatePool.Projection, EmptyPoolElement>)(a -> EmptyPoolElement.INSTANCE);
    }
    
    public static Function<StructureTemplatePool.Projection, LegacySinglePoolElement> legacy(final String string) {
        return (Function<StructureTemplatePool.Projection, LegacySinglePoolElement>)(a -> new LegacySinglePoolElement((Either<ResourceLocation, StructureTemplate>)Either.left(new ResourceLocation(string)), (Supplier<StructureProcessorList>)(() -> ProcessorLists.EMPTY), a));
    }
    
    public static Function<StructureTemplatePool.Projection, LegacySinglePoolElement> legacy(final String string, final StructureProcessorList csw) {
        return (Function<StructureTemplatePool.Projection, LegacySinglePoolElement>)(a -> new LegacySinglePoolElement((Either<ResourceLocation, StructureTemplate>)Either.left(new ResourceLocation(string)), (Supplier<StructureProcessorList>)(() -> csw), a));
    }
    
    public static Function<StructureTemplatePool.Projection, SinglePoolElement> single(final String string) {
        return (Function<StructureTemplatePool.Projection, SinglePoolElement>)(a -> new SinglePoolElement((Either<ResourceLocation, StructureTemplate>)Either.left(new ResourceLocation(string)), (Supplier<StructureProcessorList>)(() -> ProcessorLists.EMPTY), a));
    }
    
    public static Function<StructureTemplatePool.Projection, SinglePoolElement> single(final String string, final StructureProcessorList csw) {
        return (Function<StructureTemplatePool.Projection, SinglePoolElement>)(a -> new SinglePoolElement((Either<ResourceLocation, StructureTemplate>)Either.left(new ResourceLocation(string)), (Supplier<StructureProcessorList>)(() -> csw), a));
    }
    
    public static Function<StructureTemplatePool.Projection, FeaturePoolElement> feature(final ConfiguredFeature<?, ?> cis) {
        return (Function<StructureTemplatePool.Projection, FeaturePoolElement>)(a -> new FeaturePoolElement((Supplier<ConfiguredFeature<?, ?>>)(() -> cis), a));
    }
    
    public static Function<StructureTemplatePool.Projection, ListPoolElement> list(final List<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>> list) {
        return (Function<StructureTemplatePool.Projection, ListPoolElement>)(a -> new ListPoolElement((List<StructurePoolElement>)list.stream().map(function -> (StructurePoolElement)function.apply(a)).collect(Collectors.toList()), a));
    }
    
    static {
        CODEC = Registry.STRUCTURE_POOL_ELEMENT.dispatch("element_type", StructurePoolElement::getType, StructurePoolElementType::codec);
    }
}

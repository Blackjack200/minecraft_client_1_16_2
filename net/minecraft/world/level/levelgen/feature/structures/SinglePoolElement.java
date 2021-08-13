package net.minecraft.world.level.levelgen.feature.structures;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.world.level.levelgen.structure.templatesystem.JigsawReplacementProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import java.util.Collections;
import java.util.Random;
import java.util.Iterator;
import net.minecraft.world.level.block.state.properties.StructureMode;
import com.google.common.collect.Lists;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import java.util.List;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import java.util.function.Function;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import java.util.function.Supplier;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.resources.ResourceLocation;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;

public class SinglePoolElement extends StructurePoolElement {
    private static final Codec<Either<ResourceLocation, StructureTemplate>> TEMPLATE_CODEC;
    public static final Codec<SinglePoolElement> CODEC;
    protected final Either<ResourceLocation, StructureTemplate> template;
    protected final Supplier<StructureProcessorList> processors;
    
    private static <T> DataResult<T> encodeTemplate(final Either<ResourceLocation, StructureTemplate> either, final DynamicOps<T> dynamicOps, final T object) {
        final Optional<ResourceLocation> optional4 = (Optional<ResourceLocation>)either.left();
        if (!optional4.isPresent()) {
            return (DataResult<T>)DataResult.error("Can not serialize a runtime pool element");
        }
        return (DataResult<T>)ResourceLocation.CODEC.encode(optional4.get(), (DynamicOps)dynamicOps, object);
    }
    
    protected static <E extends SinglePoolElement> RecordCodecBuilder<E, Supplier<StructureProcessorList>> processorsCodec() {
        return (RecordCodecBuilder<E, Supplier<StructureProcessorList>>)StructureProcessorType.LIST_CODEC.fieldOf("processors").forGetter(coe -> coe.processors);
    }
    
    protected static <E extends SinglePoolElement> RecordCodecBuilder<E, Either<ResourceLocation, StructureTemplate>> templateCodec() {
        return (RecordCodecBuilder<E, Either<ResourceLocation, StructureTemplate>>)SinglePoolElement.TEMPLATE_CODEC.fieldOf("location").forGetter(coe -> coe.template);
    }
    
    protected SinglePoolElement(final Either<ResourceLocation, StructureTemplate> either, final Supplier<StructureProcessorList> supplier, final StructureTemplatePool.Projection a) {
        super(a);
        this.template = either;
        this.processors = supplier;
    }
    
    public SinglePoolElement(final StructureTemplate csy) {
        this((Either<ResourceLocation, StructureTemplate>)Either.right(csy), (Supplier<StructureProcessorList>)(() -> ProcessorLists.EMPTY), StructureTemplatePool.Projection.RIGID);
    }
    
    private StructureTemplate getTemplate(final StructureManager cst) {
        return (StructureTemplate)this.template.map(cst::getOrCreate, Function.identity());
    }
    
    public List<StructureTemplate.StructureBlockInfo> getDataMarkers(final StructureManager cst, final BlockPos fx, final Rotation bzj, final boolean boolean4) {
        final StructureTemplate csy6 = this.getTemplate(cst);
        final List<StructureTemplate.StructureBlockInfo> list7 = csy6.filterBlocks(fx, new StructurePlaceSettings().setRotation(bzj), Blocks.STRUCTURE_BLOCK, boolean4);
        final List<StructureTemplate.StructureBlockInfo> list8 = (List<StructureTemplate.StructureBlockInfo>)Lists.newArrayList();
        for (final StructureTemplate.StructureBlockInfo c10 : list7) {
            if (c10.nbt == null) {
                continue;
            }
            final StructureMode cfl11 = StructureMode.valueOf(c10.nbt.getString("mode"));
            if (cfl11 != StructureMode.DATA) {
                continue;
            }
            list8.add(c10);
        }
        return list8;
    }
    
    @Override
    public List<StructureTemplate.StructureBlockInfo> getShuffledJigsawBlocks(final StructureManager cst, final BlockPos fx, final Rotation bzj, final Random random) {
        final StructureTemplate csy6 = this.getTemplate(cst);
        final List<StructureTemplate.StructureBlockInfo> list7 = csy6.filterBlocks(fx, new StructurePlaceSettings().setRotation(bzj), Blocks.JIGSAW, true);
        Collections.shuffle((List)list7, random);
        return list7;
    }
    
    @Override
    public BoundingBox getBoundingBox(final StructureManager cst, final BlockPos fx, final Rotation bzj) {
        final StructureTemplate csy5 = this.getTemplate(cst);
        return csy5.getBoundingBox(new StructurePlaceSettings().setRotation(bzj), fx);
    }
    
    @Override
    public boolean place(final StructureManager cst, final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final BlockPos fx5, final BlockPos fx6, final Rotation bzj, final BoundingBox cqx, final Random random, final boolean boolean10) {
        final StructureTemplate csy12 = this.getTemplate(cst);
        final StructurePlaceSettings csu13 = this.getSettings(bzj, cqx, boolean10);
        if (csy12.placeInWorld(bso, fx5, fx6, csu13, random, 18)) {
            final List<StructureTemplate.StructureBlockInfo> list14 = StructureTemplate.processBlockInfos(bso, fx5, fx6, csu13, this.getDataMarkers(cst, fx5, bzj, false));
            for (final StructureTemplate.StructureBlockInfo c16 : list14) {
                this.handleDataMarker(bso, c16, fx5, bzj, random, cqx);
            }
            return true;
        }
        return false;
    }
    
    protected StructurePlaceSettings getSettings(final Rotation bzj, final BoundingBox cqx, final boolean boolean3) {
        final StructurePlaceSettings csu5 = new StructurePlaceSettings();
        csu5.setBoundingBox(cqx);
        csu5.setRotation(bzj);
        csu5.setKnownShape(true);
        csu5.setIgnoreEntities(false);
        csu5.addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        csu5.setFinalizeEntities(true);
        if (!boolean3) {
            csu5.addProcessor(JigsawReplacementProcessor.INSTANCE);
        }
        ((StructureProcessorList)this.processors.get()).list().forEach(csu5::addProcessor);
        this.getProjection().getProcessors().forEach(csu5::addProcessor);
        return csu5;
    }
    
    @Override
    public StructurePoolElementType<?> getType() {
        return StructurePoolElementType.SINGLE;
    }
    
    public String toString() {
        return new StringBuilder().append("Single[").append(this.template).append("]").toString();
    }
    
    static {
        TEMPLATE_CODEC = Codec.of(SinglePoolElement::encodeTemplate, ResourceLocation.CODEC.map(Either::left));
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)SinglePoolElement.<SinglePoolElement>templateCodec(), (App)SinglePoolElement.<SinglePoolElement>processorsCodec(), (App)StructurePoolElement.<StructurePoolElement>projectionCodec()).apply((Applicative)instance, SinglePoolElement::new));
    }
}

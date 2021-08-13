package net.minecraft.world.level.levelgen.feature.structures;

import net.minecraft.world.level.block.state.StateHolder;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import com.google.common.collect.Lists;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import java.util.List;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.block.entity.JigsawBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import java.util.function.Supplier;
import com.mojang.serialization.Codec;

public class FeaturePoolElement extends StructurePoolElement {
    public static final Codec<FeaturePoolElement> CODEC;
    private final Supplier<ConfiguredFeature<?, ?>> feature;
    private final CompoundTag defaultJigsawNBT;
    
    protected FeaturePoolElement(final Supplier<ConfiguredFeature<?, ?>> supplier, final StructureTemplatePool.Projection a) {
        super(a);
        this.feature = supplier;
        this.defaultJigsawNBT = this.fillDefaultJigsawNBT();
    }
    
    private CompoundTag fillDefaultJigsawNBT() {
        final CompoundTag md2 = new CompoundTag();
        md2.putString("name", "minecraft:bottom");
        md2.putString("final_state", "minecraft:air");
        md2.putString("pool", "minecraft:empty");
        md2.putString("target", "minecraft:empty");
        md2.putString("joint", JigsawBlockEntity.JointType.ROLLABLE.getSerializedName());
        return md2;
    }
    
    public BlockPos getSize(final StructureManager cst, final Rotation bzj) {
        return BlockPos.ZERO;
    }
    
    @Override
    public List<StructureTemplate.StructureBlockInfo> getShuffledJigsawBlocks(final StructureManager cst, final BlockPos fx, final Rotation bzj, final Random random) {
        final List<StructureTemplate.StructureBlockInfo> list6 = (List<StructureTemplate.StructureBlockInfo>)Lists.newArrayList();
        list6.add(new StructureTemplate.StructureBlockInfo(fx, ((StateHolder<O, BlockState>)Blocks.JIGSAW.defaultBlockState()).<FrontAndTop, FrontAndTop>setValue(JigsawBlock.ORIENTATION, FrontAndTop.fromFrontAndTop(Direction.DOWN, Direction.SOUTH)), this.defaultJigsawNBT));
        return list6;
    }
    
    @Override
    public BoundingBox getBoundingBox(final StructureManager cst, final BlockPos fx, final Rotation bzj) {
        final BlockPos fx2 = this.getSize(cst, bzj);
        return new BoundingBox(fx.getX(), fx.getY(), fx.getZ(), fx.getX() + fx2.getX(), fx.getY() + fx2.getY(), fx.getZ() + fx2.getZ());
    }
    
    @Override
    public boolean place(final StructureManager cst, final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final BlockPos fx5, final BlockPos fx6, final Rotation bzj, final BoundingBox cqx, final Random random, final boolean boolean10) {
        return ((ConfiguredFeature)this.feature.get()).place(bso, cfv, random, fx5);
    }
    
    @Override
    public StructurePoolElementType<?> getType() {
        return StructurePoolElementType.FEATURE;
    }
    
    public String toString() {
        return new StringBuilder().append("Feature[").append(Registry.FEATURE.getKey(((ConfiguredFeature)this.feature.get()).feature())).append("]").toString();
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)ConfiguredFeature.CODEC.fieldOf("feature").forGetter(cnz -> cnz.feature), (App)StructurePoolElement.<StructurePoolElement>projectionCodec()).apply((Applicative)instance, FeaturePoolElement::new));
    }
}

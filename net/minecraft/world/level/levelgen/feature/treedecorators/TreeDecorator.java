package net.minecraft.world.level.levelgen.feature.treedecorators;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import java.util.Set;
import net.minecraft.core.BlockPos;
import java.util.List;
import java.util.Random;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;

public abstract class TreeDecorator {
    public static final Codec<TreeDecorator> CODEC;
    
    protected abstract TreeDecoratorType<?> type();
    
    public abstract void place(final WorldGenLevel bso, final Random random, final List<BlockPos> list3, final List<BlockPos> list4, final Set<BlockPos> set, final BoundingBox cqx);
    
    protected void placeVine(final LevelWriter bsb, final BlockPos fx, final BooleanProperty cev, final Set<BlockPos> set, final BoundingBox cqx) {
        this.setBlock(bsb, fx, ((StateHolder<O, BlockState>)Blocks.VINE.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)cev, true), set, cqx);
    }
    
    protected void setBlock(final LevelWriter bsb, final BlockPos fx, final BlockState cee, final Set<BlockPos> set, final BoundingBox cqx) {
        bsb.setBlock(fx, cee, 19);
        set.add(fx);
        cqx.expand(new BoundingBox(fx, fx));
    }
    
    static {
        CODEC = Registry.TREE_DECORATOR_TYPES.dispatch(TreeDecorator::type, TreeDecoratorType::codec);
    }
}

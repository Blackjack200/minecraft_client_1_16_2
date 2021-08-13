package net.minecraft.commands.arguments.blocks;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import java.util.Iterator;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtUtils;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.properties.Property;
import java.util.Set;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import java.util.function.Predicate;

public class BlockInput implements Predicate<BlockInWorld> {
    private final BlockState state;
    private final Set<Property<?>> properties;
    @Nullable
    private final CompoundTag tag;
    
    public BlockInput(final BlockState cee, final Set<Property<?>> set, @Nullable final CompoundTag md) {
        this.state = cee;
        this.properties = set;
        this.tag = md;
    }
    
    public BlockState getState() {
        return this.state;
    }
    
    public boolean test(final BlockInWorld cei) {
        final BlockState cee3 = cei.getState();
        if (!cee3.is(this.state.getBlock())) {
            return false;
        }
        for (final Property<?> cfg5 : this.properties) {
            if (cee3.getValue(cfg5) != this.state.getValue(cfg5)) {
                return false;
            }
        }
        if (this.tag != null) {
            final BlockEntity ccg4 = cei.getEntity();
            return ccg4 != null && NbtUtils.compareNbt(this.tag, ccg4.save(new CompoundTag()), true);
        }
        return true;
    }
    
    public boolean place(final ServerLevel aag, final BlockPos fx, final int integer) {
        BlockState cee5 = Block.updateFromNeighbourShapes(this.state, aag, fx);
        if (cee5.isAir()) {
            cee5 = this.state;
        }
        if (!aag.setBlock(fx, cee5, integer)) {
            return false;
        }
        if (this.tag != null) {
            final BlockEntity ccg6 = aag.getBlockEntity(fx);
            if (ccg6 != null) {
                final CompoundTag md7 = this.tag.copy();
                md7.putInt("x", fx.getX());
                md7.putInt("y", fx.getY());
                md7.putInt("z", fx.getZ());
                ccg6.load(cee5, md7);
            }
        }
        return true;
    }
}

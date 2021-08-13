package net.minecraft.client.color.block;

import net.minecraft.core.BlockPos;
import javax.annotation.Nullable;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockColor {
    int getColor(final BlockState cee, @Nullable final BlockAndTintGetter bqx, @Nullable final BlockPos fx, final int integer);
}

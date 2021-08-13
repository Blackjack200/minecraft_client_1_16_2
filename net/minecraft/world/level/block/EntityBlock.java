package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;

public interface EntityBlock {
    @Nullable
    BlockEntity newBlockEntity(final BlockGetter bqz);
}

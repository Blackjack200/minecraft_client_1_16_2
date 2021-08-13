package net.minecraft.data.models.blockstates;

import net.minecraft.world.level.block.Block;
import com.google.gson.JsonElement;
import java.util.function.Supplier;

public interface BlockStateGenerator extends Supplier<JsonElement> {
    Block getBlock();
}

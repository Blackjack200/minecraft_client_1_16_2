package net.minecraft.world.level.block;

import java.util.List;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class PlayerWallHeadBlock extends WallSkullBlock {
    protected PlayerWallHeadBlock(final Properties c) {
        super(SkullBlock.Types.PLAYER, c);
    }
    
    @Override
    public void setPlacedBy(final Level bru, final BlockPos fx, final BlockState cee, @Nullable final LivingEntity aqj, final ItemStack bly) {
        Blocks.PLAYER_HEAD.setPlacedBy(bru, fx, cee, aqj, bly);
    }
    
    public List<ItemStack> getDrops(final BlockState cee, final LootContext.Builder a) {
        return Blocks.PLAYER_HEAD.getDrops(cee, a);
    }
}

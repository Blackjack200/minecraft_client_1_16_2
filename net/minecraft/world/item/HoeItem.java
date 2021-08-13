package net.minecraft.world.item;

import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import java.util.function.Consumer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Map;
import net.minecraft.world.level.block.Block;
import java.util.Set;

public class HoeItem extends DiggerItem {
    private static final Set<Block> DIGGABLES;
    protected static final Map<Block, BlockState> TILLABLES;
    
    protected HoeItem(final Tier bne, final int integer, final float float3, final Properties a) {
        super((float)integer, float3, bne, HoeItem.DIGGABLES, a);
    }
    
    @Override
    public InteractionResult useOn(final UseOnContext bnx) {
        final Level bru3 = bnx.getLevel();
        final BlockPos fx4 = bnx.getClickedPos();
        if (bnx.getClickedFace() != Direction.DOWN && bru3.getBlockState(fx4.above()).isAir()) {
            final BlockState cee5 = (BlockState)HoeItem.TILLABLES.get(bru3.getBlockState(fx4).getBlock());
            if (cee5 != null) {
                final Player bft6 = bnx.getPlayer();
                bru3.playSound(bft6, fx4, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0f, 1.0f);
                if (!bru3.isClientSide) {
                    bru3.setBlock(fx4, cee5, 11);
                    if (bft6 != null) {
                        bnx.getItemInHand().<Player>hurtAndBreak(1, bft6, (java.util.function.Consumer<Player>)(bft -> bft.broadcastBreakEvent(bnx.getHand())));
                    }
                }
                return InteractionResult.sidedSuccess(bru3.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }
    
    static {
        DIGGABLES = (Set)ImmutableSet.of(Blocks.NETHER_WART_BLOCK, Blocks.WARPED_WART_BLOCK, Blocks.HAY_BLOCK, Blocks.DRIED_KELP_BLOCK, Blocks.TARGET, Blocks.SHROOMLIGHT, (Object[])new Block[] { Blocks.SPONGE, Blocks.WET_SPONGE, Blocks.JUNGLE_LEAVES, Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.ACACIA_LEAVES, Blocks.BIRCH_LEAVES });
        TILLABLES = (Map)Maps.newHashMap((Map)ImmutableMap.of(Blocks.GRASS_BLOCK, Blocks.FARMLAND.defaultBlockState(), Blocks.GRASS_PATH, Blocks.FARMLAND.defaultBlockState(), Blocks.DIRT, Blocks.FARMLAND.defaultBlockState(), Blocks.COARSE_DIRT, Blocks.DIRT.defaultBlockState()));
    }
}

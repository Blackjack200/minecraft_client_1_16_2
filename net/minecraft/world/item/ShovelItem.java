package net.minecraft.world.item;

import net.minecraft.world.level.block.state.StateHolder;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import java.util.function.Consumer;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Map;
import net.minecraft.world.level.block.Block;
import java.util.Set;

public class ShovelItem extends DiggerItem {
    private static final Set<Block> DIGGABLES;
    protected static final Map<Block, BlockState> FLATTENABLES;
    
    public ShovelItem(final Tier bne, final float float2, final float float3, final Properties a) {
        super(float2, float3, bne, ShovelItem.DIGGABLES, a);
    }
    
    @Override
    public boolean isCorrectToolForDrops(final BlockState cee) {
        return cee.is(Blocks.SNOW) || cee.is(Blocks.SNOW_BLOCK);
    }
    
    @Override
    public InteractionResult useOn(final UseOnContext bnx) {
        final Level bru3 = bnx.getLevel();
        final BlockPos fx4 = bnx.getClickedPos();
        final BlockState cee5 = bru3.getBlockState(fx4);
        if (bnx.getClickedFace() == Direction.DOWN) {
            return InteractionResult.PASS;
        }
        final Player bft6 = bnx.getPlayer();
        final BlockState cee6 = (BlockState)ShovelItem.FLATTENABLES.get(cee5.getBlock());
        BlockState cee7 = null;
        if (cee6 != null && bru3.getBlockState(fx4.above()).isAir()) {
            bru3.playSound(bft6, fx4, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0f, 1.0f);
            cee7 = cee6;
        }
        else if (cee5.getBlock() instanceof CampfireBlock && cee5.<Boolean>getValue((Property<Boolean>)CampfireBlock.LIT)) {
            if (!bru3.isClientSide()) {
                bru3.levelEvent(null, 1009, fx4, 0);
            }
            CampfireBlock.dowse(bru3, fx4, cee5);
            cee7 = ((StateHolder<O, BlockState>)cee5).<Comparable, Boolean>setValue((Property<Comparable>)CampfireBlock.LIT, false);
        }
        if (cee7 != null) {
            if (!bru3.isClientSide) {
                bru3.setBlock(fx4, cee7, 11);
                if (bft6 != null) {
                    bnx.getItemInHand().<Player>hurtAndBreak(1, bft6, (java.util.function.Consumer<Player>)(bft -> bft.broadcastBreakEvent(bnx.getHand())));
                }
            }
            return InteractionResult.sidedSuccess(bru3.isClientSide);
        }
        return InteractionResult.PASS;
    }
    
    static {
        DIGGABLES = (Set)Sets.newHashSet((Object[])new Block[] { Blocks.CLAY, Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.PODZOL, Blocks.FARMLAND, Blocks.GRASS_BLOCK, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.RED_SAND, Blocks.SNOW_BLOCK, Blocks.SNOW, Blocks.SOUL_SAND, Blocks.GRASS_PATH, Blocks.WHITE_CONCRETE_POWDER, Blocks.ORANGE_CONCRETE_POWDER, Blocks.MAGENTA_CONCRETE_POWDER, Blocks.LIGHT_BLUE_CONCRETE_POWDER, Blocks.YELLOW_CONCRETE_POWDER, Blocks.LIME_CONCRETE_POWDER, Blocks.PINK_CONCRETE_POWDER, Blocks.GRAY_CONCRETE_POWDER, Blocks.LIGHT_GRAY_CONCRETE_POWDER, Blocks.CYAN_CONCRETE_POWDER, Blocks.PURPLE_CONCRETE_POWDER, Blocks.BLUE_CONCRETE_POWDER, Blocks.BROWN_CONCRETE_POWDER, Blocks.GREEN_CONCRETE_POWDER, Blocks.RED_CONCRETE_POWDER, Blocks.BLACK_CONCRETE_POWDER, Blocks.SOUL_SOIL });
        FLATTENABLES = (Map)Maps.newHashMap((Map)ImmutableMap.of(Blocks.GRASS_BLOCK, Blocks.GRASS_PATH.defaultBlockState()));
    }
}

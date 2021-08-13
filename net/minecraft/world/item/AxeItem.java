package net.minecraft.world.item;

import net.minecraft.world.level.block.state.StateHolder;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.level.block.Blocks;
import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import java.util.function.Consumer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Map;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import java.util.Set;

public class AxeItem extends DiggerItem {
    private static final Set<Material> DIGGABLE_MATERIALS;
    private static final Set<Block> OTHER_DIGGABLE_BLOCKS;
    protected static final Map<Block, Block> STRIPABLES;
    
    protected AxeItem(final Tier bne, final float float2, final float float3, final Properties a) {
        super(float2, float3, bne, AxeItem.OTHER_DIGGABLE_BLOCKS, a);
    }
    
    @Override
    public float getDestroySpeed(final ItemStack bly, final BlockState cee) {
        final Material cux4 = cee.getMaterial();
        if (AxeItem.DIGGABLE_MATERIALS.contains(cux4)) {
            return this.speed;
        }
        return super.getDestroySpeed(bly, cee);
    }
    
    @Override
    public InteractionResult useOn(final UseOnContext bnx) {
        final Level bru3 = bnx.getLevel();
        final BlockPos fx4 = bnx.getClickedPos();
        final BlockState cee5 = bru3.getBlockState(fx4);
        final Block bul6 = (Block)AxeItem.STRIPABLES.get(cee5.getBlock());
        if (bul6 != null) {
            final Player bft7 = bnx.getPlayer();
            bru3.playSound(bft7, fx4, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0f, 1.0f);
            if (!bru3.isClientSide) {
                bru3.setBlock(fx4, ((StateHolder<O, BlockState>)bul6.defaultBlockState()).<Direction.Axis, Comparable>setValue(RotatedPillarBlock.AXIS, (Comparable)cee5.<V>getValue((Property<V>)RotatedPillarBlock.AXIS)), 11);
                if (bft7 != null) {
                    bnx.getItemInHand().<Player>hurtAndBreak(1, bft7, (java.util.function.Consumer<Player>)(bft -> bft.broadcastBreakEvent(bnx.getHand())));
                }
            }
            return InteractionResult.sidedSuccess(bru3.isClientSide);
        }
        return InteractionResult.PASS;
    }
    
    static {
        DIGGABLE_MATERIALS = (Set)Sets.newHashSet((Object[])new Material[] { Material.WOOD, Material.NETHER_WOOD, Material.PLANT, Material.REPLACEABLE_PLANT, Material.BAMBOO, Material.VEGETABLE });
        OTHER_DIGGABLE_BLOCKS = (Set)Sets.newHashSet((Object[])new Block[] { Blocks.LADDER, Blocks.SCAFFOLDING, Blocks.OAK_BUTTON, Blocks.SPRUCE_BUTTON, Blocks.BIRCH_BUTTON, Blocks.JUNGLE_BUTTON, Blocks.DARK_OAK_BUTTON, Blocks.ACACIA_BUTTON, Blocks.CRIMSON_BUTTON, Blocks.WARPED_BUTTON });
        STRIPABLES = (Map)new ImmutableMap.Builder().put(Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD).put(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG).put(Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD).put(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG).put(Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_WOOD).put(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG).put(Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD).put(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG).put(Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_WOOD).put(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG).put(Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD).put(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG).put(Blocks.WARPED_STEM, Blocks.STRIPPED_WARPED_STEM).put(Blocks.WARPED_HYPHAE, Blocks.STRIPPED_WARPED_HYPHAE).put(Blocks.CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_STEM).put(Blocks.CRIMSON_HYPHAE, Blocks.STRIPPED_CRIMSON_HYPHAE).build();
    }
}

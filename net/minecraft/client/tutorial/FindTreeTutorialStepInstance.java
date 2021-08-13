package net.minecraft.client.tutorial;

import net.minecraft.network.chat.TranslatableComponent;
import com.google.common.collect.Sets;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.client.multiplayer.ClientLevel;
import java.util.Iterator;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import java.util.Set;

public class FindTreeTutorialStepInstance implements TutorialStepInstance {
    private static final Set<Block> TREE_BLOCKS;
    private static final Component TITLE;
    private static final Component DESCRIPTION;
    private final Tutorial tutorial;
    private TutorialToast toast;
    private int timeWaiting;
    
    public FindTreeTutorialStepInstance(final Tutorial enw) {
        this.tutorial = enw;
    }
    
    public void tick() {
        ++this.timeWaiting;
        if (this.tutorial.getGameMode() != GameType.SURVIVAL) {
            this.tutorial.setStep(TutorialSteps.NONE);
            return;
        }
        if (this.timeWaiting == 1) {
            final LocalPlayer dze2 = this.tutorial.getMinecraft().player;
            if (dze2 != null) {
                for (final Block bul4 : FindTreeTutorialStepInstance.TREE_BLOCKS) {
                    if (dze2.inventory.contains(new ItemStack(bul4))) {
                        this.tutorial.setStep(TutorialSteps.CRAFT_PLANKS);
                        return;
                    }
                }
                if (hasPunchedTreesPreviously(dze2)) {
                    this.tutorial.setStep(TutorialSteps.CRAFT_PLANKS);
                    return;
                }
            }
        }
        if (this.timeWaiting >= 6000 && this.toast == null) {
            this.toast = new TutorialToast(TutorialToast.Icons.TREE, FindTreeTutorialStepInstance.TITLE, FindTreeTutorialStepInstance.DESCRIPTION, false);
            this.tutorial.getMinecraft().getToasts().addToast(this.toast);
        }
    }
    
    public void clear() {
        if (this.toast != null) {
            this.toast.hide();
            this.toast = null;
        }
    }
    
    public void onLookAt(final ClientLevel dwl, final HitResult dci) {
        if (dci.getType() == HitResult.Type.BLOCK) {
            final BlockState cee4 = dwl.getBlockState(((BlockHitResult)dci).getBlockPos());
            if (FindTreeTutorialStepInstance.TREE_BLOCKS.contains(cee4.getBlock())) {
                this.tutorial.setStep(TutorialSteps.PUNCH_TREE);
            }
        }
    }
    
    public void onGetItem(final ItemStack bly) {
        for (final Block bul4 : FindTreeTutorialStepInstance.TREE_BLOCKS) {
            if (bly.getItem() == bul4.asItem()) {
                this.tutorial.setStep(TutorialSteps.CRAFT_PLANKS);
            }
        }
    }
    
    public static boolean hasPunchedTreesPreviously(final LocalPlayer dze) {
        for (final Block bul3 : FindTreeTutorialStepInstance.TREE_BLOCKS) {
            if (dze.getStats().getValue(Stats.BLOCK_MINED.get(bul3)) > 0) {
                return true;
            }
        }
        return false;
    }
    
    static {
        TREE_BLOCKS = (Set)Sets.newHashSet((Object[])new Block[] { Blocks.OAK_LOG, Blocks.SPRUCE_LOG, Blocks.BIRCH_LOG, Blocks.JUNGLE_LOG, Blocks.ACACIA_LOG, Blocks.DARK_OAK_LOG, Blocks.WARPED_STEM, Blocks.CRIMSON_STEM, Blocks.OAK_WOOD, Blocks.SPRUCE_WOOD, Blocks.BIRCH_WOOD, Blocks.JUNGLE_WOOD, Blocks.ACACIA_WOOD, Blocks.DARK_OAK_WOOD, Blocks.WARPED_HYPHAE, Blocks.CRIMSON_HYPHAE, Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES, Blocks.BIRCH_LEAVES, Blocks.JUNGLE_LEAVES, Blocks.ACACIA_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.NETHER_WART_BLOCK, Blocks.WARPED_WART_BLOCK });
        TITLE = new TranslatableComponent("tutorial.find_tree.title");
        DESCRIPTION = new TranslatableComponent("tutorial.find_tree.description");
    }
}

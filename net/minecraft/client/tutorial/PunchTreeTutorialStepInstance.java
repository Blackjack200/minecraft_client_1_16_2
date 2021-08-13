package net.minecraft.client.tutorial;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.world.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.GameType;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import net.minecraft.network.chat.Component;

public class PunchTreeTutorialStepInstance implements TutorialStepInstance {
    private static final Component TITLE;
    private static final Component DESCRIPTION;
    private final Tutorial tutorial;
    private TutorialToast toast;
    private int timeWaiting;
    private int resetCount;
    
    public PunchTreeTutorialStepInstance(final Tutorial enw) {
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
                if (dze2.inventory.contains(ItemTags.LOGS)) {
                    this.tutorial.setStep(TutorialSteps.CRAFT_PLANKS);
                    return;
                }
                if (FindTreeTutorialStepInstance.hasPunchedTreesPreviously(dze2)) {
                    this.tutorial.setStep(TutorialSteps.CRAFT_PLANKS);
                    return;
                }
            }
        }
        if ((this.timeWaiting >= 600 || this.resetCount > 3) && this.toast == null) {
            this.toast = new TutorialToast(TutorialToast.Icons.TREE, PunchTreeTutorialStepInstance.TITLE, PunchTreeTutorialStepInstance.DESCRIPTION, true);
            this.tutorial.getMinecraft().getToasts().addToast(this.toast);
        }
    }
    
    public void clear() {
        if (this.toast != null) {
            this.toast.hide();
            this.toast = null;
        }
    }
    
    public void onDestroyBlock(final ClientLevel dwl, final BlockPos fx, final BlockState cee, final float float4) {
        final boolean boolean6 = cee.is(BlockTags.LOGS);
        if (boolean6 && float4 > 0.0f) {
            if (this.toast != null) {
                this.toast.updateProgress(float4);
            }
            if (float4 >= 1.0f) {
                this.tutorial.setStep(TutorialSteps.OPEN_INVENTORY);
            }
        }
        else if (this.toast != null) {
            this.toast.updateProgress(0.0f);
        }
        else if (boolean6) {
            ++this.resetCount;
        }
    }
    
    public void onGetItem(final ItemStack bly) {
        if (ItemTags.LOGS.contains(bly.getItem())) {
            this.tutorial.setStep(TutorialSteps.CRAFT_PLANKS);
        }
    }
    
    static {
        TITLE = new TranslatableComponent("tutorial.punch_tree.title");
        DESCRIPTION = new TranslatableComponent("tutorial.punch_tree.description", new Object[] { Tutorial.key("attack") });
    }
}

package net.minecraft.client.tutorial;

import net.minecraft.network.chat.TranslatableComponent;
import java.util.Iterator;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.world.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.GameType;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import net.minecraft.network.chat.Component;

public class CraftPlanksTutorialStep implements TutorialStepInstance {
    private static final Component CRAFT_TITLE;
    private static final Component CRAFT_DESCRIPTION;
    private final Tutorial tutorial;
    private TutorialToast toast;
    private int timeWaiting;
    
    public CraftPlanksTutorialStep(final Tutorial enw) {
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
                if (dze2.inventory.contains(ItemTags.PLANKS)) {
                    this.tutorial.setStep(TutorialSteps.NONE);
                    return;
                }
                if (hasCraftedPlanksPreviously(dze2, ItemTags.PLANKS)) {
                    this.tutorial.setStep(TutorialSteps.NONE);
                    return;
                }
            }
        }
        if (this.timeWaiting >= 1200 && this.toast == null) {
            this.toast = new TutorialToast(TutorialToast.Icons.WOODEN_PLANKS, CraftPlanksTutorialStep.CRAFT_TITLE, CraftPlanksTutorialStep.CRAFT_DESCRIPTION, false);
            this.tutorial.getMinecraft().getToasts().addToast(this.toast);
        }
    }
    
    public void clear() {
        if (this.toast != null) {
            this.toast.hide();
            this.toast = null;
        }
    }
    
    public void onGetItem(final ItemStack bly) {
        final Item blu3 = bly.getItem();
        if (ItemTags.PLANKS.contains(blu3)) {
            this.tutorial.setStep(TutorialSteps.NONE);
        }
    }
    
    public static boolean hasCraftedPlanksPreviously(final LocalPlayer dze, final Tag<Item> aej) {
        for (final Item blu4 : aej.getValues()) {
            if (dze.getStats().getValue(Stats.ITEM_CRAFTED.get(blu4)) > 0) {
                return true;
            }
        }
        return false;
    }
    
    static {
        CRAFT_TITLE = new TranslatableComponent("tutorial.craft_planks.title");
        CRAFT_DESCRIPTION = new TranslatableComponent("tutorial.craft_planks.description");
    }
}

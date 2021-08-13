package net.minecraft.client.tutorial;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.world.level.GameType;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import net.minecraft.network.chat.Component;

public class OpenInventoryTutorialStep implements TutorialStepInstance {
    private static final Component TITLE;
    private static final Component DESCRIPTION;
    private final Tutorial tutorial;
    private TutorialToast toast;
    private int timeWaiting;
    
    public OpenInventoryTutorialStep(final Tutorial enw) {
        this.tutorial = enw;
    }
    
    public void tick() {
        ++this.timeWaiting;
        if (this.tutorial.getGameMode() != GameType.SURVIVAL) {
            this.tutorial.setStep(TutorialSteps.NONE);
            return;
        }
        if (this.timeWaiting >= 600 && this.toast == null) {
            this.toast = new TutorialToast(TutorialToast.Icons.RECIPE_BOOK, OpenInventoryTutorialStep.TITLE, OpenInventoryTutorialStep.DESCRIPTION, false);
            this.tutorial.getMinecraft().getToasts().addToast(this.toast);
        }
    }
    
    public void clear() {
        if (this.toast != null) {
            this.toast.hide();
            this.toast = null;
        }
    }
    
    public void onOpenInventory() {
        this.tutorial.setStep(TutorialSteps.CRAFT_PLANKS);
    }
    
    static {
        TITLE = new TranslatableComponent("tutorial.open_inventory.title");
        DESCRIPTION = new TranslatableComponent("tutorial.open_inventory.description", new Object[] { Tutorial.key("inventory") });
    }
}

package net.minecraft.client.tutorial;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.player.Input;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.world.level.GameType;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import net.minecraft.network.chat.Component;

public class MovementTutorialStepInstance implements TutorialStepInstance {
    private static final Component MOVE_TITLE;
    private static final Component MOVE_DESCRIPTION;
    private static final Component LOOK_TITLE;
    private static final Component LOOK_DESCRIPTION;
    private final Tutorial tutorial;
    private TutorialToast moveToast;
    private TutorialToast lookToast;
    private int timeWaiting;
    private int timeMoved;
    private int timeLooked;
    private boolean moved;
    private boolean turned;
    private int moveCompleted;
    private int lookCompleted;
    
    public MovementTutorialStepInstance(final Tutorial enw) {
        this.moveCompleted = -1;
        this.lookCompleted = -1;
        this.tutorial = enw;
    }
    
    public void tick() {
        ++this.timeWaiting;
        if (this.moved) {
            ++this.timeMoved;
            this.moved = false;
        }
        if (this.turned) {
            ++this.timeLooked;
            this.turned = false;
        }
        if (this.moveCompleted == -1 && this.timeMoved > 40) {
            if (this.moveToast != null) {
                this.moveToast.hide();
                this.moveToast = null;
            }
            this.moveCompleted = this.timeWaiting;
        }
        if (this.lookCompleted == -1 && this.timeLooked > 40) {
            if (this.lookToast != null) {
                this.lookToast.hide();
                this.lookToast = null;
            }
            this.lookCompleted = this.timeWaiting;
        }
        if (this.moveCompleted != -1 && this.lookCompleted != -1) {
            if (this.tutorial.getGameMode() == GameType.SURVIVAL) {
                this.tutorial.setStep(TutorialSteps.FIND_TREE);
            }
            else {
                this.tutorial.setStep(TutorialSteps.NONE);
            }
        }
        if (this.moveToast != null) {
            this.moveToast.updateProgress(this.timeMoved / 40.0f);
        }
        if (this.lookToast != null) {
            this.lookToast.updateProgress(this.timeLooked / 40.0f);
        }
        if (this.timeWaiting >= 100) {
            if (this.moveCompleted == -1 && this.moveToast == null) {
                this.moveToast = new TutorialToast(TutorialToast.Icons.MOVEMENT_KEYS, MovementTutorialStepInstance.MOVE_TITLE, MovementTutorialStepInstance.MOVE_DESCRIPTION, true);
                this.tutorial.getMinecraft().getToasts().addToast(this.moveToast);
            }
            else if (this.moveCompleted != -1 && this.timeWaiting - this.moveCompleted >= 20 && this.lookCompleted == -1 && this.lookToast == null) {
                this.lookToast = new TutorialToast(TutorialToast.Icons.MOUSE, MovementTutorialStepInstance.LOOK_TITLE, MovementTutorialStepInstance.LOOK_DESCRIPTION, true);
                this.tutorial.getMinecraft().getToasts().addToast(this.lookToast);
            }
        }
    }
    
    public void clear() {
        if (this.moveToast != null) {
            this.moveToast.hide();
            this.moveToast = null;
        }
        if (this.lookToast != null) {
            this.lookToast.hide();
            this.lookToast = null;
        }
    }
    
    public void onInput(final Input dzc) {
        if (dzc.up || dzc.down || dzc.left || dzc.right || dzc.jumping) {
            this.moved = true;
        }
    }
    
    public void onMouse(final double double1, final double double2) {
        if (Math.abs(double1) > 0.01 || Math.abs(double2) > 0.01) {
            this.turned = true;
        }
    }
    
    static {
        MOVE_TITLE = new TranslatableComponent("tutorial.move.title", new Object[] { Tutorial.key("forward"), Tutorial.key("left"), Tutorial.key("back"), Tutorial.key("right") });
        MOVE_DESCRIPTION = new TranslatableComponent("tutorial.move.description", new Object[] { Tutorial.key("jump") });
        LOOK_TITLE = new TranslatableComponent("tutorial.look.title");
        LOOK_DESCRIPTION = new TranslatableComponent("tutorial.look.description");
    }
}

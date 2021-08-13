package net.minecraft.client.tutorial;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.KeybindComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.GameType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.HitResult;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.Input;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;

public class Tutorial {
    private final Minecraft minecraft;
    @Nullable
    private TutorialStepInstance instance;
    
    public Tutorial(final Minecraft djw) {
        this.minecraft = djw;
    }
    
    public void onInput(final Input dzc) {
        if (this.instance != null) {
            this.instance.onInput(dzc);
        }
    }
    
    public void onMouse(final double double1, final double double2) {
        if (this.instance != null) {
            this.instance.onMouse(double1, double2);
        }
    }
    
    public void onLookAt(@Nullable final ClientLevel dwl, @Nullable final HitResult dci) {
        if (this.instance != null && dci != null && dwl != null) {
            this.instance.onLookAt(dwl, dci);
        }
    }
    
    public void onDestroyBlock(final ClientLevel dwl, final BlockPos fx, final BlockState cee, final float float4) {
        if (this.instance != null) {
            this.instance.onDestroyBlock(dwl, fx, cee, float4);
        }
    }
    
    public void onOpenInventory() {
        if (this.instance != null) {
            this.instance.onOpenInventory();
        }
    }
    
    public void onGetItem(final ItemStack bly) {
        if (this.instance != null) {
            this.instance.onGetItem(bly);
        }
    }
    
    public void stop() {
        if (this.instance == null) {
            return;
        }
        this.instance.clear();
        this.instance = null;
    }
    
    public void start() {
        if (this.instance != null) {
            this.stop();
        }
        this.instance = this.minecraft.options.tutorialStep.create(this);
    }
    
    public void tick() {
        if (this.instance != null) {
            if (this.minecraft.level != null) {
                this.instance.tick();
            }
            else {
                this.stop();
            }
        }
        else if (this.minecraft.level != null) {
            this.start();
        }
    }
    
    public void setStep(final TutorialSteps eny) {
        this.minecraft.options.tutorialStep = eny;
        this.minecraft.options.save();
        if (this.instance != null) {
            this.instance.clear();
            this.instance = eny.create(this);
        }
    }
    
    public Minecraft getMinecraft() {
        return this.minecraft;
    }
    
    public GameType getGameMode() {
        if (this.minecraft.gameMode == null) {
            return GameType.NOT_SET;
        }
        return this.minecraft.gameMode.getPlayerMode();
    }
    
    public static Component key(final String string) {
        return new KeybindComponent("key." + string).withStyle(ChatFormatting.BOLD);
    }
}

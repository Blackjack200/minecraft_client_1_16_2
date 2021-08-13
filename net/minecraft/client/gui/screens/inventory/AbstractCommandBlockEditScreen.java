package net.minecraft.client.gui.screens.inventory;

import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import java.util.function.Consumer;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.Screen;

public abstract class AbstractCommandBlockEditScreen extends Screen {
    private static final Component SET_COMMAND_LABEL;
    private static final Component COMMAND_LABEL;
    private static final Component PREVIOUS_OUTPUT_LABEL;
    protected EditBox commandEdit;
    protected EditBox previousEdit;
    protected Button doneButton;
    protected Button cancelButton;
    protected Button outputButton;
    protected boolean trackOutput;
    private CommandSuggestions commandSuggestions;
    
    public AbstractCommandBlockEditScreen() {
        super(NarratorChatListener.NO_TITLE);
    }
    
    @Override
    public void tick() {
        this.commandEdit.tick();
    }
    
    abstract BaseCommandBlock getCommandBlock();
    
    abstract int getPreviousY();
    
    @Override
    protected void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.doneButton = this.<Button>addButton(new Button(this.width / 2 - 4 - 150, this.height / 4 + 120 + 12, 150, 20, CommonComponents.GUI_DONE, dlg -> this.onDone()));
        this.cancelButton = this.<Button>addButton(new Button(this.width / 2 + 4, this.height / 4 + 120 + 12, 150, 20, CommonComponents.GUI_CANCEL, dlg -> this.onClose()));
        final BaseCommandBlock bqv3;
        this.outputButton = this.<Button>addButton(new Button(this.width / 2 + 150 - 20, this.getPreviousY(), 20, 20, new TextComponent("O"), dlg -> {
            bqv3 = this.getCommandBlock();
            bqv3.setTrackOutput(!bqv3.isTrackOutput());
            this.updateCommandOutput();
            return;
        }));
        (this.commandEdit = new EditBox(this.font, this.width / 2 - 150, 50, 300, 20, new TranslatableComponent("advMode.command")) {
            @Override
            protected MutableComponent createNarrationMessage() {
                return super.createNarrationMessage().append(AbstractCommandBlockEditScreen.this.commandSuggestions.getNarrationMessage());
            }
        }).setMaxLength(32500);
        this.commandEdit.setResponder((Consumer<String>)this::onEdited);
        this.children.add(this.commandEdit);
        (this.previousEdit = new EditBox(this.font, this.width / 2 - 150, this.getPreviousY(), 276, 20, new TranslatableComponent("advMode.previousOutput"))).setMaxLength(32500);
        this.previousEdit.setEditable(false);
        this.previousEdit.setValue("-");
        this.children.add(this.previousEdit);
        this.setInitialFocus(this.commandEdit);
        this.commandEdit.setFocus(true);
        (this.commandSuggestions = new CommandSuggestions(this.minecraft, this, this.commandEdit, this.font, true, true, 0, 7, false, Integer.MIN_VALUE)).setAllowSuggestions(true);
        this.commandSuggestions.updateCommandInfo();
    }
    
    @Override
    public void resize(final Minecraft djw, final int integer2, final int integer3) {
        final String string5 = this.commandEdit.getValue();
        this.init(djw, integer2, integer3);
        this.commandEdit.setValue(string5);
        this.commandSuggestions.updateCommandInfo();
    }
    
    protected void updateCommandOutput() {
        if (this.getCommandBlock().isTrackOutput()) {
            this.outputButton.setMessage(new TextComponent("O"));
            this.previousEdit.setValue(this.getCommandBlock().getLastOutput().getString());
        }
        else {
            this.outputButton.setMessage(new TextComponent("X"));
            this.previousEdit.setValue("-");
        }
    }
    
    protected void onDone() {
        final BaseCommandBlock bqv2 = this.getCommandBlock();
        this.populateAndSendPacket(bqv2);
        if (!bqv2.isTrackOutput()) {
            bqv2.setLastOutput(null);
        }
        this.minecraft.setScreen(null);
    }
    
    @Override
    public void removed() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }
    
    protected abstract void populateAndSendPacket(final BaseCommandBlock bqv);
    
    @Override
    public void onClose() {
        this.getCommandBlock().setTrackOutput(this.trackOutput);
        this.minecraft.setScreen(null);
    }
    
    private void onEdited(final String string) {
        this.commandSuggestions.updateCommandInfo();
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (this.commandSuggestions.keyPressed(integer1, integer2, integer3)) {
            return true;
        }
        if (super.keyPressed(integer1, integer2, integer3)) {
            return true;
        }
        if (integer1 == 257 || integer1 == 335) {
            this.onDone();
            return true;
        }
        return false;
    }
    
    @Override
    public boolean mouseScrolled(final double double1, final double double2, final double double3) {
        return this.commandSuggestions.mouseScrolled(double3) || super.mouseScrolled(double1, double2, double3);
    }
    
    @Override
    public boolean mouseClicked(final double double1, final double double2, final int integer) {
        return this.commandSuggestions.mouseClicked(double1, double2, integer) || super.mouseClicked(double1, double2, integer);
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        GuiComponent.drawCenteredString(dfj, this.font, AbstractCommandBlockEditScreen.SET_COMMAND_LABEL, this.width / 2, 20, 16777215);
        GuiComponent.drawString(dfj, this.font, AbstractCommandBlockEditScreen.COMMAND_LABEL, this.width / 2 - 150, 40, 10526880);
        this.commandEdit.render(dfj, integer2, integer3, float4);
        int integer4 = 75;
        if (!this.previousEdit.getValue().isEmpty()) {
            final int n = integer4;
            final int n2 = 5;
            this.font.getClass();
            integer4 = n + (n2 * 9 + 1 + this.getPreviousY() - 135);
            GuiComponent.drawString(dfj, this.font, AbstractCommandBlockEditScreen.PREVIOUS_OUTPUT_LABEL, this.width / 2 - 150, integer4 + 4, 10526880);
            this.previousEdit.render(dfj, integer2, integer3, float4);
        }
        super.render(dfj, integer2, integer3, float4);
        this.commandSuggestions.render(dfj, integer2, integer3);
    }
    
    static {
        SET_COMMAND_LABEL = new TranslatableComponent("advMode.setCommand");
        COMMAND_LABEL = new TranslatableComponent("advMode.command");
        PREVIOUS_OUTPUT_LABEL = new TranslatableComponent("advMode.previousOutput");
    }
}

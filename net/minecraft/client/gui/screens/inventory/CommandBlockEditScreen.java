package net.minecraft.client.gui.screens.inventory;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundSetCommandBlockPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.client.gui.components.Button;
import net.minecraft.world.level.block.entity.CommandBlockEntity;

public class CommandBlockEditScreen extends AbstractCommandBlockEditScreen {
    private final CommandBlockEntity autoCommandBlock;
    private Button modeButton;
    private Button conditionalButton;
    private Button autoexecButton;
    private CommandBlockEntity.Mode mode;
    private boolean conditional;
    private boolean autoexec;
    
    public CommandBlockEditScreen(final CommandBlockEntity ccl) {
        this.mode = CommandBlockEntity.Mode.REDSTONE;
        this.autoCommandBlock = ccl;
    }
    
    @Override
    BaseCommandBlock getCommandBlock() {
        return this.autoCommandBlock.getCommandBlock();
    }
    
    @Override
    int getPreviousY() {
        return 135;
    }
    
    @Override
    protected void init() {
        super.init();
        this.modeButton = this.<Button>addButton(new Button(this.width / 2 - 50 - 100 - 4, 165, 100, 20, new TranslatableComponent("advMode.mode.sequence"), dlg -> {
            this.nextMode();
            this.updateMode();
            return;
        }));
        this.conditionalButton = this.<Button>addButton(new Button(this.width / 2 - 50, 165, 100, 20, new TranslatableComponent("advMode.mode.unconditional"), dlg -> {
            this.conditional = !this.conditional;
            this.updateConditional();
            return;
        }));
        this.autoexecButton = this.<Button>addButton(new Button(this.width / 2 + 50 + 4, 165, 100, 20, new TranslatableComponent("advMode.mode.redstoneTriggered"), dlg -> {
            this.autoexec = !this.autoexec;
            this.updateAutoexec();
            return;
        }));
        this.doneButton.active = false;
        this.outputButton.active = false;
        this.modeButton.active = false;
        this.conditionalButton.active = false;
        this.autoexecButton.active = false;
    }
    
    public void updateGui() {
        final BaseCommandBlock bqv2 = this.autoCommandBlock.getCommandBlock();
        this.commandEdit.setValue(bqv2.getCommand());
        this.trackOutput = bqv2.isTrackOutput();
        this.mode = this.autoCommandBlock.getMode();
        this.conditional = this.autoCommandBlock.isConditional();
        this.autoexec = this.autoCommandBlock.isAutomatic();
        this.updateCommandOutput();
        this.updateMode();
        this.updateConditional();
        this.updateAutoexec();
        this.doneButton.active = true;
        this.outputButton.active = true;
        this.modeButton.active = true;
        this.conditionalButton.active = true;
        this.autoexecButton.active = true;
    }
    
    @Override
    public void resize(final Minecraft djw, final int integer2, final int integer3) {
        super.resize(djw, integer2, integer3);
        this.updateCommandOutput();
        this.updateMode();
        this.updateConditional();
        this.updateAutoexec();
        this.doneButton.active = true;
        this.outputButton.active = true;
        this.modeButton.active = true;
        this.conditionalButton.active = true;
        this.autoexecButton.active = true;
    }
    
    @Override
    protected void populateAndSendPacket(final BaseCommandBlock bqv) {
        this.minecraft.getConnection().send(new ServerboundSetCommandBlockPacket(new BlockPos(bqv.getPosition()), this.commandEdit.getValue(), this.mode, bqv.isTrackOutput(), this.conditional, this.autoexec));
    }
    
    private void updateMode() {
        switch (this.mode) {
            case SEQUENCE: {
                this.modeButton.setMessage(new TranslatableComponent("advMode.mode.sequence"));
                break;
            }
            case AUTO: {
                this.modeButton.setMessage(new TranslatableComponent("advMode.mode.auto"));
                break;
            }
            case REDSTONE: {
                this.modeButton.setMessage(new TranslatableComponent("advMode.mode.redstone"));
                break;
            }
        }
    }
    
    private void nextMode() {
        switch (this.mode) {
            case SEQUENCE: {
                this.mode = CommandBlockEntity.Mode.AUTO;
                break;
            }
            case AUTO: {
                this.mode = CommandBlockEntity.Mode.REDSTONE;
                break;
            }
            case REDSTONE: {
                this.mode = CommandBlockEntity.Mode.SEQUENCE;
                break;
            }
        }
    }
    
    private void updateConditional() {
        if (this.conditional) {
            this.conditionalButton.setMessage(new TranslatableComponent("advMode.mode.conditional"));
        }
        else {
            this.conditionalButton.setMessage(new TranslatableComponent("advMode.mode.unconditional"));
        }
    }
    
    private void updateAutoexec() {
        if (this.autoexec) {
            this.autoexecButton.setMessage(new TranslatableComponent("advMode.mode.autoexec.bat"));
        }
        else {
            this.autoexecButton.setMessage(new TranslatableComponent("advMode.mode.redstoneTriggered"));
        }
    }
}

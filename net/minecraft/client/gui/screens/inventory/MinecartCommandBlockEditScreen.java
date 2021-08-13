package net.minecraft.client.gui.screens.inventory;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundSetCommandMinecartPacket;
import net.minecraft.world.entity.vehicle.MinecartCommandBlock;
import net.minecraft.world.level.BaseCommandBlock;

public class MinecartCommandBlockEditScreen extends AbstractCommandBlockEditScreen {
    private final BaseCommandBlock commandBlock;
    
    public MinecartCommandBlockEditScreen(final BaseCommandBlock bqv) {
        this.commandBlock = bqv;
    }
    
    public BaseCommandBlock getCommandBlock() {
        return this.commandBlock;
    }
    
    @Override
    int getPreviousY() {
        return 150;
    }
    
    @Override
    protected void init() {
        super.init();
        this.trackOutput = this.getCommandBlock().isTrackOutput();
        this.updateCommandOutput();
        this.commandEdit.setValue(this.getCommandBlock().getCommand());
    }
    
    @Override
    protected void populateAndSendPacket(final BaseCommandBlock bqv) {
        if (bqv instanceof MinecartCommandBlock.MinecartCommandBase) {
            final MinecartCommandBlock.MinecartCommandBase a3 = (MinecartCommandBlock.MinecartCommandBase)bqv;
            this.minecraft.getConnection().send(new ServerboundSetCommandMinecartPacket(a3.getMinecart().getId(), this.commandEdit.getValue(), bqv.isTrackOutput()));
        }
    }
}

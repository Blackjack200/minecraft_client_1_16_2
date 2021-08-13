package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;

public class ServerboundSetCommandBlockPacket implements Packet<ServerGamePacketListener> {
    private BlockPos pos;
    private String command;
    private boolean trackOutput;
    private boolean conditional;
    private boolean automatic;
    private CommandBlockEntity.Mode mode;
    
    public ServerboundSetCommandBlockPacket() {
    }
    
    public ServerboundSetCommandBlockPacket(final BlockPos fx, final String string, final CommandBlockEntity.Mode a, final boolean boolean4, final boolean boolean5, final boolean boolean6) {
        this.pos = fx;
        this.command = string;
        this.trackOutput = boolean4;
        this.conditional = boolean5;
        this.automatic = boolean6;
        this.mode = a;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.pos = nf.readBlockPos();
        this.command = nf.readUtf(32767);
        this.mode = nf.<CommandBlockEntity.Mode>readEnum(CommandBlockEntity.Mode.class);
        final int integer3 = nf.readByte();
        this.trackOutput = ((integer3 & 0x1) != 0x0);
        this.conditional = ((integer3 & 0x2) != 0x0);
        this.automatic = ((integer3 & 0x4) != 0x0);
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeBlockPos(this.pos);
        nf.writeUtf(this.command);
        nf.writeEnum(this.mode);
        int integer3 = 0;
        if (this.trackOutput) {
            integer3 |= 0x1;
        }
        if (this.conditional) {
            integer3 |= 0x2;
        }
        if (this.automatic) {
            integer3 |= 0x4;
        }
        nf.writeByte(integer3);
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleSetCommandBlock(this);
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public String getCommand() {
        return this.command;
    }
    
    public boolean isTrackOutput() {
        return this.trackOutput;
    }
    
    public boolean isConditional() {
        return this.conditional;
    }
    
    public boolean isAutomatic() {
        return this.automatic;
    }
    
    public CommandBlockEntity.Mode getMode() {
        return this.mode;
    }
}

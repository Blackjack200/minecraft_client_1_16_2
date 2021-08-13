package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;

public class ServerboundPlayerActionPacket implements Packet<ServerGamePacketListener> {
    private BlockPos pos;
    private Direction direction;
    private Action action;
    
    public ServerboundPlayerActionPacket() {
    }
    
    public ServerboundPlayerActionPacket(final Action a, final BlockPos fx, final Direction gc) {
        this.action = a;
        this.pos = fx.immutable();
        this.direction = gc;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.action = nf.<Action>readEnum(Action.class);
        this.pos = nf.readBlockPos();
        this.direction = Direction.from3DDataValue(nf.readUnsignedByte());
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeEnum(this.action);
        nf.writeBlockPos(this.pos);
        nf.writeByte(this.direction.get3DDataValue());
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handlePlayerAction(this);
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public Direction getDirection() {
        return this.direction;
    }
    
    public Action getAction() {
        return this.action;
    }
    
    public enum Action {
        START_DESTROY_BLOCK, 
        ABORT_DESTROY_BLOCK, 
        STOP_DESTROY_BLOCK, 
        DROP_ALL_ITEMS, 
        DROP_ITEM, 
        RELEASE_USE_ITEM, 
        SWAP_ITEM_WITH_OFFHAND;
    }
}

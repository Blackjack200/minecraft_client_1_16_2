package net.minecraft.network.protocol.game;

import org.apache.logging.log4j.LogManager;
import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.world.level.block.Block;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import org.apache.logging.log4j.Logger;
import net.minecraft.network.protocol.Packet;

public class ClientboundBlockBreakAckPacket implements Packet<ClientGamePacketListener> {
    private static final Logger LOGGER;
    private BlockPos pos;
    private BlockState state;
    ServerboundPlayerActionPacket.Action action;
    private boolean allGood;
    
    public ClientboundBlockBreakAckPacket() {
    }
    
    public ClientboundBlockBreakAckPacket(final BlockPos fx, final BlockState cee, final ServerboundPlayerActionPacket.Action a, final boolean boolean4, final String string) {
        this.pos = fx.immutable();
        this.state = cee;
        this.action = a;
        this.allGood = boolean4;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.pos = nf.readBlockPos();
        this.state = Block.BLOCK_STATE_REGISTRY.byId(nf.readVarInt());
        this.action = nf.<ServerboundPlayerActionPacket.Action>readEnum(ServerboundPlayerActionPacket.Action.class);
        this.allGood = nf.readBoolean();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeBlockPos(this.pos);
        nf.writeVarInt(Block.getId(this.state));
        nf.writeEnum(this.action);
        nf.writeBoolean(this.allGood);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleBlockBreakAck(this);
    }
    
    public BlockState getState() {
        return this.state;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public boolean allGood() {
        return this.allGood;
    }
    
    public ServerboundPlayerActionPacket.Action action() {
        return this.action;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}

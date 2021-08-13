package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Difficulty;
import net.minecraft.network.protocol.Packet;

public class ClientboundChangeDifficultyPacket implements Packet<ClientGamePacketListener> {
    private Difficulty difficulty;
    private boolean locked;
    
    public ClientboundChangeDifficultyPacket() {
    }
    
    public ClientboundChangeDifficultyPacket(final Difficulty aoo, final boolean boolean2) {
        this.difficulty = aoo;
        this.locked = boolean2;
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleChangeDifficulty(this);
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.difficulty = Difficulty.byId(nf.readUnsignedByte());
        this.locked = nf.readBoolean();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeByte(this.difficulty.getId());
        nf.writeBoolean(this.locked);
    }
    
    public boolean isLocked() {
        return this.locked;
    }
    
    public Difficulty getDifficulty() {
        return this.difficulty;
    }
}

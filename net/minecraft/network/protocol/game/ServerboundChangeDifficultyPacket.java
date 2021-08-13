package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Difficulty;
import net.minecraft.network.protocol.Packet;

public class ServerboundChangeDifficultyPacket implements Packet<ServerGamePacketListener> {
    private Difficulty difficulty;
    
    public ServerboundChangeDifficultyPacket() {
    }
    
    public ServerboundChangeDifficultyPacket(final Difficulty aoo) {
        this.difficulty = aoo;
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleChangeDifficulty(this);
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.difficulty = Difficulty.byId(nf.readUnsignedByte());
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeByte(this.difficulty.getId());
    }
    
    public Difficulty getDifficulty() {
        return this.difficulty;
    }
}

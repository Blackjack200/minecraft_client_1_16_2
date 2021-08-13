package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import java.util.UUID;
import net.minecraft.network.protocol.Packet;

public class ServerboundTeleportToEntityPacket implements Packet<ServerGamePacketListener> {
    private UUID uuid;
    
    public ServerboundTeleportToEntityPacket() {
    }
    
    public ServerboundTeleportToEntityPacket(final UUID uUID) {
        this.uuid = uUID;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.uuid = nf.readUUID();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeUUID(this.uuid);
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleTeleportToEntityPacket(this);
    }
    
    @Nullable
    public Entity getEntity(final ServerLevel aag) {
        return aag.getEntity(this.uuid);
    }
}

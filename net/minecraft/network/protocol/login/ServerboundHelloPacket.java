package net.minecraft.network.protocol.login;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.Packet;

public class ServerboundHelloPacket implements Packet<ServerLoginPacketListener> {
    private GameProfile gameProfile;
    
    public ServerboundHelloPacket() {
    }
    
    public ServerboundHelloPacket(final GameProfile gameProfile) {
        this.gameProfile = gameProfile;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.gameProfile = new GameProfile((UUID)null, nf.readUtf(16));
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeUtf(this.gameProfile.getName());
    }
    
    public void handle(final ServerLoginPacketListener ue) {
        ue.handleHello(this);
    }
    
    public GameProfile getGameProfile() {
        return this.gameProfile;
    }
}

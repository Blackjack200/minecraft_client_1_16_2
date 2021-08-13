package net.minecraft.network.protocol.login;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import java.util.UUID;
import net.minecraft.core.SerializableUUID;
import net.minecraft.network.FriendlyByteBuf;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.Packet;

public class ClientboundGameProfilePacket implements Packet<ClientLoginPacketListener> {
    private GameProfile gameProfile;
    
    public ClientboundGameProfilePacket() {
    }
    
    public ClientboundGameProfilePacket(final GameProfile gameProfile) {
        this.gameProfile = gameProfile;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        final int[] arr3 = new int[4];
        for (int integer4 = 0; integer4 < arr3.length; ++integer4) {
            arr3[integer4] = nf.readInt();
        }
        final UUID uUID4 = SerializableUUID.uuidFromIntArray(arr3);
        final String string5 = nf.readUtf(16);
        this.gameProfile = new GameProfile(uUID4, string5);
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        for (final int integer6 : SerializableUUID.uuidToIntArray(this.gameProfile.getId())) {
            nf.writeInt(integer6);
        }
        nf.writeUtf(this.gameProfile.getName());
    }
    
    public void handle(final ClientLoginPacketListener ty) {
        ty.handleGameProfile(this);
    }
    
    public GameProfile getGameProfile() {
        return this.gameProfile;
    }
}

package net.minecraft.network.protocol.status;

import com.google.gson.TypeAdapterFactory;
import net.minecraft.util.LowerCaseEnumTypeAdapterFactory;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;
import java.lang.reflect.Type;
import com.google.gson.GsonBuilder;
import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.util.GsonHelper;
import net.minecraft.network.FriendlyByteBuf;
import com.google.gson.Gson;
import net.minecraft.network.protocol.Packet;

public class ClientboundStatusResponsePacket implements Packet<ClientStatusPacketListener> {
    private static final Gson GSON;
    private ServerStatus status;
    
    public ClientboundStatusResponsePacket() {
    }
    
    public ClientboundStatusResponsePacket(final ServerStatus un) {
        this.status = un;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.status = GsonHelper.<ServerStatus>fromJson(ClientboundStatusResponsePacket.GSON, nf.readUtf(32767), ServerStatus.class);
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeUtf(ClientboundStatusResponsePacket.GSON.toJson(this.status));
    }
    
    public void handle(final ClientStatusPacketListener uk) {
        uk.handleStatusResponse(this);
    }
    
    public ServerStatus getStatus() {
        return this.status;
    }
    
    static {
        GSON = new GsonBuilder().registerTypeAdapter((Type)ServerStatus.Version.class, new ServerStatus.Version.Serializer()).registerTypeAdapter((Type)ServerStatus.Players.class, new ServerStatus.Players.Serializer()).registerTypeAdapter((Type)ServerStatus.class, new ServerStatus.Serializer()).registerTypeHierarchyAdapter((Class)Component.class, new Component.Serializer()).registerTypeHierarchyAdapter((Class)Style.class, new Style.Serializer()).registerTypeAdapterFactory((TypeAdapterFactory)new LowerCaseEnumTypeAdapterFactory()).create();
    }
}

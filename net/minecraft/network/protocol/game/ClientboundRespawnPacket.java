package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.core.Registry;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.network.protocol.Packet;

public class ClientboundRespawnPacket implements Packet<ClientGamePacketListener> {
    private DimensionType dimensionType;
    private ResourceKey<Level> dimension;
    private long seed;
    private GameType playerGameType;
    private GameType previousPlayerGameType;
    private boolean isDebug;
    private boolean isFlat;
    private boolean keepAllPlayerData;
    
    public ClientboundRespawnPacket() {
    }
    
    public ClientboundRespawnPacket(final DimensionType cha, final ResourceKey<Level> vj, final long long3, final GameType brr4, final GameType brr5, final boolean boolean6, final boolean boolean7, final boolean boolean8) {
        this.dimensionType = cha;
        this.dimension = vj;
        this.seed = long3;
        this.playerGameType = brr4;
        this.previousPlayerGameType = brr5;
        this.isDebug = boolean6;
        this.isFlat = boolean7;
        this.keepAllPlayerData = boolean8;
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleRespawn(this);
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.dimensionType = (DimensionType)nf.<Supplier<DimensionType>>readWithCodec(DimensionType.CODEC).get();
        this.dimension = ResourceKey.<Level>create(Registry.DIMENSION_REGISTRY, nf.readResourceLocation());
        this.seed = nf.readLong();
        this.playerGameType = GameType.byId(nf.readUnsignedByte());
        this.previousPlayerGameType = GameType.byId(nf.readUnsignedByte());
        this.isDebug = nf.readBoolean();
        this.isFlat = nf.readBoolean();
        this.keepAllPlayerData = nf.readBoolean();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.<Supplier<DimensionType>>writeWithCodec(DimensionType.CODEC, (Supplier<DimensionType>)(() -> this.dimensionType));
        nf.writeResourceLocation(this.dimension.location());
        nf.writeLong(this.seed);
        nf.writeByte(this.playerGameType.getId());
        nf.writeByte(this.previousPlayerGameType.getId());
        nf.writeBoolean(this.isDebug);
        nf.writeBoolean(this.isFlat);
        nf.writeBoolean(this.keepAllPlayerData);
    }
    
    public DimensionType getDimensionType() {
        return this.dimensionType;
    }
    
    public ResourceKey<Level> getDimension() {
        return this.dimension;
    }
    
    public long getSeed() {
        return this.seed;
    }
    
    public GameType getPlayerGameType() {
        return this.playerGameType;
    }
    
    public GameType getPreviousPlayerGameType() {
        return this.previousPlayerGameType;
    }
    
    public boolean isDebug() {
        return this.isDebug;
    }
    
    public boolean isFlat() {
        return this.isFlat;
    }
    
    public boolean shouldKeepAllPlayerData() {
        return this.keepAllPlayerData;
    }
}

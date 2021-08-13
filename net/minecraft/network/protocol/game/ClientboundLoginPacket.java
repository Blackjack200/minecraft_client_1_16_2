package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.util.Iterator;
import java.io.IOException;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import com.google.common.collect.Sets;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.Set;
import net.minecraft.world.level.GameType;
import net.minecraft.network.protocol.Packet;

public class ClientboundLoginPacket implements Packet<ClientGamePacketListener> {
    private int playerId;
    private long seed;
    private boolean hardcore;
    private GameType gameType;
    private GameType previousGameType;
    private Set<ResourceKey<Level>> levels;
    private RegistryAccess.RegistryHolder registryHolder;
    private DimensionType dimensionType;
    private ResourceKey<Level> dimension;
    private int maxPlayers;
    private int chunkRadius;
    private boolean reducedDebugInfo;
    private boolean showDeathScreen;
    private boolean isDebug;
    private boolean isFlat;
    
    public ClientboundLoginPacket() {
    }
    
    public ClientboundLoginPacket(final int integer1, final GameType brr2, final GameType brr3, final long long4, final boolean boolean5, final Set<ResourceKey<Level>> set, final RegistryAccess.RegistryHolder b, final DimensionType cha, final ResourceKey<Level> vj, final int integer10, final int integer11, final boolean boolean12, final boolean boolean13, final boolean boolean14, final boolean boolean15) {
        this.playerId = integer1;
        this.levels = set;
        this.registryHolder = b;
        this.dimensionType = cha;
        this.dimension = vj;
        this.seed = long4;
        this.gameType = brr2;
        this.previousGameType = brr3;
        this.maxPlayers = integer10;
        this.hardcore = boolean5;
        this.chunkRadius = integer11;
        this.reducedDebugInfo = boolean12;
        this.showDeathScreen = boolean13;
        this.isDebug = boolean14;
        this.isFlat = boolean15;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.playerId = nf.readInt();
        this.hardcore = nf.readBoolean();
        this.gameType = GameType.byId(nf.readByte());
        this.previousGameType = GameType.byId(nf.readByte());
        final int integer3 = nf.readVarInt();
        this.levels = (Set<ResourceKey<Level>>)Sets.newHashSet();
        for (int integer4 = 0; integer4 < integer3; ++integer4) {
            this.levels.add(ResourceKey.create(Registry.DIMENSION_REGISTRY, nf.readResourceLocation()));
        }
        this.registryHolder = nf.<RegistryAccess.RegistryHolder>readWithCodec(RegistryAccess.RegistryHolder.NETWORK_CODEC);
        this.dimensionType = (DimensionType)nf.<Supplier<DimensionType>>readWithCodec(DimensionType.CODEC).get();
        this.dimension = ResourceKey.<Level>create(Registry.DIMENSION_REGISTRY, nf.readResourceLocation());
        this.seed = nf.readLong();
        this.maxPlayers = nf.readVarInt();
        this.chunkRadius = nf.readVarInt();
        this.reducedDebugInfo = nf.readBoolean();
        this.showDeathScreen = nf.readBoolean();
        this.isDebug = nf.readBoolean();
        this.isFlat = nf.readBoolean();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeInt(this.playerId);
        nf.writeBoolean(this.hardcore);
        nf.writeByte(this.gameType.getId());
        nf.writeByte(this.previousGameType.getId());
        nf.writeVarInt(this.levels.size());
        for (final ResourceKey<Level> vj4 : this.levels) {
            nf.writeResourceLocation(vj4.location());
        }
        nf.<RegistryAccess.RegistryHolder>writeWithCodec(RegistryAccess.RegistryHolder.NETWORK_CODEC, this.registryHolder);
        nf.<Supplier<DimensionType>>writeWithCodec(DimensionType.CODEC, (Supplier<DimensionType>)(() -> this.dimensionType));
        nf.writeResourceLocation(this.dimension.location());
        nf.writeLong(this.seed);
        nf.writeVarInt(this.maxPlayers);
        nf.writeVarInt(this.chunkRadius);
        nf.writeBoolean(this.reducedDebugInfo);
        nf.writeBoolean(this.showDeathScreen);
        nf.writeBoolean(this.isDebug);
        nf.writeBoolean(this.isFlat);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleLogin(this);
    }
    
    public int getPlayerId() {
        return this.playerId;
    }
    
    public long getSeed() {
        return this.seed;
    }
    
    public boolean isHardcore() {
        return this.hardcore;
    }
    
    public GameType getGameType() {
        return this.gameType;
    }
    
    public GameType getPreviousGameType() {
        return this.previousGameType;
    }
    
    public Set<ResourceKey<Level>> levels() {
        return this.levels;
    }
    
    public RegistryAccess registryAccess() {
        return this.registryHolder;
    }
    
    public DimensionType getDimensionType() {
        return this.dimensionType;
    }
    
    public ResourceKey<Level> getDimension() {
        return this.dimension;
    }
    
    public int getChunkRadius() {
        return this.chunkRadius;
    }
    
    public boolean isReducedDebugInfo() {
        return this.reducedDebugInfo;
    }
    
    public boolean shouldShowDeathScreen() {
        return this.showDeathScreen;
    }
    
    public boolean isDebug() {
        return this.isDebug;
    }
    
    public boolean isFlat() {
        return this.isFlat;
    }
}

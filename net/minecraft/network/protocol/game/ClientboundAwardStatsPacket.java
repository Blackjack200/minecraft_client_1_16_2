package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.IOException;
import net.minecraft.core.Registry;
import net.minecraft.stats.StatType;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.stats.Stat;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.network.protocol.Packet;

public class ClientboundAwardStatsPacket implements Packet<ClientGamePacketListener> {
    private Object2IntMap<Stat<?>> stats;
    
    public ClientboundAwardStatsPacket() {
    }
    
    public ClientboundAwardStatsPacket(final Object2IntMap<Stat<?>> object2IntMap) {
        this.stats = object2IntMap;
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleAwardStats(this);
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        final int integer3 = nf.readVarInt();
        this.stats = (Object2IntMap<Stat<?>>)new Object2IntOpenHashMap(integer3);
        for (int integer4 = 0; integer4 < integer3; ++integer4) {
            this.readStat(Registry.STAT_TYPE.byId(nf.readVarInt()), nf);
        }
    }
    
    private <T> void readStat(final StatType<T> adx, final FriendlyByteBuf nf) {
        final int integer4 = nf.readVarInt();
        final int integer5 = nf.readVarInt();
        this.stats.put(adx.get(adx.getRegistry().byId(integer4)), integer5);
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.stats.size());
        for (final Object2IntMap.Entry<Stat<?>> entry4 : this.stats.object2IntEntrySet()) {
            final Stat<?> adv5 = entry4.getKey();
            nf.writeVarInt(Registry.STAT_TYPE.getId(adv5.getType()));
            nf.writeVarInt(this.getId(adv5));
            nf.writeVarInt(entry4.getIntValue());
        }
    }
    
    private <T> int getId(final Stat<T> adv) {
        return adv.getType().getRegistry().getId(adv.getValue());
    }
    
    public Map<Stat<?>, Integer> getStats() {
        return (Map<Stat<?>, Integer>)this.stats;
    }
}

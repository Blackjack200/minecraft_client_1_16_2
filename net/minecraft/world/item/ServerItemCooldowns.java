package net.minecraft.world.item;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundCooldownPacket;
import net.minecraft.server.level.ServerPlayer;

public class ServerItemCooldowns extends ItemCooldowns {
    private final ServerPlayer player;
    
    public ServerItemCooldowns(final ServerPlayer aah) {
        this.player = aah;
    }
    
    @Override
    protected void onCooldownStarted(final Item blu, final int integer) {
        super.onCooldownStarted(blu, integer);
        this.player.connection.send(new ClientboundCooldownPacket(blu, integer));
    }
    
    @Override
    protected void onCooldownEnded(final Item blu) {
        super.onCooldownEnded(blu);
        this.player.connection.send(new ClientboundCooldownPacket(blu, 0));
    }
}

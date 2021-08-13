package net.minecraft.world.item;

import javax.annotation.Nullable;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ComplexItem extends Item {
    public ComplexItem(final Properties a) {
        super(a);
    }
    
    @Override
    public boolean isComplex() {
        return true;
    }
    
    @Nullable
    public Packet<?> getUpdatePacket(final ItemStack bly, final Level bru, final Player bft) {
        return null;
    }
}

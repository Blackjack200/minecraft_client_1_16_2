package net.minecraft.server.gui;

import net.minecraft.server.level.ServerPlayer;
import java.util.Vector;
import net.minecraft.server.MinecraftServer;
import javax.swing.JList;

public class PlayerListComponent extends JList<String> {
    private final MinecraftServer server;
    private int tickCount;
    
    public PlayerListComponent(final MinecraftServer minecraftServer) {
        (this.server = minecraftServer).addTickable(this::tick);
    }
    
    public void tick() {
        if (this.tickCount++ % 20 == 0) {
            final Vector<String> vector2 = (Vector<String>)new Vector();
            for (int integer3 = 0; integer3 < this.server.getPlayerList().getPlayers().size(); ++integer3) {
                vector2.add(((ServerPlayer)this.server.getPlayerList().getPlayers().get(integer3)).getGameProfile().getName());
            }
            this.setListData((Vector)vector2);
        }
    }
}

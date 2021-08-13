package net.minecraft.server.rcon;

import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec2;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.commands.CommandSource;

public class RconConsoleSource implements CommandSource {
    private static final TextComponent RCON_COMPONENT;
    private final StringBuffer buffer;
    private final MinecraftServer server;
    
    public RconConsoleSource(final MinecraftServer minecraftServer) {
        this.buffer = new StringBuffer();
        this.server = minecraftServer;
    }
    
    public void prepareForCommand() {
        this.buffer.setLength(0);
    }
    
    public String getCommandResponse() {
        return this.buffer.toString();
    }
    
    public CommandSourceStack createCommandSourceStack() {
        final ServerLevel aag2 = this.server.overworld();
        return new CommandSourceStack((CommandSource)this, Vec3.atLowerCornerOf(aag2.getSharedSpawnPos()), Vec2.ZERO, aag2, 4, "Rcon", (Component)RconConsoleSource.RCON_COMPONENT, this.server, (Entity)null);
    }
    
    public void sendMessage(final Component nr, final UUID uUID) {
        this.buffer.append(nr.getString());
    }
    
    public boolean acceptsSuccess() {
        return true;
    }
    
    public boolean acceptsFailure() {
        return true;
    }
    
    public boolean shouldInformAdmins() {
        return this.server.shouldRconBroadcast();
    }
    
    static {
        RCON_COMPONENT = new TextComponent("Rcon");
    }
}

package net.minecraft.world.level.timers;

import net.minecraft.nbt.CompoundTag;
import java.util.Iterator;
import net.minecraft.tags.Tag;
import net.minecraft.server.ServerFunctionManager;
import net.minecraft.commands.CommandFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

public class FunctionTagCallback implements TimerCallback<MinecraftServer> {
    private final ResourceLocation tagId;
    
    public FunctionTagCallback(final ResourceLocation vk) {
        this.tagId = vk;
    }
    
    public void handle(final MinecraftServer minecraftServer, final TimerQueue<MinecraftServer> dcc, final long long3) {
        final ServerFunctionManager vx6 = minecraftServer.getFunctions();
        final Tag<CommandFunction> aej7 = vx6.getTag(this.tagId);
        for (final CommandFunction cy9 : aej7.getValues()) {
            vx6.execute(cy9, vx6.getGameLoopSender());
        }
    }
    
    public static class Serializer extends TimerCallback.Serializer<MinecraftServer, FunctionTagCallback> {
        public Serializer() {
            super(new ResourceLocation("function_tag"), FunctionTagCallback.class);
        }
        
        @Override
        public void serialize(final CompoundTag md, final FunctionTagCallback dbz) {
            md.putString("Name", dbz.tagId.toString());
        }
        
        @Override
        public FunctionTagCallback deserialize(final CompoundTag md) {
            final ResourceLocation vk3 = new ResourceLocation(md.getString("Name"));
            return new FunctionTagCallback(vk3);
        }
    }
}

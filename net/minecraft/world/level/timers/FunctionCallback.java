package net.minecraft.world.level.timers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.commands.CommandFunction;
import net.minecraft.server.ServerFunctionManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

public class FunctionCallback implements TimerCallback<MinecraftServer> {
    private final ResourceLocation functionId;
    
    public FunctionCallback(final ResourceLocation vk) {
        this.functionId = vk;
    }
    
    public void handle(final MinecraftServer minecraftServer, final TimerQueue<MinecraftServer> dcc, final long long3) {
        final ServerFunctionManager vx6 = minecraftServer.getFunctions();
        vx6.get(this.functionId).ifPresent(cy -> vx6.execute(cy, vx6.getGameLoopSender()));
    }
    
    public static class Serializer extends TimerCallback.Serializer<MinecraftServer, FunctionCallback> {
        public Serializer() {
            super(new ResourceLocation("function"), FunctionCallback.class);
        }
        
        @Override
        public void serialize(final CompoundTag md, final FunctionCallback dby) {
            md.putString("Name", dby.functionId.toString());
        }
        
        @Override
        public FunctionCallback deserialize(final CompoundTag md) {
            final ResourceLocation vk3 = new ResourceLocation(md.getString("Name"));
            return new FunctionCallback(vk3);
        }
    }
}

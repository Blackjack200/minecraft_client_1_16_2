package net.minecraft.world.level.timers;

import org.apache.logging.log4j.LogManager;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;

public class TimerCallbacks<C> {
    private static final Logger LOGGER;
    public static final TimerCallbacks<MinecraftServer> SERVER_CALLBACKS;
    private final Map<ResourceLocation, TimerCallback.Serializer<C, ?>> idToSerializer;
    private final Map<Class<?>, TimerCallback.Serializer<C, ?>> classToSerializer;
    
    @VisibleForTesting
    public TimerCallbacks() {
        this.idToSerializer = (Map<ResourceLocation, TimerCallback.Serializer<C, ?>>)Maps.newHashMap();
        this.classToSerializer = (Map<Class<?>, TimerCallback.Serializer<C, ?>>)Maps.newHashMap();
    }
    
    public TimerCallbacks<C> register(final TimerCallback.Serializer<C, ?> a) {
        this.idToSerializer.put(a.getId(), a);
        this.classToSerializer.put(a.getCls(), a);
        return this;
    }
    
    private <T extends TimerCallback<C>> TimerCallback.Serializer<C, T> getSerializer(final Class<?> class1) {
        return (TimerCallback.Serializer<C, T>)this.classToSerializer.get(class1);
    }
    
    public <T extends TimerCallback<C>> CompoundTag serialize(final T dca) {
        final TimerCallback.Serializer<C, T> a3 = this.<T>getSerializer(dca.getClass());
        final CompoundTag md4 = new CompoundTag();
        a3.serialize(md4, dca);
        md4.putString("Type", a3.getId().toString());
        return md4;
    }
    
    @Nullable
    public TimerCallback<C> deserialize(final CompoundTag md) {
        final ResourceLocation vk3 = ResourceLocation.tryParse(md.getString("Type"));
        final TimerCallback.Serializer<C, ?> a4 = this.idToSerializer.get(vk3);
        if (a4 == null) {
            TimerCallbacks.LOGGER.error(new StringBuilder().append("Failed to deserialize timer callback: ").append(md).toString());
            return null;
        }
        try {
            return (TimerCallback<C>)a4.deserialize(md);
        }
        catch (Exception exception5) {
            TimerCallbacks.LOGGER.error(new StringBuilder().append("Failed to deserialize timer callback: ").append(md).toString(), (Throwable)exception5);
            return null;
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        SERVER_CALLBACKS = new TimerCallbacks<MinecraftServer>().register(new FunctionCallback.Serializer()).register(new FunctionTagCallback.Serializer());
    }
}

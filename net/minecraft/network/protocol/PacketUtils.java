package net.minecraft.network.protocol;

import org.apache.logging.log4j.LogManager;
import net.minecraft.server.RunningOnDifferentThreadException;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraft.network.PacketListener;
import net.minecraft.server.level.ServerLevel;
import org.apache.logging.log4j.Logger;

public class PacketUtils {
    private static final Logger LOGGER;
    
    public static <T extends PacketListener> void ensureRunningOnSameThread(final Packet<T> oj, final T ni, final ServerLevel aag) throws RunningOnDifferentThreadException {
        PacketUtils.<T>ensureRunningOnSameThread(oj, ni, aag.getServer());
    }
    
    public static <T extends PacketListener> void ensureRunningOnSameThread(final Packet<T> oj, final T ni, final BlockableEventLoop<?> any) throws RunningOnDifferentThreadException {
        if (!any.isSameThread()) {
            any.execute(() -> {
                if (ni.getConnection().isConnected()) {
                    oj.handle(ni);
                }
                else {
                    PacketUtils.LOGGER.debug(new StringBuilder().append("Ignoring packet due to disconnection: ").append(oj).toString());
                }
            });
            throw RunningOnDifferentThreadException.RUNNING_ON_DIFFERENT_THREAD;
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}

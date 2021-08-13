package net.minecraft.client.server;

import org.apache.logging.log4j.LogManager;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import net.minecraft.DefaultUncaughtExceptionHandler;
import java.net.DatagramSocket;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.atomic.AtomicInteger;

public class LanServerPinger extends Thread {
    private static final AtomicInteger UNIQUE_THREAD_ID;
    private static final Logger LOGGER;
    private final String motd;
    private final DatagramSocket socket;
    private boolean isRunning;
    private final String serverAddress;
    
    public LanServerPinger(final String string1, final String string2) throws IOException {
        super(new StringBuilder().append("LanServerPinger #").append(LanServerPinger.UNIQUE_THREAD_ID.incrementAndGet()).toString());
        this.isRunning = true;
        this.motd = string1;
        this.serverAddress = string2;
        this.setDaemon(true);
        this.setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new DefaultUncaughtExceptionHandler(LanServerPinger.LOGGER));
        this.socket = new DatagramSocket();
    }
    
    public void run() {
        final String string2 = createPingString(this.motd, this.serverAddress);
        final byte[] arr3 = string2.getBytes(StandardCharsets.UTF_8);
        while (!this.isInterrupted() && this.isRunning) {
            try {
                final InetAddress inetAddress4 = InetAddress.getByName("224.0.2.60");
                final DatagramPacket datagramPacket5 = new DatagramPacket(arr3, arr3.length, inetAddress4, 4445);
                this.socket.send(datagramPacket5);
            }
            catch (IOException iOException4) {
                LanServerPinger.LOGGER.warn("LanServerPinger: {}", iOException4.getMessage());
                break;
            }
            try {
                sleep(1500L);
            }
            catch (InterruptedException ex) {}
        }
    }
    
    public void interrupt() {
        super.interrupt();
        this.isRunning = false;
    }
    
    public static String createPingString(final String string1, final String string2) {
        return "[MOTD]" + string1 + "[/MOTD][AD]" + string2 + "[/AD]";
    }
    
    public static String parseMotd(final String string) {
        final int integer2 = string.indexOf("[MOTD]");
        if (integer2 < 0) {
            return "missing no";
        }
        final int integer3 = string.indexOf("[/MOTD]", integer2 + "[MOTD]".length());
        if (integer3 < integer2) {
            return "missing no";
        }
        return string.substring(integer2 + "[MOTD]".length(), integer3);
    }
    
    public static String parseAddress(final String string) {
        final int integer2 = string.indexOf("[/MOTD]");
        if (integer2 < 0) {
            return null;
        }
        final int integer3 = string.indexOf("[/MOTD]", integer2 + "[/MOTD]".length());
        if (integer3 >= 0) {
            return null;
        }
        final int integer4 = string.indexOf("[AD]", integer2 + "[/MOTD]".length());
        if (integer4 < 0) {
            return null;
        }
        final int integer5 = string.indexOf("[/AD]", integer4 + "[AD]".length());
        if (integer5 < integer4) {
            return null;
        }
        return string.substring(integer4 + "[AD]".length(), integer5);
    }
    
    static {
        UNIQUE_THREAD_ID = new AtomicInteger(0);
        LOGGER = LogManager.getLogger();
    }
}

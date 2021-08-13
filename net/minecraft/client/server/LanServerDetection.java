package net.minecraft.client.server;

import java.nio.charset.StandardCharsets;
import java.net.SocketTimeoutException;
import java.net.DatagramPacket;
import java.io.IOException;
import net.minecraft.DefaultUncaughtExceptionHandler;
import java.net.MulticastSocket;
import java.util.Iterator;
import java.net.InetAddress;
import java.util.Collections;
import com.google.common.collect.Lists;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.atomic.AtomicInteger;

public class LanServerDetection {
    private static final AtomicInteger UNIQUE_THREAD_ID;
    private static final Logger LOGGER;
    
    static {
        UNIQUE_THREAD_ID = new AtomicInteger(0);
        LOGGER = LogManager.getLogger();
    }
    
    public static class LanServerList {
        private final List<LanServer> servers;
        private boolean isDirty;
        
        public LanServerList() {
            this.servers = (List<LanServer>)Lists.newArrayList();
        }
        
        public synchronized boolean isDirty() {
            return this.isDirty;
        }
        
        public synchronized void markClean() {
            this.isDirty = false;
        }
        
        public synchronized List<LanServer> getServers() {
            return (List<LanServer>)Collections.unmodifiableList((List)this.servers);
        }
        
        public synchronized void addServer(final String string, final InetAddress inetAddress) {
            final String string2 = LanServerPinger.parseMotd(string);
            String string3 = LanServerPinger.parseAddress(string);
            if (string3 == null) {
                return;
            }
            string3 = inetAddress.getHostAddress() + ":" + string3;
            boolean boolean6 = false;
            for (final LanServer emz8 : this.servers) {
                if (emz8.getAddress().equals(string3)) {
                    emz8.updatePingTime();
                    boolean6 = true;
                    break;
                }
            }
            if (!boolean6) {
                this.servers.add(new LanServer(string2, string3));
                this.isDirty = true;
            }
        }
    }
    
    public static class LanServerDetector extends Thread {
        private final LanServerList serverList;
        private final InetAddress pingGroup;
        private final MulticastSocket socket;
        
        public LanServerDetector(final LanServerList b) throws IOException {
            super(new StringBuilder().append("LanServerDetector #").append(LanServerDetection.UNIQUE_THREAD_ID.incrementAndGet()).toString());
            this.serverList = b;
            this.setDaemon(true);
            this.setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new DefaultUncaughtExceptionHandler(LanServerDetection.LOGGER));
            this.socket = new MulticastSocket(4445);
            this.pingGroup = InetAddress.getByName("224.0.2.60");
            this.socket.setSoTimeout(5000);
            this.socket.joinGroup(this.pingGroup);
        }
        
        public void run() {
            final byte[] arr3 = new byte[1024];
            while (!this.isInterrupted()) {
                final DatagramPacket datagramPacket2 = new DatagramPacket(arr3, arr3.length);
                try {
                    this.socket.receive(datagramPacket2);
                }
                catch (SocketTimeoutException socketTimeoutException4) {
                    continue;
                }
                catch (IOException iOException4) {
                    LanServerDetection.LOGGER.error("Couldn't ping server", (Throwable)iOException4);
                    break;
                }
                final String string4 = new String(datagramPacket2.getData(), datagramPacket2.getOffset(), datagramPacket2.getLength(), StandardCharsets.UTF_8);
                LanServerDetection.LOGGER.debug("{}: {}", datagramPacket2.getAddress(), string4);
                this.serverList.addServer(string4, datagramPacket2.getAddress());
            }
            try {
                this.socket.leaveGroup(this.pingGroup);
            }
            catch (IOException ex) {}
            this.socket.close();
        }
    }
}

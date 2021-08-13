package net.minecraft.server.rcon.thread;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.server.dedicated.DedicatedServerProperties;
import java.net.InetAddress;
import java.net.Socket;
import java.io.IOException;
import java.net.SocketTimeoutException;
import com.google.common.collect.Lists;
import net.minecraft.server.ServerInterface;
import java.util.List;
import java.net.ServerSocket;
import org.apache.logging.log4j.Logger;

public class RconThread extends GenericThread {
    private static final Logger LOGGER;
    private final ServerSocket socket;
    private final String rconPassword;
    private final List<RconClient> clients;
    private final ServerInterface serverInterface;
    
    private RconThread(final ServerInterface vy, final ServerSocket serverSocket, final String string) {
        super("RCON Listener");
        this.clients = (List<RconClient>)Lists.newArrayList();
        this.serverInterface = vy;
        this.socket = serverSocket;
        this.rconPassword = string;
    }
    
    private void clearClients() {
        this.clients.removeIf(adi -> !adi.isRunning());
    }
    
    public void run() {
        try {
            while (this.running) {
                try {
                    final Socket socket2 = this.socket.accept();
                    final RconClient adi3 = new RconClient(this.serverInterface, this.rconPassword, socket2);
                    adi3.start();
                    this.clients.add(adi3);
                    this.clearClients();
                }
                catch (SocketTimeoutException socketTimeoutException2) {
                    this.clearClients();
                }
                catch (IOException iOException2) {
                    if (!this.running) {
                        continue;
                    }
                    RconThread.LOGGER.info("IO exception: ", (Throwable)iOException2);
                }
            }
        }
        finally {
            this.closeSocket(this.socket);
        }
    }
    
    @Nullable
    public static RconThread create(final ServerInterface vy) {
        final DedicatedServerProperties zh2 = vy.getProperties();
        String string3 = vy.getServerIp();
        if (string3.isEmpty()) {
            string3 = "0.0.0.0";
        }
        final int integer4 = zh2.rconPort;
        if (0 >= integer4 || 65535 < integer4) {
            RconThread.LOGGER.warn("Invalid rcon port {} found in server.properties, rcon disabled!", integer4);
            return null;
        }
        final String string4 = zh2.rconPassword;
        if (string4.isEmpty()) {
            RconThread.LOGGER.warn("No rcon password set in server.properties, rcon disabled!");
            return null;
        }
        try {
            final ServerSocket serverSocket6 = new ServerSocket(integer4, 0, InetAddress.getByName(string3));
            serverSocket6.setSoTimeout(500);
            final RconThread adj7 = new RconThread(vy, serverSocket6, string4);
            if (!adj7.start()) {
                return null;
            }
            RconThread.LOGGER.info("RCON running on {}:{}", string3, integer4);
            return adj7;
        }
        catch (IOException iOException6) {
            RconThread.LOGGER.warn("Unable to initialise RCON on {}:{}", string3, integer4, iOException6);
            return null;
        }
    }
    
    @Override
    public void stop() {
        this.running = false;
        this.closeSocket(this.socket);
        super.stop();
        for (final RconClient adi3 : this.clients) {
            if (adi3.isRunning()) {
                adi3.stop();
            }
        }
        this.clients.clear();
    }
    
    private void closeSocket(final ServerSocket serverSocket) {
        RconThread.LOGGER.debug("closeSocket: {}", serverSocket);
        try {
            serverSocket.close();
        }
        catch (IOException iOException3) {
            RconThread.LOGGER.warn("Failed to close socket", (Throwable)iOException3);
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}

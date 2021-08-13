package net.minecraft.server.rcon.thread;

import org.apache.logging.log4j.LogManager;
import java.nio.charset.StandardCharsets;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import net.minecraft.server.rcon.PktUtils;
import java.io.BufferedInputStream;
import net.minecraft.server.ServerInterface;
import java.net.Socket;
import org.apache.logging.log4j.Logger;

public class RconClient extends GenericThread {
    private static final Logger LOGGER;
    private boolean authed;
    private final Socket client;
    private final byte[] buf;
    private final String rconPassword;
    private final ServerInterface serverInterface;
    
    RconClient(final ServerInterface vy, final String string, final Socket socket) {
        super(new StringBuilder().append("RCON Client ").append(socket.getInetAddress()).toString());
        this.buf = new byte[1460];
        this.serverInterface = vy;
        this.client = socket;
        try {
            this.client.setSoTimeout(0);
        }
        catch (Exception exception5) {
            this.running = false;
        }
        this.rconPassword = string;
    }
    
    public void run() {
        try {
            while (this.running) {
                final BufferedInputStream bufferedInputStream2 = new BufferedInputStream(this.client.getInputStream());
                final int integer3 = bufferedInputStream2.read(this.buf, 0, 1460);
                if (10 > integer3) {
                    return;
                }
                int integer4 = 0;
                final int integer5 = PktUtils.intFromByteArray(this.buf, 0, integer3);
                if (integer5 != integer3 - 4) {
                    return;
                }
                integer4 += 4;
                final int integer6 = PktUtils.intFromByteArray(this.buf, integer4, integer3);
                integer4 += 4;
                final int integer7 = PktUtils.intFromByteArray(this.buf, integer4);
                integer4 += 4;
                switch (integer7) {
                    case 3: {
                        final String string8 = PktUtils.stringFromByteArray(this.buf, integer4, integer3);
                        integer4 += string8.length();
                        if (!string8.isEmpty() && string8.equals(this.rconPassword)) {
                            this.authed = true;
                            this.send(integer6, 2, "");
                            continue;
                        }
                        this.authed = false;
                        this.sendAuthFailure();
                        continue;
                    }
                    case 2: {
                        if (this.authed) {
                            final String string9 = PktUtils.stringFromByteArray(this.buf, integer4, integer3);
                            try {
                                this.sendCmdResponse(integer6, this.serverInterface.runCommand(string9));
                            }
                            catch (Exception exception10) {
                                this.sendCmdResponse(integer6, "Error executing: " + string9 + " (" + exception10.getMessage() + ")");
                            }
                            continue;
                        }
                        this.sendAuthFailure();
                        continue;
                    }
                    default: {
                        this.sendCmdResponse(integer6, String.format("Unknown request %s", new Object[] { Integer.toHexString(integer7) }));
                        continue;
                    }
                }
            }
        }
        catch (IOException ex) {}
        catch (Exception exception11) {
            RconClient.LOGGER.error("Exception whilst parsing RCON input", (Throwable)exception11);
        }
        finally {
            this.closeSocket();
            RconClient.LOGGER.info("Thread {} shutting down", this.name);
            this.running = false;
        }
    }
    
    private void send(final int integer1, final int integer2, final String string) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream5 = new ByteArrayOutputStream(1248);
        final DataOutputStream dataOutputStream6 = new DataOutputStream((OutputStream)byteArrayOutputStream5);
        final byte[] arr7 = string.getBytes(StandardCharsets.UTF_8);
        dataOutputStream6.writeInt(Integer.reverseBytes(arr7.length + 10));
        dataOutputStream6.writeInt(Integer.reverseBytes(integer1));
        dataOutputStream6.writeInt(Integer.reverseBytes(integer2));
        dataOutputStream6.write(arr7);
        dataOutputStream6.write(0);
        dataOutputStream6.write(0);
        this.client.getOutputStream().write(byteArrayOutputStream5.toByteArray());
    }
    
    private void sendAuthFailure() throws IOException {
        this.send(-1, 2, "");
    }
    
    private void sendCmdResponse(final int integer, String string) throws IOException {
        int integer2 = string.length();
        do {
            final int integer3 = (4096 <= integer2) ? 4096 : integer2;
            this.send(integer, 0, string.substring(0, integer3));
            string = string.substring(integer3);
            integer2 = string.length();
        } while (0 != integer2);
    }
    
    @Override
    public void stop() {
        this.running = false;
        this.closeSocket();
        super.stop();
    }
    
    private void closeSocket() {
        try {
            this.client.close();
        }
        catch (IOException iOException2) {
            RconClient.LOGGER.warn("Failed to close socket", (Throwable)iOException2);
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}

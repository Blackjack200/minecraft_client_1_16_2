package net.minecraft.server.rcon.thread;

import java.util.Random;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import java.net.PortUnreachableException;
import java.net.SocketTimeoutException;
import net.minecraft.Util;
import net.minecraft.server.rcon.PktUtils;
import java.io.IOException;
import java.net.DatagramPacket;
import javax.annotation.Nullable;
import com.google.common.collect.Maps;
import java.net.UnknownHostException;
import java.net.InetAddress;
import net.minecraft.server.ServerInterface;
import net.minecraft.server.rcon.NetworkDataOutputStream;
import java.net.SocketAddress;
import java.util.Map;
import java.net.DatagramSocket;
import org.apache.logging.log4j.Logger;

public class QueryThreadGs4 extends GenericThread {
    private static final Logger LOGGER;
    private long lastChallengeCheck;
    private final int port;
    private final int serverPort;
    private final int maxPlayers;
    private final String serverName;
    private final String worldName;
    private DatagramSocket socket;
    private final byte[] buffer;
    private String hostIp;
    private String serverIp;
    private final Map<SocketAddress, RequestChallenge> validChallenges;
    private final NetworkDataOutputStream rulesResponse;
    private long lastRulesResponse;
    private final ServerInterface serverInterface;
    
    private QueryThreadGs4(final ServerInterface vy, final int integer) {
        super("Query Listener");
        this.buffer = new byte[1460];
        this.serverInterface = vy;
        this.port = integer;
        this.serverIp = vy.getServerIp();
        this.serverPort = vy.getServerPort();
        this.serverName = vy.getServerName();
        this.maxPlayers = vy.getMaxPlayers();
        this.worldName = vy.getLevelIdName();
        this.lastRulesResponse = 0L;
        this.hostIp = "0.0.0.0";
        if (this.serverIp.isEmpty() || this.hostIp.equals(this.serverIp)) {
            this.serverIp = "0.0.0.0";
            try {
                final InetAddress inetAddress4 = InetAddress.getLocalHost();
                this.hostIp = inetAddress4.getHostAddress();
            }
            catch (UnknownHostException unknownHostException4) {
                QueryThreadGs4.LOGGER.warn("Unable to determine local host IP, please set server-ip in server.properties", (Throwable)unknownHostException4);
            }
        }
        else {
            this.hostIp = this.serverIp;
        }
        this.rulesResponse = new NetworkDataOutputStream(1460);
        this.validChallenges = (Map<SocketAddress, RequestChallenge>)Maps.newHashMap();
    }
    
    @Nullable
    public static QueryThreadGs4 create(final ServerInterface vy) {
        final int integer2 = vy.getProperties().queryPort;
        if (0 >= integer2 || 65535 < integer2) {
            QueryThreadGs4.LOGGER.warn("Invalid query port {} found in server.properties (queries disabled)", integer2);
            return null;
        }
        final QueryThreadGs4 adh3 = new QueryThreadGs4(vy, integer2);
        if (!adh3.start()) {
            return null;
        }
        return adh3;
    }
    
    private void sendTo(final byte[] arr, final DatagramPacket datagramPacket) throws IOException {
        this.socket.send(new DatagramPacket(arr, arr.length, datagramPacket.getSocketAddress()));
    }
    
    private boolean processPacket(final DatagramPacket datagramPacket) throws IOException {
        final byte[] arr3 = datagramPacket.getData();
        final int integer4 = datagramPacket.getLength();
        final SocketAddress socketAddress5 = datagramPacket.getSocketAddress();
        QueryThreadGs4.LOGGER.debug("Packet len {} [{}]", integer4, socketAddress5);
        if (3 > integer4 || -2 != arr3[0] || -3 != arr3[1]) {
            QueryThreadGs4.LOGGER.debug("Invalid packet [{}]", socketAddress5);
            return false;
        }
        QueryThreadGs4.LOGGER.debug("Packet '{}' [{}]", PktUtils.toHexString(arr3[2]), socketAddress5);
        switch (arr3[2]) {
            case 9: {
                this.sendChallenge(datagramPacket);
                QueryThreadGs4.LOGGER.debug("Challenge [{}]", socketAddress5);
                return true;
            }
            case 0: {
                if (!this.validChallenge(datagramPacket)) {
                    QueryThreadGs4.LOGGER.debug("Invalid challenge [{}]", socketAddress5);
                    return false;
                }
                if (15 == integer4) {
                    this.sendTo(this.buildRuleResponse(datagramPacket), datagramPacket);
                    QueryThreadGs4.LOGGER.debug("Rules [{}]", socketAddress5);
                    break;
                }
                final NetworkDataOutputStream adc6 = new NetworkDataOutputStream(1460);
                adc6.write(0);
                adc6.writeBytes(this.getIdentBytes(datagramPacket.getSocketAddress()));
                adc6.writeString(this.serverName);
                adc6.writeString("SMP");
                adc6.writeString(this.worldName);
                adc6.writeString(Integer.toString(this.serverInterface.getPlayerCount()));
                adc6.writeString(Integer.toString(this.maxPlayers));
                adc6.writeShort((short)this.serverPort);
                adc6.writeString(this.hostIp);
                this.sendTo(adc6.toByteArray(), datagramPacket);
                QueryThreadGs4.LOGGER.debug("Status [{}]", socketAddress5);
                break;
            }
        }
        return true;
    }
    
    private byte[] buildRuleResponse(final DatagramPacket datagramPacket) throws IOException {
        final long long3 = Util.getMillis();
        if (long3 < this.lastRulesResponse + 5000L) {
            final byte[] arr5 = this.rulesResponse.toByteArray();
            final byte[] arr6 = this.getIdentBytes(datagramPacket.getSocketAddress());
            arr5[1] = arr6[0];
            arr5[2] = arr6[1];
            arr5[3] = arr6[2];
            arr5[4] = arr6[3];
            return arr5;
        }
        this.lastRulesResponse = long3;
        this.rulesResponse.reset();
        this.rulesResponse.write(0);
        this.rulesResponse.writeBytes(this.getIdentBytes(datagramPacket.getSocketAddress()));
        this.rulesResponse.writeString("splitnum");
        this.rulesResponse.write(128);
        this.rulesResponse.write(0);
        this.rulesResponse.writeString("hostname");
        this.rulesResponse.writeString(this.serverName);
        this.rulesResponse.writeString("gametype");
        this.rulesResponse.writeString("SMP");
        this.rulesResponse.writeString("game_id");
        this.rulesResponse.writeString("MINECRAFT");
        this.rulesResponse.writeString("version");
        this.rulesResponse.writeString(this.serverInterface.getServerVersion());
        this.rulesResponse.writeString("plugins");
        this.rulesResponse.writeString(this.serverInterface.getPluginNames());
        this.rulesResponse.writeString("map");
        this.rulesResponse.writeString(this.worldName);
        this.rulesResponse.writeString("numplayers");
        this.rulesResponse.writeString(new StringBuilder().append("").append(this.serverInterface.getPlayerCount()).toString());
        this.rulesResponse.writeString("maxplayers");
        this.rulesResponse.writeString(new StringBuilder().append("").append(this.maxPlayers).toString());
        this.rulesResponse.writeString("hostport");
        this.rulesResponse.writeString(new StringBuilder().append("").append(this.serverPort).toString());
        this.rulesResponse.writeString("hostip");
        this.rulesResponse.writeString(this.hostIp);
        this.rulesResponse.write(0);
        this.rulesResponse.write(1);
        this.rulesResponse.writeString("player_");
        this.rulesResponse.write(0);
        final String[] playerNames;
        final String[] arr7 = playerNames = this.serverInterface.getPlayerNames();
        for (final String string9 : playerNames) {
            this.rulesResponse.writeString(string9);
        }
        this.rulesResponse.write(0);
        return this.rulesResponse.toByteArray();
    }
    
    private byte[] getIdentBytes(final SocketAddress socketAddress) {
        return ((RequestChallenge)this.validChallenges.get(socketAddress)).getIdentBytes();
    }
    
    private Boolean validChallenge(final DatagramPacket datagramPacket) {
        final SocketAddress socketAddress3 = datagramPacket.getSocketAddress();
        if (!this.validChallenges.containsKey(socketAddress3)) {
            return false;
        }
        final byte[] arr4 = datagramPacket.getData();
        return ((RequestChallenge)this.validChallenges.get(socketAddress3)).getChallenge() == PktUtils.intFromNetworkByteArray(arr4, 7, datagramPacket.getLength());
    }
    
    private void sendChallenge(final DatagramPacket datagramPacket) throws IOException {
        final RequestChallenge a3 = new RequestChallenge(datagramPacket);
        this.validChallenges.put(datagramPacket.getSocketAddress(), a3);
        this.sendTo(a3.getChallengeBytes(), datagramPacket);
    }
    
    private void pruneChallenges() {
        if (!this.running) {
            return;
        }
        final long long2 = Util.getMillis();
        if (long2 < this.lastChallengeCheck + 30000L) {
            return;
        }
        this.lastChallengeCheck = long2;
        this.validChallenges.values().removeIf(a -> a.before(long2));
    }
    
    public void run() {
        QueryThreadGs4.LOGGER.info("Query running on {}:{}", this.serverIp, this.port);
        this.lastChallengeCheck = Util.getMillis();
        final DatagramPacket datagramPacket2 = new DatagramPacket(this.buffer, this.buffer.length);
        try {
            while (this.running) {
                try {
                    this.socket.receive(datagramPacket2);
                    this.pruneChallenges();
                    this.processPacket(datagramPacket2);
                }
                catch (SocketTimeoutException socketTimeoutException3) {
                    this.pruneChallenges();
                }
                catch (PortUnreachableException ex) {}
                catch (IOException iOException3) {
                    this.recoverSocketError((Exception)iOException3);
                }
            }
        }
        finally {
            QueryThreadGs4.LOGGER.debug("closeSocket: {}:{}", this.serverIp, this.port);
            this.socket.close();
        }
    }
    
    @Override
    public boolean start() {
        return this.running || (this.initSocket() && super.start());
    }
    
    private void recoverSocketError(final Exception exception) {
        if (!this.running) {
            return;
        }
        QueryThreadGs4.LOGGER.warn("Unexpected exception", (Throwable)exception);
        if (!this.initSocket()) {
            QueryThreadGs4.LOGGER.error("Failed to recover from exception, shutting down!");
            this.running = false;
        }
    }
    
    private boolean initSocket() {
        try {
            (this.socket = new DatagramSocket(this.port, InetAddress.getByName(this.serverIp))).setSoTimeout(500);
            return true;
        }
        catch (Exception exception2) {
            QueryThreadGs4.LOGGER.warn("Unable to initialise query system on {}:{}", this.serverIp, this.port, exception2);
            return false;
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    static class RequestChallenge {
        private final long time;
        private final int challenge;
        private final byte[] identBytes;
        private final byte[] challengeBytes;
        private final String ident;
        
        public RequestChallenge(final DatagramPacket datagramPacket) {
            this.time = new Date().getTime();
            final byte[] arr3 = datagramPacket.getData();
            (this.identBytes = new byte[4])[0] = arr3[3];
            this.identBytes[1] = arr3[4];
            this.identBytes[2] = arr3[5];
            this.identBytes[3] = arr3[6];
            this.ident = new String(this.identBytes, StandardCharsets.UTF_8);
            this.challenge = new Random().nextInt(16777216);
            this.challengeBytes = String.format("\t%s%d\u0000", new Object[] { this.ident, this.challenge }).getBytes(StandardCharsets.UTF_8);
        }
        
        public Boolean before(final long long1) {
            return this.time < long1;
        }
        
        public int getChallenge() {
            return this.challenge;
        }
        
        public byte[] getChallengeBytes() {
            return this.challengeBytes;
        }
        
        public byte[] getIdentBytes() {
            return this.identBytes;
        }
    }
}

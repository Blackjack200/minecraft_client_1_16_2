package com.mojang.realmsclient.client;

import net.minecraft.Util;
import java.net.SocketAddress;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.util.Comparator;
import com.google.common.collect.Lists;
import com.mojang.realmsclient.dto.RegionPingResult;
import java.util.List;

public class Ping {
    public static List<RegionPingResult> ping(final Region... arr) {
        for (final Region a5 : arr) {
            ping(a5.endpoint);
        }
        final List<RegionPingResult> list2 = (List<RegionPingResult>)Lists.newArrayList();
        for (final Region a6 : arr) {
            list2.add(new RegionPingResult(a6.name, ping(a6.endpoint)));
        }
        list2.sort(Comparator.comparingInt(RegionPingResult::ping));
        return list2;
    }
    
    private static int ping(final String string) {
        final int integer2 = 700;
        long long3 = 0L;
        Socket socket5 = null;
        for (int integer3 = 0; integer3 < 5; ++integer3) {
            try {
                final SocketAddress socketAddress7 = (SocketAddress)new InetSocketAddress(string, 80);
                socket5 = new Socket();
                final long long4 = now();
                socket5.connect(socketAddress7, 700);
                long3 += now() - long4;
            }
            catch (Exception exception7) {
                long3 += 700L;
            }
            finally {
                close(socket5);
            }
        }
        return (int)(long3 / 5.0);
    }
    
    private static void close(final Socket socket) {
        try {
            if (socket != null) {
                socket.close();
            }
        }
        catch (Throwable t) {}
    }
    
    private static long now() {
        return Util.getMillis();
    }
    
    public static List<RegionPingResult> pingAllRegions() {
        return ping(Region.values());
    }
    
    enum Region {
        US_EAST_1("us-east-1", "ec2.us-east-1.amazonaws.com"), 
        US_WEST_2("us-west-2", "ec2.us-west-2.amazonaws.com"), 
        US_WEST_1("us-west-1", "ec2.us-west-1.amazonaws.com"), 
        EU_WEST_1("eu-west-1", "ec2.eu-west-1.amazonaws.com"), 
        AP_SOUTHEAST_1("ap-southeast-1", "ec2.ap-southeast-1.amazonaws.com"), 
        AP_SOUTHEAST_2("ap-southeast-2", "ec2.ap-southeast-2.amazonaws.com"), 
        AP_NORTHEAST_1("ap-northeast-1", "ec2.ap-northeast-1.amazonaws.com"), 
        SA_EAST_1("sa-east-1", "ec2.sa-east-1.amazonaws.com");
        
        private final String name;
        private final String endpoint;
        
        private Region(final String string3, final String string4) {
            this.name = string3;
            this.endpoint = string4;
        }
    }
}

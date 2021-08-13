package net.minecraft.server.rcon;

import java.nio.charset.StandardCharsets;

public class PktUtils {
    public static final char[] HEX_CHAR;
    
    public static String stringFromByteArray(final byte[] arr, final int integer2, final int integer3) {
        int integer4;
        int integer5;
        for (integer4 = integer3 - 1, integer5 = ((integer2 > integer4) ? integer4 : integer2); 0 != arr[integer5] && integer5 < integer4; ++integer5) {}
        return new String(arr, integer2, integer5 - integer2, StandardCharsets.UTF_8);
    }
    
    public static int intFromByteArray(final byte[] arr, final int integer) {
        return intFromByteArray(arr, integer, arr.length);
    }
    
    public static int intFromByteArray(final byte[] arr, final int integer2, final int integer3) {
        if (0 > integer3 - integer2 - 4) {
            return 0;
        }
        return arr[integer2 + 3] << 24 | (arr[integer2 + 2] & 0xFF) << 16 | (arr[integer2 + 1] & 0xFF) << 8 | (arr[integer2] & 0xFF);
    }
    
    public static int intFromNetworkByteArray(final byte[] arr, final int integer2, final int integer3) {
        if (0 > integer3 - integer2 - 4) {
            return 0;
        }
        return arr[integer2] << 24 | (arr[integer2 + 1] & 0xFF) << 16 | (arr[integer2 + 2] & 0xFF) << 8 | (arr[integer2 + 3] & 0xFF);
    }
    
    public static String toHexString(final byte byte1) {
        return new StringBuilder().append("").append(PktUtils.HEX_CHAR[(byte1 & 0xF0) >>> 4]).append(PktUtils.HEX_CHAR[byte1 & 0xF]).toString();
    }
    
    static {
        HEX_CHAR = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    }
}

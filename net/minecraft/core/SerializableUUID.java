package net.minecraft.core;

import net.minecraft.Util;
import com.mojang.serialization.DataResult;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.UUID;
import com.mojang.serialization.Codec;

public final class SerializableUUID {
    public static final Codec<UUID> CODEC;
    
    public static UUID uuidFromIntArray(final int[] arr) {
        return new UUID((long)arr[0] << 32 | ((long)arr[1] & 0xFFFFFFFFL), (long)arr[2] << 32 | ((long)arr[3] & 0xFFFFFFFFL));
    }
    
    public static int[] uuidToIntArray(final UUID uUID) {
        final long long2 = uUID.getMostSignificantBits();
        final long long3 = uUID.getLeastSignificantBits();
        return leastMostToIntArray(long2, long3);
    }
    
    private static int[] leastMostToIntArray(final long long1, final long long2) {
        return new int[] { (int)(long1 >> 32), (int)long1, (int)(long2 >> 32), (int)long2 };
    }
    
    static {
        CODEC = Codec.INT_STREAM.comapFlatMap(intStream -> Util.fixedSize(intStream, 4).map(SerializableUUID::uuidFromIntArray), uUID -> Arrays.stream(uuidToIntArray(uUID)));
    }
}

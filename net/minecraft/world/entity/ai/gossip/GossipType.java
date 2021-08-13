package net.minecraft.world.entity.ai.gossip;

import com.google.common.collect.ImmutableMap;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import java.util.Map;

public enum GossipType {
    MAJOR_NEGATIVE("major_negative", -5, 100, 10, 10), 
    MINOR_NEGATIVE("minor_negative", -1, 200, 20, 20), 
    MINOR_POSITIVE("minor_positive", 1, 200, 1, 5), 
    MAJOR_POSITIVE("major_positive", 5, 100, 0, 100), 
    TRADING("trading", 1, 25, 2, 20);
    
    public final String id;
    public final int weight;
    public final int max;
    public final int decayPerDay;
    public final int decayPerTransfer;
    private static final Map<String, GossipType> BY_ID;
    
    private GossipType(final String string3, final int integer4, final int integer5, final int integer6, final int integer7) {
        this.id = string3;
        this.weight = integer4;
        this.max = integer5;
        this.decayPerDay = integer6;
        this.decayPerTransfer = integer7;
    }
    
    @Nullable
    public static GossipType byId(final String string) {
        return (GossipType)GossipType.BY_ID.get(string);
    }
    
    static {
        BY_ID = (Map)Stream.of((Object[])values()).collect(ImmutableMap.toImmutableMap(axx -> axx.id, Function.identity()));
    }
}

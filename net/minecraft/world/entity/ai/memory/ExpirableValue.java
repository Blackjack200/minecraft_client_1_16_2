package net.minecraft.world.entity.ai.memory;

import java.util.Optional;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;

public class ExpirableValue<T> {
    private final T value;
    private long timeToLive;
    
    public ExpirableValue(final T object, final long long2) {
        this.value = object;
        this.timeToLive = long2;
    }
    
    public void tick() {
        if (this.canExpire()) {
            --this.timeToLive;
        }
    }
    
    public static <T> ExpirableValue<T> of(final T object) {
        return new ExpirableValue<T>(object, Long.MAX_VALUE);
    }
    
    public static <T> ExpirableValue<T> of(final T object, final long long2) {
        return new ExpirableValue<T>(object, long2);
    }
    
    public T getValue() {
        return this.value;
    }
    
    public boolean hasExpired() {
        return this.timeToLive <= 0L;
    }
    
    public String toString() {
        return this.value.toString() + (this.canExpire() ? new StringBuilder().append(" (ttl: ").append(this.timeToLive).append(")").toString() : "");
    }
    
    public boolean canExpire() {
        return this.timeToLive != Long.MAX_VALUE;
    }
    
    public static <T> Codec<ExpirableValue<T>> codec(final Codec<T> codec) {
        return (Codec<ExpirableValue<T>>)RecordCodecBuilder.create(instance -> instance.group((App)codec.fieldOf("value").forGetter(axz -> axz.value), (App)Codec.LONG.optionalFieldOf("ttl").forGetter(axz -> axz.canExpire() ? Optional.of(axz.timeToLive) : Optional.empty())).apply((Applicative)instance, (object, optional) -> new ExpirableValue((T)object, (long)optional.orElse(Long.MAX_VALUE))));
    }
}

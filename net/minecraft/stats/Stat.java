package net.minecraft.stats;

import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;

public class Stat<T> extends ObjectiveCriteria {
    private final StatFormatter formatter;
    private final T value;
    private final StatType<T> type;
    
    protected Stat(final StatType<T> adx, final T object, final StatFormatter adw) {
        super(Stat.<T>buildName(adx, object));
        this.type = adx;
        this.formatter = adw;
        this.value = object;
    }
    
    public static <T> String buildName(final StatType<T> adx, final T object) {
        return Stat.locationToKey(Registry.STAT_TYPE.getKey(adx)) + ":" + Stat.locationToKey(adx.getRegistry().getKey(object));
    }
    
    private static <T> String locationToKey(@Nullable final ResourceLocation vk) {
        return vk.toString().replace(':', '.');
    }
    
    public StatType<T> getType() {
        return this.type;
    }
    
    public T getValue() {
        return this.value;
    }
    
    public String format(final int integer) {
        return this.formatter.format(integer);
    }
    
    public boolean equals(final Object object) {
        return this == object || (object instanceof Stat && Objects.equals(this.getName(), ((Stat)object).getName()));
    }
    
    public int hashCode() {
        return this.getName().hashCode();
    }
    
    public String toString() {
        return "Stat{name=" + this.getName() + ", formatter=" + this.formatter + '}';
    }
}

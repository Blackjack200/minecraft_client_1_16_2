package net.minecraft.core;

import java.util.Random;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.mojang.serialization.Lifecycle;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class DefaultedRegistry<T> extends MappedRegistry<T> {
    private final ResourceLocation defaultKey;
    private T defaultValue;
    
    public DefaultedRegistry(final String string, final ResourceKey<? extends Registry<T>> vj, final Lifecycle lifecycle) {
        super(vj, lifecycle);
        this.defaultKey = new ResourceLocation(string);
    }
    
    @Override
    public <V extends T> V registerMapping(final int integer, final ResourceKey<T> vj, final V object, final Lifecycle lifecycle) {
        if (this.defaultKey.equals(vj.location())) {
            this.defaultValue = object;
        }
        return super.<V>registerMapping(integer, vj, object, lifecycle);
    }
    
    @Override
    public int getId(@Nullable final T object) {
        final int integer3 = super.getId(object);
        return (integer3 == -1) ? super.getId(this.defaultValue) : integer3;
    }
    
    @Nonnull
    @Override
    public ResourceLocation getKey(final T object) {
        final ResourceLocation vk3 = super.getKey(object);
        return (vk3 == null) ? this.defaultKey : vk3;
    }
    
    @Nonnull
    @Override
    public T get(@Nullable final ResourceLocation vk) {
        final T object3 = super.get(vk);
        return (object3 == null) ? this.defaultValue : object3;
    }
    
    @Override
    public Optional<T> getOptional(@Nullable final ResourceLocation vk) {
        return (Optional<T>)Optional.ofNullable(super.get(vk));
    }
    
    @Nonnull
    @Override
    public T byId(final int integer) {
        final T object3 = super.byId(integer);
        return (object3 == null) ? this.defaultValue : object3;
    }
    
    @Nonnull
    @Override
    public T getRandom(final Random random) {
        final T object3 = super.getRandom(random);
        return (object3 == null) ? this.defaultValue : object3;
    }
    
    public ResourceLocation getDefaultKey() {
        return this.defaultKey;
    }
}

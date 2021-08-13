package net.minecraft.world.level.timers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

@FunctionalInterface
public interface TimerCallback<T> {
    void handle(final T object, final TimerQueue<T> dcc, final long long3);
    
    public abstract static class Serializer<T, C extends TimerCallback<T>> {
        private final ResourceLocation id;
        private final Class<?> cls;
        
        public Serializer(final ResourceLocation vk, final Class<?> class2) {
            this.id = vk;
            this.cls = class2;
        }
        
        public ResourceLocation getId() {
            return this.id;
        }
        
        public Class<?> getCls() {
            return this.cls;
        }
        
        public abstract void serialize(final CompoundTag md, final C dca);
        
        public abstract C deserialize(final CompoundTag md);
    }
}

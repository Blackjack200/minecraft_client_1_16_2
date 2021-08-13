package net.minecraft.world.level.storage.loot;

public class SerializerType<T> {
    private final Serializer<? extends T> serializer;
    
    public SerializerType(final Serializer<? extends T> czb) {
        this.serializer = czb;
    }
    
    public Serializer<? extends T> getSerializer() {
        return this.serializer;
    }
}

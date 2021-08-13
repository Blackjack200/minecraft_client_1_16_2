package net.minecraft.network.syncher;

public class EntityDataAccessor<T> {
    private final int id;
    private final EntityDataSerializer<T> serializer;
    
    public EntityDataAccessor(final int integer, final EntityDataSerializer<T> ut) {
        this.id = integer;
        this.serializer = ut;
    }
    
    public int getId() {
        return this.id;
    }
    
    public EntityDataSerializer<T> getSerializer() {
        return this.serializer;
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        final EntityDataAccessor<?> us3 = object;
        return this.id == us3.id;
    }
    
    public int hashCode() {
        return this.id;
    }
    
    public String toString() {
        return new StringBuilder().append("<entity data: ").append(this.id).append(">").toString();
    }
}

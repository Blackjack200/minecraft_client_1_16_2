package net.minecraft.world.inventory;

public interface ContainerData {
    int get(final int integer);
    
    void set(final int integer1, final int integer2);
    
    int getCount();
}

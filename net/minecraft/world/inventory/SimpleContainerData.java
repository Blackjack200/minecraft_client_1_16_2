package net.minecraft.world.inventory;

public class SimpleContainerData implements ContainerData {
    private final int[] ints;
    
    public SimpleContainerData(final int integer) {
        this.ints = new int[integer];
    }
    
    public int get(final int integer) {
        return this.ints[integer];
    }
    
    public void set(final int integer1, final int integer2) {
        this.ints[integer1] = integer2;
    }
    
    public int getCount() {
        return this.ints.length;
    }
}

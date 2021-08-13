package net.minecraft.world.inventory;

public abstract class DataSlot {
    private int prevValue;
    
    public static DataSlot forContainer(final ContainerData bii, final int integer) {
        return new DataSlot() {
            @Override
            public int get() {
                return bii.get(integer);
            }
            
            @Override
            public void set(final int integer) {
                bii.set(integer, integer);
            }
        };
    }
    
    public static DataSlot shared(final int[] arr, final int integer) {
        return new DataSlot() {
            @Override
            public int get() {
                return arr[integer];
            }
            
            @Override
            public void set(final int integer) {
                arr[integer] = integer;
            }
        };
    }
    
    public static DataSlot standalone() {
        return new DataSlot() {
            private int value;
            
            @Override
            public int get() {
                return this.value;
            }
            
            @Override
            public void set(final int integer) {
                this.value = integer;
            }
        };
    }
    
    public abstract int get();
    
    public abstract void set(final int integer);
    
    public boolean checkAndClearUpdateFlag() {
        final int integer2 = this.get();
        final boolean boolean3 = integer2 != this.prevValue;
        this.prevValue = integer2;
        return boolean3;
    }
}

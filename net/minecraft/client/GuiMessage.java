package net.minecraft.client;

public class GuiMessage<T> {
    private final int addedTime;
    private final T message;
    private final int id;
    
    public GuiMessage(final int integer1, final T object, final int integer3) {
        this.message = object;
        this.addedTime = integer1;
        this.id = integer3;
    }
    
    public T getMessage() {
        return this.message;
    }
    
    public int getAddedTime() {
        return this.addedTime;
    }
    
    public int getId() {
        return this.id;
    }
}

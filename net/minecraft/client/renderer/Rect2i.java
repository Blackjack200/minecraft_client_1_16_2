package net.minecraft.client.renderer;

public class Rect2i {
    private int xPos;
    private int yPos;
    private int width;
    private int height;
    
    public Rect2i(final int integer1, final int integer2, final int integer3, final int integer4) {
        this.xPos = integer1;
        this.yPos = integer2;
        this.width = integer3;
        this.height = integer4;
    }
    
    public int getX() {
        return this.xPos;
    }
    
    public int getY() {
        return this.yPos;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public boolean contains(final int integer1, final int integer2) {
        return integer1 >= this.xPos && integer1 <= this.xPos + this.width && integer2 >= this.yPos && integer2 <= this.yPos + this.height;
    }
}

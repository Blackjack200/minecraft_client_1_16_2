package net.minecraft.network.chat;

public enum ChatType {
    CHAT((byte)0, false), 
    SYSTEM((byte)1, true), 
    GAME_INFO((byte)2, true);
    
    private final byte index;
    private final boolean interrupt;
    
    private ChatType(final byte byte3, final boolean boolean4) {
        this.index = byte3;
        this.interrupt = boolean4;
    }
    
    public byte getIndex() {
        return this.index;
    }
    
    public static ChatType getForIndex(final byte byte1) {
        for (final ChatType no5 : values()) {
            if (byte1 == no5.index) {
                return no5;
            }
        }
        return ChatType.CHAT;
    }
    
    public boolean shouldInterrupt() {
        return this.interrupt;
    }
}

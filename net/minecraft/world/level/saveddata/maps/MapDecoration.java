package net.minecraft.world.level.saveddata.maps;

import net.minecraft.util.Mth;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;

public class MapDecoration {
    private final Type type;
    private byte x;
    private byte y;
    private byte rot;
    private final Component name;
    
    public MapDecoration(final Type a, final byte byte2, final byte byte3, final byte byte4, @Nullable final Component nr) {
        this.type = a;
        this.x = byte2;
        this.y = byte3;
        this.rot = byte4;
        this.name = nr;
    }
    
    public byte getImage() {
        return this.type.getIcon();
    }
    
    public Type getType() {
        return this.type;
    }
    
    public byte getX() {
        return this.x;
    }
    
    public byte getY() {
        return this.y;
    }
    
    public byte getRot() {
        return this.rot;
    }
    
    public boolean renderOnFrame() {
        return this.type.isRenderedOnFrame();
    }
    
    @Nullable
    public Component getName() {
        return this.name;
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof MapDecoration)) {
            return false;
        }
        final MapDecoration cxr3 = (MapDecoration)object;
        return this.type == cxr3.type && this.rot == cxr3.rot && this.x == cxr3.x && this.y == cxr3.y && Objects.equals(this.name, cxr3.name);
    }
    
    public int hashCode() {
        int integer2 = this.type.getIcon();
        integer2 = 31 * integer2 + this.x;
        integer2 = 31 * integer2 + this.y;
        integer2 = 31 * integer2 + this.rot;
        integer2 = 31 * integer2 + Objects.hashCode(this.name);
        return integer2;
    }
    
    public enum Type {
        PLAYER(false), 
        FRAME(true), 
        RED_MARKER(false), 
        BLUE_MARKER(false), 
        TARGET_X(true), 
        TARGET_POINT(true), 
        PLAYER_OFF_MAP(false), 
        PLAYER_OFF_LIMITS(false), 
        MANSION(true, 5393476), 
        MONUMENT(true, 3830373), 
        BANNER_WHITE(true), 
        BANNER_ORANGE(true), 
        BANNER_MAGENTA(true), 
        BANNER_LIGHT_BLUE(true), 
        BANNER_YELLOW(true), 
        BANNER_LIME(true), 
        BANNER_PINK(true), 
        BANNER_GRAY(true), 
        BANNER_LIGHT_GRAY(true), 
        BANNER_CYAN(true), 
        BANNER_PURPLE(true), 
        BANNER_BLUE(true), 
        BANNER_BROWN(true), 
        BANNER_GREEN(true), 
        BANNER_RED(true), 
        BANNER_BLACK(true), 
        RED_X(true);
        
        private final byte icon;
        private final boolean renderedOnFrame;
        private final int mapColor;
        
        private Type(final boolean boolean3) {
            this(boolean3, -1);
        }
        
        private Type(final boolean boolean3, final int integer4) {
            this.icon = (byte)this.ordinal();
            this.renderedOnFrame = boolean3;
            this.mapColor = integer4;
        }
        
        public byte getIcon() {
            return this.icon;
        }
        
        public boolean isRenderedOnFrame() {
            return this.renderedOnFrame;
        }
        
        public boolean hasMapColor() {
            return this.mapColor >= 0;
        }
        
        public int getMapColor() {
            return this.mapColor;
        }
        
        public static Type byIcon(final byte byte1) {
            return values()[Mth.clamp(byte1, 0, values().length - 1)];
        }
    }
}

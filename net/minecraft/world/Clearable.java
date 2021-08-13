package net.minecraft.world;

import javax.annotation.Nullable;

public interface Clearable {
    void clearContent();
    
    default void tryClear(@Nullable final Object object) {
        if (object instanceof Clearable) {
            ((Clearable)object).clearContent();
        }
    }
}

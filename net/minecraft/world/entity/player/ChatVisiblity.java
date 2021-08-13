package net.minecraft.world.entity.player;

import java.util.Comparator;
import java.util.Arrays;
import net.minecraft.util.Mth;

public enum ChatVisiblity {
    FULL(0, "options.chat.visibility.full"), 
    SYSTEM(1, "options.chat.visibility.system"), 
    HIDDEN(2, "options.chat.visibility.hidden");
    
    private static final ChatVisiblity[] BY_ID;
    private final int id;
    private final String key;
    
    private ChatVisiblity(final int integer3, final String string4) {
        this.id = integer3;
        this.key = string4;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public static ChatVisiblity byId(final int integer) {
        return ChatVisiblity.BY_ID[Mth.positiveModulo(integer, ChatVisiblity.BY_ID.length)];
    }
    
    static {
        BY_ID = (ChatVisiblity[])Arrays.stream((Object[])values()).sorted(Comparator.comparingInt(ChatVisiblity::getId)).toArray(ChatVisiblity[]::new);
    }
}

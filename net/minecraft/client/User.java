package net.minecraft.client;

import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Arrays;
import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import com.mojang.util.UUIDTypeAdapter;
import com.mojang.authlib.GameProfile;

public class User {
    private final String name;
    private final String uuid;
    private final String accessToken;
    private final Type type;
    
    public User(final String string1, final String string2, final String string3, final String string4) {
        this.name = string1;
        this.uuid = string2;
        this.accessToken = string3;
        this.type = Type.byName(string4);
    }
    
    public String getSessionId() {
        return "token:" + this.accessToken + ":" + this.uuid;
    }
    
    public String getUuid() {
        return this.uuid;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getAccessToken() {
        return this.accessToken;
    }
    
    public GameProfile getGameProfile() {
        try {
            final UUID uUID2 = UUIDTypeAdapter.fromString(this.getUuid());
            return new GameProfile(uUID2, this.getName());
        }
        catch (IllegalArgumentException illegalArgumentException2) {
            return new GameProfile((UUID)null, this.getName());
        }
    }
    
    public enum Type {
        LEGACY("legacy"), 
        MOJANG("mojang");
        
        private static final Map<String, Type> BY_NAME;
        private final String name;
        
        private Type(final String string3) {
            this.name = string3;
        }
        
        @Nullable
        public static Type byName(final String string) {
            return (Type)Type.BY_NAME.get(string.toLowerCase(Locale.ROOT));
        }
        
        static {
            BY_NAME = (Map)Arrays.stream((Object[])values()).collect(Collectors.toMap(a -> a.name, Function.identity()));
        }
    }
}

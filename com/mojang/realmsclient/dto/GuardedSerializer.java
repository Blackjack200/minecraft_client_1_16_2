package com.mojang.realmsclient.dto;

import com.google.gson.Gson;

public class GuardedSerializer {
    private final Gson gson;
    
    public GuardedSerializer() {
        this.gson = new Gson();
    }
    
    public String toJson(final ReflectionBasedSerialization dgv) {
        return this.gson.toJson(dgv);
    }
    
    public <T extends ReflectionBasedSerialization> T fromJson(final String string, final Class<T> class2) {
        return (T)this.gson.fromJson(string, (Class)class2);
    }
}

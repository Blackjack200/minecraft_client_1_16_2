package com.mojang.realmsclient.util;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;

public class UploadTokenCache {
    private static final Long2ObjectMap<String> TOKEN_CACHE;
    
    public static String get(final long long1) {
        return (String)UploadTokenCache.TOKEN_CACHE.get(long1);
    }
    
    public static void invalidate(final long long1) {
        UploadTokenCache.TOKEN_CACHE.remove(long1);
    }
    
    public static void put(final long long1, final String string) {
        UploadTokenCache.TOKEN_CACHE.put(long1, string);
    }
    
    static {
        TOKEN_CACHE = (Long2ObjectMap)new Long2ObjectOpenHashMap();
    }
}

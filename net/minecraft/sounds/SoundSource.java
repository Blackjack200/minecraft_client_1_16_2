package net.minecraft.sounds;

import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Arrays;
import java.util.Map;

public enum SoundSource {
    MASTER("master"), 
    MUSIC("music"), 
    RECORDS("record"), 
    WEATHER("weather"), 
    BLOCKS("block"), 
    HOSTILE("hostile"), 
    NEUTRAL("neutral"), 
    PLAYERS("player"), 
    AMBIENT("ambient"), 
    VOICE("voice");
    
    private static final Map<String, SoundSource> BY_NAME;
    private final String name;
    
    private SoundSource(final String string3) {
        this.name = string3;
    }
    
    public String getName() {
        return this.name;
    }
    
    static {
        BY_NAME = (Map)Arrays.stream((Object[])values()).collect(Collectors.toMap(SoundSource::getName, Function.identity()));
    }
}

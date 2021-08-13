package net.minecraft.client.resources.sounds;

import javax.annotation.Nullable;
import java.util.List;

public class SoundEventRegistration {
    private final List<Sound> sounds;
    private final boolean replace;
    private final String subtitle;
    
    public SoundEventRegistration(final List<Sound> list, final boolean boolean2, final String string) {
        this.sounds = list;
        this.replace = boolean2;
        this.subtitle = string;
    }
    
    public List<Sound> getSounds() {
        return this.sounds;
    }
    
    public boolean isReplace() {
        return this.replace;
    }
    
    @Nullable
    public String getSubtitle() {
        return this.subtitle;
    }
}

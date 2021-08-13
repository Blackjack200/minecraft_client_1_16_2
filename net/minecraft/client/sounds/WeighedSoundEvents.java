package net.minecraft.client.sounds;

import java.util.Iterator;
import net.minecraft.network.chat.TranslatableComponent;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import java.util.Random;
import java.util.List;
import net.minecraft.client.resources.sounds.Sound;

public class WeighedSoundEvents implements Weighted<Sound> {
    private final List<Weighted<Sound>> list;
    private final Random random;
    private final ResourceLocation location;
    @Nullable
    private final Component subtitle;
    
    public WeighedSoundEvents(final ResourceLocation vk, @Nullable final String string) {
        this.list = (List<Weighted<Sound>>)Lists.newArrayList();
        this.random = new Random();
        this.location = vk;
        this.subtitle = ((string == null) ? null : new TranslatableComponent(string));
    }
    
    public int getWeight() {
        int integer2 = 0;
        for (final Weighted<Sound> eno4 : this.list) {
            integer2 += eno4.getWeight();
        }
        return integer2;
    }
    
    public Sound getSound() {
        final int integer2 = this.getWeight();
        if (this.list.isEmpty() || integer2 == 0) {
            return SoundManager.EMPTY_SOUND;
        }
        int integer3 = this.random.nextInt(integer2);
        for (final Weighted<Sound> eno5 : this.list) {
            integer3 -= eno5.getWeight();
            if (integer3 < 0) {
                return eno5.getSound();
            }
        }
        return SoundManager.EMPTY_SOUND;
    }
    
    public void addSound(final Weighted<Sound> eno) {
        this.list.add(eno);
    }
    
    @Nullable
    public Component getSubtitle() {
        return this.subtitle;
    }
    
    public void preloadIfRequired(final SoundEngine enj) {
        for (final Weighted<Sound> eno4 : this.list) {
            eno4.preloadIfRequired(enj);
        }
    }
}

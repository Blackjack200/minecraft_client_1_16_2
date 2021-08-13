package net.minecraft.client.sounds;

import java.lang.reflect.Type;
import net.minecraft.client.resources.sounds.SoundEventRegistrationSerializer;
import net.minecraft.network.chat.Component;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import net.minecraft.sounds.SoundSource;
import net.minecraft.client.Camera;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import java.util.Collection;
import javax.annotation.Nullable;
import net.minecraft.core.Registry;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TranslatableComponent;
import java.io.Reader;
import java.io.InputStream;
import java.util.List;
import java.util.Iterator;
import java.io.IOException;
import net.minecraft.util.GsonHelper;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.profiling.ProfilerFiller;
import com.google.common.collect.Maps;
import net.minecraft.client.Options;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.resources.sounds.SoundEventRegistration;
import java.util.Map;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;

public class SoundManager extends SimplePreparableReloadListener<Preparations> {
    public static final Sound EMPTY_SOUND;
    private static final Logger LOGGER;
    private static final Gson GSON;
    private static final TypeToken<Map<String, SoundEventRegistration>> SOUND_EVENT_REGISTRATION_TYPE;
    private final Map<ResourceLocation, WeighedSoundEvents> registry;
    private final SoundEngine soundEngine;
    
    public SoundManager(final ResourceManager acf, final Options dka) {
        this.registry = (Map<ResourceLocation, WeighedSoundEvents>)Maps.newHashMap();
        this.soundEngine = new SoundEngine(this, dka, acf);
    }
    
    @Override
    protected Preparations prepare(final ResourceManager acf, final ProfilerFiller ant) {
        final Preparations a4 = new Preparations();
        ant.startTick();
        for (final String string6 : acf.getNamespaces()) {
            ant.push(string6);
            try {
                final List<Resource> list7 = acf.getResources(new ResourceLocation(string6, "sounds.json"));
                for (final Resource ace9 : list7) {
                    ant.push(ace9.getSourceName());
                    try (final InputStream inputStream10 = ace9.getInputStream();
                         final Reader reader12 = (Reader)new InputStreamReader(inputStream10, StandardCharsets.UTF_8)) {
                        ant.push("parse");
                        final Map<String, SoundEventRegistration> map14 = GsonHelper.<Map<String, SoundEventRegistration>>fromJson(SoundManager.GSON, reader12, SoundManager.SOUND_EVENT_REGISTRATION_TYPE);
                        ant.popPush("register");
                        for (final Map.Entry<String, SoundEventRegistration> entry16 : map14.entrySet()) {
                            a4.handleRegistration(new ResourceLocation(string6, (String)entry16.getKey()), (SoundEventRegistration)entry16.getValue(), acf);
                        }
                        ant.pop();
                    }
                    catch (RuntimeException runtimeException10) {
                        SoundManager.LOGGER.warn("Invalid sounds.json in resourcepack: '{}'", ace9.getSourceName(), runtimeException10);
                    }
                    ant.pop();
                }
            }
            catch (IOException ex) {}
            ant.pop();
        }
        ant.endTick();
        return a4;
    }
    
    @Override
    protected void apply(final Preparations a, final ResourceManager acf, final ProfilerFiller ant) {
        a.apply(this.registry, this.soundEngine);
        for (final ResourceLocation vk6 : this.registry.keySet()) {
            final WeighedSoundEvents enn7 = (WeighedSoundEvents)this.registry.get(vk6);
            if (enn7.getSubtitle() instanceof TranslatableComponent) {
                final String string8 = ((TranslatableComponent)enn7.getSubtitle()).getKey();
                if (I18n.exists(string8)) {
                    continue;
                }
                SoundManager.LOGGER.debug("Missing subtitle {} for event: {}", string8, vk6);
            }
        }
        if (SoundManager.LOGGER.isDebugEnabled()) {
            for (final ResourceLocation vk6 : this.registry.keySet()) {
                if (!Registry.SOUND_EVENT.containsKey(vk6)) {
                    SoundManager.LOGGER.debug("Not having sound event for: {}", vk6);
                }
            }
        }
        this.soundEngine.reload();
    }
    
    private static boolean validateSoundResource(final Sound emi, final ResourceLocation vk, final ResourceManager acf) {
        final ResourceLocation vk2 = emi.getPath();
        if (!acf.hasResource(vk2)) {
            SoundManager.LOGGER.warn("File {} does not exist, cannot add it to event {}", vk2, vk);
            return false;
        }
        return true;
    }
    
    @Nullable
    public WeighedSoundEvents getSoundEvent(final ResourceLocation vk) {
        return (WeighedSoundEvents)this.registry.get(vk);
    }
    
    public Collection<ResourceLocation> getAvailableSounds() {
        return (Collection<ResourceLocation>)this.registry.keySet();
    }
    
    public void queueTickingSound(final TickableSoundInstance emm) {
        this.soundEngine.queueTickingSound(emm);
    }
    
    public void play(final SoundInstance eml) {
        this.soundEngine.play(eml);
    }
    
    public void playDelayed(final SoundInstance eml, final int integer) {
        this.soundEngine.playDelayed(eml, integer);
    }
    
    public void updateSource(final Camera djh) {
        this.soundEngine.updateSource(djh);
    }
    
    public void pause() {
        this.soundEngine.pause();
    }
    
    public void stop() {
        this.soundEngine.stopAll();
    }
    
    public void destroy() {
        this.soundEngine.destroy();
    }
    
    public void tick(final boolean boolean1) {
        this.soundEngine.tick(boolean1);
    }
    
    public void resume() {
        this.soundEngine.resume();
    }
    
    public void updateSourceVolume(final SoundSource adp, final float float2) {
        if (adp == SoundSource.MASTER && float2 <= 0.0f) {
            this.stop();
        }
        this.soundEngine.updateCategoryVolume(adp, float2);
    }
    
    public void stop(final SoundInstance eml) {
        this.soundEngine.stop(eml);
    }
    
    public boolean isActive(final SoundInstance eml) {
        return this.soundEngine.isActive(eml);
    }
    
    public void addListener(final SoundEventListener enl) {
        this.soundEngine.addEventListener(enl);
    }
    
    public void removeListener(final SoundEventListener enl) {
        this.soundEngine.removeEventListener(enl);
    }
    
    public void stop(@Nullable final ResourceLocation vk, @Nullable final SoundSource adp) {
        this.soundEngine.stop(vk, adp);
    }
    
    public String getDebugString() {
        return this.soundEngine.getDebugString();
    }
    
    static {
        EMPTY_SOUND = new Sound("meta:missing_sound", 1.0f, 1.0f, 1, Sound.Type.FILE, false, false, 16);
        LOGGER = LogManager.getLogger();
        GSON = new GsonBuilder().registerTypeHierarchyAdapter((Class)Component.class, new Component.Serializer()).registerTypeAdapter((Type)SoundEventRegistration.class, new SoundEventRegistrationSerializer()).create();
        SOUND_EVENT_REGISTRATION_TYPE = new TypeToken<Map<String, SoundEventRegistration>>() {};
    }
    
    public static class Preparations {
        private final Map<ResourceLocation, WeighedSoundEvents> registry;
        
        protected Preparations() {
            this.registry = (Map<ResourceLocation, WeighedSoundEvents>)Maps.newHashMap();
        }
        
        private void handleRegistration(final ResourceLocation vk, final SoundEventRegistration emj, final ResourceManager acf) {
            WeighedSoundEvents enn5 = (WeighedSoundEvents)this.registry.get(vk);
            final boolean boolean6 = enn5 == null;
            if (boolean6 || emj.isReplace()) {
                if (!boolean6) {
                    SoundManager.LOGGER.debug("Replaced sound event location {}", vk);
                }
                enn5 = new WeighedSoundEvents(vk, emj.getSubtitle());
                this.registry.put(vk, enn5);
            }
            for (final Sound emi8 : emj.getSounds()) {
                final ResourceLocation vk2 = emi8.getLocation();
                Weighted<Sound> eno10 = null;
                switch (emi8.getType()) {
                    case FILE: {
                        if (!validateSoundResource(emi8, vk, acf)) {
                            continue;
                        }
                        eno10 = emi8;
                        break;
                    }
                    case SOUND_EVENT: {
                        eno10 = new Weighted<Sound>() {
                            public int getWeight() {
                                final WeighedSoundEvents enn2 = (WeighedSoundEvents)Preparations.this.registry.get(vk2);
                                return (enn2 == null) ? 0 : enn2.getWeight();
                            }
                            
                            public Sound getSound() {
                                final WeighedSoundEvents enn2 = (WeighedSoundEvents)Preparations.this.registry.get(vk2);
                                if (enn2 == null) {
                                    return SoundManager.EMPTY_SOUND;
                                }
                                final Sound emi3 = enn2.getSound();
                                return new Sound(emi3.getLocation().toString(), emi3.getVolume() * emi8.getVolume(), emi3.getPitch() * emi8.getPitch(), emi8.getWeight(), Sound.Type.FILE, emi3.shouldStream() || emi8.shouldStream(), emi3.shouldPreload(), emi3.getAttenuationDistance());
                            }
                            
                            public void preloadIfRequired(final SoundEngine enj) {
                                final WeighedSoundEvents enn3 = (WeighedSoundEvents)Preparations.this.registry.get(vk2);
                                if (enn3 == null) {
                                    return;
                                }
                                enn3.preloadIfRequired(enj);
                            }
                        };
                        break;
                    }
                    default: {
                        throw new IllegalStateException(new StringBuilder().append("Unknown SoundEventRegistration type: ").append(emi8.getType()).toString());
                    }
                }
                enn5.addSound(eno10);
            }
        }
        
        public void apply(final Map<ResourceLocation, WeighedSoundEvents> map, final SoundEngine enj) {
            map.clear();
            for (final Map.Entry<ResourceLocation, WeighedSoundEvents> entry5 : this.registry.entrySet()) {
                map.put(entry5.getKey(), entry5.getValue());
                ((WeighedSoundEvents)entry5.getValue()).preloadIfRequired(enj);
            }
        }
    }
}

package net.minecraft.client.sounds;

import com.google.common.collect.Sets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.MarkerManager;
import com.mojang.blaze3d.audio.SoundBuffer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import java.util.stream.Stream;
import net.minecraft.util.Mth;
import java.util.concurrent.CompletableFuture;
import net.minecraft.world.phys.Vec3;
import java.util.function.Consumer;
import com.mojang.blaze3d.audio.Channel;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Iterator;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.Registry;
import com.google.common.collect.Lists;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import java.util.concurrent.Executor;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import java.util.List;
import net.minecraft.sounds.SoundSource;
import com.google.common.collect.Multimap;
import net.minecraft.client.resources.sounds.SoundInstance;
import java.util.Map;
import com.mojang.blaze3d.audio.Listener;
import com.mojang.blaze3d.audio.Library;
import net.minecraft.client.Options;
import net.minecraft.resources.ResourceLocation;
import java.util.Set;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;

public class SoundEngine {
    private static final Marker MARKER;
    private static final Logger LOGGER;
    private static final Set<ResourceLocation> ONLY_WARN_ONCE;
    private final SoundManager soundManager;
    private final Options options;
    private boolean loaded;
    private final Library library;
    private final Listener listener;
    private final SoundBufferLibrary soundBuffers;
    private final SoundEngineExecutor executor;
    private final ChannelAccess channelAccess;
    private int tickCount;
    private final Map<SoundInstance, ChannelAccess.ChannelHandle> instanceToChannel;
    private final Multimap<SoundSource, SoundInstance> instanceBySource;
    private final List<TickableSoundInstance> tickingSounds;
    private final Map<SoundInstance, Integer> queuedSounds;
    private final Map<SoundInstance, Integer> soundDeleteTime;
    private final List<SoundEventListener> listeners;
    private final List<TickableSoundInstance> queuedTickableSounds;
    private final List<Sound> preloadQueue;
    
    public SoundEngine(final SoundManager enm, final Options dka, final ResourceManager acf) {
        this.library = new Library();
        this.listener = this.library.getListener();
        this.executor = new SoundEngineExecutor();
        this.channelAccess = new ChannelAccess(this.library, (Executor)this.executor);
        this.instanceToChannel = (Map<SoundInstance, ChannelAccess.ChannelHandle>)Maps.newHashMap();
        this.instanceBySource = (Multimap<SoundSource, SoundInstance>)HashMultimap.create();
        this.tickingSounds = (List<TickableSoundInstance>)Lists.newArrayList();
        this.queuedSounds = (Map<SoundInstance, Integer>)Maps.newHashMap();
        this.soundDeleteTime = (Map<SoundInstance, Integer>)Maps.newHashMap();
        this.listeners = (List<SoundEventListener>)Lists.newArrayList();
        this.queuedTickableSounds = (List<TickableSoundInstance>)Lists.newArrayList();
        this.preloadQueue = (List<Sound>)Lists.newArrayList();
        this.soundManager = enm;
        this.options = dka;
        this.soundBuffers = new SoundBufferLibrary(acf);
    }
    
    public void reload() {
        SoundEngine.ONLY_WARN_ONCE.clear();
        for (final SoundEvent adn3 : Registry.SOUND_EVENT) {
            final ResourceLocation vk4 = adn3.getLocation();
            if (this.soundManager.getSoundEvent(vk4) == null) {
                SoundEngine.LOGGER.warn("Missing sound for event: {}", Registry.SOUND_EVENT.getKey(adn3));
                SoundEngine.ONLY_WARN_ONCE.add(vk4);
            }
        }
        this.destroy();
        this.loadLibrary();
    }
    
    private synchronized void loadLibrary() {
        if (this.loaded) {
            return;
        }
        try {
            this.library.init();
            this.listener.reset();
            this.listener.setGain(this.options.getSoundSourceVolume(SoundSource.MASTER));
            this.soundBuffers.preload((Collection<Sound>)this.preloadQueue).thenRun(this.preloadQueue::clear);
            this.loaded = true;
            SoundEngine.LOGGER.info(SoundEngine.MARKER, "Sound engine started");
        }
        catch (RuntimeException runtimeException2) {
            SoundEngine.LOGGER.error(SoundEngine.MARKER, "Error starting SoundSystem. Turning off sounds & music", (Throwable)runtimeException2);
        }
    }
    
    private float getVolume(@Nullable final SoundSource adp) {
        if (adp == null || adp == SoundSource.MASTER) {
            return 1.0f;
        }
        return this.options.getSoundSourceVolume(adp);
    }
    
    public void updateCategoryVolume(final SoundSource adp, final float float2) {
        if (!this.loaded) {
            return;
        }
        if (adp == SoundSource.MASTER) {
            this.listener.setGain(float2);
            return;
        }
        this.instanceToChannel.forEach((eml, a) -> {
            final float float4 = this.calculateVolume(eml);
            a.execute((Consumer<Channel>)(ddr -> {
                if (float4 <= 0.0f) {
                    ddr.stop();
                }
                else {
                    ddr.setVolume(float4);
                }
            }));
        });
    }
    
    public void destroy() {
        if (this.loaded) {
            this.stopAll();
            this.soundBuffers.clear();
            this.library.cleanup();
            this.loaded = false;
        }
    }
    
    public void stop(final SoundInstance eml) {
        if (this.loaded) {
            final ChannelAccess.ChannelHandle a3 = (ChannelAccess.ChannelHandle)this.instanceToChannel.get(eml);
            if (a3 != null) {
                a3.execute((Consumer<Channel>)Channel::stop);
            }
        }
    }
    
    public void stopAll() {
        if (this.loaded) {
            this.executor.flush();
            this.instanceToChannel.values().forEach(a -> a.execute((Consumer<Channel>)Channel::stop));
            this.instanceToChannel.clear();
            this.channelAccess.clear();
            this.queuedSounds.clear();
            this.tickingSounds.clear();
            this.instanceBySource.clear();
            this.soundDeleteTime.clear();
            this.queuedTickableSounds.clear();
        }
    }
    
    public void addEventListener(final SoundEventListener enl) {
        this.listeners.add(enl);
    }
    
    public void removeEventListener(final SoundEventListener enl) {
        this.listeners.remove(enl);
    }
    
    public void tick(final boolean boolean1) {
        if (!boolean1) {
            this.tickNonPaused();
        }
        this.channelAccess.scheduleTick();
    }
    
    private void tickNonPaused() {
        ++this.tickCount;
        this.queuedTickableSounds.stream().filter(SoundInstance::canPlaySound).forEach(this::play);
        this.queuedTickableSounds.clear();
        for (final TickableSoundInstance emm3 : this.tickingSounds) {
            if (!emm3.canPlaySound()) {
                this.stop(emm3);
            }
            emm3.tick();
            if (emm3.isStopped()) {
                this.stop(emm3);
            }
            else {
                final float float4 = this.calculateVolume(emm3);
                final float float5 = this.calculatePitch(emm3);
                final Vec3 dck6 = new Vec3(emm3.getX(), emm3.getY(), emm3.getZ());
                final ChannelAccess.ChannelHandle a7 = (ChannelAccess.ChannelHandle)this.instanceToChannel.get(emm3);
                if (a7 == null) {
                    continue;
                }
                a7.execute((Consumer<Channel>)(ddr -> {
                    ddr.setVolume(float4);
                    ddr.setPitch(float5);
                    ddr.setSelfPosition(dck6);
                }));
            }
        }
        final Iterator<Map.Entry<SoundInstance, ChannelAccess.ChannelHandle>> iterator2 = (Iterator<Map.Entry<SoundInstance, ChannelAccess.ChannelHandle>>)this.instanceToChannel.entrySet().iterator();
        while (iterator2.hasNext()) {
            final Map.Entry<SoundInstance, ChannelAccess.ChannelHandle> entry3 = (Map.Entry<SoundInstance, ChannelAccess.ChannelHandle>)iterator2.next();
            final ChannelAccess.ChannelHandle a8 = (ChannelAccess.ChannelHandle)entry3.getValue();
            final SoundInstance eml5 = (SoundInstance)entry3.getKey();
            final float float6 = this.options.getSoundSourceVolume(eml5.getSource());
            if (float6 <= 0.0f) {
                a8.execute((Consumer<Channel>)Channel::stop);
                iterator2.remove();
            }
            else {
                if (!a8.isStopped()) {
                    continue;
                }
                final int integer7 = (int)this.soundDeleteTime.get(eml5);
                if (integer7 > this.tickCount) {
                    continue;
                }
                if (shouldLoopManually(eml5)) {
                    this.queuedSounds.put(eml5, (this.tickCount + eml5.getDelay()));
                }
                iterator2.remove();
                SoundEngine.LOGGER.debug(SoundEngine.MARKER, "Removed channel {} because it's not playing anymore", a8);
                this.soundDeleteTime.remove(eml5);
                try {
                    this.instanceBySource.remove(eml5.getSource(), eml5);
                }
                catch (RuntimeException ex) {}
                if (!(eml5 instanceof TickableSoundInstance)) {
                    continue;
                }
                this.tickingSounds.remove(eml5);
            }
        }
        final Iterator<Map.Entry<SoundInstance, Integer>> iterator3 = (Iterator<Map.Entry<SoundInstance, Integer>>)this.queuedSounds.entrySet().iterator();
        while (iterator3.hasNext()) {
            final Map.Entry<SoundInstance, Integer> entry4 = (Map.Entry<SoundInstance, Integer>)iterator3.next();
            if (this.tickCount >= (int)entry4.getValue()) {
                final SoundInstance eml5 = (SoundInstance)entry4.getKey();
                if (eml5 instanceof TickableSoundInstance) {
                    ((TickableSoundInstance)eml5).tick();
                }
                this.play(eml5);
                iterator3.remove();
            }
        }
    }
    
    private static boolean requiresManualLooping(final SoundInstance eml) {
        return eml.getDelay() > 0;
    }
    
    private static boolean shouldLoopManually(final SoundInstance eml) {
        return eml.isLooping() && requiresManualLooping(eml);
    }
    
    private static boolean shouldLoopAutomatically(final SoundInstance eml) {
        return eml.isLooping() && !requiresManualLooping(eml);
    }
    
    public boolean isActive(final SoundInstance eml) {
        return this.loaded && ((this.soundDeleteTime.containsKey(eml) && (int)this.soundDeleteTime.get(eml) <= this.tickCount) || this.instanceToChannel.containsKey(eml));
    }
    
    public void play(final SoundInstance eml) {
        if (!this.loaded) {
            return;
        }
        if (!eml.canPlaySound()) {
            return;
        }
        final WeighedSoundEvents enn3 = eml.resolve(this.soundManager);
        final ResourceLocation vk4 = eml.getLocation();
        if (enn3 == null) {
            if (SoundEngine.ONLY_WARN_ONCE.add(vk4)) {
                SoundEngine.LOGGER.warn(SoundEngine.MARKER, "Unable to play unknown soundEvent: {}", vk4);
            }
            return;
        }
        final Sound emi5 = eml.getSound();
        if (emi5 == SoundManager.EMPTY_SOUND) {
            if (SoundEngine.ONLY_WARN_ONCE.add(vk4)) {
                SoundEngine.LOGGER.warn(SoundEngine.MARKER, "Unable to play empty soundEvent: {}", vk4);
            }
            return;
        }
        final float float6 = eml.getVolume();
        final float float7 = Math.max(float6, 1.0f) * emi5.getAttenuationDistance();
        final SoundSource adp8 = eml.getSource();
        final float float8 = this.calculateVolume(eml);
        final float float9 = this.calculatePitch(eml);
        final SoundInstance.Attenuation a11 = eml.getAttenuation();
        final boolean boolean12 = eml.isRelative();
        if (float8 == 0.0f && !eml.canStartSilent()) {
            SoundEngine.LOGGER.debug(SoundEngine.MARKER, "Skipped playing sound {}, volume was zero.", emi5.getLocation());
            return;
        }
        final Vec3 dck13 = new Vec3(eml.getX(), eml.getY(), eml.getZ());
        if (!this.listeners.isEmpty()) {
            final boolean boolean13 = boolean12 || a11 == SoundInstance.Attenuation.NONE || this.listener.getListenerPosition().distanceToSqr(dck13) < float7 * float7;
            if (boolean13) {
                for (final SoundEventListener enl16 : this.listeners) {
                    enl16.onPlaySound(eml, enn3);
                }
            }
            else {
                SoundEngine.LOGGER.debug(SoundEngine.MARKER, "Did not notify listeners of soundEvent: {}, it is too far away to hear", vk4);
            }
        }
        if (this.listener.getGain() <= 0.0f) {
            SoundEngine.LOGGER.debug(SoundEngine.MARKER, "Skipped playing soundEvent: {}, master volume was zero", vk4);
            return;
        }
        final boolean boolean13 = shouldLoopAutomatically(eml);
        final boolean boolean14 = emi5.shouldStream();
        final CompletableFuture<ChannelAccess.ChannelHandle> completableFuture16 = this.channelAccess.createHandle(emi5.shouldStream() ? Library.Pool.STREAMING : Library.Pool.STATIC);
        final ChannelAccess.ChannelHandle a12 = (ChannelAccess.ChannelHandle)completableFuture16.join();
        if (a12 == null) {
            SoundEngine.LOGGER.warn("Failed to create new sound handle");
            return;
        }
        SoundEngine.LOGGER.debug(SoundEngine.MARKER, "Playing sound {} for event {}", emi5.getLocation(), vk4);
        this.soundDeleteTime.put(eml, (this.tickCount + 20));
        this.instanceToChannel.put(eml, a12);
        this.instanceBySource.put(adp8, eml);
        a12.execute((Consumer<Channel>)(ddr -> {
            ddr.setPitch(float9);
            ddr.setVolume(float8);
            if (a11 == SoundInstance.Attenuation.LINEAR) {
                ddr.linearAttenuation(float7);
            }
            else {
                ddr.disableAttenuation();
            }
            ddr.setLooping(boolean13 && !boolean14);
            ddr.setSelfPosition(dck13);
            ddr.setRelative(boolean12);
        }));
        if (!boolean14) {
            this.soundBuffers.getCompleteBuffer(emi5.getPath()).thenAccept(ddw -> a12.execute((Consumer<Channel>)(ddr -> {
                ddr.attachStaticBuffer(ddw);
                ddr.play();
            })));
        }
        else {
            this.soundBuffers.getStream(emi5.getPath(), boolean13).thenAccept(ene -> a12.execute((Consumer<Channel>)(ddr -> {
                ddr.attachBufferStream(ene);
                ddr.play();
            })));
        }
        if (eml instanceof TickableSoundInstance) {
            this.tickingSounds.add(eml);
        }
    }
    
    public void queueTickingSound(final TickableSoundInstance emm) {
        this.queuedTickableSounds.add(emm);
    }
    
    public void requestPreload(final Sound emi) {
        this.preloadQueue.add(emi);
    }
    
    private float calculatePitch(final SoundInstance eml) {
        return Mth.clamp(eml.getPitch(), 0.5f, 2.0f);
    }
    
    private float calculateVolume(final SoundInstance eml) {
        return Mth.clamp(eml.getVolume() * this.getVolume(eml.getSource()), 0.0f, 1.0f);
    }
    
    public void pause() {
        if (this.loaded) {
            this.channelAccess.executeOnChannels((Consumer<Stream<Channel>>)(stream -> stream.forEach(Channel::pause)));
        }
    }
    
    public void resume() {
        if (this.loaded) {
            this.channelAccess.executeOnChannels((Consumer<Stream<Channel>>)(stream -> stream.forEach(Channel::unpause)));
        }
    }
    
    public void playDelayed(final SoundInstance eml, final int integer) {
        this.queuedSounds.put(eml, (this.tickCount + integer));
    }
    
    public void updateSource(final Camera djh) {
        if (!this.loaded || !djh.isInitialized()) {
            return;
        }
        final Vec3 dck3 = djh.getPosition();
        final Vector3f g4 = djh.getLookVector();
        final Vector3f g5 = djh.getUpVector();
        this.executor.execute(() -> {
            this.listener.setListenerPosition(dck3);
            this.listener.setListenerOrientation(g4, g5);
        });
    }
    
    public void stop(@Nullable final ResourceLocation vk, @Nullable final SoundSource adp) {
        if (adp != null) {
            for (final SoundInstance eml5 : this.instanceBySource.get(adp)) {
                if (vk == null || eml5.getLocation().equals(vk)) {
                    this.stop(eml5);
                }
            }
        }
        else if (vk == null) {
            this.stopAll();
        }
        else {
            for (final SoundInstance eml5 : this.instanceToChannel.keySet()) {
                if (eml5.getLocation().equals(vk)) {
                    this.stop(eml5);
                }
            }
        }
    }
    
    public String getDebugString() {
        return this.library.getDebugString();
    }
    
    static {
        MARKER = MarkerManager.getMarker("SOUNDS");
        LOGGER = LogManager.getLogger();
        ONLY_WARN_ONCE = (Set)Sets.newHashSet();
    }
}

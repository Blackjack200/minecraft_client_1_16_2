package net.minecraft.client.sounds;

import java.nio.ByteBuffer;
import java.io.InputStream;
import net.minecraft.server.packs.resources.Resource;
import java.io.IOException;
import java.util.concurrent.CompletionException;
import com.mojang.blaze3d.audio.OggAudioStream;
import net.minecraft.client.resources.sounds.Sound;
import java.util.Collection;
import net.minecraft.Util;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.audio.SoundBuffer;
import java.util.concurrent.CompletableFuture;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import net.minecraft.server.packs.resources.ResourceManager;

public class SoundBufferLibrary {
    private final ResourceManager resourceManager;
    private final Map<ResourceLocation, CompletableFuture<SoundBuffer>> cache;
    
    public SoundBufferLibrary(final ResourceManager acf) {
        this.cache = (Map<ResourceLocation, CompletableFuture<SoundBuffer>>)Maps.newHashMap();
        this.resourceManager = acf;
    }
    
    public CompletableFuture<SoundBuffer> getCompleteBuffer(final ResourceLocation vk) {
        return (CompletableFuture<SoundBuffer>)this.cache.computeIfAbsent(vk, vk -> CompletableFuture.supplyAsync(() -> {
            try (final Resource ace3 = this.resourceManager.getResource(vk);
                 final InputStream inputStream5 = ace3.getInputStream();
                 final OggAudioStream ddu7 = new OggAudioStream(inputStream5)) {
                final ByteBuffer byteBuffer9 = ddu7.readAll();
                return new SoundBuffer(byteBuffer9, ddu7.getFormat());
            }
            catch (IOException iOException3) {
                throw new CompletionException((Throwable)iOException3);
            }
        }, Util.backgroundExecutor()));
    }
    
    public CompletableFuture<AudioStream> getStream(final ResourceLocation vk, final boolean boolean2) {
        return (CompletableFuture<AudioStream>)CompletableFuture.supplyAsync(() -> {
            try {
                final Resource ace4 = this.resourceManager.getResource(vk);
                final InputStream inputStream5 = ace4.getInputStream();
                return boolean2 ? new LoopingAudioStream(OggAudioStream::new, inputStream5) : new OggAudioStream(inputStream5);
            }
            catch (IOException iOException4) {
                throw new CompletionException((Throwable)iOException4);
            }
        }, Util.backgroundExecutor());
    }
    
    public void clear() {
        this.cache.values().forEach(completableFuture -> completableFuture.thenAccept(SoundBuffer::discardAlBuffer));
        this.cache.clear();
    }
    
    public CompletableFuture<?> preload(final Collection<Sound> collection) {
        return CompletableFuture.allOf((CompletableFuture[])collection.stream().map(emi -> this.getCompleteBuffer(emi.getPath())).toArray(CompletableFuture[]::new));
    }
}

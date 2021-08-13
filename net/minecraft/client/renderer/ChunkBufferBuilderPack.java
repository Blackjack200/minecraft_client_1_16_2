package net.minecraft.client.renderer;

import java.util.stream.Collectors;
import com.mojang.blaze3d.vertex.BufferBuilder;
import java.util.Map;

public class ChunkBufferBuilderPack {
    private final Map<RenderType, BufferBuilder> builders;
    
    public ChunkBufferBuilderPack() {
        this.builders = (Map<RenderType, BufferBuilder>)RenderType.chunkBufferLayers().stream().collect(Collectors.toMap(eag -> eag, eag -> new BufferBuilder(eag.bufferSize())));
    }
    
    public BufferBuilder builder(final RenderType eag) {
        return (BufferBuilder)this.builders.get(eag);
    }
    
    public void clearAll() {
        this.builders.values().forEach(BufferBuilder::clear);
    }
    
    public void discardAll() {
        this.builders.values().forEach(BufferBuilder::discard);
    }
}

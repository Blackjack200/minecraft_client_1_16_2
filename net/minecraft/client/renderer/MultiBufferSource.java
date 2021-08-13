package net.minecraft.client.renderer;

import java.util.Iterator;
import java.util.Objects;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.Optional;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.BufferBuilder;

public interface MultiBufferSource {
    default BufferSource immediate(final BufferBuilder dfe) {
        return immediateWithBuffers((Map<RenderType, BufferBuilder>)ImmutableMap.of(), dfe);
    }
    
    default BufferSource immediateWithBuffers(final Map<RenderType, BufferBuilder> map, final BufferBuilder dfe) {
        return new BufferSource(dfe, map);
    }
    
    VertexConsumer getBuffer(final RenderType eag);
    
    public static class BufferSource implements MultiBufferSource {
        protected final BufferBuilder builder;
        protected final Map<RenderType, BufferBuilder> fixedBuffers;
        protected Optional<RenderType> lastState;
        protected final Set<BufferBuilder> startedBuffers;
        
        protected BufferSource(final BufferBuilder dfe, final Map<RenderType, BufferBuilder> map) {
            this.lastState = (Optional<RenderType>)Optional.empty();
            this.startedBuffers = (Set<BufferBuilder>)Sets.newHashSet();
            this.builder = dfe;
            this.fixedBuffers = map;
        }
        
        public VertexConsumer getBuffer(final RenderType eag) {
            final Optional<RenderType> optional3 = eag.asOptional();
            final BufferBuilder dfe4 = this.getBuilderRaw(eag);
            if (!Objects.equals(this.lastState, optional3)) {
                if (this.lastState.isPresent()) {
                    final RenderType eag2 = (RenderType)this.lastState.get();
                    if (!this.fixedBuffers.containsKey(eag2)) {
                        this.endBatch(eag2);
                    }
                }
                if (this.startedBuffers.add(dfe4)) {
                    dfe4.begin(eag.mode(), eag.format());
                }
                this.lastState = optional3;
            }
            return dfe4;
        }
        
        private BufferBuilder getBuilderRaw(final RenderType eag) {
            return (BufferBuilder)this.fixedBuffers.getOrDefault(eag, this.builder);
        }
        
        public void endBatch() {
            this.lastState.ifPresent(eag -> {
                final VertexConsumer dfn3 = this.getBuffer(eag);
                if (dfn3 == this.builder) {
                    this.endBatch(eag);
                }
            });
            for (final RenderType eag3 : this.fixedBuffers.keySet()) {
                this.endBatch(eag3);
            }
        }
        
        public void endBatch(final RenderType eag) {
            final BufferBuilder dfe3 = this.getBuilderRaw(eag);
            final boolean boolean4 = Objects.equals(this.lastState, eag.asOptional());
            if (!boolean4 && dfe3 == this.builder) {
                return;
            }
            if (!this.startedBuffers.remove(dfe3)) {
                return;
            }
            eag.end(dfe3, 0, 0, 0);
            if (boolean4) {
                this.lastState = (Optional<RenderType>)Optional.empty();
            }
        }
    }
}

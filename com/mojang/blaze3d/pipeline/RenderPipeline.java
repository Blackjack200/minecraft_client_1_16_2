package com.mojang.blaze3d.pipeline;

import com.google.common.collect.ImmutableList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.List;

public class RenderPipeline {
    private final List<ConcurrentLinkedQueue<RenderCall>> renderCalls;
    private volatile int recordingBuffer;
    private volatile int processedBuffer;
    private volatile int renderingBuffer;
    
    public RenderPipeline() {
        this.renderCalls = (List<ConcurrentLinkedQueue<RenderCall>>)ImmutableList.of(new ConcurrentLinkedQueue(), new ConcurrentLinkedQueue(), new ConcurrentLinkedQueue(), new ConcurrentLinkedQueue());
        final int n = this.renderingBuffer + 1;
        this.processedBuffer = n;
        this.recordingBuffer = n;
    }
}

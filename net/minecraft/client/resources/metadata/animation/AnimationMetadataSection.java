package net.minecraft.client.resources.metadata.animation;

import com.google.common.collect.Lists;
import java.util.Iterator;
import com.google.common.collect.Sets;
import java.util.Set;
import com.mojang.datafixers.util.Pair;
import java.util.List;

public class AnimationMetadataSection {
    public static final AnimationMetadataSectionSerializer SERIALIZER;
    public static final AnimationMetadataSection EMPTY;
    private final List<AnimationFrame> frames;
    private final int frameWidth;
    private final int frameHeight;
    private final int defaultFrameTime;
    private final boolean interpolatedFrames;
    
    public AnimationMetadataSection(final List<AnimationFrame> list, final int integer2, final int integer3, final int integer4, final boolean boolean5) {
        this.frames = list;
        this.frameWidth = integer2;
        this.frameHeight = integer3;
        this.defaultFrameTime = integer4;
        this.interpolatedFrames = boolean5;
    }
    
    private static boolean isDivisionInteger(final int integer1, final int integer2) {
        return integer1 / integer2 * integer2 == integer1;
    }
    
    public Pair<Integer, Integer> getFrameSize(final int integer1, final int integer2) {
        final Pair<Integer, Integer> pair4 = this.calculateFrameSize(integer1, integer2);
        final int integer3 = (int)pair4.getFirst();
        final int integer4 = (int)pair4.getSecond();
        if (!isDivisionInteger(integer1, integer3) || !isDivisionInteger(integer2, integer4)) {
            throw new IllegalArgumentException(String.format("Image size %s,%s is not multiply of frame size %s,%s", new Object[] { integer1, integer2, integer3, integer4 }));
        }
        return pair4;
    }
    
    private Pair<Integer, Integer> calculateFrameSize(final int integer1, final int integer2) {
        if (this.frameWidth != -1) {
            if (this.frameHeight != -1) {
                return (Pair<Integer, Integer>)Pair.of(this.frameWidth, this.frameHeight);
            }
            return (Pair<Integer, Integer>)Pair.of(this.frameWidth, integer2);
        }
        else {
            if (this.frameHeight != -1) {
                return (Pair<Integer, Integer>)Pair.of(integer1, this.frameHeight);
            }
            final int integer3 = Math.min(integer1, integer2);
            return (Pair<Integer, Integer>)Pair.of(integer3, integer3);
        }
    }
    
    public int getFrameHeight(final int integer) {
        return (this.frameHeight == -1) ? integer : this.frameHeight;
    }
    
    public int getFrameWidth(final int integer) {
        return (this.frameWidth == -1) ? integer : this.frameWidth;
    }
    
    public int getFrameCount() {
        return this.frames.size();
    }
    
    public int getDefaultFrameTime() {
        return this.defaultFrameTime;
    }
    
    public boolean isInterpolatedFrames() {
        return this.interpolatedFrames;
    }
    
    private AnimationFrame getFrame(final int integer) {
        return (AnimationFrame)this.frames.get(integer);
    }
    
    public int getFrameTime(final int integer) {
        final AnimationFrame ekt3 = this.getFrame(integer);
        if (ekt3.isTimeUnknown()) {
            return this.defaultFrameTime;
        }
        return ekt3.getTime();
    }
    
    public int getFrameIndex(final int integer) {
        return ((AnimationFrame)this.frames.get(integer)).getIndex();
    }
    
    public Set<Integer> getUniqueFrameIndices() {
        final Set<Integer> set2 = (Set<Integer>)Sets.newHashSet();
        for (final AnimationFrame ekt4 : this.frames) {
            set2.add(ekt4.getIndex());
        }
        return set2;
    }
    
    static {
        SERIALIZER = new AnimationMetadataSectionSerializer();
        EMPTY = new AnimationMetadataSection((List)Lists.newArrayList(), -1, -1, 1, false) {
            @Override
            public Pair<Integer, Integer> getFrameSize(final int integer1, final int integer2) {
                return (Pair<Integer, Integer>)Pair.of(integer1, integer2);
            }
        };
    }
}

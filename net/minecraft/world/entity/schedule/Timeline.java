package net.minecraft.world.entity.schedule;

import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import java.util.Collection;
import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import com.google.common.collect.Lists;
import java.util.List;

public class Timeline {
    private final List<Keyframe> keyframes;
    private int previousIndex;
    
    public Timeline() {
        this.keyframes = (List<Keyframe>)Lists.newArrayList();
    }
    
    public Timeline addKeyframe(final int integer, final float float2) {
        this.keyframes.add(new Keyframe(integer, float2));
        this.sortAndDeduplicateKeyframes();
        return this;
    }
    
    private void sortAndDeduplicateKeyframes() {
        final Int2ObjectSortedMap<Keyframe> int2ObjectSortedMap2 = (Int2ObjectSortedMap<Keyframe>)new Int2ObjectAVLTreeMap();
        this.keyframes.forEach(bhd -> {
            final Keyframe keyframe = (Keyframe)int2ObjectSortedMap2.put(bhd.getTimeStamp(), bhd);
        });
        this.keyframes.clear();
        this.keyframes.addAll((Collection)int2ObjectSortedMap2.values());
        this.previousIndex = 0;
    }
    
    public float getValueAt(final int integer) {
        if (this.keyframes.size() <= 0) {
            return 0.0f;
        }
        final Keyframe bhd3 = (Keyframe)this.keyframes.get(this.previousIndex);
        final Keyframe bhd4 = (Keyframe)this.keyframes.get(this.keyframes.size() - 1);
        final boolean boolean5 = integer < bhd3.getTimeStamp();
        final int integer2 = boolean5 ? 0 : this.previousIndex;
        float float7 = boolean5 ? bhd4.getValue() : bhd3.getValue();
        for (int integer3 = integer2; integer3 < this.keyframes.size(); ++integer3) {
            final Keyframe bhd5 = (Keyframe)this.keyframes.get(integer3);
            if (bhd5.getTimeStamp() > integer) {
                break;
            }
            this.previousIndex = integer3;
            float7 = bhd5.getValue();
        }
        return float7;
    }
}

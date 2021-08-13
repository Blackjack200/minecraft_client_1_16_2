package net.minecraft.world.item;

import java.util.Iterator;
import net.minecraft.util.Mth;
import com.google.common.collect.Maps;
import java.util.Map;

public class ItemCooldowns {
    private final Map<Item, CooldownInstance> cooldowns;
    private int tickCount;
    
    public ItemCooldowns() {
        this.cooldowns = (Map<Item, CooldownInstance>)Maps.newHashMap();
    }
    
    public boolean isOnCooldown(final Item blu) {
        return this.getCooldownPercent(blu, 0.0f) > 0.0f;
    }
    
    public float getCooldownPercent(final Item blu, final float float2) {
        final CooldownInstance a4 = (CooldownInstance)this.cooldowns.get(blu);
        if (a4 != null) {
            final float float3 = (float)(a4.endTime - a4.startTime);
            final float float4 = a4.endTime - (this.tickCount + float2);
            return Mth.clamp(float4 / float3, 0.0f, 1.0f);
        }
        return 0.0f;
    }
    
    public void tick() {
        ++this.tickCount;
        if (!this.cooldowns.isEmpty()) {
            final Iterator<Map.Entry<Item, CooldownInstance>> iterator2 = (Iterator<Map.Entry<Item, CooldownInstance>>)this.cooldowns.entrySet().iterator();
            while (iterator2.hasNext()) {
                final Map.Entry<Item, CooldownInstance> entry3 = (Map.Entry<Item, CooldownInstance>)iterator2.next();
                if (((CooldownInstance)entry3.getValue()).endTime <= this.tickCount) {
                    iterator2.remove();
                    this.onCooldownEnded((Item)entry3.getKey());
                }
            }
        }
    }
    
    public void addCooldown(final Item blu, final int integer) {
        this.cooldowns.put(blu, new CooldownInstance(this.tickCount, this.tickCount + integer));
        this.onCooldownStarted(blu, integer);
    }
    
    public void removeCooldown(final Item blu) {
        this.cooldowns.remove(blu);
        this.onCooldownEnded(blu);
    }
    
    protected void onCooldownStarted(final Item blu, final int integer) {
    }
    
    protected void onCooldownEnded(final Item blu) {
    }
    
    class CooldownInstance {
        private final int startTime;
        private final int endTime;
        
        private CooldownInstance(final int integer2, final int integer3) {
            this.startTime = integer2;
            this.endTime = integer3;
        }
    }
}

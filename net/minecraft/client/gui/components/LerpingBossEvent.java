package net.minecraft.client.gui.components;

import net.minecraft.util.Mth;
import net.minecraft.Util;
import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
import net.minecraft.world.BossEvent;

public class LerpingBossEvent extends BossEvent {
    protected float targetPercent;
    protected long setTime;
    
    public LerpingBossEvent(final ClientboundBossEventPacket oz) {
        super(oz.getId(), oz.getName(), oz.getColor(), oz.getOverlay());
        this.targetPercent = oz.getPercent();
        this.percent = oz.getPercent();
        this.setTime = Util.getMillis();
        this.setDarkenScreen(oz.shouldDarkenScreen());
        this.setPlayBossMusic(oz.shouldPlayMusic());
        this.setCreateWorldFog(oz.shouldCreateWorldFog());
    }
    
    @Override
    public void setPercent(final float float1) {
        this.percent = this.getPercent();
        this.targetPercent = float1;
        this.setTime = Util.getMillis();
    }
    
    @Override
    public float getPercent() {
        final long long2 = Util.getMillis() - this.setTime;
        final float float4 = Mth.clamp(long2 / 100.0f, 0.0f, 1.0f);
        return Mth.lerp(float4, this.percent, this.targetPercent);
    }
    
    public void update(final ClientboundBossEventPacket oz) {
        switch (oz.getOperation()) {
            case UPDATE_NAME: {
                this.setName(oz.getName());
                break;
            }
            case UPDATE_PCT: {
                this.setPercent(oz.getPercent());
                break;
            }
            case UPDATE_STYLE: {
                this.setColor(oz.getColor());
                this.setOverlay(oz.getOverlay());
                break;
            }
            case UPDATE_PROPERTIES: {
                this.setDarkenScreen(oz.shouldDarkenScreen());
                this.setPlayBossMusic(oz.shouldPlayMusic());
                break;
            }
        }
    }
}

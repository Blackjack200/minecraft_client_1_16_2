package net.minecraft.client;

import net.minecraft.util.FrameTimer;
import com.mojang.bridge.game.PerformanceMetrics;
import net.minecraft.client.multiplayer.ClientLevel;
import com.mojang.bridge.game.GameSession;
import com.mojang.bridge.game.Language;
import net.minecraft.SharedConstants;
import com.mojang.bridge.game.GameVersion;
import com.mojang.bridge.Bridge;
import com.mojang.bridge.launcher.SessionEventListener;
import javax.annotation.Nullable;
import com.mojang.bridge.launcher.Launcher;
import com.mojang.bridge.game.RunningGame;

public class Game implements RunningGame {
    private final Minecraft minecraft;
    @Nullable
    private final Launcher launcher;
    private SessionEventListener listener;
    
    public Game(final Minecraft djw) {
        this.listener = SessionEventListener.NONE;
        this.minecraft = djw;
        this.launcher = Bridge.getLauncher();
        if (this.launcher != null) {
            this.launcher.registerGame((RunningGame)this);
        }
    }
    
    public GameVersion getVersion() {
        return SharedConstants.getCurrentVersion();
    }
    
    public Language getSelectedLanguage() {
        return (Language)this.minecraft.getLanguageManager().getSelected();
    }
    
    @Nullable
    public GameSession getCurrentSession() {
        final ClientLevel dwl2 = this.minecraft.level;
        return (GameSession)((dwl2 == null) ? null : new Session(dwl2, this.minecraft.player, this.minecraft.player.connection));
    }
    
    public PerformanceMetrics getPerformanceMetrics() {
        final FrameTimer aez2 = this.minecraft.getFrameTimer();
        long long3 = 2147483647L;
        long long4 = -2147483648L;
        long long5 = 0L;
        for (final long long6 : aez2.getLog()) {
            long3 = Math.min(long3, long6);
            long4 = Math.max(long4, long6);
            long5 += long6;
        }
        return (PerformanceMetrics)new Metrics((int)long3, (int)long4, (int)(long5 / aez2.getLog().length), aez2.getLog().length);
    }
    
    public void setSessionEventListener(final SessionEventListener sessionEventListener) {
        this.listener = sessionEventListener;
    }
    
    public void onStartGameSession() {
        this.listener.onStartGameSession(this.getCurrentSession());
    }
    
    public void onLeaveGameSession() {
        this.listener.onLeaveGameSession(this.getCurrentSession());
    }
    
    static class Metrics implements PerformanceMetrics {
        private final int min;
        private final int max;
        private final int average;
        private final int samples;
        
        public Metrics(final int integer1, final int integer2, final int integer3, final int integer4) {
            this.min = integer1;
            this.max = integer2;
            this.average = integer3;
            this.samples = integer4;
        }
        
        public int getMinTime() {
            return this.min;
        }
        
        public int getMaxTime() {
            return this.max;
        }
        
        public int getAverageTime() {
            return this.average;
        }
        
        public int getSampleCount() {
            return this.samples;
        }
    }
}

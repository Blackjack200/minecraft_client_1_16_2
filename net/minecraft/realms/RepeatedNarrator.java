package net.minecraft.realms;

import com.google.common.util.concurrent.RateLimiter;
import net.minecraft.network.chat.Component;
import net.minecraft.Util;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.ChatType;
import net.minecraft.client.gui.chat.NarratorChatListener;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

public class RepeatedNarrator {
    private final float permitsPerSecond;
    private final AtomicReference<Params> params;
    
    public RepeatedNarrator(final Duration duration) {
        this.params = (AtomicReference<Params>)new AtomicReference();
        this.permitsPerSecond = 1000.0f / duration.toMillis();
    }
    
    public void narrate(final String string) {
        final Params a3 = (Params)this.params.updateAndGet(a -> {
            if (a == null || !string.equals(a.narration)) {
                return new Params(string, RateLimiter.create((double)this.permitsPerSecond));
            }
            return a;
        });
        if (a3.rateLimiter.tryAcquire(1)) {
            NarratorChatListener.INSTANCE.handle(ChatType.SYSTEM, new TextComponent(string), Util.NIL_UUID);
        }
    }
    
    static class Params {
        private final String narration;
        private final RateLimiter rateLimiter;
        
        Params(final String string, final RateLimiter rateLimiter) {
            this.narration = string;
            this.rateLimiter = rateLimiter;
        }
    }
}

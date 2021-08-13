package net.minecraft.gametest.framework;

import java.util.Iterator;
import java.util.List;

public class GameTestSequence {
    private final GameTestInfo parent;
    private final List<GameTestEvent> events;
    private long lastTick;
    
    public void tickAndContinue(final long long1) {
        try {
            this.tick(long1);
        }
        catch (Exception ex) {}
    }
    
    public void tickAndFailIfNotComplete(final long long1) {
        try {
            this.tick(long1);
        }
        catch (Exception exception4) {
            this.parent.fail((Throwable)exception4);
        }
    }
    
    private void tick(final long long1) {
        final Iterator<GameTestEvent> iterator4 = (Iterator<GameTestEvent>)this.events.iterator();
        while (iterator4.hasNext()) {
            final GameTestEvent lc5 = (GameTestEvent)iterator4.next();
            lc5.assertion.run();
            iterator4.remove();
            final long long2 = long1 - this.lastTick;
            final long long3 = this.lastTick;
            this.lastTick = long1;
            if (lc5.expectedDelay != null && lc5.expectedDelay != long2) {
                this.parent.fail((Throwable)new GameTestAssertException(new StringBuilder().append("Succeeded in invalid tick: expected ").append(long3 + lc5.expectedDelay).append(", but current tick is ").append(long1).toString()));
                break;
            }
        }
    }
}

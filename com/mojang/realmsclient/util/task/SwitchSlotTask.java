package com.mojang.realmsclient.util.task;

import com.mojang.realmsclient.exception.RetryCallException;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.realmsclient.client.RealmsClient;

public class SwitchSlotTask extends LongRunningTask {
    private final long worldId;
    private final int slot;
    private final Runnable callback;
    
    public SwitchSlotTask(final long long1, final int integer, final Runnable runnable) {
        this.worldId = long1;
        this.slot = integer;
        this.callback = runnable;
    }
    
    public void run() {
        final RealmsClient dfy2 = RealmsClient.create();
        this.setTitle(new TranslatableComponent("mco.minigame.world.slot.screen.title"));
        for (int integer3 = 0; integer3 < 25; ++integer3) {
            try {
                if (this.aborted()) {
                    return;
                }
                if (dfy2.switchSlot(this.worldId, this.slot)) {
                    this.callback.run();
                    break;
                }
            }
            catch (RetryCallException dhg4) {
                if (this.aborted()) {
                    return;
                }
                LongRunningTask.pause(dhg4.delaySeconds);
            }
            catch (Exception exception4) {
                if (this.aborted()) {
                    return;
                }
                SwitchSlotTask.LOGGER.error("Couldn't switch world!");
                this.error(exception4.toString());
            }
        }
    }
}

package com.mojang.realmsclient.util.task;

import com.mojang.realmsclient.exception.RetryCallException;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.gui.screens.RealmsConfigureWorldScreen;
import com.mojang.realmsclient.dto.WorldTemplate;

public class SwitchMinigameTask extends LongRunningTask {
    private final long worldId;
    private final WorldTemplate worldTemplate;
    private final RealmsConfigureWorldScreen lastScreen;
    
    public SwitchMinigameTask(final long long1, final WorldTemplate dhb, final RealmsConfigureWorldScreen dhp) {
        this.worldId = long1;
        this.worldTemplate = dhb;
        this.lastScreen = dhp;
    }
    
    public void run() {
        final RealmsClient dfy2 = RealmsClient.create();
        this.setTitle(new TranslatableComponent("mco.minigame.world.starting.screen.title"));
        for (int integer3 = 0; integer3 < 25; ++integer3) {
            try {
                if (this.aborted()) {
                    return;
                }
                if (dfy2.putIntoMinigameMode(this.worldId, this.worldTemplate.id)) {
                    LongRunningTask.setScreen(this.lastScreen);
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
                SwitchMinigameTask.LOGGER.error("Couldn't start mini game!");
                this.error(exception4.toString());
            }
        }
    }
}

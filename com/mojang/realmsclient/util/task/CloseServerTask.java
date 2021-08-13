package com.mojang.realmsclient.util.task;

import com.mojang.realmsclient.exception.RetryCallException;
import net.minecraft.client.gui.screens.Screen;
import com.mojang.realmsclient.client.RealmsClient;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.realmsclient.gui.screens.RealmsConfigureWorldScreen;
import com.mojang.realmsclient.dto.RealmsServer;

public class CloseServerTask extends LongRunningTask {
    private final RealmsServer serverData;
    private final RealmsConfigureWorldScreen configureScreen;
    
    public CloseServerTask(final RealmsServer dgn, final RealmsConfigureWorldScreen dhp) {
        this.serverData = dgn;
        this.configureScreen = dhp;
    }
    
    public void run() {
        this.setTitle(new TranslatableComponent("mco.configure.world.closing"));
        final RealmsClient dfy2 = RealmsClient.create();
        for (int integer3 = 0; integer3 < 25; ++integer3) {
            if (this.aborted()) {
                return;
            }
            try {
                final boolean boolean4 = dfy2.close(this.serverData.id);
                if (boolean4) {
                    this.configureScreen.stateChanged();
                    this.serverData.state = RealmsServer.State.CLOSED;
                    LongRunningTask.setScreen(this.configureScreen);
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
                CloseServerTask.LOGGER.error("Failed to close server", (Throwable)exception4);
                this.error("Failed to close the server");
            }
        }
    }
}

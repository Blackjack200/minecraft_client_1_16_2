package com.mojang.realmsclient.util.task;

import com.mojang.realmsclient.exception.RetryCallException;
import com.mojang.realmsclient.gui.screens.RealmsConfigureWorldScreen;
import com.mojang.realmsclient.client.RealmsClient;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.realmsclient.RealmsMainScreen;
import net.minecraft.client.gui.screens.Screen;
import com.mojang.realmsclient.dto.RealmsServer;

public class OpenServerTask extends LongRunningTask {
    private final RealmsServer serverData;
    private final Screen returnScreen;
    private final boolean join;
    private final RealmsMainScreen mainScreen;
    
    public OpenServerTask(final RealmsServer dgn, final Screen doq, final RealmsMainScreen dft, final boolean boolean4) {
        this.serverData = dgn;
        this.returnScreen = doq;
        this.join = boolean4;
        this.mainScreen = dft;
    }
    
    public void run() {
        this.setTitle(new TranslatableComponent("mco.configure.world.opening"));
        final RealmsClient dfy2 = RealmsClient.create();
        for (int integer3 = 0; integer3 < 25; ++integer3) {
            if (this.aborted()) {
                return;
            }
            try {
                final boolean boolean4 = dfy2.open(this.serverData.id);
                if (boolean4) {
                    if (this.returnScreen instanceof RealmsConfigureWorldScreen) {
                        ((RealmsConfigureWorldScreen)this.returnScreen).stateChanged();
                    }
                    this.serverData.state = RealmsServer.State.OPEN;
                    if (this.join) {
                        this.mainScreen.play(this.serverData, this.returnScreen);
                        break;
                    }
                    LongRunningTask.setScreen(this.returnScreen);
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
                OpenServerTask.LOGGER.error("Failed to open server", (Throwable)exception4);
                this.error("Failed to open the server");
            }
        }
    }
}

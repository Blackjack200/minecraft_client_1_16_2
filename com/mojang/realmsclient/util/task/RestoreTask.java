package com.mojang.realmsclient.util.task;

import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
import com.mojang.realmsclient.exception.RetryCallException;
import net.minecraft.client.gui.screens.Screen;
import com.mojang.realmsclient.client.RealmsClient;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.realmsclient.gui.screens.RealmsConfigureWorldScreen;
import com.mojang.realmsclient.dto.Backup;

public class RestoreTask extends LongRunningTask {
    private final Backup backup;
    private final long worldId;
    private final RealmsConfigureWorldScreen lastScreen;
    
    public RestoreTask(final Backup dgd, final long long2, final RealmsConfigureWorldScreen dhp) {
        this.backup = dgd;
        this.worldId = long2;
        this.lastScreen = dhp;
    }
    
    public void run() {
        this.setTitle(new TranslatableComponent("mco.backup.restoring"));
        final RealmsClient dfy2 = RealmsClient.create();
        for (int integer3 = 0; integer3 < 25; ++integer3) {
            try {
                if (this.aborted()) {
                    return;
                }
                dfy2.restoreWorld(this.worldId, this.backup.backupId);
                LongRunningTask.pause(1);
                if (this.aborted()) {
                    return;
                }
                LongRunningTask.setScreen(this.lastScreen.getNewScreen());
                return;
            }
            catch (RetryCallException dhg4) {
                if (this.aborted()) {
                    return;
                }
                LongRunningTask.pause(dhg4.delaySeconds);
            }
            catch (RealmsServiceException dhf4) {
                if (this.aborted()) {
                    return;
                }
                RestoreTask.LOGGER.error("Couldn't restore backup", (Throwable)dhf4);
                LongRunningTask.setScreen(new RealmsGenericErrorScreen(dhf4, this.lastScreen));
                return;
            }
            catch (Exception exception4) {
                if (this.aborted()) {
                    return;
                }
                RestoreTask.LOGGER.error("Couldn't restore backup", (Throwable)exception4);
                this.error(exception4.getLocalizedMessage());
                return;
            }
        }
    }
}

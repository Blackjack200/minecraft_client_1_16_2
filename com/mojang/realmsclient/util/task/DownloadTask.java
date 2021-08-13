package com.mojang.realmsclient.util.task;

import com.mojang.realmsclient.dto.WorldDownload;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
import com.mojang.realmsclient.exception.RetryCallException;
import com.mojang.realmsclient.gui.screens.RealmsDownloadLatestWorldScreen;
import com.mojang.realmsclient.client.RealmsClient;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.gui.screens.Screen;

public class DownloadTask extends LongRunningTask {
    private final long worldId;
    private final int slot;
    private final Screen lastScreen;
    private final String downloadName;
    
    public DownloadTask(final long long1, final int integer, final String string, final Screen doq) {
        this.worldId = long1;
        this.slot = integer;
        this.lastScreen = doq;
        this.downloadName = string;
    }
    
    public void run() {
        this.setTitle(new TranslatableComponent("mco.download.preparing"));
        final RealmsClient dfy2 = RealmsClient.create();
        for (int integer3 = 0; integer3 < 25; ++integer3) {
            try {
                if (this.aborted()) {
                    return;
                }
                final WorldDownload dha4 = dfy2.requestDownloadInfo(this.worldId, this.slot);
                LongRunningTask.pause(1);
                if (this.aborted()) {
                    return;
                }
                LongRunningTask.setScreen(new RealmsDownloadLatestWorldScreen(this.lastScreen, dha4, this.downloadName, boolean1 -> {}));
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
                DownloadTask.LOGGER.error("Couldn't download world data");
                LongRunningTask.setScreen(new RealmsGenericErrorScreen(dhf4, this.lastScreen));
                return;
            }
            catch (Exception exception4) {
                if (this.aborted()) {
                    return;
                }
                DownloadTask.LOGGER.error("Couldn't download world data", (Throwable)exception4);
                this.error(exception4.getLocalizedMessage());
                return;
            }
        }
    }
}

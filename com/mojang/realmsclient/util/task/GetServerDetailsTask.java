package com.mojang.realmsclient.util.task;

import net.minecraft.network.chat.TextComponent;
import java.util.function.Function;
import com.mojang.realmsclient.dto.RealmsServerAddress;
import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
import com.mojang.realmsclient.gui.screens.RealmsLongConfirmationScreen;
import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
import com.mojang.realmsclient.gui.screens.RealmsBrokenWorldScreen;
import net.minecraft.client.Minecraft;
import com.mojang.realmsclient.gui.screens.RealmsTermsScreen;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.exception.RetryCallException;
import com.mojang.realmsclient.client.RealmsClient;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.concurrent.locks.ReentrantLock;
import com.mojang.realmsclient.RealmsMainScreen;
import net.minecraft.client.gui.screens.Screen;
import com.mojang.realmsclient.dto.RealmsServer;

public class GetServerDetailsTask extends LongRunningTask {
    private final RealmsServer server;
    private final Screen lastScreen;
    private final RealmsMainScreen mainScreen;
    private final ReentrantLock connectLock;
    
    public GetServerDetailsTask(final RealmsMainScreen dft, final Screen doq, final RealmsServer dgn, final ReentrantLock reentrantLock) {
        this.lastScreen = doq;
        this.mainScreen = dft;
        this.server = dgn;
        this.connectLock = reentrantLock;
    }
    
    public void run() {
        this.setTitle(new TranslatableComponent("mco.connect.connecting"));
        final RealmsClient dfy2 = RealmsClient.create();
        boolean boolean3 = false;
        boolean boolean4 = false;
        int integer5 = 5;
        RealmsServerAddress dgo6 = null;
        boolean boolean5 = false;
        boolean boolean6 = false;
        for (int integer6 = 0; integer6 < 40; ++integer6) {
            if (this.aborted()) {
                break;
            }
            try {
                dgo6 = dfy2.join(this.server.id);
                boolean3 = true;
            }
            catch (RetryCallException dhg10) {
                integer5 = dhg10.delaySeconds;
            }
            catch (RealmsServiceException dhf10) {
                if (dhf10.errorCode == 6002) {
                    boolean5 = true;
                    break;
                }
                if (dhf10.errorCode == 6006) {
                    boolean6 = true;
                    break;
                }
                boolean4 = true;
                this.error(dhf10.toString());
                GetServerDetailsTask.LOGGER.error("Couldn't connect to world", (Throwable)dhf10);
                break;
            }
            catch (Exception exception10) {
                boolean4 = true;
                GetServerDetailsTask.LOGGER.error("Couldn't connect to world", (Throwable)exception10);
                this.error(exception10.getLocalizedMessage());
                break;
            }
            if (boolean3) {
                break;
            }
            this.sleep(integer5);
        }
        if (boolean5) {
            LongRunningTask.setScreen(new RealmsTermsScreen(this.lastScreen, this.mainScreen, this.server));
        }
        else if (boolean6) {
            if (this.server.ownerUUID.equals(Minecraft.getInstance().getUser().getUuid())) {
                LongRunningTask.setScreen(new RealmsBrokenWorldScreen(this.lastScreen, this.mainScreen, this.server.id, this.server.worldType == RealmsServer.WorldType.MINIGAME));
            }
            else {
                LongRunningTask.setScreen(new RealmsGenericErrorScreen(new TranslatableComponent("mco.brokenworld.nonowner.title"), new TranslatableComponent("mco.brokenworld.nonowner.error"), this.lastScreen));
            }
        }
        else if (!this.aborted() && !boolean4) {
            if (boolean3) {
                final RealmsServerAddress dgo7 = dgo6;
                if (dgo7.resourcePackUrl != null && dgo7.resourcePackHash != null) {
                    final Component nr10 = new TranslatableComponent("mco.configure.world.resourcepack.question.line1");
                    final Component nr11 = new TranslatableComponent("mco.configure.world.resourcepack.question.line2");
                    LongRunningTask.setScreen(new RealmsLongConfirmationScreen(boolean2 -> {
                        try {
                            if (boolean2) {
                                final Function<Throwable, Void> function4 = (Function<Throwable, Void>)(throwable -> {
                                    Minecraft.getInstance().getClientPackSource().clearServerPack();
                                    GetServerDetailsTask.LOGGER.error(throwable);
                                    LongRunningTask.setScreen(new RealmsGenericErrorScreen(new TextComponent("Failed to download resource pack!"), this.lastScreen));
                                    return null;
                                });
                                try {
                                    Minecraft.getInstance().getClientPackSource().downloadAndSelectResourcePack(dgo7.resourcePackUrl, dgo7.resourcePackHash).thenRun(() -> this.setScreen(new RealmsLongRunningMcoTaskScreen(this.lastScreen, new ConnectTask(this.lastScreen, dgo7)))).exceptionally((Function)function4);
                                }
                                catch (Exception exception5) {
                                    function4.apply(exception5);
                                }
                            }
                            else {
                                LongRunningTask.setScreen(this.lastScreen);
                            }
                        }
                        finally {
                            if (this.connectLock != null && this.connectLock.isHeldByCurrentThread()) {
                                this.connectLock.unlock();
                            }
                        }
                    }, RealmsLongConfirmationScreen.Type.Info, nr10, nr11, true));
                }
                else {
                    this.setScreen(new RealmsLongRunningMcoTaskScreen(this.lastScreen, new ConnectTask(this.lastScreen, dgo7)));
                }
            }
            else {
                this.error(new TranslatableComponent("mco.errorMessage.connectionFailure"));
            }
        }
    }
    
    private void sleep(final int integer) {
        try {
            Thread.sleep((long)(integer * 1000));
        }
        catch (InterruptedException interruptedException3) {
            GetServerDetailsTask.LOGGER.warn(interruptedException3.getLocalizedMessage());
        }
    }
}

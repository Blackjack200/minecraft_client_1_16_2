package com.mojang.realmsclient.util.task;

import com.mojang.realmsclient.exception.RetryCallException;
import com.mojang.realmsclient.client.RealmsClient;
import net.minecraft.network.chat.TranslatableComponent;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import com.mojang.realmsclient.dto.WorldTemplate;

public class ResettingWorldTask extends LongRunningTask {
    private final String seed;
    private final WorldTemplate worldTemplate;
    private final int levelType;
    private final boolean generateStructures;
    private final long serverId;
    private Component title;
    private final Runnable callback;
    
    public ResettingWorldTask(@Nullable final String string, @Nullable final WorldTemplate dhb, final int integer, final boolean boolean4, final long long5, @Nullable final Component nr, final Runnable runnable) {
        this.title = new TranslatableComponent("mco.reset.world.resetting.screen.title");
        this.seed = string;
        this.worldTemplate = dhb;
        this.levelType = integer;
        this.generateStructures = boolean4;
        this.serverId = long5;
        if (nr != null) {
            this.title = nr;
        }
        this.callback = runnable;
    }
    
    public void run() {
        final RealmsClient dfy2 = RealmsClient.create();
        this.setTitle(this.title);
        for (int integer3 = 0; integer3 < 25; ++integer3) {
            try {
                if (this.aborted()) {
                    return;
                }
                if (this.worldTemplate != null) {
                    dfy2.resetWorldWithTemplate(this.serverId, this.worldTemplate.id);
                }
                else {
                    dfy2.resetWorldWithSeed(this.serverId, this.seed, this.levelType, this.generateStructures);
                }
                if (this.aborted()) {
                    return;
                }
                this.callback.run();
                return;
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
                ResettingWorldTask.LOGGER.error("Couldn't reset world");
                this.error(exception4.toString());
                return;
            }
        }
    }
}

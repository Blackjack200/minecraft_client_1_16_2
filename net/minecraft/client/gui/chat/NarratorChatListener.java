package net.minecraft.client.gui.chat;

import org.apache.logging.log4j.LogManager;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.NarratorStatus;
import net.minecraft.client.Minecraft;
import java.util.UUID;
import net.minecraft.network.chat.ChatType;
import com.mojang.text2speech.Narrator;
import org.apache.logging.log4j.Logger;
import net.minecraft.network.chat.Component;

public class NarratorChatListener implements ChatListener {
    public static final Component NO_TITLE;
    private static final Logger LOGGER;
    public static final NarratorChatListener INSTANCE;
    private final Narrator narrator;
    
    public NarratorChatListener() {
        this.narrator = Narrator.getNarrator();
    }
    
    public void handle(final ChatType no, final Component nr, final UUID uUID) {
        if (Minecraft.getInstance().isBlocked(uUID)) {
            return;
        }
        final NarratorStatus djy5 = getStatus();
        if (djy5 == NarratorStatus.OFF || !this.narrator.active()) {
            return;
        }
        if (djy5 == NarratorStatus.ALL || (djy5 == NarratorStatus.CHAT && no == ChatType.CHAT) || (djy5 == NarratorStatus.SYSTEM && no == ChatType.SYSTEM)) {
            Component nr2;
            if (nr instanceof TranslatableComponent && "chat.type.text".equals(((TranslatableComponent)nr).getKey())) {
                nr2 = new TranslatableComponent("chat.type.text.narrate", ((TranslatableComponent)nr).getArgs());
            }
            else {
                nr2 = nr;
            }
            this.doSay(no.shouldInterrupt(), nr2.getString());
        }
    }
    
    public void sayNow(final String string) {
        final NarratorStatus djy3 = getStatus();
        if (this.narrator.active() && djy3 != NarratorStatus.OFF && djy3 != NarratorStatus.CHAT && !string.isEmpty()) {
            this.narrator.clear();
            this.doSay(true, string);
        }
    }
    
    private static NarratorStatus getStatus() {
        return Minecraft.getInstance().options.narratorStatus;
    }
    
    private void doSay(final boolean boolean1, final String string) {
        if (SharedConstants.IS_RUNNING_IN_IDE) {
            NarratorChatListener.LOGGER.debug("Narrating: {}", string.replaceAll("\n", "\\\\n"));
        }
        this.narrator.say(string, boolean1);
    }
    
    public void updateNarratorStatus(final NarratorStatus djy) {
        this.clear();
        this.narrator.say(new TranslatableComponent("options.narrator").append(" : ").append(djy.getName()).getString(), true);
        final ToastComponent dmo3 = Minecraft.getInstance().getToasts();
        if (this.narrator.active()) {
            if (djy == NarratorStatus.OFF) {
                SystemToast.addOrUpdate(dmo3, SystemToast.SystemToastIds.NARRATOR_TOGGLE, new TranslatableComponent("narrator.toast.disabled"), null);
            }
            else {
                SystemToast.addOrUpdate(dmo3, SystemToast.SystemToastIds.NARRATOR_TOGGLE, new TranslatableComponent("narrator.toast.enabled"), djy.getName());
            }
        }
        else {
            SystemToast.addOrUpdate(dmo3, SystemToast.SystemToastIds.NARRATOR_TOGGLE, new TranslatableComponent("narrator.toast.disabled"), new TranslatableComponent("options.narrator.notavailable"));
        }
    }
    
    public boolean isActive() {
        return this.narrator.active();
    }
    
    public void clear() {
        if (getStatus() == NarratorStatus.OFF || !this.narrator.active()) {
            return;
        }
        this.narrator.clear();
    }
    
    public void destroy() {
        this.narrator.destroy();
    }
    
    static {
        NO_TITLE = TextComponent.EMPTY;
        LOGGER = LogManager.getLogger();
        INSTANCE = new NarratorChatListener();
    }
}

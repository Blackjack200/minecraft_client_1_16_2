package net.minecraft.network.chat;

public class CommonComponents {
    public static final Component OPTION_ON;
    public static final Component OPTION_OFF;
    public static final Component GUI_DONE;
    public static final Component GUI_CANCEL;
    public static final Component GUI_YES;
    public static final Component GUI_NO;
    public static final Component GUI_PROCEED;
    public static final Component GUI_BACK;
    public static final Component CONNECT_FAILED;
    
    public static Component optionStatus(final boolean boolean1) {
        return boolean1 ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF;
    }
    
    public static MutableComponent optionStatus(final Component nr, final boolean boolean2) {
        return new TranslatableComponent(boolean2 ? "options.on.composed" : "options.off.composed", new Object[] { nr });
    }
    
    static {
        OPTION_ON = new TranslatableComponent("options.on");
        OPTION_OFF = new TranslatableComponent("options.off");
        GUI_DONE = new TranslatableComponent("gui.done");
        GUI_CANCEL = new TranslatableComponent("gui.cancel");
        GUI_YES = new TranslatableComponent("gui.yes");
        GUI_NO = new TranslatableComponent("gui.no");
        GUI_PROCEED = new TranslatableComponent("gui.proceed");
        GUI_BACK = new TranslatableComponent("gui.back");
        CONNECT_FAILED = new TranslatableComponent("connect.failed");
    }
}

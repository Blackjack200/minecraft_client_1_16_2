package net.minecraft.network.chat;

import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Map;

public class ClickEvent {
    private final Action action;
    private final String value;
    
    public ClickEvent(final Action a, final String string) {
        this.action = a;
        this.value = string;
    }
    
    public Action getAction() {
        return this.action;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        final ClickEvent np3 = (ClickEvent)object;
        if (this.action != np3.action) {
            return false;
        }
        if (this.value != null) {
            if (this.value.equals(np3.value)) {
                return true;
            }
        }
        else if (np3.value == null) {
            return true;
        }
        return false;
    }
    
    public String toString() {
        return new StringBuilder().append("ClickEvent{action=").append(this.action).append(", value='").append(this.value).append('\'').append('}').toString();
    }
    
    public int hashCode() {
        int integer2 = this.action.hashCode();
        integer2 = 31 * integer2 + ((this.value != null) ? this.value.hashCode() : 0);
        return integer2;
    }
    
    public enum Action {
        OPEN_URL("open_url", true), 
        OPEN_FILE("open_file", false), 
        RUN_COMMAND("run_command", true), 
        SUGGEST_COMMAND("suggest_command", true), 
        CHANGE_PAGE("change_page", true), 
        COPY_TO_CLIPBOARD("copy_to_clipboard", true);
        
        private static final Map<String, Action> LOOKUP;
        private final boolean allowFromServer;
        private final String name;
        
        private Action(final String string3, final boolean boolean4) {
            this.name = string3;
            this.allowFromServer = boolean4;
        }
        
        public boolean isAllowedFromServer() {
            return this.allowFromServer;
        }
        
        public String getName() {
            return this.name;
        }
        
        public static Action getByName(final String string) {
            return (Action)Action.LOOKUP.get(string);
        }
        
        static {
            LOOKUP = (Map)Arrays.stream((Object[])values()).collect(Collectors.toMap(Action::getName, a -> a));
        }
    }
}

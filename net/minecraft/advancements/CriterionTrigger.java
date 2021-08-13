package net.minecraft.advancements;

import net.minecraft.advancements.critereon.DeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.resources.ResourceLocation;

public interface CriterionTrigger<T extends CriterionTriggerInstance> {
    ResourceLocation getId();
    
    void addPlayerListener(final PlayerAdvancements vt, final Listener<T> a);
    
    void removePlayerListener(final PlayerAdvancements vt, final Listener<T> a);
    
    void removePlayerListeners(final PlayerAdvancements vt);
    
    T createInstance(final JsonObject jsonObject, final DeserializationContext ax);
    
    public static class Listener<T extends CriterionTriggerInstance> {
        private final T trigger;
        private final Advancement advancement;
        private final String criterion;
        
        public Listener(final T ag, final Advancement y, final String string) {
            this.trigger = ag;
            this.advancement = y;
            this.criterion = string;
        }
        
        public T getTriggerInstance() {
            return this.trigger;
        }
        
        public void run(final PlayerAdvancements vt) {
            vt.award(this.advancement, this.criterion);
        }
        
        public boolean equals(final Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || this.getClass() != object.getClass()) {
                return false;
            }
            final Listener<?> a3 = object;
            return this.trigger.equals(a3.trigger) && this.advancement.equals(a3.advancement) && this.criterion.equals(a3.criterion);
        }
        
        public int hashCode() {
            int integer2 = this.trigger.hashCode();
            integer2 = 31 * integer2 + this.advancement.hashCode();
            integer2 = 31 * integer2 + this.criterion.hashCode();
            return integer2;
        }
    }
    
    public static class Listener<T extends CriterionTriggerInstance> {
        private final T trigger;
        private final Advancement advancement;
        private final String criterion;
        
        public Listener(final T ag, final Advancement y, final String string) {
            this.trigger = ag;
            this.advancement = y;
            this.criterion = string;
        }
        
        public T getTriggerInstance() {
            return this.trigger;
        }
        
        public void run(final PlayerAdvancements vt) {
            vt.award(this.advancement, this.criterion);
        }
        
        public boolean equals(final Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || this.getClass() != object.getClass()) {
                return false;
            }
            final Listener<?> a3 = object;
            return this.trigger.equals(a3.trigger) && this.advancement.equals(a3.advancement) && this.criterion.equals(a3.criterion);
        }
        
        public int hashCode() {
            int integer2 = this.trigger.hashCode();
            integer2 = 31 * integer2 + this.advancement.hashCode();
            integer2 = 31 * integer2 + this.criterion.hashCode();
            return integer2;
        }
    }
}

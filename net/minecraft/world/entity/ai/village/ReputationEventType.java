package net.minecraft.world.entity.ai.village;

public interface ReputationEventType {
    public static final ReputationEventType ZOMBIE_VILLAGER_CURED = register("zombie_villager_cured");
    public static final ReputationEventType GOLEM_KILLED = register("golem_killed");
    public static final ReputationEventType VILLAGER_HURT = register("villager_hurt");
    public static final ReputationEventType VILLAGER_KILLED = register("villager_killed");
    public static final ReputationEventType TRADE = register("trade");
    
    default ReputationEventType register(final String string) {
        return new ReputationEventType() {
            public String toString() {
                return string;
            }
        };
    }
}

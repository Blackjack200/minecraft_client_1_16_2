package net.minecraft.client.tutorial;

import java.util.function.Function;

public enum TutorialSteps {
    MOVEMENT("movement", (Function<Tutorial, T>)MovementTutorialStepInstance::new), 
    FIND_TREE("find_tree", (Function<Tutorial, T>)FindTreeTutorialStepInstance::new), 
    PUNCH_TREE("punch_tree", (Function<Tutorial, T>)PunchTreeTutorialStepInstance::new), 
    OPEN_INVENTORY("open_inventory", (Function<Tutorial, T>)OpenInventoryTutorialStep::new), 
    CRAFT_PLANKS("craft_planks", (Function<Tutorial, T>)CraftPlanksTutorialStep::new), 
    NONE("none", (Function<Tutorial, T>)CompletedTutorialStepInstance::new);
    
    private final String name;
    private final Function<Tutorial, ? extends TutorialStepInstance> constructor;
    
    private <T extends TutorialStepInstance> TutorialSteps(final String string3, final Function<Tutorial, T> function) {
        this.name = string3;
        this.constructor = function;
    }
    
    public TutorialStepInstance create(final Tutorial enw) {
        return (TutorialStepInstance)this.constructor.apply(enw);
    }
    
    public String getName() {
        return this.name;
    }
    
    public static TutorialSteps getByName(final String string) {
        for (final TutorialSteps eny5 : values()) {
            if (eny5.name.equals(string)) {
                return eny5;
            }
        }
        return TutorialSteps.NONE;
    }
}

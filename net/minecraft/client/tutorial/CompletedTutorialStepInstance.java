package net.minecraft.client.tutorial;

public class CompletedTutorialStepInstance implements TutorialStepInstance {
    private final Tutorial tutorial;
    
    public CompletedTutorialStepInstance(final Tutorial enw) {
        this.tutorial = enw;
    }
}

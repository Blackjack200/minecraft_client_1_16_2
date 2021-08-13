package net.minecraft.world.item;

public interface TooltipFlag {
    boolean isAdvanced();
    
    public enum Default implements TooltipFlag {
        NORMAL(false), 
        ADVANCED(true);
        
        private final boolean advanced;
        
        private Default(final boolean boolean3) {
            this.advanced = boolean3;
        }
        
        public boolean isAdvanced() {
            return this.advanced;
        }
    }
}

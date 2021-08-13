package net.minecraft.world.entity.ai.attributes;

public class Attribute {
    private final double defaultValue;
    private boolean syncable;
    private final String descriptionId;
    
    protected Attribute(final String string, final double double2) {
        this.defaultValue = double2;
        this.descriptionId = string;
    }
    
    public double getDefaultValue() {
        return this.defaultValue;
    }
    
    public boolean isClientSyncable() {
        return this.syncable;
    }
    
    public Attribute setSyncable(final boolean boolean1) {
        this.syncable = boolean1;
        return this;
    }
    
    public double sanitizeValue(final double double1) {
        return double1;
    }
    
    public String getDescriptionId() {
        return this.descriptionId;
    }
}

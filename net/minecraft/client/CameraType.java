package net.minecraft.client;

public enum CameraType {
    FIRST_PERSON(true, false), 
    THIRD_PERSON_BACK(false, false), 
    THIRD_PERSON_FRONT(false, true);
    
    private static final CameraType[] VALUES;
    private boolean firstPerson;
    private boolean mirrored;
    
    private CameraType(final boolean boolean3, final boolean boolean4) {
        this.firstPerson = boolean3;
        this.mirrored = boolean4;
    }
    
    public boolean isFirstPerson() {
        return this.firstPerson;
    }
    
    public boolean isMirrored() {
        return this.mirrored;
    }
    
    public CameraType cycle() {
        return CameraType.VALUES[(this.ordinal() + 1) % CameraType.VALUES.length];
    }
    
    static {
        VALUES = values();
    }
}

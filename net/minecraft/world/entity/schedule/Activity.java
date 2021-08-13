package net.minecraft.world.entity.schedule;

import net.minecraft.core.Registry;

public class Activity {
    public static final Activity CORE;
    public static final Activity IDLE;
    public static final Activity WORK;
    public static final Activity PLAY;
    public static final Activity REST;
    public static final Activity MEET;
    public static final Activity PANIC;
    public static final Activity RAID;
    public static final Activity PRE_RAID;
    public static final Activity HIDE;
    public static final Activity FIGHT;
    public static final Activity CELEBRATE;
    public static final Activity ADMIRE_ITEM;
    public static final Activity AVOID;
    public static final Activity RIDE;
    private final String name;
    private final int hashCode;
    
    private Activity(final String string) {
        this.name = string;
        this.hashCode = string.hashCode();
    }
    
    public String getName() {
        return this.name;
    }
    
    private static Activity register(final String string) {
        return Registry.<Activity>register(Registry.ACTIVITY, string, new Activity(string));
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        final Activity bhc3 = (Activity)object;
        return this.name.equals(bhc3.name);
    }
    
    public int hashCode() {
        return this.hashCode;
    }
    
    public String toString() {
        return this.getName();
    }
    
    static {
        CORE = register("core");
        IDLE = register("idle");
        WORK = register("work");
        PLAY = register("play");
        REST = register("rest");
        MEET = register("meet");
        PANIC = register("panic");
        RAID = register("raid");
        PRE_RAID = register("pre_raid");
        HIDE = register("hide");
        FIGHT = register("fight");
        CELEBRATE = register("celebrate");
        ADMIRE_ITEM = register("admire_item");
        AVOID = register("avoid");
        RIDE = register("ride");
    }
}

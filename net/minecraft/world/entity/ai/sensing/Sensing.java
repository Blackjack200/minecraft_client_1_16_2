package net.minecraft.world.entity.ai.sensing;

import com.google.common.collect.Lists;
import net.minecraft.world.entity.Entity;
import java.util.List;
import net.minecraft.world.entity.Mob;

public class Sensing {
    private final Mob mob;
    private final List<Entity> seen;
    private final List<Entity> unseen;
    
    public Sensing(final Mob aqk) {
        this.seen = (List<Entity>)Lists.newArrayList();
        this.unseen = (List<Entity>)Lists.newArrayList();
        this.mob = aqk;
    }
    
    public void tick() {
        this.seen.clear();
        this.unseen.clear();
    }
    
    public boolean canSee(final Entity apx) {
        if (this.seen.contains(apx)) {
            return true;
        }
        if (this.unseen.contains(apx)) {
            return false;
        }
        this.mob.level.getProfiler().push("canSee");
        final boolean boolean3 = this.mob.canSee(apx);
        this.mob.level.getProfiler().pop();
        if (boolean3) {
            this.seen.add(apx);
        }
        else {
            this.unseen.add(apx);
        }
        return boolean3;
    }
}

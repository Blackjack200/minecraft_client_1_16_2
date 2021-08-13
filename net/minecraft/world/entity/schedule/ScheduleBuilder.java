package net.minecraft.world.entity.schedule;

import java.util.stream.Collectors;
import java.util.Set;
import com.google.common.collect.Lists;
import java.util.List;

public class ScheduleBuilder {
    private final Schedule schedule;
    private final List<ActivityTransition> transitions;
    
    public ScheduleBuilder(final Schedule bhe) {
        this.transitions = (List<ActivityTransition>)Lists.newArrayList();
        this.schedule = bhe;
    }
    
    public ScheduleBuilder changeActivityAt(final int integer, final Activity bhc) {
        this.transitions.add(new ActivityTransition(integer, bhc));
        return this;
    }
    
    public Schedule build() {
        ((Set)this.transitions.stream().map(ActivityTransition::getActivity).collect(Collectors.toSet())).forEach(this.schedule::ensureTimelineExistsFor);
        this.transitions.forEach(a -> {
            final Activity bhc3 = a.getActivity();
            this.schedule.getAllTimelinesExceptFor(bhc3).forEach(bhg -> bhg.addKeyframe(a.getTime(), 0.0f));
            this.schedule.getTimelineFor(bhc3).addKeyframe(a.getTime(), 1.0f);
        });
        return this.schedule;
    }
    
    static class ActivityTransition {
        private final int time;
        private final Activity activity;
        
        public ActivityTransition(final int integer, final Activity bhc) {
            this.time = integer;
            this.activity = bhc;
        }
        
        public int getTime() {
            return this.time;
        }
        
        public Activity getActivity() {
            return this.activity;
        }
    }
}

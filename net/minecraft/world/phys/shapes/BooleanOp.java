package net.minecraft.world.phys.shapes;

public interface BooleanOp {
    public static final BooleanOp FALSE = (boolean1, boolean2) -> false;
    public static final BooleanOp NOT_OR = (boolean1, boolean2) -> !boolean1 && !boolean2;
    public static final BooleanOp ONLY_SECOND = (boolean1, boolean2) -> boolean2 && !boolean1;
    public static final BooleanOp NOT_FIRST = (boolean1, boolean2) -> !boolean1;
    public static final BooleanOp ONLY_FIRST = (boolean1, boolean2) -> boolean1 && !boolean2;
    public static final BooleanOp NOT_SECOND = (boolean1, boolean2) -> !boolean2;
    public static final BooleanOp NOT_SAME = (boolean1, boolean2) -> boolean1 != boolean2;
    public static final BooleanOp NOT_AND = (boolean1, boolean2) -> !boolean1 || !boolean2;
    public static final BooleanOp AND = (boolean1, boolean2) -> boolean1 && boolean2;
    public static final BooleanOp SAME = (boolean1, boolean2) -> boolean1 == boolean2;
    public static final BooleanOp SECOND = (boolean1, boolean2) -> boolean2;
    public static final BooleanOp CAUSES = (boolean1, boolean2) -> !boolean1 || boolean2;
    public static final BooleanOp FIRST = (boolean1, boolean2) -> boolean1;
    public static final BooleanOp CAUSED_BY = (boolean1, boolean2) -> boolean1 || !boolean2;
    public static final BooleanOp OR = (boolean1, boolean2) -> boolean1 || boolean2;
    public static final BooleanOp TRUE = (boolean1, boolean2) -> true;
    
    boolean apply(final boolean boolean1, final boolean boolean2);
}

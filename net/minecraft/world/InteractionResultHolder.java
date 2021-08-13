package net.minecraft.world;

public class InteractionResultHolder<T> {
    private final InteractionResult result;
    private final T object;
    
    public InteractionResultHolder(final InteractionResult aor, final T object) {
        this.result = aor;
        this.object = object;
    }
    
    public InteractionResult getResult() {
        return this.result;
    }
    
    public T getObject() {
        return this.object;
    }
    
    public static <T> InteractionResultHolder<T> success(final T object) {
        return new InteractionResultHolder<T>(InteractionResult.SUCCESS, object);
    }
    
    public static <T> InteractionResultHolder<T> consume(final T object) {
        return new InteractionResultHolder<T>(InteractionResult.CONSUME, object);
    }
    
    public static <T> InteractionResultHolder<T> pass(final T object) {
        return new InteractionResultHolder<T>(InteractionResult.PASS, object);
    }
    
    public static <T> InteractionResultHolder<T> fail(final T object) {
        return new InteractionResultHolder<T>(InteractionResult.FAIL, object);
    }
    
    public static <T> InteractionResultHolder<T> sidedSuccess(final T object, final boolean boolean2) {
        return boolean2 ? InteractionResultHolder.<T>success(object) : InteractionResultHolder.<T>consume(object);
    }
}

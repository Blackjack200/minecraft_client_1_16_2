package net.minecraft.util;

public class Tuple<A, B> {
    private A a;
    private B b;
    
    public Tuple(final A object1, final B object2) {
        this.a = object1;
        this.b = object2;
    }
    
    public A getA() {
        return this.a;
    }
    
    public B getB() {
        return this.b;
    }
}

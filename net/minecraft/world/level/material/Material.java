package net.minecraft.world.level.material;

public final class Material {
    public static final Material AIR;
    public static final Material STRUCTURAL_AIR;
    public static final Material PORTAL;
    public static final Material CLOTH_DECORATION;
    public static final Material PLANT;
    public static final Material WATER_PLANT;
    public static final Material REPLACEABLE_PLANT;
    public static final Material REPLACEABLE_FIREPROOF_PLANT;
    public static final Material REPLACEABLE_WATER_PLANT;
    public static final Material WATER;
    public static final Material BUBBLE_COLUMN;
    public static final Material LAVA;
    public static final Material TOP_SNOW;
    public static final Material FIRE;
    public static final Material DECORATION;
    public static final Material WEB;
    public static final Material BUILDABLE_GLASS;
    public static final Material CLAY;
    public static final Material DIRT;
    public static final Material GRASS;
    public static final Material ICE_SOLID;
    public static final Material SAND;
    public static final Material SPONGE;
    public static final Material SHULKER_SHELL;
    public static final Material WOOD;
    public static final Material NETHER_WOOD;
    public static final Material BAMBOO_SAPLING;
    public static final Material BAMBOO;
    public static final Material WOOL;
    public static final Material EXPLOSIVE;
    public static final Material LEAVES;
    public static final Material GLASS;
    public static final Material ICE;
    public static final Material CACTUS;
    public static final Material STONE;
    public static final Material METAL;
    public static final Material SNOW;
    public static final Material HEAVY_METAL;
    public static final Material BARRIER;
    public static final Material PISTON;
    public static final Material CORAL;
    public static final Material VEGETABLE;
    public static final Material EGG;
    public static final Material CAKE;
    private final MaterialColor color;
    private final PushReaction pushReaction;
    private final boolean blocksMotion;
    private final boolean flammable;
    private final boolean liquid;
    private final boolean solidBlocking;
    private final boolean replaceable;
    private final boolean solid;
    
    public Material(final MaterialColor cuy, final boolean boolean2, final boolean boolean3, final boolean boolean4, final boolean boolean5, final boolean boolean6, final boolean boolean7, final PushReaction cuz) {
        this.color = cuy;
        this.liquid = boolean2;
        this.solid = boolean3;
        this.blocksMotion = boolean4;
        this.solidBlocking = boolean5;
        this.flammable = boolean6;
        this.replaceable = boolean7;
        this.pushReaction = cuz;
    }
    
    public boolean isLiquid() {
        return this.liquid;
    }
    
    public boolean isSolid() {
        return this.solid;
    }
    
    public boolean blocksMotion() {
        return this.blocksMotion;
    }
    
    public boolean isFlammable() {
        return this.flammable;
    }
    
    public boolean isReplaceable() {
        return this.replaceable;
    }
    
    public boolean isSolidBlocking() {
        return this.solidBlocking;
    }
    
    public PushReaction getPushReaction() {
        return this.pushReaction;
    }
    
    public MaterialColor getColor() {
        return this.color;
    }
    
    static {
        AIR = new Builder(MaterialColor.NONE).noCollider().notSolidBlocking().nonSolid().replaceable().build();
        STRUCTURAL_AIR = new Builder(MaterialColor.NONE).noCollider().notSolidBlocking().nonSolid().replaceable().build();
        PORTAL = new Builder(MaterialColor.NONE).noCollider().notSolidBlocking().nonSolid().notPushable().build();
        CLOTH_DECORATION = new Builder(MaterialColor.WOOL).noCollider().notSolidBlocking().nonSolid().flammable().build();
        PLANT = new Builder(MaterialColor.PLANT).noCollider().notSolidBlocking().nonSolid().destroyOnPush().build();
        WATER_PLANT = new Builder(MaterialColor.WATER).noCollider().notSolidBlocking().nonSolid().destroyOnPush().build();
        REPLACEABLE_PLANT = new Builder(MaterialColor.PLANT).noCollider().notSolidBlocking().nonSolid().destroyOnPush().replaceable().flammable().build();
        REPLACEABLE_FIREPROOF_PLANT = new Builder(MaterialColor.PLANT).noCollider().notSolidBlocking().nonSolid().destroyOnPush().replaceable().build();
        REPLACEABLE_WATER_PLANT = new Builder(MaterialColor.WATER).noCollider().notSolidBlocking().nonSolid().destroyOnPush().replaceable().build();
        WATER = new Builder(MaterialColor.WATER).noCollider().notSolidBlocking().nonSolid().destroyOnPush().replaceable().liquid().build();
        BUBBLE_COLUMN = new Builder(MaterialColor.WATER).noCollider().notSolidBlocking().nonSolid().destroyOnPush().replaceable().liquid().build();
        LAVA = new Builder(MaterialColor.FIRE).noCollider().notSolidBlocking().nonSolid().destroyOnPush().replaceable().liquid().build();
        TOP_SNOW = new Builder(MaterialColor.SNOW).noCollider().notSolidBlocking().nonSolid().destroyOnPush().replaceable().build();
        FIRE = new Builder(MaterialColor.NONE).noCollider().notSolidBlocking().nonSolid().destroyOnPush().replaceable().build();
        DECORATION = new Builder(MaterialColor.NONE).noCollider().notSolidBlocking().nonSolid().destroyOnPush().build();
        WEB = new Builder(MaterialColor.WOOL).noCollider().notSolidBlocking().destroyOnPush().build();
        BUILDABLE_GLASS = new Builder(MaterialColor.NONE).build();
        CLAY = new Builder(MaterialColor.CLAY).build();
        DIRT = new Builder(MaterialColor.DIRT).build();
        GRASS = new Builder(MaterialColor.GRASS).build();
        ICE_SOLID = new Builder(MaterialColor.ICE).build();
        SAND = new Builder(MaterialColor.SAND).build();
        SPONGE = new Builder(MaterialColor.COLOR_YELLOW).build();
        SHULKER_SHELL = new Builder(MaterialColor.COLOR_PURPLE).build();
        WOOD = new Builder(MaterialColor.WOOD).flammable().build();
        NETHER_WOOD = new Builder(MaterialColor.WOOD).build();
        BAMBOO_SAPLING = new Builder(MaterialColor.WOOD).flammable().destroyOnPush().noCollider().build();
        BAMBOO = new Builder(MaterialColor.WOOD).flammable().destroyOnPush().build();
        WOOL = new Builder(MaterialColor.WOOL).flammable().build();
        EXPLOSIVE = new Builder(MaterialColor.FIRE).flammable().notSolidBlocking().build();
        LEAVES = new Builder(MaterialColor.PLANT).flammable().notSolidBlocking().destroyOnPush().build();
        GLASS = new Builder(MaterialColor.NONE).notSolidBlocking().build();
        ICE = new Builder(MaterialColor.ICE).notSolidBlocking().build();
        CACTUS = new Builder(MaterialColor.PLANT).notSolidBlocking().destroyOnPush().build();
        STONE = new Builder(MaterialColor.STONE).build();
        METAL = new Builder(MaterialColor.METAL).build();
        SNOW = new Builder(MaterialColor.SNOW).build();
        HEAVY_METAL = new Builder(MaterialColor.METAL).notPushable().build();
        BARRIER = new Builder(MaterialColor.NONE).notPushable().build();
        PISTON = new Builder(MaterialColor.STONE).notPushable().build();
        CORAL = new Builder(MaterialColor.PLANT).destroyOnPush().build();
        VEGETABLE = new Builder(MaterialColor.PLANT).destroyOnPush().build();
        EGG = new Builder(MaterialColor.PLANT).destroyOnPush().build();
        CAKE = new Builder(MaterialColor.NONE).destroyOnPush().build();
    }
    
    public static class Builder {
        private PushReaction pushReaction;
        private boolean blocksMotion;
        private boolean flammable;
        private boolean liquid;
        private boolean replaceable;
        private boolean solid;
        private final MaterialColor color;
        private boolean solidBlocking;
        
        public Builder(final MaterialColor cuy) {
            this.pushReaction = PushReaction.NORMAL;
            this.blocksMotion = true;
            this.solid = true;
            this.solidBlocking = true;
            this.color = cuy;
        }
        
        public Builder liquid() {
            this.liquid = true;
            return this;
        }
        
        public Builder nonSolid() {
            this.solid = false;
            return this;
        }
        
        public Builder noCollider() {
            this.blocksMotion = false;
            return this;
        }
        
        private Builder notSolidBlocking() {
            this.solidBlocking = false;
            return this;
        }
        
        protected Builder flammable() {
            this.flammable = true;
            return this;
        }
        
        public Builder replaceable() {
            this.replaceable = true;
            return this;
        }
        
        protected Builder destroyOnPush() {
            this.pushReaction = PushReaction.DESTROY;
            return this;
        }
        
        protected Builder notPushable() {
            this.pushReaction = PushReaction.BLOCK;
            return this;
        }
        
        public Material build() {
            return new Material(this.color, this.liquid, this.solid, this.blocksMotion, this.solidBlocking, this.flammable, this.replaceable, this.pushReaction);
        }
    }
}

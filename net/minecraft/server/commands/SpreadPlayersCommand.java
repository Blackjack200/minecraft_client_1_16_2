package net.minecraft.server.commands;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.CommandContext;
import java.util.Map;
import net.minecraft.util.Mth;
import com.google.common.collect.Maps;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.server.level.ServerLevel;
import java.util.Iterator;
import net.minecraft.world.scores.Team;
import java.util.Set;
import net.minecraft.world.entity.player.Player;
import com.google.common.collect.Sets;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.Locale;
import java.util.Random;
import net.minecraft.world.entity.Entity;
import java.util.Collection;
import net.minecraft.world.phys.Vec2;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.coordinates.Vec2Argument;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.Dynamic4CommandExceptionType;

public class SpreadPlayersCommand {
    private static final Dynamic4CommandExceptionType ERROR_FAILED_TO_SPREAD_TEAMS;
    private static final Dynamic4CommandExceptionType ERROR_FAILED_TO_SPREAD_ENTITIES;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("spreadplayers").requires(db -> db.hasPermission(2))).then(Commands.argument("center", (com.mojang.brigadier.arguments.ArgumentType<Object>)Vec2Argument.vec2()).then(Commands.argument("spreadDistance", (com.mojang.brigadier.arguments.ArgumentType<Object>)FloatArgumentType.floatArg(0.0f)).then(((RequiredArgumentBuilder)Commands.argument("maxRange", (com.mojang.brigadier.arguments.ArgumentType<Object>)FloatArgumentType.floatArg(1.0f)).then(Commands.argument("respectTeams", (com.mojang.brigadier.arguments.ArgumentType<Object>)BoolArgumentType.bool()).then(Commands.argument("targets", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.entities()).executes(commandContext -> spreadPlayers((CommandSourceStack)commandContext.getSource(), Vec2Argument.getVec2((CommandContext<CommandSourceStack>)commandContext, "center"), FloatArgumentType.getFloat(commandContext, "spreadDistance"), FloatArgumentType.getFloat(commandContext, "maxRange"), 256, BoolArgumentType.getBool(commandContext, "respectTeams"), EntityArgument.getEntities((CommandContext<CommandSourceStack>)commandContext, "targets")))))).then(Commands.literal("under").then(Commands.argument("maxHeight", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer(0)).then(Commands.argument("respectTeams", (com.mojang.brigadier.arguments.ArgumentType<Object>)BoolArgumentType.bool()).then(Commands.argument("targets", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.entities()).executes(commandContext -> spreadPlayers((CommandSourceStack)commandContext.getSource(), Vec2Argument.getVec2((CommandContext<CommandSourceStack>)commandContext, "center"), FloatArgumentType.getFloat(commandContext, "spreadDistance"), FloatArgumentType.getFloat(commandContext, "maxRange"), IntegerArgumentType.getInteger(commandContext, "maxHeight"), BoolArgumentType.getBool(commandContext, "respectTeams"), EntityArgument.getEntities((CommandContext<CommandSourceStack>)commandContext, "targets")))))))))));
    }
    
    private static int spreadPlayers(final CommandSourceStack db, final Vec2 dcj, final float float3, final float float4, final int integer, final boolean boolean6, final Collection<? extends Entity> collection) throws CommandSyntaxException {
        final Random random8 = new Random();
        final double double9 = dcj.x - float4;
        final double double10 = dcj.y - float4;
        final double double11 = dcj.x + float4;
        final double double12 = dcj.y + float4;
        final Position[] arr17 = createInitialPositions(random8, boolean6 ? getNumberOfTeams(collection) : collection.size(), double9, double10, double11, double12);
        spreadPositions(dcj, float3, db.getLevel(), random8, double9, double10, double11, double12, integer, arr17, boolean6);
        final double double13 = setPlayerPositions(collection, db.getLevel(), arr17, integer, boolean6);
        db.sendSuccess(new TranslatableComponent(new StringBuilder().append("commands.spreadplayers.success.").append(boolean6 ? "teams" : "entities").toString(), new Object[] { arr17.length, dcj.x, dcj.y, String.format(Locale.ROOT, "%.2f", new Object[] { double13 }) }), true);
        return arr17.length;
    }
    
    private static int getNumberOfTeams(final Collection<? extends Entity> collection) {
        final Set<Team> set2 = (Set<Team>)Sets.newHashSet();
        for (final Entity apx4 : collection) {
            if (apx4 instanceof Player) {
                set2.add(apx4.getTeam());
            }
            else {
                set2.add(null);
            }
        }
        return set2.size();
    }
    
    private static void spreadPositions(final Vec2 dcj, final double double2, final ServerLevel aag, final Random random, final double double5, final double double6, final double double7, final double double8, final int integer, final Position[] arr, final boolean boolean11) throws CommandSyntaxException {
        boolean boolean12 = true;
        double double9 = 3.4028234663852886E38;
        int integer2;
        for (integer2 = 0; integer2 < 10000 && boolean12; ++integer2) {
            boolean12 = false;
            double9 = 3.4028234663852886E38;
            for (int integer3 = 0; integer3 < arr.length; ++integer3) {
                final Position a22 = arr[integer3];
                int integer4 = 0;
                final Position a23 = new Position();
                for (int integer5 = 0; integer5 < arr.length; ++integer5) {
                    if (integer3 != integer5) {
                        final Position a24 = arr[integer5];
                        final double double10 = a22.dist(a24);
                        double9 = Math.min(double10, double9);
                        if (double10 < double2) {
                            ++integer4;
                            a23.x += a24.x - a22.x;
                            a23.z += a24.z - a22.z;
                        }
                    }
                }
                if (integer4 > 0) {
                    a23.x /= integer4;
                    a23.z /= integer4;
                    final double double11 = a23.getLength();
                    if (double11 > 0.0) {
                        a23.normalize();
                        a22.moveAway(a23);
                    }
                    else {
                        a22.randomize(random, double5, double6, double7, double8);
                    }
                    boolean12 = true;
                }
                if (a22.clamp(double5, double6, double7, double8)) {
                    boolean12 = true;
                }
            }
            if (!boolean12) {
                for (final Position a23 : arr) {
                    if (!a23.isSafe(aag, integer)) {
                        a23.randomize(random, double5, double6, double7, double8);
                        boolean12 = true;
                    }
                }
            }
        }
        if (double9 == 3.4028234663852886E38) {
            double9 = 0.0;
        }
        if (integer2 < 10000) {
            return;
        }
        if (boolean11) {
            throw SpreadPlayersCommand.ERROR_FAILED_TO_SPREAD_TEAMS.create(arr.length, dcj.x, dcj.y, String.format(Locale.ROOT, "%.2f", new Object[] { double9 }));
        }
        throw SpreadPlayersCommand.ERROR_FAILED_TO_SPREAD_ENTITIES.create(arr.length, dcj.x, dcj.y, String.format(Locale.ROOT, "%.2f", new Object[] { double9 }));
    }
    
    private static double setPlayerPositions(final Collection<? extends Entity> collection, final ServerLevel aag, final Position[] arr, final int integer, final boolean boolean5) {
        double double6 = 0.0;
        int integer2 = 0;
        final Map<Team, Position> map9 = (Map<Team, Position>)Maps.newHashMap();
        for (final Entity apx11 : collection) {
            Position a12;
            if (boolean5) {
                final Team ddm13 = (apx11 instanceof Player) ? apx11.getTeam() : null;
                if (!map9.containsKey(ddm13)) {
                    map9.put(ddm13, arr[integer2++]);
                }
                a12 = (Position)map9.get(ddm13);
            }
            else {
                a12 = arr[integer2++];
            }
            apx11.teleportToWithTicket(Mth.floor(a12.x) + 0.5, a12.getSpawnY(aag, integer), Mth.floor(a12.z) + 0.5);
            double double7 = Double.MAX_VALUE;
            for (final Position a13 : arr) {
                if (a12 != a13) {
                    final double double8 = a12.dist(a13);
                    double7 = Math.min(double8, double7);
                }
            }
            double6 += double7;
        }
        if (collection.size() < 2) {
            return 0.0;
        }
        double6 /= collection.size();
        return double6;
    }
    
    private static Position[] createInitialPositions(final Random random, final int integer, final double double3, final double double4, final double double5, final double double6) {
        final Position[] arr11 = new Position[integer];
        for (int integer2 = 0; integer2 < arr11.length; ++integer2) {
            final Position a13 = new Position();
            a13.randomize(random, double3, double4, double5, double6);
            arr11[integer2] = a13;
        }
        return arr11;
    }
    
    static {
        ERROR_FAILED_TO_SPREAD_TEAMS = new Dynamic4CommandExceptionType((object1, object2, object3, object4) -> new TranslatableComponent("commands.spreadplayers.failed.teams", new Object[] { object1, object2, object3, object4 }));
        ERROR_FAILED_TO_SPREAD_ENTITIES = new Dynamic4CommandExceptionType((object1, object2, object3, object4) -> new TranslatableComponent("commands.spreadplayers.failed.entities", new Object[] { object1, object2, object3, object4 }));
    }
    
    static class Position {
        private double x;
        private double z;
        
        double dist(final Position a) {
            final double double3 = this.x - a.x;
            final double double4 = this.z - a.z;
            return Math.sqrt(double3 * double3 + double4 * double4);
        }
        
        void normalize() {
            final double double2 = this.getLength();
            this.x /= double2;
            this.z /= double2;
        }
        
        float getLength() {
            return Mth.sqrt(this.x * this.x + this.z * this.z);
        }
        
        public void moveAway(final Position a) {
            this.x -= a.x;
            this.z -= a.z;
        }
        
        public boolean clamp(final double double1, final double double2, final double double3, final double double4) {
            boolean boolean10 = false;
            if (this.x < double1) {
                this.x = double1;
                boolean10 = true;
            }
            else if (this.x > double3) {
                this.x = double3;
                boolean10 = true;
            }
            if (this.z < double2) {
                this.z = double2;
                boolean10 = true;
            }
            else if (this.z > double4) {
                this.z = double4;
                boolean10 = true;
            }
            return boolean10;
        }
        
        public int getSpawnY(final BlockGetter bqz, final int integer) {
            final BlockPos.MutableBlockPos a4 = new BlockPos.MutableBlockPos(this.x, integer + 1, this.z);
            boolean boolean5 = bqz.getBlockState(a4).isAir();
            a4.move(Direction.DOWN);
            boolean boolean6 = bqz.getBlockState(a4).isAir();
            while (a4.getY() > 0) {
                a4.move(Direction.DOWN);
                final boolean boolean7 = bqz.getBlockState(a4).isAir();
                if (!boolean7 && boolean6 && boolean5) {
                    return a4.getY() + 1;
                }
                boolean5 = boolean6;
                boolean6 = boolean7;
            }
            return integer + 1;
        }
        
        public boolean isSafe(final BlockGetter bqz, final int integer) {
            final BlockPos fx4 = new BlockPos(this.x, this.getSpawnY(bqz, integer) - 1, this.z);
            final BlockState cee5 = bqz.getBlockState(fx4);
            final Material cux6 = cee5.getMaterial();
            return fx4.getY() < integer && !cux6.isLiquid() && cux6 != Material.FIRE;
        }
        
        public void randomize(final Random random, final double double2, final double double3, final double double4, final double double5) {
            this.x = Mth.nextDouble(random, double2, double4);
            this.z = Mth.nextDouble(random, double3, double5);
        }
    }
}

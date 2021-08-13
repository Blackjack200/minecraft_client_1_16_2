package net.minecraft.server.commands.data;

import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;

public interface DataAccessor {
    void setData(final CompoundTag md) throws CommandSyntaxException;
    
    CompoundTag getData() throws CommandSyntaxException;
    
    Component getModifiedSuccess();
    
    Component getPrintSuccess(final Tag mt);
    
    Component getPrintSuccess(final NbtPathArgument.NbtPath h, final double double2, final int integer);
}

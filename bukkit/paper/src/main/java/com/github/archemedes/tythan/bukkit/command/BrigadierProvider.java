package com.github.archemedes.tythan.bukkit.command;

import com.comphenix.protocol.utility.MinecraftReflection;
import com.github.archemedes.tythan.Tythan;
import com.github.archemedes.tythan.bukkit.TythanBukkit;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang.Validate;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class BrigadierProvider {

    /**
     * THIS CLASS WILL BE A MESS FOR NOW SINCE THIS PROJECT WORK HAS A MUCH BETTER AND PAINLESS NMS A LOT OF CLEAN UP IS GOING TO BE MADE
     */
    private static final BrigadierProvider INSTANCE = new BrigadierProvider();
    public static BrigadierProvider get() { return INSTANCE; }

    @Getter private boolean functional = true;

    private Method getServer;
    private Method getBrigadier;
    private Method getItemStack;

    private Constructor<ArgumentType<Object>> itemStackArgument;

    private Method getBukkitSender = null;

    /**
     * 1.18 getCommandDispatcher = aA
     * 1.19 getCommandDispatcher = aC
     * */

    @SuppressWarnings("unchecked")
    private BrigadierProvider() {
        try {
            var serverClass = MinecraftReflection.getMinecraftServerClass();
            getServer = serverClass.getMethod("getServer");
            //getItemStack = reflectItemStackGetter();
            //temStackArgument = (Constructor<ArgumentType<Object>>) MinecraftReflection.getMinecraftClass("ArgumentItemStack").getConstructor();
        } catch(Exception e) {
            Tythan.get().getLogger().severe("We were unable to set up the BrigadierProvider. Likely a reflection error!");
            functional = false;
            e.printStackTrace();
        }
    }

    private @NotNull Method reflectItemStackGetter() throws Exception {
        var dispatcherClass = MinecraftReflection.getMinecraftClass("ArgumentPredicateItemStack");
        var itemStackClass = MinecraftReflection.getItemStackClass();
        for(var xx : dispatcherClass.getDeclaredMethods()) {
            if(xx.getParameterCount() == 2
                    && itemStackClass.isAssignableFrom(xx.getReturnType())
                    && xx.getParameters()[0].getType() == int.class
                    && xx.getParameters()[1].getType() == boolean.class)
                return xx;
        }
        throw new NoSuchMethodError("ArgumentPredicateItemStack getter for ItemStack");
    }

    @SuppressWarnings("unchecked")
    public CommandDispatcher<Object> getBrigadier() {
        return TythanBukkit.get().getMcVerHandler().getTythanNMS().getDispatcherCommand();
    }

    public ArgumentType<Object> argumentItemStack(){
        try {
            return itemStackArgument.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Method getItemStackParser() {
        return getItemStack;
    }

    @SneakyThrows
    public CommandSender getBukkitSender(Object commandListenerWrapper) {
        if(getBukkitSender == null) getBukkitSender = commandListenerWrapper.getClass().getMethod("getBukkitSender");
        return (CommandSender) getBukkitSender.invoke(commandListenerWrapper);
    }
}

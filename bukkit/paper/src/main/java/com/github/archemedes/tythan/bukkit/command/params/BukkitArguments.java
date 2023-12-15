package com.github.archemedes.tythan.bukkit.command.params;

import com.github.archemedes.tythan.bukkit.command.Commands;
import lombok.experimental.UtilityClass;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;


@UtilityClass
public class BukkitArguments {
    public static void buildBukkitParameter() {
        Commands.defineArgumentType(World.class)
                .defaultName("World")
                .defaultError("Could not find world by the name")
                .mapper(Bukkit::getWorld)
                .completer(()->{
                    Collection<String> world = new HashSet<>();
                    Bukkit.getWorlds().forEach(w->world.add(w.getName()));
                    return world;
                })
                .register();
        Commands.defineArgumentType(Material.class)
                .defaultName("Material")
                .defaultError("Could not find material by the name")
                .mapper(s-> Material.valueOf(s.toUpperCase()))
                .completer(()->{
                    Collection<String> data = new HashSet<>();
                    Arrays.stream(Material.values()).forEach(x->data.add(x.name()));
                    return data;
                })
                .register();

        Commands.defineArgumentType(ItemStack.class)
                .defaultName("ItemStack")
                .defaultError("Could not find the item by that name!")
                .mapper(s->{
                    try {
                        var item = new ItemStack(Material.getMaterial(s.toUpperCase()));
                        if (item!=null) return item;
                    } catch (Exception ignored) {}
                    return null;
                })
                .completer((s,i)-> Arrays.stream(Material.values()).map(Material::name).toList())
                .register();


        Commands.defineArgumentType(Particle.class)
                .defaultName("Particles")
                .defaultError("Could not find particle by the name")
                .mapper(Particle::valueOf)
                .completer(()->{
                    Collection<String> data = new HashSet<>();
                    Arrays.stream(Particle.values()).forEach(x->data.add(x.name()));
                    return data;
                })
                .register();
        Commands.defineArgumentType(Sound.class)
                .defaultName("Sound")
                .defaultError("Could not find material by the name")
                .mapper(Sound::valueOf)
                .completer(()->{
                    Collection<String> data = new HashSet<>();
                    Arrays.stream(Sound.values()).forEach(x->data.add(x.name()));
                    return data;
                })
                .register();

        Commands.defineArgumentType(Enchantment.class)
                .defaultName("Enchantment")
                .defaultError("Could not find enchantment by that name")
                .mapper(Enchantment::getByName)
                .completer((s,i)-> Arrays.stream(Enchantment.values()).filter(Objects::nonNull).map(Enchantment::getName).collect(Collectors.toList()))
                .register();

    }

}

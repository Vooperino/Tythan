package com.github.archemedes.tythan.manager;

import com.github.archemedes.tythan.Tythan;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class TythanCoreManager {
    private static HashSet<ITythanManager> coreManagers = new HashSet<>();

    @Getter private TythanCoreConfig configManager;

    public void startManager() {
        this.initObjects();
        this.registerCoreManagers();
    }
    public void stopManager() {
        var cm = new HashSet<>(coreManagers);
        cm.forEach(this::unregister);
    }

    private void initObjects() {
        this.configManager = new TythanCoreConfig();
    }

    private void registerCoreManagers() {
        this.register(this.configManager);
    }

    private void register(@NotNull ITythanManager manager) {
        if (coreManagers.contains(manager)) {
            Tythan.get().getLogger().warning(manager.getClass().getSimpleName()+" core manager already registered!");
            return;
        }
        try {
            manager.onEnable();
        } catch (Exception e) {e.printStackTrace();} finally {
            Tythan.get().getLogger().info(manager.getClass().getSimpleName()+" core manager registered!");
            coreManagers.add(manager);
        }
    }
    private void unregister(@NotNull ITythanManager manager) {
        if (!coreManagers.contains(manager)) {
            Tythan.get().getLogger().warning(manager.getClass().getSimpleName()+" core manager is not registered!");
            return;
        }
        try {
            manager.onDisable();
        } catch (Exception e) {e.printStackTrace();} finally {
            Tythan.get().getLogger().info(manager.getClass().getSimpleName()+" core manager unregistered!");
            coreManagers.remove(manager);
        }
    }
}

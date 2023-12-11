package com.github.archemedes.tythan.manager;

public interface ITythanManager {
    void onEnable();
    default void onDisable() {}
}

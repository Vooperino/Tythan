package com.github.archemedes.tythan.agnostic;

import org.jetbrains.annotations.NotNull;

public interface PluginOwned<T> {
    @NotNull T getPlugin();
}

package com.github.archemedes.tythan.agnostic;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Command {
    @Nullable String getPermission();
    @NotNull String getName();
    @Nullable String getDescription();
    @Nullable List<String> getAliases();

}

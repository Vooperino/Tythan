package com.github.archemedes.tythan;

import org.jetbrains.annotations.NotNull;

public class TythanInstanceProvider {
    static Tythan INSTANCE = null;

    public TythanInstanceProvider(@NotNull Tythan tythan) {
        if (INSTANCE!=null) throw new IllegalStateException("Tythan could not be initialized as it was already initialized!");
        INSTANCE = tythan;
        if (!INSTANCE.getRootDirectory().exists()) INSTANCE.getRootDirectory().mkdir();
    }

    public void startTythanCore() {}

    public void stopTythanCore() {}

}

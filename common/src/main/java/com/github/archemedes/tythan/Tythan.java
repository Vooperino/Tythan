package com.github.archemedes.tythan;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.util.logging.Logger;

public interface Tythan {

    static Tythan get() {return TythanInstanceProvider.INSTANCE;}

    @NotNull @Unmodifiable File getRootDirectory();
    @NotNull @Unmodifiable Logger getLogger();

}

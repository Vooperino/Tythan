package com.github.archemedes.tythan;

import com.github.archemedes.tythan.manager.TythanCoreManager;
import com.github.archemedes.tythan.utils.PluginDebugLogger;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Level;

public class TythanInstanceProvider {
    static Tythan INSTANCE = null;
    @Getter private static TythanCoreManager coreManager;
    @Getter private static PluginDebugLogger debugLogger;

    private TythanInstanceProvider() {}

    public static void startTythanCore(@NotNull Tythan tythan) {
        if (INSTANCE!=null) throw new IllegalStateException("Tythan could not be initialized as it was already initialized!");
        INSTANCE = tythan;
        if (!INSTANCE.getRootDirectory().exists()) INSTANCE.getRootDirectory().mkdir();
        debugLogger = new PluginDebugLogger(INSTANCE.getRootDirectory(),INSTANCE.getLogger());
        coreManager = new TythanCoreManager();
        coreManager.startManager();
    }

    public static void stopTythanCore() {
        if (INSTANCE==null) throw new IllegalStateException("Tythan was not initialized!");
        coreManager.stopManager();
    }

    public static void debug(Level level, String message) {
        debugLogger.log(level,message);
    }


}

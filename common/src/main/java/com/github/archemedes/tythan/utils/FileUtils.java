package com.github.archemedes.tythan.utils;

import com.github.archemedes.tythan.Tythan;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.util.logging.Logger;


@UtilityClass
public class FileUtils {
    public static void copyFile(@NotNull InputStream in, @NotNull File file) {
        try{
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0) out.write(buf,0,len);
            out.close();
            in.close();
        } catch(Exception e){e.printStackTrace();}
    }

    public static void validateFolder(@NotNull File file, @NotNull Logger logger, boolean silent){
        if (!file.exists()){
            if (!silent) logger.info("Attempting to create a folder named "+file.getName());
            file.mkdirs();
            if (!file.exists()) if (!silent) logger.severe("Failed to create folder! (Take a look of the throwable if there's any!)");
            else if (!silent) logger.info("Created folder in "+file.getPath());
        }
        return;
    }
    public static void validateFolder(@NotNull File file, boolean silent){
        if (!file.exists()){
            if (!silent) Tythan.get().getLogger().info("Attempting to create a folder named "+file.getName());
            file.mkdirs();
            if (!file.exists()) if (!silent) Tythan.get().getLogger().severe("Failed to create folder! (Take a look of the throwable if there's any!)");
            else if (!silent) Tythan.get().getLogger().info("Created folder in "+file.getPath());
        }
        return;
    }
    @SneakyThrows public static void validateFile(@NotNull File file, @NotNull Logger logger, boolean silent) {
        if (!file.exists()){
            if (!silent) logger.info("Attempting to create a file named "+file.getName());
            file.createNewFile();
            if (!file.exists()) if (!silent) logger.severe("Failed to create file (Take a look of the throwable)!");
            else if (!silent) logger.info("Created file in "+file.getPath());
        }
        return;
    }
    @SneakyThrows public static void validateFile(@NotNull File file, boolean silent) {
        if (!file.exists()){
            if (!silent) Tythan.get().getLogger().info("Attempting to create a file named "+file.getName());
            file.createNewFile();
            if (!file.exists()) if (!silent) Tythan.get().getLogger().severe("Failed to create file (Take a look of the throwable)!");
            else if (!silent) Tythan.get().getLogger().info("Created file in "+file.getPath());
        }
        return;
    }
    @SneakyThrows public static @NotNull String getChecksum(@NotNull File file) {
        InputStream is = Files.newInputStream(file.toPath());
        return DigestUtils.md5Hex(is);
    }

}

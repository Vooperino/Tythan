package com.github.archemedes.tythan.utils;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PluginDebugLogger {
    @NotNull @Unmodifiable private final File pluginRootDirectory;
    @NotNull @Unmodifiable private final Logger logger;

    @Getter @Setter private boolean debugMode;

    public PluginDebugLogger(@NotNull @Unmodifiable File pluginRootDirectory, @NotNull @Unmodifiable Logger logger) {
        this.pluginRootDirectory = pluginRootDirectory;
        this.logger = logger;
        this.debugMode = false;
    }

    public void log(Level level, String message) {
        if (!this.debugMode) return;
        File debug_folder = new File(this.pluginRootDirectory+File.separator+"debug-logger");
        FileUtils.validateFolder(debug_folder,true);
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        SimpleDateFormat format_file = new SimpleDateFormat("dd-MM-yyyy");
        File debugfile = new File(debug_folder+File.separator+"debug_log-"+format_file.format(now)+".txt");
        this.logger.log(level,"(Debug) "+message);
        FileUtils.validateFile(debugfile,true);
        if(debugfile.exists()) {
            FileWriter fw = null;
            BufferedWriter bw = null;
            PrintWriter out = null;
            try {
                fw = new FileWriter(debugfile, true);
                bw = new BufferedWriter(fw);
                out = new PrintWriter(bw);
                out.println("("+ ZoneId.systemDefault()+") ["+format.format(now)+"]: "+ message);
                out.close();
            } catch (IOException e){
                this.logger.log(Level.SEVERE,"(Debug) An Error Has been encountered while logging debug event!");
                e.printStackTrace();
            } finally {
                try {
                    if (out!=null)out.close();
                    if (bw!=null)bw.close();
                    if (fw!=null)fw.close();
                } catch (IOException e) {
                    this.logger.log(Level.SEVERE,"(Debug) An Error Has been encountered while saving logged debug event!");
                    e.printStackTrace();
                }
            }
        } else this.logger.log(Level.SEVERE,"(Debug) Failed locate debug log text file ignoring logging for this debug event!");
    }
}

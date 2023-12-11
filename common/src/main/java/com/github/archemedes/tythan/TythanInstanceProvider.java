package com.github.archemedes.tythan;

import com.github.archemedes.tythan.manager.TythanCoreManager;
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

    private TythanInstanceProvider() {}

    public static void startTythanCore(@NotNull Tythan tythan) {
        if (INSTANCE!=null) throw new IllegalStateException("Tythan could not be initialized as it was already initialized!");
        INSTANCE = tythan;
        if (!INSTANCE.getRootDirectory().exists()) INSTANCE.getRootDirectory().mkdir();
        coreManager = new TythanCoreManager();
        coreManager.startManager();
    }

    public static void stopTythanCore() {
        if (INSTANCE==null) throw new IllegalStateException("Tythan was not initialized!");
        coreManager.stopManager();
    }

    public static void debug(Level level, String message) {
        if (!coreManager.getConfigManager().getData().isDebug()) return;
        File debug_folder = new File(INSTANCE.getRootDirectory()+File.separator+"debug");
        if (!debug_folder.exists()) debug_folder.mkdir();
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        SimpleDateFormat format_file = new SimpleDateFormat("dd-MM-yyyy");
        File debugfile = new File(debug_folder+File.separator+"debug_log-"+format_file.format(now)+".txt");
        INSTANCE.getLogger().log(level,"(Debug) "+message);
        if(!debugfile.exists()) {
            try {
                debugfile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                INSTANCE.getLogger().log(Level.SEVERE,"(Debug) Failed to create debug log text file!");
            }
        }
        if(debugfile.exists()) {
            FileWriter fw = null;
            BufferedWriter bw = null;
            PrintWriter out = null;
            try {
                fw = new FileWriter(debugfile, true);
                bw = new BufferedWriter(fw);
                out = new PrintWriter(bw);
                out.println("("+ ZoneId.systemDefault()+") ["+format.format(now)+"] (Essn-Debug): "+ message);
                out.close();
            } catch (IOException e){
                INSTANCE.getLogger().log(Level.SEVERE,"(Debug) An Error Has been encountered while logging debug event!");
                e.printStackTrace();
            } finally {
                try {
                    if (out!=null)out.close();
                    if (bw!=null)bw.close();
                    if (fw!=null)fw.close();
                } catch (IOException e) {
                    INSTANCE.getLogger().log(Level.SEVERE,"(Debug) An Error Has been encountered while saving logged debug event!");
                    e.printStackTrace();
                }
            }
        } else INSTANCE.getLogger().log(Level.SEVERE,"(Debug) Failed locate debug log text file ignoring logging for this debug event!");
    }


}

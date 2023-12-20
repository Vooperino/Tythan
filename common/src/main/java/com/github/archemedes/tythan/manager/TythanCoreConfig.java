package com.github.archemedes.tythan.manager;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.github.archemedes.tythan.Tythan;
import com.github.archemedes.tythan.TythanInstanceProvider;
import com.github.archemedes.tythan.utils.FileUtils;
import com.github.archemedes.tythan.utils.config.ConfigDefaultValueChecker;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.File;

public class TythanCoreConfig implements ITythanManager {

    @Getter private File configuration_file;
    @Getter private ConfigData data;

    @Override
    public void onEnable() {
        this.configuration_file = new File(Tythan.get().getRootDirectory(),"CoreConfig.toml");
        this.reload();
    }

    public void reload() {
        FileUtils.validateFile(this.configuration_file,false);
        this.validateConfigFile();
        this.data = null;
        this.data = new ConfigData();
        this.data.obtainData();
    }

    private void validateConfigFile() {
        FileConfig cfg = FileConfig.of(this.configuration_file);
        new ConfigDefaultValueChecker(cfg)
                .append("debug",false)
                .append("Commands.settings.ShowErrorArg",false)
                .execute();
    }

    @Getter @EqualsAndHashCode public class ConfigData {

        private boolean debug;
        private boolean commandSettingsShowErrorArg;
        void obtainData() {
            FileConfig cfg = FileConfig.of(configuration_file);
            cfg.load();
            this.debug = cfg.get("debug");
            this.commandSettingsShowErrorArg = cfg.get("Commands.settings.ShowErrorArg");
            cfg.close();

            TythanInstanceProvider.getDebugLogger().setDebugMode(this.debug);
        }
    }
}

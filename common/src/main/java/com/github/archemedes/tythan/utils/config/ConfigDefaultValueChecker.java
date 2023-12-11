package com.github.archemedes.tythan.utils.config;

import com.electronwill.nightconfig.core.file.FileConfig;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;

@RequiredArgsConstructor
public class ConfigDefaultValueChecker {
    private final FileConfig fileConfig;
    private HashMap<String,Object> values = new HashMap<>();

    public ConfigDefaultValueChecker append(String entry, Object value) {
        if (!values.containsKey(entry)) values.put(entry,value);
        return this;
    }

    public void execute() {
        fileConfig.load();
        for (String entry : values.keySet()) {
            Object value = this.values.get(entry);
            if (fileConfig.get(entry)==null) fileConfig.set(entry,value);
        }
        fileConfig.save();
        fileConfig.close();
    }
}

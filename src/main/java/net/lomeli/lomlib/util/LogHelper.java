package net.lomeli.lomlib.util;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;

public class LogHelper {
    private String modName;

    public LogHelper(String modName) {
        this.modName = modName;
    }

    public void log(Level logLevel, String message) {
        FMLLog.log(modName, logLevel, message, new Object[] {});
    }

    public void logBasic(String message) {
        log(Level.INFO, message);
    }

    public void logWarning(String message) {
        log(Level.WARN, message);
    }

    public void logInfo(String message) {
        log(Level.INFO, message);
    }

    public void logError(String message) {
        log(Level.FATAL, message);
    }
}

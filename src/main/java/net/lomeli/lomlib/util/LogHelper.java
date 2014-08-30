package net.lomeli.lomlib.util;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;

public class LogHelper {
    private String modName;

    public LogHelper(String modName) {
        this.modName = modName;
    }

    public void log(Level logLevel, Object message) {
        FMLLog.log(this.modName, logLevel, String.valueOf(message));
    }

    public void logBasic(Object message) {
        log(Level.INFO, message);
    }

    public void logWarning(Object message) {
        log(Level.WARN, message);
    }

    public void logInfo(Object message) {
        log(Level.INFO, message);
    }

    public void logError(Object message) {
        log(Level.ERROR, message);
    }

    public void logException(Exception e) {
        FMLLog.log(this.modName, Level.ERROR, e, null);
    }
}

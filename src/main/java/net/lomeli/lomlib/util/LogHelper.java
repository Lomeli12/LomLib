package net.lomeli.lomlib.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogHelper {
    private String modName;
    private Logger logger;

    public LogHelper(String modName) {
        this.modName = modName;
        logger = Logger.getLogger(this.modName);
    }

    public void log(Level logLevel, String message) {
        logger.log(logLevel, "[" + this.modName + "]: " + message);
    }

    public void logBasic(String message) {
        log(Level.INFO, message);
    }

    public void logWarning(String message) {
        log(Level.WARNING, message);
    }

    public void logInfo(String message) {
        log(Level.INFO, message);
    }

    public void logError(String message) {
        log(Level.SEVERE, message);
    }
}

package net.lomeli.lomlib.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.FMLLog;

/**
 * A very basic logger class
 */
public class LogHelper {
    private String modName;
    private Logger logger;

    private LogHelper(String modName) {
        this.modName = modName;
        this.logger = LogManager.getLogger(modName);
    }

    public static LogHelper createLogger(String mod) {
        return new LogHelper(mod);
    }

    public void log(Level logLevel, Object message) {
        this.logger.log(logLevel, "[" + modName + "] " + String.valueOf(message));
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
        this.logger.log(Level.ERROR, "", e);
    }

    public Logger getLogger() {
        return logger;
    }

    public String getModName() {
        return modName;
    }
}

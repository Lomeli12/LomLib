package net.lomeli.lomlib.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    public void log(Level logLevel, Object message, Object... args) {
        this.logger.log(logLevel, "[" + modName + "]: " + String.format(String.valueOf(message), args));
    }

    public void logBasic(Object message, Object... args) {
        log(Level.INFO, message, args);
    }

    public void logWarning(Object message, Object... args) {
        log(Level.WARN, message, args);
    }

    public void logInfo(Object message, Object... args) {
        log(Level.INFO, message, args);
    }

    public void logError(Object message, Object... args) {
        log(Level.ERROR, message, args);
    }

    public void logException(Exception e) {
        this.logger.log(Level.ERROR, "", e);
    }

    public void logDebug(Object message, Object... args) {
        if (ObfUtil.isObf())
            log(Level.DEBUG, args);
    }

    public Logger getLogger() {
        return logger;
    }
}

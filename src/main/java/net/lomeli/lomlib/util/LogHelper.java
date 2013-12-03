package net.lomeli.lomlib.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import cpw.mods.fml.common.FMLLog;

public class LogHelper {
    private Logger modLogger;

    public LogHelper(String modName) {
        modLogger = Logger.getLogger(modName);
        init();
    }

    public void init() {
        modLogger.setParent(FMLLog.getLogger());
    }

    public void log(Level logLevel, String message) {
        modLogger.log(logLevel, message);
    }

}

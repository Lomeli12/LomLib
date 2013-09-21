package net.lomeli.lomlib.util;

import java.util.logging.Level;

import cpw.mods.fml.common.Loader;

import net.lomeli.lomlib.LomLib;

public class ModLoaded {

    public static boolean isModInstalled(String modID, String modName) {
        return isModInstalled(modID, modName, false);
    }

    public static boolean isModInstalled(String modID, String modName,
            boolean display) {
        boolean isInstalled = false;
        if(Loader.isModLoaded(modID)) {
            try {
                if(display)
                    LomLib.logger.log(Level.FINE, (modName + " is installed!"));

                isInstalled = true;
            }catch(Exception ex) {
                if(display)
                    LomLib.logger.log(Level.WARNING,
                            (modName + " is not installed!"));

                isInstalled = false;
            }
        }else {
            if(display)
                LomLib.logger.log(Level.WARNING,
                        (modName + " is not installed!"));

            isInstalled = false;
        }

        return isInstalled;
    }

    public static boolean isModInstalled(String modID) {
        return isModInstalled(modID, false);
    }

    public static boolean isModInstalled(String modID, boolean display) {
        boolean isInstalled = false;
        if(Loader.isModLoaded(modID)) {
            try {
                if(display)
                    LomLib.logger.log(Level.FINE, (modID + " is installed!"));

                isInstalled = true;
            }catch(Exception ex) {
                if(display)
                    LomLib.logger.log(Level.WARNING,
                            (modID + " is not installed!"));

                isInstalled = false;
            }
        }else {
            if(display)
                LomLib.logger
                        .log(Level.WARNING, (modID + " is not installed!"));

            isInstalled = false;
        }

        return isInstalled;
    }
}

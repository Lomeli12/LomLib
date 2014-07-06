package net.lomeli.lomlib.util;

import cpw.mods.fml.common.Loader;

import net.lomeli.lomlib.LomLib;

public class ModLoaded {

    public static boolean isModInstalled(String modID, String modName) {
        return isModInstalled(modID, modName, false);
    }

    public static boolean isModInstalled(String modID, String modName, boolean display) {
        boolean isInstalled = false;
        if (Loader.isModLoaded(modID)) {
            try {
                if (display)
                    LomLib.logger.logBasic(modName + " is installed!");

                isInstalled = true;
            } catch (Exception ex) {
                if (display)
                    LomLib.logger.logWarning(modName + " is not installed!");

                isInstalled = false;
            }
        } else {
            if (display)
                LomLib.logger.logWarning(modName + " is not installed!");

            isInstalled = false;
        }

        return isInstalled;
    }

    public static boolean isModInstalled(String modID) {
        return isModInstalled(modID, false);
    }

    public static boolean isModInstalled(String modID, boolean display) {
        boolean isInstalled = false;
        if (Loader.isModLoaded(modID)) {
            try {
                if (display)
                    LomLib.logger.logBasic(modID + " is installed!");

                isInstalled = true;
            } catch (Exception ex) {
                if (display)
                    LomLib.logger.logWarning(modID + " is not installed!");

                isInstalled = false;
            }
        } else {
            if (display)
                LomLib.logger.logWarning(modID + " is not installed!");

            isInstalled = false;
        }

        return isInstalled;
    }
}

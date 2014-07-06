package net.lomeli.lomlib.util;

import java.util.ArrayList;
import java.util.List;

import net.lomeli.lomlib.LomLib;

/**
 * Allows your mod to check if it's the latest version. You'll have to make a
 * tick handler (or something else) to tell the player about the update
 * <p/>
 * <p/>
 * <strong>How to use</strong> <br>
 * XML Example - http://bit.ly/1eqGV2D <br>
 * Main Class Example - http://paste.ubuntu.com/6126515/ <br>
 * TickHandler Example - http://paste.ubuntu.com/6126522/
 *
 * @author Lomeli12
 */
public class UpdateHelper {
    public static List<UpdateHelper> modList;
    private boolean isUpdated;
    private String modName;
    private String modID;
    private String downloadURL;
    private int[] latestVersion;

    public UpdateHelper(String modName) {
        this(modName, modName);
    }

    public UpdateHelper(String modName, String modId) {
        this.modName = modName;
        this.modID = modId;
        this.isUpdated = true;
    }

    public boolean isUpdate() {
        return isUpdated;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public String getModName() {
        return this.modName;
    }

    public String getModID() {
        return this.modID;
    }

    public void check(String updateUrl, int major, int minor, int revision) {
        checkForUpdates(updateUrl, major, minor, revision);
    }

    private void checkForUpdates(String updateURL, int major, int minor, int revision) {
        latestVersion = new int[]{XMLUtil.getInteger(updateURL, "majorBuildNumber"), XMLUtil.getInteger(updateURL, "minorBuildNumber"), XMLUtil.getInteger(updateURL, "revisionBuildNumber")};

        if (latestVersion[0] > major)
            isUpdated = false;
        else if (latestVersion[0] == major) {
            if (latestVersion[1] > minor)
                isUpdated = false;
            else if (latestVersion[1] == minor) {
                if (latestVersion[2] > revision)
                    isUpdated = false;
            }
        }

        if (!isUpdated) {
            downloadURL = XMLUtil.getStringValue(updateURL, "modURL");
            LomLib.logger.logInfo("A new version of " + getModName() + " can be downloaded at " + downloadURL);
            if (UpdateHelper.modList == null)
                UpdateHelper.modList = new ArrayList<UpdateHelper>();
            UpdateHelper.modList.add(this);
        }
    }

    public int[] getNewVersion() {
        return latestVersion;
    }
}

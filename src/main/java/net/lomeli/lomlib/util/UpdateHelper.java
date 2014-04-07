package net.lomeli.lomlib.util;

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
    private boolean isUpdated;
    private String downloadURL;

    public UpdateHelper() {
        isUpdated = true;
    }

    public boolean isUpdate() {
        return isUpdated;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public void check(String modname, String updateUrl, int major, int minor, int revision) {
        checkForUpdates(modname, updateUrl, major, minor, revision);
    }

    private void checkForUpdates(String modname, String updateURL, int major, int minor, int revision) {
        int[] latestVersion = {XMLUtil.getInteger(updateURL, "majorBuildNumber"),
                XMLUtil.getInteger(updateURL, "minorBuildNumber"), XMLUtil.getInteger(updateURL, "revisionBuildNumber")};
        int[] currentVersion = {major, minor, revision};

        for (int i = 0; i < 3; i++) {
            if (latestVersion[i] > currentVersion[i]) {
                isUpdated = false;
                downloadURL = XMLUtil.getStringValue(updateURL, "modURL");
                System.out.println("A new version of " + modname + " can be downloaded at " + downloadURL);
            }
        }
    }
}

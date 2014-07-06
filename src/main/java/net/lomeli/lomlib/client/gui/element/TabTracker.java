package net.lomeli.lomlib.client.gui.element;

public class TabTracker {

    private static Class<? extends TabBase> openedLeftTab;
    private static Class<? extends TabBase> openedRightTab;

    public static Class<? extends TabBase> getOpenedLeftTab() {
        return openedLeftTab;
    }

    public static void setOpenedLeftTab(Class<? extends TabBase> tabClass) {
        openedLeftTab = tabClass;
    }

    public static Class<? extends TabBase> getOpenedRightTab() {
        return openedRightTab;
    }

    public static void setOpenedRightTab(Class<? extends TabBase> tabClass) {
        openedRightTab = tabClass;
    }

}

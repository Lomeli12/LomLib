package net.lomeli.lomlib.client.gui.element;

import java.util.HashMap;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class IconRegistry {
    public static HashMap<String, IIcon> icons = new HashMap<String, IIcon>();

    public static void addIcon(String iconName, String iconLocation, IIconRegister ir) {
        icons.put(iconName, ir.registerIcon(iconLocation));
    }

    public static void addIcon(String iconName, IIcon icon) {
        icons.put(iconName, icon);
    }

    public static IIcon getIcon(String iconName) {
        return icons.get(iconName);
    }

    public static IIcon getIcon(String iconName, int iconOffset) {
        return icons.get(iconName + iconOffset);
    }
}

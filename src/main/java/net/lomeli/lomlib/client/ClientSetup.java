package net.lomeli.lomlib.client;

import net.lomeli.lomlib.LomLib;
import net.lomeli.lomlib.client.nei.NEIAddon;
import net.lomeli.lomlib.util.ModLoaded;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ClientSetup {
    
    @SideOnly(Side.CLIENT)
    public static void loadPreInit(){
        ResourceUtil.initResourceUtil();
        
        if(LomLib.capes)
            CapeUtil.getInstance().readXML();
    }
    
    @SideOnly(Side.CLIENT)
    public static void loadPostInit(){
        if(ModLoaded.isModInstalled("NotEnoughItems"))
            NEIAddon.loadAddon();
    }
}

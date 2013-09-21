package net.lomeli.lomlib;

import net.lomeli.lomlib.capes.CapeUtil;
import net.lomeli.lomlib.util.ResourceUtil;

public class ClientProxy extends CommonProxy {
    @Override
    public void loadCapes() {
        CapeUtil.getInstance().readXML();
    }
    
    @Override
    public void loadResources() {
        ResourceUtil.initResourceUtil();
    }
}

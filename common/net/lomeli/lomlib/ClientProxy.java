package net.lomeli.lomlib;

import net.lomeli.lomlib.capes.CapeUtil;

public class ClientProxy extends CommonProxy {
	@Override
	public void loadCapes(){
		CapeUtil.getInstance().readXML();
	}
}

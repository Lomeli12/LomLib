package net.lomeli.lomlib.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CustomCreativeTab extends CreativeTabs
{
	private String tabName;
	private ItemStack tabIcon;
	
	public CustomCreativeTab(int par1, String par2Str, ItemStack tab)
    {
	    super(par1, par2Str);
	    tabName = par2Str;
	    tabIcon = tab;
    }
	
	public CustomCreativeTab(int par1, String par2Str, Item tab)
    {
	    super(par1, par2Str);
	    tabName = par2Str;
	    tabIcon = new ItemStack(tab);
    }

	@Override
    public ItemStack getIconItemStack()
    {
		return tabIcon;
    }
	
	@Override
    public String getTranslatedTabLabel()
    {
        return tabName;
    }
}

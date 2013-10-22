package net.lomeli.lomlib.util;

import net.minecraft.client.Minecraft;

public class LocalizationManager {
    protected String localizationFile, modId, defaultLang;
    
    public LocalizationManager(String modID, String defaultLang){
        this.modId = modID;
        this.localizationFile = Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode();
        this.defaultLang = defaultLang;
    }
    
    
    

}
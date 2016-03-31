package net.lomeli.lomlib.core.version;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.net.URL;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.lomeli.lomlib.LomLib;
import net.lomeli.lomlib.util.LangUtil;

/**
 * An example of the JSON file you'll need online can be seen <a href="http://paste.ubuntu.com/10992152/">here</a> (now with comments!)
 *
 * @author Lomeli12
 */
public class VersionChecker implements Runnable {
    private int mod_major, mod_minor, mod_rev;
    private boolean needsUpdate, isDirect, doneTelling;
    private String version, downloadURL, jsonURL, modid, modname, currentVer;
    private String[] changeList;

    public VersionChecker(String jsonURL, String modid, String modname, int major, int minor, int rev) {
        this.jsonURL = jsonURL;
        this.modid = modid;
        this.modname = modname;
        this.mod_major = major;
        this.mod_minor = minor;
        this.mod_rev = rev;
        this.currentVer = this.mod_major + "." + this.mod_minor + "." + this.mod_rev;
        this.needsUpdate = false;
        this.isDirect = false;
        this.doneTelling = true;

        MinecraftForge.EVENT_BUS.register(this);
    }

    public VersionChecker(String jsonURL, String modid, String modname) {
        this(jsonURL, modid, modname, 0, 0, 0);
        int[] arr = getVersionFromID(modid);
        this.mod_major = arr[0];
        this.mod_minor = arr[1];
        this.mod_rev = arr[2];
        this.currentVer = this.mod_major + "." + this.mod_minor + "." + this.mod_rev;
    }

    private int[] getVersionFromID(String str) {
        int[] versionNumb = {0, 0, 0};
        String[] arr = str.split(".");
        for (int i = 0; i < 3; i++) {
            if (i < arr.length && i < versionNumb.length)
                versionNumb[i] = parseInt(arr[i]);
        }
        return versionNumb;
    }

    private int parseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void run() {
        try {
            LomLib.logger.logInfo(LangUtil.translate("message.lomlib.update.checking", this.modname));
            URL url = new URL(this.jsonURL);
            Gson gson = new Gson();
            UpdateJson update = gson.fromJson(new InputStreamReader(url.openStream()), UpdateJson.class);
            if (update != null) {
                this.needsUpdate = compareVersion(update);
                if (this.needsUpdate) {
                    this.downloadURL = update.getDownloadURL();
                    this.isDirect = update.isDirect();
                    this.changeList = update.getChangeLog();
                    this.version = update.getVersion();
                    this.doneTelling = false;
                    sendMessage();
                } else
                    LomLib.logger.logInfo(LangUtil.translate("message.lomlib.update.none", this.modname));
            }
        } catch (Exception e) {
            LomLib.logger.logError(LangUtil.translate("message.lomlib.update.failed", this.modname));
        }
    }

    private boolean compareVersion(UpdateJson update) {
        if (mod_major > update.getMajor()) return false;
        if (mod_major < update.getMajor()) return true;
        if (mod_minor > update.getMinor()) return false;
        if (mod_minor < update.getMinor()) return true;
        if (mod_rev > update.getRevision()) return false;
        if (mod_rev < update.getRevision()) return true;
        return false;
    }

    private void sendMessage() {
        if (Loader.isModLoaded("VersionChecker")) {
            String changeLog = "";
            if (this.changeList != null && this.changeList.length > 0) {
                for (String i : this.changeList)
                    changeLog += "- " + i;
            }

            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("modDisplayName", this.modname);
            tag.setString("oldVersion", this.currentVer);
            tag.setString("newVersion", this.version);
            tag.setString("updateUrl", this.downloadURL);
            tag.setBoolean("isDirectLink", this.isDirect);
            tag.setString("changeLog", changeLog);
            FMLInterModComms.sendMessage("VersionChecker", "addUpdate", tag);
        }
        LomLib.logger.logInfo(LangUtil.translate("message.lomlib.update.message", this.modname, this.downloadURL));
        LomLib.logger.logInfo(LangUtil.translate("message.lomlib.update.old", this.modname, this.currentVer));
        LomLib.logger.logInfo(LangUtil.translate("message.lomlib.update.new", this.modname, this.version));
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && Minecraft.getMinecraft().thePlayer != null) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            if (this.needsUpdate) {
                if (!this.version.isEmpty() && this.doneTelling) {
                    player.addChatComponentMessage(new TextComponentString(LangUtil.translate("message.lomlib.update.message", this.modname, this.downloadURL)));
                    if (this.changeList != null && this.changeList.length > 0) {
                        player.addChatComponentMessage(new TextComponentString(LangUtil.translate("message.lomlib.update.changelog")));
                        for (String change : changeList)
                            player.addChatComponentMessage(new TextComponentString("- " + change));
                    }
                    this.doneTelling = true;
                }
            }
        }
    }
}

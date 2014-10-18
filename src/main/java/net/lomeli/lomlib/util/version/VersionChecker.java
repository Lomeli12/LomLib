package net.lomeli.lomlib.util.version;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.apache.logging.log4j.Level;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.lomlib.LomLib;

import static cpw.mods.fml.relauncher.Side.CLIENT;

/**
 * A nicer version check implementation (based on what i use for Elemental Creepers)
 *
 * An example of the JSON file you'll need online can be seen <a href="http://paste.ubuntu.com/8464419/">here</a> (now with comments!)
 *
 * @author Lomeli12
 */
public class VersionChecker {
    public static HashMap<String, ModDownloader> updateList = new HashMap<String, ModDownloader>();
    private int mod_major, mod_minor, mod_rev;
    private long size;
    private boolean needsUpdate, isDirect, doneTelling;
    private String version, downloadURL, jsonURL, modid, modname, currentVer;
    private List<String> changeList;

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
        this.changeList = new ArrayList<String>();

        FMLCommonHandler.instance().bus().register(this);
    }

    public VersionChecker(String jsonURL, String mod, int major, int minor, int rev) {
        this(jsonURL, mod, mod, major, minor, rev);
    }

    public void checkForUpdates() {
        try {
            URL url = new URL(this.jsonURL);
            int major = 0, minor = 0, revision = 0;
            JsonParser parser = new JsonParser();
            JsonReader reader = new JsonReader(new InputStreamReader(url.openStream()));
            if (reader != null) {
                JsonElement element = parser.parse(reader);
                JsonObject jsonObject = element.getAsJsonObject();
                major = jsonObject.get("major").getAsInt();
                minor = jsonObject.get("minor").getAsInt();
                revision = jsonObject.get("revision").getAsInt();
                this.downloadURL = jsonObject.get("downloadURL").getAsString();
                this.isDirect = jsonObject.get("direct").getAsBoolean();
                this.size = jsonObject.get("fileSize").getAsLong();
                JsonArray changeLog = jsonObject.get("changeLog").getAsJsonArray();
                Iterator<JsonElement> it = changeLog.iterator();
                while (it.hasNext()) {
                    this.changeList.add(it.next().getAsString());
                }
                reader.endObject();
                reader.close();
            }

            this.needsUpdate = (this.mod_major > major ? (this.mod_minor > minor ? (this.mod_rev > revision ? false : true) : true ): true);
            if (this.needsUpdate) {
                this.version = major + "." + minor + "." + revision;
                this.doneTelling = false;
                sendMessage();
            }
        } catch (Exception e) {
            LomLib.logger.logError("Could not check for updates!");
            //LomLib.logger.logException(e);
        }
    }

    private String translate(String unlocalized) {
        return StatCollector.translateToLocal(unlocalized);
    }

    private void sendMessage() {
        if (Loader.isModLoaded("VersionChecker")) {
            String changeLog = "";
            for (String i : this.changeList)
                changeLog += "- " + i;

            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("modDisplayName", this.modname);
            tag.setString("oldVersion", this.currentVer);
            tag.setString("newVersion", this.version);
            tag.setString("updateUrl", this.downloadURL);
            tag.setBoolean("isDirectLink", this.isDirect);
            tag.setString("changeLog", changeLog);
            FMLInterModComms.sendMessage("VersionChecker", "addUpdate", tag);
        } else if (this.isDirect)
            updateList.put(this.modid, new ModDownloader(this.modname, this.downloadURL, this.size));
        FMLLog.log(Level.INFO, String.format(translate("message.lomlib.updateMessage"), this.modname, this.downloadURL));
        FMLLog.log(Level.INFO, translate("message.lomlib.updateOld") + " " + this.currentVer);
        FMLLog.log(Level.INFO, translate("message.lomlib.updateNew") + " " + this.version);
        if (!Loader.isModLoaded("VersionChecker")) {
            FMLLog.log(Level.INFO, String.format(translate("message.lomlib.updateDownloadP1"), this.modid));
            FMLLog.log(Level.INFO, translate("message.lomlib.updateDownloadP2"));
        }
    }

    @SubscribeEvent
    @SideOnly(CLIENT)
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && Minecraft.getMinecraft().thePlayer != null) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            if (this.needsUpdate) {
                if (!this.version.isEmpty() && this.doneTelling) {
                    player.addChatComponentMessage(new ChatComponentText(String.format(translate("message.lomlib.updateMessage"), this.modname, this.downloadURL)));
                    player.addChatComponentMessage(new ChatComponentText(translate("message.lomlib.updateChangeLog")));
                    for (String change : changeList)
                        player.addChatComponentMessage(new ChatComponentText("- " + change));
                    if (!Loader.isModLoaded("VersionChecker")) {
                        player.addChatComponentMessage(new ChatComponentText(String.format(translate("message.lomlib.updateDownloadP1"), this.modid)));
                        player.addChatComponentMessage(new ChatComponentText(translate("message.lomlib.updateDownloadP2")));
                    }
                    this.doneTelling = true;
                }
            }
        }
    }

    public static void beginModDownloader(ICommandSender sender, String id) {
        if (updateList.containsKey(id)) {
            ModDownloader downloader = updateList.get(id);
            if (downloader != null) {
                downloader.start();
                updateList.remove(id);
                sender.addChatMessage(new ChatComponentText(String.format(StatCollector.translateToLocal("message.lomlib.updateCommand"), downloader.modname)));
            }
        }
    }
}

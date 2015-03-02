package net.lomeli.lomlib.core.version;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.apache.logging.log4j.Level;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.lomeli.lomlib.LomLib;

/**
 * A nicer version check implementation (based on what i use for Elemental Creepers)
 * <p/>
 * An example of the JSON file you'll need online can be seen <a href="http://paste.ubuntu.com/8464419/">here</a> (now with comments!)
 *
 * @author Lomeli12
 */
public class VersionChecker {
    private int mod_major, mod_minor, mod_rev;
    private boolean needsUpdate, isDirect, doneTelling;
    private String version, downloadURL, jsonURL, modid, modname, currentVer, cursePage;
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
                if (jsonObject.has("changeLog")) {
                    if (jsonObject.get("changeLog").isJsonArray()) {
                        JsonArray changeLog = jsonObject.get("changeLog").getAsJsonArray();
                        Iterator<JsonElement> it = changeLog.iterator();
                        while (it.hasNext()) {
                            this.changeList.add(it.next().getAsString());
                        }
                    } else
                        this.changeList.add(jsonObject.get("changeLog").getAsString());
                }
                reader.endObject();
                reader.close();
            }

            this.needsUpdate = (this.mod_major > major ? (this.mod_minor > minor ? (this.mod_rev > revision ? false : true) : true) : true);
            if (this.needsUpdate) {
                this.version = major + "." + minor + "." + revision;
                this.doneTelling = false;
                sendMessage();
            }
        } catch (Exception e) {
            LomLib.logger.logError("Could not check for updates!");
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
        }
        FMLLog.log(Level.INFO, String.format(translate("message.lomlib.updateMessage"), this.modname, this.downloadURL));
        FMLLog.log(Level.INFO, translate("message.lomlib.updateOld") + " " + this.currentVer);
        FMLLog.log(Level.INFO, translate("message.lomlib.updateNew") + " " + this.version);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && Minecraft.getMinecraft().thePlayer != null) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            if (this.needsUpdate) {
                if (!this.version.isEmpty() && this.doneTelling) {
                    player.addChatComponentMessage(new ChatComponentText(String.format(translate("message.lomlib.updateMessage"), this.modname, this.downloadURL)));
                    player.addChatComponentMessage(new ChatComponentText(translate("message.lomlib.updateChangeLog")));
                    for (String change : changeList)
                        player.addChatComponentMessage(new ChatComponentText("- " + change));
                    this.doneTelling = true;
                }
            }
        }
    }
}

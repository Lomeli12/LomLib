package net.lomeli.lomlib.core.version

import com.google.gson.Gson
import net.lomeli.lomlib.LomLib
import net.lomeli.lomlib.core.version.UpdateJson
import net.lomeli.lomlib.util.LangUtil
import net.minecraft.client.Minecraft
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.text.TextComponentString
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.event.FMLInterModComms
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.io.InputStreamReader
import java.net.URL

class VersionChecker : Runnable {
    private var mod_major: Int = 0
    private var mod_minor:Int = 0
    private var mod_rev:Int = 0
    private var needsUpdate: Boolean = false
    private var isDirect:Boolean = false
    private var doneTelling:Boolean = false
    private var version: String? = null
    private var downloadURL:String? = null
    private val jsonURL:String
    private val modid:String
    private val modname:String
    private var currentVer:String? = null
    private var changeList: Array<String> = emptyArray()

    constructor(jsonURL: String, modid: String, modname: String, major: Int, minor: Int, rev: Int){
        this.jsonURL = jsonURL
        this.modid = modid
        this.modname = modname
        this.mod_major = major
        this.mod_minor = minor
        this.mod_rev = rev
        this.currentVer = "${this.mod_major}.${this.mod_minor}.${this.mod_rev}"
        this.needsUpdate = false
        this.isDirect = false
        this.doneTelling = true
    }

    constructor(jsonURL: String, modid: String, modname: String, modVersion: String): this(jsonURL, modid, modname, 0, 0, 0) {
        val arr = getVersionFromID(modVersion)
        this.mod_major = arr[0]
        this.mod_minor = arr[1]
        this.mod_rev = arr[2]
        this.currentVer = modVersion
    }

    fun register() {
        MinecraftForge.EVENT_BUS.register(this)
    }

    private fun getVersionFromID(str: String): IntArray {
        val versionNumb = intArrayOf(0, 0, 0)
        val arr = str.split("".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (i in 0..2) {
            if (i < arr.size && i < versionNumb.size)
                versionNumb[i] = parseInt(arr[i])
        }
        return versionNumb
    }

    private fun parseInt(str: String): Int {
        try {
            return Integer.parseInt(str)
        } catch (e: Exception) {
        }
        return 0
    }

    override fun run() {
        try {
            if (this.currentVer.equals("@VERSION@")) return
            LomLib.logger.logInfo(LangUtil.translate("message.lomlib.update.checking", this.modname))
            val url = URL(this.jsonURL)
            val gson = Gson()
            val update = gson.fromJson(InputStreamReader(url.openStream()), UpdateJson::class.java)
            if (update != null) {
                this.needsUpdate = compareVersion(update)
                if (this.needsUpdate) {
                    this.downloadURL = update.downloadURL
                    this.isDirect = update.direct
                    this.changeList = update.changeLog.toTypedArray()
                    this.version = update.getVersion()
                    this.doneTelling = false
                    sendMessage()
                } else
                    LomLib.logger.logInfo(LangUtil.translate("message.lomlib.update.none", this.modname))
            }
        } catch (e: Exception) {
            LomLib.logger.logError(LangUtil.translate("message.lomlib.update.failed", this.modname))
        }

    }

    private fun compareVersion(update: UpdateJson): Boolean {
        if (mod_major > update.major) return false
        if (mod_major < update.major) return true
        if (mod_minor > update.minor) return false
        if (mod_minor < update.minor) return true
        if (mod_rev > update.revision) return false
        if (mod_rev < update.revision) return true
        return false
    }

    private fun sendMessage() {
        if (Loader.isModLoaded("VersionChecker")) {
            var changeLog = ""
            if (this.changeList != null && !this.changeList.isEmpty()) {
                for (i in this.changeList)
                    changeLog += "- " + i
            }

            val tag = NBTTagCompound()
            tag.setString("modDisplayName", this.modname)
            tag.setString("oldVersion", this.currentVer)
            tag.setString("newVersion", this.version)
            tag.setString("updateUrl", this.downloadURL)
            tag.setBoolean("isDirectLink", this.isDirect)
            tag.setString("changeLog", changeLog)
            FMLInterModComms.sendMessage("VersionChecker", "addUpdate", tag)
        }
        LomLib.logger.logInfo(LangUtil.translate("message.lomlib.update.message", this.modname, this.downloadURL!!))
        LomLib.logger.logInfo(LangUtil.translate("message.lomlib.update.old", this.modname, this.currentVer!!))
        LomLib.logger.logInfo(LangUtil.translate("message.lomlib.update.new", this.modname, this.version!!))
    }

    @SubscribeEvent @SideOnly(Side.CLIENT) fun onClientTick(event: TickEvent.ClientTickEvent) {
        if (event.phase == TickEvent.Phase.END && Minecraft.getMinecraft().thePlayer != null) {
            val player = Minecraft.getMinecraft().thePlayer
            if (this.needsUpdate) {
                if (!this.version!!.isEmpty() && this.doneTelling) {
                    player.addChatComponentMessage(TextComponentString(LangUtil.translate("message.lomlib.update.message", this.modname, this.downloadURL!!)))
                    if (this.changeList != null && this.changeList.size > 0) {
                        player.addChatComponentMessage(TextComponentString(LangUtil.translate("message.lomlib.update.changelog")))
                        for (change in changeList)
                            player.addChatComponentMessage(TextComponentString("- " + change))
                    }
                    this.doneTelling = true
                }
            }
        }
    }
}
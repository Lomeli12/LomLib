package net.lomeli.lomlib.client.layer.patreon

import com.google.common.collect.Lists
import com.google.gson.Gson
import net.lomeli.lomlib.LomLib

import java.io.InputStreamReader
import java.net.URL

import net.minecraft.entity.player.EntityPlayer

import net.lomeli.lomlib.lib.ModLibs

class PatreonList : Runnable {
    private val idList: MutableList<String>

    init {
        idList = Lists.newArrayList<String>()
    }

    override fun run() {
        getLatestList()
    }

    fun getLatestList() {
        try {
            idList.clear()
            val gson = Gson()
            val file = InputStreamReader(URL(ModLibs.PATREON_URL).openStream())
            val json = gson.fromJson(file, Array<String>::class.java)
            file.close()
            if (json != null) {
                for (s in json) {
                    idList.add(s)
                }
            }
        } catch (e: Exception) {

        }
    }

    fun isPatreon(player: EntityPlayer?): Boolean = player != null && player.persistentID != null && idList.contains(player.persistentID.toString())
}

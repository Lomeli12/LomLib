package net.lomeli.lomlib.core.version

import com.google.common.collect.Lists
import java.util.*

class UpdateJson(val major: Int, val minor: Int, val revision: Int, val downloadURL: String, val direct: Boolean, vararg changes: String) {

    val changeLog: ArrayList<String>

    init {
        this.changeLog = Lists.newArrayList()
        if (changes.size > 0) {
            for (i in changes.indices)
                changeLog.add(changes[i])
        }
    }

    fun getVersion(): String = "$major.$minor.$revision"
}
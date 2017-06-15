package net.lomeli.lomlib.core.version

import com.google.common.collect.Lists

class UpdateJson(val major: Int, val minor: Int, val revision: Int, val downloadURL: String, val direct: Boolean, vararg changes: String) {

    val changeLog = Lists.newArrayList<String>()

    init {
        if (changes.size > 0)
            changes.forEach { item -> changeLog.add(item) }
    }

    fun getVersion(): String = "$major.$minor.$revision"
}
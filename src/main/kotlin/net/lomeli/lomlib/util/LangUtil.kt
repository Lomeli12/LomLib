package net.lomeli.lomlib.util

import com.google.common.base.Strings
import com.google.common.collect.Maps

import net.minecraft.util.text.TextFormatting
import net.minecraft.util.text.translation.I18n

object LangUtil {
    private val chatFormat = "/&"
    private var formatMap: MutableMap<Char, TextFormatting>? = null

    init {
        formatMap = Maps.newHashMap<Char, TextFormatting>()
        for (formatting in TextFormatting.values())
            formatMap!!.put(formatting.formattingCode, formatting)
    }

    fun translate(unlocal: String, vararg args: Any): String {
        if (!Strings.isNullOrEmpty(unlocal))
            return if (args != null && args.size > 0) String.format(translate(unlocal), *args) else translate(unlocal)
        return unlocal
    }

    private fun translate(unlocal: String): String {
        return if (I18n.canTranslate(unlocal))
            I18n.translateToLocal(unlocal).replace(chatFormat.toRegex(), "\u00a7")
        else
            I18n.translateToFallback(unlocal).replace(chatFormat.toRegex(), "\u00a7")
    }

    private fun getFormatFromChar(ch: Char): TextFormatting {
        val format = formatMap!![ch]
        return format ?: TextFormatting.RESET
    }
}

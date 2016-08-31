package net.lomeli.lomlib.util

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class LogUtil {
    private val modName: String
    private val logger : Logger

    constructor(modid: String) {
        this.modName = modid
        this.logger = LogManager.getLogger(modid)
    }

    fun log(logLevel: Level, message: Any, vararg args: Any) {
        this.logger.log(logLevel, "[$modName]: " + String.format(message.toString(), *args))
    }

    fun logBasic(message: Any, vararg args: Any) {
        log(Level.INFO, message, *args)
    }

    fun logWarning(message: Any, vararg args: Any) {
        log(Level.WARN, message, *args)
    }

    fun logInfo(message: Any, vararg args: Any) {
        log(Level.INFO, message, *args)
    }

    fun logError(message: Any, vararg args: Any) {
        log(Level.ERROR, message, *args)
    }

    fun logException(e: Exception) {
        this.logger.log(Level.ERROR, "", e)
    }

    fun logDebug(message: Any, vararg args: Any) {
        if (ObfUtil.isObf)
            log(Level.DEBUG, args)
    }
}
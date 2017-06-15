package net.lomeli.lomlib

import net.minecraftforge.fml.common.ILanguageAdapter
import net.minecraftforge.fml.common.FMLModContainer
import net.minecraftforge.fml.common.ModContainer
import net.minecraftforge.fml.relauncher.Side
import org.apache.logging.log4j.LogManager

import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * Copied from shadowfacts Forgelin, which is better maintained than Emberwalker
 * Forge {@link ILanguageAdapter} for Kotlin
 * Usage: Set the {@code modLanguageAdapter} field in your {@code @Mod} annotation to {@code net.shadowfacts.forgelin.KotlinAdapter}
 * @author shadowfacts
 */
class KotlinAdapter : ILanguageAdapter {

    private val logger = LogManager.getLogger("ILanguageAdapter/Kotlin")

    override fun setProxy(target: Field, proxyTarget: Class<*>, proxy: Any) {
        logger.debug("Setting proxy on target: ${target.declaringClass.simpleName}.${target.name} -> $proxy")
        target.set(proxyTarget.kotlin.objectInstance, proxy)
    }

    override fun getNewInstance(container: FMLModContainer?, objectClass: Class<*>, classLoader: ClassLoader, factoryMarkedAnnotation: Method?): Any? {
        logger.debug("Constructing new instance of ${objectClass.simpleName}")
        return objectClass.kotlin.objectInstance ?: objectClass.newInstance()
    }

    override fun supportsStatics(): Boolean = false

    override fun setInternalProxies(mod: ModContainer?, side: Side?, loader: ClassLoader?) {
    }
}
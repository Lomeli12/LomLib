package net.lomeli.lomlib.core

import net.lomeli.lomlib.LomLib
import net.lomeli.lomlib.client.layer.patreon.PatreonList
import net.lomeli.lomlib.core.recipes.AnvilRecipeManager
import net.lomeli.lomlib.core.recipes.ShapedFluidRecipe
import net.lomeli.lomlib.core.recipes.ShapelessFluidRecipe
import net.lomeli.lomlib.core.version.VersionChecker
import net.lomeli.lomlib.lib.Config
import net.lomeli.lomlib.lib.ModLibs
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.oredict.RecipeSorter

open class Proxy {
    var list = PatreonList()
    var updater = VersionChecker(ModLibs.UPDATE_URL, ModLibs.MOD_ID, ModLibs.MOD_NAME, ModLibs.VERSION)

    open fun preInit() {
        LomLib.logger.logBasic("Pre-Init")
        RecipeSorter.register(ModLibs.NEI_SHAPED, ShapedFluidRecipe::class.java, RecipeSorter.Category.SHAPED, "after:minecraft:shaped before:minecraft:shapeless")
        RecipeSorter.register(ModLibs.NEI_SHAPELESS, ShapelessFluidRecipe::class.java, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless")
        if (Config.checkForUpdates)
            checkForUpdate()
    }

    open fun init() {
        LomLib.logger.logBasic("Init")
        Thread(list).start()
        MinecraftForge.EVENT_BUS.register(AnvilRecipeManager)
        //AnvilRecipeManager.addRecipe(new FluidAnvilRecipe(new ItemStack(Items.diamond), Items.stick, Items.paper, 13));
    }

    open fun postInit() {
        LomLib.logger.logBasic("Post-Init")
    }

    fun checkForUpdate() {
        updater.register()
        Thread(updater).start()
    }

    open fun messageClient(msg: String) {
    }
}
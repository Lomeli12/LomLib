package net.lomeli.lomlib.core.recipes

import com.google.common.collect.Lists
import net.lomeli.lomlib.util.forge.FluidUtil
import net.minecraft.block.Block
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.ShapelessRecipes
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.oredict.OreDictionary
import java.util.*

class ShapelessFluidRecipe : IRecipe {
    private var input : ArrayList<Any>
    private var output: ItemStack

    constructor(result: ItemStack, vararg recipe: Any) {
        output = result.copy()
        input = Lists.newArrayList()
        for (itemInput in recipe) {
            if (itemInput is ItemStack) {
                if (FluidUtil.isFilledContainer(itemInput))
                    input.add(FluidUtil.getContainersForFluid(FluidUtil.getContainerFluid(itemInput)))
                else
                    input.add(itemInput.copy())
            } else if (itemInput is Item) {
                val itemStack = ItemStack(itemInput)
                if (FluidUtil.isFilledContainer(itemStack))
                    input.add(FluidUtil.getContainersForFluid(FluidUtil.getContainerFluid(itemStack)))
                else
                    input.add(itemStack)
            } else if (itemInput is Block)
                input.add(ItemStack(itemInput))
            else if (itemInput is String) {
                if (itemInput.startsWith("fluid$")) {
                    val fluidName = itemInput.substring(6)
                    if (FluidRegistry.isFluidRegistered(fluidName))
                        input.add(FluidUtil.getContainersForFluid(FluidRegistry.getFluid(fluidName)))
                } else
                    input.add(OreDictionary.getOres(itemInput))
            } else {
                var ret = "Invalid shapeless ore recipe: "
                for (tmp in recipe) {
                    ret += "$tmp, "
                }
                ret += output
                throw RuntimeException(ret)
            }
        }
    }

    constructor(result: Block, vararg recipe: Any) : this(ItemStack(result), recipe)
    constructor(result: Item, vararg recipe: Any) : this(ItemStack(result), recipe)
    constructor(recipe: ShapelessRecipes, replacements: Map<ItemStack, String>) {
        output = recipe.recipeOutput!!.copy()
        input = Lists.newArrayList()
        for (ingred in recipe.recipeItems) {
            var finalObj: Any = ingred
            for (entry in replacements.entries) {
                if (OreDictionary.itemMatches(entry.key, ingred, false)) {
                    finalObj = OreDictionary.getOres(entry.value)
                    break
                } else if (FluidUtil.isFilledContainer(entry.key)) {
                    finalObj = FluidUtil.getContainersForFluid(FluidUtil.getContainerFluid(entry.key))
                    break
                }
            }
            input.add(finalObj)
        }
    }

    override fun getRecipeSize(): Int = input.size

    override fun getRecipeOutput(): ItemStack? = output.copy()

    override fun getCraftingResult(var1: InventoryCrafting): ItemStack? = output.copy()

    @SuppressWarnings("unchecked", "rawtypes")
    override fun matches(var1: InventoryCrafting, world: World): Boolean {
        val required = ArrayList(input)

        for (x in 0..var1.sizeInventory - 1) {
            val slot = var1.getStackInSlot(x)

            if (slot != null) {
                var inRecipe = false
                val req = required.iterator()

                while (req.hasNext()) {
                    var match = false

                    val next = req.next()

                    if (next is ItemStack)
                        match = OreDictionary.itemMatches(next, slot, false)
                    else if (next is List<*>) {
                        val itr = (next as List<ItemStack>).iterator()
                        while (itr.hasNext() && !match) {
                            match = OreDictionary.itemMatches(itr.next(), slot, false)
                        }
                    }
                    if (match) {
                        inRecipe = true
                        required.remove(next)
                        break
                    }
                }
                if (!inRecipe)
                    return false
            }
        }
        return required.isEmpty()
    }

    fun getInput(): ArrayList<Any> = this.input

    override fun getRemainingItems(inv: InventoryCrafting): Array<ItemStack> = ForgeHooks.defaultRecipeGetRemainingItems(inv)
}
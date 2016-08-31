package net.lomeli.lomlib.core.recipes

import com.google.common.collect.Lists
import net.lomeli.lomlib.util.forge.FluidUtil
import net.minecraft.block.Block
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.ShapedRecipes
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.oredict.OreDictionary
import java.util.*

class ShapedFluidRecipe : IRecipe {
    private val MAX_CRAFT_GRID_WIDTH = 3
    private val MAX_CRAFT_GRID_HEIGHT = 3
    private var inputs: ArrayList<Any?>
    var width = 0
    var height = 0
    private var output: ItemStack
    private var mirrored = true

    constructor(result: ItemStack, vararg recipeIn: Any) {
        output = result.copy()

        var shape = ""
        var idx = 0
        // varargs are val, and so can't be edited unless I copy it into a var
        var recipe = recipeIn

        if (recipe[idx] is Boolean) {
            mirrored = recipe[idx] as Boolean
            if (recipe[idx + 1] is Array<*>)
                recipe = recipe[idx + 1] as Array<Any>
            else
                idx = 1
        }

        if (recipe[idx] is Array<*>) {
            val parts = recipe[idx++] as Array<String>

            for (s in parts) {
                width = s.length
                shape += s
            }

            height = parts.size
        } else {
            while (recipe[idx] is String) {
                val s = recipe[idx++] as String
                shape += s
                width = s.length
                height++
            }
        }

        if (width * height != shape.length) {
            var ret = "Invalid shaped ore recipe: "
            for (tmp in recipe) {
                ret += "$tmp, "
            }
            ret += output
            throw RuntimeException(ret)
        }

        val itemMap = HashMap<Char, Any>()

        while (idx < recipe.size) {
            val chr = recipe[idx] as Char
            val item = recipe[idx + 1]

            if (item is ItemStack) {
                if (FluidUtil.isFilledContainer(item))
                    itemMap.put(chr, FluidUtil.getContainersForFluid(FluidUtil.getContainerFluid(item)))
                else
                    itemMap.put(chr, item.copy())
            } else if (item is Item) {
                if (FluidUtil.isFilledContainer(ItemStack(item)))
                    itemMap.put(chr, FluidUtil.getContainersForFluid(FluidUtil.getContainerFluid(ItemStack(item))))
                else
                    itemMap.put(chr, ItemStack(item))
            } else if (item is Block)
                itemMap.put(chr, ItemStack(item, 1, OreDictionary.WILDCARD_VALUE))
            else if (item is String) {
                if (item.startsWith("fluid$")) {
                    val fluidName = item.substring(6)
                    if (FluidRegistry.isFluidRegistered(fluidName))
                        itemMap.put(chr, FluidUtil.getContainersForFluid(FluidRegistry.getFluid(fluidName)))
                } else
                    itemMap.put(chr, OreDictionary.getOres(item))
            } else {
                var ret = "Invalid shaped ore recipe: "
                for (tmp in recipe) {
                    ret += "$tmp, "
                }
                ret += output
                throw RuntimeException(ret)
            }
            idx += 2
        }

        inputs = Lists.newArrayList()
        for (i in 0..(height * width)) {
            if (i < (height * width)) inputs.add(null)
        }
        var x = 0
        for (chr in shape.toCharArray()) {
            inputs[x++] = itemMap[chr]
        }
    }

    constructor(result: Block, vararg recipe: Any) : this(ItemStack(result), recipe)
    constructor(result: Item, vararg recipe: Any) : this(ItemStack(result), recipe)

    constructor(recipe: ShapedRecipes, replacements: Map<ItemStack, String>) {
        output = recipe.recipeOutput!!.copy()
        width = recipe.recipeWidth
        height = recipe.recipeHeight

        inputs = Lists.newArrayList()
        for (i in 0..recipe.recipeItems.size) {
            if (i < recipe.recipeItems.size) inputs.add(null)
        }

        for (i in inputs.indices) {
            val ingred = recipe.recipeItems[i] ?: continue

            inputs[i] = recipe.recipeItems[i]

            for (entry in replacements.entries) {
                if (OreDictionary.itemMatches(entry.key, ingred, true)) {
                    inputs[i] = OreDictionary.getOres(entry.value)
                    break
                } else if (FluidUtil.isFilledContainer(entry.key)) {
                    inputs[i] = FluidUtil.getContainersForFluid(FluidUtil.getContainerFluid(entry.key))
                    break
                }
            }
        }
    }

    override fun getCraftingResult(var1: InventoryCrafting): ItemStack? = output.copy()

    override fun getRecipeSize(): Int = inputs.size

    override fun getRecipeOutput(): ItemStack? = output.copy()

    override fun matches(inv: InventoryCrafting, world: World): Boolean {
        for (x in 0..MAX_CRAFT_GRID_WIDTH - width) {
            for (y in 0..MAX_CRAFT_GRID_HEIGHT - height) {
                if (checkMatch(inv, x, y, false))
                    return true

                if (mirrored && checkMatch(inv, x, y, true))
                    return true
            }
        }

        return false
    }

    @SuppressWarnings("unchecked")
    private fun checkMatch(inv: InventoryCrafting, startX: Int, startY: Int, mirror: Boolean): Boolean {
        for (x in 0..MAX_CRAFT_GRID_WIDTH - 1) {
            for (y in 0..MAX_CRAFT_GRID_HEIGHT - 1) {
                val subX = x - startX
                val subY = y - startY
                var target: Any? = null

                if (subX >= 0 && subY >= 0 && subX < width && subY < height) {
                    if (mirror) {
                        target = inputs[width - subX - 1 + subY * width]
                    } else {
                        target = inputs[subX + subY * width]
                    }
                }

                val slot = inv.getStackInRowAndColumn(x, y)

                if (target is ItemStack) {
                    if (!OreDictionary.itemMatches(target as ItemStack?, slot, false))
                        return false
                } else if (target is List<*>) {
                    var matched = false

                    val itr = (target as List<ItemStack>).iterator()
                    while (itr.hasNext() && !matched)
                        matched = OreDictionary.itemMatches(itr.next(), slot, false)

                    if (!matched)
                        return false
                } else if (target == null && slot != null)
                    return false
            }
        }

        return true
    }

    fun getInput(): List<Any?> = Lists.newArrayList(this.inputs)

    override fun getRemainingItems(inv: InventoryCrafting): Array<ItemStack> = ForgeHooks.defaultRecipeGetRemainingItems(inv)
}
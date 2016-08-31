package net.lomeli.lomlib.core.recipes

import net.lomeli.lomlib.util.forge.FluidUtil
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.oredict.OreDictionary

class FluidAnvilRecipe : IAnvilRecipe {
    private val output: ItemStack
    private val inputs = arrayOfNulls<Any>(2)
    private var expLvlCost = 0
    private val reverse: Boolean

    constructor(output: ItemStack, leftInput: Any, rightInput: Any, expCost: Int, reverseable: Boolean) {
        this.output = output
        addInput(leftInput, 0)
        addInput(rightInput, 1)
        setXPCost(expCost)
        if (leftInput == null && rightInput == null)
            throw RuntimeException("Invalid Anvil Recipe: Both inputs cannot be null!")
        reverse = reverseable
    }

    constructor(output: ItemStack, leftInput: Any, rightInput: Any, expCost: Int) : this(output, leftInput, rightInput, expCost, false)

    private fun setXPCost(exp: Int) {
        if (exp < 0)
            throw RuntimeException("Invalid Anvil Recipe: Recipe cost less than zero!")
        this.expLvlCost = exp
    }

    private fun addInput(input: Any?, slot: Int) {
        if (slot == 0 || slot == 1) {
            if (input != null) {
                if (input is ItemStack)
                    inputs[slot] = input
                else if (input is Item)
                    inputs[slot] = ItemStack((input as Item?)!!, 1)
                else if (input is Block)
                    inputs[slot] = ItemStack((input as Block?)!!, 1)
                else if (input is String) {
                    if (input.startsWith("fluid$")) {
                        val fluidName = input.substring(6)
                        if (FluidRegistry.isFluidRegistered(fluidName))
                            inputs[slot] = FluidUtil.getContainersForFluid(FluidRegistry.getFluid(fluidName))
                    } else
                        inputs[slot] = OreDictionary.getOres(input)
                } else
                    throw RuntimeException("Invalid Anvil Recipe: " + input)
            }
        }
    }

    override fun matches(left: ItemStack, right: ItemStack): Boolean {
        var flag = doItemsMatch(left, 0) && doItemsMatch(right, 1)
        if (!flag && reverse)
            flag = doItemsMatch(left, 1) && doItemsMatch(right, 0)
        return flag
    }

    @SuppressWarnings("unchecked")
    override fun doItemsMatch(itemStack: ItemStack, slot: Int): Boolean {
        if (slot == 0 || slot == 1) {
            val target = inputs[slot]
            if (target == null)
                return itemStack == null
            else if (target is ItemStack) {
                if (!OreDictionary.itemMatches(target as ItemStack?, itemStack, false))
                    return false
            } else if (target is List<*>) {
                var matched = false
                for (item in (target as List<ItemStack>?)!!) {
                    matched = matched || OreDictionary.itemMatches(item, itemStack, false)
                }
                if (!matched)
                    return false
            }
            return true
        }
        return false
    }

    override fun getCraftingResult(left: ItemStack, right: ItemStack): ItemStack = output.copy()

    override fun recipeOutput(): ItemStack = output

    override fun recipeInputs(): Array<Any> = inputs.requireNoNulls()

    override fun recipeCost(): Int = expLvlCost
}
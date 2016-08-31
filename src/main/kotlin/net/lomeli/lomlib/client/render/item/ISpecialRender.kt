package net.lomeli.lomlib.client.render.item

interface ISpecialRender {
    fun getRenderer(): IItemRenderer

    fun resourceName(): String
}
package net.lomeli.lomlib.client.render.old.item

interface ISpecialRender {
    fun getRenderer(): IItemRenderer

    fun resourceName(): String
}
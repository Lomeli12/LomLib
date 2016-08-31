package net.lomeli.lomlib.client.models

interface IModelVariant : IModelHolder {
    fun getModelTypes(): Array<String>
}
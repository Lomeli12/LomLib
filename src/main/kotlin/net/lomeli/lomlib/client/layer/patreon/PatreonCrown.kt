package net.lomeli.lomlib.client.layer.patreon

import net.minecraft.client.model.ModelBase
import net.minecraft.client.model.ModelRenderer
import net.minecraft.entity.Entity

/**
 * PatreonCrown - Lomeli12
 * Created using Tabula 4.1.1
 */
class PatreonCrown : ModelBase() {
    var baseCrown: ModelRenderer
    var baseCrown1: ModelRenderer
    var baseCrown2: ModelRenderer
    var baseCrown3: ModelRenderer

    init {
        this.textureWidth = 16
        this.textureHeight = 16
        this.baseCrown = ModelRenderer(this, 0, -1)
        this.baseCrown.setRotationPoint(-5.0f, 12.0f, 2.0f)
        this.baseCrown.addBox(0.0f, 0.0f, 0.0f, 5, 4, 0, 0.0f)
        this.setRotateAngle(baseCrown, -0.3490658503988659f, 0.0f, 0.0f)
        this.baseCrown1 = ModelRenderer(this, 0, -6)
        this.baseCrown1.setRotationPoint(-5.0f, 12.0f, 2.0f)
        this.baseCrown1.addBox(0.0f, 0.0f, 0.0f, 0, 4, 5, 0.0f)
        this.setRotateAngle(baseCrown1, -0.3490658503988659f, 0.0f, 0.0f)
        this.baseCrown2 = ModelRenderer(this, 0, -1)
        this.baseCrown2.setRotationPoint(-5.0f, 13.71f, 6.71f)
        this.baseCrown2.addBox(0.0f, 0.0f, 0.0f, 5, 4, 0, 0.0f)
        this.setRotateAngle(baseCrown2, -0.3490658503988659f, 0.0f, 0.0f)
        this.baseCrown3 = ModelRenderer(this, 0, -6)
        this.baseCrown3.setRotationPoint(0.0f, 12.0f, 2.0f)
        this.baseCrown3.addBox(0.0f, 0.0f, 0.0f, 0, 4, 5, 0.0f)
        this.setRotateAngle(baseCrown3, -0.3490658503988659f, 0.0f, 0.0f)
    }

    override fun render(entity: Entity?, f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
        this.baseCrown.render(f5)
        this.baseCrown1.render(f5)
        this.baseCrown2.render(f5)
        this.baseCrown3.render(f5)
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    fun setRotateAngle(modelRenderer: ModelRenderer, x: Float, y: Float, z: Float) {
        modelRenderer.rotateAngleX = x
        modelRenderer.rotateAngleY = y
        modelRenderer.rotateAngleZ = z
    }

    fun render(f: Float) {
        this.baseCrown3.render(f)
        this.baseCrown2.render(f)
        this.baseCrown1.render(f)
        this.baseCrown.render(f)
    }
}
package net.lomeli.lomlib.client.patreon;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * PatreonCrown - Lomeli12
 * Created using Tabula 4.1.1
 */
public class PatreonCrown extends ModelBase {
    public ModelRenderer baseCrown;
    public ModelRenderer baseCrown1;
    public ModelRenderer baseCrown2;
    public ModelRenderer baseCrown3;

    public PatreonCrown() {
        this.textureWidth = 16;
        this.textureHeight = 16;
        this.baseCrown = new ModelRenderer(this, 0, -1);
        this.baseCrown.setRotationPoint(-5.0F, 12.0F, 2.0F);
        this.baseCrown.addBox(0.0F, 0.0F, 0.0F, 5, 4, 0, 0.0F);
        this.setRotateAngle(baseCrown, -0.3490658503988659F, 0.0F, 0.0F);
        this.baseCrown1 = new ModelRenderer(this, 0, -2);
        this.baseCrown1.setRotationPoint(-5.0F, 12.0F, 2.0F);
        this.baseCrown1.addBox(0.0F, 0.0F, 0.0F, 0, 4, 5, 0.0F);
        this.setRotateAngle(baseCrown1, -0.3490658503988659F, 0.0F, 0.0F);
        this.baseCrown2 = new ModelRenderer(this, 0, -1);
        this.baseCrown2.setRotationPoint(-5.0F, 13.71F, 6.71F);
        this.baseCrown2.addBox(0.0F, 0.0F, 0.0F, 5, 4, 0, 0.0F);
        this.setRotateAngle(baseCrown2, -0.3490658503988659F, 0.0F, 0.0F);
        this.baseCrown3 = new ModelRenderer(this, 0, -2);
        this.baseCrown3.setRotationPoint(0.0F, 12.0F, 2.0F);
        this.baseCrown3.addBox(0.0F, 0.0F, 0.0F, 0, 4, 5, 0.0F);
        this.setRotateAngle(baseCrown3, -0.3490658503988659F, 0.0F, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.baseCrown.render(f5);
        this.baseCrown1.render(f5);
        this.baseCrown2.render(f5);
        this.baseCrown3.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public void render(float f) {
        this.baseCrown3.render(f);
        this.baseCrown2.render(f);
        this.baseCrown1.render(f);
        this.baseCrown.render(f);
    }
}

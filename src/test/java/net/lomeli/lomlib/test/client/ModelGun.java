package net.lomeli.lomlib.test.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelGun extends ModelBase {
    public ModelRenderer shape1;
    public ModelRenderer shape2;
    public ModelRenderer shape3;
    public ModelRenderer shape4;
    public ModelRenderer shape5;
    public ModelRenderer shape6;
    public ModelRenderer shape7;
    public ModelRenderer shape8;
    public ModelRenderer shape9;
    public ModelRenderer shape10;
    public ModelRenderer shape11;
    public ModelRenderer shape12;
    public ModelRenderer shape13;
    public ModelRenderer shape14;
    public ModelRenderer shape15;
    public ModelRenderer shape16;
    public ModelRenderer shape17;
    public ModelRenderer shape18;
    public ModelRenderer shape19;
    public ModelRenderer shape20;
    public ModelRenderer shape21;

    public ModelGun() {
        this.textureWidth = 256;
        this.textureHeight = 57;
        this.shape4 = new ModelRenderer(this, 45, 0);
        this.shape4.setRotationPoint(-0.5F, 1.2999999523162842F, -22.700000762939453F);
        this.shape4.addBox(0.0F, 0.0F, 0.0F, 1, 3, 1, 0.0F);
        this.shape7 = new ModelRenderer(this, 80, 0);
        this.shape7.setRotationPoint(-1.5F, -0.20000000298023224F, 0.0F);
        this.shape7.addBox(0.0F, 0.0F, 0.0F, 3, 5, 8, 0.0F);
        this.shape16 = new ModelRenderer(this, 0, 15);
        this.shape16.setRotationPoint(-0.5F, 4.0F, 5.5F);
        this.shape16.addBox(0.0F, 0.0F, 0.0F, 1, 3, 0, 0.0F);
        this.shape21 = new ModelRenderer(this, 12, 11);
        this.shape21.setRotationPoint(-0.5F, 5.300000190734863F, 7.599999904632568F);
        this.shape21.addBox(0.0F, 0.0F, 0.0F, 1, 1, 0, 0.0F);
        this.setRotateAngle(shape21, -0.0012184696697081217F, -0.0F, 0.0F);
        this.shape14 = new ModelRenderer(this, 116, 11);
        this.shape14.setRotationPoint(-1.0F, 1.0F, -20.0F);
        this.shape14.addBox(0.0F, 0.0F, 0.0F, 0, 2, 14, 0.0F);
        this.setRotateAngle(shape14, 0.0F, -0.0F, 0.006092348218503444F);
        this.shape10 = new ModelRenderer(this, 80, 30);
        this.shape10.setRotationPoint(-1.5F, 2.0F, 13.5F);
        this.shape10.addBox(0.0F, 0.0F, 0.0F, 3, 4, 8, 0.0F);
        this.setRotateAngle(shape10, -0.010661609642455354F, -0.0F, 0.0F);
        this.shape11 = new ModelRenderer(this, 111, 0);
        this.shape11.setRotationPoint(-2.0F, 2.0F, -20.0F);
        this.shape11.addBox(0.0F, 0.0F, 0.0F, 4, 3, 15, 0.0F);
        this.shape9 = new ModelRenderer(this, 80, 15);
        this.shape9.setRotationPoint(-1.5F, -0.20000000298023224F, 8.0F);
        this.shape9.addBox(0.0F, 0.0F, 0.0F, 3, 5, 6, 0.0F);
        this.setRotateAngle(shape9, -0.00548311370874229F, -0.0F, 0.0F);
        this.shape19 = new ModelRenderer(this, 13, 15);
        this.shape19.setRotationPoint(-0.5F, 5.0F, 8.5F);
        this.shape19.addBox(0.0F, 0.0F, 0.0F, 1, 2, 0, 0.0F);
        this.shape3 = new ModelRenderer(this, 50, 0);
        this.shape3.setRotationPoint(-1.5F, 1.5F, -22.0F);
        this.shape3.addBox(0.0F, 0.0F, 0.0F, 3, 3, 1, 0.0F);
        this.shape15 = new ModelRenderer(this, 0, 10);
        this.shape15.setRotationPoint(-0.5F, 4.559999942779541F, 4.800000190734863F);
        this.shape15.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.setRotateAngle(shape15, 0.013707784271855726F, -0.0F, 0.0F);
        this.shape20 = new ModelRenderer(this, 0, 10);
        this.shape20.setRotationPoint(-0.5F, 4.5F, 7.099999904632568F);
        this.shape20.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.setRotateAngle(shape20, 0.009138522847903816F, -0.0F, 0.0F);
        this.shape2 = new ModelRenderer(this, 134, 31);
        this.shape2.setRotationPoint(-1.0F, 2.5F, -21.0F);
        this.shape2.addBox(0.0F, 0.0F, 0.0F, 2, 2, 24, 0.0F);
        this.shape5 = new ModelRenderer(this, 50, 5);
        this.shape5.setRotationPoint(-0.5F, 1.5F, -24.0F);
        this.shape5.addBox(0.0F, 0.0F, 0.0F, 1, 1, 2, 0.0F);
        this.shape8 = new ModelRenderer(this, 63, 0);
        this.shape8.setRotationPoint(-0.5F, 1.5F, -4.0F);
        this.shape8.addBox(0.0F, 0.0F, 0.0F, 1, 1, 4, 0.0F);
        this.shape12 = new ModelRenderer(this, 170, 0);
        this.shape12.setRotationPoint(-2.0F, 5.5F, -6.0F);
        this.shape12.addBox(0.0F, 0.0F, 0.0F, 4, 2, 14, 0.0F);
        this.setRotateAngle(shape12, 0.05483113708742291F, -0.0F, 0.0F);
        this.shape1 = new ModelRenderer(this, 130, 0);
        this.shape1.setRotationPoint(-1.0F, 0.0F, -25.0F);
        this.shape1.addBox(0.0F, 0.0F, 0.0F, 2, 2, 28, 0.0F);
        this.shape18 = new ModelRenderer(this, 5, 10);
        this.shape18.setRotationPoint(-0.5F, 5.0F, 8.0F);
        this.shape18.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.setRotateAngle(shape18, 0.009138522847903816F, -0.0F, 0.0F);
        this.shape6 = new ModelRenderer(this, 45, 5);
        this.shape6.setRotationPoint(-0.5F, 2.299999952316284F, -23.5F);
        this.shape6.addBox(0.0F, 0.0F, 0.0F, 1, 2, 1, 0.0F);
        this.setRotateAngle(shape6, 0.013707784271855726F, -0.0F, 0.0F);
        this.shape17 = new ModelRenderer(this, 3, 15);
        this.shape17.setRotationPoint(-0.5F, 7.0F, 5.5F);
        this.shape17.addBox(0.0F, 0.0F, 0.0F, 1, 0, 3, 0.0F);
        this.shape13 = new ModelRenderer(this, 115, 10);
        this.shape13.setRotationPoint(1.0F, 1.0F, -20.0F);
        this.shape13.addBox(0.0F, 0.0F, 0.0F, 0, 2, 15, 0.0F);
        this.setRotateAngle(shape13, 0.0F, -0.0F, -0.006092348218503444F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.shape4.render(f5);
        this.shape7.render(f5);
        this.shape16.render(f5);
        this.shape21.render(f5);
        this.shape14.render(f5);
        this.shape10.render(f5);
        this.shape11.render(f5);
        this.shape9.render(f5);
        this.shape19.render(f5);
        this.shape3.render(f5);
        this.shape15.render(f5);
        this.shape20.render(f5);
        this.shape2.render(f5);
        this.shape5.render(f5);
        this.shape8.render(f5);
        this.shape12.render(f5);
        this.shape1.render(f5);
        this.shape18.render(f5);
        this.shape6.render(f5);
        this.shape17.render(f5);
        this.shape13.render(f5);
    }

    public void render(float f5) {
        this.shape4.render(f5);
        this.shape7.render(f5);
        this.shape16.render(f5);
        this.shape21.render(f5);
        this.shape14.render(f5);
        this.shape10.render(f5);
        this.shape11.render(f5);
        this.shape9.render(f5);
        this.shape19.render(f5);
        this.shape3.render(f5);
        this.shape15.render(f5);
        this.shape20.render(f5);
        this.shape2.render(f5);
        this.shape5.render(f5);
        this.shape8.render(f5);
        this.shape12.render(f5);
        this.shape1.render(f5);
        this.shape18.render(f5);
        this.shape6.render(f5);
        this.shape17.render(f5);
        this.shape13.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}

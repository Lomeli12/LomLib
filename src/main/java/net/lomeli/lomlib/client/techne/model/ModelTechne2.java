package net.lomeli.lomlib.client.techne.model;

import java.util.ArrayList;

import net.minecraft.client.model.ModelBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.lomlib.LomLib;
import net.lomeli.lomlib.client.techne.TC2Info;
import net.lomeli.lomlib.client.techne.model.components.*;


/**
 * All the fields are public. Go ahead and rotate/animate them before calling render.
 */
@SideOnly(Side.CLIENT)
public class ModelTechne2 extends ModelBase {

    public ArrayList<ModelPart> modelParts = new ArrayList<ModelPart>();

    public ModelTechne2(TC2Info info) {
        for (TC2Info.Model model : info.Techne.Models) {
            try {
                ModelPart part = new ModelPart();
                String[] scale = model.Model.GlScale.split(",");
                part.scaleX = Float.parseFloat(scale[0]);
                part.scaleY = Float.parseFloat(scale[1]);
                part.scaleZ = Float.parseFloat(scale[2]);

                String[] textureSize = model.Model.TextureSize.split(",");
                part.textureWidth = Integer.parseInt(textureSize[0]);
                part.textureHeight = Integer.parseInt(textureSize[1]);

                part.image = model.Model.image;

                this.textureWidth = part.textureWidth;
                this.textureHeight = part.textureHeight;

                populateGroup(this, EnumGroupType.NONE, model.Model.Geometry, part.models);

                modelParts.add(part);
            } catch (NumberFormatException e) {
                LomLib.logger.logError("Error parsing Techne 2 model: Invalid number");
                e.printStackTrace();
            } catch (ArrayIndexOutOfBoundsException e) {
                LomLib.logger.logError("Error parsing Techne 2 model: Array too short");
            }
        }
    }

    public static void populateGroup(ModelBase model, EnumGroupType type, TC2Info.Group geometry, GroupModels models) {
        for (TC2Info.Circular circular : geometry.Circular) {
            ComponentCircularArray array = new ComponentCircularArray();

            setupComponent(model, type == EnumGroupType.NONE ? EnumGroupType.CIRCULAR : type, circular, array);

            array.count = circular.Count;
            array.radius = circular.Radius;

            models.componentCircularArray.add(array);
        }
        for (TC2Info.Linear linear : geometry.Linear) {
            ComponentLinearArray array = new ComponentLinearArray();

            setupComponent(model, type == EnumGroupType.NONE ? EnumGroupType.LINEAR : type, linear, array);

            String[] count = linear.Count.split(",");
            array.countX = Integer.parseInt(count[0]);
            array.countY = Integer.parseInt(count[1]);
            array.countZ = Integer.parseInt(count[2]);
            if (!(array.countX == 0 && array.countY == 0 && array.countZ == 0)) {
                if (array.countX == 0)
                    array.countX = 1;

                if (array.countY == 0)
                    array.countY = 1;

                if (array.countZ == 0)
                    array.countZ = 1;
            }

            String[] space = linear.Spacing.split(",");
            array.spaceX = Float.parseFloat(space[0]);
            array.spaceY = Float.parseFloat(space[1]);
            array.spaceZ = Float.parseFloat(space[2]);

            models.componentLinearArray.add(array);
        }
        for (TC2Info.Null nul : geometry.Null) {
            ComponentGroup array = new ComponentGroup();

            setupComponent(model, type, nul, array);

            models.componentGroup.add(array);
        }
        for (TC2Info.Shape shape : geometry.Shape) {
            String[] textureOffset = shape.TextureOffset.split(",");
            //These model types are used to ensure correct rotational rendering of the models. Models are expected to not be interacted with mods which play around with models (such as Morph). If expected to be interacted with by such mods, please use the default ModelRenderer that Minecraft provides.
            ModelRendererTechne cube = new ModelRendererTechne(model, shape.Name);
            cube.setTextureOffset(Integer.parseInt(textureOffset[0]), Integer.parseInt(textureOffset[1]));
            cube.mirror = !shape.IsMirrored.equals("False");

            String[] pos = shape.Position.split(",");
            String[] rot = shape.Rotation.split(",");
            String[] size = shape.Size.split(",");

            String[] offset = new String[]{"0", "0", "0"}; //Techne 2 files by default lack the Offset field.
            if (shape.Offset != null)
                offset = shape.Offset.split(",");
            else if (type == EnumGroupType.CIRCULAR) {
                offset = new String[]{Float.toString(Float.parseFloat(size[0]) / -2F), Float.toString(Float.parseFloat(size[1]) / -2F), Float.toString(Float.parseFloat(size[2]) / -2F)};
                pos = rot = new String[]{"0", "0", "0"};
            }

            cube.addBox(Float.parseFloat(offset[0]), Float.parseFloat(offset[1]), Float.parseFloat(offset[2]), Integer.parseInt(size[0]), Integer.parseInt(size[1]), Integer.parseInt(size[2]));
            cube.setRotationPoint(Float.parseFloat(pos[0]), Float.parseFloat(pos[1]), Float.parseFloat(pos[2]));

            cube.rotateAngleX = Float.parseFloat(rot[0]);
            cube.rotateAngleY = Float.parseFloat(rot[1]);
            cube.rotateAngleZ = Float.parseFloat(rot[2]);

            models.models.add(cube);
        }
    }

    public static void setupComponent(ModelBase model, EnumGroupType type, TC2Info.Null info, ComponentGroup component) {
        String[] pos = info.Position.split(",");
        component.posX = Float.parseFloat(pos[0]);
        component.posY = Float.parseFloat(pos[1]);
        component.posZ = Float.parseFloat(pos[2]);

        String[] rot = info.Rotation.split(",");
        component.rotationX = Float.parseFloat(rot[0]);
        component.rotationY = Float.parseFloat(rot[1]);
        component.rotationZ = Float.parseFloat(rot[2]);

        populateGroup(model, type, info.Children, component.groupModels);
    }

    public static enum EnumGroupType {
        NONE,
        CIRCULAR,
        LINEAR
    }

    public void render(boolean bindTexture, float f5) {
        for (int i = modelParts.size() - 1; i >= 0; i--) {
            ModelPart modelPart = modelParts.get(i);
            if (!modelPart.render(bindTexture, f5))
                modelParts.remove(i);
        }
    }
}

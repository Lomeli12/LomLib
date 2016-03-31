package net.lomeli.lomlib.client.models;

import com.google.common.collect.Lists;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;

import net.minecraftforge.client.model.ModelLoader;

public class ModelHandler {

    private static List<IColorProvider> colorProviders = Lists.newArrayList();

    public static void registerModel(IModelHolder holder) {
        if (holder == null)
            return;
        if (holder instanceof IColorProvider)
            colorProviders.add((IColorProvider) holder);
        if (holder instanceof IMeshVariant) {
            ItemMeshDefinition def = ((IMeshVariant) holder).getCustomMesh();
            if (def != null) {
                ModelLoader.setCustomMeshDefinition((Item) holder, def);
                return;
            }
        }
        Item item = (Item) holder;
        String[] variants = holder.getVariants();
        if (variants != null && variants.length > 0) {
            for (int i = 0; i < variants.length; i++) {
                String name = variants[i];
                ModelResourceLocation loc = new ModelResourceLocation(name, "inventory");
                ModelLoader.setCustomModelResourceLocation(item, i, loc);
            }
        }
    }

    public static void registerModel(Item item) {
        if (item instanceof IModelHolder) registerModel((IModelHolder) item);
    }

    public static void registerColorProviers() {
        ItemColors colors = Minecraft.getMinecraft().getItemColors();
        colorProviders.stream().filter(c -> c != null).forEach(c -> colors.registerItemColorHandler(c.getColor(), (Item) c));
    }
}

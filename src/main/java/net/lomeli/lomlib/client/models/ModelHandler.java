package net.lomeli.lomlib.client.models;

import com.google.common.collect.Lists;

import java.util.List;

import net.lomeli.lomlib.LomLib;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;

import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;

public class ModelHandler {

    private static List<IColorProvider> colorProviders = Lists.newArrayList();

    public static void registerModel(IModelHolder holder) {
        if (holder == null)
            return;
        if (Loader.instance().getModState(Loader.instance().activeModContainer()) != LoaderState.ModState.PREINITIALIZED) {
            LomLib.logger.logError("Must be registered in Pre-Init");
        }
        if (holder instanceof IColorProvider)
            colorProviders.add((IColorProvider) holder);
        if (holder instanceof IMeshVariant) {
            ItemMeshDefinition def = ((IMeshVariant) holder).getCustomMesh();
            if (def != null) {
                ModelLoader.setCustomMeshDefinition(holder instanceof Item ? (Item) holder : holder instanceof Block ? Item.getItemFromBlock((Block) holder) : null, def);
                return;
            }
        }

        if (holder instanceof Item) {
            Item item = (Item) holder;
            String[] variants = holder.getVariants();
            if (variants != null && variants.length > 0) {
                for (int i = 0; i < variants.length; i++) {
                    String name = variants[i];
                    ModelResourceLocation loc = new ModelResourceLocation(name, "inventory");
                    ModelLoader.setCustomModelResourceLocation(item, i, loc);
                }
            }
        } else if (holder instanceof Block) {
            Block block = (Block) holder;
            String[] variants = holder.getVariants();
            if (variants != null && variants.length > 0) {
                for (int i = 0; i < variants.length; i++) {
                    String name = variants[i];
                    ModelResourceLocation loc = new ModelResourceLocation(name, "inventory");
                    if (holder instanceof IModelVariant) {
                        loc = new ModelResourceLocation(name, ((IModelVariant) holder).getModelTypes()[i]);
                        ModelLoader.registerItemVariants(Item.getItemFromBlock(block), loc);
                    }
                    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), i, loc);
                }
            }
        }
    }

    public static void registerModel(Object obj) {
        if (obj instanceof IModelHolder) registerModel((IModelHolder) obj);
    }

    public static void registerColorProviers() {
        ItemColors colors = Minecraft.getMinecraft().getItemColors();
        colorProviders.stream().filter(c -> c != null).forEach(c -> colors.registerItemColorHandler(c.getColor(), (Item) c));
    }
}

package net.lomeli.lomlib.client.render;

import com.google.common.collect.Lists;

import java.util.List;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.lomeli.lomlib.LomLib;
import net.lomeli.lomlib.client.render.item.IItemRenderer;
import net.lomeli.lomlib.client.render.item.ISpecialRender;
import net.lomeli.lomlib.client.render.item.ItemRenderWrapper;
import net.lomeli.lomlib.lib.ModLibs;

@SuppressWarnings("deprecation")
public class ModelGenerator {
    private static List<ISpecialRender> specialRenders = Lists.newArrayList();

    public static void addItemRender(ISpecialRender renderHandler) {
        if (specialRenders == null)
            specialRenders = Lists.newArrayList();
        if (ModLibs.initialized)
            LomLib.logger.logError("Render handler must be registered during pre-init. - " + renderHandler.resourceName());
        if (renderHandler != null && !specialRenders.contains(renderHandler))
            specialRenders.add(renderHandler);
    }

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) {
        if (specialRenders == null) {
            specialRenders = Lists.newArrayList();
            return;
        }

        if (specialRenders.size() > 0) {
            for (ISpecialRender renderHandler : specialRenders) {
                IItemRenderer renderer = renderHandler.getRenderer();
                event.modelRegistry.putObject(new ModelResourceLocation(renderHandler.resourceName(), "inventory"), new ItemRenderWrapper(renderer));
            }
        }
    }
}

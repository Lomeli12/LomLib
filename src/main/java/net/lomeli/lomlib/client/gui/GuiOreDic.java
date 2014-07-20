package net.lomeli.lomlib.client.gui;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.oredict.OreDictionary;

import net.lomeli.lomlib.client.render.RenderUtils;
import net.lomeli.lomlib.libs.Strings;

public class GuiOreDic extends InventoryEffectRenderer {
    private final ResourceLocation guiTexture = new ResourceLocation(Strings.MOD_ID.toLowerCase() + ":textures/gui/oreDicSearch.png");
    private GuiButton prev, next;
    private int guiWidth = 176, guiHeight = 166, left, top, index, mouseX, mouseY;
    private List<String> oreDicList;
    private ItemStack toolTipStack;

    public GuiOreDic() {
        super(new DummyContainer());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        if (index <= 0)
            this.prev.visible = false;
        else
            this.prev.visible = true;

        if (index >= oreDicList.size() - 1)
            this.next.visible = false;
        else
            this.next.visible = true;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture(guiTexture);
        this.drawTexturedModalRect(left, top, 0, 0, guiWidth, guiHeight);

        renderOreDicItems();
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        this.left = (this.width - guiWidth) / 2;
        this.top = (this.height - guiHeight) / 2;
        this.buttonList.add(this.prev = new TabButton(1, left + 5, top + 7, "<", guiTexture));
        this.buttonList.add(this.next = new TabButton(2, left + 158, top + 7, ">", guiTexture));
        this.oreDicList = new ArrayList<String>();
        for (String ore : OreDictionary.getOreNames()) {
            if (ore != null)
                this.oreDicList.add(ore);
        }
        this.index = 0;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        super.actionPerformed(button);
        if (button != null) {
            if (button.id == this.next.id) {
                if (this.index < oreDicList.size() - 1)
                    this.index++;
            } else if (button.id == this.prev.id) {
                if (this.index > 0)
                    this.index--;
            }
        }
    }

    private void renderOreDicItems() {
        if (this.index >= oreDicList.size())
            this.index = oreDicList.size() - 1;
        else if (this.index < 0)
            this.index = 0;
        String s = oreDicList.get(index);
        mc.fontRenderer.drawString(s, left + 85 - (mc.fontRenderer.getStringWidth(s) / 2), top + 10, 0);

        List<ItemStack> list = OreDictionary.getOres(s);
        if (list != null && !list.isEmpty()) {
            try {
                for (int i = 0; i < list.size(); i++) {
                    if (i / 9 < 7) {
                        ItemStack stack = list.get(i);
                        if (stack != null && stack.getItem() != null) {
                            int x = left + 8 + (18 * (i % 9));
                            int y = top + 34 + (18 * (i / 9));
                            this.renderItem(x, y, stack);
                        }
                    }
                }
            } catch (Exception e) {
            }
        }

        if (toolTipStack != null)
            RenderUtils.renderItemToolTip(mouseX, mouseY, toolTipStack);
        toolTipStack = null;
    }

    private void renderItem(int x, int y, ItemStack item) {
        RenderItem renderItem = new RenderItem();
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        renderItem.renderItemIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().renderEngine, item, x, y);
        renderItem.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().renderEngine, item, x, y);
        RenderHelper.disableStandardItemLighting();
        GL11.glPopMatrix();
        if ((mouseX >= x && mouseX <= x + 16) && (mouseY >= y && mouseY <= y + 16))
            toolTipStack = item;
    }

    private void bindTexture(ResourceLocation loc) {
        mc.getTextureManager().bindTexture(loc);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void handleMouseInput() {
        int x = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

        mouseX = x;
        mouseY = y;

        super.handleMouseInput();
    }

    public static class TabButton extends GuiButton {
        private final ResourceLocation texture;

        public TabButton(int par1, int par2, int par3, String text, ResourceLocation texture) {
            super(par1, par2, par3, 13, 13, text);
            this.texture = texture;
        }

        @Override
        public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_) {
            if (this.visible) {
                boolean flag = p_146112_2_ >= this.xPosition && p_146112_3_ >= this.yPosition && p_146112_2_ < this.xPosition + this.width && p_146112_3_ < this.yPosition + this.height;
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                p_146112_1_.getTextureManager().bindTexture(texture);
                int k = 0;
                int l = 166;

                if (flag)
                    k += 13;

                this.drawTexturedModalRect(this.xPosition, this.yPosition, k, l, 13, 13);
                this.drawString(Minecraft.getMinecraft().fontRenderer, this.displayString, (this.xPosition + this.width / 2) - (Minecraft.getMinecraft().fontRenderer.getStringWidth(this.displayString) / 2), this.yPosition + (this.height - 6) / 2, Color.WHITE.getRGB());
            }
        }
    }

    static class DummyContainer extends Container {
        @Override
        public boolean canInteractWith(EntityPlayer p_75145_1_) {
            return true;
        }
    }
}

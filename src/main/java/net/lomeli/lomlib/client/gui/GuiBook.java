package net.lomeli.lomlib.client.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;

import net.lomeli.lomlib.client.gui.pages.PageBase;
import net.lomeli.lomlib.client.render.RenderUtils;
import net.lomeli.lomlib.libs.Strings;

public class GuiBook extends GuiScreen {
    public List<PageBase> pagesLeft = new ArrayList<PageBase>();
    public List<PageBase> pagesRight = new ArrayList<PageBase>();

    private int bookImageWidth = 256, bookImageHeight = 207, currentPage, maxPages, guiLeft, guiTop;
    public int pageLeftX, pageRightX, pageY;
    private GuiButtonNextPage buttonNextPage, buttonPreviousPage;

    public GuiBook() {
        this.currentPage = 0;
    }

    public GuiBook addPages(boolean right, PageBase page) {
        if (right) {
            pagesRight.add(page);
            pagesLeft.add(null);
        }else {
            pagesRight.add(null);
            pagesLeft.add(page);
        }
        return this;
    }

    public GuiBook addPages(PageBase left, PageBase right) {
        pagesLeft.add(left);
        pagesRight.add(right);
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        super.initGui();

        if (pagesLeft.size() > pagesRight.size())
            maxPages = pagesLeft.size();
        else
            maxPages = pagesRight.size();
        maxPages--;
        guiLeft = (width - bookImageWidth) / 2;
        guiTop = (height - bookImageHeight) / 2;
        pageLeftX = guiLeft + 15;
        pageRightX = guiLeft + 135;
        pageY = guiTop + 13;

        buttonList.add(buttonNextPage = new GuiButtonNextPage(0, guiLeft + 220, guiTop + 180, true));
        buttonList.add(buttonPreviousPage = new GuiButtonNextPage(1, guiLeft + 12, guiTop + 180, false));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button != null) {
            if (button.equals(buttonNextPage))
                nextPage();
            else if (button.equals(buttonPreviousPage))
                previousPage();
        }
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderUtils.bindTexture(Strings.MOD_ID.toLowerCase(), "textures/gui/book.png");
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, bookImageWidth, bookImageHeight);

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.enableGUIStandardItemLighting();

        PageBase leftPage = currentPage < pagesLeft.size() ? pagesLeft.get(currentPage) : null;
        if (leftPage != null)
            leftPage.setPos(pageLeftX, pageY).draw();

        PageBase rightPage = currentPage < pagesRight.size() ? pagesRight.get(currentPage) : null;
        if (rightPage != null)
            rightPage.setPos(pageRightX, pageY).draw();

        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        super.drawScreen(par1, par2, par3);
    }

    public void nextPage() {
        if (currentPage < maxPages)
            currentPage++;
    }

    public void previousPage() {
        if (currentPage > 0)
            currentPage--;
    }
}

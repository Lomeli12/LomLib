package net.lomeli.lomlib.client;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ThreadDownloadImageData;

import net.minecraftforge.client.event.RenderPlayerEvent;

import net.lomeli.lomlib.LomLib;
import net.lomeli.lomlib.libs.Strings;
import net.lomeli.lomlib.util.ModLoaded;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DevCapes {
    private static final Graphics TEST_GRAPHICS = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB).getGraphics();
    private HashMap<String, String> capes = new HashMap<String, String>();
    private ArrayList<AbstractClientPlayer> capePlayers = new ArrayList<AbstractClientPlayer>();

    private static DevCapes instance;

    public DevCapes() {
        buildCapeDatabase();
    }

    public static DevCapes getInstance() {
        if (instance == null)
            instance = new DevCapes();
        return instance;
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPreRenderSpecials(RenderPlayerEvent.Specials.Pre event) {
        if (!ModLoaded.isModInstalled("shadersmod") && (event.entityPlayer instanceof AbstractClientPlayer)) {
            AbstractClientPlayer abstractClientPlayer = (AbstractClientPlayer) event.entityPlayer;

            if (!capePlayers.contains(abstractClientPlayer)) {
                String cloakURL = capes.get(event.entityPlayer.getDisplayName().toLowerCase());
                if (cloakURL != null) {
                    capePlayers.add(abstractClientPlayer);

                    ReflectionHelper.setPrivateValue(ThreadDownloadImageData.class, abstractClientPlayer.getTextureCape(), false, new String[] { "textureUploaded", "field_110559_g" });

                    new Thread(new CloakThread(abstractClientPlayer, cloakURL)).start();
                    event.renderCape = true;
                }
            }
        }
    }

    public void buildCapeDatabase() {
        try {
            URL xmlURL = new URL(Strings.CAPE_URL);
            InputStream xml = xmlURL.openStream();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(xml);

            doc.getDocumentElement().normalize();

            NodeList list = doc.getChildNodes().item(0).getChildNodes();
            for (int i = 1; i <= list.getLength(); i++) {
                String nodeName = list.item(i).getNodeName();
                String capeURL = list.item(i).getTextContent();
                if (nodeName != "#text" && capeURL != "") {
                    new Thread(new CloakPreload(capeURL)).start();
                    capes.put(nodeName, capeURL);
                }
            }
            xml.close();
        }catch (Exception e) {
        }
    }

    public void refreshCapes() {
        capes.clear();
        capePlayers.clear();
        buildCapeDatabase();
    }

    private class CloakThread implements Runnable {
        AbstractClientPlayer abstractClientPlayer;
        String cloakURL;

        public CloakThread(AbstractClientPlayer player, String cloak) {
            abstractClientPlayer = player;
            cloakURL = cloak;
        }

        @Override
        public void run() {
            try {
                Image cape = new ImageIcon(new URL(cloakURL)).getImage();
                BufferedImage bo = new BufferedImage(cape.getWidth(null), cape.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                bo.getGraphics().drawImage(cape, 0, 0, null);

                ReflectionHelper.setPrivateValue(ThreadDownloadImageData.class, abstractClientPlayer.getTextureCape(), bo, new String[] { "bufferedImage", "field_110560_d" });
            }catch (Exception e) {
                LomLib.logger.logError("Failed to load cape!");
                e.printStackTrace();
            }
        }
    }

    private class CloakPreload implements Runnable {
        String cloakURL;

        public CloakPreload(String link) {
            cloakURL = link;
        }

        @Override
        public void run() {
            try {
                TEST_GRAPHICS.drawImage(new ImageIcon(new URL(cloakURL)).getImage(), 0, 0, null);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
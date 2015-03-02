package net.lomeli.lomlib.client.patreon;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

import net.lomeli.lomlib.core.Strings;

public class PatreonList {
    private List<String> idList;

    public PatreonList() {
        idList = new ArrayList<String>();
    }

    public void getLatestList() {
        try {
            idList.clear();
            Gson gson = new Gson();
            Reader file = new InputStreamReader(new URL(Strings.PATREON_URL).openStream());
            String[] json = gson.fromJson(file, String[].class);
            file.close();
            if (json != null) {
                for (String s : json) {
                    idList.add(s);
                }
            }
        } catch (Exception e) {

        }
    }

    public boolean isPatreon(EntityPlayer player) {
        return player != null && player.getUniqueID() != null && idList.contains(player.getUniqueID().toString());
    }
}

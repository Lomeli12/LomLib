package net.lomeli.lomlib.entity;

import com.mojang.authlib.GameProfile;

import java.util.UUID;

import net.minecraft.world.WorldServer;

import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

public class FakePlayerLomLib extends FakePlayer {

    protected static UUID uuid = UUID.fromString("17787906-E54E-4FE9-AA9D-A8A2C736F31B");
    private static GameProfile profile = new GameProfile(uuid, "[LomLib]");

    private static FakePlayerLomLib fakePlayer = null;

    public FakePlayerLomLib(WorldServer world, GameProfile name) {
        super(world, name);
    }

    public static FakePlayerLomLib lazyPlayer(WorldServer world) {
        if (fakePlayer == null)
            fakePlayer = (FakePlayerLomLib)FakePlayerFactory.get(world, profile);
        return fakePlayer;
    }
}

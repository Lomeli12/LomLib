package net.lomeli.lomlib.entity;

import com.mojang.authlib.GameProfile;

import net.minecraft.world.WorldServer;

import net.minecraftforge.common.util.FakePlayer;

public class FakePlayerLomLib extends FakePlayer {

    protected static String uuid = "17787906-E54E-4FE9-AA9D-A8A2C736F31B";

    private static FakePlayerLomLib fakePlayer = null;

    public FakePlayerLomLib(WorldServer world, GameProfile name) {
        super(world, name);
    }

    public static FakePlayerLomLib lazyPlayer(WorldServer world) {
        if (fakePlayer == null)
            fakePlayer = new FakePlayerLomLib(world, new GameProfile(uuid, "[FakeLomLibPlayer"));
        return fakePlayer;
    }
}

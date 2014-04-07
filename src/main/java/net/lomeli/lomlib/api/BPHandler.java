package net.lomeli.lomlib.api;

import net.minecraft.nbt.NBTTagCompound;

public class BPHandler implements IBasicPower {

    private int bp, maxBp, maxIn, maxOut;

    public BPHandler() {
        this(10000, 10000, 10000);
    }

    public BPHandler(int maxBp) {
        this(maxBp, maxBp, maxBp);
    }

    public BPHandler(int maxBp, int maxIn, int maxOut) {
        this.maxBp = maxBp;
        this.maxIn = maxIn;
        this.maxOut = maxOut;
    }

    @Override
    public int getBPCharge() {
        return this.bp;
    }

    @Override
    public int getBPMax() {
        return this.maxBp;
    }

    @Override
    public void setBPCharge(int BP) {
        this.bp = BP;
    }

    @Override
    public void setBPMax(int BP) {
        this.maxBp = BP;
    }

    @Override
    public int useBP(int maxUse, boolean simulated) {
        int used = Math.min(this.bp, Math.min(this.maxOut, maxUse));
        if (!simulated)
            this.bp -= used;
        return used;
    }

    @Override
    public int addBP(int maxRecieve, boolean simulated) {
        int added = Math.min(this.maxBp - this.bp, Math.min(this.maxIn, maxRecieve));
        if (!simulated)
            this.bp += added;
        return added;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        if (this.bp < 0)
            this.bp = 0;
        tag.setInteger("BPEnergy", this.bp);
        return tag;
    }

    public BPHandler readFromNBT(NBTTagCompound tag) {
        this.bp = tag.getInteger("BPEnergy");
        if (this.bp > this.maxBp)
            this.bp = this.maxBp;
        return this;
    }
}

package net.lomeli.lomlib.api;

public interface IBasicPower {
    public int getBPCharge();

    public int getBPMax();

    public void setBPCharge(int BP);

    public void setBPMax(int BP);

    /**
     * @param minUse    - Minimum that should be used.
     * @param maxUse    - Maximum that can be used if it can.
     * @param simulated - True = simulated, do not actually use energy
     * @return Amount of energy that was (or would have been, if simulated) used.
     */
    public int useBP(int maxUse, boolean simulated);

    /**
     * @param amount    - amount that will be added
     * @param simulated - True = simulated, do not actually use energy
     * @return Amount of energy that was (or would have been, if simulated) received.
     */
    public int addBP(int maxRecieve, boolean simulated);


}

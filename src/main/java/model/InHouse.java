package model;

/**
 * A subclass of Part.
 */
public class InHouse extends Part {
    private int machineId;

    /**
     * Constructor for InHouse objects. LOGICAL ERROR: I had a logical error that caused added In-house parts (and Outsourced parts) to add to the allParts list without their final attribute (Machine ID or Company Name). This error was caused by my neglecting to set <code>this.machineId = machineId</code> in my constructor. I added that statement and the corresponding statement in the Outsourced class, and the error was corrected.
     * @param id the Part ID.
     * @param name the Part name.
     * @param price the price of the Part.
     * @param stock the inventory level of the Part.
     * @param min the minimum inventory level allowed.
     * @param max the maximum inventory level allowed.
     * @param machineId the ID of the machine that created the Part.
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * @return the machineId
     */
    public int getMachineId() {
        return machineId;
    }

    /**
     *
     * @param machineId the machineId to set
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}

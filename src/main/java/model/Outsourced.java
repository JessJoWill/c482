package model;

/**
 * A subclass of Part
 */
public class Outsourced extends Part{

    private String companyName;

    /**
     * Constructor for objects in the Outsourced class
     * @param id the Part ID
     * @param name the Part name
     * @param price the price of the Part
     * @param stock the inventory level of the Part
     * @param min the minimum inventory level allowed
     * @param max the maximum inventory level allowed
     * @param companyName the name of the company that produces the Part
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     *
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     *
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}

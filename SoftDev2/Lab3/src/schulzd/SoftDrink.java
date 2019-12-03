/*
 * Course: CS1021 - 0x1
 * Winter 2018-2019
 * Lab 3 - Interfaces
 * Name: David Schulz
 * Created: 12/13/18
 */
package schulzd;

import java.time.LocalDate;

/**
 * SoftDrink class
 * @author David Schulz
 * @version 1.0
 */
public class SoftDrink extends Beverage {
    /**
     * Package types for soft drinks
     */
    public enum PackageType { SINGLE, SIX_PACK, TWELVE_PACK, TWENTYFOUR_PACK }
    private final PackageType packaging;
    private final int sixPackSize = 6;
    private final int twelvePackSize = 12;
    private final int twentyFourPackSize = 24;
    private final double quantityFactorBase = 0.99;
    private final double pricePerFlOz = 0.08;

    /**
     * Constructor
     * @param volume Total number of fluid ounces
     * @param brand The brand name of the beverage
     * @param sellByDate Last date on which this product can be sold
     * @param packaging the type of packaging
     */
    public SoftDrink(double volume, String brand, LocalDate sellByDate, PackageType packaging) {
        super(volume, brand, sellByDate);
        this.packaging = packaging;
    }

    /**
     * Returns the number of containers that are part of this soft drink object
     * @return the number of containers that are part of this soft drink object
     */
    @Override
    public int getNumberOfContainers() {
        int quantity = 1;
        switch (packaging) {
            case SIX_PACK:
                quantity = sixPackSize;
                break;
            case TWELVE_PACK:
                quantity = twelvePackSize;
                break;
            case TWENTYFOUR_PACK:
                quantity = twentyFourPackSize;
                break;
        }
        return quantity;
    }

    /**
     * Calculates the total price of the desired soft drink case
     * @return the price of the soft drink case
     */
    public double price() {
        double sizeFactor = (twelvePackSize * getNumberOfContainers()) / getVolumeInFlOz();
        double quantityFactor = Math.pow(quantityFactorBase, getNumberOfContainers());
        return sizeFactor * quantityFactor * pricePerFlOz;
    }

    /**
     * Calculates the tax of the desired soft drink case
     * @return the tax of the soft drink case
     */
    public double tax() {
        double stateTax = price() * WI_STATE_TAX_RATE;
        double countyTax = price() * MKE_COUNTY_TAX_RATE;
        return stateTax + countyTax;
    }
}

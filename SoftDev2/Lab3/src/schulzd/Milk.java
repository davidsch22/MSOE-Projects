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
 * Milk class
 * @author David Schulz
 * @version 1.0
 */
public class Milk extends Beverage {
    private enum FatContent { SKIM, ONE_PERCENT, TWO_PERCENT, WHOLE, HALF_AND_HALF, CREAM }
    private final FatContent fatContent;
    private final int ozPerGallon = 128;
    private final double milkPricePerGallon = 2.5;
    private final int halfHalfPricePerGallon = 5;
    private final double creamPricePerGallon = 7.5;

    /**
     * Constructor
     * @param volume Total number of fluid ounces
     * @param sellByDate Last date on which this product can be sold
     * @param fatContent A string representing the amount of fat content
     */
    public Milk(double volume, LocalDate sellByDate, String fatContent) {
        super(volume, "milk", sellByDate);
        switch (fatContent.toLowerCase()) {
            case "cream":
                this.fatContent = FatContent.CREAM;
                break;
            case "half and half":
                this.fatContent = FatContent.HALF_AND_HALF;
                break;
            case "2%":
                this.fatContent = FatContent.TWO_PERCENT;
                break;
            case "1%":
                this.fatContent = FatContent.ONE_PERCENT;
                break;
            default:
                this.fatContent = FatContent.SKIM;
        }
    }

    /**
     * Returns a description of the brand name including the fat content
     * @return a description of the brand name including the fat content
     */
    @Override
    public String getBrand() {
        String descriptor = super.getBrand();
        switch (fatContent) {
            case CREAM:
                descriptor = "cream";
                break;
            case HALF_AND_HALF:
                descriptor = "half and half";
                break;
            case WHOLE:
                descriptor = "whole " + descriptor;
                break;
            case TWO_PERCENT:
                descriptor = "2% " + descriptor;
                break;
            case ONE_PERCENT:
                descriptor = "1% " + descriptor;
                break;
            default:
                descriptor = "fat free " + descriptor;
        }
        return descriptor;
    }

    /**
     * Calculates the total price of the specific amount of certain milk
     * @return price of milk
     */
    public double price() {
        double gallons = getVolumeInFlOz() / ozPerGallon;
        switch (fatContent) {
            case SKIM:
                return gallons * milkPricePerGallon;
            case ONE_PERCENT:
                return gallons * milkPricePerGallon;
            case TWO_PERCENT:
                return gallons * milkPricePerGallon;
            case WHOLE:
                return gallons * milkPricePerGallon;
            case HALF_AND_HALF:
                return gallons * halfHalfPricePerGallon;
            case CREAM:
                return gallons * creamPricePerGallon;
            default:
                return 0;
        }
    }

    /**
     * Returns the tax of milk, which is always zero
     * @return milk tax
     */
    public double tax() {
        return 0;
    }
}

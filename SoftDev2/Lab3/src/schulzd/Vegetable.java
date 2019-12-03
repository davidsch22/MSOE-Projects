/*
 * Course: CS1021 - 0x1
 * Winter 2018-2019
 * Lab 3 - Interfaces
 * Name: David Schulz
 * Created: 12/13/18
 */
package schulzd;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Vegetable class
 * @author David Schulz
 * @version 1.0
 */
public class Vegetable extends Produce {
    private static final int MINIMUM_DAYS_TO_SELL = 5;
    private enum VeggieType { UNKNOWN, LEAFY_VEGETABLE, PODDED_VEGETABLE,
        STEM_VEGETABLE, ROOT_VEGETABLE, SEA_VEGETABLE }
    private final VeggieType veggieType;
    private final double seaSellByFactor = 1.2;
    private final double stemSellByFactor = 1.5;
    private final double seaPricePerKg = 20;
    private final double stemPricePerKg = 1.5;
    private final double rootPricePerKg = 0.3;

    /**
     * Constructor
     * @param name Name of the vegetable
     * @param weight Total weight of the vegetable
     * @param harvestDate Date on which the vegetable was harvested
     */
    public Vegetable(String name, double weight, LocalDate harvestDate) {
        super(name, weight, harvestDate);
        veggieType = setType(name);
    }

    /**
     * Returns the last date on which this product can be sold
     * @return the last date on which this product can be sold
     */
    @Override
    public LocalDate getSellByDate() {
        double sellByFactor = 1.0;
        switch (veggieType) {
            case SEA_VEGETABLE:
                sellByFactor = seaSellByFactor;
                break;
            case PODDED_VEGETABLE:
            case STEM_VEGETABLE:
                sellByFactor = stemSellByFactor;
                break;
            case ROOT_VEGETABLE:
                sellByFactor = 4.0;
                break;
        }
        return getHarvestDate().plus((long)(MINIMUM_DAYS_TO_SELL*sellByFactor),
                ChronoUnit.DAYS);
    }

    /**
     * Determines the type of vegetable based on the name of the vegetable
     * @param name the name of the vegetable
     * @return the type of vegetable based on the name of the vegetable
     */
    private VeggieType setType(String name) {
        VeggieType type = VeggieType.UNKNOWN;
        switch (name.toLowerCase()) {
            case "lettuce":
            case "spinach":
            case "mustard greens":
            case "collard greens":
                type = VeggieType.LEAFY_VEGETABLE;
                break;
            case "peas":
            case "green beans":
            case "snow peas":
                type = VeggieType.PODDED_VEGETABLE;
                break;
            case "asparagus":
            case "broccoli":
            case "celery":
                type = VeggieType.STEM_VEGETABLE;
                break;
            case "sweet potato":
            case "beet":
            case "yam":
                type = VeggieType.ROOT_VEGETABLE;
                break;
            case "kelp":
            case "kombu":
            case "nori":
                type = VeggieType.SEA_VEGETABLE;
                break;
        }
        return type;
    }

    /**
     * Calculates the total price of the desired vegetable
     * @return price of the vegetable
     */
    public double price() {
        switch (veggieType) {
            case SEA_VEGETABLE:
                return getWeightInKg() * seaPricePerKg;
            case PODDED_VEGETABLE:
                return getWeightInKg() * 2;
            case STEM_VEGETABLE:
                return getWeightInKg() * stemPricePerKg;
            case ROOT_VEGETABLE:
                return getWeightInKg() * rootPricePerKg;
            case LEAFY_VEGETABLE:
                return getWeightInKg() * 1;
            case UNKNOWN:
                return getWeightInKg() * 1;
            default:
                return 0;
        }
    }
}

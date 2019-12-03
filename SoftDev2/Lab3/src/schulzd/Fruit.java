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
 * Fruit class
 * @author David Schulz
 * @version 1.0
 */
public class Fruit extends Produce {
    private static final int MINIMUM_DAYS_TO_SELL = 20;
    private enum FruitType { UNKNOWN, FLESHY, STONE, AGGREGATE }
    private final FruitType fruitType;
    private int quantity;
    private final double onePointFive = 1.5;
    private final double aggregatePricePerKg = 2.3;
    private final double pricePerStoneFruit = 0.5;
    private final double pricePerFleshyFruit = 0.8;
    private final double pricePerOtherFruit = (Math.random() * ((2 - 0.1) + 1)) + 0.1;

    /**
     * Constructor
     * @param name Name of the fruit
     * @param weight Total weight of the fruit
     * @param harvestDate Date on which the fruit was harvested
     * @param quantity Amount of the fruit
     */
    public Fruit(String name, double weight, LocalDate harvestDate, int quantity) {
        super(name, weight, harvestDate);
        fruitType = setType(name);
        this.quantity = quantity;
    }

    /**
     * Returns the last date on which this product can be sold
     * @return the last date on which this product can be sold
     */
    @Override
    public LocalDate getSellByDate() {
        int daysToSell = fruitType==FruitType.STONE
                ? (int)(onePointFive * MINIMUM_DAYS_TO_SELL)
                : MINIMUM_DAYS_TO_SELL;
        return getHarvestDate().plus(daysToSell, ChronoUnit.DAYS);
    }

    /**
     * Determines the type of fruit based on the name of the fruit
     * @param name the name of the fruit
     * @return the type of fruit based on the name of the fruit
     */
    private FruitType setType(String name) {
        FruitType type = FruitType.UNKNOWN;
        switch (name.toLowerCase()) {
            case "apple":
            case "pear":
            case "orange":
            case "banana":
            case "grape":
                type = FruitType.FLESHY;
                break;
            case "peach":
            case "plum":
            case "mango":
                type = FruitType.STONE;
                break;
            case "strawberries":
            case "blackberries":
            case "grapes":
                type = FruitType.AGGREGATE;
                break;
        }
        return type;
    }

    /**
     * String representation of the fruit
     * @return String representation of the fruit
     */
    @Override
    public String toString() {
        return "Quantity: " + quantity + " of " + super.toString();
    }

    /**
     * Calculates the total price of the desired fruit
     * @return price of the fruit
     */
    public double price() {
        switch (fruitType) {
            case AGGREGATE:
                return getWeightInKg() * aggregatePricePerKg;
            case STONE:
                return quantity * pricePerStoneFruit;
            case FLESHY:
                return quantity * pricePerFleshyFruit;
            case UNKNOWN:
                return quantity * pricePerOtherFruit;
            default:
                return 0;
        }
    }
}


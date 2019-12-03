/*
 * Course: CS1021 - 0x1
 * Winter 2018-2019
 * Lab 3 - Interfaces
 * Name: David Schulz
 * Created: 12/13/18
 */
package schulzd;

import java.text.DecimalFormat;
import java.time.LocalDate;

/**
 * Produce class
 * @author David Schulz
 * @version 1.0
 */
public abstract class Produce implements Sellable {
    private final String name;
    private double weightInKg;
    private final LocalDate harvestDate;
    private static final DecimalFormat FORMATTER = new DecimalFormat("###,###.#");

    public String getName() {
        return name;
    }

    public LocalDate getHarvestDate() {
        return harvestDate;
    }

    public double getWeightInKg() {
        return weightInKg;
    }

    public void setWeightInKg(double weightInKg) {
        this.weightInKg = weightInKg;
    }

    /**
     * Constructor
     * @param name Name of the produce
     * @param weight Total weight of the produce item
     * @param harvestDate Date on which the produce was harvested
     */
    public Produce(String name, double weight, LocalDate harvestDate) {
        this.name = name;
        weightInKg = weight;
        this.harvestDate = harvestDate;
    }

    /**
     * String representation of the produce
     * @return String representation of the produce
     */
    @Override
    public String toString() {

        return name + " weighing " + FORMATTER.format(weightInKg)
                + "Kg, harvested on: " + harvestDate;
    }

    /**
     * Returns the last date on which this product can be sold
     * @return the last date on which this product can be sold
     */
    public abstract LocalDate getSellByDate();

    /**
     * Returns the tax of produce, which is always zero
     * @return produce tax
     */
    public double tax() {
        return 0;
    }
}

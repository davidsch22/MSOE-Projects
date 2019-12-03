/*
 * CS1021 - 0x1
 * Winter 2018-2019
 * Lab 3 - Interfaces
 * Name: TODO
 * Created: TODO
 */
package schulzd;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;

/**
 * Test driver class for Lab 3
 */
public class Driver {
    private static final Random RAND = new Random();

    private static final String[] FRUIT_NAMES = {"apple", "Pear", "orange",
            "banana", "grape", "grapes", "peach", "plum", "mango",
            "strawberries", "blackberry", "kiwi"};
    private static final String[] VEGGIE_NAMES = {"lettuce", "spinach",
            "mustard greens", "collard greens", "peas",
            "green beans", "snow peas", "asparagus", "broccoli",
            "celery", "sweet potato", "beet", "yam", "kelp",
            "kombu", "nori", "weird"};
    private static final String[] FAT = {"skim", "1%", "2%", "whole",
            "half and half", "cream"};
    private static final SoftDrink.PackageType[] PACKAGE_TYPE = {
            SoftDrink.PackageType.SINGLE,
            SoftDrink.PackageType.SIX_PACK,
            SoftDrink.PackageType.TWELVE_PACK,
            SoftDrink.PackageType.TWENTYFOUR_PACK
    };

    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart();
        for(String name : FRUIT_NAMES) {
            cart.add(createFruit(name));
        }
        for(String name : VEGGIE_NAMES) {
            cart.add(createVeggie(name));
        }
        for(String fatContent : FAT) {
            cart.add(createMilk(fatContent));
        }
        for(SoftDrink.PackageType type : PACKAGE_TYPE) {
            cart.add(createSoftDrink(type));
        }
        System.out.format("Cart total: $%6.2f%n", cart.cost());
        System.out.format(" Taxes due: $%6.2f%n", cart.taxDue());
        System.out.format(" Total due: $%6.2f%n", cart.cost() + cart.taxDue());
    }

    private static Fruit createFruit(String name) {
        final int timeBound = 20;
        final int quantityBound = 10;
        return new Fruit(name, RAND.nextDouble() * 2,
                LocalDate.now().minus(RAND.nextInt(timeBound), ChronoUnit.DAYS),
                RAND.nextInt(quantityBound)+1);
    }

    private static Vegetable createVeggie(String name) {
        final int timeBound = 7;
        return new Vegetable(name, RAND.nextDouble()*2,
                LocalDate.now().minus(RAND.nextInt(timeBound), ChronoUnit.DAYS));
    }

    private static Milk createMilk(String fatContent) {
        final int quantity = 64;
        final int timeBound = 7;
        return new Milk(quantity * (RAND.nextInt(2) + 1),
                LocalDate.now().minus(RAND.nextInt(timeBound), ChronoUnit.DAYS),
                fatContent);
    }

    private static SoftDrink createSoftDrink(SoftDrink.PackageType type) {
        final int volume = 8;
        final int volumeBound = 24;
        final int timeBound = 30;
        return new SoftDrink(volume * (RAND.nextInt(volumeBound) + 1), "Coke",
                LocalDate.now().minus(RAND.nextInt(timeBound), ChronoUnit.DAYS),
                type);
    }
}

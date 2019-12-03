package schulzd;

import java.util.ArrayList;

public class ShoppingCart {
    private ArrayList<Sellable> cart;

    public ShoppingCart() {
        cart = new ArrayList<>();
    }

    public void add(Milk milk) {
        cart.add(milk);
    }

    public void add(SoftDrink softDrink) {
        cart.add(softDrink);
    }

    public void add(Fruit fruit) {
        cart.add(fruit);
    }

    public void add(Vegetable vegetable) {
        cart.add(vegetable);
    }

    public double cost() {
        double total = 0;
        for (Sellable item : cart) {
            total += item.price();
        }
        return total;
    }

    public double taxDue() {
        double total = 0;
        for (Sellable item : cart) {
            total += item.tax();
        }
        return total;
    }
}

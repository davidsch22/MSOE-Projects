package schulzd;

public class Pickup extends FixedPlowVehicle {
    public Pickup(String name, int numWheels, double weight, double loadCapacity, double plowWidth) {
        this.name = name;
        this.numWheels = numWheels;
        this.weight = weight;
        this.loadCapacity = loadCapacity;
        plow = new Plow(plowWidth);
    }
}

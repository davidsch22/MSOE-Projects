package schulzd;

public class Dumptruck extends FixedPlowVehicle {
    private boolean loadRaised;

    public Dumptruck(String name, int numWheels, double weight, double loadCapacity, double plowWidth) {
        this.name = name;
        this.numWheels = numWheels;
        this.weight = weight;
        this.loadCapacity = loadCapacity;
        plow = new Plow(plowWidth);
    }

    public void raiseLoad() {
        if (!loadRaised) {
            System.out.println(name + " is raising the load.");
            loadRaised = true;
        } else {
            System.out.println(name + "'s load is already raised.");
        }
    }

    public void lowerLoad() {
        if (loadRaised) {
            System.out.println(name + " is lowering the load.");
            loadRaised = false;
        } else {
            System.out.println(name + "'s load is already lowered.");
        }
    }
}

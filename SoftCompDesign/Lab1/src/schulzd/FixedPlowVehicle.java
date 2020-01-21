package schulzd;

public abstract class FixedPlowVehicle extends Vehicle {
    double loadCapacity;
    Plow plow;

    void raisePlow() {
        boolean success = plow.raisePlow();
        if (success) {
            System.out.println(name + " is raising the plow.");
        } else {
            System.out.println(name + "'s plow is already raised.");
        }
    }

    void lowerPlow() {
        boolean success = plow.lowerPlow();
        if (success) {
            System.out.println(name + " is lowering the plow.");
        } else {
            System.out.println(name + "'s plow is already lowered.");
        }
    }

    public double getLoadCapacity() {
        return loadCapacity;
    }

    public void setLoadCapacity(double loadCapacity) {
        this.loadCapacity = loadCapacity;
    }
}

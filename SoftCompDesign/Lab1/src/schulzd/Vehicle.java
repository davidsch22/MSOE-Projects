package schulzd;

public abstract class Vehicle {
    String name;
    int numWheels;
    double weight;

    void goBackward(double speed, double accel) {
        System.out.println(name + " is going backwards at a speed of " + speed + " with acceleration " + accel + ".");
    }

    void goForward(double speed, double accel) {
        System.out.println(name + " is going forwards at a speed of " + speed + " with acceleration " + accel + ".");
    }

    void start() {
        System.out.println(name + " has started.");
    }

    void stop() {
        System.out.println(name + " has stopped.");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumWheels() {
        return numWheels;
    }

    public void setNumWheels(int numWheels) {
        this.numWheels = numWheels;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}

package schulzd;

import java.util.Optional;

public class Convertible extends Car {
    private Optional<Plow> plow;
    private boolean roofRaised;

    public Convertible(String name, int numWheels, double weight) {
        super(name, numWheels, weight);
        plow = Optional.ofNullable(null);
    }

    public Convertible(String name, int numWheels, double weight, double plowWidth) {
        super(name, numWheels, weight);
        plow = Optional.ofNullable(new Plow(plowWidth));
    }

    public void raiseRoof() {
        if (!roofRaised) {
            roofRaised = true;
            System.out.println(name + " is raising the roof.");
        } else {
            System.out.println(name + "'s roof is already raised.");
        }
    }

    public void lowerRoof() {
        if (roofRaised) {
            roofRaised = false;
            System.out.println(name + " is lowering the roof.");
        } else {
            System.out.println(name + "'s roof is already lowered.");
        }
    }

    public void raisePlow() {
        if (plow.isPresent()) {
            boolean success = plow.get().raisePlow();
            if (success) {
                System.out.println(name + " is raising the plow.");
            } else {
                System.out.println(name + "'s plow is already raised.");
            }
        } else {
            System.out.println(name + " does not have a plow.");
        }
    }

    public void lowerPlow() {
        if (plow.isPresent()) {
            boolean success = plow.get().lowerPlow();
            if (success) {
                System.out.println(name + " is lowering the plow.");
            } else {
                System.out.println(name + "'s plow is already lowered.");
            }
        } else {
            System.out.println(name + " does not have a plow.");
        }
    }
}

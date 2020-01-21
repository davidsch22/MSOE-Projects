package schulzd;

public class VehicleApp {
    public static void main(String[] args) {
        Car car = new Car("Cary", 4, 420.5);
        Convertible convertNoPlow = new Convertible("Not Plowy", 3, 369.3);
        Convertible convertPlow = new Convertible("Plowy", 4, 555, 20);
        Pickup pickup = new Pickup("Picky", 6, 666, 1500, 25);
        Dumptruck dumptruck = new Dumptruck("Dumpy", 8, 999, 2800, 30);

        car.start();
        System.out.println(car.getName() + " has " + car.getNumWheels() + " wheels.");
        System.out.println(car.getName() + " weighs " + car.getWeight() + ".");
        car.goForward(73.4, 9.7);
        car.goBackward(21.3, 2.6);
        car.stop();
        //car.raiseRoof();
        //car.lowerRoof();
        //car.raisePlow();
        //car.lowerPlow();
        //car.raiseLoad();
        //car.lowerLoad();
        System.out.println();

        convertNoPlow.start();
        System.out.println(convertNoPlow.getName() + " has " + convertNoPlow.getNumWheels() + " wheels.");
        System.out.println(convertNoPlow.getName() + " weighs " + convertNoPlow.getWeight() + ".");
        convertNoPlow.goForward(84.5, 14.7);
        convertNoPlow.goBackward(31.3, 8.6);
        convertNoPlow.raiseRoof();
        convertNoPlow.lowerRoof();
        convertNoPlow.lowerRoof();
        convertNoPlow.raisePlow();
        convertNoPlow.stop();
        //convertNoPlow.raiseLoad();
        //convertNoPlow.lowerLoad();
        System.out.println();

        convertPlow.start();
        System.out.println(convertPlow.getName() + " has " + convertPlow.getNumWheels() + " wheels.");
        System.out.println(convertPlow.getName() + " weighs " + convertPlow.getWeight() + ".");
        convertPlow.goForward(83.1, 12.4);
        convertPlow.goBackward(29.7, 6.2);
        convertNoPlow.raiseRoof();
        convertNoPlow.lowerRoof();
        convertNoPlow.lowerRoof();
        convertNoPlow.raisePlow();
        convertNoPlow.lowerPlow();
        convertNoPlow.lowerPlow();
        convertPlow.stop();
        //convertPlow.raiseLoad();
        //convertPlow.lowerLoad();
        System.out.println();

        pickup.start();
        System.out.println(pickup.getName() + " has " + pickup.getNumWheels() + " wheels.");
        System.out.println(pickup.getName() + " weighs " + pickup.getWeight() + ".");
        System.out.println(pickup.getName() + " has a load capacity of " + pickup.getLoadCapacity() + ".");
        pickup.goForward(67.4, 7.3);
        pickup.goBackward(19.3, 2.3);
        pickup.raisePlow();
        pickup.lowerPlow();
        pickup.lowerPlow();
        pickup.stop();
        //pickup.raiseRoof();
        //pickup.lowerRoof();
        //pickup.raiseLoad();
        //pickup.lowerLoad();
        System.out.println();

        dumptruck.start();
        System.out.println(dumptruck.getName() + " has " + dumptruck.getNumWheels() + " wheels.");
        System.out.println(dumptruck.getName() + " weighs " + dumptruck.getWeight() + ".");
        System.out.println(dumptruck.getName() + " has a load capacity of " + dumptruck.getLoadCapacity() + ".");
        dumptruck.goForward(53.4, 6.7);
        dumptruck.goBackward(11.2, 1.8);
        dumptruck.raiseLoad();
        dumptruck.lowerLoad();
        dumptruck.lowerLoad();
        dumptruck.raisePlow();
        dumptruck.lowerPlow();
        dumptruck.lowerPlow();
        dumptruck.stop();
        //dumptruck.raiseRoof();
        //dumptruck.lowerRoof();
    }
}

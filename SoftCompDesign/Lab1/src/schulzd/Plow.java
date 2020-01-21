package schulzd;

public class Plow {
    private double plowWidth;
    private boolean plowLowered;

    public Plow(double plowWidth) {
        this.plowWidth = plowWidth;
    }

    public boolean raisePlow() {
        if (plowLowered) {
            plowLowered = false;
            return true;
        }
        return false;
    }

    public boolean lowerPlow() {
        if (!plowLowered) {
            plowLowered = true;
            return true;
        }
        return false;
    }

    double getPlowWidth() {
        return plowWidth;
    }

    void setPlowWidth(double plowWidth) {
        this.plowWidth = plowWidth;
    }
}

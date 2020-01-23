package bee_simulator;

/**
 * @author johnsonn
 * @version 1.0
 * @created 17-Dec-2019 15:12:26
 */
public class Location {
	private int xPos;
	private int yPos;

	public Location(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
	}

    public int getX() {
        return xPos;
    }

    public int getY() {
        return yPos;
    }

    public void setX(int x) {
	    xPos = x;
	}

	public void setY(int y) {
	    yPos = y;
    }

    public double distanceTo(Location otherLocation) {
	    int halfLength = Entity.ENTITY_SIZE_PX / 2;
	    int centerX = getX() + halfLength;
	    int centerY = getY() + halfLength;
	    int otherCenterX = otherLocation.getX() + halfLength;
	    int otherCenterY = otherLocation.getY() + halfLength;

	    double xDist = centerX - otherCenterX;
        double yDist = centerY - otherCenterY;
        return Math.sqrt((xDist * xDist) + (yDist * yDist));
    }

    public boolean isColliding(Location otherLocation, double diameter) {
	    return distanceTo(otherLocation) <= diameter;
    }
}
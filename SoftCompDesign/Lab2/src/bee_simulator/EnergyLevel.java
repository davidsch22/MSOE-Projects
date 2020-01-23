package bee_simulator;

/**
 * @author johnsonn
 * @version 1.0
 * @created 17-Dec-2019 15:12:26
 */
public class EnergyLevel {
	private int level;

	public EnergyLevel(int level){
        this.level = level;
	}

    public EnergyLevel(EnergyLevel energyLevel) {
	    this(energyLevel.level);
    }

    public void tick() {
		level -= 2;
	}

	public void takeFrom(EnergyLevel other) {
		this.level += other.level;
		other.level = 0;
	}

    public boolean isGreaterThanZero() {
	    return level > 0;
    }

    public int getLevel() {
	    return level;
    }
}
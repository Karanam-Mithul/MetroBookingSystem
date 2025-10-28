package model;

public class Station {
    private final int id;
    private final String name;
    private final double distanceFromStart;

    public Station(int id, String name, double distanceFromStart) {
        this.id = id;
        this.name = name;
        this.distanceFromStart = distanceFromStart;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getDistanceFromStart() { return distanceFromStart; }

    @Override
    public String toString() {
        return id + ". " + name + " (" + distanceFromStart + " km)";
    }
}

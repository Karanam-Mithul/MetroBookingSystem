package model;

public class Ticket {
    private int id;
    private final String source;
    private final String destination;
    private final double fare;

    public Ticket(String source, String destination, double fare) {
        this(0, source, destination, fare);
    }

    public Ticket(int id, String source, String destination, double fare) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.fare = fare;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getSource() { return source; }
    public String getDestination() { return destination; }
    public double getFare() { return fare; }

    @Override
    public String toString() {
        return "Ticket ID: " + id + " | " + source + " → " + destination + " | Fare: ₹" + fare;
    }
}

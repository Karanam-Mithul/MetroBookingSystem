package service;

import dao.TicketDAO;
import model.Station;
import model.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MetroService {
    private final List<Station> stations = new ArrayList<>();
    private final TicketDAO ticketDAO;

    public MetroService() {
        this(new TicketDAO());
    }

    public MetroService(TicketDAO dao) {
        this.ticketDAO = Objects.requireNonNull(dao);
        seedStations();
    }

    private void seedStations() {
        stations.clear();
        stations.add(new Station(1, "Miyapur", 0));
        stations.add(new Station(2, "JNTU College", 4));
        stations.add(new Station(3, "KPHB Colony", 6));
        stations.add(new Station(4, "Ameerpet", 11));
        stations.add(new Station(5, "Punjagutta", 13));
        stations.add(new Station(6, "Irrum Manzil", 14));
        stations.add(new Station(7, "Begumpet", 15));
        stations.add(new Station(8, "Parade Ground", 16));
        stations.add(new Station(9, "Secunderabad", 18));
        stations.add(new Station(10, "MGBS", 20));
        stations.add(new Station(11, "Raidurg", 20));
        stations.add(new Station(12, "Nagole", 25));
        stations.add(new Station(13, "LB Nagar", 29));
    }

    public List<Station> getStations() {
        return new ArrayList<>(stations);
    }

    public Station findStationByName(String name) {
        if (name == null) return null;
        for (Station s : stations) {
            if (s.getName().equalsIgnoreCase(name.trim())) return s;
        }
        return null;
    }

    public double calculateFare(double distanceKm) {
        if (distanceKm <= 2) return 10.0;
        if (distanceKm <= 5) return 15.0;
        if (distanceKm <= 10) return 25.0;
        if (distanceKm <= 20) return 35.0;
        return 45.0;
    }

    public Ticket bookTicket(String sourceName, String destinationName) {
        Station s1 = findStationByName(sourceName);
        Station s2 = findStationByName(destinationName);
        if (s1 == null || s2 == null) {
            throw new IllegalArgumentException("Invalid Station Name. Please enter a valid Hyderabad Metro station.");
        }
        if (s1.getName().equalsIgnoreCase(s2.getName())) {
            throw new IllegalArgumentException("Source and destination cannot be the same.");
        }

        double distance = Math.abs(s2.getDistanceFromStart() - s1.getDistanceFromStart());
        double fare = calculateFare(distance);
        Ticket ticket = new Ticket(s1.getName(), s2.getName(), fare);
        return ticketDAO.saveTicket(ticket);
    }

    public List<Ticket> getAllTickets() {
        return ticketDAO.getAllTickets();
    }
}

package dao;

import model.Ticket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {
    private final String url = "jdbc:h2:mem:metrodb;DB_CLOSE_DELAY=-1";

    public TicketDAO() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        try (Connection con = getConnection();
             Statement stmt = con.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS ticket (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "source VARCHAR(100), " +
                    "destination VARCHAR(100), " +
                    "fare DOUBLE)");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, "sa", "");
    }

    public Ticket saveTicket(Ticket ticket) {
        String sql = "INSERT INTO ticket (source, destination, fare) VALUES (?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, ticket.getSource());
            ps.setString(2, ticket.getDestination());
            ps.setDouble(3, ticket.getFare());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) ticket.setId(rs.getInt(1));
            }
            return ticket;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving ticket", e);
        }
    }

    public List<Ticket> getAllTickets() {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT id, source, destination, fare FROM ticket ORDER BY id";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                tickets.add(new Ticket(
                        rs.getInt("id"),
                        rs.getString("source"),
                        rs.getString("destination"),
                        rs.getDouble("fare")
                ));
            }
            return tickets;
        } catch (SQLException e) {
            throw new RuntimeException("Error reading tickets", e);
        }
    }
}

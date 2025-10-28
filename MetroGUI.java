package gui;

import model.Ticket;
import service.MetroService;
import model.Station;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MetroGUI extends JFrame {

    private final MetroService metroService;
    private JComboBox<String> sourceCombo, destinationCombo;
    private JLabel fareLabel;
    private DefaultTableModel tableModel;

    public MetroGUI() {
        metroService = new MetroService();
        initUI();
    }

    private void initUI() {
        setTitle("Hyderabad Metro Ticket Booking (H2 Embedded)");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Hyderabad Metro Rail System", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setOpaque(true);
        header.setBackground(new Color(0, 102, 204));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(header, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        List<Station> stations = metroService.getStations();
        String[] stationNames = stations.stream().map(Station::getName).toArray(String[]::new);

        sourceCombo = new JComboBox<>(stationNames);
        destinationCombo = new JComboBox<>(stationNames);
        fareLabel = new JLabel("Fare: ₹0", SwingConstants.LEFT);

        JButton calcFareBtn = new JButton("Calculate Fare");
        calcFareBtn.setBackground(new Color(0, 102, 204));
        calcFareBtn.setForeground(Color.WHITE);
        calcFareBtn.addActionListener(this::calculateFare);

        JButton bookBtn = new JButton("Book Ticket");
        bookBtn.setBackground(new Color(0, 153, 76));
        bookBtn.setForeground(Color.WHITE);
        bookBtn.addActionListener(this::bookTicket);

        centerPanel.add(new JLabel("Source Station:"));
        centerPanel.add(sourceCombo);
        centerPanel.add(new JLabel("Destination Station:"));
        centerPanel.add(destinationCombo);
        centerPanel.add(calcFareBtn);
        centerPanel.add(fareLabel);
        centerPanel.add(new JLabel(""));
        centerPanel.add(bookBtn);

        add(centerPanel, BorderLayout.CENTER);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Source", "Destination", "Fare (₹)"}, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton refreshBtn = new JButton("Refresh Tickets");
        refreshBtn.addActionListener(e -> loadTickets());

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(refreshBtn, BorderLayout.NORTH);
        bottomPanel.add(scrollPane, BorderLayout.CENTER);
        bottomPanel.setPreferredSize(new Dimension(800, 230));
        add(bottomPanel, BorderLayout.SOUTH);

        loadTickets();
    }

    private void calculateFare(ActionEvent e) {
        String src = (String) sourceCombo.getSelectedItem();
        String dest = (String) destinationCombo.getSelectedItem();
        if (src.equals(dest)) {
            JOptionPane.showMessageDialog(this, "Source and destination cannot be the same!");
            return;
        }

        double distance = Math.abs(
                metroService.findStationByName(dest).getDistanceFromStart()
                        - metroService.findStationByName(src).getDistanceFromStart());
        double fare = metroService.calculateFare(distance);
        fareLabel.setText("Fare: ₹" + fare);
    }

    private void bookTicket(ActionEvent e) {
        try {
            String src = (String) sourceCombo.getSelectedItem();
            String dest = (String) destinationCombo.getSelectedItem();
            Ticket ticket = metroService.bookTicket(src, dest);
            JOptionPane.showMessageDialog(this,
                    "Ticket booked successfully!\n" +
                            "From: " + ticket.getSource() + "\nTo: " + ticket.getDestination() + "\nFare: ₹" + ticket.getFare(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            loadTickets();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                    "Booking Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTickets() {
        tableModel.setRowCount(0);
        List<Ticket> tickets = metroService.getAllTickets();
        for (Ticket t : tickets) {
            tableModel.addRow(new Object[]{t.getId(), t.getSource(), t.getDestination(), t.getFare()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MetroGUI gui = new MetroGUI();
            gui.setVisible(true);
        });
    }
}

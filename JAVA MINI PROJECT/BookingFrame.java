import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class BookingFrame extends JFrame {
    private String username;

    public BookingFrame(String username) {
        this.username = username;
        setTitle("Book Flight - Airline Management System");

        // Custom main panel with gradient background
        GradientPanel mainPanel = new GradientPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setLayout(new BorderLayout());

        // Title Label at the top
        JLabel titleLabel = new JLabel("Book Your Flight");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(255, 255, 255));  // White color for contrast
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form panel for inputs
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setOpaque(false);  // Make form panel transparent
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Source Place
        JLabel sourceLabel = new JLabel("Source Place:");
        JTextField sourceField = new JTextField();
        styleLabel(sourceLabel);
        styleTextField(sourceField);
        addToFormPanel(formPanel, gbc, sourceLabel, sourceField, 0);

        // Destination Place
        JLabel destinationLabel = new JLabel("Destination Place:");
        JTextField destinationField = new JTextField();
        styleLabel(destinationLabel);
        styleTextField(destinationField);
        addToFormPanel(formPanel, gbc, destinationLabel, destinationField, 1);

        // Flight Date
        JLabel dateLabel = new JLabel("Flight Date (YYYY-MM-DD):");
        JTextField dateField = new JTextField();
        styleLabel(dateLabel);
        styleTextField(dateField);
        addToFormPanel(formPanel, gbc, dateLabel, dateField, 2);

        // Flight Time
        JLabel timeLabel = new JLabel("Flight Time (HH:MM):");
        JTextField timeField = new JTextField();
        styleLabel(timeLabel);
        styleTextField(timeField);
        addToFormPanel(formPanel, gbc, timeLabel, timeField, 3);

        // Book Button at the bottom
        JButton bookButton = new JButton("Book Flight");
        styleButton(bookButton);
        bookButton.addActionListener(e -> handleBooking(sourceField, destinationField, dateField, timeField));

        // Adding form and button to the main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(bookButton, BorderLayout.SOUTH);

        // Set up the frame
        add(mainPanel);
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void handleBooking(JTextField sourceField, JTextField destinationField, JTextField dateField, JTextField timeField) {
        String source = sourceField.getText();
        String destination = destinationField.getText();
        String date = dateField.getText();
        String time = timeField.getText();

        if (source.isEmpty() || destination.isEmpty() || date.isEmpty() || time.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            java.sql.Date flightDate = new java.sql.Date(dateFormat.parse(date).getTime());
            java.sql.Time flightTime = new java.sql.Time(timeFormat.parse(time).getTime());

            Connection conn = DatabaseConnection.getConnection();
            String query = "INSERT INTO bookings (username, source_place, destination_place, flight_date, flight_time) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, source);
            stmt.setString(3, destination);
            stmt.setDate(4, flightDate);
            stmt.setTime(5, flightTime);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Flight booked successfully!");
            dispose();
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date or time format. Please use YYYY-MM-DD for date and HH:MM for time.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    private void addToFormPanel(JPanel panel, GridBagConstraints gbc, JLabel label, JTextField field, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(label, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private void styleLabel(JLabel label) {
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setForeground(new Color(255, 255, 255));  // White for visibility on gradient
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        textField.setPreferredSize(new Dimension(250, 30));
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
        button.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255), 2));
    }

    // Custom panel class for gradient background
    class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int width = getWidth();
            int height = getHeight();
            Color color1 = new Color(0, 102, 204);  // Dark blue
            Color color2 = new Color(153, 204, 255);  // Light blue
            GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, width, height);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BookingFrame("user123").setVisible(true));
    }
}

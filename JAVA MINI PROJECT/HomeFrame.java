import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HomeFrame extends JFrame {
    private String username;

    public HomeFrame(String username) {
        this.username = username;

        // Frame title
        setTitle("Airline Management System - Home");

        // Main Panel with background color and padding
        JPanel mainPanel = new JPanel() {
            // Custom paintComponent to add a background gradient
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color color1 = new Color(240, 248, 255);
                Color color2 = new Color(0, 102, 204);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Title label with custom font and alignment
        JLabel titleLabel = new JLabel("Welcome to Airline Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.BLACK);

        // Subtitle label for personalized welcome
        JLabel welcomeLabel = new JLabel("Hello, " + username);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setForeground(Color.YELLOW);

        // Panel for buttons with GridBagLayout for alignment
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false); // Transparent to show gradient background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);  // Space around buttons

        // Book Flight Button
        JButton bookFlightButton = new JButton("Book a Flight");
        styleButton(bookFlightButton);
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(bookFlightButton, gbc);
        bookFlightButton.addActionListener(e -> new BookingFrame(username).setVisible(true));

        // Cancel Booking Button
        JButton cancelBookingButton = new JButton("Cancel Booking");
        styleButton(cancelBookingButton);
        gbc.gridy = 1;
        buttonPanel.add(cancelBookingButton, gbc);
        cancelBookingButton.addActionListener(e -> new CancelBookingPage(username).setVisible(true));

        // Logout Button
        JButton logoutButton = new JButton("Logout");
        styleButton(logoutButton);
        gbc.gridy = 2;
        buttonPanel.add(logoutButton, gbc);
        logoutButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        // Add components to the main panel
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacer
        mainPanel.add(welcomeLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30))); // Spacer
        mainPanel.add(buttonPanel);

        // Set up the frame
        add(mainPanel);
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Center the frame
        setResizable(false);
    }

    // Helper method to style buttons with rounded edges, shadow effect, and improved colors
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(0, 153, 255));  // Brighter blue background
        button.setForeground(Color.WHITE);  // White text
        button.setFocusPainted(false);  // Remove focus border
        button.setPreferredSize(new Dimension(250, 45));  // Set button size
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 102, 204), 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor on hover
    }

    // Main method to run the home frame (for testing)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HomeFrame("user123").setVisible(true);  // Example username
        });
    }
}

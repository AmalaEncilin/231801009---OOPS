import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CancelBookingPage extends JFrame {

    private JTextField bookingIdField;
    private JButton cancelButton, backButton;

    public CancelBookingPage(String username) {
        setTitle("Cancel Booking");
        setSize(450, 300);  // Adjusted size for more space
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set up the panel and components
        JPanel cancelPanel = new JPanel();
        cancelPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Set the background color of the cancel panel to match the home UI background color
        cancelPanel.setBackground(new Color(255, 255, 255));  // Light background similar to Home UI
        
        // Add padding around content
        cancelPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Label with Font and Color
        JLabel titleLabel = new JLabel("Cancel Your Booking");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 204));  // Blue color for consistency with buttons
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);  // Space below the title
        gbc.anchor = GridBagConstraints.CENTER;
        cancelPanel.add(titleLabel, gbc);

        // Booking ID Label
        JLabel bookingIdLabel = new JLabel("Enter Booking ID:");
        bookingIdLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        bookingIdLabel.setForeground(new Color(0, 102, 204));  // Matching color with the title
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 0, 5, 0);  // Space between elements
        cancelPanel.add(bookingIdLabel, gbc);

        // Create a JPanel to hold the booking ID label and field together
        JPanel bookingPanel = new JPanel();
        bookingPanel.setLayout(new BorderLayout());
        bookingPanel.setBackground(new Color(255, 255, 255));  // Light background to match home UI

        // Create a JTextField for Booking ID with custom style to match other boxes
        bookingIdField = new JTextField();
        bookingIdField.setFont(new Font("Arial", Font.PLAIN, 16));
        bookingIdField.setPreferredSize(new Dimension(300, 40));  // Adjusted size for readability
        bookingIdField.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));  // Add border with color
        bookingIdField.setBackground(new Color(240, 248, 255));  // Light background similar to other input fields
        bookingIdField.setEditable(true);  // Ensure the text field is editable
        bookingIdField.requestFocusInWindow();  // Set focus to the text field on window open

        // Add the JTextField to the JPanel
        bookingPanel.add(bookingIdField, BorderLayout.CENTER);

        // Adjust GridBagConstraints for the JTextField to make it responsive
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;  // Allows the text field to stretch horizontally
        gbc.weightx = 1.0;  // Expands horizontally within the available space
        gbc.insets = new Insets(10, 0, 5, 0);  // Space between elements
        cancelPanel.add(bookingPanel, gbc);

        // Cancel Button with Styling
        cancelButton = new JButton("Cancel Booking");
        styleButton(cancelButton);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 10, 0);  // Space around button
        cancelPanel.add(cancelButton, gbc);

        // Action listener for the Cancel button
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int bookingId = Integer.parseInt(bookingIdField.getText());
                    cancelBooking(bookingId);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(CancelBookingPage.this, "Please enter a valid booking ID.");
                }
            }
        });

        // Back to Home Button with Styling
        backButton = new JButton("Back to Home");
        styleButton(backButton);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 0, 0);  // Space around button
        cancelPanel.add(backButton, gbc);

        // Action listener for the Back button
        backButton.addActionListener(e -> {
            new HomeFrame(username).setVisible(true);
            dispose();  // Close current page
        });

        // Add panel to frame
        add(cancelPanel);
    }

    // Method to style buttons with rounded edges, shadow effect, and improved colors
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(0, 102, 204));  // Blue background
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(250, 45));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 102, 204), 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));  // Change cursor on hover

        // Add Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 153, 255)); // Lighter blue on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 102, 204)); // Original color
            }
        });
    }

    // Method to cancel the booking by booking ID
    private void cancelBooking(int bookingId) {
        try {
            // Establish connection to the database
            Connection conn = DatabaseConnection.getConnection();

            // Check if the booking exists in the database
            String checkQuery = "SELECT * FROM bookings WHERE booking_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, bookingId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Booking exists, now cancel it by updating the status to 'canceled'
                String cancelQuery = "UPDATE bookings SET status = 'canceled' WHERE booking_id = ?";
                PreparedStatement cancelStmt = conn.prepareStatement(cancelQuery);
                cancelStmt.setInt(1, bookingId);
                cancelStmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Booking ID " + bookingId + " canceled successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Booking ID not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Run the cancel booking page
        SwingUtilities.invokeLater(() -> {
            new CancelBookingPage("user123").setVisible(true);
        });
    }
}

// Import Section
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

public class MovieRentalSystem extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private Connection conn;

    // Login Panel
    private JPanel modeSelectionPanel, adminLoginPanel, userLoginRegisterPanel;
    // Admin Panel
    private JPanel adminPanel;
    // User Panel
    private JPanel userPanel;

    // Admin Components
    private JTextField adminUsernameField, adminPasswordField;
    private JTextField movieTitleField, movieGenreField, movieIdField;
    private JButton addMovieBtn, updateMovieBtn, deleteMovieBtn, viewUsersBtn;

    // User Components
    private JTextField userNameField, userEmailField;
    private JTextField loginUserIdField, rentMovieIdField, returnMovieIdField;
    private JButton registerBtn, loginBtn, rentBtn, returnBtn, viewMyRentedMoviesBtn;

    private int loggedInUserId = -1;

    public MovieRentalSystem() {
        connectDB();
        setTitle("Movie Rental System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        initModeSelectionPanel();
        initAdminLoginPanel();
        initUserLoginRegisterPanel();
        initAdminPanel();
        initUserPanel();

        add(mainPanel);
        cardLayout.show(mainPanel, "modeSelection");
        setVisible(true);
    }

    private void connectDB() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MovieRental", "root", "YOUR_MYSQL_PASSWORD");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initModeSelectionPanel() {
        modeSelectionPanel = new JPanel();
        JButton adminMode = new JButton("Admin Login");
        JButton userMode = new JButton("User Login/Register");

        adminMode.addActionListener(e -> cardLayout.show(mainPanel, "adminLogin"));
        userMode.addActionListener(e -> cardLayout.show(mainPanel, "userLoginRegister"));

        modeSelectionPanel.add(adminMode);
        modeSelectionPanel.add(userMode);
        mainPanel.add(modeSelectionPanel, "modeSelection");
    }

    private void initAdminLoginPanel() {
        adminLoginPanel = new JPanel(new GridLayout(4, 2));
        adminUsernameField = new JTextField();
        adminPasswordField = new JPasswordField();
        JButton login = new JButton("Login as Admin");

        login.addActionListener(e -> {
            if (adminUsernameField.getText().equals("admin") && adminPasswordField.getText().equals("admin123")) {
                cardLayout.show(mainPanel, "adminPanel");
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect Admin Credentials");
            }
        });

        adminLoginPanel.add(new JLabel("Username:"));
        adminLoginPanel.add(adminUsernameField);
        adminLoginPanel.add(new JLabel("Password:"));
        adminLoginPanel.add(adminPasswordField);
        adminLoginPanel.add(login);
        mainPanel.add(adminLoginPanel, "adminLogin");
    }

    private void initUserLoginRegisterPanel() {
        userLoginRegisterPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // New User Registration Section
        JLabel newUserLabel = new JLabel("New User Registration:");
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        userLoginRegisterPanel.add(newUserLabel, gbc);

        JLabel nameLabel = new JLabel("Name:");
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        userLoginRegisterPanel.add(nameLabel, gbc);

        userNameField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 1;
        userLoginRegisterPanel.add(userNameField, gbc);

        JLabel emailLabel = new JLabel("Email:");
        gbc.gridx = 0; gbc.gridy = 2;
        userLoginRegisterPanel.add(emailLabel, gbc);

        userEmailField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 2;
        userLoginRegisterPanel.add(userEmailField, gbc);

        registerBtn = new JButton("Register");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        userLoginRegisterPanel.add(registerBtn, gbc);

        // Divider
        JLabel dividerLabel = new JLabel("OR");
        dividerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        userLoginRegisterPanel.add(dividerLabel, gbc);

        // Existing User Login Section
        JLabel existingUserLabel = new JLabel("Existing User Login:");
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        userLoginRegisterPanel.add(existingUserLabel, gbc);

        JLabel userIdLabel = new JLabel("User ID:");
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 1;
        userLoginRegisterPanel.add(userIdLabel, gbc);

        loginUserIdField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 6;
        userLoginRegisterPanel.add(loginUserIdField, gbc);

        loginBtn = new JButton("Login");
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        userLoginRegisterPanel.add(loginBtn, gbc);

        // Register Button Functionality
        registerBtn.addActionListener(e -> {
            try {
                String name = userNameField.getText().trim();
                String email = userEmailField.getText().trim();

                if (name.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Name and Email cannot be empty!");
                    return;
                }

                PreparedStatement ps = conn.prepareStatement("INSERT INTO Customers (name, email) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, name);
                ps.setString(2, email);
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    loggedInUserId = rs.getInt(1);
                    JOptionPane.showMessageDialog(this, "Registered with ID: " + loggedInUserId);
                    cardLayout.show(mainPanel, "userPanel");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Registration failed: " + ex.getMessage());
            }
        });

        // Login Button Functionality
        loginBtn.addActionListener(e -> {
            try {
                int userId = Integer.parseInt(loginUserIdField.getText().trim());
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM Customers WHERE id = ?");
                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    loggedInUserId = userId; // Ensure this is set correctly
                    cardLayout.show(mainPanel, "userPanel");
                } else {
                    JOptionPane.showMessageDialog(this, "User not found");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Login failed: " + ex.getMessage());
            }
        });

        mainPanel.add(userLoginRegisterPanel, "userLoginRegister");
    }

    private void initAdminPanel() {
        adminPanel = new JPanel(new GridLayout(7, 2));
        movieTitleField = new JTextField();
        movieGenreField = new JTextField();
        movieIdField = new JTextField();
        addMovieBtn = new JButton("Add Movie");
        updateMovieBtn = new JButton("Update Movie Genre");
        deleteMovieBtn = new JButton("Delete Movie");
        viewUsersBtn = new JButton("View Users Who Rented");
        JButton viewAllMoviesBtn = new JButton("View All Movies"); // New button
        JButton adminLogoutBtn = new JButton("Logout"); // Logout button

        addMovieBtn.addActionListener(e -> {
            try {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO Movies (title, genre) VALUES (?, ?)");
                ps.setString(1, movieTitleField.getText());
                ps.setString(2, movieGenreField.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Movie Added!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        updateMovieBtn.addActionListener(e -> {
            try {
                PreparedStatement ps = conn.prepareStatement("UPDATE Movies SET genre=? WHERE id=?");
                ps.setString(1, movieGenreField.getText());
                ps.setInt(2, Integer.parseInt(movieIdField.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Movie Updated!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        deleteMovieBtn.addActionListener(e -> {
            try {
                PreparedStatement ps = conn.prepareStatement("DELETE FROM Movies WHERE id=?");
                ps.setInt(1, Integer.parseInt(movieIdField.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Movie Deleted!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        viewUsersBtn.addActionListener(e -> {
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(
                    "SELECT Rentals.movie_id, Customers.name " +
                    "FROM Rentals " +
                    "JOIN Customers ON Rentals.user_id = Customers.id " +
                    "WHERE Rentals.returned_on IS NULL"
                );

                StringBuilder sb = new StringBuilder("Active Rentals:\n");
                while (rs.next()) {
                    sb.append("Movie ID: ").append(rs.getInt("movie_id"))
                      .append(" rented by ").append(rs.getString("name"))
                      .append("\n");
                }

                if (sb.length() == 14) { // No rentals found
                    sb.append("No active rentals found.");
                }

                JOptionPane.showMessageDialog(this, sb.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to Fetch Users Who Rented: " + ex.getMessage());
            }
        });

        // Action for "View All Movies" button
        viewAllMoviesBtn.addActionListener(e -> {
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM Movies");
                StringBuilder sb = new StringBuilder("All Movies:\n");
                while (rs.next()) {
                    sb.append("ID: ").append(rs.getInt("id"))
                      .append(", Title: ").append(rs.getString("title"))
                      .append(", Genre: ").append(rs.getString("genre"))
                      .append(", Available: ").append(rs.getBoolean("available") ? "Yes" : "No")
                      .append("\n");
                }
                JOptionPane.showMessageDialog(this, sb.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Action for "Logout" button
        adminLogoutBtn.addActionListener(e -> cardLayout.show(mainPanel, "modeSelection"));

        adminPanel.add(new JLabel("Movie Title:")); adminPanel.add(movieTitleField);
        adminPanel.add(new JLabel("Genre:")); adminPanel.add(movieGenreField);
        adminPanel.add(new JLabel("Movie ID (for Update/Delete):")); adminPanel.add(movieIdField);
        adminPanel.add(addMovieBtn); adminPanel.add(updateMovieBtn);
        adminPanel.add(deleteMovieBtn); adminPanel.add(viewUsersBtn);
        adminPanel.add(viewAllMoviesBtn); // Add "View All Movies" button
        adminPanel.add(adminLogoutBtn); // Add "Logout" button

        mainPanel.add(adminPanel, "adminPanel");
    }

    private void initUserPanel() {
        userPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Add padding between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Rent Movie Section
        JLabel rentMovieLabel = new JLabel("Rent Movie ID:");
        rentMovieIdField = new JTextField(15);
        rentBtn = new JButton("Rent Movie");

        gbc.gridx = 0; gbc.gridy = 0; // First column, first row
        userPanel.add(rentMovieLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; // Second column, first row
        userPanel.add(rentMovieIdField, gbc);
        gbc.gridx = 2; gbc.gridy = 0; // Third column, first row
        userPanel.add(rentBtn, gbc);

        // Return Movie Section
        JLabel returnMovieLabel = new JLabel("Return Movie ID:");
        returnMovieIdField = new JTextField(15);
        returnBtn = new JButton("Return Movie");

        gbc.gridx = 0; gbc.gridy = 1; // First column, second row
        userPanel.add(returnMovieLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; // Second column, second row
        userPanel.add(returnMovieIdField, gbc);
        gbc.gridx = 2; gbc.gridy = 1; // Third column, second row
        userPanel.add(returnBtn, gbc);

        // View My Rented Movies Section
        viewMyRentedMoviesBtn = new JButton("My Rented Movies");
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3; // Span across 3 columns
        userPanel.add(viewMyRentedMoviesBtn, gbc);

        // Show All Available Movies Button
        JButton showAvailableMoviesBtn = new JButton("Show Available Movies");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 3; // Span across 3 columns
        userPanel.add(showAvailableMoviesBtn, gbc);

        // Logout Button
        JButton userLogoutBtn = new JButton("Logout");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 3; // Span across 3 columns
        userPanel.add(userLogoutBtn, gbc);

        // Rent Movie Functionality
        rentBtn.addActionListener(e -> {
            try {
                int movieId = Integer.parseInt(rentMovieIdField.getText().trim());
                PreparedStatement ps = conn.prepareStatement("INSERT INTO Rentals (user_id, movie_id, rented_on, due_date) VALUES (?, ?, ?, ?)");
                ps.setInt(1, loggedInUserId); // Ensure loggedInUserId is set correctly
                ps.setInt(2, movieId);
                ps.setDate(3, Date.valueOf(LocalDate.now()));
                ps.setDate(4, Date.valueOf(LocalDate.now().plusDays(7)));
                ps.executeUpdate();

                PreparedStatement update = conn.prepareStatement("UPDATE Movies SET available = FALSE WHERE id = ?");
                update.setInt(1, movieId);
                update.executeUpdate();

                JOptionPane.showMessageDialog(this, "Movie Rented Successfully!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to Rent Movie: " + ex.getMessage());
            }
        });

        // Return Movie Functionality
        returnBtn.addActionListener(e -> {
            try {
                int movieId = Integer.parseInt(returnMovieIdField.getText().trim());
                PreparedStatement ps = conn.prepareStatement("UPDATE Rentals SET returned_on = ? WHERE movie_id = ? AND user_id = ? AND returned_on IS NULL");
                ps.setDate(1, Date.valueOf(LocalDate.now()));
                ps.setInt(2, movieId);
                ps.setInt(3, loggedInUserId);
                ps.executeUpdate();

                PreparedStatement update = conn.prepareStatement("UPDATE Movies SET available = TRUE WHERE id = ?");
                update.setInt(1, movieId);
                update.executeUpdate();

                JOptionPane.showMessageDialog(this, "Movie Returned Successfully!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to Return Movie: " + ex.getMessage());
            }
        });

        // Show Available Movies Functionality
        showAvailableMoviesBtn.addActionListener(e -> {
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT id, title, genre FROM Movies WHERE available = TRUE");
                StringBuilder sb = new StringBuilder("Available Movies:\n");
                while (rs.next()) {
                    sb.append("ID: ").append(rs.getInt("id"))
                      .append(", Title: ").append(rs.getString("title"))
                      .append(", Genre: ").append(rs.getString("genre"))
                      .append("\n");
                }
                JOptionPane.showMessageDialog(this, sb.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to Fetch Available Movies: " + ex.getMessage());
            }
        });

        // View My Rented Movies Functionality
        viewMyRentedMoviesBtn.addActionListener(e -> {
            try {
                PreparedStatement ps = conn.prepareStatement(
                    "SELECT Movies.title " +
                    "FROM Rentals " +
                    "JOIN Movies ON Rentals.movie_id = Movies.id " +
                    "WHERE Rentals.user_id = ? AND Rentals.returned_on IS NULL"
                );
                ps.setInt(1, loggedInUserId);
                ResultSet rs = ps.executeQuery();

                StringBuilder sb = new StringBuilder("Your Rented Movies:\n");
                while (rs.next()) {
                    sb.append(rs.getString("title")).append("\n");
                }

                if (sb.length() == 19) { // No movies found (length of "Your Rented Movies:\n")
                    sb.append("No rented movies found.");
                }

                JOptionPane.showMessageDialog(this, sb.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to Fetch Rented Movies: " + ex.getMessage());
            }
        });

        // Logout Functionality
        userLogoutBtn.addActionListener(e -> cardLayout.show(mainPanel, "modeSelection"));

        mainPanel.add(userPanel, "userPanel");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MovieRentalSystem::new);
    }
}

package EmployeeManagementSystemProject;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login_Signup extends JFrame {
    private static final long serialVersionUID = 1L;

    // --- Database Constants and Connection ---
    // Best practice: Use a separate config file or constants class for these
    private static final String DB_URL = "jdbc:mysql://localhost:3306/employeemanagement";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private Connection con = null;

    // --- UI Components for Login Panel (use JPanel for grouping) ---
    private final JPanel loginPanel = new JPanel(null); // Use null for absolute positioning
    private final JTextField namelogintxt = new JTextField();
    private final JPasswordField passwordlogintxt = new JPasswordField();
    private final JButton loginButton = new JButton("Login");

    // --- UI Components for Signup Panel (use JPanel for grouping) ---
    private final JPanel signupPanel = new JPanel(null); // Use null for absolute positioning
    private final JTextField namesignuptxt = new JTextField();
    private final JTextField emailsignuptxt = new JTextField();
    private final JTextField contactsignuptxt = new JTextField();
    private final JTextField addresssignuptxt = new JTextField();
    private final JTextField usernamesignuptxt = new JTextField();
    private final JPasswordField passwordsignuptxt = new JPasswordField();
    private final JPasswordField repasswordsignuptxt = new JPasswordField();
    private final JButton signupButton = new JButton("Signup");
    
    // --- Other UI Components ---
    private final Container contentPane;
    private final String imageFilePath = "file:///C:/Users/GAJENRA%20SINGH/eclipse-workspace/gajendra/src/images/Gajendra__Boss.jpg";

    // --- Constructor ---
    public Login_Signup() {
        setTitle("Signup & Login");
        setSize(1600, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPane = getContentPane();
        contentPane.setLayout(null); // Keep absolute layout for the main frame

        Connect();
        initComponents();
        addListeners();
        
        // Start with the Login frame visible
        showPanel(loginPanel);

        setVisible(true);
    }

    // --- Initialization Methods ---
    private void initComponents() {
        // 1. Title and Image Panel
        JLabel title = new JLabel("Login & Signup");
        title.setBounds(550, 0, 400, 100);
        title.setFont(new Font("Time new Roman", Font.BOLD, 50));
        title.setForeground(Color.BLACK);
        contentPane.add(title);
        
        // Custom panel for background image
        JPanel imagePanel = new JPanel() {
            private Image backgroundImage;
            {
                try {
                    backgroundImage = ImageIO.read(new File(imageFilePath));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Error loading image from path: " + imageFilePath);
                }
            }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 400, 200, getWidth(), getHeight(), this);
                }
            }
        };
        imagePanel.setBounds(480, 25, 55, 55);
        contentPane.add(imagePanel);

        // 2. Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Option");
        JMenuItem loginItem = new JMenuItem("Login");
        JMenuItem signupItem = new JMenuItem("Signup");

        menu.add(loginItem);
        menu.add(signupItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);
        

        // ActionListener for menu items (view switching)
        ActionListener menuListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
                if (e.getActionCommand().equals("Login")) {
                    showPanel(loginPanel);
                } else if (e.getActionCommand().equals("Signup")) {
                    showPanel(signupPanel);
                }
            }
        };
        loginItem.addActionListener(menuListener);
        signupItem.addActionListener(menuListener);

        // 3. Create and add Login and Signup Panels
        createLoginPanel();
        createSignupPanel();
        contentPane.add(loginPanel);
        contentPane.add(signupPanel);
    }

    private void createLoginPanel() {
        loginPanel.setBounds(0, 100, 1750, 900); // Set position and size for the panel
        
        JLabel namelogin = new JLabel("User Name -");
        JLabel passwordlogin = new JLabel("Password -");

        // Layout settings
        int labelX = 450, textX = 600, width = 200, height = 22, vGap = 80;
        int yStart = 100;

        namelogin.setBounds(labelX, yStart, 120, 25);
        passwordlogin.setBounds(labelX, yStart + vGap, 120, 25);
        namelogintxt.setBounds(textX, yStart, width, height);
        passwordlogintxt.setBounds(textX, yStart + vGap, width, height);
        loginButton.setBounds(600, 300, 150, 30);

        // Fonts
        namelogin.setFont(new Font("Castellar", Font.BOLD, 15));
        passwordlogin.setFont(new Font("Castellar", Font.BOLD, 15));
        loginButton.setFont(new Font("Time new Roman", Font.PLAIN, 18));
        
        // Add components to the panel
        loginPanel.add(namelogin);
        loginPanel.add(passwordlogin);
        loginPanel.add(namelogintxt);
        loginPanel.add(passwordlogintxt);
        loginPanel.add(loginButton);
    }

    private void createSignupPanel() {
        signupPanel.setBounds(0, 100, 3550, 1000); // Set position and size for the panel

        JLabel namesignup = new JLabel("Name -");
        JLabel emailsignup = new JLabel("Email -");
        JLabel contactsignup = new JLabel("Contact No. -");
        JLabel addresssignup = new JLabel("Address -");
        JLabel usernamesignup = new JLabel("User Name -");
        JLabel passwordsignup = new JLabel("Password -");
        JLabel repasswordsignup = new JLabel("Re Password -");

        // Layout settings
        int labelX = 500, textX = 700, width = 200, height = 22, vGap = 50;
        int yStart = 50;
        Font labelFont = new Font("Castellar", Font.BOLD, 15);

        JLabel[] labels = {namesignup, emailsignup, contactsignup, addresssignup, usernamesignup, passwordsignup, repasswordsignup};
        JTextField[] textFields = {namesignuptxt, emailsignuptxt, contactsignuptxt, addresssignuptxt, usernamesignuptxt, passwordsignuptxt};
        JPasswordField[] passwordFields = {repasswordsignuptxt};

        // Position and style labels/text fields
        for (int i = 0; i < labels.length; i++) {
            int y = yStart + i * vGap;
            labels[i].setBounds(labelX, y, 150, 25);
            labels[i].setFont(labelFont);
            labels[i].setForeground(Color.BLACK);
            signupPanel.add(labels[i]);
            
            if (i < textFields.length) {
                textFields[i].setBounds(textX, y, width, height);
                signupPanel.add(textFields[i]);
            }
        }
        
        // Position the RePassword field
        repasswordsignuptxt.setBounds(textX, yStart + 6 * vGap, width, height);
        signupPanel.add(repasswordsignuptxt);
        
        signupButton.setBounds(610, 430, 150, 30);
        signupButton.setFont(new Font("Time new Roman", Font.BOLD, 15));
        signupButton.setForeground(Color.BLACK);
        signupPanel.add(signupButton);
        
        // Initially hide the signup panel
        signupPanel.setVisible(false);
    }

    private void addListeners() {
        // --- Login Button Listener ---
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = namelogintxt.getText();
                String password = new String(passwordlogintxt.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username and Password cannot be empty!", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (validateLogin(username, password)) {
                    JOptionPane.showMessageDialog(null, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                    new modified_Home1().setVisible(true);
                    setVisible(false);
                }
                
                if(validateLogin(username, password)){
                    JOptionPane.showMessageDialog(null, "welcome to Gajendra", "Welcome", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                    new modified_Home1().setVisible(false);
                    setVisible(false);
                    
                }
                  else {
                    JOptionPane.showMessageDialog(null, "Invalid Username or Password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // --- Signup Button Listener ---
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get data from fields
                String name = namesignuptxt.getText().trim();
                String email = emailsignuptxt.getText().trim(); // Corrected: Reading from JTextField
                String contact = contactsignuptxt.getText().trim();
                String address = addresssignuptxt.getText().trim();
                String username = usernamesignuptxt.getText().trim();
                String password = new String(passwordsignuptxt.getPassword());
                String repassword = new String(repasswordsignuptxt.getPassword());

                // Validation
                if (name.isEmpty() || email.isEmpty() || contact.isEmpty() || address.isEmpty() || 
                    username.isEmpty() || password.isEmpty() || repassword.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                    // You might want to use individual checks to set focus:
                    // if (name.isEmpty()) namesignuptxt.requestFocus();
                    return;
                }
                
                if (!password.equals(repassword)) {
                    JOptionPane.showMessageDialog(null, "Passwords do not match.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    passwordsignuptxt.requestFocus();
                    return;
                }

                // Database Insert
                try (PreparedStatement pst = con.prepareStatement(
                        "INSERT INTO loginsignup(Name, Email, Contact, Address, UserName, Password, RePassword) VALUES(?, ?, ?, ?, ?, ?, ?)")
                ) {
                    pst.setString(1, name);
                    pst.setString(2, email);
                    pst.setString(3, contact);
                    pst.setString(4, address);
                    pst.setString(5, username);
                    pst.setString(6, password);
                    pst.setString(7, repassword);
                    
                    int rowsAffected = pst.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearFields();
                        // Optionally switch to login view after successful signup
                        showPanel(loginPanel);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Database Error: Failed to register user.", "Error", JOptionPane.ERROR_MESSAGE);
                  
                }
            }
        });
    }

    // --- Utility Methods ---
    
    /**
     * Switches between the login and signup views.
     * @param panelToShow The JPanel to make visible (loginPanel or signupPanel).
     */
    private void showPanel(JPanel panelToShow) {
        loginPanel.setVisible(panelToShow == loginPanel);
        signupPanel.setVisible(panelToShow == signupPanel);
    }
    
    public void clearFields() {
        // Clear all fields for both forms
        namelogintxt.setText("");
        passwordlogintxt.setText("");
        
        namesignuptxt.setText("");
        emailsignuptxt.setText("");
        contactsignuptxt.setText("");
        addresssignuptxt.setText("");
        usernamesignuptxt.setText("");
        passwordsignuptxt.setText("");
        repasswordsignuptxt.setText("");
    }

    // --- Database Methods ---

    public void Connect() {
        try {
            // Loading the driver is often optional now, but kept for compatibility
            Class.forName("com.mysql.jdbc.Driver"); 
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            // Optionally check if connection is null here
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to the database!", "Database Connection Error", JOptionPane.ERROR_MESSAGE);
           
        }
        
    }
    
    // Note: Switched to 'com.mysql.cj.jdbc.Driver' which is the modern class name.

    private boolean validateLogin(String username, String password) {
        // Use try-with-resources for automatic resource management
        String sql = "SELECT UserName FROM loginsignup WHERE UserName = ? AND Password = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, username);
            pst.setString(2, password);
            
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next(); // true if a matching record is found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // A JOptionPane here might be redundant but useful for debugging
            return false;
        }
    }

    public static void main (String [] args) {
        // Use EventQueue.invokeLater for thread safety in Swing apps
        SwingUtilities.invokeLater(() -> new Login_Signup());
    }
}
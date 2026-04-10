package EmployeeManagementSystemProject;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * ViewEmployeesPanel
 * -------------------
 * ✅ Displays all employees in a JTable.
 * ✅ Allows searching, editing, deleting, and refreshing records.
 * ✅ Connects to MySQL database (employeemanagement).
 */
public class ViewEmployeesPanel extends JPanel {

    // =============== COMPONENT DECLARATIONS ===============
    private JTable employeeTable;             // Table to display employee data
    private DefaultTableModel tableModel;     // Table model to manage table data
    private JButton btnRefresh;               // Refresh button to reload data
    private JTextField txtSearch;             // Search input box

    // =============== CONSTRUCTOR ===============
    public ViewEmployeesPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // -------------------- TOP PANEL --------------------
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);

        // Label and TextField for Search
        topPanel.add(new JLabel(" Search (by ID, Name, Dept, Contact):"));
        txtSearch = new JTextField(18);
        topPanel.add(txtSearch);

        // Refresh Button styling
        btnRefresh = new JButton("Refresh");
        btnRefresh.setBackground(new Color(0, 123, 255));  // Blue
        btnRefresh.setForeground(Color.WHITE);
        topPanel.add(btnRefresh);

        add(topPanel, BorderLayout.NORTH); // Add top panel to frame

        // -------------------- TABLE SETUP --------------------
        // Define table columns
        String[] columns = {
            "ID", "Name", "DOB", "Contact", "Dept",
            "Position", "Salary", "Join Date", "Address", "Email", " Edit", " Delete"
        };

        // Table model — only Edit/Delete columns are editable
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 10 || column == 11; // Only last 2 columns editable
            }
        };

        // Table properties
        employeeTable = new JTable(tableModel);
        employeeTable.setRowHeight(25);
        employeeTable.setFont(new Font("Arial", Font.PLAIN, 12));
        employeeTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        // Add table to scroll pane (for scrolling)
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        add(scrollPane, BorderLayout.CENTER);

        // =============== EVENT LISTENERS ===============

        // Reloads data when refresh button is clicked
        btnRefresh.addActionListener(e -> loadEmployeeData(""));

        // Real-time search — triggers every time user types
        txtSearch.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                loadEmployeeData(txtSearch.getText().trim());
            }
        });

        // Add edit/delete mouse click functionality
        addActionButtonsToTable();

        // Load data initially when panel opens
        loadEmployeeData("");
    }

    // ==================================================================
    //  METHOD: Load Employee Data from Database (with optional filtering)
    // ==================================================================
    private void loadEmployeeData(String searchText) {
        try {
            tableModel.setRowCount(0); // Clear previous data

            // Load JDBC driver and connect to MySQL
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/employeemanagement?useSSL=false&serverTimezone=UTC",
                    "root", "");

            // SQL Query
            String sql = "SELECT * FROM addemployees";
            if (!searchText.isEmpty()) {
                // Add WHERE clause if user typed something
                sql += " WHERE CAST(emp_id AS CHAR) LIKE ? OR emp_name LIKE ? OR dept LIKE ? OR contact LIKE ?";
            }

            PreparedStatement pst = con.prepareStatement(sql);

            // If there is a search input, bind it to query
            if (!searchText.isEmpty()) {
                String pattern = "%" + searchText + "%";
                pst.setString(1, pattern);
                pst.setString(2, pattern);
                pst.setString(3, pattern);
                pst.setString(4, pattern);
            }

            // Execute query and get results
            ResultSet rs = pst.executeQuery();

            // Add each employee to the table
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("emp_id"),
                        rs.getString("emp_name"),
                        rs.getString("dob"),
                        rs.getString("contact"),
                        rs.getString("dept"),
                        rs.getString("position"),
                        rs.getDouble("salary"),
                        rs.getString("join_date"),
                        rs.getString("address"),
                        rs.getString("email"),
                        "Edit", "Delete" // Buttons displayed as text
                });
            }

            // Close connections
            rs.close();
            pst.close();
            con.close();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, " Error loading data: " + ex.getMessage());
        }
    }

    // ==================================================================
    //  METHOD: Add "Edit" and "Delete" action when clicking table cells
    // ==================================================================
    private void addActionButtonsToTable() {
        employeeTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = employeeTable.rowAtPoint(e.getPoint());      // Which row clicked
                int column = employeeTable.columnAtPoint(e.getPoint()); // Which column clicked

                // If user clicked on "Delete" column
                if (column == 11) deleteEmployee(row);

                // If user clicked on "Edit" column
                if (column == 10) openEditDialog(row);
            }
        });
    }

    // ==================================================================
    //  METHOD: Delete Employee by ID
    // ==================================================================
    private void deleteEmployee(int row) {
        int empId = (int) tableModel.getValueAt(row, 0); // Get employee ID

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete Employee ID " + empId + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Connect to database
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/employeemanagement", "root", "");

                // Delete employee
                PreparedStatement pst = con.prepareStatement("DELETE FROM addemployees WHERE emp_id = ?");
                pst.setInt(1, empId);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "✅ Employee deleted!");
                loadEmployeeData(""); // Refresh table after deletion

                pst.close();
                con.close();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error deleting: " + ex.getMessage());
            }
        }
    }

    // ==================================================================
    //  METHOD: Edit Employee (shows input dialog to update record)
    // ==================================================================
    private void openEditDialog(int row) {
        int empId = (int) tableModel.getValueAt(row, 0); // Get selected employee ID

        // Create editable text fields pre-filled with existing data
        JTextField nameField = new JTextField((String) tableModel.getValueAt(row, 1));
        JTextField dobField = new JTextField((String) tableModel.getValueAt(row, 2));
        JTextField contactField = new JTextField((String) tableModel.getValueAt(row, 3));
        JTextField deptField = new JTextField((String) tableModel.getValueAt(row, 4));
        JTextField positionField = new JTextField((String) tableModel.getValueAt(row, 5));
        JTextField salaryField = new JTextField(String.valueOf(tableModel.getValueAt(row, 6)));
        JTextField joinDateField = new JTextField((String) tableModel.getValueAt(row, 7));
        JTextField addressField = new JTextField((String) tableModel.getValueAt(row, 8));
        JTextField emailField = new JTextField((String) tableModel.getValueAt(row, 9));

        // Panel for form layout (2 columns)
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("DOB:")); panel.add(dobField);
        panel.add(new JLabel("Contact:")); panel.add(contactField);
        panel.add(new JLabel("Department:")); panel.add(deptField);
        panel.add(new JLabel("Position:")); panel.add(positionField);
        panel.add(new JLabel("Salary:")); panel.add(salaryField);
        panel.add(new JLabel("Join Date:")); panel.add(joinDateField);
        panel.add(new JLabel("Address:")); panel.add(addressField);
        panel.add(new JLabel("Email:")); panel.add(emailField);

        // Show editable dialog box
        int option = JOptionPane.showConfirmDialog(this, panel,
                "Edit Employee (ID: " + empId + ")", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // If user clicks OK, update record in DB
        if (option == JOptionPane.OK_OPTION) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/employeemanagement", "root", "");

                // Update query with placeholders
                String sql = "UPDATE addemployees SET emp_name=?, dob=?, contact=?, dept=?, position=?, salary=?, join_date=?, address=?, email=? WHERE emp_id=?";
                PreparedStatement pst = con.prepareStatement(sql);

                // Bind user input to query
                pst.setString(1, nameField.getText());
                pst.setString(2, dobField.getText());
                pst.setString(3, contactField.getText());
                pst.setString(4, deptField.getText());
                pst.setString(5, positionField.getText());
                pst.setDouble(6, Double.parseDouble(salaryField.getText()));
                pst.setString(7, joinDateField.getText());
                pst.setString(8, addressField.getText());
                pst.setString(9, emailField.getText());
                pst.setInt(10, empId);

                // Execute update
                int updated = pst.executeUpdate();
                if (updated > 0) {
                    JOptionPane.showMessageDialog(this, "Employee updated successfully!");
                    loadEmployeeData(""); // Refresh data
                }

                pst.close();
                con.close();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error updating employee: " + ex.getMessage());
            }
        }
    }

    // ==================================================================
    //  MAIN METHOD (for standalone testing)
    // ==================================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("View Employees");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(1000, 500);
            f.add(new ViewEmployeesPanel());
            f.setVisible(true);
        });
    }
}

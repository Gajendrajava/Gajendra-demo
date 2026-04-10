package EmployeeManagementSystemProject;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class addemployees extends JPanel {

    private JTextField txtEmpId, txtEmpName, txtContact, txtSalary, txtAddress, txtEmail;
    private JComboBox<String> comboDept, comboPosition;
    private JDateChooser dobChooser, joinDateChooser;
    private JButton btnSave;

    public addemployees() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        JLabel titleLabel = new JLabel("Add Employee", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(new Color(0, 102, 204));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = y++;
        add(titleLabel, gbc);
        gbc.gridwidth = 1;

        addLabelAndField("Employee ID:", txtEmpId = new JTextField(20), gbc, y++);
        addLabelAndField("Employee Name:", txtEmpName = new JTextField(20), gbc, y++);

        dobChooser = new JDateChooser();
        addLabelAndComponent("Date of Birth:", dobChooser, gbc, y++);

        addLabelAndField("Contact:", txtContact = new JTextField(20), gbc, y++);

        comboDept = new JComboBox<>(new String[]{
            "Select Department", "HR", "Finance", "IT", "Sales", "Maintenance"
        });
        addLabelAndComponent("Department:", comboDept, gbc, y++);

        comboPosition = new JComboBox<>(new String[]{
            "Select Position", "Manager", "Team Lead", "Clerk", "Receptionist", "Technician"
        });
        addLabelAndComponent("Position:", comboPosition, gbc, y++);

        addLabelAndField("Salary:", txtSalary = new JTextField(20), gbc, y++);

        joinDateChooser = new JDateChooser();
        addLabelAndComponent("Join Date:", joinDateChooser, gbc, y++);

        addLabelAndField("Address:", txtAddress = new JTextField(20), gbc, y++);
        addLabelAndField("E-mail:", txtEmail = new JTextField(20), gbc, y++);

        btnSave = new JButton("Save Employee");
        btnSave.setBackground(new Color(50, 150, 250));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 1;
        gbc.gridy = y;
        add(btnSave, gbc);

        btnSave.addActionListener(e -> saveEmployeeData());
    }

    private void addLabelAndField(String labelText, JTextField field, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel(labelText), gbc);
        gbc.gridx = 1;
        add(field, gbc);
    }

    private void addLabelAndComponent(String labelText, JComponent comp, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel(labelText), gbc);
        gbc.gridx = 1;
        add(comp, gbc);
    }

    private void saveEmployeeData() {
        try {
            // ✅ Correct Driver and Connection
            Class.forName("com.mysql.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/employeemanagement", "root", "")) {

                int empId = Integer.parseInt(txtEmpId.getText().trim());
                String name = txtEmpName.getText().trim();
                String contact = txtContact.getText().trim();
                String dept = comboDept.getSelectedItem().toString();
                String position = comboPosition.getSelectedItem().toString();
                double salary = Double.parseDouble(txtSalary.getText().trim());
                String address = txtAddress.getText().trim();
                String email = txtEmail.getText().trim();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dob = (dobChooser.getDate() != null) ? sdf.format(dobChooser.getDate()) : null;
                String joinDate = (joinDateChooser.getDate() != null) ? sdf.format(joinDateChooser.getDate()) : null;

                if (name.isEmpty() || contact.isEmpty() || dept.equals("Select Department") || position.equals("Select Position")) {
                    JOptionPane.showMessageDialog(this, "⚠️ Please fill all required fields!");
                    return;
                }

                String sql = "INSERT INTO addemployees (emp_id, emp_name, dob, contact, dept, position, salary, join_date, address, email) "
                           + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement pst = con.prepareStatement(sql)) {
                    pst.setInt(1, empId);
                    pst.setString(2, name);
                    pst.setString(3, dob);
                    pst.setString(4, contact);
                    pst.setString(5, dept);
                    pst.setString(6, position);
                    pst.setDouble(7, salary);
                    pst.setString(8, joinDate);
                    pst.setString(9, address);
                    pst.setString(10, email);

                    int rows = pst.executeUpdate();
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(this, "✅ Employee added successfully!");
                        clearFields();
                    }
                }
            }
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "❌ JDBC Driver not found! Add mysql-connector-j.jar");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Database Error: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ ID & Salary must be numeric values!");
        }
    }

    private void clearFields() {
        txtEmpId.setText("");
        txtEmpName.setText("");
        txtContact.setText("");
        txtSalary.setText("");
        txtAddress.setText("");
        txtEmail.setText("");
        comboDept.setSelectedIndex(0);
        comboPosition.setSelectedIndex(0);
        dobChooser.setDate(null);
        joinDateChooser.setDate(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Add Employee");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 650);
            frame.add(new addemployees());
            frame.setVisible(true);
        });
    }
}

package EmployeeManagementSystemProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import com.toedter.calendar.JDateChooser;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddAttendence extends JPanel {

    JTextField txtEmpId, txtEmpName;
    JComboBox<String> cmbStatus;
    JDateChooser dateChooser, searchDateChooser;
    JButton btnMark, btnViewAll, btnClear, btnSearch;
    JTable table;
    DefaultTableModel model;

    String url = "jdbc:mysql://localhost:3306/employeemanagement";
    String user = "root";
    String pass = "";

    public AddAttendence() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ---------- TOP PANEL ----------
        JPanel topPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        topPanel.add(new JLabel("Employee ID:"));
        txtEmpId = new JTextField();
        topPanel.add(txtEmpId);

        topPanel.add(new JLabel("Employee Name:"));
        txtEmpName = new JTextField();
        txtEmpName.setEditable(false);
        topPanel.add(txtEmpName);

        topPanel.add(new JLabel("Select Date:"));
        dateChooser = new JDateChooser();
        dateChooser.setDate(new Date());
        dateChooser.setDateFormatString("yyyy-MM-dd");
        topPanel.add(dateChooser);

        topPanel.add(new JLabel("Status:"));
        cmbStatus = new JComboBox<>(new String[]{"Present", "Absent", "Leave"});
        topPanel.add(cmbStatus);

        add(topPanel, BorderLayout.NORTH);

        // ---------- SEARCH PANEL ABOVE TABLE ----------
        JPanel searchPanel = new JPanel();
        searchPanel.setBorder(BorderFactory.createTitledBorder("🔍 Search Attendance by Date"));
        searchDateChooser = new JDateChooser();
        searchDateChooser.setDateFormatString("yyyy-MM-dd");
        btnSearch = new JButton("Search");
        btnViewAll = new JButton("View All");

        searchPanel.add(new JLabel("Select Date:"));
        searchPanel.add(searchDateChooser);
        searchPanel.add(btnSearch);
        searchPanel.add(btnViewAll);

        // ---------- TABLE PANEL ----------
        model = new DefaultTableModel(new String[]{"ID", "Emp ID", "Name", "Date", "Status"}, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);

        // Combine Search + Table vertically
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(sp, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // ---------- BUTTON PANEL ----------
        JPanel btnPanel = new JPanel();
        btnMark = new JButton("Mark Attendance");
        btnClear = new JButton("Clear");
        btnPanel.add(btnMark);
        btnPanel.add(btnClear);
        add(btnPanel, BorderLayout.SOUTH);

        // ---------- EVENT HANDLERS ----------
        txtEmpId.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                fetchEmployeeName();
            }
        });

        btnMark.addActionListener(e -> markAttendance());
        btnViewAll.addActionListener(e -> loadAttendance());
        btnClear.addActionListener(e -> clearFields());
        btnSearch.addActionListener(e -> searchAttendanceByDate());

        loadAttendance();
    }

    // FETCH EMPLOYEE NAME
    void fetchEmployeeName() {
        String empId = txtEmpId.getText().trim();
        if (empId.isEmpty()) {
            txtEmpName.setText("");
            return;
        }

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = con.prepareStatement("SELECT emp_name FROM addemployees WHERE emp_id=?")) {

            ps.setInt(1, Integer.parseInt(empId));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                txtEmpName.setText(rs.getString("emp_name"));
            } else {
                txtEmpName.setText("Not Found");
            }
        } catch (Exception e) {
            txtEmpName.setText("Error");
        }
    }

    // MARK ATTENDANCE
    void markAttendance() {
        String empId = txtEmpId.getText().trim();
        String empName = txtEmpName.getText().trim();
        String status = cmbStatus.getSelectedItem().toString();
        Date selectedDate = dateChooser.getDate();

        if (empId.isEmpty() || empName.isEmpty() || empName.equals("Not Found") || selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(selectedDate);

        try (Connection con = DriverManager.getConnection(url, user, pass)) {
            // Check if attendance already exists
            PreparedStatement check = con.prepareStatement("SELECT * FROM add_attendance WHERE emp_id=? AND date=?");
            check.setInt(1, Integer.parseInt(empId));
            check.setString(2, dateStr);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Attendance already marked for this date!");
                return;
            }

            // Insert attendance record
            PreparedStatement ps = con.prepareStatement(
                 "INSERT INTO add_attendance(emp_id, emp_name, date, status) VALUES(?, ?, ?, ?)");
            
      
            ps.setInt(1, Integer.parseInt(empId));
            ps.setString(2, empName);
            ps.setString(3, dateStr);
            ps.setString(4, status);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "✅ Attendance Marked Successfully!");
            loadAttendance();
            clearFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    // LOAD ALL ATTENDANCE
    void loadAttendance() {
        model.setRowCount(0);
        try (Connection con = DriverManager.getConnection(url, user, pass);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM add_attendance ORDER BY date DESC")) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getInt("emp_id"),
                        rs.getString("emp_name"),
                        rs.getDate("date"),
                        rs.getString("status")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }

    // SEARCH ATTENDANCE BY DATE
    void searchAttendanceByDate() {
        Date selectedDate = searchDateChooser.getDate();
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Please select a date to search!");
            return;
        }

        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(selectedDate);
        model.setRowCount(0);

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = con.prepareStatement("SELECT * FROM add_attendance WHERE date=?")) {

            ps.setString(1, dateStr);
            ResultSet rs = ps.executeQuery();

            boolean found = false;
            while (rs.next()) {
                found = true;
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getInt("emp_id"),
                        rs.getString("emp_name"),
                        rs.getDate("date"),
                        rs.getString("status")
                });
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "No attendance found for " + dateStr);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }

    // CLEAR FIELDS
    void clearFields() {
        txtEmpId.setText("");
        txtEmpName.setText("");
        cmbStatus.setSelectedIndex(0);
        dateChooser.setDate(new Date());
        searchDateChooser.setDate(null);
        table.clearSelection();
    }

    // ---------- TEST MAIN ----------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Add Attendance");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.add(new AddAttendence());
            frame.setVisible(true);
        });
    }
}

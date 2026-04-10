package EmployeeManagementSystemProject;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class modified_Home1 extends JFrame {

    // 🔹 Panels and Layout
    JPanel sidebar, mainPanel;
    JLabel TitleLabel;
    CardLayout cardLayout; // for switching between different panels (Add/View/Attendance)

    // 🔹 Sidebar button names
    String[] buttonLabel = {"👥 Add Employees", "🕓 Add Attendance", "👁 View", "🚪 Logout"};

    // 🔹 Constant names for identifying cards (panels)
    private static final String VIEW_ADD_EMPLOYEE = "AddEmployee";
    private static final String VIEW_ADD_ATTENDANCE = "AddAttendance";
    private static final String VIEW_EMPLOYEE = "ViewEmployees";

    // 🔹 Object references for each panel
    addemployees addEmployeePanel;
    ViewEmployeesPanel viewEmployeePanel;
    AddAttendence addAttendancePanel;

    // 🔹 Constructor (Program start hone pe sab load hota h)
    public modified_Home1() {

        setTitle("Employee Management - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 800);
        setLayout(new BorderLayout());

        // 🔹 Left Sidebar create karna
        sidebar = createsidebar();
        add(sidebar, BorderLayout.WEST);

        // 🔹 CardLayout initialize karna for switching panels
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 🔹 Add Employee panel (Form for adding employee)
        addEmployeePanel = new addemployees();
        mainPanel.add(addEmployeePanel, VIEW_ADD_EMPLOYEE);

        // 🔹 View Employee panel (Table of employees)
        viewEmployeePanel = new ViewEmployeesPanel();
        mainPanel.add(viewEmployeePanel, VIEW_EMPLOYEE);

        // 🔹 Add Attendance panel
        addAttendancePanel = new AddAttendence();
        mainPanel.add(addAttendancePanel, VIEW_ADD_ATTENDANCE);

        // 🔹 Default Blank Welcome Panel (jab kuch select na ho)
        JPanel blankPanel = new JPanel(new GridBagLayout());
        blankPanel.setBackground(Color.WHITE);
        JLabel welcomeLabel = new JLabel("Welcome! Select an option from the sidebar.");
        welcomeLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        blankPanel.add(welcomeLabel);
        mainPanel.add(blankPanel, "BLANK_PANEL");

        // 🔹 Start me blank panel show kare
        cardLayout.show(mainPanel, "BLANK_PANEL");

        // 🔹 Main area me panel add karna
        add(mainPanel, BorderLayout.CENTER);

        // 🔹 Window center me open karna
        setLocationRelativeTo(null);
    }

    // =============================== //
    // 🔹 Sidebar creation method
    // =============================== //
    public JPanel createsidebar() {
        JPanel sbpanel = new JPanel();
        sbpanel.setLayout(new BoxLayout(sbpanel, BoxLayout.Y_AXIS));
        sbpanel.setBackground(Color.DARK_GRAY);
        sbpanel.setPreferredSize(new Dimension(250, getHeight()));

        // 🔹 Sidebar title
        TitleLabel = new JLabel("⚙ Management");
        TitleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        TitleLabel.setForeground(Color.white);

        sbpanel.add(Box.createRigidArea(new Dimension(0, 20)));
        TitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sbpanel.add(TitleLabel);
        sbpanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // 🔹 Loop se sidebar buttons create karna
        for (String text : buttonLabel) {
            JButton button = new JButton(text);
            button.setFont(new Font("SansSerif", Font.PLAIN, 14));
            button.setMaximumSize(new Dimension(200, 40));
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setBackground(new Color(50, 150, 180));
            button.setForeground(Color.WHITE);
            button.setOpaque(true);
            button.setBorderPainted(false);

            // 🔹 Each button ke liye listener attach karna
            button.addActionListener(new SidebarButtonListener(text));

            sbpanel.add(button);
            sbpanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        sbpanel.add(Box.createVerticalGlue());
        return sbpanel;
    }

    // ===================================== //
    // 🔹 Action Listener for Sidebar Buttons
    // ===================================== //
    private class SidebarButtonListener implements ActionListener {
        private String buttonText;

        public SidebarButtonListener(String text) {
            this.buttonText = text;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // 🔹 Switch statement sidebar ke button click ke according panel show karega
            switch (buttonText) {

                case "👥 Add Employees":
                    cardLayout.show(mainPanel, VIEW_ADD_EMPLOYEE);
                    break;

                case "👁 View":
                    cardLayout.show(mainPanel, VIEW_EMPLOYEE);
                    break;

                case "🕓 Add Attendance":
                    cardLayout.show(mainPanel, VIEW_ADD_ATTENDANCE);
                    break;

                case "🚪 Logout":
                    // 🔹 Confirmation message before logout
                    int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to logout?", "Logout",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (result == JOptionPane.YES_OPTION) {
                        JOptionPane.showMessageDialog(null, "Logout successful!");
                        
                        // 🔹 Current dashboard band kare
                        dispose();

                        // 🔹 Login page fir se open kare
                        SwingUtilities.invokeLater(() -> {
                            new Login_Signup().setVisible(true); // ← yaha apka login frame class name dena h
                        });
                    }
                    break;
            }
        }
    }

    // ================================= //
    // 🔹 Main method (program entry point)
    // ================================= //
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new modified_Home1().setVisible(true));
    }
}

package EmployeeManagementSystemProject;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JFrame {

    JProgressBar progress;

    public SplashScreen() {

        setTitle("Employee Management System - Loading");
        setSize(600, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        getContentPane().setBackground(Color.WHITE);

        // --- Title ---
        JLabel title = new JLabel("Employee Management System", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBounds(0, 50, 600, 40);
        add(title);

        // --- Loading Message ---
        JLabel loading = new JLabel("Loading, please wait...", JLabel.CENTER);
        loading.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loading.setBounds(0, 100, 600, 30);
        add(loading);

        // --- Progress Bar ---
        progress = new JProgressBar();
        progress.setBounds(100, 180, 400, 30);
        progress.setStringPainted(true);
        add(progress);

        setVisible(true);

        startLoading();
    }

    // -------------------------
    // Progress Bar Animation (8 Seconds)
    // -------------------------
    private void startLoading() {
        new Thread(() -> {
            try {
                for (int i = 0; i <= 100; i++) {

                    progress.setValue(i);

                    // 8000 ms (8 sec) total
                    // 8000 / 100 = 80 ms per step  
                    Thread.sleep(60);
                }

            } catch (Exception ignored) {}

            dispose();      // Close Splash Screen

            new Login_Signup();   // Open Login Page
        }).start();
    }

    // -----------------------
    // Main Method
    // -----------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SplashScreen();
        });
    }
}

package com.taskreminder.ui;

import com.taskreminder.model.TaskManager;
import com.taskreminder.thread.NotificationThread;
import com.taskreminder.util.FileHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Main Application Frame
 * Demonstrates: JFrame, Layout Managers, Event Handling, Menu
 */
public class MainFrame extends JFrame {
    private TaskPanel taskPanel;
    private NotificationThread notificationThread;
    private JLabel statusLabel;
    
    public MainFrame() {
        setTitle("Task Reminder Application");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        // Initialize components
        initComponents();
        initMenuBar();
        
        // Load tasks from file
        loadTasks();
        
        // Start notification thread
        startNotificationThread();
        
        // Add window listener for saving on close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(41, 128, 185));
        JLabel titleLabel = new JLabel("📋 Task Reminder Application");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Task Panel (Center)
        taskPanel = new TaskPanel();
        JScrollPane scrollPane = new JScrollPane(taskPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        JButton addButton = createStyledButton("➕ Add Task", new Color(46, 204, 113));
        JButton refreshButton = createStyledButton("🔄 Refresh", new Color(52, 152, 219));
        JButton deleteButton = createStyledButton("🗑️ Delete Completed", new Color(231, 76, 60));
        
        addButton.addActionListener(e -> showAddTaskDialog());
        refreshButton.addActionListener(e -> taskPanel.refreshTasks());
        deleteButton.addActionListener(e -> deleteCompletedTasks());
        
        buttonPanel.add(addButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);
        
        // Status Panel
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusPanel.add(statusLabel, BorderLayout.WEST);
        
        // Add to frame
        add(titlePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(statusPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(180, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save Tasks");
        JMenuItem loadItem = new JMenuItem("Load Tasks");
        JMenuItem exitItem = new JMenuItem("Exit");
        
        saveItem.addActionListener(e -> saveTasks());
        loadItem.addActionListener(e -> loadTasks());
        exitItem.addActionListener(e -> exitApplication());
        
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    private void showAddTaskDialog() {
        AddTaskDialog dialog = new AddTaskDialog(this);
        dialog.setVisible(true);
        
        if (dialog.isTaskAdded()) {
            taskPanel.refreshTasks();
            updateStatus("Task added successfully!");
        }
    }
    
    private void deleteCompletedTasks() {
        TaskManager manager = TaskManager.getInstance();
        int completedCount = manager.getCompletedTasks().size();
        
        if (completedCount == 0) {
            JOptionPane.showMessageDialog(this,
                "No completed tasks to delete.",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int choice = JOptionPane.showConfirmDialog(this,
            "Delete " + completedCount + " completed task(s)?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            manager.getCompletedTasks().forEach(manager::removeTask);
            taskPanel.refreshTasks();
            updateStatus(completedCount + " completed task(s) deleted.");
        }
    }
    
    private void saveTasks() {
        try {
            FileHandler.saveTasks();
            updateStatus("Tasks saved successfully!");
            JOptionPane.showMessageDialog(this,
                "Tasks saved successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error saving tasks: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadTasks() {
        try {
            FileHandler.loadTasks();
            taskPanel.refreshTasks();
            updateStatus("Tasks loaded successfully!");
        } catch (Exception e) {
            updateStatus("No previous tasks found.");
        }
    }
    
    private void startNotificationThread() {
        notificationThread = new NotificationThread();
        notificationThread.start();
    }
    
    private void showAboutDialog() {
        String message = "Task Reminder Application\n\n" +
                        "Version: 1.0\n" +
                        "Developed using Java Swing\n\n" +
                        "Features:\n" +
                        "• Add, Edit, Delete Tasks\n" +
                        "• Set Reminders\n" +
                        "• Priority Management\n" +
                        "• Desktop Notifications\n" +
                        "• Data Persistence";
        
        JOptionPane.showMessageDialog(this,
            message,
            "About",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void exitApplication() {
        int choice = JOptionPane.showConfirmDialog(this,
            "Do you want to save tasks before exiting?",
            "Exit Application",
            JOptionPane.YES_NO_CANCEL_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            saveTasks();
            System.exit(0);
        } else if (choice == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
    }
    
    private void updateStatus(String message) {
        statusLabel.setText(message);
        Timer timer = new Timer(3000, e -> statusLabel.setText("Ready"));
        timer.setRepeats(false);
        timer.start();
    }
}
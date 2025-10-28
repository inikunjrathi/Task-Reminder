package com.taskreminder.ui;

import com.taskreminder.model.Task;
import com.taskreminder.model.TaskManager;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

/**
 * Dialog for adding new tasks
 * Demonstrates: JDialog, GridBagLayout, Input Validation
 */
public class AddTaskDialog extends JDialog {
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JSpinner dateSpinner;
    private JSpinner timeSpinner;
    private JComboBox<String> priorityCombo;
    private boolean taskAdded = false;
    
    public AddTaskDialog(JFrame parent) {
        super(parent, "Add New Task", true);
        setSize(500, 450);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Title:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        titleField = new JTextField(20);
        mainPanel.add(titleField, gbc);
        
        // Description
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(new JLabel("Description:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        mainPanel.add(scrollPane, gbc);
        
        // Date
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(new JLabel("Date:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        mainPanel.add(dateSpinner, gbc);
        
        // Time
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Time:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        SpinnerDateModel timeModel = new SpinnerDateModel();
        timeSpinner = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        mainPanel.add(timeSpinner, gbc);
        
        // Priority
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Priority:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        String[] priorities = {"HIGH", "MEDIUM", "LOW"};
        priorityCombo = new JComboBox<>(priorities);
        priorityCombo.setSelectedIndex(1);
        mainPanel.add(priorityCombo, gbc);
        
        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(20, 5, 5, 5);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        JButton saveButton = new JButton("Save Task");
        saveButton.setBackground(new Color(46, 204, 113));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Arial", Font.BOLD, 12));
        saveButton.addActionListener(e -> saveTask());
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(149, 165, 166));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 12));
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel);
    }
    
    private void saveTask() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        
        // Validation
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a title for the task.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Get date and time
            java.util.Date date = (java.util.Date) dateSpinner.getValue();
            java.util.Date time = (java.util.Date) timeSpinner.getValue();
            
            // Combine date and time
            java.util.Calendar dateCal = java.util.Calendar.getInstance();
            dateCal.setTime(date);
            
            java.util.Calendar timeCal = java.util.Calendar.getInstance();
            timeCal.setTime(time);
            
            dateCal.set(java.util.Calendar.HOUR_OF_DAY, timeCal.get(java.util.Calendar.HOUR_OF_DAY));
            dateCal.set(java.util.Calendar.MINUTE, timeCal.get(java.util.Calendar.MINUTE));
            dateCal.set(java.util.Calendar.SECOND, 0);
            
            LocalDateTime reminderTime = LocalDateTime.ofInstant(
                dateCal.toInstant(),
                java.time.ZoneId.systemDefault()
            );
            
            // Check if time is in the past
            if (reminderTime.isBefore(LocalDateTime.now())) {
                int choice = JOptionPane.showConfirmDialog(this,
                    "The reminder time is in the past. Continue?",
                    "Warning",
                    JOptionPane.YES_NO_OPTION);
                
                if (choice != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            
            String priority = (String) priorityCombo.getSelectedItem();
            
            // Create and add task
            Task task = new Task(title, description, reminderTime, priority);
            TaskManager.getInstance().addTask(task);
            
            taskAdded = true;
            dispose();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error creating task: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isTaskAdded() {
        return taskAdded;
    }
}
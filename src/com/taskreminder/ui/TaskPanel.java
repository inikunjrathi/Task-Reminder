package com.taskreminder.ui;

import com.taskreminder.model.Task;
import com.taskreminder.model.TaskManager;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

/**
 * Panel to display all tasks
 * Demonstrates: JPanel, BoxLayout, Custom Components
 */
public class TaskPanel extends JPanel {
    private JPanel tasksContainer;
    
    public TaskPanel() {
        setLayout(new BorderLayout());
        
        tasksContainer = new JPanel();
        tasksContainer.setLayout(new BoxLayout(tasksContainer, BoxLayout.Y_AXIS));
        tasksContainer.setBackground(Color.WHITE);
        
        add(tasksContainer, BorderLayout.NORTH);
        refreshTasks();
    }
    
    public void refreshTasks() {
        tasksContainer.removeAll();
        
        TaskManager manager = TaskManager.getInstance();
        java.util.List<Task> tasks = manager.getAllTasks();
        
        if (tasks.isEmpty()) {
            JLabel emptyLabel = new JLabel("No tasks yet. Click 'Add Task' to create one!");
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            tasksContainer.add(Box.createVerticalStrut(50));
            tasksContainer.add(emptyLabel);
        } else {
            for (Task task : tasks) {
                tasksContainer.add(createTaskCard(task));
                tasksContainer.add(Box.createVerticalStrut(10));
            }
        }
        
        tasksContainer.revalidate();
        tasksContainer.repaint();
    }
    
    private JPanel createTaskCard(Task task) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(task.isCompleted() ? Color.GREEN : getPriorityColor(task.getPriority()), 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        card.setBackground(Color.WHITE);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        // Left side - Task info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(task.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        if (task.isCompleted()) {
            titleLabel.setText("\u2713 " + task.getTitle());
            titleLabel.setForeground(Color.GRAY);
        }
        
        JLabel descLabel = new JLabel(task.getDescription());
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setForeground(Color.DARK_GRAY);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        JLabel timeLabel = new JLabel("\u23f0 " + task.getReminderTime().format(formatter));
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        timeLabel.setForeground(task.isDue() && !task.isCompleted() ? Color.RED : Color.BLUE);
        
        JLabel priorityLabel = new JLabel("Priority: " + task.getPriority());
        priorityLabel.setFont(new Font("Arial", Font.BOLD, 11));
        priorityLabel.setForeground(getPriorityColor(task.getPriority()));
        
        infoPanel.add(titleLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(descLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(timeLabel);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(priorityLabel);
        
        // Right side - Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton completeButton = new JButton(task.isCompleted() ? "Completed" : "Complete");
        completeButton.setEnabled(!task.isCompleted());
        completeButton.addActionListener(e -> {
            TaskManager.getInstance().markTaskCompleted(task.getId());
            refreshTasks();
        });
        
        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this,
                "Delete this task?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
            
            if (choice == JOptionPane.YES_OPTION) {
                TaskManager.getInstance().removeTask(task);
                refreshTasks();
            }
        });
        
        buttonPanel.add(completeButton);
        buttonPanel.add(Box.createVerticalStrut(5));
        buttonPanel.add(deleteButton);
        
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);
        
        return card;
    }
    
    private Color getPriorityColor(String priority) {
        switch (priority) {
            case "HIGH":
                return new Color(231, 76, 60);
            case "MEDIUM":
                return new Color(241, 196, 15);
            case "LOW":
                return new Color(46, 204, 113);
            default:
                return Color.GRAY;
        }
    }
}
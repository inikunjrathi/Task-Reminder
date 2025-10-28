package com.taskreminder.util;

import com.taskreminder.model.Task;

import javax.swing.*;
import java.awt.*;

/**
 * Notification Manager for displaying alerts
 * Demonstrates: System Tray, TrayIcon, Desktop Notifications
 */
public class NotificationManager {
    private static SystemTray tray;
    private static TrayIcon trayIcon;
    
    static {
        initializeSystemTray();
    }
    
    /**
     * Initialize System Tray
     */
    private static void initializeSystemTray() {
        if (SystemTray.isSupported()) {
            try {
                tray = SystemTray.getSystemTray();
                
                // Create tray icon image (simple colored square)
                Image image = createTrayImage();
                
                trayIcon = new TrayIcon(image, "Task Reminder");
                trayIcon.setImageAutoSize(true);
                trayIcon.setToolTip("Task Reminder Application");
                
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println("TrayIcon could not be added: " + e.getMessage());
            }
        } else {
            System.err.println("SystemTray is not supported on this platform.");
        }
    }
    
    /**
     * Create a simple image for tray icon
     */
    private static Image createTrayImage() {
        int size = 16;
        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(
            size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw a blue circle
        g.setColor(new Color(41, 128, 185));
        g.fillOval(0, 0, size, size);
        
        // Draw a white bell shape
        g.setColor(Color.WHITE);
        g.fillArc(4, 4, 8, 8, 0, 180);
        g.fillRect(7, 8, 2, 4);
        
        g.dispose();
        return image;
    }
    
    /**
     * Show notification for a task
     */
    public static void showNotification(Task task) {
        if (trayIcon != null) {
            trayIcon.displayMessage(
                "Task Reminder: " + task.getTitle(),
                task.getDescription() + "\n\nPriority: " + task.getPriority(),
                TrayIcon.MessageType.INFO
            );
            
            // Play system beep
            Toolkit.getDefaultToolkit().beep();
        } else {
            // Fallback to dialog if system tray not available
            showDialogNotification(task);
        }
    }
    
    /**
     * Show dialog notification (fallback)
     */
    private static void showDialogNotification(Task task) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                null,
                task.getDescription() + "\n\nPriority: " + task.getPriority(),
                "Task Reminder: " + task.getTitle(),
                JOptionPane.INFORMATION_MESSAGE
            );
            Toolkit.getDefaultToolkit().beep();
        });
    }
    
    /**
     * Show custom notification with action
     */
    public static void showNotificationWithAction(Task task, Runnable action) {
        if (trayIcon != null) {
            // Add action listener temporarily
            java.awt.event.ActionListener listener = e -> {
                if (action != null) {
                    action.run();
                }
            };
            
            trayIcon.addActionListener(listener);
            
            trayIcon.displayMessage(
                "Task Reminder: " + task.getTitle(),
                task.getDescription() + "\n\nClick to mark as complete.",
                TrayIcon.MessageType.INFO
            );
            
            Toolkit.getDefaultToolkit().beep();
            
            // Remove listener after 5 seconds
            Timer timer = new Timer(5000, e -> trayIcon.removeActionListener(listener));
            timer.setRepeats(false);
            timer.start();
        }
    }
    
    /**
     * Remove tray icon
     */
    public static void removeTrayIcon() {
        if (tray != null && trayIcon != null) {
            tray.remove(trayIcon);
        }
    }
}
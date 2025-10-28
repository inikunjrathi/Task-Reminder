package com.taskreminder.thread;

import com.taskreminder.model.Task;
import com.taskreminder.model.TaskManager;
import com.taskreminder.util.NotificationManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Background thread for checking and triggering notifications
 * Demonstrates: Thread, Runnable, Thread Synchronization, Thread Sleep
 */
public class NotificationThread extends Thread {
    private volatile boolean running = true;
    private Set<String> notifiedTasks;
    private static final long CHECK_INTERVAL = 30000; // Check every 30 seconds
    
    public NotificationThread() {
        super("NotificationThread");
        this.notifiedTasks = new HashSet<>();
        setDaemon(true); // Daemon thread
    }
    
    @Override
    public void run() {
        System.out.println("Notification thread started...");
        
        while (running) {
            try {
                checkAndNotify();
                Thread.sleep(CHECK_INTERVAL);
            } catch (InterruptedException e) {
                System.err.println("Notification thread interrupted: " + e.getMessage());
                break;
            } catch (Exception e) {
                System.err.println("Error in notification thread: " + e.getMessage());
            }
        }
        
        System.out.println("Notification thread stopped.");
    }
    
    /**
     * Check tasks and send notifications
     */
    private void checkAndNotify() {
        TaskManager manager = TaskManager.getInstance();
        List<Task> tasks = manager.getTasksDueForNotification();
        
        for (Task task : tasks) {
            // Check if already notified
            if (!notifiedTasks.contains(task.getId())) {
                NotificationManager.showNotification(task);
                notifiedTasks.add(task.getId());
                System.out.println("Notification sent for task: " + task.getTitle());
            }
        }
        
        // Clean up notified tasks that are completed
        cleanupNotifiedTasks();
    }
    
    /**
     * Remove completed tasks from notified set
     */
    private void cleanupNotifiedTasks() {
        TaskManager manager = TaskManager.getInstance();
        notifiedTasks.removeIf(taskId -> {
            Task task = manager.getTaskById(taskId);
            return task == null || task.isCompleted();
        });
    }
    
    /**
     * Stop the notification thread
     */
    public void stopNotifications() {
        running = false;
        this.interrupt();
    }
    
    /**
     * Reset notified tasks (useful after loading new tasks)
     */
    public void resetNotifications() {
        notifiedTasks.clear();
    }
}
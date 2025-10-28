package com.taskreminder.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Task Manager - Manages all tasks
 * Demonstrates: Collections (ArrayList), List interface, Stream API
 */
public class TaskManager {
    private List<Task> tasks;
    private static TaskManager instance;
    
    // Singleton pattern
    private TaskManager() {
        tasks = new ArrayList<>();
    }
    
    public static synchronized TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }
    
    // Add task
    public void addTask(Task task) {
        tasks.add(task);
    }
    
    // Remove task
    public void removeTask(Task task) {
        tasks.remove(task);
    }
    
    // Get all tasks
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }
    
    // Get pending tasks
    public List<Task> getPendingTasks() {
        return tasks.stream()
                   .filter(task -> !task.isCompleted())
                   .collect(Collectors.toList());
    }
    
    // Get completed tasks
    public List<Task> getCompletedTasks() {
        return tasks.stream()
                   .filter(Task::isCompleted)
                   .collect(Collectors.toList());
    }
    
    // Get tasks due for notification
    public List<Task> getTasksDueForNotification() {
        return tasks.stream()
                   .filter(Task::shouldNotify)
                   .collect(Collectors.toList());
    }
    
    // Mark task as completed
    public void markTaskCompleted(String taskId) {
        tasks.stream()
             .filter(task -> task.getId().equals(taskId))
             .findFirst()
             .ifPresent(task -> task.setCompleted(true));
    }
    
    // Get task by ID
    public Task getTaskById(String id) {
        return tasks.stream()
                   .filter(task -> task.getId().equals(id))
                   .findFirst()
                   .orElse(null);
    }
    
    // Clear all tasks
    public void clearAllTasks() {
        tasks.clear();
    }
    
    // Set tasks (for loading from file)
    public void setTasks(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }
    
    // Get task count
    public int getTaskCount() {
        return tasks.size();
    }
}
package com.taskreminder.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Task Model Class
 * Demonstrates: Class, Encapsulation, POJO for JSON, toString, equals, hashCode
 */
public class Task {
    
    private String id;
    private String title;
    private String description;
    private LocalDateTime reminderTime;
    private boolean completed;
    private String priority; // HIGH, MEDIUM, LOW
    
    // Constructor
    public Task(String title, String description, LocalDateTime reminderTime, String priority) {
        this.id = generateId();
        this.title = title;
        this.description = description;
        this.reminderTime = reminderTime;
        this.priority = priority;
        this.completed = false;
    }
    
    // Generate unique ID
    private String generateId() {
        return "TASK_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }
    
    // Getters and Setters (Encapsulation)
    public String getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getReminderTime() {
        return reminderTime;
    }
    
    public void setReminderTime(LocalDateTime reminderTime) {
        this.reminderTime = reminderTime;
    }
    
    public boolean isCompleted() {
        return completed;
    }
    
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    
    public String getPriority() {
        return priority;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    // Check if task is due
    public boolean isDue() {
        return !completed && LocalDateTime.now().isAfter(reminderTime);
    }
    
    // Check if task reminder should trigger (within 1 minute)
    public boolean shouldNotify() {
        if (completed) return false;
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(reminderTime) && now.isBefore(reminderTime.plusMinutes(1));
    }
    
    // toString method
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("[%s] %s - %s (Due: %s)", 
            priority, title, completed ? "✓" : "⏰", reminderTime.format(formatter));
    }
    
    // equals method
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return id.equals(task.id);
    }
    
    // hashCode method
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
package com.taskreminder.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.taskreminder.model.Task;
import com.taskreminder.model.TaskManager;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * File Handler for JSON persistence
 * Demonstrates: File I/O, JSON serialization with Gson, Exception Handling
 */
public class FileHandler {
    private static final String DATA_DIR = "data";
    private static final String TASKS_FILE = DATA_DIR + File.separator + "tasks.json";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static Gson gson;
    
    static {
        // Initialize Gson with custom serializers for LocalDateTime
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, 
                    (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) -> 
                        context.serialize(src.format(DATE_FORMATTER)))
                .registerTypeAdapter(LocalDateTime.class,
                    (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) ->
                        LocalDateTime.parse(json.getAsString(), DATE_FORMATTER))
                .setPrettyPrinting()
                .create();
        
        // Create data directory if it doesn't exist
        createDataDirectory();
    }
    
    /**
     * Create data directory
     */
    private static void createDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    /**
     * Save tasks to JSON file
     */
    public static void saveTasks() throws IOException {
        TaskManager manager = TaskManager.getInstance();
        List<Task> tasks = manager.getAllTasks();
        
        String json = gson.toJson(tasks);
        
        try (FileWriter writer = new FileWriter(TASKS_FILE)) {
            writer.write(json);
        }
    }
    
    /**
     * Load tasks from JSON file
     */
    public static void loadTasks() throws IOException {
        File file = new File(TASKS_FILE);
        
        if (!file.exists()) {
            throw new FileNotFoundException("Tasks file not found");
        }
        
        String json = new String(Files.readAllBytes(Paths.get(TASKS_FILE)));
        
        Type taskListType = new TypeToken<ArrayList<Task>>(){}.getType();
        List<Task> tasks = gson.fromJson(json, taskListType);
        
        if (tasks != null) {
            TaskManager.getInstance().setTasks(tasks);
        }
    }
    
    /**
     * Export tasks to a specific file
     */
    public static void exportTasks(String filePath) throws IOException {
        TaskManager manager = TaskManager.getInstance();
        List<Task> tasks = manager.getAllTasks();
        
        String json = gson.toJson(tasks);
        
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(json);
        }
    }
    
    /**
     * Import tasks from a specific file
     */
    public static void importTasks(String filePath) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(filePath)));
        
        Type taskListType = new TypeToken<ArrayList<Task>>(){}.getType();
        List<Task> tasks = gson.fromJson(json, taskListType);
        
        if (tasks != null) {
            TaskManager manager = TaskManager.getInstance();
            for (Task task : tasks) {
                manager.addTask(task);
            }
        }
    }
    
    /**
     * Delete tasks file
     */
    public static void deleteTasksFile() {
        File file = new File(TASKS_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
    
    /**
     * Check if tasks file exists
     */
    public static boolean tasksFileExists() {
        return new File(TASKS_FILE).exists();
    }
}
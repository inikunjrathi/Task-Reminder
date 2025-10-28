# 📋 Task Reminder Desktop Application

A feature-rich desktop application built with Java Swing for managing tasks with intelligent reminder notifications. The application demonstrates core Java programming concepts including OOP, multithreading, file I/O, and GUI development.

![Java](https://img.shields.io/badge/Java-8%2B-orange)
![Swing](https://img.shields.io/badge/GUI-Swing-blue)
![JSON](https://img.shields.io/badge/Storage-JSON-green)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

## 📸 Features

### Core Functionality
- ✅ **Add Tasks** - Create tasks with title, description, date/time, and priority
- ✅ **View Tasks** - Scrollable panel displaying all tasks with color-coded priorities
- ✅ **Complete Tasks** - Mark tasks as completed with visual feedback
- ✅ **Delete Tasks** - Remove individual or all completed tasks
- ✅ **Priority Management** - Three levels: HIGH (Red), MEDIUM (Yellow), LOW (Green)

### Notification System
- 🔔 **Desktop Notifications** - System tray popup alerts
- ⏰ **Background Monitoring** - Checks for due tasks every 30 seconds
- 🔊 **Audio Alerts** - System beep on notification
- 📢 **Visual Alerts** - Overdue tasks highlighted in red

### Data Management
- 💾 **JSON Storage** - Tasks saved in human-readable JSON format
- 📁 **Auto-Save** - Prompt to save on application exit
- 📂 **Manual Save/Load** - Menu options for data persistence
- 🔄 **Import/Export** - Backup and restore functionality

## 🏗️ Project Structure

```
TaskReminderApp/
│
├── src/
│   └── com/
│       └── taskreminder/
│           ├── Main.java                      # Application entry point
│           │
│           ├── ui/                            # User Interface
│           │   ├── MainFrame.java            # Main window
│           │   ├── TaskPanel.java            # Task display panel
│           │   └── AddTaskDialog.java        # Add task dialog
│           │
│           ├── model/                         # Data Models
│           │   ├── Task.java                 # Task entity
│           │   └── TaskManager.java          # Task management (Singleton)
│           │
│           ├── util/                          # Utilities
│           │   ├── NotificationManager.java  # System notifications
│           │   └── FileHandler.java          # JSON file operations
│           │
│           └── thread/                        # Threading
│               └── NotificationThread.java   # Background task checker
│
├── data/
│   └── tasks.json                            # Task storage (auto-generated)
│
├── lib/
│   └── gson-2.13.1.jar                       # Gson library
│
├── build/                                     # Compiled classes
│
└── README.md                                  # This file
```

## 🚀 Getting Started

### Prerequisites

- **Java Development Kit (JDK)** 8 or higher
- **Gson Library** 2.13.1
- Any Java IDE (IntelliJ IDEA, Eclipse, NetBeans) or command-line tools

### Installation

#### Step 1: Clone or Download the Project

```bash
git clone <repository-url>
cd TaskReminderApp
```

#### Step 2: Download Gson Library

Download Gson JAR from Maven Central:
```
https://repo1.maven.org/maven2/com/google/code/gson/gson/2.13.1/gson-2.13.1.jar
```

Place it in the `lib/` directory.

#### Step 3: Create Required Directories

```bash
mkdir -p data lib build
```

### 🔨 Compilation & Execution

#### Using Command Line (Windows)

```bash
# Compile
javac -d build -cp "lib/*;src" src/com/taskreminder/*.java src/com/taskreminder/*/*.java

# Run
java -cp "build;lib/*" com.taskreminder.Main
```

#### Using Command Line (Linux/Mac)

```bash
# Compile
javac -d build -cp "lib/*:src" src/com/taskreminder/*.java src/com/taskreminder/*/*.java

# Run
java -cp "build:lib/*" com.taskreminder.Main
```

#### Using IntelliJ IDEA

1. Open IntelliJ IDEA → **File** → **New** → **Project from Existing Sources**
2. Select the `TaskReminderApp` folder
3. Right-click on `lib/gson-2.13.1.jar` → **Add as Library**
4. Right-click on `Main.java` → **Run 'Main.main()'**

#### Using Eclipse

1. **File** → **Import** → **Existing Projects into Workspace**
2. Select the project root directory
3. Right-click project → **Build Path** → **Configure Build Path**
4. **Libraries** → **Add External JARs** → Select `gson-2.13.1.jar`
5. Right-click `Main.java` → **Run As** → **Java Application**

#### Using NetBeans

1. **File** → **Open Project** → Select project folder
2. Right-click **Libraries** → **Add JAR/Folder** → Select `gson-2.13.1.jar`
3. Right-click project → **Run**

### 📦 Creating Executable JAR

```bash
# Create manifest file
echo "Main-Class: com.taskreminder.Main" > manifest.txt
echo "Class-Path: lib/gson-2.13.1.jar" >> manifest.txt

# Create JAR
jar cvfm TaskReminder.jar manifest.txt -C build .

# Copy library
mkdir -p dist/lib
cp lib/gson-2.13.1.jar dist/lib/
cp TaskReminder.jar dist/

# Run
cd dist
java -jar TaskReminder.jar
```

## 💻 Usage Guide

### Adding a Task

1. Click **"➕ Add Task"** button
2. Fill in the form:
   - **Title**: Brief task name
   - **Description**: Detailed description
   - **Date**: Select reminder date
   - **Time**: Select reminder time
   - **Priority**: Choose HIGH, MEDIUM, or LOW
3. Click **"Save Task"**

### Managing Tasks

- **Complete Task**: Click "Complete" button on any task card
- **Delete Task**: Click "Delete" button on any task card
- **Delete All Completed**: Click "🗑️ Delete Completed" button
- **Refresh View**: Click "🔄 Refresh" button

### Saving & Loading

- **Auto-Save**: Prompted on exit
- **Manual Save**: Menu → **File** → **Save Tasks**
- **Load Tasks**: Menu → **File** → **Load Tasks**
- **Data Location**: `data/tasks.json`

### Notifications

- Background thread checks every 30 seconds
- Notifications appear when task time is reached
- System tray icon shows alerts
- Audio beep accompanies notifications

## 🗂️ JSON Data Format

Tasks are stored in `data/tasks.json`:

```json
[
  {
    "id": "TASK_1730106000000_456",
    "title": "Team Meeting",
    "description": "Discuss Q4 project goals and milestones",
    "reminderTime": "2025-10-28T14:30:00",
    "completed": false,
    "priority": "HIGH"
  },
  {
    "id": "TASK_1730106100000_789",
    "title": "Code Review",
    "description": "Review pull requests from team members",
    "reminderTime": "2025-10-29T10:00:00",
    "completed": false,
    "priority": "MEDIUM"
  }
]
```

## 📚 Java Concepts Demonstrated

### Object-Oriented Programming
- **Encapsulation**: Private fields with getters/setters
- **Inheritance**: Extending JFrame, JDialog, Thread
- **Polymorphism**: Method overriding (toString, equals, hashCode)
- **Abstraction**: Interface implementations

### Collections Framework
- **ArrayList**: Dynamic task storage
- **HashSet**: Tracking notified tasks
- **Stream API**: Filtering and processing tasks
- **Lambda Expressions**: Functional programming

### Multithreading
- **Thread Class**: NotificationThread extends Thread
- **Daemon Threads**: Background task monitoring
- **Thread Synchronization**: volatile keyword
- **Thread Safety**: Concurrent access handling

### File I/O & JSON
- **FileWriter/FileReader**: File operations
- **Gson Library**: JSON serialization/deserialization
- **Custom Type Adapters**: LocalDateTime handling
- **Exception Handling**: try-catch-finally blocks

### GUI Programming (Swing & AWT)
- **JFrame**: Main application window
- **JPanel**: Container components
- **JDialog**: Modal dialogs
- **Layout Managers**: BorderLayout, GridBagLayout, BoxLayout, FlowLayout
- **Event Handling**: ActionListener, MouseListener, WindowListener
- **SystemTray**: Desktop notifications
- **Graphics2D**: Custom icon drawing

### Design Patterns
- **Singleton**: TaskManager instance management
- **Observer**: GUI update mechanism
- **MVC**: Model-View-Controller separation
- **Factory**: Task creation

## 🎨 UI Components

### Main Window
- Title bar with application name
- Menu bar (File, Help)
- Scrollable task display panel
- Action buttons (Add, Refresh, Delete)
- Status bar with notifications

### Task Card
- Color-coded border (priority-based)
- Task title and description
- Reminder date/time
- Priority label
- Action buttons (Complete, Delete)

### Add Task Dialog
- Input fields with validation
- Date/Time spinners
- Priority dropdown
- Save/Cancel buttons

## 🔧 Configuration

### Notification Interval
Edit `NotificationThread.java`:
```java
private static final long CHECK_INTERVAL = 30000; // milliseconds
```

### Data File Location
Edit `FileHandler.java`:
```java
private static final String DATA_DIR = "data";
private static final String TASKS_FILE = DATA_DIR + File.separator + "tasks.json";
```

## ❗ Troubleshooting

### ClassNotFoundException: com.google.gson.Gson
**Solution**: Ensure `gson-2.10.1.jar` is in the `lib/` folder and added to classpath.

### Notifications Not Showing
**Solution**: 
- Check if SystemTray is supported: `SystemTray.isSupported()`
- On Linux, ensure notification daemon is running
- Fallback dialog notifications will appear automatically

### Tasks Not Persisting
**Solution**: 
- Check file permissions for `data/` directory
- Verify `tasks.json` is not corrupted
- Use Menu → File → Save Tasks manually

### JSON Parse Error
**Solution**: 
- Delete `data/tasks.json` and restart application
- Check JSON format validity
- Restore from backup if available

### High Memory Usage
**Solution**: 
- Clear completed tasks regularly
- Limit number of active tasks
- Check for memory leaks in custom code

## 🔐 System Requirements

- **OS**: Windows 7/8/10/11, Linux, macOS 10.12+
- **Java**: JDK 8 or higher (JDK 11+ recommended)
- **RAM**: Minimum 512 MB
- **Disk**: 50 MB free space
- **Display**: 1024x768 or higher resolution

## 📦 Dependencies

| Library | Version | Purpose |
|---------|---------|---------|
| Gson | 2.13.1 | JSON serialization/deserialization |
| Java Swing | Built-in | GUI framework |
| Java AWT | Built-in | System tray notifications |
| Java Time API | Built-in | Date/time handling |

## 🤝 Contributing

This is an educational project. Feel free to:
- Fork the repository
- Add new features
- Fix bugs
- Improve documentation
- Submit pull requests

## 📝 License

This project is created for educational purposes demonstrating Java programming concepts based on standard programming textbooks.

## 👨‍💻 Author

Created as a demonstration of Java programming concepts including:
- Object-Oriented Programming
- GUI Development with Swing
- Multithreading
- File I/O and JSON handling
- Event-driven programming

## 📖 Learning Resources

### Books Referenced
- "Programming in Java" by Sachin Malhotra
- Core Java concepts and best practices

### Topics Covered
1. Classes and Objects
2. Inheritance and Polymorphism
3. Exception Handling
4. Collections Framework
5. File I/O
6. Multithreading
7. GUI Programming with Swing
8. Event Handling
9. Java 8+ Features (Lambda, Stream API)
10. Design Patterns

## 🎯 Future Enhancements

- [ ] Database integration (SQLite/MySQL)
- [ ] User authentication and multi-user support
- [ ] Task categories and tags
- [ ] Search and filter functionality
- [ ] Task recurrence (daily, weekly, monthly)
- [ ] Export to PDF/Excel
- [ ] Dark mode theme
- [ ] Mobile companion app
- [ ] Cloud synchronization
- [ ] Internationalization (i18n)

## 📞 Support

For issues, questions, or suggestions:
- Check the Troubleshooting section
- Review Java documentation
- Consult Swing/AWT tutorials
- Search Stack Overflow

## ⭐ Acknowledgments

- Gson library by Google
- Java Swing framework by Oracle
- Java community for resources and tutorials

---

**Made with ☕ and Java**

*Last Updated: October 2025*

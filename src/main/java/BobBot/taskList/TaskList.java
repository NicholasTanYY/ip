package BobBot.taskList;

import java.util.ArrayList;

import BobBot.parser.Parser;
import BobBot.tasks.Task;
import BobBot.ui.Ui;

/**
 * Implements a task list that stores all tasks entered by the user.
 * 
 * @author NicholasTanYY
 * @since January 2024
 * @version 1.0
 */
public class TaskList {
    private static ArrayList<Task> allTasks = new ArrayList<>();
    private static int numberOfTasks = 0;

    public static int getNumberOfTasks() {
        return numberOfTasks;
    }

    public static ArrayList<Task> getTaskList() {
        return allTasks;
    }

    /**
     * Adds a task to the list.
     * 
     * @param line The command entered by the user.
     * @param isLoad A boolean to indicate if the task is loaded from the save file.
     */
    public static void addTask(String line, boolean isLoad) {

        Task newTask = null;
        newTask = Parser.parseTaskCommands(line, newTask);
        
        if (newTask == null) {
            return;
        }

        allTasks.add(newTask);
        numberOfTasks += 1;

        if (!isLoad) {
            Ui.echoCommand(line, newTask);
        }
    }

    public enum TaskStatus {
        MARK, UNMARK, DELETE
    }

    /**
     * Performs the task operation based on the command entered by the user.
     * 
     * @param line The command entered by the user.
     * @param status The type of task operation to perform.
     */
    public static void performTaskOperation(String line, TaskStatus status) {
        int taskNumber = Integer.parseInt(line.replaceAll("\\D", "").trim()) - 1;
        
        try {
            Task task = allTasks.get(taskNumber);   
            if (status == TaskStatus.MARK) {
                task.markAsDone();
                Ui.printTaskOperationMessage(task, "Marking this task as done:");
            } else if (status == TaskStatus.UNMARK) {
                task.markAsUndone();
                Ui.printTaskOperationMessage(task, "Unmarking this task:");
            } else if (status == TaskStatus.DELETE) {
                allTasks.remove(taskNumber);
                Ui.printTaskOperationMessage(task, "Deleting this task:");
                numberOfTasks -= 1;
            } else {
                System.out.println("Oh no!");
            }
        } catch (IndexOutOfBoundsException e) {
            Ui.printNonExistentTaskErrorMessage(taskNumber);
        }
    }

    /**
     * Finds tasks from the list based on the keyword entered by the user.
     * 
     * @param line The command entered by the user.
     * @return The list of tasks found based on the keyword.
     */
    public static ArrayList<Task> findTasksFromKeyword(String line) {
        ArrayList<Task> tasksOfInterest = new ArrayList<>();
        String keywordString = line.substring("find".length()).trim();

        for (int taskIndex = 0; taskIndex < numberOfTasks; taskIndex += 1) {
            Task task = allTasks.get(taskIndex);
            String taskDescription = task.getDescription();

            if (taskDescription.contains(keywordString)) {
                tasksOfInterest.add(task);
            }
        }

        return tasksOfInterest;
    }

}

package ui;

import Dao.ActivityLogDao;
import model.Activitylog;
import java.util.List;
import java.util.Scanner;

public class AdminMenu {

    private final ActivityLogDao logDao = new ActivityLogDao();
    private final Scanner scanner;

    public AdminMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void displayAuditLogMenu() {
        while (true) {
            System.out.println("\n=== SYSTEM AUDIT LOGS ===");
            System.out.println("1. View All System Logs");
            System.out.println("2. Filter Logs by User ID");
            System.out.println("3. Clear Historical Logs");
            System.out.println("4. Back to Main Menu");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            switch (choice) {
                case 1 -> printLogs(logDao.getAllLogs());
                case 2 -> {
                    System.out.print("Enter User ID to filter: ");
                    int userId = scanner.nextInt();
                    scanner.nextLine();
                    printLogs(logDao.getLogsByUserId(userId));
                }
                case 3 -> {
                    System.out.print("Are you sure you want to clear all historical logs? (y/n): ");
                    String confirm = scanner.nextLine();
                    if (confirm.equalsIgnoreCase("y")) {
                        logDao.clearAllLogs();
                        System.out.println("Audit logs cleared successfully.");
                    }
                }
                case 4 -> { return; }
                default -> System.out.println("Invalid selection. Try again.");
            }
        }
    }

    private void printLogs(List<Activitylog> logs) {
        if (logs.isEmpty()) {
            System.out.println("No activity logs found.");
            return;
        }

        System.out.println("-".repeat(95));
        System.out.printf("%-5s | %-8s | %-18s | %-35s | %-20s%n", "ID", "User ID", "Action Type", "Description", "Timestamp");
        System.out.println("-".repeat(95));

        for (Activitylog log : logs) {
            System.out.printf("%-5d | %-8d | %-18s | %-35s | %-20s%n",
                    log.logId(),
                    log.userId(),
                    log.actionType(),
                    log.description(),
                    log.timestamp().toString()
            );
        }
        System.out.println("-".repeat(95));
    }
}
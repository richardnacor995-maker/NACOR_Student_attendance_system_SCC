package ui;

import Dao.UserDao;
import model.User;
import java.util.Scanner;

public class Authentication {
    private final UserDao userDao = new UserDao();
    private final Scanner scanner;

    public Authentication(Scanner scanner) {
        this.scanner = scanner;
    }

    public void start() {
        while (true) {
            System.out.println("\n=== STUDENT ATTENDANCE SYSTEM OF SCC ===");
            System.out.println("1. Login");
            System.out.println("2. Register Account");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> handleLogin();
                case 2 -> handleRegistration();
                case 3 -> {
                    System.out.println("Exiting system. Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void handleLogin() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = userDao.login(username, password);

        if (user == null) {
            System.out.println("Login failed: Invalid username or password.");
            return;
        }

        if ("PENDING".equalsIgnoreCase(user.getStatus())) {
            System.out.println("Login failed: Your account is pending administrator approval.");
            return;
        }

        System.out.println("Login successful! Welcome, " + user.getUsername() + " (" + user.getRole() + ")");
        routeUserByRole(user);
    }

    private void handleRegistration() {
        System.out.print("Enter new username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.println("Select Role:");
        System.out.println("1. ADMIN");
        System.out.println("2. INSTRUCTOR");
        System.out.println("3. STUDENT");
        System.out.print("Choose role option: ");
        int roleChoice = scanner.nextInt();
        scanner.nextLine();

        String role = switch (roleChoice) {
            case 1 -> "ADMIN";
            case 2 -> "INSTRUCTOR";
            case 3 -> "STUDENT";
            default -> "STUDENT";
        };

        boolean success = userDao.register(username, password, role);
        if (success) {
            System.out.println("Registration successful! Your account status is PENDING administrator approval.");
        } else {
            System.out.println("Registration failed: Username may already exist.");
        }
    }

    private void routeUserByRole(User user) {
        String role = user.getRole().toUpperCase();
        switch (role) {
            case "ADMIN" -> {
                AdminMenu adminMenu = new AdminMenu(scanner);
                adminMenu.displayAuditLogMenu();
            }
            case "INSTRUCTOR" -> System.out.println("\n[Instructor Dashboard]");
            case "STUDENT" -> System.out.println("\n[Student Dashboard]");
            default -> System.out.println("Unrecognized role permissions.");
        }
    }
}
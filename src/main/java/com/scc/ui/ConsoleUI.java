package com.scc.ui;

import com.scc.dao.UserDao;
import com.scc.model.User;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final Scanner scanner = new Scanner(System.in);
    private final UserDao userDao = new UserDao();

    public void start() {
        boolean running = true;

        System.out.println("==================================================");
        System.out.println("  STUDENT ATTENDANCE SYSTEM OF SCC (CLI Version)  ");
        System.out.println("==================================================");

        while (running) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("[1] Register New User");
            System.out.println("[2] View All Users");
            System.out.println("[3] Admin: Approve User Account");
            System.out.println("[4] User Login");
            System.out.println("[5] Exit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> handleRegistration();
                case "2" -> handleViewUsers();
                case "3" -> handleAdminApproval();
                case "4" -> handleLogin();
                case "5" -> {
                    running = false;
                    System.out.println("Exiting system. Goodbye!");
                }
                default -> System.out.println("[INVALID] Please enter a valid option (1-5).");
            }
        }
        scanner.close();
    }

    private void handleRegistration() {
        System.out.println("\n--- User Registration ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastname = scanner.nextLine();
        System.out.print("Enter course & section (e.g., IT2D): ");
        String courseSection = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        User newUser = new User(0, username, password, lastname, courseSection, email, "Pending");
        if (userDao.insertUser(newUser)) {
            System.out.println("[SUCCESS] Registration successful!");
            System.out.println("[NOTICE] Status: Pending approval by admin.");
        } else {
            System.out.println("[ERROR] Failed to register user.");
        }
    }

    private void handleViewUsers() {
        System.out.println("\n--- Registered Users List ---");
        List<User> users = userDao.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No records found.");
        } else {
            for (User u : users) {
                System.out.println("ID: " + u.id() + " | Name: " + u.lastname() + ", " + u.username() + " | Section: " + u.courseSection() + " | Status: " + u.status());
            }
        }
    }

    private void handleAdminApproval() {
        System.out.println("\n--- ADMIN: Approve User Accounts ---");
        List<User> allUsers = userDao.getAllUsers();
        if (allUsers.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }

        for (User u : allUsers) {
            System.out.println("Username: " + u.username() + " | Status: " + u.status());
        }

        System.out.print("\nEnter username to approve (change to Active): ");
        String targetUser = scanner.nextLine();

        if (userDao.updateUserStatus(targetUser, "Active")) {
            System.out.println("[SUCCESS] User " + targetUser + " is now Active and can log in!");
        } else {
            System.out.println("[ERROR] User not found or update failed.");
        }
    }

    private void handleLogin() {
        System.out.println("\n--- User Login ---");
        System.out.print("Enter username: ");
        String loginUser = scanner.nextLine();
        System.out.print("Enter password: ");
        String loginPass = scanner.nextLine();

        if (userDao.canLogin(loginUser, loginPass)) {
            System.out.println("[SUCCESS] Login successful! Welcome, " + loginUser + ".");
        }
    }
}
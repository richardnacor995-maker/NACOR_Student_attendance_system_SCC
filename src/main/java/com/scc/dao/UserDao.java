package com.scc.dao;

import com.scc.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    public boolean insertUser(User user) {
        String sql = "INSERT INTO users (username, password, lastname, course_section, email, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.username());
            pstmt.setString(2, user.password());
            pstmt.setString(3, user.lastname());
            pstmt.setString(4, user.courseSection());
            pstmt.setString(5, user.email());
            pstmt.setString(6, user.status());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
            return false;
        }
    }

    public List<User> getAllUsers() {
        String sql = "SELECT id, username, password, lastname, course_section, email, status FROM users";
        List<User> users = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("lastname"),
                        rs.getString("course_section"),
                        rs.getString("email"),
                        rs.getString("status")
                );
                users.add(user);
            }

        } catch (SQLException e) {
            System.out.println("Fetch failed: " + e.getMessage());
        }

        return users;
    }

    // Updates a user's status (e.g., Pending -> Active)
    public boolean updateUserStatus(String username, String newStatus) {
        String sql = "UPDATE users SET status = ? WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newStatus);
            pstmt.setString(2, username);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Status update failed: " + e.getMessage());
            return false;
        }
    }

    // Validates credentials and checks if the account status is Active
    public boolean canLogin(String username, String password) {
        String sql = "SELECT status FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String status = rs.getString("status");
                if ("Active".equalsIgnoreCase(status)) {
                    return true;
                } else {
                    System.out.println("[ACCESS DENIED] Your account is still pending admin approval.");
                    return false;
                }
            } else {
                System.out.println("[ERROR] Invalid username or password.");
            }
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return false;
    }
}
package Dao;

import model.User;
import java.sql.*;

public class UserDao {
    // Directly instantiate ActivityLogDao here to avoid creating separate helper classes
    private final ActivityLogDao logDao = new ActivityLogDao();

    public User login(String username, String password) {
        String sql = "SELECT user_id, username, password, role, status FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role"),
                            rs.getString("status")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean register(String username, String password, String role) {
        String sql = "INSERT INTO users (username, password, role, status) VALUES (?, ?, ?, 'PENDING')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    // Inserted Account Approval with Auto-Logging
    public void approveUser(int adminId, int targetUserId) {
        String sql = "UPDATE users SET status = 'APPROVED' WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, targetUserId);
            stmt.executeUpdate();

            // Automatically logs the approval action into activity_logs table
            logDao.logAction(adminId, "USER_APPROVED", "Admin [" + adminId + "] approved account for User ID " + targetUserId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Inserted Role/Status Update with Auto-Logging
    public void updateUserRoleAndStatus(int adminId, int targetUserId, String newRole, String newStatus) {
        String sql = "UPDATE users SET role = ?, status = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newRole.toUpperCase());
            stmt.setString(2, newStatus.toUpperCase());
            stmt.setInt(3, targetUserId);
            stmt.executeUpdate();

            // Automatically logs the role update action into activity_logs table
            logDao.logAction(adminId, "USER_UPDATED", "Admin [" + adminId + "] changed role of User " + targetUserId + " to " + newRole.toUpperCase());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
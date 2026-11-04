# SCC Student Attendance System

## Requirements
- Java 17 or newer
- Maven
- IntelliJ IDEA (recommended)

## Run
Open this `SCCStudentAttendance` folder as a Maven project, reload Maven, then run:

```bash
mvn clean compile
mvn exec:java
```

The SQLite database used by this Maven project is `attendance.db` in this folder.

## Demonstration administrator
- Username: `admin`
- Password: `admin123`

The database stores SHA-256 password hashes. The default administrator is inserted only if it does not already exist.

## Administrative user update
From the Admin Menu, choose `4. Update User Role and Status`. The feature validates numeric IDs, validates roles and statuses, accepts `0` to cancel each prompt, and blocks editing the currently logged-in administrator account.

The supplied database schema uses `users.user_id` and `users.role` (singular), with role values `ADMINISTRATOR`, `INSTRUCTOR`, and `STUDENT`. The console accepts ADMIN/TEACHER as friendly aliases and maps them to the database's allowed values.

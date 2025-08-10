
import java.sql.*;
import java.util.Scanner;

public class StudentManagementSystem {
    private static final String URL = "jdbc:mysql://localhost:3306/student_db";
    private static final String USER = "root"; // Change as per your DB
    private static final String PASSWORD = ""; // Add your MySQL password

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try{
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println("MySQL JDBC Driver not found.");
                e.printStackTrace();
                return;
            }

            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            while (true) {
                System.out.println("\n1. Add Student\n2. View Students\n3. Update Student\n4. Delete Student\n5. Exit");
                System.out.print("Enter choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();  // consume newline

                switch (choice) {
                    case 1 -> addStudent(conn, scanner);
                    case 2 -> viewStudents(conn);
                    case 3 -> updateStudent(conn, scanner);
                    case 4 -> deleteStudent(conn, scanner);
                    case 5 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid choice");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addStudent(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter course: ");
        String course = scanner.nextLine();

        String sql = "INSERT INTO students(name, email, course) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, course);
            stmt.executeUpdate();
            System.out.println("Student added successfully.");
        }
    }

    private static void viewStudents(Connection conn) throws SQLException {
        String sql = "SELECT * FROM students";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("ID | Name | Email | Course");
            while (rs.next()) {
                System.out.printf("%d | %s | %s | %s%n",
                        rs.getInt("id"), rs.getString("name"),
                        rs.getString("email"), rs.getString("course"));
            }
        }
    }

    private static void updateStudent(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter student ID to update: ");
        int id = scanner.nextInt(); scanner.nextLine();
        System.out.print("Enter new name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new email: ");
        String email = scanner.nextLine();
        System.out.print("Enter new course: ");
        String course = scanner.nextLine();

        String sql = "UPDATE students SET name=?, email=?, course=? WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, course);
            stmt.setInt(4, id);
            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("Student updated.");
            else System.out.println("Student not found.");
        }
    }

    private static void deleteStudent(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter student ID to delete: ");
        int id = scanner.nextInt(); scanner.nextLine();

        String sql = "DELETE FROM students WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("Student deleted.");
            else System.out.println("Student not found.");
        }
    }
}

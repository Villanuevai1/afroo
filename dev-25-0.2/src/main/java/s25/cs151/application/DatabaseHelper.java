package s25.cs151.application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    private static final String DB_PATH = "data/identifier.sqlite";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;

    public static void initializeDatabase() {
        new java.io.File("data").mkdirs();
        System.out.println("🛠 Using DB at: " + DB_PATH);

        String createOfficeHoursTableSQL = "CREATE TABLE IF NOT EXISTS office_hours ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "semester TEXT NOT NULL, "
                + "year INTEGER NOT NULL, "
                + "day TEXT NOT NULL, "
                + "time TEXT NOT NULL,"
                + "UNIQUE (semester, year)"
                + ");";

        String createCoursesTableSQL = "CREATE TABLE IF NOT EXISTS courses ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "code TEXT NOT NULL, "
                + "name TEXT NOT NULL, "
                + "section TEXT NOT NULL, "
                + "UNIQUE (code, name, section)"
                + ");";

        String createTimeSlotsTableSQL = "CREATE TABLE IF NOT EXISTS time_slots ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "from_hour TEXT NOT NULL, "
                + "to_hour TEXT NOT NULL"
                + ");";

        String createOfficeHourEntriesTableSQL = "CREATE TABLE IF NOT EXISTS office_hour_entries ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "student_name TEXT NOT NULL, "
                + "date TEXT NOT NULL, "
                + "time_slot TEXT NOT NULL, "
                + "course TEXT NOT NULL, "
                + "reason TEXT, "
                + "comment TEXT"
                + ");";

        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement()) {

                stmt.execute(createOfficeHoursTableSQL);
                stmt.execute(createCoursesTableSQL);
                stmt.execute(createTimeSlotsTableSQL);
                stmt.execute(createOfficeHourEntriesTableSQL);

                System.out.println(" Database initialized: " + DB_PATH);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // New method to insert Office Hour Schedule entry
    public void insertOfficeHourEntry(String studentName, String date, String timeSlot, String course, String reason, String comment) {
        String insertSQL = "INSERT INTO office_hour_entries (student_name, date, time_slot, course, reason, comment) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(insertSQL)) {

            stmt.setString(1, studentName);
            stmt.setString(2, date);
            stmt.setString(3, timeSlot);
            stmt.setString(4, course);
            stmt.setString(5, reason);
            stmt.setString(6, comment);
            stmt.executeUpdate();

            System.out.println(" Office hour entry inserted for: " + studentName);
        } catch (SQLException e) {
            System.err.println(" Insert office hour entry failed: " + e.getMessage());
        }
    }

    public void insertOfficeHour(String semester, int year, String day, String time) {
        System.out.println(" insertOfficeHour() called with: " + semester + ", " + year + ", " + day + ", " + time);
        System.out.println(" DB URL used: " + DB_URL);

        String query = "INSERT INTO office_hours (semester, year, day, time) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, semester);
            stmt.setInt(2, year);
            stmt.setString(3, day);
            stmt.setString(4, time);
            stmt.executeUpdate();

            System.out.println(" Office hour inserted: " + semester + " " + year);
        } catch (SQLException e) {
            System.err.println("Insert office hour failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean officeHourExists(String semester, int year) {
        String query = "SELECT COUNT(*) FROM office_hours WHERE semester = ? AND year = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, semester);
            stmt.setInt(2, year);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Office hour duplicate check failed: " + e.getMessage());
        }

        return false;
    }

    public void debugPrintAll() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM office_hours")) {

            System.out.println("Current office_hours table:");
            while (rs.next()) {
                System.out.println(
                        rs.getInt("id") + " | " +
                                rs.getString("semester") + " | " +
                                rs.getInt("year") + " | " +
                                rs.getString("day") + " | " +
                                rs.getString("time"));
            }

        } catch (SQLException e) {
            System.err.println("Error reading office_hours table: " + e.getMessage());
        }
    }

    public void insertCourse(String code, String name, String section) {
        String query = "INSERT INTO courses (code, name, section) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, code);
            stmt.setString(2, name);
            stmt.setString(3, section);
            stmt.executeUpdate();

            System.out.println("Course inserted: " + code + " - " + name + " (Section " + section + ")");
        } catch (SQLException e) {
            System.err.println("Course insert failed: " + e.getMessage());
        }
    }

    public boolean courseExists(String code, String name, String section) {
        String query = "SELECT COUNT(*) FROM courses WHERE code = ? AND name = ? AND section = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, code);
            stmt.setString(2, name);
            stmt.setString(3, section);

            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;

        } catch (SQLException e) {
            System.err.println("Course duplicate check failed: " + e.getMessage());
        }

        return false;
    }

    public List<Course> getAllCoursesSortedDescByCode() {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT code, name, section FROM courses ORDER BY code DESC";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String code = rs.getString("code");
                String name = rs.getString("name");
                String section = rs.getString("section");
                courses.add(new Course(code, name, section));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return courses;
    }

    public void insertTimeSlot(String fromHour, String toHour) {
        String insertSQL = "INSERT INTO time_slots (from_hour, to_hour) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(insertSQL)) {

            stmt.setString(1, fromHour);
            stmt.setString(2, toHour);
            stmt.executeUpdate();

            System.out.println(" Time slot inserted: " + fromHour + " - " + toHour);
        } catch (SQLException e) {
            System.err.println("Insert time slot failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<TimeSlot> getAllTimeSlotsSorted() {
        List<TimeSlot> slots = new ArrayList<>();
        String query = "SELECT from_hour, to_hour FROM time_slots ORDER BY " +
                "strftime('%H:%M', substr(from_hour, 1, instr(from_hour, ' ') - 1) || ':00 ' || substr(from_hour, -2)) ASC";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String from = rs.getString("from_hour");
                String to = rs.getString("to_hour");
                slots.add(new TimeSlot(from, to));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return slots;
    }
}

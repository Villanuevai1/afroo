package s25.cs151.application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class EntriesController {

    @FXML private TableView<OfficeHourEntry> entriesTable;
    @FXML private TableColumn<OfficeHourEntry, String> studentNameColumn;
    @FXML private TableColumn<OfficeHourEntry, String> dateColumn;
    @FXML private TableColumn<OfficeHourEntry, String> timeSlotColumn;
    @FXML private TableColumn<OfficeHourEntry, String> courseColumn;
    @FXML private TableColumn<OfficeHourEntry, String> reasonColumn;
    @FXML private TableColumn<OfficeHourEntry, String> commentColumn;

    private final ObservableList<OfficeHourEntry> entryList = FXCollections.observableArrayList();
    private static final String DB_URL = "jdbc:sqlite:data/identifier.sqlite";

    @FXML
    public void initialize() {
        studentNameColumn.setCellValueFactory(cell -> cell.getValue().studentNameProperty());
        dateColumn.setCellValueFactory(cell -> cell.getValue().dateProperty());
        timeSlotColumn.setCellValueFactory(cell -> cell.getValue().timeSlotProperty());
        courseColumn.setCellValueFactory(cell -> cell.getValue().courseProperty());
        reasonColumn.setCellValueFactory(cell -> cell.getValue().reasonProperty());
        commentColumn.setCellValueFactory(cell -> cell.getValue().commentProperty());

        loadEntries();
    }

    private void loadEntries() {
        String query = "SELECT * FROM office_hour_entries ORDER BY date DESC";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            entryList.clear();
            while (rs.next()) {
                entryList.add(new OfficeHourEntry(
                        rs.getString("student_name"),
                        rs.getString("date"),
                        rs.getString("time_slot"),
                        rs.getString("course"),
                        rs.getString("reason"),
                        rs.getString("comment")
                ));
            }
            entriesTable.setItems(entryList);

        } catch (SQLException e) {
            System.err.println("Failed to load office hour entries: " + e.getMessage());
        }
    }

    @FXML
    public void onAddEntryClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/s25/cs151/application/office_hour_schedule.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Enter Office Hour Schedule");
            stage.setScene(new Scene(root, 600, 500));

            // 👇 Reload entries when the window is closed
            stage.setOnHidden(e -> loadEntries());

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onBackToDashboardClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/s25/cs151/application/dashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1920, 1080));
            stage.setTitle("Dashboard");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


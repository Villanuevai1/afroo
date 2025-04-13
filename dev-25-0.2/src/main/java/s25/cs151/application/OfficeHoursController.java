package s25.cs151.application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.sql.*;

public class OfficeHoursController {

    @FXML private TableView<OfficeHour> officeHoursTable;
    @FXML private TableColumn<OfficeHour, String> semesterColumn;
    @FXML private TableColumn<OfficeHour, Integer> yearColumn;
    @FXML private TableColumn<OfficeHour, String> daysColumn;
    @FXML private TableColumn<OfficeHour, String> timeColumn;

    private final ObservableList<OfficeHour> officeHoursList = FXCollections.observableArrayList();

    //   points to local project folder's data directory
    private static final String DB_PATH = "data/identifier.sqlite";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;

    @FXML
    public void initialize() {
        semesterColumn.setCellValueFactory(cellData -> cellData.getValue().semesterProperty());
        yearColumn.setCellValueFactory(cellData -> cellData.getValue().yearProperty().asObject());
        daysColumn.setCellValueFactory(cellData -> cellData.getValue().dayProperty());
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeProperty());

        loadOfficeHours();
    }

    private void loadOfficeHours() {
        System.out.println(" Loading from DB: " + DB_PATH);

        String query = "SELECT * FROM office_hours " +
                "ORDER BY year DESC, " +
                "CASE semester " +
                "WHEN 'Spring' THEN 1 " +
                "WHEN 'Summer' THEN 2 " +
                "WHEN 'Fall' THEN 3 " +
                "WHEN 'Winter' THEN 4 " +
                "ELSE 5 END";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            officeHoursList.clear();

            if (!rs.isBeforeFirst()) {
                System.out.println(" No rows found in DB.");
            }

            while (rs.next()) {
                String semester = rs.getString("semester");
                int year = rs.getInt("year");
                String days = rs.getString("day");
                String time = rs.getString("time");

                System.out.println("Loaded: " + semester + ", " + year + ", " + days + ", " + time);

                OfficeHour officeHour = new OfficeHour(semester, year, days, time);
                officeHoursList.add(officeHour);
            }

            officeHoursTable.setItems(officeHoursList);

        } catch (SQLException e) {
            System.err.println(" Error loading from DB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void onAddOfficeHourClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/s25/cs151/application/add_office_hour.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Office Hour");
            stage.setScene(new Scene(root, 400, 400));
            stage.showAndWait();

            loadOfficeHours(); // reload after adding

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onRemoveOfficeHourClick(ActionEvent actionEvent) {
        OfficeHour selected = officeHoursTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String deleteSQL = "DELETE FROM office_hours WHERE semester = ? AND year = ? AND day = ? AND time = ?";

            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement stmt = conn.prepareStatement(deleteSQL)) {

                stmt.setString(1, selected.getSemester());
                stmt.setInt(2, selected.getYear());
                stmt.setString(3, selected.getDay());
                stmt.setString(4, selected.getTime());
                stmt.executeUpdate();

                officeHoursList.remove(selected);
                System.out.println("Removed: " + selected.getSemester() + ", " + selected.getYear());

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void onBackToDashboardClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/s25/cs151/application/dashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1920, 1080));
            stage.setTitle("Dashboard");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

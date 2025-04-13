package s25.cs151.application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.IntStream;

public class TimeSlotsController {

    @FXML private ComboBox<String> fromHourCombo;
    @FXML private ComboBox<String> fromMinuteCombo;
    @FXML private ComboBox<String> fromAmPmCombo;
    @FXML private ComboBox<String> toHourCombo;
    @FXML private ComboBox<String> toMinuteCombo;
    @FXML private ComboBox<String> toAmPmCombo;
    @FXML private TableView<TimeSlot> timeSlotsTable;
    @FXML private TableColumn<TimeSlot, String> fromColumn;
    @FXML private TableColumn<TimeSlot, String> toColumn;

    private final ObservableList<TimeSlot> timeSlotsList = FXCollections.observableArrayList();
    private final DatabaseHelper dbHelper = new DatabaseHelper();

    private static final String DB_PATH = "data/identifier.sqlite";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;

    @FXML
    public void initialize() {
        fromColumn.setCellValueFactory(cell -> cell.getValue().fromHourProperty());
        toColumn.setCellValueFactory(cell -> cell.getValue().toHourProperty());

        fromHourCombo.setItems(FXCollections.observableArrayList(IntStream.rangeClosed(1, 12).mapToObj(String::valueOf).toList()));
        toHourCombo.setItems(FXCollections.observableArrayList(IntStream.rangeClosed(1, 12).mapToObj(String::valueOf).toList()));

        ObservableList<String> minutes = FXCollections.observableArrayList("00", "15", "30", "45");
        fromMinuteCombo.setItems(minutes);
        toMinuteCombo.setItems(minutes);

        ObservableList<String> amPm = FXCollections.observableArrayList("AM", "PM");
        fromAmPmCombo.setItems(amPm);
        toAmPmCombo.setItems(amPm);

        loadTimeSlots();
    }

    @FXML
    public void onSaveAllClick() {
        String fromHour = fromHourCombo.getValue();
        String fromMinute = fromMinuteCombo.getValue();
        String fromAmPm = fromAmPmCombo.getValue();
        String toHour = toHourCombo.getValue();
        String toMinute = toMinuteCombo.getValue();
        String toAmPm = toAmPmCombo.getValue();

        if (fromHour == null || fromMinute == null || fromAmPm == null ||
                toHour == null || toMinute == null || toAmPm == null) {
            new Alert(Alert.AlertType.WARNING, "Please select all time components.").showAndWait();
            return;
        }

        String fromTime = fromHour + ":" + fromMinute + " " + fromAmPm;
        String toTime = toHour + ":" + toMinute + " " + toAmPm;

        if (!isValidTimeRange(fromTime, toTime)) {
            new Alert(Alert.AlertType.ERROR, "'To Time' must be after 'From Time'.").showAndWait();
            return;
        }

        dbHelper.insertTimeSlot(fromTime, toTime);
        loadTimeSlots();
    }

    private boolean isValidTimeRange(String fromTime, String toTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            Date from = sdf.parse(fromTime);
            Date to = sdf.parse(toTime);
            return to.after(from);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void loadTimeSlots() {
        timeSlotsList.setAll(dbHelper.getAllTimeSlotsSorted());
        timeSlotsTable.setItems(timeSlotsList);
    }

    @FXML
    public void onBackToDashboardClick(javafx.event.ActionEvent event) {
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
